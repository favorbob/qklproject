package cc.stbl.token.innerdisc.common.push.service;

import cc.stbl.token.innerdisc.common.push.inter.IPushService;
import cc.stbl.token.innerdisc.common.push.inter.model.ChannelPushResult;
import cc.stbl.token.innerdisc.common.push.inter.model.PushPayload;
import cc.stbl.token.innerdisc.common.push.inter.model.PushResult;
import cc.stbl.token.innerdisc.common.push.inter.model.application.Application;
import cc.stbl.token.innerdisc.common.push.inter.model.config.ApplicationConfig;
import cc.stbl.token.innerdisc.common.push.inter.model.paltform.DeviceType;
import cc.stbl.token.innerdisc.common.push.inter.model.paltform.Platform;
import cc.stbl.token.innerdisc.common.push.service.access.AbstractChannelConfig;
import cc.stbl.token.innerdisc.common.push.service.access.ApplicationAccessInfo;
import cc.stbl.token.innerdisc.common.push.service.access.ApplicationAccessInfoManager;
import cc.stbl.token.innerdisc.common.push.service.channel.AbstractPushClientWrapper;
import cc.stbl.token.innerdisc.common.push.service.channel.ISuperChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Developer on 2017-4-11.
 */
@Service
public class PushService implements IPushService {

    private static final Logger logger = LoggerFactory.getLogger(PushService.class);

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @Autowired
    private ISuperChannelFactory superChannelFactory;
    @Autowired
    private ApplicationAccessInfoManager applicationAccessInfoManager;

    @Value("${applications.MALL.channels.jpush.apnsProduction}")
    private Boolean apnsProduction;



    public PushResult push(PushPayload payload) {
        if(apnsProduction != null && apnsProduction){
            payload.resetOptionsApnsProduction(apnsProduction);
            logger.info("----------->根据配置将推送环境切换为生产环境");
        }else{
            if(apnsProduction == null){
                logger.warn("推送环境配置不生效");
            }else if(!apnsProduction){
                payload.resetOptionsApnsProduction(apnsProduction);
                logger.info("当前推送环境为非生产环境");
            }
        }

        final StopWatch sw = new StopWatch("推送时长监控:" + payload.getSendno());
        sw.start("初始化推送渠道");
        PushResult pushResult = new PushResult();
        pushResult.msgId = UUID.randomUUID().toString();
        pushResult.sendno = payload.getSendno();
        Application application = payload.getApplication();
        Platform platform = payload.getPlatform();
        Set<DeviceType> deviceTypes = platform == null ? null : platform.isAll() ? null : platform.getDeviceTypes();
        Collection<AbstractPushClientWrapper> clientWrappers = new HashSet<>();
        Collection<String> applications = null;
        if (application.isAll()) {
            applications = applicationAccessInfoManager.getApplications().keySet();
        } else {
            applications = application.getValues();
        }
        applications.parallelStream().forEach(s ->
                clientWrappers.addAll(superChannelFactory.createPushClientWrapper(s, deviceTypes)));

        logger.debug("共有{}个推送渠道:{}", clientWrappers.size(), clientWrappers);
        sw.stop();
        sw.start("渠道已经准备好，开始推送，有x个推送渠道:" + clientWrappers.size());
        Collection<Future<ChannelPushResult>> c = new HashSet<>();
        clientWrappers.forEach(clientWrapper ->
                c.add(executor.submit(() -> clientWrapper.doPush(payload)))
        );
        for (Future<ChannelPushResult> future : c) {
            ChannelPushResult cPushResult = null;
            try {
                cPushResult = future.get();
            } catch (InterruptedException e) {
                logger.warn(e.getMessage());
            } catch (ExecutionException e) {
                logger.error(e.getMessage(), e);
                pushResult.statusCode = PushResult.ERROR_CODE_NOT_ALL_DONE;
                break;
            }
            if (cPushResult != null) {
                pushResult.channelPushResults.add(cPushResult);
                if (cPushResult.statusCode != ChannelPushResult.ERROR_CODE_OK)
                    pushResult.statusCode = PushResult.ERROR_CODE_NOT_ALL_DONE;
            }
        }

        if(pushResult.statusCode == PushResult.ERROR_CODE_NONE)
            pushResult.statusCode = PushResult.ERROR_CODE_OK;
        sw.stop();
        logger.debug(sw.prettyPrint());
        return pushResult;
    }

    public ApplicationConfig getApplicationConfig(String application) {
        ApplicationAccessInfo applicationAccessInfo = applicationAccessInfoManager.getApplications().get(application);
        if (applicationAccessInfo == null)
            return null;
        ApplicationConfig ac = new ApplicationConfig();
        ac.setApplication(application);
        ac.setEnabled(applicationAccessInfo.getEnabled());

        Collection<AbstractChannelConfig> cfgs = superChannelFactory.createChannelConfig(application);
        if (cfgs != null)
            cfgs.forEach(abstractChannelConfig -> {
                ac.addChannels(abstractChannelConfig.getName(), abstractChannelConfig);
            });
        return ac;
    }

    public Collection<ApplicationConfig> getAllApplicationConfigs() {
        Set<String> strings = applicationAccessInfoManager.getApplications().keySet();
        Collection<ApplicationConfig> c = new HashSet<>();
        if (strings != null)
            strings.forEach(s -> c.add(this.getApplicationConfig(s)));
        return c;
    }
}

package cc.stbl.token.innerdisc.modules.message.service;

import cc.stbl.token.innerdisc.common.push.PushPayloadUtil;
import cc.stbl.token.innerdisc.common.push.inter.model.Message;
import cc.stbl.token.innerdisc.common.push.inter.model.PushPayload;
import cc.stbl.token.innerdisc.common.push.inter.model.PushResult;
import cc.stbl.token.innerdisc.common.push.inter.model.application.Application;
import cc.stbl.token.innerdisc.common.push.inter.model.audience.Audience;
import cc.stbl.token.innerdisc.common.push.inter.model.notification.AndroidNotification;
import cc.stbl.token.innerdisc.common.push.inter.model.notification.IosNotification;
import cc.stbl.token.innerdisc.common.push.inter.model.notification.Notification;
import cc.stbl.token.innerdisc.common.push.inter.model.paltform.Platform;
import cc.stbl.token.innerdisc.common.push.service.PushService;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessagePushService{

    public static final Logger logger = LoggerFactory.getLogger(MessagePushService.class);
    /**
     * 安卓、ios push 跳页面
     */

    @Autowired
    private PushService pushService;


    @Async
    public void buildPushObject_android_ios_alert(String title,String content,String phone, Map<String, String> extras){

        logger.info("极光推送标题、内容、extras==========>>>>>:[{};{};{}]",title,content, JSON.toJSON(extras));

        try {
            Notification build = Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder().setAlert(title).addExtras(extras).build())
                    .addPlatformNotification(AndroidNotification.newBuilder().setAlert(title).addExtras(extras).build())
                    .build();

            Message message = Message.newBuilder().setMsgContent(content).build();
            PushPayload payload = PushPayload.newBuilder()
                    .setApplication(Application.all())
                    .setPlatform(Platform.android())
                    .setAudience(Audience.alias(phone))
                    .setNotification(build)
                    .setMessage(message)
                    .build();
            PushResult push = pushService.push(payload);
//            SpringContextHolder.getBean(PushClient.class).push(payload);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("推送消息失败",e.getMessage());
        }
    }
}

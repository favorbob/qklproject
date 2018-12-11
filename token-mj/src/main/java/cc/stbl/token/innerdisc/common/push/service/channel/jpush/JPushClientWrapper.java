package cc.stbl.token.innerdisc.common.push.service.channel.jpush;

import cn.jiguang.common.DeviceType;
import cn.jiguang.common.resp.BaseResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.audience.AudienceType;
import cn.jpush.api.push.model.notification.*;
import cc.stbl.token.innerdisc.common.push.inter.model.ChannelPushResult;
import cc.stbl.token.innerdisc.common.push.inter.model.PushPayload;
import cc.stbl.token.innerdisc.common.push.service.channel.AbstractPushClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static cc.stbl.token.innerdisc.common.push.inter.model.PushResult.ERROR_CODE_ERROR;
import static cc.stbl.token.innerdisc.common.push.inter.model.PushResult.ERROR_CODE_OK;

/**
 * Created by Developer on 2017-4-11.
 */
public class JPushClientWrapper extends AbstractPushClientWrapper {

    private static final Logger logger = LoggerFactory.getLogger(JPushClientWrapper.class);

    private JPushClient client;

    JPushClientWrapper(String application, String channelKey, JPushClient client) {
        super(application, channelKey);
        this.client = client;
    }

    @Override
    protected ChannelPushResult push(PushPayload payload) {
        ChannelPushResult myPushResult = new ChannelPushResult();
        myPushResult.sendno = payload.getSendno();
        myPushResult.channel = super.channelKey;
        myPushResult.application = super.application;
        try {
            cn.jpush.api.push.model.PushPayload.Builder pushPayloadBuilder = cn.jpush.api.push.model.PushPayload.newBuilder();
            cc.stbl.token.innerdisc.common.push.inter.model.paltform.Platform myPlatform = payload.getPlatform();
            cc.stbl.token.innerdisc.common.push.inter.model.audience.Audience myAudience = payload.getAudience();
            cc.stbl.token.innerdisc.common.push.inter.model.Message myMessage = payload.getMessage();
            cc.stbl.token.innerdisc.common.push.inter.model.notification.Notification myNotification = payload.getNotification();
            cc.stbl.token.innerdisc.common.push.inter.model.Options myOptions = payload.getOptions();

            myPushResult.sendno = myOptions.getSendno();

            Platform platform = null;
            Audience audience = null;
            Notification notification = null;
            Message message = null;
            Options options = null;

            Constructor declaredConstructor = null;

            declaredConstructor = Platform.class.getDeclaredConstructor(boolean.class, Set.class);
            declaredConstructor.setAccessible(true);
            Set<cc.stbl.token.innerdisc.common.push.inter.model.paltform.DeviceType> myDeviceTypes = myPlatform.getDeviceTypes();
            final Set<DeviceType> deviceTypes = new HashSet<>();
            if (myDeviceTypes != null && !myDeviceTypes.isEmpty()) {
                Collection<DeviceType> c = this.mapDeviceType(myDeviceTypes);
                if (c != null)
                    deviceTypes.addAll(c);
            }
            platform = (Platform) declaredConstructor.newInstance(myPlatform.isAll(), deviceTypes.isEmpty() ? null : deviceTypes);


            if (myAudience != null) {
                Set<cc.stbl.token.innerdisc.common.push.inter.model.audience.AudienceTarget> myTargets = myAudience.getTargets();
                Set<AudienceTarget> targets = new HashSet<>();
                if (myTargets != null)
                    for (cc.stbl.token.innerdisc.common.push.inter.model.audience.AudienceTarget myTarget : myTargets) {
                        AudienceType audienceType = AudienceType.valueOf(myTarget.getAudienceType().name());
                        declaredConstructor = AudienceTarget.class.getDeclaredConstructor(AudienceType.class, Set.class);
                        declaredConstructor.setAccessible(true);
                        AudienceTarget audienceTarget = (AudienceTarget) declaredConstructor.newInstance(audienceType, myTarget.getValues());
                        targets.add(audienceTarget);
                    }

                declaredConstructor = Audience.class.getDeclaredConstructor(boolean.class, Set.class);
                declaredConstructor.setAccessible(true);
                audience = (Audience) declaredConstructor.newInstance(myAudience.isAll(), targets);
            }

            if (myNotification != null) {
                declaredConstructor = Notification.class.getDeclaredConstructor(Object.class, Set.class);
                Set<cc.stbl.token.innerdisc.common.push.inter.model.notification.PlatformNotification> myNotifications = myNotification.getNotifications();
                Set<PlatformNotification> notifications = new HashSet<>();
                if (myNotifications != null && !myNotifications.isEmpty()) {
                    for (cc.stbl.token.innerdisc.common.push.inter.model.notification.PlatformNotification platformNotification : myNotifications) {
                        PlatformNotification targetPlatformNotification = null;
                        if (platformNotification instanceof cc.stbl.token.innerdisc.common.push.inter.model.notification.WinphoneNotification) {
                            cc.stbl.token.innerdisc.common.push.inter.model.notification.WinphoneNotification myWinphoneNotification = (cc.stbl.token.innerdisc.common.push.inter.model.notification.WinphoneNotification) platformNotification;
                            targetPlatformNotification = WinphoneNotification.newBuilder()
                                    .setAlert(myWinphoneNotification.getAlert())
                                    .setOpenPage(myWinphoneNotification.getOpenPage())
                                    .setTitle(myWinphoneNotification.getTitle())
                                    .setTitle(myWinphoneNotification.getTitle())
                                    .addExtras(myWinphoneNotification.getExtras())
                                    .build();
                        }else if (platformNotification instanceof cc.stbl.token.innerdisc.common.push.inter.model.notification.AndroidNotification){
                            cc.stbl.token.innerdisc.common.push.inter.model.notification.AndroidNotification myAndroidNotification = (cc.stbl.token.innerdisc.common.push.inter.model.notification.AndroidNotification) platformNotification;
                            targetPlatformNotification = AndroidNotification.newBuilder()
                                    .setAlert(myAndroidNotification.getAlert())
                                    .setBuilderId(myAndroidNotification.getBuilderId())
                                    .setTitle(myAndroidNotification.getTitle())
                                    .addExtras(myAndroidNotification.getExtras())
                                    .build();
                        }else if (platformNotification instanceof cc.stbl.token.innerdisc.common.push.inter.model.notification.IosNotification){
                            cc.stbl.token.innerdisc.common.push.inter.model.notification.IosNotification myIosNotification = (cc.stbl.token.innerdisc.common.push.inter.model.notification.IosNotification) platformNotification;
                            IosNotification.Builder targetPlatformNotificationBuilder = IosNotification.newBuilder();
                            if(StringUtils.hasText(myIosNotification.getBadge())){
                                targetPlatformNotificationBuilder.setBadge(Integer.parseInt(myIosNotification.getBadge()));
                            }
                            targetPlatformNotificationBuilder.setCategory(myIosNotification.getCategory()).setSound(myIosNotification.getSound()).setAlert(myIosNotification.getAlert()).addExtras(myIosNotification.getExtras());
                            targetPlatformNotification = targetPlatformNotificationBuilder.build();
                        }
                        if (targetPlatformNotification != null) {
                            notifications.add(targetPlatformNotification);
                        }
                    }
                }
                declaredConstructor.setAccessible(true);
                notification = (Notification) declaredConstructor.newInstance(myNotification.getAlert(), notifications);
            }

            if (myMessage != null) {
                declaredConstructor = Message.class.getDeclaredConstructor(String.class, String.class, String.class, Map.class, Map.class, Map.class);
                declaredConstructor.setAccessible(true);
                message = (Message) declaredConstructor.newInstance(myMessage.getTitle(), myMessage.getMsgContent(), myMessage.getContentType(), myMessage.getExtras(), null, null);
            }

            if (myOptions != null) {
                declaredConstructor = Options.class.getDeclaredConstructor(int.class, long.class, long.class, boolean.class, int.class);
                declaredConstructor.setAccessible(true);
                options = (Options) declaredConstructor.newInstance(myOptions.getSendno(), myOptions.getOverrideMsgId(), myOptions.getTimeToLive(), myOptions.isApnsProduction(), myOptions.getBigPushDuration());
            }

            pushPayloadBuilder.setAudience(audience).setPlatform(platform).setMessage(message).setNotification(notification).setOptions(options);
            cn.jpush.api.push.model.PushPayload ppl = pushPayloadBuilder.build();
            ppl.resetOptionsApnsProduction(true);
            logger.info("{}->{}[{}]>>>>{}", this.application, this.channelKey, payload.getSendno(), ppl.toString());
            cn.jpush.api.push.PushResult pushResult = client.sendPush(ppl);
            logger.info("{}->{}[{}]<<<<{}", this.application, this.channelKey, payload.getSendno(), pushResult.toString());
            if (pushResult.isResultOK() && pushResult.statusCode == BaseResult.ERROR_CODE_OK) {
                myPushResult.statusCode = ERROR_CODE_OK;
                myPushResult.msgId = String.valueOf(pushResult.msg_id);
            } else {
                myPushResult.statusCode = ERROR_CODE_ERROR;
                myPushResult.statusMsg = pushResult.getOriginalContent();
                myPushResult.msgId = String.valueOf(pushResult.msg_id);
            }
        } catch (Exception e) {
            logger.error("{}->{}[{}]>><<{}", this.application, this.channelKey, payload.getSendno(), e.getMessage(), e);
            myPushResult.statusCode = ERROR_CODE_ERROR;
            myPushResult.statusMsg = e.getMessage();
        }
        return myPushResult;
    }

    private Collection<DeviceType> mapDeviceType(Set<cc.stbl.token.innerdisc.common.push.inter.model.paltform.DeviceType> myDeviceTypes) {
        Collection<DeviceType> deviceTypes = new HashSet<>();
        myDeviceTypes.forEach(deviceType -> {
            switch (deviceType) {
                case IOS:
                    deviceTypes.add(DeviceType.IOS);
                    break;
                case ANDROID:
                    deviceTypes.add(DeviceType.Android);
                    break;
                case WINPHONE:
                    deviceTypes.add(DeviceType.WinPhone);
                    break;
            }
        });
        return deviceTypes;
    }

}

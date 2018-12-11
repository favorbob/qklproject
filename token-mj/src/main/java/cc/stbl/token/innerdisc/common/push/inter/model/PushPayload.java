package cc.stbl.token.innerdisc.common.push.inter.model;

import cc.stbl.token.innerdisc.common.push.inter.model.application.Application;
import cc.stbl.token.innerdisc.common.push.inter.model.audience.Audience;
import cc.stbl.token.innerdisc.common.push.inter.model.notification.Notification;
import cc.stbl.token.innerdisc.common.push.inter.model.paltform.Platform;
import com.google.gson.Gson;

/**
 * The object you should build for sending a push.
 * 
 * Basically start with newBuilder() method to build a PushPayload object.
 * 
 * alertAll() is a shortcut for quickly build payload of alert to all platform and all audience;
 * mesageAll() is a shortcut for quickly build payload of message to all platform and all audience.
 * 
 */
public class PushPayload {
    private static final Gson gson = new Gson();
    private Application application;    //发送给哪个应用（暂时调用all()即可）
    private Platform platform;          //发送到哪个平台（安卓/IOS/H5）（目前调用all()即可）
    private Audience audience;          //接收群体（所有人、根据标签、别名等）
    private Notification notification;  //发送通知（通知栏会显示出来）
    private Message message;            //发送消息（通知栏不会显示出来，APP端可自定义接收到消息后进行展现或进行业务处理）
    private Token token;                //
    private Options options;            //推送环境（开发/生产）、自定义消息编码、设置保存时间等

    private PushPayload(){
        super();
    }

    private PushPayload(Application application, Platform platform, Audience audience,
            Notification notification, Message message,Token token, Options options) {
        this.application = application;
        this.platform = platform;
        this.audience = audience;
        this.notification = notification;
        this.message = message;
        this.token = token;
        this.options = options;
    }

    public Application getApplication() {
        return this.application;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Audience getAudience() {
        return audience;
    }

    public Notification getNotification() {
        return notification;
    }

    public Message getMessage() {
        return message;
    }

    public Options getOptions() {
        return options;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * The entrance for building a PushPayload object.
     * @return PushPayload builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }
    
    /**
     * The shortcut of building a simple alert notification object to all platforms and all audiences
     * @param alert The alert message.
     * @return PushPayload
     */
    public static PushPayload alertAll(String alert) {
        return new Builder()
                .setApplication(Application.all())
            .setPlatform(Platform.all())
            .setAudience(Audience.all())
            .setNotification(Notification.alert(alert)).build();
    }

    /**
     * The shortcut of building a simple message object to all platforms and all audiences
     * @param msgContent The message content.
     * @return PushPayload
     */
    public static PushPayload messageAll(String msgContent) {
        return new Builder()
            .setPlatform(Platform.all())
            .setAudience(Audience.all())
            .setMessage(Message.content(msgContent)).build();
    }

    public void resetOptionsApnsProduction(boolean apnsProduction) {
        if(null == this.options) {
            this.options = Options.newBuilder().setApnsProduction(apnsProduction).build();
        } else {
            this.options.setApnsProduction(apnsProduction);
        }

    }

    public void resetOptionsTimeToLive(long timeToLive) {
        if(null == this.options) {
            this.options = Options.newBuilder().setTimeToLive(timeToLive).build();
        } else {
            this.options.setTimeToLive(timeToLive);
        }

    }

    public int getSendno() {
        return null != this.options?this.options.getSendno():0;
    }


    public static class Builder {
        private Application application = null;
        private Platform platform = null;
        private Audience audience = null;
        private Notification notification = null;
        private Message message = null;
        private Options options = null;
        private Token token = null;

        public Builder setApplication(Application application) {
            this.application = application;
            return this;
        }

        public Builder setPlatform(Platform platform) {
            this.platform = platform;
            return this;
        }
        
        public Builder setAudience(Audience audience) {
            this.audience = audience;
            return this;
        }
        
        public Builder setNotification(Notification notification) {
            this.notification = notification;
            return this;
        }
        
        public Builder setMessage(Message message) {
            this.message = message;
            return this;
        }
        public Builder setToken(Token token) {
            this.token = token;
            return this;
        }

        public Builder setOptions(Options options){
            this.options = options;
            return this;
        }

        public PushPayload build() {
           if(null == audience)
               audience = Audience.all();
           if(null == platform)
               platform = Platform.all();
            if(null == token)
                token = Token.disuseToken();
            Preconditions.checkArgument(! (null == notification && null == message), "notification or message should be set at least one.");
            Preconditions.checkArgument(null != application, "application should be set.");
            if(null == this.options) {
                this.options = Options.sendno();
            }
            return new PushPayload(application, platform, audience, notification, message,token, this.options);
        }
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    public static void main(String[] args) {
//        String json = "{\"application\":{\"all\":true},\"platform\":{\"all\":true},\"audience\":{\"all\":false,\"targets\":[{\"audienceType\":\"ALIAS\",\"values\":[\"72e9078ef27f4e25d2d82c6b20724df9\",\"d8806da52225995b0fd9fb7d3bf4fc8b\",\"83b866a0e0063d8d790edbe706ffdb22\"]}]},\"notification\":{\"notifications\":[{\"soundDisabled\":false,\"badgeDisabled\":false,\"contentAvailable\":false,\"mutableContent\":false,\"alert\":\"模板二\",\"extras\":{\"OPERATE\":\"OPERATE\"}},{\"builderId\":0,\"alert\":\"模板二\",\"extras\":{\"OPERATE\":\"OPERATE\"}}]},\"message\":{\"msgContent\":\"notify\"},\"options\":{\"sendno\":1079857924,\"overrideMsgId\":0,\"timeToLive\":-1,\"apnsProduction\":false,\"bigPushDuration\":0}}";
        String json="";
        PushPayload pushPayload = gson.fromJson(json, PushPayload.class);
        System.err.println(pushPayload);
    }
}


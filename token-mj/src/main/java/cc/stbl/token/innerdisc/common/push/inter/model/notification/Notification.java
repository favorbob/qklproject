package cc.stbl.token.innerdisc.common.push.inter.model.notification;

import com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Notification {
    private Object alert;
    private Set<PlatformNotification> notifications;

    private Notification(){
        super();
    }

    private Notification(Object alert, Set<PlatformNotification> notifications) {
        this.alert = alert;
        this.notifications = notifications;
    }

    public Object getAlert() {
        return alert;
    }

    public Set<PlatformNotification> getNotifications() {
        return notifications;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    
    /**
     * Quick set all platform alert. 
     * Platform notification can override this alert. 
     * 
     * @param alert Notification alert
     * @return first level notification object
     */
    public static Notification alert(Object alert) {
        return newBuilder().setAlert(alert).build();
    }

    public static Notification android(String alert, String title, Map<String, String> extras) {
        return newBuilder()
                .addPlatformNotification(AndroidNotification.newBuilder()
                    .setAlert(alert)
                    .setTitle(title)
                    .addExtras(extras)
                    .build())
                .build();
    }

    public static Notification ios(Object alert, Map<String, String> extras) {
        return newBuilder()
                .addPlatformNotification(IosNotification.newBuilder()
                    .setAlert(alert)
                    .addExtras(extras)
                    .build())
                .build();
    }

    public static Notification ios_auto_badge() {
        return newBuilder()
                .addPlatformNotification(IosNotification.newBuilder()
                    .setAlert("")
                    .autoBadge()
                    .build())
                .build();
    }

    public static Notification wechat() {
        return new Notification("",null);
    }

    public static Notification ios_set_badge(int badge) {
        return newBuilder()
                .addPlatformNotification(IosNotification.newBuilder()
                    .setAlert("")
                    .setBadge(badge)
                    .build())
                .build();
    }

    public static Notification ios_incr_badge(int badge) {
        return newBuilder()
                .addPlatformNotification(IosNotification.newBuilder()
                    .setAlert("")
                    .incrBadge(badge)
                    .build())
                .build();
    }

    public static Notification winphone(String alert, Map<String, String> extras) {
        return newBuilder()
                .addPlatformNotification(WinphoneNotification.newBuilder()
                    .setAlert(alert)
                    .addExtras(extras)
                    .build())
                .build();
    }

    public static class Builder {
        private Object alert;
        private Set<PlatformNotification> builder;
        
        public Builder setAlert(Object alert) {
            this.alert = alert;
            return this;
        }
        
        public Builder addPlatformNotification(PlatformNotification notification) {
            if (null == builder) {
                builder = new HashSet<PlatformNotification>();
            }
            builder.add(notification);
            return this;
        }
        
        public Notification build() {
            Preconditions.checkArgument(! (null == builder && null == alert),
                    "No notification payload is set.");
            return new Notification(alert, builder);
        }
    }
}


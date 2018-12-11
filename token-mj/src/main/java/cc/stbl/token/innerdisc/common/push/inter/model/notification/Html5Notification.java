package cc.stbl.token.innerdisc.common.push.inter.model.notification;

import java.util.Map;

public class Html5Notification extends PlatformNotification {

    public static final String NOTIFICATION_HTML5 = "html5";

    private String title;

    private Html5Notification(){
        super();
    }

    private Html5Notification(Object alert, String title, Map<String, String> extras) {
        super(alert, extras);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public Object getAlert() {
        return super.getAlert();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Html5Notification alert(String alert) {
        return newBuilder().setAlert(alert).build();
    }


    @Override
    public String getPlatform() {
        return NOTIFICATION_HTML5;
    }

    public static class Builder extends PlatformNotification.Builder<Html5Notification, Builder> {
        private String title;

        protected Builder getThis() {
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setAlert(Object alert) {
            this.alert = alert;
            return this;
        }

        public Html5Notification build() {
            return new Html5Notification(alert, title, extrasBuilder);
        }
    }
}

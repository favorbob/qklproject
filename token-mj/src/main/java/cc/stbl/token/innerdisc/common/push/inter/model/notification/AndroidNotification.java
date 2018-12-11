package cc.stbl.token.innerdisc.common.push.inter.model.notification;

import java.util.Map;

public class AndroidNotification extends PlatformNotification {
    public static final String NOTIFICATION_ANDROID = "android";

    private static final String TITLE = "title";
    private static final String BUILDER_ID = "builder_id";

    private String title;
    private int builderId;

    private AndroidNotification(){
        super();
    }

    private AndroidNotification(Object alert, String title, int builderId,
                                Map<String, String> extras) {
        super(alert, extras);

        this.title = title;
        this.builderId = builderId;
    }

    public String getTitle() {
        return title;
    }

    public int getBuilderId() {
        return builderId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static AndroidNotification alert(String alert) {
        return newBuilder().setAlert(alert).build();
    }


    @Override
    public String getPlatform() {
        return NOTIFICATION_ANDROID;
    }

    public static class Builder extends PlatformNotification.Builder<AndroidNotification, Builder> {
        private String title;
        private int builderId;

        protected Builder getThis() {
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setBuilderId(int builderId) {
            this.builderId = builderId;
            return this;
        }

        public Builder setAlert(Object alert) {
            this.alert = alert;
            return this;
        }


        public AndroidNotification build() {
            return new AndroidNotification(alert, title, builderId, extrasBuilder);
        }
    }
}

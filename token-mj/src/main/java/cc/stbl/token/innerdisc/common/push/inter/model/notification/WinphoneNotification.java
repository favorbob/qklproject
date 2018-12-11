package cc.stbl.token.innerdisc.common.push.inter.model.notification;

import java.util.Map;

public class WinphoneNotification extends PlatformNotification {
    public static final String NOTIFICATION_WINPHONE = "winphone";
    
    private static final String TITLE = "title";
    private static final String _OPEN_PAGE = "_open_page";
    
    private String title;
    private String openPage;

    public String getTitle() {
        return title;
    }

    public String getOpenPage() {
        return openPage;
    }

    private WinphoneNotification(){
        super();
    }

    private WinphoneNotification(Object alert, String title, String openPage,
    		Map<String, String> extras) {
    	super(alert, extras);
        
        this.title = title;
        this.openPage = openPage;
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static WinphoneNotification alert(String alert) {
        return newBuilder().setAlert(alert).build();
    }
    
    
    @Override
    public String getPlatform() {
        return NOTIFICATION_WINPHONE;
    }
    
    public static class Builder extends PlatformNotification.Builder<WinphoneNotification, Builder> {
        private String title;
        private String openPage;
        
        protected Builder getThis() {
        	return this;
        }
        
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        
        public Builder setOpenPage(String openPage) {
            this.openPage = openPage;
            return this;
        }
        
        public Builder setAlert(Object alert) {
            this.alert = alert;
            return this;
        }

        
        public WinphoneNotification build() {
            return new WinphoneNotification(alert, title, openPage, 
            		extrasBuilder);
        }
    }
}

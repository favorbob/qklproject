package cc.stbl.token.innerdisc.common.push.inter.model.notification;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
public abstract class PlatformNotification {

    protected static final Logger LOG = LoggerFactory.getLogger(PlatformNotification.class);

    private Object alert;
    private Map<String, String> extras;

    public PlatformNotification(){

    }

    public PlatformNotification(Object alert, Map<String, String> extras) {
        this.alert = alert;
        this.extras = extras;
    }

    public Map<String, String> getExtras() {
        return extras;
    }

    public Object getAlert() {
        return this.alert;
    }
    
    protected void setAlert(Object alert) {
        this.alert = alert;
    }

    public abstract String getPlatform();
    
    protected abstract static class Builder<T extends PlatformNotification, B extends Builder<T, B>> {
    	private B theBuilder;
    	
        protected Object alert;
        protected Map<String, String> extrasBuilder;

        public Builder () {
        	theBuilder = getThis();
        }
        
        protected abstract B getThis();
        
        public abstract B setAlert(Object alert);
                
        public B addExtra(String key, String value) {
            Preconditions.checkArgument(! (null == key), "Key should not be null.");
            if (null == value) {
                LOG.debug("Extra value is null, throw away it.");
                return theBuilder;
            }
            if (null == extrasBuilder) {
                extrasBuilder = new HashMap<String, String>();
            }
            extrasBuilder.put(key, value);
            return theBuilder;
        }

        public B addExtras(Map<String, String> extras) {
            if (null == extras) {
                LOG.warn("Null extras param. Throw away it.");
                return theBuilder;
            }
            
            if (null == extrasBuilder) {
                extrasBuilder = new HashMap<String, String>();
            }
            for (String key : extras.keySet()) {
                extrasBuilder.put(key, extras.get(key));
            }
            return theBuilder;
        }
        
        public abstract T build();
    }

    
}

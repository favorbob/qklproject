package cc.stbl.token.innerdisc.common.push.inter.model;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private static final String TITLE = "title";
    private static final String MSG_CONTENT = "msg_content";
    private static final String CONTENT_TYPE = "content_type";
    private static final String EXTRAS = "extras";
    
    private String title;
    private String msgContent;
    private String contentType;
    private Map<String, String> extras;

    private Message(){

    }

    private Message(String title, String msgContent, String contentType, 
    		Map<String, String> extras) {
        this.title = title;
        this.msgContent = msgContent;
        this.contentType = contentType;
        this.extras = extras;
    }

    public String getTitle() {
        return title;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getExtras() {
        return extras;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static Message content(String msgContent) {
        return new Builder().setMsgContent(msgContent).build();
    }

    public static class Builder {
        private String title;
        private String msgContent;
        private String contentType;
        private Map<String, String> extrasBuilder;
        
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        
        public Builder setMsgContent(String msgContent) {
            this.msgContent = msgContent;
            return this;
        }
        
        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }
        
        public Builder addExtra(String key, String value) {
            Preconditions.checkArgument(! (null == key || null == value), "Key/Value should not be null.");
            if (null == extrasBuilder) {
                extrasBuilder = new HashMap<String, String>();
            }
            extrasBuilder.put(key, value);
            return this;
        }
        
        public Builder addExtras(Map<String, String> extras) {
            Preconditions.checkArgument(! (null == extras), "extras should not be null.");
            for (String key : extras.keySet()) {
                addExtra(key, extras.get(key));
            }
            return this;
        }

        public Message build() {
            Preconditions.checkArgument(! (null == msgContent), 
                    "msgContent should be set");
            return new Message(title, msgContent, contentType, 
            		extrasBuilder);
        }
    }
}

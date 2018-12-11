//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cc.stbl.token.innerdisc.common.push.inter.model;

import com.google.gson.Gson;

public class ChannelPushResult {
    private static final Gson gson = new Gson();
    private static final long serialVersionUID = 93783137655776743L;

    public static final int ERROR_CODE_NONE = -1;
    public static final int ERROR_CODE_OK = 1;
    public static final int ERROR_CODE_ERROR = 0;
    public static final String ERROR_MESSAGE_NONE = "None error message.";

    public int sendno;

    public int statusCode;

    public String application;

    public String channel;

    public String statusMsg = ERROR_MESSAGE_NONE;

    public String msgId;

    public ChannelPushResult() {
        this.statusCode = ERROR_CODE_NONE;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}

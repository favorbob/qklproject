//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cc.stbl.token.innerdisc.common.push.inter.model;

import com.google.gson.Gson;

import java.util.Collection;
import java.util.HashSet;

public class PushResult {
    private static final Gson gson = new Gson();
    private static final long serialVersionUID = 93783137655776743L;

    /**初始状态*/
    public static final int ERROR_CODE_NONE = -1;
    /**成功送达各渠道端*/
    public static final int ERROR_CODE_OK = 1;
    /**部分未送达渠道端，查询 channelPushResults 进一步确认。部分未送达渠道可能未包含在内*/
    public static final int ERROR_CODE_NOT_ALL_DONE = 10;
    /**调用服务失败，消息送到达渠道端*/
    public static final int ERROR_CODE_ERROR = 0;
    public static final String ERROR_MESSAGE_NONE = "None error message.";

    public int sendno;

    public int statusCode;

    public final Collection<ChannelPushResult> channelPushResults = new HashSet<>();

    public String statusMsg = ERROR_MESSAGE_NONE;

    public String msgId;

    public PushResult() {
        this.statusCode = ERROR_CODE_NONE;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}

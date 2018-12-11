package cc.stbl.token.innerdisc.restapi.app.trades;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PayedAuthProto {

    @Data
    public static class UploadPayedRequest{
        @NotNull(message = "交易记录id不能为空")
        private String recordId;
        @NotNull.List(@NotNull(message = "凭证URL不能为空"))
        private List<String> authUrls;
    }

    @Data
    public static class TradePayedAuthRequest {
        @NotNull(message = "交易记录id不能为空")
        private String recordId;
    }

    @Data
    public static class TradePayedAuthResponse{
        private List<String> authUrls = new ArrayList<>();
        public void addAuthUrl(String url){
            this.authUrls.add(url);
        }
    }
}

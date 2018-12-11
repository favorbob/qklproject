package cc.stbl.token.innerdisc.restapi.app.resources;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
public class UserUsedTimeProto {

    @Data
    public static class RequestCountUsedTimeStart {

        @NotBlank
        @ApiModelProperty("应用 ID")
        private String resourceId;

        @NotBlank
        @ApiModelProperty("设备 ID")
        private String deviceId;

        @NotNull
        @ApiModelProperty("应用类型  1游戏/2电影")
        private Integer resourceType;

        /*@NotBlank
        @ApiModelProperty("开始标识 传 1")
        private Integer start;*/
    }

    @Data
    public static class ResponseCountUsedTimeStart {

        @ApiModelProperty("请求开始后返回的 id，每次请求都要传")
        private String id;

    }

    @Data
    public static class RequestContinuousCall {

        @NotBlank
        @ApiModelProperty("请求开始后返回的 id，每次请求都要传")
        private String id;

        @NotBlank
        @ApiModelProperty("结束标识 未结束 0 / 结束 1")
        private String end;

        public static boolean isEnd(String endCheck){
            if ("1".equals(endCheck)){
                return true;
            }
            return false;
        }
    }
}

package cc.stbl.token.innerdisc.restapi.admin.sys;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class AppVersionProto {


    @Data
    public static class QueryCondition extends BaseProto.RquestPager {
        @ApiModelProperty("客户端类型")
        private String clientType;		// 客户端类型
        @ApiModelProperty("版本号")
        private Integer versionCode;		// 版本号
        @ApiModelProperty("版本名")
        private String versionName;		// 版本名
        @ApiModelProperty("最小版本号")
        private Integer minVersionCode;		// 最小版本号
    }

    @Data
    public static class Detele {
        private String id;
    }

    @Data
    public static class RequestSave {
        @NotNull
        private String clientType;		// 客户端类型
        private Integer versionCode;		// 版本号
        @NotNull
        private String versionName;		// 版本名
        @NotNull
        private Integer minVersionCode;		// 最小版本号
        @NotNull
        private String url;		// app地址
        private String updateInfo;		// 更新描述
        private String memo;		// 备注
        private Long size;		// 文件大小
        private String id;
    }
}

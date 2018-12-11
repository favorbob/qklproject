package cc.stbl.token.innerdisc.restapi.admin.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public class UploadFileProto {

    @Data
    public static class ResponseUpload {
        @ApiModelProperty("文件名")
        private String name;
        @ApiModelProperty("文件相对路径")
        private String path;
        @ApiModelProperty("文件完整路径")
        private String url;
        @ApiModelProperty("文件大小")
        private Long fileSize;
    }
}

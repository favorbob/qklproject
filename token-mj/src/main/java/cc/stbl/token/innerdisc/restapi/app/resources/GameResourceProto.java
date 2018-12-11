package cc.stbl.token.innerdisc.restapi.app.resources;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel
public class GameResourceProto {

    @Data
    public static class RequestGameList extends BaseProto.RquestPager{

    }

    @Data
    public static class ResponseGameDetail {

        private String id;

        @ApiModelProperty("资源名称")
        private String resourceName;

        @ApiModelProperty("资源类型: 1游戏/2电影")
        private Integer resourceType;

        @ApiModelProperty("资logo")
        private String resourceLogo;

        @ApiModelProperty("资源简介")
        private String resourceDescribe;

        @ApiModelProperty("上传时间")
        private Date uploadTime;

        @ApiModelProperty("资源状态: 0未上架/1已上架/2已下架")
        private Integer resourceState;

        private String userId;

        @ApiModelProperty("零钱购买挖矿比例")
        private Float smallChangeMiningRatio;

        @ApiModelProperty("价格 资产")
        private BigDecimal price;
    }
}

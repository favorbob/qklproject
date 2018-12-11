package cc.stbl.token.innerdisc.restapi.admin.resources;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
public class ResourceThirdPartyProto {

    @Data
    public static class RequestThirdPartySettings {

        @ApiModelProperty("修改时需要传此settingsId")
        private String settingsId;

        @ApiModelProperty("观看免费时长 秒")
        private Integer freeUseTime;

        @ApiModelProperty("资源类型  1游戏/2电影")
        private Integer resourceType;

        /*@ApiModelProperty("购买方式: 1：零钱，2：资产")
        private Integer buyingPattern;*/

        @ApiModelProperty("类别: 1：解锁前(游戏)，2：解锁后(游戏)，0：默认(电影用)")
        private Integer buyingCategory;

        @ApiModelProperty("优先级 默认为 0")
        private Integer priority;

        @ApiModelProperty("价格:零钱购买方式")
        private BigDecimal priceChange;
        @ApiModelProperty("价格:资产购买方式")
        private BigDecimal priceAsset;
    }

    @Data
    public static class ResponseGetThirdPartySettings {

        private String settingsId;

        @ApiModelProperty("观看免费时长 秒")
        private Integer freeUseTime;

        @ApiModelProperty("资源类型  1游戏/2电影")
        private Integer resourceType;

        @ApiModelProperty("类别: 1：解锁前(游戏)，2：解锁后(游戏)，0：默认(电影用)")
        private Integer buyingCategory;

        @ApiModelProperty("优先级 默认为 0")
        private Integer priority;

        @ApiModelProperty("价格:零钱购买方式")
        private BigDecimal priceChange;
        @ApiModelProperty("价格:资产购买方式")
        private BigDecimal priceAsset;
    }

    @Data
    public static class RequestGetThirdPartySettings {

        @NotNull(message = "资源类型不能为空")
        @ApiModelProperty("资源类型  1游戏/2电影")
        private Integer resourceType;
    }
}

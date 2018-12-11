package cc.stbl.token.innerdisc.restapi.admin.base;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
public class VrUserInfoFlowProto {

    @Data
    public static class RequestQuery extends BaseProto.RquestPager {
        @ApiModelProperty("用户编号")
        private String phoneNum;
    }
    
    @Data
    public static class RequestUpdate  {
        @ApiModelProperty("修改后的值")
        private String value;
        @ApiModelProperty("修改类型")
        private Integer type;
        @ApiModelProperty("电话号码")
        private String phoneNum;
        @ApiModelProperty("用户的userId")
        private String userId;
        
    }
    
    @Data
    public static class RequestAssetDetailList  extends BaseProto.RquestPager{
        @ApiModelProperty("电话号码")
        private String phoneNum;
        @ApiModelProperty("类型")
        private Integer assetType;
    	private Date beginTime;
    	private Date endTime;
    }
}

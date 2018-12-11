package cc.stbl.token.innerdisc.restapi.admin.user;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel
public class VrUserProto {

    @Data
    public static class ListVrUserInfoRequest{
        private String phoneNum;
        @NotNull(message = "pageNo不能为空")
        protected Integer pageNo;
        @NotNull(message = "pageSize不能为空")
        protected Integer pageSize;
        @ApiModelProperty(value = "用户状态，1：正常，2：冻结，-1：删除")
        private Integer status;
        @ApiModelProperty(value = "用户区等级")
        private String userAreaLevel;
        private String beginTime;
        private String endTime;
    }
    
    @Data
    public static class GetUserAttribute{
        
    	@NotBlank(message = "请输入用户id")
        private String userId;
    }
    
    @Data
    public static class UserAttribute{
        
    	private String id;
    	private String address;
    	private String bankName;
    	private String bankCardNum;
    	private String bankAddress;
    	private String wechatPhoneNum;
    	private String aliplay;
    	private String userId;
    	private String newPassword;
    	private String userName;
    	private String payPassword;
    	private String remark;
    	
    }
    
    
    @Data
    public static class CreateUserRequest {
        @NotBlank(message = "请输入正确的手机号码")
        private String phoneNum;
        @NotBlank(message = "请输入密码")
        private String password;
        @ApiModelProperty("用户等级,1:系统用户，2:团队领导，9:普通用户")
        @NotNull(message = "请选择用户等级")
        private Integer userLevel;
        @ApiModelProperty("推荐码")
        private String parentInviteCode;
        @NotBlank(message = "请输入姓名")
        private String userName;
        @NotBlank(message = "请输入正确的身份证号码")
        private String idCard;
    }

    @Data
    public static class UserFreeze {
        @NotBlank(message = "请输入UserId")
        private String userId;
        @NotBlank(message = "请输入冻结原因")
        private String freeze;
    }
    
    @Data
    public static class UserId {
        @NotBlank(message = "请输入UserId")
        private String userId;
    }
    
    
    @Data
    public static class UserUnfreeze {
        @NotBlank(message = "请输入UserId")
        private String userId;
        @NotBlank(message = "请输入解冻原因")
        private String unfreeze;
    }
    
    @Data
    public static class UserRemark {
        @NotBlank(message = "请输入UserId")
        private String userId;
        @NotBlank(message = "请输入备注")
        private String remark;
    }
    

    @Data
    public static class RequestModifyPassword {
        @NotBlank(message = "请输入旧密码")
        private String oldPassword;
        @NotBlank(message = "请输入新密码")
        private String newPassword;
    }

    @Data
    public static class ResponseUserDownlineDetail{
        @ApiModelProperty(value = "用户Id")
        private String userId; //用户ID
        @ApiModelProperty(value = "电话")
        private String phoneNum;//电话
        @ApiModelProperty(value = "用户等级,1:系统，2:团队领导,9:普通用户")
        private Integer userLevel;//用户等级,1:系统，2:团队领导,9:普通用户
        @ApiModelProperty(value = "用户姓名")
        private String userName;//用户姓名
        @ApiModelProperty(value = "用户头像")
        private String userIcon;// 用户头像
        @ApiModelProperty(value = "邀请码")
        private String inviteCode;//邀请码
        @ApiModelProperty(value = "创建时间")
        private Date createDate;//创建时间
        @ApiModelProperty(value = "注册时间")
        private Date registerDate;//注册时间
        @ApiModelProperty(value = "身份证号码")
        private String idCard;//身份证号码
        @ApiModelProperty(value = "我的余额")
        private BigDecimal myBalance;//我的余额
        @ApiModelProperty(value = "可用资产")
        private BigDecimal availableAssets;//可用资产
        @ApiModelProperty(value = "受限资产")
        private BigDecimal restrictedAssets;//受限资产
        @ApiModelProperty(value = "我的积分")
        private BigDecimal myPoints;//我的积分
        @ApiModelProperty(value = "区块链钱包地址")
        private String blockChainAddress;//区块链钱包地址
        @ApiModelProperty(value = "我的上级")
        private String parentUserId;//我的上级
        @ApiModelProperty(value = "一级")
        private Integer level1;//一级
        @ApiModelProperty(value = "二级")
        private Integer level2;//二级
        @ApiModelProperty(value = "无限级")
        private Integer infiniteLevel;//无限级

    }

    @Data
    public static class RequestUser {
        @NotBlank(message = "请输入UserId")
        private String userId;
        Integer pageNo = 1;
        Integer pageSize = 20;
    }

    @Data
    public static class ResponseUserInfo{
        @ApiModelProperty(value = "用户Id")
        private String userId;
        @ApiModelProperty(value = "手机号码")
        private String phoneNum;
        @ApiModelProperty(value = "用户等级,1:系统，2:团队领导,9:普通用户")
        private Integer userLevel;
        @ApiModelProperty(value = "用户姓名")
        private String userName;
        @ApiModelProperty(value = "用户头像")
        private String userIcon;
        @ApiModelProperty(value = "用户状态(1:正常,2:冻结,-1:删除)")
        private Integer status;
        @ApiModelProperty(value = "邀请码")
        private String inviteCode;
        @ApiModelProperty(value = "创建时间")
        private Date createDate;
        @ApiModelProperty(value = "更新时间")
        private Date updateDate;
        @ApiModelProperty(value = "身份证号码")
        private String idCard;

        //by view
        @ApiModelProperty(value = "总积分")
        private String integral;
        @ApiModelProperty(value = "总资产")
        private String totalBalance;
        @ApiModelProperty(value = "我的上级信息")
        private VrUserInfo parent;
        @ApiModelProperty(value = "我的电话")
        private String parentPhoneNum;
        @ApiModelProperty(value = "我的上级")
        private String parentUserId;
        @ApiModelProperty(value = "可用资产")
        private String availableAssets;
        @ApiModelProperty(value = "受限资产")
        private String limitAssets;
        @ApiModelProperty(value = "冻结资产")
        private String frozenAssets;

        private String parentUserName;
        @ApiModelProperty("会有数")
        private Integer vipUserCount;
        
      //登录密码
        private String password;
        //支付密码
        private String payPassword;
        //登录密码 明文
        private String newPassword;
        
        //设备号
        private String deviceId;
        
        //用户序号，用于顶层用户code 默认都是1
        private Integer seq=1;
        
        //用户所在层级
        private Integer userCodeLevel;
        
        //用户层级关系code
        private String userCode;
        
        //推荐人
        private String registerPhoneNum;
        //接点人
        private String nodePhoneNum;
        //A区值
        private Long aArea;
        //B区值
        private Long bArea;
        //用户所属区
        private String area;
        //用户级别
        private String userAreaLevel;
        private String remark;//备注
        private String freeze;//冻结原因
        private String unfreeze;//解冻原因
        
    }

    //根据姓名模糊搜索，传递姓名
    @Data
    public static class UserNameQuery  extends BaseProto.RquestPager {
        @ApiModelProperty(value = "用户姓名")
        private String userName;
    }

}

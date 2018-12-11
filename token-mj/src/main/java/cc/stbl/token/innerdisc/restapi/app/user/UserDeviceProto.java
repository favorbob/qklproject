package cc.stbl.token.innerdisc.restapi.app.user;

import cc.stbl.token.innerdisc.modules.basic.dto.DeviceUseNumDTO;
import cc.stbl.token.innerdisc.modules.basic.dto.DeviceUseTimeDTO;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserDevice;
import cc.stbl.token.innerdisc.restapi.BaseProto;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@ApiModel
public class UserDeviceProto {


    /*------------------------------------------------*/

    /**
     * 新增设备请求参数
     */
    @Data
    public static class addDeviceReq{
        @NotBlank(message = "设备编码（标识）不能为空")
        private String code;
        @NotBlank(message = "设备名称不能为空")
        private String name;
    }

    @Data
    public static class delDeviceReq{
        @ApiModelProperty("设备ID")
        private String deviceId;
    }

    @Data
    public static class getUserDeviceListReq extends BaseProto.RquestPager{

    }

    /**
     * 设备使用时长记录请求参数
     */
    @Data
    public static class DeviceUseTimeRecordReq extends BaseProto.RquestPager{
        @ApiModelProperty("设备ID")
        private String deviceId;
        @ApiModelProperty("发生月份")
        private Date month;
        @ApiModelProperty("记录类型： 1、游戏，2、电影")
        private Integer resourceType;
    }

    /**
     * 设备使用次数记录请求参数
     */
    @Data
    public static class DeviceUseNumRecordReq extends BaseProto.RquestPager{
        @ApiModelProperty("设备ID")
        private String deviceId;
        @ApiModelProperty("发生月份")
        private Date month;
        @ApiModelProperty("记录类型： 1、游戏，2、电影")
        private Integer resourceType;
    }


    /*------------------------------------------------*/

    /**
     * 获取用户设备列表响应
     */
    /*@Data
    public static class UserDeviceListResp{
        private List<VrUserDevice> deviceList;
    }*/


    /**
     * 设备使用时长记录响应
     */
    @Data
    public static class DeviceUseTimeRecordResp{
        @ApiModelProperty("总时长（秒数）")
        private Long countTimes;
        @ApiModelProperty("电影时长（秒数）")
        private Long movieTimes;
        @ApiModelProperty("游戏时长（秒数）")
        private Long gameTimes;
        @ApiModelProperty("使用时长记录")
        private Pager<DeviceUseTimeDTO> pager;
    }

    /**
     * 设备使用次数记录响应
     */
    @Data
    public static class DeviceUseNumRecordResp{
        @ApiModelProperty("总次数")
        private Integer countNum;
        @ApiModelProperty("电影次数")
        private Integer movieNum;
        @ApiModelProperty("游戏次数")
        private Integer gameNum;
        @ApiModelProperty("使用次数记录")
        private Pager<DeviceUseNumDTO> pager;

    }

}

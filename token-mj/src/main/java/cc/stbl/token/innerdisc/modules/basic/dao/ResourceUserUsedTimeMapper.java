/**
* generator by mybatis plugin Wed Aug 22 18:10:59 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.dto.DeviceUseNumDTO;
import cc.stbl.token.innerdisc.modules.basic.dto.DeviceUseTimeDTO;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceUserUsedTime;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

public interface ResourceUserUsedTimeMapper extends IDataStoreMapper<String, ResourceUserUsedTime> {


    /**
     * 获取设备使用时长记录
     * @param deviceId 设备ID
     * @param month 月份
     * @param resourceType 资源类型
     * @return
     */
    List<DeviceUseTimeDTO> getDeviceUseTimeRecord(@Param("deviceId") String deviceId, @Param("month") Date month, @Param("resourceType") Integer resourceType, RowBounds rowBounds);

    /**
     * 获取设备使用时长记录合计数
     * @param deviceId 设备ID
     * @param month 月份
     * @param resourceType 资源类型
     * @return
     */
    Long getDeviceUseTimeRecordCount(@Param("deviceId") String deviceId, @Param("month") Date month, @Param("resourceType") Integer resourceType);


    /**
     * 获取设备使用次数记录
     * @param deviceId 设备ID
     * @param month 月份
     * @param resourceType 资源类型
     * @return
     */
    List<DeviceUseNumDTO> getDeviceUseNumRecord(@Param("deviceId") String deviceId,@Param("month") Date month,@Param("resourceType") Integer resourceType,RowBounds rowBounds);

    /**
     * 获取设备使用次数记录合计数
     * @param deviceId 设备ID
     * @param month 月份
     * @param resourceType 资源类型
     * @return
     */
    Long getDeviceUseNumRecordCount(@Param("deviceId") String deviceId, @Param("month") Date month, @Param("resourceType") Integer resourceType);


    /**
     * 获取设备累计使用时长数
     * @param deviceId 设备ID
     * @param resourceType 资源类型
     * @return
     */
    Long getDeviceUseTimes(@Param("deviceId") String deviceId,@Param("resourceType") Integer resourceType);

    /**
     * 获取设备累计使用次数
     * @param deviceId 设备ID
     * @param resourceType 资源类型
     * @return
     */
    Integer getDeviceUseNum(@Param("deviceId") String deviceId,@Param("resourceType") Integer resourceType);

}
/**
* generator by mybatis plugin Thu Aug 23 14:57:29 CST 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserDevice;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VrUserDeviceMapper extends IDataStoreMapper<String, VrUserDevice> {

    /**
     * 获取用设备列表
     * @param userId 用户ID
     * @return
     */
    List<VrUserDevice> getByUserId(@Param("userId") String userId);

    /**
     * 删除设备(软删除)
     * @param deviceId 设备ID
     * @return
     */
    Integer delDevice(@Param("deviceId") String deviceId);

}
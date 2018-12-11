/**
* generator by mybatis plugin Tue Aug 28 11:33:27 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserImg;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VrUserImgMapper extends IDataStoreMapper<String, VrUserImg> {

    Integer expireStatusByUserId(@Param("userId") String userId, @Param("imgType")Integer imgType);

    List<VrUserImg> selectByUserId(@Param("userId") String userId, @Param("status")Integer status, @Param("imgType")Integer imgType);

    List<VrUserImg> selectReceiptCodeByUserId(@Param("userId") String userId);
}
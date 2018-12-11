/**
* caojinbo
**/
package cc.stbl.token.innerdisc.modules.centerManager.dao;

import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterEditServicePic;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CenterEditServicePicMapper extends IDataStoreMapper<String, CenterEditServicePic> {
    CenterEditServicePic selectByEditId(@Param("editServiceId") String editServiceId);
    Long findCountByServiceId(CenterEditServicePic record); //根据客服标题id，查询客服总数
    List<CenterEditServicePic> findListByServiceId(CenterEditServicePic query, RowBounds rowBounds); //根据客服标题id，查询客服list集合-分页
}
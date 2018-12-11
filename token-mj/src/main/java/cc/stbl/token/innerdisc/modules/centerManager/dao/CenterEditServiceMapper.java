/**
* caojinbo
**/
package cc.stbl.token.innerdisc.modules.centerManager.dao;

import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterEditService;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CenterEditServiceMapper extends IDataStoreMapper<String, CenterEditService> {
    List<CenterEditService> selectEditServiceAndPicsResult();   //返回标题+客服图片集合
}
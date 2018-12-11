/**
* caojinbo
**/
package cc.stbl.token.innerdisc.modules.centerManager.dao;

import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterHomePic;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CenterHomePicMapper extends IDataStoreMapper<String, CenterHomePic> {

    int batchUpdateSortByIds(List<CenterHomePic> record);  //根据ids，更新pic_sort，更新排序
}
/**
* generator by mybatis plugin Mon Aug 20 18:00:12 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.dto.ResourceInfoDTO;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceInfo;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ResourceInfoMapper extends IDataStoreMapper<String, ResourceInfo> {

    List<ResourceInfo> findPageList(ResourceInfoDTO dto, RowBounds rowBounds);

    Long findPageListCount(ResourceInfoDTO dto);
}
/**
* caojinbo
**/
package cc.stbl.token.innerdisc.modules.sys.dao;

import cc.stbl.token.innerdisc.modules.sys.entity.SysLog;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface SysLogMapper extends IDataStoreMapper<String, SysLog> {
    Long findSysLogCount(SysLog query); //查询总数
    List<SysLog> findSysLogList(SysLog query, RowBounds rowBounds);
}
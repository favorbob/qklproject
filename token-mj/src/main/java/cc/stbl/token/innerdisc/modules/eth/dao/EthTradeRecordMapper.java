/**
* generator by mybatis plugin Tue Jul 10 16:13:33 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.dao;

import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface EthTradeRecordMapper extends IDataStoreMapper<String, EthTradeRecord> {
    List<EthTradeRecord> unionAllUserID(EthTradeRecord ethTradeRecord);

    List<EthTradeRecord> getList(EthTradeRecord query, RowBounds rowBounds);

    Long getCount(EthTradeRecord query);

    Long findSumTradeAmount(EthTradeRecord query);

}
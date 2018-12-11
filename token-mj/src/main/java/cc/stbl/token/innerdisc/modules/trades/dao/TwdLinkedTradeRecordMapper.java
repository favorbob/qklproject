/**
* generator by mybatis plugin Wed Aug 22 11:16:49 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.trades.dao;

import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeRecord;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeStatDay;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface TwdLinkedTradeRecordMapper extends IDataStoreMapper<String, TwdLinkedTradeRecord> {

    TwdLinkedTradeStatDay countTwdLinkedTradeStatDay();

    Long findUserBuyAssetCount(TwdLinkedTradeRecord query);

    List<TwdLinkedTradeRecord> findUserBuyAssetList(TwdLinkedTradeRecord query, RowBounds rowBounds);

    List<TwdLinkedTradeRecord> findUserLinkedList(TwdLinkedTradeRecord linkedTrade, RowBounds rowBounds);
}
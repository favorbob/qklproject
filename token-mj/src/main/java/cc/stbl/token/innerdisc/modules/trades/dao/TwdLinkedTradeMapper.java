/**
* generator by mybatis plugin Wed Aug 22 11:20:15 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.trades.dao;

import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTrade;
import cc.stbl.token.innerdisc.restapi.app.trades.LinkedTradeProto;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface TwdLinkedTradeMapper extends IDataStoreMapper<String, TwdLinkedTrade> {
    List<TwdLinkedTrade> findUserLinkedList(TwdLinkedTrade linkedTrade, RowBounds rowBounds);
    HashMap getSummary(@Param("from_flow_type")Integer balanceType);
    BigDecimal getTodayAveragePrice(@Param("from_flow_type")Integer balanceType);
}
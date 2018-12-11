/**
* generator by mybatis plugin Fri Jul 13 17:29:06 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.dao;

import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;
import com.ks.common.datastore.dao.IDataStoreMapper;

import java.util.List;

public interface EthAssetFlowMapper extends IDataStoreMapper<String, EthAssetFlow> {

    List<EthAssetFlow> getUserDailyIncomeList(String stringDate);

    Long findSumTradeAmount(EthAssetFlow query);
}
package cc.stbl.token.innerdisc.modules.eth.trades.listener.impl;

import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthIntegralReturnedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.stbl.token.innerdisc.modules.basic.entity.VrPrizeDetail;
import cc.stbl.token.innerdisc.modules.basic.service.VrPrizeDetailService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthReturnedIntegral;


/**
 * 获取红包回调
 * @author fyf
 *
 */
@Component
public class EthReturnedIntegralListenerImpl extends EthIntegralReturnedListener {

	public static Logger logger = LoggerFactory.getLogger(EthReturnedIntegralListenerImpl.class);
	
	@Override
	public void onSuccess(EthReturnedIntegral returnedIntegral) {

	}

}

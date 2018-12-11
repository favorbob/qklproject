package cc.stbl.token.innerdisc.modules.basic.service;

import org.springframework.stereotype.Service;

import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.modules.basic.dao.ActivateCardFlowDetailMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateCardFlowDetail;

@Service
public class ActivateCardFlowDetailService extends DataStoreServiceImpl<String, ActivateCardFlowDetail, ActivateCardFlowDetailMapper> {

}

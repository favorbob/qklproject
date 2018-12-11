/**
* generator by mybatis plugin Fri Sep 21 09:41:52 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.trades.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.trades.dao.TwdOfflinePayedAuthMapper;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeRecord;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdOfflinePayedAuth;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TwdOfflinePayedAuthService extends DataStoreServiceImpl<String, TwdOfflinePayedAuth, TwdOfflinePayedAuthMapper> {

    public void uploadPayed(TwdLinkedTradeRecord record, List<String> authUrls) {
        String loginUserId = ShiroUtils.getLoginUserId();
        for (String authUrl : authUrls) {
            TwdOfflinePayedAuth auth = new TwdOfflinePayedAuth();
            auth.setAuthUrl(authUrl);
            auth.setCreateDate(new Date());
            auth.setId(JavaUUIDGenerator.get());
            auth.setRecordId(record.getId());
            auth.setLinkedId(record.getLinkedId());
            auth.setUserId(loginUserId);
            this.add(auth);
        }
    }

    public List<TwdOfflinePayedAuth> getPayedAuthByRecordId(String recordId) {
        TwdOfflinePayedAuth auth = new TwdOfflinePayedAuth();
        auth.setRecordId(recordId);
        return this.findList(auth);
    }
}
package cc.stbl.token.innerdisc.restapi.app.payment;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.payment.service.MyWalletService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletRestControllerTest extends AbstractTestCase {

    @Autowired
    private MyWalletService myWalletService;

    @Test
    public void createWalletAccount(){
        String userId = "a8c89810cc134412ab6267617f4c3a1b";
        Response wallet = myWalletService.createWallet(userId);
        System.out.println(JSON.toJSON(wallet));
    }

    @Test
    public void bindAccountTest(){
        WalletProto.ReqBindSupports request = new WalletProto.ReqBindSupports();
        request.setAccount("12345679");
        request.setUserName("唐昱轩");
        request.setBindMethod("ALI-APP-PRI");
        request.setPhone("18127818723");
        String userId = "a8c89810cc134412ab6267617f4c3a1b";
        myWalletService.bindAccount(request,userId);
    }

}

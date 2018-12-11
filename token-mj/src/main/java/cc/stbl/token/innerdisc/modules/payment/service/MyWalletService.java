package cc.stbl.token.innerdisc.modules.payment.service;

import cc.stbl.framework.protocol.interfaces.ScrollPager;
import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.payment.enm.BusinessTypeEnm;
import cc.stbl.token.innerdisc.restapi.app.payment.WalletProto;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.stbl.payment.adapter.PaymentInterface;
import com.stbl.payment.adapter.WalletInterface;
import com.stbl.payment.adapter.WithdrawInterface;
import com.stbl.payment.boot.PaymentProperties;
import com.stbl.payment.business.wallet.entity.Wallet;
import com.stbl.payment.providerImpl.bizorder.bean.BizPayOrderBean;
import com.stbl.payment.providerImpl.bizorder.bean.CreateWithDrawOrderResponse;
import com.stbl.payment.providerImpl.bizorder.bean.DrawingBean;
import com.stbl.payment.providerImpl.bizorder.bean.PayMethodBean;
import com.stbl.payment.providerImpl.trades.bean.QueryTradeWaterFlowList;
import com.stbl.payment.providerImpl.trades.bean.TradeWaterFlowBean;
import com.stbl.payment.providerImpl.waller.BankCardServiceProvider;
import com.stbl.payment.providerImpl.waller.bean.ReqBindObject;
import com.stbl.payment.providerImpl.waller.bean.ReqCardInfo;
import com.stbl.payment.providerImpl.waller.bean.ResCardInfo;
import com.stbl.payment.providerImpl.waller.bean.WalletInfoBean;
import javaxx.financial.payment.channel.utils.AmountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MyWalletService {

    public static final Logger logger = LoggerFactory.getLogger(MyWalletService.class);

    @Autowired
    private PaymentInterface orderProvider;

    @Autowired
    private BankCardServiceProvider bankCardServiceProvider;

    @Autowired
    private WalletInterface walletInterface;

    @Autowired
    private WithdrawInterface withdrawInterface;


    @Autowired
    private PaymentProperties paymentProperties;

    @Autowired
    private EthWalletService ethWalletService; //以太坊钱包

    //创建钱包
    public Response createWallet(String userId) {
        Response<Wallet> wallet = walletInterface.createWallet(userId);
        WalletProto.ResponseWalletSupports wResponse = new WalletProto.ResponseWalletSupports();
        if (wallet.getData() == null){
            return Response.error(wallet.getMessage());
        }
        BeanUtils.copyProperties(wallet.getData(),wResponse);
        return Response.success(wResponse);
    }

    public Response createEthWallet(String userId, String payPassword, String walletName){
        EthWallet ethWallet = ethWalletService.getUserWallet(userId);
        if(ethWallet == null) {
            ethWallet = ethWalletService.createEthWallet(userId,payPassword,walletName);
        }
        return Response.success(ethWallet.getAddress());
    }

    //绑账号
    public Response bindAccount(WalletProto.ReqBindSupports request,String userId){
        Response<WalletProto.ResponseWalletSupports> walletDetail = getWalletDetail(userId);
        ReqBindObject bindObject = new ReqBindObject();
        bindObject.setAccount(request.getAccount());
        bindObject.setBankName(request.getUserName());
        bindObject.setBindMethod(request.getBindMethod());
        bindObject.setRealName(request.getUserName());
        bindObject.setPhone(request.getPhone());
        bindObject.setUserId(userId);
        bindObject.setIdCard("");
        bindObject.setPartner(paymentProperties.getPartner());
        bindObject.setWalletId(walletDetail.getData().getWalletId());
        Response<String> response = bankCardServiceProvider.bindObject(bindObject);
        if(!response.isSuccess()) {
            logger.warn("绑定账号报错 userId={}`account={}`username={}`error={}",
                    userId,request.getAccount(),request.getUserName(),
                    response.getMessage());
            return Response.error(response.getMessage());
        }
        return Response.success(response.getData());
    }

    //充值
    public Response recharge(WalletProto.RechargeRequestSupports request){
        String userId = ShiroUtils.getLoginUserId();
        BizPayOrderBean order = null;
        try {
            order = new BizPayOrderBean();
            order.setBusinessType(BusinessTypeEnm.VR_RECHARGE.getBusinessType());
            order.setBusinessId(UUID.randomUUID().toString().replace("-",""));
            order.setRemarks("充值");
            order.setPayerId(userId);
            order.setTotalAmount(AmountUtils.convertMinute(request.getAmount().doubleValue()));
            order.setPayeeId(userId);
            List<PayMethodBean> pays = new ArrayList<>();
            PayMethodBean methodBean = new PayMethodBean();
            BigDecimal payAmount = request.getAmount();
            methodBean.setPayAmount(AmountUtils.convertMinute(payAmount.doubleValue()));
            methodBean.setPayMethod(request.getPayMethod());
            pays.add(methodBean);
            order.setPayMethods(pays);
            return Response.success(orderProvider.payOrder(order));
        } catch (StructWithCodeException e){
            throw e;
        }  catch (Exception e) {
            logger.error(e.getMessage(),e);
            return Response.error("充值失败!");
        }

    }
    //零钱提现

    public Response withdraw(WalletProto.RechargeRequestSupports request){
        String userId = ShiroUtils.getLoginUserId();
        Response<ResCardInfo> resCardInfoResponse = cartInfo(userId);
        DrawingBean db = new DrawingBean();
        db.setDrawingMoney(AmountUtils.convertMinute(request.getAmount().doubleValue()));
        db.setDraweeAccount(resCardInfoResponse.getData().getAccount());
        db.setDraweeName(resCardInfoResponse.getData().getRealName());
        db.setBusinessId(UUID.randomUUID().toString().replace("-",""));
        db.setBusinessType(BusinessTypeEnm.VR_WITHDRAW.getBusinessType());
        db.setUserId(userId);
        db.setRemark("提现");
        db.setDrawMethod(request.getPayMethod());
        db.setWithdrawtype(2);
        CreateWithDrawOrderResponse res = withdrawInterface.createDrawingBusinessOrder(db);
        if (res != null){
            return Response.success(res);
        }
        return Response.error("提现失败!");
    }

    //钱包流水
    public Response<ScrollPager<WalletProto.ResponseWaterFlowSupports>>  getWalletTradeFlow(@RequestBody WalletProto.ReqWalletListSupports request) {
        QueryTradeWaterFlowList qWaterFlowList = new QueryTradeWaterFlowList();
        qWaterFlowList.setUserId(request.getUserId());
        qWaterFlowList.setLimit(20);
        qWaterFlowList.setDirection(request.getDirection());
        qWaterFlowList.setStartTradeTime(request.getStartTradeTime());
        qWaterFlowList.setEndTradeTime(request.getEndTradeTime());
        qWaterFlowList.setFlag(request.getFlag());
//        setMethodStatus(qWaterFlowList);
        Response<ScrollPager<TradeWaterFlowBean>> scrollPagerResponse = walletInterface.queryTradeWaterFlowList(qWaterFlowList);
        if(scrollPagerResponse.getData().getList().size() == 0){
            return Response.error("没有数据！");
        }
        List<WalletProto.ResponseWaterFlowSupports> wallterFlowBeanList = new ArrayList<>();
        for (TradeWaterFlowBean tw: scrollPagerResponse.getData().getList()) {
            WalletProto.ResponseWaterFlowSupports wtWaterFlowBean = new WalletProto.ResponseWaterFlowSupports();
            BeanUtils.copyProperties(tw,wtWaterFlowBean);
            wallterFlowBeanList.add(wtWaterFlowBean);
        }
        ScrollPager scrollPager = new ScrollPager();
        scrollPager.setList(wallterFlowBeanList);
        return Response.success(scrollPager);
    }

    //获取钱包信息
    public Response<WalletProto.ResponseWalletSupports> getWalletDetail(String userId){
        Response<WalletInfoBean> walletInfo = walletInterface.getWalletInfo(userId);
        if(walletInfo.getCode() == 1404) {
            walletInterface.createWallet(userId);
            walletInfo = walletInterface.getWalletInfo(userId);
//            throw new StructWithCodeException(ResponseCode.NO_WALLET);
        }
        WalletProto.ResponseWalletSupports responseData = new WalletProto.ResponseWalletSupports();
        BeanUtils.copyProperties(walletInfo.getData(),responseData);
        return Response.success(responseData);
    }

    public Response<ResCardInfo> cartInfo(String userId){
        ReqCardInfo reqCardInfo = new ReqCardInfo();
        reqCardInfo.setUserId(userId);
        Response<ResCardInfo> cardInfoResponse = walletInterface.cardInfo(reqCardInfo);
        return cardInfoResponse;
    }

    public boolean hasEthWallet(String userId){
        return ethWalletService.hasWallet(userId);
    }
    public boolean checkHasWallet(String userId){
        try{
            Response<WalletInfoBean> walletInfo = walletInterface.getWalletInfo(userId);
            return walletInfo.getCode() != 1404;
        }catch (StructWithCodeException e){
            return false;
        }
    }
}

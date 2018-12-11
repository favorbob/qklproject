package cc.stbl.token.innerdisc.modules.payment.service;

import com.alibaba.fastjson.JSON;
import com.stbl.payment.adapter.MerchantTradeInterface;
import com.stbl.payment.adapter.PaymentInterface;
import com.stbl.payment.adapter.notify.PaymentNotifyInterface;
import com.stbl.payment.providerImpl.bizorder.bean.BizPaymentOrderEntry;
import com.stbl.payment.providerImpl.bizorder.bean.PaymentNotifyParty;
import com.stbl.payment.providerImpl.bizorder.bean.RefundNotifyParty;
import com.stbl.payment.providerImpl.bizorder.bean.WithDrawNotifyParty;
import com.stbl.payment.providerImpl.merchant.bean.CreatePaymentOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyService implements PaymentNotifyInterface {

    public static final Logger logger = LoggerFactory.getLogger(NotifyService.class);

    @Autowired
    private PaymentInterface paymentInterface;

    @Autowired
    private MerchantTradeInterface merchantTradeInterface;

    @Override
    public boolean payNotify(PaymentNotifyParty notifyParty) {
        logger.info("接收组件回调参数{}:", JSON.toJSON(notifyParty));
        try {
//            String businessId = notifyParty.getBusinessId().replace("-", "");
            BizPaymentOrderEntry orderEntry = paymentInterface.queryPayOrder(notifyParty.getBusinessId(),notifyParty.getBusinessType());
            CreatePaymentOrderRequest createPaymentOrderRequest = new CreatePaymentOrderRequest();
            createPaymentOrderRequest.setBusinessId(orderEntry.getBusinessId());
            createPaymentOrderRequest.setBusinessType(orderEntry.getBusinessType());
            createPaymentOrderRequest.setPayAmount(orderEntry.getOrderPayAmount());
            merchantTradeInterface.merchantPayment(createPaymentOrderRequest);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean refundNotify(RefundNotifyParty notifyParty) {
        logger.info("接收组件回调退款参数{}:", JSON.toJSON(notifyParty));

        return false;
    }

    @Override
    public boolean withDrawNotify(WithDrawNotifyParty notifyParty) {

        logger.info("接收组件回调提现参数{}:", JSON.toJSON(notifyParty));
//        UserWithdraw userWithdraw = new UserWithdraw();
//        userWithdraw.setTradeStatus(TradeOrderStatusEnum.ORDER_WITHDRAW_TRADE_SUCCESS.intCode());
//        userWithdraw.setOutWithdrawNumber(withDrawNotifyParty.getTradeNo());
//        withdrawService.update(userWithdraw);
        return true;
    }
}

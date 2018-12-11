package cc.stbl.token.innerdisc.modules.payment.service;


import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.payment.enm.BusinessTypeEnm;
//import cc.stbl.token.innerdisc.modules.payment.utils.PaymentUtil;
import cc.stbl.token.innerdisc.restapi.app.payment.PayMethodProto;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.stbl.payment.adapter.impl.PaymentInterfaceImpl;
import com.stbl.payment.providerImpl.bizorder.bean.BizPayOrderBean;
import com.stbl.payment.providerImpl.bizorder.bean.CreatePayBusinessResponse;
import com.stbl.payment.providerImpl.bizorder.bean.PayMethodBean;
import com.stbl.payment.providerImpl.cashier.bean.ChannelDscBean;
import com.stbl.payment.providerImpl.cashier.bean.ChannelQueryBean;
import javaxx.financial.payment.channel.utils.AmountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    public static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentInterfaceImpl paymentInterface;

    //获取支付渠道
    public List<PayMethodProto.ResponseSupports> supportChannel(@RequestBody @Valid PayMethodProto.RequestSupports request){
        List<PayMethodProto.ResponseSupports> supports = new ArrayList<>();
        ChannelQueryBean channelQueryBean = new ChannelQueryBean();
        channelQueryBean.setBusinessType(request.getBusinessType());
        channelQueryBean.setAmount(0L);
        List<ChannelDscBean> channelDscBeans = paymentInterface.supportChannel(channelQueryBean);
        for (ChannelDscBean desBean:channelDscBeans) {
            PayMethodProto.ResponseSupports s = new PayMethodProto.ResponseSupports();
            s.setPayMethod(desBean.getCode());
            s.setIcon(desBean.getPic());
            supports.add(s);
        }
        return supports;
    }

    //购买订单
    public Response payOrder(PayMethodProto.PayRequestSupports tradeOrder) {
        String userId = ShiroUtils.getLoginUserId();
        logger.info("支付订单开始=======》》{}", JSON.toJSON(tradeOrder));
        try {
            tradeOrder.setBusinessId(tradeOrder.getBusinessId());
            Response<CreatePayBusinessResponse> responseData = this.createPaymentOrder(tradeOrder);
            if(!responseData.isSuccess() || responseData.getData() == null){
                logger.error("订单号{} 支付失败{}", tradeOrder.getBusinessId(), responseData.getMessage());
                return Response.error("支付失败:" + responseData.getMessage());
            }
            return Response.success(responseData.getData());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return null;
        }

    }

    public Response<CreatePayBusinessResponse> createPaymentOrder(PayMethodProto.PayRequestSupports paymentOrder) {
        BizPayOrderBean order = null;
        try {
            order = new BizPayOrderBean();
            order.setBusinessType(BusinessTypeEnm.VR_PAY.getBusinessType());
            order.setBusinessId(paymentOrder.getBusinessId());
            order.setRemarks(paymentOrder.getRemarks());
            order.setPayerId(paymentOrder.getPayerId());
            order.setTotalAmount(AmountUtils.convertMinute(paymentOrder.getOrderAmount().doubleValue()));
            order.setPayeeId(paymentOrder.getPayeeId());
            order.setRemarks(paymentOrder.getRemarks());
            List<PayMethodBean> pays = new ArrayList<>();
            PayMethodBean methodBean = new PayMethodBean();
            BigDecimal payAmount = paymentOrder.getOrderAmount();
            methodBean.setPayAmount(AmountUtils.convertMinute(payAmount.doubleValue()));
            methodBean.setPayMethod(paymentOrder.getPayMethod());
            pays.add(methodBean);
            order.setPayMethods(pays);
            return Response.success(paymentInterface.payOrder(order));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new StructWithCodeException(e.getMessage());
//            return Response.error(e.getMessage()) ;
        }

    }
}

package cc.stbl.token.innerdisc.restapi.app.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


public class PayMethodProto {
    @Data
    public static class RequestSupports {
        private String businessType;
    }

    @Data
    public static class ResponseSupports {
        @ApiModelProperty("支付方法")
        private String payMethod;
        @ApiModelProperty("图标")
        private String icon;
    }

    @Data
    public static class PayRequestSupports{

        private String payerId;//付款人ID

        private BigDecimal orderAmount;//订单金额

        private String remarks;//备注

        private String payeeId;//收款人id

        private String businessId; //业务ID

        private String payMethod;//支付方法

    }

}

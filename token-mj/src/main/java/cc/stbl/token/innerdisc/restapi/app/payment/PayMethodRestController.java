package cc.stbl.token.innerdisc.restapi.app.payment;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.modules.payment.service.PaymentService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = {PathPrefix.API_PATH + "/payMethod"})
@Api(description = "支付")
public class PayMethodRestController {

    @Autowired
    private PaymentService paymentService;

    @ApiOperation("获取已开通的支付方法列表")
    @ApiImplicitParam(name = "businessType",value = "业务类型类型",required = true,dataType = "String")
    @RequestMapping(value = {"/supportChannel"},method = RequestMethod.POST)
    @ResponseBody
    public Response<List<PayMethodProto.ResponseSupports>> supportChannel(@RequestBody @Valid PayMethodProto.RequestSupports request){
        return Response.success(paymentService.supportChannel(request));
    }


}

package cc.stbl.token.innerdisc.restapi.app.trades;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeRecord;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdOfflinePayedAuth;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeRecordService;
import cc.stbl.token.innerdisc.modules.trades.service.TwdOfflinePayedAuthService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {PathPrefix.API_PATH + "/linked/payedAuth"})
@Api(description = "支付凭证@Leon")
public class PayedAuthRestController {

    @Autowired
    private TwdLinkedTradeRecordService recordService;

    @Autowired
    private TwdOfflinePayedAuthService payedAuthService;

    @Autowired
    private OssProperties ossProperties;

    @RequestMapping(value = {"/uploadPayed"},method = RequestMethod.POST)
    @ApiOperation(value = "上传凭证图",notes = "先调用上传upload-file-rest-controller获得图片地址")
    public Response uploadPayed(@RequestBody PayedAuthProto.UploadPayedRequest request){
        TwdLinkedTradeRecord record = recordService.get(request.getRecordId());
        if(record == null) return Response.bulid(ResponseCode.LINKED_TRADE_ORDER_NOT_FOUND);
        payedAuthService.uploadPayed(record,request.getAuthUrls());
        return Response.success();
    }

    @RequestMapping(value = {"/tradePayedAuth"},method = RequestMethod.POST)
    @ApiOperation(value = "获取支付凭证")
    public Response<PayedAuthProto.TradePayedAuthResponse> tradePayedAuth(@RequestBody PayedAuthProto.TradePayedAuthRequest request){
        List<TwdOfflinePayedAuth> payedAuths = payedAuthService.getPayedAuthByRecordId(request.getRecordId());
        PayedAuthProto.TradePayedAuthResponse response = new PayedAuthProto.TradePayedAuthResponse();
        for (TwdOfflinePayedAuth payedAuth : payedAuths) {
            response.addAuthUrl(ossProperties.getDefault().getHost() + payedAuth.getAuthUrl());
        }
        return Response.success(response);
    }
}

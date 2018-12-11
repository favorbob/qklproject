package cc.stbl.token.innerdisc.restapi.admin.trades;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RequestMapping(value = {PathPrefix.ADMIN_PATH + "/trades/vrToken"})
@RestController
@Api(description = "资产交易管理")
public class VrTokenTradeController {

    @Autowired
    private VrTokenTradeService tokenTradeService;

    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    @RequestMapping(value = {"/chargeAsset"},method = {RequestMethod.POST})
    @RequiresPermissions(value = {"trade:coin:charge"})
    @ApiOperation("资产充值")
    public Response chargeAsset(@RequestBody @Valid VrTokenTradeProto.ChargeAssetBalanceRequest request){
//        VrUserInfo user = userService.getUserByPhoneNum(request.getPhoneNum());
        boolean success = tokenTradeService.chargeAsset(request.getUserId(), BEnum.SYS_CHARGE,request.getAmount(),"系统充值");
        return Response.success("交易信息已提交");
    }
    @RequestMapping(value = {"/deductAsset"},method = {RequestMethod.POST})
    @RequiresPermissions(value = {"trade:coin:deduct"})
    @ApiOperation("资产扣除")
    public Response deductAsset(@RequestBody @Valid VrTokenTradeProto.DeductAssetBalanceRequest request){
//        VrUserInfo user = userService.getUserByPhoneNum(request.getPhoneNum());
        boolean success = tokenTradeService.deductAsset(request.getUserId(), BEnum.DEDUCT,request.getAmount(),"系统扣除");
        return Response.success("交易信息已提交");
    }

    @RequestMapping(value = {"/assetFlowWater"},method = {RequestMethod.POST})
    @RequiresPermissions(value = {"trade:coin:view"})
    @ApiOperation("资产交易流水")
    public Response assetFlowWater(@RequestBody @Valid VrTokenTradeProto.AssetFlowWaterRequest request){
        EthAssetFlow query = new EthAssetFlow();
        BeanUtils.copyProperties(request,query);
        Pager<EthAssetFlow> pager = ethAssetFlowService.findPager(query,request.getPageNo(),request.getPageSize());
        return Response.success(pager);
    }

    @RequestMapping(value = {"/chargeSubAsset"},method = {RequestMethod.POST})
    @RequiresPermissions(value = {"trade:coin:chargeSub"})
    @ApiOperation("下线资产充值")
    public Response chargeSubAsset(@RequestBody @Valid VrTokenTradeProto.ChargeSubAssetBalanceRequest request){
        boolean success = tokenTradeService.chargeSubAsset(request.getSubUserId(),request.getAmount(),request.getPayPwd(),request.getRemark());
        return Response.success("交易信息已提交");
    }
}

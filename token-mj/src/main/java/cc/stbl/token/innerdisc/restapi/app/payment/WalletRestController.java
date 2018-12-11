package cc.stbl.token.innerdisc.restapi.app.payment;

import cc.stbl.framework.protocol.interfaces.ScrollPager;
import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.payment.service.MyWalletService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.stbl.payment.business.wallet.entity.Wallet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = {PathPrefix.API_PATH + "/wallet"})
@Api(description = "我的钱包@唐昱轩")
public class WalletRestController {

    @Autowired
    private MyWalletService myWalletService;

    @ApiOperation("创建钱包")
    @RequestMapping(value = {"/createWallet"},method = RequestMethod.POST)
    public Response createWallet(@RequestBody @Valid WalletProto.RequestCreateWallet request) {
        String userId = ShiroUtils.getLoginUserId();
        Response ethWallet = myWalletService.createEthWallet(userId, request.getPayPassword(),request.getWalletName());
        myWalletService.createWallet(userId);
        return ethWallet;
    }

    //绑定提现账号
    @ApiOperation("绑定提现账号")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "account",value = "账号",required = true,dataType = "String"),
//            @ApiImplicitParam(name = "userName",value = "姓名",required = true,dataType = "String"),
//            @ApiImplicitParam(name = "bindMethod",value = "绑定方法",required = true,dataType = "String"),
//            @ApiImplicitParam(name = "phone",value = "电话",required = true,dataType = "String")
//    })
    @RequestMapping(value = {"/bindAccount"},method = RequestMethod.POST)
    @ResponseBody
    public Response bindAccount(@RequestBody @Valid WalletProto.ReqBindSupports request){
        String userId = ShiroUtils.getLoginUserId();
        return myWalletService.bindAccount(request,userId);
    }

    //充值
    @ApiOperation("充值")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "payMethod",value = "账号",required = true,dataType = "String"),
//            @ApiImplicitParam(name = "amount",value = "姓名",required = true,dataType = "BigDecimal")
    })
    @RequestMapping(value = {"/recharge"},method = RequestMethod.POST)
    @ResponseBody
    public Response recharge(@RequestBody @Valid WalletProto.RechargeRequestSupports request){
        return myWalletService.recharge(request);
    }

    //零钱提现
    @ApiOperation("零钱提现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payMethod",value = "账号",required = true,dataType = "String"),
            @ApiImplicitParam(name = "amount",value = "姓名",required = true,dataType = "BigDecimal")
    })
    @RequestMapping(value = {"/withdraw"},method = RequestMethod.POST)
    @ResponseBody
    public Response withdraw(@RequestBody @Valid WalletProto.RechargeRequestSupports request){
        return myWalletService.withdraw(request);
    }

    //钱包流水
    @ApiOperation("钱包流水")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTradeTime",value = "开始时间",required = true,dataType = "Date"),
            @ApiImplicitParam(name = "endTradeTime",value = "结束时间",required = true,dataType = "Date"),
            @ApiImplicitParam(name = "flag",value = "滑动下拉标志,第一次不用传,之后根据返回值传",required = true,dataType = "Date")
    })
    @RequestMapping(value = {"/getWalletTradeFlow"},method = RequestMethod.POST)
    @ResponseBody
    public Response<ScrollPager<WalletProto.ResponseWaterFlowSupports>>  getWalletTradeFlow(WalletProto.ReqWalletListSupports request){
        return myWalletService.getWalletTradeFlow(request);
    }


    //获取钱包
    @ApiOperation("获取钱包")
    @RequestMapping(value = {"/getWalletDetail"},method = RequestMethod.GET)
    @ResponseBody
    public Response<WalletProto.ResponseWalletSupports> getWalletDetail(){
        String userId = ShiroUtils.getLoginUserId();
        return myWalletService.getWalletDetail(userId);
    }
}

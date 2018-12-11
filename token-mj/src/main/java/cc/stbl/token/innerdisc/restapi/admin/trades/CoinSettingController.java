package cc.stbl.token.innerdisc.restapi.admin.trades;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.tx.EthNonceManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.basic.service.SiteSettingService;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import cc.stbl.token.innerdisc.modules.eth.trades.accelerate.AbstractIntegralAdapter;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;

import javax.validation.Valid;

@RequestMapping(value = {PathPrefix.ADMIN_PATH + "/trades/coinSetting"})
@RestController
@Api(description = "区块链币设置")
public class CoinSettingController {

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private SiteSettingService siteSettingService;

    @Autowired
    private VrTokenTradeService vrTokenTradeService;

    @Autowired
    private EthNonceManager ethNonceManager;

    @Autowired
    private Credentials sysCredentials;

    @RequestMapping(value = {"/get"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"coin:setting:view"})
    @ApiOperation("区块链币设置数据获取")
    public Response<CoinSettingProto.Settings> getSettings(){
        CoinSettingProto.Settings view = new CoinSettingProto.Settings();
        VRToken vrToken = vrTokenManager.getLastVrToken();
        view.setCoinAttrs(new CoinSettingProto.CoinAttrs());
        if(vrToken != null) {
            view.getCoinAttrs().setAddress(vrToken.getContractAddress());
            view.getCoinAttrs().setInitSupply(UnitConvertUtils.toEtherBigInter(vrTokenManager.initTotalSupply(vrToken)));
//            view.getCoinAttrs().setRatio(new BigDecimal(integralRebateAdapter.readMagnification(ShiroUtils.getLoginUserId())));
        }
        view.setSysAttrs(new CoinSettingProto.SysAttrs());
        view.getSysAttrs().setIntegralRebateRatio(siteSettingService.getIntegralRebateRatio());
        view.setTradeAttrs(new CoinSettingProto.TradeAttrs());
        view.getTradeAttrs().setSellerPrice(siteSettingService.getSellerPrice());
        return Response.success(view);
    }
    @RequestMapping(value = {"/syncNonce"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"coin:setting:edit"})
    @ApiOperation("同步nonce")
    public Response syncNonce(){
        ethNonceManager.syncNonce(sysCredentials.getAddress());
        return Response.success();
    }

    @RequestMapping(value = {"/submitLoseTx"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"coin:setting:edit"})
    @ApiOperation("补偿交易")
    public Response submitLoseTx() throws Exception {
        return Response.success(ethNonceManager.submitLoseTx(sysCredentials.getAddress()));
    }

    @RequestMapping(value = {"/save"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"coin:setting:edit"})
    @ApiOperation("区块链币设置保存")
    public Response saveSettings(@RequestBody @Valid CoinSettingProto.Settings setting){
        if(setting.getCoinAttrs() != null) {
            CoinSettingProto.CoinAttrs coinAttrs = setting.getCoinAttrs();
            VRToken vrToken;
            if(StringUtils.isBlank(coinAttrs.getAddress())) {
                if(coinAttrs.getInitSupply() == null) return Response.error("首发金额错误");
                vrToken = vrTokenManager.deployed(ShiroUtils.getLoginUserId(),"VR币","VRC",coinAttrs.getInitSupply());
                coinAttrs.setAddress(vrToken.getContractAddress());
            } else {
                vrToken = vrTokenManager.getLastVrToken();
                coinAttrs.setInitSupply(UnitConvertUtils.toEtherBigInter(vrTokenManager.initTotalSupply(vrToken)));
            }
        }
        if(setting.getSysAttrs() != null) {
            siteSettingService.setIntegralRebateRatio(setting.getSysAttrs().getIntegralRebateRatio());
        }
        if(setting.getTradeAttrs() != null) {
            siteSettingService.setSellerPrice(setting.getTradeAttrs().getSellerPrice());
        }
        return Response.success(setting);
    }

    @ApiOperation("从指定的上个合约导入数据")
    @RequiresPermissions(value = {"coin:setting:edit"})
    @RequestMapping(value = {"/importData"},method = RequestMethod.POST)
    public Response importData(@RequestBody @Valid CoinSettingProto.ImportData request){
       return Response.success(vrTokenTradeService.importDataFrom(request.getAddress(),request.getMock(),request.getBatch()));
    }

    @ApiOperation("查看钱包信息")
    @RequiresPermissions(value = {"coin:setting:edit"})
    @RequestMapping(value = {"/dumpWalletInfo"},method = RequestMethod.POST)
    public Response dumpWalletInfo(String address){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address",address);
        jsonObject.put("aiic",UnitConvertUtils.toEther(vrTokenManager.balanceOf(address)));
        jsonObject.put("mj",UnitConvertUtils.toEther(vrTokenManager.limitBalanceOf(address)));
        jsonObject.put("asset",UnitConvertUtils.toEther(vrTokenManager.integralOf(address)));
        return Response.success(jsonObject);
    }

    @RequestMapping(value = {"/getTotalStationSetting"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"coin:setting:view"})
    @ApiOperation("获取全站设置")
    public Response<CoinSettingProto.CoinAttrs> getTotalStationSetting(){
        CoinSettingProto.CoinAttrs coinAttrs = siteSettingService.getCoinAttrs();
        String systemReceiptCode = siteSettingService.getSystemReceiptCode();
        coinAttrs.setReceiptCode(systemReceiptCode);
        return Response.success(coinAttrs);
    }

    @RequestMapping(value = {"/totalStationSetting"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"coin:setting:edit"})
    @ApiOperation("全站设置")
    public Response totalStationSetting(@RequestBody CoinSettingProto.CoinAttrs coinAttrs){
        siteSettingService.setCoinAttrs(coinAttrs);
        siteSettingService.saveSystemReceiptCode(coinAttrs.getReceiptCode());
        return Response.success();
    }


}

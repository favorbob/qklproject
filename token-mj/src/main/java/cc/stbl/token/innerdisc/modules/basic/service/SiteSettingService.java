package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;
import cc.stbl.token.innerdisc.restapi.admin.rebate.RebateProto;
import cc.stbl.token.innerdisc.restapi.admin.trades.CoinSettingProto;
import com.ks.common.datastore.exception.StructWithCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SiteSettingService {

    @Autowired
    private SysPropertiesService propertiesService;

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private OssProperties ossProperties;

    @Autowired
    private RebateConfigService rebateConfigService;

    public CoinSettingProto.CoinAttrs getCoinAttrs(){
        CoinSettingProto.CoinAttrs coinAttrs = new CoinSettingProto.CoinAttrs();
        VRToken vrToken = vrTokenManager.getLastVrToken();
        if(vrToken != null) {
            coinAttrs.setAddress(vrToken.getContractAddress());
            coinAttrs.setInitSupply(UnitConvertUtils.toEtherBigInter(vrTokenManager.initTotalSupply(vrToken)));
            coinAttrs.setRatio(getMagnification());
        }
        return coinAttrs;
    }

    public void setCoinAttrs(CoinSettingProto.CoinAttrs coinAttrs){
        VRToken vrToken;
        if(StringUtils.isBlank(coinAttrs.getAddress())) {
            if(coinAttrs.getInitSupply() == null) throw new StructWithCodeException("首发金额错误");
            vrToken = vrTokenManager.deployed(ShiroUtils.getLoginUserId(),"VR币","VRC",coinAttrs.getInitSupply());
            coinAttrs.setAddress(vrToken.getContractAddress());
        }
        try {
            if(coinAttrs.getRatio() != null) setMagnification(coinAttrs.getRatio());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTransferToIntegral(boolean is){
        propertiesService.setProperties("vrToken","sys.trade.isTransferToIntegral",String.valueOf(is));
    }
    //系统上线设置
    public boolean isTransferToIntegral() {
        return propertiesService.getBoolean("vrToken", "sys.trade.isTransferToIntegral",false);
    }

    //积分释放比例
    public void setIntegralRebateRatio(BigDecimal bigDecimal){
        propertiesService.setProperties("vrToken","integral.rebate.ratio",bigDecimal.toString());
    }
    //积分释放比例
    public BigDecimal getIntegralRebateRatio(){
       return propertiesService.getBigDecimal("vrToken","integral.rebate.ratio");
    }
    //平台单价
    public void setSellerPrice(BigDecimal bigDecimal){
        propertiesService.setProperties("vrToken","trade.seller.price",bigDecimal.toString());
    }
    //平台单价
    public BigDecimal getSellerPrice(){
       return propertiesService.getBigDecimal("vrToken","trade.seller.price");
    }
    //积分释放可用和受限资产比例
    public BigDecimal getLimitedRatio() {
        BigDecimal v = propertiesService.getBigDecimal("vrToken", "integral.rebate.limitedRatio");
        return v == null ? new BigDecimal("3") : v;
    }
    //积分释放可用和受限资产比例
    public void setLimitedRatio(BigDecimal value) {
        propertiesService.setProperties("vrToken", "integral.rebate.limitedRatio",value.toString());
    }

    //资产积分倍数  1：Magnification
    public BigDecimal getMagnification() {
        BigDecimal v = propertiesService.getBigDecimal("vrToken", "trade.amply.Magnification");
        return v == null ? new BigDecimal("3") : v;
    }
    //资产积分倍数  1：Magnification
    public void setMagnification(BigDecimal value) {
        propertiesService.setProperties("vrToken", "trade.amply.Magnification",value.toString());
    }

    //挂单交易开关
    public boolean getLinkedOnOff() {
        return propertiesService.getBoolean("vrToken","trade.linked.onOff",true);
    }

    public void setLinkedOnOff(boolean onOff){
        propertiesService.setProperties("vrToken","trade.linked.onOff",String.valueOf(onOff));
    }


    public SysPropertiesService getPropertiesService() {
        return propertiesService;
    }

    public void setPropertiesService(SysPropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    public void saveSystemReceiptCode(String path){
        propertiesService.setProperties("vrToken","sys.receipt.code",path);
    }

    public String getSystemReceiptCode(){
        String url = propertiesService.getString("vrToken", "sys.receipt.code");
        return ossProperties.getDefault().getHost() + url;
    }

    public void saveInvolSetting(RebateProto.InvolSetting involSetting){
        // 资产加速设置
        rebateConfigService.addRebateConfigList(involSetting.getRebateConfigList());
        // 系统上线设置
        this.setTransferToIntegral(involSetting.getSystemOnline());
        // TODO 定时上线时间

        // 挂单交易平台启动设置
        this.setLinkedOnOff(involSetting.getLinkedOnOff());
        // 资产与积分比例
        BigDecimal asset = involSetting.getAsset();
        BigDecimal integral = involSetting.getIntegral();
        this.setMagnification(integral.divide(asset));
        // 初始值比例设置
        this.setIntegralRebateRatio(involSetting.getInitIntegralRebateRatio().divide(BigDecimal.valueOf(100L)));
        // 积分转化资产设置
        BigDecimal availableAssets = involSetting.getAvailableAssets();
        BigDecimal limitedAssets = involSetting.getLimitedAssets();
        this.setLimitedRatio(limitedAssets.divide(BigDecimal.valueOf(100L)));
    }

    public RebateProto.InvolSetting getInvolSetting(){
        RebateProto.InvolSetting involSetting = new RebateProto.InvolSetting();
        // 资产加速设置
        List<RebateProto.RebateConfig> rebateConfigList = rebateConfigService.getRebateConfigList();
        involSetting.setRebateConfigList(rebateConfigList);
        // 系统上线设置
        involSetting.setSystemOnline(this.isTransferToIntegral());
        // TODO 定时上线时间

        // 挂单交易平台启动设置
        involSetting.setLinkedOnOff(this.getLinkedOnOff());
        // 资产与积分比例
        BigDecimal magnification = this.getMagnification();
        involSetting.setIntegral(magnification);
        involSetting.setAsset(BigDecimal.ONE);
        // 初始值比例设置
        involSetting.setInitIntegralRebateRatio(this.getIntegralRebateRatio().multiply(BigDecimal.valueOf(100L)));
        // 积分转化资产设置
        BigDecimal limitedRatio = this.getLimitedRatio();
        involSetting.setLimitedAssets(limitedRatio);
        involSetting.setAvailableAssets(BigDecimal.ONE.subtract(limitedRatio).multiply(BigDecimal.valueOf(100L)));
        return involSetting;
    }
    public void setIosDownLoadUrl(String url){
        propertiesService.setProperties("appVersion","ios.download.url",url);
    }
    public void setAndroidDownLoadUrl(String url){
        propertiesService.setProperties("appVersion","android.download.url",url);
    }

    public String getIosDownLoadUrl() {
        return propertiesService.getString("appVersion","ios.download.url");
    }

    public String getAndroidDownLoadUrl() {
        return propertiesService.getString("appVersion","android.download.url");
    }
}

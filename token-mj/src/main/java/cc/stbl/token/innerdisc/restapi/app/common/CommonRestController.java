package cc.stbl.token.innerdisc.restapi.app.common;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.entity.IntegralFlow;
import cc.stbl.token.innerdisc.modules.eth.service.IntegralFlowService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.payment.service.MyAssetsService;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeRecord;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeRecordService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.app.payment.MyAssetsProto;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import javaxx.financial.payment.channel.utils.AmountUtils;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = {PathPrefix.API_PATH + "/common"})
@Api(description = "通用接口")
public class CommonRestController {

    @Autowired
    private SysPropertiesService proServive;

    @RequestMapping(value = {"/getSysParameters"},method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("获取系统参数配置")
    public Response<CommonProto.ResponseParameters> getSysParameters(){
        BigDecimal aiicRiseRatio = new BigDecimal(proServive.getStringFromDB("prize", "aiic.rise.ratio"));//aiic上浮比例
        BigDecimal aiicFallRatio = new BigDecimal(proServive.getStringFromDB("prize", "aiic.fall.ratio"));//aiic下浮比例
        BigDecimal mjRiseRatio = new BigDecimal(proServive.getStringFromDB("prize", "mj.rise.ratio"));//aiic上浮比例
        BigDecimal mjFallRatio = new BigDecimal(proServive.getStringFromDB("prize", "mj.fall.ratio"));//aiic下浮比例
        Long aiicSellMinNum = new Long(proServive.getStringFromDB("prize", "aiic.sell.minNum"));//aiic卖出最小数量
        Long aiicSellMaxNum = new Long(proServive.getStringFromDB("prize", "aiic.sell.maxNum"));//aiic卖出最大数量
        Long aiicBuyMinNum = new Long(proServive.getStringFromDB("prize", "aiic.buy.minNum"));//aiic买入最小数量
        Long aiicBuyMaxNum = new Long(proServive.getStringFromDB("prize", "aiic.buy.maxNum"));//aiic买入最大数量
        Long mjSellMinNum = new Long(proServive.getStringFromDB("prize", "mj.sell.minNum"));//mj卖出最小数量
        Long mjSellMaxNum = new Long(proServive.getStringFromDB("prize", "mj.sell.maxNum"));//mj卖出最大数量
        Long mjBuyMinNum = new Long(proServive.getStringFromDB("prize", "mj.buy.minNum"));//mj买入最小数量
        Long mjBuyMaxNum = new Long(proServive.getStringFromDB("prize", "mj.buy.maxNum"));//mj买入最大数量
        CommonProto.ResponseParameters responseParameters = new CommonProto.ResponseParameters();
        responseParameters.setAiicRiseRatio(aiicRiseRatio);
        responseParameters.setAiicFallRatio(aiicFallRatio);
        responseParameters.setMjRiseRatio(mjRiseRatio);
        responseParameters.setMjFallRatio(mjFallRatio);
        responseParameters.setAiicSellMinNum(aiicSellMinNum);
        responseParameters.setAiicSellMaxNum(aiicSellMaxNum);
        responseParameters.setAiicBuyMinNum(aiicBuyMinNum);
        responseParameters.setAiicBuyMaxNum(aiicBuyMaxNum);
        responseParameters.setMjSellMinNum(mjSellMinNum);
        responseParameters.setMjSellMaxNum(mjSellMaxNum);
        responseParameters.setMjBuyMinNum(mjBuyMinNum);
        responseParameters.setMjBuyMaxNum(mjBuyMaxNum);

        return Response.success(responseParameters);
    }


}

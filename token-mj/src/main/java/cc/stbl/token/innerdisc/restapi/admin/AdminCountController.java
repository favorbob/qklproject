package cc.stbl.token.innerdisc.restapi.admin;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.enums.ResourceTypeEnum;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.ResourceInfoService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.service.EthTradeRecordService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RequestMapping(value = {PathPrefix.ADMIN_PATH })
@RestController
@Api(description = "首页-管理后台")
public class AdminCountController {

    @Autowired
    private VrUserInfoService vrUserInfoService;

    @Autowired
    private EthTradeRecordService ethTradeRecordService;

    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    @Autowired
    private ResourceInfoService resourceInfoService;


    @RequestMapping(value = "/count",method = RequestMethod.GET)
    @ApiOperation("统计数-首页")
    public Response<List<RepsoneCount>> count(){
        List<RepsoneCount> result = new ArrayList<RepsoneCount>();

        result.add(new RepsoneCount(vrUserInfoService.findCount(new VrUserInfo()),"用户数",null));

        result.add(new RepsoneCount(ethTradeRecordService.findCount(new EthTradeRecord()),"交易数",null));

        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setResourceType(ResourceTypeEnum.VIDEO.getCode());
        result.add(new RepsoneCount(resourceInfoService.findCount(resourceInfo),"电影应用数",null));

        resourceInfo.setResourceType(ResourceTypeEnum.GAME.getCode());
        result.add(new RepsoneCount(resourceInfoService.findCount(resourceInfo),"游戏应用数",null));

        result.add(new RepsoneCount(0l,"首次发行币的剩余总资产",null));//待确定

        result.add(new RepsoneCount(0l,"当天释放总资产",null));//待确定
        return Response.success(result);
    }

    @RequestMapping(value = "/tradeCount",method = RequestMethod.GET)
    @ApiOperation("统计数-交易记录页面")
    public Response<List<RepsoneCount>> tradeCount(){
        List<RepsoneCount> result = new ArrayList<RepsoneCount>();
        Date currDate = new Date();//今日
        Date yesterday = DateUtils.addDays(currDate, -1);//昨日
        Date lately = DateUtils.addDays(currDate, -8);//前七日

        //交易流水
        EthTradeRecord ethTradeRecord = new EthTradeRecord();
        ethTradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_SUCCESS);

        result.add(new RepsoneCount(ethTradeRecordService.findSumTradeAmount(ethTradeRecord),"交易总流水",null));

        ethTradeRecord.setCreateDateByDay(currDate);
        result.add(new RepsoneCount(ethTradeRecordService.findCount(ethTradeRecord),"今日交易流水",null));

        ethTradeRecord.setCreateDateByDay(yesterday);
        result.add(new RepsoneCount(ethTradeRecordService.findCount(ethTradeRecord),"昨天交易流水",null));

        ethTradeRecord.setStartTradeDate(lately);
        ethTradeRecord.setEntTradeDate(currDate);
        result.add(new RepsoneCount(ethTradeRecordService.findCount(ethTradeRecord),"前七天交易流水",null));


        //交易资产
        EthAssetFlow ethAssetFlow = new EthAssetFlow();
        ethAssetFlow.setIsPlus(TradeConsts.TRADE_FLOW_BTYPE_INCOME);

        result.add(new RepsoneCount(ethAssetFlowService.findCount(ethAssetFlow),"交易总资产",null));

        ethAssetFlow.setCreateTime(currDate);
        result.add(new RepsoneCount(ethAssetFlowService.findCount(ethAssetFlow),"今日交易资产",null));

        ethAssetFlow.setCreateDateByDay(yesterday);
        result.add(new RepsoneCount(ethAssetFlowService.findCount(ethAssetFlow),"昨日交易资产",null));

        ethAssetFlow.setStartTradeDate(lately);
        ethAssetFlow.setEntTradeDate(currDate);
        result.add(new RepsoneCount(ethAssetFlowService.findCount(ethAssetFlow),"前七天交易资产",null));

        return Response.success(result);
    }

    @Api("管理后台-首页统计数据")
    @Data
    public static class RepsoneCount{
        @ApiModelProperty("统计数")
        private Long count;
        @ApiModelProperty("统计名")
        private String name;
        @ApiModelProperty("地址")
        private String link;

        public RepsoneCount(Long count, String name, String link) {
            this.count = count;
            this.name = name;
            this.link = link;
        }
    }

}

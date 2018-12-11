package cc.stbl.token.innerdisc.restapi.admin.trades;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.modules.eth.service.EthTradeRecordService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping(value = {PathPrefix.ADMIN_PATH + "/trades/vrToken"})
@RestController
@Api(description = "内盘-交易记录管理")
public class EthTradeRecordController {

    @Autowired
    private EthTradeRecordService tradeRecordService;

    @RequestMapping(value = {"/getTradeRecordList"},method = {RequestMethod.POST})
    @ApiOperation("列表 天宇")
    public Response<Pager<EthTradeRecordProto.ResponseGetTradeRecords>> getTradeRecordList(@RequestBody EthTradeRecordProto.RequestListCondition condition){
        return Response.success(tradeRecordService.getPager(condition));
    }

    @RequestMapping(value = {"/getDetail"},method = {RequestMethod.POST})
    @ApiOperation("详情 天宇")
    public Response<List<EthTradeRecordProto.ResponseDetail>> getDetail(@RequestBody EthTradeRecordProto.RequestDetail condition){
        return Response.success(tradeRecordService.getDetail(condition.getId()));
    }

}

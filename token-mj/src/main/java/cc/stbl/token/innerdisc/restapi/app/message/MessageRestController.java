package cc.stbl.token.innerdisc.restapi.app.message;


import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.message.entity.MessageAccepter;
import cc.stbl.token.innerdisc.modules.message.service.MessageAccepterService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = {PathPrefix.API_PATH + "/message"})
@Api(description = "消息@唐昱轩")
public class MessageRestController {

    @Autowired
    private MessageAccepterService messageAccepterService;

    //发送消息
    @ApiOperation("发送消息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "accepterId",value = "消息接收人ID",required = true,dataType = "String"),
//            @ApiImplicitParam(name = "assetsId",value = "资产消息Id",required = true,dataType = "String")
//    })
    @RequestMapping(value = {"/sendMessage"},method = RequestMethod.POST)
    @ResponseBody
    public Response sendMessage(@RequestBody @Valid MessageProto.MessageRequestSupports request){
        String userId = ShiroUtils.getLoginUserId();
        return messageAccepterService.sendMessage(request,userId);
    }


    @ApiOperation("获取消息列表")
    @RequestMapping(value = {"/getMessageList"},method = RequestMethod.GET)
    @ResponseBody
    public Response<List<MessageProto.ResponseMessage>> getMessageList(){
        String userId = ShiroUtils.getLoginUserId();
        return messageAccepterService.getMessageList(userId);
    }

    @ApiOperation("获取消息详情")
    @RequestMapping(value = {"/messageDetail"},method = RequestMethod.POST)
    @ResponseBody
    public Response<MessageProto.ResponseMessage> messageDetail(@RequestBody @Valid MessageProto.MessageId request){
        return messageAccepterService.messageDetail(request.getId());
    }

    @ApiOperation("改变消息读取状态")
    @RequestMapping(value = {"/changeMessageStatus"},method = RequestMethod.POST)
    @ResponseBody
    public Response changeMessageStatus(@RequestBody @Valid MessageProto.MessageId request){
        return messageAccepterService.changeMessageStatus(request.getId());
    }

    @PostMapping("messageType")
    @ApiOperation(value = "极光推送消息参数",notes =
            "买入资产{'cmd':'BUYASSET'}<br/>" +
            "卖出资产{'cmd':'SELLASSET'<br/>" +
            "撤销资产{'cmd':'CANCELASSET'<br/>" +
            "求购资产{'cmd':'APPLYLINKEDBUY'},<br/>" +
            "......<br/>")
    public String messageType(){
        return null;
    }
}

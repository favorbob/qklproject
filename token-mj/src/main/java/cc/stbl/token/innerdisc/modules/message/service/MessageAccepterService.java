/**
* generator by mybatis plugin Thu Aug 23 21:17:46 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.message.service;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.message.dao.MessageAccepterMapper;
import cc.stbl.token.innerdisc.modules.message.enm.MessageEnm;
import cc.stbl.token.innerdisc.modules.message.entity.MessageAccepter;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.app.message.MessageProto;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MessageAccepterService extends DataStoreServiceImpl<String, MessageAccepter, MessageAccepterMapper> {

    @Autowired
    private VrUserInfoService userInfoService;

    //发送消息
    public Response sendMessage(MessageProto.MessageRequestSupports request,String userId){
        MessageAccepter messageAccepter = new MessageAccepter();
        VrUserInfo vrUserInfo = userInfoService.get(request.getAccepterId());
        VrUserInfo createByInfo = userInfoService.get(userId);
        String accepter = vrUserInfo.getUserName();
        messageAccepter.setId(UUID.randomUUID().toString().replace("-",""));
        messageAccepter.setCreateBy(userId);
        messageAccepter.setAccepterName(accepter);
        messageAccepter.setAccepterId(vrUserInfo.getUserId());
        messageAccepter.setSendDate(new Date());
        messageAccepter.setCreateDate(new Date());
        messageAccepter.setStatus(MessageEnm.MESSAGE_SEND_SUCCESS.getCode());
        messageAccepter.setCheckStatus(MessageEnm.MESSAGE_UNREAD.getCode());
        messageAccepter.setTitle(createByInfo.getUserName() + ",资产已支付,请确认收到钱。");
        messageAccepter.setContent(createByInfo.getUserName() + ",资产已支付,请确认收到钱。");
        messageAccepter.setMessageType(MessageEnm.MESSAGE_ASSETS.getCode());
        messageAccepter.setExtendId(request.getExtendId());
        try {
            this.add(messageAccepter);
            return Response.success("消息发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw new StructWithCodeException(ResponseCode.MESSAGE_SEND_ERROR);
        }
    }

    //获取消息列表
    public Response<List<MessageProto.ResponseMessage>> getMessageList(String userId){
        MessageAccepter messageAccepter = new MessageAccepter();
        List<MessageProto.ResponseMessage> messageList = new ArrayList<>();
        messageAccepter.setAccepterId(userId);
        messageAccepter.desc("createDate");
        List<MessageAccepter> list = this.findList(messageAccepter);
        for (MessageAccepter m : list){
            MessageProto.ResponseMessage message = new MessageProto.ResponseMessage();
            BeanUtils.copyProperties(m,message);
            messageList.add(message);
        }
        return Response.success(messageList);
    }

    //获取消息详情
    public Response<MessageProto.ResponseMessage > messageDetail(String id){
        MessageProto.ResponseMessage responseMessage = new MessageProto.ResponseMessage();
        MessageAccepter messageAccepter = this.get(id);
        BeanUtils.copyProperties(messageAccepter,responseMessage);
        return Response.success(responseMessage);
    }

    //改变消息读取状态
    public Response changeMessageStatus(String id){
        MessageAccepter messageAccepter = this.get(id);
        messageAccepter.setCheckStatus(0);
        this.update(messageAccepter);
        return Response.success("改变消息读取状态成功");
    }

}
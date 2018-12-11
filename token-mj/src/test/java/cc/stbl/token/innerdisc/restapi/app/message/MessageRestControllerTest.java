package cc.stbl.token.innerdisc.restapi.app.message;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.message.service.MessageAccepterService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageRestControllerTest extends AbstractTestCase {

    @Autowired
    private MessageAccepterService messageAccepterService;

    @Test
    public void sendMessageTest(){
        MessageProto.MessageRequestSupports requestSupports = new MessageProto.MessageRequestSupports();
        requestSupports.setAccepterId("dade8789e1fd4b28adecb39ba1dd5da3");
        requestSupports.setExtendId("123456");
        String userId = "a8c89810cc134412ab6267617f4c3a1b";
        messageAccepterService.sendMessage(requestSupports,userId);
    }

    @Test
    public void getMessageListTest(){
        String userId = "dade8789e1fd4b28adecb39ba1dd5da3";
        Response messageList = messageAccepterService.getMessageList(userId);
        System.out.println(JSON.toJSON(messageList.getData()));
    }

    @Test
    public void messageDetailTest(){
        String id = "75628f0710994abe8a499e69ef73db75";
        Response response = messageAccepterService.messageDetail(id);
        System.out.println(JSON.toJSON(response.getData()));
    }

}

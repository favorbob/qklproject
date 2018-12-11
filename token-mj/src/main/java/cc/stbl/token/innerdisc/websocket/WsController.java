package cc.stbl.token.innerdisc.websocket;

import java.math.BigDecimal;
import java.util.Random;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import cc.stbl.token.innerdisc.restapi.app.user.UserProto;

/**
 * websocket 处理类
 * 查看demo http://aiic.fun/ws/ws.html?abc
 *   其中abc为用户id，可以随意更新
 */
@Controller
public class WsController {

	@MessageMapping("/wsRequest")
	public void wsRequest(@Valid WSRequest request) {
		log.debug("收到前端发来websocket请求：{}", JSON.toJSONString(request));
		
		try { // 模拟业务处理耗时，实际写业务代码时，该代码需要删除
			Thread.sleep(1000 + new Random().nextInt(3000));
		} catch (Exception e) {
		}

		// TODO 此处是实际业务逻辑

		// 根据userId、type，处理相关数据，数据处理完成后，推送信息给前端

		// 以下为模拟需要返回给前端的数据
		UserProto.ResponseSubInfo info = new UserProto.ResponseSubInfo();
		info.setUserId(request.getUserId());
		info.setContributedAssets(new BigDecimal("12.34"));
		info.setMobile("13112345678");
		info.setNickName("hello");

		WSResponse<UserProto.ResponseSubInfo> response = new WSResponse<>();
		response.setType(request.getType());
		response.setData(info);
		log.debug("向前端推送消息：{}", JSON.toJSONString(response));
		template.convertAndSendToUser(request.getUserId(), "/response", response);
	}

	@Autowired
	private SimpMessagingTemplate template;

	private Logger log = LoggerFactory.getLogger(this.getClass());
}

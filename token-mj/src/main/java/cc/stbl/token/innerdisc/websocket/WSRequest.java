package cc.stbl.token.innerdisc.websocket;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

public class WSRequest implements Serializable {

	private static final long serialVersionUID = -3600893930076531093L;
	
	@NotBlank(message = "userId不能为空")
	private String userId;
	
	@NotBlank(message = "type不能为空")
	private String type; // 0-激活 1-复投

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

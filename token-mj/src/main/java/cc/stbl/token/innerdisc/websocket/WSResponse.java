package cc.stbl.token.innerdisc.websocket;

import java.io.Serializable;

public class WSResponse<T> implements Serializable{

	private static final long serialVersionUID = -3890076296823737293L;
	// 交易类型 0-激活 1-复投
	private String type;

    //数据
    private T data;

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

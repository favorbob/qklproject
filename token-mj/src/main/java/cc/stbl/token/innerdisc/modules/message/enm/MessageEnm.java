package cc.stbl.token.innerdisc.modules.message.enm;

public enum MessageEnm {

    MESSAGE_READ(0,"已读"),
    MESSAGE_UNREAD(1,"未读"),

    //发送状态
    MESSAGE_SEND_SUCCESS(0,"发送成功"),
    MESSAGE_SEND_ERROR(0,"发送失败"),

    //消息类型
    MESSAGE_SYS(0,"系统消息"),
    MESSAGE_ASSETS(1,"资产消息"),

    //极光推送消息类型
    BUY_ASSET("BUYASSET","资产购买成功","资产已经到了您的账户，但处于冻结状态，等待对方确认收款，才能释放资产。"),//买入资产
    SELL_ASSET("SELLASSET","资产挂单成功","资产已经成功在挂单平台上卖出，等待买家购买。"),//卖出资产
    CANCEL_ASSET("CANCELASSET","资产撤销成功","您的资产已经在挂单平台上成功撤销，请注意查看资产是否到账。"),//撤销资产
    APPLY_LINKED_BUY("APPLYLINKEDBUY","资产求购成功","您的资产求购需求已经成功挂单在平台，请耐心等待卖家卖出。")//求购资产
    ;



    private Integer code;
    private String name;
    private String title;
    private String content;


    MessageEnm(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    MessageEnm(String name,String title,String content){
        this.name = name;
        this.title = title;
        this.content = content;
    }

    public Integer getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

}

package cc.stbl.token.innerdisc.modules.trades.entity;

import java.util.Date;

/**
* create by framework, create date 2018/09/21 09:41:52
*/
public class TwdOfflinePayedAuth {
    /**
    * 字段：twd_offline_payed_auth.id；备注：
     */
    private String id;

    /**
    * 字段：twd_offline_payed_auth.record_id；备注：交易记录id
     */
    private String recordId;

    /**
    * 字段：twd_offline_payed_auth.user_id；备注：用户id
     */
    private String userId;

    /**
    * 字段：twd_offline_payed_auth.linked_id；备注：挂单记录id
     */
    private String linkedId;

    /**
    * 字段：twd_offline_payed_auth.create_date；备注：创建时间
     */
    private Date createDate;

    /**
    * 字段：twd_offline_payed_auth.auth_url；备注：支付凭证url
     */
    private String authUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }
}
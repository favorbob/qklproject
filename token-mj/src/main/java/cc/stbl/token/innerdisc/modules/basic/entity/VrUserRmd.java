package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

public class VrUserRmd {
    private String id;

    private String userId;

    private String parentUserId;

    private Date registerDate;

    private Integer userLevel;

    private String inviteCode;

    private String parentInviteCode;

    private String seedUserId;

    private Integer rmdLevel;

    private Integer subordinates;

    private Date startRegisterDate;
    private Date endRegisterDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(String parentUserId) {
        this.parentUserId = parentUserId;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getStartRegisterDate() {
        return startRegisterDate;
    }

    public void setStartRegisterDate(Date startRegisterDate) {
        this.startRegisterDate = startRegisterDate;
    }

    public Date getEndRegisterDate() {
        return endRegisterDate;
    }

    public void setEndRegisterDate(Date endRegisterDate) {
        this.endRegisterDate = endRegisterDate;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getSeedUserId() {
        return seedUserId;
    }

    public void setSeedUserId(String seedUserId) {
        this.seedUserId = seedUserId;
    }

    public Integer getRmdLevel() {
        return rmdLevel;
    }

    public void setRmdLevel(Integer rmdLevel) {
        this.rmdLevel = rmdLevel;
    }

    public Integer getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Integer subordinates) {
        this.subordinates = subordinates;
    }

    public String getParentInviteCode() {
        return parentInviteCode;
    }

    public void setParentInviteCode(String parentInviteCode) {
        this.parentInviteCode = parentInviteCode;
    }
}
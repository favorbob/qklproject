package cc.stbl.token.innerdisc.modules.basic.entity;

/**
* create by framework, create date 2018/08/28 11:37:44
*/
public class VrUserCount {
    /**
    * 字段：vr_user_count.user_id；备注：用户id
     */
    private String userId;

    /**
    * 字段：vr_user_count.user_level1；备注：1级人数
     */
    private Integer userLevel1;

    /**
    * 字段：vr_user_count.user_level2；备注：2级人数
     */
    private Integer userLevel2;

    /**
    * 字段：vr_user_count.user_levelN；备注：n级人数
     */
    private Integer userLeveln;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUserLevel1() {
        return userLevel1;
    }

    public void setUserLevel1(Integer userLevel1) {
        this.userLevel1 = userLevel1;
    }

    public Integer getUserLevel2() {
        return userLevel2;
    }

    public void setUserLevel2(Integer userLevel2) {
        this.userLevel2 = userLevel2;
    }

    public Integer getUserLeveln() {
        return userLeveln;
    }

    public void setUserLeveln(Integer userLeveln) {
        this.userLeveln = userLeveln;
    }
}
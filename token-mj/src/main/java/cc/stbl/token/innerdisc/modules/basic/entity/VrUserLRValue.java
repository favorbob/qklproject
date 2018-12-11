package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

/**
* create by framework, create date 2018/10/10 16:10:20
*/
public class VrUserLRValue {
    /**
    * 字段：vr_user_lrvalue.user_id；备注：用户id
     */
    private String userId;

    /**
    * 字段：vr_user_lrvalue.pid；备注：上级id
     */
    private String pid;

    /**
    * 字段：vr_user_lrvalue.lft；备注：左值
     */
    private Integer lft;

    /**
    * 字段：vr_user_lrvalue.rgt；备注：右值
     */
    private Integer rgt;

    /**
    * 字段：vr_user_lrvalue.user_level；备注：用户层级，顶级用户为1
     */
    private Integer userLevel;

    /**
    * 字段：vr_user_lrvalue.area；备注：所在区域，目前为 A或B
     */
    private String area;

    /**
    * 字段：vr_user_lrvalue.tree_id；备注：用户所在树标识
     */
    private String treeId;

    private Integer status;
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
    * 字段：vr_user_lrvalue.create_date；备注：
     */
    private Date createDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getLft() {
        return lft;
    }

    public void setLft(Integer lft) {
        this.lft = lft;
    }

    public Integer getRgt() {
        return rgt;
    }

    public void setRgt(Integer rgt) {
        this.rgt = rgt;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
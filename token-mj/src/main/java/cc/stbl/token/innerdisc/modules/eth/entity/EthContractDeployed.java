package cc.stbl.token.innerdisc.modules.eth.entity;

import java.util.Date;

public class EthContractDeployed {
    private String id;

    private Date deployDate;

    private String clazz;

    private String contractAt;

    private String deployOwner;

    private String deployArgs;

    private Long atBlockNumber;
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDeployDate() {
        return deployDate;
    }

    public void setDeployDate(Date deployDate) {
        this.deployDate = deployDate;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getContractAt() {
        return contractAt;
    }

    public void setContractAt(String contractAt) {
        this.contractAt = contractAt;
    }

    public String getDeployOwner() {
        return deployOwner;
    }

    public void setDeployOwner(String deployOwner) {
        this.deployOwner = deployOwner;
    }

    public String getDeployArgs() {
        return deployArgs;
    }

    public void setDeployArgs(String deployArgs) {
        this.deployArgs = deployArgs;
    }

    public Long getAtBlockNumber() {
        return atBlockNumber;
    }

    public void setAtBlockNumber(Long atBlockNumber) {
        this.atBlockNumber = atBlockNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
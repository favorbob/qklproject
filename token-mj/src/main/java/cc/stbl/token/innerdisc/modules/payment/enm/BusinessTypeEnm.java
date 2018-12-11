package cc.stbl.token.innerdisc.modules.payment.enm;

public enum  BusinessTypeEnm {
    VR_PAY("vrPay","支付"),
    VR_REFUND("vrRefund","退款"),
    VR_WITHDRAW("vrWithdraw","提现"),
    VR_RECHARGE("mallSkuOrder","充值"),
    VR_TRANSFER("vrTransfer","转账"),
    VR_LOCK("vrLock","冻结"),
    VR_UNLOCK("vrUnLock","解冻"),
    VR_DEDUCT("deductWallet","钱包扣钱"),
            ;
    private String businessType;
    private String name;

    BusinessTypeEnm(String businessType,String name){
        this.businessType = businessType;
        this.name = name;
    }

    public String getBusinessType(){
        return businessType;
    }

    public String getName(){
        return name;
    }
}

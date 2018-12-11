package cc.stbl.token.innerdisc.restapi.admin.trades;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CoinSettingProto {

    @Data
    public static class Settings {
        private CoinAttrs coinAttrs;
        @Valid
        private SysAttrs sysAttrs;
        @Valid
        private TradeAttrs tradeAttrs;
    }

    @Data
    public static class CoinAttrs{
        private String address; //合约地址
        private BigDecimal ratio; // 资产积分比例
        @Length(min = 136000000,max = 1000000000,message = "币总额介于{min}和{max}之间")
        private BigDecimal maxSupplyLimit; //币总额
        @Length(min = 10000000,max = 136000000,message = "首发数量介于{min}和{max}之间")
        private BigInteger initSupply; //首发量

        private String receiptCode;
    }

    @Data
    public static class SysAttrs{
//        private BigDecimal redPackRatio; // % 登录红包比例，按总资产返
        @NotNull(message = "积分比例不能为空")
        private BigDecimal integralRebateRatio; //积分释放比列 0.2 %
    }

    @Data
    public static class TradeAttrs {
        @NotNull(message = "平台当前售价不能为空")
        private BigDecimal sellerPrice; //
    }

    @Data
    public static class ImportData {
        @NotNull
        private String address;
        @NotNull
        private String batch;
        private Boolean mock = true;
    }

    @Data
    public static class ImportDataReport {
        private Integer totalSize = 0;
        private Integer importSize =0;
        private Integer successSize =0;
        private List<Object> messages = new ArrayList<>();
    }
}

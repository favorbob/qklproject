package cc.stbl.token.innerdisc.restapi;

import lombok.Data;

import javax.validation.constraints.NotNull;

public abstract class BaseProto {

    @Data
    public static abstract class RquestPager {
        @NotNull(message = "pageNo不能为空")
        protected Integer pageNo;
        @NotNull(message = "pageSize不能为空")
        protected Integer pageSize;
    }
}

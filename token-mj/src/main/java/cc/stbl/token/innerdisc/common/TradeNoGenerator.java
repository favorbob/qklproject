package cc.stbl.token.innerdisc.common;

import cc.stbl.token.innerdisc.common.generator.SnowFlakeGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


@Component
public class TradeNoGenerator implements InitializingBean{

    private SnowFlakeGenerator generator;

    public String get(){
        return String.valueOf(generator.nextId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        generator = new SnowFlakeGenerator(1L,1L);
    }
}

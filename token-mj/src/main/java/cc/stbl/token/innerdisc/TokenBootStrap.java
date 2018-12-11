package cc.stbl.token.innerdisc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cogent.cache.boot.SpringCacheRedisConfig;
import com.ks.common.datastore.dao.IDataStoreMapper;

import cc.stbl.token.innerdisc.common.shiro.ShiroConfiguration;
import cc.stbl.token.innerdisc.modules.scheduler.ElasticJobConfiguration;

@SpringBootApplication
@Import({ SpringCacheRedisConfig.class, ShiroConfiguration.class, ElasticJobConfiguration.class })
@MapperScan(basePackages = {"cc.stbl.token.innerdisc","com.stbl.payment"},markerInterface = IDataStoreMapper.class)
@EnableTransactionManagement(proxyTargetClass = true)
// @EnableScheduling
public class TokenBootStrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(TokenBootStrap.class)
                .web(true).build().run(args);
        context.registerShutdownHook();
    }
}

package cc.stbl.token.innerdisc.modules.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = { "classpath:job/application-context-elastic-job.xml" })
public class ElasticJobConfiguration {

}

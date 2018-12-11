package cc.stbl.token.innerdisc.common.push.inter;

import cc.stbl.token.innerdisc.common.push.inter.model.PushPayload;
import cc.stbl.token.innerdisc.common.push.inter.model.PushResult;
import cc.stbl.token.innerdisc.common.push.inter.model.config.ApplicationConfig;

import java.util.Collection;

/**
 * Created by Developer on 2017-4-11.
 */
public interface IPushService {

    PushResult push(PushPayload payload);

    ApplicationConfig getApplicationConfig(String application);

    Collection<ApplicationConfig> getAllApplicationConfigs();
}

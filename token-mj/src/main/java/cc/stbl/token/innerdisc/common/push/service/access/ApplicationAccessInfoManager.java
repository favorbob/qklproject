package cc.stbl.token.innerdisc.common.push.service.access;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Developer on 2017-4-13.
 */
@ConfigurationProperties()
@Component
public class ApplicationAccessInfoManager {

    public Map<String, ApplicationAccessInfo> getApplications() {
        return applications;
    }

    public void setApplications(Map<String, ApplicationAccessInfo> applications) {
        this.applications = applications;
    }

    private Map<String, ApplicationAccessInfo> applications = new HashMap<>();


}

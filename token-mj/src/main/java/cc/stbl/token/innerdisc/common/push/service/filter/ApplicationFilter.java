package cc.stbl.token.innerdisc.common.push.service.filter;

import cc.stbl.token.innerdisc.common.push.service.channel.AbstractPushClientWrapper;

import java.util.Collection;

/**
 * Created by Developer on 2017-4-13.
 */
public class ApplicationFilter implements Filter<AbstractPushClientWrapper> {

    private String application;

    public ApplicationFilter(String application){
        this.application = application;
    }

    @Override
    public Collection<AbstractPushClientWrapper> meetCriteria(Collection<AbstractPushClientWrapper> sources) {
        return null;
    }
}

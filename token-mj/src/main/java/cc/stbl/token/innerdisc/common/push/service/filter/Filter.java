package cc.stbl.token.innerdisc.common.push.service.filter;

import java.util.Collection;

public interface Filter<T> {
    public Collection<T> meetCriteria(Collection<T> sources);
}
package cc.stbl.token.innerdisc.common.push.inter.model.application;

import cc.stbl.token.innerdisc.common.push.inter.model.Preconditions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Application {

    private boolean all;
    private Set<String> values;

    private Application(){

    };
    private Application(boolean all, Set<String> values) {
        this.all = all;
        this.values = values;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Application all() {
        return newBuilder().setAll(true).build();
    }



    public boolean isAll() {
        return all;
    }

    public Set<String> getValues() {
        return this.values;
    }

    public static class Builder {
        private boolean all;
        private Set<String> values;

        public Builder setAll(boolean all) {
            this.all = all;
            return this;
        }

        public Builder addValue(String value) {
            if (null == values) {
                values = new HashSet<String>();
            }
            values.add(value);
            return this;
        }

        public Builder addValues(Collection<String> values) {
            if (null == this.values) {
                this.values = new HashSet<String>();
            }
            this.values.addAll(values);
            return this;
        }
        
        public Application build() {
            Preconditions.checkArgument(! (all && null != values), "Since all is enabled, any application should not be set.");
            Preconditions.checkArgument(! (!all && null == values), "No any application is set.");
            return new Application(all, values);
        }
    }
    
}



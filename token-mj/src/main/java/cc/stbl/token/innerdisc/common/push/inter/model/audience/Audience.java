package cc.stbl.token.innerdisc.common.push.inter.model.audience;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Audience {

    private boolean all;
    private Set<AudienceTarget> targets;

    private Audience(){

    }

    private Audience(boolean all, Set<AudienceTarget> targets) {
        this.all = all;
        this.targets = targets;
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static Audience all() {
        return newBuilder().setAll(true).build();
    }
    
    public static Audience tag(String... tagValue) {
        AudienceTarget target = AudienceTarget.newBuilder()
                .setAudienceType(AudienceType.TAG)
                .addAudienceTargetValues(tagValue).build();
        return newBuilder().addAudienceTarget(target).build();
    }
    
    public static Audience tag(Collection<String> tagValues) {
        AudienceTarget target = AudienceTarget.newBuilder()
                .setAudienceType(AudienceType.TAG)
                .addAudienceTargetValues(tagValues).build();
        return newBuilder().addAudienceTarget(target).build();
    }
    
    public static Audience tag_and(String... tagValue) {
        AudienceTarget target = AudienceTarget.newBuilder()
                .setAudienceType(AudienceType.TAG_AND)
                .addAudienceTargetValues(tagValue).build();
        return newBuilder().addAudienceTarget(target).build();
    }
    
    public static Audience tag_and(Collection<String> tagValues) {
        AudienceTarget target = AudienceTarget.newBuilder()
                .setAudienceType(AudienceType.TAG_AND)
                .addAudienceTargetValues(tagValues).build();
        return newBuilder().addAudienceTarget(target).build();
    }
    
    public static Audience alias(String... alias) {
        AudienceTarget target = AudienceTarget.newBuilder()
                .setAudienceType(AudienceType.ALIAS)
                .addAudienceTargetValues(alias).build();
        return newBuilder().addAudienceTarget(target).build();
    }
    
    public static Audience alias(Collection<String> aliases) {
        AudienceTarget target = AudienceTarget.newBuilder()
                .setAudienceType(AudienceType.ALIAS)
                .addAudienceTargetValues(aliases).build();
        return newBuilder().addAudienceTarget(target).build();
    }

    
    public boolean isAll() {
        return this.all;
    }

    public Set<AudienceTarget> getTargets() {
        return targets;
    }

    public static class Builder {
        private boolean all = false;
        private Set<AudienceTarget> audienceBuilder = null;
        
        public Builder setAll(boolean all) {
            this.all = all;
            return this;
        }
        
        public Builder addAudienceTarget(AudienceTarget target) {
            if (null == audienceBuilder) {
                audienceBuilder = new HashSet<AudienceTarget>();
            }
            audienceBuilder.add(target);
            return this;
        }
        
        public Audience build() {
            Preconditions.checkArgument(! (all && null != audienceBuilder), "If audience is all, no any other audience may be set.");
            Preconditions.checkArgument(! (all == false && null == audienceBuilder), "No any audience target is set.");
            return new Audience(all, audienceBuilder);
        }
    }
    
}



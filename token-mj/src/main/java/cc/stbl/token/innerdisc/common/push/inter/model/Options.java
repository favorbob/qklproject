package cc.stbl.token.innerdisc.common.push.inter.model;


import java.util.Random;

public class Options {

    private static final long NONE_TIME_TO_LIVE = -1;
    
    private int sendno;
    private long overrideMsgId;
    private long timeToLive;
    private boolean apnsProduction;
    private int bigPushDuration;	// minutes

    private Options(){

    }

    private Options(int sendno, long overrideMsgId, long timeToLive, boolean apnsProduction, 
    		int bigPushDuration) {
        this.sendno = sendno;
        this.overrideMsgId = overrideMsgId;
        this.timeToLive = timeToLive;
        this.apnsProduction = apnsProduction;
        this.bigPushDuration = bigPushDuration;
    }

    public long getOverrideMsgId() {
        return overrideMsgId;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public boolean isApnsProduction() {
        return apnsProduction;
    }

    public int getBigPushDuration() {
        return bigPushDuration;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static Options sendno() {
        return newBuilder().setSendno(generateSendno()).build();
    }
    
    public static Options sendno(int sendno) {
        return newBuilder().setSendno(sendno).build();
    }
    
    public void setApnsProduction(boolean apnsProduction) {
        this.apnsProduction = apnsProduction;
    }
    
    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }
    
    public void setBigPushDuration(int bigPushDuration) {
    	this.bigPushDuration = bigPushDuration;
    }
    
    public int getSendno() {
        return this.sendno;
    }

    public static class Builder {
        private int sendno = 0;
        private long overrideMsgId = 0;
        private long timeToLive = NONE_TIME_TO_LIVE;
        private boolean apnsProduction = false;
        private int bigPushDuration = 0;
        
        public Builder setSendno(int sendno) {
            this.sendno = sendno;
            return this;
        }
        
        public Builder setOverrideMsgId(long overrideMsgId) {
            this.overrideMsgId = overrideMsgId;
            return this;
        }
        
        public Builder setTimeToLive(long timeToLive) {
            this.timeToLive = timeToLive;
            return this;
        }
        
        public Builder setApnsProduction(boolean apnsProduction) {
            this.apnsProduction = apnsProduction;
            return this;
        }
        
        public Builder setBigPushDuration(int bigPushDuration) {
        	this.bigPushDuration = bigPushDuration;
        	return this;
        }
        
        public Options build() {
            Preconditions.checkArgument(sendno >= 0, "sendno should be greater than 0.");
            Preconditions.checkArgument(overrideMsgId >= 0, "override_msg_id should be greater than 0.");
            Preconditions.checkArgument(timeToLive >= NONE_TIME_TO_LIVE, "time_to_live should be greater than 0.");
            Preconditions.checkArgument(bigPushDuration >= 0, "bigPushDuration should be greater than 0.");
            if (sendno <= 0) {
                sendno = generateSendno();
            }
            
            return new Options(sendno, overrideMsgId, timeToLive, apnsProduction, bigPushDuration);
        }
    }


    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static int generateSendno() {
        return RANDOM.nextInt(2147383648) + 100000;
    }
}

package cc.stbl.token.innerdisc.common;

import java.util.UUID;

public interface JavaUUIDGenerator {
    static String get(){
        return UUID.randomUUID().toString().replace("-","");
    }
}

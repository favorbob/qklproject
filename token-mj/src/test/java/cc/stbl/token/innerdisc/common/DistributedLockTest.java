package cc.stbl.token.innerdisc.common;

import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cogent.cache.lock.DistributedLock;

public class DistributedLockTest extends AbstractTestCase {

	@Autowired
	DistributedLock lock;
	
	@Test
	public void tryLockTest(){
		String lockId = "lrvalue";
		String requestId = UUID.randomUUID().toString();
		int expireTime = 3600;
		boolean flag = lock.tryLock(lockId, requestId, expireTime);
		System.out.println(flag);
	}
	
	@Test
	public void unLockTest(){
		String lockId = "lrvalue";
		String requestId = "310f7a76-6f02-4897-beb9-02678fd0e655";
		boolean flag = lock.unLock(lockId, requestId);
		System.out.println(flag);
	}
	
	
}

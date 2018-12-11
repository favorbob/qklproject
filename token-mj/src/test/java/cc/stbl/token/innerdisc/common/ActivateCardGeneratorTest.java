package cc.stbl.token.innerdisc.common;

import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivateCardGeneratorTest extends AbstractTestCase{

	@Autowired
	ActivateCardGenerator generator;
	
	@Test
	public void testGet(){
		Set<String> set = generator.get(ActivateCardGenerator.ActivateCardType.GS, 10);
		set.forEach(System.out::println);
		
		//校验激活码是否合法
		set.forEach(str ->{
			System.out.println("ActivateCardGenerator.verifyCode("+str+"):"+ActivateCardGenerator.verifyCode(str));
		});
	}
}

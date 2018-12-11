package cc.stbl.token.innerdisc.modules.basic.service;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.basic.entity.VrPrizeDetail;

public class VrPrizeDetailServiceTest extends AbstractTestCase {

	String id = "VrPrizeDetailtest";

	@Autowired
	private VrPrizeDetailService service;

	@Test
	public void testBefore() {
		service.delete(id);
	}

	@Test
	public void testInsert() {
		VrPrizeDetail detail = new VrPrizeDetail();
		detail.setId(id);
		detail.setUserId("abc123");
		detail.setCreateDate(LocalDate.now().toString());
		service.add(detail);
	}

	@Test
	public void testSelect() {
		VrPrizeDetail detail = service.get(id);
		System.out.println("=======detail====" + JSON.toJSONString(detail));
	}

}

package cc.stbl.token.innerdisc.modules.basic.service;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValue;

public class VrUserLRValueServiceTest extends AbstractTestCase {

	@Autowired
	VrUserLRValueService service;
	
	@Autowired
    private VrUserLRValueRecordService vrUserLRValueRecordService;
	
	@Test
	public void selectAllZeroStatus(){
		String startDate = LocalDate.now().plusDays(-100).toString();
		vrUserLRValueRecordService.selectAllZeroStatus(startDate);
	}

	@Test
	public void setLRValue() {
		String startDate = LocalDate.now().plusDays(-100).toString();
		List<VrUserLRValue> list = service.getNeedSetList(startDate);
		System.out.println("===list:"+list);
		list.forEach(lr -> {
			System.out.println("======getNeedSetList======" + JSON.toJSONString(lr));
			service.setLRValue(lr);
		});
	}

	@Test
	public void subList() {
		//返回 id为7b883c92c41c48bdac89a4377d7db639，及其往下二层的用户信息
		List<VrUserLRValue> list = service.getSubList("7b883c92c41c48bdac89a4377d7db639", 2);
		list.forEach(lr -> {
			System.out.println("=======subList=====" + JSON.toJSONString(lr));
		});
		
		//返回 id为7b883c92c41c48bdac89a4377d7db639，及其往下二层且用户状态为1或2的用户信息
		list = service.getSubList("7b883c92c41c48bdac89a4377d7db639", 2, 1, 2);
		list.forEach(lr -> {
			System.out.println("=======subList==status===" + JSON.toJSONString(lr));
		});
	}
	
	@Test
	public void parentList(){
		String userId = "94c5fb4a564449e5aecd98109d18f6ff";
//		String userId = "88bc1c3c8da54d3985791f4c66d09da1";
		List<VrUserLRValue> allParentList = service.getAllParentList(userId);
		allParentList.forEach(lr -> System.out.println("====allParentList=========" + JSON.toJSONString(lr)));
		
		//查询用户状态为1的上级列表
		allParentList = service.getAllParentList(userId,1);
		allParentList.forEach(lr -> System.out.println("====allParentList=====status====" + JSON.toJSONString(lr)));
		
		List<VrUserLRValue> parentList = service.getParentList(userId,1);
		parentList.forEach(lr -> System.out.println("====parentList=========" + JSON.toJSONString(lr)));
		
		//查询用户状态为1、2的直接上级列表
		parentList = service.getParentList(userId,1,1,2);
		parentList.forEach(lr -> System.out.println("====parentList=====status====" + JSON.toJSONString(lr)));
	}
	
	@Test
	public void prize(){
		List<VrUserLRValue> list = service.getPrizeUserList(2, LocalDate.now().toString());
		list.forEach(lr -> {
			System.out.println("=====prize=======" + JSON.toJSONString(lr));
		});
	}
	
	@Test
	public void save(){
		VrUserLRValue lr = new VrUserLRValue();
		lr.setUserId("abcdef");
		lr.setPid("88bc1c3c8da54d3985791f4c66d09da1");
		lr.setArea("B");
		service.add(lr);
	}
	
	@Test
	public void updateStatus(){
		VrUserLRValue lr = new VrUserLRValue();
		lr.setUserId("abcdef");
		lr.setStatus(1);
		service.update(lr);
	}
	
	@Test
	public void update(){
		VrUserLRValue lr = new VrUserLRValue();
		lr.setUserId("abcdef");
		lr.setLft(1);
		lr.setRgt(2);
		lr.setUserLevel(21);
		lr.setTreeId("123:x");
		service.update(lr);
	}
/*	
	@Test
	public void batch(){
		service.batch();
	}
	
	
	@Test
	public void set(){
		long t1 = System.currentTimeMillis();
		
		service.set();
		
		System.out.println("==total time:" + (System.currentTimeMillis() - t1));
	}*/
}

/**
* generator by mybatis plugin Wed Oct 10 16:10:20 CST 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.modules.basic.dao.VrUserLRValueRecordMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValue;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValueRecord;

@Service
public class VrUserLRValueRecordService extends DataStoreServiceImpl<String, VrUserLRValueRecord, VrUserLRValueRecordMapper> {

	

	@Autowired
	private VrUserLRValueService service;
	@Autowired
	private VrUserLRValueRecordService serviceRecord;
	
	@Autowired
	private VrUserInfoService vrUserInfoService;

    @Value("${spring.datasource.batchSize}")
    private int batchSize;
	
	/*
	 * 查询所有状态为0的实体
	 */
	public List<VrUserLRValueRecord> selectAllZeroStatus(String startDate) {
		return mapper.selectAllZeroStatus(startDate);
	}
	
	@Transactional
	public void handleLRArea(VrUserLRValueRecord  v) {
		//获取area和业绩
		String userId = v.getUserId();
		VrUserLRValue vulr = service.get(userId);
		String area = vulr.getArea();
		Long addValue = new Long(v.getAddValue());

		//获取上级用户list
		List<VrUserLRValue> allParentList = service.getAllParentList(userId,0,1,2);
		List<VrUserInfo> vrUserInfoList = new ArrayList<VrUserInfo>();
//		VrUserInfo vrUserInfoDB = vrUserInfoService.get(userId);
//		String registerPhoneNum = vrUserInfoDB.getRegisterPhoneNum();
//		VrUserInfo registerUser = vrUserInfoService.findByPhoneNum(registerPhoneNum);
//		String registerUserId = registerUser.getUserId();
//		
//		//根据flag获取用户id
//		int flag = 0;
//		for(int i=0;i<allParentList.size();i++) {
//			VrUserLRValue vrUserLRValue = allParentList.get(i);
//			String tempUserId = vrUserLRValue.getUserId();
//			if(tempUserId.equals(registerUserId)) {
//				flag = i+1;
//				break;
//			}
//		}
		
		//获取需要更新的用户list
		for(int i=0;i<allParentList.size();i++) {
			VrUserLRValue vrUserLRValue = allParentList.get(i);
			VrUserInfo vrUserInfo = new VrUserInfo();
			vrUserInfo.setUserId(vrUserLRValue.getUserId());
			if(area.equals(VrUserInfo.USER_AAREA)) {
				vrUserInfo.setaArea(addValue);
				vrUserInfo.setbArea(0L);
			}else {
				vrUserInfo.setaArea(0L);
				vrUserInfo.setbArea(addValue);
			}
			area = vrUserLRValue.getArea();
			vrUserInfoList.add(vrUserInfo);
		}

		//更新业绩
		if(vrUserInfoList.size()>0) {
			//把用户表业绩更新
            List<List<VrUserInfo>> lists = splitList(vrUserInfoList, batchSize);
            lists.forEach(vrUserInfoService::updateABAreaByIds);
		}
		//把业绩记录表更新
		VrUserLRValueRecord vNew = new VrUserLRValueRecord();
		vNew.setId(v.getId());
		vNew.setStatus(VrUserLRValueRecord.STATUS_1);
		serviceRecord.update(vNew);
	}
}
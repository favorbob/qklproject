/**
* caojinbo
 * 中心管理 首页图片管理
**/
package cc.stbl.token.innerdisc.modules.centerManager.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.centerManager.dao.CenterEditServiceMapper;
import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterEditService;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CenterEditServiceService extends DataStoreServiceImpl<String, CenterEditService, CenterEditServiceMapper> {

    //查询客服编辑list
    public List<CenterEditService> selectEditServiceAndPicsResult() {
        return mapper.selectEditServiceAndPicsResult();
    }

    //删除客服编辑信息
    public void deleteEditService(String id) {
        super.delete(id);    //通过主键删除数据
    }

}
/**
* caojinbo
**/
package cc.stbl.token.innerdisc.modules.sys.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.common.shiro.service.UserRolePermission;
import cc.stbl.token.innerdisc.modules.sys.dao.SysLogMapper;
import cc.stbl.token.innerdisc.modules.sys.entity.SysLog;
import cc.stbl.token.innerdisc.modules.sys.entity.SysMenu;
import cc.stbl.token.innerdisc.modules.sys.entity.SysRole;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysLogService extends DataStoreServiceImpl<String, SysLog, SysLogMapper> {

    //获取查询的日志信息-分页
    public Pager<SysLog> getSysLogInfoPage(SysLog query, Integer pageNo, Integer pageSize) {

        Long total = mapper.findSysLogCount(query);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<>(pageNo, pageSize, new ArrayList<SysLog>(), total);
        }
        List<SysLog> list = mapper.findSysLogList(query,new RowBounds(offset,pageSize));
        return new Pager<>(pageNo, pageSize, list, total);
    }

    public void addSysLog(SysLog sysLog) {
        sysLog.setId(JavaUUIDGenerator.get()); //id
        sysLog.setCreateDate(new Date());      //时间
        super.add(sysLog); //保存日志
    }
    public static void main(String args[]) {
        System.out.println(JavaUUIDGenerator.get());
    }

}
/**
* caojinbo
 * 中心管理 首页图片管理
**/
package cc.stbl.token.innerdisc.modules.centerManager.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.centerManager.dao.CenterEditServicePicMapper;
import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterEditServicePic;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.FileUtil;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CenterEditServicePicService extends DataStoreServiceImpl<String, CenterEditServicePic, CenterEditServicePicMapper> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private final static String COMMON_PATH = "/mnt/public/";  //服务文件上传、下载基础目录

    //保存客服图片信息
    public void addServicePic(CenterEditServicePic servicePic) {
        servicePic.setId(JavaUUIDGenerator.get()); //id
        servicePic.setCreateDate(new Date());      //时间

        super.add(servicePic); //保存客服图片信息
    }

    //删除客服图片信息
    public void deleteServicePic(String picId) {
        CenterEditServicePic servicePic = super.get(picId);
        String picurl = servicePic.getPicUrl();
        String msg = "";
        //删除文件
        if( picurl.startsWith(COMMON_PATH) ) {
            msg = FileUtil.deleteFileByPath(picurl) ;
        } else  {
           String path = COMMON_PATH.substring(0, COMMON_PATH.length()-1) + picurl ;
            msg = FileUtil.deleteFileByPath(path);
        }
        logger.info("删除客服图片操作" + msg);

        super.delete(picId);    //通过主键删除数据
    }

    /**
     * 根据客服标题id，查询客服图片分页集合
     * @param query
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<CenterEditServicePic> findPageByServiceId(CenterEditServicePic query, Integer pageNo, Integer pageSize) {
        Long total = this.mapper.findCountByServiceId(query);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<CenterEditServicePic>(pageNo,pageSize,new ArrayList<CenterEditServicePic>(),total);
        }
        RowBounds rowBounds = new RowBounds(offset,pageSize) ;
        List<CenterEditServicePic> list = this.mapper.findListByServiceId(query, rowBounds); //根据客服标题id，查询客服list
        return new Pager<CenterEditServicePic>(pageNo,pageSize,list,total);
    }
}
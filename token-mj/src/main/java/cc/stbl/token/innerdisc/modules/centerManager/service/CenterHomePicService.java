/**
* caojinbo
 * 中心管理 首页图片管理
**/
package cc.stbl.token.innerdisc.modules.centerManager.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.centerManager.dao.CenterHomePicMapper;
import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterHomePic;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.FileUtil;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CenterHomePicService extends DataStoreServiceImpl<String, CenterHomePic, CenterHomePicMapper> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private final static String COMMON_PATH = "/mnt/public/";  //服务文件上传、下载基础目录

    //获取首页图片-分页
    public Pager<CenterHomePic> getSysLogInfoPage(CenterHomePic query, Integer pageNo, Integer pageSize) {

        Long total = mapper.findCount(query);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<>(pageNo, pageSize, new ArrayList<CenterHomePic>(), total);
        }
        List<CenterHomePic> list = mapper.findList(query,new RowBounds(offset,pageSize));
        return new Pager<>(pageNo, pageSize, list, total);
    }

    //保存首页信息
    public void addHomePic(CenterHomePic homePic) {
        Long total = this.mapper.findCount(new CenterHomePic()) ;//查询记录数
        int sortNum = total.intValue()+ 1;
        homePic.setId(JavaUUIDGenerator.get()); //id
        homePic.setCreateDate(new Date());      //时间
        homePic.setPicSort(sortNum);        //设置排序值

        super.add(homePic); //保存s首页信息
    }

    //删除首页图片
    public void deleteHomePic(String picId, String picUrl) {
        CenterHomePic homePic = super.get(picId);
        String path = homePic.getPicUrl();
        String msg = "";
        //删除文件
        if( path.startsWith(COMMON_PATH) ) {
            msg = FileUtil.deleteFileByPath(path) ;
        } else  {
            String copath = COMMON_PATH.substring(0, COMMON_PATH.length()-1) + path ;
            msg = FileUtil.deleteFileByPath(copath);
        }
        logger.info("删除首页图片操作" + msg);

        super.delete(picId);    //删除数据
    }

    /**
     * 分页查询首页图片
     * @param query
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<CenterHomePic> findPageByquey(CenterHomePic query, Integer pageNo, Integer pageSize) {
        Long total = this.findCount(query);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<CenterHomePic>(pageNo,pageSize,new ArrayList<CenterHomePic>(),total);
        }
        List<CenterHomePic> list = this.findList(query,offset,pageSize);
        //URL路径去掉 /mnt/public
        String mnt_publ = "/mnt/public/";
        for(CenterHomePic hpic : list) {
            if( hpic.getPicUrl().startsWith(mnt_publ) ) {
                String picUrl = hpic.getPicUrl().substring(mnt_publ.length()-1) ;
                hpic.setPicUrl(picUrl); //url去掉 /mnt/public/
            }
        }
        return new Pager<CenterHomePic>(pageNo,pageSize,list,total);
    }

    /**
     * 根据id集合更新sort字段
     * @param ids id字符串
     * @param picSorts sort字符串
     * @return 返回更新结果
     */
    public String updatePicSortByIds(String ids, String picSorts ) {
        String msg = "";

        String[] hpicIds = ids.split(",");
        String[] hpicSorts = picSorts.split(",");

        if(hpicIds.length != hpicSorts.length) {
            msg = "更新首页图片排序失败，用户名id个数和sort个数不一致！";
            return msg;
        }
        List<CenterHomePic> homePics = new ArrayList<>();

        for(int i=0; i<hpicIds.length; i++) {
            CenterHomePic object = new CenterHomePic();
            object.setId(hpicIds[i]);
            object.setPicSort(Integer.valueOf(hpicSorts[i]));   //转为整型
            homePics.add(object);
        }
        int unum = this.mapper.batchUpdateSortByIds(homePics); //批量更新排序字段
        return  "更新首页图片排序成功，更新" + hpicIds.length +"条记录！";
    }

}
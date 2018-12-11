/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import com.ks.common.datastore.dao.IDataStoreMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface VrUserInfoMapper extends IDataStoreMapper<String, VrUserInfo> {
    List<VrUserInfo> findAllList();
    /**
     * 通过手机号码查询用户
     * @param phoneNum
     * @return
     */
    VrUserInfo findByPhoneNum(String phoneNum);
    
    /**
     * 获取下级用户
     * @param userCode
     * @param userCodeLevel
     * @return
     */
    List<VrUserInfo> findDownLevelUsresByUserCodeAndLevel(@Param("userCode") String userCode,@Param("userCodeLevel") int userCodeLevel);
    
    /**
     * 通过userCode userCodeLevel 找 对象
     * @param userCode
     * @param userCodeLevel
     * @return
     */
    VrUserInfo findByUserCodeAndLevel(@Param("userCode")String userCode,@Param("userCodeLevel")int userCodeLevel);

    /**
     * 更新a区或者b区的值
     * @param list
     */
    public void updateABAreaByIds(@Param("list")List<VrUserInfo>list);

	List<VrUserInfo> getPrizeUserList(@Param("minLevel") int minLevel, @Param("createDate") String createDate);

	List<VrUserInfo> getSubLevelUserList(@Param("userCode") String userCode, @Param("level") int level);

    Long findCountByName(String name);   //根据姓名模糊查询总数
    List<VrUserInfo> findListByName(String name, RowBounds rowBounds); //根据姓名模糊查询列表
    List<VrUserInfo> selectSubUser(@Param("pid")String pid);
    
    VrUserInfo selectParentByUserId(@Param("userId")String userId);
}
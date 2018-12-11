/**
* generator by mybatis plugin Tue Aug 28 11:37:44 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.modules.basic.dao.VrUserCountMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCount;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserRmd;
import com.alibaba.fastjson.JSON;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VrUserCountService extends DataStoreServiceImpl<String, VrUserCount, VrUserCountMapper> {

    @Autowired
    private VrUserInfoService vrUserInfoService;

    @Autowired
    private VrUserRmdService vrUserRmdService;

    public void timer(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,26);
        calendar.set(Calendar.SECOND,0);

        Date time = calendar.getTime();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("");
                selectInsertUserCount();

            }
        },time,1000 * 60 * 60 * 24);
    }


    //@Scheduled(cron = "0/5 * * * * ?")
    public void selectInsertUserCount(){
        List<VrUserInfo> userInfos = vrUserInfoService.findAllList();
        for (VrUserInfo userInfo : userInfos){
            logger.info("用户信息Id:" + JSON.toJSONString(userInfo));
            VrUserRmd vrUserRmd = new VrUserRmd();
            VrUserCount vrUserCount = new VrUserCount();
            vrUserCount.setUserId(userInfo.getUserId());
            vrUserRmd.setParentUserId(userInfo.getUserId());
            List<VrUserRmd> rmdList = vrUserRmdService.findList(vrUserRmd);
            //this.saveOrUpdate(vrUserCount);
            if (rmdList.size() == 0){
                vrUserCount.setUserLevel1(0);
                vrUserCount.setUserLevel2(0);
                vrUserCount.setUserLeveln(0);
                this.saveOrUpdate(vrUserCount);
            } else {
                statUser(vrUserRmd,vrUserCount);
            }
            this.saveOrUpdate(vrUserCount);
        }
    }


    public void statUser(VrUserRmd vrUserRmd,VrUserCount vrUserCount){
        Integer depth = 1;
        //VrUserCount vrUserCount = new VrUserCount();
        List<VrUserRmd> userRmds = vrUserRmdService.findList(vrUserRmd);
        vrUserCount.setUserLevel1(userRmds.size());
        for (VrUserRmd userRmd : userRmds) {
            //vrUserCount.setUserId(userRmd.getUserId());
            recursion(vrUserCount,userRmd,depth + 1);
        }
    }

    public void recursion(VrUserCount vrUserCount,VrUserRmd userRmd,Integer depth){
            int num = 0;
            VrUserRmd vrUserRmd = new VrUserRmd();
            vrUserRmd.setParentUserId(userRmd.getUserId());
            List<VrUserRmd> userRmds = vrUserRmdService.findList(vrUserRmd);
            if (userRmds.size() == 0){
//                vrUserCount.setUserLevel2(0);
//                vrUserCount.setUserLeveln(0);
//                this.saveOrUpdate(vrUserCount);
                return;
            }else {
                if(depth == 2){
                    vrUserCount.setUserLevel2(userRmds.size());
                    for (VrUserRmd rmd : userRmds) {
                        VrUserRmd v = new VrUserRmd();
                        v.setParentUserId(rmd.getUserId());
                        List<VrUserRmd> us = vrUserRmdService.findList(v);
                        if (us.size() == 0){
                            //this.saveOrUpdate(vrUserCount);
                            return;
                        }else {
                            VrUserRmd vm = new VrUserRmd();
                            vm.setUserId(rmd.getUserId());
                            recursion(vrUserCount,vm,depth + 1);
                        }
                    }
                }else {
                    num += userRmds.size();
                    vrUserCount.setUserLeveln(num);
                    //this.saveOrUpdate(vrUserCount);
                    for (VrUserRmd rmd : userRmds) {
                        VrUserRmd rm = new VrUserRmd();
                        rm.setUserId(rmd.getUserId());
                        recursion(vrUserCount,rmd,depth + 1);
                    }
                }

            }

        }

        public void saveOrUpdate(VrUserCount vrUserCount){
            mapper.saveOrUpdate(vrUserCount);
        }
}
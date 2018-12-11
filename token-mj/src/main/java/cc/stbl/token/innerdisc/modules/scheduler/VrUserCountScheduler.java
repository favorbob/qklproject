package cc.stbl.token.innerdisc.modules.scheduler;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCount;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserRmd;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserCountService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserRmdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
public class VrUserCountScheduler {

    @Autowired
    private VrUserInfoService vrUserInfoService;

    @Autowired
    private VrUserRmdService vrUserRmdService;

    @Autowired
    private VrUserCountService vrUserCountService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void infiniteWar() {
        List<VrUserCount> list = new ArrayList<>();
        List<VrUserInfo> userInfos = vrUserInfoService.findAllList();
        for (VrUserInfo userInfo : userInfos) {

            VrUserCount vrUserCount = new VrUserCount();
            vrUserCount.setUserId(userInfo.getUserId());
            vrUserCount.setUserLevel1(0);
            vrUserCount.setUserLevel2(0);
            vrUserCount.setUserLeveln(0);

            List<VrUserRmd> rmdList = vrUserRmdService.selectByParentUserId(userInfo.getUserId());
            if (rmdList.size() > 0) {
                this.first(rmdList, vrUserCount);
            }
            list.add(vrUserCount);
        }
        if (list.size() > 0){
            for (VrUserCount tbl : list){
                vrUserCountService.saveOrUpdate(tbl);
            }
        }
    }

    public void first(List<VrUserRmd> userRmds, VrUserCount vrUserCount) {
        // 一级下线数量
        vrUserCount.setUserLevel1(userRmds.size());
        for (VrUserRmd rmd : userRmds){
            List<VrUserRmd> rmdList = vrUserRmdService.selectByParentUserId(rmd.getUserId());
            if (rmdList.size() > 0) {
                second(vrUserCount, rmdList);
            }
        }
    }

    public void second(VrUserCount vrUserCount, List<VrUserRmd> userRmds) {
        // 二级下线数量
        vrUserCount.setUserLevel2(userRmds.size());
        for (VrUserRmd rmd : userRmds) {
            List<VrUserRmd> rmdList = vrUserRmdService.selectByParentUserId(rmd.getUserId());
            if (rmdList.size() > 0) {
                infinite(vrUserCount, rmdList);
            }
        }
    }

    public void infinite(VrUserCount vrUserCount, List<VrUserRmd> userRmds) {
        // 无限级下线数量
        vrUserCount.setUserLeveln(vrUserCount.getUserLeveln() + userRmds.size());
        for (VrUserRmd rmd : userRmds) {
            List<VrUserRmd> rmdList = vrUserRmdService.selectByParentUserId(rmd.getUserId());
            if (rmdList.size() > 0) {
                infinite(vrUserCount, rmdList);
            }
        }
    }

}

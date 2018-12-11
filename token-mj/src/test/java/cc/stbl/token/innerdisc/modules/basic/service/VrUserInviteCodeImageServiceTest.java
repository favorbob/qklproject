package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInviteCodeImage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class VrUserInviteCodeImageServiceTest extends AbstractTestCase {

    @Autowired
    private VrUserInviteCodeImageService vrUserInviteCodeImageService;
    @Autowired
    private VrUserInfoService vrUserInfoService;

    @Test
    public void genInviteCodeImage() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddHHmm");
        String userId = "2a6c44cd77c044c392af0de3f870c76f";
        String inviteCode = sdf.format(new Date());
        //String content = "https://www.baidu.com/";
        String url = vrUserInviteCodeImageService.genInviteCodeImage(userId, inviteCode);
        System.out.println("二维码路径:" + url);
        List<VrUserInviteCodeImage> all = vrUserInviteCodeImageService.findAll();
        for (VrUserInviteCodeImage vo : all){
            System.out.println("VrUserInviteCodeImageServiceTest.genInviteCodeImage" + vo);
        }
    }

    @Test
    public void batchUpdateInviteCodeImg(){
        List<VrUserInfo> list = vrUserInfoService.findList(new VrUserInfo());
        for (VrUserInfo info : list){
            vrUserInviteCodeImageService.genInviteCodeImage(info.getUserId(), info.getInviteCode());
        }
        List<VrUserInviteCodeImage> all = vrUserInviteCodeImageService.findAll();
        for (VrUserInviteCodeImage vo : all){
            System.out.println(vo.getQrCodeImg());
        }
    }
}
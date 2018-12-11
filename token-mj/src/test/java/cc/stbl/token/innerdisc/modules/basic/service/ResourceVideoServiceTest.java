package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceInfo;
import cc.stbl.token.innerdisc.restapi.app.resources.GameResourceProto;
import cc.stbl.token.innerdisc.restapi.app.resources.VideoResourceProto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ResourceVideoServiceTest extends AbstractTestCase {

    @Autowired
    private ResourceVideoService resourceVideoService;
    @Autowired
    private ResourceInfoService resourceInfoService;

    @Test
    public void getVideoDetailToShow() {
        ResourceInfo resourceInfo = new ResourceInfo();
        List<ResourceInfo> list = resourceInfoService.findList(resourceInfo, 10);
        for (ResourceInfo info : list){
            VideoResourceProto.ResponseVideoDetail detail = resourceVideoService.getVideoDetailToShow(info.getId());
            System.out.println("ResourceVideoServiceTest.getVideoDetailToShow-----)" + detail);
        }
    }

    @Test
    public void getPageList() {
        VideoResourceProto.RequestVideoList request = new VideoResourceProto.RequestVideoList();
        request.setPageNo(1);
        request.setPageSize(10);
        List<VideoResourceProto.ResponseVideoDetail> list = resourceVideoService.getPageList(request);
        for (VideoResourceProto.ResponseVideoDetail info : list){
            System.out.println("ResourceVideoServiceTest.getPageList-----)" + info);
        }
    }
}
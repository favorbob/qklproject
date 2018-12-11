package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceInfo;
import cc.stbl.token.innerdisc.restapi.app.resources.GameResourceProto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ResourceGameServiceTest extends AbstractTestCase {

    @Autowired
    private ResourceGameService resourceGameService;
    @Autowired
    private ResourceInfoService resourceInfoService;

    @Test
    public void getGameDetailToShow() {
        ResourceInfo resourceInfo = new ResourceInfo();
        List<ResourceInfo> list = resourceInfoService.findList(resourceInfo, 10);
        for (ResourceInfo info : list){
            GameResourceProto.ResponseGameDetail detail = resourceGameService.getGameDetailToShow(info.getId());
            System.out.println("ResourceGameServiceTest.getGameDetailToShow-----)" + detail);
        }
    }

    @Test
    public void getPageList() {
        GameResourceProto.RequestGameList request = new GameResourceProto.RequestGameList();
        request.setPageNo(1);
        request.setPageSize(10);
        List<GameResourceProto.ResponseGameDetail> list = resourceGameService.getPageList(request);
        for (GameResourceProto.ResponseGameDetail info : list){
            System.out.println("ResourceGameServiceTest.getPageList-----)" + info);
        }
    }
}
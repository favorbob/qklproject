package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceInfo;
import cc.stbl.token.innerdisc.restapi.admin.resources.ResourceInfoProto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ResourceInfoServiceTest extends AbstractTestCase {

    @Autowired
    private ResourceInfoService resourceInfoService;

    @Test
    public void getPageList() {
        ResourceInfoProto.RequestResourceInfoList requestResourceInfo = new ResourceInfoProto.RequestResourceInfoList();
        requestResourceInfo.setPageNo(1);
        requestResourceInfo.setPageSize(10);
        List<ResourceInfoProto.ResponseResourceDetail> list = resourceInfoService.getPageList(requestResourceInfo).getList();
        for (ResourceInfoProto.ResponseResourceDetail info : list){
            System.out.println("ResourceInfoServiceTest.getPageList-----)" + info);
        }
    }

    @Test
    public void getResourceDetailToShow() {
        ResourceInfo resourceInfo = new ResourceInfo();
        List<ResourceInfo> list = resourceInfoService.findList(resourceInfo, 10);
        for (ResourceInfo info : list){
            ResourceInfoProto.ResponseResourceDetail resourceDetail = resourceInfoService.getResourceDetailToShow(info.getId());
            System.out.println("ResourceInfoServiceTest.getResourceDetailToShow-----)" + resourceDetail);
        }
    }
}
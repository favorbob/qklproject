package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.enums.ResourceTypeEnum;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceInfo;
import cc.stbl.token.innerdisc.restapi.app.resources.GameResourceProto;
import cc.stbl.token.innerdisc.restapi.app.resources.VideoResourceProto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceVideoService {

    @Autowired
    private ResourceInfoService resourceInfoService;

    @Autowired
    private OssProperties ossProperties;

    public VideoResourceProto.ResponseVideoDetail getVideoDetailToShow(String id){
        ResourceInfo resourceInfo = resourceInfoService.get(id);
        VideoResourceProto.ResponseVideoDetail VideoDetail = this.resourceInfo2ResponseVideoDetail(resourceInfo);
        return VideoDetail;
    }

    public List<VideoResourceProto.ResponseVideoDetail> getPageList(VideoResourceProto.RequestVideoList request){
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setResourceType(ResourceTypeEnum.VIDEO.getCode());
        List<ResourceInfo> list = resourceInfoService.findPageList(resourceInfo, request.getPageNo(), request.getPageSize());
        return this.resourceInfoList2ResponseVideoDetailList(list);
    }

    private List<VideoResourceProto.ResponseVideoDetail> resourceInfoList2ResponseVideoDetailList(List<ResourceInfo> list){
        List<VideoResourceProto.ResponseVideoDetail> details = new ArrayList<>();
        for (ResourceInfo info : list){
            VideoResourceProto.ResponseVideoDetail VideoDetail = this.resourceInfo2ResponseVideoDetail(info);
            details.add(VideoDetail);
        }
        return details;
    }

    private VideoResourceProto.ResponseVideoDetail resourceInfo2ResponseVideoDetail(ResourceInfo resourceInfo){
        VideoResourceProto.ResponseVideoDetail detail = new VideoResourceProto.ResponseVideoDetail();
        BeanUtils.copyProperties(resourceInfo, detail);
        String resourceLogo = detail.getResourceLogo();
        if (StringUtils.isNotEmpty(resourceLogo)){
            if (resourceLogo.startsWith("http:") || resourceLogo.startsWith("https:")){
                return detail;
            }
            String s = ossProperties.getDefault().getHost() + resourceLogo;
            detail.setResourceLogo(s);
        }
        return detail;
    }
}

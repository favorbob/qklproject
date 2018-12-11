package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.enums.ResourceTypeEnum;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceInfo;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.app.resources.GameResourceProto;
import com.ks.common.datastore.exception.StructWithCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceGameService {

    @Autowired
    private ResourceInfoService resourceInfoService;

    @Autowired
    private OssProperties ossProperties;

    public GameResourceProto.ResponseGameDetail getGameDetailToShow(String id){
        ResourceInfo resourceInfo = resourceInfoService.get(id);
        if (resourceInfo == null){
            throw new StructWithCodeException(ResponseCode.RESOURCE_NOT_EXIST);
        }
        GameResourceProto.ResponseGameDetail gameDetail = this.resourceInfo2ResponseGameDetail(resourceInfo);
        return gameDetail;
    }

    public List<GameResourceProto.ResponseGameDetail> getPageList(GameResourceProto.RequestGameList request){
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setResourceType(ResourceTypeEnum.GAME.getCode());
        List<ResourceInfo> list = resourceInfoService.findPageList(resourceInfo, request.getPageNo(), request.getPageSize());
        return this.resourceInfoList2ResponseGameDetailList(list);
    }

    private List<GameResourceProto.ResponseGameDetail> resourceInfoList2ResponseGameDetailList(List<ResourceInfo> list){
        List<GameResourceProto.ResponseGameDetail> details = new ArrayList<>();
        for (ResourceInfo info : list){
            GameResourceProto.ResponseGameDetail gameDetail = this.resourceInfo2ResponseGameDetail(info);
            details.add(gameDetail);
        }
        return details;
    }

    private GameResourceProto.ResponseGameDetail resourceInfo2ResponseGameDetail(ResourceInfo resourceInfo){
        GameResourceProto.ResponseGameDetail detail = new GameResourceProto.ResponseGameDetail();
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

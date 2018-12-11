/**
* generator by mybatis plugin Mon Aug 20 18:00:12 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.modules.basic.dao.ResourceInfoMapper;
import cc.stbl.token.innerdisc.modules.basic.dto.ResourceInfoDTO;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceInfo;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.admin.resources.ResourceInfoProto;
import cc.stbl.token.innerdisc.restapi.admin.trades.EthTradeRecordProto;
import cc.stbl.token.innerdisc.restapi.app.resources.GameResourceProto;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceInfoService extends DataStoreServiceImpl<String, ResourceInfo, ResourceInfoMapper> {

    @Autowired
    private OssProperties ossProperties;

    public Pager<ResourceInfoProto.ResponseResourceDetail> getPageList(ResourceInfoProto.RequestResourceInfoList requestResourceInfo){
        ResourceInfoDTO dto = this.requestResourceInfo2ResourceInfoDTO(requestResourceInfo);

        Long total = mapper.findPageListCount(dto);
        Integer pageNo = requestResourceInfo.getPageNo();
        Integer pageSize = requestResourceInfo.getPageSize();
        Integer offset = (pageNo - 1) * pageSize;
        if (total == 0L) {
            return new Pager(pageNo, pageSize, new ArrayList(), total);
        } else {
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            List<ResourceInfo> list = mapper.findPageList(dto, rowBounds);
            List<ResourceInfoProto.ResponseResourceDetail> details = this.resourceList2ResourceDetailList(list);
            return new Pager(pageNo, pageSize, details, total);
        }
    }

    public ResourceInfoProto.ResponseResourceDetail getResourceDetailToShow(String id){
        ResourceInfo resourceInfo = super.get(id);
        if (resourceInfo == null){
            throw new StructWithCodeException(ResponseCode.RESOURCE_NOT_EXIST);
        }
        return this.resourceInfo2ResourceDetail(resourceInfo);
    }

    private ResourceInfoDTO requestResourceInfo2ResourceInfoDTO(ResourceInfoProto.RequestResourceInfoList requestResourceInfo){
        ResourceInfoDTO dto = new ResourceInfoDTO();
        BeanUtils.copyProperties(requestResourceInfo, dto);
        Integer pageNo = requestResourceInfo.getPageNo();
        Integer pageSize = requestResourceInfo.getPageSize();
        Integer startRow = (pageNo - 1) * pageSize;
        dto.setStartRow(startRow);
        return dto;
    }

    private List<ResourceInfoProto.ResponseResourceDetail> resourceList2ResourceDetailList(List<ResourceInfo> list){
        List<ResourceInfoProto.ResponseResourceDetail> details = new ArrayList<>();
        for (ResourceInfo info : list){
            ResourceInfoProto.ResponseResourceDetail detail = this.resourceInfo2ResourceDetail(info);
            details.add(detail);
        }
        return details;
    }

    private ResourceInfoProto.ResponseResourceDetail resourceInfo2ResourceDetail(ResourceInfo resourceInfo){
        ResourceInfoProto.ResponseResourceDetail detail = new ResourceInfoProto.ResponseResourceDetail();
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
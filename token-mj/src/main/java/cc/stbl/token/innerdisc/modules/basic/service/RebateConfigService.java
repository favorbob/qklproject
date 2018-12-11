/**
* generator by mybatis plugin Mon Aug 20 15:24:18 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.modules.basic.dao.RebateConfigMapper;
import cc.stbl.token.innerdisc.restapi.admin.rebate.RebateProto;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RebateConfigService extends DataStoreServiceImpl<String, cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig, RebateConfigMapper> {

    public void addRebateConfigList(List<RebateProto.RebateConfig> list){
        List<cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig> configs = new ArrayList<>();
        list.stream().forEach(config -> {
            String id = config.getId();
            if (id != null){
                cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig rebateConfig = super.get(id);
                BeanUtils.copyProperties(config, rebateConfig);
                configs.add(rebateConfig);
            }else {
                cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig rebateConfig = new cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig();
                rebateConfig.setId(UUID.randomUUID().toString());
                rebateConfig.setRatio(config.getRatio());
                rebateConfig.setRebateRatio(config.getRebateRatio());
                configs.add(rebateConfig);
            }
        });
        if (configs.size() > 0){
            super.addBatch(configs);
        }
    }

    public List<RebateProto.RebateConfig>  getRebateConfigList(){
        List<RebateProto.RebateConfig> list = new ArrayList<>();
        List<cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig> rebateConfigs = super.findList(new cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig());
        rebateConfigs.stream().forEach(config -> {
            RebateProto.RebateConfig rebateConfig = new RebateProto.RebateConfig();
            BeanUtils.copyProperties(config, rebateConfig);
            list.add(rebateConfig);
        });
        return list;
    }

    public void addRebateConfig(RebateProto.RebateConfig requestAddRebateConfig){
        cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig rebateConfig = new cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig();
        rebateConfig.setId(UUID.randomUUID().toString());
        rebateConfig.setRatio(requestAddRebateConfig.getRatio());
        rebateConfig.setRebateRatio(requestAddRebateConfig.getRebateRatio());
        mapper.insertSelective(rebateConfig);
    }

}
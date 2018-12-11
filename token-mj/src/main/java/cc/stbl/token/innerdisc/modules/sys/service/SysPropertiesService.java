/**
* generator by mybatis plugin Wed Jul 11 17:09:44 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.sys.dao.SysPropertiesMapper;
import cc.stbl.token.innerdisc.modules.sys.entity.SysProperties;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SysPropertiesService extends DataStoreServiceImpl<String, SysProperties, SysPropertiesMapper> implements InitializingBean{

    @Override
    public SysProperties add(SysProperties sysProperties) {
        SysProperties add = super.add(sysProperties);
        cacheValue(add);
        return add;
    }

    //设置参数
    public boolean setProperties(String kind,String name,String value){
        SysProperties properties = this.getSysProperties(kind,name);
        if(properties != null) {
            properties.setValue(value);
            this.update(properties);
        } else {
            properties = new SysProperties();
            properties.setValue(value);
            properties.setKind(kind);
            properties.setCreateDate(new Date());
            properties.setId(JavaUUIDGenerator.get());
            properties.setLabel(name);
            properties.setName(name);
            this.add(properties);
        }
        return true;
    }

    //根据kind 和name 得到参数对象
    private SysProperties getByKindName(String kind, String name) {
//        SysProperties p = getSysProperties(kind,name);
//        if(p == null) {
            SysProperties query = new SysProperties();
            query.setKind(kind);
            query.setName(name);
            return this.findOne(query);
//        }
//        return ;
    }

    //更新 参数
    @Override
    public int update(SysProperties sysProperties) {
        int update = super.update(sysProperties);
        cacheValue(sysProperties);
        return update;
    }

	public String getStringFromDB(String kind, String name) {
		return getStringFromDB(kind, name, "");
	}

	public String getStringFromDB(String kind, String name, String defaultVal) {
		SysProperties pro = getByKindName(kind, name);
		if (pro == null) {
			return defaultVal;
		}
		return pro.getValue();
	}

    public String getString(String kind, String name){ return getString(kind,name,"");}
    public String getString(String kind,String name,String defaultValue) {
        SysProperties properties = getSysProperties(kind,name);
        return properties == null ? defaultValue : properties.getValue();
    }

    protected SysProperties getSysProperties(String kind, String name){
        Map<String,SysProperties> kindMap = cached.get(kind);
        if(kindMap == null || !kindMap.containsKey(name)) {
            SysProperties byKindName = getByKindName(kind, name);
            if(byKindName != null) {
                if(kindMap == null) {
                    kindMap = new HashMap<>();
                    cached.put(kind,kindMap);
                    kindMap.put(name,byKindName);
                }
            }
            return byKindName;
        }
        return kindMap.get(name);
    }
    private Map<String,Map<String,SysProperties>> cached;
    @Override
    public void afterPropertiesSet() throws Exception {
        cached = new HashMap<>();
        SysProperties query = new SysProperties();
        query.desc("kind").desc("sort");
        List<SysProperties> properties = this.findList(query);
        for (SysProperties sp : properties) {
            cacheValue(sp);
            logger.info("SysProperties [{} ==> {}={},{}]",sp.getKind(),sp.getName(),sp.getValue(),sp.getLabel());
        }
    }

    protected void cacheValue(SysProperties sp) {
        if(!cached.containsKey(sp.getKind())) cached.put(sp.getKind(),new LinkedHashMap<>());
        cached.get(sp.getKind()).put(sp.getName(),sp);
    }

    public BigDecimal getBigDecimal(String kind, String name) {
        try {
            String value = getString(kind,name,null);
            if(value == null) return null;
            return new BigDecimal(value);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    public Long getLong(String kind,String name) {
        return getLong(kind,name, null);
    }

    public Long getLong(String kind, String name, Long defaultValue) {
        String value = getString(kind,name,null);
        return value == null ? defaultValue : Long.valueOf(value);
    }
    public boolean getBoolean(String kind, String name){
        return getBoolean(kind,name,false);
    }
    public boolean getBoolean(String kind, String name,boolean defaultValue) {
        String value = getString(kind,name,null);
        if(value == null) return defaultValue;
        return Boolean.valueOf(value);
    }
}
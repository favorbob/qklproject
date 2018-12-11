package cc.stbl.token.innerdisc.modules.basic.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;

@Component
public class AreaUtil {

    @Autowired
	private  SysPropertiesService sysPropertiesService;
	  /**
     * 计算级别
     * @param aArea
     * @param ABrea
     * @return
     */
    public  String getLevel(Long aArea,Long bArea) {
    	  if(aArea == null || bArea == null) {
    		  return "";
    	  }
    	  String v1Value = sysPropertiesService.getString("sys", "v1"); 
          String v2Value = sysPropertiesService.getString("sys", "v2"); 
          String v3Value = sysPropertiesService.getString("sys", "v3"); 
          
          String level = "V1";
          long value;
          if(aArea.longValue() >= bArea.longValue()) {
        	  value = bArea.longValue();
          }else {
        	  value = aArea.longValue();
          }
          
          if(value < Long.parseLong(v1Value)) {
        	  level = "V1";
          }else if(value<Long.parseLong(v2Value)) {
        	  level = "V2";
          }else if(value<Long.parseLong(v3Value)) {
        	  level = "V3";
          }else {
        	  level = "V4";
          }
          
          return level;
    }
    
    public int getAreaMultiple(Long aArea,Long bArea) {
    	String level = getLevel( aArea, bArea);
    	String multiple = "5";
    	switch(level) {
    	case VrUserInfo.USER_AREA_LEVEL_V1:
    		multiple = sysPropertiesService.getString("sys", "v1.multiple");
			break;
			
    	case VrUserInfo.USER_AREA_LEVEL_V2:
    		multiple = sysPropertiesService.getString("sys", "v2.multiple");
			break;
			
    	case VrUserInfo.USER_AREA_LEVEL_V3:
    		multiple = sysPropertiesService.getString("sys", "v3.multiple");
			break;
			
    	case VrUserInfo.USER_AREA_LEVEL_V4:
    		multiple = sysPropertiesService.getString("sys", "v4.multiple");
			break;
	}
    	return Integer.parseInt(multiple);
    }
}

/**
* generator by mybatis plugin Tue Aug 28 11:33:27 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.enums.UserImgTypeEnum;
import cc.stbl.token.innerdisc.modules.basic.dao.VrUserImgMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserImg;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VrUserImgService extends DataStoreServiceImpl<String, VrUserImg, VrUserImgMapper> {

    public void saveReceiptCodeImg(String path, String userId, Integer type) {
        logger.info("保存收款码图片相对路径:" + path);
        try {

            mapper.expireStatusByUserId(userId, type);

            VrUserImg vrUserImg = new VrUserImg();
            vrUserImg.setCreateDate(new Date());
            vrUserImg.setId(JavaUUIDGenerator.get());
            vrUserImg.setImgType(type);
            vrUserImg.setUserId(userId);
            vrUserImg.setIsDefault(0);
            vrUserImg.setStatus(1);
            if (!path.startsWith("/")){
                path = "/" + path;
            }
            vrUserImg.setImgUrl(path);
            super.add(vrUserImg);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<VrUserImg> getByUserId(String userId, Integer status){
        return mapper.selectByUserId(userId, status, null);
    }

    public VrUserImg getByUserId(String userId, Integer status, Integer type){
        List<VrUserImg> vrUserImgs = mapper.selectByUserId(userId, status, type);
        if (vrUserImgs != null && vrUserImgs.size() > 0){
            return vrUserImgs.get(0);
        }
        return null;
    }

    public void addEthReceiveCode(String receiveCode, String userId){
        VrUserImg vrUserImg = new VrUserImg();
        vrUserImg.setImgUrl(receiveCode);
        vrUserImg.setStatus(1);
        vrUserImg.setIsDefault(0);
        vrUserImg.setUserId(userId);
        vrUserImg.setImgType(UserImgTypeEnum.ETH_ASSET.getCode());
        vrUserImg.setId(JavaUUIDGenerator.get());
        vrUserImg.setCreateDate(new Date());
        super.add(vrUserImg);
    }
}
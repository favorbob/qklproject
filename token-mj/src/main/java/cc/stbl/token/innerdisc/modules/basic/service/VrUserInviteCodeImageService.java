/**
* generator by mybatis plugin Mon Aug 20 19:59:14 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.qrcode.ZXingCodeUtil;
import cc.stbl.token.innerdisc.common.qrcode.ZXingConfig;
import cc.stbl.token.innerdisc.common.remote.RemoteFileTransfer;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.modules.basic.dao.VrUserInviteCodeImageMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInviteCodeImage;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class VrUserInviteCodeImageService extends DataStoreServiceImpl<String, VrUserInviteCodeImage, VrUserInviteCodeImageMapper> {

    @Autowired
    private RemoteFileTransfer remoteFileTransfer;
    @Autowired
    private OssProperties ossProperties;

    private static final String OSS_INVITE_CODE_PATH = "/upload/invite_qrcode/";

    @Value("${application.website.domain}")
    private String host;

    public VrUserInviteCodeImage getByUserId(String userId){
        VrUserInviteCodeImage codeImage = new VrUserInviteCodeImage();
        codeImage.setUserId(userId);
        VrUserInviteCodeImage one = super.findOne(codeImage);
        return one;
    }

    public String genInviteCodeImage(String userId, String inviteCode){
        String url;
        try {
            String conten = host +  "/api/packageMgr/download?inviteCode=" + inviteCode;
            url = this.genInviteCodeImageAndUpload(conten);
            VrUserInviteCodeImage inviteCodeImage = this.getByUserId(userId);
            if (inviteCodeImage != null){
                inviteCodeImage.setQrCodeImg(url);
                inviteCodeImage.setInviteCode(inviteCode);
                super.update(inviteCodeImage);
            }else {
                inviteCodeImage = new VrUserInviteCodeImage();
                String id = UUID.randomUUID().toString();
                inviteCodeImage.setId(id);
                inviteCodeImage.setCreateDate(new Date());
                inviteCodeImage.setInviteCode(inviteCode);
                inviteCodeImage.setUserId(userId);
                inviteCodeImage.setQrCodeImg(url);
                super.add(inviteCodeImage);
            }
        }catch (Exception e){
            throw new StructWithCodeException(ResponseCode.GEN_QRCODE_ERROR);
        }
        return url;
    }

    private String genInviteCodeImageAndUpload(String conten) throws Exception {
        ZXingConfig zXingConfig = new ZXingConfig();
        int qRCode = new Color(0,0,0,1).getRGB();//二维码颜色
        zXingConfig.setQRCode(qRCode);
        zXingConfig.setContent(conten);
        ZXingCodeUtil zXingCodeUtil = new ZXingCodeUtil();
        BufferedImage qr_codeBufferedImage = zXingCodeUtil.getQR_CODEBufferedImage(zXingConfig);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qr_codeBufferedImage, "jpg", byteArrayOutputStream);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String path = OSS_INVITE_CODE_PATH + DateFormatUtils.format(new Date(), "yyyyMM") + "/";
        // 上传
        this.uploadToOss(inputStream, fileName, path);
        return ossProperties.getDefault().getHost() + path + fileName;
    }

    private void uploadToOss(InputStream inputStream, String fileName, String path) throws Exception {
        remoteFileTransfer.upload(path, fileName, inputStream);
    }

}
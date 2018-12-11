package cc.stbl.token.innerdisc.restapi.admin.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ks.common.datastore.model.Pager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCard;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserTrade;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserCardService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserTradeService;
import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterEditServicePic;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/sys/vrUserTrade")
@Api(description = "交易管理")
public class VrUserTradeController {

    private static Logger logger = LoggerFactory.getLogger(VrUserTradeController.class);

    @Autowired
    private VrUserTradeService vrUserTradeService;
 
    private final static String COMMON_PATH = "/mnt/public/";
 
    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    @ApiOperation("转账列表")
    @ResponseBody
    public Response<Pager<VrUserTrade>> list(@RequestBody @Valid VrUserTradeProto.ListVrUserTradeRequest request){
    	VrUserTrade query = new VrUserTrade();
        BeanUtils.copyProperties(request,query);
        //sql 已经有 ORDER BY update_time desc，这里不需要指定了
        Pager<VrUserTrade> pager = vrUserTradeService.findPage(query,request.getPageNo(),request.getPageSize());
        return Response.success(pager);
    }

    
    @RequestMapping(value = "/update",method = {RequestMethod.POST})
    @ApiOperation(value = "转账交易确认")
    public Response<String> upload(@RequestParam(required = false) MultipartFile file1,
                                   @RequestParam(required = false) MultipartFile file2,
                                   @RequestParam(required = false) MultipartFile file3,
                                   @RequestParam(required=true) String tradeId,
                                   @RequestParam(required=false) String reason,
                                   @RequestParam(required=true) String tradeType) throws Exception {

        List<MultipartFile> fileList = new ArrayList<MultipartFile>();
        String[] msgStr ;
        if(file1 != null ){
            fileList.add(file1);
        }
        if(file2 != null ) {
            fileList.add(file2);
        }
        if(file3 != null ) {
            fileList.add(file3);
        }
        msgStr = new String[fileList.size()];

        logger.info("---files.length:" + fileList.size() );
        StringBuffer resb = new StringBuffer();
        if( "".equals(tradeId.trim()) ) {
            return Response.error("操作失败，转账交易id不能为空，请重新操作！");
        }
        if((!"4".equals(tradeType.trim())) && (!"5".equals(tradeType.trim())) ) {
            return Response.error("操作类型传值错误，请重新操作！");
        }

        //多个文件，每个文件进行上传。最多3个文件
        for(int i = 0; i < fileList.size(); i++ ) {
            String filedataFileName = fileList.get(i).getOriginalFilename();  //多个文件
            String path = COMMON_PATH + DateFormatUtils.format(new Date(),"yyyyMMdd") + "/" + ShiroUtils.getLoginUserId() + "/";
            String newFileName = JavaUUIDGenerator.get()+filedataFileName.substring(filedataFileName.indexOf("."),filedataFileName.length());
            String message;
            String err = "";
            String msg = path + newFileName;
            msgStr[i] = msg.substring(COMMON_PATH.length() -1); //去掉COMMON_PATH路径

            logger.info("转账交易上传信息第" +i +"个图片的信息是:" +msg + ",交易id是:"+ tradeId.trim());
            try {
                FileUtil.uploadFile(fileList.get(i).getBytes(), path, newFileName);
            } catch (Exception e) {
                err = e.getMessage();
            }
            //返回给前端不包括 COMMON_PATH 路径
            message = "{\"err\":\"" + err + "\",\"msg\":\"" + msg.substring(COMMON_PATH.length() -1)
                    + "\"}";
            err = message;
            resb.append(err);
        }
        //上传完成最多3个文件后，把文件路径等信息更新到数据库
        VrUserTrade object = new VrUserTrade();
        object.setId(tradeId.trim());
        object.setRemark(reason.trim()); //设置交易原因
        object.setStatus( Integer.valueOf(tradeType.trim()) ); //更新status字段 4-XT交易取消，5-XT交易完成

        logger.info("---msgStr.length:" + msgStr.length );

        if(msgStr.length > 0) {
            if(msgStr.length == 1) {
                object.setImage1(msgStr[0]);
            }
            if(msgStr.length == 2) {
                object.setImage1(msgStr[0]);
                object.setImage2(msgStr[1]);
            }
            if(msgStr.length == 3) {
                object.setImage1(msgStr[0]);
                object.setImage2(msgStr[1]);
                object.setImage3(msgStr[2]);
            }
        }
        //调试使用
        logger.info(" object.getImage1():" + object.getImage1());
        logger.info(" object.getImage2():" + object.getImage2());
        logger.info(" object.getImage2():" + object.getImage3());
        logger.info(" object.getStatus():" + object.getStatus());
        logger.info(" object.getId():" + object.getId());

        vrUserTradeService.update(object) ;
        logger.info("转账交易操作完成交易id是:"+ tradeId.trim());
        return Response.success("转账交易操作完成交易id是:"+ tradeId.trim()+"" + "上传的图片信息是:" + resb.toString());
    }

    @RequestMapping(value = "/update2",method = {RequestMethod.POST})
    @ApiOperation(value = "转账交易确认2")
    public Response<String> upload2(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> fileList = ((MultipartHttpServletRequest) request).getFiles("files");
        String tradeId = params.getParameter("tradeId");
        String reason = params.getParameter("reason");
        String tradeType = params.getParameter("tradeType");

        MultipartFile file = null;

        String[] msgStr ;
        if(fileList != null && fileList.size()>0 ){
            msgStr = new String[fileList.size()]; //初始化字符数组
        } else {
           // return Response.error("操作失败，必须上传图片，请重新操作！");
            msgStr = new String[0]; //数组为空
            logger.info("-update2: 转账交易无文件上传！" );
        }
        logger.info("---fileList.size:" + fileList.size() );

        StringBuffer resb = new StringBuffer();
        if( "".equals(tradeId.trim()) ) {
            return Response.error("操作失败，转账交易id不能为空，请重新操作！");
        }
        if((!"4".equals(tradeType.trim())) && (!"5".equals(tradeType.trim())) ) {
            return Response.error("操作类型传值错误，请重新操作！");
        }

        //多个文件，每个文件进行上传。最多3个文件
        for(int i = 0; i < fileList.size(); i++ ) {
            file = fileList.get(i);  //多个文件
            String filedataFileName = file.getOriginalFilename();  //多个文件

            String path = COMMON_PATH + DateFormatUtils.format(new Date(),"yyyyMMdd") + "/" + ShiroUtils.getLoginUserId() + "/";
            String newFileName = JavaUUIDGenerator.get()+filedataFileName.substring(filedataFileName.indexOf("."),filedataFileName.length());
            String message;
            String err = "";
            String msg = path + newFileName;
            msgStr[i] = msg.substring(COMMON_PATH.length() -1); //去掉COMMON_PATH路径

            logger.info("转账交易上传信息第" +i +"个图片的信息是:" +msg + ",交易id是:"+ tradeId.trim());
            try {
                FileUtil.uploadFile(file.getBytes(), path, newFileName);
            } catch (Exception e) {
                err = e.getMessage();
            }
            //返回给前端不包括 COMMON_PATH 路径
            message = "{\"err\":\"" + err + "\",\"msg\":\"" + msg.substring(COMMON_PATH.length() -1)
                    + "\"}";
            err = message;
            resb.append(err);
        }
        //上传完成最多3个文件后，把文件路径等信息更新到数据库
        VrUserTrade object = new VrUserTrade();
        object.setId(tradeId.trim());
        object.setRemark(reason.trim()); //设置交易原因
        object.setStatus( Integer.valueOf(tradeType.trim()) ); //更新status字段 4-XT交易取消，5-XT交易完成

        logger.info("---msgStr.length:" + msgStr.length );

        if(msgStr.length > 0) {
            if(msgStr.length == 1) {
                object.setImage1(msgStr[0]);
            }
            if(msgStr.length == 2) {
                object.setImage1(msgStr[0]);
                object.setImage2(msgStr[1]);
            }
            if(msgStr.length == 3) {
                object.setImage1(msgStr[0]);
                object.setImage2(msgStr[1]);
                object.setImage3(msgStr[2]);
            }
        }
        //调试使用
        logger.info(" object.getImage1():" + object.getImage1());
        logger.info(" object.getImage2():" + object.getImage2());
        logger.info(" object.getImage2():" + object.getImage3());
        logger.info(" object.getStatus():" + object.getStatus());
        logger.info(" object.getId():" + object.getId());

        vrUserTradeService.update(object) ;
        logger.info("转账交易操作完成交易id是:"+ tradeId.trim());
        return Response.success("转账交易操作完成交易id是:"+ tradeId.trim()+"" + "上传的图片信息是:" + resb.toString());
    }
}

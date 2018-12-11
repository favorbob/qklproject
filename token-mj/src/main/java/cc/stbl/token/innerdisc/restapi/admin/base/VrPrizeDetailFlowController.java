package cc.stbl.token.innerdisc.restapi.admin.base;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.modules.basic.entity.VrPrizeDetail;
import cc.stbl.token.innerdisc.modules.basic.service.VrPrizeDetailService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.ExcelData;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.ExportCVSUtil;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.ExportExcelUtils;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 奖金明细查询Controller
 * @author caojinbo
 * @version 2018-09-27
 */

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/base/vaprizedetail")
@Api(description = "查询/奖金明细查询")
public class VrPrizeDetailFlowController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private VrPrizeDetailService vrPrizeDetailService;

	//根据分页 奖金明细查询
	@RequestMapping(value = {"/getVrprizeDetailPage"},method = RequestMethod.POST)
	@ApiOperation("奖金明细查询list")
	public Response<Pager<VrPrizeDetail>> getVrprizeDetailPage(@RequestBody VrPrizeDetailFlowProto.RequestQuery condition) {
		VrPrizeDetail cardFlow = new VrPrizeDetail();
		BeanUtils.copyProperties(condition, cardFlow);
		Pager<VrPrizeDetail> page = vrPrizeDetailService.findPageVrPrizeDetail(cardFlow, condition.getPageNo(), condition.getPageSize());
		List<VrPrizeDetail> list = page.getList();
		for(VrPrizeDetail v:list) {
			boolean settleCount = v.getSettleCount();
			String settleDate = DateFormatUtils.format(v.getSettleDate(), "yyyy/MM/dd HH:mm:ss") ;
			v.setSettleDateStr(settleCount==true?settleDate:"");
		}
		
		return Response.success(page);
	}

	//导出奖金明细 为CSV
	@RequestMapping(value = {"/exportDataOne"},method = RequestMethod.GET)
	@ApiOperation("导出奖金明细1")
	public void exportDataOne(HttpServletResponse response,
                                        @RequestParam(required=false) String phoneNum,
                                        @RequestParam(required=false) Date beginTime,
                                        @RequestParam(required=false) Date endTime,
                                        @RequestParam(required=false) Integer pageNo,
                                        @RequestParam(required=false) Integer pageSize) {
//	public void exportVrprizeDetailData(HttpServletResponse response) {
		VrPrizeDetail cardFlow = new VrPrizeDetail();
        cardFlow.setPhoneNum(phoneNum);
        cardFlow.setBeginTime(beginTime);
        cardFlow.setEndTime(endTime);

        List<VrPrizeDetail> vrlist  =  vrPrizeDetailService.findListVrPrizeDetail(cardFlow, pageNo, pageSize);

//		BeanUtils.copyProperties(cardFlow, cardFlow);
//		List<VrPrizeDetail> vrlist  =  vrPrizeDetailService.findListVrPrizeDetail(cardFlow, 1, 20);
		String msg = "";
		if (vrlist.size() == 0) {
			//return Response.success("无数据导出");
			return;
		}

		List<Map<String, Object>> dataList = null;
		String sTitle = "会员编号,会员昵称,级别,总静态收益,MJ静态收益,AIIC静态收益,层级奖,原资产账户,改变后资产账户,原mj账户,改变后mj账户,原aiic账户,改变后aiic账户,结算时间,领取日期,是否领取";
		String fName = "mj_acci";
		String mapKey = "phoneNum,userName,userLevel,totalEarning,mjEarning,aiicEarning,levelAward,originalAsset,afterAsset,originalMj,afterMj,originalAiic,afterAiic,createDate,settleDate,settleCount";
		dataList = new ArrayList<>();
		Map<String, Object> map = null;
		for (VrPrizeDetail oList : vrlist) {
			map = new HashMap<>();

			map.put("phoneNum", oList.getPhoneNum());
			map.put("userName", oList.getUserName());
			map.put("userLevel", oList.getUserLevel());
			map.put("totalEarning", oList.getTotalEarning());
			map.put("mjEarning", oList.getMjEarning());
			map.put("aiicEarning", oList.getAiicEarning());
			map.put("levelAward", oList.getLevelAward());
			map.put("originalAsset", oList.getOriginalAsset());
			map.put("afterAsset", oList.getAfterAsset());
			
			map.put("originalMj", oList.getOriginalMj());
			map.put("afterMj", oList.getAfterMj());
			map.put("originalAiic", oList.getOriginalAiic());
			map.put("afterAiic", oList.getAfterAiic());
			map.put("createDate", oList.getCreateDate());
			map.put("settleDate",  oList.getSettleCount()==true?DateFormatUtils.format(oList.getSettleDate(), "yyyy/MM/dd HH:mm:ss") :"");
			map.put("settleCount", oList.getSettleCount()==true?"是":"否");
			
			//map.put("createDate", DateFormatUtils.format(order.getCreateDate(), "yyyy/MM/dd HH:mm"));

			dataList.add(map);
		}
		try (final OutputStream os = response.getOutputStream()) {
			ExportCVSUtil.responseSetProperties(fName, response);
			ExportCVSUtil.doExport(dataList, sTitle, mapKey, os);
			return ;
		} catch (Exception e) {
			logger.error("奖金明细查询导出，生成csv文件失败", e);
		}
		msg = "数据导出出错";

		//return Response.success(msg);
		return ;
	}


	@RequestMapping(value = {"/exportDataTwo"},method = RequestMethod.GET)
	@ApiOperation("导出奖金明细2")
	public void exportDataTwo(HttpServletResponse response,
                      @RequestParam(required=false) String phoneNum,
                      @RequestParam(required=false) Date beginTime,
                      @RequestParam(required=false) Date endTime,
                      @RequestParam(required=false) Integer pageNo,
                      @RequestParam(required=false) Integer pageSize) throws Exception {

		ExcelData data = new ExcelData();
		data.setName("奖金明细");
		List<String> titles = new ArrayList();
		titles.add("会员编号");
		titles.add("会员昵称");
		titles.add("级别");
        titles.add("总静态收益");
        titles.add("MJ静态收益");
        titles.add("AIIC静态收益");
        titles.add("层级奖");
        titles.add("原资产账户");
        titles.add("改变后资产账户");
		data.setTitles(titles);

		List<List<Object>> rows = new ArrayList();
		List<Object> row = new ArrayList();

        VrPrizeDetail cardFlow = new VrPrizeDetail();
        cardFlow.setPhoneNum(phoneNum);
        cardFlow.setBeginTime(beginTime);
        cardFlow.setEndTime(endTime);

        List<VrPrizeDetail> vrlist  =  vrPrizeDetailService.findListVrPrizeDetail(cardFlow, pageNo, pageSize);
        if(vrlist.size() > 0 ) {
            for( VrPrizeDetail detail : vrlist ) {
                row.add(detail.getPhoneNum()); //会员编号
                row.add(detail.getUserName()); //会员昵称
                row.add(detail.getUserLevel());//级别
                row.add(detail.getTotalEarning());//总静态收益
                row.add(detail.getMjEarning());//MJ静态收益
                row.add(detail.getAiicEarning());//AIIC静态收益
                row.add(detail.getLevelAward());//层级奖
                row.add(detail.getOriginalAsset());//原资产账户
                row.add(detail.getAfterAsset());//改变后资产账户

                rows.add(row);
            }
        }
		data.setRows(rows);

		ExportExcelUtils.exportExcel( response,"VrPrizeDetailData.xlsx",data );
	}

    @RequestMapping(value = {"/exportDataThree"},method = RequestMethod.GET)
    @ApiOperation("导出奖金明细3")
    public void exportDataThree(HttpServletResponse response,
                                @RequestParam(required=false) String phoneNum,
                                @RequestParam(required=false) Date beginTime,
                                @RequestParam(required=false) Date endTime,
                                @RequestParam(required=false) Integer pageNo,
                                @RequestParam(required=false) Integer pageSize) throws Exception {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("统计表");

        VrPrizeDetailExport.createTitle(workbook,sheet); //设置表头
        //设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

        VrPrizeDetail cardFlow = new VrPrizeDetail();
        cardFlow.setPhoneNum(phoneNum);
        cardFlow.setBeginTime(beginTime);
        cardFlow.setEndTime(endTime);

        List<VrPrizeDetail> vrlist  =  vrPrizeDetailService.findListVrPrizeDetail(cardFlow, pageNo, pageSize);

        //新增数据行，并且设置单元格数据
        for(int i = 0; i < vrlist.size(); i++ ){
            VrPrizeDetail detail = vrlist.get(i);
            HSSFRow row = sheet.createRow(i+1);

            row.createCell(0).setCellValue(detail.getPhoneNum()); //会员编号
            row.createCell(1).setCellValue(detail.getUserName()); //会员昵称
            row.createCell(2).setCellValue(detail.getUserLevel());//级别
            row.createCell(3).setCellValue(String.valueOf(detail.getTotalEarning()) );//总静态收益
            row.createCell(4).setCellValue(String.valueOf(detail.getMjEarning()) );//MJ静态收益
            row.createCell(5).setCellValue(String.valueOf(detail.getAiicEarning()) );//AIIC静态收益
            row.createCell(6).setCellValue(String.valueOf(detail.getLevelAward()) );//层级奖
            row.createCell(7).setCellValue(String.valueOf(detail.getOriginalAsset()) );//原资产账户
            row.createCell(8).setCellValue(String.valueOf(detail.getAfterAsset()) );//改变后资产账户
            row.createCell(9).setCellValue( String.valueOf(detail.getOriginalMj()) );
            row.createCell(10).setCellValue( String.valueOf(detail.getAfterMj()) );
            row.createCell(11).setCellValue( String.valueOf(detail.getOriginalAiic()) );
            row.createCell(12).setCellValue( String.valueOf(detail.getAfterAiic()) );
            row.createCell(13).setCellValue( detail.getCreateDate() );
            row.createCell(14).setCellValue( detail.getSettleCount()==true?DateFormatUtils.format(detail.getSettleDate(), "yyyy/MM/dd HH:mm:ss") :"" );
            row.createCell(15).setCellValue( detail.getSettleCount()==true?"是":"否" );


//          HSSFCell cell = row.createCell(3); //时间字段的处理
//          cell.setCellValue(detail.getCreateDate());
//          cell.setCellStyle(style);

        }

        String fileName = "VrPrizeDetailData.xls";
        //生成excel文件--本地路径生成
       // VrPrizeDetailExport.buildExcelFile("E:\\root\\sheet\\VrPrizeDetailData2.xls", workbook);
       // VrPrizeDetailExport.buildExcelFile(fileName, workbook);

        //浏览器下载excel
        VrPrizeDetailExport.buildExcelDocument(fileName,workbook,response);
    }

        /**
         * 导出日志查询列表
         */
        @RequestMapping(value = "/log_excel")
        public void exportLogList(HttpServletRequest request, HttpServletResponse response) throws Exception {
            try {
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("日志信息列表");
                HSSFRow headRow = sheet.createRow(0);

                //这是一些要导出列的标题
                String[] title = new String[] { "ID","操作时间"};
                for (int i = 0; i < title.length; i++) {
                    headRow.createCell(i).setCellValue(title[i]);
                }
                //要导出的数据对象集合
                List<VrPrizeDetail> vrlist  = new ArrayList<VrPrizeDetail>();
                VrPrizeDetail vr = new VrPrizeDetail();
                vr.setUserName("11111");
                vr.setPhoneNum("13112341234");

                VrPrizeDetail vr2 = new VrPrizeDetail();
                vr2.setUserName("2222");
                vr2.setPhoneNum("13122222222");

                vrlist.add(vr);
                vrlist.add(vr2);

                if (vrlist != null && vrlist.size() > 0) {
                    for (VrPrizeDetail vru : vrlist) {
                        HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
                        this.setCellStyle(workbook,dataRow.createCell(0),vru.getUserName());
                        this.setCellStyle(workbook,dataRow.createCell(1),vru.getPhoneNum());
                    }
                }
                sheet.autoSizeColumn(1, true);
                //对象置空
                vrlist = null;
                response.reset();
                response.setContentType("application/ms-excel;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode("testst.xls", "UTF-8"))));
                try {
                    workbook.write(response.getOutputStream());
                    response.getOutputStream().flush();
                } catch (Exception e) {
                    throw e;
                }finally{
                    if(workbook!=null){
                        workbook.close();
                    }
                    if(response.getOutputStream()!=null){
                        response.getOutputStream().close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public HSSFCell setCellStyle(HSSFWorkbook workbook,HSSFCell cell,String value){
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            HSSFDataFormat format = workbook.createDataFormat();
            cellStyle.setDataFormat(format.getFormat("@"));
            cell.setCellStyle(cellStyle);
            cell.setCellValue(value);
            return cell;
        }

}

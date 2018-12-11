package cc.stbl.token.innerdisc.restapi.admin.base;

import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

public class VrPrizeDetailExport {

    //创建奖金明细查询导出的表头
    protected static void createTitle(HSSFWorkbook workbook, HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1,12*256);
        sheet.setColumnWidth(3,17*256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("会员编号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("会员昵称");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("级别");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("总静态收益");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("MJ静态收益");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("AIIC静态收益");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("层级奖");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("原资产账户");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("改变后资产账户");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("原mj账户");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("改变后mj账户");
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellValue("原aiic账户");
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellValue("改变后aiic账户");
        cell.setCellStyle(style);

        cell = row.createCell(13);
        cell.setCellValue("结算时间");
        cell.setCellStyle(style);

        cell = row.createCell(14);
        cell.setCellValue("领取日期");
        cell.setCellStyle(style);

        cell = row.createCell(15);
        cell.setCellValue("是否领取");
        cell.setCellStyle(style);

    }

    //生成excel文件
    protected static void buildExcelFile(String filename,HSSFWorkbook workbook) throws Exception{
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    //浏览器下载excel
    protected static void buildExcelDocument(String filename,HSSFWorkbook workbook,HttpServletResponse response) throws Exception{
//         //测试1
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename, "utf-8"));
            //测试2
//        response.setContentType("application/x-download");
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1"));
            //测试3
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("multipart/form-data");
//        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));

//        filename = new String(filename.getBytes("GBK"), "ISO8859_1");
//        response.setContentType("application/msexcel");
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + filename);

        filename = new String(filename.getBytes("GBK"), "ISO8859_1");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

    }
}

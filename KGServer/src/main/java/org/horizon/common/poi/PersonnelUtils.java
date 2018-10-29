package org.horizon.common.poi;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.horizon.bean.Personnel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by lzd on 2018/2/24.
 *
 */
public class PersonnelUtils {

    public static ResponseEntity<byte[]> exportEmp2Excel(List<Personnel> pres) {
        HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
        try {
            //1.创建Excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            //2.创建文档摘要
            workbook.createInformationProperties();
            //3.获取文档信息，并配置
            DocumentSummaryInformation dsi = workbook.getDocumentSummaryInformation();
            //3.1文档类别
            dsi.setCategory("班级信息");
            //3.2设置文档管理员
            dsi.setManager("lzd");
            //3.3设置组织机构
            dsi.setCompany("XXX班级");
            //4.获取摘要信息并配置
            SummaryInformation si = workbook.getSummaryInformation();
            //4.1设置文档主题
            si.setSubject("班级信息表");
            //4.2.设置文档标题
            si.setTitle("班级信息");
            //4.3 设置文档作者
            si.setAuthor("XXX班级");
            //4.4设置文档备注
            si.setComments("备注信息暂无");
            //创建Excel表单
            HSSFSheet sheet = workbook.createSheet("XXX班级信息表");
            //创建日期显示格式
            HSSFCellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
            //创建标题的显示样式
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //定义列的宽度
            sheet.setColumnWidth(0, 5 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 10 * 256);
            sheet.setColumnWidth(3, 5 * 256);
            sheet.setColumnWidth(4, 12 * 256);//第四列(出生日期那一列)的宽度为10个字符的宽度
            sheet.setColumnWidth(5, 20 * 256);

            //5.设置表头
            HSSFRow headerRow = sheet.createRow(0);
            HSSFCell cell0 = headerRow.createCell(0);
            cell0.setCellValue("编号");
            cell0.setCellStyle(headerStyle);
            HSSFCell cell1 = headerRow.createCell(1);
            cell1.setCellValue("班级名称");
            cell1.setCellStyle(headerStyle);
            HSSFCell cell2 = headerRow.createCell(2);
            cell2.setCellValue("班级人数");
            cell2.setCellStyle(headerStyle);
            HSSFCell cell3 = headerRow.createCell(3);
            cell3.setCellValue("班级描述");
            cell3.setCellStyle(headerStyle);
            HSSFCell cell4 = headerRow.createCell(4);
            cell4.setCellValue("开始日期");
            cell4.setCellStyle(headerStyle);
            HSSFCell cell5 = headerRow.createCell(5);
            cell5.setCellValue("结束日期");
            cell5.setCellStyle(headerStyle);
            //6.装数据
            for (int i = 0; i < pres.size(); i++) {
                HSSFRow row = sheet.createRow(i + 1);
                Personnel per = pres.get(i);
                row.createCell(0).setCellValue(per.getId());
                row.createCell(1).setCellValue(per.getName());
                row.createCell(2).setCellValue(per.getNum_stu());
                row.createCell(3).setCellValue(per.getDes());
                HSSFCell birthdayCell = row.createCell(4);
                birthdayCell.setCellValue(per.getBegin_date());
                birthdayCell.setCellStyle(dateCellStyle);

                HSSFCell beginDateCell = row.createCell(5);
                beginDateCell.setCellValue(per.getEnd_date());
                beginDateCell.setCellStyle(dateCellStyle);

            }
            headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", new String("班级表.xls".getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            baos = new ByteArrayOutputStream();
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

}

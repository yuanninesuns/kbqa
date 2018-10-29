package org.horizon.common.poi;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.horizon.bean.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thinkpad on 2018/2/26.
 */
public class StudentUtils {
    public static ResponseEntity<byte[]> exportEmp2Excel(List<Student> stus) {
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
            dsi.setCategory("学生信息");
            //3.2设置文档管理员
            dsi.setManager("系统管理员");
            //3.3设置组织机构
            dsi.setCompany("XXX");
            //4.获取摘要信息并配置
            SummaryInformation si = workbook.getSummaryInformation();
            //4.1设置文档主题
            si.setSubject("学生信息表");
            //4.2.设置文档标题
            si.setTitle("学生信息");
            //4.3 设置文档作者
            si.setAuthor("XXX");
            //4.4设置文档备注
            si.setComments("备注信息暂无");
            //创建Excel表单
            HSSFSheet sheet = workbook.createSheet("XXX信息表");
            //创建日期显示格式
            HSSFCellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
            //创建标题的显示样式
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //定义列的宽度

            sheet.setColumnWidth(0, 12 * 256);
            sheet.setColumnWidth(1, 10 * 256);
            sheet.setColumnWidth(2, 5 * 256);
            sheet.setColumnWidth(3, 12 * 256);//第四列(出生日期那一列)的宽度为10个字符的宽度
            sheet.setColumnWidth(4, 20 * 256);

            //5.设置表头
            HSSFRow headerRow = sheet.createRow(0);
            HSSFCell cell1 = headerRow.createCell(0);
            cell1.setCellValue("姓名");
            cell1.setCellStyle(headerStyle);
            HSSFCell cell2 = headerRow.createCell(1);
            cell2.setCellValue("学号");
            cell2.setCellStyle(headerStyle);
            HSSFCell cell3 = headerRow.createCell(2);
            cell3.setCellValue("性别");
            cell3.setCellStyle(headerStyle);
            HSSFCell cell4 = headerRow.createCell(3);
            cell4.setCellValue("班级");
            cell4.setCellStyle(headerStyle);
            HSSFCell cell5 = headerRow.createCell(4);
            cell5.setCellValue("分数");
            cell5.setCellStyle(headerStyle);
            //6.装数据
            for (int i = 0; i < stus.size(); i++) {
                HSSFRow row = sheet.createRow(i + 1);
                Student stu = stus.get(i);
                row.createCell(0).setCellValue(stu.getName());
                row.createCell(1).setCellValue(stu.getStudentId());
                row.createCell(2).setCellValue(stu.getGender());
                row.createCell(3).setCellValue(stu.getStudentClassName());
                row.createCell(4).setCellValue(String.valueOf(stu.getOrdinary_performance()));
            }
            headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", new String("学生信息表.xls".getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            baos = new ByteArrayOutputStream();
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    public static List<Student> importEmp2List(MultipartFile file) {
        List<Student> stuList = new ArrayList<>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                Student stu;
                for (int j = 0; j < physicalNumberOfRows; j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//没数据
                    }
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    stu = new Student();
                    for (int k = 0; k < physicalNumberOfCells; k++) {
                        HSSFCell cell = row.getCell(k);
                        switch (cell.getCellTypeEnum()) {
                            case STRING: {
                                String cellValue = cell.getStringCellValue();
                                if (cellValue == null) {
                                    cellValue = "";
                                }
                                switch (k) {
                                    case 0:
                                        stu.setName(cellValue);
                                        break;
                                    case 1:
                                        stu.setStudentId(cellValue);
                                        break;
                                    case 2:
                                        stu.setGender(cellValue);
                                        break;
                                    case 3:
                                        stu.setStudentClassName(cellValue);
                                        break;
                                    case 4:
                                        stu.setOrdinary_performance(Double.parseDouble(cellValue));
                                        stu.setCreate_date(getDayDate());
                                        stu.setUpdate_date(getDayDate());
                                        stu.setCreate_by(3);
                                        stu.setUpdate_by(3);
                                        break;
                                }
                            }
                            break;
                            default: {

                            }
                            break;
                        }
                    }
                    stuList.add(stu);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stuList;
    }
    private static String getDayDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }
}

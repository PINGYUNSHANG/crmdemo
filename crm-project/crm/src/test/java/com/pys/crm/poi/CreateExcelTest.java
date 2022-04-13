package com.pys.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 使用apache-poi生成excel文件
 */
public class CreateExcelTest {
    @Test
    public void testCreateExcel() throws IOException {
        //创建 HSSFWorkbook对象  excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //往文件中写内容
        // workbook 创建页对象 对应workbook文件的一页
        HSSFSheet sheet = workbook.createSheet("学生列表");
        //使用sheet创建 HSSFRow对象
        HSSFRow row = sheet.createRow(0);//行号 从0开始,依次开始
        //使用row创建HSSFCell 对象
        HSSFCell cell = row.createCell(0);//列号 ，从0开始,依次增加
        cell.setCellValue("学号");
        cell = row.createCell(1);//列号 ，从0开始,依次增加
        cell.setCellValue("姓名");
        cell = row.createCell(2);//列号 ，从0开始,依次增加
        cell.setCellValue("年龄");

        //生成样式对象
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 1; i <= 10; i++) {
            row = sheet.createRow(i);
            cell = row.createCell(0);//列号 ，从0开始,依次增加
            cell.setCellValue(100 + i);
            cell = row.createCell(1);//列号 ，从0开始,依次增加
            cell.setCellValue("name" + i);
            cell = row.createCell(2);//列号 ，从0开始,依次增加
            cell.setCellValue(20 + i);
            cell.setCellStyle(cellStyle);
        }
        //生成excel文件
        OutputStream os = new FileOutputStream("/Users/pingyunshangpingyunshang/Downloads/crm-ssm项目/studentList.xls");
        workbook.write(os);


        os.close();
        workbook.close();

        System.out.println("ok～");
    }
}

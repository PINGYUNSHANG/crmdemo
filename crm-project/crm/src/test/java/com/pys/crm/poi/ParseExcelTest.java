package com.pys.crm.poi;

import com.pys.crm.commons.utils.HSSFUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

//使用apache-poi解析excel
public class ParseExcelTest {
    //根据指定excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
    public static void main(String[] args) throws Exception {
        FileInputStream is = new FileInputStream("/Users/pingyunshangpingyunshang/Downloads/activityList.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        //获取HSSFSheet对象，封装了一页的所有信息
        HSSFSheet sheet = workbook.getSheetAt(0);//根据页的下标获取， 从0开始
        //根据sheet来获取HSSFRow对象
        //getLastRowNum() 获取最后一行的下标
        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);//行的下标，下标从0开始，依次增加
//            根据row来获取HSSFCell

            //row.getLastCellNum() 是总列数
            for (int j = 0; j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);//列的下标，下标从0开始依次增加
                System.out.print( HSSFUtils.getCellValueForStr(cell)+"  ");
            }

            System.out.println();
        }
    }

}

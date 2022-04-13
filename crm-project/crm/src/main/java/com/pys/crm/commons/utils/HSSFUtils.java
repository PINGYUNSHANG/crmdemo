package com.pys.crm.commons.utils;


import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * 关于excel文件操作的工具类
 */
public class HSSFUtils {
    //工具方法

    /**
     * 从指定的HSSFCell来获取列的值
     *
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell) {
        //获取列中的数据
        String res = "";
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            res = cell.getStringCellValue();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            res = cell.getNumericCellValue() + "";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
            res = cell.getBooleanCellValue() + "";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
            res = cell.getCellFormula();
        } else {
            res = "";
        }
        return res;
    }

}

package com.ysh.utils;

import com.ysh.excel.ExcelSet;
import com.ysh.excel.ExcelSheet;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author 闫世豪
 * @date 2019/11/10 12:57 上午
 * @email whynightcode@gmail.com
 */
public class ExcelUtil {
    /**
     * 解析Excel表格
     *
     * @param file 文件
     * @return
     * @throws Exception
     */
    public static ExcelSet resolveExcel(File file) throws Exception {

        ExcelSet excelSet = new ExcelSet();


        //Excel文件
        Workbook workbook = WorkbookFactory.create(file);

        try {

            List<ExcelSheet> sheets = new ArrayList<ExcelSheet>();
            Iterator<Sheet> its = workbook.sheetIterator();
            //处理每个sheet
            while (its.hasNext()) {
                Sheet sheet = its.next();

                ExcelSheet excelSheet = new ExcelSheet();
                excelSheet.setName(sheet.getSheetName());

                List<List<String>> content = new ArrayList<List<String>>();
                Iterator<Row> itr = sheet.rowIterator();
                //处理该sheet下每一行
                while (itr.hasNext()) {
                    Row row = itr.next();

                    List<String> contentsOfRow = new ArrayList<String>();
                    Iterator<Cell> itc = row.cellIterator();
                    //处理该行每个cell
                    while (itc.hasNext()) {
                        Cell cell = itc.next();
                        // 添加这一行解决数值类型单元格无法正确读取问题
                        String value;
                        CellType cellType = cell.getCellType();
                        if (cellType == CellType.NUMERIC) {
                            if (DateUtil.isCellDateFormatted(cell)) {
                                Date tempValue = cell.getDateCellValue();
                                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
                                value = simpleFormat.format(tempValue);
                            } else {
                                cell.setCellType(CellType.STRING);
                                value = cell.toString();

                            }
                        } else {
                            cell.setCellType(CellType.STRING);
                            value = cell.toString();
                        }
                        contentsOfRow.add(value);
                    }
                    content.add(contentsOfRow);
                }
                excelSheet.setContent(content);
                sheets.add(excelSheet);
            }
            excelSet.setSheets(sheets);
            excelSet.setExcelFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("文件解析错误: " + e.getMessage(), e);
        } finally {
            workbook.close();
        }


        return excelSet;
    }

    /**
     * 获取指定单元格内容
     *
     * @param excelSheet
     * @param row
     * @param col
     * @return
     */
    public static String getExcelCellValue(ExcelSheet excelSheet, int row, int col) {
        return excelSheet.getContent().get(row).get(col).trim();
    }

    /**
     * 获取指定单元格内容
     *
     * @param excelSet
     * @param sheetIndex
     * @param row
     * @param col
     * @return
     */
    public static String getExcelCellValue(ExcelSet excelSet, int sheetIndex, int row, int col) {
        return excelSet.getSheets().get(sheetIndex).getContent().get(row).get(col).trim();
    }


    /**
     * 写内容到指定工作表和单元格
     *
     * @param content
     * @param excelSet
     * @param sheetIndex
     * @param rowIndex
     * @param colIndex
     * @throws Exception
     */
    public static void writeCellToExcelFile(String content, ExcelSet excelSet, int sheetIndex, int rowIndex, int colIndex) throws Exception {

        String filename = excelSet.getExcelFile().getAbsolutePath();

        Workbook workbook = WorkbookFactory.create(new FileInputStream(filename));
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(colIndex);
        cell.setCellValue(content);


        FileOutputStream fo = new FileOutputStream(filename);
        try {
            workbook.write(fo);
        } finally {
            fo.flush();
            fo.close();
        }

    }

    /**
     * 将ExcelSet对象存入文件
     *
     * @param excelSet
     * @throws Exception
     */
    public static void saveExcelSetToFile(ExcelSet excelSet) throws Exception {

        String filename = excelSet.getExcelFile().getAbsolutePath();
        Workbook workbook = WorkbookFactory.create(new FileInputStream(filename));

        List<ExcelSheet> sheets = excelSet.getSheets();

        FileOutputStream fo = new FileOutputStream(filename);

        try {
//         每个工作表
            for (int sheetIndex = 0; sheetIndex < sheets.size(); sheetIndex++) {
                Sheet toSheet = workbook.getSheetAt(sheetIndex);
                ExcelSheet sheet = sheets.get(sheetIndex);

                List<List<String>> content = sheet.getContent();
//            每个工作表的每一行
                for (int rowIndex = 0; rowIndex < content.size(); rowIndex++) {
                    Row toRow = toSheet.getRow(rowIndex);
                    List<String> row = content.get(rowIndex);

                    int colIndex = 0;
//                每一行的单元格
                    for (int toColIndex = 0; toColIndex < row.size(); toColIndex++) {
                        Cell toCell = toRow.getCell(toColIndex);
                        String cellValue = row.get(colIndex);


                        if (toCell != null) {
                            if (toCell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                                toCell.setCellValue(Double.parseDouble(cellValue));
                            } else {
                                toCell.setCellValue(cellValue);
                            }

                            colIndex++;
                        }
                    }

                }
            }

            workbook.write(fo);

        } finally {
            fo.flush();
            fo.close();
        }
    }

}

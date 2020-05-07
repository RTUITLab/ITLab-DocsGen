/*package com.service.generatexls.utils;


import com.service.generatexls.service.RestTemplateService;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;



public class xlsCreator {
    XSSFCell cell;
    XSSFRow row;
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet1 = workbook.createSheet("Сводка");
    XSSFFont font = workbook.createFont();
    XSSFCellStyle style = workbook.createCellStyle();
    int rowNum = 0;
    int colNum = 0;

    public void xlsCreator(RestTemplateService itr) {
        font.setFontName("Arial");
        style.setFont(font);
        String surname;
        String name;
        String[] splited;
        row = sheet1.createRow(rowNum++);
        cell = row.createCell(colNum++);
        cell.setCellValue("фамилия");
        cell.setCellStyle(style);
        sheet1.autoSizeColumn(0);

        cell = row.createCell(colNum++);
        cell.setCellStyle(style);
        cell.setCellValue("имя");
        sheet1.autoSizeColumn(1);
        colNum = 0;
        while ()

    }
}
*/
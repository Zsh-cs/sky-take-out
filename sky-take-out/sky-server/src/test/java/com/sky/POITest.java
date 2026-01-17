package com.sky;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

/**
 * 使用POI操作Excel文件
 */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class POITest {

    // 通过POI创建Excel文件并写入内容
    public static void write() throws IOException {

        // 在内存中创建一个Excel文件
        XSSFWorkbook excel = new XSSFWorkbook();
        // 在Excel文件中创建一个Sheet
        XSSFSheet sheet = excel.createSheet("Sheet1");
        // 在Sheet中创建行对象
        XSSFRow row1 = sheet.createRow(1);// rownum编号从0开始

        // 在行中创建单元格，并向单元格写入内容
        // columnIndex编号从0开始
        row1.createCell(1).setCellValue("name");
        row1.createCell(2).setCellValue("city");

        // 创建新行
        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(1).setCellValue("Zsh");
        row2.createCell(2).setCellValue("Swatow");

        XSSFRow row3 = sheet.createRow(3);
        row3.createCell(1).setCellValue("Aj");
        row3.createCell(2).setCellValue("Beijing");

        // 将这个Excel文件从内存写入到磁盘中
        FileOutputStream fos = new FileOutputStream("src\\main\\resources\\test.xlsx");
        excel.write(fos);

        // 关闭资源
        fos.close();
        excel.close();

    }

    // 通过POI读取Excel文件
    public static void read() throws IOException {

        FileInputStream fis = new FileInputStream("src\\main\\resources\\test.xlsx");
        XSSFWorkbook excel = new XSSFWorkbook(fis);
        XSSFSheet sheet = excel.getSheetAt(0);

        // 获得Sheet中有文字的最后一行的行号
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            System.out.println("column2: " + row.getCell(1).getStringCellValue() +
                    ", column3: " + row.getCell(2).getStringCellValue());
        }

        // 关闭资源
        excel.close();
        fis.close();
    }


    @Test
    public void testWrite() throws IOException {
        write();
    }

    @Test
    public void testRead() throws IOException {
        read();
    }

}

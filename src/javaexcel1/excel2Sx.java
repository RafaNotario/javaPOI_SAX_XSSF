/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaexcel1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class excel2Sx {

    static void generateSXSSF() throws IOException {
        Workbook wb = new SXSSFWorkbook(500); // keep 100 rows in memory, exceeding rows will be flushed to disk
        Sheet sh = wb.createSheet();
        for (int rownum = 0; rownum < 100 * 100; rownum++) {
            Row row = sh.createRow(rownum);
            for (int cellnum = 0; cellnum < 20; cellnum++) {
                Cell cell = row.createCell(cellnum);
                String address = new CellReference(cell).formatAsString();
                cell.setCellValue(address);
            }
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream("sxssf.xlsx");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(excel2Sx.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            wb.write(out);
        } catch (IOException ex) {
            Logger.getLogger(excel2Sx.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.close();
    }

    public static void main(String[] argv) {

        try {
            generateSXSSF();
        } catch (IOException ex) {
            Logger.getLogger(excel2Sx.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

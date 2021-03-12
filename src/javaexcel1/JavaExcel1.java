
package javaexcel1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
import conexion.ConexionDBOriginal;
import controllerInsert.controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JavaExcel1 {
    
   static ConexionDBOriginal con2 = new ConexionDBOriginal();
   controller cont = new controller();
    public static void main(String[] args) {
       //crearExcel();
       
      // cargar();
      leer();
    }
    
    public static void crearExcel(){//version xls
          int contador = 0;
        //Workbook book= new HSSFWorkbook();//version anterior de excel
        Workbook book= new XSSFWorkbook();//nueva version xlsx
        Sheet sheet =  book.createSheet("Prueba");//crear una hoja en el libro
        
         
       for (int i = 0; i < 3000; i++) {
        Row row = sheet.createRow(i);
            for (int j = 0; j < 10; j++) {
                contador++;
                     row.createCell(j).setCellValue("Fila"+contador+"col"+j);
            }
        } 
        
        try {
//            FileOutputStream fileout = new FileOutputStream("Excel.xls");//formato anterior
             FileOutputStream fileout = new FileOutputStream("Excel.xlsx");//formato anterior
            
             book.write(fileout);
             fileout.close();
             
             System.out.println("Escribio :termino ");
                     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JavaExcel1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JavaExcel1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void leer(){
        int RowCount;
        try {
            Object val = null;
            FileInputStream file = new FileInputStream(new File("A://PRUEBAS SISTEMA/OFSC/SERVICIOS_APLICADOS_DE_COBRANZA_MERCANTIL_VD_01032021.xlsx"));
            XSSFWorkbook wb =  new XSSFWorkbook(file);
            Sheet sheet = wb.getSheetAt(0);
            int a =  wb.getNumberOfSheets();//obtiene el  numero de las pestañitas
            RowCount = sheet.getLastRowNum();

           for (int i = 0; i <= RowCount; i++) {
                Row fila = sheet.getRow(i);
                int numcols = fila.getLastCellNum();
                
                for (int j = 0; j < numcols; j++) {
                       Cell celda = fila.getCell(j);

                   switch(celda.getCellTypeEnum().toString()){
                            case "NUMERIC":
                              System.out.print(celda.getNumericCellValue()+ ' ');
                              break;
                            case "STRING":
                                System.out.print(celda.getStringCellValue()+' ');
                                break;
                           case "FORMULA":
                                System.out.print(celda.getCellFormula());
                                break;
                        }
                }
                System.out.println("");
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JavaExcel1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JavaExcel1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        public static void cargar(){
            
           Connection cn = con2.conexion();
            PreparedStatement ps;
            
        int RowCount;
        try {
            Object val = null;
            FileInputStream file = new FileInputStream(new File("Acumulado General Cierres OFSC-HEAT-Enero 2021 - copia.xlsx"));
            XSSFWorkbook wb =  new XSSFWorkbook(file);
            Sheet sheet = wb.getSheetAt(0);
            int a =  wb.getNumberOfSheets();//obtiene el  numero de las pestañitas
            RowCount = sheet.getLastRowNum();

           for (int i = 1; i <= RowCount; i++) {
                Row fila = sheet.getRow(i); 
               
                
                
                /*try {
                    ps = cn.prepareStatement("LOCK TABLES `producto` WRITE;"
                            + "INSERT INTO  `producto` VALUES (?,?,?,?,?,?,?,?,?,?);");
                    ps.setString(1, fila.getCell(0).getStringCellValue());
                    ps.setString(2, fila.getCell(1).getStringCellValue());
                    ps.setString(3, fila.getCell(2).getStringCellValue());
                    ps.setString(4, fila.getCell(3).getStringCellValue());
                    ps.setString(5, fila.getCell(4).getStringCellValue());
                    ps.setString(6, fila.getCell(5).getStringCellValue());
                    ps.setString(7, fila.getCell(6).getStringCellValue());
                    ps.setString(8, fila.getCell(7).getStringCellValue());
                    ps.setString(9, fila.getCell(8).getStringCellValue());
                    ps.setString(10, fila.getCell(9).getStringCellValue());
                    
                    ps.execute();
                    System.out.println(i);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(JavaExcel1.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                

            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JavaExcel1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JavaExcel1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

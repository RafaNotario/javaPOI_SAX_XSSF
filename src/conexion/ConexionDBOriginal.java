
package conexion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JOptionPane;

public class ConexionDBOriginal {

    Connection cn;
    
    public Connection conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//com.mysql.jdbc.Driver 
// cn = DriverManager.getConnection("jdbc:mysql://"+cargaConfig()+"/admindcr?","root","0ehn4TNU5I");//Version de conector 5.1.24 DrNuño
//NUEVA VERSION DE CONECTOR PA MYSQLSERVER MAMALON POWER+++ 8.0.17 FUCKK //allowPublicKeyRetrieval=true&
cn = DriverManager.getConnection("jdbc:mysql://localhost/sacsavd?allowPublicKeyRetrieval=true&useSSL=false&useTimezone=true&serverTimezone=UTC","root","0ehn4TNU5R");//0ehn4TNU5I +cargaConfig()+
    //System.out.println("CONEXION EXITOSA ConexionDBOriginal");
        } catch (Exception ex) {
            System.out.println("ERROR EN CONEXION");
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }      
        return cn;
    }
            
    Statement createStatement(){
        throw new UnsupportedOperationException("No soportado");
    }
    
    
    private String cargaConfig() {//List
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
         String ruta="C:/adminDCR/config.txt";//2700 los 4; solo dos: 1850del, 1450traseros, 
         //List<String> contentL=new ArrayList<String>();
         String cadena="",rgresa="";  
        try {
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).     
        fr = new FileReader(ruta);
        br = new BufferedReader(fr);
        while((cadena = br.readLine())!=null) {
            //contentL.add(cadena.trim());
            rgresa=cadena.trim();
        }
        br.close();
        }catch(Exception e){
         e.printStackTrace();
      }finally{
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try{                    
            if( null != fr){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
         }
      }
        
        return rgresa;
    }
    
}

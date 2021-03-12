package javaexcel1;

import com.mysql.cj.util.StringUtils;
import conexion.ConexionDBOriginal;
import controllerInsert.controller;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class leeCSV {

    static ConexionDBOriginal con2 = new ConexionDBOriginal();
    static controller cont = new controller();
    String cad = "A://PRUEBAS SISTEMA/HEAT NEW/01032021/csv/Servicios Aplicados De Cobranza Mercantil Vd Sa De Cv   Extrajudicial.csv";

    public void procesPorLotes(int tot) {//se tarda 1 hora
        int acum = 0;
        int totalIs = tot;
        String csvFile = cad;
        BufferedReader br = null;
        String line = "";
//Se define separador ","
        String cvsSplitBy = ",", cadenaInSert = "", conParentesis = "";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null && acum < 20) {
                String[] datos = line.split(cvsSplitBy);
                //cadenaInSert += "(";
                if (acum > 0) {
                    for (int i = 0; i < datos.length; i++) {
                        if (i == 0) {
                            cadenaInSert += "('" + datos[i] + "'";
                        } else {
                            //System.out.print("'"+datos[i].trim()+"'"+ "," );
                            cadenaInSert += ",'" + datos[i] + "'";
                        }
                    }
                    cadenaInSert += "),\n";
                }
                acum++;
            }
            cadenaInSert = cadenaInSert.replaceFirst(".$", "");

            // System.out.print(cadenaInSert);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String procesaAll() {//se tarda 1 hora
        int acum = 0;
        int totalIs = cuentaLines();
        String csvFile = cad;
        BufferedReader br = null;
        String line = "";
//Se define separador ","
        String cvsSplitBy = ",", cadenaInSert = "", conParentesis = "";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null && acum < 400) {
                if (acum > 0) {
                    String[] datos = line.split(cvsSplitBy);
                    System.err.println("dats: " + datos.length);

                    //cadenaInSert += "(";
                    for (int i = 0; i < datos.length; i++) {
                        if (i == 0) {
                            cadenaInSert += "('" + datos[i] + "'";
                        } else {
                            //System.out.print("'"+datos[i].trim()+"'"+ "," );
                            cadenaInSert += ",'" + datos[i] + "'";
                        }
                    }
                    cadenaInSert += "),\n";
                }
                acum++;
            }
            //cadenaInSert = StringUtils.chop(cadenaInSert);
            cadenaInSert = cadenaInSert.replaceFirst(".$", "");
            //System.out.print(cadenaInSert);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cadenaInSert;
    }

    public String procesaAll2() {//se tarda 1 hora
        int acum = 0;
        int totalIs = cuentaLines();
        String csvFile = cad;
        BufferedReader br = null;
        String line = "";
//Se define separador ","
        String cvsSplitBy = ",", cadenaInSert = "", conParentesis = "";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null ) {//numero de lineas a procesar/insertar && acum < 2000   
                if (acum > 0) {
                    String[] datos = line.split(cvsSplitBy);
              //      System.out.println("length: " + datos.length);
                    cadenaInSert += "('" + Integer.toString(acum) + "',";//Siempre comiennza ('1',
                    if (datos.length > 49) {
                        int sum = datos.length - 49;
                        for (int i = 0; i < datos.length; i++) {
                            if (i == 0) {
                                cadenaInSert += "'" + datos[i] + "'";
                            } else if (i >= 27 && i <= (27 + sum)){
                                if(i == 27)
                                    //System.out.print("'"+datos[i].trim()+"'"+ "," );
                                    cadenaInSert += ",'"+datos[i] +"-";
                                else if(i == (27+sum))
                                    cadenaInSert += datos[i] +"'";
                                else
                                    cadenaInSert += datos[i] +"-";
                            }else{
                                cadenaInSert += ",'" + datos[i] + "'";
                            }
                        }//for
                    } else {
                        for (int i = 0; i < datos.length; i++) {
                            if (i == 0) {
                                cadenaInSert += "'" + datos[i] + "'";
                            } else {
                                //System.out.print("'"+datos[i].trim()+"'"+ "," );
                                cadenaInSert += ",'" + datos[i] + "'";
                            }
                        }
                    }//else
                    cadenaInSert += "),\n";//siempre va a terminar igual
                }
               
               
            if( acum % 150 == 0 || (line = br.readLine()) == null ){//&& acum != 0 
                cadenaInSert = cadenaInSert.replaceFirst(".$", "");
               // cont.GuardaTurno(cadenaInSert);
                System.out.print(cadenaInSert);
                cadenaInSert = "";
            }
             acum++;//sirmpre va a avanzar
            
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cadenaInSert;
    }

    public int cuentaLines() {
        int acum = 0;
        String csvFile = cad;
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                acum++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return acum;
    }

    public static void main(String[] argv) {
        leeCSV lee1 = new leeCSV();

        System.out.println("Tiene ln : " + lee1.cuentaLines());
        System.out.println(lee1.procesaAll2());

          //cont.GuardaTurno(lee1.procesaAll2());
    }//main

}

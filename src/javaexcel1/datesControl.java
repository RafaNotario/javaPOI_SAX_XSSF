

package javaexcel1;

//import com.toedter.calendar.JCalendar;
//import com.toedter.calendar.JDateChooser;
import conexion.ConexionDBOriginal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 *
 * @author Antonio R. Notario Rodriguez
 */
public class datesControl {
    
 
//formato para date sql
    SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
    
    SimpleDateFormat formatoPrueba = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat formatGuion = new SimpleDateFormat("yyyy-MM-dd");
     //****USO DE FECHAS
    public String setDateActualGuion(){
    //        DateFormat df = DateFormat.getDateInstance();
        Date fechaAct = new Date();    
    //        jDateChooser1.setDate(fechaAct);
            return formatGuion.format(fechaAct);
    }
    
    public Date StringDate(String fecha){//tenia: java.util.Date
    //    SimpleDateFormat formato_texto = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaE = null;
        try{
            fechaE = formato.parse(fecha);
            return fechaE;
        }catch(ParseException ex){
            return null;
        }
}
   
/*    
    public String getFechaCal(JCalendar jd){
    if(jd.getDate()!= null){
        return formato.format(jd.getDate());
    }else{
        return null;
    }
}
    
public String getFecha(JDateChooser jd){
    if(jd.getDate()!= null){
        return formato.format(jd.getDate());
    }else{
        return null;
    }
}
*/
    
    public Date cargafecha() {
        Date fechaAct = new Date();
        return fechaAct;
    }
    
public String setDateActual(){
//        DateFormat df = DateFormat.getDateInstance();
    Date fechaAct = new Date();   
//        jDateChooser1.setDate(fechaAct);
        return formato.format(fechaAct);
}

        public String volteaFecha(String cad,int opc){//opc=0:dd/mm/YYYY; opc=1:YYYY/mm/dd
            //cad = "16/06/2017";
            char var[];
            var = cad.toCharArray();
            String p1 ="",p2="",p3="",newFech="";
            if(opc == 0)
            {
                for(int i = 0; i< var.length;i++){
                    if(i<2)
                        p1+=var[i];
                
                    if(i>2 && i<5)
                        p2+=var[i];
                
                    if(i>5 && i<var.length)
                        p3+=var[i];
                }
            }
            if(opc == 1)
            {
                for(int i = 0;i<var.length;i++){
                    if(i<4)
                        p1+=var[i];
                    
                    if(i>4 && i<7)
                        p2+=var[i];
                    
                    if(i>7 && i<var.length)
                        p3+=var[i];
                }
            }
            newFech=p3+"/"+p2+"/"+p1;
            return newFech;
        }

    /**
 * Calcula la diferencia entre dos fechas. Devuelve el resultado en días, meses o años según sea el valor del parámetro 'tipo'
 * @param fechaInicio Fecha inicial
 * @param fechaFin Fecha final
 * @param tipo 0=TotalAños; 1=TotalMeses; 2=TotalDías; 3=MesesDelAnio; 4=DiasDelMes
 * @return numero de días, meses o años de diferencia
 */
    public long getDiffDates(Date fechaInicio, Date fechaFin, int tipo) {
	// Fecha inicio
	Calendar calendarInicio = Calendar.getInstance();
	calendarInicio.setTime(fechaInicio);
	int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
	int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	int anioInicio = calendarInicio.get(Calendar.YEAR);
	// Fecha fin
	Calendar calendarFin = Calendar.getInstance();
	calendarFin.setTime(fechaFin);
	int diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
	int mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	int anioFin = calendarFin.get(Calendar.YEAR);
	int anios = 0;
	int mesesPorAnio = 0;
	int diasPorMes = 0;
	int diasTipoMes = 0;
	//
	// Calculo de días del mes
	//
	if (mesInicio == 2) {
		// Febrero
		if ((anioFin % 4 == 0) && ((anioFin % 100 != 0) || (anioFin % 400 == 0))) {
			// Bisiesto
			diasTipoMes = 29;
		} else {
			// No bisiesto
			diasTipoMes = 28;
		}
	} else if (mesInicio <= 7) {
		// De Enero a Julio los meses pares tienen 30 y los impares 31
		if (mesInicio % 2 == 0) {
			diasTipoMes = 30;
		} else {
			diasTipoMes = 31;
		}
	} else if (mesInicio > 7) {
		// De Julio a Diciembre los meses pares tienen 31 y los impares 30
		if (mesInicio % 2 == 0) {
			diasTipoMes = 31;
		} else {
			diasTipoMes = 30;
		}
	}
	//
	// Calculo de diferencia de año, mes y dia
	//
	if ((anioInicio > anioFin) || (anioInicio == anioFin && mesInicio > mesFin)
			|| (anioInicio == anioFin && mesInicio == mesFin && diaInicio > diaFin)) {
		// La fecha de inicio es posterior a la fecha fin
		// System.out.println("La fecha de inicio ha de ser anterior a la fecha fin");
		return -1;
	} else {
		if (mesInicio <= mesFin) {
			anios = anioFin - anioInicio;
			if (diaInicio <= diaFin) {
				mesesPorAnio = mesFin - mesInicio;
				diasPorMes = diaFin - diaInicio;
			} else {
				if (mesFin == mesInicio) {
					anios = anios - 1;
				}
				mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
				diasPorMes = diasTipoMes - (diaInicio - diaFin);
			}
		} else {
			anios = anioFin - anioInicio - 1;
//			System.out.println(anios);
			if (diaInicio > diaFin) {
				mesesPorAnio = mesFin - mesInicio - 1 + 12;
				diasPorMes = diasTipoMes - (diaInicio - diaFin);
			} else {
				mesesPorAnio = mesFin - mesInicio + 12;
				diasPorMes = diaFin - diaInicio;
			}
		}
	}
	//System.out.println("Han transcurrido " + anios + " Años, " + mesesPorAnio + " Meses y " + diasPorMes + " Días.");		
	//
	// Totales
	//
	long returnValue = -1;
 
	switch (tipo) {
		case 0:
			// Total Años
			returnValue = anios;
			// System.out.println("Total años: " + returnValue + " Años.");
			break;
		case 1:
			// Total Meses
			returnValue = anios * 12 + mesesPorAnio;
			// System.out.println("Total meses: " + returnValue + " Meses.");
			break;
 
		case 2:
			// Total Dias (se calcula a partir de los milisegundos por día)
			long millsecsPerDay = 86400000; // Milisegundos al día
			returnValue = (fechaFin.getTime() - fechaInicio.getTime()) / millsecsPerDay;
			// System.out.println("Total días: " + returnValue + " Días.");
			break;
		case 3:
			// Meses del año
			returnValue = mesesPorAnio;
			// System.out.println("Meses del año: " + returnValue);
			break;
		case 4:
			// Dias del mes
			returnValue = diasPorMes;
			// System.out.println("Dias del mes: " + returnValue);
			break;
		default:
			break;
	}
	return returnValue;
}//@end getdiffdates
        
public int semanYear(String dat,int band){
    Calendar calendar = Calendar.getInstance();
    calendar.setFirstDayOfWeek( Calendar.MONDAY);//MONDAY
   calendar.setMinimalDaysInFirstWeek(1);
    calendar.setTime(StringDate(dat));
    
    int  ret=0;
    if(band == 0){//regresa numero de semana en el año
        //calendar.setFirstDayOfWeek( Calendar.SUNDAY);//MONDAY
        ret = calendar.get(Calendar.WEEK_OF_YEAR);      
    }
    if(band == 1){//regresa numero de dia se la semana
       calendar.setFirstDayOfWeek( 0);//MONDAY
        ret = calendar.get(Calendar.DAY_OF_WEEK);        
    } 
    //System.out.printf("%s %n", DayOfWeek.MONDAY);//
    
    return ret;
}

    public String numSemanaLocal(){
        String letrero = "Semana ";
        Locale l = new Locale("es","MX");//"es","MX" 
       DateFormat dfE = DateFormat.getDateInstance(DateFormat.FULL, l);//instancia para escribir fechotas jajaja
       Calendar cF = Calendar.getInstance(l);
           
        System.out.println("WeekofYear : "+ cF.get(Calendar.WEEK_OF_YEAR)+" Calendar"+Calendar.SUNDAY);
        return letrero + cF.get(Calendar.WEEK_OF_YEAR);
    }
    
    public void jLocalFechas (String fech){
//       DateFormat dfE = DateFormat.getDateInstance(DateFormat.FULL, l);//instancia para escribir fechotas jajaja
       //DayOfWeek lunes = DayOfWeek.MONDAY; 
       Locale l = new Locale("es","MX");//"es","MX" 
       DateFormat dfE = DateFormat.getDateInstance(DateFormat.FULL, l);//instancia para escribir fechotas jajaja
       Calendar cF = Calendar.getInstance(Locale.FRANCE);
      System.out.println("first dayWeek:" + cF.getFirstDayOfWeek());
       cF.setFirstDayOfWeek(Calendar.MONDAY);
      System.out.println("first dayWeek2:" + cF.getMinimalDaysInFirstWeek());
       cF.setTime(StringDate(fech));
       System.out.println("TextStyle.FULL:" + cF.getDisplayName(Calendar.MONTH,Calendar.SHORT, l) + " "+cF.get(Calendar.DAY_OF_MONTH)); 
      // System.out.println("TextStyle.NARROW:" + lunes.getDisplayName(TextStyle.NARROW, l)); 
       //System.out.println("TextStyle.SHORT:" + lunes.getDisplayName(TextStyle.SHORT, l)); 
       System.out.println("FORMATOTE: "+dfE.format(StringDate(fech)));//new Date()
       System.out.println("DayWeek: "+ cF.get(Calendar.DAY_OF_WEEK) );
       System.out.println("WeekofYear : "+ cF.get(Calendar.WEEK_OF_YEAR)+" Calendar"+Calendar.SUNDAY);
       cF.add(Calendar.WEEK_OF_YEAR, 1);
       System.out.println("WeekofYear +1: "+ cF.get(Calendar.WEEK_OF_YEAR));
}
    
    public String getWeekStartDate(String fech) {
       String fec = fech.replace('-', '/');
         Locale l = new Locale("es","MX");//"es","MX" 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(StringDate(fec));
      //  while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
          //  calendar.add(Calendar.DATE, -1);
        //}
    //return calendar.getTime();
        return calendar.get(Calendar.DAY_OF_MONTH)+ " "+calendar.getDisplayName(Calendar.MONTH,Calendar.SHORT, l)+"." ;
    }

public String getWeekEndDate(String fech) {
    Locale l = new Locale("es","MX");
    Calendar calendar = Calendar.getInstance(l);
    //calendar.setTime(StringDate(fech));
    System.out.println("Dia es: "+ Calendar.DAY_OF_WEEK +"Llegra"+ Calendar.MONDAY);
    
    if(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
    {
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -1);
    }else {
        calendar.add(Calendar.DATE,  6);
    }    
    //return calendar.getTime();
    return calendar.get(Calendar.DAY_OF_MONTH) + " "+calendar.getDisplayName(Calendar.MONTH,Calendar.SHORT, l) +". "+calendar.get(Calendar.YEAR);
}

//funcion que dado un numero devuelva lapso de semana
public String lapsoSem(String fech,int numSem){
    Calendar c = Calendar.getInstance();
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.setMinimalDaysInFirstWeek(1);
    String var = "";
    int mes =0;
    String mesU ="";
    c.clear();
    c.set(Calendar.YEAR, 2049);
    c.set(Calendar.WEEK_OF_YEAR, 40);
    mes =c.get(Calendar.MONTH);
    mes+=1;
    if(Integer.toString(mes).length() ==1){
        mesU="0"+Integer.toString(mes);
    }else{
        mesU=Integer.toString(mes);
    }
    var= Integer.toString(c.get(Calendar.DAY_OF_MONTH))+"/"+mesU+"/"+Integer.toString(c.get(Calendar.YEAR));
   // System.out.printf("Fecha del Lunes: %te-%<tm-%<ty %n", c.getTime());
 System.out.println("quedo "+var);
    // System.out.printf("Semana del mes (%tB %<tY): %d%n", c.getTime(), c.get(Calendar.WEEK_OF_YEAR));
    c.add(Calendar.DAY_OF_YEAR, 6);
    //System.out.printf("Fecha del Domingo: %te - %<tm - %<ty %n", c.getTime());
    //System.out.printf("Semana del mes (%tB %<tY): %d%n", c.getTime(), c.get(Calendar.WEEK_OF_YEAR));
    return var;
}

public String getHour(){
    Calendar calendario = Calendar.getInstance();    
    int hora, minutos, segundos;
    hora =calendario.get(Calendar.HOUR_OF_DAY);
    minutos = calendario.get(Calendar.MINUTE);
    segundos = calendario.get(Calendar.SECOND);
    String hor = Integer.toString(hora)+":"+Integer.toString(minutos)+":"+Integer.toString(segundos);
    return hor;
}
 
public String getSumFechDay(String fech, int nDays){
        Date prox = null;
        Calendar cal = Calendar.getInstance(); 
//obtenemos la fecha actual y la convertmos a date
        cal.setTime(StringDate(fech));
        cal.add(Calendar.DAY_OF_YEAR, nDays);
        prox=cal.getTime();
        return formato.format(prox);
    }
/*
public String getsumaFecha(JDateChooser jd,int monts){
      Date prox = null;
      Calendar cal = Calendar.getInstance(); 
      
    if(jd.getDate()!= null){
        cal.setTime(StringDate(formato.format(jd.getDate())));
        cal.add(Calendar.MONTH,monts);
        prox=cal.getTime();
        return formato.format(prox);
    }else{
        return null;
    }
}
*/
    public static void main(String argv[]){
        datesControl dC = new datesControl();
        
//        System.out.println("Semana del año es: "+dC.semanYear("2020/04/13",0)+"\t dia de la semana: "+dC.semanYear("2020/04/13",1));
          
     System.out.println(dC.getSumFechDay("2020/04/1",-1));
        System.out.println("PRUEBAS DE STACKOVERFLOW");
        
        //System.out.println("La hora es : "+dC.getsumaFecha("2020/06/05",-1));
        
       // System.out.println("Semana fin: "+dC.getWeekEndDate("2020/03/23"));
        //System.out.println("Lapso"+dC.lapsoSem("",1));
       // dC.lapsoSem("",0);
        
    }//finMain

}//finClass
/*
run:
THURSDAY
THURSDAY
Semana del año es: 16	 dia de la semana: 2
*/

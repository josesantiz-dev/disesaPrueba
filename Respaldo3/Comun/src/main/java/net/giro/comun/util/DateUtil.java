package net.giro.comun.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static final String fmtParseFecha = "MM/dd/yyyy HH:mm:ss"; 
	public static final String fmtParseFechaSinHora = "MM/dd/yyyy";
	public static final String fmtSalidaFechaWeb = "dd / MMM / yyyy";
	public static final String fmtSalidaFecha = "dd/MMM/yyyy";
	public static final String fmtSalidaFechaBuro = "ddMMyyyy";
	
	static SimpleDateFormat sdf = new SimpleDateFormat(fmtParseFecha); 
	static SimpleDateFormat sdfSinHora = new SimpleDateFormat(fmtParseFechaSinHora); 
	static SimpleDateFormat sdfBuro = new SimpleDateFormat(fmtSalidaFechaBuro);
	/**
	 * Metodo que devuelve si la fecha1 es anterior a la fecha2 
	 * @param fecha1, fecha2
	 * @return boolean
	 */
	
	public static boolean fechaAntes(Date fecha1, Date fecha2) throws ParseException{
		
		String fechaActual = "";
		String fechaAComparar = "";
		
		Date fechaUno = null;
		Date fechaDos = null;
		
		if(fecha1 != null && fecha2 != null){
			
			fechaActual = sdfSinHora.format(fecha1);
			fechaAComparar = sdfSinHora.format(fecha2);
			
			fechaUno = sdfSinHora.parse(fechaActual);
			fechaDos = sdfSinHora.parse(fechaAComparar);
			
			if (fechaUno.before(fechaDos))
				return true;
			else
				return false;
			
		}else{
			
			return false;
		
		}
		
	}
	
	/**
	 * Metodo que devuelve si la fecha1 es posterior a la fecha2
	 * @param fecha1, fecha2
	 * @return boolean
	 */
	public static boolean fechaDespues(Date fecha1, Date fecha2) throws ParseException{
		
		String fechaActual = "";
		String fechaAComparar = "";
		
		Date fechaUno = null;
		Date fechaDos = null;
		
		if(fecha1 != null && fecha2 != null){
			
			fechaActual = sdfSinHora.format(fecha1);
			fechaAComparar = sdfSinHora.format(fecha2);
			fechaUno = sdfSinHora.parse(fechaActual);
			fechaDos = sdfSinHora.parse(fechaAComparar);
			
			if (fechaUno.after(fechaDos))
				return true;
			else
				return false;
			
		}else{
			
			return false;
		
		}
			
	}
	
	/**
	 * Metodo que devuelve el dia de la semana en letra (ej: Lunes, Martes, etc) 
	 * @param dia: Long del dia empezando con 1 = Domingo
	 * @return String
	 */
	public static String diaSemana(int dia){
		String res = "";
		switch (dia) {
		case 1:
			res = "Domingo";
			break;
		case 2:
			res = "Lunes";
			break;
		case 3:
			res = "Martes";
			break;
		case 4:
			res = "Miercoles";
			break;
		case 5:
			res = "Jueves";
			break;
		case 6:
			res = "Viernes";
			break;
		case 7:
			res = "Sabado";
			break;
		default:
			res = "Dia Invalido";
			break;
		}
		return res;
	}
	
	/**
	 * 
	 * @param fecInicio primera fecha
	 * @param fecFinal segunda fecha
	 * @return numero de dias de diferencia entre las fechas
	 */
	public static int getDifDiasFechas(Date fecInicio, Date fecFinal){
		Long tmp = 0L;
		long val = 0;
		
		val = 1000 * 60 * 60 * 24;

		tmp = (fecFinal.getTime() / val) - (fecInicio.getTime() / val); 
		
		return tmp.intValue();
	}

	/**
	 * Metodo que devuelve el mes del año en letra (ej: Enero, Febrero, etc) 
	 * @param mes: Long del mes empezando con 1 = Enero
	 * @return String
	 */
	
	public static String mesALetra(int mes){
		String res = "";
		switch (mes) {
		case 1:
			res = "Enero";
			break;
		case 2:
			res = "Febrero";
			break;
		case 3:
			res = "Marzo";
			break;
		case 4:
			res = "Abril";
			break;
		case 5:
			res = "Mayo";
			break;
		case 6:
			res = "Junio";
			break;
		case 7:
			res = "Julio";
			break;
		case 8:
			res = "Agosto";
			break;
		case 9:
			res = "Septiembre";
			break;
		case 10:
			res = "Octubre";
			break;
		case 11:
			res = "Noviembre";
			break;
		case 12:
			res = "Diciembre";
			break;
		default:
			res = "Mes no existe";
			break;
		}
		return res;
	}
	
	public static boolean isFechaValida(String fecha) {

	    if (fecha == null)
	      return false;

	    //set the format to use as a constructor argument "dd-MM-yyyy" 
	    SimpleDateFormat dateFormat = new SimpleDateFormat(fmtParseFecha);
	    
	    if (fecha.trim().length() != dateFormat.toPattern().length())
	      return false;

	    dateFormat.setLenient(false);
	    
	    try {
	    	dateFormat.parse(fecha.trim());
	    }catch (ParseException pe) {
	      return false;
	    }
	    return true;
	  }
	
	public static Date stringToDate(String fecha) {

	    try {
	    	//parse the inDate parameter
	    	return  sdf.parse(fecha.trim());
	    }catch (ParseException pe) {
	    	pe.printStackTrace();
	    	return null;
	    }
	   
	  }
	
	public static Date stringToDate(String fecha,String formato) {
	    try {
	    	return   new SimpleDateFormat(formato).parse(fecha); 
	    }catch (ParseException pe) {
	    	pe.printStackTrace();
	    	return null;
	    }
	  }
	
	public static String  StringToDateStringFrm(String fecha,String formato) {
		Date dt;
		String frm= "";		
		int dia;
		int mes;
		int anio;
		int hora;
		int min;
		int sec;
		
		try {
			if (fecha== null || "".equals(fecha)) {
				return "";
			}	    	
			switch (formato) {
			case "DMYHMS" :
				dt=   new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(fecha);	    		
				break;
			case "DMYHM" :
				dt=   new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(fecha);	    		
				break;				
			default :
				dt=   new SimpleDateFormat("MM/dd/yyyy").parse(fecha);	    		
				break;	    			    		
			}
			Calendar fechaConvertir = Calendar.getInstance();	    		
			fechaConvertir.setTime(dt);
			dia= fechaConvertir.get(Calendar.DAY_OF_MONTH);
			mes = fechaConvertir.get(Calendar.MONTH)+1;
			anio = fechaConvertir.get(Calendar.YEAR);
			hora = fechaConvertir.get(Calendar.HOUR_OF_DAY);
			min = fechaConvertir.get(Calendar.MINUTE);
			sec = fechaConvertir.get(Calendar.SECOND);
			
			frm =String.format("%02d/%s/%d", dia, mesALetra(mes).substring(0,3), anio);
			
			switch (formato) {
			case "DMYHMS" :
				frm =String.format("%02d/%s/%d %02d:%02d:%02d", dia, mesALetra(mes).substring(0,3), anio, hora, min, sec);	    		
				break;
			case "DMYHM" :
				frm =String.format("%02d/%s/%d %02d:%02d", dia, mesALetra(mes).substring(0,3), anio, hora, min);	    		
				break;				
			default :
				frm =String.format("%02d/%s/%d", dia, mesALetra(mes).substring(0,3), anio);    		
				break;	    			    		
			}

		}
		catch (ParseException pe) {
			pe.printStackTrace();
			frm= "";
		}
		return frm;
	  }
	
	public static String dateToString(Date fecha) {

	    try {
	    	return  sdf.format(fecha);
	    }catch (Exception e) {
	    	return null;
	    }
	   
	  }
	
	public static String dateToStringBuro(Date fecha) {
	    try {
	    	return  sdfBuro.format(fecha);
	    }catch (Exception e) {
	    	return null;
	    }
	   
	  }

	@SuppressWarnings("static-access")
	public static String fechaCompleta(Date fecha){
		
		String fechaCompleta = "";
		
		int dia;
		int mes;
		int anio;
		
		Calendar fechaConvertir = Calendar.getInstance();
		
		fechaConvertir.setTime(fecha);
		
		dia = fechaConvertir.DAY_OF_WEEK;
		mes = fechaConvertir.MONTH;
		anio = fechaConvertir.YEAR;
		
		fechaCompleta = diaSemana(dia) + " " + dia + " de " + mesALetra(mes) + " de " + anio;
		
		return fechaCompleta;
	}
	
	public static java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}
	
	public static String dateToStringDDMMYYYYHHMMSS(Date date){
		if (date == null)
			return null;

		return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(date);
	}
	
	public static String dateToStringMMDDYYYYHHMMSS(Date date){
		if (date == null)
			return null;

		return new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(date);
	}
	
	public static java.sql.Timestamp getCurrentTimeStamp(String date) {
		 SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		
		java.util.Date today = new java.util.Date();
		 try {
			today = formato.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        } 
		return new java.sql.Timestamp(today.getTime());
	}
		
	public static java.sql.Timestamp getCurrentTimeStampUS(String date) {
		 SimpleDateFormat formato = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		
		java.util.Date today = new java.util.Date();
		 try {
			today = formato.parse(date);
       } catch (ParseException ex) {
           ex.printStackTrace();
       } 
		return new java.sql.Timestamp(today.getTime());
	}

	public static Date dateDDMMYYYY(Date date){
		if (date == null)
			return null;

			try {
				return new SimpleDateFormat("dd/MM/yyyy").parse(dateToStringDDMMYYYYHHMMSS(date));
			} catch (ParseException e) {
				e.printStackTrace();
				return date;
			}
	}
}

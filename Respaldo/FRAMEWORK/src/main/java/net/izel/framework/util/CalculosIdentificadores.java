package net.izel.framework.util;

import java.util.Calendar;

public class CalculosIdentificadores 
{

	/**
	 * 
	 * @param strReferencia
	 * @return
	 */
	public String  DigitoVerificador(String strReferencia)
	{
		String strNumero;
		Short bytNumero;
		Integer intInter;
		Integer intTotal ;
		Integer intI;
		
		intTotal= 0;
		bytNumero= 2;
		if (strReferencia.trim().length() > 3) {
			for (int i=0 ; i < strReferencia.length() ; i++) {
				strNumero=  String.valueOf( Short.parseShort( strReferencia.substring(i, i+1) ) *  bytNumero ); 
				strNumero= strNumero.trim();
				
				if (strNumero.length() > 1) {					
					intInter= Integer.parseInt(strNumero.substring(0,1)  + strNumero.substring(1,1));
				}
				else {
					intInter= Integer.parseInt(strNumero);
				}				
				// System.out.println(intInter);				 
				intTotal += intInter;
				bytNumero= (short) ((bytNumero==2) ? 1 : 2);
				
			}
			//System.out.println(intTotal);
			if ((intTotal % 10)== 0) {
				strReferencia += "0";
			}
			else {
				strNumero=  String.valueOf( intTotal );
				intI = Integer.parseInt(strNumero.substring(0,1));
				strNumero=  String.valueOf( intI +1 ) + "0";
				//System.out.println(strNumero);
				intInter= Integer.parseInt(strNumero);				
				strReferencia += String.valueOf( intInter-intTotal );
			}
			
		}		
		return strReferencia;
	}
	
	/**
	 * Algoritmo de BANXICO para calcular digito verificador
	 * @param cuentaClabe
	 * @return
	 */
	public String speiDigitoVerificador(String cuentaClabe)
	{
		String cadena[] ={"3","7","1","3","7","1","3","7","1","3","7","1","3","7","1","3","7"};
		String scuentaClave;
		Integer ifactor;
		Integer idigito;
		Integer imultiplo;
		Integer imodulo;
		Integer ioperacion;
		String  idigitoverified;
		
		idigitoverified= "";
		if (cuentaClabe.trim().length()>= 17 && cuentaClabe.trim().length() <= 18) {
			scuentaClave= cuentaClabe.substring(0,17);
			ioperacion= 0;
			for (int i= 0 ; i < 17 ; i ++) {
				ifactor= Integer.parseInt( cadena[i] );
				idigito= Integer.parseInt( scuentaClave.substring(i, i + 1) );
				imultiplo= ifactor * idigito;
				imodulo  = imultiplo % 10;
				ioperacion = ioperacion + imodulo;            
			}
			ioperacion = ioperacion % 10;
			ioperacion =  10 - ioperacion;
		    idigitoverified = String.valueOf( ioperacion % 10 );         
			
		}
		else {
			idigitoverified= "*";
		}
		
		return idigitoverified;
		
	}
	
	
    public boolean DigitoVerificadorValido( String cuentaClabe ) 
    {
    	String sdigito;
    	boolean hr;
    
    	sdigito= speiDigitoVerificador(cuentaClabe);
		hr=  ( sdigito!= null && sdigito.equals(cuentaClabe .substring(cuentaClabe .length()-1)) ) ? true : false ;		
        return hr;					
    }
	
	/**
	 * 
	 * @return
	 */
	public String speiPlazaInstitucion () 
	{
		String plazaIns;
		
		plazaIns= "";		
		try {		
			ReferenciaSPEI.procesa();
			plazaIns= String.format("%s%s", ReferenciaSPEI.getClaveInstitucion(), ReferenciaSPEI.getClavePlaza());
		}
		catch (Exception ex) {
			plazaIns= "";
		}
		return plazaIns;
	}
	
	
	public static String  DigitoVerificadorReferencia(String strReferencia)
	{
		String strNumero;
		Integer intTotal ;
		int intI;
		int ival;
		int tmp;
		Short bytNumero;
		
		intTotal= 0;
		bytNumero= 2;
		if (strReferencia.trim().length() > 3) {
			for (int i=0 ; i < strReferencia.length() ; i++) {
				strNumero=  String.valueOf( Short.parseShort( strReferencia.substring(i, i+1) )); 
				ival = (int)Integer.valueOf(strNumero.trim());
				ival *= bytNumero;
				
				if (ival > 10) {					
					tmp= 1 + (ival-10);
				}
				else {
					tmp= ival;
				}				
				intTotal += tmp;
				bytNumero= (short) ((bytNumero==2) ? 1 : 2);
				
			}
			if ((intTotal % 10)== 0) {
				strReferencia += "0";
			}
			else {
				strNumero=  String.valueOf( intTotal );
				intI = Integer.parseInt(strNumero.substring(0,1));
				strNumero=  String.valueOf( intI + 1 ) + "0";
				ival= Integer.parseInt(strNumero);				
				strReferencia += String.valueOf( ival-intTotal );
			}
			
		}		
		return strReferencia;
	}	
	
	
	public static String GeneraDigito()	
	{
		Calendar fechaConvertir = Calendar.getInstance();
		String ref;		
		int dia;
		int min;
		int hora;
		int sec;
		
		fechaConvertir.setTime(Calendar.getInstance().getTime());
		dia = fechaConvertir.get(Calendar.DAY_OF_YEAR);
		hora= fechaConvertir.get(Calendar.HOUR_OF_DAY);
		min= fechaConvertir.get(Calendar.MINUTE);
		sec= fechaConvertir.get(Calendar.SECOND);
		
		int total= dia + hora + min + sec;
		ref= DigitoVerificadorReferencia (String.valueOf(total));
		if (ref!= null && !"".equals(ref)) {
			ref= ref.substring(ref.length()-1);
		}
		else {
			ref="0";
		}
		return ref;
	}
}


/** !CalculosIdentificadores */


package net.giro.comun.util;

import java.text.ParseException;

public class FuncionesCalculos {
	
	/**
	 * Metodo que devuelve el tiempo calculado en base a diasFrecuencia, numeroCuotas y diasConvertir.
	 * @param Long diasFrecuencia, Long numeroCuotas, Long diasConvertir
	 * @return Long tiempo
	 */
	
	public static Long CalcularTiempo(Long diasFrecuencia, Long numeroCuotas, Long diasConvertir) throws ParseException{
		
		Long tiempo = 0l;
		
		if(diasFrecuencia == null ){
			diasFrecuencia = 0l;
		}
		
		if(numeroCuotas == null ){
			numeroCuotas = 0l;
		}	
		
		if(diasConvertir == null || diasConvertir.longValue() <= 0){
			return 0l;
		}		
		
		tiempo = ((diasFrecuencia * numeroCuotas) / diasConvertir);
		
		return tiempo;
		
	}
	
}


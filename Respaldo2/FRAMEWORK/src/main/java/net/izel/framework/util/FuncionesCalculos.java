package net.izel.framework.util;

import java.math.BigDecimal;
import java.text.ParseException;
/*import java.util.Collections;
import java.util.Comparator;
import java.util.List;*/

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

	/**
	 *  Obtener el porcentaje de una cantidad.
	 * @return
	 */
	public static BigDecimal CalcularPorcentaje(BigDecimal montoTotal, BigDecimal montoCalcular)
	{
		BigDecimal montoCalculado;
		if (montoTotal== null || montoTotal.longValue()== 0L) {
			return new BigDecimal("0");
		}
		if (montoCalcular== null) {
			montoCalcular=  new BigDecimal("0");
		}
		montoCalculado= montoCalcular.multiply(new BigDecimal(100));
		montoCalculado= montoCalculado.divide(montoTotal);
		return montoCalculado;
	}


	/**
	 * Obtener el monto a partir de un porcentaje.
	 * @return
	 */
	public static BigDecimal CalcularMontoPorcentaje(BigDecimal porcentaje, BigDecimal montoTotal)
	{
		BigDecimal montoCalculado;
		if (porcentaje== null || porcentaje.longValue()== 0L || montoTotal== null || montoTotal.longValue()== 0L) {
			return new BigDecimal("0");
		}
		montoCalculado= 	montoTotal.multiply(porcentaje);
		montoCalculado= montoCalculado.divide(new BigDecimal(100));
		return montoCalculado;
	}



	/*
	public static List<SelectItem> ordenaLista(List<SelectItem> listado){

		try {
		   Collections.sort(listado, new Comparator<SelectItem>(){
	        	@Override
	        	public int compare(SelectItem s1, SelectItem s2) {
	        		    return s1.getLabel().compareTo(s2.getLabel());
	        	 }
	        });
		} catch (Exception e) {
		}

		return listado;

	}
	 */

	public static BigDecimal round(BigDecimal d, int scale, boolean roundUp) {
		int mode = (roundUp) ? BigDecimal.ROUND_HALF_UP : BigDecimal.ROUND_HALF_DOWN;
		return d.setScale(scale, mode);
	}
	
	public static BigDecimal CalculoTasaReal(BigDecimal montoFinanciado , BigDecimal saldoIntereses, Long diasPlazoPrestamo, Long diasCalculo)
	{
		BigDecimal tasaReal = new BigDecimal(0);
		BigDecimal tasaTotal = new BigDecimal(0);
		BigDecimal interesDiario = new BigDecimal(0);

		if (montoFinanciado == null || diasPlazoPrestamo == 0L) {
			return tasaReal;
		}
		tasaTotal = saldoIntereses;
		tasaTotal = tasaTotal.divide(montoFinanciado, 8, BigDecimal.ROUND_HALF_UP);

		interesDiario = tasaTotal;
		interesDiario = interesDiario.divide(new BigDecimal(diasPlazoPrestamo), 8, BigDecimal.ROUND_HALF_UP);

		tasaReal = (interesDiario.multiply(new BigDecimal(diasCalculo)));
		tasaReal = tasaReal.multiply(new BigDecimal(100));
		tasaReal = round(tasaReal, 8, true); 

		return tasaReal;
	}
	







}


package net.izel.framework.util.pl;

import java.math.BigDecimal;
import java.util.Date;

public class ObtenerReferenciasMedioPago 
{
	public String[] ReferenciaMedioPago( 
			String claveMedioPago, 
			BigDecimal importe, 
			Date fechaValor, 
			String monedaOperacion, 
			String conceptoOperacion, 
			String cuenta, 
			String titular, 
			String producto, 
			String monedaCuenta
			)
	{
		String listReferencias[] = {null, null};
		String referencia1;
		String referencia2;
		
		referencia1= "";
		referencia2= "";
		
		if (claveMedioPago!= null && !"".equals(claveMedioPago)) {
			referencia1 = claveMedioPago;
		}	
		if (fechaValor!= null) {
			referencia1 += String.format(" FECHA : %s", fechaValor) ;
		}
		if (fechaValor!= null) {
			referencia1 += String.format(" IMPORTE : %s", importe) ;
		}
		if (fechaValor!= null) {
			referencia1 += String.format(" MONEDA OPERACION : %s", monedaOperacion) ;
		}
		
		if (!"".equals(referencia1)) {
			listReferencias[0]  = referencia1;
		}			
		
		if (claveMedioPago!= null && "CTACONT".equals(claveMedioPago)) {
			if (conceptoOperacion!= null && !"".equals(conceptoOperacion)) {
				referencia2  = conceptoOperacion;
			}	
		}	
		
		if (claveMedioPago!= null && "ABOCTA".equals(claveMedioPago)) {
			if (cuenta!= null && !"".equals(cuenta)) {
				referencia2 += String.format(" CUENTA : %s", cuenta) ;
			}
			if (titular!= null && !"".equals(titular)) {
				referencia2 += String.format(" TITULAR : %s", titular) ;
			}
			if (producto!= null && !"".equals(producto)) {
				referencia2 += String.format(" PRODUCTO : %s", producto) ;
			}
			if (monedaCuenta!= null && !"".equals(monedaCuenta)) {
				referencia2 += String.format(" MONEDA : %s", monedaCuenta) ;
			}
		}
		
		if (!"".equals(referencia2)) {
			listReferencias[1]  = referencia2;
		}	
		
		return listReferencias;		
	}
}

/** !ObtenerReferenciasMedioPago.java */




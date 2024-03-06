package net.giro.cxp.beans;

public enum TiposPagosGastos {
	/**
	 * P: REGISTRO DE GASTOS (PAGO)
	 */
	REGISTRO_GASTO { public String toString() { return "P"; }},
	/**
	 * C: CAJA CHICA
	 */
	CAJA_CHICA { public String toString() { return "C"; }},
	/**
	 * G: GASTOS A COMPROBAR (GASTO)
	 */
	GASTOS_COMPROBAR { public String toString() { return "G"; }},
	/**
	 * M: MOVIMIENTOS DE CUENTAS
	 */
	MOVIMIENTO_DE_CUENTAS { public String toString() { return "M"; }},
	/**
	 * F: PROVISION DE FACTURAS (FACTURAS)
	 */
	PROVISIONES_FACTURAS { public String toString() { return "F"; }}
}

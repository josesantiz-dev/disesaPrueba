package net.giro.cxp.beans;

public enum TiposGastoCXP {
	/** C - Caja Chica */
	CajaChica { public String toString() { return "C"; }},
	
	/** G - Gasto */
	Gastos { public String toString() { return "G"; }},
	
	/** F - Provision */
	Provision { public String toString() { return "F"; }},
	
	/** P - Registro de Gasto */
	RegistroGasto { public String toString() { return "P"; }},
	
	/** M - Movimiento de Cuentas */
	MovimientoCuentas { public String toString() { return "M"; }},
	
	/** D - Devolucion de Sobrante */
	Devolucion { public String toString() { return "D"; }},
	
	/** R - Reembolso */
	Reembolso { public String toString() { return "R"; }},
	
	/** T - Transferencia Bancaria */
	Transferencia { public String toString() { return "T"; }},
	
	/** Z - ComprobacionGastos */
	ComprobacionGastos { public String toString() { return "Z"; }},
	
	/** X - Desconocido */
	Desconocido { public String toString() { return "X"; }};
	
	public static TiposGastoCXP fromString(String value) {
		for (TiposGastoCXP item : TiposGastoCXP.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TiposGastoCXP fromOrdinal(int value) {
		for (TiposGastoCXP item : TiposGastoCXP.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}

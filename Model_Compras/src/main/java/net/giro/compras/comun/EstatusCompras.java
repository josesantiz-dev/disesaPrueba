package net.giro.compras.comun;

public enum EstatusCompras {
	/** Activa
	 * @value 0
	 * @category Compras
	 */
	ACTIVA { public String toString() { return "Activa"; }},
	
	/** Cancelada
	 * @value 1
	 * @category Compras
	 */
	CANCELADA { public String toString() { return "Cancelada"; }},
	
	/** Suministrada
	 * @value 2
	 * @category Compras
	 */
	SUMINISTRADA { public String toString() { return "Suministrada"; }};
	
	public static EstatusCompras fromString(String value) {
		for (EstatusCompras item : EstatusCompras.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static EstatusCompras fromOrdinal(int value) {
		for (EstatusCompras item : EstatusCompras.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}

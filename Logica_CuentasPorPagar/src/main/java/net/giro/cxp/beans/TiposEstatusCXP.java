package net.giro.cxp.beans;

public enum TiposEstatusCXP {
	/** C - Comprobado */
	Comprobado { public String toString() { return "C"; }},
	
	/** G - Generado */
	Generado { public String toString() { return "G"; }},

	/** P - Pagado? */
	Pagado { public String toString() { return "P"; }},

	/** A - Aprobado? */
	Aprobado { public String toString() { return "A"; }},
	
	/** X - Cancelado */
	Cancelado { public String toString() { return "X"; }};
	
	public static TiposEstatusCXP fromString(String value) {
		for (TiposEstatusCXP item : TiposEstatusCXP.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TiposEstatusCXP fromOrdinal(int value) {
		for (TiposEstatusCXP item : TiposEstatusCXP.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}

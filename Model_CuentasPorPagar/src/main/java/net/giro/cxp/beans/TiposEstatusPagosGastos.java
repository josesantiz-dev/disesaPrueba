package net.giro.cxp.beans;

public enum TiposEstatusPagosGastos {
	/**
	 * C: COMPROBADO
	 */
	COMPROBADO { public String toString() { return "C"; }},
	/**
	 * X: CANCELADO
	 */
	CANCELADO { public String toString() { return "X"; }},
	/**
	 * G: GENERADO
	 */
	GENERADO { public String toString() { return "G"; }}
}

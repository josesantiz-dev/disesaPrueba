package net.giro.comparativa;

public enum MovimientoReferencia {
	/**
	 * ORDEN DE COMPRA
	 */
	ORDEN_COMPRA { public String toString() { return "OC"; }},
	/**
	 * TRASPASO A BODEGA
	 */
	TRASPASO_BODEGA { public String toString() { return "TR"; }},
	/**
	 * DEVOLUCION A ALMACEN
	 */
	DEVOLUCION_ALMACEN { public String toString() { return "DE"; }},
	/**
	 * SALIDA A OBRA
	 */
	SALIDA_OBRA { public String toString() { return "SO"; }},
	/**
	 * DEVOLUCION A BODEGA
	 */
	DEVOLUCION_BODEGA { public String toString() { return "DX"; }},
	/**
	 * TRASPASO A ALMACEN
	 */
	TRASPASO_ALMACEN { public String toString() { return "TX"; }}
}

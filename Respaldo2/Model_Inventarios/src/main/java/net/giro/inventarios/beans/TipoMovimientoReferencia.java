package net.giro.inventarios.beans;

public enum TipoMovimientoReferencia {
	/**
	 * OC: ORDEN DE COMPRA
	 */
	ORDEN_COMPRA { public String toString() { return "OC"; }},
	/**
	 * TR: TRASPASO DE ALMACEN A BODEGA
	 */
	TRASPASO { public String toString() { return "TR"; }},
	/**
	 * DE: DEVOLUCION DE BODEGA A ALMACEN
	 */
	DEVOLUCION { public String toString() { return "DE"; }},
	/**
	 * SO: SALIDA A OBRA
	 */
	SALIDA_OBRA { public String toString() { return "SO"; }}/*,
	/ * *
	 * DX: DEVOLUCION DE OBRA A BODEGA
	 * /
	DEVOLUCION_BODEGA { public String toString() { return "DX"; }},
	/ * *
	 * TX: TRASPASO DE ALMACEN A ALMACEN
	 * /
	TRASPASO_ALMACEN { public String toString() { return "TX"; }}*/
}

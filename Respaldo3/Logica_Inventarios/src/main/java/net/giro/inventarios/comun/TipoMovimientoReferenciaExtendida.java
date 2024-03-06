package net.giro.inventarios.comun;

public enum TipoMovimientoReferenciaExtendida {
	/**
	 * OC: ORDEN DE COMPRA A ALMACEN
	 */
	ORDEN_COMPRA_ALMACEN { public String toString() { return "OC"; }},
	/**
	 * CB: ORDEN DE COMPRA A BODEGA. Genera: OC,TR
	 */
	ORDEN_COMPRA_BODEGA { public String toString() { return "CB"; }},
	/**
	 * TR: TRASPASO DE ALMACEN A BODEGA
	 */
	TRASPASO_ALMACEN_BODEGA { public String toString() { return "TR"; }},
	/**
	 * TX: TRASPASO DE ALMACEN A ALMACEN
	 */
	TRASPASO_ALMACEN_ALMACEN { public String toString() { return "TX"; }},
	/**
	 * TZ: TRASPASO DE BODEGA A BODEGA (Misma sucursal). Genera: DE,TR
	 */
	TRASPASO_BODEGA_BODEGA { public String toString() { return "TZ"; }},
	/**
	 * SB: SOLICITUD A BODEGA
	 */
	SOLICITUD_BODEGA { public String toString() { return "SB"; }},
	/**
	 * DE: DEVOLUCION DE BODEGA A ALMACEN
	 */
	DEVOLUCION_BODEGA_ALMACEN { public String toString() { return "DE"; }},
	/**
	 * SO: DEVOLUCION DE OBRA A BODEGA
	 */
	DEVOLUCION_OBRA_BODEGA { public String toString() { return "SO"; }},
	/**
	 * SO: SALIDA A OBRA
	 */
	SALIDA_OBRA { public String toString() { return "SO"; }}
}

package net.giro.plataforma.topics;

public enum TopicEventosCompras {
	/** Actualizar estatus
	 * @value BO-RQ
	 * @param target idRequisicion
	 * @param referencia idOrdenCompra
	 * @param atributos productos (Lista Long:Double)
	 * @category BackOffice
	 */
	REQUISICION_ESTATUS { public String toString() { return "BO-RQ"; }},

	/** Aumentar cantidad en requisicion
	 * @value COM-RQ-CANTIDAD
	 * @param target idRequisicion
	 * @param atributos productos (Lista Long:Double)
	 * @category BackOrder
	 */
	REQUISICION_CANTIDAD { public String toString() { return "COM-RQ-CANTIDAD"; }},
	
	/** Actualizar el suministro
	 * @value BO-SR
	 * @param target idRequisicion
	 * @param atributos productos (Lista Long:Double)
	 * @category BackOrder
	 */
	REQUISICION_SUMINISTRO { public String toString() { return "BO-SR"; }},
	
	/** Actualizar estatus
	 * @value BO-CO
	 * @param target idCotizacion
	 * @param referencia idOrdenCompra
	 * @param atributos productos (Lista Long:Double)
	 * @category BackOffice
	 */
	COTIZACION_ESTATUS { public String toString() { return "BO-CO"; }},

	/** Aumentar cantidad en Cotizacion
	 * @value COM-CO-CANTIDAD
	 * @param target idCotizacion
	 * @param atributos productos (Lista Long:Double)
	 * @category BackOrder
	 */
	COTIZACION_CANTIDAD { public String toString() { return "COM-CO-CANTIDAD"; }},
	
	/** Actualizar el suministro
	 * @value BO-SC
	 * @param target idCotizacion
	 * @param atributos productos (Lista Long:Double)
	 * @category BackOrder
	 */
	COTIZACION_SUMINISTRO { public String toString() { return "BO-SC"; }},
	
	/** Actualizar precios de Cotizacion en Orden de Compra
	 * @value BO-CO$
	 * @param target idCotizacion
	 * @param atributos productos (Lista Long:Double)
	 * @category BackOffice
	 */
	COTIZACION_PRECIOS { public String toString() { return "BO-CO$"; }},

	/** Actualizar cantidad de productos en Cotizacion y Requisicion
	 * @value BO-OC
	 * @param target idOrdenCompra
	 * @param atributos productos (Lista Long:Double)
	 * @category BackOffice
	 */
	ORDEN_ESTATUS { public String toString() { return "BO-OC"; }},

	/** Actualizar el suministro
	 * @value BO-SU
	 * @param target idOrdenCompra
	 * @param referencia idMovimiento
	 * @param atributos productos (Lista Long:Double)
	 * @category BackOrder
	 */
	ORDEN_SUMINISTRO { public String toString() { return "BO-SU"; }},

	/** Determina el tipo de Solicitud a Bodega que debe generarse
	 * @value COMPRAS_SBO
	 * @param target idObra
	 * @param referencia idCotizacion (SBO) o idRequisicion (SBR)
	 * @param referenciaExtra idCotizacion (solo para SBR)
	 * @param atributos productos (Lista Long: Lista Long:Double)
	 * @category BackOffice
	 */
	COMPRAS_SBO { public String toString() { return "COMPRAS-SBO"; }};
	
	public static TopicEventosCompras fromString(String value) {
		for (TopicEventosCompras item : TopicEventosCompras.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TopicEventosCompras fromOrdinal(int value) {
		for (TopicEventosCompras item : TopicEventosCompras.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}

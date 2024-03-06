package net.giro.plataforma.topics;

public enum TopicEventosInventarios {
	/** Proceso para Entrada/Salida de Productos en Almacen/Bodega
	 * @value MOVIMIENTO
	 * @param target idMovimiento
	 * @param referencia IdTraspaso/TipoEntrada
	 * @param atributos lista de MovimientosDetalle
	 * @category BackOffice
	 */
	MOVIMIENTO { public String toString() { return "MOVIMIENTO"; }},

	/** Proceso para Entrada/Salida de Productos en Almacen/Bodega
	 * @value BO-INVENTARIO
	 * @param target idMovimiento
	 * @param referencia IdTraspaso/TipoEntrada
	 * @param atributos lista de MovimientosDetalle
	 * @category BackOffice
	 */
	MOVIMIENTO_OLD { public String toString() { return "BO-INVENTARIO"; }},
	
	/** Proceso de Cancelacion de Entrada/Salida de Productos en Almacen/Bodega
	 * @value BO-INVENTARIO-CANCEL
	 * @param target idMovimiento
	 * @category BackOffice
	 */
	MOVIMIENTO_CANCEL { public String toString() { return "BO-INVENTARIO-CANCEL"; }},

	/** Proceso para Actualizar lo recibido del Traspaso
	 * @value BO-TRASPASOS
	 * @param target idTraspaso
	 * @param referencia idMovimiento
	 * @param atributos lista de MovimientosDetalle
	 * @category BackOffice
	 */
	TRASPASOS { public String toString() { return "BO-TRASPASOS"; }},

	/** Proceso de Cancelacion de Traspasos
	 * @value BO-TRASPASOS-CANCEL
	 * @param target idTraspaso
	 * @category BackOffice
	 */
	TRASPASOS_CANCEL { public String toString() { return "BO-TRASPASOS-CANCEL"; }},

	/** Proceso para generar los movimientos necesarios para soportar la entrada
	 * @value ENTRADA
	 * @param target idMovimiento
	 * @category BackOffice
	 */
	POST_ENTRADA { public String toString() { return "ENTRADA"; }},

	/** Proceso para generar los movimientos necesarios para soportar la entrada
	 * @value BO-ENTRADA
	 * @param target idMovimiento
	 * @category BackOffice
	 */
	POST_ENTRADA_OLD { public String toString() { return "BO-ENTRADA"; }},

	/** Proceso para generar el movimiento de salida necesario para soportar el Traspaso
	 * @value POST-TRASPASO
	 * @param target idTraspaso
	 * @category BackOffice
	 */
	POST_TRASPASO { public String toString() { return "POST-TRASPASO"; }},

	/** Proceso para generar el movimiento de salida necesario para soportar el Traspaso
	 * @value BO-TRASPASO
	 * @param target idTraspaso
	 * @category BackOffice
	 */
	POST_TRASPASO_OLD { public String toString() { return "BO-TRASPASO"; }},

	/** Proceso de Solicitud de Almacen a Bodega por Explosion de Insumos
	 * @value SBO-INSUMOS
	 * @param target idObra
	 * @param referencia idCotizacion
	 * @param usuario idUsuario
	 * @param atributos tupla de idProducto y cantidad
	 * @category BackOffice
	 */
	SOLICITUD_BODEGA { public String toString() { return "SBO-INSUMOS"; }},

	/** Proceso de Solicitud de Almacen a Bodega por Explosion de Insumos
	 * @value BO-SBO
	 * @param target idObra
	 * @param referencia idCotizacion
	 * @param usuario idUsuario
	 * @param atributos tupla de idProducto y cantidad
	 * @category BackOffice
	 */
	SOLICITUD_BODEGA_OLD { public String toString() { return "BO-SBO"; }},

	/** Proceso de Solicitud de Almacen a Bodega por Requisicion
	 * @value SBO-REQUISICION
	 * @param target idObra
	 * @param referencia idRequisicion
	 * @param referenciaExtra idCotizacion
	 * @param usuario idUsuario
	 * @param atributos tupla de idProducto y cantidad
	 * @category BackOffice
	 */
	SOLICITUD_BODEGA_REQUISICION { public String toString() { return "SBO-REQUISICION"; }},

	/** Proceso de Solicitud de Almacen a Bodega por Requisicion
	 * @value BO-SBR
	 * @param target idObra
	 * @param referencia idRequisicion
	 * @param referenciaExtra idCotizacion
	 * @param usuario idUsuario
	 * @param atributos tupla de idProducto y cantidad
	 * @category BackOffice
	 */
	SOLICITUD_BODEGA_REQUISICION_OLD { public String toString() { return "BO-SBR"; }},

	/** [BO-SBO-DESCARGA] Back Office Solicitud a Bodega: Descarga de cantidades en Inventario por Solicitud a Bodega */
	SOLICITUD_BODEGA_DESCARGA { public String toString() { return "BO-SBO-DESCARGA"; }},

	/** [BO-SBO-CANT] Back Office Solicitud a Bodega: Actualizacion de cantidad en Inventario (AlmacenProducto::solicitud) */
	SOLICITUD_BODEGA_CANTIDAD { public String toString() { return "BO-SBO-CANT"; }},

	/** [BO-SBO-CANCEL] Back Office Solicitud a Bodega: Cancelacion de Solicitud a Bodega */
	SOLICITUD_BODEGA_CANCELACION { public String toString() { return "BO-SBO-CANCEL"; }},

	/** [BO-SBO-CANCEL] Back Office Solicitud a Bodega: Quitar producto de Solicitud a Bodega */
	SOLICITUD_BODEGA_QUITAR { public String toString() { return "BO-SBO-QUITAR"; }},

	/** [BO-PRECIOS] Back Office Productos: Actualizacion de precios en Producto */
	PRODUCTO_PRECIOS { public String toString() { return "BO-PRECIOS"; }},

	/** [BO-DEA] Back Office Almacen/Bodega: Desasignar Encargado en Almacen/Bodega */
	ALMACEN_QUITAR_ENCARGADO { public String toString() { return "BO-DEA"; }},

	/** [ASIG-ALMACENES] Back Office Almacen/Bodega: Recuperar Almacenes y enviarlos a GP */
	ASIGNACION_ALMACENES { public String toString() { return "ASIG-ALMACENES"; }};
	
	public static TopicEventosInventarios fromString(String value) {
		for (TopicEventosInventarios item : TopicEventosInventarios.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TopicEventosInventarios fromOrdinal(int value) {
		for (TopicEventosInventarios item : TopicEventosInventarios.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}

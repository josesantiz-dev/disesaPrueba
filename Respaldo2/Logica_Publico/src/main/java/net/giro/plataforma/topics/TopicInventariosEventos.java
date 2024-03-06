package net.giro.plataforma.topics;

public enum TopicInventariosEventos {
	/**
	 * BackOrder Orden de Compra: Marca los productos suministrados de la Orden de Compra
	 */
	BackOrderCompras  { public String toString() { return "BO-COMPRAS";  }},
	/**
	 * Back Office Inventario: Aumenta/Disminuye la Existencia de Productos en Almacen/Bodega
	 */
	BackOfficeInventario  { public String toString() { return "BO-INVENTARIO";  }},
	/**
	 * Back Office Solicitud a Bodega: Genera Traspaso de Almacen a Bodega de la Explosion de una Insumos
	 */
	BackOfficeSolicitudBodega  { public String toString() { return "BO-SBO";  }},
	/**
	 * Back Office Solicitud a Bodega: Genera Traspaso de Almacen a Bodega de una Requisicion
	 */
	BackOfficeSolicitudBodegaRequisicion  { public String toString() { return "BO-SBR";  }}
}

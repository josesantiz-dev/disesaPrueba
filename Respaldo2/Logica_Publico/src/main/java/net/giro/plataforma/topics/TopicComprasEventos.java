package net.giro.plataforma.topics;

public enum TopicComprasEventos {
	/**
	 * BackOffice Requisicion: Actualiza los Estatus de Requisicion en base a la Cotizacion dada
	 */
	BO_RQ  { public String toString() { return "BO-RQ";  }},
	/**
	 * BackOffice Cotizacion:  Actualiza los Estatus de Cotizacion en base a la Orden de Compra dada
	 */
	BO_CO  { public String toString() { return "BO-CO";  }},
	/**
	 * BackOffice Cotizacion:  Actualiza los Precios de Cotizacion a la Orden de Compra dada
	 */
	BO_CO$ { public String toString() { return "BO-CO$"; }},
	/**
	 * BackOffice Orden de Compra: Actualiza los precios de la Orden de Compra en el catalogo de Productos
	 */
	BO_OC  { public String toString() { return "BO-OC";  }}
}

package net.giro.plataforma.topics;

public enum TopicEventosCARGAS {
	/** Actualizacion de datos en Facturas cargadas
	 * @value BO-UPDATE
	 * @param target ?
	 * @param referencia ?
	 * @param atributos ?
	 * @category BackOffice
	 */
	ACTUALIZAR_DATOS { public String toString() { return "BO-UPDATE"; }},
	
	/** Validar Estatus CFDI en SAT
	 * @value CFDI_ESTATUS
	 * @param target fechaDesde (dd-MM-yyyy)
	 * @param referencia fechaHasta (dd-MM-yyyy)
	 * @category BackOffice
	 */
	CFDI_ESTATUS { public String toString() { return "CFDI_ESTATUS"; }};
	
	public static TopicEventosCARGAS fromString(String value) {
		for (TopicEventosCARGAS item : TopicEventosCARGAS.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TopicEventosCARGAS fromOrdinal(int value) {
		for (TopicEventosCARGAS item : TopicEventosCARGAS.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}

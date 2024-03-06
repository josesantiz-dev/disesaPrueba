package net.giro.plataforma.topics;

public enum TopicEventosCXP {
	/** Actualizacion de Saldo
	 * @value BO-SALDO
	 * @param target ?
	 * @param referencia ?
	 * @param atributos ?
	 * @category BackOffice
	 */
	SALDO { public String toString() { return "BO-SALDO"; }},
	
	/** Registro de Abono
	 * @value BO-PROVISION
	 * @param target ?
	 * @param referencia ?
	 * @param atributos ?
	 * @category BackOffice
	 */
	PROVISION { public String toString() { return "BO-PROVISION"; }},
	
	/** Cancelacion de Factura
	 * @value BO-CANCEL
	 * @param target ?
	 * @param referencia ?
	 * @param atributos ?
	 * @category BackOffice
	 */
	CANCELACION { public String toString() { return "BO-CANCEL"; }},
	
	/** Actualizador de Estatus de CFDI
	 * @value CFDI_ESTATUS
	 * @param atributos lista de Id de CFDI (XML)
	 * @category BackOffice
	 */
	CFDI_ESTATUS { public String toString() { return "CFDI_ESTATUS"; }},
	
	/** Evento para CONTABILIZADOR
	 * @value CXP_TRANSACCION
	 * @param target idPagosGastos
	 * @category BackOffice
	 */
	TRANSACCION { public String toString() { return "CXP_TRANSACCION"; }},
	
	/** Evento para crear Nuevo PROVEEDOR (NEGOCIO)
	 * @value CXP_PROVEEDOR
	 * @param target uniqueValue donde se asignara el nuevo Proveedor
	 * @param atributos valores para el nuevo Proveedor (nombre, rfc, tipoPersona)
	 * @category BackOffice
	 */
	PROVEEDOR { public String toString() { return "CXP_PROVEEDOR"; }};
	
	public static TopicEventosCXP fromString(String value) {
		for (TopicEventosCXP item : TopicEventosCXP.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TopicEventosCXP fromOrdinal(int value) {
		for (TopicEventosCXP item : TopicEventosCXP.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}

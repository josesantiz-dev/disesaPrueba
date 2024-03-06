package net.giro.plataforma.topics;

public enum TopicEventosTYG {
	/** Actualizar los saldos de las Cuentas Bancarias
	 * @value TYG-SALDO
	 * @param target ?
	 * @param referencia ?
	 * @param atributos ?
	 * @category BackOffice
	 */
	SALDO_CUENTAS { public String toString() { return "TYG-SALDO"; }},
	
	/** Recuperar Tipo de Cambio de BANXICO
	 * @value TYG-TCAMBIO
	 * @param target moneda Abreviacion moneda extranjera (Ej. USD)
	 * @param referencia monedaBase Abreviacion moneda destino (Ej. MXN)
	 * @category BackOffice
	 */
	TIPO_CAMBIO { public String toString() { return "TYG-TCAMBIO"; }};
	
	public static TopicEventosTYG fromString(String value) {
		for (TopicEventosTYG item : TopicEventosTYG.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TopicEventosTYG fromOrdinal(int value) {
		for (TopicEventosTYG item : TopicEventosTYG.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}

package net.giro.plataforma.topics;

public enum TopicEventosCXC {
	/** Proceso para registrar Pago para Facturas PUE 
	 * @value CXC-PUE-PAGO
	 * @param target idFactura
	 * @category BackOffice
	 */
	PUE_PAGO { public String toString() { return "CXC-PUE-PAGO"; }},
	
	/** Proceso para Actualizar Saldo de Facturas
	 * @value CXC-SALDO
	 * @param target idFacturaPago
	 * @param referencia Agrupador de los pagos involucrados
	 * @param atributos lista de Id de Facturas
	 * @category BackOffice
	 */
	SALDO { public String toString() { return "CXC-SALDO"; }},
	
	/** Proceso para Actualizar Cobranza de Facturas
	 * @value CXC-COBRANZA
	 * @param atributos Lista de Id de Facturas
	 * @category BackOffice
	 */
	COBRANZA { public String toString() { return "CXC-COBRANZA"; }},
	
	/** Proceso para Solicitud de Cancelacion de Facturas
	 * @value CXC-CANCEL
	 * @param target idFactura
	 * @category BackOffice
	 */
	CANCELACION_FACTURA { public String toString() { return "CXC-CANCEL"; }},
	
	/** Proceso para Solicitud de Cancelacion de Pagos de Facturas
	 * @value CXC-CANCEL-PAGO
	 * @param target idTimbre
	 * @category BackOffice
	 */
	CANCELACION_PAGO { public String toString() { return "CXC-CANCEL-PAGO"; }},
	
	/** Procesar Solicitud pendientes de Cancelacion (Facturas y Pagos de Facturas) 
	 * @value CXC-CANCEL-PEND 
	 * @category BackOffice
	 */
	CANCELACIONES_PENDIENTES { public String toString() { return "CXC-CANCEL-PEND"; }},
	
	/** [CXC-CANCEL-ESTATUS] BackOffice CXC: Comprobacion de estatus de CFDI ante el PAC */
	CANCELACION_ESTATUS { public String toString() { return "CXC-CANCEL-ESTATUS"; }},
	
	/** Provision de Factura 
	 * @value CXC-PROVISION
	 * @param target idFactura
	 * @param monto montoProvision
	 * @param referencia grupo
	 * @category BackOffice
	 */
	PROVISION { public String toString() { return "CXC-PROVISION"; }},
	
	/** Provision de Facturas PPD Activas del mes 
	 * @value CXC-PROVMENSUAL
	 * @category BackOffice
	 */
	PROVISION_MENSUAL { public String toString() { return "CXC-PROVMENSUAL"; }},
	
	/** Consultar Estatus de CFDI 
	 * @value CXC-CFDI-ESTATUS
	 * @param target ID Timbre 
	 * @param referencia Tipo de Timbre: F - Factura, P - Pagos (Complemento Pago)
	 * @category BackOffice
	 */
	CFDI_ESTATUS { public String toString() { return "CXC-CFDI-ESTATUS"; }};
	
	public static TopicEventosCXC fromString(String value) {
		for (TopicEventosCXC item : TopicEventosCXC.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TopicEventosCXC fromOrdinal(int value) {
		for (TopicEventosCXC item : TopicEventosCXC.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}

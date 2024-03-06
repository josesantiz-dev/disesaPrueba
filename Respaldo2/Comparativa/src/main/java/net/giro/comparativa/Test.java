package net.giro.comparativa;

/**
 * Opciones de Test
 * @author javaz
 */
public enum Test {
	/**
	 * BO-OC: Back Office Orden Compra
	 */
	BackOfficeOrdenCompra("BO-OC"),
	/**
	 * BO-CO: Back Office Cotizacion
	 */
	BackOfficeCotizacion("BO-CO"),
	/**
	 * BO-RQ: Back Office Requisicion
	 */
	BackOfficeRequisicion("BO-RQ");

	String val = "";
	Test(String val) {
		this.val = val;
	}
}

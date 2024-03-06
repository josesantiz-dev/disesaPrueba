package net.giro.cxc.realvirtual.beans;

public interface CFDI {
	/**
	 * Metodo para indicar si estamos en modo DEBUG
	 * @param debugging Indica si estamos en modo DEBUG
	 */
	public void debugging(boolean debugging);

	/**
	 * Metodo para indicar si estamos en modo TESTING
	 * @param testing Indica si estamos en modo TESTING
	 */
	public void testing(boolean testing);
	
	/**
	 * Metodo para indicar la version de peticion que utilizara soap, default 1.1
	 * @param vesionSoapRequest [1.1, 1.2]
	 */
	public void soapRequestVersion(String soapRequestVersion);
	
	/**
	 * Timbre el XML dado. Produccion
	 * @param xml CFDI para timbrar
	 * @param timbreUser Usuario de acceso al servicio de timbrado
	 * @param timbrePass Contraseña de acceso al servicio de timbrado
	 * @return
	 */
	public AcuseRecepcion timbrar(String xml, String timbreUser, String timbrePass);
	
	/**
	 * Cancela timbre dado. Produccion
	 * @param xml CFDI timbrado para cancelar
	 * @param timbreUser Usuario de acceso al servicio de timbrado
	 * @param timbrePass Contraseña de acceso al servicio de timbrado
	 * @return
	 */
	public AcuseCancelacion cancelar(String xml, String timbreUser, String timbrePass);
	
	/**
	 * Metodo para consultar el estatus del CFDI indicado
	 * @param rfcEmisor
	 * @param rfcReceptor
	 * @param total
	 * @param uuid
	 * @param timbreUser
	 * @param timbrePass
	 * @return
	 */
	public AcuseEstatus estatus(String rfcEmisor, String rfcReceptor, String total, String uuid, String timbreUser, String timbrePass);
	
	/**
	 * Metodo para recuperar el CFDI
	 * @param uuid
	 * @param rfcEmisor
	 * @param timbreUser
	 * @param timbrePass
	 * @return
	 */
	public AcuseCFDI acuseCfdi(String uuid, String fecha, String rfcEmisor, String timbreUser, String timbrePass);
	
	/**
	 * Timbre el XML dado. Pruebas
	 * @param xml CFDI para timbrar
	 * @param timbreUser Usuario de acceso al servicio de timbrado
	 * @param timbrePass Contraseña de acceso al servicio de timbrado
	 * @return
	 */
	public AcuseRecepcion timbrarTest(String xml, String timbreUser, String timbrePass);

	/**
	 * Cancela timbre dado. Pruebas
	 * @param xml CFDI timbrado para cancelar
	 * @param timbreUser Usuario de acceso al servicio de timbrado
	 * @param timbrePass Contraseña de acceso al servicio de timbrado
	 * @return
	 */
	public AcuseCancelacion cancelarTest(String xml, String timbreUser, String timbrePass);
	
	public AcuseSchema schema(String xml, String timbreUser, String timbrePass);
}
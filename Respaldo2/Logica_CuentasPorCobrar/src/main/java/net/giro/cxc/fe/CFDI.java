package net.giro.cxc.fe;

public interface CFDI {
	/**
	 * Metodo para indicar si estamos en modo DEBUG
	 * @param debugging Indica si estamos en modo DEBUG
	 */
	public void debugging(boolean debugging);
	
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
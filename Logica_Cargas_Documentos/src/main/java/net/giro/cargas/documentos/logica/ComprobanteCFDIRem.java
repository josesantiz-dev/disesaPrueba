package net.giro.cargas.documentos.logica;

import javax.ejb.Remote;

import net.giro.cargas.documentos.util.TipoDeComprobante;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface ComprobanteCFDIRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	/**
	 * Metodo para procesar y almacenar el CFDI (XML) indicado
	 * @param archivoSrc Arreglo de bytes con el contenido del CFDI
	 * @param prefijo 
	 * @return
	 * @throws Exception
	 */
	public Respuesta importarXML(byte[] archivoSrc, String prefijo) throws Exception;
	
	/**
	 * Metodo para recuperar el CFDI (XML) almacenado
	 * @param idComprobacion
	 * @param tipoDeComprobante
	 * @param prefijo
	 * @return
	 * @throws Exception
	 */
	public Respuesta exportarXML(long idComprobacion, TipoDeComprobante tipoDeComprobante, String prefijo) throws Exception;
	
	/**
	 * Metodo para almacenar fisicamente el archivo (byte[]) indicado
	 * @param archivoSrc
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public boolean storage(byte[] archivoSrc, String filename) throws Exception;

	//public ComprobacionFactura findCfdiById(Long idComprobacionFactura) throws Exception;

	//public ComprobacionPago findPagoById(Long idComprobacionPago) throws Exception;

	//public ComprobacionNomina findNominaById(Long idComprobacionNomina) throws Exception;

	/* -----------------------------------------------------------------------------------------------
	// CFDI - Ingreso, Egreso
	// -----------------------------------------------------------------------------------------------
	
	public Respuesta save(ComprobacionFactura entity) throws Exception;

	public void cancelar(ComprobacionFactura entity) throws Exception;

	public ComprobacionFactura findCfdiById(Long idComprobacionFactura) throws Exception;

	// -----------------------------------------------------------------------------------------------
	// CFDI - Complemento de Pago
	// -----------------------------------------------------------------------------------------------
	
	public Respuesta save(ComprobacionPago entity) throws Exception;

	public void cancelar(ComprobacionPago entity) throws Exception;

	public ComprobacionPago findPagoById(Long idComprobacionPago) throws Exception;

	// -----------------------------------------------------------------------------------------------
	// CFDI - Compĺemento de Nomina
	// -----------------------------------------------------------------------------------------------
	
	public Respuesta save(ComprobacionNomina entity) throws Exception;

	public void cancelar(ComprobacionNomina entity) throws Exception;

	public ComprobacionPago findNominaById(Long idComprobacionNomina) throws Exception;
	*/
}

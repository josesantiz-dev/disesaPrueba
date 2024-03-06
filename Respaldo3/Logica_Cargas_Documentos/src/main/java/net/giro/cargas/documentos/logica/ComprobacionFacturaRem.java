package net.giro.cargas.documentos.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import mx.gob.sat.cfdi.v33.Comprobante;
import net.giro.cargas.documentos.beans.ComprobacionFactura;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface ComprobacionFacturaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ComprobacionFactura entity) throws Exception;
	
	public List<ComprobacionFactura> saveOrUpdateList(List<ComprobacionFactura> entities) throws Exception;
	
	public void update(ComprobacionFactura entity) throws Exception;
	
	public void cancelar(long idComprobanteFactura) throws Exception;
	
	public void cancelar(ComprobacionFactura entity) throws Exception;
	
	public void deleteXML(long idComprobanteFactura);

	public ComprobacionFactura findById(long idComprobacionFactura) throws Exception;

	public List<ComprobacionFactura> findByDates(Date fechaDesde, Date fechaHasta) throws Exception;
	
	public List<ComprobacionFactura> findByDates(Date fechaDesde, Date fechaHasta, String orderBy) throws Exception;
	
	public List<ComprobacionFactura> comprobar(String expresionImpresa, String orderBy) throws Exception;
	
	public void comprobarEstatusSAT(long idComprobanteFactura) throws Exception;
	
	public void comprobarEstatusSAT(ComprobacionFactura entity) throws Exception;

	public Respuesta comprobarEstatusSAT(String expresionImpresa) throws Exception;
	
	public Respuesta analizarXML(byte[] archivoSrc, boolean validarEstatus) throws Exception;
	
	public Respuesta importarCFDI(Comprobante comprobante) throws Exception;
	
	/**
	 * Recupera la informacion indicada en @property
	 * @param idFactura
	 * @param property uuid, nombre, factura, serie, folio, rfcEmisor, rfcReceptor, total, tipo
	 * @return
	 */
	public String getProperty(long idFactura, String property);

	/**
	 * Establece la informacion indicada en @property
	 * @param idFactura
	 * @param property nombre, serie, folio
	 * @return
	 */
	public void setProperty(long idFactura, String property, String value) throws Exception;
	
	public double getFacturaTipoCambio(long idFactura) throws Exception;

	public double getFacturaDescuento(long idFactura) throws Exception;
	
	public double getFacturaSubtotal(long idFactura) throws Exception;
	
	public double getFacturaTotal(long idFactura) throws Exception;
}

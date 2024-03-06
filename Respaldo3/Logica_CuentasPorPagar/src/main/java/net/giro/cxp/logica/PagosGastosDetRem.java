package net.giro.cxp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.TiposGastoCXP;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface PagosGastosDetRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(PagosGastosDet entity) throws Exception;

	public List<PagosGastosDet> saveOrUpdateList(List<PagosGastosDet> entities) throws Exception;

	public void update(PagosGastosDet entity) throws Exception;

	public void delete(long idPagosGastosDet) throws Exception;

	public PagosGastosDet findById(Long idPagosGastosDet);
	
	public PagosGastosDet findByUniqueValue(String uniqueValue) throws Exception;

	public List<PagosGastosDet> findAll(long idPagosGastos);

	public List<PagosGastosDet> findByFactura(long idFactura) throws Exception;

	public List<PagosGastosDet> findByFactura(long idFactura, boolean incluyeCanceladas) throws Exception;

	/**
	 * Metodo para procesar un factura (XML)
	 * @param fileSrc Factura CFDI (XML)
	 * @param tipoGasto Caja Chica(C), Gasto(G), Provision(F), Registro de Gasto(P), Movimiento de Cuentas(M), ComprobacionGastos(Z), Desconocido(X)
	 * @return
	 * @throws Exception
	 */
	public Respuesta cargaFacturaXML(byte[] fileSrc, TiposGastoCXP tipoGasto) throws Exception;

	/**
	 * Metodo para procesar un factura (XML)
	 * @param fileSrc Factura CFDI (XML)
	 * @param tipoGasto Caja Chica(C), Gasto(G), Provision(F), Registro de Gasto(P), Movimiento de Cuentas(M), ComprobacionGastos(Z), Desconocido(X)
	 * @param idReferencia ID de referencia (Caja Chica, Provision, Registro de Gasto, etc) que ocupemos ignorar
	 * @return
	 * @throws Exception
	 */
	public Respuesta cargaFacturaXML(byte[] fileSrc, TiposGastoCXP tipoGasto, Long idReferencia) throws Exception;

	/**
	 * Procesar el CFDI (xml) cargado previamente
	 * @param fileSrc Fuente de xml
	 * @param tipoGasto Caja Chica(C), Gasto(G), Provision(F), Registro de Gasto(P), Movimiento de Cuentas(M), ComprobacionGastos(Z), Desconocido(X)
	 * @param idReferencia ID de referencia (Caja Chica, Provision, Registro de Gasto, etc) que ocupemos ignorar
	 * @param comprobaciones Listado de comprobaciones actuales
	 * @return
	 * @throws Exception
	 */
	public Respuesta procesarCFDI(byte[] fileSrc, TiposGastoCXP tipoGasto, Long idReferencia,  String fileName) throws Exception;
	public Respuesta procesarCFDI(byte[] fileSrc, TiposGastoCXP tipoGasto, Long idReferencia,  String fileName, String rfcReferencia) throws Exception;
	
	public void eliminarFactura(long idFactura) throws Exception;
	
	/**
	 * Recupera toda la informacion disponible de la factura indicada en @idFactura
	 * @param idFactura 
	 * @return Mapa de calores disponibles en la factura
	 * @throws Exception
	 */
	public HashMap<String, Object> facturaValores(long idFactura) throws Exception;

	/**
	 * Recupera la informacion indicada en @property
	 * @param idFactura
	 * @param property uuid, nombre, factura, serie, folio, rfcEmisor, rfcReceptor, total, tipo
	 * @return
	 */
	public String getFacturaProperty(long idFactura, String property);

	/**
	 * Establece la informacion indicada en @property
	 * @param idFactura
	 * @param property nombre, serie, folio
	 * @return
	 */
	public void setFacturaProperty(long idFactura, String property, String value) throws Exception;

	public double getFacturaTipoCambio(long idFactura) throws Exception;

	public double getFacturaDescuento(long idFactura) throws Exception;

	public double getFacturaSubtotal(long idFactura) throws Exception;

	public double getFacturaTotal(long idFactura) throws Exception;

	// ------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// ------------------------------------------------------------------------------------------------

	public PagosGastosDetExt convertir(PagosGastosDet entity) throws Exception;
	
	public PagosGastosDet convertir(PagosGastosDetExt extendido) throws Exception;

	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------

	public Long save(PagosGastosDetExt extendido) throws Exception;

	public List<PagosGastosDetExt> saveOrUpdateListExt(List<PagosGastosDetExt> extendidos) throws Exception;

	public void update(PagosGastosDetExt extendido) throws Exception;

	public List<PagosGastosDetExt> findExtAll(long idPagosGastos) throws Exception;
}

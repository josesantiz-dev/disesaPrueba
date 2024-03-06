package net.giro.cxc.logica;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.FEv33.Comprobante;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface FacturaPagosRem {
	public void showSystemOuts(boolean value);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public void setInfoSesion(long idUsuario, long idEmpresa, long codigoEmpresa);

	public long save(FacturaPagos entity) throws Exception;

	public List<FacturaPagos> saveOrUpdateList(List<FacturaPagos> entities) throws Exception;
	
	public void update(FacturaPagos entity) throws Exception;

	/**
	 * Cancela el pago y actualiza el saldo de la factura. Realiza la cancelacion del timbre si corresponde
	 * @param entity Pago a cancelar
	 * @return
	 * @throws Exception
	 */
	public Respuesta cancelar(long idFacturaPago) throws Exception;

	/**
	 * Cancela el pago y actualiza el saldo de la factura. Realiza la cancelacion del timbre si corresponde
	 * @param entity Pago a cancelar
	 * @return
	 * @throws Exception
	 */
	public Respuesta cancelar(FacturaPagos entity) throws Exception;
	
	public Respuesta evaluaCancelacion(long idFactura) throws Exception;
	
	/**
	 * Cancela todos los pagos de la factura indicada
	 * @param idFactura
	 * @return
	 * @throws Exception
	 */
	public Respuesta cancelarByFactura(long idFactura) throws Exception;

	/**
	 * Cancela todos los pagos de la factura indicada
	 * @param idFactura
	 * @return
	 * @throws Exception
	 */
	public Respuesta cancelarByFactura(Factura idFactura) throws Exception;

	public void delete(long idFacturaPago) throws Exception;

	public boolean guardarPagoAplicable(long idFacturaOriginal, long idFactura) throws Exception;

	public boolean aplicarPagos(long idFactura) throws Exception;

	public FacturaPagos findById(long idFacturaPago);

	public List<FacturaPagos> findAll(long idFactura) throws Exception;

	public List<FacturaPagos> findAll(long idFactura, boolean incluyeCanceladas, boolean soloTimbrado, String orderBy) throws Exception;

	public List<FacturaPagos> findLike(String value, boolean incluyeCanceladas, boolean soloTimbrado, String orderBy, int limite) throws Exception;
	
	public List<FacturaPagos> findLikeProperty(String propertyName, Object value, boolean incluyeCanceladas, boolean soloTimbrado, String orderBy, int limite) throws Exception;
	
    public List<FacturaPagos> findLikeProperty(String propertyName, String value) throws Exception;

	public List<FacturaPagos> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, boolean soloTimbrado, String orderBy, int limite) throws Exception;
	
	public List<FacturaPagos> findByProperty(String propertyName, Object value) throws Exception;

	public List<FacturaPagos> findByTimbre(long idTimbre) throws Exception;

	public List<FacturaPagos> findByTimbre(long idTimbre, boolean incluyeCanceladas, String orderBy) throws Exception;

	public List<FacturaPagos> findByAgrupador(String agrupador) throws Exception;

	/**
	 * Recupera lo efectivamente pagado de la factura indicada
	 * @param idFactura ID Factura
	 * @param fechaDesde Inicio del periodo donde se consideraran los pagos. Default null
	 * @param fechaHasta Fin  del periodo donde se consideraran los pagos. Default null
	 * @return
	 * @throws Exception
	 */
	public BigDecimal findLiquidado(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception;

	/**
	 * Recupera lo efectivamente pagado convertido a pesos si corresponde de la factura indicada
	 * @param idFactura ID Factura
	 * @param fechaDesde Inicio del periodo donde se consideraran los pagos. Default null
	 * @param fechaHasta Fin  del periodo donde se consideraran los pagos. Default null
	 * @return
	 * @throws Exception
	 */
	public BigDecimal findLiquidadoPesos(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception;

    public Respuesta enviarTransaccion(Long entityId) throws Exception;

    public Respuesta enviarTransaccion(String agrupador) throws Exception;
    
    public Respuesta enviarTransaccion(FacturaPagos entity) throws Exception;
    
    public int findParcialidad(long idFactura) throws Exception;
    
    public Comprobante generarComprobante(long idFacturaPago, String serie, String folio) throws Exception;

    public Comprobante generarComprobante(String agrupador, String serie, String folio) throws Exception;

    public Comprobante generarComprobante(List<FacturaPagosExt> pagos, String serie, String folio) throws Exception;

	public String formarXML(long idFacturaPago) throws Exception;

	public String formarXML(Comprobante comprobante) throws Exception;

    public Long folioComplementoPago() throws Exception;
    
    public Long folioComplementoPago(long codigoEmpresa, String serieComplementoPago) throws Exception;

	public Respuesta timbrar(FacturaPagos pago, String serie, String folio) throws Exception;

	public Respuesta timbrar(FacturaPagosExt pago, String serie, String folio) throws Exception;

	public Respuesta timbrar(List<FacturaPagosExt> pagos, String serie, String folio) throws Exception;
	
	public Respuesta timbrar(List<FacturaPagosExt> pagos, String serie, String folio, boolean debugging, boolean testing, boolean noTimbrar) throws Exception;
	
	/**
	 * Metodo para validar el numero de pagos multiples en los cuales esta asignada la factura
	 * @param idFactura
	 * @return Devuelve el numero de pagos multiples en los cuales esta asignada la factura
	 * @throws Exception
	 */
	public int validarPagosMultiples(long idFactura) throws Exception;
	
	public LinkedHashMap<String, String> auditoria(long idFacturaPago) throws Exception;
	
    public FacturaPagos convertir(FacturaPagosExt pojoTarget) throws Exception;
    
    public FacturaPagosExt convertir(FacturaPagos pojoTarget) throws Exception;
    
	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------

	public long save(FacturaPagosExt extendido) throws Exception;

	public List<FacturaPagosExt> saveOrUpdateListExt(List<FacturaPagosExt> extendidos) throws Exception;
	
	public void update(FacturaPagosExt extendido)  throws Exception;
	
	public FacturaPagosExt findByIdExt(long idFacturaPagos) throws Exception;

	public List<FacturaPagosExt> findExtByFactura(long idFactura) throws Exception;

	public List<FacturaPagosExt> findExtByTimbre(long idTimbre) throws Exception;

	public List<FacturaPagosExt> findExtByAgrupador(String agrupador) throws Exception;

	public List<FacturaPagosExt> findByPropertyExt(String propertyName, Object value) throws Exception;
	
    public List<FacturaPagosExt> findLikePropertyExt(String propertyName, String value) throws Exception;

    public List<FacturaPagosExt> findLikeFolioFactura(String value) throws Exception;
    
    public List<FacturaPagosExt> findLikeBeneficiario(String value) throws Exception;
    
    public List<FacturaPagosExt> findLikeCuentaBancaria(String value) throws Exception;

    public Respuesta enviarTransaccion(FacturaPagosExt entity) throws Exception;
}

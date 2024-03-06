package net.giro.cxc.logica;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.Obra;
import net.giro.cxc.FEv33.Comprobante;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface FacturaRem {
	public void showSystemOuts(boolean value);
	
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void setInfoSesion(long idUsuario, long idEmpresa, long codigoEmpresa);
	
	public Long save(Factura entity) throws Exception;
	
	public List<Factura> save(List<Factura> listEntities) throws Exception;
	
	public void update(Factura entity) throws Exception;
	
	public void delete(long idFactura) throws Exception;
	
	public Factura findById(long idFactura) throws Exception;
	
	public Factura findByIdTimbre(long idTimbre) throws Exception;

	public List<Factura> findLike(String value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<Factura> findLikeProperty(String propertyName, Object value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<Factura> findByProperty(String propertyName, Object value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<Factura> findList(List<Long> idFacturas) throws Exception;
	
	public List<Factura> findLike(String value, String tipoComprobante, int tipoObra, boolean incluyeCanceladas, int limite) throws Exception;

	public List<Factura> findLikeProperty(String propertyName, Object value) throws Exception;
	
	public List<Factura> findLikeProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<Factura> findLikeProperty(String propertyName, Object value, int tipoObra, int limite) throws Exception;
	
	public List<Factura> findLikeProperty(String propertyName, Object value, String tipoComprobante, int tipoObra, boolean incluyeCanceladas, int limite) throws Exception;

	public List<Factura> findLikePropertySinProvision(String propertyName, Object value, int tipoObra, int limite) throws Exception;

	public List<Factura> findByProperty(String propertyName, Object value) throws Exception;
	
	public List<Factura> findByProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<Factura> findByProperty(String propertyName, Object value, int tipoObra, int limite) throws Exception;
	
	public List<Factura> findByProperty(String propertyName, Object value, String tipoComprobante, int tipoObra, int limite) throws Exception;
	
	public List<Factura> findByPropertyPojoCompleto(String propertyName, Object value, int tipo) throws Exception;	
	
	public List<Obra> ObrafindByProperty(String propertyName, final Object value) throws Exception;
	
	public List<Factura> findLikeProperties(HashMap<String, Object> params) throws Exception;

	public Respuesta cancelarFactura(long idFactura, String motivoCancelacion) throws Exception;
	
	public Respuesta cancelarFactura(Factura entity, String motivoCancelacion) throws Exception;

	public Respuesta cancelarFactura(Factura entity, String motivoCancelacion, boolean debugging, boolean testing) throws Exception;

	public Respuesta evaluaCancelacion(long idFactura) throws Exception;

	public Respuesta provisionar(long idFactura, double montoProvision, long usuarioId) throws Exception;
	
	public Respuesta provisionar(Factura entity, double montoProvision, long usuarioId) throws Exception;

	public void facturaProvisionada(Factura entity, long usuarioId) throws Exception;
	
	public void cancelarProvision(Factura entity, long usuarioId) throws Exception;

	public BigDecimal calcularLiquidado(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception;

	public BigDecimal calcularLiquidadoPesos(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception;

	public BigDecimal calcularEgresos(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception;

	public BigDecimal calcularEgresosPesos(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception;

	public BigDecimal calcularSaldo(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception;

	public BigDecimal calcularSaldoPesos(Long idFactura, Date fechaDesde, Date fechaHasta) throws Exception;

	public BigDecimal calcularSaldoAFecha(Long idFactura, Date fecha) throws Exception;

	public BigDecimal calcularSaldoAFechaPesos(Long idFactura, Date fecha) throws Exception;

	public List<Factura> findNotasCreditoByFactura(long idFactura) throws Exception;

	public BigDecimal findMontoNotasCreditoByFactura(long idFactura);
	
	public List<Factura> comprobarFolioFacturacion(String serie, String folio) throws Exception;

	public List<Factura> findTimbradas(String orderBy, int limite) throws Exception;
	
	public List<Factura> findTimbradas(String propertyName, Object value, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<Factura> findProvisionadas(String orderBy, int limite) throws Exception;
	
	public List<Factura> provisionMensual(int mes, int limite) throws Exception;
	
	public List<Factura> provisionMensual(Date fechaDesde, Date fechaHasta, int limite) throws Exception;
	
	public List<Factura> paraProvisionar(int year, int month, String orderBy) throws Exception;
	
	public Respuesta timbrar(FacturaExt factura, String version, String usoCfdi, int cfdiRelacionado, String cfdiRelacionadoUuid, String cfdiRelacionadoTipoRelacion) throws Exception;
	
	public Respuesta timbrar(FacturaExt factura, String version, String usoCfdi, int cfdiRelacionado, String cfdiRelacionadoUuid, String cfdiRelacionadoTipoRelacion, boolean debugging, boolean testing, boolean noTimbrar) throws Exception;
	
	public Respuesta consultarEstatus(long idFactura) throws Exception;
	
	public void cobranza(Long idFactura) throws Exception;

	public void cobranzaUbicacionPrevia(Long idFactura) throws Exception;

	public BigDecimal pagado(long idFactura);

	public BigDecimal pagado(Factura factura);

	public BigDecimal notasCredito(long idFactura);

	public BigDecimal notasCredito(Factura factura);
	
	public BigDecimal saldo(long idFactura);

	public BigDecimal saldo(Factura factura);

	public BigDecimal recalculaSaldo(long idFactura, BigDecimal total);
	
	public Comprobante generarComprobante(long idFactura) throws Exception;
	
	public String formarXML(long idFactura) throws Exception;

	public String formarXML(Comprobante comprobante) throws Exception;
	
	public LinkedHashMap<String, String> auditoria(long idFactura) throws Exception;

	// ------------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// ------------------------------------------------------------------------------------------------------
	
	public Factura convertir(FacturaExt extendido) throws Exception;
	
	public FacturaExt convertir(Factura entity) throws Exception;
	
	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------
	
	public Long save(FacturaExt extendido) throws Exception;

	public List<FacturaExt> saveExt(List<FacturaExt> listEntities) throws Exception;
	
	public void update(FacturaExt extendido) throws Exception;
	
	public FacturaExt findExtById(long idFactura) throws Exception;

	public Respuesta cancelarFactura(FacturaExt extendido, String motivoCancelacion, boolean debugging, boolean testing) throws Exception;
	
	public Respuesta provisionar(FacturaExt extendido, double montoProvision, long usuarioId) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.1 | 2017-04-06 | Javier Tirado 	| Añado los metodos convertir. Conviente de pojo a extendido y viceversa
 */
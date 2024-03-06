package net.giro.cxc.logica;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.Obra;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface FacturaRem {
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Factura entity) throws Exception;

	public List<Factura> save(List<Factura> listEntities) throws Exception;

	public void update(Factura entity) throws Exception;
	
	public void delete(Factura entity) throws Exception;

	public List<Factura> findAll();

	public List<Factura> findByProperty(String propertyName, Object value);
	
	public List<Factura> findByProperty(String propertyName, Object value, int max);
	
	public List<Factura> findByProperty(String propertyName, Object value, int tipoObra, int max);
	
	public List<Factura> findLikeProperty(String propertyName, Object value);
	
	public List<Factura> findLikeProperty(String propertyName, Object value, int max);
	
	public List<Factura> findLikeProperty(String propertyName, Object value, int tipoObra, int max);

	public List<Factura> findByPropertyPojoCompleto(String propertyName, Object value, int tipo);	
	
	public List<Obra> ObrafindByProperty(String propertyName, final Object value) throws Exception;
	
	public List<Factura> findLikeProperties(HashMap<String, Object> params) throws Exception;

	public Respuesta cancelarFactura(Long entityId, long usuarioId) throws Exception;
	
	public Respuesta cancelarFactura(Factura entity, long usuarioId) throws Exception;

	public Respuesta provisionar(Long entityId, double montoProvision, long usuarioId) throws Exception;
	
	public Respuesta provisionar(Factura entity, double montoProvision, long usuarioId) throws Exception;

	public void facturaProvisionada(Factura entity, long usuarioId) throws Exception;
	
	public void cancelarProvision(Factura entity, long usuarioId) throws Exception;
	
	public BigDecimal findSaldoFactura(Long entityId) throws Exception;
	
	public BigDecimal findSaldoFactura(Factura entity) throws Exception;

	public List<Factura> comprobarFolioFacturacion(String serie, String folio) throws Exception;

	public List<Factura> findTimbradas(String orderBy, int limite) throws Exception;
	
	public Factura convertir(FacturaExt pojoFactura) throws Exception;
	
	public FacturaExt convertir(Factura pojoFactura) throws Exception;
	
	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------
	
	public Long save(FacturaExt entityExt) throws Exception;

	public void update(FacturaExt entity) throws Exception;

	public void delete(FacturaExt entityExt) throws Exception;

	public Factura findById(Long id);

	public FacturaExt findExtById(Long id) throws Exception;
	
	public List<FacturaExt> findByPropertyExt(String propertyName, final Object value) throws Exception;
	
	public List<FacturaExt> findLikePropertyExt(String propertyName, Object value, int tipoObra, int max) throws Exception;
	
	public List<FacturaExt> findByPropertyPojoCompletoExt(String propertyName, Object value, int tipo) throws Exception;

	public List<FacturaExt> findLikePropertiesExt(HashMap<String, Object> params) throws Exception;
	
	public Respuesta cancelarFactura(FacturaExt entityId, long usuarioId) throws Exception;
	
	public Respuesta provisionar(FacturaExt entity, double montoProvision, long usuarioId) throws Exception;
	
	public BigDecimal findSaldoFactura(FacturaExt entityExt) throws Exception;
	
	public List<FacturaExt> comprobarExtFolioFacturacion(String serie, String folio) throws Exception;
	
	public List<FacturaExt> findExtTimbradas(String orderBy, int limite) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.1 | 2017-04-06 | Javier Tirado 	| Añado los metodos convertir. Conviente de pojo a extendido y viceversa
 */
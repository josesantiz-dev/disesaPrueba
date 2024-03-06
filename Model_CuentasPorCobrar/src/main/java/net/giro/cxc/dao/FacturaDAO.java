package net.giro.cxc.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.Factura;

@Remote
public interface FacturaDAO extends DAO<Factura> {
	public long save(Factura entity, long codigoEmpresa) throws Exception;
	
	public List<Factura> saveOrUpdateList(List<Factura> entities, long codigoEmpresa) throws Exception;
	
	public List<Factura> findAll(String orderBy, long idEmpresa) throws Exception;
	
	public List<Factura> findLike(String value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite) throws Exception;
	
	public List<Factura> findLikeProperty(String propertyName, Object value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite) throws Exception;
	
	public List<Factura> findByProperty(String propertyName, Object value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite) throws Exception;

	public List<Factura> findLikePropertySinProvision(String propertyName, Object value, int tipoObra, long idEmpresa, int limite) throws Exception;

	public List<Factura> findLikeProperties(HashMap<String, Object> params, long idEmpresa) throws Exception;
	
	public List<Factura> findProvisionadas(String orderBy, long idEmpresa, int limite) throws Exception;
	
	public List<Factura> comprobarFolioFacturacion(String serie, String folio, String orderBy, long idEmpresa) throws Exception;
	
	public List<Factura> provisionMensual(Date fechaDesde, Date fechaHasta, String orderBy, long idEmpresa, int limite) throws Exception;
	
	public List<Factura> paraProvisionar(Date fechaDesde, Date fechaHasta, String orderBy, long idEmpresa) throws Exception;
	
	public List<Factura> findList(List<Long> idFacturas, String orderBy, long idEmpresa) throws Exception;
}

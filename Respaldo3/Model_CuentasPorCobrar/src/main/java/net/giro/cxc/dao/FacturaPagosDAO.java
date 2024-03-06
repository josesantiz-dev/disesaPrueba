package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.FacturaPagos;

@Remote
public interface FacturaPagosDAO extends DAO<FacturaPagos> {
	public long save(FacturaPagos entity, long codigoEmpresa) throws Exception;
	
	public List<FacturaPagos> saveOrUpdateList(List<FacturaPagos> entities, long codigoEmpresa) throws Exception;

	public List<FacturaPagos> findAll(long idFactura, boolean incluyeCanceladas, boolean soloTimbrado, String orderBy) throws Exception;

	public List<FacturaPagos> findLike(String value, boolean incluyeCanceladas, boolean soloTimbrado, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<FacturaPagos> findLikeProperty(String propertyName, Object value, boolean incluyeCanceladas, boolean soloTimbrado, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<FacturaPagos> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, boolean soloTimbrado, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<FacturaPagos> findInProperty(String propertyName, List<Long> values, boolean incluyeCanceladas, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<FacturaPagos> findByTimbre(long idTimbre, boolean incluyeCanceladas, String orderBy) throws Exception;
}

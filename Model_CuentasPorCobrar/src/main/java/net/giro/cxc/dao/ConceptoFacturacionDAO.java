package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.ConceptoFacturacion;

@Remote
public interface ConceptoFacturacionDAO extends DAO<ConceptoFacturacion> {
	public long save(ConceptoFacturacion entity, long codigoEmpresa) throws Exception;
	
	public List<ConceptoFacturacion> saveOrUpdateList(List<ConceptoFacturacion> entities, long codigoEmpresa) throws Exception;
	
	public List<ConceptoFacturacion> findAll(boolean incluyeCanceladas, String orderBy, long idEmpresa);
	
	public List<ConceptoFacturacion> findLike(String value, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite) throws Exception;

	public List<ConceptoFacturacion> findLikeProperty(String propertyName, Object value, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite);

	public List<ConceptoFacturacion> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite);
}

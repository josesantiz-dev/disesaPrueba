package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.FacturaTimbre;

@Remote
public interface FacturaTimbreDAO extends DAO<FacturaTimbre> {
	public long save(FacturaTimbre entity, long codigoEmpresa) throws Exception;
	
	public long saveOrUpdate(FacturaTimbre entity, long codigoEmpresa) throws Exception;
		
	public List<FacturaTimbre> saveOrUpdateList(List<FacturaTimbre> entities, long codigoEmpresa) throws Exception;
	
	public List<FacturaTimbre> findAll(long idEmpresa) throws Exception;
	
	public List<FacturaTimbre> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<FacturaTimbre> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<FacturaTimbre> comprobarTimbre(String serie, String folio, long idEmpresa) throws Exception;
}

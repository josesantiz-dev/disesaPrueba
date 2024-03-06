package net.giro.cxc.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.Provisiones;

@Remote
public interface ProvisionesDAO extends DAO<Provisiones> {
	public long save(Provisiones entity, long codigoEmpresa) throws Exception;
	
	public List<Provisiones> saveOrUpdateList(List<Provisiones> entities, long codigoEmpresa) throws Exception;
	
	public List<Provisiones> findAll(long idEmpresa);
	
	public List<Provisiones> findAllInactives(long idEmpresa);

	public List<Provisiones> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception;
	
	public List<Provisiones> findByProperties(HashMap<String, Object> params, String orderBy, long idEmpresa, int limite) throws Exception;
	
	public List<Provisiones> findLikeProperty(String propertyName, String value, long idEmpresa, int limite) throws Exception;
	
	public List<Provisiones> findLikeProperties(HashMap<String, String> params, String orderBy, long idEmpresa, int limite) throws Exception;
	
	public Integer findLastGrupo(long idEmpresa) throws Exception;
	
	public Integer findNextGrupo(long idEmpresa) throws Exception;
}

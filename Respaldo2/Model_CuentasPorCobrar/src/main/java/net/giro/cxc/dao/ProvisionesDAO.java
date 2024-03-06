package net.giro.cxc.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.Provisiones;

@Remote
public interface ProvisionesDAO extends DAO<Provisiones> {

	public void setEmpresa(Long idEmpresa);
		
	public long save(Provisiones entity) throws Exception;
	
	public List<Provisiones> saveOrUpdateList(List<Provisiones> entities) throws Exception;
	
	public List<Provisiones> findAll();
	
	public List<Provisiones> findAllInactives();

	public List<Provisiones> findByProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<Provisiones> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception;
	
	public List<Provisiones> findLikeProperty(String propertyName, String value, int limite) throws Exception;
	
	public List<Provisiones> findLikeProperties(HashMap<String, String> params, String orderBy, int limite) throws Exception;
	
	public Integer findLastGrupo() throws Exception;
	
	public Integer findNextGrupo() throws Exception;
}

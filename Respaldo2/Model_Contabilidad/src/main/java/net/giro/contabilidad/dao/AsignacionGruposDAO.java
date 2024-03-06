package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.AsignacionGrupos;

@Remote
public interface AsignacionGruposDAO extends DAO<AsignacionGrupos> {
	public void orderBy(String orderBy);

	public void setEmpresa(Long idEmpresa);
	
	public long save(AsignacionGrupos entity, Long idEmpresa) throws Exception;
	
	public List<AsignacionGrupos> saveOrUpdateList(List<AsignacionGrupos> entities, Long idEmpresa) throws Exception;

	public List<AsignacionGrupos> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<AsignacionGrupos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<AsignacionGrupos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<AsignacionGrupos> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<AsignacionGrupos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

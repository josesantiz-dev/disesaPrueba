package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.AsignacionGrupos;

@Remote
public interface AsignacionGruposDAO extends DAO<AsignacionGrupos> {
	public void orderBy(String orderBy);

	public long save(AsignacionGrupos entity, long codigoEmpresa) throws Exception;
	
	public List<AsignacionGrupos> saveOrUpdateList(List<AsignacionGrupos> entities, long codigoEmpresa) throws Exception;

	public List<AsignacionGrupos> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<AsignacionGrupos> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<AsignacionGrupos> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<AsignacionGrupos> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<AsignacionGrupos> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

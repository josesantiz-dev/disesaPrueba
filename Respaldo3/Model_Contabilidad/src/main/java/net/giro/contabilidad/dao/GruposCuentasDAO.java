package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.GruposCuentas;

@Remote
public interface GruposCuentasDAO extends DAO<GruposCuentas> {
	public void orderBy(String orderBy);

	public long save(GruposCuentas entity, long codigoEmpresa) throws Exception;
	
	public List<GruposCuentas> saveOrUpdateList(List<GruposCuentas> entities, long codigoEmpresa) throws Exception;

	public List<GruposCuentas> findAll(long idGrupo, String orderBy) throws Exception;

	public List<GruposCuentas> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception;

	public List<GruposCuentas> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception;
	
	public List<GruposCuentas> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<GruposCuentas> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<GruposCuentas> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

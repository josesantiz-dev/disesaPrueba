package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.Grupos;

@Remote
public interface GruposDAO extends DAO<Grupos> {
	public void orderBy(String orderBy);

	public long save(Grupos entity, long codigoEmpresa) throws Exception;
	
	public List<Grupos> saveOrUpdateList(List<Grupos> entities, long codigoEmpresa) throws Exception;

	public List<Grupos> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<Grupos> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<Grupos> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<Grupos> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<Grupos> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

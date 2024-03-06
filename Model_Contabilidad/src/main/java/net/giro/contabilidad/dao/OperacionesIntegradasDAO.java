package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.OperacionesIntegradas;

@Remote
public interface OperacionesIntegradasDAO extends DAO<OperacionesIntegradas> {
	public void orderBy(String orderBy);

	public long save(OperacionesIntegradas entity, long codigoEmpresa) throws Exception;
	
	public List<OperacionesIntegradas> saveOrUpdateList(List<OperacionesIntegradas> entities, long codigoEmpresa) throws Exception;

	public List<OperacionesIntegradas> findAll(long idOperacion) throws Exception;

	public List<OperacionesIntegradas> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradas> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<OperacionesIntegradas> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradas> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradas> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

}

package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;

@Remote
public interface OperacionesIntegradasTransaccionesDAO extends DAO<OperacionesIntegradasTransacciones> {
	public void orderBy(String orderBy);

	public void setEmpresa(Long idEmpresa);
	
	public long save(OperacionesIntegradasTransacciones entity, Long idEmpresa) throws Exception;
	
	public List<OperacionesIntegradasTransacciones> saveOrUpdateList(List<OperacionesIntegradasTransacciones> entities, Long idEmpresa) throws Exception;

	public List<OperacionesIntegradasTransacciones> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesIntegradasTransacciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

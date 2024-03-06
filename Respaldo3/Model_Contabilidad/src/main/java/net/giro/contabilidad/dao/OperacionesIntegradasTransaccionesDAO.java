package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;
import net.giro.contabilidad.beans.Transacciones;

@Remote
public interface OperacionesIntegradasTransaccionesDAO extends DAO<OperacionesIntegradasTransacciones> {
	public void orderBy(String orderBy);

	public long save(OperacionesIntegradasTransacciones entity, long codigoEmpresa) throws Exception;
	
	public List<OperacionesIntegradasTransacciones> saveOrUpdateList(List<OperacionesIntegradasTransacciones> entities, long codigoEmpresa) throws Exception;

	public OperacionesIntegradasTransacciones findByTransaccion(Transacciones idTransaccion) throws Exception;

	public OperacionesIntegradasTransacciones findByCodigoTransaccion(long codigoTransaccion, long idEmpresa) throws Exception;

	public List<OperacionesIntegradasTransacciones> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<OperacionesIntegradasTransacciones> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

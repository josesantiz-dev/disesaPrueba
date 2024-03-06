package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.TransaccionesDataDetalle;

@Remote
public interface TransaccionesDataDetalleDAO extends DAO<TransaccionesDataDetalle> {
	public void orderBy(String orderBy);

	public long save(TransaccionesDataDetalle entity, long codigoEmpresa) throws Exception;
	
	public List<TransaccionesDataDetalle> saveOrUpdateList(List<TransaccionesDataDetalle> entities, long codigoEmpresa) throws Exception;

	public List<TransaccionesDataDetalle> findAll(long idTransaccionData) throws Exception;

	public List<TransaccionesDataDetalle> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<TransaccionesDataDetalle> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

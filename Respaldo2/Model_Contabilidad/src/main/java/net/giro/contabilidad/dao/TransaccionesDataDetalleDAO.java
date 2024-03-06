package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.TransaccionesDataDetalle;

@Remote
public interface TransaccionesDataDetalleDAO extends DAO<TransaccionesDataDetalle> {
	public void orderBy(String orderBy);

	public void setEmpresa(Long idEmpresa);
	
	public long save(TransaccionesDataDetalle entity, Long idEmpresa) throws Exception;
	
	public List<TransaccionesDataDetalle> saveOrUpdateList(List<TransaccionesDataDetalle> entities, Long idEmpresa) throws Exception;

	public List<TransaccionesDataDetalle> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<TransaccionesDataDetalle> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

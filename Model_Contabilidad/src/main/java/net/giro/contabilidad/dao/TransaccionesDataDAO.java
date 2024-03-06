package net.giro.contabilidad.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.TransaccionesData;

@Remote
public interface TransaccionesDataDAO extends DAO<TransaccionesData> {
	public void orderBy(String orderBy);
	
	public long save(TransaccionesData entity, long codigoEmpresa) throws Exception;
	
	public List<TransaccionesData> saveOrUpdateList(List<TransaccionesData> entities, long codigoEmpresa) throws Exception;

	public List<TransaccionesData> findAll(long idMensajeTransaccion, String orderBy) throws Exception;

	public List<TransaccionesData> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<TransaccionesData> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<TransaccionesData> findPrevio(Long codigoTransaccion, Long poliza, Long lote) throws Exception;
	
	public List<TransaccionesData> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	/*public List<TransaccionesData> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<TransaccionesData> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;*/
}

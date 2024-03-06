package net.giro.contabilidad.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.TransaccionesData;
import net.giro.plataforma.InfoSesion;

@Remote
public interface TransaccionesDataRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(TransaccionesData entity) throws Exception;

	public List<TransaccionesData> saveOrUpdateList(List<TransaccionesData> entities) throws Exception;

	public void update(TransaccionesData entity) throws Exception;
	
	public void delete(Long idTransaccionesData) throws Exception;

	public void delete(List<TransaccionesData> listTransaccionesData) throws Exception;

	public TransaccionesData findById(Long idTransaccionesData);
	
	public List<TransaccionesData> findAll(long idMensajeTransaccion, String orderBy) throws Exception;
	
	public List<TransaccionesData> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<TransaccionesData> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<TransaccionesData> findPrevio(Long codigoTransaccion, Long poliza, Long lote) throws Exception;
	
	public List<TransaccionesData> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	/*public List<TransaccionesData> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<TransaccionesData> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;*/
}
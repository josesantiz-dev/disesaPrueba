package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.TransaccionesDataDetalle;
import net.giro.plataforma.InfoSesion;

@Remote
public interface TransaccionesDataDetalleRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(TransaccionesDataDetalle entity) throws Exception;

	public List<TransaccionesDataDetalle> saveOrUpdateList(List<TransaccionesDataDetalle> entities) throws Exception;

	public void update(TransaccionesDataDetalle entity) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public TransaccionesDataDetalle findById(Long id);
	
	public List<TransaccionesDataDetalle> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<TransaccionesDataDetalle> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
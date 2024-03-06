package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OperacionesIntegradasTransaccionesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public Long save(OperacionesIntegradasTransacciones entity) throws Exception;

	public List<OperacionesIntegradasTransacciones> saveOrUpdateList(List<OperacionesIntegradasTransacciones> entities) throws Exception;

	public void update(OperacionesIntegradasTransacciones entity) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public OperacionesIntegradasTransacciones findById(Long id);
	
	public List<OperacionesIntegradasTransacciones> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesIntegradasTransacciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
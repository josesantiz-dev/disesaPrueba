package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.PolizasDetalles;
import net.giro.plataforma.InfoSesion;

@Remote
public interface PolizasDetallesRem {
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(PolizasDetalles entity) throws Exception;

	public List<PolizasDetalles> saveOrUpdateList(List<PolizasDetalles> entities) throws Exception;

	public void update(PolizasDetalles entity) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public PolizasDetalles findById(Long id);
	
	public List<PolizasDetalles> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<PolizasDetalles> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PolizasDetalles> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<PolizasDetalles> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<PolizasDetalles> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
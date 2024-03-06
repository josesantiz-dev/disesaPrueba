package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.PolizasInterfaces;
import net.giro.plataforma.InfoSesion;

@Remote
public interface PolizasInterfacesRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(PolizasInterfaces entity) throws Exception;

	public List<PolizasInterfaces> saveOrUpdateList(List<PolizasInterfaces> entities) throws Exception;

	public void update(PolizasInterfaces entity) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public PolizasInterfaces findById(Long id);
	
	public List<PolizasInterfaces> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<PolizasInterfaces> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PolizasInterfaces> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<PolizasInterfaces> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<PolizasInterfaces> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
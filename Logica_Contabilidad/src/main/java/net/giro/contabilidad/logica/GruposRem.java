package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.Grupos;
import net.giro.plataforma.InfoSesion;

@Remote
public interface GruposRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Grupos entity) throws Exception;

	public List<Grupos> saveOrUpdateList(List<Grupos> entities) throws Exception;

	public void update(Grupos entity) throws Exception;
	
	public void delete(Long entity) throws Exception;
	
	public void delete(Grupos entity) throws Exception;

	public Grupos findById(Long id);
	
	public List<Grupos> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Grupos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Grupos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Grupos> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Grupos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
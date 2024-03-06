package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.Llaves;
import net.giro.plataforma.InfoSesion;

@Remote
public interface LlavesRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Llaves entity) throws Exception;

	public List<Llaves> saveOrUpdateList(List<Llaves> entities) throws Exception;

	public void update(Llaves entity) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public Llaves findById(Long id);
	
	public List<Llaves> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Llaves> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Llaves> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Llaves> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Llaves> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public boolean comprobarPosicion(Long idLlave, int posicion) throws Exception;
}
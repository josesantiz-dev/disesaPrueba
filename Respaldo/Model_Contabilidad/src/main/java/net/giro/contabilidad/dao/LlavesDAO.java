package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.Llaves;

@Remote
public interface LlavesDAO extends DAO<Llaves> {
	public void orderBy(String orderBy);
	
	public List<Llaves> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Llaves> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Llaves> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Llaves> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Llaves> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public boolean comprobarPosicion(Long idLlave, int posicion) throws Exception;
}

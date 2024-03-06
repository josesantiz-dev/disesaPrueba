package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.Transacciones;

@Remote
public interface TransaccionesDAO extends DAO<Transacciones> {
	public void orderBy(String orderBy);
	
	public Transacciones findByCodigo(Long codigoTransaccion) throws Exception;
	
	public List<Transacciones> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Transacciones> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Transacciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Transacciones> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Transacciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public boolean comprobarCodigo(Long idTransaccion, Long codigo) throws Exception;
}

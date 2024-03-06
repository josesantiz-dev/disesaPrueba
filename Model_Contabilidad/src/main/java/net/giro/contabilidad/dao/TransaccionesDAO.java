package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.Transacciones;

@Remote
public interface TransaccionesDAO extends DAO<Transacciones> {
	public void orderBy(String orderBy);
	
	public long save(Transacciones entity, long codigoEmpresa) throws Exception;
	
	public List<Transacciones> saveOrUpdateList(List<Transacciones> entities, long codigoEmpresa) throws Exception;

	public Transacciones findByCodigo(long codigoTransaccion, long idEmpresa) throws Exception;
	
	public List<Transacciones> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<Transacciones> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<Transacciones> findInProperty(String propertyName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<Transacciones> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<Transacciones> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
	
	public boolean comprobarCodigo(long idTransaccion, long codigo, long idEmpresa) throws Exception;
}

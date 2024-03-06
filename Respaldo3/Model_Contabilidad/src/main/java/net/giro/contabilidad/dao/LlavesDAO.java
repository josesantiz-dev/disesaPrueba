package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.Llaves;

@Remote
public interface LlavesDAO extends DAO<Llaves> {
	public void orderBy(String orderBy);

	public long save(Llaves entity, long codigoEmpresa) throws Exception;
	
	public List<Llaves> saveOrUpdateList(List<Llaves> entities, long codigoEmpresa) throws Exception;

	public List<Llaves> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<Llaves> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<Llaves> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<Llaves> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<Llaves> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
	
	public boolean comprobarPosicion(Long idLlave, int posicion, long idEmpresa) throws Exception;
}

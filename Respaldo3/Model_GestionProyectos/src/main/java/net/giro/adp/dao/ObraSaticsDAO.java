package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraSatics;

@Remote
public interface ObraSaticsDAO extends DAO<ObraSatics> {
	public void orderBy(String orderBy);

	public long save(ObraSatics entity, long codigoEmpresa) throws Exception;
	
	public List<ObraSatics> saveOrUpdateList(List<ObraSatics> entities, long codigoEmpresa) throws Exception;
	
	public List<ObraSatics> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<ObraSatics> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<ObraSatics> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<ObraSatics> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<ObraSatics> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|23/05/2016		|Javier Tirado	|Creando la interface ObraSaticsDAO
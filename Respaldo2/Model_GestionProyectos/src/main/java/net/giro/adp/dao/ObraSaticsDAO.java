package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraSatics;

@Remote
public interface ObraSaticsDAO extends DAO<ObraSatics> {
	public void setEmpresa(Long idEmpresa);
	
	public void orderBy(String orderBy);
	
	public List<ObraSatics> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraSatics> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ObraSatics> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ObraSatics> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<ObraSatics> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|23/05/2016		|Javier Tirado	|Creando la interface ObraSaticsDAO
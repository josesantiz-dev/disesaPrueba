package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraSatics;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraSaticsRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void orderBy(String orderBy);

	public Long save(ObraSatics entity) throws Exception;

	public List<ObraSatics> saveOrUpdateList(List<ObraSatics> entities) throws Exception;
	
	public void update(ObraSatics entity) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public ObraSatics findById(Long id);
	
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
//2.2		|23/05/2016		|Javier Tirado	|Creando la interface remota ObraSaticsRem
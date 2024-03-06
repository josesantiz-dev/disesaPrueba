package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraAlmacenes;

@Remote
public interface ObraAlmacenesDAO extends DAO<ObraAlmacenes> {
	public void orderBy(String orderBy);

	public void setEmpresa(Long idEmpresa);
	
	public List<ObraAlmacenes> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraAlmacenes> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ObraAlmacenes> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ObraAlmacenes> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<ObraAlmacenes> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public ObraAlmacenes findAlmacenPrincipal(long idObra, long idSucursal);
	
	public ObraAlmacenes findBodega(long idObra);
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|19/05/2016		|Javier Tirado	|Creando la clase ObraAlmacenesDAO
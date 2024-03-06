package net.giro.compras.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.Cotizacion;

@Remote
public interface CotizacionDAO extends DAO<Cotizacion> {
	public void estatus(Integer estatus);
	public void OrderBy(String orderBy);
	
	public List<Cotizacion> findByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<Cotizacion> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<Cotizacion> findInProperty(String columnName, List<Object> values) throws Exception;
	
	public int findConsecutivoByProveedor(long idProveedor) throws Exception;
	
	public List<Cotizacion> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<Cotizacion> findLikeProperties(HashMap<String, String> params) throws Exception;
	
	public List<Cotizacion> findByPropertyWithObra(String propertyName, final Object value, long idObra, int max) throws Exception;

	public List<Cotizacion> findLikePropertyWithObra(String propertyName, final Object value, long idObra, int max) throws Exception;
	
	public List<Cotizacion> findInPropertyWithObra(String columnName, List<Object> values, long idObra) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. 
 */
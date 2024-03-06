package net.giro.compras.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.Cotizacion;
import net.giro.comun.ExcepConstraint;

@Remote
public interface CotizacionDAO extends DAO<Cotizacion> {
	public void estatus(Integer estatus);
	
	public void OrderBy(String orderBy);
	
    public long save(Cotizacion entity, Long idEmpresa) throws ExcepConstraint;
    
    public List<Cotizacion> saveOrUpdateList(List<Cotizacion> entities, Long idEmpresa) throws Exception;
	
	public List<Cotizacion> findByProperty(String propertyName, final Object value, int limite, Long idEmpresa) throws Exception;

	public List<Cotizacion> findLikeProperty(String propertyName, final Object value, int limite, Long idEmpresa) throws Exception;
	
	public List<Cotizacion> findInProperty(String columnName, List<Object> values, Long idEmpresa) throws Exception;
	
	public int findConsecutivoByProveedor(long idProveedor, Long idEmpresa) throws Exception;
	
	public List<Cotizacion> findByProperties(HashMap<String, Object> params, Long idEmpresa) throws Exception;
	
	public List<Cotizacion> findLikeProperties(HashMap<String, String> params, Long idEmpresa) throws Exception;
	
	public List<Cotizacion> findByPropertyWithObra(String propertyName, final Object value, long idObra, int limite, Long idEmpresa) throws Exception;

	public List<Cotizacion> findLikePropertyWithObra(String propertyName, final Object value, long idObra, int limite, Long idEmpresa) throws Exception;
	
	public List<Cotizacion> findInPropertyWithObra(String columnName, List<Object> values, long idObra, Long idEmpresa) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. 
 */
package net.giro.compras.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.CotizacionDetalle;
import net.giro.comun.ExcepConstraint;

@Remote
public interface CotizacionDetalleDAO extends DAO<CotizacionDetalle> {
	public void OrderBy(String orderBy);
	
    public long save(CotizacionDetalle entity, Long idEmpresa) throws ExcepConstraint;
	
	public List<CotizacionDetalle> saveOrUpdateList(List<CotizacionDetalle> entities, Long idEmpresa) throws Exception;
	
	public List<CotizacionDetalle> findByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<CotizacionDetalle> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<CotizacionDetalle> findInProperty(String columnName, List<Object> values) throws Exception;
	
	public List<CotizacionDetalle> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<CotizacionDetalle> findLikeProperties(HashMap<String, String> params) throws Exception;

	public List<CotizacionDetalle> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<CotizacionDetalle> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.2 | 2017-06-21 | Javier Tirado 	| Añado los metodos findByProperties y findLikeProperties
 */
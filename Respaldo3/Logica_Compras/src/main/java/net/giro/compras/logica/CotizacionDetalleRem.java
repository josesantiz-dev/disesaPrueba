package net.giro.compras.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.CotizacionDetalleExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface CotizacionDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void OrderBy(String orderBy);

	public Long save(CotizacionDetalle entity) throws Exception;
	
	public void update(CotizacionDetalle entity) throws Exception;
	
	public List<CotizacionDetalle> saveOrUpdateList(List<CotizacionDetalle> entities) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public CotizacionDetalle findById(Long idCotizacionDetalle);
	
	public List<CotizacionDetalle> findAll(Long idCotizacion) throws Exception;
	
	public List<CotizacionDetalle> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<CotizacionDetalle> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<CotizacionDetalle> findInProperty(String propertyName, List<Object> values) throws Exception;	

	public List<CotizacionDetalle> findByProperties(HashMap<String, Object> params) throws Exception;

	public List<CotizacionDetalle> findLikeProperties(HashMap<String, String> params) throws Exception;
	
	public CotizacionDetalle convertir(CotizacionDetalleExt pojoTarget) throws Exception;

	public CotizacionDetalleExt convertir(CotizacionDetalle pojoTarget) throws Exception;
	
	// ----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------------------------------------------------------------
	
	public Long save(CotizacionDetalleExt entityExt) throws Exception;
	
	public List<CotizacionDetalleExt> saveOrUpdateListExt(List<CotizacionDetalleExt> entities) throws Exception;
	
	public void update(CotizacionDetalleExt entityExt) throws Exception;
	
	public CotizacionDetalleExt findExtById(Long idCotizacionDetalle) throws Exception;
	
	public List<CotizacionDetalleExt> findExtAll(Long idCotizacion) throws Exception;
	
	public List<CotizacionDetalleExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<CotizacionDetalleExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<CotizacionDetalleExt> findExtInProperty(String propertyName, List<Object> values) throws Exception;
	
	public List<CotizacionDetalleExt> findExtByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<CotizacionDetalleExt> findExtLikeProperties(HashMap<String, String> params) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.2 | 2017-06-21 | Javier Tirado 	| Añado los metodos findByProperties y findLikeProperties
 */
package net.giro.compras.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.CotizacionDetalleExt;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface CotizacionDetalleRem {
	public void setInfoSecion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void OrderBy(String orderBy);
	

	public CotizacionDetalle convertir(CotizacionDetalleExt pojoTarget) throws Exception;

	public CotizacionDetalleExt convertir(CotizacionDetalle pojoTarget) throws Exception;

	public Long save(CotizacionDetalle entity) throws ExcepConstraint;
	
	public Long save(CotizacionDetalleExt entityExt) throws ExcepConstraint;
	
	public void update(CotizacionDetalle entity) throws ExcepConstraint;
	
	public void update(CotizacionDetalleExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public CotizacionDetalle findById(Long id);
	
	public CotizacionDetalleExt findExtById(Long id) throws Exception;
	
	public List<CotizacionDetalle> findByProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<CotizacionDetalleExt> findExtByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<CotizacionDetalle> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<CotizacionDetalleExt> findExtLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<CotizacionDetalle> findInProperty(String propertyName, List<Object> values) throws Exception;	
	
	public List<CotizacionDetalleExt> findExtInProperty(String propertyName, List<Object> values) throws Exception;

	public List<CotizacionDetalle> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<CotizacionDetalleExt> findExtByProperties(HashMap<String, Object> params) throws Exception;

	public List<CotizacionDetalle> findLikeProperties(HashMap<String, String> params) throws Exception;
	
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
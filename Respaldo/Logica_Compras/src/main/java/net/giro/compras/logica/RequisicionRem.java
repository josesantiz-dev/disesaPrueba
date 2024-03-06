package net.giro.compras.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionExt;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface RequisicionRem {
	public void setInfoSecion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void OrderBy(String orderBy);
	
	public void estatus(Long estatus);

	public Long save(Requisicion entity) throws ExcepConstraint;
	
	public Long save(RequisicionExt entityExt) throws ExcepConstraint;
	
	public void update(Requisicion entity) throws ExcepConstraint;
	
	public void update(RequisicionExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public Requisicion findById(Long id);
	
	public RequisicionExt findExtById(Long id) throws Exception;
	
	public List<Requisicion> findByProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<RequisicionExt> findExtByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<Requisicion> findLikeProperty(String propertyName, final Object value, int max) throws Exception;

	public List<Requisicion> findLikeProperty(String propertyName, final String value, int tipo, int limite) throws Exception;
	
	public List<RequisicionExt> findExtLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<Requisicion> findInProperty(String propertyName, List<Object> values) throws Exception;	
	
	public List<RequisicionExt> findExtInProperty(String propertyName, List<Object> values) throws Exception;

	public List<Requisicion> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<RequisicionExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<Requisicion> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<RequisicionExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-19 | Javier Tirado 	| Añado los metodos estatus, findByProperties y findLikeProperties. Normal y extendido
 */
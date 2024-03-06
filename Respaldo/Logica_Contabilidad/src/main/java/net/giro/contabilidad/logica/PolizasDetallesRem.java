package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.PolizasDetalles;
import net.giro.plataforma.InfoSesion;

@Remote
public interface PolizasDetallesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(PolizasDetalles entity) throws ExcepConstraint;
	//public Long save(PolizasDetallesExt entityExt) throws ExcepConstraint;
	
	public void update(PolizasDetalles entity) throws ExcepConstraint;
	//public void update(PolizasDetallesExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public PolizasDetalles findById(Long id);
	//public PolizasDetallesExt findExtById(Long id) throws Exception;
	
	public List<PolizasDetalles> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<PolizasDetallesExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<PolizasDetalles> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<PolizasDetallesExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PolizasDetalles> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<PolizasDetallesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<PolizasDetalles> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<PolizasDetallesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<PolizasDetalles> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<PolizasDetallesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
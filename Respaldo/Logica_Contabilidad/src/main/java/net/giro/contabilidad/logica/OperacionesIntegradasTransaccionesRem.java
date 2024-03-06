package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OperacionesIntegradasTransaccionesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(OperacionesIntegradasTransacciones entity) throws ExcepConstraint;
	//public Long save(OperacionesIntegradasTransaccionesExt entityExt) throws ExcepConstraint;
	
	public void update(OperacionesIntegradasTransacciones entity) throws ExcepConstraint;
	//public void update(OperacionesIntegradasTransaccionesExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public OperacionesIntegradasTransacciones findById(Long id);
	//public OperacionesIntegradasTransaccionesExt findExtById(Long id) throws Exception;
	
	public List<OperacionesIntegradasTransacciones> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<OperacionesIntegradasTransaccionesExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<OperacionesIntegradasTransaccionesExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesIntegradasTransacciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<OperacionesIntegradasTransaccionesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<OperacionesIntegradasTransaccionesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<OperacionesIntegradasTransacciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<OperacionesIntegradasTransaccionesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
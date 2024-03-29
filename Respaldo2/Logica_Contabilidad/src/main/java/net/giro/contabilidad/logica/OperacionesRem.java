package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.Operaciones;
import net.giro.contabilidad.beans.OperacionesExt;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.seguridad.beans.Aplicacion;

@Remote
public interface OperacionesRem {
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Operaciones entity) throws Exception;
	
	public Long save(OperacionesExt entityExt) throws Exception;

	public List<Operaciones> saveOrUpdateList(List<Operaciones> entities) throws Exception;

	public void update(Operaciones entity) throws Exception;
	
	public void update(OperacionesExt entityExt) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public Operaciones findById(Long id);
	
	public OperacionesExt findExtById(Long id) throws Exception;
	
	public List<Operaciones> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Operaciones> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Operaciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	
	public List<OperacionesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Operaciones> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<OperacionesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Operaciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<OperacionesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<Aplicacion> findAllAplicaciones() throws Exception;
	
	public List<Aplicacion> findAplicacionLikeProperty(String propertyName, String propertyValue) throws Exception;
}
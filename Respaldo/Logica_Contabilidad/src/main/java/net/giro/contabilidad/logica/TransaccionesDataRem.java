package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.TransaccionesData;
import net.giro.plataforma.InfoSesion;

@Remote
public interface TransaccionesDataRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(TransaccionesData entity) throws ExcepConstraint;
	//public Long save(TransaccionesDataExt entityExt) throws ExcepConstraint;
	
	public void update(TransaccionesData entity) throws ExcepConstraint;
	//public void update(TransaccionesDataExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public TransaccionesData findById(Long id);
	//public TransaccionesDataExt findExtById(Long id) throws Exception;
	
	public List<TransaccionesData> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<TransaccionesDataExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<TransaccionesData> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<TransaccionesDataExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<TransaccionesData> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<TransaccionesDataExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<TransaccionesData> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<TransaccionesDataExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<TransaccionesData> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<TransaccionesDataExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
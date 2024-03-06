package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.TransaccionesDataDetalle;
import net.giro.plataforma.InfoSesion;

@Remote
public interface TransaccionesDataDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(TransaccionesDataDetalle entity) throws ExcepConstraint;
	//public Long save(TransaccionesDataDetalleExt entityExt) throws ExcepConstraint;
	
	public void update(TransaccionesDataDetalle entity) throws ExcepConstraint;
	//public void update(TransaccionesDataDetalleExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public TransaccionesDataDetalle findById(Long id);
	//public TransaccionesDataDetalleExt findExtById(Long id) throws Exception;
	
	public List<TransaccionesDataDetalle> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<TransaccionesDataDetalleExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<TransaccionesDataDetalleExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<TransaccionesDataDetalle> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<TransaccionesDataDetalleExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<TransaccionesDataDetalleExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<TransaccionesDataDetalle> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<TransaccionesDataDetalleExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
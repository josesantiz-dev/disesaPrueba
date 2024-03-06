package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.beans.TransaccionesExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface TransaccionesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public Long save(Transacciones entity) throws ExcepConstraint;
	
	public Long save(TransaccionesExt entityExt) throws ExcepConstraint;
	
	public void update(Transacciones entity) throws ExcepConstraint;
	
	public void update(TransaccionesExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public Transacciones findById(Long id);
	
	public TransaccionesExt findExtById(Long id) throws Exception;
	
	public Transacciones findByCodigo(Long codigoTransaccion) throws Exception;
	
	public TransaccionesExt findExtByCodigo(Long codigoTransaccion) throws Exception;
	
	public List<Transacciones> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<TransaccionesExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Transacciones> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<TransaccionesExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Transacciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	
	public List<TransaccionesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Transacciones> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<TransaccionesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Transacciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<TransaccionesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public boolean comprobarCodigo(Long idTransaccion, Long codigo) throws Exception;
	
	public Long findIdByCodigo(Long codigoTransaccion) throws Exception;
}
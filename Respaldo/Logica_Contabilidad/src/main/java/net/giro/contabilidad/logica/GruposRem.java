package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.Grupos;
import net.giro.plataforma.InfoSesion;

@Remote
public interface GruposRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(Grupos entity) throws ExcepConstraint;
	//public Long save(GruposExt entityExt) throws ExcepConstraint;
	
	public void update(Grupos entity) throws ExcepConstraint;
	//public void update(GruposExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public Grupos findById(Long id);
	//public GruposExt findExtById(Long id) throws Exception;
	
	public List<Grupos> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<GruposExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Grupos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<GruposExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Grupos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<GruposExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Grupos> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<GruposExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Grupos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<GruposExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	//public HashMap<Long, String> ejecutaQuery(String strQuery) throws Exception;
}
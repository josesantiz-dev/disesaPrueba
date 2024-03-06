package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.PolizasInterfaces;
import net.giro.plataforma.InfoSesion;

@Remote
public interface PolizasInterfacesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(PolizasInterfaces entity) throws ExcepConstraint;
	//public Long save(PolizasInterfacesExt entityExt) throws ExcepConstraint;
	
	public void update(PolizasInterfaces entity) throws ExcepConstraint;
	//public void update(PolizasInterfacesExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public PolizasInterfaces findById(Long id);
	//public PolizasInterfacesExt findExtById(Long id) throws Exception;
	
	public List<PolizasInterfaces> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<PolizasInterfacesExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<PolizasInterfaces> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<PolizasInterfacesExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PolizasInterfaces> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<PolizasInterfacesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<PolizasInterfaces> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<PolizasInterfacesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<PolizasInterfaces> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<PolizasInterfacesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
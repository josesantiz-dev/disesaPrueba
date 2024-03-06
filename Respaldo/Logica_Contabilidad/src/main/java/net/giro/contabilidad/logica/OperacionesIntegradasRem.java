package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.OperacionesIntegradas;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OperacionesIntegradasRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public Long save(OperacionesIntegradas entity) throws ExcepConstraint;
	
	public void update(OperacionesIntegradas entity) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public OperacionesIntegradas findById(Long id);
	
	public List<OperacionesIntegradas> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<OperacionesIntegradas> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesIntegradas> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<OperacionesIntegradas> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<OperacionesIntegradas> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public OperacionesIntegradas comprobarOperacionIntegrada(OperacionesIntegradas entity) throws Exception;
}
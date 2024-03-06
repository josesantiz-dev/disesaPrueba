package net.giro.contabilidad.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.PolizasInterfaces;
import net.giro.plataforma.InfoSesion;

@Remote
public interface PolizasInterfacesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(PolizasInterfaces entity) throws Exception;

	public List<PolizasInterfaces> saveOrUpdateList(List<PolizasInterfaces> entities) throws Exception;

	public void update(PolizasInterfaces entity) throws Exception;
	
	public void delete(Long idPolizasInterfaces) throws Exception;

	public PolizasInterfaces findById(Long idPolizasInterfaces);
	
	public List<PolizasInterfaces> findAll(long idMensajeTransaccion, String orderBy) throws Exception;
	
	public List<PolizasInterfaces> findByProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception;

	public List<PolizasInterfaces> findLikeProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception;
	
	/*public List<PolizasInterfaces> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<PolizasInterfaces> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<PolizasInterfaces> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;*/
}
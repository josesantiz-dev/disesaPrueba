package net.giro.rh.admon.logica;

import java.util.List; 

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.beans.AreaExt;

@Remote
public interface AreaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Area entity) throws Exception;
	
	public List<Area> saveOrUpdateList(List<Area> entities) throws Exception;

	public void update(Area entity) throws Exception;
	
	public void delete(Area entity) throws Exception;
	
	public List<Area> findAll();
	
	public List<Area> findAllActivos();

	public List<Area> findByProperty(String propertyName, Object value);

	public List<Area> findLikeProperty(String propertyName, Object value);
	
	public List<Area> findByPropertyPojoCompleto(String propertyName, String tipo, long value);

	// ---------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------------
	
	public Long save(AreaExt entityExt) throws Exception;

	public void update(AreaExt entity) throws Exception;
	
	public void delete(AreaExt entityExt) throws Exception;

	public Area findById(Long id);

	public AreaExt findExtById(Long id);
	
	public List<AreaExt> findAllExt();
	
	public List<AreaExt> findExtAllActivos();
	
	public List<AreaExt> findByPropertyExt(String propertyName, Object value);
	
	public List<AreaExt> findLikePropertyExt(String propertyName, Object value);
	
	public List<AreaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
}

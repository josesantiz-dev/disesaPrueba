package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.Area;

@Remote
public interface AreaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Area entity) throws Exception;
	
	public List<Area> saveOrUpdateList(List<Area> entities) throws Exception;

	public void update(Area entity) throws Exception;

	public void delete(Area entity) throws Exception;

	public Area findById(Integer id);

	public List<Area> findAll();

	public List<Area> findByProperty(String propertyName, Object value);
	
	public List<Area> findLikeClaveNombre(String value);
}

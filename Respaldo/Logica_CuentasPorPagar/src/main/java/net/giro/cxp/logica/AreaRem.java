package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.Area;

@Remote
public interface AreaRem {
	public void save(Area entity) throws ExcepConstraint;

	public void delete(Area entity);

	public Area update(Area entity);

	public Area findById(Integer id);

	public List<Area> findByProperty(String propertyName, Object value);

	public List<Area> findAll();
	
	public List<Area> findLikeClaveNombre(String value);
}

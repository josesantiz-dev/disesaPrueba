package net.giro.rh.admon.dao;

import java.util.List;
import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.rh.admon.beans.Area;

@Remote
public interface AreaDAO extends DAO<Area>  {
	
	public void delete(Area entity);

	public void update(Area entity);

	public Area findById(Integer id);

	public List<Area> findByProperty(String propertyName, Object value);
		
	public List<Area> findByPropertyPojoCompleto(String propertyName, String tipo, Object value);

	public List<Area> findAll();
	public List<Area> findAllActivos();
	
	public List<Area> findLikeClaveNombre(String value);
	
}

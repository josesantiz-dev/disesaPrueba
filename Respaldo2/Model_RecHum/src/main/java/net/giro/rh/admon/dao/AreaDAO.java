package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.Area;

@Remote
public interface AreaDAO extends DAO<Area> {
	public void setEmpresa(Long idEmpresa);
	
	public long save(Area entity) throws Exception;
	
	public List<Area> saveOrUpdateList(List<Area> entities) throws Exception;

	public List<Area> findAll();
	
	public List<Area> findAllActivos();

	public List<Area> findByProperty(String propertyName, Object value);
		
	public List<Area> findByPropertyPojoCompleto(String propertyName, String tipo, Object value);
	
	public List<Area> findLikeClaveNombre(String value);
}

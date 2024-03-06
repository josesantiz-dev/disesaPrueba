package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.Area;

@Remote
public interface AreaDAO extends DAO<Area> {
	public long save(Area entity, long codigoEmpresa) throws Exception;
	
	public List<Area> saveOrUpdateList(List<Area> entities, long codigoEmpresa) throws Exception;
	
	public List<Area> findAll(long idEmpresa);
	
	public List<Area> findAllActivos(long idEmpresa);

	public List<Area> findByProperty(String propertyName, Object value, long idEmpresa);

	public List<Area> findLikeProperty(String propertyName, Object value, long idEmpresa);
		
	public List<Area> findByPropertyPojoCompleto(String propertyName, String tipo, Object value, long idEmpresa);
	
	public List<Area> findLikeClaveNombre(String value, long idEmpresa);
}

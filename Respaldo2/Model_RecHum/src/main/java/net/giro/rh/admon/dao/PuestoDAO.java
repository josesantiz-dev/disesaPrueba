package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.Puesto;

@Remote
public interface PuestoDAO extends DAO<Puesto> {
	public void setEmpresa(Long idEmpresa);
	
	public long save(Puesto entity) throws Exception;
	
	public List<Puesto> saveOrUpdateList(List<Puesto> entities) throws Exception;
	
	public List<Puesto> findAll();
	
	public List<Puesto> findAllActivos();

	public List<Puesto> findByProperty(String propertyName, Object value);
	
	public List<Puesto> findLikeClaveNombre(String value);
	
	public List<Puesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
}

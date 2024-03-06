package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.Puesto;

@Remote
public interface PuestoDAO extends DAO<Puesto> {

	public void delete(Puesto entity);

	public void update(Puesto entity);

	public Puesto findById(Integer id);

	public List<Puesto> findByProperty(String propertyName, Object value);

	public List<Puesto> findAll();
	public List<Puesto> findAllActivos();
	
	public List<Puesto> findLikeClaveNombre(String value);
	
	public List<Puesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
}

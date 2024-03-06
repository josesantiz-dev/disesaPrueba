package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.Categoria;

@Remote
public interface CategoriaDAO extends DAO<Categoria> {

	public void delete(Categoria entity);

	public void update(Categoria entity);

	public Categoria findById(Integer id);

	public List<Categoria> findByProperty(String propertyName, Object value);

	public List<Categoria> findAll();
	public List<Categoria> findAllActivos();
	
	public List<Categoria> findLikeClaveNombre(String value);

	public List<Categoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
}

package net.giro.rh.admon.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.PuestoCategoria;

@Remote
public interface PuestoCategoriaDAO extends DAO<PuestoCategoria> {

	public void delete(PuestoCategoria entity);

	public void update(PuestoCategoria entity);

	public PuestoCategoria findById(Integer id);

	public List<PuestoCategoria> findByProperty(String propertyName, Object value);
	public List<PuestoCategoria> findByPuestoCategoria(int idPuesto, int idCategoria);
	
	public List<PuestoCategoria> findByIdPuesto(int idPuesto);

	public List<PuestoCategoria> findAll();
	
	public List<PuestoCategoria> findLikeClaveNombre(String value);
	
	public List<PuestoCategoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	public List<PuestoCategoria> findByProperties(HashMap<String, Object> params) throws Exception;
}

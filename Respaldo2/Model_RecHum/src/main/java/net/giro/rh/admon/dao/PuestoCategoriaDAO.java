package net.giro.rh.admon.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.PuestoCategoria;

@Remote
public interface PuestoCategoriaDAO extends DAO<PuestoCategoria> {
	public void setEmpresa(Long idEmpresa);
	
	public long save(PuestoCategoria entity) throws Exception;
	
	public List<PuestoCategoria> saveOrUpdateList(List<PuestoCategoria> entities) throws Exception;
	
	public List<PuestoCategoria> findByIdPuesto(long idPuesto);

	public List<PuestoCategoria> findAll(Long idEmpresa);

	public List<PuestoCategoria> findByProperty(String propertyName, Object value);
	
	public List<PuestoCategoria> findByPuestoCategoria(long idPuesto, long idCategoria);
	
	public List<PuestoCategoria> findLikeClaveNombre(String value);
	
	public List<PuestoCategoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	public List<PuestoCategoria> findByProperties(HashMap<String, Object> params) throws Exception;
}

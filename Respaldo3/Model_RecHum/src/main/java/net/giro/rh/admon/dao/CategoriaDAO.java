package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.Categoria;

@Remote
public interface CategoriaDAO extends DAO<Categoria> {
	public long save(Categoria entity, long codigoEmpresa) throws Exception;
	
	public List<Categoria> saveOrUpdateList(List<Categoria> entities, long codigoEmpresa) throws Exception;
	
	public List<Categoria> findAll(long idEmpresa);
	
	public List<Categoria> findAllActivos(long idEmpresa);

	public List<Categoria> findByProperty(String propertyName, Object value, long idEmpresa);
	
	public List<Categoria> findLikeClaveNombre(String value, long idEmpresa);

	public List<Categoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value, long idEmpresa);
}

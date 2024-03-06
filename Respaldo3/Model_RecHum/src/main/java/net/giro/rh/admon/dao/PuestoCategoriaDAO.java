package net.giro.rh.admon.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.PuestoCategoria;

@Remote
public interface PuestoCategoriaDAO extends DAO<PuestoCategoria> {
	public long save(PuestoCategoria entity, long codigoEmpresa) throws Exception;
	
	public List<PuestoCategoria> saveOrUpdateList(List<PuestoCategoria> entities, long codigoEmpresa) throws Exception;
	
	public List<PuestoCategoria> findByIdPuesto(long idPuesto, long idEmpresa);

	public List<PuestoCategoria> findAll(long idEmpresa);

	public List<PuestoCategoria> findByProperty(String propertyName, Object value, long idEmpresa);
	
	public List<PuestoCategoria> findByPuestoCategoria(long idPuesto, long idCategoria, long idEmpresa);
	
	public List<PuestoCategoria> findLikeClaveNombre(String value, long idEmpresa);
	
	public List<PuestoCategoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value, long idEmpresa);
	
	public List<PuestoCategoria> findByProperties(HashMap<String, Object> params, long idEmpresa) throws Exception;
}

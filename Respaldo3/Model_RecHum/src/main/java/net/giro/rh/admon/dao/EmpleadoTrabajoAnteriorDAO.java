package net.giro.rh.admon.dao;

import java.util.List; 

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoTrabajoAnterior;

@Remote
public interface EmpleadoTrabajoAnteriorDAO extends DAO<EmpleadoTrabajoAnterior> {
	public long save(EmpleadoTrabajoAnterior entity, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoTrabajoAnterior> saveOrUpdateList(List<EmpleadoTrabajoAnterior> entities, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoTrabajoAnterior> findByProperty(String propertyName, Object value, long idEmpresa);

	public List<EmpleadoTrabajoAnterior> findAll(long idEmpresa);
	
	public List<EmpleadoTrabajoAnterior> findLikeClaveNombre(String value, long idEmpresa);
	
	public List<EmpleadoTrabajoAnterior> findByPropertyPojoCompleto(String propertyName, String tipo, long value, long idEmpresa);
}

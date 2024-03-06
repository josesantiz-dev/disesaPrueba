package net.giro.rh.admon.dao; 

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoPariente;

@Remote
public interface EmpleadoParienteDAO extends DAO<EmpleadoPariente> {
	public long save(EmpleadoPariente entity, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoPariente> saveOrUpdateList(List<EmpleadoPariente> entities, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoPariente> findAll(long idEmpresa);
	
	public List<EmpleadoPariente> findByIdEmpleadoParentesco(long idEmpleado, long idEmpresa);
	
	public List<EmpleadoPariente> findByProperty(String propertyName, Object value, long idEmpresa);
	
	public List<EmpleadoPariente> findLikeClaveNombre(String value, long idEmpresa);

	public List<EmpleadoPariente> findByPropertyPojoCompleto(String propertyName, String tipo, long value, long idEmpresa);
}

package net.giro.rh.admon.dao; 

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;


//import net.giro.rh.admon.beans.EmpleadoPariente;
import net.giro.rh.admon.beans.EmpleadoPariente;

@Remote
public interface EmpleadoParienteDAO extends DAO<EmpleadoPariente> {
	public void setEmpresa(Long idEmpresa);
	
	public List<EmpleadoPariente> findAll();
	
	public List<EmpleadoPariente> findByIdEmpleadoParentesco(long idEmpleado);
	
	public List<EmpleadoPariente> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoPariente> findLikeClaveNombre(String value);

	public List<EmpleadoPariente> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
}

package net.giro.rh.admon.dao;

import java.util.List;
import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoContrato;

@Remote
public interface EmpleadoContratoDAO extends DAO<EmpleadoContrato>{
	
	public void delete(EmpleadoContrato entity);

	public void update(EmpleadoContrato entity);

	public EmpleadoContrato findById(Integer id);

	public List<EmpleadoContrato> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoContrato> findAll();
	
	public List<EmpleadoContrato> findAllByIdEmpleado(long idEmpleado);
	
	public List<EmpleadoContrato> contratoValido(long idEmpleado);
}

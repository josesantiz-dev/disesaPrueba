package net.giro.rh.admon.dao;

import java.util.List;
import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoFiniquito;

@Remote 
public interface EmpleadoFiniquitoDAO extends DAO<EmpleadoFiniquito>{
	
	public void delete(EmpleadoFiniquito entity);

	public void update(EmpleadoFiniquito entity);

	public EmpleadoFiniquito findById(Integer id);

	public List<EmpleadoFiniquito> findByProperty(String propertyName, Object value);

	public List<EmpleadoFiniquito> findAll();

	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado);
	
}

package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoContrato;

@Remote
public interface EmpleadoContratoDAO extends DAO<EmpleadoContrato> {
	public void setEmpresa(Long idEmpresa);
	
	public List<EmpleadoContrato> findAllByIdEmpleado(long idEmpleado);
	
	public List<EmpleadoContrato> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoContrato> contratoValido(long idEmpleado);
}

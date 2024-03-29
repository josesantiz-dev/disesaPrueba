package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoBeneficiario;

@Remote
public interface EmpleadoBeneficiarioDAO extends DAO<EmpleadoBeneficiario> {

	public void delete(EmpleadoBeneficiario entity);

	public void update(EmpleadoBeneficiario entity);

	public EmpleadoBeneficiario findById(Integer id);

	public List<EmpleadoBeneficiario> findByProperty(String propertyName, Object value);

	public List<EmpleadoBeneficiario> findAll();
	public List<EmpleadoBeneficiario> findByIdEmpleado(long idEmpleado);
	
	public List<EmpleadoBeneficiario> findLikeClaveNombre(String value);
	
	public List<EmpleadoBeneficiario> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
}

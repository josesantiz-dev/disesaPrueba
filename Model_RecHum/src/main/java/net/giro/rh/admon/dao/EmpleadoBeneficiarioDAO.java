package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoBeneficiario;

@Remote
public interface EmpleadoBeneficiarioDAO extends DAO<EmpleadoBeneficiario> {
	public long save(EmpleadoBeneficiario entity, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoBeneficiario> saveOrUpdateList(List<EmpleadoBeneficiario> entities, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoBeneficiario> findAll(long idEmpresa);
	
	public List<EmpleadoBeneficiario> findByIdEmpleado(long idEmpleado, long idEmpresa);

	public List<EmpleadoBeneficiario> findByProperty(String propertyName, Object value, long idEmpresa);
	
	public List<EmpleadoBeneficiario> findLikeClaveNombre(String value, long idEmpresa);
	
	public List<EmpleadoBeneficiario> findByPropertyPojoCompleto(String propertyName, String tipo, long value, long idEmpresa);
}

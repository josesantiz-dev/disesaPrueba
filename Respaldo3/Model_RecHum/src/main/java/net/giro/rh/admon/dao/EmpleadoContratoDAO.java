package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoContrato;

@Remote
public interface EmpleadoContratoDAO extends DAO<EmpleadoContrato> {
	public long save(EmpleadoContrato entity, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoContrato> saveOrUpdateList(List<EmpleadoContrato> entities, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoContrato> findAll(long idEmpleado, String orderBy, boolean incluyeContratosCancelados, boolean incluyeEmpleadosSistema, long idEmpresa) throws Exception;
	
	public List<EmpleadoContrato> findByProperty(String propertyName, Object value, boolean incluyeContratosCancelados, boolean incluyeEmpleadosSistema, long idEmpresa) throws Exception;
	
	public List<EmpleadoContrato> contratoValido(long idEmpleado, long idEmpresa) throws Exception;
}

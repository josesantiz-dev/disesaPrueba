package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoFiniquito;

@Remote 
public interface EmpleadoFiniquitoDAO extends DAO<EmpleadoFiniquito> {
	public long save(EmpleadoFiniquito entity, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoFiniquito> saveOrUpdateList(List<EmpleadoFiniquito> entities, long codigoEmpresa) throws Exception;

	public List<EmpleadoFiniquito> findAll(long idEmpresa);

	public EmpleadoFiniquito findByIdEmpleado(long idEmpleado, long idContrato, boolean incluyeAprobados, boolean incluyeCancelado);

	public List<EmpleadoFiniquito> findFiniquitosByEmpleado(long idEmpleado, boolean incluyeAprobados, boolean incluyeCancelado);

	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado, long idEmpresa);
	
	public List<EmpleadoFiniquito> findByProperty(String propertyName, Object value, long idEmpresa);
}

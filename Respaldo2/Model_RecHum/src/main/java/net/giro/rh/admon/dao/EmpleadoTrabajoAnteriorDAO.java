package net.giro.rh.admon.dao;

import java.util.List; 

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoTrabajoAnterior;

@Remote
public interface EmpleadoTrabajoAnteriorDAO extends DAO<EmpleadoTrabajoAnterior> {
	public void setEmpresa(Long idEmpresa);
	
	public List<EmpleadoTrabajoAnterior> findByProperty(String propertyName, Object value);

	public List<EmpleadoTrabajoAnterior> findAll();
	
	public List<EmpleadoTrabajoAnterior> findLikeClaveNombre(String value);
	
	public List<EmpleadoTrabajoAnterior> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
}

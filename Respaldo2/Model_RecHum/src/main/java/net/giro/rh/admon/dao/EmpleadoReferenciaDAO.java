package net.giro.rh.admon.dao; 

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoReferencia;

@Remote
public interface EmpleadoReferenciaDAO extends DAO<EmpleadoReferencia> {
	public void setEmpresa(Long idEmpresa);
	
	public List<EmpleadoReferencia> findByProperty(String propertyName, Object value);

	public List<EmpleadoReferencia> findAll();
	
	public List<EmpleadoReferencia> findLikeClaveNombre(String value);
	
	public List<EmpleadoReferencia> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
}

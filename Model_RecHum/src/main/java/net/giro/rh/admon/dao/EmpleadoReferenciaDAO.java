package net.giro.rh.admon.dao; 

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.EmpleadoReferencia;

@Remote
public interface EmpleadoReferenciaDAO extends DAO<EmpleadoReferencia> {
	public long save(EmpleadoReferencia entity, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoReferencia> saveOrUpdateList(List<EmpleadoReferencia> entities, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoReferencia> findByProperty(String propertyName, Object value, long idEmpresa);

	public List<EmpleadoReferencia> findAll(long idEmpresa);
	
	public List<EmpleadoReferencia> findLikeClaveNombre(String value, long idEmpresa);
	
	public List<EmpleadoReferencia> findByPropertyPojoCompleto(String propertyName, String tipo, long value, long idEmpresa);	
}

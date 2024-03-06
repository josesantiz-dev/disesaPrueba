package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.plataforma.beans.ImpuestoEquivalencia;

@Remote
public interface ImpuestoEquivalenciaDAO extends DAO<ImpuestoEquivalencia> {
	public void setEmpresa(Long idEmpresa);
	
	public long save(ImpuestoEquivalencia entity) throws Exception;
	
	public List<ImpuestoEquivalencia> saveOrUpdateList(List<ImpuestoEquivalencia> entities) throws Exception;
	
	public List<ImpuestoEquivalencia> findByTransaccion(Long codigoTransaccion) throws Exception;
	
	public List<ImpuestoEquivalencia> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ImpuestoEquivalencia> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
}

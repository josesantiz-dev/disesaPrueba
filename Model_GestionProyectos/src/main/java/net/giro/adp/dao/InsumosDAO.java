package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.Insumos;

@Remote
public interface InsumosDAO  extends DAO<Insumos> {
	public long save(Insumos entity, long codigoEmpresa) throws Exception;
	
	public List<Insumos> saveOrUpdateList(List<Insumos> entities, long codigoEmpresa) throws Exception;
	
	public Insumos findActual(long idObra) throws Exception;
	
	public List<Insumos> findAll(long idObra, boolean incluyeCanceladas) throws Exception;
	
	public List<Insumos> findAllActivos(long idObra) throws Exception;
	
	public List<Insumos> findLike(String value, boolean incluyeCanceladas, long idEmpresa, int limite) throws Exception;
	
	public List<Insumos> findLikeProperty(String propertyName, final Object value, boolean incluyeCanceladas, long idEmpresa, int limite) throws Exception;

	public List<Insumos> findByProperty(String propertyName, final Object value, boolean incluyeCanceladas, long idEmpresa, int limite) throws Exception;
}

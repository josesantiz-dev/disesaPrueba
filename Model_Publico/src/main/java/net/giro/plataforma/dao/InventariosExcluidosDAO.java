package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.plataforma.beans.InventariosExcluidos;

@Remote
public interface InventariosExcluidosDAO extends DAO<InventariosExcluidos> {
	public void setEmpresa(Long idEmpresa);
	
	public long save(InventariosExcluidos entity) throws Exception;
	
	public List<InventariosExcluidos> saveOrUpdateList(List<InventariosExcluidos> entities) throws Exception;
	
	public List<InventariosExcluidos> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<InventariosExcluidos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
}

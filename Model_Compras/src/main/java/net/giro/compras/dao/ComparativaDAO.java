package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.Comparativa;
import net.giro.compras.comun.ExcepConstraint;

@Remote
public interface ComparativaDAO extends DAO<Comparativa> {
	public void OrderBy(String orderBy);
	
    public long save(Comparativa entity, Long idEmpresa) throws ExcepConstraint;
    
    public List<Comparativa> saveOrUpdateList(List<Comparativa> entities, Long idEmpresa) throws Exception;
	
	public List<Comparativa> findByProperty(String propertyName, final Object value, int limite, Long idEmpresa) throws Exception;

	public List<Comparativa> findLikeProperty(String propertyName, final Object value, int limite, Long idEmpresa) throws Exception;
	
	public List<Comparativa> findInProperty(String columnName, List<Object> values, Long idEmpresa) throws Exception;
}

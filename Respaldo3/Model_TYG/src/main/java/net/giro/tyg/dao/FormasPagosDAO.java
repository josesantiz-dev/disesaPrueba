package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.FormasPagos;

@Remote
public interface FormasPagosDAO extends DAO<FormasPagos> {
	public long save(FormasPagos entity, Long codigoEmpresa) throws ExcepConstraint;
    
    public List<FormasPagos> saveOrUpdateList(List<FormasPagos> entities, Long codigoEmpresa) throws Exception;

	public List<FormasPagos> findAll(String orderBy) throws Exception;
	
	public List<FormasPagos> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
    
    public List<FormasPagos> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception;
}

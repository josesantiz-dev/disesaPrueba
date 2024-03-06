package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.tyg.admon.FormasPagos;

@Remote
public interface FormasPagosDAO extends DAO<FormasPagos> {
	public List<FormasPagos> findAll(String orderBy) throws Exception;
	
	public List<FormasPagos> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
    
    public List<FormasPagos> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception;
}

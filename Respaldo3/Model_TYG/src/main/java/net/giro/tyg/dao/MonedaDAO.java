package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAO;
import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.Moneda;

import javax.ejb.Remote;

@Remote
public interface MonedaDAO extends DAO<Moneda> { 
	public long save(Moneda entity, Long codigoEmpresa) throws ExcepConstraint;
    
    public List<Moneda> saveOrUpdateList(List<Moneda> entities, Long codigoEmpresa) throws Exception;

	public List<Moneda> findAll();

	public List<Moneda> findByProperty(String propertyName, Object value);
	
	public List<Moneda> findLikeProperty(String propertyName, final String value);
	
	public Moneda findByAbreviacion(String valor);
}

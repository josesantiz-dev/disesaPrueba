package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAO;
import net.giro.tyg.admon.Moneda;

import javax.ejb.Remote;

@Remote
public interface MonedaDAO extends DAO<Moneda> { 
	public List<Moneda> findAll();

	public List<Moneda> findByProperty(String propertyName, Object value);
	
	public List<Moneda> findByPropertyLike(String propertyName, final String value);
	
	public Moneda findByAbreviacion(String valor);
}

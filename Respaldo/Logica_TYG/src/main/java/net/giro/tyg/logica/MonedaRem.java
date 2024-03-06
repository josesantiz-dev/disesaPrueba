package net.giro.tyg.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.tyg.admon.Moneda;

@Remote
public interface MonedaRem {
	public long save(Moneda entity) throws Exception;

	public void delete(Moneda entity) throws Exception;

	public Moneda update(Moneda entity) throws Exception;

	public Moneda findById(Short id) throws Exception;

	public List<Moneda> findByProperty(String propertyName, Object value) throws Exception;

	public List<Moneda> findAll() throws Exception;
	
	public List<Moneda> findByPropertyLike(String propertyName,final String value) throws Exception;
	
	public Moneda findByAbreviacion(String valor) throws Exception;
}

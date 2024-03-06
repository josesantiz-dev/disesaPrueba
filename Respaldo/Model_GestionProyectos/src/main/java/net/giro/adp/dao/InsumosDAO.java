package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.Insumos;

@Remote
public interface InsumosDAO  extends DAO<Insumos> {
	public List<Insumos> findByProperty(String propertyName, final Object value, int max);
	
	public List<Insumos> findByProperties(HashMap<String, Object> params) throws Exception;

	public List<Insumos> findLikeProperty(String propertyName, final Object value, int max);
	
	public List<Insumos> findLikeProperties(HashMap<String, String> params) throws Exception;
	
	public List<Insumos> findInProperty(String columnName, List<Object> values) throws Exception;
	
	public List<Insumos> comprobarInsumos(Long idObra);
	
	public List<Insumos> findByActivos(int max);
}

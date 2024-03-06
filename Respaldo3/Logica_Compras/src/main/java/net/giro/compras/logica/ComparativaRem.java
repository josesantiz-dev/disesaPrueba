package net.giro.compras.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.Comparativa;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ComparativaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void OrderBy(String orderBy);

	public Long save(Comparativa entity) throws Exception;
	
	public void update(Comparativa entity) throws Exception;
    
    public List<Comparativa> saveOrUpdateList(List<Comparativa> entities) throws Exception;
	
	public void delete(Long idComparativa) throws Exception;

	public Comparativa findById(Long idComparativa);

	public List<Comparativa> findByProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<Comparativa> findLikeProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<Comparativa> findInProperty(String propertyName, List<Object> values) throws Exception;
}

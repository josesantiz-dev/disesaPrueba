package net.giro.compras.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.Comparativa;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ComparativaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void OrderBy(String orderBy);

	public Long save(Comparativa entity) throws ExcepConstraint;
	
	public void update(Comparativa entity) throws ExcepConstraint;
    
    public List<Comparativa> saveOrUpdateList(List<Comparativa> entities) throws Exception;
	
	public void delete(Long id) throws ExcepConstraint;

	public Comparativa findById(Long id);

	public List<Comparativa> findByProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<Comparativa> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<Comparativa> findInProperty(String columnName, List<Object> values) throws Exception;
}

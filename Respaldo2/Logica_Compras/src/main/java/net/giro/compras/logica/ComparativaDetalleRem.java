package net.giro.compras.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.ComparativaDetalle;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ComparativaDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void OrderBy(String orderBy);
	
	public Long save(ComparativaDetalle entity) throws ExcepConstraint;
	
	public void update(ComparativaDetalle entity) throws ExcepConstraint;
    
    public List<ComparativaDetalle> saveOrUpdateList(List<ComparativaDetalle> entities) throws Exception;
	
	public void delete(Long id) throws ExcepConstraint;

	public ComparativaDetalle findById(Long id);

	public List<ComparativaDetalle> findByProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<ComparativaDetalle> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<ComparativaDetalle> findInProperty(String columnName, List<Object> values) throws Exception;
}

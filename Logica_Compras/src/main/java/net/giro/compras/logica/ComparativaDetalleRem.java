package net.giro.compras.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.ComparativaDetalle;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ComparativaDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void OrderBy(String orderBy);
	
	public Long save(ComparativaDetalle entity) throws Exception;
	
	public void update(ComparativaDetalle entity) throws Exception;
    
    public List<ComparativaDetalle> saveOrUpdateList(List<ComparativaDetalle> entities) throws Exception;
	
	public void delete(Long idComparativaDetalle) throws Exception;

	public ComparativaDetalle findById(long idComparativaDetalle);

	public List<ComparativaDetalle> findAll(long idComparativa) throws Exception;
	
	public List<ComparativaDetalle> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ComparativaDetalle> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ComparativaDetalle> findInProperty(String propertyName, List<Object> values) throws Exception;
}

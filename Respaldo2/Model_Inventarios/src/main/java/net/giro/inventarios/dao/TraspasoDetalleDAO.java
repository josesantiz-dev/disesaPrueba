package net.giro.inventarios.dao;

import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;
import net.giro.inventarios.beans.TraspasoDetalle;

@Remote
public interface TraspasoDetalleDAO extends DAO<TraspasoDetalle>{

	public List<TraspasoDetalle> findAll();
	
	public List<TraspasoDetalle> findAllActivos();
	
	public List<TraspasoDetalle> findByProperty(String propertyName, Object value);
	
	public List<TraspasoDetalle> findDetallesById(long idAlmacenTraspaso);
}

package net.giro.inventarios.dao;

import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;
import net.giro.inventarios.beans.AlmacenMovimientos;

@Remote
public interface AlmacenMovimientosDAO extends DAO<AlmacenMovimientos>{
	
	public void delete(AlmacenMovimientos entity);

	public void update(AlmacenMovimientos entity);

	public AlmacenMovimientos findById(Integer id);

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value);
	public List<AlmacenMovimientos> findByEspecificField(String propertyName, final Object value, int tipoMovimiento);

	public List<AlmacenMovimientos> findAll();
	
	public List<AlmacenMovimientos> findAllActivos();

}

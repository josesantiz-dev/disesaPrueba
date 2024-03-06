package net.giro.inventarios.dao;

import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;
import net.giro.inventarios.beans.AlmacenProducto;

@Remote
public interface AlmacenProductoDAO extends DAO<AlmacenProducto>{
	
	public void delete(AlmacenProducto entity);

	public void update(AlmacenProducto entity);

	public AlmacenProducto findById(Integer id);

	public List<AlmacenProducto> findByProperty(String propertyName, Object value);
	
	public List<AlmacenProducto> findCantidadEnAlmacen(long idAlmacen, long idProducto);
	
	public List<AlmacenProducto> findAlmacenProducto(long idAlmacen, long idProducto);

	public List<AlmacenProducto> findAll();
	
	public List<AlmacenProducto> findAllActivos();

	public List<AlmacenProducto> findExistentes(String campoBusqueda, long idAlmacen, String valor);
	
	public AlmacenProducto findProductoExistente(String campoBusqueda, long idAlmacen, String valor);
	
}

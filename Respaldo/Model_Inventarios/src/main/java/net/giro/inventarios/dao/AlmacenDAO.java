package net.giro.inventarios.dao;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;

import net.giro.inventarios.beans.Almacen;

@Remote
public interface AlmacenDAO extends DAO<Almacen>{
	
	public void delete(Almacen entity);

	public void update(Almacen entity);

	public Almacen findById(Integer id);

	public List<Almacen> findByProperty(String propertyName, Object value);
	public List<Almacen> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<Almacen> findLikeProperty(String propertyName, final Object value) throws Exception;
	public List<Almacen> findLikeProperties(HashMap<String, String> params) throws Exception;

	public List<Almacen> findAll();
	
	public List<Almacen> findAllActivos();
	
	public List<Almacen> comprobarPrincipal(Long idSucursal, Long idAlmacen) throws Exception;
	public List<Almacen> comprobarNombre(String nombre, Long idAlmacen) throws Exception;
}

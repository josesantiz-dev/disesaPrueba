package net.giro.inventarios.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.Almacen;

@Remote
public interface AlmacenDAO extends DAO<Almacen> {
	public void setEmpresa(Long idEmpresa);
		
	public long save(Almacen entity) throws Exception;
	
	public List<Almacen> saveOrUpdateList(List<Almacen> entities) throws Exception;
	
	public List<Almacen> findAll();
	
	public List<Almacen> findAllActivos();
	
	public List<Almacen> findByProperty(String propertyName, Object value);
	
	public List<Almacen> findLikeProperty(String propertyName, final Object value) throws Exception;
	
	public List<Almacen> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<Almacen> findLikeProperties(HashMap<String, String> params) throws Exception;

	public List<Almacen> comprobarPrincipal(Long idSucursal, Long idAlmacen) throws Exception;
	
	public List<Almacen> comprobarNombre(String nombre, Long idAlmacen) throws Exception;
}

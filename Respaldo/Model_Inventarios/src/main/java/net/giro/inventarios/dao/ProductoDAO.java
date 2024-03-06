package net.giro.inventarios.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.Producto;

@Remote
public interface ProductoDAO extends DAO<Producto>{
	
	public void delete(Producto entity);

	public void update(Producto entity);

	public List<Producto> saveOrUpdateList(List<Producto> entities) throws Exception;

	public Producto findById(Long id);

	public List<Producto> findAll();
	
	public List<Producto> findByNombre(String descripcion);
 	
	public List<Producto> findByClave(String clave);
	
	public List<Producto> findAllActivos();
	
	public List<Producto> findAllInactivos();
	
	public List<Producto> findAllOcultos();

	public List<Producto> findByClaveRango(String prefix, int inicioRango, int limiteRango) throws Exception;

	public List<Producto> findByProperty(String propertyName, Object value);

	public List<Producto> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<Producto> findByProperties(HashMap<String, Object> params, int limite, String orderBy) throws Exception;
	
	public List<Producto> findLikeProperty(String propertyName, String propertyValue, int limite);
	
	public List<Producto> findLikeProperty(String propertyName, String propertyValue, Long idFamilia, int limite);
	
	public List<Producto> findLikeProperty(String propertyName, String propertyValue, Long idFamilia, int tipo, int limite);
	
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, int limite) throws Exception;

	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, int limite, String orderBy) throws Exception;

	public List<Producto> findLikeProperties(HashMap<String, String> params, int tipo, int limite, String orderBy) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-06-19 | Javier Tirado 	| Añado el metodo findLikeProperties
 *  2.1 | 2016-11-12 | Javier Tirado 	| Añado el metodo saveOrUpdateList(List<Producto> entities)
 */
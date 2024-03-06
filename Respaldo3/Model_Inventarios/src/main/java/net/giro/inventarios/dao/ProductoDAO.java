package net.giro.inventarios.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.Producto;

@Remote
public interface ProductoDAO extends DAO<Producto> {
	public long save(Producto entity, long codigoEmpresa) throws Exception;
	
	public List<Producto> saveOrUpdateList(List<Producto> entities, long codigoEmpresa) throws Exception;
	
	public List<Producto> findAll(boolean incluyeCancelados, boolean incluyeOcultos, String orderBy, long idEmpresa);

	public List<Producto> findLike(String value, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, long idEmpresa, String orderBy, int limite);

	public List<Producto> findLikeProperty(String propertyName, Object value, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, long idEmpresa, String orderBy, int limite);

	public List<Producto> findByProperty(String propertyName, Object propertyValue, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, long idEmpresa, String orderBy, int limite);
	
	public List<Producto> findByClave(String clave, boolean incluyeCancelados, boolean incluyeOcultos, String orderBy, long idEmpresa);

	public List<Producto> findByClaveRango(String prefix, int inicioRango, int limiteRango, long idEmpresa) throws Exception;
	
	public List<Producto> findList(List<Long> idProductos) throws Exception;

	// ---------------------------------------------------------------------------------------------------------------------
	
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, long idEmpresa, int limite) throws Exception;

	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, String orderBy, long idEmpresa, int limite) throws Exception;

	public List<Producto> findLikeProperties(HashMap<String, String> params, int tipo, String orderBy, long idEmpresa, int limite) throws Exception;

	public List<Producto> findByProperties(HashMap<String, Object> params, long idEmpresa, int limite) throws Exception;

	public List<Producto> findByProperties(HashMap<String, Object> params, String orderBy, long idEmpresa, int limite) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-06-19 | Javier Tirado 	| Añado el metodo findLikeProperties
 *  2.1 | 2016-11-12 | Javier Tirado 	| Añado el metodo saveOrUpdateList(List<Producto> entities)
 */
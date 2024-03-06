package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.AlmacenProducto;

@Remote
public interface AlmacenProductoDAO extends DAO<AlmacenProducto> {
	public void setEmpresa(Long idEmpresa);
		
	public long save(AlmacenProducto entity) throws Exception;
	
	public List<AlmacenProducto> saveOrUpdateList(List<AlmacenProducto> entities) throws Exception;
	
	public List<AlmacenProducto> findAll();
	
	public List<AlmacenProducto> findAllActivos();
	
	public List<AlmacenProducto> findBy(long idAlmacen, Object value, long idFamilia, int tipoMaestro, int limite);
	
	public List<AlmacenProducto> findByProperty(String propertyName, Object value);
	
	public List<AlmacenProducto> findLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite);
	
	public List<AlmacenProducto> findLikeProperty(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite);
	
	public List<AlmacenProducto> findLikeProperty(String propertyName, String value);
	
	public List<AlmacenProducto> findAlmacenProducto(long idAlmacen, long idProducto);
	
	public List<AlmacenProducto> findCantidadEnAlmacen(long idAlmacen, long idProducto);

	public List<AlmacenProducto> findExistentes(String campoBusqueda, long idAlmacen, String valor);
	
	/***
	 * Recupera los productos con entradas al almacen indicado, aun cuando no tenga existencia
	 * @param idAlmacen ID Almacen
	 * @param value Valor de busqueda (en producto)
	 * @param idFamilia ID FAmilia
	 * @param tipoMaestro TipoMaestro (1:Insumos, 2:Admisnitrativo)
	 * @param limite Maximo de resultados permitidos
	 * @return
	 */
	public List<AlmacenProducto> findExistentes(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia);

	public List<AlmacenProducto> findExistentes(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia);
	
	public AlmacenProducto findProductoExistente(String campoBusqueda, long idAlmacen, String valor);
}

package net.giro.inventarios.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface AlmacenProductoRem  {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(AlmacenProducto entity) throws Exception;

	public List<AlmacenProducto> saveOrUpdateList(List<AlmacenProducto> entities) throws Exception;
	
	public void update(AlmacenProducto entity) throws Exception;
	
	public void delete(AlmacenProducto entity) throws Exception;
	
	public AlmacenProducto findById(Long id);
	
	public List<AlmacenProducto> findAll();
	
	public List<AlmacenProducto> findAllActivos();
	
	public List<AlmacenProducto> findBy(long idAlmacen, Object value, long idFamilia, int tipoMaestro, int limite);

	public List<AlmacenProducto> findByProperty(String propertyName, Object value);
	
	public List<AlmacenProducto> findLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite);
	
	public List<AlmacenProducto> findLikeProperty(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite);
	
	public List<AlmacenProducto> findExistentes(String campoBusqueda, long idAlmacen, String valor);

	/**
	 * Recupera los productos con entradas al almacen indicado
	 * @param idAlmacen
	 * @param value Valor de busqueda (en producto, cualquier atributo)
	 * @param idFamilia
	 * @param tipoMaestro   Tipo de Maestro: 1.Insumos, 2.Administrativo
	 * @param limite
	 * @param excluyeExistencia Indica si debe devolver los productos sin existencia
	 * @return
	 */
	public List<AlmacenProducto> findExistentes(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia);

	/**
	 * Recupera los productos con entradas al almacen indicado
	 * @param idAlmacen
	 * @param propertyName  Nombre de la propiedad a la cual debe realizar la busqueda (en producto)
	 * @param propertyValue Valor de busqueda (en producto)
	 * @param idFamilia
	 * @param tipoMaestro   Tipo de Maestro: 1.Insumos, 2.Administrativo
	 * @param limite
	 * @param excluyeExistencia Indica si debe devolver los productos sin existencia
	 * @return
	 */
	public List<AlmacenProducto> findExistentes(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia);
	
	public List<AlmacenProducto> findInventario(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite);

	public List<AlmacenProducto> findInventarioExistente(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite);

	public double findCantidadEnAlmacen(long idAlmacen, long idProducto);
	
	public AlmacenProducto findAlmacenProducto(long idAlmacen, long idProducto);

	public AlmacenProducto findProductoExistente(String campoBusqueda, long idAlmacen, String valor);
	
	public AlmacenProducto convertir(AlmacenProductoExt entity);

	public AlmacenProductoExt convertir(AlmacenProducto entity);
	
	// ------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------

	public Long save(AlmacenProductoExt entityExt) throws Exception;

	public void update(AlmacenProductoExt entity) throws Exception;

	public void delete(AlmacenProductoExt entityExt) throws Exception;
	
	public AlmacenProductoExt findByIdExt(Long id);

	public List<AlmacenProductoExt> findAllExt();
	
	public List<AlmacenProductoExt> findExtBy(long idAlmacen, Object value, long idFamilia, int tipoMaestro, int limite);

	public List<AlmacenProductoExt> findExtByProperty(String propertyName, Object value);

	public List<AlmacenProductoExt> findExtLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite);

	public List<AlmacenProductoExt> findExtLikeProperty(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite);

	public List<AlmacenProductoExt> findExistentesExt(String campoBusqueda, long idAlmacen, String valor);

	/**
	 * Recupera los productos con entradas al almacen indicado
	 * @param idAlmacen
	 * @param value Valor de busqueda (en producto, cualquier atributo)
	 * @param idFamilia
	 * @param tipoMaestro   Tipo de Maestro: 1.Insumos, 2.Administrativo
	 * @param limite
	 * @param excluyeExistencia Indica si debe devolver los productos sin existencia
	 * @return
	 */
	public List<AlmacenProductoExt> findExtExistentes(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia);

	/**
	 * Recupera los productos con entradas al almacen indicado
	 * @param idAlmacen
	 * @param propertyName  Nombre de la propiedad a la cual debe realizar la busqueda (en producto)
	 * @param propertyValue Valor de busqueda (en producto)
	 * @param idFamilia
	 * @param tipoMaestro   Tipo de Maestro: 1.Insumos, 2.Administrativo
	 * @param limite
	 * @param excluyeExistencia Indica si debe devolver los productos sin existencia
	 * @return
	 */
	public List<AlmacenProductoExt> findExtExistentes(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia);

	public List<AlmacenProductoExt> findExtInventario(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite);

	public List<AlmacenProductoExt> findExtInventarioExistente(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite);

	public AlmacenProductoExt findProductoExistenteExt(String campoBusqueda, long idAlmacen, String valor);
	
}

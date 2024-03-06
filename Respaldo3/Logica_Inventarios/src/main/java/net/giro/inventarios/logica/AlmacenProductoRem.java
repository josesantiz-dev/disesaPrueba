package net.giro.inventarios.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.beans.DesgloceSBO;
import net.giro.inventarios.beans.ProductoHistorial;
import net.giro.plataforma.InfoSesion;

@Remote
public interface AlmacenProductoRem  {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(AlmacenProducto entity) throws Exception;

	public List<AlmacenProducto> saveOrUpdateList(List<AlmacenProducto> entities) throws Exception;
	
	public void update(AlmacenProducto entity) throws Exception;
	
	public void delete(AlmacenProducto entity) throws Exception;
	
	public AlmacenProducto findById(long idAlmacenProducto);

	public List<AlmacenProducto> findAll(long idAlmacen, boolean excluyeSinExistencia, String orderBy) throws Exception;

	public List<AlmacenProducto> findAll(long idAlmacen, boolean excluyeSinExistencia) throws Exception;

	public List<AlmacenProducto> findAll(long idAlmacen) throws Exception;

	public List<AlmacenProducto> findLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, String orderBy, int limite);

	public List<AlmacenProducto> findLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite);
	
	public List<AlmacenProducto> findLikeProperty(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, String orderBy, int limite);

	public List<AlmacenProducto> findLikeProperty(long idAlmacen, String propertyName, String value, long idFamilia, int tipoMaestro, int limite);
	
	public List<AlmacenProducto> findByProperty(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, String orderBy, int limite);

	public List<AlmacenProducto> findByProperty(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, int limite);
	
	public List<AlmacenProducto> encontrarExistencia(long idAlmacen, List<Long> listProductos, boolean excluyeSinExistencia, String orderBy) throws Exception;

	public List<AlmacenProducto> encontrarExistencia(long idAlmacen, List<Long> listProductos) throws Exception;

	public HashMap<Long, Double> encontrarExistenciaProductos(long idAlmacen, List<Long> listProductos) throws Exception;
	
	public List<AlmacenProducto> findAlmacenProductos(long idAlmacen, long idProducto) throws Exception;

	public AlmacenProducto findAlmacenProducto(long idAlmacen, long idProducto) throws Exception;
	
	public AlmacenProducto findProductoExistente(long idAlmacen, String propertyName, Object value);

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
	
	public List<AlmacenProducto> findInventario(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) throws Exception;

	public List<AlmacenProducto> findInventarioExistente(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) throws Exception;

	public double findCantidadEnAlmacen(long idAlmacen, long idProducto) throws Exception;
	
	/**
	 * Recupera los movimiento de Entradas y Salidas del Producto en el Almacen/Bodega y fechas indicados
	 * @param idAlmacen ID Almacen/Bodega donde se realizara la busqueda 
	 * @param idProducto ID Producto para buscar
	 * @param fechaDesde Fecha a partir de la cual se tomaran en cuenta los movimientos
	 * @param fechaHasta Fecha limite hasta la cual se tomaran en cuenta los movimientos
	 * @return Listado de movimientos encontrados
	 * @throws Exception
	 */
	public List<ProductoHistorial> historial(long idAlmacen, long idProducto, Date fechaDesde, Date fechaHasta) throws Exception;
	
	/**
	 * Recupera las cantidad en solicitudes del Producto en el Almacen/Bodega indicados
	 * @param idAlmacen ID Almacen/Bodega del cual se realizara la busqueda 
	 * @param idProducto ID Producto para buscar
	 * @return
	 * @throws Exception
	 */
	public List<DesgloceSBO> desgloceSBO(long idAlmacen, long idProducto) throws Exception;
	
	public AlmacenProducto convertir(AlmacenProductoExt entity);

	public AlmacenProductoExt convertir(AlmacenProducto entity);
	
	// ------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------

	public Long save(AlmacenProductoExt entityExt) throws Exception;

	public void update(AlmacenProductoExt entity) throws Exception;

	public AlmacenProductoExt findByIdExt(long idAlmacenProducto);
	
	public List<AlmacenProductoExt> findLikePropertyExt(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, String orderBy, int limite);

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
}

package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.AlmacenProducto;

@Remote
public interface AlmacenProductoDAO extends DAO<AlmacenProducto> {
	public long save(AlmacenProducto entity, long codigoEmpresa) throws Exception;

	public List<AlmacenProducto> saveOrUpdateList(List<AlmacenProducto> entities, long codigoEmpresa) throws Exception;
	
	public List<AlmacenProducto> findAll(long idAlmacen, boolean excluyeSinExistencia, long idEmpresa, String orderBy) throws Exception;

	public List<AlmacenProducto> findLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, long idEmpresa, String orderBy, int limite);

	public List<AlmacenProducto> findLikeProperty(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, long idEmpresa, String orderBy, int limite);

	public List<AlmacenProducto> findByProperty(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, long idEmpresa, String orderBy, int limite);
	
	public List<AlmacenProducto> encontrarExistencia(long idAlmacen, List<Long> listProductos, boolean excluyeSinExistencia, long idEmpresa, String orderBy) throws Exception;

	public List<AlmacenProducto> findAlmacenProductos(long idAlmacen, long idProducto, long idEmpresa) throws Exception;

	public AlmacenProducto findAlmacenProducto(long idAlmacen, long idProducto, long idEmpresa) throws Exception;
	
	public AlmacenProducto findProductoExistente(long idAlmacen, String propertyName, Object value, long idEmpresa);
}

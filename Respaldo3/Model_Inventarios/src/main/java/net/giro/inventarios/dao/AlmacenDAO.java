package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.Almacen;

@Remote
public interface AlmacenDAO extends DAO<Almacen> {
	public long save(Almacen entity, long codigoEmpresa) throws Exception;
	
	public List<Almacen> saveOrUpdateList(List<Almacen> entities, long codigoEmpresa) throws Exception;
	
	public List<Almacen> findAll(int tipo, boolean incluyeEliminados, long idEmpresa) throws Exception;
	
	public List<Almacen> findLike(String value, int tipo, boolean incluyeEliminados, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<Almacen> findLikeProperty(String propertyName, Object value, int tipo, boolean incluyeEliminados, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Almacen> findByProperty(String propertyName, Object value, int tipo, boolean incluyeEliminados, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Almacen> comprobarPrincipal(Long idSucursal, Long idAlmacen, long idEmpresa) throws Exception;
	
	public List<Almacen> comprobarNombre(String nombre, Long idAlmacen, long idEmpresa) throws Exception;
	
	public List<Almacen> findByTipo(List<Integer> tipos, boolean incluyeEliminados, long idEmpresa, String orderBy) throws Exception;
}

package net.giro.inventarios.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface AlmacenRem{
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Almacen entity) throws Exception;
	
	public List<Almacen> saveOrUpdateList(List<Almacen> entities) throws Exception;

	public void update(Almacen entity) throws Exception;

	public void delete(long idAlmacen) throws Exception;

	public Almacen findById(long idAlmacen) throws Exception;

	public List<Almacen> findAll() throws Exception;

	public List<Almacen> findByTipo(List<Integer> tipos, boolean incluyeEliminados, String orderBy) throws Exception;

	public List<Almacen> findAll(int tipo) throws Exception;

	public List<Almacen> findAll(int tipo, boolean incluyeEliminados) throws Exception;

	public List<Almacen> findByProperty(String propertyName, Object value) throws Exception;

	public List<Almacen> findByProperty(String propertyName, Object value, int tipo, String orderBy, int limite) throws Exception;

	public List<Almacen> findLike(String value, int tipo, boolean incluyeEliminados, String orderBy, int limite) throws Exception;
	
	public List<Almacen> findLikeProperty(String propertyName, Object value, int tipo, boolean incluyeEliminados, String orderBy, int limite) throws Exception;
	
	public List<Almacen> findLikeProperty(String propertyName, String value) throws Exception;
	
	public List<Almacen> findLikeProperty(String propertyName, String value, int tipo) throws Exception;
	
	public boolean comprobarPrincipal(long idSucursal, long idAlmacen) throws Exception;
	
	public boolean comprobarNombre(String nombre, long idAlmacen) throws Exception;

	public List<Almacen> findByEncargado(long idEncargado) throws Exception;
	
	public Almacen convertir(AlmacenExt extendido) throws Exception;
	
	public AlmacenExt convertir(Almacen entity) throws Exception;

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	public Long save(AlmacenExt entityExt) throws Exception;

	public void update(AlmacenExt entity) throws Exception;

	//public AlmacenExt findByIdExt(long idAlmacen) throws Exception;

	//public List<AlmacenExt> findAllExt() throws Exception;

	//public List<AlmacenExt> findExtByProperty(String propertyName, Object value) throws Exception;

	//public List<AlmacenExt> findExtLike(String value, int tipo, boolean incluyeEliminados, int limite) throws Exception;
	
	//public List<AlmacenExt> findExtLikeProperty(String propertyName, Object value, int tipo, boolean incluyeEliminados, int limite) throws Exception;

	//public List<AlmacenExt> findExtLikeProperty(String propertyName, String value) throws Exception;
}

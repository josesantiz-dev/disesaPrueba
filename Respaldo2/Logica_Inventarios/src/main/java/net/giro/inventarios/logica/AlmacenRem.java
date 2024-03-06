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

	public void delete(Almacen entity) throws Exception;

	public Almacen findById(Long id);
	
	public List<Almacen> findAll();

	public List<Almacen> findAllActivos();

	public List<Almacen> findByProperty(String propertyName, Object value);
	
	public List<Almacen> findLikeProperty(String propertyName, String value) throws Exception;
	
	public boolean comprobarPrincipal(Long idSucursal, Long idAlmacen) throws Exception;
	
	public boolean comprobarNombre(String nombre, Long idAlmacen) throws Exception;

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	public Long save(AlmacenExt entityExt) throws Exception;

	public void update(AlmacenExt entity) throws Exception;

	public void delete(AlmacenExt entityExt) throws Exception;

	public AlmacenExt findByIdExt(Long id) throws Exception;

	public List<AlmacenExt> findAllExt() throws Exception;

	public List<AlmacenExt> findExtByProperty(String propertyName, Object value) throws Exception;

	public List<AlmacenExt> findExtLikeProperty(String propertyName, String value) throws Exception;
}

package net.giro.inventarios.logica;

import java.util.List;
import javax.ejb.Remote;
import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenExt;

@Remote
public interface AlmacenRem{
	
	public Long save(Almacen entity) throws ExcepConstraint;
	public Long save(AlmacenExt entityExt) throws ExcepConstraint;
	
	public void delete(Almacen entity) throws ExcepConstraint;
	public void delete(AlmacenExt entityExt) throws ExcepConstraint;

	public Almacen update(Almacen entity) throws ExcepConstraint;
	public Almacen update(AlmacenExt entity) throws ExcepConstraint;

	public Almacen findById(Long id);
	public AlmacenExt findByIdExt(Long id);

	public List<Almacen> findByProperty(String propertyName, Object value);
	public List<AlmacenExt> findExtByProperty(String propertyName, Object value);

	public List<Almacen> findLikeProperty(String propertyName, String value) throws Exception;
	public List<AlmacenExt> findExtLikeProperty(String propertyName, String value) throws Exception;

	public List<Almacen> findAll();
	public List<AlmacenExt> findAllExt();
	
	public List<Almacen> findAllActivos();
	
	public boolean comprobarPrincipal(Long idSucursal, Long idAlmacen) throws Exception;
	public boolean comprobarNombre(String nombre, Long idAlmacen) throws Exception;
}

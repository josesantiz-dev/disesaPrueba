package net.giro.inventarios.logica;
import java.util.List;
import javax.ejb.Remote;
import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;

@Remote
public interface AlmacenProductoRem  {
	
	public Long save(AlmacenProducto entity) throws ExcepConstraint;
	public Long save(AlmacenProductoExt entityExt) throws ExcepConstraint;
	
	public void delete(AlmacenProducto entity) throws ExcepConstraint;
	public void delete(AlmacenProductoExt entityExt) throws ExcepConstraint;

	public AlmacenProducto update(AlmacenProducto entity) throws ExcepConstraint;
	public AlmacenProducto update(AlmacenProductoExt entity) throws ExcepConstraint;

	public AlmacenProducto findById(Long id);
	public AlmacenProductoExt findByIdExt(Long id);

	public List<AlmacenProducto> findByProperty(String propertyName, Object value);
	
	public double findCantidadEnAlmacen(long idAlmacen, long idProducto);
	
	public AlmacenProducto findAlmacenProducto(long idAlmacen, long idProducto);

	public List<AlmacenProducto> findAll();
	public List<AlmacenProductoExt> findAllExt();
	
	public List<AlmacenProducto> findAllActivos();

	public List<AlmacenProductoExt> findExistentesExt(String campoBusqueda, long idAlmacen, String valor);
	public List<AlmacenProducto> findExistentes(String campoBusqueda, long idAlmacen, String valor);

	public AlmacenProducto findProductoExistente(String campoBusqueda, long idAlmacen, String valor);
	public AlmacenProductoExt findProductoExistenteExt(String campoBusqueda, long idAlmacen, String valor);

}

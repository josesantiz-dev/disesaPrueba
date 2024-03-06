package net.giro.inventarios.logica;
import java.util.List;
import javax.ejb.Remote;
import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;

@Remote
public interface AlmacenTraspasoRem {
	
	public Long save(AlmacenTraspaso entity) throws ExcepConstraint;
	public Long save(AlmacenTraspasoExt entityExt) throws ExcepConstraint;
	
	public void delete(AlmacenTraspaso entity) throws ExcepConstraint;
	public void delete(AlmacenTraspasoExt entityExt) throws ExcepConstraint;

	public AlmacenTraspaso update(AlmacenTraspaso entity) throws ExcepConstraint;
	public AlmacenTraspaso update(AlmacenTraspasoExt entity) throws ExcepConstraint;

	public AlmacenTraspaso findById(Long id);
	public AlmacenTraspasoExt findByIdExt(Long id);

	public List<AlmacenTraspaso> findByProperty(String propertyName, Object value);

	public List<AlmacenTraspaso> findAll();
	public List<AlmacenTraspasoExt> findAllExt();
	
	public List<AlmacenTraspaso> findAllActivos();
	
	

	public List<AlmacenTraspaso> findByAlmacenOrigen(String nombreAlmacen);
	public List<AlmacenTraspasoExt> findExtByAlmacenOrigen(String nombreAlmacen);

	public List<AlmacenTraspaso> findByAlmacenDestino(String nombreAlmacen);
	public List<AlmacenTraspasoExt> findExtByAlmacenDestino(String nombreAlmacen);

	public List<AlmacenTraspaso> findByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen);
	public List<AlmacenTraspasoExt> findExtByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen);

	public AlmacenTraspaso convertir(AlmacenTraspasoExt entity);
	public AlmacenTraspasoExt convertir(AlmacenTraspaso entity);
}

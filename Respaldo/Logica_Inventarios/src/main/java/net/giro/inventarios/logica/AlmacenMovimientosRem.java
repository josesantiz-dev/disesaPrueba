package net.giro.inventarios.logica;
import java.util.List;
import javax.ejb.Remote;
import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenMovimientosExt;

@Remote
public interface AlmacenMovimientosRem {
	
	public Long save(AlmacenMovimientos entity) throws ExcepConstraint;
	public Long save(AlmacenMovimientosExt entityExt) throws ExcepConstraint;
	
	public void delete(AlmacenMovimientos entity) throws ExcepConstraint;
	public void delete(AlmacenMovimientosExt entityExt) throws ExcepConstraint;

	public AlmacenMovimientos update(AlmacenMovimientos entity) throws ExcepConstraint;
	public AlmacenMovimientos update(AlmacenMovimientosExt entity) throws ExcepConstraint;

	public AlmacenMovimientos findById(Long id);
	public AlmacenMovimientosExt findByIdExt(Long id);
	
	public List<AlmacenMovimientos> findByEspecificField(String propertyName, final Object value, int tipoMovimiento);
	public List<AlmacenMovimientosExt> findExtByEspecificField(String propertyName, final Object value, int tipoMovimiento); 

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value);

	public List<AlmacenMovimientos> findAll();
	public List<AlmacenMovimientosExt> findAllExt();
	
	public List<AlmacenMovimientos> findAllActivos();
	
}

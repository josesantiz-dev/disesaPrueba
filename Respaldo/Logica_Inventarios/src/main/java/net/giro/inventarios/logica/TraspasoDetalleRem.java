package net.giro.inventarios.logica;
import java.util.List;
import javax.ejb.Remote;
import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.beans.TraspasoDetalleExt;

@Remote
public interface TraspasoDetalleRem {

	public Long save(TraspasoDetalle entity) throws ExcepConstraint;
	public Long save(TraspasoDetalleExt entityExt) throws ExcepConstraint;
	
	public void delete(TraspasoDetalle entity) throws ExcepConstraint;
	public void delete(TraspasoDetalleExt entityExt) throws ExcepConstraint;

	public TraspasoDetalle update(TraspasoDetalle entity) throws ExcepConstraint;
	public TraspasoDetalle update(TraspasoDetalleExt entity) throws ExcepConstraint;

	public TraspasoDetalle findById(Long id);
	public TraspasoDetalleExt findByIdExt(Long id);

	public List<TraspasoDetalle> findByProperty(String propertyName, Object value);
	public List<TraspasoDetalleExt> findExtByProperty(String propertyName, Object value);

	public List<TraspasoDetalle> findAll();
	public List<TraspasoDetalleExt> findAllExt();
	
	public List<TraspasoDetalle> findAllActivos();
	
	public List<TraspasoDetalleExt> findDetallesExtById(long idAlmacenTraspaso);

	public TraspasoDetalle convertir(TraspasoDetalleExt entity);
	public TraspasoDetalleExt convertir(TraspasoDetalle entity);
}

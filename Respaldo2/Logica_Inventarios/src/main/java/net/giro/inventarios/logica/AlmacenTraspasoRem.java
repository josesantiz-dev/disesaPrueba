package net.giro.inventarios.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface AlmacenTraspasoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(AlmacenTraspaso entity) throws Exception;
	
	public List<AlmacenTraspaso> saveOrUpdateList(List<AlmacenTraspaso> entities) throws Exception;

	public void update(AlmacenTraspaso entity) throws Exception;
	
	public void delete(AlmacenTraspaso entity) throws Exception;

	public AlmacenTraspaso findById(Long id);

	public List<AlmacenTraspaso> findAll();
	
	public List<AlmacenTraspaso> findAllActivos();

	public List<AlmacenTraspaso> findByProperty(String propertyName, Object value);

	public List<AlmacenTraspaso> findLikeProperty(String propertyName, String value);

	public List<AlmacenTraspaso> findByAlmacen(String nombreAlmacen);
	
	public List<AlmacenTraspaso> findByAlmacenOrigen(String nombreAlmacen);

	public List<AlmacenTraspaso> findByAlmacenDestino(String nombreAlmacen);

	public List<AlmacenTraspaso> findByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen);
	
	public List<AlmacenTraspaso> findLikeWithDestino(String value, long idAlmacenDestino);
	
	public List<AlmacenTraspaso> findIncompletosLikeProperty(String propertyName, String propertyValue, String orderBy, int limite);

	public AlmacenTraspaso convertir(AlmacenTraspasoExt entity);
	
	public AlmacenTraspasoExt convertir(AlmacenTraspaso entity);

	// -----------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------
	
	public Long save(AlmacenTraspasoExt entityExt) throws Exception;
	
	public void update(AlmacenTraspasoExt entity) throws Exception;
	
	public void delete(AlmacenTraspasoExt entityExt) throws Exception;
	
	public AlmacenTraspasoExt findByIdExt(Long id);

	public List<AlmacenTraspasoExt> findExtLikeProperty(String propertyName, String value);

	public List<AlmacenTraspasoExt> findExtByAlmacen(String nombreAlmacen);
	
	public List<AlmacenTraspasoExt> findAllExt();
	
	public List<AlmacenTraspasoExt> findExtByAlmacenOrigen(String nombreAlmacen);
		
	public List<AlmacenTraspasoExt> findExtByAlmacenDestino(String nombreAlmacen);
	
	public List<AlmacenTraspasoExt> findExtByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen);
	
	public List<AlmacenTraspasoExt> findExtLikeWithDestino(String value, long idAlmacenDestino);

	public List<AlmacenTraspasoExt> findExtIncompletosLikeProperty(String propertyName, String propertyValue, String orderBy, int limite);
}

package net.giro.inventarios.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface AlmacenMovimientosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(AlmacenMovimientos entity) throws Exception;
	
	public List<AlmacenMovimientos> saveOrUpdateList(List<AlmacenMovimientos> entities) throws Exception;

	public void update(AlmacenMovimientos entity) throws Exception;
	
	public void delete(AlmacenMovimientos entity) throws Exception;

	public AlmacenMovimientos findById(Long id);

	public List<AlmacenMovimientos> findAll();
	
	public List<AlmacenMovimientos> findAllActivos();

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value);

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value, int limite);

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value, int tipoMovimiento, String tipoEntrada, int limite);

	public List<AlmacenMovimientos> findLikeProperty(String propertyName, String value, int tipoMovimiento, String tipoEntrada, int limite);

	public List<AlmacenMovimientos> findSalidaByTraspaso(long idTraspaso, int tipoTraspaso, int limite);
	
	public List<AlmacenMovimientos> findByEspecificField(String propertyName, final Object value, int tipoMovimiento);
	
	public AlmacenMovimientos convertir(AlmacenMovimientosExt target);

	public AlmacenMovimientosExt convertir(AlmacenMovimientos target);

	// ----------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------------------------------------------------------
	
	public Long save(AlmacenMovimientosExt entityExt) throws Exception;
	
	public void update(AlmacenMovimientosExt entity) throws Exception;
	
	public void delete(AlmacenMovimientosExt entityExt) throws Exception;
	
	public AlmacenMovimientosExt findByIdExt(Long id);
	
	public List<AlmacenMovimientosExt> findAllExt();
	
	public List<AlmacenMovimientosExt> findExtAllActivos();

	public List<AlmacenMovimientosExt> findExtByProperty(String propertyName, Object value, int limite);

	public List<AlmacenMovimientosExt> findExtByProperty(String propertyName, Object value, int tipoMovimiento, String tipoEntrada, int limite);

	public List<AlmacenMovimientosExt> findExtLikeProperty(String propertyName, String value, int tipoMovimiento, String tipoEntrada, int limite);

	public List<AlmacenMovimientosExt> findExtSalidaByTraspaso(long idTraspaso, int tipoTraspaso, int limite);
	
	public List<AlmacenMovimientosExt> findExtByEspecificField(String propertyName, final Object value, int tipoMovimiento); 
}

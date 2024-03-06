package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.AlmacenMovimientos;

@Remote
public interface AlmacenMovimientosDAO extends DAO<AlmacenMovimientos> {
	public void setEmpresa(Long idEmpresa);
		
	public long save(AlmacenMovimientos entity) throws Exception;
	
	public List<AlmacenMovimientos> saveOrUpdateList(List<AlmacenMovimientos> entities) throws Exception;
	
	public List<AlmacenMovimientos> findAll();
	
	public List<AlmacenMovimientos> findAllActivos();

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value, int limite);

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value, int tipoMovimiento, String tipoEntrada, int limite);

	public List<AlmacenMovimientos> findLikeProperty(String propertyName, String value, int tipoMovimiento, String tipoEntrada, int limite);

	public List<AlmacenMovimientos> findSalidaByTraspaso(long idTraspaso, int tipoTraspaso, int limite);
	
	public List<AlmacenMovimientos> findByEspecificField(String propertyName, final Object value, int tipoMovimiento);
}

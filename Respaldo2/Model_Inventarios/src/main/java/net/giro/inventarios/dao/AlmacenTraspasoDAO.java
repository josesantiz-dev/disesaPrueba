package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.AlmacenTraspaso;

@Remote
public interface AlmacenTraspasoDAO extends DAO<AlmacenTraspaso> {
	public void setEmpresa(Long idEmpresa);
		
	public long save(AlmacenTraspaso entity) throws Exception;
	
	public List<AlmacenTraspaso> saveOrUpdateList(List<AlmacenTraspaso> entities) throws Exception;
	
	public List<AlmacenTraspaso> findAll();
	
	public List<AlmacenTraspaso> findAllActivos();

	public List<AlmacenTraspaso> findByProperty(String propertyName, Object value);

	public List<AlmacenTraspaso> findLikeProperty(String propertyName, String value);

	public List<AlmacenTraspaso> findByAlmacen(String nombreAlmacen);

	public List<AlmacenTraspaso> findByAlmacenOrigen(String nombreAlmacen);

	public List<AlmacenTraspaso> findByAlmacenDestino(String nombreAlmacen);
	
	public List<AlmacenTraspaso> findByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen);
	
	public List<AlmacenTraspaso> findLikeWithDestino(String value, long idAlmacenDestino);
	
	/**
	 * Consulta los traspaso que no han sido recibidos total o parcialmente
	 * @param propertyName Nombre de la propiedad donde se buscara. Default *
	 * @param propertyValue Valor para la busqueda
	 * @param orderBy 
	 * @param limite
	 * @return
	 */
	public List<AlmacenTraspaso> findIncompletosLikeProperty(String propertyName, String propertyValue, String orderBy, int limite);
}

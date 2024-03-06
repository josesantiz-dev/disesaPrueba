package net.giro;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.giro.compras.comun.ExcepConstraint;

/**
 * <pre>
 * Interface generica para cualquier Objecto de Acceso a Datos(DAO)
 * se declara todos los metodos usuales que deberia de tener un DAO.
 * </pre>
 *
 * @author Oscar Vasquez
 * @version 01.00
 * <p>
 * <pre>
 * <b>Fecha:</b>2012-02-19
 * <b>Historial:</b>
 * Version  |    Fecha    |   Modificado por   |  Metodo Modificado |  Desc. del cambio
 *---------------------------------------------------------------------------------------------------------
 *  01.00     2012-02-19  	  ovasquez    	    Ninguno.	         Se creo la clase.
 * <P>
 * DAO.java
 * <b>Copyright (c) 2012 Banco Central de Reserva del Peru</b>
 * </pre>
 **/

public interface DAO<T extends Serializable> {
	
	/**
	 * Agregar una entidad de tipo <T>.
	 * @param object La entidad de tipo <T>
	 * @throws Exception 
	 */
    public long save(T object, Long idEmpresa) throws ExcepConstraint;

    /**
     * Actualizar una entida de tipo <T>.
     * @param object La entidad de tipo <T>
     */
    public void update(T object)  throws ExcepConstraint;

    /**
     * Agregar o Actualizar una lista de tipo <T>.
     * @param entities Lista de entidad de tipo <T>
     */
    public List<T> saveOrUpdateList(List<T> entities, Long idEmpresa) throws Exception;
    
    /**
     * Eliminar una entidad segun el ID de la entidad.  
     * @param id el ID de la entidad.
     * @throws ExcepConstraint 
     */
    public void delete(Object id) throws ExcepConstraint;

    /**
     * Buscar a la entidad segun el ID de la entidad.
     * @param id
     * @return La entidad de tipo <T>
     */
    public T findById(long id);

    /**
     * Listar a todas las entidades
     * @return Lista de todas las entidades de tipo <T>
     */
    public List<T> findAll();

    /**
     * Actualiza la lista de entidades objetos con los datos de las entidades en la BD,
     * es decir luego de ejecutar este metodo se reemplazara los datos del objeto
     * con los valores que esten en la BD. 
     * Es lo contrario al metodo em.flush().
     * @param result List de entidades tipo <T> 
     */
    public void refresh(List<T> result);

    /**
     * Actualiza la entidade objeto con los datos de las entidad en la BD,
     * es decir luego de ejecutar este metodo se reemplazara los datos del objeto
     * con los valores que esten en la BD. 
     * Es lo contrario al metodo em.flush().
     * @param o una entidad tipo <T>
     */
    public void refresh(T object);
    
    /**
     * 
     * @param columnName
     * @param value
     * @return
     */
    public List<T> findByColumnName(String columnName, Object value);
    
    public List<T> findLikeColumnName(String columnName, String value);
    
    public List<T> namedQuery(String queryName, List<Object> parameterValues);

    public List<T> namedQuery(String queryName, Map<String, Object> parameterValues);

    public List<T> query(String queryString, List<Object> parameterValues);

    public List<T> query(String queryString, Map<String, Object> parameterValues);

    public List<T> nativeQuery(String queryString, List<Object> parameterValues);

    public List<T> nativeQuery(String queryString, Map<String, Object> parameterValues);

    public List<T> nativeQuery(String queryString, List<Object> parameterValues, String resultSetMapping);

    public List<T> nativeQuery(String queryString, Map<String, Object> parameterValues, String resultSetMapping);
}

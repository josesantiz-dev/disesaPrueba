package net.giro;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;

import org.apache.log4j.Logger;

import net.giro.co.dao.CorrelativoInterfaceLocal;
import net.giro.comun.ExcepConstraint;

@SuppressWarnings("unchecked")
public abstract class DAOImpl<T extends Serializable> implements DAO<T> {
    private static Logger log = Logger.getLogger(DAOImpl.class);
    @PersistenceContext
    protected EntityManager em;
    protected Class<T> persistenceClass;
    @EJB
    CorrelativoInterfaceLocal ifzCorrelativo;
    
	public DAOImpl() {
        this.persistenceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long save(T entity, Long idEmpresa) throws ExcepConstraint {
		Long secuen = -1L;
		Class cls = null;
		Method setter = null;
		String tabla = "";

		try {
			if (idEmpresa == null || idEmpresa <= 0L)
				idEmpresa = 1L;
			else if (idEmpresa > 10000000)
				idEmpresa -= 10000000;
			
			cls = entity.getClass();
			setter = cls.getMethod("setId", long.class);
			tabla = entity.getClass().getCanonicalName();
		} catch (Exception e) {
			log.error("TYG :: Error REFLECTION ", e);
			throw new ExcepConstraint("REFLECTION", entity);
		}
		
		try {
			secuen = ifzCorrelativo.GenerarCorrelativo(idEmpresa, tabla);
			setter.invoke(entity, secuen);
		} catch (Exception e) {
			log.error("TYG :: Error ASIGNAR SECUENCIA :: " + tabla + " :: " + secuen, e);
			throw new ExcepConstraint("ASIGNAR SECUENCIA", entity);			
		}
		
		try {
			em.persist(entity);
			em.flush();
		} catch (Exception e) {
			log.error("TYG :: Error GRABAR ENTIDAD :: " + tabla + " :: " + secuen, e);
			throw new ExcepConstraint("GRABAR ENTIDAD", entity);			
		}
		
        return secuen;
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<T> saveOrUpdateList(List<T> entities, Long idEmpresa) throws Exception {
		Long secuen = -1l;
		String tabla = "";
		long idStored = 0;
		int index = 0;
		// -----------------------------
		@SuppressWarnings("rawtypes")
		Class cls = null;
		Method setter = null;
		Method getter = null;
		T base = null;

		try {
			if (entities == null || entities.isEmpty())
				return entities;
			if (idEmpresa == null || idEmpresa <= 0L)
				idEmpresa = 1L;
			else if (idEmpresa > 10000000)
				idEmpresa -= 10000000;
			
			base = entities.get(0);
			cls = base.getClass();
			setter = cls.getMethod("setId", long.class);
			getter = cls.getMethod("getId");
			tabla = base.getClass().getCanonicalName();
		} catch (Exception e) {
			log.error("TYG :: Error REFLECTION ", e);
			throw e;
		}
		
		for (T entity : entities) {
			index = entities.indexOf(entity);
			idStored = getter.invoke(entity) == null ? 0 : (long) getter.invoke(entity);
			if (idStored <= 0) {
				try {
					// Genero ID
					secuen = ifzCorrelativo.GenerarCorrelativo(idEmpresa, tabla);
					setter.invoke(entity, secuen);
				} catch (Exception e) {
					log.error("TYG :: Error ASIGNAR SECUENCIA :: " + tabla + " :: " + secuen + " [" + index + "]", e);
					throw new ExcepConstraint("ASIGNAR SECUENCIA", entity);			
				}
				
				try {
					// Guardo ENTITY
					em.persist(entity);
					em.flush();
				} catch (Exception e) {
					log.error("TYG :: Error GRABAR ENTIDAD :: " + tabla + " :: " + secuen + " [" + index + "]", e);
					throw new ExcepConstraint("GRABAR ENTIDAD", entity);			
				}
			} else {
				// Actualizo
				try {
					em.merge(entity);
				} catch (Exception e) {
					log.error("TYG :: Error ACTUALIZAR ENTIDAD :: " + tabla + " :: " + idStored + " [" + index + "]", e);
					throw new ExcepConstraint("ACTUALIZAR ENTIDAD", entity);	
				}
			}
		}
		
		return entities;
    }

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void update(T object) throws ExcepConstraint {
        em.merge(object);
    }

    public void delete(Object id) throws ExcepConstraint {
        T o = em.find(persistenceClass, id);
        puedeBorrar(o);
        em.remove(o);	
    }

    public void puedeBorrar(T entity) throws ExcepConstraint {
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public T findById(long id) {
        return em.find(persistenceClass, id);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> findAll() {
        String sql = getQuery();
        
        Query query = em.createQuery(sql, persistenceClass);
        query.setMaxResults(getMaxResults());
        return query.getResultList();
    }

    public int getMaxResults() {
    	return 1000;
    }
    
    public String getQuery() {
    	return "SELECT o FROM " + persistenceClass.getSimpleName() +" o ";
    }
    
    public void refresh(List<T> result) {
        for (T o : result) {
            em.refresh(o);
        }
    }

    public void refresh(T o) {
        em.refresh(o);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> findByProperty(String columnName, Object value) {
    	return findByColumnName(columnName,value);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> findByColumnName(String columnName, Object value) {
        String sql =  getQuery() + " WHERE o." + columnName + " = :" + columnName;
        Query query = em.createQuery(sql, persistenceClass);
        query.setParameter(columnName, value);
        query.setMaxResults(getMaxResults());
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> findLikeColumnName(String columnName, String value) {
    	String sqlWhere = "";
    	StringBuffer sb = null;
    	
    	if(value!=null && !"".equals(value)){
    		sqlWhere += " WHERE lower(o." + columnName + ") LIKE :" + columnName;
    		sb = new StringBuffer();
    		sb.append("%");
    		sb.append(value.toLowerCase());
    		sb.append("%");
    		log.info(sb.toString());
    	}
    	
        String strQuery = getQuery() + sqlWhere;
        log.info(strQuery);
        TypedQuery<T> query = em.createQuery(strQuery, persistenceClass);
        
        if(value!=null && !"".equals(value))
        	query.setParameter(columnName, sb.toString());
        
        query.setMaxResults(getMaxResults());
        return query.getResultList();
    }
    
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> namedQuery(String queryName, List<Object> parameterValues) {
        Query query = em.createNamedQuery(queryName, persistenceClass);
        query.setMaxResults(getMaxResults());
        return setParameters(query, parameterValues).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> namedQuery(String queryName, Map<String, Object> parameterValues) {
        /*Query query = em.createNamedQuery(queryName, persistenceClass);
        return setParameters(query, parameterValues).getResultList();*/
    	return null;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> query(String queryString, List<Object> parameterValues) {
        Query query = em.createQuery(queryString, persistenceClass);
        return setParameters(query, parameterValues).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> query(String queryString, Map<String, Object> parameterValues) {
        /*Query query = em.createQuery(queryString, persistenceClass);
        return setParameters(query, parameterValues).getResultList();*/
    	return null;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> nativeQuery(String queryString, List<Object> parameterValues) {
        Query query = em.createNativeQuery(queryString, persistenceClass);
        return setParameters(query, parameterValues).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> nativeQuery(String queryString, Map<String, Object> parameterValues) {
        Query query = em.createNativeQuery(queryString, persistenceClass);
        return setParameters(query, parameterValues).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> nativeQuery(String queryString, List<Object> parameterValues, String resultSetMapping) {
        Query query = em.createNativeQuery(queryString, resultSetMapping);
        return setParameters(query, parameterValues).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<T> nativeQuery(String queryString, Map<String, Object> parameterValues, String resultSetMapping) {
        Query query = em.createNativeQuery(queryString, resultSetMapping);
        return setParameters(query, parameterValues).getResultList();
    }

    protected Query setParameters(Query query, List<Object> parameterValues) {
        int idx = 0;
        for (Object o : parameterValues) {
            query = query.setParameter(idx, o);
            idx++;
        }
        return query;
    }

    protected Query setParameters(Query query, Map<String, Object> parameterValues) {
        for (String key : parameterValues.keySet()) {
            query = query.setParameter(key, parameterValues.get(key));
        }
        return query;
    }
}
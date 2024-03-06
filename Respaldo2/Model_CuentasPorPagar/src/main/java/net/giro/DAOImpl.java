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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long save(T entity, Long idEmpresa) throws ExcepConstraint {
		Long secuen = -1l;
		Class cls = null;
		Method met = null;
		String tabla = "";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0L)
				idEmpresa = 1L;
			if (idEmpresa > 10000000)
				idEmpresa -= 10000000;
			tabla = entity.getClass().getCanonicalName();
			secuen = ifzCorrelativo.GenerarCorrelativo(idEmpresa, tabla);

			cls = entity.getClass();
			met = cls.getMethod("setId", long.class);
			met.invoke(entity, secuen);
		} catch (Exception e) {
			log.error("Error", e);
			throw new ExcepConstraint ("ASIGNAR SECUENCIA", entity);			
		}
		
		try {
			em.persist(entity);
			em.flush();
		} catch (Exception e) {
			log.error("Error", e);
			throw new ExcepConstraint ("GRABAR ENTIDAD", entity);			
		}
		
        return secuen;
    }

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(T object) throws ExcepConstraint {
        em.merge(object);
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<T> saveOrUpdateList(List<T> entities, Long idEmpresa) throws Exception {
		Long secuen = -1l;
		String tabla = "";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0L)
				idEmpresa = 1L;
			if (idEmpresa > 10000000)
				idEmpresa -= 10000000;
			T t = entities.get(0);
			@SuppressWarnings("rawtypes")
			Class c = t.getClass();
			Method m = c.getMethod("setId", long.class);
			Method m2 = c.getMethod("getId");
			
			tabla = t.getClass().getCanonicalName();
			
			for (T entity : entities) {
				long id = m2.invoke(entity) == null ? 0 : (long) m2.invoke(entity);
				
				if (id <= 0) {
					try {
						secuen = ifzCorrelativo.GenerarCorrelativo(idEmpresa, tabla);
						m.invoke(entity, secuen);
					} catch (Exception e) {
						log.error("Error", e);
						throw new ExcepConstraint ("ASIGNAR SECUENCIA", entity);			
					}
					
					try {
						em.persist(entity);
						em.flush();
					} catch (Exception e) {
						log.error("Error", e);
						throw new ExcepConstraint ("GRABAR ENTIDAD", entity);			
					}
				} else {
					try {
						em.merge(entity);
					} catch (Exception e) {
						log.error("Error", e);
						throw new ExcepConstraint ("ACTUALIZAR ENTIDAD", entity);	
					}
				}
			}
			
			return entities;
		} catch (Exception e) {
			throw e;			
		}
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
    	return 100;
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
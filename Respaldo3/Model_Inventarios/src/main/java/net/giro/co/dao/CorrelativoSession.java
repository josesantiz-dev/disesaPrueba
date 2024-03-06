package net.giro.co.dao;


import java.util.*;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

import net.giro.co.beans.Correlativo;
import net.giro.co.beans.CorrelativoId;
import net.giro.comun.util.TextUtil;


/**
 * Facade for entity Correlativo.
 * 
 * @see gen.Correlativo
 * @author MyEclipse Persistence Tools
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CorrelativoSession implements CorrelativoInterfaceLocal {
	static private HashMap<String, Long> _hm;

	// property constants
	public static final String NUMEROCORRELATIVO = "numeroCorrelativo";
	public static final String DESCRITABLA = "descriTabla";
	public static final String USUARIOINSERCION = "usuarioInsercion";
	public static final String TERMINALINSERCION = "terminalInsercion";
	public static final String IPINSERCION = "ipInsercion";
	public static final String USUARIOMODIFICACION = "usuarioModificacion";
	public static final String TERMINALMODIFICACION = "terminalModificacion";
	public static final String IPMODIFICACION = "ipModificacion";

	// Constructor de Metodos Estaticos
	{
		
		_hm = new HashMap<String, Long>();
	}
	
	//@EJB
	//CBEmpresaInterfaceLocal cbEmpresaInterfaceLocal = null;
	
	@EJB
	CorrelativoBloqueoInterfaceLocal cbCorrelativoBloqueoInterfaceLocal=null;
	
	@PersistenceContext
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void save(Correlativo transientInstance) {
		
		try {
			entityManager.persist(transientInstance);

		} catch (RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void delete(Correlativo persistentInstance) {
		try {
			entityManager.remove(persistentInstance);
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Correlativo update(Correlativo detachedInstance) {
		try {
			Correlativo result = entityManager.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Correlativo findById(CorrelativoId id) {
		try {
			Correlativo instance = entityManager.find(Correlativo.class, id);
			return instance;
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Correlativo> findByProperty(String propertyName, Object value) {
		try {
			String queryString = "select model from Correlativo model where model."
					+ propertyName + "= :propertyValue";
			return entityManager.createQuery(queryString).setParameter(
					"propertyValue", value).getResultList();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
	}

	public List<Correlativo> findByNumeroCorrelativo(Object numeroCorrelativo) {
		return findByProperty(NUMEROCORRELATIVO, numeroCorrelativo);
	}

	public List<Correlativo> findByDescriTabla(Object descriTabla) {
		return findByProperty(DESCRITABLA, descriTabla);
	}

	public List<Correlativo> findByUsuarioInsercion(Object usuarioInsercion) {
		return findByProperty(USUARIOINSERCION, usuarioInsercion);
	}

	public List<Correlativo> findByTerminalInsercion(Object terminalInsercion) {
		return findByProperty(TERMINALINSERCION, terminalInsercion);
	}

	public List<Correlativo> findByIpInsercion(Object ipInsercion) {
		return findByProperty(IPINSERCION, ipInsercion);
	}

	public List<Correlativo> findByUsuarioModificacion(Object usuarioModificacion) {
		return findByProperty(USUARIOMODIFICACION, usuarioModificacion);
	}

	public List<Correlativo> findByTerminalModificacion(Object terminalModificacion) {
		return findByProperty(TERMINALMODIFICACION, terminalModificacion);
	}

	public List<Correlativo> findByIpModificacion(Object ipModificacion) {
		return findByProperty(IPMODIFICACION, ipModificacion);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private String getSequencia(Long codigoEmpresa, 
							   String nombreTabla)
	{
		boolean needRead   = false;
		
		synchronized(_hm)
		{
			String  hashKey   = "["+codigoEmpresa.toString()+"]["+nombreTabla+"]";
			Long    hashValue = _hm.get(hashKey);
			Long    newHashValue = new Long(1);
			
			if (hashValue == null)
				needRead = true;  // El valor no existe, generar nueva semilla
			else if (((hashValue.longValue() + 1) % 100) == 0)
				needRead = true;  // Se alcanzo el maximo numero de secuencias, generar nueva semilla
			else
			{
				// aun tenemos semillas para generar 
				newHashValue = new Long(hashValue.longValue()+1);
				_hm.put(hashKey, newHashValue);
			}
			
			if (needRead)
			{
				// obtener el registro bloqueando el registro
				Correlativo cbCorrelativo = null;
				int           retries = 0;
				
				while(cbCorrelativo == null)
				{
					try
					{
						cbCorrelativo = entityManager.find(Correlativo.class, new CorrelativoId(codigoEmpresa, nombreTabla)); 
						entityManager.lock(cbCorrelativo, LockModeType.WRITE);
						
						hashValue    = cbCorrelativo.getNumeroCorrelativo();
						newHashValue = new Long(hashValue.longValue()+1);
							
						cbCorrelativo.setNumeroCorrelativo(new Long(hashValue.longValue()+100));
					
						entityManager.persist(cbCorrelativo);
					
						_hm.put(hashKey, newHashValue);
						
						break;
					}
					catch(OptimisticLockException ole)
					{
						cbCorrelativo = null;
						if (retries++ > 20)
							throw ole;
					}
					catch(Exception e)
					{
						cbCorrelativo = new Correlativo();
						cbCorrelativo.setId(new CorrelativoId(codigoEmpresa, nombreTabla));
						cbCorrelativo.setNumeroCorrelativo(new Long(100));
						entityManager.persist(cbCorrelativo);
						
						hashValue = new Long(0);
						newHashValue = new Long(1);
						
						_hm.put(hashKey, newHashValue);
						break;
					}
				}
			}
	 		return newHashValue.toString();
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public synchronized Long GenerarCorrelativo( Long strCodEmpr, String strNombTab){	
		
		int tries = 0;
        final int maxTries = 20;

        Long correlativoSec = new Long(0);
     
		try{
		do{
			try {
				synchronized (cbCorrelativoBloqueoInterfaceLocal) {
					correlativoSec = cbCorrelativoBloqueoInterfaceLocal.GenerarCorrelativo(	strCodEmpr, strNombTab);
				}
					
			} catch (EJBException e) {
					if (e.getCausedByException() instanceof OptimisticLockException) {
	                    tries++;
	                } else {
	                    throw e;
	                }
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			}while (correlativoSec==null && tries < maxTries) ;
				
			 if (correlativoSec==null) {
		            final String msg = "Exceeded maxTries=" + maxTries + " attempts";
		            throw new EJBException(msg);
		        }	
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return correlativoSec;

	}
	
	@SuppressWarnings("unchecked")
	public List<Correlativo> findAll() {
		try {
			String queryString = "select model from Correlativo model";
			return entityManager.createQuery(queryString).getResultList();
		} catch (RuntimeException re) {
			re.printStackTrace();
		
			throw re;
		}
	}
	
	public String GenerarCorrelativoBatch(
						Long strCodEmpr,
						String strNombTab){	
		
		int numeroDigitos = 15;
		String strCorrelativo = getSequencia(strCodEmpr, strNombTab);
		
 		if (!"".equals(strCodEmpr) || strNombTab != null){
            String strCorrelativosdeCeros = (TextUtil.llenaCeros("0", numeroDigitos - 3) + strCorrelativo).substring(strCorrelativo.length(),strCorrelativo.length()+ numeroDigitos- 3);
           
            strCorrelativo = strCodEmpr + strCorrelativosdeCeros;
        }
 		
		return strCorrelativo;
	}

}
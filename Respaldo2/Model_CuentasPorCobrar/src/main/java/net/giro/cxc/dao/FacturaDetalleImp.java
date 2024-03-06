package net.giro.cxc.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.FacturaDetalle;

@Stateless
public class FacturaDetalleImp extends DAOImpl<FacturaDetalle> implements FacturaDetalleDAO{
	@PersistenceContext
	private EntityManager entityManager;
/*
	public long save(FacturaDetalle entity) {
		try {
			entityManager.persist(entity);
			return entity.getId();
		} catch (RuntimeException re) {	
			throw re;
		}
	}*/

	public void delete(FacturaDetalle entity) {
		try {
			entity = entityManager.getReference(FacturaDetalle.class, entity.getId());
			entityManager.remove(entity);
			
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void update(FacturaDetalle entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public FacturaDetalle findById(Long id) {
		try {
			FacturaDetalle instance = entityManager.find(FacturaDetalle.class,id);
			return instance;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<FacturaDetalle> findByProperty(String propertyName,	final Object value) {
		try {
			final String queryString = "select model from FacturaDetalle model where model."+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<FacturaDetalle> findAll() {
		try {
			final String queryString = "select model from FacturaDetalle model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<FacturaDetalle> findByPropertyPojoCompleto(String propertyName,  Object value) {
		
		String where="";
		
		String queryString = "select detalle "+
				 "from FacturaDetalle detalle " + 
				 "where detalle.idFactura = " +value;	//+ propertyName + "= :propertyValue ";
		/*
		queryString = "Select detalle" +
			" from FacturaDetalle detalle " +
				" inner join fetch  "
		;*/
		
		queryString = queryString + where;
		Query query = entityManager.createQuery(queryString);
		//query.setParameter("propertyValue", value);
		return query.getResultList();
		
		/*
		Select detalle.*, concepto.descripcion from factura_detalle detalle inner join concepto_facturacion concepto on detalle.id_concepto = concepto.id  where detalle.id_factura = 10000031
		 * */
		
	}

	/*
	@SuppressWarnings("unchecked")
	public List<FacturaDetalle> findByPropertyPojoCompleto(String propertyName, String tipo, Object value){
		String where="";
		try{
			String queryString = "select gtosImpto "+
								 "from GastosImpuesto gtosImpto "+
								 "inner join fetch gtosImpto.impuestoId impto "+
								 "where gtosImpto."	+ propertyName + "= :propertyValue ";
			if("AC".equals(tipo))
				where= "and impto.tipoCuenta= 'AC' ";
			else if(tipo != null && !"".equals(tipo))
				where= "and (impto.tipoCuenta != 'AC' or impto.tipoCuenta is null)";
			
			queryString = queryString + where;
					
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();		
		}catch(RuntimeException re){
			throw re;
		}
	}*/
}

package net.giro.inventarios.dao;

import net.giro.DAOImpl;
import net.giro.inventarios.beans.MovimientosDetalle;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MovimientosDetalleImp extends DAOImpl<MovimientosDetalle> implements MovimientosDetalleDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<MovimientosDetalle> saveOrUpdateList(List<MovimientosDetalle> entities) throws Exception {
		return super.saveOrUpdateList(entities, 1L);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MovimientosDetalle> findAllActivos() {
		try {
			final String queryString = "select model from MovimientosDetalle model where model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MovimientosDetalle> findDetallesById(long idAlmacenMovimiento){
		try {
			final String queryString = "select model from MovimientosDetalle model where model.idAlmacenMovimiento = "+idAlmacenMovimiento;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MovimientosDetalle> findByProperty(String propertyName, final Object value) {
		try {
			
			String queryString = "select model from MovimientosDetalle model";
			
			if(propertyName=="id"){
				queryString = "select model from MovimientosDetalle model where id = "+ value ;
				
			}else{
				queryString = "select model from MovimientosDetalle model where lower( model." + propertyName + " ) like '%"+ value.toString().toLowerCase() +"%'";
			}
			
			Query query = entityManager.createQuery(queryString);

			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}

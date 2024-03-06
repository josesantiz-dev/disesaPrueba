package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDet;

@Stateless
public class PagosGastosDetImp extends DAOImpl<PagosGastosDet> implements PagosGastosDetDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastosDet> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from PagosGastosDet model where model." + propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastosDet> findByPropertyALL(String propertyName,final Object value) {
		try {
			final String queryString = "select model from PagosGastosDet model inner join fetch model.idPagosGastos pag " + 
				"where model." + propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastosDet> findAll() {
		try {
			final String queryString = "select model from PagosGastosDet model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastosDet> findLikePojoCompleto(Object value,int max){
		try {
			String queryString = "select det from PagosGastosDet det " +
							   "inner join fetch det.idPagosGastos pag "+
							   "where cast(det.idPagosGastos as string) like '%"+ value.toString().toLowerCase() + "%'";
			
			Query query = entityManager.createQuery(queryString);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re ) {		
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastosDet> findPagoProvisionado(String propertyName, Object value, String sucursales) {
		String queryString = "";
		String v_where = "";
		
		try {	
			queryString = "select det from PagosGastosDet det inner join fetch det.idPagosGastos pag ";
			if ("".equals(value.toString())) {
				v_where = "where pag.tipo = 'A' and det.estatus='C' ";
			} else {
				if  ("Proveedor".equals(propertyName)){
					v_where = "where ((lower(pag.beneficiario) like '%"+ value.toString().toLowerCase()+ "%') ";/*or " + 
							" (lower(prov.apellidoPaterno) like '%"+ value.toString().toLowerCase()+ "%') or " +
							" (lower(prov.apellidoMaterno) like '%"+ value.toString().toLowerCase()+ "%'))" ; */
				} else if ("Folio Factura".equals(propertyName)) {
					v_where = "where (lower(det.referencia) like '%"+ value.toString().toLowerCase()+ "%')";
				} else if("Fecha Factura".equals(propertyName)) {
					v_where="where ''''||(extract(year from det.fecha)||'-'||extract(month from det.fecha)||'-'||extract(day from det.fecha))||''''  like '%"+ value.toString().toLowerCase() +"%' )";
				}
			}
			
			queryString = queryString + v_where + " and suc.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ")" ;;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re ) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastosDet> findDetGtos2MovtoCtas(Object value,int max){
		try {	
			String queryString = "select det  " +
									   "from PagosGastosDet det " +
									   "inner join fetch det.idPagosGastos pag "+		
									   "inner join fetch det.idProveedor prov "+
									   "inner join fetch det.idConcepto gto "+
									   "where det.idPagosGastos= :propertyValue " +
									   "order by det.idConcepto";		
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
				
			if(max>0)
				query.setMaxResults(max);				
			return query.getResultList();
		} catch (RuntimeException re ) {
			throw re;
		}
	}

	@Override
	public Double saldoRepCajaChica(Object value){
		try{
			Double res=0D;
			String queryString = "select sum(det.subtotal+det.totalImpuestos)" +
									" from PagosGastosDet det" +
									" inner join det.idPagosGastos pag" +
									" where det.idPagosGastos=:propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			res = (Double)query.getSingleResult();
			return res != null ? res:0;
		}catch(RuntimeException re){
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastosDet> findByPagosGastos(PagosGastos entity, int limite) throws Exception {
		String queryString = "";
		
		try {
			queryString = "select det from PagosGastosDet det inner join fetch det.idPagosGastos pag " 
					+ "where det.idPagosGastos = :propertyValue ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", entity);
			if(limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception e) {		
			throw e;
		}
	}
}

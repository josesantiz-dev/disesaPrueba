package net.giro.ne.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.ne.beans.Sucursal;

@Stateless
public class SucursalImp extends DAOImpl<Sucursal> implements SucursalDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Sucursal entity, long empresa) throws Exception {
		try {
			return super.save(entity, empresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Sucursal> saveOrUpdateList(List<Sucursal> entities, long empresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, empresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Sucursal> findAll(String orderBy, long empresa) {
		String queryString = "select model from Sucursal model where :empresa in (model.idEmpresa.id, model.idEmpresa.codigo) and model.estatus = 0 ";
		
		try {
			if (empresa <= 0L)
				empresa = 1L;
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "order by model.id ";
			queryString += orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("empresa", empresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Sucursal> findLikePropiedad(String propiedad, String valor, long empresa) throws Exception {
		String where = "";
		String queryString = "select model from Sucursal model where :empresa in (model.idEmpresa.id, model.idEmpresa.codigo) and model.estatus = 0 ";
		
		try {
			if (empresa <= 0L)
				empresa = 1L;
			if(valor == null || "".equals(valor)) {
				where = "";
			} else {
				if("id".equals(propiedad)){
				   where = "and cast(model.id as string) like '%" +valor+ "%' ";
			   } else {
				   where = "and lower(model." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
			   }
			}
			
			queryString += where;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("empresa", empresa);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}

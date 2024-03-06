package net.giro.ne.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Sucursal;

@Stateless
public class SucursalImp extends DAOImpl<Sucursal> implements SucursalDAO  {
	@PersistenceContext
	private EntityManager entityManager;
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Sucursal entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Sucursal> saveOrUpdateList(List<Sucursal> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Sucursal> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint{
		String where = "";
		String queryString = "select model from Sucursal model where model.idEmpresa.id = :idEmpresa ";
		try {
			
			if(valor == null || "".equals(valor)){
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
			query.setParameter("idEmpresa", getIdEmpresa());
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

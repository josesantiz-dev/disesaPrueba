package net.giro.credito.admon.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.credito.admon.beans.Oficial;

@Stateless
public class OficialImp extends DAOImpl<Oficial> implements OficialDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Oficial> findByListUsuariosId(List<Long> listUsuariosId){
		String where = "";
		try {
			for(Long id : listUsuariosId){
				if(where.length() == 0L)
					where = "where ofi.usuarioId = " + id;
				else
					where += " or ofi.usuarioId = " + id;
			}
			
			String queryString = "select ofi from Oficial ofi " +
									where;
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

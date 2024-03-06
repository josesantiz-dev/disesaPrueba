package net.giro.credito.admon.dao;

import java.util.ArrayList;
import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import net.giro.credito.admon.beans.Oficial;

@Stateless
public class OficialImp extends DAOImpl<Oficial> implements OficialDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Oficial> findByListUsuariosId(List<Long> listUsuariosId) {
		String queryString = "select ofi from Oficial ofi where ofi.usuarioId in (:lista) ";
		String lista = "";
		
		try {
			listUsuariosId = (listUsuariosId != null && ! listUsuariosId.isEmpty() ? listUsuariosId : new ArrayList<Long>());
			lista = (! listUsuariosId.isEmpty() ?  StringUtils.join(listUsuariosId, ",") : "0");
			queryString = queryString.replace(":lista", lista);
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

package net.giro.plataforma.ubicacion.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.ubicacion.beans.Colonia;
//import net.giro.plataforma.ubicacion.beans.Localidad;

@Stateless
public class ColoniaImp extends DAOImpl<Colonia> implements ColoniaDAO  {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Colonia> findByPropertyLikeValor(String propertyName,final Object value,String propertyName2,final Object value2) throws ExcepConstraint{
		try {
			String quer = "";
			
			if(value2 == null)
				quer = "";
			else
				quer = " and lower(model."+ propertyName2 + ")" + "like '%"+value2.toString().toLowerCase()+"%' ";
			
			//select model from localidad model inner join fetch model.estado where model.estado = 1 and lower(model.nombre) like '%lo%'
			final String queryString = "select model from Colonia model " +
									   "inner join fetch model.localidad Loc " +
									   "inner join fetch Loc.municipio Mun " + 
									   "inner join fetch Mun.estado " +
									   "where model." + propertyName + "= :propertyValue " + quer + "order by model.nombre asc";	
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Colonia> findLikeColumnName(String columnName, String valorBusqueda){
		String sqlWhere = "";
    	StringBuffer sb = null;
    	
    	if(valorBusqueda !=null && !"".equals(valorBusqueda)){
    		sqlWhere += " WHERE lower(o." + columnName + ") LIKE :valorBusqueda";
    		sb = new StringBuffer();
    		sb.append("%");
    		sb.append(valorBusqueda.toLowerCase());
    		sb.append("%");
    	}
    	
        String strQuery = getQuery() + sqlWhere;
        TypedQuery<Colonia> query = entityManager.createQuery(strQuery, persistenceClass);
        
        if(valorBusqueda != null && !"".equals(valorBusqueda))
        	query.setParameter("valorBusqueda", sb.toString());
        
        query.setMaxResults(getMaxResults());
        return query.getResultList();
	}
}

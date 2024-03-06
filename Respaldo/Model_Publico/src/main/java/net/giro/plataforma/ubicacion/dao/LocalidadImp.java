package net.giro.plataforma.ubicacion.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

//import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.ubicacion.beans.Localidad;
//import net.giro.plataforma.ubicacion.beans.Municipio;

@Stateless
public class LocalidadImp extends DAOImpl<Localidad> implements LocalidadDAO  {
	@PersistenceContext
	private EntityManager entityManager;	
	
	@SuppressWarnings("unchecked")
	public List<Localidad> findByPropertyLikeValor(String propertyName,final Object value,String propertyName2,final Object value2) {
		try {
			String quer = "";
			
			if(value2 == null)
				quer = "";
			else
				quer = " and lower(model."+ propertyName2 + ")" + "like '%"+value2.toString().toLowerCase()+"%' ";
			
			//select model from localidad model inner join fetch model.estado where model.estado = 1 and lower(model.nombre) like '%lo%'
			final String queryString = "select model from Localidad model " +
					"inner join fetch model.municipio mun " +
					"inner join fetch mun.estado " +
					"where model." + propertyName + "= :propertyValue " + quer + "order by model.nombre asc";	
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Localidad> findLikeColumnName(String columnName, String valorBusqueda){
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
        TypedQuery<Localidad> query = em.createQuery(strQuery, persistenceClass);
        
        if(valorBusqueda != null && !"".equals(valorBusqueda))
        	query.setParameter("valorBusqueda", sb.toString());
        
        query.setMaxResults(getMaxResults());
        return query.getResultList();
	}
}

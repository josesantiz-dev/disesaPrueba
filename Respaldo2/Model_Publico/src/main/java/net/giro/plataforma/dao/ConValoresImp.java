package net.giro.plataforma.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;

@Stateless
public class ConValoresImp extends DAOImpl<ConValores> implements ConValoresDAO  {
	@PersistenceContext
	private EntityManager entityManager;


	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findAll(ConGrupoValores grupo, String orderBy, int limite) {
		String queryString = "select model from ConValores model ";
		
		try {
			queryString += "where model.grupoValorId.id = " + grupo.getId();
			
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.descripcion";
			queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findByGrupoNombreByParams(String grupoNombre, HashMap<String, String> params) {
		String where = "";
		
		try {
			if(params != null) {
				for(Entry<String, String> entry : params.entrySet()) {
					where += " and model." + entry.getKey() + " = '" + entry.getValue() + "'";
				}
			}
			
			final String queryString = "select model from ConValores model " +
										" inner join fetch model.grupoValorId grupo " +
										" where grupo.nombre='"+ grupoNombre + "'" +
										where + " order by model.descripcion, model.valor";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findByGrupoNombreLikeParams(String grupoNombre, HashMap<String, String> params){
		String where = "";
		
		try {
			if(params != null) {
				for(Entry<String, String> entry : params.entrySet()) {
					if (! where.isEmpty())
						where += " or ";
					where += " lower(model." + entry.getKey() + ") like lower('%" + entry.getValue() + "%')";
				}
				
				if (! where.isEmpty())
					where = " and (" + where + ")";
			}
			
			final String queryString = "select model from ConValores model " +
										" inner join fetch model.grupoValorId grupo " +
										" where grupo.nombre='"+ grupoNombre + "'" +
										where + " order by model.descripcion, model.valor";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public ConValores findByPropertyPojoSolito(String propertyName,final Object value) {
		try {
			final String queryString = "select model from ConValores model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return (ConValores) query.getSingleResult();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public ConValores findByValorGrupo(String valor, ConGrupoValores grupo) {
		String queryString = "select model from ConValores model ";
		
		try {
			queryString += "where trim(lower(model.valor)) = trim('" + valor + "') and model.grupoValorId = :paramGrupo order by model.descripcion";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("paramGrupo", grupo);
			return (ConValores) query.getSingleResult();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> buscaValorGrupo(String campo, String valor, ConGrupoValores grupo) {
		try {
			String queryString = "select model from ConValores model ";
			
			if ("valorId".equals(campo)) {
				queryString = queryString + "where lower(cast(model.id, string)) like '%"+(valor != null ? valor.toLowerCase() : "") + "%' ";
			} else {
				queryString = queryString + "where lower(model."+ campo +") like '%"+(valor != null ? valor.toLowerCase() : "") + "%' ";
			}
			
			queryString = queryString + " and model.grupoValorId = :paramGrupo order by model.descripcion";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("paramGrupo", grupo);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findLikeValorIdPropiedadGrupo(String dato, ConGrupoValores grupo,int limit){
		try {
			if (dato == null)
				dato="";
			
			final String queryString = "select conVal from ConValores conVal " +																			
						"where (lower(conVal.descripcion) like '%" + dato.toLowerCase() + "%' or cast(conVal.id, string) like '%" + dato.toLowerCase() + "%' ) " +
							"and conVal.grupoValorId.id = "+ grupo.getId() + " " +
						"order by conVal.descripcion ";
			Query query = entityManager.createQuery(queryString);
			query.setMaxResults(limit);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findLikeClaveValorGrupo(String dato, ConGrupoValores grupo,int limit){
		try {
			if (dato == null)
				dato="";
			
			final String queryString = "select conVal from ConValores conVal " +
										"inner join fetch conVal.grupoValorId " +
										"where (lower(conVal.valor) like '%" + dato.toLowerCase() + "%' or cast(conVal.id, string) like '%" + dato.toLowerCase() + "%' or lower(conVal.descripcion) like '%" + dato.toLowerCase() + "%' ) " +
										(grupo != null ? "and conVal.grupoValorId = :grupo " : "") +
										"order by conVal.descripcion ";
			Query query = entityManager.createQuery(queryString);
			if(grupo != null)
				query.setParameter("grupo", grupo);
			if(limit > 0 )
				query.setMaxResults(limit);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findLikeByProperty(String dato, String valor,int max) {
		String v_where = "";
		try {
			String queryString = "select ret "+
								"from ConValores ret " +
								"where ret.grupoValorId = 1 and ret.tipoCuenta= 'AC' ";
			
			if("Clave".equals(dato) && !"".equals(valor)){
				v_where= "and lower(cast(ret.id as string)) like '%" + valor.toLowerCase() + "%'";
			}
			else if("Descripcion".equals(dato) && !"".equals(valor)){
				v_where= "and lower(ret.descripcion) like '%" + valor.toLowerCase() + "%'";
			}
			else if("Cuenta Contable".equals(dato) && !"".equals(valor)){
				v_where= "and lower(ret.valor) like '%" + valor.toLowerCase() + "%'";
			}
			
			queryString = queryString + v_where + " ORDER BY ret.descripcion, ret.valor;";
			Query query = entityManager.createQuery(queryString);
			if(max >0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findByGrupoNombre(String grupoNombre) {
		try {
			final String queryString = "select model from ConValores model inner join fetch model.grupoValorId grupo " +
			                           "where grupo.nombre='"+ grupoNombre + "'" + " order by model.descripcion, model.valor ";			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findByProperties(String propertyName1,final Object value1,String propertyName2,final Object value2) {
		try {
			final String queryString = "select model from ConValores model where model."
					+ propertyName1 + " = '"+value1.toString()+"' and " + propertyName2 + "= :propertyValue2" 
					+ " order by model.descripcion, model.valor ";
			
			Query query = entityManager.createQuery(queryString); 
			query.setParameter("propertyValue2", value2);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findLikeByProperties(final Object valueDescripcion,final Object valueCuenta,final Object valueGrupo,int max){
		String v_where = "";
		try {
			String queryString = "select val from ConValores val " ;

			if(!valueDescripcion.toString().equals(""))
				v_where = " where lower(val.descripcion) like '%" + valueDescripcion.toString().toLowerCase() + "%'";

			if(!valueCuenta.toString().equals("")) {
				if (v_where.length() > 0)
					v_where = v_where + " and lower(val.valor) like '%" + valueCuenta.toString().toLowerCase() + "'";
				else
					v_where = v_where + " where lower(val.valor) like '%" + valueCuenta.toString().toLowerCase() + "'";
			}
			
			if (v_where.length() > 0)
				v_where = v_where + " and grupoValorId = :propertyValueGrupo"; 
			else
				v_where = v_where + " where grupoValorId = :propertyValueGrupo"; 
								
			queryString = queryString + v_where + " order by model.descripcion, model.valor ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValueGrupo", valueGrupo);
			if(max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findByGrupoEntreValores(String propertyName1,final Object valueInicial,String propertyName2,final Object valueFinal,final Object grupo) {
		try {
			final String queryString = "select model from ConValores model where model.grupoValorId = :grupoDeValor and model."
					+ propertyName1 + " >= '"+valueInicial.toString()+"' and model." + propertyName2 + " <= '"+valueFinal.toString()+"'";
			
			Query query = entityManager.createQuery(queryString); 
			query.setParameter("grupoDeValor", grupo);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from ConValores model ";
		String whereString = "";
		
		try {
			
			for(Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " and";
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
						
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			queryString += " order by model.valor";

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		String queryString = "select model from ConValores model ";
		String whereString = "";
		String key = "";
		
		try {
			for(Entry<String, String> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " and";
				
				key = e.getKey();
				if (key.contains("-"))
					key = key.substring(0, key.indexOf("-"));
				whereString += " cast(model." + key + " as string) like '%" + e.getValue() + "%'";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			queryString += " order by model.valor ";

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findByProperty(String propertyName, Object value, ConGrupoValores grupo, int limit) throws Exception {
		String queryString = "select model from ConValores model ";
		
		try {
			if ("valorId".equals(propertyName))
				queryString += "where lower(cast(model.id, string)) = '" + (value != null ? value.toString().toLowerCase() : "") + "' ";
				else
				queryString += "where lower(model."+ propertyName +") = '" + (value != null ? value.toString().toLowerCase() : "") + "' ";
			queryString += " and model.grupoValorId = :paramGrupo order by model.descripcion";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("paramGrupo", grupo);
			if (limit > 0)
				query.setMaxResults(limit);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConValores> findLikeProperty(String propertyName, String value, ConGrupoValores grupo, int limit) throws Exception {
		String queryString = "select model from ConValores model ";
		
		try {
			if ("valorId".equals(propertyName))
				queryString += "where lower(cast(model.id, string)) like '%" + (value != null ? value.toLowerCase() : "") + "%' ";
				else
				queryString += "where lower(model."+ propertyName +") like '%" + (value != null ? value.toLowerCase() : "") + "%' ";
			queryString += " and model.grupoValorId = :paramGrupo order by model.descripcion";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("paramGrupo", grupo);
			if (limit > 0)
				query.setMaxResults(limit);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

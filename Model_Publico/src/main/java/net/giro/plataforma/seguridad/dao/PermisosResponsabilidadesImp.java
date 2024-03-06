package net.giro.plataforma.seguridad.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.seguridad.beans.PermisosResponsabilidad;

@Stateless
public class PermisosResponsabilidadesImp extends DAOImpl<PermisosResponsabilidad> implements PermisosResponsabilidadesDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(PermisosResponsabilidad entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PermisosResponsabilidad> saveOrUpdateList(List<PermisosResponsabilidad> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PermisosResponsabilidad> findAll(long idResponsabilidad, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from PermisosResponsabilidad model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idResponsabilidad = (idResponsabilidad > 0 ? idResponsabilidad : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idAplicacion.aplicacion, model.idFuncion.nombre");
			if (idResponsabilidad > 0L)
				queryString += "and model.idResponsabilidad.id = :idResponsabilidad ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idResponsabilidad > 0L)
				query.setParameter("idResponsabilidad", idResponsabilidad);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-04-27 | Javier Tirado 	| Creacion de EJB
 */
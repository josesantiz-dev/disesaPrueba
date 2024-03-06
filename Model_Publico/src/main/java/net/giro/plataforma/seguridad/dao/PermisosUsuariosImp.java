package net.giro.plataforma.seguridad.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.seguridad.beans.PermisosUsuario;

@Stateless
public class PermisosUsuariosImp extends DAOImpl<PermisosUsuario> implements PermisosUsuariosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(PermisosUsuario entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PermisosUsuario> saveOrUpdateList(List<PermisosUsuario> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PermisosUsuario> findAll(long idUsuario, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from PermisosUsuario model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idUsuario = (idUsuario > 0 ? idUsuario : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idAplicacion.aplicacion, model.idFuncion.nombre");
			if (idUsuario > 0L)
				queryString += "and model.idUsuario.id = :idUsuario ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idUsuario > 0L)
				query.setParameter("idUsuario", idUsuario);
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
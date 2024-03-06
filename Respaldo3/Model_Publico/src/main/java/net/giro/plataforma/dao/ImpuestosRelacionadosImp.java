package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.beans.ImpuestosRelacionados;

@Stateless
public class ImpuestosRelacionadosImp extends DAOImpl<ImpuestosRelacionados> implements ImpuestosRelacionadosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ImpuestosRelacionados entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ImpuestosRelacionados> saveOrUpdateList(List<ImpuestosRelacionados> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ImpuestosRelacionados> findByImpuesto(Long idImpuesto) throws Exception {
		String queryString = "select model from ImpuestosRelacionados model where model.idImpuestoBase = :idImpuesto order by model.idImpuestoBase, model.idImpuestoRelacionado ";
		
		try {
			idImpuesto = (idImpuesto != null) ? idImpuesto : 0L;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idImpuesto", idImpuesto);
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
 *  1.2 | 2020-04-15 | Javier Tirado 	| Creacion de EJB.
 */
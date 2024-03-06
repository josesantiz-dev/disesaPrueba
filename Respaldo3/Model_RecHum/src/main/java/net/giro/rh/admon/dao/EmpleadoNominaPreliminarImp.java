package net.giro.rh.admon.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.rh.admon.beans.EmpleadoNominaPreliminar;

@Stateless
public class EmpleadoNominaPreliminarImp extends DAOImpl<EmpleadoNominaPreliminar> implements EmpleadoNominaPreliminarDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(EmpleadoNominaPreliminar entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<EmpleadoNominaPreliminar> saveOrUpdateList(List<EmpleadoNominaPreliminar> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoNominaPreliminar> deleteAll(List<EmpleadoNominaPreliminar> entities) throws Exception {
		List<EmpleadoNominaPreliminar> deleted = null;
		
		try {
			deleted = super.deleteAll(entities);
			if (deleted != null && ! deleted.isEmpty()) {
				for (EmpleadoNominaPreliminar delete : deleted)
					entities.remove(delete);
			}
			return entities;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoNominaPreliminar> findByDates(Date fechaDesde, Date fechaHasta, long idEmpresa) throws Exception {
		String queryString = "select model from EmpleadoNominaPreliminar model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (fechaDesde == null || fechaHasta == null)
				return new ArrayList<EmpleadoNominaPreliminar>();
			if (fechaDesde.after(fechaHasta))
				return new ArrayList<EmpleadoNominaPreliminar>();
			queryString += "and DATE(model.fecha) between DATE(:fechaDesde) and DATE(:fechaHasta)";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("fechaDesde", formatter.format(fechaDesde));
			query.setParameter("fechaHasta", formatter.format(fechaHasta));
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaPreliminarImp
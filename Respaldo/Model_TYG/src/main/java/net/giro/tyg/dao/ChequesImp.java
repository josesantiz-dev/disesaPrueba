package net.giro.tyg.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.giro.DAOImpl;
import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.Cheques;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class ChequesImp extends DAOImpl<Cheques> implements ChequesDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	public long save(Cheques entity) {
		try {
			entityManager.persist(entity);
			return entity.getId();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(Cheques entity) {
		try {
			entity = entityManager.getReference(Cheques.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(Cheques entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Cheques findById(Integer id) {
		try {
			Cheques instance = entityManager.find(Cheques.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Cheques findChequeCompleto(String noCheque, Short ctaBancaria, String tipo, String estatus) {
		try {
			String queryString = "select cheq " + "from Cheques cheq "
					+ "inner join fetch cheq.bancoId idCta "
					+ "where cheq.folio='" + noCheque + "' and "
					+ "idCta.ctaBancoId=" + ctaBancaria + " and "
					+ "cheq.tipo='" + tipo + "' and " + "cheq.estatus='"
					+ estatus + "'";

			Query query = entityManager.createQuery(queryString);

			return (Cheques) query.getSingleResult();

		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cheques> findByFolioCtaCheque(String folio, String ctaCheques) throws ExcepConstraint, RuntimeException {
		try {
			String queryString = "select cheq "
					+ "from Cheques cheq "
					+ "inner join fetch cheq.bancoId idCta "
					+ "where cheq.folio=:folio and idCta.ctaCheques=:ctaCheques";

			Query query = entityManager.createQuery(queryString);
			query.setParameter("folio", folio);
			query.setParameter("ctaCheques", ctaCheques);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cheques> findByProperty(String propertyName, final Object value) {
		List<Cheques> res = null;
		try {
			final String queryString = "select model from Cheques model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			res = query.getResultList();
			return res.isEmpty() ? null : res;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cheques> findChequeConContrato(String control,
			String folioCheque, String banco, Date fecha, String estatus,
			Double importe) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String queryString = "select cheq from Cheques cheq "
					+ "where cheq.control = '" + control + "' and "
					+ "cheq.folio = " + folioCheque + " and "
					+ "cheq.bancoId = " + banco + " and "
					+ "cheq.tipoTrans = 'E' and " + "cheq.estatus = '"
					+ estatus + "' and "
					+ "cast(cheq.fechaCreacion as date) = '"
					+ sdf.format(fecha) + "' and " + "cheq.importe = "
					+ importe;
			Query query = entityManager.createQuery(queryString);
			query.getResultList();

			return query.getResultList();

		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cheques> findAll() {
		try {
			final String queryString = "select model from Cheques model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

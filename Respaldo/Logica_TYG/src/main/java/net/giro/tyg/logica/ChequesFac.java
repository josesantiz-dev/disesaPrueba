package net.giro.tyg.logica;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.Cheques;
import net.giro.tyg.dao.ChequesDAO;

@Stateless
public class ChequesFac implements ChequesRem {
	private InitialContext ctx = null;
	private ChequesDAO ifzCheques;
	
	public ChequesFac() throws Exception { 
    	Hashtable<String, Object> p = new Hashtable<String, Object>();
        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");            
        ctx = new InitialContext(p);
        
		this.ifzCheques = (ChequesDAO) ctx.lookup("ejb:/Model_TYG//ChequesImp!net.giro.clientes.dao.ChequesDAO");
	}

	public long save(Cheques entity) {
		try {
			return this.ifzCheques.save(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(Cheques entity) {
		try {
			this.ifzCheques.delete(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Cheques update(Cheques entity) {
		try {
			this.ifzCheques.update(entity);
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Cheques findById(Integer id) {
		try {
			return this.ifzCheques.findById(id);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Cheques findChequeCompleto(String noCheque, Short ctaBancaria, String tipo, String estatus) {
		try {
			return this.ifzCheques.findChequeCompleto(noCheque, ctaBancaria, tipo, estatus);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Cheques findByFolioCtaCheque(String folio, String ctaCheques) throws ExcepConstraint, RuntimeException {
		List<Cheques> listRes = null;
		
		try {
			listRes = this.ifzCheques.findByFolioCtaCheque(folio, ctaCheques);
			if (!listRes.isEmpty() && listRes.size() > 1)
				throw new ExcepConstraint("Se encontro 2 o mas cheques con el folio " + 
						folio + " y ctaCheques " + ctaCheques, "");
			return !listRes.isEmpty() ? listRes.get(0) : null;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Cheques> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzCheques.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Cheques> findChequeConContrato(String control, String folioCheque, String banco, Date fecha, String estatus, Double importe) {
		try {
			return this.ifzCheques.findChequeConContrato(control, folioCheque, banco, fecha, estatus, importe);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Cheques> findAll() {
		try {
			return this.ifzCheques.findAll();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

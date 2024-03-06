package net.giro.tyg.logica;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.plataforma.InfoSesion;
import net.giro.tyg.admon.Cheques;
import net.giro.tyg.dao.ChequesDAO;

@Stateless
public class ChequesFac implements ChequesRem {
	private InitialContext ctx = null;
	private ChequesDAO ifzCheques;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
	public ChequesFac() throws Exception { 
    	Hashtable<String, Object> p = new Hashtable<String, Object>();
        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");            
        ctx = new InitialContext(p);
		this.ifzCheques = (ChequesDAO) this.ctx.lookup("ejb:/Model_TYG//ChequesImp!net.giro.tyg.dao.ChequesDAO");
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	public long save(Cheques entity) throws Exception {
		try {
			return this.ifzCheques.save(entity, 1L);
		} catch (Exception re) {
			throw re;
		}
	}

	public void delete(Cheques entity) {
		try {
			this.ifzCheques.delete(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	public Cheques update(Cheques entity) {
		try {
			this.ifzCheques.update(entity);
			return entity;
		} catch (Exception re) {
			throw re;
		}
	}

	public Cheques findById(Integer id) {
		try {
			return this.ifzCheques.findById(id);
		} catch (Exception re) {
			throw re;
		}
	}

	public Cheques findChequeCompleto(String noCheque, Short ctaBancaria, String tipo, String estatus) {
		try {
			return this.ifzCheques.findChequeCompleto(noCheque, ctaBancaria, tipo, estatus);
		} catch (Exception re) {
			throw re;
		}
	}

	public Cheques findByFolioCtaCheque(String folio, String ctaCheques) throws Exception {
		List<Cheques> listRes = null;
		
		try {
			listRes = this.ifzCheques.findByFolioCtaCheque(folio, ctaCheques);
			if (! listRes.isEmpty() && listRes.size() > 1)
				throw new Exception("Se encontro 2 o mas cheques con el folio " + folio + " y ctaCheques " + ctaCheques);
			return !listRes.isEmpty() ? listRes.get(0) : null;
		} catch (Exception re) {
			throw re;
		}
	}

	public List<Cheques> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzCheques.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	public List<Cheques> findChequeConContrato(String control, String folioCheque, String banco, Date fecha, String estatus, Double importe) {
		try {
			return this.ifzCheques.findChequeConContrato(control, folioCheque, banco, fecha, estatus, importe);
		} catch (Exception re) {
			throw re;
		}
	}

	public List<Cheques> findAll() {
		try {
			return this.ifzCheques.findAll();
		} catch (Exception re) {
			throw re;
		}
	}
}

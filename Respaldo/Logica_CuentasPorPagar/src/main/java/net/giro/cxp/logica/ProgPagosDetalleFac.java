package net.giro.cxp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxp.beans.ProgPagosDetalle;
import net.giro.cxp.beans.ProgPagosDetalleExt;
import net.giro.cxp.dao.ProgPagosDetalleDAO;

@Stateless
public class ProgPagosDetalleFac implements ProgPagosDetalleRem {
	private static Logger log = Logger.getLogger(ProgPagosDetalleFac.class);
	
	InitialContext ctx;
	private ProgPagosDetalleDAO ifzProgPagosDetalle;
	
	private ConvertExt convertidor;
	
	// property constants
	public static final String MONTO = "monto";
	public static final String MONTO_REV = "montoRev";

	public ProgPagosDetalleFac(){
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    		this.ifzProgPagosDetalle = (ProgPagosDetalleDAO) ctx.lookup("ejb:/Model_CuentasPorPagar//ProgPagosDetalleImp!net.giro.cxp.dao.ProgPagosDetalleDAO");
    		
    		this.convertidor = new ConvertExt();
    		this.convertidor.setFrom("ProgPagosDetalleFac");
    		this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear ProgPagosDetalleFac", e);
			ctx = null;
		}
	}
	
	public Long save(ProgPagosDetalleExt entityExt) throws Exception {
		Long id;
		
		try {
			ProgPagosDetalle entity = this.convertidor.ProgPagosDetalleExtToProgPagosDetalle(entityExt);
			id = this.ifzProgPagosDetalle.save(entity);
		} catch (RuntimeException re) {
			throw re;
		}
		
		return id;
	}
	
	public void delete(ProgPagosDetalleExt entityExt) throws Exception {
		try {
			ProgPagosDetalle entity = this.convertidor.ProgPagosDetalleExtToProgPagosDetalle(entityExt);
			this.ifzProgPagosDetalle.delete(entity.getId());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void update(ProgPagosDetalle entity) throws Exception {
		try {
			this.ifzProgPagosDetalle.update(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void update(ProgPagosDetalleExt entity) throws Exception {
		try {
			ProgPagosDetalle pojoProgPagosDetalle = this.convertidor.ProgPagosDetalleExtToProgPagosDetalle(entity);
			this.ifzProgPagosDetalle.update(pojoProgPagosDetalle);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ProgPagosDetalleExt findById(Long id) throws Exception {
		try {
			return this.convertidor.ProgPagosDetalleToProgPagosDetalleExt(this.ifzProgPagosDetalle.findById(id));
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<ProgPagosDetalleExt> findByProperty(String propertyName,final Object value) throws Exception {
		List<ProgPagosDetalleExt> listProgPagosDetalleExt = new ArrayList<ProgPagosDetalleExt>();
		
		try {
			List<ProgPagosDetalle> listProgPagosDetalle = this.ifzProgPagosDetalle.findByProperty(propertyName, value);
			if (listProgPagosDetalle.size() > 0) {
				for (ProgPagosDetalle var : listProgPagosDetalle) {
					ProgPagosDetalleExt pojoExt = this.convertidor.ProgPagosDetalleToProgPagosDetalleExt(var);
					listProgPagosDetalleExt.add(pojoExt);
				}
			} 
		} catch (RuntimeException re) {
			throw re;
		}
		
		return listProgPagosDetalleExt;
	}

	public List<ProgPagosDetalleExt> findByPropertyPojoCompleto(String propertyName,final Object value) throws Exception {
		List<ProgPagosDetalleExt> listProgPagosDetalleExt = new ArrayList<ProgPagosDetalleExt>();
		
		try {
			List<ProgPagosDetalle> listProgPagosDetalle = this.ifzProgPagosDetalle.findByPropertyPojoCompleto(propertyName, value);
			if (listProgPagosDetalle.size() > 0) {
				for (ProgPagosDetalle var : listProgPagosDetalle) {
					ProgPagosDetalleExt pojoExt = this.convertidor.ProgPagosDetalleToProgPagosDetalleExt(var);
					listProgPagosDetalleExt.add(pojoExt);
				}
			} 
		} catch (RuntimeException re) {
			throw re;
		}
		
		return listProgPagosDetalleExt;
	}
	
	public List<ProgPagosDetalleExt> findByPropertyPojoCompletoMontoNoCero(String propertyName, final Object value) throws Exception {
		List<ProgPagosDetalleExt> listProgPagosDetalleExt = new ArrayList<ProgPagosDetalleExt>();
		
		try {
			List<ProgPagosDetalle> listProgPagosDetalle = this.ifzProgPagosDetalle.findByPropertyPojoCompletoMontoNoCero(propertyName, value);
			if (listProgPagosDetalle.size() > 0) {
				for (ProgPagosDetalle var : listProgPagosDetalle) {
					ProgPagosDetalleExt pojoExt = this.convertidor.ProgPagosDetalleToProgPagosDetalleExt(var);
					listProgPagosDetalleExt.add(pojoExt);
				}
			} 
		} catch (RuntimeException re) {
			throw re;
		}
		
		return listProgPagosDetalleExt;
	}
	
	public List<ProgPagosDetalleExt> findByAgenteEstatusMontoConcepto(String agenteValue, String estatusValue, Long conceptoValue) throws Exception {
		List<ProgPagosDetalleExt> listProgPagosDetalleExt = new ArrayList<ProgPagosDetalleExt>();
		
		try {
			List<ProgPagosDetalle> listProgPagosDetalle = this.ifzProgPagosDetalle.findByAgenteEstatusMontoConcepto(agenteValue, estatusValue, conceptoValue);
			if (listProgPagosDetalle.size() > 0) {
				for (ProgPagosDetalle var : listProgPagosDetalle) {
					ProgPagosDetalleExt pojoExt = this.convertidor.ProgPagosDetalleToProgPagosDetalleExt(var);
					listProgPagosDetalleExt.add(pojoExt);
				}
			} 
		} catch (RuntimeException re) {
			throw re;
		}
		
		return listProgPagosDetalleExt;
	}
	
	public List<ProgPagosDetalleExt> findAll() throws Exception {
		List<ProgPagosDetalleExt> listProgPagosDetalleExt = new ArrayList<ProgPagosDetalleExt>();
		
		try {
			List<ProgPagosDetalle> listProgPagosDetalle = this.ifzProgPagosDetalle.findAll();
			if (listProgPagosDetalle.size() > 0) {
				for (ProgPagosDetalle var : listProgPagosDetalle) {
					ProgPagosDetalleExt pojoExt = this.convertidor.ProgPagosDetalleToProgPagosDetalleExt(var);
					listProgPagosDetalleExt.add(pojoExt);
				}
			} 
		} catch (RuntimeException re) {
			throw re;
		}
		
		return listProgPagosDetalleExt;
	}
	
	public List<ProgPagosDetalle> findByMontoConceptoEstatus(Double montoValue, Long conceptoValue, String estatusValue) throws Exception {
		try {
			return this.ifzProgPagosDetalle.findByMontoConceptoEstatus(montoValue, conceptoValue, estatusValue);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<ProgPagosDetalleExt> findByMontoConceptoEstatusExt(Double montoValue, Long conceptoValue, String estatusValue) throws Exception {
		List<ProgPagosDetalleExt> listProgPagosDetalleExt = new ArrayList<ProgPagosDetalleExt>();
		
		try {
			List<ProgPagosDetalle> listProgPagosDetalle = this.ifzProgPagosDetalle.findByMontoConceptoEstatus(montoValue, conceptoValue, estatusValue);
			if (listProgPagosDetalle.size() > 0) {
				for (ProgPagosDetalle var : listProgPagosDetalle) {
					ProgPagosDetalleExt pojoExt = this.convertidor.ProgPagosDetalleToProgPagosDetalleExt(var);
					listProgPagosDetalleExt.add(pojoExt);
				}
			} 
		} catch (RuntimeException re) {
			throw re;
		}
		
		return listProgPagosDetalleExt;
	}
}

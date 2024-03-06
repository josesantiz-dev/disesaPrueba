package net.giro.cxp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestos;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.cxp.dao.PagosGastosDetImpuestosDAO;

@Stateless
public class PagosGastosDetImpuestosFac implements PagosGastosDetImpuestosRem {
	private static Logger log = Logger.getLogger(PagosGastosDetImpuestosFac.class);
	
	InitialContext ctx;
	private PagosGastosDetImpuestosDAO ifzPagosGastosDetImpuestos;
	ConvertExt convertidor;
	
	// property constants
	public static final String VALOR = "valor";

	public PagosGastosDetImpuestosFac(){
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    		this.ifzPagosGastosDetImpuestos = (PagosGastosDetImpuestosDAO) ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosDetImpuestosImp!net.giro.cxp.dao.PagosGastosDetImpuestosDAO");
    		
    		this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear PagosGastosDetImpuestosFac", e);
			ctx = null;
		}
	}

	public Long save(PagosGastosDetImpuestos entity) throws ExcepConstraint {
		try {
			return this.ifzPagosGastosDetImpuestos.save(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Long save(PagosGastosDetImpuestosExt entityExt) throws Exception {
		long id = 0L;
		
		try {
			PagosGastosDetImpuestos entity = this.convertidor.PagosGastosDetImpuestosExtToPagosGastosDetImpuestos(entityExt);
			id = this.ifzPagosGastosDetImpuestos.save(entity);
		} catch (RuntimeException re) {
			throw re;
		}
		
		return id;
	}
	
	public PagosGastosDetImpuestos update(PagosGastosDetImpuestos entity) throws ExcepConstraint {
		try {
			this.ifzPagosGastosDetImpuestos.update(entity);
			
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public PagosGastosDetImpuestos update(PagosGastosDetImpuestosExt entityExt) throws Exception {
		try {
			PagosGastosDetImpuestos entity = this.convertidor.PagosGastosDetImpuestosExtToPagosGastosDetImpuestos(entityExt);
			this.ifzPagosGastosDetImpuestos.update(entity);
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void delete(PagosGastosDetImpuestos entity) throws ExcepConstraint {
		try {
			this.ifzPagosGastosDetImpuestos.delete(entity.getId());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void delete(PagosGastosDetImpuestosExt entityExt) throws Exception {
		try {
			PagosGastosDetImpuestos entity = this.convertidor.PagosGastosDetImpuestosExtToPagosGastosDetImpuestos(entityExt);
			this.ifzPagosGastosDetImpuestos.delete(entity.getId());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public PagosGastosDetImpuestos findById(Long id) {
		try {
			return this.ifzPagosGastosDetImpuestos.findById(id);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<PagosGastosDetImpuestos> findByProperty(String propertyName,final Object value) {
		try {
			return this.ifzPagosGastosDetImpuestos.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<PagosGastosDetImpuestos> findAll() {
		try {
			return this.ifzPagosGastosDetImpuestos.findAll();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<PagosGastosDetImpuestos> findLikePojoCompleto(Object value, int max){
		try {
			return this.ifzPagosGastosDetImpuestos.findLikePojoCompleto(value, max);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<PagosGastosDetImpuestosExt> findLikePojoCompletoExt(PagosGastosDetExt entityExt, int max) throws Exception{
		List<PagosGastosDetImpuestosExt> listaExt = new ArrayList<PagosGastosDetImpuestosExt>();
		
		try {
			PagosGastosDet entity = this.convertidor.PagosGastosDetExtToPagosGastosDet(entityExt);
			List<PagosGastosDetImpuestos> lista = this.ifzPagosGastosDetImpuestos.findLikePojoCompleto(entity, max);
			
			for (PagosGastosDetImpuestos pagosGastosDetImpuestos : lista) {
				PagosGastosDetImpuestosExt pojoAux = this.convertidor.PagosGastosDetImpuestosToPagosGastosDetImpuestosExt(pagosGastosDetImpuestos);
				listaExt.add(pojoAux);
			}
		} catch (RuntimeException re) {
			throw re;
		}
		
		return listaExt;
	}

	public List<PagosGastosDetImpuestos> findImptos2DetGtos (Object value,int max){
		try {
			return this.ifzPagosGastosDetImpuestos.findImptos2DetGtos(value, max);
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

package net.giro.cxp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.cxp.beans.GastosImpuesto;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.dao.GastosImpuestoDAO;

@Stateless
public class GastosImpuestoFac implements GastosImpuestoRem {
	private static Logger log = Logger.getLogger(GastosImpuestoFac.class);
	
	InitialContext ctx;
	private GastosImpuestoDAO ifzGastosImpuesto;
	private ConvertExt convertidor;

	public GastosImpuestoFac(){
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    		this.ifzGastosImpuesto = (GastosImpuestoDAO) ctx.lookup("ejb:/Model_CuentasPorPagar//GastosImpuestoImp!net.giro.cxp.dao.GastosImpuestoDAO");
    		
    		this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear GastosImpuestoFac", e);
			ctx = null;
		}
	}

	public Long save(GastosImpuesto entity) throws ExcepConstraint {
		try {
			return this.ifzGastosImpuesto.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(GastosImpuestoExt entityExt) throws ExcepConstraint {
		try {
			GastosImpuesto entity = this.convertidor.GastosImpuestoExtToGastosImpuesto(entityExt);
			return this.ifzGastosImpuesto.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(GastosImpuesto entity) throws ExcepConstraint {
		try {
			this.ifzGastosImpuesto.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(GastosImpuestoExt entityExt) throws ExcepConstraint {
		try {
			GastosImpuesto entity = this.convertidor.GastosImpuestoExtToGastosImpuesto(entityExt);
			this.ifzGastosImpuesto.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public GastosImpuesto update(GastosImpuesto entity) throws ExcepConstraint {
		try {
			this.ifzGastosImpuesto.update(entity);
			
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public GastosImpuesto update(GastosImpuestoExt entityExt) throws ExcepConstraint {
		try {
			GastosImpuesto entity = this.convertidor.GastosImpuestoExtToGastosImpuesto(entityExt);
			this.ifzGastosImpuesto.update(entity);
			
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public GastosImpuesto findById(Long id) {
		try {
			return this.ifzGastosImpuesto.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<GastosImpuesto> findByProperty(String propertyName,	final Object value) {
		try {
			return this.ifzGastosImpuesto.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<GastosImpuesto> findAll() {
		try {
			return this.ifzGastosImpuesto.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<GastosImpuesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			List<GastosImpuesto> lista = this.ifzGastosImpuesto.findByPropertyPojoCompleto(propertyName, tipo, value);
			return lista;
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	public List<GastosImpuestoExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<GastosImpuestoExt> listaExt = new ArrayList<GastosImpuestoExt>();
		
		try{
			List<GastosImpuesto> lista = this.ifzGastosImpuesto.findByPropertyPojoCompleto(propertyName, tipo, value);
			
			for (GastosImpuesto gastosImpuesto : lista) {
				GastosImpuestoExt pojoAux = this.convertidor.GastosImpuestoToGastosImpuestoExt(gastosImpuesto);
				
				if ("".equals(tipo)) {
					listaExt.add(pojoAux);
				} else {
					if (tipo.equals(pojoAux.getImpuestoId().getTipoCuenta())) {
						listaExt.add(pojoAux);
					}
				}
			}
		} catch(RuntimeException re) {
			throw re;
		}

		return listaExt;
	}
}

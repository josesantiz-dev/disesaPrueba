package net.giro.adp.logica;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ConceptoPresupuesto;
import net.giro.adp.beans.ConceptoPresupuestoExt;
import net.giro.adp.dao.ConceptoPresupuestoDAO;
import net.giro.comun.ExcepConstraint;

@Stateless
public class ConceptoPresupuestoFac implements ConceptoPresupuestoRem {

	private static Logger log = Logger.getLogger(ConceptoPresupuestoFac.class);
	
	InitialContext ctx;
	private ConvertExt convertidor;
	private ConceptoPresupuestoDAO ifzConceptoPresupuesto;
	
	public ConceptoPresupuestoFac() {
		try{
			
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_GestionProyectos//ConceptoPresupuestoImp!net.giro.adp.dao.ConceptoPresupuestoDAO";
            this.ifzConceptoPresupuesto = (ConceptoPresupuestoDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
			
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear ConceptoPresupuestoFac", e);
			ctx = null;
		}
	}

	public Long save(ConceptoPresupuesto entity) throws ExcepConstraint {
		try {
			return this.ifzConceptoPresupuesto.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public Long save(ConceptoPresupuestoExt entityExt) throws ExcepConstraint {
		try {
			return this.ifzConceptoPresupuesto.save( this.convertidor.ConceptoPresupuestoExtToConceptoPresupuesto(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(ConceptoPresupuesto entity) throws ExcepConstraint {
		try {
			this.ifzConceptoPresupuesto.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(ConceptoPresupuestoExt entityExt) throws ExcepConstraint {
		try {
			this.ifzConceptoPresupuesto.delete( this.convertidor.ConceptoPresupuestoExtToConceptoPresupuesto(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public ConceptoPresupuesto update(ConceptoPresupuesto entity) throws ExcepConstraint {
		try {
			this.ifzConceptoPresupuesto.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public ConceptoPresupuesto update(ConceptoPresupuestoExt entityExt) throws ExcepConstraint {
		try {
			ConceptoPresupuesto entity = this.convertidor.ConceptoPresupuestoExtToConceptoPresupuesto(entityExt);
			this.ifzConceptoPresupuesto.update( entity );
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public ConceptoPresupuesto findById(long id) {
		try {
			return this.ifzConceptoPresupuesto.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public ConceptoPresupuestoExt findExtById(long id) {
		try {
			return this.convertidor.ConceptoPresupuestoToConceptoPresupuestoExt( this.ifzConceptoPresupuesto.findById( id ) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	
	public List<ConceptoPresupuesto> findByProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzConceptoPresupuesto.findByProperty(propertyName, value, max);
		} catch (RuntimeException re) {	
			log.error("Error en ConceptoPresupuestoFac.findByProperty(propertyName, value, max)", re);
			throw re;
		}
	}

	public List<ConceptoPresupuestoExt> findExtByProperty(String propertyName, Object value, int max) {
		try {
			List<ConceptoPresupuesto> lista = this.ifzConceptoPresupuesto.findByProperty(propertyName, value, max);
			List<ConceptoPresupuestoExt> listaExt = new ArrayList<>();
			for(ConceptoPresupuesto var: lista){
				listaExt.add( this.convertidor.ConceptoPresupuestoToConceptoPresupuestoExt(var) );
			}
			return listaExt;
		} catch (RuntimeException re) {	
			log.error("Error en ConceptoPresupuestoFac.findByProperty(propertyName, value, max)", re);
			throw re;
		}
	}

	public List<ConceptoPresupuesto> findLikeProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzConceptoPresupuesto.findLikeProperty(propertyName, value, max);
		} catch (RuntimeException re) {	
			log.error("Error en ConceptoPresupuestoFac.findLikeProperty(propertyName, value, max)", re);
			throw re;
		}
	}

	public List<ConceptoPresupuestoExt> findExtLikeProperty(String propertyName, Object value, int max) {
		try {
			List<ConceptoPresupuesto> lista =  this.ifzConceptoPresupuesto.findLikeProperty(propertyName, value, max);
			List<ConceptoPresupuestoExt> listaExt = new ArrayList<>();
			for(ConceptoPresupuesto var: lista){
				listaExt.add( this.convertidor.ConceptoPresupuestoToConceptoPresupuestoExt(var) );
			}
			return listaExt;
		} catch (RuntimeException re) {	
			log.error("Error en ConceptoPresupuestoFac.findLikeProperty(propertyName, value, max)", re);
			throw re;
		}
	}

	public List<ConceptoPresupuesto> findAllActivos() {
		try {
			return this.ifzConceptoPresupuesto.findAllActivos();
		} catch (RuntimeException re) {	
			log.error("Error en ConceptoPresupuestoFac.findAllActivos()", re);
			throw re;
		}
	}

	public List<ConceptoPresupuestoExt> findAllExtActivos() {
		try {
			List<ConceptoPresupuesto> lista = this.ifzConceptoPresupuesto.findAllActivos();
			List<ConceptoPresupuestoExt> listaExt = new ArrayList<>();
			for(ConceptoPresupuesto var: lista){
				listaExt.add( this.convertidor.ConceptoPresupuestoToConceptoPresupuestoExt(var) );
			}
			return listaExt;
		} catch (RuntimeException re) {	
			log.error("Error en ConceptoPresupuestoFac.findAllExtActivos()", re);
			throw re;
		}
	}

	
	
	
}

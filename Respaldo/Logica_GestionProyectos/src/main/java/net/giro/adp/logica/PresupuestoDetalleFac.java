package net.giro.adp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.adp.beans.PresupuestoDetalle;
import net.giro.adp.beans.PresupuestoDetalleExt;
import net.giro.adp.dao.PresupuestoDetalleDAO;
import net.giro.comun.ExcepConstraint;

import org.apache.log4j.Logger;

@Stateless
public class PresupuestoDetalleFac implements PresupuestoDetalleRem {
	private static Logger log = Logger.getLogger(PresupuestoDetalleFac.class);	
	InitialContext ctx;
	private PresupuestoDetalleDAO ifzPresupuestoDetalle;
	private ConvertExt convertidor;
	
	
	public PresupuestoDetalleFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            
            this.ifzPresupuestoDetalle = (PresupuestoDetalleDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//PresupuestoDetalleImp!net.giro.adp.dao.PresupuestoDetalleDAO");
            
           this.convertidor = new ConvertExt();
           this.convertidor.setFrom("PresupuestoDetalleFac");
           this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear PresupuestoDetalle", e);
			ctx = null;
		}
	}

	public Long save(PresupuestoDetalle entity) throws ExcepConstraint {
		try {
			return this.ifzPresupuestoDetalle.save(entity);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.save(entity)", e);
			throw e;
		}
	}

	public Long save(PresupuestoDetalleExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.PresupuestoDetalleExtToPresupuestoDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.save(entityExt)", e);
			throw e;
		}
	}


	public void update(PresupuestoDetalle entity) throws ExcepConstraint {
		try {
			this.ifzPresupuestoDetalle.update(entity);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.update(entity)", e);
			throw e;
		}
	}

	public void update(PresupuestoDetalleExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.PresupuestoDetalleExtToPresupuestoDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.update(entityExt)", e);
			throw e;
		}
	}


	public void delete(Long id) throws ExcepConstraint {
		try {
			this.ifzPresupuestoDetalle.delete(id);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.delete(id)", e);
			throw e;
		}
	}

	public List<PresupuestoDetalle> findAll() {
		try {
			return this.ifzPresupuestoDetalle.findAll();
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findAll()", e);
			throw e;
		}
	}

	public PresupuestoDetalle findById(Long id) {
		try {
			return this.ifzPresupuestoDetalle.findById(id);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findById(id)", e);
			throw e;
		}
	}

	public PresupuestoDetalleExt findByIdExt(Long id) throws Exception {
		try {
			return this.convertidor.PresupuestoDetalleToPresupuestoDetalleExt(this.findById(id));
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findByIdExt(id)", e);
			throw e;
		}
	}

	public List<PresupuestoDetalle> findByProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzPresupuestoDetalle.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findByProperty(propertyName, value, max)", e);
			throw e;
		}
	}

	public List<PresupuestoDetalleExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception {
		List<PresupuestoDetalleExt> listaExt = new ArrayList<PresupuestoDetalleExt>();
		
		try {
			List<PresupuestoDetalle> lista = this.findByProperty(propertyName, value, max);
			for(PresupuestoDetalle var : lista)
				listaExt.add(this.convertidor.PresupuestoDetalleToPresupuestoDetalleExt(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	public List<PresupuestoDetalle> findLikeProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzPresupuestoDetalle.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		}
	}
	
	public List<PresupuestoDetalleExt> findLikePropertyExt(String propertyName, Object value, int max) throws Exception {
		List<PresupuestoDetalleExt> listaExt = new ArrayList<PresupuestoDetalleExt>();
		
		try {
			List<PresupuestoDetalle> lista = this.findLikeProperty(propertyName, value, max);
			for(PresupuestoDetalle var : lista)
				listaExt.add(this.convertidor.PresupuestoDetalleToPresupuestoDetalleExt(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikePropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}


}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase

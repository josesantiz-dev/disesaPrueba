package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.dao.TraspasoDetalleDAO;

@Stateless
public class TraspasoDetalleFac implements TraspasoDetalleRem {
	private static Logger log = Logger.getLogger(TraspasoDetalleFac.class);
	
	InitialContext ctx;
	private ConvertExt convertidor;
	private TraspasoDetalleDAO ifzTraspasoDetalle;
	
	
	public TraspasoDetalleFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_Inventarios//TraspasoDetalleImp!net.giro.inventarios.dao.TraspasoDetalleDAO";
            this.ifzTraspasoDetalle = (TraspasoDetalleDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear TraspasoDetalleFac", e);
			ctx = null;
		}
	}

	
	public Long save(TraspasoDetalle entity) throws ExcepConstraint {
		try {
			return this.ifzTraspasoDetalle.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public Long save(TraspasoDetalleExt entityExt) throws ExcepConstraint {
		try {
			return this.ifzTraspasoDetalle.save( this.convertidor.TraspasoDetalleExtToTraspasoDetalle(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(TraspasoDetalle entity) throws ExcepConstraint {
		try {
			this.ifzTraspasoDetalle.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(TraspasoDetalleExt entityExt) throws ExcepConstraint {
		try {
			this.ifzTraspasoDetalle.delete( this.convertidor.TraspasoDetalleExtToTraspasoDetalle(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public TraspasoDetalle update(TraspasoDetalle entity) throws ExcepConstraint {
		try {
			this.ifzTraspasoDetalle.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public TraspasoDetalle update(TraspasoDetalleExt entityExt) throws ExcepConstraint {
		try {
			TraspasoDetalle entity = this.convertidor.TraspasoDetalleExtToTraspasoDetalle(entityExt);
			this.ifzTraspasoDetalle.update( entity );
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public TraspasoDetalle findById(Long id) {
		try {
			return this.ifzTraspasoDetalle.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public TraspasoDetalleExt findByIdExt(Long id) {
		try {
			return this.convertidor.TraspasoDetalleToTraspasoDetalleExt( this.ifzTraspasoDetalle.findById( id ) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public List<TraspasoDetalle> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzTraspasoDetalle.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<TraspasoDetalleExt> findExtByProperty(String propertyName, Object value) {
		try {
			List<TraspasoDetalleExt> listaExt = new ArrayList<>();
			
			List<TraspasoDetalle> lista = this.ifzTraspasoDetalle.findByProperty(propertyName, value);
			
			for(TraspasoDetalle a:lista){
				listaExt.add(this.convertidor.TraspasoDetalleToTraspasoDetalleExt(a));
			}
			return listaExt;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<TraspasoDetalle> findAll() {
		try {
			return this.ifzTraspasoDetalle.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	

	public List<TraspasoDetalleExt> findAllExt() {
		List<TraspasoDetalleExt> listaExt = new ArrayList<>();
		try {
			List<TraspasoDetalle> lista = this.ifzTraspasoDetalle.findAll();
			for(TraspasoDetalle a:lista){
				listaExt.add( this.convertidor.TraspasoDetalleToTraspasoDetalleExt(a) );
			}
			return listaExt;
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<TraspasoDetalle> findAllActivos() {
		try {
			return this.ifzTraspasoDetalle.findAllActivos();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalleExt> findDetallesExtById(long idAlmacenTraspaso) {
		List<TraspasoDetalle> lista = this.ifzTraspasoDetalle.findDetallesById(idAlmacenTraspaso);
		List<TraspasoDetalleExt> listaExt = new ArrayList<>();
		for(TraspasoDetalle td: lista){
			listaExt.add( this.convertidor.TraspasoDetalleToTraspasoDetalleExt(td) );
		}
		return listaExt;
	}
	
	@Override
	public TraspasoDetalle convertir(TraspasoDetalleExt entity) {
		return this.convertidor.TraspasoDetalleExtToTraspasoDetalle(entity);
	}

	@Override
	public TraspasoDetalleExt convertir(TraspasoDetalle entity) {
		return this.convertidor.TraspasoDetalleToTraspasoDetalleExt(entity);
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Añado disponibilidad del convertidor
 */
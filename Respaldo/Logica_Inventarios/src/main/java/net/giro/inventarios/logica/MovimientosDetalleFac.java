package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.dao.MovimientosDetalleDAO;

@Stateless
public class MovimientosDetalleFac  implements MovimientosDetalleRem{

	private static Logger log = Logger.getLogger(MovimientosDetalleFac.class);
	
	InitialContext ctx;
	private ConvertExt convertidor;
	private MovimientosDetalleDAO ifzMovimientos;
	
	public MovimientosDetalleFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_Inventarios//MovimientosDetalleImp!net.giro.inventarios.dao.MovimientosDetalleDAO";
            this.ifzMovimientos = (MovimientosDetalleDAO) ctx.lookup(ejbName);
            

        	
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear MovimientosDetalleFac", e);
			ctx = null;
		}
	}

	public Long save(MovimientosDetalle entity) throws ExcepConstraint {
		try {
			return this.ifzMovimientos.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public Long save(MovimientosDetalleExt entityExt) throws ExcepConstraint {
		try {
			return this.ifzMovimientos.save( this.convertidor.MovimientosDetalleExtToMovimientosDetalle(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(MovimientosDetalle entity) throws ExcepConstraint {
		try {
			this.ifzMovimientos.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(MovimientosDetalleExt entityExt) throws ExcepConstraint {
		try {
			this.ifzMovimientos.delete( this.convertidor.MovimientosDetalleExtToMovimientosDetalle(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public MovimientosDetalle update(MovimientosDetalle entity) throws ExcepConstraint {
		try {
			this.ifzMovimientos.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public MovimientosDetalle update(MovimientosDetalleExt entityExt) throws ExcepConstraint {
		try {
			MovimientosDetalle entity = this.convertidor.MovimientosDetalleExtToMovimientosDetalle(entityExt);
			this.ifzMovimientos.update( entity );
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public MovimientosDetalle findById(Long id) {
		try {
			return this.ifzMovimientos.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public MovimientosDetalleExt findByIdExt(Long id) {
		try {
			return this.convertidor.MovimientosDetalleToMovimientosDetalleExt( this.ifzMovimientos.findById( id ) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public List<MovimientosDetalle> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzMovimientos.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<MovimientosDetalleExt> findExtByProperty(String propertyName, Object value) {
		try {
			List<MovimientosDetalleExt> listaExt = new ArrayList<>();
			
			List<MovimientosDetalle> lista = this.ifzMovimientos.findByProperty(propertyName, value);
			
			for(MovimientosDetalle a:lista){
				listaExt.add(this.convertidor.MovimientosDetalleToMovimientosDetalleExt(a));
			}
			return listaExt;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<MovimientosDetalle> findAll() {
		try {
			return this.ifzMovimientos.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	

	public List<MovimientosDetalleExt> findAllExt() {
		List<MovimientosDetalleExt> listaExt = new ArrayList<>();
		try {
			List<MovimientosDetalle> lista = this.ifzMovimientos.findAll();
			for(MovimientosDetalle a:lista){
				listaExt.add( this.convertidor.MovimientosDetalleToMovimientosDetalleExt(a) );
			}
			return listaExt;
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<MovimientosDetalle> findAllActivos() {
		try {
			return this.ifzMovimientos.findAllActivos();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<MovimientosDetalle> findDetallesById(long idAlmacenMovimiento){
		try {
			return this.ifzMovimientos.findDetallesById(idAlmacenMovimiento);
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<MovimientosDetalleExt> findDetallesExtById(long idAlmacenMovimiento){
		try {
			List<MovimientosDetalle> lista = this.ifzMovimientos.findDetallesById(idAlmacenMovimiento);
			List<MovimientosDetalleExt> listaExt = new ArrayList<>();
			
			for(MovimientosDetalle md:lista){
				listaExt.add( this.convertidor.MovimientosDetalleToMovimientosDetalleExt(md) );
			}
			
			return listaExt;
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	@Override
	public List<MovimientosDetalleExt> findDetallesExtByIdOrdenCompra(long idOrdenCompra) {
		return null;
	}
	
}

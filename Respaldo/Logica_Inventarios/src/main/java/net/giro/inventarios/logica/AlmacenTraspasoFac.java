package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.dao.AlmacenTraspasoDAO;

@Stateless
public class AlmacenTraspasoFac implements AlmacenTraspasoRem {
	private static Logger log = Logger.getLogger(AlmacenTraspasoFac.class);
	
	InitialContext ctx;
	private ConvertExt convertidor;
	private AlmacenTraspasoDAO ifzAlmacenTraspaso;
	
	
	public AlmacenTraspasoFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_Inventarios//AlmacenTraspasoImp!net.giro.inventarios.dao.AlmacenTraspasoDAO";
            this.ifzAlmacenTraspaso = (AlmacenTraspasoDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear AlmacenTraspasoFac", e);
			ctx = null;
		}
	}

	
	public Long save(AlmacenTraspaso entity) throws ExcepConstraint {
		try {
			return this.ifzAlmacenTraspaso.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(AlmacenTraspasoExt entityExt) throws ExcepConstraint {
		try {
			//log.info("IdEntrega: "+entityExt.getEntrega().getId()+", idRecibe: "+entityExt.getRecibe().getId());
			return this.ifzAlmacenTraspaso.save( this.convertidor.AlmacenTraspasoExtToAlmacenTraspaso(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(AlmacenTraspaso entity) throws ExcepConstraint {
		try {
			this.ifzAlmacenTraspaso.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(AlmacenTraspasoExt entityExt) throws ExcepConstraint {
		try {
			this.ifzAlmacenTraspaso.delete( this.convertidor.AlmacenTraspasoExtToAlmacenTraspaso(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public AlmacenTraspaso update(AlmacenTraspaso entity) throws ExcepConstraint {
		try {
			this.ifzAlmacenTraspaso.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public AlmacenTraspaso update(AlmacenTraspasoExt entityExt) throws ExcepConstraint {
		try {
			AlmacenTraspaso entity = this.convertidor.AlmacenTraspasoExtToAlmacenTraspaso(entityExt);
			this.ifzAlmacenTraspaso.update( entity );
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public AlmacenTraspaso findById(Long id) {
		try {
			return this.ifzAlmacenTraspaso.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public AlmacenTraspasoExt findByIdExt(Long id) {
		try {
			return this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt( this.ifzAlmacenTraspaso.findById( id ) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public List<AlmacenTraspaso> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzAlmacenTraspaso.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<AlmacenTraspaso> findAll() {
		try {
			return this.ifzAlmacenTraspaso.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<AlmacenTraspasoExt> findAllExt() {
		try {
			List<AlmacenTraspaso> lista = this.ifzAlmacenTraspaso.findAll();
			List<AlmacenTraspasoExt> listaExt = new ArrayList<>();
			
			for(AlmacenTraspaso at:lista){
				listaExt.add( this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at) );
			}
			
			return listaExt;
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<AlmacenTraspaso> findAllActivos() {
		try {
			return this.ifzAlmacenTraspaso.findAllActivos();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<AlmacenTraspaso> findByAlmacenOrigen(String nombreAlmacen) {
		try {
			return this.ifzAlmacenTraspaso.findByAlmacenOrigen(nombreAlmacen);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<AlmacenTraspaso> findByAlmacenDestino(String nombreAlmacen) {
		try {
			return this.ifzAlmacenTraspaso.findByAlmacenDestino(nombreAlmacen);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<AlmacenTraspasoExt> findExtByAlmacenOrigen(String nombreAlmacen) {
		try {
			List<AlmacenTraspaso> lista = this.ifzAlmacenTraspaso.findByAlmacenOrigen(nombreAlmacen);
			List<AlmacenTraspasoExt> listaExt = new ArrayList<>();
			for(AlmacenTraspaso at:lista){
				listaExt.add( this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at) );
			}
			return listaExt;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<AlmacenTraspasoExt> findExtByAlmacenDestino(String nombreAlmacen) {
		try {
			List<AlmacenTraspaso> lista = this.ifzAlmacenTraspaso.findByAlmacenDestino(nombreAlmacen);
			List<AlmacenTraspasoExt> listaExt = new ArrayList<>();
			for(AlmacenTraspaso at:lista){
				listaExt.add( this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at) );
			}
			return listaExt;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<AlmacenTraspaso> findByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen) {
		try {
			return this.ifzAlmacenTraspaso.findByAlmacenOrdenCompleta(tipoAlmacen, nombreAlmacen);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<AlmacenTraspasoExt> findExtByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen) {
		try {
			List<AlmacenTraspaso> lista = this.ifzAlmacenTraspaso.findByAlmacenOrdenCompleta(tipoAlmacen, nombreAlmacen);
			List<AlmacenTraspasoExt> listaExt = new ArrayList<>(); 
			
			for(AlmacenTraspaso var: lista){
				listaExt.add(this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(var));
			}
			
			return listaExt;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public AlmacenTraspaso convertir(AlmacenTraspasoExt entity) {
		return this.convertidor.AlmacenTraspasoExtToAlmacenTraspaso(entity);
	}

	@Override
	public AlmacenTraspasoExt convertir(AlmacenTraspaso entity) {
		return this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(entity);
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Añado disponibilidad del convertidor
 */
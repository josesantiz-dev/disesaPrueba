package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenExt;
import net.giro.inventarios.dao.AlmacenDAO;

@Stateless
public class AlmacenFac implements AlmacenRem {
	private static Logger log = Logger.getLogger(AlmacenFac.class);
	
	InitialContext ctx;
	private ConvertExt convertidor;
	private AlmacenDAO ifzAlmacen;
	
	public AlmacenFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_Inventarios//AlmacenImp!net.giro.inventarios.dao.AlmacenDAO";
            this.ifzAlmacen = (AlmacenDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear AlmacenFac", e);
			ctx = null;
		}
	}

	public Long save(Almacen entity) throws ExcepConstraint {
		try {
			return this.ifzAlmacen.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public Long save(AlmacenExt entityExt) throws ExcepConstraint {
		try {
			return this.ifzAlmacen.save( this.convertidor.AlmacenExtToAlmacen(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(Almacen entity) throws ExcepConstraint {
		try {
			this.ifzAlmacen.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(AlmacenExt entityExt) throws ExcepConstraint {
		try {
			this.ifzAlmacen.delete( this.convertidor.AlmacenExtToAlmacen(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public Almacen update(Almacen entity) throws ExcepConstraint {
		try {
			this.ifzAlmacen.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public Almacen update(AlmacenExt entityExt) throws ExcepConstraint {
		try {
			Almacen entity = this.convertidor.AlmacenExtToAlmacen(entityExt);
			this.ifzAlmacen.update( entity );
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public Almacen findById(Long id) {
		try {
			return this.ifzAlmacen.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public AlmacenExt findByIdExt(Long id) {
		try {
			return this.convertidor.AlmacenToAlmacenExt( this.ifzAlmacen.findById( id ) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public List<Almacen> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzAlmacen.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<AlmacenExt> findExtByProperty(String propertyName, Object value) {
		try {
			List<AlmacenExt> listaExt = new ArrayList<>();
			
			List<Almacen> lista = this.ifzAlmacen.findByProperty(propertyName, value);
			
			for(Almacen a:lista){
				listaExt.add(this.convertidor.AlmacenToAlmacenExt(a));
			}
			return listaExt;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<Almacen> findLikeProperty(String propertyName, String value) throws Exception {
		try {
			return this.ifzAlmacen.findLikeProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenExt> findExtLikeProperty(String propertyName, String value) throws Exception {
		try {
			List<AlmacenExt> listaExt = new ArrayList<>();
			List<Almacen> lista = this.findLikeProperty(propertyName, value);
			for(Almacen a : lista){
				listaExt.add(this.convertidor.AlmacenToAlmacenExt(a));
			}
			
			return listaExt;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<Almacen> findAll() {
		try {
			return this.ifzAlmacen.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<AlmacenExt> findAllExt() {
		List<AlmacenExt> listaExt = new ArrayList<>();
		try {
			List<Almacen> lista = this.ifzAlmacen.findAll();
			for(Almacen a:lista){
				listaExt.add( this.convertidor.AlmacenToAlmacenExt(a) );
			}
			return listaExt;
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<Almacen> findAllActivos() {
		try {
			return this.ifzAlmacen.findAllActivos();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	@Override
	public boolean comprobarPrincipal(Long idSucursal, Long idAlmacen) throws Exception {
		try {
			List<Almacen> lista = this.ifzAlmacen.comprobarPrincipal(idSucursal, idAlmacen);
			return lista != null && ! lista.isEmpty() ? true : false;
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	public boolean comprobarNombre(String nombre, Long idAlmacen) throws Exception {
		try {
			List<Almacen> lista = this.ifzAlmacen.comprobarNombre(nombre, idAlmacen);
			return lista != null && ! lista.isEmpty() ? true : false;
		} catch (Exception re) {	
			throw re;
		}
	}
}

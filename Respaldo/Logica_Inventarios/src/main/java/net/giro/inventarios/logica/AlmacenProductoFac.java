package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.dao.AlmacenProductoDAO;

@Stateless
public class AlmacenProductoFac  implements AlmacenProductoRem{

	private static Logger log = Logger.getLogger(AlmacenProductoFac.class);
	
	InitialContext ctx;
	private ConvertExt convertidor;
	private AlmacenProductoDAO ifzAlmacenProductos;
	
	public AlmacenProductoFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_Inventarios//AlmacenProductoImp!net.giro.inventarios.dao.AlmacenProductoDAO";
            this.ifzAlmacenProductos = (AlmacenProductoDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear AlmacenProductoFac", e);
			ctx = null;
		}
	}

	
	public Long save(AlmacenProducto entity) throws ExcepConstraint {
		try {
			return this.ifzAlmacenProductos.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	
	public Long save(AlmacenProductoExt entityExt) throws ExcepConstraint {
		try {
			return this.ifzAlmacenProductos.save( this.convertidor.AlmacenProductoExtToAlmacenProducto(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	
	public void delete(AlmacenProducto entity) throws ExcepConstraint {
		try {
			this.ifzAlmacenProductos.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	
	public void delete(AlmacenProductoExt entityExt) throws ExcepConstraint {
		try {
			this.ifzAlmacenProductos.delete( this.convertidor.AlmacenProductoExtToAlmacenProducto(entityExt) );;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	
	public AlmacenProducto update(AlmacenProducto entity) throws ExcepConstraint {
		try {
			this.ifzAlmacenProductos.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	
	public AlmacenProducto update(AlmacenProductoExt entityExt) throws ExcepConstraint {
		try {
			AlmacenProducto entity = this.convertidor.AlmacenProductoExtToAlmacenProducto(entityExt);
			this.ifzAlmacenProductos.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	
	public AlmacenProducto findById(Long id) {
		try {
			return this.ifzAlmacenProductos.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	
	public AlmacenProductoExt findByIdExt(Long id) {
		try {
			return this.convertidor.AlmacenProductoToAlmacenProductoExt( this.ifzAlmacenProductos.findById(id) ) ;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	
	public List<AlmacenProducto> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzAlmacenProductos.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public double findCantidadEnAlmacen(long idAlmacen, long idProducto) {
		
		//Debe regresar solo 1, ya que debe ser unico tanto idproducto como almacen
		List<AlmacenProducto> producto = this.ifzAlmacenProductos.findCantidadEnAlmacen(idAlmacen, idProducto);
		
		//si no regresa nulo, revisar que la lista no este vacia, ya que el producto puede existir, pero no estar en almacen
		return producto == null ? 0: producto.size() == 0 ? 0 : producto.get(0).getExistencia();
	}
	
	public AlmacenProducto findAlmacenProducto(long idAlmacen, long idProducto) {
		
		log.info("Logica findAlmacenProducto, Almacen: "+idAlmacen+", Producto: "+idProducto);
		
		//Debe regresar solo 1, ya que debe ser unico tanto idproducto como almacen
		List<AlmacenProducto> almacenProducto = this.ifzAlmacenProductos.findAlmacenProducto(idAlmacen, idProducto);
		
		//si la lista regresa cero, quiere decir que no hay este producto en almacen
		return almacenProducto == null ? null : almacenProducto.size() == 0 ? null : almacenProducto.get(0) ;		//aqui esta el error
	}
	
	public List<AlmacenProducto> findAll() {
		try {
			return this.ifzAlmacenProductos.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<AlmacenProductoExt> findAllExt() {
		try {
			List<AlmacenProducto> lista = this.ifzAlmacenProductos.findAll();
			List<AlmacenProductoExt> listaExt = new ArrayList<>();
			
			for(AlmacenProducto ap: lista){
				listaExt.add( this.convertidor.AlmacenProductoToAlmacenProductoExt(ap) );
			}
			
			return listaExt;
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<AlmacenProducto> findAllActivos() {
		try {
			return this.ifzAlmacenProductos.findAllActivos();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<AlmacenProducto> findExistentes(String campoBusqueda, long idAlmacen, String valor){
		try {
			return this.ifzAlmacenProductos.findExistentes(campoBusqueda, idAlmacen, valor);
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<AlmacenProductoExt> findExistentesExt(String campoBusqueda, long idAlmacen, String valor){
		try {
			List<AlmacenProducto> lista = this.ifzAlmacenProductos.findExistentes(campoBusqueda, idAlmacen, valor);
			List<AlmacenProductoExt> listaExt = new ArrayList<>();
			
			for(AlmacenProducto var:lista){
				listaExt.add( this.convertidor.AlmacenProductoToAlmacenProductoExt(var) );
			}
			
			return listaExt;
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public AlmacenProducto findProductoExistente(String campoBusqueda, long idAlmacen, String valor){
		try {
			return this.ifzAlmacenProductos.findProductoExistente(campoBusqueda, idAlmacen, valor);
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public AlmacenProductoExt findProductoExistenteExt(String campoBusqueda, long idAlmacen, String valor){
		try {
			AlmacenProducto ap = this.ifzAlmacenProductos.findProductoExistente(campoBusqueda, idAlmacen, valor);
			
			AlmacenProductoExt apExt = null;
			
			if(ap != null){
				apExt = this.convertidor.AlmacenProductoToAlmacenProductoExt(ap);
			}
			
			return apExt;
			
		} catch (RuntimeException re) {		
			throw re;
		}
	}

}

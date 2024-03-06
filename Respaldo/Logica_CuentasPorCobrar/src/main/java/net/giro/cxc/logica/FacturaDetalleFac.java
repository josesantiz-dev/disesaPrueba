package net.giro.cxc.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.dao.FacturaDetalleDAO;

@Stateless
public class FacturaDetalleFac implements FacturaDetalleRem {
	private static Logger log = Logger.getLogger(FacturaDetalleFac.class);	
	private InitialContext ctx;	
	private FacturaDetalleDAO ifzFacturaDetalle;
	private ConvertExt convertidor;
	
	public FacturaDetalleFac() {
		try {
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            this.ifzFacturaDetalle = (FacturaDetalleDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaDetalleImp!net.giro.cxc.dao.FacturaDetalleDAO");
            this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaFac", e);
			this.ctx = null;
		}
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public Long save(FacturaDetalle entity) throws ExcepConstraint {
		try {
			return this.ifzFacturaDetalle.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public Long save(FacturaDetalleExt entityExt) throws ExcepConstraint {
		try {
			FacturaDetalle entity = this.convertidor.FacturaDetalleExtToFacturaDetalle(entityExt);
			return this.ifzFacturaDetalle.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public void delete(FacturaDetalle entity) throws ExcepConstraint {
		try {
			this.ifzFacturaDetalle.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public void delete(FacturaDetalleExt entityExt) throws ExcepConstraint {
		try {
			FacturaDetalle entity = this.convertidor.FacturaDetalleExtToFacturaDetalle(entityExt);
			this.ifzFacturaDetalle.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalle update(FacturaDetalle entity) throws ExcepConstraint {
		try {
			this.ifzFacturaDetalle.update(entity);
			
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalle update(FacturaDetalleExt entityExt) throws ExcepConstraint {
		try {
			FacturaDetalle entity = this.convertidor.FacturaDetalleExtToFacturaDetalle(entityExt);
			this.ifzFacturaDetalle.update(entity);
			
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalle findById(Long id) {
		try {
			return this.ifzFacturaDetalle.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzFacturaDetalle.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<FacturaDetalle> findAll() {
		try {
			return this.ifzFacturaDetalle.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> findByPropertyPojoCompleto(String propertyName, long value){
		try{
			return this.findByProperty(propertyName, value);
		}catch(RuntimeException re){
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleExt> findByPropertyPojoCompletoExt(String propertyName, long value) throws Exception{
		List<FacturaDetalleExt> listaExt = new ArrayList<FacturaDetalleExt>();
		
		try {
			List<FacturaDetalle> lista = this.ifzFacturaDetalle.findByPropertyPojoCompleto(propertyName, value);
			for (FacturaDetalle fd : lista) {
				listaExt.add(this.convertidor.FacturaDetalleToFacturaDetalleExt( fd  ) );
			}
		} catch (RuntimeException re) {
			throw re;
		}

		return listaExt;
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.1 | 2017-04-25 | Javier Tirado 	| Quito los mensajes a consola y añado la anotacion Override a los metodos
 */
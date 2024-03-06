package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.dao.MovimientosDetalleDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class MovimientosDetalleFac implements MovimientosDetalleRem {
	private static Logger log = Logger.getLogger(MovimientosDetalleFac.class);
	private InitialContext ctx;
	private ConvertExt convertidor;
	private MovimientosDetalleDAO ifzMovimientos;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
	
	public MovimientosDetalleFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try{
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzMovimientos = (MovimientosDetalleDAO) this.ctx.lookup("ejb:/Model_Inventarios//MovimientosDetalleImp!net.giro.inventarios.dao.MovimientosDetalleDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear MovimientosDetalleFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(MovimientosDetalle entity) throws Exception {
		try {
			return this.ifzMovimientos.save(entity, null);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<MovimientosDetalle> saveOrUpdateList(List<MovimientosDetalle> entities) throws Exception {
		try {
			return this.ifzMovimientos.saveOrUpdateList(entities, null);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void update(MovimientosDetalle entity) throws Exception {
		try {
			this.ifzMovimientos.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(MovimientosDetalle entity) throws Exception {
		try {
			this.ifzMovimientos.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public MovimientosDetalle findById(Long id) {
		try {
			return this.ifzMovimientos.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<MovimientosDetalle> findAll() {
		try {
			return this.ifzMovimientos.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<MovimientosDetalle> findAllActivos() {
		try {
			return this.ifzMovimientos.findAllActivos();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<MovimientosDetalle> findDetallesById(long idAlmacenMovimiento){
		try {
			return this.ifzMovimientos.findDetallesById(idAlmacenMovimiento);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<MovimientosDetalle> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzMovimientos.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public MovimientosDetalle convertir(MovimientosDetalleExt target) {
		return this.convertidor.MovimientosDetalleExtToMovimientosDetalle(target);
	}

	@Override
	public MovimientosDetalleExt convertir(MovimientosDetalle target) {
		return this.convertidor.MovimientosDetalleToMovimientosDetalleExt(target);
	}

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	@Override
	public Long save(MovimientosDetalleExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.MovimientosDetalleExtToMovimientosDetalle(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(MovimientosDetalleExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.MovimientosDetalleExtToMovimientosDetalle(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(MovimientosDetalleExt entityExt) throws Exception {
		try {
			this.delete(this.convertidor.MovimientosDetalleExtToMovimientosDetalle(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public MovimientosDetalleExt findByIdExt(Long id) {
		try {
			return this.convertidor.MovimientosDetalleToMovimientosDetalleExt(this.ifzMovimientos.findById(id));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<MovimientosDetalleExt> findAllExt() {
		List<MovimientosDetalleExt> listaExt = new ArrayList<MovimientosDetalleExt>();
		List<MovimientosDetalle> lista = null;
		
		try {
			lista = this.findAll();
			if (lista != null && ! lista.isEmpty()) {
				for (MovimientosDetalle a : lista) 
					listaExt.add(this.convertidor.MovimientosDetalleToMovimientosDetalleExt(a));
			}
		} catch (Exception re) {		
			throw re;
		}

		return listaExt;
	}

	@Override
	public List<MovimientosDetalleExt> findExtAllActivos() {
		List<MovimientosDetalleExt> listaExt = new ArrayList<MovimientosDetalleExt>();
		List<MovimientosDetalle> lista = null;
		
		try {
			lista = this.findAllActivos();
			if (lista != null && ! lista.isEmpty()) {
				for (MovimientosDetalle a : lista) 
					listaExt.add(this.convertidor.MovimientosDetalleToMovimientosDetalleExt(a));
			}
		} catch (Exception re) {		
			throw re;
		}

		return listaExt;
	}

	@Override
	public List<MovimientosDetalleExt> findDetallesExtById(long idAlmacenMovimiento){
		List<MovimientosDetalleExt> listaExt = new ArrayList<MovimientosDetalleExt>();
		List<MovimientosDetalle> lista = null;
		
		try {
			lista = this.findDetallesById(idAlmacenMovimiento);
			if (lista != null && ! lista.isEmpty()) {
				for (MovimientosDetalle md : lista)
					listaExt.add(this.convertidor.MovimientosDetalleToMovimientosDetalleExt(md));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<MovimientosDetalleExt> findExtByProperty(String propertyName, Object value) {
		List<MovimientosDetalleExt> listaExt = new ArrayList<MovimientosDetalleExt>();
		List<MovimientosDetalle> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value);
			if (lista != null && ! lista.isEmpty()) {
				for (MovimientosDetalle a : lista)
					listaExt.add(this.convertidor.MovimientosDetalleToMovimientosDetalleExt(a));
			}
		} catch (Exception re) {
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<MovimientosDetalleExt> findDetallesExtByIdOrdenCompra(long idOrdenCompra) {
		return null;
	}

	@Override
	public List<MovimientosDetalleExt> saveOrUpdateListExt(List<MovimientosDetalleExt> entities) throws Exception {
		List<MovimientosDetalleExt> listaExt = new ArrayList<MovimientosDetalleExt>();
		List<MovimientosDetalle> lista = new ArrayList<MovimientosDetalle>();
		
		try {
			for (MovimientosDetalleExt item : entities)
				lista.add(this.convertidor.MovimientosDetalleExtToMovimientosDetalle(item));
			lista = this.saveOrUpdateList(lista);
			for (MovimientosDetalle a : lista)
				listaExt.add(this.convertidor.MovimientosDetalleToMovimientosDetalleExt(a));
		} catch (Exception re) {
			throw re;
		}
		
		return listaExt;
	}
}

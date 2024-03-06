package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.dao.TraspasoDetalleDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class TraspasoDetalleFac implements TraspasoDetalleRem {
	private static Logger log = Logger.getLogger(TraspasoDetalleFac.class);
	private InitialContext ctx;
	private ConvertExt convertidor;
	private TraspasoDetalleDAO ifzTraspasoDetalle;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
	
	public TraspasoDetalleFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzTraspasoDetalle = (TraspasoDetalleDAO) this.ctx.lookup("ejb:/Model_Inventarios//TraspasoDetalleImp!net.giro.inventarios.dao.TraspasoDetalleDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear TraspasoDetalleFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(TraspasoDetalle entity) throws Exception {
		try {
			return this.ifzTraspasoDetalle.save(entity, null);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalle> saveOrUpdateList(List<TraspasoDetalle> entities) throws Exception {
		try {
			return this.ifzTraspasoDetalle.saveOrUpdateList(entities, null);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(TraspasoDetalle entity) throws Exception {
		try {
			this.ifzTraspasoDetalle.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(TraspasoDetalle entity) throws Exception {
		try {
			this.ifzTraspasoDetalle.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public TraspasoDetalle findById(Long id) {
		try {
			return this.ifzTraspasoDetalle.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalle> findAll() {
		try {
			return this.ifzTraspasoDetalle.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalle> findAllActivos() {
		try {
			return this.ifzTraspasoDetalle.findAllActivos();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalle> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzTraspasoDetalle.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalle> findDetallesByIdTraspaso(long idAlmacenTraspaso) {
		try {
			return this.ifzTraspasoDetalle.findDetallesById(idAlmacenTraspaso);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public TraspasoDetalle convertir(TraspasoDetalleExt entity) {
		return this.convertidor.TraspasoDetalleExtToTraspasoDetalle(entity);
	}

	@Override
	public TraspasoDetalleExt convertir(TraspasoDetalle entity) {
		return this.convertidor.TraspasoDetalleToTraspasoDetalleExt(entity);
	}

	// -------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------

	@Override
	public Long save(TraspasoDetalleExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.TraspasoDetalleExtToTraspasoDetalle(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(TraspasoDetalleExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.TraspasoDetalleExtToTraspasoDetalle(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(TraspasoDetalleExt entityExt) throws Exception {
		try {
			this.delete(this.convertidor.TraspasoDetalleExtToTraspasoDetalle(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public TraspasoDetalleExt findByIdExt(Long id) {
		try {
			return this.convertidor.TraspasoDetalleToTraspasoDetalleExt(this.ifzTraspasoDetalle.findById(id));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalleExt> findAllExt() {
		List<TraspasoDetalleExt> listaExt = new ArrayList<>();
		List<TraspasoDetalle> lista = null;
		
		try {
			lista = this.findAll();
			if (lista != null && ! lista.isEmpty()) {
				for (TraspasoDetalle a : lista)
					listaExt.add(this.convertidor.TraspasoDetalleToTraspasoDetalleExt(a));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<TraspasoDetalleExt> findExtByProperty(String propertyName, Object value) {
		List<TraspasoDetalleExt> listaExt = new ArrayList<>();
		List<TraspasoDetalle> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value);
			if (lista != null && ! lista.isEmpty()) {
				for (TraspasoDetalle a : lista)
					listaExt.add(this.convertidor.TraspasoDetalleToTraspasoDetalleExt(a));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<TraspasoDetalleExt> findExtDetallesByIdTraspaso(long idAlmacenTraspaso) {
		List<TraspasoDetalleExt> listaExt = new ArrayList<>();
		List<TraspasoDetalle> lista = null;
		
		try {
			lista = this.findDetallesByIdTraspaso(idAlmacenTraspaso);
			if (lista != null && ! lista.isEmpty()) {
				for (TraspasoDetalle a : lista)
					listaExt.add(this.convertidor.TraspasoDetalleToTraspasoDetalleExt(a));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Añado disponibilidad del convertidor
 */
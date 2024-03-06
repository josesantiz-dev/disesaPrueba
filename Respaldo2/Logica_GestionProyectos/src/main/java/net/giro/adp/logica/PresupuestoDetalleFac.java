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
import net.giro.plataforma.InfoSesion;

import org.apache.log4j.Logger;

@Stateless
public class PresupuestoDetalleFac implements PresupuestoDetalleRem {
	private static Logger log = Logger.getLogger(PresupuestoDetalleFac.class);	
	private InitialContext ctx;
	private PresupuestoDetalleDAO ifzPresupuestoDetalle;
	private ConvertExt convertidor;
	private InfoSesion infoSesion;
	
	
	public PresupuestoDetalleFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            this.ifzPresupuestoDetalle = (PresupuestoDetalleDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//PresupuestoDetalleImp!net.giro.adp.dao.PresupuestoDetalleDAO");
            
            this.convertidor = new ConvertExt();
            this.convertidor.setFrom("PresupuestoDetalleFac");
            this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear PresupuestoDetalle", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(PresupuestoDetalle entity) throws Exception {
		try {
			return this.ifzPresupuestoDetalle.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalle> saveOrUpdateList(List<PresupuestoDetalle> entities) throws Exception {
		try {
			return this.ifzPresupuestoDetalle.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.saveOrUpdateList(List<ObraSatics> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(PresupuestoDetalle entity) throws Exception {
		try {
			this.ifzPresupuestoDetalle.update(entity);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long id) throws Exception {
		try {
			this.ifzPresupuestoDetalle.delete(id);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.delete(id)", e);
			throw e;
		}
	}

	@Override
	public PresupuestoDetalle findById(Long id) {
		try {
			return this.ifzPresupuestoDetalle.findById(id);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalle> findAll() {
		try {
			return this.ifzPresupuestoDetalle.findAll();
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findAll()", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalle> findByProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzPresupuestoDetalle.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findByProperty(propertyName, value, max)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalle> findLikeProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzPresupuestoDetalle.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		}
	}
	
	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------

	@Override
	public Long save(PresupuestoDetalleExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.PresupuestoDetalleExtToPresupuestoDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.save(entityExt)", e);
			throw e;
		}
	}

	@Override
	public void update(PresupuestoDetalleExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.PresupuestoDetalleExtToPresupuestoDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.update(entityExt)", e);
			throw e;
		}
	}

	@Override
	public PresupuestoDetalleExt findByIdExt(Long id) throws Exception {
		try {
			return this.convertidor.PresupuestoDetalleToPresupuestoDetalleExt(this.findById(id));
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findByIdExt(id)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalleExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception {
		List<PresupuestoDetalleExt> listaExt = new ArrayList<PresupuestoDetalleExt>();
		List<PresupuestoDetalle> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value, max);
			if (lista != null && ! lista.isEmpty()) {
				for (PresupuestoDetalle var : lista)
					listaExt.add(this.convertidor.PresupuestoDetalleToPresupuestoDetalleExt(var));
			}
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<PresupuestoDetalleExt> findLikePropertyExt(String propertyName, Object value, int max) throws Exception {
		List<PresupuestoDetalleExt> listaExt = new ArrayList<PresupuestoDetalleExt>();
		List<PresupuestoDetalle> lista = null;
		
		try {
			lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null && ! lista.isEmpty()) {
				for (PresupuestoDetalle var : lista)
					listaExt.add(this.convertidor.PresupuestoDetalleToPresupuestoDetalleExt(var));
			}
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikePropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	// -------------------------------------------------------------------------------------------
	// PRIVADOS
	// -------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase

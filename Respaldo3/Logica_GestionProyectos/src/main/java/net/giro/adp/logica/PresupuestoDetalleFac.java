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
		Hashtable<String, Object> environtment = null;
		
		try {
			environtment = new Hashtable<String, Object>();
            environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environtment);
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
			return this.ifzPresupuestoDetalle.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalle> saveOrUpdateList(List<PresupuestoDetalle> entities) throws Exception {
		try {
			return this.ifzPresupuestoDetalle.saveOrUpdateList(entities, getCodigoEmpresa());
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
	public void delete(long idPresupuesto) throws Exception {
		try {
			this.ifzPresupuestoDetalle.delete(idPresupuesto);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.delete(idPresupuesto)", e);
			throw e;
		}
	}

	@Override
	public PresupuestoDetalle findById(long idPresupuestoDetalle) {
		try {
			return this.ifzPresupuestoDetalle.findById(idPresupuestoDetalle);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findById(idPresupuestoDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalle> findAll(long idPresupuesto) {
		try {
			return this.findAll(idPresupuesto, "");
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findAll(idPresupuesto)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalle> findAll(long idPresupuesto, String orderBy) {
		try {
			return this.ifzPresupuestoDetalle.findAll(idPresupuesto, orderBy);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findAll(idPresupuesto, orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalle> findByProperty(String propertyName, Object value, int limite) {
		try {
			return this.findByProperty(propertyName, value, "", limite);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalle> findByProperty(String propertyName, Object value, String orderBy, int limite) {
		try {
			return this.ifzPresupuestoDetalle.findByProperty(propertyName, value, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}
	
	@Override
	public List<PresupuestoDetalle> findLikeProperty(String propertyName, Object value, int limite) {
		try {
			return this.findLikeProperty(propertyName, value, "", limite);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalle> findLikeProperty(String propertyName, Object value, String orderBy, int limite) {
		try {
			return this.ifzPresupuestoDetalle.findLikeProperty(propertyName, value, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findLikeProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		}
	}
	
	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------

	@Override
	public Long save(PresupuestoDetalleExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(entityExt));
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.save(entityExt)", e);
			throw e;
		}
	}

	@Override
	public void update(PresupuestoDetalleExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.getPojo(entityExt));
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.update(entityExt)", e);
			throw e;
		}
	}

	@Override
	public PresupuestoDetalleExt findByIdExt(long idPresupuestoDetalle) throws Exception {
		try {
			return this.convertidor.getExtendido(this.findById(idPresupuestoDetalle));
		} catch (Exception e) {
			log.error("Error en PresupuestoDetalleFac.findByIdExt(idPresupuestoDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalleExt> findAllExt(long idPresupuesto) {
		List<PresupuestoDetalleExt> extendidos = new ArrayList<PresupuestoDetalleExt>();
		List<PresupuestoDetalle> entities = null;
		
		try {
			entities = this.findAll(idPresupuesto);
			if (entities == null || entities.isEmpty()) 
				return extendidos;
			for (PresupuestoDetalle entity : entities)
				extendidos.add(this.convertidor.getExtendido(entity));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findAllExt(idPresupuesto)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<PresupuestoDetalleExt> findByPropertyExt(String propertyName, Object value, int limite) throws Exception {
		try {
			return findByPropertyExt(propertyName, value, "", limite);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalleExt> findByPropertyExt(String propertyName, Object value, String orderBy, int limite) throws Exception {
		List<PresupuestoDetalleExt> extendidos = new ArrayList<PresupuestoDetalleExt>();
		List<PresupuestoDetalle> entities = null;
		
		try {
			entities = this.findByProperty(propertyName, value, orderBy, limite);
			if (entities == null || entities.isEmpty()) 
				return extendidos;
			for (PresupuestoDetalle entity : entities)
				extendidos.add(this.convertidor.getExtendido(entity));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<PresupuestoDetalleExt> findLikePropertyExt(String propertyName, Object value, int limite) throws Exception {
		try {
			return findLikePropertyExt(propertyName, value, "", limite);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PresupuestoDetalleExt> findLikePropertyExt(String propertyName, Object value, String orderBy, int limite) throws Exception {
		List<PresupuestoDetalleExt> extendidos = new ArrayList<PresupuestoDetalleExt>();
		List<PresupuestoDetalle> entities = null;
		
		try {
			entities = this.findLikeProperty(propertyName, value, orderBy, limite);
			if (entities == null || entities.isEmpty()) 
				return extendidos;
			for (PresupuestoDetalle entity : entities)
				extendidos.add(this.convertidor.getExtendido(entity));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return extendidos;
	}

	// -------------------------------------------------------------------------------------------
	// PRIVADOS
	// -------------------------------------------------------------------------------------------

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
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

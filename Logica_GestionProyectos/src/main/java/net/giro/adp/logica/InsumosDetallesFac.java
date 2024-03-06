package net.giro.adp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.InsumosDetalles;
import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.adp.dao.InsumosDetallesDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class InsumosDetallesFac implements InsumosDetallesRem {
	private static Logger log = Logger.getLogger(InsumosDetallesFac.class);
	private InitialContext ctx;	
	private InsumosDetallesDAO ifzInsumosDetalles;	
	private ConvertExt convertidor;
	private InfoSesion infoSesion;
	
	public InsumosDetallesFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environment);
            this.ifzInsumosDetalles = (InsumosDetallesDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//InsumosDetallesImp!net.giro.adp.dao.InsumosDetallesDAO");
            this.convertidor = new ConvertExt();
            this.convertidor.setFrom("InsumosDetallesFac");
            this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear InsumosDetallesFac", e);
			ctx = null;
		}
	}
	
	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(InsumosDetalles entity) throws Exception {
		try {
			return this.ifzInsumosDetalles.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<InsumosDetalles> saveOrUpdateList(List<InsumosDetalles> entities) throws Exception {
		try {
			return this.ifzInsumosDetalles.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.InsumosDetallesFac.saveOrUpdateList(List<ObraSatics> entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(InsumosDetalles entity) throws Exception {
		try {
			this.ifzInsumosDetalles.update(entity);
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idInsumosDetalles) throws Exception {
		try {
			this.ifzInsumosDetalles.delete(idInsumosDetalles);
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.delete(idInsumosDetalles)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<InsumosDetalles> findAll(Long idInsumos) {
		try {
			return this.ifzInsumosDetalles.findAll(idInsumos);
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.findAll(idInsumos)", e);
			throw e;
		}
	}

	@Override
	public InsumosDetalles findById(Long idInsumosDetalles) {
		try {
			return this.ifzInsumosDetalles.findById(idInsumosDetalles);
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.findById(idInsumosDetalles)", e);
			throw e;
		}
	}

	@Override
	public List<InsumosDetalles> findByProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzInsumosDetalles.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.findByProperty(propertyName, value, max)", e);
			throw e;
		}
	}

	@Override
	public List<InsumosDetalles> findLikeProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzInsumosDetalles.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		}
	}

	@Override
	public InsumosDetalles convertir(InsumosDetallesExt target) {
		try {
			return this.convertir(target);
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.InsumosFac.convertir(InsumosDetallesExt target)", e);
			throw e;
		}
	}
	
	@Override
	public InsumosDetallesExt convertir(InsumosDetalles target) {
		try {
			return this.convertir(target);
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.InsumosFac.convertir(InsumosDetallesExt target)", e);
			throw e;
		}
	}
	
	// ----------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------------------------------------------
	
	@Override
	public Long save(InsumosDetallesExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(entityExt));
		} catch (Exception e) {
			log.error("Error en InsumosFac.save(entityExt)", e);
			throw e;
		}
	}
	
	@Override
	public List<InsumosDetallesExt> saveOrUpdateListExt(List<InsumosDetallesExt> extendidos) throws Exception {
		List<InsumosDetalles> entities = new ArrayList<InsumosDetalles>();
		
		try {
			if (extendidos == null || extendidos.isEmpty())
				return extendidos;
			for (InsumosDetallesExt extendido : extendidos)
				entities.add(this.convertidor.getPojo(extendido));
			extendidos.clear();
			entities = this.saveOrUpdateList(entities);
			for (InsumosDetalles entity : entities)
				extendidos.add(this.convertidor.getExtendido(entity));
		} catch (Exception e) {
			log.error("Error en InsumosFac.saveOrUpdateListExt(extendidos)", e);
			throw e;
		}
		
		return extendidos;
	}
	
	@Override
	public void update(InsumosDetallesExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.getPojo(entityExt));
		} catch (Exception e) {
			log.error("Error en InsumosFac.update(entityExt)", e);
			throw e;
		}
	}
	
	@Override
	public InsumosDetallesExt findByIdExt(Long idInsumosDetalles) throws Exception {
		try {
			return this.convertidor.getExtendido(this.findById(idInsumosDetalles));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByIdExt(idInsumosDetalles)", e);
			throw e;
		}
	}
	
	@Override
	public List<InsumosDetallesExt> findAllExt(Long idInsumos) throws Exception {
		List<InsumosDetallesExt> listaExt = new ArrayList<InsumosDetallesExt>();
		List<InsumosDetalles> lista = null;
		
		try {
			lista = this.findAll(idInsumos);
			for(InsumosDetalles var : lista)
				listaExt.add(this.convertidor.getExtendido(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<InsumosDetallesExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception {
		List<InsumosDetallesExt> listaExt = new ArrayList<InsumosDetallesExt>();
		
		try {
			List<InsumosDetalles> lista = this.findByProperty(propertyName, value, max);
			for(InsumosDetalles var : lista)
				listaExt.add(this.convertidor.getExtendido(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<InsumosDetallesExt> findLikePropertyExt(String propertyName, Object value, int max) throws Exception {
		List<InsumosDetallesExt> listaExt = new ArrayList<InsumosDetallesExt>();
		
		try {
			List<InsumosDetalles> lista = this.findLikeProperty(propertyName, value, max);
			for(InsumosDetalles var : lista)
				listaExt.add(this.convertidor.getExtendido(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikePropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<InsumosDetallesExt> extenderInsumosDetalles(List<InsumosDetalles> entities) throws Exception {
		List<InsumosDetallesExt> extendidos = new ArrayList<InsumosDetallesExt>();
		
		try {
			for (InsumosDetalles entity : entities)
				extendidos.add(this.convertidor.getExtendido(entity));
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.InsumosFac.extenderInsumosDetalles(lista)", e);
			throw e;
		}
		
		return extendidos;
	}
	
	// ----------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ----------------------------------------------------------------------------------------------------

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Implemento metodos extenderInsumosDetalles y convertir (x2)
 */
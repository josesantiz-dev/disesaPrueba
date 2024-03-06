package net.giro.adp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
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
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            this.ifzInsumosDetalles = (InsumosDetallesDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//InsumosDetallesImp!net.giro.adp.dao.InsumosDetallesDAO");
            
           this.convertidor = new ConvertExt();
           this.convertidor.setFrom("InsumosDetallesFac");
           this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
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
	public Long save(InsumosDetalles entity) throws Exception {
		try {
			return this.ifzInsumosDetalles.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public List<InsumosDetalles> saveOrUpdateList(List<InsumosDetalles> entities) throws Exception {
		try {
			return this.ifzInsumosDetalles.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.InsumosDetallesFac.saveOrUpdateList(List<ObraSatics> entities)", e);
			throw e;
		}
	}

	@Override
	public Long save(InsumosDetallesExt entityExt) throws Exception {
		try {
			InsumosDetalles entity = this.convertidor.InsumosDetallesExtToInsumosDetalles(entityExt);
			return this.save(entity);
		} catch (Exception e) {
			log.error("Error en InsumosFac.save(entityExt)", e);
			throw e;
		}
	}

	@Override
	public void update(InsumosDetalles entity) throws Exception {
		try {
			this.ifzInsumosDetalles.update(entity);
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public void update(InsumosDetallesExt entityExt) throws Exception {
		try {
			InsumosDetalles entity = this.convertidor.InsumosDetallesExtToInsumosDetalles(entityExt);
			this.update(entity);
		} catch (Exception e) {
			log.error("Error en InsumosFac.update(entityExt)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long id) throws Exception {
		try {
			this.ifzInsumosDetalles.delete(id);
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.delete(id)", e);
			throw e;
		}
	}

	@Override
	public List<InsumosDetalles> findAll() {
		try {
			return this.ifzInsumosDetalles.findAll();
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.findAll()", e);
			throw e;
		}
	}

	@Override
	public InsumosDetalles findById(Long id) {
		try {
			return this.ifzInsumosDetalles.findById(id);
		} catch (Exception e) {
			log.error("Error en InsumosDetallesFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public InsumosDetallesExt findByIdExt(Long id) throws Exception {
		try {
			return this.convertidor.InsumosDetallesToInsumosDetallesExt(this.findById(id));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByIdExt(propertyName, value, max)", e);
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
	public List<InsumosDetallesExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception {
		List<InsumosDetallesExt> listaExt = new ArrayList<InsumosDetallesExt>();
		
		try {
			List<InsumosDetalles> lista = this.findByProperty(propertyName, value, max);
			for(InsumosDetalles var : lista)
				listaExt.add(this.convertidor.InsumosDetallesToInsumosDetallesExt(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
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
	public List<InsumosDetallesExt> findLikePropertyExt(String propertyName, Object value, int max) throws Exception {
		List<InsumosDetallesExt> listaExt = new ArrayList<InsumosDetallesExt>();
		
		try {
			List<InsumosDetalles> lista = this.findLikeProperty(propertyName, value, max);
			for(InsumosDetalles var : lista)
				listaExt.add(this.convertidor.InsumosDetallesToInsumosDetallesExt(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikePropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<InsumosDetallesExt> extenderInsumosDetalles(List<InsumosDetalles> lista) throws Exception {
		List<InsumosDetallesExt> listaExt = new ArrayList<InsumosDetallesExt>();
		
		try {
			for(InsumosDetalles var : lista)
				listaExt.add(this.convertidor.InsumosDetallesToInsumosDetallesExt(var));
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.InsumosFac.extenderInsumosDetalles(lista)", e);
			throw e;
		}
		
		return listaExt;
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
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Implemento metodos extenderInsumosDetalles y convertir (x2)
 */
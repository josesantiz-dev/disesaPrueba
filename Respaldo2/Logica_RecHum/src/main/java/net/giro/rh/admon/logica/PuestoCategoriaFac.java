package net.giro.rh.admon.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.PuestoCategoria;
import net.giro.rh.admon.dao.PuestoCategoriaDAO;

@Stateless
public class PuestoCategoriaFac implements PuestoCategoriaRem {
	private static Logger log = Logger.getLogger(PuestoCategoriaFac.class);
	private InitialContext ctx;
	private PuestoCategoriaDAO ifzPuestoCategoria;
	private InfoSesion infoSesion;
	
	public PuestoCategoriaFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            this.ifzPuestoCategoria = (PuestoCategoriaDAO) ctx.lookup("ejb:/Model_RecHum//PuestoCategoriaImp!net.giro.rh.admon.dao.PuestoCategoriaDAO");
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear PuestoCategoriaFac", e);
			ctx = null;
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

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(PuestoCategoria entity) throws Exception {
		try {
			return this.ifzPuestoCategoria.save(entity, null);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<PuestoCategoria> saveOrUpdateList(List<PuestoCategoria> entities) throws Exception {
		try {
			return this.ifzPuestoCategoria.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ifzPuestoCategoria.saveOrUpdateList(List<TT> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(PuestoCategoria entity) throws Exception {
		try {
			this.ifzPuestoCategoria.setEmpresa(getIdEmpresa());
			this.ifzPuestoCategoria.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	} 

	@Override
	public void delete(PuestoCategoria entity) throws Exception {
		try {
			this.ifzPuestoCategoria.setEmpresa(getIdEmpresa());
			this.ifzPuestoCategoria.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public PuestoCategoria findById(Long id) {
		try {
			this.ifzPuestoCategoria.setEmpresa(getIdEmpresa());
			return this.ifzPuestoCategoria.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<PuestoCategoria> findByProperty(String propertyName, final Object value) {
		try {
			this.ifzPuestoCategoria.setEmpresa(getIdEmpresa());
			return this.ifzPuestoCategoria.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PuestoCategoria> findAll() {
		try {
			this.ifzPuestoCategoria.setEmpresa(getIdEmpresa());
			return this.ifzPuestoCategoria.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<PuestoCategoria> findByIdPuesto(long idPuesto) {
		try {
			this.ifzPuestoCategoria.setEmpresa(getIdEmpresa());
			return this.ifzPuestoCategoria.findByIdPuesto(idPuesto);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PuestoCategoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value) {
		try {
			this.ifzPuestoCategoria.setEmpresa(getIdEmpresa());
			return this.ifzPuestoCategoria.findByPropertyPojoCompleto(propertyName, tipo, value);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<PuestoCategoria> findByPuestoCategoria(Long idPuesto, Long idCategoria) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		try {
			params.put("idPuesto", idPuesto);
			params.put("idCategoria", idCategoria);
			return this.findByProperties(params);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.PuestoCategoriaFac.findByPuestoAndCategoria(idPuesto, idCategoria)", e);
			throw e;
		}
	}
	
	@Override
	public List<PuestoCategoria> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			this.ifzPuestoCategoria.setEmpresa(getIdEmpresa());
			return this.ifzPuestoCategoria.findByProperties(params);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.PuestoCategoriaFac.findByProperties(params)", e);
			throw e;
		}
	}
}


//HISTORIAL DE MODIFICACIONES 
//-------------------------------------------------------------------------------------------------------------------
//VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
//-------------------------------------------------------------------------------------------------------------------
//  1.0   | 2016-09-20 | Javier Tirado      | Se agrego el metodo salvar a partir de un pojo extendido

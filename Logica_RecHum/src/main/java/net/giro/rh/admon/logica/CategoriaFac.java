package net.giro.rh.admon.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.Categoria;
import net.giro.rh.admon.dao.CategoriaDAO;

@Stateless
public class CategoriaFac implements CategoriaRem {
	private static Logger log = Logger.getLogger(CategoriaFac.class);
	private InitialContext ctx;
	private CategoriaDAO ifzCategoria;
	private InfoSesion infoSesion;
	
	public CategoriaFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environment);
            this.ifzCategoria = (CategoriaDAO) this.ctx.lookup("ejb:/Model_RecHum//CategoriaImp!net.giro.rh.admon.dao.CategoriaDAO");
        } catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear CategoriaFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(Categoria entity) throws Exception {
		try {
			return this.ifzCategoria.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Categoria> saveOrUpdateList(List<Categoria> entities) throws java.lang.Exception {
		try {
			return this.ifzCategoria.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(Categoria entity) throws Exception {
		try {
			this.ifzCategoria.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(Categoria entity) throws Exception {
		try {
			this.ifzCategoria.delete(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Categoria findById(Long id) {
		try {
			return this.ifzCategoria.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Categoria> findByProperty(String propertyName, final Object value) {
		
		try {
			return this.ifzCategoria.findByProperty(propertyName, value, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Categoria> findAll() {
		try {
			return this.ifzCategoria.findAll(getIdEmpresa());
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Categoria> findAllActivos() {
		try {
			return this.ifzCategoria.findAllActivos(getIdEmpresa());
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Categoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try {
			return this.ifzCategoria.findByPropertyPojoCompleto(propertyName, tipo, value, getIdEmpresa());
		}catch(Exception re){
			throw re;
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}

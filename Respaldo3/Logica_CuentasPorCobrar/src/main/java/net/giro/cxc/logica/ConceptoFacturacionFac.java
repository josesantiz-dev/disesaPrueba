package net.giro.cxc.logica;

import java.util.Hashtable;
import java.util.List;

import net.giro.cxc.beans.ConceptoFacturacion;
import net.giro.cxc.dao.ConceptoFacturacionDAO;
import net.giro.plataforma.InfoSesion;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

@Stateless
public class ConceptoFacturacionFac implements ConceptoFacturacionRem {
	private static Logger log = Logger.getLogger(ConceptoFacturacionFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private ConceptoFacturacionDAO ifzConceptoFacturacion;
	
	public ConceptoFacturacionFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(environment);
			this.ifzConceptoFacturacion = (ConceptoFacturacionDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//ConceptoFacturacionImp!net.giro.cxc.dao.ConceptoFacturacionDAO");
		} catch(Exception e) {
			log.error("Error en el método de contexto");
			this.ctx = null;
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(ConceptoFacturacion entity) throws Exception {
		try {
			return this.ifzConceptoFacturacion.save(entity, getCodigoEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void update(ConceptoFacturacion entity) throws Exception {
		try {
			this.ifzConceptoFacturacion.update(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void delete(ConceptoFacturacion entity) throws Exception {
		try {
			this.ifzConceptoFacturacion.delete(entity.getId());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public ConceptoFacturacion findById(Long idConceptoFacturacion) throws Exception {
		try {
			return this.ifzConceptoFacturacion.findById(idConceptoFacturacion);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findAll() throws Exception {
		try {
			return this.findAll(false, "");
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findAll(boolean incluyeCanceladas, String orderBy) throws Exception {
		try {
			return this.ifzConceptoFacturacion.findAll(incluyeCanceladas, orderBy, getIdEmpresa());
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findLike(String value, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			value = value.trim().replace(" ", "%");
			return this.ifzConceptoFacturacion.findLike(value, incluyeCanceladas, orderBy, getIdEmpresa(), limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, false, "", limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findLikeProperty(String propertyName, String value, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), incluyeCanceladas, orderBy, limite);
			value = value.trim().replace(" ", "%");
			return this.ifzConceptoFacturacion.findLikeProperty(propertyName, value, incluyeCanceladas, orderBy, getIdEmpresa(), limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, false, "", limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			return this.ifzConceptoFacturacion.findByProperty(propertyName, value, incluyeCanceladas, orderBy, getIdEmpresa(), limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findByPropertyPojoCompleto(String propertyName, String tipo, long value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, false, "", 0);
		} catch (Exception re) {
			throw re;
		}
	}
	
	// ------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L);
	}
	
	private Long getCodigoEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}

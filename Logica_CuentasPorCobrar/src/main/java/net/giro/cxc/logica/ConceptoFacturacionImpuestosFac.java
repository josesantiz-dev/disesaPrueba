package net.giro.cxc.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxc.beans.ConceptoFacturacion;
import net.giro.cxc.beans.ConceptoFacturacionImpuestos;
import net.giro.cxc.beans.ConceptoFacturacionImpuestosExt;
import net.giro.cxc.dao.ConceptoFacturacionImpuestosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ConceptoFacturacionImpuestosFac implements ConceptoFacturacionImpuestosRem {
	private static Logger log = Logger.getLogger(ConceptoFacturacionFac.class);
	private InitialContext ctx;
	private ConceptoFacturacionImpuestosDAO ifzConceptoFacturacionImpuestos;
	private InfoSesion infoSesion;
	private ConvertExt convertidor;
	
	public ConceptoFacturacionImpuestosFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(environment);
			this.ifzConceptoFacturacionImpuestos = (ConceptoFacturacionImpuestosDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//ConceptoFacturacionImpuestosImp!net.giro.cxc.dao.ConceptoFacturacionImpuestosDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ConceptoFacturacionImpuestosFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método de contexto");
			ctx = null;
		}
	}

	
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(ConceptoFacturacionImpuestos entity) throws Exception {
		try {
			return this.ifzConceptoFacturacionImpuestos.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConceptoFacturacionImpuestos> saveOrUpdateList(List<ConceptoFacturacionImpuestos> entities) throws Exception {
		try {
			return this.ifzConceptoFacturacionImpuestos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void delete(long idConceptoFacturacionImpuestos) throws Exception {
		try {
			this.ifzConceptoFacturacionImpuestos.delete(idConceptoFacturacionImpuestos);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void update(ConceptoFacturacionImpuestos entity) throws Exception {
		try {
			this.ifzConceptoFacturacionImpuestos.update(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ConceptoFacturacionImpuestos findById(long idConceptoFacturacionImpuestos) {
		try {
			return this.ifzConceptoFacturacionImpuestos.findById(idConceptoFacturacionImpuestos);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConceptoFacturacionImpuestos> findAll(long idConceptoFacturacion) {
		try {
			return this.ifzConceptoFacturacionImpuestos.findAll(idConceptoFacturacion);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConceptoFacturacionImpuestos> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzConceptoFacturacionImpuestos.findByColumnName(propertyName, value);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConceptoFacturacionImpuestos> findLikeProperty(String propertyName, Object value) {
		try {
			return this.ifzConceptoFacturacionImpuestos.findLikeColumnName(propertyName, value.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConceptoFacturacionImpuestos> findByConcepto(ConceptoFacturacion idConcepto) {
		return null;
	}

	@Override
	public ConceptoFacturacionImpuestos convertir(ConceptoFacturacionImpuestosExt extendido) {
		return this.convertidor.ConceptoFacturacionImpuestosExtToConceptoFacturacionImpuestos(extendido);
	}

	@Override
	public ConceptoFacturacionImpuestosExt convertir(ConceptoFacturacionImpuestos entity) {
		return this.convertidor.ConceptoFacturacionImpuestosToConceptoFacturacionImpuestosExt(entity);
	}

	// -----------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------

	@Override
	public long save(ConceptoFacturacionImpuestosExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.ConceptoFacturacionImpuestosExtToConceptoFacturacionImpuestos(extendido));
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void update(ConceptoFacturacionImpuestosExt extendido) throws Exception {
		try {
			this.update(this.convertidor.ConceptoFacturacionImpuestosExtToConceptoFacturacionImpuestos(extendido));
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConceptoFacturacionImpuestosExt> findAllExt(long idConceptoFacturacion) {
		List<ConceptoFacturacionImpuestosExt> extendidos = new ArrayList<ConceptoFacturacionImpuestosExt>();
		List<ConceptoFacturacionImpuestos> entities = null;
		
		try {
			entities = this.findAll(idConceptoFacturacion);
			for (ConceptoFacturacionImpuestos var : entities) 
				extendidos.add(this.convertidor.ConceptoFacturacionImpuestosToConceptoFacturacionImpuestosExt(var));
		} catch (Exception e) {
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<ConceptoFacturacionImpuestosExt> findByPropertyExt(String propertyName, Object value) {
		List<ConceptoFacturacionImpuestosExt> extendidos = new ArrayList<ConceptoFacturacionImpuestosExt>();
		List<ConceptoFacturacionImpuestos> entities = null;
		
		try {
			entities = this.findByProperty(propertyName, value);
			for (ConceptoFacturacionImpuestos var : entities) 
				extendidos.add(this.convertidor.ConceptoFacturacionImpuestosToConceptoFacturacionImpuestosExt(var));
		} catch (Exception e) {
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<ConceptoFacturacionImpuestosExt> findLikePropertyExt(String propertyName, Object value) {
		List<ConceptoFacturacionImpuestosExt> extendidos = new ArrayList<ConceptoFacturacionImpuestosExt>();
		List<ConceptoFacturacionImpuestos> entities = null;
		
		try {
			entities = this.findLikeProperty(propertyName, value);
			for (ConceptoFacturacionImpuestos entity : entities) 
				extendidos.add(this.convertidor.ConceptoFacturacionImpuestosToConceptoFacturacionImpuestosExt(entity));
		} catch (Exception e) {
			throw e;
		}
		
		return extendidos;
	}

	// -----------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------
	
	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();//.getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}

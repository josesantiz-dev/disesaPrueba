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
	
	
	public ConceptoFacturacionImpuestosFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(p);
			
			this.ifzConceptoFacturacionImpuestos = (ConceptoFacturacionImpuestosDAO) ctx.lookup("ejb:/Model_CuentasPorCobrar//ConceptoFacturacionImpuestosImp!net.giro.cxc.dao.ConceptoFacturacionImpuestosDAO");
			
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
			return this.ifzConceptoFacturacionImpuestos.save(entity, getIdEmpresa());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConceptoFacturacionImpuestos> saveOrUpdateList(List<ConceptoFacturacionImpuestos> entities) throws Exception {
		try {
			return this.ifzConceptoFacturacionImpuestos.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void delete(Long id) throws Exception {
		try {
			this.ifzConceptoFacturacionImpuestos.delete(id);
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
	public ConceptoFacturacionImpuestos findById(Long id) {
		try {
			return this.ifzConceptoFacturacionImpuestos.findById(id);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConceptoFacturacionImpuestos> findAll() {
		try {
			return this.ifzConceptoFacturacionImpuestos.findAll();
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
	public ConceptoFacturacionImpuestos convertir(ConceptoFacturacionImpuestosExt target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConceptoFacturacionImpuestosExt convertir(ConceptoFacturacionImpuestos target) {
		// TODO Auto-generated method stub
		return null;
	}

	// -----------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------

	@Override
	public long save(ConceptoFacturacionImpuestosExt entityExt) throws Exception {
		try {
			ConceptoFacturacionImpuestos entity = this.convertidor.ConceptoFacturacionImpuestosExtToConceptoFacturacionImpuestos(entityExt);
			return this.save(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void update(ConceptoFacturacionImpuestosExt entityExt) throws Exception {
		try {
			ConceptoFacturacionImpuestos entity = this.convertidor.ConceptoFacturacionImpuestosExtToConceptoFacturacionImpuestos(entityExt);
			this.update(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConceptoFacturacionImpuestosExt> findByPropertyExt(String propertyName, Object value) {
		List<ConceptoFacturacionImpuestosExt> listaExt = new ArrayList<ConceptoFacturacionImpuestosExt>();
		
		try {
			List<ConceptoFacturacionImpuestos> lista = this.findByProperty(propertyName, value);
			for (ConceptoFacturacionImpuestos var : lista) {
				listaExt.add(this.convertidor.ConceptoFacturacionImpuestosToConceptoFacturacionImpuestosExt(var));
			}
		} catch (Exception e) {
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ConceptoFacturacionImpuestosExt> findLikePropertyExt(String propertyName, Object value) {
		List<ConceptoFacturacionImpuestosExt> listaExt = new ArrayList<ConceptoFacturacionImpuestosExt>();
		
		try {
			List<ConceptoFacturacionImpuestos> lista = this.findLikeProperty(propertyName, value);
			
			for (ConceptoFacturacionImpuestos var : lista) {
				listaExt.add(this.convertidor.ConceptoFacturacionImpuestosToConceptoFacturacionImpuestosExt(var));
			}
		} catch (Exception e) {
			throw e;
		}
		
		return listaExt;
	}

	// -----------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}

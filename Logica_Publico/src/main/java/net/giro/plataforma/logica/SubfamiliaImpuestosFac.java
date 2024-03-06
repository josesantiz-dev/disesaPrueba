package net.giro.plataforma.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.beans.SubfamiliaImpuestos;
import net.giro.plataforma.beans.SubfamiliaImpuestosExt;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.dao.SubfamiliaImpuestosDAO;

@Stateless
public class SubfamiliaImpuestosFac implements SubfamiliaImpuestosRem {
	private static Logger log = Logger.getLogger(SubfamiliaImpuestosFac.class);
	private InitialContext ctx;
	private SubfamiliaImpuestosDAO ifzSubfamiliaImpuestos;
	private ConValoresDAO ifzConValores;
	private InfoSesion infoSesion;

	
	public SubfamiliaImpuestosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzSubfamiliaImpuestos = (SubfamiliaImpuestosDAO) this.ctx.lookup("ejb:/Model_Publico//SubfamiliaImpuestosImp!net.giro.plataforma.dao.SubfamiliaImpuestosDAO");
			this.ifzConValores = (ConValoresDAO) this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Publico.SubfamiliaImpuestosFac", e);
			ctx = null;
		}
	}


	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();//.getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(SubfamiliaImpuestos entity) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.save(SubfamiliaImpuestos entity)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> saveOrUpdateList(List<SubfamiliaImpuestos> entities) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.saveOrUpdateList(List<SubfamiliaImpuestos> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(SubfamiliaImpuestos entity) throws ExcepConstraint {
		try {
			this.ifzSubfamiliaImpuestos.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.update(SubfamiliaImpuestos)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzSubfamiliaImpuestos.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public SubfamiliaImpuestos findById(Long id) {
		try {
			return this.ifzSubfamiliaImpuestos.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findAll() throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findAll();
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.findAll()", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findByProperty(String propertyName, Object value) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findByProperty(propertyName, value, 0);
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.findByProperty(propertyName, value)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findByProperties(params, 0);
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.findByProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findLikeProperty(String propertyName, String value) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findLikeProperty(propertyName, value, 0);
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.findLikeProperty(propertyName, value)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findLikeProperties(HashMap<String, String> params) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findLikeProperties(params, 0);
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.findLikeProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestos> findInProperty(String columnName, List<Object> values) throws Exception {
		try {
			return this.ifzSubfamiliaImpuestos.findInProperty(columnName, values, 0);
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.findInProperty(columnName, values)", e);
			throw e;
		}
	}

	@Override
	public SubfamiliaImpuestos convertir(SubfamiliaImpuestosExt entityExt) throws Exception {
		SubfamiliaImpuestos entity = null;
		
		try {
			entity = new SubfamiliaImpuestos();
			entity.setId(entityExt.getId());
			entity.setDescSubfamilia(entityExt.getDescSubfamilia());
			entity.setDescImpuesto(entityExt.getDescImpuesto());
			entity.setPorcentaje(entityExt.getPorcentaje());
			entity.setValor(entityExt.getValor()); 
			entity.setAplicaEn(entityExt.getAplicaEn());
			entity.setCreadoPor(entityExt.getCreadoPor());
			entity.setFechaCreacion(entityExt.getFechaCreacion());
			entity.setModificadoPor(entityExt.getModificadoPor());
			entity.setFechaModificacion(entityExt.getFechaModificacion());
			
			if (entityExt.getIdSubfamilia() != null && entityExt.getIdSubfamilia().getId() > 0L)
				entity.setIdSubfamilia(entityExt.getIdSubfamilia().getId());

			if (entityExt.getIdImpuesto() != null && entityExt.getIdImpuesto().getId() > 0L)
				entity.setIdImpuesto(entityExt.getIdImpuesto().getId());
			
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.convertir(SubfamiliaImpuestosExt entityExt)", e);
			throw e;
		}
	}

	@Override
	public SubfamiliaImpuestosExt convertir(SubfamiliaImpuestos entity) throws Exception {
		SubfamiliaImpuestosExt entityExt = null;
		
		try {
			entityExt = new SubfamiliaImpuestosExt();
			entityExt.setId(entity.getId());
			entityExt.setDescSubfamilia(entity.getDescSubfamilia());
			entityExt.setDescImpuesto(entity.getDescImpuesto());
			entityExt.setPorcentaje(entity.getPorcentaje());
			entityExt.setValor(entity.getValor()); 
			entityExt.setAplicaEn(entity.getAplicaEn());
			entityExt.setCreadoPor(entity.getCreadoPor());
			entityExt.setFechaCreacion(entity.getFechaCreacion());
			entityExt.setModificadoPor(entity.getModificadoPor());
			entityExt.setFechaModificacion(entity.getFechaModificacion());

			if (entity.getIdSubfamilia() != null && entity.getIdSubfamilia() > 0L) {
				ConValores aux = this.ifzConValores.findById(entity.getIdSubfamilia());
				if (aux == null)
					aux = new ConValores();
				entityExt.setIdSubfamilia(aux);
			}

			if (entity.getIdImpuesto() != null && entity.getIdImpuesto() > 0L) {
				ConValores aux = this.ifzConValores.findById(entity.getIdImpuesto());
				if (aux == null)
					aux = new ConValores();
				entityExt.setIdImpuesto(aux);
			}
			
			return entityExt;
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.convertir(SubfamiliaImpuestos entity)", e);
			throw e;
		}
	}

	// ---------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------------------------------

	@Override
	public SubfamiliaImpuestosExt findExtById(Long id) throws Exception {
		try {
			return this.convertir(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.findExtById(Long id)", e);
			throw e;
		}
	}

	@Override
	public List<SubfamiliaImpuestosExt> findExtByProperty(String propertyName, Object value) throws Exception {
		List<SubfamiliaImpuestosExt> extendidos = new ArrayList<SubfamiliaImpuestosExt>();
		List<SubfamiliaImpuestos> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value);
			if (lista != null && ! lista.isEmpty()) {
				for (SubfamiliaImpuestos var : lista)
					extendidos.add(this.convertir(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.findExtByProperty(String propertyName, Object value)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<SubfamiliaImpuestosExt> findExtLikeProperty(String propertyName, String value) throws Exception {
		List<SubfamiliaImpuestosExt> extendidos = new ArrayList<SubfamiliaImpuestosExt>();
		List<SubfamiliaImpuestos> lista = null;
		
		try {
			lista = this.findLikeProperty(propertyName, value);
			if (lista != null && ! lista.isEmpty()) {
				for (SubfamiliaImpuestos var : lista)
					extendidos.add(this.convertir(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.SubfamiliaImpuestosFac.findExtLikeProperty(String propertyName, Object value)", e);
			throw e;
		}
		
		return extendidos;
	}
}

package net.giro.contabilidad.logica;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.Conceptos;
import net.giro.contabilidad.dao.ConceptosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ConceptosFac implements ConceptosRem {
	private static Logger log = Logger.getLogger(ConceptosFac.class);
	private InfoSesion infoSesion;
	InitialContext ctx;

	private ConceptosDAO ifzConceptoss;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public ConceptosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzConceptoss = (ConceptosDAO) this.ctx.lookup("ejb:/Model_Contabilidad//ConceptosImp!net.giro.contabilidad.dao.ConceptosDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ConceptosFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.ConceptosFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		//this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void orderBy(String orderBy) {
		ConceptosFac.orderBy = orderBy;
	}

	@Override
	public Long save(Conceptos entity) throws ExcepConstraint {
		try {
			return this.ifzConceptoss.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.save(Conceptos)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(ConceptosExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.ConceptosExtToConceptos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.save(ConceptosExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(Conceptos entity) throws ExcepConstraint {
		try {
			this.ifzConceptoss.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.update(Conceptos)", e);
			throw e;
		}
	}

	/*@Override
	public void update(ConceptosExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.ConceptosExtToConceptos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.update(ConceptosExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzConceptoss.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.delete(Long)", e);
			throw e;
		}
	}
	
	@Override
	public Conceptos findById(Long id) {
		try {
			return this.ifzConceptoss.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public ConceptosExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.ConceptosToConceptosExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<Conceptos> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<ConceptosExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<ConceptosExt> listaExt = new ArrayList<ConceptosExt>();
		
		try {
			List<Conceptos> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(Conceptos var : lista) {
					listaExt.add(this.convertidor.ConceptosToConceptosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Conceptos> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Conceptos> findLikeProperty(String propertyName, Object value, Integer limite) throws Exception {
		try {
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
	
	/*@Override
	public List<ConceptosExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<ConceptosExt> listaExt = new ArrayList<ConceptosExt>();
		
		try {
			List<Conceptos> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(Conceptos var : lista) {
					listaExt.add(this.convertidor.ConceptosToConceptosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Conceptos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<ConceptosExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<ConceptosExt> listaExt = new ArrayList<ConceptosExt>();
		
		try {
			List<Conceptos> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(Conceptos var : lista) {
					listaExt.add(this.convertidor.ConceptosToConceptosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Conceptos> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<ConceptosExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ConceptosExt> listaExt = new ArrayList<ConceptosExt>();
		
		try {
			List<Conceptos> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(Conceptos var : lista) {
					listaExt.add(this.convertidor.ConceptosToConceptosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Conceptos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
	
	/*@Override
	public List<ConceptosExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ConceptosExt> listaExt = new ArrayList<ConceptosExt>();
		
		try {
			List<Conceptos> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(Conceptos var : lista) {
					listaExt.add(this.convertidor.ConceptosToConceptosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public Conceptos cancelar(Conceptos entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.ifzConceptoss.update(entity);
			
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.cancelar(entity)", e);
			throw e;
		}
	}

	
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| DD/MM/YYYY | Javier Tirado	| Creacion de ConceptosFac
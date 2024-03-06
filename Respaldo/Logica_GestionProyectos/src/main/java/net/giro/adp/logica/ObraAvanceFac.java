package net.giro.adp.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraAvance;
import net.giro.adp.dao.ObraAvanceDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraAvanceFac implements ObraAvanceRem {
	private static Logger log = Logger.getLogger(ObraAvanceFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private ObraAvanceDAO ifzObraAvance;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public ObraAvanceFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzObraAvance = (ObraAvanceDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraAvanceImp!net.giro.adp.dao.ObraAvanceDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ObraAvanceFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraAvanceFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { /*this.convertidor.setMostrarSystemOut(value);*/ }

	@Override
	public void orderBy(String orderBy) { ObraAvanceFac.orderBy = orderBy; }

	@Override
	public Long save(ObraAvance entity) throws ExcepConstraint {
		try {
			return this.ifzObraAvance.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.save(ObraAvance)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(ObraAvanceExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.ObraAvanceExtToObraAvance(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.save(ObraAvanceExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(ObraAvance entity) throws ExcepConstraint {
		try {
			this.ifzObraAvance.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.update(ObraAvance)", e);
			throw e;
		}
	}

	/*@Override
	public void update(ObraAvanceExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.ObraAvanceExtToObraAvance(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.update(ObraAvanceExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzObraAvance.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public ObraAvance findById(Long id) {
		try {
			return this.ifzObraAvance.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public ObraAvanceExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.ObraAvanceToObraAvanceExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<ObraAvance> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraAvance.orderBy(orderBy);
			return this.ifzObraAvance.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraAvanceExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<ObraAvanceExt> listaExt = new ArrayList<ObraAvanceExt>();
		
		try {
			List<ObraAvance> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(ObraAvance var : lista) {
					listaExt.add(this.convertidor.ObraAvanceToObraAvanceExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraAvance> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraAvance.orderBy(orderBy);
			return this.ifzObraAvance.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraAvanceExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<ObraAvanceExt> listaExt = new ArrayList<ObraAvanceExt>();
		
		try {
			List<ObraAvance> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(ObraAvance var : lista) {
					listaExt.add(this.convertidor.ObraAvanceToObraAvanceExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraAvance> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzObraAvance.orderBy(orderBy);
			return this.ifzObraAvance.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraAvanceExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<ObraAvanceExt> listaExt = new ArrayList<ObraAvanceExt>();
		
		try {
			List<ObraAvance> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(ObraAvance var : lista) {
					listaExt.add(this.convertidor.ObraAvanceToObraAvanceExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraAvance> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzObraAvance.orderBy(orderBy);
			return this.ifzObraAvance.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraAvanceExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<ObraAvanceExt> listaExt = new ArrayList<ObraAvanceExt>();
		
		try {
			List<ObraAvance> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(ObraAvance var : lista) {
					listaExt.add(this.convertidor.ObraAvanceToObraAvanceExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraAvance> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraAvance.orderBy(orderBy);
			return this.ifzObraAvance.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraAvanceExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ObraAvanceExt> listaExt = new ArrayList<ObraAvanceExt>();
		
		try {
			List<ObraAvance> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(ObraAvance var : lista) {
					listaExt.add(this.convertidor.ObraAvanceToObraAvanceExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	/*@Override
	public ObraAvance cancelar(ObraAvance entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.update(entity);
			
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.cancelar(entity)", e);
			throw e;
		}
	}*/

	/*@Override
	public void cancelar(ObraAvanceExt entityExt) throws ExcepConstraint {
		try {
			entityExt.setEstatus(1);
			entityExt.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
			entityExt.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.update(entityExt);
			
			return entityExt;
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.cancelar(entityExt)", e);
			throw e;
		}
	}*/
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 14/06/2016 | Javier Tirado	| Creacion de ObraAvanceFac
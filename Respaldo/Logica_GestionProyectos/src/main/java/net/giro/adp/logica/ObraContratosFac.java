package net.giro.adp.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraContratos;
import net.giro.adp.dao.ObraContratosDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraContratosFac implements ObraContratosRem {
	private static Logger log = Logger.getLogger(ObraContratosFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private ObraContratosDAO ifzObraContratos;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public ObraContratosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzObraContratos = (ObraContratosDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraContratosImp!net.giro.adp.dao.ObraContratosDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ObraContratosFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraContratosFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { /*this.convertidor.setMostrarSystemOut(value);*/ }

	@Override
	public void orderBy(String orderBy) { ObraContratosFac.orderBy = orderBy; }

	@Override
	public Long save(ObraContratos entity) throws ExcepConstraint {
		try {
			return this.ifzObraContratos.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.save(ObraContratos)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(ObraContratosExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.ObraContratosExtToObraContratos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.save(ObraContratosExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(ObraContratos entity) throws ExcepConstraint {
		try {
			this.ifzObraContratos.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.update(ObraContratos)", e);
			throw e;
		}
	}

	/*@Override
	public void update(ObraContratosExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.ObraContratosExtToObraContratos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.update(ObraContratosExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzObraContratos.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public ObraContratos findById(Long id) {
		try {
			return this.ifzObraContratos.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public ObraContratosExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.ObraContratosToObraContratosExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<ObraContratos> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraContratos.orderBy(orderBy);
			return this.ifzObraContratos.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraContratosExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<ObraContratosExt> listaExt = new ArrayList<ObraContratosExt>();
		
		try {
			List<ObraContratos> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(ObraContratos var : lista) {
					listaExt.add(this.convertidor.ObraContratosToObraContratosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraContratos> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraContratos.orderBy(orderBy);
			return this.ifzObraContratos.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraContratosExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<ObraContratosExt> listaExt = new ArrayList<ObraContratosExt>();
		
		try {
			List<ObraContratos> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(ObraContratos var : lista) {
					listaExt.add(this.convertidor.ObraContratosToObraContratosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraContratos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzObraContratos.orderBy(orderBy);
			return this.ifzObraContratos.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraContratosExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<ObraContratosExt> listaExt = new ArrayList<ObraContratosExt>();
		
		try {
			List<ObraContratos> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(ObraContratos var : lista) {
					listaExt.add(this.convertidor.ObraContratosToObraContratosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraContratos> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzObraContratos.orderBy(orderBy);
			return this.ifzObraContratos.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraContratosExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<ObraContratosExt> listaExt = new ArrayList<ObraContratosExt>();
		
		try {
			List<ObraContratos> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(ObraContratos var : lista) {
					listaExt.add(this.convertidor.ObraContratosToObraContratosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraContratos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraContratos.orderBy(orderBy);
			return this.ifzObraContratos.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraContratosExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ObraContratosExt> listaExt = new ArrayList<ObraContratosExt>();
		
		try {
			List<ObraContratos> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(ObraContratos var : lista) {
					listaExt.add(this.convertidor.ObraContratosToObraContratosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	/*@Override
	public ObraContratos cancelar(ObraContratos entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.update(entity);
			
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.cancelar(entity)", e);
			throw e;
		}
	}*/

	/*@Override
	public void cancelar(ObraContratosExt entityExt) throws ExcepConstraint {
		try {
			entityExt.setEstatus(1);
			entityExt.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
			entityExt.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.update(entityExt);
			
			return entityExt;
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.cancelar(entityExt)", e);
			throw e;
		}
	}*/
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 15/06/2016 | Javier Tirado	| Creacion de ObraContratosFac
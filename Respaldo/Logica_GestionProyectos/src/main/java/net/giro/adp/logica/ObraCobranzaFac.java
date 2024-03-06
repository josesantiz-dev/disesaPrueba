package net.giro.adp.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraCobranza;
import net.giro.adp.dao.ObraCobranzaDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraCobranzaFac implements ObraCobranzaRem {
	private static Logger log = Logger.getLogger(ObraCobranzaFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private ObraCobranzaDAO ifzObraCobranzas;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public ObraCobranzaFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzObraCobranzas = (ObraCobranzaDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraCobranzaImp!net.giro.adp.dao.ObraCobranzaDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ObraCobranzaFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraCobranzaFac", e);
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
		ObraCobranzaFac.orderBy = orderBy;
	}

	@Override
	public Long save(ObraCobranza entity) throws ExcepConstraint {
		try {
			return this.ifzObraCobranzas.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.save(ObraCobranza)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(ObraCobranzaExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.ObraCobranzaExtToObraCobranza(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.save(ObraCobranzaExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(ObraCobranza entity) throws ExcepConstraint {
		try {
			this.ifzObraCobranzas.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.update(ObraCobranza)", e);
			throw e;
		}
	}

	/*@Override
	public void update(ObraCobranzaExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.ObraCobranzaExtToObraCobranza(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.update(ObraCobranzaExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzObraCobranzas.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public ObraCobranza findById(Long id) {
		try {
			return this.ifzObraCobranzas.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public ObraCobranzaExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.ObraCobranzaToObraCobranzaExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<ObraCobranza> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraCobranzas.orderBy(orderBy);
			return this.ifzObraCobranzas.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraCobranzaExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<ObraCobranzaExt> listaExt = new ArrayList<ObraCobranzaExt>();
		
		try {
			List<ObraCobranza> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(ObraCobranza var : lista) {
					listaExt.add(this.convertidor.ObraCobranzaToObraCobranzaExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraCobranza> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraCobranzas.orderBy(orderBy);
			return this.ifzObraCobranzas.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraCobranzaExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<ObraCobranzaExt> listaExt = new ArrayList<ObraCobranzaExt>();
		
		try {
			List<ObraCobranza> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(ObraCobranza var : lista) {
					listaExt.add(this.convertidor.ObraCobranzaToObraCobranzaExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraCobranza> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzObraCobranzas.orderBy(orderBy);
			return this.ifzObraCobranzas.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraCobranzaExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<ObraCobranzaExt> listaExt = new ArrayList<ObraCobranzaExt>();
		
		try {
			List<ObraCobranza> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(ObraCobranza var : lista) {
					listaExt.add(this.convertidor.ObraCobranzaToObraCobranzaExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraCobranza> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraCobranzas.orderBy(orderBy);
			return this.ifzObraCobranzas.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraCobranzaExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ObraCobranzaExt> listaExt = new ArrayList<ObraCobranzaExt>();
		
		try {
			List<ObraCobranza> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(ObraCobranza var : lista) {
					listaExt.add(this.convertidor.ObraCobranzaToObraCobranzaExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraCobranza> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraCobranzas.orderBy(orderBy);
			return this.ifzObraCobranzas.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraCobranzaExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ObraCobranzaExt> listaExt = new ArrayList<ObraCobranzaExt>();
		
		try {
			List<ObraCobranza> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(ObraCobranza var : lista) {
					listaExt.add(this.convertidor.ObraCobranzaToObraCobranzaExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraCobranza> findCobranza(List<Object> facturasActuales, long idObra, int limite) throws Exception {
		return null;
	}

	@Override
	public ObraCobranza comprobarConcepto(Long idObra, Long idFactura, Long idConcepto) throws Exception {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("idObra.id", idObra.toString());
			params.put("idFactura", idFactura.toString());
			params.put("idConcepto", idConcepto.toString());
			
			List<ObraCobranza> lista = this.findByProperties(params, 0);
			if (lista == null || lista.isEmpty())
				return null;
			
			return lista.get(0);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.comprobarConcepto(idObra, idFactura, idConcepto)", e);
			throw e;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 24/05/2016 | Javier Tirado	| Creacion de ObraCobranzaFac
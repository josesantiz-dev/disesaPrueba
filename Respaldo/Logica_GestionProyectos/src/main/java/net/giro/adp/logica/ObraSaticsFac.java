package net.giro.adp.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraSatics;
import net.giro.adp.dao.ObraSaticsDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraSaticsFac implements ObraSaticsRem {
	private static Logger log = Logger.getLogger(ObraSaticsFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private ObraSaticsDAO ifzObraSatics;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public ObraSaticsFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzObraSatics = (ObraSaticsDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraSaticsImp!net.giro.adp.dao.ObraSaticsDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ObraSaticsFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraSaticsFac", e);
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
		ObraSaticsFac.orderBy = orderBy;
	}

	@Override
	public Long save(ObraSatics entity) throws ExcepConstraint {
		try {
			return this.ifzObraSatics.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.save(ObraSatics)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(ObraSaticsExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.ObraSaticsExtToObraSatics(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.save(ObraSaticsExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(ObraSatics entity) throws ExcepConstraint {
		try {
			this.ifzObraSatics.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.update(ObraSatics)", e);
			throw e;
		}
	}

	/*@Override
	public void update(ObraSaticsExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.ObraSaticsExtToObraSatics(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.update(ObraSaticsExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzObraSatics.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public ObraSatics findById(Long id) {
		try {
			return this.ifzObraSatics.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public ObraSaticsExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.ObraSaticsToObraSaticsExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<ObraSatics> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraSatics.orderBy(orderBy);
			return this.ifzObraSatics.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraSaticsExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<ObraSaticsExt> listaExt = new ArrayList<ObraSaticsExt>();
		
		try {
			List<ObraSatics> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(ObraSatics var : lista) {
					listaExt.add(this.convertidor.ObraSaticsToObraSaticsExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraSatics> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraSatics.orderBy(orderBy);
			return this.ifzObraSatics.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraSaticsExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<ObraSaticsExt> listaExt = new ArrayList<ObraSaticsExt>();
		
		try {
			List<ObraSatics> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(ObraSatics var : lista) {
					listaExt.add(this.convertidor.ObraSaticsToObraSaticsExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraSatics> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzObraSatics.orderBy(orderBy);
			return this.ifzObraSatics.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraSaticsExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<ObraSaticsExt> listaExt = new ArrayList<ObraSaticsExt>();
		
		try {
			List<ObraSatics> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(ObraSatics var : lista) {
					listaExt.add(this.convertidor.ObraSaticsToObraSaticsExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraSatics> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraSatics.orderBy(orderBy);
			return this.ifzObraSatics.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraSaticsExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ObraSaticsExt> listaExt = new ArrayList<ObraSaticsExt>();
		
		try {
			List<ObraSatics> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(ObraSatics var : lista) {
					listaExt.add(this.convertidor.ObraSaticsToObraSaticsExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<ObraSatics> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraSatics.orderBy(orderBy);
			return this.ifzObraSatics.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	/*@Override
	public List<ObraSaticsExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ObraSaticsExt> listaExt = new ArrayList<ObraSaticsExt>();
		
		try {
			List<ObraSatics> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(ObraSatics var : lista) {
					listaExt.add(this.convertidor.ObraSaticsToObraSaticsExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|23/05/2016		|Javier Tirado	|Creando el facade ObraSaticsFac
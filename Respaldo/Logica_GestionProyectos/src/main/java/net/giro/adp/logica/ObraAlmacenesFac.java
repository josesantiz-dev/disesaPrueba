package net.giro.adp.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.ObraAlmacenesExt;
import net.giro.adp.dao.ObraAlmacenesDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraAlmacenesFac implements ObraAlmacenesRem {
	private static Logger log = Logger.getLogger(ObraAlmacenesFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private ObraAlmacenesDAO ifzObraAlmacenes;
	private ConvertExt convertidor;
	private static String orderBy;
	
	public ObraAlmacenesFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzObraAlmacenes = (ObraAlmacenesDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraAlmacenesImp!net.giro.adp.dao.ObraAlmacenesDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ObraAlmacenesFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraAlmacenesFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void orderBy(String orderBy) {
		ObraAlmacenesFac.orderBy = orderBy;
	}

	@Override
	public Long save(ObraAlmacenes entity) throws ExcepConstraint {
		try {
			return this.ifzObraAlmacenes.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.save(ObraAlmacenes)", e);
			throw e;
		}
	}

	@Override
	public Long save(ObraAlmacenesExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.ObraAlmacenesExtToObraAlmacenes(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.save(ObraAlmacenesExt)", e);
			throw e;
		}
	}

	@Override
	public void update(ObraAlmacenes entity) throws ExcepConstraint {
		try {
			this.ifzObraAlmacenes.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.update(ObraAlmacenes)", e);
			throw e;
		}
	}

	@Override
	public void update(ObraAlmacenesExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.ObraAlmacenesExtToObraAlmacenes(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.update(ObraAlmacenesExt)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzObraAlmacenes.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public ObraAlmacenes findById(Long id) {
		try {
			return this.ifzObraAlmacenes.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public ObraAlmacenesExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.ObraAlmacenesToObraAlmacenesExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<ObraAlmacenes> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			if (orderBy != null && !"".equals(orderBy))
				this.ifzObraAlmacenes.orderBy(orderBy);
			
			return this.ifzObraAlmacenes.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findByProperty(propertyName, value, max)", e);
			throw e;
		}
	}

	@Override
	public List<ObraAlmacenesExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<ObraAlmacenesExt> listaExt = new ArrayList<ObraAlmacenesExt>();
		
		try {
			List<ObraAlmacenes> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(ObraAlmacenes var : lista) {
					listaExt.add(this.convertidor.ObraAlmacenesToObraAlmacenesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ObraAlmacenes> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraAlmacenes.orderBy(orderBy);
			return this.ifzObraAlmacenes.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ObraAlmacenesExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<ObraAlmacenesExt> listaExt = new ArrayList<ObraAlmacenesExt>();
		
		try {
			List<ObraAlmacenes> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(ObraAlmacenes var : lista) {
					listaExt.add(this.convertidor.ObraAlmacenesToObraAlmacenesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ObraAlmacenes> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzObraAlmacenes.orderBy(orderBy);
			return this.ifzObraAlmacenes.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ObraAlmacenesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<ObraAlmacenesExt> listaExt = new ArrayList<ObraAlmacenesExt>();
		
		try {
			List<ObraAlmacenes> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(ObraAlmacenes var : lista) {
					listaExt.add(this.convertidor.ObraAlmacenesToObraAlmacenesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ObraAlmacenes> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraAlmacenes.orderBy(orderBy);
			return this.ifzObraAlmacenes.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ObraAlmacenesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ObraAlmacenesExt> listaExt = new ArrayList<ObraAlmacenesExt>();
		
		try {
			List<ObraAlmacenes> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(ObraAlmacenes var : lista) {
					listaExt.add(this.convertidor.ObraAlmacenesToObraAlmacenesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ObraAlmacenes> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraAlmacenes.orderBy(orderBy);
			return this.ifzObraAlmacenes.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ObraAlmacenesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ObraAlmacenesExt> listaExt = new ArrayList<ObraAlmacenesExt>();
		
		try {
			List<ObraAlmacenes> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(ObraAlmacenes var : lista) {
					listaExt.add(this.convertidor.ObraAlmacenesToObraAlmacenesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAlmacenesFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|19/05/2016		|Javier Tirado	|Creando el facade ObraAlmacenesFac
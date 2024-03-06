package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.PolizasDetalles;
import net.giro.contabilidad.dao.PolizasDetallesDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class PolizasDetallesFac implements PolizasDetallesRem {
	private static Logger log = Logger.getLogger(PolizasDetallesFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private PolizasDetallesDAO ifzPolizasDetalless;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public PolizasDetallesFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzPolizasDetalless = (PolizasDetallesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//PolizasDetallesImp!net.giro.contabilidad.dao.PolizasDetallesDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("PolizasDetallesFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.PolizasDetallesFac", e);
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
		PolizasDetallesFac.orderBy = orderBy;
	}

	@Override
	public Long save(PolizasDetalles entity) throws ExcepConstraint {
		try {
			return this.ifzPolizasDetalless.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.save(PolizasDetalles)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(PolizasDetallesExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.PolizasDetallesExtToPolizasDetalles(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.save(PolizasDetallesExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(PolizasDetalles entity) throws ExcepConstraint {
		try {
			this.ifzPolizasDetalless.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.update(PolizasDetalles)", e);
			throw e;
		}
	}

	/*@Override
	public void update(PolizasDetallesExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.PolizasDetallesExtToPolizasDetalles(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.update(PolizasDetallesExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzPolizasDetalless.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public PolizasDetalles findById(Long id) {
		try {
			return this.ifzPolizasDetalless.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public PolizasDetallesExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.PolizasDetallesToPolizasDetallesExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<PolizasDetalles> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzPolizasDetalless.orderBy(orderBy);
			return this.ifzPolizasDetalless.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<PolizasDetallesExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<PolizasDetallesExt> listaExt = new ArrayList<PolizasDetallesExt>();
		
		try {
			List<PolizasDetalles> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(PolizasDetalles var : lista) {
					listaExt.add(this.convertidor.PolizasDetallesToPolizasDetallesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<PolizasDetalles> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzPolizasDetalless.orderBy(orderBy);
			return this.ifzPolizasDetalless.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<PolizasDetallesExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<PolizasDetallesExt> listaExt = new ArrayList<PolizasDetallesExt>();
		
		try {
			List<PolizasDetalles> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(PolizasDetalles var : lista) {
					listaExt.add(this.convertidor.PolizasDetallesToPolizasDetallesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<PolizasDetalles> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzPolizasDetalless.orderBy(orderBy);
			return this.ifzPolizasDetalless.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<PolizasDetallesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<PolizasDetallesExt> listaExt = new ArrayList<PolizasDetallesExt>();
		
		try {
			List<PolizasDetalles> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(PolizasDetalles var : lista) {
					listaExt.add(this.convertidor.PolizasDetallesToPolizasDetallesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<PolizasDetalles> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzPolizasDetalless.orderBy(orderBy);
			return this.ifzPolizasDetalless.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<PolizasDetallesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<PolizasDetallesExt> listaExt = new ArrayList<PolizasDetallesExt>();
		
		try {
			List<PolizasDetalles> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(PolizasDetalles var : lista) {
					listaExt.add(this.convertidor.PolizasDetallesToPolizasDetallesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<PolizasDetalles> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzPolizasDetalless.orderBy(orderBy);
			return this.ifzPolizasDetalless.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<PolizasDetallesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<PolizasDetallesExt> listaExt = new ArrayList<PolizasDetallesExt>();
		
		try {
			List<PolizasDetalles> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(PolizasDetalles var : lista) {
					listaExt.add(this.convertidor.PolizasDetallesToPolizasDetallesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
}
package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;
import net.giro.contabilidad.dao.OperacionesIntegradasTransaccionesDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class OperacionesIntegradasTransaccionesFac implements OperacionesIntegradasTransaccionesRem {
	private static Logger log = Logger.getLogger(OperacionesIntegradasTransaccionesFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private OperacionesIntegradasTransaccionesDAO ifzOperacionesIntegradasTransaccioness;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public OperacionesIntegradasTransaccionesFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzOperacionesIntegradasTransaccioness = (OperacionesIntegradasTransaccionesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//OperacionesIntegradasTransaccionesImp!net.giro.contabilidad.dao.OperacionesIntegradasTransaccionesDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("OperacionesIntegradasTransaccionesFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.OperacionesIntegradasTransaccionesFac", e);
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
		OperacionesIntegradasTransaccionesFac.orderBy = orderBy;
	}

	@Override
	public Long save(OperacionesIntegradasTransacciones entity) throws ExcepConstraint {
		try {
			return this.ifzOperacionesIntegradasTransaccioness.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.save(OperacionesIntegradasTransacciones)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(OperacionesIntegradasTransaccionesExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.OperacionesIntegradasTransaccionesExtToOperacionesIntegradasTransacciones(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.save(OperacionesIntegradasTransaccionesExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(OperacionesIntegradasTransacciones entity) throws ExcepConstraint {
		try {
			this.ifzOperacionesIntegradasTransaccioness.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.update(OperacionesIntegradasTransacciones)", e);
			throw e;
		}
	}

	/*@Override
	public void update(OperacionesIntegradasTransaccionesExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.OperacionesIntegradasTransaccionesExtToOperacionesIntegradasTransacciones(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.update(OperacionesIntegradasTransaccionesExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzOperacionesIntegradasTransaccioness.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasTransacciones findById(Long id) {
		try {
			return this.ifzOperacionesIntegradasTransaccioness.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public OperacionesIntegradasTransaccionesExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.OperacionesIntegradasTransaccionesToOperacionesIntegradasTransaccionesExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<OperacionesIntegradasTransacciones> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOperacionesIntegradasTransaccioness.orderBy(orderBy);
			return this.ifzOperacionesIntegradasTransaccioness.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<OperacionesIntegradasTransaccionesExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<OperacionesIntegradasTransaccionesExt> listaExt = new ArrayList<OperacionesIntegradasTransaccionesExt>();
		
		try {
			List<OperacionesIntegradasTransacciones> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(OperacionesIntegradasTransacciones var : lista) {
					listaExt.add(this.convertidor.OperacionesIntegradasTransaccionesToOperacionesIntegradasTransaccionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasTransacciones> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOperacionesIntegradasTransaccioness.orderBy(orderBy);
			return this.ifzOperacionesIntegradasTransaccioness.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<OperacionesIntegradasTransaccionesExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<OperacionesIntegradasTransaccionesExt> listaExt = new ArrayList<OperacionesIntegradasTransaccionesExt>();
		
		try {
			List<OperacionesIntegradasTransacciones> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(OperacionesIntegradasTransacciones var : lista) {
					listaExt.add(this.convertidor.OperacionesIntegradasTransaccionesToOperacionesIntegradasTransaccionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasTransacciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzOperacionesIntegradasTransaccioness.orderBy(orderBy);
			return this.ifzOperacionesIntegradasTransaccioness.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<OperacionesIntegradasTransaccionesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<OperacionesIntegradasTransaccionesExt> listaExt = new ArrayList<OperacionesIntegradasTransaccionesExt>();
		
		try {
			List<OperacionesIntegradasTransacciones> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(OperacionesIntegradasTransacciones var : lista) {
					listaExt.add(this.convertidor.OperacionesIntegradasTransaccionesToOperacionesIntegradasTransaccionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasTransacciones> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOperacionesIntegradasTransaccioness.orderBy(orderBy);
			return this.ifzOperacionesIntegradasTransaccioness.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<OperacionesIntegradasTransaccionesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<OperacionesIntegradasTransaccionesExt> listaExt = new ArrayList<OperacionesIntegradasTransaccionesExt>();
		
		try {
			List<OperacionesIntegradasTransacciones> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(OperacionesIntegradasTransacciones var : lista) {
					listaExt.add(this.convertidor.OperacionesIntegradasTransaccionesToOperacionesIntegradasTransaccionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<OperacionesIntegradasTransacciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOperacionesIntegradasTransaccioness.orderBy(orderBy);
			return this.ifzOperacionesIntegradasTransaccioness.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<OperacionesIntegradasTransaccionesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<OperacionesIntegradasTransaccionesExt> listaExt = new ArrayList<OperacionesIntegradasTransaccionesExt>();
		
		try {
			List<OperacionesIntegradasTransacciones> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(OperacionesIntegradasTransacciones var : lista) {
					listaExt.add(this.convertidor.OperacionesIntegradasTransaccionesToOperacionesIntegradasTransaccionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
}
package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.Llaves;
import net.giro.contabilidad.dao.LlavesDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class LlavesFac implements LlavesRem {
	private static Logger log = Logger.getLogger(LlavesFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private LlavesDAO ifzLlaves;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public LlavesFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzLlaves = (LlavesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//LlavesImp!net.giro.contabilidad.dao.LlavesDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("LlavesFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.LlavesFac", e);
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
		LlavesFac.orderBy = orderBy;
	}

	@Override
	public Long save(Llaves entity) throws ExcepConstraint {
		try {
			return this.ifzLlaves.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.save(Llaves)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(LlavesExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.LlavesExtToLlaves(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.save(LlavesExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(Llaves entity) throws ExcepConstraint {
		try {
			this.ifzLlaves.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.update(Llaves)", e);
			throw e;
		}
	}

	/*@Override
	public void update(LlavesExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.LlavesExtToLlaves(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.update(LlavesExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzLlaves.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public Llaves findById(Long id) {
		try {
			return this.ifzLlaves.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public LlavesExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.LlavesToLlavesExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<Llaves> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzLlaves.orderBy(orderBy);
			return this.ifzLlaves.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<LlavesExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<LlavesExt> listaExt = new ArrayList<LlavesExt>();
		
		try {
			List<Llaves> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(Llaves var : lista) {
					listaExt.add(this.convertidor.LlavesToLlavesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Llaves> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzLlaves.orderBy(orderBy);
			return this.ifzLlaves.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<LlavesExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<LlavesExt> listaExt = new ArrayList<LlavesExt>();
		
		try {
			List<Llaves> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(Llaves var : lista) {
					listaExt.add(this.convertidor.LlavesToLlavesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Llaves> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzLlaves.orderBy(orderBy);
			return this.ifzLlaves.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<LlavesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<LlavesExt> listaExt = new ArrayList<LlavesExt>();
		
		try {
			List<Llaves> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(Llaves var : lista) {
					listaExt.add(this.convertidor.LlavesToLlavesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Llaves> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzLlaves.orderBy(orderBy);
			return this.ifzLlaves.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<LlavesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<LlavesExt> listaExt = new ArrayList<LlavesExt>();
		
		try {
			List<Llaves> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(Llaves var : lista) {
					listaExt.add(this.convertidor.LlavesToLlavesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<Llaves> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzLlaves.orderBy(orderBy);
			return this.ifzLlaves.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<LlavesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<LlavesExt> listaExt = new ArrayList<LlavesExt>();
		
		try {
			List<Llaves> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(Llaves var : lista) {
					listaExt.add(this.convertidor.LlavesToLlavesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public boolean comprobarPosicion(Long idLlave, int posicion) throws Exception {
		try {
			return this.ifzLlaves.comprobarPosicion(idLlave, posicion);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.comprobarPosicion(idLlave, posicion)", e);
			throw e;
		}
	}
}
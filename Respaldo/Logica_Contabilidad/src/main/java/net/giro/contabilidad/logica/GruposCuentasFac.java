package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.GruposCuentas;
import net.giro.contabilidad.dao.GruposCuentasDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class GruposCuentasFac implements GruposCuentasRem {
	private static Logger log = Logger.getLogger(GruposCuentasFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private GruposCuentasDAO ifzGruposCuentass;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public GruposCuentasFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzGruposCuentass = (GruposCuentasDAO) this.ctx.lookup("ejb:/Model_Contabilidad//GruposCuentasImp!net.giro.contabilidad.dao.GruposCuentasDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("GruposCuentasFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.GruposCuentasFac", e);
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
		GruposCuentasFac.orderBy = orderBy;
	}

	@Override
	public Long save(GruposCuentas entity) throws ExcepConstraint {
		try {
			return this.ifzGruposCuentass.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.save(GruposCuentas)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(GruposCuentasExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.GruposCuentasExtToGruposCuentas(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.save(GruposCuentasExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(GruposCuentas entity) throws ExcepConstraint {
		try {
			this.ifzGruposCuentass.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.update(GruposCuentas)", e);
			throw e;
		}
	}

	/*@Override
	public void update(GruposCuentasExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.GruposCuentasExtToGruposCuentas(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.update(GruposCuentasExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzGruposCuentass.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public GruposCuentas findById(Long id) {
		try {
			return this.ifzGruposCuentass.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public GruposCuentasExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.GruposCuentasToGruposCuentasExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<GruposCuentas> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzGruposCuentass.orderBy(orderBy);			
			return this.ifzGruposCuentass.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<GruposCuentasExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<GruposCuentasExt> listaExt = new ArrayList<GruposCuentasExt>();
		
		try {
			List<GruposCuentas> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(GruposCuentas var : lista) {
					listaExt.add(this.convertidor.GruposCuentasToGruposCuentasExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<GruposCuentas> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzGruposCuentass.orderBy(orderBy);
			return this.ifzGruposCuentass.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<GruposCuentasExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<GruposCuentasExt> listaExt = new ArrayList<GruposCuentasExt>();
		
		try {
			List<GruposCuentas> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(GruposCuentas var : lista) {
					listaExt.add(this.convertidor.GruposCuentasToGruposCuentasExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<GruposCuentas> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzGruposCuentass.orderBy(orderBy);
			return this.ifzGruposCuentass.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<GruposCuentasExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<GruposCuentasExt> listaExt = new ArrayList<GruposCuentasExt>();
		
		try {
			List<GruposCuentas> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(GruposCuentas var : lista) {
					listaExt.add(this.convertidor.GruposCuentasToGruposCuentasExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<GruposCuentas> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzGruposCuentass.orderBy(orderBy);
			return this.ifzGruposCuentass.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<GruposCuentasExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<GruposCuentasExt> listaExt = new ArrayList<GruposCuentasExt>();
		
		try {
			List<GruposCuentas> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(GruposCuentas var : lista) {
					listaExt.add(this.convertidor.GruposCuentasToGruposCuentasExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<GruposCuentas> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzGruposCuentass.orderBy(orderBy);
			return this.ifzGruposCuentass.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<GruposCuentasExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<GruposCuentasExt> listaExt = new ArrayList<GruposCuentasExt>();
		
		try {
			List<GruposCuentas> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(GruposCuentas var : lista) {
					listaExt.add(this.convertidor.GruposCuentasToGruposCuentasExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
}
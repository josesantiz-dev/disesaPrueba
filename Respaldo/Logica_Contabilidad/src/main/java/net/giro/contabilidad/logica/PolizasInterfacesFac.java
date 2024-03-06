package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.PolizasInterfaces;
import net.giro.contabilidad.dao.PolizasInterfacesDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class PolizasInterfacesFac implements PolizasInterfacesRem {
	private static Logger log = Logger.getLogger(PolizasInterfacesFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private PolizasInterfacesDAO ifzPolizasInterfacess;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public PolizasInterfacesFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzPolizasInterfacess = (PolizasInterfacesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//PolizasInterfacesImp!net.giro.contabilidad.dao.PolizasInterfacesDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("PolizasInterfacesFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.PolizasInterfacesFac", e);
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
		PolizasInterfacesFac.orderBy = orderBy;
	}

	@Override
	public Long save(PolizasInterfaces entity) throws ExcepConstraint {
		try {
			return this.ifzPolizasInterfacess.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.save(PolizasInterfaces)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(PolizasInterfacesExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.PolizasInterfacesExtToPolizasInterfaces(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.save(PolizasInterfacesExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(PolizasInterfaces entity) throws ExcepConstraint {
		try {
			this.ifzPolizasInterfacess.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.update(PolizasInterfaces)", e);
			throw e;
		}
	}

	/*@Override
	public void update(PolizasInterfacesExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.PolizasInterfacesExtToPolizasInterfaces(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.update(PolizasInterfacesExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzPolizasInterfacess.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public PolizasInterfaces findById(Long id) {
		try {
			return this.ifzPolizasInterfacess.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public PolizasInterfacesExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.PolizasInterfacesToPolizasInterfacesExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<PolizasInterfaces> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<PolizasInterfacesExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<PolizasInterfacesExt> listaExt = new ArrayList<PolizasInterfacesExt>();
		
		try {
			List<PolizasInterfaces> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(PolizasInterfaces var : lista) {
					listaExt.add(this.convertidor.PolizasInterfacesToPolizasInterfacesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<PolizasInterfaces> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<PolizasInterfacesExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<PolizasInterfacesExt> listaExt = new ArrayList<PolizasInterfacesExt>();
		
		try {
			List<PolizasInterfaces> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(PolizasInterfaces var : lista) {
					listaExt.add(this.convertidor.PolizasInterfacesToPolizasInterfacesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<PolizasInterfaces> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<PolizasInterfacesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<PolizasInterfacesExt> listaExt = new ArrayList<PolizasInterfacesExt>();
		
		try {
			List<PolizasInterfaces> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(PolizasInterfaces var : lista) {
					listaExt.add(this.convertidor.PolizasInterfacesToPolizasInterfacesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<PolizasInterfaces> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<PolizasInterfacesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<PolizasInterfacesExt> listaExt = new ArrayList<PolizasInterfacesExt>();
		
		try {
			List<PolizasInterfaces> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(PolizasInterfaces var : lista) {
					listaExt.add(this.convertidor.PolizasInterfacesToPolizasInterfacesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<PolizasInterfaces> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<PolizasInterfacesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<PolizasInterfacesExt> listaExt = new ArrayList<PolizasInterfacesExt>();
		
		try {
			List<PolizasInterfaces> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(PolizasInterfaces var : lista) {
					listaExt.add(this.convertidor.PolizasInterfacesToPolizasInterfacesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/
}
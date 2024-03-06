package net.giro.contabilidad.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.AsignacionGrupos;
import net.giro.contabilidad.beans.AsignacionGruposExt;
import net.giro.contabilidad.dao.AsignacionGruposDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class AsignacionGruposFac implements AsignacionGruposRem {
	private static Logger log = Logger.getLogger(AsignacionGruposFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private AsignacionGruposDAO ifzAsignacionGruposs;
	private ConvertExt convertidor;
	private static String orderBy;
	
	
	public AsignacionGruposFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzAsignacionGruposs = (AsignacionGruposDAO) this.ctx.lookup("ejb:/Model_Contabilidad//AsignacionGruposImp!net.giro.contabilidad.dao.AsignacionGruposDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("AsignacionGruposFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.AsignacionGruposFac", e);
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
		AsignacionGruposFac.orderBy = orderBy;
	}

	@Override
	public Long save(AsignacionGrupos entity) throws ExcepConstraint {
		try {
			return this.ifzAsignacionGruposs.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.save(AsignacionGrupos)", e);
			throw e;
		}
	}

	@Override
	public Long save(AsignacionGruposExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.AsignacionGruposExtToAsignacionGrupos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.save(AsignacionGruposExt)", e);
			throw e;
		}
	}

	@Override
	public void update(AsignacionGrupos entity) throws ExcepConstraint {
		try {
			this.ifzAsignacionGruposs.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.update(AsignacionGrupos)", e);
			throw e;
		}
	}

	@Override
	public void update(AsignacionGruposExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.AsignacionGruposExtToAsignacionGrupos(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.update(AsignacionGruposExt)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzAsignacionGruposs.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public AsignacionGrupos findById(Long id) {
		try {
			return this.ifzAsignacionGruposs.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public AsignacionGruposExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.AsignacionGruposToAsignacionGruposExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<AsignacionGrupos> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzAsignacionGruposs.orderBy(orderBy);
			return this.ifzAsignacionGruposs.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<AsignacionGruposExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<AsignacionGruposExt> listaExt = new ArrayList<AsignacionGruposExt>();
		
		try {
			List<AsignacionGrupos> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(AsignacionGrupos var : lista) {
					listaExt.add(this.convertidor.AsignacionGruposToAsignacionGruposExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<AsignacionGrupos> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzAsignacionGruposs.orderBy(orderBy);
			return this.ifzAsignacionGruposs.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<AsignacionGruposExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<AsignacionGruposExt> listaExt = new ArrayList<AsignacionGruposExt>();
		
		try {
			List<AsignacionGrupos> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(AsignacionGrupos var : lista) {
					listaExt.add(this.convertidor.AsignacionGruposToAsignacionGruposExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<AsignacionGrupos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzAsignacionGruposs.orderBy(orderBy);
			return this.ifzAsignacionGruposs.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<AsignacionGruposExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<AsignacionGruposExt> listaExt = new ArrayList<AsignacionGruposExt>();
		
		try {
			List<AsignacionGrupos> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(AsignacionGrupos var : lista) {
					listaExt.add(this.convertidor.AsignacionGruposToAsignacionGruposExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<AsignacionGrupos> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzAsignacionGruposs.orderBy(orderBy);
			return this.ifzAsignacionGruposs.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<AsignacionGruposExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<AsignacionGruposExt> listaExt = new ArrayList<AsignacionGruposExt>();
		
		try {
			List<AsignacionGrupos> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(AsignacionGrupos var : lista) {
					listaExt.add(this.convertidor.AsignacionGruposToAsignacionGruposExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<AsignacionGrupos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzAsignacionGruposs.orderBy(orderBy);
			return this.ifzAsignacionGruposs.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<AsignacionGruposExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<AsignacionGruposExt> listaExt = new ArrayList<AsignacionGruposExt>();
		
		try {
			List<AsignacionGrupos> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(AsignacionGrupos var : lista) {
					listaExt.add(this.convertidor.AsignacionGruposToAsignacionGruposExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public AsignacionGruposExt convertir(AsignacionGrupos entity) throws Exception {
		try {
			return this.convertir(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.convertir(AsignacionGrupos)", e);
			throw e;
		}
	}

	@Override
	public AsignacionGrupos convertir(AsignacionGruposExt entityExt) throws Exception {
		try {
			return this.convertir(entityExt);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.AsignacionGruposFac.convertir(AsignacionGruposExt)", e);
			throw e;
		}
	}
}
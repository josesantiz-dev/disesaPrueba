package net.giro.contabilidad.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.Operaciones;
import net.giro.contabilidad.beans.OperacionesExt;
import net.giro.contabilidad.dao.OperacionesDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.respuesta.Respuesta;

@Stateless
public class OperacionesFac implements OperacionesRem {
	private static Logger log = Logger.getLogger(OperacionesFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private OperacionesDAO ifzOperacioness;
	private AdministracionRem ifzAdmon;
	private ConvertExt convertidor;
	private static String orderBy;
	
	public OperacionesFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzOperacioness = (OperacionesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//OperacionesImp!net.giro.contabilidad.dao.OperacionesDAO");
			this.ifzAdmon = (AdministracionRem) this.ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("OperacionesFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.OperacionesFac", e);
			ctx = null;
		}
	}


	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
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
		OperacionesFac.orderBy = orderBy;
	}

	@Override
	public Long save(Operaciones entity) throws Exception {
		try {
			return this.ifzOperacioness.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.save(Operaciones)", e);
			throw e;
		}
	}

	@Override
	public List<Operaciones> saveOrUpdateList(List<Operaciones> entities) throws Exception {
		try {
			return this.ifzOperacioness.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.save(Operaciones)", e);
			throw e;
		}
	}

	@Override
	public void update(Operaciones entity) throws Exception {
		try {
			this.ifzOperacioness.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.update(Operaciones)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idOperacion) throws Exception {
		try {
			this.ifzOperacioness.delete(idOperacion);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.delete(idOperacion)", e);
			throw e;
		}
	}

	@Override
	public Operaciones findById(Long idOperacion) {
		try {
			return this.ifzOperacioness.findById(idOperacion);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findById(idOperacion)", e);
			throw e;
		}
	}

	@Override
	public List<Operaciones> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzOperacioness.orderBy(orderBy);
			return this.ifzOperacioness.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Operaciones> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzOperacioness.orderBy(orderBy);
			if (value != null && value.getClass() == java.lang.String.class)
				value = ((value.toString().trim().contains(" ")) ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			return this.ifzOperacioness.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Operaciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzOperacioness.orderBy(orderBy);
			return this.ifzOperacioness.findInProperty(columnName, values, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Operaciones> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOperacioness.orderBy(orderBy);
			return this.ifzOperacioness.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Operaciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOperacioness.orderBy(orderBy);
			return this.ifzOperacioness.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Aplicacion> findAllAplicaciones() {
		try {
			this.ifzAdmon.setInfoSesion(this.infoSesion);
			Respuesta resp = this.ifzAdmon.autocompletarAplicacion();
			return (List<Aplicacion>) resp.getBody().getValor("listAplicaciones");
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findAllAplicaciones()", e);
			throw e;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Aplicacion> findAplicacionLikeProperty(String propertyName, String propertyValue) {
		try {
			this.ifzAdmon.setInfoSesion(this.infoSesion);
			Respuesta resp = this.ifzAdmon.buscarAplicacion(propertyName, propertyValue);
			return (List<Aplicacion>) resp.getBody().getValor("listAplicaciones");
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findAplicacionLikeProperty(propertyName, propertyValue)", e);
			throw e;
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------------------------------------

	@Override
	public Long save(OperacionesExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.OperacionesExtToOperaciones(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.save(OperacionesExt)", e);
			throw e;
		}
	}

	@Override
	public void update(OperacionesExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.OperacionesExtToOperaciones(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.update(OperacionesExt)", e);
			throw e;
		}
	}

	@Override
	public OperacionesExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.OperacionesToOperacionesExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<OperacionesExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<OperacionesExt> listaExt = new ArrayList<OperacionesExt>();
		
		try {
			List<Operaciones> lista = this.findByProperty(propertyName, value, limite);
			if (lista != null) {
				for(Operaciones var : lista) {
					listaExt.add(this.convertidor.OperacionesToOperacionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OperacionesExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<OperacionesExt> listaExt = new ArrayList<OperacionesExt>();
		
		try {
			List<Operaciones> lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null) {
				for(Operaciones var : lista) {
					listaExt.add(this.convertidor.OperacionesToOperacionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OperacionesExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<OperacionesExt> listaExt = new ArrayList<OperacionesExt>();
		
		try {
			List<Operaciones> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(Operaciones var : lista) {
					listaExt.add(this.convertidor.OperacionesToOperacionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OperacionesExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<OperacionesExt> listaExt = new ArrayList<OperacionesExt>();
		
		try {
			List<Operaciones> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(Operaciones var : lista) {
					listaExt.add(this.convertidor.OperacionesToOperacionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OperacionesExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<OperacionesExt> listaExt = new ArrayList<OperacionesExt>();
		
		try {
			List<Operaciones> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(Operaciones var : lista) {
					listaExt.add(this.convertidor.OperacionesToOperacionesExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}
	
}
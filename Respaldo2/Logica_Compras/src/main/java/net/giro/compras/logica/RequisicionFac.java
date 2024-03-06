package net.giro.compras.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionExt;
import net.giro.compras.dao.RequisicionDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

import org.apache.log4j.Logger;

@Stateless
public class RequisicionFac implements RequisicionRem {
	private static Logger log = Logger.getLogger(RequisicionFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private RequisicionDAO ifzRequisiciones;
	private ConvertExt convertidor;
	private static String orderBy;
	private static Long estatus;
	
	public RequisicionFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzRequisiciones = (RequisicionDAO) this.ctx.lookup("ejb:/Model_Compras//RequisicionImp!net.giro.compras.dao.RequisicionDAO");
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("RequisicionFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.RequisicionFac", e);
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
	public void OrderBy(String orderBy) {
		RequisicionFac.orderBy = orderBy;
	}

	@Override
	public void estatus(Long estatus) {
		RequisicionFac.estatus = estatus;
	}

	@Override
	public Long save(Requisicion entity) throws Exception {
		try {
			return this.ifzRequisiciones.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.save(Requisicion)", e);
			throw e;
		}
	}

	@Override
	public Long save(RequisicionExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.RequisicionExtToRequisicion(entityExt));
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.save(RequisicionExt)", e);
			throw e;
		}
	}

	@Override
	public void update(Requisicion entity) throws Exception {
		try {
			this.ifzRequisiciones.update(entity);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.update(Requisicion)", e);
			throw e;
		}
	}

	@Override
	public List<Requisicion> saveOrUpdateList(List<Requisicion> entities) throws Exception {
		try {
			return this.ifzRequisiciones.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.saveOrUpdateList(List<Requisicion> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(RequisicionExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.RequisicionExtToRequisicion(entityExt));
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.update(RequisicionExt)", e);
			throw e;
		}
	}

	@Override
	public Requisicion cancelar(Requisicion entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.update(entity);
			return entity;
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.cancelar(Requisicion entity)", e);
			throw e;
		}
	}

	@Override
	public RequisicionExt cancelar(RequisicionExt entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.update(this.convertidor.RequisicionExtToRequisicion(entity));
			return entity;
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.cancelar(RequisicionExt entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzRequisiciones.delete(entity);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public Requisicion findById(Long id) {
		try {
			return this.ifzRequisiciones.findById(id);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public RequisicionExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.RequisicionToRequisicionExt(this.findById(id));
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<Requisicion> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzRequisiciones.estatus(estatus);
			this.ifzRequisiciones.OrderBy(orderBy);
			return this.ifzRequisiciones.findByProperty(propertyName, value, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<RequisicionExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<RequisicionExt> listaExt = new ArrayList<RequisicionExt>();
		
		try {
			List<Requisicion> lista = this.findByProperty(propertyName, value, limite);
			if (lista != null) {
				for(Requisicion var : lista) {
					listaExt.add(this.convertidor.RequisicionToRequisicionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Requisicion> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzRequisiciones.estatus(estatus);
			this.ifzRequisiciones.OrderBy(orderBy);
			return this.ifzRequisiciones.findLikeProperty(propertyName, value, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}
	
	@Override
	public List<Requisicion> findLikeProperty(String propertyName, String value, int tipo, int limite) throws Exception {
		try {
			this.ifzRequisiciones.estatus(estatus);
			this.ifzRequisiciones.OrderBy(orderBy);
			return this.ifzRequisiciones.findLikeProperty(propertyName, value, tipo, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findLikeProperty(propertyName, value, tipo, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<RequisicionExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<RequisicionExt> listaExt = new ArrayList<RequisicionExt>();
		
		try {
			List<Requisicion> lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null) {
				for(Requisicion var : lista) {
					listaExt.add(this.convertidor.RequisicionToRequisicionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Requisicion> findInProperty(String propertyName, List<Object> values) throws Exception {
		try {
			this.ifzRequisiciones.estatus(estatus);
			this.ifzRequisiciones.OrderBy(orderBy);
			return this.ifzRequisiciones.findInProperty(propertyName, values, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findInProperty(propertyName, values)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<RequisicionExt> findExtInProperty(String propertyName, List<Object> values) throws Exception {
		List<RequisicionExt> listaExt = new ArrayList<RequisicionExt>();
		
		try {
			List<Requisicion> lista = this.findInProperty(propertyName, values);
			if (lista != null) {
				for(Requisicion var : lista) {
					listaExt.add(this.convertidor.RequisicionToRequisicionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findExtInProperty(propertyName, values)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Requisicion> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzRequisiciones.estatus(estatus);
			this.ifzRequisiciones.OrderBy(orderBy);
			return this.ifzRequisiciones.findByProperties(params, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<RequisicionExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<RequisicionExt> listaExt = new ArrayList<RequisicionExt>();
		
		try {
			List<Requisicion> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(Requisicion var : lista) {
					listaExt.add(this.convertidor.RequisicionToRequisicionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Requisicion> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzRequisiciones.estatus(estatus);
			this.ifzRequisiciones.OrderBy(orderBy);
			return this.ifzRequisiciones.findLikeProperties(params, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<RequisicionExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<RequisicionExt> listaExt = new ArrayList<RequisicionExt>();
		
		try {
			List<Requisicion> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(Requisicion var : lista) {
					listaExt.add(this.convertidor.RequisicionToRequisicionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	private Long getIdEmpresa() {
		Long idEmpresa = 1L;
		
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getId();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-19 | Javier Tirado 	| Añado los metodos estatus, findByProperties y findLikeProperties. Normal y extendido
 */
package net.giro.compras.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.beans.RequisicionDetalleExt;
import net.giro.compras.dao.RequisicionDetalleDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

import org.apache.log4j.Logger;

@Stateless
public class RequisicionDetalleFac implements RequisicionDetalleRem {
	private static Logger log = Logger.getLogger(RequisicionDetalleFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private RequisicionDetalleDAO ifzRequisicionDetalles;
	private ConvertExt convertidor;
	private static String orderBy;
	
	
	public RequisicionDetalleFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzRequisicionDetalles = (RequisicionDetalleDAO) this.ctx.lookup("ejb:/Model_Compras//RequisicionDetalleImp!net.giro.compras.dao.RequisicionDetalleDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("RequisicionDetalleFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.RequisicionDetalleFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSecion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void OrderBy(String orderBy) {
		RequisicionDetalleFac.orderBy = orderBy;
	}

	@Override
	public Long save(RequisicionDetalle entity) throws ExcepConstraint {
		try {
			return this.ifzRequisicionDetalles.save(entity);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.save(RequisicionDetalle)", e);
			throw e;
		}
	}

	@Override
	public Long save(RequisicionDetalleExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.RequisicionDetalleExtToRequisicionDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.save(RequisicionDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public void update(RequisicionDetalle entity) throws ExcepConstraint {
		try {
			this.ifzRequisicionDetalles.update(entity);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.update(RequisicionDetalle)", e);
			throw e;
		}
	}

	@Override
	public void update(RequisicionDetalleExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.RequisicionDetalleExtToRequisicionDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.update(RequisicionDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzRequisicionDetalles.delete(entity);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public RequisicionDetalle findById(Long id) {
		try {
			return this.ifzRequisicionDetalles.findById(id);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public RequisicionDetalleExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.RequisicionDetalleToRequisicionDetalleExt(this.findById(id));
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<RequisicionDetalle> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzRequisicionDetalles.OrderBy(orderBy);
			return this.ifzRequisicionDetalles.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<RequisicionDetalleExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<RequisicionDetalleExt> listaExt = new ArrayList<RequisicionDetalleExt>();
		
		try {
			List<RequisicionDetalle> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(RequisicionDetalle var : lista) {
					listaExt.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<RequisicionDetalle> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzRequisicionDetalles.OrderBy(orderBy);
			return this.ifzRequisicionDetalles.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<RequisicionDetalleExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<RequisicionDetalleExt> listaExt = new ArrayList<RequisicionDetalleExt>();
		
		try {
			List<RequisicionDetalle> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(RequisicionDetalle var : lista) {
					listaExt.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<RequisicionDetalle> findInProperty(String propertyName, List<Object> values) throws Exception {
		try {
			this.ifzRequisicionDetalles.OrderBy(orderBy);
			return this.ifzRequisicionDetalles.findInProperty(propertyName, values);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findInProperty(propertyName, values)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<RequisicionDetalleExt> findExtInProperty(String propertyName, List<Object> values) throws Exception {
		List<RequisicionDetalleExt> listaExt = new ArrayList<RequisicionDetalleExt>();
		
		try {
			List<RequisicionDetalle> lista = this.findInProperty(propertyName, values);
			if (lista != null) {
				for(RequisicionDetalle var : lista) {
					listaExt.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtInProperty(propertyName, values)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<RequisicionDetalle> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			return this.ifzRequisicionDetalles.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findByProperties(params, limite)", e);
			throw e;
		}
	}

	@Override
	public List<RequisicionDetalleExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<RequisicionDetalleExt> listaExt = new ArrayList<RequisicionDetalleExt>();
		
		try {
			List<RequisicionDetalle> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(RequisicionDetalle var : lista) {
					listaExt.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<RequisicionDetalle> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			return this.ifzRequisicionDetalles.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findLikeProperties(params, limite)", e);
			throw e;
		}
	}

	@Override
	public List<RequisicionDetalleExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<RequisicionDetalleExt> listaExt = new ArrayList<RequisicionDetalleExt>();
		
		try {
			List<RequisicionDetalle> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(RequisicionDetalle var : lista) {
					listaExt.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}
}

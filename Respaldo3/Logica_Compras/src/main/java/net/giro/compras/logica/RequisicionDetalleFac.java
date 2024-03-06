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
import net.giro.compras.beans.RequisicionDetalleHistorico;
import net.giro.compras.dao.RequisicionDetalleDAO;
import net.giro.compras.dao.RequisicionDetalleHistoricoDAO;
import net.giro.plataforma.InfoSesion;

import org.apache.log4j.Logger;

@Stateless
public class RequisicionDetalleFac implements RequisicionDetalleRem {
	private static Logger log = Logger.getLogger(RequisicionDetalleFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private RequisicionDetalleDAO ifzRequisicionDetalles;
	private RequisicionDetalleHistoricoDAO ifzHistorico;
	private ConvertExt convertidor;
	private static String orderBy;
	
	public RequisicionDetalleFac() {
		Hashtable<String, Object> envorontment = null;
		
		try {
			envorontment = new Hashtable<String, Object>();
			envorontment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(envorontment);
			this.ifzRequisicionDetalles = (RequisicionDetalleDAO) this.ctx.lookup("ejb:/Model_Compras//RequisicionDetalleImp!net.giro.compras.dao.RequisicionDetalleDAO");
			this.ifzHistorico = (RequisicionDetalleHistoricoDAO) this.ctx.lookup("ejb:/Model_Compras//RequisicionDetalleHistoricoImp!net.giro.compras.dao.RequisicionDetalleHistoricoDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("RequisicionDetalleFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.RequisicionDetalleFac", e);
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
		RequisicionDetalleFac.orderBy = orderBy;
	}

	@Override
	public Long save(RequisicionDetalle entity) throws Exception {
		try {
			return this.ifzRequisicionDetalles.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.save(RequisicionDetalle)", e);
			throw e;
		}
	}

	@Override
	public void update(RequisicionDetalle entity) throws Exception {
		try {
			this.ifzRequisicionDetalles.update(entity);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.update(RequisicionDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<RequisicionDetalle> saveOrUpdateList(List<RequisicionDetalle> entities) throws Exception {
		try {
			return this.ifzRequisicionDetalles.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.saveOrUpdateList(List<RequisicionDetalle> entities)", e);
			throw e;
		}
	}

	@Override
	public void delete(long idRequisicionDetalle) throws Exception {
		RequisicionDetalleHistorico historico = null;
		RequisicionDetalle detalle = null;
		
		try {
			detalle = this.ifzRequisicionDetalles.findById(idRequisicionDetalle);
			if (detalle != null) {
				try {
					historico = new RequisicionDetalleHistorico(detalle, detalle.getModificadoPor());
					this.ifzHistorico.save(historico);
				} catch (Exception e) {
					log.error("Ocurrio un problema al registrar el Historico del detalle indicado: " + idRequisicionDetalle, e);
				}
			}
			
			this.ifzRequisicionDetalles.delete(idRequisicionDetalle);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.delete(idRequisicionDetalle)", e);
			throw e;
		}
	}

	@Override
	public void deleteAll(List<RequisicionDetalle> entities) throws Exception {
		RequisicionDetalleHistorico historico = null;
		RequisicionDetalle detalle = null;
		
		try {
			if (entities == null || entities.isEmpty())
				return;
			
			for (RequisicionDetalle entity : entities) {
				if (entity.getId() == null || entity.getId() <= 0L)
					continue;
				
				detalle = this.ifzRequisicionDetalles.findById(entity.getId());
				if (detalle != null) {
					try {
						historico = new RequisicionDetalleHistorico(detalle, detalle.getModificadoPor());
						this.ifzHistorico.save(historico);
					} catch (Exception e) {
						log.error("Ocurrio un problema al registrar el Historico del detalle indicado: " + entity.getId(), e);
					}
				}
				
				// Lo borramos de la requisicion
				this.ifzRequisicionDetalles.delete(entity.getId());
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.deleteAll(entities)", e);
			throw e;
		}
	}

	@Override
	public RequisicionDetalle findById(long idRequisicionDetalle) {
		try {
			return this.ifzRequisicionDetalles.findById(idRequisicionDetalle);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findById(idRequisicionDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<RequisicionDetalle> findAll(long idRequisicion) throws Exception {
		try {
			this.ifzRequisicionDetalles.OrderBy("id");
			return this.ifzRequisicionDetalles.findByProperty("idRequisicion", idRequisicion, 0);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findAll(long idRequisicion)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<RequisicionDetalle> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzRequisicionDetalles.OrderBy(orderBy);
			return this.ifzRequisicionDetalles.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<RequisicionDetalle> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzRequisicionDetalles.OrderBy(orderBy);
			return this.ifzRequisicionDetalles.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
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
	public List<RequisicionDetalle> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			return this.ifzRequisicionDetalles.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findByProperties(params, limite)", e);
			throw e;
		}
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
	public RequisicionDetalle convertir(RequisicionDetalleExt pojoTarget) throws Exception {
		return this.convertidor.RequisicionDetalleExtToRequisicionDetalle(pojoTarget);
	}

	@Override
	public RequisicionDetalleExt convertir(RequisicionDetalle pojoTarget) throws Exception {
		return this.convertidor.RequisicionDetalleToRequisicionDetalleExt(pojoTarget);
	}

	//------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	//------------------------------------------------------------------------------------------------------

	@Override
	public Long save(RequisicionDetalleExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.RequisicionDetalleExtToRequisicionDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.save(RequisicionDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public List<RequisicionDetalleExt> saveOrUpdateListExt(List<RequisicionDetalleExt> extendidos) throws Exception {
		List<RequisicionDetalle> entities = new ArrayList<RequisicionDetalle>();
		
		try {
			if (extendidos == null || extendidos.isEmpty())
				return extendidos;
			for (RequisicionDetalleExt extendido : extendidos) 
				entities.add(this.convertidor.RequisicionDetalleExtToRequisicionDetalle(extendido));
			extendidos.clear();
			entities = this.saveOrUpdateList(entities);
			for (RequisicionDetalle entity : entities) 
				extendidos.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(entity));
			return extendidos;
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.saveOrUpdateListExt(extendidos)", e);
			throw e;
		}
	}

	@Override
	public void update(RequisicionDetalleExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.RequisicionDetalleExtToRequisicionDetalle(entityExt));
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.update(RequisicionDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public void deleteAllExt(List<RequisicionDetalleExt> extendidos) throws Exception {
		List<RequisicionDetalle> entities = new ArrayList<RequisicionDetalle>();
		
		try {
			if (extendidos == null || extendidos.isEmpty())
				return;
			for (RequisicionDetalleExt extendido : extendidos)
				entities.add(this.convertir(extendido));
			this.deleteAll(entities);
			/*for (RequisicionDetalleExt extendido : extendidos) {
				if (extendido.getId() == null || extendido.getId() <= 0L)
					continue;
				this.ifzRequisicionDetalles.delete(extendido.getId());
			}*/
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.deleteAllExt(extendidos)", e);
			throw e;
		}
	}

	@Override
	public RequisicionDetalleExt findExtById(long idRequisicionDetalle) throws Exception {
		try {
			return this.convertidor.RequisicionDetalleToRequisicionDetalleExt(this.findById(idRequisicionDetalle));
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtById(idRequisicionDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<RequisicionDetalleExt> findExtInProperty(String propertyName, List<Object> values) throws Exception {
		List<RequisicionDetalleExt> extendidos = new ArrayList<RequisicionDetalleExt>();
		List<RequisicionDetalle> entities = null;
		
		try {
			entities = this.findInProperty(propertyName, values);
			if (entities != null) {
				for (RequisicionDetalle var : entities)
					extendidos.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtInProperty(propertyName, values)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<RequisicionDetalleExt> findExtAll(long idRequisicion) throws Exception {
		List<RequisicionDetalleExt> extendidos = new ArrayList<RequisicionDetalleExt>();
		List<RequisicionDetalle> entities = null;
		
		try {
			entities = this.findAll(idRequisicion);
			if (entities != null && ! entities.isEmpty()) {
				for (RequisicionDetalle var : entities) {
					extendidos.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<RequisicionDetalleExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<RequisicionDetalleExt> extendidos = new ArrayList<RequisicionDetalleExt>();
		List<RequisicionDetalle> entities = null;
		
		try {
			entities = this.findByProperty(propertyName, value, limite);
			if (entities != null) {
				for(RequisicionDetalle var : entities) {
					extendidos.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<RequisicionDetalleExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<RequisicionDetalleExt> extendidos = new ArrayList<RequisicionDetalleExt>();
		List<RequisicionDetalle> entities = null;
		
		try {
			entities = this.findLikeProperty(propertyName, value, limite);
			if (entities != null) {
				for(RequisicionDetalle var : entities) {
					extendidos.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<RequisicionDetalleExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<RequisicionDetalleExt> extendidos = new ArrayList<RequisicionDetalleExt>();
		List<RequisicionDetalle> entities = null;
		
		try {
			entities = this.findByProperties(params, limite);
			if (entities != null) {
				for(RequisicionDetalle var : entities) {
					extendidos.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<RequisicionDetalleExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<RequisicionDetalleExt> extendidos = new ArrayList<RequisicionDetalleExt>();
		List<RequisicionDetalle> entities = null;
		
		try {
			entities = this.findLikeProperties(params, limite);
			if (entities != null) {
				for(RequisicionDetalle var : entities) {
					extendidos.add(this.convertidor.RequisicionDetalleToRequisicionDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en RequisicionDetalleFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return extendidos;
	}

	//------------------------------------------------------------------------------------------------------
	// PRIVADOS
	//------------------------------------------------------------------------------------------------------

	private Long getCodigoEmpresa() {
		Long idEmpresa = 1L;
		
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getCodigo();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
}

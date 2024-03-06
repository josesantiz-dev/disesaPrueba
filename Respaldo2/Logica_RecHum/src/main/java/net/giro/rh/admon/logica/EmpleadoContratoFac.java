package net.giro.rh.admon.logica;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoContratoExt;
import net.giro.rh.admon.dao.EmpleadoContratoDAO;

@Stateless
public class EmpleadoContratoFac implements EmpleadoContratoRem {
	private static Logger log = Logger.getLogger(EmpleadoContratoFac.class);
	private InitialContext ctx;
	private EmpleadoContratoDAO ifzEmpleadoContrato;
	private ConvertExt convertidor;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
	
	public EmpleadoContratoFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzEmpleadoContrato = (EmpleadoContratoDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoContratoImp!net.giro.rh.admon.dao.EmpleadoContratoDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear EmpleadoFac", e);
			ctx = null;
		}
	}
	

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(EmpleadoContrato entity) throws Exception {
		try {
			return this.ifzEmpleadoContrato.save(entity, null);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.save(EmpleadoContrato entity)", re);
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> saveOrUpdateList(List<EmpleadoContrato> entities) throws Exception {
		try {
			return this.ifzEmpleadoContrato.saveOrUpdateList(entities, null);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.saveOrUpdateList(List<EmpleadoContrato> entities)", re);
			throw re;
		}
	}
	
	@Override
	public Long save(EmpleadoContratoExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.EmpleadoContratoExtToEmpleadoContrato(entityExt));
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.save(EmpleadoContratoExt entityExt)", re);
			throw re;
		}
	}

	@Override
	public void update(EmpleadoContrato entity) throws Exception {
		try {
			this.ifzEmpleadoContrato.update(entity);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.update(EmpleadoContrato entity)", re);
			throw re;
		}
	}

	@Override
	public void update(EmpleadoContratoExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.EmpleadoContratoExtToEmpleadoContrato(entityExt));
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.update(EmpleadoContratoExt entityExt)", re);
			throw re;
		}
	}

	@Override
	public void cancelar(long idContrato, long idUsuario) throws Exception {
		try {
			this.cancelar(this.findById(idContrato), idUsuario);
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.cancelar(long idContrato, long idUsuario)", e);
			throw e;
		}
	}
	
	@Override
	public void cancelar(EmpleadoContrato entity, long idUsuario) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor(idUsuario);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			this.update(entity);
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.cancelar(EmpleadoContrato entity, long idUsuario)", e);
			throw e;
		}
	}
	
	@Override
	public void cancelar(EmpleadoContratoExt entityExt, long idUsuario) throws Exception {
		try {
			this.cancelar(this.convertidor.EmpleadoContratoExtToEmpleadoContrato(entityExt), idUsuario);
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.cancelar(EmpleadoContratoExt entityExt, long idUsuario)", e);
			throw e;
		}
	}

	@Override
	public void delete(EmpleadoContrato entity) throws Exception {
		try {
			this.ifzEmpleadoContrato.delete(entity.getId());
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.delete(EmpleadoContrato entity)", e);
			throw e;
		}
	}

	@Override
	public void delete(EmpleadoContratoExt entityExt) throws Exception {
		try {
			this.ifzEmpleadoContrato.delete(entityExt.getId());
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.delete(EmpleadoContratoExt entityExt)", re);
			throw re;
		}
	}

	@Override
	public EmpleadoContrato findById(Long id) {
		try {
			return this.ifzEmpleadoContrato.findById(id);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findById(Long id)", re);
			throw re;
		}
	}

	@Override
	public EmpleadoContratoExt findByIdExt(Long id) {
		try {
			return this.convertidor.EmpleadoContratoToEmpleadoContratoExt(this.findById(id)); 
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findByIdExt(Long id)", re);
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleadoContrato.findByProperty(propertyName, value);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findByProperty(String propertyName, final Object value)", re);
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> findAll() {
		try {
			return this.ifzEmpleadoContrato.findAll();
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findAll()", re);
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> findAllByIdEmpleado(long idEmpleado) {
		try {
			return this.ifzEmpleadoContrato.findAllByIdEmpleado(idEmpleado);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findAllByIdEmpleado(long idEmpleado)", re);
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> contratoValido(long idEmpleado) throws Exception {
		try {
			return this.ifzEmpleadoContrato.contratoValido(idEmpleado);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.contratoValido(long idEmpleado)", re);
			throw re;
		}
	}
	
	@Override
	public void cancelarContratosPrevios(long idEmpleado, long idContratoActual, long idUsuario) throws Exception {
		List<EmpleadoContrato> contratos = null;
		
		try {
			contratos = this.findAllByIdEmpleado(idEmpleado);
			if (contratos != null && ! contratos.isEmpty()) {
				for (EmpleadoContrato contrato : contratos) {
					if (contrato.getEstatus() == 1)
						continue;
					if (idContratoActual == contrato.getId().longValue())
						continue;
					this.cancelar(contrato, idUsuario);
				}
			}
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.cancelarContratosPrevios(long idEmpleado, long idContratoActual, long idUsuario)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoContrato findContratoByEmpleado(long idEmpleado) throws Exception {
		List<EmpleadoContrato> contratos = null;
		EmpleadoContrato contrato = null;
		
		try {
			contratos = this.findAllByIdEmpleado(idEmpleado);
			if (contratos != null && ! contratos.isEmpty()) {
				for (EmpleadoContrato item : contratos) {
					if (item.getEstatus() == 0) {
						contrato = item;
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.findContratoByEmpleado(long idEmpleado)", e);
			throw e;
		}
		
		return contrato;
	}

	@Override
	public EmpleadoContrato convertir(EmpleadoContratoExt entityExt) throws Exception {
		return this.convertidor.EmpleadoContratoExtToEmpleadoContrato(entityExt);
	}

	@Override
	public EmpleadoContratoExt convertir(EmpleadoContrato entity) throws Exception {
		return this.convertidor.EmpleadoContratoToEmpleadoContratoExt(entity);
	}
}

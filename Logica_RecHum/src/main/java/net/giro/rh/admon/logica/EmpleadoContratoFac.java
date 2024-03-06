package net.giro.rh.admon.logica;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosRH;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoContratoExt;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.dao.EmpleadoContratoDAO;

@Stateless
public class EmpleadoContratoFac implements EmpleadoContratoRem {
	private static Logger log = Logger.getLogger(EmpleadoContratoFac.class);
	private InfoSesion infoSesion;
	// -----------------------------------------------------------------------
	private EmpleadoContratoDAO ifzEmpleadoContrato;
	private EmpleadoFiniquitoRem ifzFiniquitos;
	private ConvertExt convertidor;
	
	public EmpleadoContratoFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environment);
            this.ifzEmpleadoContrato = (EmpleadoContratoDAO) ctx.lookup("ejb:/Model_RecHum//EmpleadoContratoImp!net.giro.rh.admon.dao.EmpleadoContratoDAO");
            this.ifzFiniquitos = (EmpleadoFiniquitoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFiniquitoFac!net.giro.rh.admon.logica.EmpleadoFiniquitoRem");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error al inicializar " + this.getClass().getCanonicalName(), e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(EmpleadoContrato entity) throws Exception {
		try {
			entity.setId(this.ifzEmpleadoContrato.save(entity, getCodigoEmpresa()));
			actualizaContratos(entity.getIdEmpleado().getId(), entity.getId());
			return entity.getId();
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.save(EmpleadoContrato entity)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<EmpleadoContrato> saveOrUpdateList(List<EmpleadoContrato> entities) throws Exception {
		try {
			return this.ifzEmpleadoContrato.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.saveOrUpdateList(List<EmpleadoContrato> entities)", re);
			throw re;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(EmpleadoContrato entity) throws Exception {
		try {
			this.ifzEmpleadoContrato.update(entity);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.update(EmpleadoContrato entity)", re);
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
	public Respuesta cancelar(long idContrato) throws Exception {
		Respuesta respuesta = null;
		EmpleadoContrato contrato = null;
		String validacion = "";
		
		try {
			respuesta = new Respuesta();
			respuesta.getBody().addValor("idContrato", idContrato);
			
			// Buscamos contrato
			contrato = this.findById(idContrato);
			if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No se pudo encontrar el Contrato indicado");
				return respuesta;
			}
			
			// Validamos contrato por finiquitos
			validacion = this.validarCancelacion(contrato);
			if (validacion != null && ! "".equals(validacion.trim())) {
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError(validacion);
				respuesta.getBody().addValor("contrato", contrato);
				return respuesta;
			}
			
			// Cancelamos el contrato
			contrato.setEstatus(1);
			contrato.setFechaModificacion(Calendar.getInstance().getTime());
			contrato.setModificadoPor((this.infoSesion != null ? this.infoSesion.getAcceso().getUsuario().getId() : 1L));
			this.update(contrato);
			
			respuesta.getBody().addValor("contrato", contrato);
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.cancelar(EmpleadoContrato entity, long idUsuario)", e);
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public String validarCancelacion(long idContrato) throws Exception {
		EmpleadoContrato contrato = null;
		
		try {
			// Buscamos contrato
			contrato = this.findById(idContrato);
			if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L) 
				return "No se pudo encontrar el Contrato indicado";
			
			// Validamos contrato por finiquitos
			return this.validarCancelacion(contrato);
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.cancelar(EmpleadoContrato entity, long idUsuario)", e);
			throw e;
		}
	}

	@Override
	public void delete(long idContrato) throws Exception {
		try {
			this.ifzEmpleadoContrato.delete(idContrato);
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.delete(idContrato)", e);
			throw e;
		}
	}

	@Override
	public void delete(EmpleadoContrato entity) throws Exception {
		try {
			this.delete(entity.getId());
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.delete(EmpleadoContrato)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoContrato findById(long idContrato) throws Exception {
		try {
			return this.ifzEmpleadoContrato.findById(idContrato);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findById(idContrato)", re);
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> findAll(long idEmpleado) throws Exception {
		try {
			return this.findAll(idEmpleado, "model.id desc", false, false);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findAll(idEmpleado)", re);
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> findAll(long idEmpleado, String orderBy, boolean incluyeContratosCancelados, boolean incluyeEmpleadosSistema) throws Exception {
		try {
			return this.ifzEmpleadoContrato.findAll(idEmpleado, orderBy, incluyeContratosCancelados, incluyeEmpleadosSistema, getIdEmpresa());
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findAll(orderBy, incluyeCancelados, incluyeEmpleadosSistema)", re);
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> findByProperty(String propertyName, final Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, false, false);
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findByProperty(String propertyName, final Object value)", re);
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> findByProperty(String propertyName, final Object value, boolean incluyeContratosCancelados, boolean incluyeEmpleadosSistema) throws Exception {
		try {
			return this.ifzEmpleadoContrato.findByProperty(propertyName, value, incluyeContratosCancelados, incluyeEmpleadosSistema, getIdEmpresa());
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findByProperty(propertyName, value, incluyeContratosCancelados, incluyeEmpleadosSistema)", re);
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> contratoValido(long idEmpleado) throws Exception {
		try {
			return this.ifzEmpleadoContrato.contratoValido(idEmpleado, getIdEmpresa());
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.contratoValido(long idEmpleado)", re);
			throw re;
		}
	}
	
	@Override
	public void cancelarContratosPrevios(long idEmpleado, long idContratoActual, long idUsuario) throws Exception {
		List<EmpleadoContrato> contratos = null;
		
		try {
			contratos = this.findAll(idEmpleado);
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
	public EmpleadoContrato findContrato(long idEmpleado) throws Exception {
		List<EmpleadoContrato> contratos = null;
		
		try {
			contratos = this.findAll(idEmpleado, "model.id desc", false, false);
			if (contratos != null && ! contratos.isEmpty())
				return contratos.get(0);
			return null;
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.findContratoByEmpleado(long idEmpleado)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoContrato findContrato(long idEmpleado, Date fechaDesde, Date fechaHasta) throws Exception {
		List<EmpleadoContrato> contratos = null;
		
		try {
			contratos = this.findAll(idEmpleado, "model.estatus, model.id desc", true, false);
			if (contratos == null || contratos.isEmpty())
				return null;

			for (EmpleadoContrato contrato : contratos) {
				if (contrato.getFechaInicio() == null || contrato.getFechaFin() == null)
					continue;
				if (fechaDesde.compareTo(contrato.getFechaInicio()) >= 0 && fechaHasta.compareTo(contrato.getFechaFin()) <= 0) 
					return contrato;
			}
			
			return contratos.get(0);
		} catch (Exception e) {
			log.error("Error en EmpleadoContratoFac.findContratoByEmpleado(idEmpleado, fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoContrato convertir(EmpleadoContratoExt entityExt) throws Exception {
		return this.convertidor.EmpleadoContratoExtToEmpleadoContrato(entityExt);
	}

	@Override
	public EmpleadoContratoExt convertir(EmpleadoContrato entity) throws Exception {
		return this.convertidor.EmpleadoContratoToEmpleadoContratoExt(entity);
	}

	// --------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------------

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
	public void update(EmpleadoContratoExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.EmpleadoContratoExtToEmpleadoContrato(entityExt));
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.update(EmpleadoContratoExt entityExt)", re);
			throw re;
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
	public void delete(EmpleadoContratoExt entityExt) throws Exception {
		try {
			this.ifzEmpleadoContrato.delete(entityExt.getId());
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.delete(EmpleadoContratoExt entityExt)", re);
			throw re;
		}
	}

	@Override
	public EmpleadoContratoExt findByIdExt(long id) throws Exception {
		try {
			return this.convertidor.EmpleadoContratoToEmpleadoContratoExt(this.findById(id)); 
		} catch (Exception re) {
			log.error("Error en EmpleadoContratoFac.findByIdExt(Long id)", re);
			throw re;
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------
	
	private String validarCancelacion(EmpleadoContrato contrato) {
		List<EmpleadoFiniquito> finiquitos = null;
		
		try {
			if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L) 
				return "Ocurrio un problema al intentar validar el Contrato indicado";
			
			// Comprobamos si hay un finiquito activo y sin aprobar que afecte directamente a este contrato
			this.ifzFiniquitos.setInfoSesion(this.infoSesion);
			finiquitos = this.ifzFiniquitos.findFiniquitosByEmpleado(contrato.getIdEmpleado().getId());
			if (finiquitos != null && ! finiquitos.isEmpty()) {
				for (EmpleadoFiniquito finiquito : finiquitos) {
					finiquito.validarEstatus();
					if (contrato.getId().longValue() == finiquito.getIdContrato() && finiquito.getEstatus() == 0) 
						return "El Contrato tiene un Finiquito activo pendiente.\nCancele o Apruebe el Finiquito antes de continuar";
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar validar el Contrato", e);
			return "Ocurrio un problema al intentar validar el Contrato";
		}
		
		return "";
	}

	private void actualizaContratos(long idEmpleado, long idContrato) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idEmpleado <= 0L)
				idEmpleado = 0L;
			
			target = String.valueOf(idEmpleado);
			referencia = String.valueOf(idContrato);
			msgTopic = new MensajeTopic(TopicEventosRH.CONTRATOS_NUEVO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-CO\n\n" + comando + "\n\n", e);
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
}

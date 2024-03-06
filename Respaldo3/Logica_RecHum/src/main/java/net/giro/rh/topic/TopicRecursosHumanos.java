package net.giro.rh.topic;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.DiasFestivos;
import net.giro.plataforma.beans.DiasFestivosNegociacion;
import net.giro.plataforma.logica.DiasFestivosNegociacionRem;
import net.giro.plataforma.logica.DiasFestivosRem;
import net.giro.plataforma.logica.TopicEstatusRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TiposTopicEstatus;
import net.giro.plataforma.topics.TopicEventosGP;
import net.giro.plataforma.topics.TopicEventosInventarios;
import net.giro.plataforma.topics.TopicEventosRH;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoDescuento;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.beans.EmpleadoIncapacidad;
import net.giro.rh.admon.beans.EmpleadoInfonavit;
import net.giro.rh.admon.beans.EmpleadoNomina;
import net.giro.rh.admon.beans.EmpleadoNominaEstatus;
import net.giro.rh.admon.beans.EmpleadoNominaPreliminar;
import net.giro.rh.admon.dao.EmpleadoNominaEstatusDAO;
import net.giro.rh.admon.logica.ChecadorDetalleRem;
import net.giro.rh.admon.logica.ChecadorRem;
import net.giro.rh.admon.logica.EmpleadoContratoRem;
import net.giro.rh.admon.logica.EmpleadoDescuentoRem;
import net.giro.rh.admon.logica.EmpleadoFiniquitoRem;
import net.giro.rh.admon.logica.EmpleadoIncapacidadRem;
import net.giro.rh.admon.logica.EmpleadoInfonavitRem;
import net.giro.rh.admon.logica.EmpleadoNominaRem;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/RH")}, 
	mappedName = "topic/RH")
public class TopicRecursosHumanos implements MessageListener {
	private static Logger log = Logger.getLogger(TopicRecursosHumanos.class);
	private InfoSesion infoSesion;
	private long usuarioId;
	private long empresaId;
	private long aplicacionId;
	// -----------------------------------------------------------------------
	private TopicEstatusRem ifzTopicEstatus;
	private long idTopicEstatus;
	private List<String> mensajeLogs;
	// -----------------------------------------------------------------------
	private EmpleadoRem ifzEmpleados;
	private EmpleadoFiniquitoRem ifzFiniquitos;
	private EmpleadoContratoRem ifzContratos;
	private ChecadorRem ifzListas;
	private ChecadorDetalleRem ifzAsistencias;
	private EmpleadoNominaRem ifzNomina;
	private EmpleadoNominaEstatusDAO ifzNominaEstatus;
	private EmpleadoDescuentoRem ifzDescuentos;
	private EmpleadoInfonavitRem ifzInfonavit;
	private EmpleadoIncapacidadRem ifzIncapacidades;
	private DiasFestivosRem ifzFestivos;
	private DiasFestivosNegociacionRem ifzNegociaciones;
	private NQueryRem ifzQuery;
	
 	public TopicRecursosHumanos() {
 		InitialContext ctx = null;
 		
		try {
			ctx = new InitialContext();
        	this.ifzTopicEstatus = (TopicEstatusRem) ctx.lookup("ejb:/Logica_Publico//TopicEstatusFac!net.giro.plataforma.logica.TopicEstatusRem");
        	this.ifzFestivos = (DiasFestivosRem) ctx.lookup("ejb:/Logica_Publico//DiasFestivosFac!net.giro.plataforma.logica.DiasFestivosRem");
			this.ifzNegociaciones = (DiasFestivosNegociacionRem) ctx.lookup("ejb:/Logica_Publico//DiasFestivosNegociacionFac!net.giro.plataforma.logica.DiasFestivosNegociacionRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzFiniquitos = (EmpleadoFiniquitoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFiniquitoFac!net.giro.rh.admon.logica.EmpleadoFiniquitoRem");
			this.ifzContratos = (EmpleadoContratoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			this.ifzDescuentos = (EmpleadoDescuentoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoDescuentoFac!net.giro.rh.admon.logica.EmpleadoDescuentoRem");
			this.ifzInfonavit = (EmpleadoInfonavitRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoInfonavitFac!net.giro.rh.admon.logica.EmpleadoInfonavitRem");
			this.ifzNomina = (EmpleadoNominaRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoNominaFac!net.giro.rh.admon.logica.EmpleadoNominaRem");
			this.ifzNominaEstatus = (EmpleadoNominaEstatusDAO) ctx.lookup("ejb:/Model_RecHum//EmpleadoNominaEstatusImp!net.giro.rh.admon.dao.EmpleadoNominaEstatusDAO");
			this.ifzListas = (ChecadorRem) ctx.lookup("ejb:/Logica_RecHum//ChecadorFac!net.giro.rh.admon.logica.ChecadorRem");
			this.ifzAsistencias = (ChecadorDetalleRem) ctx.lookup("ejb:/Logica_RecHum//ChecadorDetalleFac!net.giro.rh.admon.logica.ChecadorDetalleRem");
			this.ifzIncapacidades = (EmpleadoIncapacidadRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoIncapacidadFac!net.giro.rh.admon.logica.EmpleadoIncapacidadRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar " + this.getClass().getCanonicalName(), e);
		}
	}

	@Override
	public void onMessage(Message message) {
		Gson gson = null;
		TextMessage mensaje = null;
		MensajeTopic mensajeTopic = null;
		TopicEventosRH evento;
		String eventoParam = "";
		
    	try {
	    	if (message instanceof TextMessage) {
				mensajeLog("############################## TOPIC RECURSOS HUMANOS --- INICIO : " + new Date());
				// Transformamos mensaje
				gson = new Gson();
				mensaje = (TextMessage) message;
				mensajeTopic = gson.fromJson(mensaje.getText(), MensajeTopic.class);
				this.infoSesion = mensajeTopic.getInfoSesion();
				setInfoSesion(mensajeTopic.getUsuario(), mensajeTopic.getEmpresa(), mensajeTopic.getAplicacion(), null);
				
				// Recuperamos datos del mensaje
				eventoParam = mensajeTopic.getEvento();
				this.idTopicEstatus = (mensajeTopic.getIdTopicEstatus() != null && mensajeTopic.getIdTopicEstatus() > 0L) ? mensajeTopic.getIdTopicEstatus() : 0L;
				if (this.idTopicEstatus <= 0L)
					topicRegistar(eventoParam, mensaje.getText().trim());
				mensajeLog(" --- ID " + this.idTopicEstatus, true);
				Thread.sleep(1000);
				
				// Lanzamos evento requerido
				evento = TopicEventosRH.fromString(eventoParam);
				switch (evento) {
					case EMPLEADO_ALTA: // ALTA DE CONTRATO
						altaContrato(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()));
						break;
						
					case EMPLEADO_ALTA_RETROACTIVA: // Alta Retroactiva de Empleado en Asistencia
						altaRetroactivaAsistencia(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), valueToDate(valueToString(mensajeTopic.getFecha()), Calendar.getInstance().getTime(), "dd-MM-yyyy HH:mm:ss")); 
						break;
						
					case EMPLEADO_BAJA: // BAJA DE EMPLEADO
						bajaEmpleado(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), 1);
						break;
						
					case FINIQUITO: // FINIQUITO
						finiquitoEmpleado(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()));
						break;
						
					case NOMINA_SEMANAL: // GENERAR NOMINA
						nominaSemanal(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()));
						break;
						
					case NOMINA_QUINCENAL: // GENERAR NOMINA
						nominaQuincenal(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()));
						break;
						
					case CONTRATOS_NUEVO: // CONTRATOS NUEVO
						contratosNuevo(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()));
						break;
						
					case CONTRATOS_VENCIDOS: // CONTRATOS VENCIDOS
						contratosVencidos(valueToDate(mensajeTopic.getTarget(), "dd-MM-yyyy"));
						break;
						
					case EMPLEADOS_INCAPACIDAD :// EMPLEADOS INCAPACIDAD
						empleadoIncapacitado(valueToLong(mensajeTopic.getTarget()));
						break;
						
					case EMPLEADOS_CANCELAR_INCAPACIDAD: // EMPLEADOS CANCELAR INCAPACIDAD
						empleadoCancelarIncapacidad(valueToLong(mensajeTopic.getTarget()));
						break;
						
					case EMPLEADOS_CANCELAR_INCAPACIDADES: // EMPLEADOS CANCELAR INCAPACIDADES
						empleadoCancelarIncapacidades(valueToDate(mensajeTopic.getTarget(), "dd-MM-yyyy HH:mm:ss"));
						break;
						
					case ASISTENCIA_DIA_FESTIVO: // ASISTENCIA DIA FESTIVO PARA EMPLEADOS SEMANALES
						asistenciaDiaFestivo(valueToDate(mensajeTopic.getTarget(), "dd-MM-yyyy"));
						break;
						
					case CANCELAR_NEGOCIACIONES_DIAS_FESTIVOS: // CANCELAR NEGOCIACIONES DE DIAS FESTIVOS
						invalidadorNegociacionesDiasFestivos(valueToDate(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()));
						break;
						
					default:
						mensajeLog(" -----> Evento '" + eventoParam + "' NO REGISTRADO :(");
						mensajeLog(" -----> Sin accion por Evento no reconocido");
						break;
				}
			}
	    	
	    	//message.acknowledge();
			printLog();
    	} catch (Exception e) {
			printLog("Ocurrio un problema al procesar el mensaje para TOPIC RECURSOS HUMANOS", e);
		} 
	}
	
	// ----------------------------------------------------------------------------------------
	// EVENTOS 
	// ----------------------------------------------------------------------------------------
	
	private void altaContrato(long idEmpleado, long idContrato) throws Exception {
		mensajeLog(" -----> Evento EMAL: BackOffice RH - Alta de Contrato");
		mensajeLog(" -----> Empleado : " + idEmpleado);
		mensajeLog(" -----> Contrato : " + idContrato);
		if (idEmpleado <= 0L || idContrato <= 0L) {
			topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
			return;
		}
		
	}
	
	private void bajaEmpleado(long idEmpleado, long idSolicitado, int estatus) throws Exception {
		Empleado empleado = null;
		
		try {
			mensajeLog(" -----> Evento EMBA: BackOffice RH - Baja de Empleados");
			mensajeLog(" -----> Empleado   : " + idEmpleado);
			mensajeLog(" -----> Solicitado : " + idSolicitado);
			if (idEmpleado <= 0L || idSolicitado <= 0L) {
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}
			
			if (estatus <= 0)
				estatus = 1;
			if (idSolicitado <= 0L)
				idSolicitado = 1L;
			mensajeLog("Comprobamos Empleado ... ", true);
			empleado = this.ifzEmpleados.findById(idEmpleado);
			if (empleado == null || empleado.getId() == null || empleado.getId() <= 0L) {
				topicSinAccion("No encontrado");
				return;
			}
			mensajeLog("OK");
			
			switch (empleado.getEstatus()) {
				case 1: 
					topicSinAccion(" Sin accion. Empleado dado de baja");
					return;
				case 2: 
					topicSinAccion(" Sin accion. Empleado finiquitado");
					return;
				case 3: 
					topicSinAccion(" Sin accion. Empleado con incapacidad previa");
					return;
				default: break;
			}

			mensajeLog("Damos de Baja (" + estatus + ") al Empleado ... ", true);
			empleado.setEstatus(estatus);
			empleado.setModificadoPor(idSolicitado);
			empleado.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzEmpleados.update(empleado);
			mensajeLog("OK");

			// Si el empleado esta incapacitado, debe mantener las asignaciones en Obras y Almacenes
			if (estatus == 3) 
				return;
			
			// Desasignamos el Empleado de las Obras
			mensajeLog("Disparamos metodo para desasignar de Obras ... ", true);
			sendBajaEmpleadoEnObra(idEmpleado);
			mensajeLog("OK");
			
			// Desasignamos el Empleado de los Almacenes/Bodegas
			mensajeLog("Disparamos metodo para desasignar de Almacenes ... ", true);
			sendDesasignarAlmacenes(idEmpleado);
			mensajeLog("OK");
		} catch (Exception e) {
			mensajeLog("ERROR : Ocurrio un problema al dar de Baja (" + estatus + ") al Empleado: " + idEmpleado + "\n" + e.getMessage(), true);
			throw e;
		}
	}
	
	private void finiquitoEmpleado(long idEmpleado, long idFiniquito) throws Exception {
		EmpleadoFiniquito finiquito = null;
		EmpleadoContrato contrato = null;
		//-------------------------------
		List<EmpleadoDescuento> descuentos = null;
		List<EmpleadoInfonavit> infonavit = null;
		List<ChecadorDetalle> asistencias = null;
		String observaciones = "";
		
		try {
			mensajeLog(" -----> Evento EMFI: BackOffice RH - Finiquitos");
			mensajeLog(" -----> Empleado  : " + idEmpleado);
			mensajeLog(" -----> Finiquito : " + idFiniquito);
			if (idEmpleado <= 0L || idFiniquito <= 0L) {
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recuperamos finiquito
			mensajeLog("Comprobamos Finiquito ... ");
			finiquito = this.ifzFiniquitos.findById(idFiniquito);
			if (finiquito == null || finiquito.getId() == null || finiquito.getId() <= 0L) {
				topicSinAccion("Sin accion. No se encontro el Finiquito indicado: " + idFiniquito);
				return;
			}

			// Comprobamos finiquito
			if (finiquito.getEstatus() == 1) {
				// Finiquito cancelado
				topicSinAccion("Sin accion. El Finiquito indicado esta Cancelado");
				return;
			}
			mensajeLog("OK", true);
			
			// Procedemos a Cancelar Contrato
			mensajeLog("Comprobamos Contrato ... ");
			if (finiquito.getIdContrato() > 0L)
				contrato = this.ifzContratos.findById(finiquito.getIdContrato());
			if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L)
				contrato = this.ifzContratos.findContrato(idEmpleado);
			if (contrato != null && contrato.getId() != null && contrato.getId() > 0L) {
				mensajeLog("OK", true);
				mensajeLog("Cancelando Contrato ... ");
				contrato.setEstatus(1);
				contrato.setFechaFin(finiquito.getFechaSolicitudBaja());
				contrato.setModificadoPor(finiquito.getAprobacionPor());
				contrato.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzContratos.update(contrato);
				mensajeLog("OK", true);
			} else {
				topicSinAccion("Cancelacion de Contrato omitida. El Empleado (" + idEmpleado + ") indicado no tiene un Contrato activo.");
			} 
			
			// Actualizamos Finiquito
			mensajeLog("Actualizamos Finiquito ... ");
			finiquito.setEstatus(2);
			finiquito.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzFiniquitos.update(finiquito);
			mensajeLog("OK", true);
			
			// Actualizamos estatus de empleado
			bajaEmpleado(idEmpleado, finiquito.getSolicitadoPor(), 2);
			
			// Cancelamos Asistencias porteriores al Finiquito si corresponde
			mensajeLog("Cancelamos Asistencias porteriores al Finiquito ... ");
			asistencias = this.ifzAsistencias.findAsistenciasPosteriorFecha(finiquito.getIdEmpleado().getId(), finiquito.getFechaSolicitudBaja(), "");
			if (asistencias != null && ! asistencias.isEmpty()) 
				this.ifzAsistencias.delete(asistencias);
			mensajeLog("OK", true);
			
			// Cancelamos Descuentos
			mensajeLog("Cancelamos Descuentos ... ");
			descuentos = this.ifzDescuentos.findAll(idEmpleado);
			if (descuentos != null && ! descuentos.isEmpty()) {
				for (EmpleadoDescuento descuento : descuentos) {
					if (descuento.getEstatus() == 0)
						continue;
					observaciones = "Cancelado por Finiquito " + idFiniquito;
					if (descuento.getObservaciones() != null && ! "".equals(descuento.getObservaciones().trim()))
						observaciones = descuento.getObservaciones() + "... " + observaciones;
					descuento.setObservaciones(observaciones);
					descuento.setEstatus(0);
					descuento.setModificadoPor(1L);
					descuento.setFechaCreacion(Calendar.getInstance().getTime());
				}
				
				// Guardamos los cambios con los descuentos
				this.ifzDescuentos.saveOrUpdateList(descuentos);
				mensajeLog("OK", true);
			}
			
			// Cancelamos Infonavit
			mensajeLog("Cancelamos Infonavit ... ");
			infonavit = this.ifzInfonavit.findAll(idEmpleado);
			if (infonavit != null && ! infonavit.isEmpty()) {
				for (EmpleadoInfonavit item : infonavit) {
					if (item.getEstatus() == 1)
						continue;
					observaciones = "Cancelado por Finiquito " + idFiniquito;
					if (item.getObservaciones() != null && ! "".equals(item.getObservaciones().trim()))
						observaciones = item.getObservaciones() + "... " + observaciones;
					item.setObservaciones(observaciones);
					item.setEstatus(1);
					item.setModificadoPor(1L);
					item.setFechaCreacion(Calendar.getInstance().getTime());
				}
				
				// Guardamos los cambios con los descuentos
				this.ifzInfonavit.saveOrUpdateList(infonavit);
				mensajeLog("OK", true);
			}
			
			mensajeLog("Finiquito terminado!");
		} catch (Exception e) {
			mensajeLog("ERROR : Ocurrio un problema al intentar procesar el Finiquito: " + idFiniquito + "\n" + e.getMessage(), true);
			throw e;
		}
	}
	
	private void altaRetroactivaAsistencia(long idEmpleado, long idObra, Date fechaBase) throws Exception {
		SimpleDateFormat dateFormat = null;
		Date diaSemanaInicio = null;
		Date diaSemanaFin = null;
		// -----------------------------------
		List<Checador> asistencias = null;
		List<ChecadorDetalle> generadas = null;
		ChecadorDetalle asistencia = null;
		EmpleadoExt pojoEmpleado = null;
		String result = "EMPLEADO_ID";
		Date ingreso = null;
		
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			diaSemanaInicio = getIniDayOfWeek(fechaBase);
			diaSemanaFin = getEndDayOfWeek(fechaBase);
			mensajeLog(" -----> Evento EMAS : BackOffice RH - Alta Retroactiva de Empleado en Asistencia");
			mensajeLog(" -----> Empleado    : " + idEmpleado);
			mensajeLog(" -----> Obra        : " + idObra);
			mensajeLog(" -----> Fecha Base  : " + dateFormat.format(fechaBase));
			mensajeLog(" ------ ----------- - ------------------- ");
			mensajeLog(" -----> Dia Inicial : " + dateFormat.format(diaSemanaInicio));
			mensajeLog(" -----> Dia Final   : " + dateFormat.format(diaSemanaFin));
			if (idEmpleado <= 0L || idObra <= 0L) { 
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}

			pojoEmpleado = this.ifzEmpleados.findByIdExt(idEmpleado);
			if (pojoEmpleado == null || pojoEmpleado.getId() == null || pojoEmpleado.getId() <= 0L) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recupero las Listas de Asistencia de la Obra especificada con el rango de fechas correspondiente
			asistencias = encontrarAsistencias(idObra, idEmpleado, diaSemanaInicio, diaSemanaFin); 
			if (asistencias == null || asistencias.isEmpty()) {
				topicSinAccion("Sin accion por Listas de Asistencias no encontradas ");
				return;
			}
			
			ingreso = pojoEmpleado.getFechaIngreso();
			generadas = new ArrayList<ChecadorDetalle>();
			for (Checador checador : asistencias) {
				if (checador.getFecha().compareTo(ingreso) < 0)
					continue;
				asistencia = generaAsistencia(checador, pojoEmpleado);
				if (asistencia != null)
					generadas.add(asistencia);
			}

			if (generadas == null || generadas.isEmpty()) {
				topicSinAccion("No hay registros de Asistencia desde la Fecha de Ingreso del Empleado");
				return;
			}
			
			// Guardamos las asistencias nuevas
			this.ifzAsistencias.saveOrUpdateList(generadas);
		} catch (Exception e) {
			if (pojoEmpleado != null)
				result = pojoEmpleado.getNombre() + " (EMPLEADO_ID)";
			result += result.replace("EMPLEADO_ID", String.valueOf(idEmpleado));
			mensajeLog("ERROR : Ocurrio un problema al generar la Asistencia retroactiva del Empleado " + result + "\n" + e.getMessage(), true);
			throw e;
		}
	}
	
	private void nominaSemanal(long idNominaEstatus, long nominaPreliminar) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		List<EmpleadoNomina> nomina = new ArrayList<EmpleadoNomina>();
		List<EmpleadoNomina> regExistentes = new ArrayList<EmpleadoNomina>();
		Date fechaDesde = null;
		Date fechaHasta = null;
		// -----------------------------------------------------------------
		EmpleadoNominaEstatus nominaEstatus = null;
		int estatusNomina = 0;
		String mensajePeticion = "";
		// -----------------------------------------------------------------
		EmpleadoNomina item = null;
		EmpleadoContrato contrato = null;
		Empleado empleado = null;
		// -----------------------------------------------------------------
		int horasJornada = 0;
		int horasTrabajadas = 0;
		int horasNormales = 0;
		int horasExtras = 0;
		double sueldoNormal = 0;
		double sueldoExtra = 0;
		double proporcionSeptimoDia = 0;
		double montoDescuentos = 0;
		double montoInfonavit = 0;
		List<String> descuentosEmpleados = null;
		double sueldoNeto = 0;
		// -----------------------------------------------------------------
		List<Checador> listas = null;
		List<ChecadorDetalle> asistencias = null;
		List<EmpleadoDescuento> descuentos = null;
		List<EmpleadoInfonavit> infonavit = null;
		DiasFestivos dia = null;
		int diaFestivo = 0;
		int diaFestivoFactor = 0;
		int diaDescanso = 0;
		// -----------------------------------------------------------------
		HashMap<Long, EmpleadoContrato> mapContratos = null;
		HashMap<Long, Integer> mapEmpleadoHorasDiaFestivo = null;
		int horasDiaFestivo = 0;
		int step = 0;
		int steps = 0;
		HashMap<Long, Integer> diasTrabajados = null;
		// -----------------------------------------------------------------
		boolean hasFestivo = false;
		HashMap<Long, Integer> mapNominaIndice= null;
		HashMap<Integer, EmpleadoNomina> mapNominaFestivoValido = null;
		List<Integer> mapNominaBorrable = null;
		
		try {
			mensajeLog(" -----> Evento EM-NOMINA: BackOffice RH - Generar Nomina Semanal");
			mensajeLog(" -----> Nomina     : " + idNominaEstatus);
			mensajeLog(" -----> Preliminar : " + (nominaPreliminar == 1 ? "SI" : "NO"));
			if (idNominaEstatus <= 0L) {
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}
			
			// lanzamos back office correspondiente
			if (nominaPreliminar == 1) {
				nominaSemanalPreliminar(idNominaEstatus);
				return;
			}
			
			// Recuperamos la solicitud de Nomina
			nominaEstatus = this.ifzNominaEstatus.findById(idNominaEstatus);
			if (nominaEstatus == null || nominaEstatus.getId() == null || nominaEstatus.getId() <= 0L) {
				topicSinAccion("Sin accion. No se pudo recuperar la peticion de Calculo de Nomina o la peticion ya fue finalizada: " + idNominaEstatus);
				mensajePeticion = "Peticion de Calculo de Nomina terminada o no encontrada: " + idNominaEstatus;
				estatusNomina = 1;
				return;
			}
			
			estatusNomina = 0;
			fechaDesde = nominaEstatus.getFechaDesde();
			fechaHasta = nominaEstatus.getFechaHasta();
			formatter.applyPattern("dd-MM-yyyy");
			mensajeLog(" -----> Desde  : " + formatter.format(fechaDesde));
			mensajeLog(" -----> Hasta  : " + formatter.format(fechaHasta));
			
			// Borramos los registros de nomina si existen
			mensajeLog("Preparando ... ");
			this.ifzNomina.orderBy("idEmpleado, fecha");
			this.ifzNomina.setInfoSesion(this.infoSesion);
			nomina = this.ifzNomina.findByDates(fechaDesde, fechaHasta);
			if (nomina != null && ! nomina.isEmpty())
				nomina = this.ifzNomina.deleteAll(nomina);
			if (! nomina.isEmpty()) {
				regExistentes.addAll(nomina);
				nomina.clear();
			}
			
			// Listas de asistencias autorizadas
			mensajeLog("Recuperando listas ... ");
			this.ifzListas.setInfoSesion(this.infoSesion);
			listas = this.ifzListas.asistenciasNominas(fechaDesde, fechaHasta, 0L, null);
			if (listas == null || listas.isEmpty()) {
				formatter.applyPattern("dd-MM-yyyy");
				mensajeLog("ERROR", true);
				topicSinAccion("Sin accion. No se encontraton listas de asistencias en la(s) fecha(s) indicada(s): " + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta));
				mensajePeticion = "No se encontraton listas de asistencias en la(s) fecha(s) indicada(s)";
				estatusNomina = 1;
				return;
			}
			mensajeLog("OK", true);

			// Iteracion de listas
			mensajeLog("Generando Nomina ... ");
			diasTrabajados = new HashMap<Long, Integer>();
			mapContratos = new HashMap<Long, EmpleadoContrato>();
			steps = listas.size();
			for (Checador lista : listas) {
				step++;
				formatter.applyPattern("dd-MM-yyyy");
				log.info("NOMINA " + idNominaEstatus + " :: " + lista.getIdObra() + " :: " + formatter.format(lista.getFecha()) + " :: " + step + "/" + steps);
				if (interrumpirProcesoNomina(idNominaEstatus)) {
					mensajeLog("Proceso interrumpido por usuario");
					mensajePeticion = "Proceso interrumpido por usuario";
					estatusNomina = 1;
					return;
				}
				
				mensajeLog("Recuperando asistencias " + formatter.format(lista.getFecha()) + " ... ");
				this.ifzAsistencias.setInfoSesion(this.infoSesion);
				asistencias = this.ifzAsistencias.findAll(lista.getId(), "idEmpleado, fecha");
				if (asistencias == null || asistencias.isEmpty()) {
					mensajeLog("ERROR", true);
					mensajeLog("Lista " + lista.getId() + " sin asistencias");
					log.info("NOMINA " + idNominaEstatus + " - Lista " + lista.getId() + " sin asistencias");
					continue;
				}
				mensajeLog("OK -> x" + asistencias.size(), true);
				
				// Comprobamos si el dia es festivo
				diaFestivo = 0;
				diaFestivoFactor = 0;
				dia = this.ifzFestivos.comprobarDiaFestivo(lista.getFecha()); 
				if (dia != null) {
					mensajeLog("Dia festivo!!!");
					log.info("Dia festivo!!!");
					diaFestivo = 1;
					diaFestivoFactor = (int) dia.getFactor();
					hasFestivo = true;
				}
				
				// Validamos si hay negociacion, es decir, tratar el dia festivo como normal
				if (verificaDiaFestivoNegociacion(dia, lista.getIdObra())) {
					mensajeLog("Dia festivo Negociado!!!");
					log.info("Dia festivo Negociado!!!");
					diaFestivoFactor = 0;
					diaFestivo = 0;
				}
				
				// Iteracion de asistencias
				for (ChecadorDetalle asistencia : asistencias) {
					// Solo asistencias autorizadas
					if (asistencia.getUsuarioAutoriza() <= 0L) {
						mensajeLog("Asistencia " + asistencia.getId() + " NO AUTORIZADA");
						log.info("Asistencia " + asistencia.getId() + " NO AUTORIZADA");
						continue;
					}
					
					// Comprobamos el registro existente
					item = null;
					formatter.applyPattern("MM-dd-yyyy");
					if (regExistentes != null && ! regExistentes.isEmpty()) {
						for (EmpleadoNomina en : regExistentes) {
							if (asistencia.getIdEmpleado().equals(en.getIdEmpleado().getId()) && formatter.format(asistencia.getFecha()).equals(formatter.format(en.getFecha()))) {
								item = en;
								break;
							} 
						}
					}
					
					// Comprobamos empleado 
					mensajeLog("Comprobamos Empleado ... ");
					empleado = ((item == null) ? this.ifzEmpleados.findById(asistencia.getIdEmpleado()) : item.getIdEmpleado());
					if (empleado == null || empleado.getId() == null || empleado.getId() <= 0L) {
						mensajeLog("ERROR", true);
						mensajeLog("Asistencia " + asistencia.getId() + " : Empleado " + asistencia.getIdEmpleado() + " NO ENCONTRADO");
						log.info("Asistencia " + asistencia.getId() + " : Empleado " + asistencia.getIdEmpleado() + " NO ENCONTRADO");
						continue;
					}
					
					// Comprobamos contrato
					mensajeLog("Comprobamos Contrato ... ");
					contrato = null;
					if (mapContratos.containsKey(empleado.getId()))
						contrato = mapContratos.get(empleado.getId()); 
					if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L) 
						contrato = this.ifzContratos.findContrato(empleado.getId());  
					if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L) 
						contrato = this.ifzContratos.findContrato(empleado.getId(), fechaDesde, fechaHasta); 
					if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L) {
						mensajeLog("ERROR", true);
						mensajeLog("Asistencia " + asistencia.getId() + " : Empleado " + asistencia.getIdEmpleado() + " SIN CONTRATO");
						log.info("Asistencia " + asistencia.getId() + " : Empleado " + asistencia.getIdEmpleado() + " SIN CONTRATO");
						continue;
					}
					
					// Añadimos el contrato al mapa si corresponde
					if (! mapContratos.containsKey(empleado.getId())) {
						// calculando Sueldo Hora de contrato
						sueldoNormal = contrato.getSueldo();
						sueldoNormal = ((sueldoNormal / 7) / 8);
						contrato.setSueldoHora(new BigDecimal((new DecimalFormat("#0.0000")).format(sueldoNormal)));
						mapContratos.put(empleado.getId(), contrato);
					}
					
					// Comprobamos incapacidad
					mensajeLog("Comprobamos incapacidad ... ");
					if (this.ifzIncapacidades.verificarIncapacidad(asistencia.getIdEmpleado(), lista.getFecha())) {
						mensajeLog("INCAPACITADO", true);
						log.info("Asistencia " + asistencia.getId() + " : Empleado " + asistencia.getIdEmpleado() + " INCAPACITADO");
						continue;
					}

					// Inicializacion
					descuentosEmpleados = (descuentosEmpleados != null ? descuentosEmpleados : new ArrayList<String>());
					formatter.applyPattern("HH");
					horasJornada = contrato.getHorasJornada();
					horasTrabajadas = 0;
					horasNormales = 0;
					horasExtras = 0;
					sueldoNormal = 0;
					sueldoExtra = 0;
					proporcionSeptimoDia = 0;
					montoDescuentos = 0;
					montoInfonavit = 0;
					sueldoNeto = 0;
					diaDescanso = 0;
					
					// Añadimos Dia Trabajado por empleado
					if (! diasTrabajados.containsKey(empleado.getId()))
						diasTrabajados.put(empleado.getId(), 0);
					diasTrabajados.put(empleado.getId(), diasTrabajados.get(empleado.getId()) + 1);
					
					// Validamos dia descando
					diaDescanso = verificaDiaDescanso(lista.getFecha(), contrato.getDiaDescanso());
					
					// Calculo y validacion de horas
					horasTrabajadas = asistencia.getHorasTrabajadas();
					if (asistencia.getHorasExtraAutorizadas() != null && asistencia.getUsuarioAutoriza() > 0) 
						horasExtras = Integer.parseInt(formatter.format(asistencia.getHorasExtraAutorizadas()));
					horasNormales = horasTrabajadas - horasExtras;
					if (horasNormales > horasJornada || (horasNormales < horasJornada && horasTrabajadas >= horasJornada))
						horasNormales = horasJornada;
					horasNormales = (horasNormales < 0 ? 0 : horasNormales);
					horasExtras   = (horasExtras   < 0 ? 0 : horasExtras);
					if (diaFestivo == 1) // Si trabajo el dia destivo la multiplica por el factor asignado al dia, si no, pago dia normal
						horasNormales = ((horasNormales == 0) ? horasJornada : (horasNormales * diaFestivoFactor));
					
					// Calculo de sueldo diario, Si trabaja su dia de descanso, se paga la hora de trabajo como hora extra
					sueldoNormal = contrato.getSueldoHora().doubleValue() * horasNormales;
					if (sueldoNormal > 0 && diaDescanso == 1)
						sueldoNormal = contrato.getSueldoHoraExtra().doubleValue() * horasNormales;
					sueldoExtra = contrato.getSueldoHoraExtra().doubleValue() * horasExtras;
					sueldoNeto  = (sueldoNormal + sueldoExtra);
					
					// Calculamos el proporcional del Septimo dia si corresponde
					if (diaDescanso == 0)
						proporcionSeptimoDia = ((contrato.getSueldoHora().doubleValue() * horasJornada) / 6);
					
					// descuentos
					mensajeLog("Comprobamos Descuentos ... ");
					descuentos = this.ifzDescuentos.comprobarDescuentosPorFecha(asistencia.getIdEmpleado(), asistencia.getFecha());
					if (descuentos != null && ! descuentos.isEmpty() && sueldoNeto > 0) {
						for (EmpleadoDescuento descuento : descuentos) {
							if (descuentosEmpleados.contains("D" + descuento.getId()))
								continue;
							descuentosEmpleados.add("D" + descuento.getId());
							montoDescuentos += descuento.getMonto().doubleValue();
						}
						mensajeLog("OK", true);
					}
					
					// infonavit
					mensajeLog("Comprobamos Infonavit ... ");
					infonavit = this.ifzInfonavit.comprobarPorFecha(empleado.getId(), lista.getFecha());
					if (infonavit != null && ! infonavit.isEmpty() && sueldoNeto > 0) {
						for (EmpleadoInfonavit info : infonavit) {
							if (descuentosEmpleados.contains("I" + info.getId()))
								continue;
							descuentosEmpleados.add("I" + info.getId());
							montoInfonavit += info.getMonto().doubleValue();
						}
						mensajeLog("OK", true);
					}
					
					// Calculamos el sueldo neto del dia
					sueldoNeto -= (montoDescuentos + montoInfonavit);
					
					// Generamos/Actualizamos el registro de nomina 
					if (item == null) {
						item = new EmpleadoNomina();
						item.setIdEmpleado(empleado);
						item.setTipo(0);
						item.setCreadoPor(nominaEstatus.getCreadoPor());
						item.setFechaCreacion(Calendar.getInstance().getTime());
					} 
					
					if (diaDescanso == 1) {
						sueldoExtra += sueldoNormal;
						horasExtras += horasNormales;
						sueldoNormal = 0;
						horasNormales = 0;
					}
					
					item.setFecha(asistencia.getFecha());
					item.setDiaDescanso(diaDescanso);
					item.setDiaFestivo(diaFestivo);
					item.setFactor(diaFestivoFactor);
					item.setIdContrato(contrato.getId());
					item.setIdPeriodicidad(contrato.getPeriodicidadPago());
					item.setIdObra(lista.getIdObra());
					item.setHorasTrabajadas(horasNormales);
					item.setHorasExtras(horasExtras);
					item.setPagoNormal(new BigDecimal(sueldoNormal));
					item.setSeptimoDia(new BigDecimal((new DecimalFormat("#0.0000")).format(proporcionSeptimoDia)));
					item.setPagoExtra(new BigDecimal(sueldoExtra));
					item.setDescuento(new BigDecimal(montoDescuentos));
					item.setInfonavit(new BigDecimal(montoInfonavit));
					item.setPagoNeto(new BigDecimal(sueldoNeto));
					item.setIdEmpresa(nominaEstatus.getIdEmpresa());
					item.setObservaciones("");
					item.setModificadoPor(nominaEstatus.getCreadoPor());
					item.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Añado a listado
					nomina.add(item);
					mensajeLog("----------------------------------------------");
				}
			}
			mensajeLog("OK", true);
			
			// Priorizamos Dia festivo si corresponde
			if (hasFestivo) { 
				mensajeLog("Priorizamos Dia festivo ... ");
				mapNominaIndice = new HashMap<Long, Integer>();
				mapNominaBorrable = new ArrayList<Integer>();
				mapNominaFestivoValido = new HashMap<Integer, EmpleadoNomina>();
				for (EmpleadoNomina nom : nomina) {
					if (nom.getDiaFestivo() == 0)
						continue;
					
					if (! mapNominaIndice.containsKey(nom.getIdEmpleado().getId())) {
						mapNominaIndice.put(nom.getIdEmpleado().getId(), nomina.indexOf(nom));
						mapNominaFestivoValido.put(nomina.indexOf(nom), nom);
						continue;
					}
					
					if (nom.getHorasTrabajadas() == 16 && mapNominaFestivoValido.get(mapNominaIndice.get(nom.getIdEmpleado().getId())).getHorasTrabajadas() == nom.getHorasTrabajadas()) {
						nom.setHorasTrabajadas(0); 
						nom.setPagoNormal(BigDecimal.ZERO);
						nom.setSeptimoDia(BigDecimal.ZERO);
						nom.setHorasExtras(0); 
						nom.setPagoExtra(BigDecimal.ZERO);
						continue;
					}
					
					if (nom.getHorasTrabajadas() != 8 && mapNominaFestivoValido.get(mapNominaIndice.get(nom.getIdEmpleado().getId())).getHorasTrabajadas() == nom.getHorasTrabajadas()) {
						nom.setHorasTrabajadas(0); 
						nom.setPagoNormal(BigDecimal.ZERO);
						nom.setSeptimoDia(BigDecimal.ZERO);
						nom.setHorasExtras(0); 
						nom.setPagoExtra(BigDecimal.ZERO);
						continue;
					}
					
					if (nom.getHorasTrabajadas() == 8 && mapNominaFestivoValido.get(mapNominaIndice.get(nom.getIdEmpleado().getId())).getHorasTrabajadas() == nom.getHorasTrabajadas()) {
						nom.setHorasTrabajadas(0); 
						nom.setPagoNormal(BigDecimal.ZERO);
						nom.setSeptimoDia(BigDecimal.ZERO);
						nom.setHorasExtras(0); 
						nom.setPagoExtra(BigDecimal.ZERO);
						continue;
					}
					
					mapNominaBorrable.add(mapNominaIndice.get(nom.getIdEmpleado().getId()));
					mapNominaFestivoValido.remove(mapNominaIndice.get(nom.getIdEmpleado().getId()));
					mapNominaIndice.put(nom.getIdEmpleado().getId(), nomina.indexOf(nom));
					mapNominaFestivoValido.put(nomina.indexOf(nom), nom);
				}
				
				if (! mapNominaBorrable.isEmpty()) {
					for (Integer index : mapNominaBorrable) {
						item = nomina.get(index);
						item.setHorasTrabajadas(0); 
						item.setPagoNormal(BigDecimal.ZERO);
						item.setSeptimoDia(BigDecimal.ZERO);
						item.setHorasExtras(0); 
						item.setPagoExtra(BigDecimal.ZERO);
						nomina.set(index, item);
					}
				}
				mensajeLog("OK", true);
			}
			
			// Consolidamos pago dia donde corresponda
			mensajeLog("Consolidando pago dia ... ");
			mapEmpleadoHorasDiaFestivo = new HashMap<Long, Integer>();
			for (EmpleadoNomina nom : nomina) {
				/*if (diasTrabajados.containsKey(nom.getIdEmpleado().getId())) {
					diasTrabajados.put(nom.getIdEmpleado().getId(), (diasTrabajados.get(nom.getIdEmpleado().getId()) <= 6 ? diasTrabajados.get(nom.getIdEmpleado().getId()) : 6));
					montoInfonavit = nom.getInfonavit().doubleValue() / diasTrabajados.get(nom.getIdEmpleado().getId());
					nom.setInfonavit(new BigDecimal(montoInfonavit));
				}*/
				
				if (nom.getHorasTrabajadas() <= 0 && nom.getDiaDescanso() == 0) {
					nom.setHorasTrabajadas(0); // Evita negativos segun condicion
					nom.setPagoNormal(BigDecimal.ZERO);
					nom.setSeptimoDia(BigDecimal.ZERO);
				}
				
				if (nom.getHorasExtras() <= 0) {
					nom.setHorasExtras(0);  // Evita negativos segun condicion
					nom.setPagoExtra(BigDecimal.ZERO);
				}
				
				if (nom.getDiaFestivo() == 1) {
					horasDiaFestivo = 0;
					if (mapEmpleadoHorasDiaFestivo.containsKey(nom.getIdEmpleado().getId())) {
						horasDiaFestivo = mapEmpleadoHorasDiaFestivo.get(nom.getIdEmpleado().getId());
						if (horasDiaFestivo >= 8) {
							nom.setHorasTrabajadas(0);
							nom.setHorasExtras(0);
							nom.setPagoNormal(BigDecimal.ZERO);
							nom.setPagoExtra(BigDecimal.ZERO);
							nom.setSeptimoDia(BigDecimal.ZERO);
							nom.setDescuento(BigDecimal.ZERO);
							nom.setInfonavit(BigDecimal.ZERO);
							nom.setPagoNeto(BigDecimal.ZERO);
							continue;
						}
					}
					
					horasDiaFestivo += nom.getHorasTrabajadas();
					mapEmpleadoHorasDiaFestivo.put(nom.getIdEmpleado().getId(), horasDiaFestivo);
				}
				
				// Recupero datos
				sueldoNormal = nom.getPagoNormal().doubleValue();
				sueldoExtra = nom.getPagoExtra().doubleValue();
				proporcionSeptimoDia = nom.getSeptimoDia().doubleValue();
				montoDescuentos = nom.getDescuento().doubleValue();
				montoInfonavit = nom.getInfonavit().doubleValue();
				sueldoNeto = nom.getPagoNeto().doubleValue();
				
				// Recalculo y asigno sueldo neto
				sueldoNeto = (sueldoNormal + sueldoExtra + proporcionSeptimoDia) - (montoDescuentos + montoInfonavit);
				nom.setPagoNeto(new BigDecimal(sueldoNeto));
			}
			mensajeLog("OK", true);
			
			// Guardamos Nomina
			mensajeLog("Guardando Nomina ... ");
			log.info("Guardando Nomina " + idNominaEstatus);
			if (! nomina.isEmpty()) {
				this.ifzNomina.setInfoSesion(this.infoSesion);
				this.ifzNomina.saveOrUpdateList(nomina);
			}
			mensajeLog("OK", true);
			
			mensajeLog("Nomina " + idNominaEstatus + " terminada!");
			if (estatusNomina == 0) {
				estatusNomina = 2;
				mensajePeticion = "OK";
			}
		} catch (Exception e) {
			mensajeLog("ERROR", true);
			mensajeLog("Ocurrio un problema al intentar Generar/Guardar Nomina\n" + e.getMessage(), true);
			mensajePeticion = "ERROR: " + e.getMessage();
			estatusNomina = 1;
			throw e;
		} finally {
			try {
				// Actualizamos estatus de peticion
				nominaEstatus.setEstatus(estatusNomina);
				nominaEstatus.setMensaje(mensajePeticion);
				nominaEstatus.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzNominaEstatus.update(nominaEstatus);
			} catch (Exception e) {
				log.error("Ocurrio un problema al actualizar el estatus de la Peticion: " + this.idTopicEstatus, e);
				throw e;
			}
		}
	}

	private void nominaSemanalPreliminar(long idNominaEstatus) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		List<EmpleadoNomina> nomina = new ArrayList<EmpleadoNomina>();
		List<EmpleadoNominaPreliminar> nominaPreliminar = null;
		List<EmpleadoNomina> regExistentes = new ArrayList<EmpleadoNomina>();
		Date fechaDesde = null;
		Date fechaHasta = null;
		// -----------------------------------------------------------------
		EmpleadoNominaEstatus nominaEstatus = null;
		int estatusNomina = 0;
		String mensajePeticion = "";
		// -----------------------------------------------------------------
		EmpleadoNomina item = null;
		EmpleadoContrato contrato = null;
		Empleado empleado = null;
		// -----------------------------------------------------------------
		int horasJornada = 0;
		int horasTrabajadas = 0;
		int horasNormales = 0;
		int horasExtras = 0;
		double sueldoNormal = 0;
		double sueldoExtra = 0;
		double proporcionSeptimoDia = 0;
		double montoDescuentos = 0;
		double montoInfonavit = 0;
		//List<Long> infonavitTmp = null;
		double sueldoNeto = 0;
		// -----------------------------------------------------------------
		List<Checador> listas = null;
		List<ChecadorDetalle> asistencias = null;
		List<EmpleadoDescuento> descuentos = null;
		List<EmpleadoInfonavit> infonavit = null;
		DiasFestivos dia = null;
		int diaFestivo = 0;
		int diaFestivoFactor = 0;
		int diaDescanso = 0;
		// -----------------------------------------------------------------
		HashMap<Long, EmpleadoContrato> mapContratos = null;
		HashMap<Long, Integer> mapEmpleadoFestivo = null;
		int horasDiaFestivo = 0;
		int step = 0;
		int steps = 0;
		HashMap<Long, Integer> diasTrabajados = null;
		// -----------------------------------------------------------------
		boolean hasFestivo = false;
		HashMap<Long, Integer> mapNominaIndice= null;
		HashMap<Integer, EmpleadoNomina> mapNominaFestivoValido = null;
		List<Integer> mapNominaBorrable = null;
		
		try {
			// Recuperamos la solicitud de Nomina
			nominaEstatus = this.ifzNominaEstatus.findById(idNominaEstatus);
			if (nominaEstatus == null || nominaEstatus.getId() == null || nominaEstatus.getId() <= 0L) {
				topicSinAccion("Sin accion. No se pudo recuperar la peticion de Calculo de Nomina o la peticion ya fue finalizada: " + idNominaEstatus);
				mensajePeticion = "Peticion de Calculo de Nomina terminada o no encontrada: " + idNominaEstatus;
				estatusNomina = 1;
				return;
			}
			
			estatusNomina = 0;
			fechaDesde = nominaEstatus.getFechaDesde();
			fechaHasta = nominaEstatus.getFechaHasta();
			formatter.applyPattern("dd-MM-yyyy");
			mensajeLog(" -----> Desde  : " + formatter.format(fechaDesde));
			mensajeLog(" -----> Hasta  : " + formatter.format(fechaHasta));
			
			// Borramos los registros de nomina si existen
			mensajeLog("Preparando ... ");
			this.ifzNomina.orderBy("idEmpleado, fecha");
			this.ifzNomina.setInfoSesion(this.infoSesion);
			nominaPreliminar = this.ifzNomina.findByDatesPreliminar(fechaDesde, fechaHasta);
			if (nominaPreliminar != null && ! nominaPreliminar.isEmpty())
				this.ifzNomina.deleteAllPreliminar(nominaPreliminar);
			
			// Listas de asistencias autorizadas
			mensajeLog("Recuperando listas ... ");
			this.ifzListas.setInfoSesion(this.infoSesion);
			listas = getListasAsistencias(fechaDesde, fechaHasta); 
			if (listas == null || listas.isEmpty()) {
				formatter.applyPattern("dd-MM-yyyy");
				topicSinAccion("Sin accion. No se encontraton listas de asistencias en la(s) fecha(s) indicada(s): " + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta));
				mensajePeticion = "No se encontraton listas de asistencias en la(s) fecha(s) indicada(s)";
				estatusNomina = 1;
				return;
			}

			// Iteracion de listas
			mensajeLog("Generando Nomina preliminar ... ");
			diasTrabajados = new HashMap<Long, Integer>();
			mapContratos = new HashMap<Long, EmpleadoContrato>();
			steps = listas.size();
			for (Checador lista : listas) {
				step++;
				log.info("NOMINA Preliminar " + idNominaEstatus + " - " + step + "/" + steps);
				if (interrumpirProcesoNomina(idNominaEstatus)) {
					mensajeLog("Proceso interrumpido por usuario");
					mensajePeticion = "Proceso interrumpido por usuario";
					estatusNomina = 1;
					break;
				}
				
				this.ifzAsistencias.setInfoSesion(this.infoSesion);
				asistencias = this.ifzAsistencias.findAll(lista.getId(), "idEmpleado, fecha");
				if (asistencias == null || asistencias.isEmpty()) {
					log.info("NOMINA " + idNominaEstatus + " - Lista " + lista.getId() + " sin asistencias");
					continue;
				}
				
				// Comprobamos si el dia es festivo
				diaFestivo = 0;
				diaFestivoFactor = 0;
				dia = this.ifzFestivos.comprobarDiaFestivo(lista.getFecha());
				if (dia != null) {
					diaFestivo = 1;
					diaFestivoFactor = (int) dia.getFactor();
					hasFestivo = true;
				}
				
				// Validamos si hay negociacion, es decir, tratar el dia festivo como normal
				if (verificaDiaFestivoNegociacion(dia, lista.getIdObra())) {
					diaFestivo = 0;
					diaFestivoFactor = 0;
				}
				
				// Iteracion de asistencias
				for (ChecadorDetalle asistencia : asistencias) {
					// Comprobamos el registro existente
					item = null;
					formatter.applyPattern("MM-dd-yyyy");
					if (regExistentes != null && ! regExistentes.isEmpty()) {
						for (EmpleadoNomina en : regExistentes) {
							if (asistencia.getIdEmpleado().equals(en.getIdEmpleado().getId()) && formatter.format(asistencia.getFecha()).equals(formatter.format(en.getFecha()))) {
								item = en;
								break;
							} 
						}
					}
					
					// Comprobamos empleado 
					if (item == null)
						empleado = this.ifzEmpleados.findById(asistencia.getIdEmpleado());
					else
						empleado = item.getIdEmpleado();
					if (empleado == null || empleado.getId() == null || empleado.getId() <= 0L){
						log.info("Asistencia " + asistencia.getId() + " : Empleado " + asistencia.getIdEmpleado() + " NO ENCONTRADO");
						continue;
					}
					
					// Comprobamos contrato
					contrato = null;
					if (mapContratos.containsKey(empleado.getId()))
						contrato = mapContratos.get(empleado.getId()); 
					if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L)
						contrato = this.ifzContratos.findContrato(empleado.getId());  
					if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L)
						contrato = this.ifzContratos.findContrato(empleado.getId(), fechaDesde, fechaHasta); 
					if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L) {
						log.info("Asistencia " + asistencia.getId() + " : Empleado " + asistencia.getIdEmpleado() + " SIN CONTRATO");
						continue;
					}
					
					if (! mapContratos.containsKey(empleado.getId()))  {
						// calculando Sueldo Hora de contrato
						sueldoNormal = contrato.getSueldo();
						sueldoNormal = ((sueldoNormal / 7) / 8);
						contrato.setSueldoHora(new BigDecimal((new DecimalFormat("#0.0000")).format(sueldoNormal)));
						mapContratos.put(empleado.getId(), contrato);
					}
					
					// Comprobamos incapacidad
					if (this.ifzIncapacidades.verificarIncapacidad(asistencia.getIdEmpleado(), lista.getFecha())) {
						log.info("Asistencia " + asistencia.getId() + " : Empleado " + asistencia.getIdEmpleado() + " INCAPACITADO");
						continue;
					}

					// Inicializacion
					formatter.applyPattern("HH");
					horasJornada = contrato.getHorasJornada();
					horasTrabajadas = 0;
					horasNormales = 0;
					horasExtras = 0;
					sueldoNormal = 0;
					sueldoExtra = 0;
					proporcionSeptimoDia = 0;
					montoDescuentos = 0;
					montoInfonavit = 0;
					sueldoNeto = 0;
					diaDescanso = 0;
					
					// Añadimos Dia Trabajado por empleado
					if (! diasTrabajados.containsKey(empleado.getId()))
						diasTrabajados.put(empleado.getId(), 0);
					diasTrabajados.put(empleado.getId(), diasTrabajados.get(empleado.getId()) + 1);
					
					// Validamos dia descando
					diaDescanso = verificaDiaDescanso(lista.getFecha(), contrato.getDiaDescanso());
					
					// Calculo y validacion de horas
					horasTrabajadas = asistencia.getHorasTrabajadas();
					if (asistencia.getHorasExtraAutorizadas() != null && asistencia.getUsuarioAutoriza() > 0) 
						horasExtras = Integer.parseInt(formatter.format(asistencia.getHorasExtraAutorizadas()));
					horasNormales = horasTrabajadas - horasExtras;
					if (horasNormales > horasJornada || (horasNormales < horasJornada && horasTrabajadas >= horasJornada))
						horasNormales = horasJornada;
					horasNormales = (horasNormales < 0 ? 0 : horasNormales);
					horasExtras   = (horasExtras   < 0 ? 0 : horasExtras);
					if (diaFestivo == 1) // Si trabajo el dia destivo la multiplica por el factor asignado al dia, si no, pago dia normal
						horasNormales = ((horasNormales == 0) ? horasJornada : (horasNormales * diaFestivoFactor));

					// Calculo de sueldo diario, Si trabaja su dia de descanso, se paga la hora de trabajo como hora extra
					sueldoNormal = contrato.getSueldoHora().doubleValue() * horasNormales;
					if (sueldoNormal > 0 && diaDescanso == 1)
						sueldoNormal = contrato.getSueldoHoraExtra().doubleValue() * horasNormales;
					sueldoExtra = contrato.getSueldoHoraExtra().doubleValue() * horasExtras;
					sueldoNeto  = (sueldoNormal + sueldoExtra);
					
					// Calculamos el proporcional del Septimo dia si corresponde
					if (diaDescanso == 0)
						proporcionSeptimoDia = ((contrato.getSueldoHora().doubleValue() * horasJornada) / 6);
					
					// descuentos
					descuentos = this.ifzDescuentos.comprobarDescuentosPorFecha(asistencia.getIdEmpleado(), asistencia.getFecha());
					if (descuentos != null && ! descuentos.isEmpty()) {
						for (EmpleadoDescuento descuento : descuentos)
							montoDescuentos += descuento.getMonto().doubleValue();
					}
					
					// infonavit
					//infonavitTmp = (infonavitTmp != null ? infonavitTmp : new ArrayList<Long>());
					infonavit = this.ifzInfonavit.comprobarPorFecha(empleado.getId(), lista.getFecha());
					if (infonavit != null && ! infonavit.isEmpty()) {
						for (EmpleadoInfonavit var : infonavit) {
							/*if (infonavitTmp.contains(var.getId()))
								continue;
							infonavitTmp.add(var.getId());*/
							montoInfonavit += var.getMonto().doubleValue();
						}
					}
					
					// Calculamos el sueldo neto del dia
					sueldoNeto -= (montoDescuentos + montoInfonavit);
					
					// Generamos/Actualizamos el registro de nomina 
					if (item == null) {
						item = new EmpleadoNomina();
						item.setIdEmpleado(empleado);
						item.setTipo(0);
						item.setCreadoPor(nominaEstatus.getCreadoPor());
						item.setFechaCreacion(Calendar.getInstance().getTime());
					} 
					
					if (diaDescanso == 1) {
						sueldoExtra += sueldoNormal;
						horasExtras += horasNormales;
						sueldoNormal = 0;
						horasNormales = 0;
					}
					
					item.setFecha(asistencia.getFecha());
					item.setDiaDescanso(diaDescanso);
					item.setDiaFestivo(diaFestivo);
					item.setFactor(diaFestivoFactor);
					item.setIdContrato(contrato.getId());
					item.setIdPeriodicidad(contrato.getPeriodicidadPago());
					item.setIdObra(lista.getIdObra());
					item.setHorasTrabajadas(horasNormales);
					item.setHorasExtras(horasExtras);
					item.setPagoNormal(new BigDecimal(sueldoNormal));
					item.setSeptimoDia(new BigDecimal((new DecimalFormat("#0.0000")).format(proporcionSeptimoDia)));
					item.setPagoExtra(new BigDecimal(sueldoExtra));
					item.setDescuento(new BigDecimal(montoDescuentos));
					item.setInfonavit(new BigDecimal(montoInfonavit));
					item.setPagoNeto(new BigDecimal(sueldoNeto));
					item.setIdEmpresa(nominaEstatus.getIdEmpresa());
					item.setObservaciones("");
					item.setModificadoPor(nominaEstatus.getCreadoPor());
					item.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Añado a listado
					nomina.add(item);
				}
			}
			
			// Priorizamos Dia festivo si corresponde
			if (hasFestivo) {
				mapNominaIndice = new HashMap<Long, Integer>();
				mapNominaBorrable = new ArrayList<Integer>();
				mapNominaFestivoValido = new HashMap<Integer, EmpleadoNomina>();
				for (EmpleadoNomina nom : nomina) {
					if (nom.getDiaFestivo() == 0)
						continue;
					
					if (! mapNominaIndice.containsKey(nom.getIdEmpleado().getId())) {
						mapNominaIndice.put(nom.getIdEmpleado().getId(), nomina.indexOf(nom));
						mapNominaFestivoValido.put(nomina.indexOf(nom), nom);
						continue;
					}
					
					if (nom.getHorasTrabajadas() == 16 && mapNominaFestivoValido.get(mapNominaIndice.get(nom.getIdEmpleado().getId())).getHorasTrabajadas() == nom.getHorasTrabajadas()) {
						nom.setHorasTrabajadas(0); 
						nom.setPagoNormal(BigDecimal.ZERO);
						nom.setSeptimoDia(BigDecimal.ZERO);
						nom.setHorasExtras(0); 
						nom.setPagoExtra(BigDecimal.ZERO);
						continue;
					}
					
					if (nom.getHorasTrabajadas() != 8 && mapNominaFestivoValido.get(mapNominaIndice.get(nom.getIdEmpleado().getId())).getHorasTrabajadas() == nom.getHorasTrabajadas()) {
						nom.setHorasTrabajadas(0); 
						nom.setPagoNormal(BigDecimal.ZERO);
						nom.setSeptimoDia(BigDecimal.ZERO);
						nom.setHorasExtras(0); 
						nom.setPagoExtra(BigDecimal.ZERO);
						continue;
					}
					
					if (nom.getHorasTrabajadas() == 8 && mapNominaFestivoValido.get(mapNominaIndice.get(nom.getIdEmpleado().getId())).getHorasTrabajadas() == nom.getHorasTrabajadas()) {
						nom.setHorasTrabajadas(0); 
						nom.setPagoNormal(BigDecimal.ZERO);
						nom.setSeptimoDia(BigDecimal.ZERO);
						nom.setHorasExtras(0); 
						nom.setPagoExtra(BigDecimal.ZERO);
						continue;
					}
					
					mapNominaBorrable.add(mapNominaIndice.get(nom.getIdEmpleado().getId()));
					mapNominaFestivoValido.remove(mapNominaIndice.get(nom.getIdEmpleado().getId()));
					mapNominaIndice.put(nom.getIdEmpleado().getId(), nomina.indexOf(nom));
					mapNominaFestivoValido.put(nomina.indexOf(nom), nom);
				}
				
				if (! mapNominaBorrable.isEmpty()) {
					for (Integer index : mapNominaBorrable) {
						item = nomina.get(index);
						item.setHorasTrabajadas(0); 
						item.setPagoNormal(BigDecimal.ZERO);
						item.setSeptimoDia(BigDecimal.ZERO);
						item.setHorasExtras(0); 
						item.setPagoExtra(BigDecimal.ZERO);
						nomina.set(index, item);
					}
				}
			}
			
			// Consolidamos pago dia donde corresponda
			mensajeLog("Consolidando pago dia ... ");
			mapEmpleadoFestivo = new HashMap<Long, Integer>();
			for (EmpleadoNomina nom : nomina) {
				if (diasTrabajados.containsKey(nom.getIdEmpleado().getId())) {
					//montoDescuentos = nom.getDescuento().doubleValue() / diasTrabajados.get(nom.getIdEmpleado().getId());
					//nom.setDescuento(new BigDecimal(montoDescuentos));
					montoInfonavit = nom.getInfonavit().doubleValue() / diasTrabajados.get(nom.getIdEmpleado().getId());
					nom.setInfonavit(new BigDecimal(montoInfonavit));
				}
				
				// Validamos pago normal segun horas normales trabajadas
				if (nom.getHorasTrabajadas() <= 0 && nom.getDiaDescanso() == 0) {
					nom.setHorasTrabajadas(0); 
					nom.setPagoNormal(BigDecimal.ZERO);
					nom.setSeptimoDia(BigDecimal.ZERO);
				}
				
				// Validamos pago extra segun horas extras trabjadas
				if (nom.getHorasExtras() <= 0) {
					nom.setHorasExtras(0);  
					nom.setPagoExtra(BigDecimal.ZERO);
				}
				
				// Si el dia es festivo
				if (nom.getDiaFestivo() == 1) {
					horasDiaFestivo = 0;
					if (mapEmpleadoFestivo.containsKey(nom.getIdEmpleado().getId())) {
						horasDiaFestivo = mapEmpleadoFestivo.get(nom.getIdEmpleado().getId());
						if (horasDiaFestivo >= 8) {
							nom.setHorasTrabajadas(0);
							nom.setHorasExtras(0);
							nom.setPagoNormal(BigDecimal.ZERO);
							nom.setPagoExtra(BigDecimal.ZERO);
							nom.setSeptimoDia(BigDecimal.ZERO);
							nom.setDescuento(BigDecimal.ZERO);
							nom.setInfonavit(BigDecimal.ZERO);
							nom.setPagoNeto(BigDecimal.ZERO);
							continue;
						}
					}
					
					horasDiaFestivo += nom.getHorasTrabajadas();
					mapEmpleadoFestivo.put(nom.getIdEmpleado().getId(), horasDiaFestivo);
				}
				
				// Recupero datos
				sueldoNormal = nom.getPagoNormal().doubleValue();
				sueldoExtra = nom.getPagoExtra().doubleValue();
				proporcionSeptimoDia = nom.getSeptimoDia().doubleValue();
				montoDescuentos = nom.getDescuento().doubleValue();
				montoInfonavit = nom.getInfonavit().doubleValue();
				sueldoNeto = nom.getPagoNeto().doubleValue();
				
				// Recalculo y asigno sueldo neto
				sueldoNeto = (sueldoNormal + sueldoExtra + proporcionSeptimoDia) - (montoDescuentos + montoInfonavit);
				nom.setPagoNeto(new BigDecimal(sueldoNeto));
			}
			
			// Guardamos Nomina
			mensajeLog("Guardando Nomina preliminar ... ");
			log.info("Guardando Nomina preliminar " + idNominaEstatus);
			if (! nomina.isEmpty()) {
				this.ifzNomina.setInfoSesion(this.infoSesion);
				this.ifzNomina.saveOrUpdateListPreliminar(nomina);
			}
			
			mensajeLog("Nomina preliminar " + idNominaEstatus + " terminada!");
			if (estatusNomina == 0) {
				estatusNomina = 2;
				mensajePeticion = "OK";
			}
		} catch (Exception e) {
			mensajeLog("ERROR : Ocurrio un problema al intentar generar Nomina preliminar\n" + e.getMessage(), true);
			mensajePeticion = "ERROR: " + e.getMessage();
			estatusNomina = 1;
			throw e;
		} finally {
			// Actualizamos estatus de peticion
			try {
				nominaEstatus.setEstatus(estatusNomina);
				nominaEstatus.setMensaje(mensajePeticion);
				nominaEstatus.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzNominaEstatus.update(nominaEstatus);
			} catch (Exception e) {
				log.error("Ocurrio un problema al actualizar el estatus de la Peticion: " + this.idTopicEstatus, e);
				throw e;
			}
		}
	}
	
	private void nominaQuincenal(long idNominaEstatus, long nominaPreliminar) throws Exception {
		mensajeLog(" -----> Evento EM-NOMINA15: BackOffice RH - Generar Nomina Quincenal");
		mensajeLog(" -----> Nomina     : " + idNominaEstatus);
		mensajeLog(" -----> Preliminar : " + (nominaPreliminar == 1 ? "SI" : "NO"));
		if (idNominaEstatus <= 0L) {
			topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
			return;
		}
		
		if (nominaPreliminar == 1) {
			this.nominaQuincenalPreliminar(idNominaEstatus);
			return;
		}
	}

	private void nominaQuincenalPreliminar(long idNominaEstatus) throws Exception {

	}
	
	private void contratosVencidos(Date fecha) throws Exception {
		SimpleDateFormat dateFormat = null;
		List<EmpleadoContrato> contratos = null;
		List<EmpleadoContrato> vencidos = null;
		// -------------------------------------
		List<EmpleadoFiniquito> finiquitos = null;
		EmpleadoFiniquito finiquito = null;
		EmpleadoFiniquito finStored = null;
		Empleado empleado = null;
		int numFiniquito = 0;
		// -------------------------------------
		List<String> empleados = null;
		HashMap<Long, Long> mapSolicitantes = null;
		long idSolicitante = 0L;
		
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			mensajeLog(" -----> Evento RH_CONTRATOS_VENCIDOS: BackOffice RH - Comprobacion de Contratos Vencidos");
			mensajeLog(" -----> Fecha : " + dateFormat.format(fecha));
			if ("".equals(dateFormat.format(fecha))) {
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Inicializamos 
			vencidos = new ArrayList<EmpleadoContrato>();
			
			// Recuperamos contratos
			mensajeLog("Recuperando Contratos ... ");
			contratos = this.ifzContratos.findAll(0L, "model.fechaFin, model.idEmpleado.nombre", false, false);
			if (contratos == null || contratos.isEmpty()) {
				topicSinAccion("Ningun Contrato activo encontrado");
				return;
			}
			mensajeLog("OK --> " + contratos.size() + " activos", true);

			mensajeLog("Validando vencimiento de Contratos ... ");
			empleados = new ArrayList<String>();
			for (EmpleadoContrato contrato : contratos) {
				// Excluimos contratos indeterminados 
				if (contrato.getFechaFin() == null || contrato.getDeterminado() == 0)
					continue;
				// Comprobamos que la fecha de terminacion del contrato es la del parametro
				if (fecha.compareTo(contrato.getFechaFin()) < 0) 
					continue;
				
				// Actualizamos contrato para indicar que esta vencido
				contrato.setEstatus(1);
				contrato.setModificadoPor(1L);
				contrato.setFechaModificacion(Calendar.getInstance().getTime());
				// Añadimos a listado de vencidos
				vencidos.add(contrato);
				if (! empleados.contains(contrato.getIdEmpleado().getId().toString()))
					empleados.add(contrato.getIdEmpleado().getId().toString());
			}
			
			contratos.clear();
			if (vencidos == null || vencidos.isEmpty()) {
				topicSinAccion("Ningun Contrato vencido");
				return;
			}
			mensajeLog("OK --> " + vencidos.size() + " vencidos", true);

			// Encontramos Solicitantes (Ingenieros) de finiquitos ... 
			mensajeLog("Encontrando Solicitantes (Ingenieros) ... ");
			mapSolicitantes = encontrarSolicitantes(empleados);
			mensajeLog("OK", true);
			
			// Generamos Finiquitos correspondientes
			numFiniquito = 0;
			finiquitos = new ArrayList<EmpleadoFiniquito>();
			mensajeLog("Generando Finiquitos ... ");
			for (EmpleadoContrato contrato : vencidos) {
				try {
					numFiniquito++;
					finStored = this.ifzFiniquitos.findByIdEmpleado(contrato.getIdEmpleado().getId(), contrato.getId());
					if (finStored != null) {
						log.info("Finiquito [" + numFiniquito + "/" + vencidos.size() + "] de " + empleado.getId() + " - " + empleado.getNombre() + " (" + empleado.getClave() + ") para el Contrato " + contrato.getId() + ". Finiquito previo: " + finStored.getId());
						continue;
					}

					empleado = contrato.getIdEmpleado();
					idSolicitante = (mapSolicitantes.containsKey(empleado.getId()) ? mapSolicitantes.get(empleado.getId()) : 1L);

					finiquito = new EmpleadoFiniquito();
					finiquito.setIdEmpleado(empleado);
					finiquito.setIdContrato(contrato.getId());
					finiquito.setFechaSolicitudBaja(contrato.getFechaFin());
					finiquito.setSolicitadoPor(idSolicitante);
					finiquito.setMonto(0);
					finiquito.setObservaciones("Solicitud automatica por Vencimiento de Contrato");
					finiquito.setVoBoRh(0);
					finiquito.setVoBoRhPor(0L);
					finiquito.setVoBoRhFecha(null);
					finiquito.setAprobacion(0);
					finiquito.setAprobacionPor(0L);
					finiquito.setAprobacionFecha(null);
					finiquito.setEstatus(0);
					finiquito.setCreadoPor(1L);
					finiquito.setFechaCreacion(Calendar.getInstance().getTime());
					finiquito.setModificadoPor(1L);
					finiquito.setFechaModificacion(Calendar.getInstance().getTime());
					finiquitos.add(finiquito);
					log.info("Finiquito [" + numFiniquito + "/" + vencidos.size() + "] de " + empleado.getId() + " - " + empleado.getNombre() + " (" + empleado.getClave() + ") para el Contrato " + contrato.getId());
				} catch (Exception e) {
					mensajeLog("ERROR Finiquito [" + numFiniquito + "/" + vencidos.size() + "] de " + empleado.getId() + " - " + empleado.getNombre() + " (" + empleado.getClave() + ") para el Contrato " + contrato.getId());
					mensajeLog("EXCEPTION\n" + e.getMessage() + "\n");
				} 
			}
			mensajeLog("OK --> x" + finiquitos.size(), true);

			// Guardamos finiquitos si corresponde
			mensajeLog("Guardando Finiquitos ... ");
			if (finiquitos != null && ! finiquitos.isEmpty())
				this.ifzFiniquitos.saveOrUpdateList(finiquitos);
			mensajeLog("OK", true);

			// Actualizando contratos si corresponde
			mensajeLog("Actualizando Contratos ... ");
			if (vencidos != null && ! vencidos.isEmpty())
				this.ifzContratos.saveOrUpdateList(vencidos);
			mensajeLog("OK", true);
			
			mensajeLog("Cancelacion de Contratos vencidos terminado!");
		} catch (Exception e) {
			mensajeLog("ERROR : Ocurrio un problema al intentar cancelar los Contratos vencidos\n" + e.getMessage(), true);
			throw e;
		}
	}
	
	private void contratosNuevo(long idEmpleado, long idContrato) throws Exception {
		List<EmpleadoContrato> contratos = null;
		int estatus = 0;
		
		try {
			mensajeLog(" -----> Evento RH_CONTRATOS_NUEVO: BackOffice RH - Actualizacion de Contratos anteriores");
			mensajeLog(" -----> Empleado : " + idEmpleado);
			mensajeLog(" -----> Contrato : " + idContrato);
			if (idEmpleado <= 0L) {
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recuperamos contratos
			mensajeLog("Recuperamos Contratos ... ");
			contratos = this.ifzContratos.findAll(idEmpleado, "model.id desc", true, false);
			if (contratos == null || contratos.isEmpty()) 
				throw new Exception("No se pudo recuperar los Contratos para el Empleado indicado");
			
			// Iteramos los contratos
			for (EmpleadoContrato contrato : contratos) {
				if (0 == contratos.indexOf(contrato) && idContrato > 0L && idContrato == contrato.getId().longValue())
					continue;
				if (estatus <= 1)
					estatus++;
				contrato.setDeterminado(1);
				contrato.setFechaFin(Calendar.getInstance().getTime());
				contrato.setEstatus(estatus);
				contrato.setModificadoPor(1L);
				contrato.setFechaModificacion(Calendar.getInstance().getTime());
			}
			mensajeLog("Actualizacion de Contratos terminado!");
		} catch (Exception e) {
			mensajeLog("ERROR : Ocurrio un problema al intentar cancelar los contratos anteriores del Empleado\n" + e.getMessage(), true);
			throw e;
		}
	}
	
	private void empleadoIncapacitado(long idIncapacidad) throws Exception {
		EmpleadoIncapacidad incapacidad = null;
		
		try {
			mensajeLog(" -----> Evento RH_EMPLEADOS_INCAPACIDAD : BackOffice RH - Cambia estatus de Empleados a incapacitado con Incapacidad indicada");
			mensajeLog(" -----> Incapacidad : " + idIncapacidad);
			if (idIncapacidad <= 0L) {
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}

			// Recuperamos empleado
			mensajeLog("Recuperamos empleado ... ");
			incapacidad = this.ifzIncapacidades.findById(idIncapacidad);
			if (incapacidad == null || incapacidad.getId() == null || incapacidad.getId() <= 0L) {
				topicSinAccion(" -----> Sin accion. Incapacidad no encontrada: " + incapacidad);
				return;
			}
			
			// Actualizamos estatus de empleado
			bajaEmpleado(incapacidad.getIdEmpleado().getId(), incapacidad.getModificadoPor(), 3);
		} catch (Exception e) {
			mensajeLog("ERROR : Ocurrio un problema al intentar cancelar los contratos vencidos\n" + e.getMessage(), true);
			throw e;
		}
	}

	private void empleadoCancelarIncapacidad(long idIncapacidad) throws Exception {
		EmpleadoIncapacidad incapacidad = null;
		Empleado empleado = null;
		
		try {
			mensajeLog(" -----> Evento RH_EMPLEADOS_CANCELAR_INCAPACIDAD : BackOffice RH - Elimina/Cancela Incapacidad de Empleados indicada");
			mensajeLog(" -----> Incapacidad : " + idIncapacidad);
			if (idIncapacidad <= 0L) {
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}

			// Recuperamos empleado
			mensajeLog("Recuperamos empleado ... ");
			incapacidad = this.ifzIncapacidades.findById(idIncapacidad);
			if (incapacidad == null || incapacidad.getId() == null || incapacidad.getId() <= 0L) {
				topicSinAccion(" -----> Sin accion. Incapacidad no encontrada: " + incapacidad);
				return;
			}
			
			// Actualizamos estatus de empleado
			mensajeLog("Actualizando estatus de empleado ... ");
			empleado = incapacidad.getIdEmpleado();
			empleado.setEstatus(0);
			empleado.setModificadoPor(1L);
			empleado.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzEmpleados.update(empleado);
			mensajeLog("Actualizacion de estatus de Empleado terminado!");
		} catch (Exception e) {
			mensajeLog("ERROR : Ocurrio un problema al intentar cancelar Incapacidad\n" + e.getMessage(), true);
			throw e;
		}
	}
	
	private void empleadoCancelarIncapacidades(Date fecha) throws Exception {
		SimpleDateFormat dateFormat = null;
		List<EmpleadoIncapacidad> incapacidades = null;
		List<Empleado> empleados = null;
		
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			mensajeLog(" -----> Evento RH_EMPLEADOS_CANCELAR_INCAPACIDADES : BackOffice RH - Elimina/Cancela Incapacidades de Empleados");
			mensajeLog(" -----> Empleado : Todos");
			mensajeLog(" -----> Fecha    : " + dateFormat.format(fecha));
			if ("".equals(dateFormat.format(fecha))) {
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recuperamos contratos
			mensajeLog("Recuperamos Incapacidades activas ... ");
			incapacidades = this.ifzIncapacidades.comprobarParaCancelar(fecha);
			if (incapacidades == null || incapacidades.isEmpty()) {
				topicSinAccion(" -----> Sin incapacidades para cancelar ");
				return;
			}
			
			// Iteramos las Incapacidades
			empleados = new ArrayList<Empleado>();
			for (EmpleadoIncapacidad incapacidad : incapacidades) {
				incapacidad.setEstatus(1);
				incapacidad.setModificadoPor(1L);
				incapacidad.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Añadimos a listados de empleados para habilitar, si corresponde
				if (incapacidad.getIdEmpleado().getEstatus() == 3)
					empleados.add(incapacidad.getIdEmpleado());
			}
			
			this.ifzIncapacidades.saveOrUpdateList(incapacidades);
			mensajeLog("Eliminacion/Cancelacion de Incapacidades terminado!");
			
			// Actualizamos estatus de Empleados involucrados en las Incapacidades
			mensajeLog("Actualizando estatus de Empleados ... ");
			for (Empleado empleado : empleados) {
				empleado.setEstatus(0);
				empleado.setModificadoPor(1L);
				empleado.setFechaModificacion(Calendar.getInstance().getTime());
			}
			this.ifzEmpleados.saveOrUpdateList(empleados);
			mensajeLog("Actualizacion de estatus de Empleados terminado!");
		} catch (Exception e) {
			mensajeLog("ERROR : Ocurrio un problema al intentar cancelar las incapacidades activas\n" + e.getMessage(), true);
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void asistenciaDiaFestivo(Date fecha) throws Exception {
		SimpleDateFormat dateFormat = null;
		List<Checador> listas = null;
		List<ChecadorDetalle> asistencias = null;
		Checador lista = null;
		ChecadorDetalle asistencia = null;
		// --------------------------------------------------------
		Respuesta respuesta = null;
		HashMap<Long, List<Long>> mapObraEmpleados = null;
		HashMap<Long,EmpleadoExt> mapEmpleados = null;
		HashMap<Long,String> mapObras = null;
		List<Long> empleados = null;
		
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			mensajeLog(" -----> Evento RH_ASISTENCIA_DIA_FESTIVO : BackOffice RH - Genera Asistencia en Dia Festivo para Empleados semanales");
			mensajeLog(" -----> Fecha    : " + dateFormat.format(fecha));
			mensajeLog(" -----> Empleado : Todos");
			if ("".equals(dateFormat.format(fecha))) {
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Valido fecha
			mensajeLog("Validando fecha ... ");
			if (! this.ifzFestivos.validarDiaFestivo(fecha)) {
				topicSinAccion(" -----> Sin accion por Dia Normal ");
				return;
			}
			mensajeLog("OK", true);
			
			// Recupero los Empleados asignados a las Obra
			mensajeLog("Recuperando asignanes de Empleados en Obras ... ");
			respuesta = obtenerAsignacionesEmpleadosObras(fecha);
			if (respuesta == null) { 
				topicSinAccion(" -----> Sin accion por Asignaciones de Obras no recuperadas ");
				return;
			}
			
			mapObraEmpleados = (HashMap<Long, List<Long>>) respuesta.getBody().getValor("asignaciones");
			mapEmpleados = (HashMap<Long,EmpleadoExt>) respuesta.getBody().getValor("empleados");
			mapObras = (HashMap<Long,String>) respuesta.getBody().getValor("obras");
			mensajeLog("OK", true);
			
			// Genero las listas de Asistencias
			mensajeLog("Generando listas de Asistencias ... ");
			listas = new ArrayList<Checador>();
			for (Entry<Long, String> obra : mapObras.entrySet()) {
				lista = new Checador();
				lista.setId(0L);
				lista.setFecha(fecha);
				lista.setIdObra(obra.getKey());
				lista.setNombreObra(obra.getValue());
				lista.setNombreArchivo("");
				lista.setIdEmpresa(1L);
				lista.setEstatus(0);
				lista.setCreadoPor(1L);
				lista.setFechaCreacion(Calendar.getInstance().getTime());
				lista.setModificadoPor(1L);
				lista.setFechaModificacion(Calendar.getInstance().getTime());
				listas.add(lista);
			}
			mensajeLog("OK", true);

			// Guardo las listas
			if (listas == null || listas.isEmpty()) { 
				topicSinAccion(" -----> Sin accion por Listas no inicializadas ");
				return;
			}

			mensajeLog("Guardando listas de Asistencias ... ");
			listas = this.ifzListas.saveOrUpdateList(listas);
			mensajeLog("OK", true);
			
			// Genero las Asistencias de Empleados
			mensajeLog("Generando Asistencias de Empleados ... ");
			asistencias = new ArrayList<ChecadorDetalle>();
			for (Checador checador : listas) {
				if (checador.getId() == null || checador.getId() <= 0L)
					continue;
				if (! mapObraEmpleados.containsKey(checador.getIdObra()))
					continue;
				empleados = mapObraEmpleados.get(checador.getIdObra());
				for (Long idEmpleado : empleados) {
					if (! mapEmpleados.containsKey(idEmpleado))
						continue;
					asistencia = generaAsistencia(checador, mapEmpleados.get(idEmpleado));
					if (asistencia == null)
						continue;
					asistencias.add(asistencia);
				}
			}
			mensajeLog("OK", true);
			
			// Guardo las asistencias
			mensajeLog("Guardando Asistencias ... ");
			if (asistencias != null && ! asistencias.isEmpty())
				this.ifzAsistencias.saveOrUpdateList(asistencias);
			mensajeLog("OK", true);
		} catch (Exception e) {
			mensajeLog("ERROR : Ocurrio un problema al hacer registros de asisntencia en dia festivo\n" + e.getMessage(), true);
			throw e;
		}
	}
	
	private void invalidadorNegociacionesDiasFestivos(Date fecha, long idObra) throws Exception {
		List<DiasFestivosNegociacion> negociaciones = null;
		SimpleDateFormat dateFormat = null;
		
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			mensajeLog(" -----> Evento RH_CANCELAR_NEGOCIACIONES_DIAS_FESTIVOS : BackOffice RH - Cancela las Negociaciones (pospuesto) realizadas en Obras para los dias festivos");
			mensajeLog(" -----> Fecha    : " + dateFormat.format(fecha));
			mensajeLog(" -----> Obra     : " + (idObra > 0L ? String.valueOf(idObra) : "Todas"));
			mensajeLog(" -----> Empleado : Todos");
			if ("".equals(dateFormat.format(fecha))) {
				topicSinAccion(" -----> Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recupero negociaciones que se pueden cancelar
			mensajeLog("Recupero negociaciones cancelables ... ");
			negociaciones = this.ifzNegociaciones.cancelables(fecha, idObra);
			if (negociaciones == null || negociaciones.isEmpty()) {
				topicSinAccion(" -----> Sin accion por Negociaciones no disponibles para cancelar ");
				return;
			}
			mensajeLog("OK", true);

			mensajeLog("Cancelando negociaciones ... ");
			for (DiasFestivosNegociacion negociacion : negociaciones) {
				negociacion.setEstatus(1);
				negociacion.setModificadoPor(1L);
				negociacion.setFechaModificacion(Calendar.getInstance().getTime());
			}
			mensajeLog("OK", true);
			
			// Guardo las asistencias
			mensajeLog("Guardando cambios ... ");
			if (negociaciones != null && ! negociaciones.isEmpty())
				this.ifzNegociaciones.saveOrUpdateList(negociaciones);
			mensajeLog("OK", true);
		} catch (Exception e) {
			mensajeLog("ERROR : Ocurrio un problema al intentar invalidar las Negociaciones de Dias Festivos de la Obra " + idObra + "\n" + e.getMessage(), true);
			throw e;
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// METODOS PRIVADOS
	// ----------------------------------------------------------------------------------------

	/*private void autoMessage(TopicEventosRH evento, Object target, Object referencia, Object atributos) {
		MensajeTopic msgTopic = null;
		
		try {
			if (evento == null)
				return;
			target = (target != null ? target : "");
			referencia = (referencia != null ? referencia : "");
			atributos = (atributos != null ? atributos : "");
			msgTopic = new MensajeTopic(evento, target.toString(), referencia.toString(), atributos.toString(), this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) { 
			log.error("Ocurrio un problema al autoenviar mensaje JMS\n" + this.getClass().getCanonicalName() + ".autoMessage(evento, target, referencia, atributos)", e);
		} 
	}*/

	private long valueToLong(Object value) {
		String strValue = "";
		long resultado = 0L;
		
		if (value != null) {
			strValue = value.toString();
			strValue = ("".equals(strValue.trim()) ? "0" : strValue.trim());
			resultado = Long.valueOf(strValue);
		}
		
		return resultado;
	}

	private String valueToString(Object value) {
		String resultado = "";
		
		if (value != null) {
			resultado = value.toString();
			resultado = ("".equals(resultado.trim()) ? "0" : resultado.trim());
		}
		
		return resultado;
	}

	private Date valueToDate(Object value) {
		return valueToDate(value, null, null);
	}

	private Date valueToDate(Object value, String formato) {
		return valueToDate(value, null, formato);
	}

	private Date valueToDate(Object value, Date fechaDefault, String formato) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String strValue = "";
		Date resultado = null;
		
		try {
			if (formato == null || "".equals(formato.trim()))
				formato = "dd-MM-yyyy";
			strValue = valueToString(value);
			if (! "".equals(strValue)) {
				dateFormat = new SimpleDateFormat(formato);
				resultado = dateFormat.parse(strValue);
			}
			
			if (resultado == null)
				resultado = fechaDefault;
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar convertir el parametro en fecha", e);
			resultado = null;
		}
		
		return resultado;
	}
	
	private Date getIniDayOfWeek(Date fechaBase) {
		Calendar resultado = Calendar.getInstance();
		String dia = "";
		int diaActual = 0;
		int diaInicial = 0;
		int diferencia = 0;
		
		dia = getPerfilValue("SYS_SEMANA_LABORAL_OBRA_INICIO");
		if (dia == null || "".equals(dia.trim()))
			dia = "Domingo";
		switch (dia) {
			case "Domingo": diaInicial = 1; break;
			case "Lunes": diaInicial = 2; break;
			case "Martes": diaInicial = 3; break;
			case "Miercoles": diaInicial = 4; break;
			case "Jueves": diaInicial = 5; break;
			case "Viernes": diaInicial = 6; break;
			case "Sabado": diaInicial = 7; break;
		}

		resultado.setTime(fechaBase);
		diaActual = resultado.get(Calendar.DAY_OF_WEEK);

		if (diaActual > diaInicial) 
			diferencia = diaActual - diaInicial;
		else if (diaActual < diaInicial)
			diferencia = (diaActual + 7) - diaInicial;
		else
			return resultado.getTime();

		diferencia *= -1;
		resultado.add(Calendar.DAY_OF_YEAR, diferencia);
		return resultado.getTime();
	}

	private Date getEndDayOfWeek(Date fechaBase) {
		Calendar resultado = Calendar.getInstance();
		String dia = "";
		int diaActual = 0;
		int diaFinal = 0;
		int diferencia = 0;
		
		dia = getPerfilValue("SYS_SEMANA_LABORAL_OBRA_FIN");
		if (dia == null || "".equals(dia.trim()))
			dia = "Sabado";
		switch (dia) {
			case "Domingo": diaFinal = 1; break;
			case "Lunes": diaFinal = 2; break;
			case "Martes": diaFinal = 3; break;
			case "Miercoles": diaFinal = 4; break;
			case "Jueves": diaFinal = 5; break;
			case "Viernes": diaFinal = 6; break;
			case "Sabado": diaFinal = 7; break;
		}

		resultado.setTime(fechaBase);
		diaActual = resultado.get(Calendar.DAY_OF_WEEK);

		if (diaActual > diaFinal) 
			diferencia = (diaFinal + 7) - diaActual;
		else if (diaActual < diaFinal)
			diferencia = diaFinal - diaActual;
		else
			return resultado.getTime();

		resultado.add(Calendar.DAY_OF_YEAR, diferencia);
		return resultado.getTime();
	}
	
	@SuppressWarnings("unchecked")
	private String getPerfilValue(String perfilName) {
		List<String> nativeResult = null;
		String queryString = "";
		String resultado = "";
		
		try {
			queryString += "select b.ai from d7729f32ba7 a inner join b761110ccfe b on b.af = a.aa where a.af = ':perfilName' ";
			queryString = queryString.replace(":perfilName", perfilName);
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty())
				resultado = nativeResult.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Perfil indicado: " + perfilName, e);
		}
		
		return resultado;
	}

	@SuppressWarnings("unchecked")
	private boolean interrumpirProcesoNomina(Long idNominaEstatus) {
		List<Short> nativeResult = null;
		String queryString = "";
		Short resultado = 0;
		
		try {
			queryString += "select estatus from empleado_nomina_estatus where id = :idNominaEstatus ";
			queryString = queryString.replace(":idNominaEstatus", idNominaEstatus.toString());
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty())
				resultado = nativeResult.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Perfil indicado: " + idNominaEstatus, e);
			resultado = 1;
		}
		
		return (resultado != null && resultado.intValue() == 1);
	}
	
	private List<Checador> getListasAsistencias(Date fechaDesde, Date fechaHasta) {
		List<Checador> listas = null;
		List<Checador> aux = null;
		
		try {
			listas = this.ifzListas.findByDates(fechaDesde, fechaHasta, 0L, null);
			if (listas != null && ! listas.isEmpty()) {
				aux = new ArrayList<Checador>();
				aux.addAll(listas);
				listas.clear();
				for (Checador lista : aux) {
					if (lista.getEstatus() == 1)
						continue;
					listas.add(lista);
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar las listas de asistencias", e);
			listas = null;
		}
		
		return listas;
	}
	
	@SuppressWarnings({ "unchecked" })
	private List<Checador> encontrarAsistencias(long idObra, long idEmpleado, Date fechaDesde, Date fechaHasta) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<Checador> resultados = null;
		List<BigDecimal> nativeResult = null;
		String queryString = "";
		
		try {
			queryString += "select distinct id from checador ";
			queryString += "where id_obra = :idObra and date(fecha) between date(':fechaDesde') and date(':fechaHasta') and id not in (";
			queryString += "	select distinct y.id_checador ";
			queryString += "	from checador x inner join checador_detalle y on y.id_checador = x.id ";
			queryString += "	where x.id_obra = :idObra and date(x.fecha) between date(':fechaDesde') and date(':fechaHasta') and y.id_empleado = :idEmpleado ";
			queryString += "	order by y.id_checador) ";
			queryString += "order by id";
			
			queryString = queryString.replace(":idObra", String.valueOf(idObra));
			queryString = queryString.replace(":idEmpleado", String.valueOf(idEmpleado));
			queryString = queryString.replace(":fechaDesde", formatter.format(fechaDesde));
			queryString = queryString.replace(":fechaHasta", formatter.format(fechaHasta));
			
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty()) {
				resultados = new ArrayList<Checador>();
				for (BigDecimal item : nativeResult) 
					resultados.add(this.ifzListas.findById(item.longValue()));
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar las Asistencia donde se añadira el Empleado " + idEmpleado, e);
		}
		
		return resultados;
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<Long, Long> encontrarSolicitantes(List<String> empleados) {
		HashMap<Long, Long> resultados = new HashMap<Long, Long>();
		// -------------------------------------------------------------------------------
		List<Object> rows = null;
		Object[] item = null;
		String queryString = "";
		
		try {
			if (empleados == null || empleados.isEmpty())
				return resultados;
			queryString += "select a.id_empleado, b.id_responsable, min(a.id) as id_asignacion ";
			queryString += "from obra_empleado a inner join obra b on b.id_obra = a.id_obra ";
			queryString += "where a.id_empleado in (:empleados) ";
			queryString += "group by a.id_empleado, b.id_responsable ";
			queryString += "order by a.id_empleado, id_asignacion";
			
			queryString = queryString.replace(":empleados", StringUtils.join(empleados, ","));
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty())
				return resultados;
			
			for (Object row : rows) {
				item = (Object[]) row;
				if (resultados.containsKey(((BigDecimal) item[0]).longValue()))
					continue;
				resultados.put(((BigDecimal) item[0]).longValue(), ((BigDecimal) item[1]).longValue());
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar los Encargados de las Obra asignadas a los Empleados", e);
			resultados = new HashMap<Long, Long>();
		}
		
		return resultados;
	}
	
	@SuppressWarnings({ "unchecked" })
	private Respuesta obtenerAsignacionesEmpleadosObras(Date fecha) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Respuesta resultados = null;
		// ----------------------------------------------------
		List<Object> nativeResult = null;
		Object[] item = null;
		String queryString = "";
		// ----------------------------------------------------
		HashMap<Long, List<Long>> mapObraEmpleados = null;
		HashMap<Long,EmpleadoExt> mapEmpleados = null;
		HashMap<Long,String> mapObras = null;
		List<Long> empleados = null;
		Long idObra = 0L;
		Long prevObra = 0L;
		Long idEmpleado = 0L;
		
		try {
			// Consulta para recuperar la asignacion de empleados en obras
			queryString += "select a.id_obra, b.nombre as obra, a.id_empleado ";
			queryString += "from obra_empleado a inner join obra b on b.id_obra = a.id_obra left join empleado_incapacidad c on c.id_empleado = a.id_empleado and date(':fecha') between date(fecha_desde) and date(fecha_hasta) ";
			queryString += "where b.estatus > 0 and b.tipo <> 4 and c.id is null order by b.nombre";
			queryString = queryString.replace(":fecha", formatter.format(fecha));
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty()) {
				// Inicializamos mapas
				mapObraEmpleados = new HashMap<Long, List<Long>>();
				mapEmpleados = new HashMap<Long, EmpleadoExt>();
				mapObras = new HashMap<Long, String>();
				
				// Ciclo para organizar la informacion
				for (Object row : nativeResult) {
					item = (Object[]) row;
					idObra = (Long) item[0];
					idEmpleado = (Long) item[2];
					if (empleados == null)
						empleados = new ArrayList<Long>();
					
					if (! mapObraEmpleados.containsKey(idObra)) {
						if (prevObra <= 0L)
							prevObra = idObra;
						mapObraEmpleados.put(prevObra, empleados);
						
						empleados.clear();
						mapObraEmpleados.put(idObra, empleados);
						prevObra = idObra;
					}
					
					if (! mapObras.containsKey(idObra))
						mapObras.put(idObra, item[1].toString());
					
					if (! mapEmpleados.containsKey(idEmpleado))
						mapEmpleados.put(idEmpleado, this.ifzEmpleados.findByIdExt(idEmpleado));
					
					empleados.add(idEmpleado);
				}

				mapObraEmpleados.put(idObra, empleados);
				
				// Generamos respuesta
				resultados = new Respuesta();
				resultados.getBody().addValor("asignaciones", mapObraEmpleados);
				resultados.getBody().addValor("empleados", mapEmpleados);
				resultados.getBody().addValor("obras", mapObras);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar las Asignaciones de Empleados en Obras", e);
		}
		
		return resultados;
		
	}
	
	private ChecadorDetalle generaAsistencia(Checador checador, EmpleadoExt empleado) {
		SimpleDateFormat dateFullFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		EmpleadoContrato pojoContrato = null;
		ChecadorDetalle asistencia = null;
		String dateString = "";
		
		try {
			asistencia = new ChecadorDetalle();
			asistencia.setIdChecador(checador);
			asistencia.setFecha(checador.getFecha()); 
			asistencia.setIdEmpleado(empleado.getId());
			asistencia.setNombreEmpleado(empleado.getNombrePorApellidos());
			
			// Recuperamos el Contrato activo del empleado
			pojoContrato = this.ifzContratos.findContrato(empleado.getId());
			if (pojoContrato != null) {
				// Entrada
				dateFormatter.applyPattern("yyyy-MM-dd");
				dateString = dateFormatter.format(checador.getFecha()) + " ";
				dateFormatter.applyPattern("HH:mm:ss");
				dateString += dateFormatter.format(pojoContrato.getHoraEntrada());
				asistencia.setHoraEntrada(dateFullFormatter.parse(dateString));
				dateFormatter.applyPattern("yyyy-MM-dd");
				dateString = dateFormatter.format(checador.getFecha()) + " 00:00:00";
				asistencia.setHoraEntradaMarcada(dateFullFormatter.parse(dateString));

				// Salida
				dateFormatter.applyPattern("yyyy-MM-dd");
				dateString = dateFormatter.format(checador.getFecha()) + " ";
				dateFormatter.applyPattern("HH:mm:ss");
				dateString += dateFormatter.format(pojoContrato.getHoraSalida());
				asistencia.setHoraSalida(dateFullFormatter.parse(dateString));
				dateFormatter.applyPattern("yyyy-MM-dd");
				dateString = dateFormatter.format(checador.getFecha()) + " 00:00:00";
				asistencia.setHoraSalidaMarcada(dateFullFormatter.parse(dateString));
				
				// Complemento ...
				if (pojoContrato.getHoraEntradaComplemento() != null && pojoContrato.getHoraSalidaComplemento() != null) {
					// Entrada
					dateFormatter.applyPattern("yyyy-MM-dd");
					dateString = dateFormatter.format(checador.getFecha()) + " ";
					dateFormatter.applyPattern("HH:mm:ss");
					dateString += dateFormatter.format(pojoContrato.getHoraEntradaComplemento());
					asistencia.setHoraEntradaComplemento(dateFullFormatter.parse(dateString));
					//dateFormatter.applyPattern("yyyy-MM-dd");
					//dateString = dateFormatter.format(item.getFecha()) + " 00:00:00";
					//asistencia.setHoraEntradaMarcadaComplemento(dateFullFormatter.parse(dateString));

					// Salida
					dateFormatter.applyPattern("yyyy-MM-dd");
					dateString = dateFormatter.format(checador.getFecha()) + " ";
					dateFormatter.applyPattern("HH:mm:ss");
					dateString += dateFormatter.format(pojoContrato.getHoraSalidaComplemento());
					asistencia.setHoraSalidaComplemento(dateFullFormatter.parse(dateString));
					//dateFormatter.applyPattern("yyyy-MM-dd");
					//dateString = dateFormatter.format(item.getFecha()) + " 00:00:00";
					//asistencia.setHoraSalidaMarcadaComplemento(dateFullFormatter.parse(dateString));
				}
			} else {
				asistencia.setContratoInvalido(true);
				dateFormatter.applyPattern("yyyy-MM-dd");
				// Entrada
				dateString = dateFormatter.format(checador.getFecha()) + " 08:00:00";
				asistencia.setHoraEntrada(dateFullFormatter.parse(dateString));
				dateString = dateFormatter.format(checador.getFecha()) + " 00:00:00";
				asistencia.setHoraEntradaMarcada(dateFullFormatter.parse(dateString));
				
				// Salida
				dateString = dateFormatter.format(checador.getFecha()) + " 16:00:00";
				asistencia.setHoraSalida(dateFullFormatter.parse(dateString));
				dateString = dateFormatter.format(checador.getFecha()) + " 00:00:00";
				asistencia.setHoraSalidaMarcada(dateFullFormatter.parse(dateString));
			}

			dateFormatter.applyPattern("yyyy-MM-dd");
			dateString = dateFormatter.format(checador.getFecha()) + " 00:00:00";
			asistencia.setTiempoAsistido(dateFullFormatter.parse(dateString));
			asistencia.setHorasTrabajadas(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar la Asistencia del Empleado " + empleado.getNombre() + " (" + empleado.getId() + ")", e);
			return null;
		}
		
		return asistencia;
	}

	private int verificaDiaDescanso(Date fecha, int diaSemana) {
		Calendar cal = Calendar.getInstance();
		int dia = 0;
		
		cal.setTime(fecha);
		dia = cal.get(Calendar.DAY_OF_WEEK);
		
		return (dia == diaSemana) ? 1 : 0;
	}
	
	private boolean verificaDiaFestivoNegociacion(DiasFestivos diaFestivo, long idObra) {
		try {
			if (diaFestivo == null || diaFestivo.getId() == null || diaFestivo.getId() <= 0L)
				return false;
			return this.ifzNegociaciones.validarNegociacion(diaFestivo.getId(), idObra);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar verificar las negociaciones de Dias festivos de la Obra " + idObra, e);
			return false;
		}
	}
	
	private void sendBajaEmpleadoEnObra(Long idEmpleado) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idEmpleado == null)
				idEmpleado = 0L;
			
			target = idEmpleado.toString();
			msgTopic = new MensajeTopic(TopicEventosGP.OBRA_QUITAR_EMPLEADO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-CO\n\n" + comando + "\n\n", e);
		}
	}
	
	private void sendDesasignarAlmacenes(Long idEmpleado) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idEmpleado == null)
				idEmpleado = 0L;
			
			target = idEmpleado.toString();
			msgTopic = new MensajeTopic(TopicEventosInventarios.ALMACEN_QUITAR_ENCARGADO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS:BO-DEA\n\n" + comando + "\n\n", e);
		}
	}

	private void setInfoSesion(Object usuario, Object empresa, Object aplicacion, Object infoSesion) {
		Gson gson = null;
		
		if (usuario != null && usuario instanceof String && ! "".equals(usuario.toString().trim()))
			this.usuarioId = Long.parseLong(usuario.toString().trim());
		if (empresa != null && empresa instanceof String && ! "".equals(empresa.toString().trim()))
			this.empresaId = Long.parseLong(empresa.toString().trim());
		if (aplicacion != null && aplicacion instanceof String && ! "".equals(aplicacion.toString().trim()))
			this.aplicacionId = Long.parseLong(aplicacion.toString().trim());

		gson = new Gson();
		if (infoSesion != null && infoSesion instanceof String && ! "".equals(infoSesion.toString().trim()))
			this.infoSesion = gson.fromJson(infoSesion.toString().trim(), InfoSesion.class);
		
		// Asignamos INFOSESION si corresponde
		if (this.infoSesion == null)
			return;

		if (this.empresaId != this.infoSesion.getEmpresa().getId())
			this.empresaId =  this.infoSesion.getEmpresa().getId();

		if (this.usuarioId != this.infoSesion.getAcceso().getUsuario().getId())
			this.usuarioId =  this.infoSesion.getAcceso().getUsuario().getId();

		if (this.aplicacionId != this.infoSesion.getAcceso().getAplicacion().getId())
			this.aplicacionId =  this.infoSesion.getAcceso().getAplicacion().getId();
	}
	
	private void mensajeLog(String mensaje) {
		mensajeLog(mensaje, false);
	}

	private void mensajeLog(String mensaje, boolean appendMessage) {
		if (this.mensajeLogs == null)
			this.mensajeLogs = new ArrayList<String>();

		// Actualizo mensaje, si corresponde
		if (appendMessage) {
			mensaje = (this.mensajeLogs.get(this.mensajeLogs.size() - 1) + mensaje);
			this.mensajeLogs.set((this.mensajeLogs.size() - 1), mensaje);
			return;
		}

		// Añado mensaje
		this.mensajeLogs.add(mensaje);
	}

	private void printLog() {
		printLog("##############################    TOPIC RECURSOS HUMANOS    ##############################");
	}

	private void printLog(String mensaje) {
		try {
			mensajeLog(mensaje);
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.info("TOPIC/RH Log\n\n" + mensaje);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Terminado, "OK\n" + mensaje);
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/RH. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}
	
	private void printLog(String mensaje, Throwable throwable) {
		try {
			if (mensaje != null && ! "".equals(mensaje.trim()))
				mensajeLog(mensaje);
			mensajeLog("############################## TOPIC RECURSOS HUMANOS --- FIN : " + new Date());
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.error("TOPIC/RH Log\n\n" + mensaje + "\nEXCEPTION\n", throwable);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Error, mensaje + "\nEXCEPTION\n" + throwable.getMessage());
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/RH. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}
	
	private void topicRegistar(String evento, String mensaje) throws Exception {
		this.idTopicEstatus = this.ifzTopicEstatus.save(this.getClass().getSimpleName(), evento, mensaje, this.idTopicEstatus);
	}
	
	private void topicSinAccion(String mensaje) throws Exception {
		this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.SinAccion, mensaje);
		this.idTopicEstatus = 0L;
		mensajeLog(mensaje);
	}
}

package net.giro.scheduler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosCARGAS;
import net.giro.plataforma.topics.TopicEventosCXC;
import net.giro.plataforma.topics.TopicEventosRH;
import net.giro.plataforma.topics.TopicEventosTYG;

import org.apache.log4j.Logger;

@Stateless
public class DisesaScheduler {
	private static Logger log = Logger.getLogger(DisesaScheduler.class);

	// ****/**/** 04:20:00. Todos los dias a las 04:20 horas
	@Schedule(year = "*", dayOfWeek = "*", hour = "4", minute = "20", persistent = false, info = "Generar asistencia en dia Festivo para Empleados semanales. Todos los dias a las 04:30 horas")
	public void asyncProcCancelarNegociacionesDiasFestivos() {
		SimpleDateFormat formatter = null;
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			// Dia en cuestion
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			target = formatter.format(Calendar.getInstance().getTime());
			
			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
			log.info("########## DisesaScheduler iniciando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
			log.info("###### --- CANCELACION DE NEGOCIACIONES DE DIAS FESTIVOS :: Lanzando evento Cancelacion de Negociaciones de dias Festivos RH");
			msgTopic = new MensajeTopic(TopicEventosRH.CANCELAR_NEGOCIACIONES_DIAS_FESTIVOS, target, referencia, atributos, null);
			msgTopic.setInfoSesion(1L, 1L, 1L);
			msgTopic.enviar();
			log.info("###### --- CANCELACION DE NEGOCIACIONES DE DIAS FESTIVOS :: Terminado");
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("###### --- CANCELACION DE NEGOCIACIONES DE DIAS FESTIVOS :: ERROR al Generar Cancelacion de Negociaciones de dias Festivos RH\n\n" + comando + "\n\n", e);
		} finally {
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
		}
	}
	
	// ****/**/** 04:30:00. Todos los dias a las 04:30 horas
	@Schedule(year = "*", dayOfWeek = "*", hour = "4", minute = "30", persistent = false, info = "Generar asistencia en dia Festivo para Empleados semanales. Todos los dias a las 04:30 horas")
	public void asyncProcAsistenciaDiaFestivo() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			// Dia en cuestion
			target = formatter.format(Calendar.getInstance().getTime());
			
			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
			log.info("########## DisesaScheduler iniciando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
			log.info("###### --- ASISTENCIA EN DIA FESTIVO PARA EMPLEADOS SEMANALES :: Lanzando evento Generar Asistencia en dia Festivo para Empleados semanales RH");
			msgTopic = new MensajeTopic(TopicEventosRH.ASISTENCIA_DIA_FESTIVO, target, referencia, atributos, null);
			msgTopic.setInfoSesion(1L, 1L, 1L);
			msgTopic.enviar();
			log.info("###### --- ASISTENCIA EN DIA FESTIVO PARA EMPLEADOS SEMANALES :: Terminado");
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("###### --- ASISTENCIA EN DIA FESTIVO PARA EMPLEADOS SEMANALES :: ERROR al Generar Asistencia en dia Festivo para Empleados semanales RH\n\n" + comando + "\n\n", e);
		} finally {
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
		}
	}
	
	// ****/**/** 04:40:00. Todos los dias a las 05:40 horas
	@Schedule(year = "*", dayOfWeek = "*", hour = "4", minute = "40", persistent = false, info = "Cancelar Incapacidades de Empleados. Todos los dias a las 04:50 horas")
	public void asyncProcCancelacionIncapacidades() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			target = formatter.format(Calendar.getInstance().getTime());
			
			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
			log.info("########## DisesaScheduler iniciando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
			log.info("###### --- CANCELACION DE INCAPACIDADES DE EMPLEADOS :: Lanzando evento Cancelacion de Incapacidades de Empleados RH");
			msgTopic = new MensajeTopic(TopicEventosRH.EMPLEADOS_CANCELAR_INCAPACIDADES, target, referencia, atributos, null);
			msgTopic.setInfoSesion(1L, 1L, 1L);
			msgTopic.enviar();
			log.info("###### --- CANCELACION DE INCAPACIDADES DE EMPLEADOS :: Terminado");
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("###### --- CANCELACION DE INCAPACIDADES DE EMPLEADOS :: ERROR al Cancelar Incapacidades de Empleados RH\n\n" + comando + "\n\n", e);
		} finally {
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
		}
	}
	
	// ****/**/** 04:50:00. Todos los dias a las 04:50 horas
	@Schedule(year = "*", dayOfWeek = "*", hour = "4", minute = "50", persistent = false, info = "Cancelar Contratos determinados. Todos los dias a las 04:50 horas")
	public void asyncProcCancelacionContratos() {
		SimpleDateFormat formatter = null;
		Calendar calendar = null;
		// -----------------------------------------
		MensajeTopic msgTopic = null;
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			// Preparamos fecha
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			calendar = Calendar.getInstance();
			calendar.setTime(Calendar.getInstance().getTime());
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			target = formatter.format(calendar.getTime());
			
			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
			log.info("########## DisesaScheduler iniciando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
			log.info("###### --- CANCELACION DE CONTRATOS VENCIDOS :: Lanzando evento Cancelacion de Contratos Vencidos RH");
			msgTopic = new MensajeTopic(TopicEventosRH.CONTRATOS_VENCIDOS, target, referencia, atributos, null);
			msgTopic.setInfoSesion(1L, 1L, 1L);
			msgTopic.enviar();
			log.info("###### --- CANCELACION DE CONTRATOS VENCIDOS :: Terminado");
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("###### --- CANCELACION DE CONTRATOS VENCIDOS :: ERROR al Cancelar Contratos Vencidos RH\n\n" + comando + "\n\n", e);
		} finally {
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
		}
	}
	
	// ****/**/** 05:00:00. Todos los dias a las 05:00 horas
	@Schedule(year = "*", dayOfWeek = "*", hour = "5", minute = "0", persistent = false, info = "Consultar Tipo de Cambio en BANXICO. Todos los dias a las 05:00 horas")
    public void asyncProcTipoCambio() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			target = "USD";
			referencia = "MXN";
			
			log.info("########## DisesaScheduler iniciando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
			log.info("###### --- TIPO DE CAMBIO :: Lanzando evento TIPO CAMBIO TYG");
			msgTopic = new MensajeTopic(TopicEventosTYG.TIPO_CAMBIO, target, referencia, atributos, null);
			msgTopic.setInfoSesion(1L, 1L, 1L);
			msgTopic.enviar();
			log.info("###### --- TIPO DE CAMBIO :: Terminado");
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("###### --- TIPO CAMBIO TYG :: ERROR al recuperar\n\n" + comando + "\n\n", e);
		} finally {
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
		}
	}

	// ****/**/05 05:10:00. Todos los 5 de cada mes a las 05:10 horas
	@Schedule(year = "*", dayOfMonth = "5", hour = "5", minute = "10", persistent = false, info = "Provisionar Facturas de CXC. Los dias 5 de cada mes a las 05:10 horas")
    public void asyncProcProvisionador() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			log.info("########## DisesaScheduler iniciando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
			log.info("###### --- PROVISION FACTURAS CXC :: Lanzando evento Provisiones CXC");
			msgTopic = new MensajeTopic(TopicEventosCXC.PROVISION_MENSUAL, target, referencia, atributos, null);
			msgTopic.setInfoSesion(1L, 1L, 1L);
			msgTopic.enviar();
			log.info("###### --- PROVISION FACTURAS CXC :: Terminado");
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("###### --- PROVISION FACTURAS CXC :: ERROR al Provisionar CXC\n\n" + comando + "\n\n", e);
		} finally {
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
		}
	}
	
	// ****/**/10 05:20:00. Todos los 10 de cada mes a las 05:20 horas
	@Schedule(year = "*", dayOfMonth = "10", hour = "5", minute = "20", persistent = false, info = "Comprobar Estatus de CFDI. Todos los dias a las 05:20 horas")
    public void asyncProcComprobacionEstatusCFDI() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "";
		String referencia = "";
		String atributos = "";
		String comando = "";
		
		try {
			log.info("########## DisesaScheduler iniciando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
			log.info("###### --- COMPROBACION ESTATUS CFDI SAT :: Lanzando evento Comprobacion de Estatus de CFDI");
			msgTopic = new MensajeTopic(TopicEventosCARGAS.CFDI_ESTATUS, target, referencia, atributos, null);
			msgTopic.setInfoSesion(1L, 1L, 1L);
			msgTopic.enviar();
			log.info("###### --- COMPROBACION ESTATUS CFDI SAT :: Terminado");
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("###### --- COMPROBACION ESTATUS CFDI SAT :: ERROR al Comprobar Estatus de CFDI\n\n" + comando + "\n\n", e);
		} finally {
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
		}
	}

	// ****/**/** 05:30:00. Todos los dias a las 05:30 horas
	@Schedule(year = "*", dayOfMonth = "*", hour = "5", minute = "30", persistent = false, info = "Comprobar estatus en SAT de facturas canceladas en CXC. Todos los dias a las 05:30 horas")
    public void asyncProcComprobacionEstatusFacturasCXC() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			log.info("########## DisesaScheduler iniciando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
			log.info("###### --- COMPROBACION FACTURAS SAT :: Lanzando evento Comprobacion Facturas CXC");
			msgTopic = new MensajeTopic(TopicEventosCXC.CANCELACION_ESTATUS, target, referencia, atributos, null);
			msgTopic.setInfoSesion(1L, 1L, 1L);
			msgTopic.enviar();
			log.info("###### --- COMPROBACION FACTURAS SAT :: Terminado");
		} catch (Exception e) { 
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("###### --- COMPROBACION FACTURAS SAT :: ERROR al Comprobar Facturas CXC\n\n" + comando + "\n\n", e);
		} finally {
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
		}
	}

	// ****/**/** 08-21:01:00, Todos los dias, cada hora despues de las 08:01 hasta las 21:01
	@Schedule(year = "*", dayOfMonth = "*", hour = "08-21", minute = "01", persistent = false, info = "Comprobar Facturas/Pagos pendientes de Cancelacion en CXC. Todos los dias, cada hora despues de las 08:01 hasta las 21:01")
	public void asyncProcPendientesCancelacionCXC() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			log.info("########## DisesaScheduler iniciando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
			log.info("###### --- COMPROBACION FACTURAS PENDIENTES CANCELACION :: Lanzando evento Comprobacion Facturas pendientes de Cancelacion CXC");
			msgTopic = new MensajeTopic(TopicEventosCXC.CANCELACIONES_PENDIENTES, target, referencia, atributos, null);
			msgTopic.setInfoSesion(1L, 1L, 1L);
			msgTopic.enviar();
			log.info("###### --- COMPROBACION FACTURAS PENDIENTES CANCELACION :: Terminado");
		} catch (Exception e) { 
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("###### --- COMPROBACION FACTURAS PENDIENTES CANCELACION :: ERROR al Comprobar Facturas pendientes de Cancelacion CXC\n\n" + comando + "\n\n", e);
		} finally {
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
		}
	}
	
	// ****/**/** 08-21:00:00, Todos los dias, cada minuto a partir de las 08:00 hasta las 20:59
	@Schedule(year = "*", dayOfMonth = "*", hour = "08-20", minute = "*", persistent = false, info = "Comprobar Actualizacion AIR. Todos los dias, cada minuto a partir de las 08:00 hasta las 20:59")
	public void asyncAirUpdater() {
		SimpleDateFormat formatter = null;
		Date fechaProgramada = null;
		String[] splitted = null;
		String archivo = "";
		String aux = "";
		// -----------------------------------------
		/*MensajeTopic msgTopic = null;
		String target = "0";
		String referencia = "0";
		String atributos = "";*/
		String comando = "";
		
		try {
			// Comprobamos archivo
			archivo = this.getArchivo("/opt/tmp/actualizar"); 
			if (archivo == null || "".equals(archivo.trim()))
				return;
			
			// Validamos fecha programado
			splitted = archivo.split(",");
			aux = splitted[0] + " " + splitted[1];
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			fechaProgramada = formatter.parse(aux.trim());
			if (fechaProgramada.compareTo(Calendar.getInstance().getTime()) < 0)
				return;
			
			/*formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
			target = archivo;
			log.info("########## DisesaScheduler iniciando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
			log.info("###### --- COMPROBACION DE ACTUALIZACION AIR :: Lanzando evento AVISO de Actualizacion AIR");
			msgTopic = new MensajeTopic(TopicEventosPublico.SERVICIOS, target, referencia, atributos, null);
			msgTopic.setInfoSesion(1L, 1L, 1L);
			msgTopic.enviar();
			log.info("###### --- COMPROBACION DE ACTUALIZACION AIR :: Terminado");
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");*/
		} catch (Exception e) { 
			/*if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}*/
			log.error("###### --- COMPROBACION DE ACTUALIZACION AIR :: ERROR al Comprobar Actualizacion de AIR\n\n" + comando + "\n\n", e);
			log.info("########## DisesaScheduler terminando a " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
		} 
	}

	// --------------------------------------------------------------------------------------------
	// UTILERIAS
	// --------------------------------------------------------------------------------------------
	
	private String getArchivo(String fileName) {
		File file = null;
		
		try {
			// Validamos archivo
			file = new File(fileName);
			if (file == null || ! file.exists()) {
				log.info("404 - Archivo no encontrado");
				return null;
			}
			
			if (file.isDirectory() || ! file.isFile()) {
				log.info("400 - El archivo indicado no es valido");
				return null;
			}
			
			// Recuperamos contenido
			return new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar obtener el archivo indicado", e);
			return null;
		}
	}
}

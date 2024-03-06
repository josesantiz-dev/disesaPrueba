package net.giro.scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

@Stateless
public class DisesaScheduler {
	private static Logger log = Logger.getLogger(DisesaScheduler.class);
	
	//@Schedule(hour = "*", minute = "*/5", persistent = false)
	@Schedule(year = "*", dayOfWeek = "*", hour = "5", minute = "0", persistent = false, info = "Consultar TIPO DE CAMBIO en BANXICO todos los dias a las 05:00 horas")
    public void asyncProcessing() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.info("########## DisesaScheduler.asyncProcessing() called at " + formatter.format(Calendar.getInstance().getTime()) + " hrs");

		try {
			log.info("###### --- TIPO DE CAMBIO :: Procesando");
			TipoCambio appTipoCambio = new TipoCambio();
			appTipoCambio.ejecutar();
			log.info("###### --- TIPO DE CAMBIO :: Terminado");
		} catch (Exception e) {
			log.error("###### --- TIPO DE CAMBIO :: ERROR al consultar/actualizar TIPO DE CAMBIO", e);
		}
		
		log.info("########## DisesaScheduler.asyncProcessing() terminated at " + formatter.format(Calendar.getInstance().getTime()) + " hrs");
	}
}

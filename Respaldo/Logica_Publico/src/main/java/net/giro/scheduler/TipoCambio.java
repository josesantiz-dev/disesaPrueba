package net.giro.scheduler;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;

import javax.naming.InitialContext;

import mx.org.banxico.dgie.ws.DgieWSPortProxy;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.dao.MonedasValoresDAO;

import org.apache.log4j.Logger;

public class TipoCambio implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TipoCambio.class);
	private InitialContext ctx;

	private MonedaDAO ifzMonedas;
	private MonedasValoresDAO ifzMonValores;

	public TipoCambio() {
		try {
			this.ctx = new InitialContext();
			this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzMonValores = (MonedasValoresDAO) this.ctx.lookup("ejb:/Model_TYG//MonedasValoresImp!net.giro.tyg.dao.MonedasValoresDAO");
		} catch (Exception e) {
			log.error("########## Error en constructor Logica_Publico.TipoCambio", e);
		}
	}
	
	/**
	 * Consulta el Tipo de Cambio de BANXICO y lo actualiza en la BD si corresponde
	 * @throws Exception
	 */
	public void ejecutar() throws Exception {
		HashMap<String, Double> monedas = new HashMap<String, Double>();
		MonedasValores resultado = null;
		Moneda mBase = new Moneda();
		Moneda mDestino = new Moneda();
		Double valor = 1.0;
		String xmlBanxico = "";
		DgieWSPortProxy proxy = null;
		DomParser dpe = null;
		
		try {
			log.info("     ###### --- --- Comprobando Interfaces");
			if (this.ifzMonedas == null || this.ifzMonValores == null) {
				log.info("     ###### --- --- --- ########## Interfaces no inicializadas ");
				throw new Exception("No se pudo inicializar las interfaces para Monedas/MonedasValores");
			}
			
			log.info("     ###### --- --- Comprobando Tipo de Cambio");
			mBase = this.ifzMonedas.findByAbreviacion("MXN");
			mDestino = this.ifzMonedas.findByAbreviacion("USD");
			resultado = this.ifzMonValores.findActual(mBase, mDestino);
			if (resultado != null) {
				log.info("     ###### --- --- --- Tipo de Cambio al dia :) " + resultado.getValor());
				return;
			}
			
			// Obtenemos el tipo de cambio: BANXICO
			log.info("     ###### --- --- Consultando Tipo de Cambio en BANXICO");
			try {
				proxy = new DgieWSPortProxy();
				dpe = new DomParser();
			} catch (Exception e) {
				log.info("     ###### --- --- --- Error al inicializar WS de banxico");
				throw e;
			}
			
			try {
				xmlBanxico = proxy.tiposDeCambioBanxico();
				dpe.runExample(xmlBanxico);
				monedas = dpe.getMonedas();
				valor = monedas.get("PesoxDoll");
				log.info("     ###### --- --- --- Tipo de Cambio obtenido: " + valor);
			} catch (Exception e) {
				log.info("     ###### --- --- --- Error al consultar el Tipo de Cambio");
				throw e;
			}
			
			// Actualizamos el tipo de cambio
			log.info("     ###### --- --- Guardando Tipo de Cambio");
			resultado = new MonedasValores();
			resultado.setMonedaBase(mBase);
			resultado.setMonedaDestino(mDestino);
			resultado.setFechaDesde(Calendar.getInstance().getTime());
			resultado.setFechaHasta(Calendar.getInstance().getTime());
			resultado.setValor(new BigDecimal(valor));
			resultado.setCreadoPor(1L);
			resultado.setFechaCreacion(Calendar.getInstance().getTime());
			resultado.setModificadoPor(1L);
			resultado.setFechaModificacion(Calendar.getInstance().getTime());
			
			resultado.setId(this.ifzMonValores.save(resultado));
			log.info("     ###### --- --- --- Tipo de Cambio guardado");
		} catch (Exception e) {
			log.info("     ###### --- --- --- No se pudo guardar el TIPO DE CAMBIO");
			throw e;
		}
	}
}

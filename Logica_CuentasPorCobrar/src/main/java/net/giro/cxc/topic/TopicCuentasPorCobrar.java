package net.giro.cxc.topic;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraCobranza;
import net.giro.adp.logica.ObraCobranzaRem;
import net.giro.adp.logica.ObraRem;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.beans.FacturaPagosTimbre;
import net.giro.cxc.beans.FacturaTimbre;
import net.giro.cxc.beans.Provisiones;
import net.giro.cxc.logica.FacturaPagosRem;
import net.giro.cxc.logica.FacturaPagosTimbreRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.cxc.logica.FacturaTimbreRem;
import net.giro.cxc.logica.ProvisionesRem;
import net.giro.cxc.realvirtual.beans.FactElectv33;
import net.giro.cxc.util.FACTURA_ESTATUS;
import net.giro.cxc.util.FACTURA_PAGO_ESTATUS;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.Meses;
import net.giro.plataforma.logica.TopicEstatusRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TiposTopicEstatus;
import net.giro.plataforma.topics.TopicEventosCXC;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.logica.MonedaRem;
import net.giro.tyg.logica.MonedasValoresRem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/CXC")}, 
	mappedName = "topic/CXC")
public class TopicCuentasPorCobrar implements MessageListener {
	private static Logger log = Logger.getLogger(TopicCuentasPorCobrar.class);
	private InfoSesion infoSesion;
	private long usuarioId;
	private long empresaId;
	private long aplicacionId;
	// -----------------------------------------------------------------------
	private TopicEstatusRem ifzTopicEstatus;
	private long idTopicEstatus;
	private List<String> mensajeLogs; 
	// -----------------------------------------------------------------------
	private FacturaRem ifzFacturas;
	private FacturaPagosRem ifzPagos;
	private FacturaTimbreRem ifzTimbresFacturas;
	private FacturaPagosTimbreRem ifzTimbresPagos;
	private ProvisionesRem ifzProvisiones;
	private ObraCobranzaRem ifzCobranza;
	private ObraRem ifzObra;
    private MonedaRem ifzMonedas;
    private MonedasValoresRem ifzMonValores;
    private NQueryRem ifzQuery;
	
	public TopicCuentasPorCobrar() throws Exception {
		InitialContext ctx = null;
		
    	try {
			ctx = new InitialContext();
			this.ifzTopicEstatus = (TopicEstatusRem) ctx.lookup("ejb:/Logica_Publico//TopicEstatusFac!net.giro.plataforma.logica.TopicEstatusRem");
			this.ifzFacturas = (FacturaRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzPagos = (FacturaPagosRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosFac!net.giro.cxc.logica.FacturaPagosRem");
			this.ifzTimbresFacturas = (FacturaTimbreRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaTimbreFac!net.giro.cxc.logica.FacturaTimbreRem");
			this.ifzTimbresPagos = (FacturaPagosTimbreRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosTimbreFac!net.giro.cxc.logica.FacturaPagosTimbreRem");
			this.ifzProvisiones = (ProvisionesRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//ProvisionesFac!net.giro.cxc.logica.ProvisionesRem");
			this.ifzCobranza = (ObraCobranzaRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraCobranzaFac!net.giro.adp.logica.ObraCobranzaRem");
			this.ifzMonedas = (MonedaRem) ctx.lookup("ejb:/Logica_TYG//MonedaFac!net.giro.tyg.logica.MonedaRem");
			this.ifzMonValores	= (MonedasValoresRem) ctx.lookup("ejb:/Logica_TYG//MonedasValoresFac!net.giro.tyg.logica.MonedasValoresRem");
			this.ifzObra = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
        } catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
        	throw e;
        }
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		Gson gson = null;
		Type tipo = null;
		TextMessage mensaje = null;
		MensajeTopic mensajeTopic = null;
		TopicEventosCXC evento;
    	String jsonString = "";
		String eventoParam = "";
    	
    	try {
        	if (message instanceof TextMessage) {
				mensajeLog("############################## TOPIC CUENTAS POR COBRAR --- INICIO : " + new Date());
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
				evento = TopicEventosCXC.fromString(eventoParam);
            	switch (evento) {
        			case PUE_PAGO: 
        				bo_facturaPuePago(valueToLong(mensajeTopic.getTarget())); 
        				break;
        				
	        		case SALDO: 
						tipo = new TypeToken<List<Long>>() {}.getType();
	        			bo_saldo(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getReferencia()), (List<Long>) gson.fromJson(mensajeTopic.getAtributos(), tipo)); 
	        			break;
						
					case COBRANZA: // Actualizacion de Facturas en Cobranza
						tipo = new TypeToken<List<Long>>() {}.getType();
						bo_cobranzaObra((List<Long>) gson.fromJson(mensajeTopic.getAtributos(), tipo));
						break;

	        		case CANCELACIONES_PENDIENTES: // Busqueda de Facturas pendientes de Cancelacion de Timbre
	        			bo_cancelacionesPendientes();
	        			break;
		        		
	        		case CANCELACION_FACTURA: // Cancelacion de Factura
	        			bo_cancelacionFactura(valueToLong(mensajeTopic.getTarget())); 
	        			break;
	        			
	        		case CANCELACION_PAGO: // Cancelacion de Pago(s) de Facturas
	        			bo_cancelacionPagosFacturas(valueToLong(mensajeTopic.getTarget())); 
	        			break;
	        			
	        		case CANCELACION_ESTATUS: // Comprobamos estatus de Solicitud de Cancelacion
	        			bo_cancelacionEstatus(); 
	        			break;
	        			
	        		case PROVISION: 
	        			bo_provision(valueToLong(mensajeTopic.getTarget()), valueToDouble(mensajeTopic.getMonto()), valueToLong(mensajeTopic.getReferencia())); 
	        			break;
	        			
	        		case PROVISION_MENSUAL: 
	        			bo_provisionMensual(); 
	        			break;
	        			
	        		case CFDI_ESTATUS:
	        			bo_cfdiEstatus(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getReferencia()));
	        			break;
	        			
	        		default: break;
            	}
			}
	    	
			printLog();
        } catch (Exception e) {
        	printLog("Ocurrio un problema al recibir Evento " + this.getClass().getSimpleName() + "\n" + jsonString, e);
		} 
	}

	// ------------------------------------------------------
	// EVENTOS
	// ------------------------------------------------------
	
	/**
	 * Metodo para generar Pago automatico para una Factura PUE Timbrada
	 * @param idFactura
	 * @throws Exception
	 */
	private void bo_facturaPuePago(long idFactura) throws Exception {
		Factura factura = null;
		FacturaPagos pago = null;
		Moneda moneda = null;
		Double tipoCambio = 1.0;
		long idMonedaBase = 0L;
		// ------------------------------------------------------
		List<Long> facturas = null;
		BigDecimal pagado = BigDecimal.ZERO;
		Properties properties = null;
		
		try {
			// Validamos y recuperamos datos
			// ------------------------------------------------------------------------------------------------------
			if (idFactura <= 0L) {
				topicSinAccion("Sin accion por falta de parametros");
				return;
			}
			
			// Factura
			// ------------------------------------------------------------------------------------------------------
			facturas = new ArrayList<Long>();
			factura = this.ifzFacturas.findById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L) {
				printLog("No se pudo generar el Pago para la Factura indicada. Ocurrio un problema al recuperar la Factura");
				return;
			}
			
			facturas.add(idFactura);
			pagado = this.ifzPagos.findLiquidado(idFactura, null, null);
			if (pagado.doubleValue() > 0 && compararMontos(pagado, factura.getTotal(), 2) >= 0) {
				topicSinAccion("Sin accion. Factura PAGADA");
				// ------------------------------------------------------------------------------------------------------
				// Lanzamos evento para actualizacion de Cobranza
				autoMessage(TopicEventosCXC.COBRANZA, null, null, new Gson().toJson(facturas));
				return;
			}

			moneda = this.ifzMonedas.findById(factura.getIdMoneda());
			idMonedaBase = this.getMonedaBase(factura.getIdEmpresa());
			if (idMonedaBase > 0 && idMonedaBase != factura.getIdMoneda().longValue())
				tipoCambio = this.ifzMonValores.tipoCambioPagos(factura.getFechaTimbrado());
			
			properties = getFacturacionProperties();
			if (properties == null) {
				printLog("No pude cargar los valores para registrar el Pago PUE automatico");
				return;
			}
			
			// Genero y guardo el Pago
			// ------------------------------------------------------------------------------------------------------
			pago = new FacturaPagos();
			pago.setFecha(factura.getFechaEmision());
			pago.setIdFactura(factura);
			pago.setIdBeneficiario(factura.getIdCliente());
			pago.setBeneficiario(factura.getCliente());
			pago.setTipoBeneficiario(factura.getTipoCliente());
			pago.setIdFormaPago(valueToLong(properties.getProperty("pago.pue.formaPagoID")));
			pago.setFormaPago(valueToString(properties.getProperty("pago.pue.formaPagoDescripcion")));
			pago.setReferenciaFormaPago(valueToString(properties.getProperty("pago.pue.formaPagoReferencia")));
			pago.setIdBancoOrigen(valueToLong(properties.getProperty("pago.pue.bancoOrigenID")));
			pago.setIdCuentaDeposito(valueToLong(properties.getProperty("pago.pue.cuentaDepositoID")));
			pago.setCuentaBanco(valueToString(properties.getProperty("pago.pue.cuentaDepositoBanco")));
			pago.setCuentaNumero(valueToString(properties.getProperty("pago.pue.cuentaDepositoNumeroCuenta")));
			pago.setParcialidad(1);
			pago.setPago(factura.getTotal());
			pago.setSaldoAnterior(factura.getTotal());
			pago.setSaldoInsoluto(BigDecimal.ZERO);
			pago.setIdMoneda(factura.getIdMoneda());
			pago.setMoneda(moneda.getNombre());
			pago.setTipoCambio(tipoCambio);
			pago.setObservaciones("");
			pago.setSistema(1);
			pago.setCreadoPor(factura.getCreadoPor());
			pago.setFechaCreacion(Calendar.getInstance().getTime());
			pago.setModificadoPor(factura.getModificadoPor());
			pago.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzPagos.setInfoSesion(this.infoSesion);
			this.ifzPagos.save(pago);
			
			// Actualizo el saldo de la Factura
			// ------------------------------------------------------------------------------------------------------
			factura.setSaldo(BigDecimal.ZERO);
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			this.ifzFacturas.update(factura);

			// ------------------------------------------------------------------------------------------------------
			// Lanzamos evento para actualizacion de Cobranza
			autoMessage(TopicEventosCXC.COBRANZA, null, null, new Gson().toJson(facturas));
		} catch (Exception e) { 
			printLog("Ocurrio un problema al intentar actualizar el Saldo de la Factura", e);
			throw e;
		} 
	}

	/**
	 * BackOffice Factura: Actualizacion de saldo de Facturas
	 * @param idFacturaPago ID del pago
	 * @param referencia Agrupador de los pagos involucrados
	 * @param facturas Listado de facturas
	 */
	private void bo_saldo(long idFacturaPago, String referencia, List<Long> facturas) {
		List<Factura> listFacturas = null;
		List<FacturaPagos> pagos = null;
		FacturaPagos pago = null;
		String agrupador = "";
		
		try {
			listFacturas = new ArrayList<Factura>();
			facturas = (facturas != null ? facturas : new ArrayList<Long>());
			if (idFacturaPago > 0L) {
				pago = this.ifzPagos.findById(idFacturaPago);
				if (pago == null || pago.getId() == null || pago.getId() <= 0L) {
					topicSinAccion("Sin accion por Pago no encontrado: " + idFacturaPago);
					return;
				}
				
				facturas = new ArrayList<Long>();
				if (pago.getAgrupador() != null && ! "".equals(pago.getAgrupador().trim())) {
					agrupador = pago.getAgrupador();
					pagos = this.ifzPagos.findByProperty("agrupador", agrupador);
					pagos = (pagos != null ? pagos : new ArrayList<FacturaPagos>());
					for (FacturaPagos item : pagos) {
						listFacturas.add(item.getIdFactura());
						facturas.add(item.getIdFactura().getId());
					}
					pagos.clear();
				} 
	
				if (listFacturas.isEmpty()) {
					agrupador = pago.getId().toString();
					listFacturas.add(pago.getIdFactura());
					facturas.add(pago.getIdFactura().getId());
				}
			} else if (facturas != null && ! facturas.isEmpty()) {
				if ("".equals(referencia) || "0".equals(referencia))
					referencia = "NA";
				agrupador = referencia;
				listFacturas = this.ifzFacturas.findList(facturas);
				listFacturas = (listFacturas != null ? listFacturas : new ArrayList<Factura>());
			}
			
			mensajeLog(" -----> Evento CXC-SALDO: BackOffice CXC - Actualiza el SALDO en facturas");
			mensajeLog(" -----> FacturaPago : " + agrupador);
			mensajeLog(" -----> Facturas    : " + listFacturas.size());
			if (listFacturas == null || listFacturas.isEmpty()) {
				topicSinAccion("Sin accion por parametros insuficientes ");
				return;
			}
			
			for (Factura factura : listFacturas) 
				factura.setSaldo(this.ifzFacturas.calcularSaldo(factura.getId(), null, null));
			autoMessage(TopicEventosCXC.COBRANZA, null, null, new Gson().toJson(facturas));
			//bo_cobranzaObra(facturas);
		} catch (Exception e) { 
			printLog("Ocurrio un problema al intentar actualizar el Saldo de la Factura", e);
		}
	}

	/**
	 * Metodo para actualizar la Cobranza correspondiente de cada factura indicada
	 * @param facturas
	 * @throws Exception
	 */
	private void bo_cobranzaObra(List<Long> facturas) throws Exception {
		HashMap <Long, List<ObraCobranza>> mapCobranza = null;
		HashMap <Long, Obra> mapObras = null;
		HashMap <Long, Moneda> mapMonedas = null;
		HashMap <Long, Integer> indices = null;
		// -------------------------------------------------------
		List<ObraCobranza> listCobranza = null;
		List<ObraCobranza> listCobranzaBorradas = null;
		ObraCobranza cobranza = null;
		Factura pojoFactura = null;
		Moneda pojoMoneda = null;
		Obra pojoObra = null;
		double porcentajeIva = 0;
		double porcentajeAnticipo = 0;
		double porcentajeRetencion = 0;
		// -------------------------------------------------------
		boolean facturaExiste = false;
		long idConcepto = 0L;
		String concepto = "";
		BigDecimal facturado = BigDecimal.ZERO;
		BigDecimal notasCredito = BigDecimal.ZERO;
		BigDecimal saldo = BigDecimal.ZERO;
		BigDecimal cobrado = BigDecimal.ZERO;
		BigDecimal cobradoPesos = BigDecimal.ZERO;
		int decimales = 2;
		long idObra = 0L;
		
		try {
			facturas = (facturas != null ? facturas : new ArrayList<Long>());
			mensajeLog(" -----> Evento CXC-COBRANZA: BackOffice CXC - Añade/Actualiza facturas en Cobranza");
			mensajeLog(" -----> Facturas  : " + facturas.size());
			if (facturas.isEmpty()) {
				topicSinAccion("Sin accion por parametros insuficientes ");
				return;
			}
			
			porcentajeIva = 1;
			concepto = getPerfilValue("VALOR_IVA");
			if (concepto != null && ! "".equals(concepto.trim()))
				porcentajeIva = Double.parseDouble(concepto);

			mapObras = new HashMap<Long, Obra>();
			mapMonedas = new HashMap<Long, Moneda>();
			mapCobranza = new HashMap<Long, List<ObraCobranza>>();
			listCobranzaBorradas = new ArrayList<ObraCobranza>();
			for (Long factura : facturas) {
				// Validaciones de factura "nueva"
				if (! facturaTipoDeComprobante(factura))
					continue;
				// Recuperamos la factura
				pojoFactura = this.ifzFacturas.findById(factura);
				if (pojoFactura == null || pojoFactura.getId() == null || pojoFactura.getId() <= 0L)
					continue;
				
				// validamos mapa de obra y cobranza
				idObra = pojoFactura.getIdObra();
				if (mapCobranza.containsKey(idObra)) {
					pojoObra = mapObras.get(idObra);
					listCobranza = mapCobranza.get(idObra);
				} else {
					pojoObra = this.ifzObra.findById(idObra);
					listCobranza = this.ifzCobranza.findAll(idObra, "", 0);
				}
				
				// Generamos indices y recuperamos porcentajes de Anticipo y Retencion
				porcentajeAnticipo = 0;
				porcentajeRetencion = 0;
				indices = new HashMap<Long, Integer>();
				for (ObraCobranza cob : listCobranza) {
					indices.put(cob.getIdFactura(), listCobranza.indexOf(cob));
					
					// Recuperamos porcentajes asignados
					if (porcentajeAnticipo <= 0 && cob.getPorcentajeAnticipo().doubleValue() > 0)
						porcentajeAnticipo = cob.getPorcentajeAnticipo().doubleValue();
					if (porcentajeRetencion <= 0 && cob.getPorcentajeRetencion().doubleValue() > 0)
						porcentajeRetencion = cob.getPorcentajeRetencion().doubleValue();
					
					// Asignamos porcentajes recuperados si corresponde
					if (porcentajeAnticipo > 0 && cob.getPorcentajeAnticipo().doubleValue() <= 0)
						cob.setPorcentajeAnticipo(new BigDecimal(porcentajeAnticipo));
					if (porcentajeRetencion > 0 && cob.getPorcentajeRetencion().doubleValue() <= 0)
						cob.setPorcentajeRetencion(new BigDecimal(porcentajeRetencion));
				}
				
				// Comprobamos factura en la Cobranza
				facturaExiste = false;
				if (indices.containsKey(factura)) {
					facturaExiste = true;
					if (! facturaEstatus(factura)) { 
						// Si la factura esta actualmente cancelada, la quitamos de la cobranza
						listCobranzaBorradas.add(listCobranza.get(indices.get(factura)));
						listCobranza.remove(listCobranza.get(indices.get(factura)));
						mapCobranza.put(idObra, listCobranza);
						continue;
					}
				}
				
				// SI no existe, Validamos el Estatus de la Factura: Si no esta activa, saltamos iteracion
				if (! facturaExiste && pojoFactura.getEstatus() != FACTURA_ESTATUS.Activa.ordinal())
					continue;
				
				// Redondeamos facturado
				facturado = pojoFactura.getTotal();
				facturado = facturado.setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
				// Recuperamos Notas de Credito
				notasCredito = this.ifzFacturas.findMontoNotasCreditoByFactura(factura);
				notasCredito = notasCredito.setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
				if (notasCredito != null && notasCredito.doubleValue() > 0) {
					facturado = facturado.subtract(notasCredito);
					facturado = facturado.setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
				}
				// Calculamos saldo
				saldo = this.ifzFacturas.calcularSaldo(factura, null, null);
				saldo = saldo.setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
				// Calculamos cobrado
				cobrado = new BigDecimal((facturado.doubleValue() - saldo.doubleValue()));
				cobrado = cobrado.setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
				// Calculamos cobrado pesos
				cobradoPesos = this.ifzPagos.findLiquidadoPesos(factura, null, null);
				// validamos mapa de monedas
				pojoMoneda = null;
				if (mapMonedas.containsKey(pojoFactura.getIdMoneda())) 
					pojoMoneda = mapMonedas.get(pojoFactura.getIdMoneda());
				else 
					pojoMoneda = this.ifzMonedas.findById(pojoFactura.getIdMoneda());
				if (pojoMoneda == null || (pojoFactura.getIdMoneda() == null || pojoFactura.getIdMoneda() <= 0L))
					pojoMoneda = this.ifzMonedas.findById(10000001L);
				mapMonedas.put(pojoFactura.getIdMoneda(), pojoMoneda);
				
				// Actualiamos facturas conservadas
				if (facturaExiste) {
					cobranza = listCobranza.get(indices.get(factura));
					cobranza.setFolio(pojoFactura.getFolioFactura());
					cobranza.setFecha(pojoFactura.getFechaEmision());
					cobranza.setFacturaTotal(facturado);
					cobranza.setFacturaSaldo(saldo);
					cobranza.setFacturaCobrado(cobrado);
					cobranza.setFacturaCobradoPesos(cobradoPesos);
					cobranza.setPorcentajeIva(new BigDecimal(porcentajeIva));
					listCobranza.set(indices.get(factura), cobranza);
					mapCobranza.put(idObra, listCobranza);
					continue;
				}
				
				// Recuperamos concepto
				idConcepto = facturaIdConcepto(factura);
				concepto = facturaConcepto(idConcepto);
				
				// cargo la nueva factura en la cobranza correspondiente
				cobranza = new ObraCobranza();
				cobranza.setIdObra(pojoObra);
				cobranza.setNombreObra(pojoObra.getNombre());
				cobranza.setIdMoneda(pojoMoneda.getId());
				cobranza.setMonedaNombre(pojoMoneda.getNombre());
				cobranza.setMonedaAbreviacion(pojoMoneda.getAbreviacion());
				cobranza.setTipoCambio(pojoFactura.getTipoCambio());
				cobranza.setIdFactura(factura);
				cobranza.setFolio(pojoFactura.getFolioFactura());
				cobranza.setFecha(pojoFactura.getFechaEmision());
				cobranza.setIdConcepto(idConcepto);
				cobranza.setConcepto(concepto);
				cobranza.setFacturaTotal(facturado); 
				cobranza.setFacturaSaldo(saldo);
				cobranza.setFacturaCobrado(cobrado);
				cobranza.setFacturaCobradoPesos(cobradoPesos);
				// Asignamos porcentajes de la cobranza previa
				cobranza.setPorcentajeIva(new BigDecimal(porcentajeIva));
				cobranza.setPorcentajeAnticipo(new BigDecimal(porcentajeAnticipo));
				cobranza.setPorcentajeRetencion(new BigDecimal(porcentajeRetencion));
				// totales
				cobranza.setAnticipo(BigDecimal.ZERO);
				cobranza.setEstimacion(BigDecimal.ZERO);
				cobranza.setAmortizacion(BigDecimal.ZERO);
				cobranza.setFondoGarantia(BigDecimal.ZERO);
				cobranza.setSubtotal(BigDecimal.ZERO);
				cobranza.setIva(BigDecimal.ZERO);
				cobranza.setTotal(BigDecimal.ZERO);
				cobranza.setCargos(BigDecimal.ZERO);
				// Agregamos al listado
				listCobranza.add(cobranza);
				mapObras.put(idObra, pojoObra);
				mapCobranza.put(idObra, listCobranza);
			}
			
			// Guardamos la Cobranza de todas las Obras
			if (mapCobranza != null && ! mapCobranza.isEmpty()) {
				for (Entry<Long, List<ObraCobranza>> item : mapCobranza.entrySet()) {
					listCobranza = item.getValue();
					this.ifzCobranza.saveOrUpdateList(listCobranza);
				}
			}
			
			// Borramos las Facturas canceladas o eliminadas de cobranza
			if (listCobranzaBorradas != null && ! listCobranzaBorradas.isEmpty())
				this.ifzCobranza.deleteAll(listCobranzaBorradas);
		} catch (Exception e) {
			printLog("Ocurrio un problema al actualizar la Cobranza: Obra " + idObra, e);
			throw e;
		}
	}

	/**
	 * Metodo para recuperar todas las Facturas pendientes de Cancelacion
	 */
	private void bo_cancelacionesPendientes() {
		List<Long> cancelables = null;
		
		try {
			cancelables = cfdiFacturasPendientesCancelacion();
			if (cancelables != null && ! cancelables.isEmpty()) {
				mensajeLog("CFDI Cancelables: x" + cancelables.size());
				for (Long idFactura : cancelables) {
					mensajeLog("Solicitud de Cancelacion CFDI " + (cancelables.indexOf(idFactura) + 1) + "/" + cancelables.size() + " (SAT)");
					autoMessage(TopicEventosCXC.CANCELACION_FACTURA, idFactura, null, null);
				}
			}
			
			cancelables = cfdiPagosPendientesCancelacion();
			if (cancelables != null && ! cancelables.isEmpty()) {
				mensajeLog("CFDI Pago Cancelables: x" + cancelables.size());
				for (Long idTimbre : cancelables) {
					mensajeLog("Solicitud de Cancelacion CFDI Pago " + (cancelables.indexOf(idTimbre) + 1) + "/" + cancelables.size() + " (SAT)");
					autoMessage(TopicEventosCXC.CANCELACION_PAGO, idTimbre, null, null);
				}
			}
		} catch (Exception e) { 
			printLog("Ocurrio un problema al intentar actualizar el Saldo de la Factura", e);
		}
	}

	/**
	 * Metodo para la Cancelacion de Timbres de Facturas
	 * @param idFactura
	 */
	private void bo_cancelacionFactura(long idFactura) {
		Respuesta respuesta = null;
		// ----------------------------------------
		List<FacturaPagos> pagos = null;
		FactElectv33 timbre33 = null;
		FacturaTimbre timbre = null;
		Factura factura = null;
		int intentosCancelacion = 0;
		boolean continuarConcelacion = false;
		// ----------------------------------------
		String rfcEmisor = "";
		List<String> folios = null;
		// ----------------------------------------
		Date fechaCancelacion = null;
		byte[] acuseCancelacion = null;
		String mensajeCancelacion = null;
		
		try {
			// Recuperamos Factura
			mensajeLog("Recuperamos Factura ... ");
			factura = this.ifzFacturas.findById(idFactura);
			if (factura == null || factura.getId() == null || factura.getId() <= 0L) {
				mensajeLog("ERROR", true);
				mensajeLog("No se pudo recuperar la Factura indicada para Cancelacion");
				return;
			}
			mensajeLog("OK", true);
			
			// Recuperamos Timbre
			mensajeLog("Recuperamos Timbre ... ");
			timbre = this.ifzTimbresFacturas.findById(factura.getIdTimbre());
			if (timbre == null || timbre.getId() == null || timbre.getId() <= 0L) {
				mensajeLog("ERROR", true);
				mensajeLog("No se pudo recuperar el Timbre o la Factura indicada No esta tu Timbrada");
				return;
			}
			mensajeLog("OK", true);
			
			// Incremento los intentos de Cancelacion
			if (timbre.getFechaSolicitudCancelacion() == null)
				timbre.setFechaSolicitudCancelacion(Calendar.getInstance().getTime());
			intentosCancelacion = timbre.getIntentosCancelacion();
			intentosCancelacion += 1;
			timbre.setIntentosCancelacion(intentosCancelacion);
			timbre.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzTimbresFacturas.setInfoSesion(this.infoSesion);
			this.ifzTimbresFacturas.update(timbre);
			
			// Generamos el comprobante
			folios = new ArrayList<String>();
			folios.add(timbre.getUuid());
			rfcEmisor = getRfcEmisor(timbre.getIdEmpresa());
			
			// Cancelamos pagos, si corresponde
			mensajeLog("Comprobando Pagos ... ");
			pagos = this.ifzPagos.findAll(idFactura);
			if (pagos != null && ! pagos.isEmpty()) {
				mensajeLog("Cancelando Pagos x" + pagos.size() + " ... ");
				for (FacturaPagos pago : pagos)
					this.ifzPagos.cancelar(pago.getId());
				mensajeLog("OK", true);
			}
			
			// Cancelamos timbre 
			mensajeLog("Inicializamos Servicios CFDI ... ");
			timbre33 = new FactElectv33(this.infoSesion);
			timbre33.setDebugging(false);
			timbre33.setTesting(false); 
			timbre33.setDigestMethodSHA1();
			timbre33.setSoapRequestVersion11();
			mensajeLog("OK", true);

			// Consultamos estatus interno de la Factura
			continuarConcelacion = true;
			mensajeLog("Consultando Estatus CFDI ... ");
			respuesta = timbre33.estatus(timbre.getRfcEmisor(), timbre.getRfcReceptor(), ((new DecimalFormat("0.00")).format(factura.getTotal())), timbre.getUuid(), timbre.getIdEmpresa());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				mensajeLog("ERROR", true);
				mensajeLog(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}
			
			if ((int) respuesta.getBody().getValor("cancelada") == 1) 
				continuarConcelacion = false;
			mensajeLog("OK", true);
			
			if (continuarConcelacion) {
				mensajeLog("Cancelando CFDI ... ");
				respuesta = timbre33.cancelar(timbre.getIdEmpresa(), rfcEmisor, folios, timbre.getCanceladoPor());
				if (respuesta.getErrores().getCodigoError() != 0L) {
					if (respuesta.getErrores().getCodigoError() == 202) {
						respuesta = timbre33.estatus(timbre.getRfcEmisor(), timbre.getRfcReceptor(), ((new DecimalFormat("0.00")).format(factura.getTotal())), timbre.getUuid(), timbre.getIdEmpresa());
						if (respuesta.getErrores().getCodigoError() != 0L) {
							mensajeLog("ERROR", true);
							mensajeLog(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
							return;
						}
					} else {
						mensajeLog("ERROR", true);
						mensajeLog(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
						return;
					}
				}
				mensajeLog("OK", true);
			}

			mensajeLog("Recuperando informacion Acuse ... ");
			if (respuesta.getBody().getValor("fecha_cancelacion") != null && ! "".equals(respuesta.getBody().getValor("fecha_cancelacion").toString().trim()))
				fechaCancelacion = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).parse(respuesta.getBody().getValor("fecha_cancelacion").toString());
			if (respuesta.getBody().getValor("acuse_cancelacion") != null && ! "".equals(respuesta.getBody().getValor("acuse_cancelacion").toString().trim()))
				acuseCancelacion = (byte[]) respuesta.getBody().getValor("acuse_cancelacion");
			if (respuesta.getBody().getValor("descripcion") != null && ! "".equals(respuesta.getBody().getValor("descripcion").toString().trim()))
				mensajeCancelacion = respuesta.getBody().getValor("descripcion").toString();
			if (fechaCancelacion == null)
				fechaCancelacion = Calendar.getInstance().getTime();
			mensajeLog("OK", true);

			mensajeLog("Actualizando timbre ... ");
			timbre.setCancelado(0);
			if ((int) respuesta.getBody().getValor("cancelada") == 1) 
				timbre.setCancelado(1);
			timbre.setEstadoCancelacion(0L);
			timbre.setFechaCancelacion(fechaCancelacion);
			timbre.setAcuseCancelacion(acuseCancelacion);
			timbre.setMensajeCancelacion(mensajeCancelacion);
			timbre.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzTimbresFacturas.setInfoSesion(this.infoSesion);
			this.ifzTimbresFacturas.update(timbre);
			mensajeLog("OK", true);

			mensajeLog("Actualizando Factura ... ");
			factura.setEstatus(FACTURA_ESTATUS.Cancelada);
			factura.setFechaCancelacion(fechaCancelacion);
			factura.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			this.ifzFacturas.update(factura);
			mensajeLog("OK", true);
		} catch (Exception e) { 
			printLog("Ocurrio un problema al intentar Cancelacion de Timbre de la Factura: " + idFactura, e);
		}
	}
	
	/**
	 * Metodo para la Cancelacion de Timbres de Complemento de Pago
	 * @param idTimbre
	 */
	private void bo_cancelacionPagosFacturas(long idTimbre) {
		Respuesta respuesta = null;
		FactElectv33 timbre33 = null;
		FacturaPagosTimbre timbre = null;
		List<FacturaPagos> pagos = null;
		int intentosCancelacion = 0;
		boolean continuarConcelacion = false;
		// ----------------------------------------
		String rfcEmisor = "";
		List<String> folios = null;
		// ----------------------------------------
		Date fechaCancelacion = null;
		byte[] acuseCancelacion = null;
		String mensajeCancelacion = null;
		
		try {
			mensajeLog("Recuperamos Timbre ... ");
			timbre = this.ifzTimbresPagos.findById(idTimbre);
			if (timbre == null || timbre.getId() == null || timbre.getId() <= 0L) {
				mensajeLog("ERROR", true);
				mensajeLog("No se pudo recuperar el Timbre indicado");
				return;
			}
			
			if (timbre.getTimbrado() != 1 || (timbre.getUuid() == null || "".equals(timbre.getUuid().trim()))) {
				mensajeLog("ERROR", true);
				mensajeLog("El CFDI indicado no esta timbrado o no se pudo recuperar el Folio Fiscal (UUID)");
				return;
			}
			mensajeLog("OK", true);
			
			// Incremento los intentos de Cancelacion
			if (timbre.getFechaSolicitudCancelacion() == null)
				timbre.setFechaSolicitudCancelacion(Calendar.getInstance().getTime());
			intentosCancelacion = timbre.getIntentosCancelacion();
			intentosCancelacion += 1;
			timbre.setIntentosCancelacion(intentosCancelacion);
			timbre.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzTimbresPagos.setInfoSesion(this.infoSesion);
			this.ifzTimbresPagos.update(timbre);
			
			// Generamos el comprobante
			folios = new ArrayList<String>();
			folios.add(timbre.getUuid());
			rfcEmisor = getRfcEmisor(timbre.getIdEmpresa());

			// Atentamos la cancelacion del Timbre 
			mensajeLog("Inicializamos Servicios CFDI ... ");
			timbre33 = new FactElectv33(this.infoSesion);
			timbre33.setDebugging(false);
			timbre33.setTesting(false); 
			timbre33.setDigestMethodSHA1();
			timbre33.setSoapRequestVersion11();
			mensajeLog("OK", true);

			// Consultamos estatus interno de la Factura
			continuarConcelacion = true;
			mensajeLog("Consultando Estatus CFDI Pagos ... ");
			respuesta = timbre33.estatus(timbre.getRfcEmisor(), timbre.getRfcReceptor(), "0.00", timbre.getUuid(), timbre.getIdEmpresa());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				mensajeLog("ERROR", true);
				mensajeLog(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}
			
			if ((int) respuesta.getBody().getValor("cancelada") == 1) 
				continuarConcelacion = false;
			mensajeLog("OK", true);
			
			if (continuarConcelacion) {
				mensajeLog("Cancelando CFDI Pagos ... ");
				respuesta = timbre33.cancelar(timbre.getIdEmpresa(), rfcEmisor, folios, timbre.getCanceladoPor());
				if (respuesta.getErrores().getCodigoError() != 0L) {
					if (202 == respuesta.getErrores().getCodigoError()) {
						respuesta = timbre33.estatus(timbre.getRfcEmisor(), timbre.getRfcReceptor(), "0.00", timbre.getUuid(), timbre.getIdEmpresa());
						if (respuesta.getErrores().getCodigoError() != 0L) {
							mensajeLog("ERROR", true);
							mensajeLog(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
							return;
						}
					} else {
						mensajeLog("ERROR", true);
						mensajeLog( respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
						return;
					}
				}
				mensajeLog("OK", true);
			}
			
			mensajeLog("Recuperando informacion Acuse ... ");
			if (respuesta.getBody().getValor("fecha_cancelacion") != null && ! "".equals(respuesta.getBody().getValor("fecha_cancelacion").toString().trim()))
				fechaCancelacion = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).parse(respuesta.getBody().getValor("fecha_cancelacion").toString());
			if (respuesta.getBody().getValor("acuse_cancelacion") != null && ! "".equals(respuesta.getBody().getValor("acuse_cancelacion").toString().trim()))
				acuseCancelacion = (byte[]) respuesta.getBody().getValor("acuse_cancelacion");
			if (respuesta.getBody().getValor("descripcion") != null && ! "".equals(respuesta.getBody().getValor("descripcion").toString().trim()))
				mensajeCancelacion = respuesta.getBody().getValor("descripcion").toString();
			if (fechaCancelacion == null)
				fechaCancelacion = Calendar.getInstance().getTime();
			mensajeLog("OK", true);
			
			mensajeLog("Actualizando timbre ... ");
			timbre.setCancelado(0);
			if ((int) respuesta.getBody().getValor("cancelada") == 1) 
				timbre.setCancelado(1);
			timbre.setEstadoCancelacion(0L);
			timbre.setFechaCancelacion(fechaCancelacion);
			timbre.setAcuseCancelacion(acuseCancelacion);
			timbre.setMensajeCancelacion(mensajeCancelacion);
			timbre.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzTimbresPagos.setInfoSesion(this.infoSesion);
			this.ifzTimbresPagos.update(timbre);
			mensajeLog("OK", true);

			// Actualizamos pagos relacionados al timbre
			mensajeLog("Actualizando Pagos ... ");
			pagos = this.ifzPagos.findByTimbre(idTimbre, true, null);
			if (pagos != null && ! pagos.isEmpty()) {
				for (FacturaPagos pago : pagos) {
					pago.setEstatus(FACTURA_PAGO_ESTATUS.Cancelado);
					pago.setFechaCancelacion(fechaCancelacion);
					pago.setFechaModificacion(Calendar.getInstance().getTime());
				}
				
				this.ifzPagos.setInfoSesion(this.infoSesion);
				pagos = this.ifzPagos.saveOrUpdateList(pagos);
			}
			mensajeLog("OK", true);
		} catch (Exception e) { 
			printLog("Ocurrio un problema al intentar actualizar el Saldo de la Factura", e);
		}
	}
	
	/**
	 * Metodo para comprobar el estatus en SAT de Facturas canceladas
	 */
	private void bo_cancelacionEstatus() {
		
	}
	
	/**
	 * Provision de Factura
	 * @param idFactura
	 * @param montoProvision
	 * @param grupo
	 */
	private void bo_provision(long idFactura, double montoProvision, long grupo) {
		
	}
	
	/**
	 * Provision de Facturas mensual
	 */
	private void bo_provisionMensual() { 
		Provisiones pojoProvision = null;
		List<Factura> listFacturas = null;
		Calendar cal = null;
		double provision = 0;
		int provisionadas = 0;
		int grupo = 0;
		String observaciones = "";
		
		try {
			cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, (cal.get(Calendar.DAY_OF_MONTH) * -1));
			grupo = getGrupo(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
			observaciones = "PROVISION " + Meses.fromOrdinal(cal.get(Calendar.MONTH)).toString().toUpperCase();
			
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			listFacturas = this.ifzFacturas.paraProvisionar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), "id desc"); 
			if (listFacturas == null || listFacturas.isEmpty()) {
				topicSinAccion("No se encontraron Facturas para Provisionar");
				return;
			}
			
			// Provisionamos Facturas encontradas
			for (Factura item : listFacturas) {
				// facturapagos FacturaPagosRem find.All(idFactura)
				// Si el saldo es menor a 1 peso, la descartamos
				if (item.getSaldo().doubleValue() < 1)
					continue;
				
				// Provisionar Factura
				provision = Double.parseDouble((new DecimalFormat("#.00")).format(item.getSaldo().doubleValue()));
				pojoProvision = this.ifzProvisiones.findProvision(item);
				if (pojoProvision == null) {
					pojoProvision = new Provisiones();
					pojoProvision.setIdFactura(item);
					pojoProvision.setMonto(provision);
					pojoProvision.setMontoOriginal(provision);
					pojoProvision.setObservaciones(observaciones);
					pojoProvision.setGrupo(grupo);
					pojoProvision.setEstatus(0);
					pojoProvision.setCreadoPor(1L);
					pojoProvision.setFechaCreacion(Calendar.getInstance().getTime());
				}
				
				pojoProvision.setModificadoPor(1L);
				pojoProvision.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzProvisiones.saveOrUpdate(pojoProvision);
				
				// Actualizamos indicadores de Provision
				item.setProvisionada(1);
				item.setProvisionadaPor(1L);
				item.setFechaProvisionada(Calendar.getInstance().getTime());
				this.ifzFacturas.update(item);
				
				// Contamos la factura provisionada
				provisionadas += 1;
			}

			printLog(provisionadas + " Facturas Provisionadas");
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar procesar la Provision de Facturas", e);
		} 
	}
	
	/**
	 * Metodo para consulta el Estatus del CFDI con el PAC (SAT)
	 * @param idTimbre 
	 * @param tipoTimbre Tipo de Timbre: F - Factura, P - Pagos (Complemento Pago)
	 * @throws Exception
	 */
	private void bo_cfdiEstatus(long idTimbre, String tipoTimbre) throws Exception {
		Respuesta respuesta = null;
		// --------------------------------------------------------
		FacturaTimbre timbreFactura = null;
		FacturaPagosTimbre timbrePago = null;
		Factura factura = null;
		
		try {
			tipoTimbre = (tipoTimbre != null ? tipoTimbre : "");
			if ("F".equals(tipoTimbre.toUpperCase().trim())) {
				timbreFactura = this.ifzTimbresFacturas.findById(idTimbre);
				if (timbreFactura == null || timbreFactura.getId() == null || timbreFactura.getId() <= 0L) {
					mensajeLog("No se pudo recuperar el Timbre de Facturas indicado");
					return;
				}
				
				if (timbreFactura.getTimbrado() != 1 || (timbreFactura.getUuid() == null || "".equals(timbreFactura.getUuid().trim()))) {
					mensajeLog("El Timbre de Facturas indicado no esta timbrado o no se pudo recuperar el Folio Fiscal (UUID)");
					return;
				}
				
				factura = this.ifzFacturas.findByIdTimbre(timbreFactura.getId());
				if (factura == null || factura.getId() == null || factura.getId() <= 0L) {
					mensajeLog("No se pudo recuperar la Factura del Timbre indicado");
					return;
				}
				
				// Atentamos la cancelacion del Timbre 
				mensajeLog("Consultando Estatus CFDI ...");
				respuesta = cfdiEstatus(timbreFactura.getRfcEmisor(), timbreFactura.getRfcReceptor(), (new DecimalFormat("0.00")).format(factura.getTotal()), timbreFactura.getUuid(), timbreFactura.getIdEmpresa());
				if (respuesta.getErrores().getCodigoError() != 0L) {
					mensajeLog("ERROR ", true);
					mensajeLog("Error " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
					return;
				}
				mensajeLog("OK", true);
			} else if ("P".equals(tipoTimbre.toUpperCase().trim())) {
				timbrePago = this.ifzTimbresPagos.findById(idTimbre);
				if (timbrePago == null || timbrePago.getId() == null || timbrePago.getId() <= 0L) {
					mensajeLog("No se pudo recuperar el Timbre de Pagos indicado");
					return;
				}
				
				if (timbrePago.getTimbrado() != 1 || (timbrePago.getUuid() == null || "".equals(timbrePago.getUuid().trim()))) {
					mensajeLog("El Timbre de Pagos indicado no esta timbrado o no se pudo recuperar el Folio Fiscal (UUID)");
					return;
				}

				// Atentamos la cancelacion del Timbre 
				mensajeLog("Consultando Estatus CFDI Pagos ...");
				respuesta = cfdiEstatus(timbrePago.getRfcEmisor(), timbrePago.getRfcReceptor(), "0.00", timbrePago.getUuid(), timbrePago.getIdEmpresa());
				if (respuesta.getErrores().getCodigoError() != 0L) {
					mensajeLog("ERROR ", true);
					mensajeLog("Error " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
					return;
				}
				mensajeLog("OK", true);
			} else {
				mensajeLog("No se pudo determinar el tipo de Timbre: " + tipoTimbre + " [" + idTimbre + "]");
				return;
			}
		} catch (Exception e) { 
			printLog("Ocurrio un problema al intentar consultar el Estatus del CFDI indicado: " + tipoTimbre + " [" + idTimbre + "]", e);
		}
		
		return;
	}
	
    // ----------------------------------------------------------------------
 	// METODOS PRIVADOS 
 	// ----------------------------------------------------------------------

	private void autoMessage(TopicEventosCXC evento, Object target, Object referencia, Object atributos) {
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
	}

	private String valueToString(Object value) {
		return (value != null ? value.toString().trim() : "");
	}

	private long valueToLong(Object value) {
		return ((! "".equals(valueToString(value))) ? Long.valueOf(valueToString(value)) : 0L);
	}
	
	private double valueToDouble(Object value) {
		return ((! "".equals(valueToString(value))) ? Double.valueOf(valueToString(value)) : 0);
	}
	
	/**
	 * Metodo para comparar montos con presicion de decimales.
	 * @param valor1
	 * @param valor2
	 * @param decimales
	 * @return -1, 0, or 1 si valor1 es menor, igual o mayor a valor2
	 */
	private int compararMontos(BigDecimal valor1, BigDecimal valor2, int decimales) {
		double num1 = 0;
		double num2 = 0;
		
		decimales = (decimales > 0 ? decimales : 0);
		num1 = valor1.setScale(decimales, BigDecimal.ROUND_HALF_EVEN).doubleValue();
		num2 = valor2.setScale(decimales, BigDecimal.ROUND_HALF_EVEN).doubleValue();
		if (num1 > num2)
			return 1;
		else if (num1 < num2) 
			return -1;
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	private String getRfcEmisor(Long idEmpresa) {
		List<String> rows = null;
		String queryString = "";
		// --------------------------------------------
		String resultado = "";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0L)
				return "";
			queryString = "select rfc from a858c9f4c5 where aa = :idEmpresa ";
			queryString = queryString.replace(":idEmpresa", idEmpresa.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = rows.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar la Moneda asignada a la Empresa indicada: " + idEmpresa, e);
			resultado = "";
		}
		
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	private long getMonedaBase(Long idEmpresa) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long resultado = 0;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0L)
				return 0L;
			queryString = "select au as id_moneda from a858c9f4c5 where aa = :idEmpresa ";
			queryString = queryString.replace(":idEmpresa", idEmpresa.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = ((BigDecimal) rows.get(0)).longValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar la Moneda asignada a la Empresa indicada: " + idEmpresa, e);
			resultado = 0;
		}
		
		return resultado;
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
	
	private int getGrupo(int year, int month) {
		String value = "";
		
		try {
			value = String.valueOf(year).substring(2) + (new DecimalFormat("00")).format(month);
			return Integer.parseInt(value);
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar el Grupo de Provision", e);
		}
		
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	private boolean facturaEstatus(Long idFactura) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		Integer resultado = 0;
		
		try {
			if (idFactura == null || idFactura <= 0L)
				return false;
			queryString += "select estatus from factura where id = :idFactura ";
			queryString = queryString.replace(":idFactura", idFactura.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = (Integer) rows.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Estatus de la Factura: " + idFactura, e);
			resultado = 0;
		}
		
		return (resultado != null && resultado.intValue() == 1);
	}
	
	@SuppressWarnings("unchecked")
	private boolean facturaTipoDeComprobante(Long idFactura) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		String resultado = "";
		
		try {
			if (idFactura == null || idFactura <= 0L)
				return false;
			queryString += "select tipo_comprobante from factura where id = :idFactura ";
			queryString = queryString.replace(":idFactura", idFactura.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = rows.get(0).toString();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Tipo de Comprobante de la Factura: " + idFactura, e);
			resultado = "";
		}
		
		return (resultado != null && "I".equals(resultado.trim()));
	}
	
	@SuppressWarnings("unchecked")
	private long facturaIdConcepto(Long idFactura) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long resultado = 0;
		
		try {
			if (idFactura == null || idFactura <= 0L)
				return 0L;
			queryString += "select id_concepto from factura_detalle where id = (select min(id) from factura_detalle where id_factura = :idFactura) ";
			queryString = queryString.replace(":idFactura", idFactura.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = ((BigDecimal) rows.get(0)).longValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el idConcepto de la Factura: " + idFactura, e);
			resultado = 0;
		}
		
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	private String facturaConcepto(Long idConcepto) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		String resultado = "";
		
		try {
			if (idConcepto == null || idConcepto <= 0L)
				return "";
			queryString += "select descripcion from concepto_facturacion where id = :idConcepto ";
			queryString = queryString.replace(":idConcepto", idConcepto.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = rows.get(0).toString();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar la descripcion del idConcepto: " + idConcepto, e);
			resultado = "";
		}
		
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> cfdiFacturasPendientesCancelacion() {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		List<Long> facturas = null;
		
		try {
			queryString = "select id from factura where estatus = 2 and id_empresa = :idEmpresa";
			queryString = queryString.replace(":idEmpresa", getIdEmpresa().toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty())
				return null;
			facturas = new ArrayList<Long>();
			for (Object row : rows)
				facturas.add(((BigDecimal) row).longValue());
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar las Facturas pendientes de Cancelacion", e);
			return null;
		}
		
		return facturas;
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> cfdiPagosPendientesCancelacion() {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		List<Long> facturas = null;
		
		try {
			queryString = "select id_timbre from factura_pagos where estatus = 2";
			queryString = queryString.replace(":idEmpresa", getIdEmpresa().toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty())
				return null;
			facturas = new ArrayList<Long>();
			for (Object row : rows)
				facturas.add(((BigDecimal) row).longValue());
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar las Pagos pendientes de Cancelacion", e);
			return null;
		}
		
		return facturas;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private List<Long> cfdiFacturasCanceladasPendientesAprobacion() {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		List<Long> facturas = null;
		
		try {
			queryString = "select id from factura where estatus = 3 and id_empresa = :idEmpresa";
			queryString = queryString.replace(":idEmpresa", getIdEmpresa().toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty())
				return null;
			facturas = new ArrayList<Long>();
			for (Object row : rows)
				facturas.add(((BigDecimal) row).longValue());
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar las Factura Canceladas pendientes de Aprobacion", e);
			return null;
		}
		
		return facturas;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private List<Long> cfdiPagosCanceladasPendientesAprobacion() {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		List<Long> timbres = null;
		
		try {
			queryString = "select id_timbre from factura_pagos where estatus = 3 ";
			queryString = queryString.replace(":idEmpresa", getIdEmpresa().toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty())
				return null;
			timbres = new ArrayList<Long>();
			for (Object row : rows)
				timbres.add(((BigDecimal) row).longValue());
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar los Pagos Cancelados pendientes de Aprobacion", e);
			return null;
		}
		
		return timbres;
	}
	
	private Respuesta cfdiEstatus(String rfcEmisor, String rfcReceptor, String total, String uuid, long idEmpresa) {
		Respuesta respuesta = null;
		// --------------------------------------------------------
		FactElectv33 timbre33 = null;

		try {
			// Atentamos la cancelacion del Timbre 
			timbre33 = new FactElectv33(this.infoSesion);
			timbre33.setDebugging(false);
			timbre33.setTesting(false); 
			timbre33.setDigestMethodSHA1();
			timbre33.setSoapRequestVersion11();
			respuesta = timbre33.estatus(rfcEmisor, rfcReceptor, total, uuid, idEmpresa);
		} catch (Exception e) { 
			log.error("\nOcurrio un problema al intentar consultar el Estatus del CFDI indicado: " + uuid + "\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError() + "\n", e);
		}
		
		return respuesta;
	}
	
	@SuppressWarnings("unchecked")
	public double obtenerLimiteFacturaPagada() throws Exception {
		List<String> nativeResult = null;
		String queryString = "";
		String resultado = "0";
		// -----------------------------
		Properties properties = null;
		String perfilName = "";
		
		try {
			properties = getFacturacionProperties();
			if (properties == null) {
				log.warn("No pude cargar los valores para registrar el Pago PUE automatico");
				return 0;
			}
			
			if (! properties.containsKey("perfil.diferencia.factura.pagada"))
				return 0;
			
			perfilName = properties.getProperty("perfil.diferencia.factura.pagada");
			queryString += "select b.ai from d7729f32ba7 a inner join b761110ccfe b on b.af = a.aa where a.af = ':perfilName' and b.ah = :idEmpresa ";
			queryString = queryString.replace(":perfilName", perfilName);
			queryString = queryString.replace(":idEmpresa", getIdEmpresa().toString());
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty())
				resultado = nativeResult.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Perfil indicado: " + perfilName, e);
			resultado = "0";
		}
		
		return Double.parseDouble(resultado);
	}
	
	private Properties getFacturacionProperties() {
		Properties properties = null;

		try {
			properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("/net/giro/cxc/logica/facturacion.properties"));
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar cargar el archivo 'facturacion.properties'.", e);
			properties = null;
		}
		
		return properties;
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
	
	private Long getIdEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L;
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
		printLog("##############################    TOPIC CUENTAS POR COBRAR    ##############################");
	}

	private void printLog(String mensaje) {
		try {
			mensajeLog(mensaje);
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.info("TOPIC/CXC Log\n\n" + mensaje);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Terminado, "OK\n" + mensaje);
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/CXC. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}

	private void printLog(String mensaje, Throwable throwable) {
		try {
			if (mensaje != null && ! "".equals(mensaje.trim()))
				mensajeLog(mensaje);
			mensajeLog("############################## TOPIC CUENTAS POR COBRAR --- FIN : " + new Date());
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.error("TOPIC/CXC Log\n\n" + mensaje + "\nEXCEPTION\n", throwable);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Error, mensaje + "\nEXCEPTION\n" + throwable.getMessage());
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/CXC. TopicEstatus: " + this.idTopicEstatus, e);
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

package net.giro.contabilidad.topic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.giro.contabilidad.beans.AsignacionGrupos;
import net.giro.contabilidad.beans.Grupos;
import net.giro.contabilidad.beans.GruposCuentas;
import net.giro.contabilidad.beans.MensajeContabilizar;
import net.giro.contabilidad.beans.MensajeTransaccionExt;
import net.giro.contabilidad.beans.PolizasInterfaces;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.beans.TransaccionesData;
import net.giro.contabilidad.logica.AsignacionGruposFac;
import net.giro.contabilidad.logica.GruposCuentasFac;
import net.giro.contabilidad.logica.MensajeTransaccionFac;
import net.giro.contabilidad.logica.PolizasInterfacesFac;
import net.giro.contabilidad.logica.TransaccionesDataFac;
import net.giro.contabilidad.logica.TransaccionesFac;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/CONTABILIZAR")}, 
	mappedName = "topic/CONTABILIZAR")
public class TopicContabilizar implements MessageListener {
	private static Logger log = Logger.getLogger(TopicContabilizar.class);
	private InitialContext ctx;
	private Connection conn;
	private DataSource datasource = null;
	private MensajeTransaccionFac ifzMensajes;
	private TransaccionesDataFac ifzTData;
	private PolizasInterfacesFac ifzPolizas;
	private MensajeTransaccionExt msgTransaccion;
	private MensajeContabilizar msgContabilizar;
	private JsonObject json;
	private List<Object> listContabilizar;
	private boolean grupoSinLlavesValores;
	private LinkedHashMap<String, String> mapImpuestos = new LinkedHashMap<String, String>();
	private LinkedHashMap<Long, List<Long>> mapConceptos = new LinkedHashMap<Long, List<Long>>();
	private HashMap<String, Long> valoresBusqueda = null;

    public TopicContabilizar() throws Exception {
    	try {
        	this.ctx = new InitialContext();
    		this.ifzMensajes = new MensajeTransaccionFac();
    		this.ifzTData = new TransaccionesDataFac();
    		this.ifzPolizas = new PolizasInterfacesFac();
    		this.msgContabilizar = new MensajeContabilizar();
        } catch (Exception e) {
        	log.error("Error al crear TransaccionesTopic", e);
        	throw e;
        }
    }
    
    public void onMessage(Message message) {
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		TextMessage mensaje = null;
    	String jsonString = "";
    	// ---------------------------------------------
    	List<TransaccionesData> listTData = null;
    	
    	try {
        	if (message instanceof TextMessage) {
        		mensaje = (TextMessage) message;
        		jsonString = mensaje.getText().trim();
        		
        		// Convertimos el mensaje en objeto JSON.
    			this.json = parser.parse(jsonString).getAsJsonObject();
        		this.msgContabilizar = gson.fromJson(jsonString, MensajeContabilizar.class);
        		this.msgTransaccion = this.ifzMensajes.convertir(this.ifzMensajes.findById(this.msgContabilizar.getIdMensajeTransaccion()));
        		
        		// Procesamos contabilizacion
        		Thread.sleep(1000);
				if (procesaContabilizacion()) {
					// Marcamos como contabilizado el lote si corresponde
					if (this.listContabilizar != null && ! this.listContabilizar.isEmpty()) {
						if (this.ifzTData == null)
							this.ifzTData = new TransaccionesDataFac();
						
			    		listTData = this.ifzTData.findInProperty("id", this.listContabilizar, 0);
			    		for (TransaccionesData tdata : listTData) {
			    			tdata.setContabilizado(1);
			    			tdata.setModificadoPor(tdata.getCreadoPor());
			    			tdata.setFechaModificacion(Calendar.getInstance().getTime());
			    			tdata.setCodigoError(0L);
			    			tdata.setDescripcionError("Contabilizado");
			    			
			    			// Actualizamos en la BD
			    			this.ifzTData.update(tdata);
			    		}
			    		
			    		// Actualizamos el Mensaje
			    		this.msgTransaccion.setEstatus(2);
			    		this.msgTransaccion.setEstatusMensaje("Contabilizado");
			    		this.ifzMensajes.update(this.msgTransaccion);
					}
				}
			}
        } catch (Exception e) {
        	guardarLog(-1L, "Error al recibir mensaje para Contabilizar. " + jsonString, null, e);
		} finally {
			this.closeConnection();
		}
    }

    // ----------------------------------------------------------------------
 	// METODOS PRIVADOS
 	// ----------------------------------------------------------------------
    
    private boolean procesaContabilizacion() {
		LinkedHashMap<Long, String> mapDescripciones = new LinkedHashMap<Long, String>();
    	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		HashMap<String, String> params = new HashMap<>();
    	TransaccionesFac ifzTransacciones = null;
    	AsignacionGruposFac ifzAsigGrupos = null;
		Transacciones pojoTransaccion = null;
		PolizasInterfaces poliza = null;
		List<TransaccionesData> listTData = null;
		List<PolizasInterfaces> polizas = null;
		List<AsignacionGrupos> listAsigGrupos = null;
		AsignacionGrupos asigGrupo = null;
		Long idConcepto = 0L;
    	
    	try {
    		if (this.ifzTData == null)
				this.ifzTData = new TransaccionesDataFac();
    		this.mapImpuestos = new LinkedHashMap<String, String>();

			// Abrimos coneccion
        	this.getConnection();
    		
    		listTData = this.ifzTData.findByProperty("lote", this.msgContabilizar.getLote(), 0);
    		if (listTData == null || listTData.isEmpty()) {
    			guardarLog(1L, "No pude encontrar registros para la peticion de contabilizacion. Transaccion " + this.msgContabilizar.getIdTransaccion(), null);
    			return false;
    		}
    		
    		params.clear();
    		ifzTransacciones = new TransaccionesFac();
    		ifzAsigGrupos = new AsignacionGruposFac();
    		polizas = new ArrayList<PolizasInterfaces>();
    		for (TransaccionesData tdata : listTData) {
    			// Recuperamos la Transacion a traves del codigo si corresponde
    			if (! mapDescripciones.containsKey(tdata.getCodigoTransaccion())) {
        			pojoTransaccion = ifzTransacciones.findByCodigo(tdata.getCodigoTransaccion());
        			if (pojoTransaccion == null) {
        				guardarLog(2L, "No se pudo obtener la Transaccion a partir del codigo: " + tdata.getCodigoTransaccion(), tdata);
            			return false;
        			}
    			}

        		// Generamos mapa de equivalencia de Impuestos por Transaccion
        		this.mapImpuestos = equivalenciaImpuestos(tdata.getCodigoTransaccion());
        		// Generamos mapa de equivalencia de Conceptos por Transaccion
    			this.mapConceptos = equivalenciaConceptos(tdata.getCodigoTransaccion());
    			idConcepto = tdata.getIdConcepto();
    			idConcepto = conceptoMatcher(tdata.getIdConcepto(), true);

    			mapDescripciones.put(pojoTransaccion.getId(), getTransacionNombre(pojoTransaccion.getId()));
    			mapDescripciones.put(tdata.getCodigoTransaccion(), "TRANSACCION");
    			mapDescripciones.put(tdata.getIdFormaPago(), getFormaPagoNombre(tdata.getIdFormaPago()));
    			mapDescripciones.put(idConcepto, getConceptoNombre(idConcepto));
        		
        		// Consultamos las asignaciones de grupos
        		// -------------------------------------------------------------------
        		params.put("idTransaccion", pojoTransaccion.getId().toString());
        		params.put("idFormaPago", tdata.getIdFormaPago().toString());
        		params.put("idConcepto", idConcepto.toString());
        		log.info("\nBusco coincidencia en Asignaciones de Grupos para: " + mapDescripciones.toString() + " ...");
        		listAsigGrupos = ifzAsigGrupos.findByProperties(params, 0);
        		if (listAsigGrupos == null || listAsigGrupos.isEmpty()) {
    	    		guardarLog(3L, "No puedo encontrar Cuentas Contables. No existe configuracion en AsignacionDeGrupos para recuperar Cuentas Contables. Parametros usados: Transaccion " + mapDescripciones.get(pojoTransaccion.getId()) + ", FormaDePago " + mapDescripciones.get(tdata.getIdFormaPago()) + " y Concepto " + mapDescripciones.get(idConcepto), tdata);
        			return false;
        		}
        		
        		if (listAsigGrupos.size() > 1) {
            		log.info(listAsigGrupos.size() + " Asignaciones de Grupos encontradas.");
    	    		guardarLog(3L, "Multiples configuraciones encontradas: No puedo determinar cual es la configuracion correcta para recuperar las Cuentas Contables. Parametros usados: Transaccion " + mapDescripciones.get(pojoTransaccion.getId()) + ", FormaDePago " + mapDescripciones.get(tdata.getIdFormaPago()) + " y Concepto " + mapDescripciones.get(idConcepto), tdata);
        			return false;
        		}

        		// Recuperamos la primera Asignacion de Grupos encontradas
        		// -------------------------------------------------------------------
        		asigGrupo = listAsigGrupos.get(0);
        		log.info("\nAsignacion de Grupo encontrada: " + asigGrupo.getId() + " :: " + params.toString() + "\n");

        		// Recuperamos las cuentas contables para grupo DEBITO
        		// -------------------------------------------------------------------
				this.valoresBusqueda = new HashMap<String, Long>();
        		poliza = getPolizaFromGrupo(tdata, asigGrupo.getIdGrupoDebito(), "C", asigGrupo.getTipoPoliza().toString(), polizas.size() + 1);
        		if (poliza == null) {
        			if (this.valoresBusqueda == null)
        				this.valoresBusqueda = new HashMap<String, Long>();
        			if (! this.grupoSinLlavesValores)
        				guardarLog(4L, "No se encontro Cuenta Contable para Grupo Debito (" + asigGrupo.getIdGrupoDebito().getId() + ") '" + asigGrupo.getIdGrupoDebito().getDescripcion() + "'. No se pudo encontrar una coincidencia en los valores de las llaves del Grupo:" + this.valoresBusqueda, tdata);
        			else
                		guardarLog(5L, "No se encontro Cuenta Contable. El Grupo Debito (" + asigGrupo.getIdGrupoCredito().getId() + ") '" + asigGrupo.getIdGrupoCredito().getDescripcion() + "' no tiene asignada ninguna llave", tdata, null);
        			this.grupoSinLlavesValores = false;
        			return false;
        		}
        		
        		poliza.setConcepto(pojoTransaccion.getGlosa() + " " + tdata.getReferencia1());
        		poliza.setFecha(formatter.parse(this.json.get("fecha").getAsString()));
        		polizas.add(poliza);

        		// Recuperamos las cuentas contables para grupo CREDITO
        		// -------------------------------------------------------------------
				this.valoresBusqueda = new HashMap<String, Long>();
        		poliza = getPolizaFromGrupo(tdata, asigGrupo.getIdGrupoCredito(), "A", asigGrupo.getTipoPoliza().toString(), polizas.size() + 1);
        		if (poliza == null) {
        			if (this.valoresBusqueda == null)
        				this.valoresBusqueda = new HashMap<String, Long>();
        			if (! this.grupoSinLlavesValores)
        				guardarLog(6L, "No se encontro Cuenta Contable para Grupo Credito (" + asigGrupo.getIdGrupoCredito().getId() + ") '" + asigGrupo.getIdGrupoCredito().getDescripcion() + "'. No se pudo encontrar una coincidencia en los valores de las llaves del Grupo:" + this.valoresBusqueda, tdata);
        			else
                		guardarLog(7L, "No se encontro Cuenta Contable. El Grupo Credito (" + asigGrupo.getIdGrupoCredito().getId() + ") '" + asigGrupo.getIdGrupoCredito().getDescripcion() + "' no tiene asignada ninguna llave", tdata, null);
        			this.grupoSinLlavesValores = false;
        			return false;
        		}
        		
        		poliza.setConcepto(pojoTransaccion.getGlosa() + " " + tdata.getReferencia1());
        		poliza.setFecha(formatter.parse(this.json.get("fecha").getAsString()));
    			polizas.add(poliza);

        		if (this.listContabilizar == null)
        			this.listContabilizar = new ArrayList<Object>(); 
				this.listContabilizar.add(tdata.getId());
    		}
    		
    		// Insertamos PolizasInterfaces
    		if (! insertaPolizaInterfaces(polizas)) {
    			guardarLog(5L, "No pude guardar las Polizas Contables generadas", null);
    			return false;
    		}
    	} catch (Exception e) {
    		guardarLog(-1L, "Error al procesar la Transaccion [TransaccionesTopic]", null, e);
			return false;
    	}
    	
    	return true;
    }
    
    private PolizasInterfaces getPolizaFromGrupo(TransaccionesData tdata, Grupos grupo, String tipo, String tipoPoliza, int noLinea) throws Exception {
    	TransaccionesData tdataAux = null;
    	GruposCuentasFac ifzGpoCuenta = new GruposCuentasFac();
    	PolizasInterfaces poliza = null;
    	List<GruposCuentas> listGpoCuenta = null;
    	HashMap<String, Long> valoresBase = null;
    	HashMap<String, Long> valoresGrupo = null;
    	String matchingLog = "";
    	String noMatchLog = "";
    	
    	try {
    		//log.info("TopicContabilizar :: Recupero LlavesValores para el Grupo " + grupo.getId() + " - " + grupo.getDescripcion());
        	listGpoCuenta = ifzGpoCuenta.findAll(grupo.getId()); 
        	if (listGpoCuenta == null || listGpoCuenta.isEmpty()) {
        		this.grupoSinLlavesValores = true;
        		return null;
        	}
        	
        	// Genero mapa de llaves valores de la TransaccionData
        	tdataAux = new TransaccionesData();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
        	BeanUtils.copyProperties(tdataAux, tdata);
        	valoresBase = generarMapaValoresBase(tdataAux, tipo);
    		matchingLog += "\n\n------------------------------------------------------------------------------------------------------------";
        	matchingLog += "\nTransaccionesData :: Concepto " + tdata.getIdConcepto() + ":" + tdata.getConcepto(); 
        	matchingLog += "\nGRUPO " + grupo.getId() + ":" + grupo.getDescripcion() + ("C".equals(tipo) ? " (DEBITO)" : " (CREDITO)")  + " :: " +  recuperaLlavesAsignadasGrupo(grupo);
        	matchingLog += "\nMATCH BASE  " + tdata.getId() + " : " + valoresBase.toString();
        	matchingLog += "\n------------------------------------------------------------------------------------------------------------\n";
        	
        	// Buscamos valores en el grupo para generar Poliza
    		for (GruposCuentas var : listGpoCuenta) {
            	// Genero mapa de llaves valores del GrupoCuenta (LlavesValores)
    			valoresGrupo = generarMapaValoresGrupo(var, tipo);
    			valoresBusqueda(valoresBase, valoresGrupo);
    			if (! valoresMatcher(valoresBase, valoresGrupo)) {
    				noMatchLog += "MATCHING ID " + var.getId() + " : " + valoresGrupo.toString() + " :: NO MATCH\n";
    				continue; 
    			}
    			
    			// Generamos poliza interfaces
    			noMatchLog = "";
				matchingLog += "MATCHING ID " + var.getId() + " : " + valoresGrupo.toString() + " :: MATCH\n";
    			poliza = new PolizasInterfaces();
    			poliza.setIdMensajeTransaccion(tdata.getIdMensajeTransaccion());
    			poliza.setIdTransaccionesData(tdata);
    			poliza.setIdInterfaz(Long.valueOf(tdata.getLote()));
    			poliza.setFolioPoliza(Long.valueOf(tdata.getPoliza()));
    			poliza.setTipoPoliza(tipoPoliza);
    			poliza.setCuentaContable(var.getNoCuenta());
    			poliza.setTipo(tipo);
    			poliza.setCargo(BigDecimal.ZERO);
    			poliza.setAbono(BigDecimal.ZERO);
    			if ("C".equals(tipo))
        			poliza.setCargo(tdata.getImporte());
    			else
        			poliza.setAbono(tdata.getImporte());
    			poliza.setEstatus("");
    			poliza.setFecha(Calendar.getInstance().getTime());
    			poliza.setSegmentoNegocio(0L);
    			poliza.setConcepto("");
    			poliza.setReferencia(tdata.getReferencia2());
    			poliza.setIdEmpresa(this.msgContabilizar.getIdEmpresa());
    			poliza.setEjercicio(this.msgContabilizar.getEjercicio());
    			poliza.setPeriodo(this.msgContabilizar.getPeriodo());
    			poliza.setNoLinea((long) noLinea);
    			poliza.setEncabezado("");
    			poliza.setIdOperacion(tdata.getIdOperacion());
    			poliza.setIdGrupoCuenta(var.getId());
    			poliza.setCreadoPor(tdata.getCreadoPor());
    			poliza.setFechaCreacion(Calendar.getInstance().getTime());
    			poliza.setModificadoPor(tdata.getCreadoPor());
    			poliza.setFechaModificacion(Calendar.getInstance().getTime());
    			break;
    		}
    		
    		if (! "".equals(noMatchLog.trim()))
    			matchingLog += noMatchLog;
    		matchingLog += "------------------------------------------------------------------------------------------------------------\n";
	    	log.info(matchingLog);
        	return poliza; 
    	} catch (Exception e) {
    		guardarLog(-1L, "Error al intentar generar poliza a partir del Grupo " + grupo.getId() + " - " + grupo.getDescripcion(), null, e);
        	throw e;
    	}
    }

    private boolean insertaPolizaInterfaces(List<PolizasInterfaces> polizas) throws Exception {
    	try { 
    		// Agrupamos polizas similares
    		polizas = agrupadorPolizas(polizas);
        	for (PolizasInterfaces poliza : polizas) {
        		poliza.setEstatus("P");
        		poliza.setEstatusMensaje("Contabilizado");
    			poliza.setFechaCreacion(Calendar.getInstance().getTime());
    			poliza.setFechaModificacion(Calendar.getInstance().getTime());
        	}
        	
        	this.ifzPolizas.saveOrUpdateList(polizas);
    	} catch (Exception e) {
    		guardarLog(-1L, "Error al insertar polizas", null, e);
    		return false;
    	}
    	
    	return true;
    }

    private boolean valoresMatcher(HashMap<String, Long> valoresBase, HashMap<String, Long> valoresGrupo) {
    	if (valoresBase == null || valoresGrupo == null)
    		return false;

    	for (Entry<String, Long> valor : valoresGrupo.entrySet()) {
    		// Si la base tiene la llave asignada en el grupo
    		if (! valoresBase.containsKey(valor.getKey()))
    			return false;
    		
    		// Si el valor de la llave del grupo coincide con el valor en la base
    		if (! valoresBase.get(valor.getKey()).equals(valor.getValue()))
    			return false;
    	}
    	
    	return true;
    }
    
    private void valoresBusqueda(HashMap<String, Long> valoresBase, HashMap<String, Long> valoresGrupo) {
    	if (this.valoresBusqueda != null)
    		this.valoresBusqueda = new HashMap<String, Long>();
    	if (valoresBase == null || valoresGrupo == null)
    		return;
    	
    	for (Entry<String, Long> valor : valoresGrupo.entrySet()) {
    		if (valoresBase.containsKey(valor.getKey()))
    			this.valoresBusqueda.put(valor.getKey(), valoresBase.get(valor.getKey()));
    	}
    }
    
    private List<PolizasInterfaces> agrupadorPolizas(List<PolizasInterfaces> polizas) {
    	return polizas;
    }
    
	private HashMap<String, Long> generarMapaValoresBase(TransaccionesData tData, String tipo) {
		HashMap<String, Long> valorLlave = new HashMap<String, Long>();
		
		if (tData == null)
			return null;
		
		try {
			// Proceso de mapeo de impuestos (Equivalencia)
			if (! this.mapImpuestos.isEmpty()) 
				tData = impuestoMatcher(tData, tipo);
			
			if (tData.getValorLlave1() != null && ! "".equals(tData.getValorLlave1()))
				valorLlave.put("valorLlave1", Long.valueOf(tData.getValorLlave1()));
			if (tData.getValorLlave2() != null && ! "".equals(tData.getValorLlave2()))
				valorLlave.put("valorLlave2", Long.valueOf(tData.getValorLlave2()));
			if (tData.getValorLlave3() != null && ! "".equals(tData.getValorLlave3()))
				valorLlave.put("valorLlave3", Long.valueOf(tData.getValorLlave3()));
			if (tData.getValorLlave4() != null && ! "".equals(tData.getValorLlave4()))
				valorLlave.put("valorLlave4", Long.valueOf(tData.getValorLlave4()));
			if (tData.getValorLlave5() != null && ! "".equals(tData.getValorLlave5()))
				valorLlave.put("valorLlave5", Long.valueOf(tData.getValorLlave5()));
			if (tData.getValorLlave6() != null && ! "".equals(tData.getValorLlave6()))
				valorLlave.put("valorLlave6", Long.valueOf(tData.getValorLlave6()));
			if (tData.getValorLlave7() != null && ! "".equals(tData.getValorLlave7()))
				valorLlave.put("valorLlave7", Long.valueOf(tData.getValorLlave7()));
			if (tData.getValorLlave8() != null && ! "".equals(tData.getValorLlave8()))
				valorLlave.put("valorLlave8", Long.valueOf(tData.getValorLlave8()));
			if (tData.getValorLlave9() != null && ! "".equals(tData.getValorLlave9()))
				valorLlave.put("valorLlave9", Long.valueOf(tData.getValorLlave9()));
			if (tData.getValorLlave10() != null && ! "".equals(tData.getValorLlave10()))
				valorLlave.put("valorLlave10", Long.valueOf(tData.getValorLlave10()));
			if (tData.getValorLlave11() != null && ! "".equals(tData.getValorLlave11()))
				valorLlave.put("valorLlave11", Long.valueOf(tData.getValorLlave11()));
			if (tData.getValorLlave12() != null && ! "".equals(tData.getValorLlave12()))
				valorLlave.put("valorLlave12", Long.valueOf(tData.getValorLlave12()));
			if (tData.getValorLlave13() != null && ! "".equals(tData.getValorLlave13()))
				valorLlave.put("valorLlave13", Long.valueOf(tData.getValorLlave13()));
			if (tData.getValorLlave14() != null && ! "".equals(tData.getValorLlave14()))
				valorLlave.put("valorLlave14", Long.valueOf(tData.getValorLlave14()));
			if (tData.getValorLlave15() != null && ! "".equals(tData.getValorLlave15()))
				valorLlave.put("valorLlave15", Long.valueOf(tData.getValorLlave15()));
			if (tData.getValorLlave16() != null && ! "".equals(tData.getValorLlave16()))
				valorLlave.put("valorLlave16", Long.valueOf(tData.getValorLlave16()));
			if (tData.getValorLlave17() != null && ! "".equals(tData.getValorLlave17()))
				valorLlave.put("valorLlave17", Long.valueOf(tData.getValorLlave17()));
			if (tData.getValorLlave18() != null && ! "".equals(tData.getValorLlave18()))
				valorLlave.put("valorLlave18", Long.valueOf(tData.getValorLlave18()));
			if (tData.getValorLlave19() != null && ! "".equals(tData.getValorLlave19()))
				valorLlave.put("valorLlave19", Long.valueOf(tData.getValorLlave19()));
			if (tData.getValorLlave20() != null && ! "".equals(tData.getValorLlave20()))
				valorLlave.put("valorLlave20", Long.valueOf(tData.getValorLlave20()));
			if (tData.getValorLlave21() != null && ! "".equals(tData.getValorLlave21()))
				valorLlave.put("valorLlave21", Long.valueOf(tData.getValorLlave21()));
			if (tData.getValorLlave22() != null && ! "".equals(tData.getValorLlave22()))
				valorLlave.put("valorLlave22", Long.valueOf(tData.getValorLlave22()));
			if (tData.getValorLlave23() != null && ! "".equals(tData.getValorLlave23()))
				valorLlave.put("valorLlave23", Long.valueOf(tData.getValorLlave23()));
			if (tData.getValorLlave24() != null && ! "".equals(tData.getValorLlave24()))
				valorLlave.put("valorLlave24", Long.valueOf(tData.getValorLlave24()));
			if (tData.getValorLlave25() != null && ! "".equals(tData.getValorLlave25()))
				valorLlave.put("valorLlave25", Long.valueOf(tData.getValorLlave25()));
			if (tData.getValorLlave26() != null && ! "".equals(tData.getValorLlave26()))
				valorLlave.put("valorLlave26", Long.valueOf(tData.getValorLlave26()));
			if (tData.getValorLlave27() != null && ! "".equals(tData.getValorLlave27()))
				valorLlave.put("valorLlave27", Long.valueOf(tData.getValorLlave27()));
			if (tData.getValorLlave28() != null && ! "".equals(tData.getValorLlave28()))
				valorLlave.put("valorLlave28", Long.valueOf(tData.getValorLlave28()));
			if (tData.getValorLlave29() != null && ! "".equals(tData.getValorLlave29()))
				valorLlave.put("valorLlave29", Long.valueOf(tData.getValorLlave29()));
			if (tData.getValorLlave30() != null && ! "".equals(tData.getValorLlave30()))
				valorLlave.put("valorLlave30", Long.valueOf(tData.getValorLlave30()));
			
			return valorLlave;
		} catch (Exception e) {
			guardarLog(-1L, "Error en Logica_Contabilidad.TopicContabiliar.getValorLLavesFromTransaccionesData(TransaccionesData)", null, e);
    		return null;
    	}
	}
    
    private HashMap<String, Long> generarMapaValoresGrupo(GruposCuentas gCuenta, String tipo) {
    	HashMap<String, Long> valorLlave = new HashMap<String, Long>();
    	
    	if (gCuenta == null)
    		return null;
    	
    	try {
			// Proceso de mapeo de impuestos (equivalencia)
			if (! this.mapImpuestos.isEmpty()) 
				gCuenta = impuestoMatcherGruposCuentas(gCuenta, tipo);
			
    		if (gCuenta.getValorLlave1() != null && gCuenta.getValorLlave1() > 0L && gCuenta.getIdGrupo().getLlave1() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave1().getPosicion(), gCuenta.getValorLlave1());
    		if (gCuenta.getValorLlave2() != null && gCuenta.getValorLlave2() > 0L && gCuenta.getIdGrupo().getLlave2() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave2().getPosicion(), gCuenta.getValorLlave2());
    		if (gCuenta.getValorLlave3() != null && gCuenta.getValorLlave3() > 0L && gCuenta.getIdGrupo().getLlave3() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave3().getPosicion(), gCuenta.getValorLlave3());
    		if (gCuenta.getValorLlave4() != null && gCuenta.getValorLlave4() > 0L && gCuenta.getIdGrupo().getLlave4() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave4().getPosicion(), gCuenta.getValorLlave4());
    		if (gCuenta.getValorLlave5() != null && gCuenta.getValorLlave5() > 0L && gCuenta.getIdGrupo().getLlave5() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave5().getPosicion(), gCuenta.getValorLlave5());
    		if (gCuenta.getValorLlave6() != null && gCuenta.getValorLlave6() > 0L && gCuenta.getIdGrupo().getLlave6() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave6().getPosicion(), gCuenta.getValorLlave6());
    		if (gCuenta.getValorLlave7() != null && gCuenta.getValorLlave7() > 0L && gCuenta.getIdGrupo().getLlave7() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave7().getPosicion(), gCuenta.getValorLlave7());
    		if (gCuenta.getValorLlave8() != null && gCuenta.getValorLlave8() > 0L && gCuenta.getIdGrupo().getLlave8() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave8().getPosicion(), gCuenta.getValorLlave8());
    		if (gCuenta.getValorLlave9() != null && gCuenta.getValorLlave9() > 0L && gCuenta.getIdGrupo().getLlave9() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave9().getPosicion(), gCuenta.getValorLlave9());
    		if (gCuenta.getValorLlave10() != null && gCuenta.getValorLlave10() > 0L && gCuenta.getIdGrupo().getLlave10() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave10().getPosicion(), gCuenta.getValorLlave10());
    		if (gCuenta.getValorLlave11() != null && gCuenta.getValorLlave11() > 0L && gCuenta.getIdGrupo().getLlave11() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave11().getPosicion(), gCuenta.getValorLlave11());
    		if (gCuenta.getValorLlave12() != null && gCuenta.getValorLlave12() > 0L && gCuenta.getIdGrupo().getLlave12() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave12().getPosicion(), gCuenta.getValorLlave12());
    		if (gCuenta.getValorLlave13() != null && gCuenta.getValorLlave13() > 0L && gCuenta.getIdGrupo().getLlave13() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave13().getPosicion(), gCuenta.getValorLlave13());
    		if (gCuenta.getValorLlave14() != null && gCuenta.getValorLlave14() > 0L && gCuenta.getIdGrupo().getLlave14() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave14().getPosicion(), gCuenta.getValorLlave14());
    		if (gCuenta.getValorLlave15() != null && gCuenta.getValorLlave15() > 0L && gCuenta.getIdGrupo().getLlave15() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave15().getPosicion(), gCuenta.getValorLlave15());
    		if (gCuenta.getValorLlave16() != null && gCuenta.getValorLlave16() > 0L && gCuenta.getIdGrupo().getLlave16() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave16().getPosicion(), gCuenta.getValorLlave16());
    		if (gCuenta.getValorLlave17() != null && gCuenta.getValorLlave17() > 0L && gCuenta.getIdGrupo().getLlave17() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave17().getPosicion(), gCuenta.getValorLlave17());
    		if (gCuenta.getValorLlave18() != null && gCuenta.getValorLlave18() > 0L && gCuenta.getIdGrupo().getLlave18() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave18().getPosicion(), gCuenta.getValorLlave18());
    		if (gCuenta.getValorLlave19() != null && gCuenta.getValorLlave19() > 0L && gCuenta.getIdGrupo().getLlave19() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave19().getPosicion(), gCuenta.getValorLlave19());
    		if (gCuenta.getValorLlave20() != null && gCuenta.getValorLlave20() > 0L && gCuenta.getIdGrupo().getLlave20() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave20().getPosicion(), gCuenta.getValorLlave20());
    		if (gCuenta.getValorLlave21() != null && gCuenta.getValorLlave21() > 0L && gCuenta.getIdGrupo().getLlave21() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave21().getPosicion(), gCuenta.getValorLlave21());
    		if (gCuenta.getValorLlave22() != null && gCuenta.getValorLlave22() > 0L && gCuenta.getIdGrupo().getLlave22() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave22().getPosicion(), gCuenta.getValorLlave22());
    		if (gCuenta.getValorLlave23() != null && gCuenta.getValorLlave23() > 0L && gCuenta.getIdGrupo().getLlave23() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave23().getPosicion(), gCuenta.getValorLlave23());
    		if (gCuenta.getValorLlave24() != null && gCuenta.getValorLlave24() > 0L && gCuenta.getIdGrupo().getLlave24() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave24().getPosicion(), gCuenta.getValorLlave24());
    		if (gCuenta.getValorLlave25() != null && gCuenta.getValorLlave25() > 0L && gCuenta.getIdGrupo().getLlave25() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave25().getPosicion(), gCuenta.getValorLlave25());
    		if (gCuenta.getValorLlave26() != null && gCuenta.getValorLlave26() > 0L && gCuenta.getIdGrupo().getLlave26() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave26().getPosicion(), gCuenta.getValorLlave26());
    		if (gCuenta.getValorLlave27() != null && gCuenta.getValorLlave27() > 0L && gCuenta.getIdGrupo().getLlave27() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave27().getPosicion(), gCuenta.getValorLlave27());
    		if (gCuenta.getValorLlave28() != null && gCuenta.getValorLlave28() > 0L && gCuenta.getIdGrupo().getLlave28() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave28().getPosicion(), gCuenta.getValorLlave28());
    		if (gCuenta.getValorLlave29() != null && gCuenta.getValorLlave29() > 0L && gCuenta.getIdGrupo().getLlave29() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave29().getPosicion(), gCuenta.getValorLlave29());
    		if (gCuenta.getValorLlave30() != null && gCuenta.getValorLlave30() > 0L && gCuenta.getIdGrupo().getLlave30() != null)
    			valorLlave.put("valorLlave" + gCuenta.getIdGrupo().getLlave30().getPosicion(), gCuenta.getValorLlave30());
        	
        	return valorLlave;
		} catch (Exception e) {
			guardarLog(-1L, "Error en Logica_Contabilidad.TopicContabiliar.getValorLLavesFromGruposCuenta(GruposCuentas)", null, e);
    		return null;
    	}
    }

    private String recuperaLlavesAsignadasGrupo(Grupos grupo) {
    	String llavesAsignadas = "";
    	
    	if (grupo == null)
    		return llavesAsignadas;
    	
    	try {
    		if (grupo.getLlave1() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave1().getPosicion();
            if (grupo.getLlave2() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave2().getPosicion();
            if (grupo.getLlave3() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave3().getPosicion();
            if (grupo.getLlave4() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave4().getPosicion();
            if (grupo.getLlave5() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave5().getPosicion();
            if (grupo.getLlave6() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave6().getPosicion();
            if (grupo.getLlave7() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave7().getPosicion();
            if (grupo.getLlave8() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave8().getPosicion();
            if (grupo.getLlave9() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave9().getPosicion();
            if (grupo.getLlave10() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave10().getPosicion();
            if (grupo.getLlave11() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave11().getPosicion();
            if (grupo.getLlave12() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave12().getPosicion();
            if (grupo.getLlave13() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave13().getPosicion();
            if (grupo.getLlave14() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave14().getPosicion();
            if (grupo.getLlave15() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave15().getPosicion();
            if (grupo.getLlave16() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave16().getPosicion();
            if (grupo.getLlave17() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave17().getPosicion();
            if (grupo.getLlave18() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave18().getPosicion();
            if (grupo.getLlave19() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave19().getPosicion();
            if (grupo.getLlave20() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave20().getPosicion();
            if (grupo.getLlave21() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave21().getPosicion();
            if (grupo.getLlave22() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave22().getPosicion();
            if (grupo.getLlave23() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave23().getPosicion();
            if (grupo.getLlave24() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave24().getPosicion();
            if (grupo.getLlave25() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave25().getPosicion();
            if (grupo.getLlave26() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave26().getPosicion();
            if (grupo.getLlave27() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave27().getPosicion();
            if (grupo.getLlave28() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave28().getPosicion();
            if (grupo.getLlave29() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave29().getPosicion();
            if (grupo.getLlave30() != null)
                llavesAsignadas += ((! "".equals(llavesAsignadas.trim())) ? "," : "") + "Llave" + grupo.getLlave30().getPosicion();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar las llaves asignadas del grupo indicado", e);
			llavesAsignadas = "";
    	}
    	
    	return llavesAsignadas;
    }
    
    private String getTransacionNombre(Long idTransaccion) {
    	String resultado = "DESCONOCIDO";
		String strQuery = "";
		ResultSet rs = null;
    	
    	try {
    		if (idTransaccion == null || idTransaccion <= 0L)
    			return "";
        	// Generamos consulta
        	strQuery = "SELECT codigo || '-' || transaccion FROM transacciones WHERE id = " + idTransaccion;
        	// Ejecutamos consulta
    		rs = this.conn.createStatement().executeQuery(strQuery);
    		if (rs.next())
    			resultado = rs.getString(1);
    	} catch (Exception e) {
			log.error("No se pudo obtener la descripcion de la Transaccion ID " + idTransaccion, e);
    	}
		
    	return resultado;
    }

    private String getFormaPagoNombre(Long idFormaPago) {
    	String resultado = "DESCONOCIDO";
		String strQuery = "";
		ResultSet rs = null;
    	
    	try {
    		if (idFormaPago == null || idFormaPago <= 0L)
    			return "";
        	// Generamos consulta
        	strQuery = "SELECT forma_pago FROM formas_pagos WHERE forma_pago_id = " + idFormaPago;
        	// Ejecutamos consulta
    		rs = this.conn.createStatement().executeQuery(strQuery);
    		if (rs.next())
    			resultado = rs.getString(1);
    	} catch (Exception e) {
			log.error("No se pudo obtener la descripcion de la Forma de Pago ID " + idFormaPago, e);
    	}
		
    	return resultado;
    }

    private String getConceptoNombre(Long idConcepto) {
    	String resultado = "DESCONOCIDO";
		String strQuery = "";
		ResultSet rs = null;
    	
    	try {
    		if (idConcepto == null || idConcepto <= 0L)
    			return "";
        	// Generamos consulta
        	strQuery = "SELECT concepto FROM conceptos WHERE id = " + idConcepto;
        	// Ejecutamos consulta
    		rs = this.conn.createStatement().executeQuery(strQuery);
    		if (rs.next())
    			resultado = rs.getString(1);
    	} catch (Exception e) {
			log.error("No se pudo obtener la descripcion del Concepto ID " + idConcepto, e);
    	}
		
    	return resultado;
    }
    
    private LinkedHashMap<String, String> equivalenciaImpuestos(Long codigoTransaccion) {
    	LinkedHashMap<String, String> mapImpuestos = new LinkedHashMap<String, String>();
		ResultSet rs = null;
		String strQuery = "";
    	
    	try {
    		if (codigoTransaccion == null || codigoTransaccion <= 0L)
    			return mapImpuestos;
        	// Generamos consulta
        	strQuery += "select id_impuesto_origen || '-' || anotacion as id_impuesto_origen, id_impuesto_destino from impuesto_equivalencia ";
        	strQuery += "where codigo_transaccion = :codigoTransaccion order by anotacion desc,id_impuesto_origen,id_impuesto_destino ";
        	strQuery = strQuery.replace(":codigoTransaccion", codigoTransaccion.toString());
        	// Ejecutamos consulta
    		rs = this.conn.createStatement().executeQuery(strQuery);
    		while (rs.next()) 
    			mapImpuestos.put(String.valueOf(rs.getString(1)), String.valueOf(rs.getLong(2)));
    	} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar equivalencias de impuestos para la transaccion " + codigoTransaccion, e);
    	} finally {
			log.info(mapImpuestos.size() + " Equivalencias de impuestos encontradas para la transaccion " + codigoTransaccion);
    	}
    	
    	return mapImpuestos;
    }

    private TransaccionesData impuestoMatcher(TransaccionesData tdata, String anotacion) {
    	String key = "";
    	
    	try {
    		key = tdata.getValorLlave5();
    		if (anotacion != null && ! "".equals(anotacion.trim()))
    			key = key + "-" + anotacion;
    		if (this.mapImpuestos.containsKey(key)) // MATCH directo
    			tdata.setValorLlave5(this.mapImpuestos.get(key));
    	} catch (Exception e) {
    		log.info("Ocurrio un problema al intentar intercambiar la equivalencia de impuesto en TransaccionesData", e);
    	}
    	
    	return tdata;
    }
    
    private GruposCuentas impuestoMatcherGruposCuentas(GruposCuentas gCuenta, String anotacion) {
    	String key = "";
    	
    	try {
    		key = String.valueOf(gCuenta.getValorLlave5());
    		if (anotacion != null && ! "".equals(anotacion.trim()))
    			key = key + "-" + anotacion;
    		if (this.mapImpuestos.containsKey(key)) // MATCH directo
    			gCuenta.setValorLlave5(Long.parseLong(this.mapImpuestos.get(key)));
    	} catch (Exception e) {
    		log.info("Ocurrio un problema al intentar intercambiar la equivalencia de impuesto en GruposCuentas", e);
    	}
    	
    	return gCuenta;
    }

    private LinkedHashMap<Long, List<Long>> equivalenciaConceptos(Long codigoTransaccion) {
    	LinkedHashMap<Long, List<Long>> mapConceptos = new LinkedHashMap<Long, List<Long>>();
    	List<Long> listConceptos = null;
		ResultSet rs = null;
		String strQuery = "";
    	
    	try {
    		if (codigoTransaccion == null || codigoTransaccion <= 0L)
    			return mapConceptos;
			
    		// Generamos consulta y Ejecutamos consulta
        	strQuery += "select id_concepto_origen, id_concepto_destino from equivalencia_conceptos_contables ";
        	strQuery += "where codigo_transaccion = :codigoTransaccion order by anotacion desc,id_concepto_origen,id_concepto_destino ";
        	strQuery = strQuery.replace(":codigoTransaccion", codigoTransaccion.toString());
        	rs = this.conn.createStatement().executeQuery(strQuery);
    		while (rs.next()) {
				listConceptos = new ArrayList<Long>();
    			if (mapConceptos.containsKey(rs.getLong(1)))
    				listConceptos = mapConceptos.get(rs.getLong(1));
				listConceptos.add(rs.getLong(2));
    			mapConceptos.put(rs.getLong(1), listConceptos);
    		}
    	} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar equivalencias de conceptos contables para la transaccion " + codigoTransaccion, e);
    	} finally {
			log.info(mapConceptos.size() + " Equivalencias de conceptos contables encontrados para la transaccion " + codigoTransaccion);
    	}
    	
    	return mapConceptos;
    }

    private long conceptoMatcher(long idConcepto, boolean reverseMatch) {
    	if (this.mapConceptos == null || this.mapConceptos.isEmpty())
    		return idConcepto;
    	if (idConcepto <= 0L)
    		return idConcepto;
    	if (! reverseMatch)
    		return this.mapConceptos.containsKey(idConcepto) ? this.mapConceptos.get(idConcepto).get(0) : idConcepto;
    	
    	for (Entry<Long, List<Long>> item : this.mapConceptos.entrySet()) {
    		if (item.getValue().contains(idConcepto)) {
    			idConcepto = item.getKey();
    			break;
    		}
    	}
    	
		return idConcepto;
    }

    private void guardarLog(long codigoError, String mensajeError, TransaccionesData tdata) {
    	guardarLog(codigoError, mensajeError, tdata, null);
    }
    
    private void guardarLog(long codigoError, String mensajeError, TransaccionesData tdata, Throwable t) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	BufferedWriter out = null;
    	FileWriter fwco = null;
    	File file = null;
    	String logFileName = "";
    	String encabezado = "";
    	
    	try {
    		// Guardamos estatus en MensajeTransaccion
    		if (this.msgTransaccion != null && this.ifzMensajes != null) {
	    		this.msgTransaccion.setEstatusMensaje(mensajeError + " :: Etapa 3 del proceso.");
	    		this.ifzMensajes.update(this.msgTransaccion);
    		}
    		
    		// Guardamos estatus en TransaccionesData si corresponde
    		if (tdata != null && this.ifzTData != null) {
    			tdata.setCodigoError(codigoError);
    			tdata.setDescripcionError(mensajeError);
    			this.ifzTData.update(tdata);
    		}
    		
    		// Guardamos archivo log
    		log.error(mensajeError, t);
    		logFileName = "/tmp/C-" + formatter.format(Calendar.getInstance().getTime()) + ".log";
    		file = new File(logFileName);
    		
    		if (! file.exists()) {
    			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
    			file.createNewFile();
    			encabezado = this.getClass().getCanonicalName() 
    					+ "        " + formatter.format(Calendar.getInstance().getTime())
    					+ "\n-------------------------------------------------------------------------\n";
    		}
    		
        	fwco = new FileWriter(new File(logFileName), true);
        	out = new BufferedWriter(fwco);
        	if (! "".equals(encabezado))
        		out.write(encabezado);
        	out.write("\nMensaje:\n");
        	out.write(this.json.toString() + "\n");
    		if (t != null) {
    			out.write("\nStackTrace:\n");
    			t.printStackTrace(new PrintWriter(out));
    		} 
    		out.close();
    	} catch (Exception e) {
    		log.error("Error en Logica_Contabilidad.TopicContabilizar.guardarLog()", e);
    	}
    }

    private void getConnection() throws Exception {
		try {
			if (this.conn == null) {
				if (this.ctx == null)
					this.ctx = new InitialContext();
				this.datasource = ((DataSource) this.ctx.lookup("java:/giroDS"));
				this.conn = this.datasource.getConnection();
			}
		} catch (Exception e) {
    		log.error("Error en Logica_Contabilidad.TopicTransacciones.getConnection(). Error al obtener la coneccion a giroDS", e);
		}
	}
    
    private void closeConnection() {
		try {
			if (this.conn == null)
				return;
			if (! this.conn.isClosed()) 
				this.conn.close();
			this.conn = null;
		} catch (SQLException e) {
    		log.error("Error en Logica_Contabilidad.TopicTransacciones.closeConnection()", e);
		}
	}
}

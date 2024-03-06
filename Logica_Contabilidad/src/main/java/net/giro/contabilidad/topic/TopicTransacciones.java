package net.giro.contabilidad.topic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.giro.contabilidad.beans.MensajeContabilizar;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;
import net.giro.contabilidad.beans.OperacionesIntegradasSql;
import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;
import net.giro.contabilidad.beans.TransaccionesData;
import net.giro.contabilidad.logica.MensajeTransaccionFac;
import net.giro.contabilidad.logica.OperacionesIntegradasConceptosSqlFac;
import net.giro.contabilidad.logica.OperacionesIntegradasSqlFac;
import net.giro.contabilidad.logica.OperacionesIntegradasTransaccionesFac;
import net.giro.contabilidad.logica.TransaccionesDataFac;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/TRANSACCIONES")}, 
	mappedName = "topic/TRANSACCIONES")
public class TopicTransacciones implements MessageListener {
	private static Logger log = Logger.getLogger(TopicTransacciones.class);
	private InitialContext ctx;
	private Connection conn;
	private DataSource datasource = null;
	private OperacionesIntegradasTransaccionesFac ifzIntegradasTransacciones;
	private OperacionesIntegradasSqlFac ifzIntegradasCamposSQL;
	private OperacionesIntegradasConceptosSqlFac ifzIntegradasConceptosSQL;
	private TransaccionesDataFac ifzTData; 
	private MensajeTransaccionFac ifzMsgTransaccion;
	private MensajeTransaccion msgTransaccion;
	private JsonObject json;
    
    public TopicTransacciones() throws Exception {
        try {
        	this.ctx = new InitialContext();
        	this.ifzMsgTransaccion = new MensajeTransaccionFac();
        	this.ifzIntegradasCamposSQL = new OperacionesIntegradasSqlFac();
        	this.ifzIntegradasConceptosSQL = new OperacionesIntegradasConceptosSqlFac();
        	this.ifzIntegradasTransacciones = new OperacionesIntegradasTransaccionesFac();
        } catch (Exception e) {
        	log.error("Error al crear TopicTransacciones", e);
        	throw e;
        }
    }
	
	public void onMessage(Message message) {
		JsonParser parser = null;
		Gson gson = null;
		TextMessage mensaje = null;
		String jsonString = "";
		
        try {
        	if (message instanceof TextMessage) {
				mensaje = (TextMessage) message;
				gson = new GsonBuilder().setDateFormat("MM/dd/yyyy HH:mm:ss").serializeNulls().create();
				parser = new JsonParser();
				jsonString = mensaje.getText().trim(); 
				
				// Transformaciones del mensaje recibido
				this.json = parser.parse(jsonString).getAsJsonObject();
				this.msgTransaccion = new MensajeTransaccion();
				this.msgTransaccion = gson.fromJson(this.json.toString(), MensajeTransaccion.class);

        		// Procesamos Transaccion
        		Thread.sleep(1000);
				if (procesaTransaccion()) // Lanzamos mensaje a cola de contabilizacion en desde un hilo secundario
					generaMensaje();
			}
        } catch (Exception e) {
    		log.error("Error en Logica_Contabilidad.TopicTransacciones.onMessage(Message). Error al recibir mensaje [TopicTransacciones] --> " + jsonString, e);
        	guardarLog("Error al recibir mensaje [TopicTransacciones] --> " + jsonString, e);
		} 
    }

	// ----------------------------------------------------------------------
	// METODOS PRIVADOS 
	// ----------------------------------------------------------------------
    
	private boolean procesaTransaccion() throws Exception {
		ResultSet rs = null;
		String strQuery = "";
		String mensajellaves = "";
    	String conceptoActual = "";
		String columnName = "";
		String columnValue = "";
		boolean conConceptos = true;
		boolean noResult = true;
		Integer orden = 0;
		// -------------------------------------------------------------
		ArrayList<HashMap<String, String>> listaMain = null;
		HashMap<String, String> listValores = null;
		HashMap<String, String> listValoresAux = null;
		// -------------------------------------------------------------
		String codigoTransaccion = "";
		OperacionesIntegradasTransacciones pojoOperacionIntegrada = null;
		List<OperacionesIntegradasSql> listSQLCampos = null;
		List<OperacionesIntegradasConceptosSql> listSQLConceptos = null;
		// -------------------------------------------------------------
		SimpleDateFormat formatter = null;
		List<TransaccionesData> listTData = null;
		TransaccionesData tdata = null;
		Gson gson = new Gson();
		String jsonString = "";
		
		try {
        	rs = null;
        	strQuery = "";
			if (this.ifzMsgTransaccion == null)
				this.ifzMsgTransaccion = new MensajeTransaccionFac();

			// Abrimos conexion
        	// -------------------------------------------------------------------------------
        	getConnection();
        	// Asignando poliza y lote
        	// -------------------------------------------------------------------------------
			log.info("Asignando Poliza y Lote ...");
        	listValores = new HashMap<String, String>();
			listValores.put("idTransaccion", json.get("idTransaccion").getAsString());
			listValores.put("poliza", this.msgTransaccion.getPoliza().toString());
			listValores.put("lote", this.msgTransaccion.getLote().toString());
        	rs = null;

			log.info("Recuperando Operacion Integradas ...");
        	codigoTransaccion = this.msgTransaccion.getIdTransaccion().toString();
        	pojoOperacionIntegrada = this.ifzIntegradasTransacciones.findByCodigoTransaccion(this.msgTransaccion.getIdTransaccion());
			if (pojoOperacionIntegrada == null || pojoOperacionIntegrada.getId() == null || pojoOperacionIntegrada.getId() <= 0L) {
				guardarLog("No se pudo obtener la Operacion Integrada de la Transaccion " + codigoTransaccion, codigoTransaccion);
				return false;
			}

			log.info("Recuperando Campos ...");
			listSQLCampos = this.ifzIntegradasCamposSQL.findAll(pojoOperacionIntegrada.getId()); 
			if (listSQLCampos == null || listSQLCampos.isEmpty()) {
				guardarLog("No se pudo obtener los Campos de la Transaccion " + codigoTransaccion, codigoTransaccion);
				return false;
			}

			log.info("Recuperando Conceptos ...");
			listSQLConceptos = this.ifzIntegradasConceptosSQL.findAll(pojoOperacionIntegrada.getId()); 
			if (listSQLConceptos == null || listSQLConceptos.isEmpty()) {
				guardarLog("No se pudo obtener los Conceptos de la Transaccion " + codigoTransaccion, codigoTransaccion);
				conConceptos = false;
			}
			
			log.info("Procesando Campos ...");
			for (OperacionesIntegradasSql iCampo : listSQLCampos) {
				if (iCampo.getSql().trim().contains("#")) 
					continue;
				
				// Obtenemos consulta 
				listValoresAux = new HashMap<String, String>();
				strQuery = reemplazaVariables(iCampo.getSql().trim(), listValoresAux);
				if (strQuery.contains("ERROR")) {
					guardarLog(strQuery, codigoTransaccion);
					return false;
				}
				
				// Ejecutamos consulta
				rs = this.conn.createStatement().executeQuery(strQuery);
				if (! rs.next()) {
					guardarLog("Error al procesar los campos de la Operacion Integrada Transaccion. No pude obtener el valor para campo '" + iCampo.getCampo() + "'", codigoTransaccion);
					return false;
				}

				// Agregamos a la lista de campos.
				columnName = iCampo.getCampo();
				if (columnName.toLowerCase().trim().contains("llave")) 
					columnName = columnName.toLowerCase().trim().replace("llave", "valorLlave");
				listValores.put(columnName, rs.getString(1));
				listValores.putAll(listValoresAux);
			}

			log.info("Procesando Conceptos ...");
			listaMain = new ArrayList<HashMap<String, String>>();
			for (OperacionesIntegradasConceptosSql iConcepto : listSQLConceptos) {
				log.info(" >>> Concepto: " + iConcepto.getIdConcepto().getId() + " - " + iConcepto.getIdConcepto().getDescripcion());
				conceptoActual = iConcepto.getIdConcepto().getDescripcion();
				strQuery = reemplazaVariables(iConcepto.getSql(), null);
				if (strQuery.contains("ERROR")) {
					guardarLog(strQuery, codigoTransaccion);
					return false;
				}
				
				// Ejecutamos consulta
				log.info("Ejecutamos consulta de OperacionesIntegradasConcepto ... ");
				rs = this.conn.createStatement().executeQuery(strQuery);
				noResult = true;
				orden = 0;
				while (rs.next()) {
					orden++;
					noResult = false;
					listValoresAux = new HashMap<String, String>();
					listValoresAux.putAll(listValores);
					listValoresAux.put("idConcepto", rs.getString(1));
					listValoresAux.put("concepto", conceptoActual.trim());
					listValoresAux.put("importe", rs.getString(2));
					listValoresAux.put("orden", orden.toString());
					if (rs.wasNull()) {
						guardarLog("La consulta para el concepto '" + iConcepto.getIdConcepto().getDescripcion() + "' no devolvio un importe valido. Transaccion " + codigoTransaccion, codigoTransaccion);
						return false;
					}
					
					if (rs.getMetaData().getColumnCount() >= 3) {
						mensajellaves = "";
						for (int col = 3; col <= rs.getMetaData().getColumnCount(); col++) {
							// nombre de columna
							columnName = toCamelCase(rs.getMetaData().getColumnName(col));
							if (columnName.toLowerCase().trim().contains("llave")) {
								columnName = columnName.toLowerCase().trim().replace("llave", "valorLlave");
								mensajellaves += ("".equals(mensajellaves) ? "" : ", ") + columnName;
							}
							
							// valor de columna
							columnValue = rs.getString(col);
							if (columnValue == null || "".equals(columnValue.trim()))
								continue;
							
							// añado a listado auxiliar de valores
							listValoresAux.put(columnName, columnValue);
						}
						
						log.info("Valores de Llaves encontradas: " + mensajellaves);
					}

					listaMain.add(listValoresAux);
				}
				
				if (noResult) {
					guardarLog("La consulta para el concepto '" + iConcepto.getIdConcepto().getDescripcion() + "' no devolvio ningun valor. Transaccion " + codigoTransaccion, codigoTransaccion);
					return false;
				}
			}
			
			// Si no tubo ningun concepto, es decir, puros campos
			if (! conConceptos) 
				listaMain.add(listValores);
			
			if (listaMain.isEmpty()) {
				guardarLog("No se encontraron elementos para generar la Transaccion.", codigoTransaccion);
				return false;
			}
			
			// Guardamos todo en TRANSACCIONES_DATA
			// -------------------------------------------------------------------------------------------
			formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			this.ifzTData = new TransaccionesDataFac();
			listTData = this.ifzTData.findPrevio(this.msgTransaccion.getIdTransaccion(),  this.msgTransaccion.getPoliza(), this.msgTransaccion.getLote());
			if (listTData != null && ! listTData.isEmpty())
				this.ifzTData.delete(listTData);
			
			listaMain = (ArrayList<HashMap<String, String>>) ordenar(listaMain, "orden");
			listTData = new ArrayList<TransaccionesData>();
			for (HashMap<String, String> hm : listaMain) {
				if (hm.containsKey("orden"))
					hm.remove("orden");
				jsonString = gson.toJson(hm);
				
				tdata = new TransaccionesData();
				tdata = gson.fromJson(jsonString, TransaccionesData.class);
				tdata.setIdMensajeTransaccion(this.msgTransaccion.getId());
				tdata.setCodigoTransaccion(this.json.get("idTransaccion").getAsLong());
				tdata.setIdEmpresa(this.json.get("idEmpresa").getAsLong());
				tdata.setCreadoPor(this.json.get("creadoPor").getAsLong());
				tdata.setFechaCreacion(formatter.parse(this.json.get("fechaCreacion").getAsString()));
				tdata.setModificadoPor(this.json.get("creadoPor").getAsLong());
				tdata.setFechaModificacion(formatter.parse(this.json.get("fechaCreacion").getAsString()));

				// Comprobamos codigo de transaccion
				if (hm.containsKey("idTransaccion") && ! tdata.getCodigoTransaccion().toString().equals(hm.get("idTransaccion")))
					tdata.setCodigoTransaccion(Long.parseLong(hm.get("idTransaccion").toString()));
				listTData.add(tdata);
			}
			
			// Guardamos en la BD las DATA generadas
			listTData = this.ifzTData.saveOrUpdateList(listTData);
			
			// Marcamos registro [MensajeTransaccion] como PENDIENTE
			this.msgTransaccion.setEstatus(1); // Pendiente
			this.msgTransaccion.setEstatusMensaje("Pendiente por Contabilizar");
			this.ifzMsgTransaccion.update(this.msgTransaccion);
			return true;
		} catch (Exception e) {
			try {
				this.msgTransaccion.setEstatusMensaje("No se pudo  procesar la Transaccion " + codigoTransaccion + ". " + this.json.toString());
				this.ifzMsgTransaccion.update(this.msgTransaccion);
			} catch (Exception e1) {
				log.error("Error en Logica_Contabilidad.TopicTransacciones.procesaTransaccion(String). No pude guardar el estatus en MensajeTransaccion", e1);
			}
			
			guardarLog("Error al procesar la Transaccion " + codigoTransaccion + ". " + this.json.toString(), e, codigoTransaccion);
    		log.error("Error en Logica_Contabilidad.TopicTransacciones.procesaTransaccion(String). Error al procesar la Transaccion [TopicTransacciones] --> " + this.json.toString(), e);
    		throw e;
		} finally {
			// Cerramos la coneccion si corresponde
			closeConnection();
		}
	}

	private void generaMensaje() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		SimpleDateFormat formatterEjercicio = new SimpleDateFormat("yyyy");
		MensajeContabilizar msgContabilizar = null;
		Gson gson = null;
		
		try {
			// Generamos mensaje para cola de contabilizacion
			msgContabilizar = new MensajeContabilizar();
			msgContabilizar.setIdTransaccion(this.msgTransaccion.getIdTransaccion());
			msgContabilizar.setPoliza(this.msgTransaccion.getPoliza());
			msgContabilizar.setLote(this.msgTransaccion.getLote());
			msgContabilizar.setIdMensajeTransaccion(this.msgTransaccion.getId());
			msgContabilizar.setFecha(formatter.format(this.msgTransaccion.getFechaRegistro()));
			msgContabilizar.setEjercicio(Long.valueOf(formatterEjercicio.format(this.msgTransaccion.getFechaRegistro())));
			msgContabilizar.setPeriodo(getPeriodo(this.msgTransaccion.getFechaRegistro()));
			if (this.json.has("idEmpresa"))
				msgContabilizar.setIdEmpresa(this.json.get("idEmpresa").getAsLong());

			// Enviamos mensaje a cola de contabilizacion
			gson = new GsonBuilder().serializeNulls().setDateFormat("MM/dd/yyyy HH:mm:ss").create();
			enviarContabilizacion(gson.toJson(msgContabilizar));
		} catch (Exception e) {
    		log.error("Error en Logica_Contabilidad.TopicTransacciones.run(). Error al generar mensaje para [TopicContabilizar]", e);
        	guardarLog("Error al generar mensaje para [TopicContabilizar] --> ", e);
		}
	}

	private void enviarContabilizacion(String jsonString) throws Exception {
		javax.jms.Connection jmsConn = null;
		QueueConnectionFactory qcf = null;
		MessageProducer sendtopic = null;
		Topic topicContabilizar = null;
		Session session = null;
		TextMessage tm = null;
		
		try {
			if ("".equals(jsonString.trim()))
				return;
			Thread.sleep(1000);
			log.info("\nMENSAJE CONTABILIZAR\n" + jsonString);
			qcf = (QueueConnectionFactory) this.ctx.lookup("ConnectionFactory");
			jmsConn = qcf.createQueueConnection();
			topicContabilizar = (Topic) this.ctx.lookup("topic/CONTABILIZAR");
			session = jmsConn.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			jmsConn.start();
			sendtopic = session.createProducer(topicContabilizar);
			tm = session.createTextMessage(jsonString);
			// enviamos mensaje
			sendtopic.send(tm);
		} catch (Exception e) {
			guardarLog("Error Logica_Contabilidad.TopicTransacciones.enviarContabilizacion", e);
			throw e;
		} finally {
			if (jmsConn != null) {
				jmsConn.stop();
				if (session != null) 
					session.close();
				jmsConn.close();
			}
		}
	}
	
	@SuppressWarnings("unused")
	private HashMap<String, String> procesarConcepto(String concepto, List<OperacionesIntegradasConceptosSql> listSQLConceptos, HashMap<String, String> listCampos) {
		String strQuery = "";
		String columnName = "";
		ResultSet rs = null;
		boolean noResult = false;
		// -------------------------------------------------------------
		ArrayList<HashMap<String, String>> listaMain = null;
		HashMap<String, String> listValues = null;
		String conceptoActual= "";
		String codigoTransaccion = "";
		String mensajellaves = "";
		
		try {
			log.info("Procesando Conceptos ...");
			listaMain = new ArrayList<HashMap<String, String>>();
			for (OperacionesIntegradasConceptosSql iConcepto : listSQLConceptos) {
				log.info("Procesamos OperacionesIntegradasConcepto: " + iConcepto.getIdConcepto().getId() + " - " + iConcepto.getIdConcepto().getDescripcion());
				conceptoActual = iConcepto.getIdConcepto().getDescripcion();
				strQuery = reemplazaVariables(iConcepto.getSql(), null);
				if (strQuery.contains("ERROR")) {
					guardarLog(strQuery, codigoTransaccion);
					return new HashMap<String, String>();
				}
				
				// Ejecutamos consulta
				log.info("Ejecutamos consulta de OperacionesIntegradasConcepto ... ");
				rs = this.conn.createStatement().executeQuery(strQuery);
				noResult = true;
				while (rs.next()) {
					noResult = false;
					listValues = new HashMap<String, String>();
					listValues.putAll(listCampos);
					listValues.put("idConcepto", rs.getString(1));
					listValues.put("concepto", conceptoActual.trim());
					listValues.put("importe", rs.getString(2));
					if (rs.wasNull()) {
						guardarLog("La consulta para el concepto '" + iConcepto.getIdConcepto().getDescripcion() + "' no devolvio un importe valido. Tramsaccion " + codigoTransaccion, codigoTransaccion);
						return new HashMap<String, String>();
					}
					
					if (rs.getMetaData().getColumnCount() >= 3) {
						mensajellaves = "";
						for (int col = 3; col <= rs.getMetaData().getColumnCount(); col++) {
							columnName = rs.getMetaData().getColumnName(col);
							if (columnName.toLowerCase().trim().contains("llave")) {
								columnName = columnName.toLowerCase().trim().replace("llave", "valorLlave");
								listValues.put(columnName, rs.getString(col));
								mensajellaves += ("".equals(mensajellaves) ? "" : ", ") + columnName;
								continue;
							}
	
							if (rs.getString(col) != null && ! "".equals(rs.getString(col).trim()))
								listValues.put(columnName, rs.getString(col));
						}
						
						log.info("Valores de Llaves encontradas: " + mensajellaves);
					}
					
					listaMain.add(listValues);
				}
				
				if (noResult) {
					guardarLog("La consulta para el concepto '" + iConcepto.getIdConcepto().getDescripcion() + "' no devolvio ningun valor. Transaccion " + codigoTransaccion, codigoTransaccion);
					return new HashMap<String, String>();
				}
			}
		} catch (Exception e) {
			
		}
		
		return listValues;
	}

	private List<HashMap<String, String>> ordenar(List<HashMap<String, String>> list, String key) { 
		List<HashMap<String, String>> temp = null;
        HashMap<Integer, String> indices = null;
        boolean containsKey = false;
        
        if ((list == null || list.isEmpty()) || (key == null || "".equals(key.trim())))
        	return list;

        indices = new HashMap<Integer, String>();
        for (HashMap<String, String> item : list) {
			indices.put(list.indexOf(item), "Z");
			if (item.containsKey(key)) {
				indices.put(list.indexOf(item), item.get(key));
				containsKey = true;
			}
        }
        
        if (! containsKey)
        	return list;

        temp = new ArrayList<HashMap<String, String>>();
        indices = ordenar(indices);
        for (Entry<Integer, String> val : indices.entrySet())
        	temp.add(list.get(val.getKey()));
        return temp; 
    } 

	private HashMap<Integer, String> ordenar(HashMap<Integer, String> hm) { 
        List<Entry<Integer, String> > list = null; 
        HashMap<Integer, String> temp = null;

        // Create a list from elements of HashMap and Sort the list 
        list = new LinkedList<Entry<Integer, String>>(hm.entrySet());
        Collections.sort(list, new Comparator<Entry<Integer, String> >() { 
            public int compare(Entry<Integer, String> o1, Entry<Integer, String> o2) { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
        
        // put data from sorted list to hashmap  
        temp = new LinkedHashMap<Integer, String>(); 
        for (Entry<Integer, String> aa : list) 
            temp.put(aa.getKey(), aa.getValue()); 
        return temp; 
    } 
	
	private String reemplazaVariables(String strQuery, HashMap<String, String> listValues) {
		String variableSQL = "";
		
		// Ciclo para reemplazar posibles variables de la consulta 
		while (strQuery.contains("$")) {
			variableSQL = getVariableSQL(strQuery, "$");
			
			// Comprobamos la variable en
			if (! this.json.has(variableSQL))
				return "Error al construir la consulta para el campo '" + variableSQL + "'. Este campo no fue encontrado en la peticion.";
			
			strQuery = strQuery.replace("$" + variableSQL, this.json.get(variableSQL).toString());
			if (listValues != null && strQuery.toLowerCase().contains("from"))
				listValues.put(variableSQL, this.json.get(variableSQL).toString());
		}
		
		return strQuery.replace("\"", "'");
	}
	
	private String toCamelCase(String value) {
		String[] splitted = null;
		String resultado = "";
		String caracter = "";
		
		if (value == null || "".equals(value.trim()))
			return resultado;
		
		value = value.replace(" ", "_");
		splitted = value.split("_");
		for (String palabra : splitted) {
			if ("".equals(resultado.trim())) {
				resultado = palabra.toLowerCase();
				continue;
			}
			
			caracter = palabra.substring(0,1);
			resultado += caracter.toUpperCase();
			resultado += palabra.substring(1).toLowerCase();
		}
		
		return resultado;
	}
	
 	private String getVariableSQL(String strQuery, String prefix) {
		String caracteresTope = "()[]{}/*-+%$#,=!.<>\n\t\r";
		String variableSQL = "";
		
		strQuery = strQuery.substring(strQuery.indexOf(prefix)).replace(prefix, "");
		for (int x = 0; x < strQuery.length(); x++) {
			if ("".equals(String.valueOf(strQuery.charAt(x)).trim()) || caracteresTope.contains(String.valueOf(strQuery.charAt(x)).trim()))
				break;
			variableSQL += strQuery.charAt(x);
		}
		variableSQL = variableSQL.replace(prefix, "");
		
		return variableSQL;
	}
	
    private String getPeriodo(Date fecha) {
		SimpleDateFormat formatterPeriodo = new SimpleDateFormat("MM");
		String periodo = "";
		
		switch (formatterPeriodo.format(fecha)) {
			case "01": periodo = "ENERO"; break;
			case "02": periodo = "FEBRERO"; break;
			case "03": periodo = "MARZO"; break;
			case "04": periodo = "ABRIL"; break;
			case "05": periodo = "MAYO"; break;
			case "06": periodo = "JUNIO"; break;
			case "07": periodo = "JULIO"; break;
			case "08": periodo = "AGOSTO"; break;
			case "09": periodo = "SEPTIEMBRE"; break;
			case "10": periodo = "OCTUBRE"; break;
			case "11": periodo = "NOVIEMBRE"; break;
			case "12": periodo = "DICIEMBRE"; break;
			default : periodo = ""; break;
		}
		
    	return periodo;
    }

    private void guardarLog(String mensaje, String codigoTransaccion) {
    	guardarLog(mensaje, null, codigoTransaccion);
    }
    
    private void guardarLog(String mensaje, Throwable t) {
    	guardarLog(mensaje, t, null);
    }
    
    private void guardarLog(String mensaje, Throwable t, String codigoTransaccion) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	BufferedWriter out = null;
    	FileWriter fwco = null;
    	String logFileName = "";
    	String encabezado = "";
    	File file = null;
    	
    	try {
    		// Guardamos estatus en MensajeTransaccion
    		if (this.msgTransaccion != null && this.ifzMsgTransaccion != null) {
	    		this.msgTransaccion.setEstatusMensaje(mensaje + " :: Etapa 2 del proceso.");
	    		this.ifzMsgTransaccion.update(this.msgTransaccion);
    		}
    		
        	log.error(mensaje, t);
        	if (codigoTransaccion != null && ! "".equals(codigoTransaccion.trim()))
        		logFileName = "/tmp/T-" + codigoTransaccion.trim() + "-" + formatter.format(Calendar.getInstance().getTime()) + ".log";
        	else
        		logFileName = "/tmp/T-" + formatter.format(Calendar.getInstance().getTime()) + ".log";
    		file = new File(logFileName);
    		
    		if (! file.exists()) {
    			formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
    			file.createNewFile();
    			encabezado = this.getClass().getCanonicalName() 
    					+ "       " + formatter.format(Calendar.getInstance().getTime())
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
    		log.error("Error en Logica_Contabilidad.TopicTransacciones.guardarLog()", e);
    	}
    }

    private void getConnection() throws Exception {
		try {
			if (this.conn == null) {
				this.ctx = new InitialContext();
				this.datasource = ((DataSource) this.ctx.lookup("java:/giroDS"));
				this.conn = this.datasource.getConnection();
			}
		} catch (Exception e) {
			guardarLog("Error al obtener la coneccion a giroDS", e);
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

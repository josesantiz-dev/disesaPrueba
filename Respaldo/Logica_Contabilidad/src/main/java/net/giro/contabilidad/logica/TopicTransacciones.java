package net.giro.contabilidad.logica;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.giro.contabilidad.beans.Conceptos;
import net.giro.contabilidad.beans.MensajeContabilizar;
import net.giro.contabilidad.beans.MensajeTransaccionExt;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;
import net.giro.contabilidad.beans.OperacionesIntegradasSql;
import net.giro.contabilidad.beans.TransaccionesData;

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
	private OperacionesIntegradasSqlFac ifzOISQL;
	private OperacionesIntegradasConceptosSqlFac ifzOIConceptosSQL;
	private TransaccionesDataFac ifzTData; 
	private MensajeTransaccionFac ifzMsgTransaccion;
	private MensajeTransaccionExt msgTransaccion;
	private JsonObject json;
    
	
    public TopicTransacciones() throws Exception {
        try {
        	this.ctx = new InitialContext();
        	this.ifzMsgTransaccion = new MensajeTransaccionFac();
        	this.ifzOISQL = new OperacionesIntegradasSqlFac();
        	this.ifzOIConceptosSQL = new OperacionesIntegradasConceptosSqlFac();
        } catch (Exception e) {
        	log.error("Error al crear TopicTransacciones", e);
        	throw e;
        }
    }
	
    
	public void onMessage(Message message) {
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		TextMessage mensaje = null;
		String jsonString = "";
		
        try {
        	Thread.sleep(3000);
        	if (message instanceof TextMessage) {
				mensaje = (TextMessage) message;
				jsonString = mensaje.getText().trim(); 
				
				// Convertimos el mensaje en objeto JSON.
				this.json = parser.parse(jsonString).getAsJsonObject();
				this.msgTransaccion = new MensajeTransaccionExt();
				this.msgTransaccion = gson.fromJson(jsonString, MensajeTransaccionExt.class);
				
				// Procesamos transaccion
				if (procesaTransaccion(jsonString)) {
					// Lanzamos mensaje a cola de contabilizacion en desde un hilo secundario
					generaMensaje();
				}
			}
        } catch (Exception e) {
    		log.error("Error en Logica_Contabilidad.TopicTransacciones.onMessage(Message). Error al recibir mensaje [TopicTransacciones] --> " + jsonString, e);
        	guardarLog("Error al recibir mensaje [TopicTransacciones] --> " + jsonString, e);
		} 
    }

	// ----------------------------------------------------------------------
	// METODOS PRIVADOS
	// ----------------------------------------------------------------------
    
	private boolean procesaTransaccion(String mensaje) {
		ArrayList<HashMap<String, String>> listaMain = new ArrayList<HashMap<String, String>>();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		HashMap<String, String> params = new HashMap<String, String>();
		HashMap<String, String> listCampos = null;
		HashMap<String, String> listValues = null;
		List<OperacionesIntegradasSql> listOperIntegradas = null;
		List<OperacionesIntegradasSql> listOISQLCampos = null;
		List<OperacionesIntegradasSql> listOISQLConceptos = null;
		List<TransaccionesData> listTData = null;
		TransaccionesData tdata = null;
		String jsonString = "";
		Gson gson = new Gson();
		String codigoTransaccion = "";
		ResultSet rs = null;
		String strQuery = "";
		String mensajellaves = "";
		boolean conConceptos = false;
		boolean noResult = true;
		
		try {
        	rs = null;
        	strQuery = "";
			if (this.ifzMsgTransaccion == null)
				this.ifzMsgTransaccion = new MensajeTransaccionFac();

			// Abrimos coneccion
        	getConnection();
			
			// Recuperamos Campos  **** NOTA..con esta consulta estás obteniendo los campos no los conceptos 
        	codigoTransaccion = String.valueOf(this.msgTransaccion.getIdTransaccion());
			listOperIntegradas = this.ifzOISQL.findByProperty("idOperacionIntegradaTransaccion.idTransaccion.codigo", this.msgTransaccion.getIdTransaccion(), 0);
			if (listOperIntegradas == null || listOperIntegradas.isEmpty()) {
				guardarLog("No se pudo obtener los Campos y Conceptos de la Transaccion " + this.msgTransaccion.getIdTransaccion(), codigoTransaccion);
				return false;
			}
        	
        	// Generamos poliza y lote
        	// -------------------------------------------------------------------------------
        	listCampos = new HashMap<String, String>();
			listCampos.put("idTransaccion", this.json.get("idTransaccion").getAsString());
			listCampos.put("poliza", this.msgTransaccion.getPoliza().toString());
			listCampos.put("lote", this.msgTransaccion.getLote().toString());
        	
			// Separo los campos y conceptos
			listOISQLCampos = new ArrayList<OperacionesIntegradasSql>();
			listOISQLConceptos = new ArrayList<OperacionesIntegradasSql>();
			for (OperacionesIntegradasSql var : listOperIntegradas) {
				if (var.getSql().trim().contains("#")) 
					listOISQLConceptos.add(var);
				else
					listOISQLCampos.add(var);
			}

        	// Proceso CAMPOS
        	// -------------------------------------------------------------------------------
        	rs = null;
			listValues = new HashMap<String, String>();
			for (OperacionesIntegradasSql iCampo : listOISQLCampos) {
				// Obtenemos consulta
				strQuery = reemplazaVariables(iCampo.getSql().trim(), listValues);
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
				if (iCampo.getCampo().toLowerCase().contains("llave"))
					listCampos.put("valor" + iCampo.getCampo(), rs.getString(1));
				else	
					listCampos.put(iCampo.getCampo(), rs.getString(1));
				listCampos.putAll(listValues);
				listValues.clear();
			}

        	// Proceso CONCEPTOS
        	// -------------------------------------------------------------------------------
        	rs = null;
        	listaMain.clear();
        	Long idConcepto = 0L;
        	String conceptoActual = "";
			HashMap<String, String> aux = new HashMap<String, String>();
			for (OperacionesIntegradasSql iConcepto : listOISQLConceptos) {
				// Obtenemos consulta
				strQuery = iConcepto.getSql().trim();
				conceptoActual = strQuery;
				
				// Recuperamos id del concepto indicado
				log.info("Recuperando concepto: " + conceptoActual);
				conConceptos = true;
				idConcepto = getConceptoId(strQuery);
				if (idConcepto <= 0L) {
					guardarLog("El concepto '" + conceptoActual + "' no se encontro en el catalogo de Conceptos", codigoTransaccion);
					return false;
				}
				
				params.clear();
				params.put("idOperacionIntegradaTransaccion.id", iConcepto.getIdOperacionIntegradaTransaccion().getId().toString());
				params.put("idConcepto", idConcepto.toString());
				
				// Recuperamos los conceptos de la transaccion
				log.info("Recuperando OperacionesIntegradasConceptos SQL con el Concepto: " + idConcepto + " - " + conceptoActual);
				List<OperacionesIntegradasConceptosSql> listaConceptosSQL = this.ifzOIConceptosSQL.findByProperties(params, 0);
				if (listaConceptosSQL == null || listaConceptosSQL.isEmpty()) {
					guardarLog("El Concepto '" + strQuery + "' no existe en el listado de Conceptos de la Transaccion " + codigoTransaccion, codigoTransaccion);
					return false;
				}

				int column = 0;
				log.info(listaConceptosSQL.size() + " OperacionesIntegradasConceptos encontradas");
				for (OperacionesIntegradasConceptosSql OIConceptoSql : listaConceptosSQL) {
					log.info("Procesamos OperacionesIntegradasConcepto: " + OIConceptoSql.getIdConcepto().getId() + " - " + OIConceptoSql.getIdConcepto().getDescripcion());
					strQuery = reemplazaVariables(OIConceptoSql.getSql(), null);
					if (strQuery.contains("ERROR")) {
						guardarLog(strQuery, codigoTransaccion);
						return false;
					}
					
					// Ejecutamos consulta
					log.info("Ejecutamos consulta de OperacionesIntegradasConcepto ... ");
					rs = this.conn.createStatement().executeQuery(strQuery);
					noResult = true;
					while (rs.next()) {
						noResult = false;
						aux = new HashMap<>();
						aux.putAll(listCampos);
						aux.put("idConcepto", rs.getString(1));
						aux.put("importe", rs.getString(2));
						if (rs.wasNull()) {
							guardarLog("La consulta para el concepto '" + OIConceptoSql.getIdConcepto().getDescripcion() + "' no devolvio un importe valido. Tramsaccion " + codigoTransaccion, codigoTransaccion);
							return false;
						}
						
						if (rs.getMetaData().getColumnCount() >= 3) {
							/*try {
								column = 0;
								column = rs.findColumn("transaccion"); 
								aux.put("idTransaccion", rs.getString(column));
							} catch (Exception e) {}*/
							
							column = 0;
							mensajellaves = "";
							for (int index = 1; index <= 30; index++) {
								try { 
									column = rs.findColumn("Llave" + index); 
								} catch (Exception e) { 
									continue; 
								}
								
								aux.put("valorLlave" + index, rs.getString(column));
								if (rs.wasNull()) {
									guardarLog("La consulta para el concepto '" + OIConceptoSql.getIdConcepto().getDescripcion() + "' no devolvio un valor valido para la llave " + index + ". Transaccion " + codigoTransaccion, codigoTransaccion);
									return false;
								}
								
								mensajellaves += ("".equals(mensajellaves) ? "" : ", ") + "valorLlave" + index;
							}
							
							log.info("Valores de Llaves encontradas: " + mensajellaves);
						}
						
						listaMain.add(aux);
					}
					
					if (noResult) {
						guardarLog("La consulta para el concepto '" + OIConceptoSql.getIdConcepto().getDescripcion() + "' no devolvio ningun valor. Tramsaccion " + codigoTransaccion, codigoTransaccion);
						return false;
					}
				} // FIN LISTA OperacionesIntegradasConceptosSql
			} // fin lista OperacionesIntegradasSql
			
			// Si no tubo ningun concepto, es decir, puros campos
			if (! conConceptos) 
				listaMain.add(listCampos);
			
			if (listaMain.isEmpty()) {
				guardarLog("No se encontraron elementos para generar la Transaccion.", codigoTransaccion);
				return false;
			}
			
			// Guardamos todo en TRANSACCIONES_DATA
			this.ifzTData = new TransaccionesDataFac();
			
			// Comprobamos TRANSACCION, LOTE y POLIZA para ver si ya existen
			params.clear();
			params.put("codigoTransaccion", this.msgTransaccion.getIdTransaccion().toString());
			params.put("poliza", this.msgTransaccion.getPoliza().toString());
			params.put("lote", this.msgTransaccion.getLote().toString());
			
			// Buscamos datos existentes, Si no existen los insertamos
			listTData = this.ifzTData.findByProperties(params, 0);
			if (listTData == null || listTData.isEmpty()) {
				for (HashMap<String, String> hm : listaMain) {
					jsonString = gson.toJson(hm);
					
					tdata = new TransaccionesData();
					tdata = gson.fromJson(jsonString, TransaccionesData.class);
					tdata.setCodigoTransaccion(json.get("idTransaccion").getAsLong());
					tdata.setCreadoPor(json.get("creadoPor").getAsInt());
					tdata.setFechaCreacion(formatter.parse(json.get("fechaCreacion").getAsString()));
					tdata.setModificadoPor(json.get("creadoPor").getAsInt());
					tdata.setFechaModificacion(formatter.parse(json.get("fechaCreacion").getAsString()));

					// Comprobamos codigo de transaccion
					if (hm.containsKey("idTransaccion") && ! tdata.getCodigoTransaccion().toString().equals(hm.get("idTransaccion")))
						tdata.setCodigoTransaccion(Long.parseLong(hm.get("idTransaccion").toString()));
					
					// Guardamos en la BD y recuperar ID generado
					tdata.setId(this.ifzTData.save(tdata));
				}
			} else {
				// Actualizamos las propiedades del mensaje guardado con el actual y lo actualizo en la BD si corresponde
				for (HashMap<String, String> hm : listaMain) {
					jsonString = gson.toJson(hm);
					
					tdata = new TransaccionesData();
					tdata = gson.fromJson(jsonString, TransaccionesData.class);
					tdata.setCodigoTransaccion(json.get("idTransaccion").getAsLong());
					tdata.setCreadoPor(json.get("creadoPor").getAsInt());
					tdata.setFechaCreacion(formatter.parse(json.get("fechaCreacion").getAsString()));
					tdata.setModificadoPor(json.get("creadoPor").getAsInt());
					tdata.setFechaModificacion(formatter.parse(json.get("fechaCreacion").getAsString()));
					
					// Comprobamos codigo de transaccion
					if (hm.containsKey("idTransaccion") && ! tdata.getCodigoTransaccion().toString().equals(hm.get("idTransaccion")))
						tdata.setCodigoTransaccion(Long.parseLong(hm.get("idTransaccion")));
					
					// Guardamos en la BD y recuperar ID generado
					for (TransaccionesData var : listTData) {
						if (tdata.getIdConcepto().longValue() != var.getIdConcepto().longValue())
							continue;
						
						if (actualizaPropiedades(tdata, var))
							this.ifzTData.update(var);
						break;
					}
				}
			}
			
			// Marcamos registro [MensajeTransaccion] como PENDIENTE
			this.msgTransaccion.setEstatus(1); // Pendiente
			this.msgTransaccion.setEstatusMensaje("Pendiente por Contabilizar");
			this.ifzMsgTransaccion.update(this.msgTransaccion);
			
			return true;
		} catch (Exception e) {
			try {
				this.msgTransaccion.setEstatusMensaje("No se pudo  procesar la Transaccion " + codigoTransaccion + ". " + mensaje);
				this.ifzMsgTransaccion.update(this.msgTransaccion);
			} catch (Exception e1) {
				log.error("Error en Logica_Contabilidad.TopicTransacciones.procesaTransaccion(String). No pude guardar el estatus en MensajeTransaccion", e1);
			}
			
			guardarLog("Error al procesar la Transaccion " + codigoTransaccion + ". " + mensaje, e, codigoTransaccion);
    		log.error("Error en Logica_Contabilidad.TopicTransacciones.procesaTransaccion(String). Error al procesar la Transaccion [TopicTransacciones] --> " + mensaje, e);
    		return false;
		} finally {
			// Cerramos la coneccion si corresponde
			closeConnection();
		}
	}

	private void generaMensaje() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		SimpleDateFormat formatterEjercicio = new SimpleDateFormat("yyyy");
		MensajeContabilizar msgContabilizar = null;
		Gson gson = new Gson();
		
		try {
			// Generamos mensaje para cola de contabilizacion
			msgContabilizar = new MensajeContabilizar();
			msgContabilizar.setIdTransaccion(this.msgTransaccion.getIdTransaccion());
			msgContabilizar.setPoliza(this.msgTransaccion.getPoliza());
			msgContabilizar.setLote(this.msgTransaccion.getLote());
			msgContabilizar.setIdMensajeTransaccion(this.msgTransaccion.getId());
			msgContabilizar.setFecha(this.msgTransaccion.getFechaRegistro());
			msgContabilizar.setEjercicio(Long.valueOf(formatterEjercicio.format(formatter.parse(this.msgTransaccion.getFechaRegistro()))));
			msgContabilizar.setPeriodo(getPeriodo(formatter.parse(this.msgTransaccion.getFechaRegistro())));
			if (this.json.has("idEmpresa"))
				msgContabilizar.setIdEmpresa(this.json.get("idEmpresa").getAsLong());

			// Enviamos mensaje a cola de contabilizacion
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
	
	private long getConceptoId(String concepto) throws Exception {
		ConceptosFac ifzConceptos = new ConceptosFac();
		List<Conceptos> listConceptos = null;
		Long idConcepto = 0L;
		
		if (concepto == null || "".equals(concepto.trim()))
			return idConcepto;
		
		concepto = concepto.trim();
		if (! concepto.contains("#"))
			return idConcepto;
		concepto = concepto.replace("#", "");
		
		log.info("---> Buscando ID Concepto [" + concepto + "] ... ");
		listConceptos = ifzConceptos.findByProperty("descripcion", concepto, 0);
		if (listConceptos == null || listConceptos.isEmpty())
			return idConcepto;
		idConcepto = listConceptos.get(0).getId();
		log.info("---> Concepto encontrado:  " + idConcepto + " - " + concepto);
		return idConcepto;
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
    
    private boolean actualizaPropiedades(TransaccionesData tdataActual, TransaccionesData tdataStored) {
		boolean cambio = false;
		
		try {
			if (comparador(tdataStored.getCodigoTransaccion(), tdataActual.getCodigoTransaccion())) {
				tdataStored.setCodigoTransaccion(tdataActual.getCodigoTransaccion());
				cambio = true;
			}
			if (comparador(tdataStored.getIdConcepto(), tdataActual.getIdConcepto())) {
				tdataStored.setIdConcepto(tdataActual.getIdConcepto());
				cambio = true;
			}
			if (comparador(tdataStored.getImporte(), tdataActual.getImporte())) {
				tdataStored.setImporte(tdataActual.getImporte());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave1(), tdataActual.getValorLlave1())) {
				tdataStored.setValorLlave1(tdataActual.getValorLlave1());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave2(), tdataActual.getValorLlave2())) {
				tdataStored.setValorLlave2(tdataActual.getValorLlave2());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave3(), tdataActual.getValorLlave3())) {
				tdataStored.setValorLlave3(tdataActual.getValorLlave3());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave4(), tdataActual.getValorLlave4())) {
				tdataStored.setValorLlave4(tdataActual.getValorLlave4());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave5(), tdataActual.getValorLlave5())) {
				tdataStored.setValorLlave5(tdataActual.getValorLlave5());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave6(), tdataActual.getValorLlave6())) {
				tdataStored.setValorLlave6(tdataActual.getValorLlave6());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave7(), tdataActual.getValorLlave7())) {
				tdataStored.setValorLlave7(tdataActual.getValorLlave7());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave8(), tdataActual.getValorLlave8())) {
				tdataStored.setValorLlave8(tdataActual.getValorLlave8());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave9(), tdataActual.getValorLlave9())) {
				tdataStored.setValorLlave9(tdataActual.getValorLlave9());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave10(), tdataActual.getValorLlave10())) {
				tdataStored.setValorLlave10(tdataActual.getValorLlave10());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave11(), tdataActual.getValorLlave11())) {
				tdataStored.setValorLlave11(tdataActual.getValorLlave11());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave12(), tdataActual.getValorLlave12())) {
				tdataStored.setValorLlave12(tdataActual.getValorLlave12());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave13(), tdataActual.getValorLlave13())) {
				tdataStored.setValorLlave13(tdataActual.getValorLlave13());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave14(), tdataActual.getValorLlave14())) {
				tdataStored.setValorLlave14(tdataActual.getValorLlave14());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave15(), tdataActual.getValorLlave15())) {
				tdataStored.setValorLlave15(tdataActual.getValorLlave15());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave16(), tdataActual.getValorLlave16())) {
				tdataStored.setValorLlave16(tdataActual.getValorLlave16());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave17(), tdataActual.getValorLlave17())) {
				tdataStored.setValorLlave17(tdataActual.getValorLlave17());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave18(), tdataActual.getValorLlave18())) {
				tdataStored.setValorLlave18(tdataActual.getValorLlave18());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave19(), tdataActual.getValorLlave19())) {
				tdataStored.setValorLlave19(tdataActual.getValorLlave19());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave20(), tdataActual.getValorLlave20())) {
				tdataStored.setValorLlave20(tdataActual.getValorLlave20());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave21(), tdataActual.getValorLlave21())) {
				tdataStored.setValorLlave21(tdataActual.getValorLlave21());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave22(), tdataActual.getValorLlave22())) {
				tdataStored.setValorLlave22(tdataActual.getValorLlave22());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave23(), tdataActual.getValorLlave23())) {
				tdataStored.setValorLlave23(tdataActual.getValorLlave23());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave24(), tdataActual.getValorLlave24())) {
				tdataStored.setValorLlave24(tdataActual.getValorLlave24());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave25(), tdataActual.getValorLlave25())) {
				tdataStored.setValorLlave25(tdataActual.getValorLlave25());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave26(), tdataActual.getValorLlave26())) {
				tdataStored.setValorLlave26(tdataActual.getValorLlave26());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave27(), tdataActual.getValorLlave27())) {
				tdataStored.setValorLlave27(tdataActual.getValorLlave27());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave28(), tdataActual.getValorLlave28())) {
				tdataStored.setValorLlave28(tdataActual.getValorLlave28());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave29(), tdataActual.getValorLlave29())) {
				tdataStored.setValorLlave29(tdataActual.getValorLlave29());
				cambio = true;
			}
			if (comparador(tdataStored.getValorLlave30(), tdataActual.getValorLlave30())) {
				tdataStored.setValorLlave30(tdataActual.getValorLlave30());
				cambio = true;
			}
			if (comparador(tdataStored.getReferencia1(), tdataActual.getReferencia1())) {
				tdataStored.setReferencia1(tdataActual.getReferencia1());
				cambio = true;
			}
			if (comparador(tdataStored.getReferencia2(), tdataActual.getReferencia2())) {
				tdataStored.setReferencia2(tdataActual.getReferencia2());
				cambio = true;
			}
			if (comparador(tdataStored.getIdCanal(), tdataActual.getIdCanal())) {
				tdataStored.setIdCanal(tdataActual.getIdCanal());
				cambio = true;
			}
			if (comparador(tdataStored.getCanal(), tdataActual.getCanal())) {
				tdataStored.setCanal(tdataActual.getCanal());
				cambio = true;
			}
			if (comparador(tdataStored.getIdSucursal(), tdataActual.getIdSucursal())) {
				tdataStored.setIdSucursal(tdataActual.getIdSucursal());
				cambio = true;
			}
			if (comparador(tdataStored.getSucursal(), tdataActual.getSucursal())) {
				tdataStored.setSucursal(tdataActual.getSucursal());
				cambio = true;
			}
			if (comparador(tdataStored.getIdUsuarioCreacion(), tdataActual.getIdUsuarioCreacion())) {
				tdataStored.setIdUsuarioCreacion(tdataActual.getIdUsuarioCreacion());
				cambio = true;
			}
			if (comparador(tdataStored.getUsuarioCreacion(), tdataActual.getUsuarioCreacion())) {
				tdataStored.setUsuarioCreacion(tdataActual.getUsuarioCreacion());
				cambio = true;
			}
			if (comparador(tdataStored.getIdMoneda(), tdataActual.getIdMoneda())) {
				tdataStored.setIdMoneda(tdataActual.getIdMoneda());
				cambio = true;
			}
			if (comparador(tdataStored.getMoneda(), tdataActual.getMoneda())) {
				tdataStored.setMoneda(tdataActual.getMoneda());
				cambio = true;
			}
			if (comparador(tdataStored.getIdFormaPago(), tdataActual.getIdFormaPago())) {
				tdataStored.setIdFormaPago(tdataActual.getIdFormaPago());
				cambio = true;
			}
			if (comparador(tdataStored.getIdOperacion(), tdataActual.getIdOperacion())) {
				tdataStored.setIdOperacion(tdataActual.getIdOperacion());
				cambio = true;
			}
			if (comparador(tdataStored.getIdFuncion(), tdataActual.getIdFuncion())) {
				tdataStored.setIdFuncion(tdataActual.getIdFuncion());
				cambio = true;
			}
			if (comparador(tdataStored.getPoliza(), tdataActual.getPoliza())) {
				tdataStored.setPoliza(tdataActual.getPoliza());
				cambio = true;
			}
			if (comparador(tdataStored.getLote(), tdataActual.getLote())) {
				tdataStored.setLote(tdataActual.getLote());
				cambio = true;
			}
			if (comparador(tdataStored.getCodigoError(), tdataActual.getCodigoError())) {
				tdataStored.setCodigoError(tdataActual.getCodigoError());
				cambio = true;
			}
			if (comparador(tdataStored.getDescripcionError(), tdataActual.getDescripcionError())) {
				tdataStored.setDescripcionError(tdataActual.getDescripcionError());
				cambio = true;
			}
		} catch (Exception e) {
			log.error("Error en Logica_Contabilidad.MensajeTransaccionFac.actualizaPropiedades(). No se pudo actualizar las propiedades el mensaje guardado", e);
			return false;
		}
		
		return cambio;
	}
    
    private boolean comparador(Object obj1, Object obj2) {
    	if (obj1 == null || obj2 == null)
    		return false;
    	
    	return (obj1.equals(obj2));
    }
}

package net.giro.adp.topic;

import java.lang.reflect.Type;
import java.math.BigDecimal;
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

import net.giro.adp.beans.Insumos;
import net.giro.adp.beans.InsumosDetalles;
import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.ObraCobranza;
import net.giro.adp.beans.ObraEmpleado;
import net.giro.adp.beans.Presupuesto;
import net.giro.adp.beans.PresupuestoDetalle;
import net.giro.adp.logica.InsumosDetallesRem;
import net.giro.adp.logica.InsumosRem;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.adp.logica.ObraCobranzaRem;
import net.giro.adp.logica.ObraEmpleadoRem;
import net.giro.adp.logica.ObraRem;
import net.giro.adp.logica.PresupuestoDetalleRem;
import net.giro.adp.logica.PresupuestoRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.logica.TopicEstatusRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TiposTopicEstatus;
import net.giro.plataforma.topics.TopicEventosGP;
import net.giro.plataforma.topics.TopicEventosInventarios;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/GP")}, 
	mappedName = "topic/GP")
public class TopicGestionProyectos implements MessageListener {
	private static Logger log = Logger.getLogger(TopicGestionProyectos.class);
	private InfoSesion infoSesion;
	private long usuarioId;
	private long empresaId;
	private long aplicacionId;
	// -----------------------------------------------------------------------
	private TopicEstatusRem ifzTopicEstatus;
	private long idTopicEstatus;
	private List<String> mensajeLogs;
	// -----------------------------------------------------------------------
	private ObraAlmacenesRem ifzOAlmacenes;
	private ObraEmpleadoRem ifzOEmpleados;
	private ObraCobranzaRem ifzCobranza;
	private ObraRem ifzObras;
	private InsumosRem ifzInsumos;
	private InsumosDetallesRem ifzInsumosDetalles;
	private PresupuestoRem ifzPPT01;
	private PresupuestoDetalleRem ifzPPT01Detalles;
	
	public TopicGestionProyectos() {
		InitialContext ctx = null;
		
		try {
			ctx = new InitialContext();
			this.ifzTopicEstatus = (TopicEstatusRem) ctx.lookup("ejb:/Logica_Publico//TopicEstatusFac!net.giro.plataforma.logica.TopicEstatusRem");
			this.ifzOAlmacenes = (ObraAlmacenesRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzOEmpleados = (ObraEmpleadoRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraEmpleadoFac!net.giro.adp.logica.ObraEmpleadoRem");
			this.ifzCobranza = (ObraCobranzaRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraCobranzaFac!net.giro.adp.logica.ObraCobranzaRem");
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzInsumos = (InsumosRem) ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzInsumosDetalles = (InsumosDetallesRem) ctx.lookup("ejb:/Logica_GestionProyectos//InsumosDetallesFac!net.giro.adp.logica.InsumosDetallesRem");
			this.ifzPPT01 = (PresupuestoRem) ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoFac!net.giro.adp.logica.PresupuestoRem");
			this.ifzPPT01Detalles = (PresupuestoDetalleRem) ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoDetalleFac!net.giro.adp.logica.PresupuestoDetalleRem");
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar TopicGestionProyectos <-> topic/GP", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		Gson gson = null;
		Type tipo = null;
		TextMessage mensaje = null;
		MensajeTopic mensajeTopic = null;
		TopicEventosGP evento;
		String eventoParam = "";
		
    	try {
	    	if (message instanceof TextMessage) {
				mensajeLog("############################## TOPIC GESTION PROYECTOS --- INICIO : " + new Date());
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
				evento = TopicEventosGP.fromString(eventoParam);
				switch (evento) {
					case OBRA_ALTA: // ALTA DE CONTRATO
						altaObra(valueToLong(mensajeTopic.getTarget()));
						break;
						
					case OBRA_BAJA: // BAJA DE EMPLEADO
						bajaObra(valueToLong(mensajeTopic.getTarget()));
						break;
						
					case OBRA_QUITAR_EMPLEADO: // FINIQUITO
						bajaEmpleadoObras(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()));
						break;
						
					case OBRA_ASIGNAR_ALMACENES:
						obraAsignacionAlmacenes(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), valueToString(mensajeTopic.getAtributos()));
						break;
						
					case COBRANZA_ACTUALIZAR:
						cobranzaActualizar(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getAtributos()));
						break;						
						
					case COBRANZA_UBICACION_PREVIA: // COBRANZA_UBICACION_PREVIA
						cobranzaUbicacionPrevia(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()));
						break;
						
					case INSUMOS_ESTATUS: 
						explosionInsumosEstatus(valueToLong(mensajeTopic.getTarget()));
						break;
						
					case INSUMOS_SUMINISTRO: // EXPLOSION DE INSUMOS: Suministro
						tipo = new TypeToken<HashMap<Long, Double>>() {}.getType();
						//atributos = new Gson().fromJson(hm.get("atributos").toString().trim(), tipo);
						explosionInsumosSuministro(valueToLong(mensajeTopic.getTarget()), (HashMap<Long, Double>) gson.fromJson(mensajeTopic.getAtributos(), tipo));
						break;
						
					case INSUMOS_TRASPASAR_SUMINISTROS: // EXPLOSION DE INSUMOS: Traspaso de suministros entre Explosiones de Insumos
						explosionInsumosTraspasarSuministrado(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()));
						break;
						
					default:
						mensajeLog(" -----> Evento '" + eventoParam + "' NO REGISTRADO :(");
						mensajeLog(" -----> Sin accion por Evento no reconocido");
						break;
				}
			}
	    	
			printLog();
    	} catch (Exception e) {
			printLog("Ocurrio un problema al procesar el mensaje para TOPIC GESTION PROYECTOS", e);
		} 
	}

	// ----------------------------------------------------------------------------------------
	// EVENTOS
	// ----------------------------------------------------------------------------------------
	
	private void altaObra(long idObra) throws Exception {
		Obra obra = null;
		
		try {
			mensajeLog(" -----> Evento OB_AL: BackOffice GP - Alta de Obra");
			mensajeLog(" -----> Obra     : " + idObra);
			if (idObra <= 0L) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Peticion para Asignacion de Almacenes
			obra = this.ifzObras.findById(idObra);
			peticionAlmacenes(obra.getId(), obra.getIdSucursal(), obra.getTipoObra());
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void bajaObra(long idObra) throws Exception {
		try {
			mensajeLog(" -----> Evento OB_BA: BackOffice GP - Baja de Obra");
			mensajeLog(" -----> Obra      : " + idObra);
			if (idObra <= 0L) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * BackOffice Obra Empleados: Quitar al Empleado de la(s) Obra(s)
	 * @param idEmpleado Empleado que se dio de baja y debe quitarse de las Obras
	 * @param idObra Obra de donde se quitara el Empleado, si no se indica, quitara de todas las Obras
	 * @throws Exception
	 */
	private void bajaEmpleadoObras(long idEmpleado, long idObra) throws Exception {
		List<ObraEmpleado> empleados = null;
		
		try {
			mensajeLog(" -----> Evento OBEM: BackOffice GP - Baja de Empleado en Obras");
			mensajeLog(" -----> Empleado  : " + idEmpleado);
			mensajeLog(" -----> Obra      : " + idObra);
			if (idEmpleado <= 0) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			if (idObra > 0) {
				mensajeLog("Comprobamos Obra ... ");
				empleados = this.ifzOEmpleados.findAll(idObra);
				mensajeLog("OK", true);
				for (ObraEmpleado item : empleados) {
					if (idEmpleado != item.getIdEmpleado().longValue()) 
						continue;
					mensajeLog("Quito Empleado ... ", true);
					this.ifzOEmpleados.delete(item.getId());
					mensajeLog("OK");
				}
			} else {
				mensajeLog("Comprobamos Asignaciones de Empleados a Obras ... ", true);
				empleados = this.ifzOEmpleados.findByProperty("idEmpleado", idEmpleado, 0);
				mensajeLog("OK");
				
				if (empleados == null || empleados.isEmpty()) 
					return;
				for (ObraEmpleado item : empleados) {
					mensajeLog("Quito Empleado de Obra " + item.getIdObra().getId() + " ... ");
					this.ifzOEmpleados.delete(item.getId());
					mensajeLog("OK", true);
				}
			}
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar quitar el Empleado de la(s) obra(s) asignadas. Empleado " + idEmpleado, e);
			throw e;
		}
	}

	/**
	 * Asignacion de Almacenes en Obra
	 * @param idObra
	 * @param idAlmacenPrincipal
	 * @param atributos
	 * @throws Exception
	 */
	private void obraAsignacionAlmacenes(Long idObra, Long idAlmacenPrincipal, String atributos) throws Exception {
		Type tipo = null;
		HashMap<Long, String> almacenes = null;
		// -------------------------------------------------
		List<ObraAlmacenes> oAlmacenes = null;
		ObraAlmacenes oAlmacen = null;
		Obra obra = null;
		
		try {
			idObra = (idObra != null ? idObra : 0L);
			idAlmacenPrincipal = (idAlmacenPrincipal != null ? idAlmacenPrincipal : 0L);
			atributos = (atributos != null ? atributos.trim() : "");
			if (! "".equals(atributos.trim())) {
				tipo = new TypeToken<HashMap<Long, String>>() {}.getType();
				almacenes = (new Gson()).fromJson(atributos, tipo);
			}
			almacenes = (almacenes != null ? almacenes : new HashMap<Long, String>());
			
			mensajeLog(" -----> Evento OB_BA: BackOffice GP - Asignacion de Almacenes en Obra");
			mensajeLog(" -----> Obra      : " + idObra);
			mensajeLog(" -----> Almacen   : " + idAlmacenPrincipal);
			mensajeLog(" -----> Almacenes : " + almacenes.size());
			if (idObra <= 0L || almacenes.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			obra = this.ifzObras.findById(idObra);
			if (obra == null || obra.getId() == null || obra.getId() <= 0L) 
				throw new Exception("Ocurrio un problema al recuperar la Obra indicada");
			
			for (Entry<Long, String> almacen : almacenes.entrySet()) {
				oAlmacen = new ObraAlmacenes();
				oAlmacen.setIdObra(obra);
				oAlmacen.setIdAlmacen(almacen.getKey());
				oAlmacen.setNombreAlmacen(almacen.getValue());
				oAlmacen.setTipo(1);
				oAlmacen.setAlmacenPrincipal(0);
				if (idAlmacenPrincipal.longValue() == almacen.getKey().longValue())
					oAlmacen.setAlmacenPrincipal(1);
				oAlmacen.setCreadoPor(obra.getCreadoPor());
				oAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
				oAlmacen.setModificadoPor(obra.getCreadoPor());
				oAlmacen.setFechaModificacion(Calendar.getInstance().getTime());
				oAlmacenes = (oAlmacenes != null ? oAlmacenes : new ArrayList<ObraAlmacenes>());
				oAlmacenes.add(oAlmacen);
			}
			
			if (oAlmacenes != null) {
				this.ifzOAlmacenes.setInfoSesion(this.infoSesion);
				this.ifzOAlmacenes.saveOrUpdateList(oAlmacenes);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * [INSUMOS_ESTATUS] BackOffice Explosion de Insumos: Actualizaciones de datos posterior al cambio de estatus de la Explosion de Insumos
	 * @param idExplosionInsumos
	 * @throws Exception
	 */
	private void explosionInsumosEstatus(long idExplosionInsumos) throws Exception {
		Insumos explosionInsumos = null;
		// ----------------------------------
		Presupuesto ppt01 = null;
		List<PresupuestoDetalle> ppt01Detalles = null;
		
		try {
			mensajeLog(" -----> Evento INSUMOS_ESTATUS: BackOffice Explosion de Insumos - Actualizaciones de datos posterior al cambio de estatus de la Explosion de Insumos");
			mensajeLog(" -----> ExplosionInsumos : " + idExplosionInsumos);
			
			// Recuperamos la Explosion de Insumos actual
			// ---------------------------------------------------------------------------------
			mensajeLog(" ---> Recuperando Explosion de Insumos ... ", true);
			explosionInsumos = this.ifzInsumos.findActual(idExplosionInsumos);
			if (explosionInsumos == null || explosionInsumos.getId() == null || explosionInsumos.getId() <= 0L) {
				topicSinAccion("No se pudo recuperar la Explosion de Insumos indicada: " + idExplosionInsumos);
				return;
			}
			mensajeLog("OK");

			// Actualizar PPT01
			// ---------------------------------------------------------------------------------
			mensajeLog(" ---> Comprobando PPT01 ... ", true);
			ppt01 = this.ifzPPT01.findActual(explosionInsumos.getIdObra());
			if (ppt01 != null && ppt01.getId() != null && ppt01.getId() > 0L) {
				mensajeLog("OK");
				ppt01Detalles = this.ifzPPT01Detalles.findAll(ppt01.getId());
				if (ppt01Detalles != null && ! ppt01Detalles.isEmpty() && ppt01Detalles.size() == 16) {
					mensajeLog(" ---> Actualizando PPT01 ... ", true);
					ppt01Detalles.get(4).setMonto(BigDecimal.ZERO);
					ppt01Detalles.get(5).setMonto(BigDecimal.ZERO);
					this.ifzPPT01Detalles.saveOrUpdateList(ppt01Detalles);
				}
			}
			mensajeLog("OK");
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar actualizar datos posterior al cambio de estatus de la Explosion de Insumos", e);
			throw e;
		}
	}
	
	/**
	 * BackOrder Explosion de Insumos. Actualizamos el Suministro de Explosion de Insumos
	 * @param idObra
	 * @param suministros Listado de Productos (ID) con su cantidad suministrada
	 */
	private void explosionInsumosSuministro(long idObra, HashMap<Long, Double> suministros) throws Exception {
		Insumos explosionInsumos = null;
		List<InsumosDetalles> detalles = null;
		int estatusExplosionInsumos = 0;
		
		try {
			mensajeLog(" -----> Evento EISU: BackOrder Explosion de Insumos - Actualizacion de suministro");
			mensajeLog(" -----> Obra      : " + idObra);
			mensajeLog(" -----> Productos : " + suministros.size());
			// Validaciones
			// ---------------------------------------------------------------------------------
			if (idObra <= 0L || suministros.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recuperamos la Explosion de Insumos actual
			// ---------------------------------------------------------------------------------
			mensajeLog(" ---> Recuperando Explosion de Insumos ... ", true);
			explosionInsumos = this.ifzInsumos.findActual(idObra);
			if (explosionInsumos == null || explosionInsumos.getId() == null || explosionInsumos.getId() <= 0L) {
				topicSinAccion("No se pudo recuperar la Explosion de Insumos de la Obra: " + idObra);
				return;
			}
			mensajeLog("OK");
			
			// Recupero los detalles de la Explosion de Insumos actual
			// ---------------------------------------------------------------------------------
			mensajeLog(" ---> Recuperando detalles de Explosion de Insumos ... ", true);
			detalles = this.ifzInsumosDetalles.findAll(explosionInsumos.getId());
			if (detalles == null || detalles.isEmpty())
				return;
			mensajeLog("OK");

			mensajeLog(" ---> Actualizando suministros ... ", true);
			for (InsumosDetalles detalle : detalles) {
				if (suministros.containsKey(detalle.getIdProducto()))
					detalle.addSuministrado(suministros.get(detalle.getIdProducto()));
			}
			mensajeLog("OK");
			
			// Guardamos lo suministros
			// ---------------------------------------------------------------------------------
			mensajeLog(" ---> Guardamos suministros ... ", true);
			this.ifzInsumosDetalles.saveOrUpdateList(detalles);
			mensajeLog("OK");
			
			// Comprobamos el estatus general de la Explosion de Insumos
			mensajeLog(" ---> Comprobamos el estatus general de la Explosion de Insumos ... ", true);
			estatusExplosionInsumos = 2; // Suministrada
			for (InsumosDetalles detalle : detalles) {
				if (detalle.getPendiente() > 0) {
					estatusExplosionInsumos = 0;
					break;
				}
			}
			mensajeLog("OK");
			
			if (estatusExplosionInsumos == 2) {
				mensajeLog(" ---> Actualizando estatus general de la Explosion de Insumos ... ", true);
				explosionInsumos.setEstatus(estatusExplosionInsumos);
				explosionInsumos.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumos.update(explosionInsumos);
				mensajeLog("OK");
			}
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar actualizar lo suministrado en la Explosion de Insumos", e);
			throw e;
		}
	}

	/**
	 * BackOffice Explosion de Insumos. Traspasar suministro de productos entre Explosion de Insumos.
	 * @param idInsumosActual
	 * @param idInsumosPrevia
	 */
	private void explosionInsumosTraspasarSuministrado(long idInsumosActual, long idInsumosPrevia) throws Exception {
		HashMap<Long,Double> suministrados = new HashMap<Long, Double>();
		List<InsumosDetalles> listDetalles = null;
		
		try {
			mensajeLog(" -----> Evento EIAS: BackOffice Explosion de Insumos - Traspaso de suministro entre Explosiones de Insumos");
			mensajeLog(" -----> Explosion de Insumos actual : " + idInsumosActual);
			mensajeLog(" -----> Explosion de Insumos previa : " + idInsumosPrevia);
			if (idInsumosActual <= 0 || idInsumosPrevia <= 0L) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recupero los detalles de la Explosion de Insumos actual
			mensajeLog("Recupero los detalles de la Explosion de Insumos anterior ... ", true);
			listDetalles = this.ifzInsumosDetalles.findAll(idInsumosPrevia);
			if (listDetalles == null || listDetalles.isEmpty()) {
				topicSinAccion("Sin accion por Explosion de Insumos vacia ");
				return;
			}
			mensajeLog("OK");

			mensajeLog("Calculo lo suministrado en la Explosion de Insumos anterior ... ", true);
			for (InsumosDetalles item : listDetalles) {
				if (item.getCantidadSurtida() > 0)
					suministrados.put(item.getIdProducto(), item.getCantidadSurtida());
			}
			
			if (suministrados.isEmpty()) {
				topicSinAccion("Sin accion por Explosion de Insumos previa sin suministros");
				return;
			}
			mensajeLog("OK");
			
			// Recupero todas las explosiones de insumos para la Obra en cuestion
			mensajeLog("Recupero los detalles de la Explosion de Insumos actual ... ", true);
			listDetalles = this.ifzInsumosDetalles.findAll(idInsumosActual);
			if (listDetalles == null || listDetalles.isEmpty())
				return;
			mensajeLog("OK");

			// Actualizamos el suministro donde corresponda y guardamos la Explosion de Insumos
			mensajeLog("Actualizo el suministro en la Explosion de Insumos actual ... ", true);
			for (InsumosDetalles item : listDetalles) {
				if (suministrados.containsKey(item.getIdProducto()))
					item.setCantidadSurtida(suministrados.get(item.getIdProducto()));
			}
			this.ifzInsumosDetalles.saveOrUpdateList(listDetalles);
			mensajeLog("OK");
			mensajeLog("Terminado");
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar actualizar lo surtido en la Explosion de Insumos: " + idInsumosActual, e);
			throw e;
		}
	}

	private void cobranzaActualizar(Long idObra, String atributos) throws Exception {
		List<ObraCobranza> listObraCobranza = null;
		Type tipo = null;
		Gson gson = null;

		try {
			idObra = (idObra != null ? idObra: 0L);
			atributos = (atributos != null ? atributos.trim() : "");
			gson = new Gson();
			tipo = new TypeToken<List<ObraCobranza>>() {}.getType();
			listObraCobranza = gson.fromJson(atributos, tipo);
			listObraCobranza = (listObraCobranza != null ? listObraCobranza : new ArrayList<ObraCobranza>());
			
			mensajeLog(" -----> Evento COBRANZA_ACTUALIZAR: BackOffice Cobranza. Actualizamos montos de cobranza");
			mensajeLog(" -----> Obra        : " + idObra.toString());
			mensajeLog(" -----> Items       : " + listObraCobranza.size());
			if (listObraCobranza.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			this.ifzCobranza.setInfoSesion(this.infoSesion);
			this.ifzCobranza.saveOrUpdateList(listObraCobranza);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar actualizar montos de Cobranza.", e);
			throw e;
		}
	}
	
	/**
	 * BackOffice Cobranza: Actualizamos ubicacion de factura en cobranza
	 * @param idFactura
	 * @param idObra
	 * @throws Exception
	 */
	private void cobranzaUbicacionPrevia(Long idFactura, Long idObraActual) throws Exception {
		List<ObraCobranza> cobranza = null;
		List<ObraCobranza> actualizados = null;
		Obra obra = null;
		
		try {
			mensajeLog(" -----> Evento COBRANZA_UBICACION_PREVIA: BackOffice Cobranza. Actualizamos ubicacion de factura en cobranza");
			mensajeLog(" -----> Factura     : " + idFactura);
			mensajeLog(" -----> Obra actual : " + idObraActual);
			if (idFactura <= 0 || idObraActual <= 0) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			obra = this.ifzObras.findById(idObraActual);
			if (obra == null || obra.getId() == null || obra.getId() <= 0L)
				throw new Exception("No se pudo recuperar la Obra indicada: Obra " + idObraActual);
			cobranza = this.ifzCobranza.findByFactura(idFactura);
			if (cobranza.isEmpty()) {
				topicSinAccion("Factura sin cobranza previa");
				return;
			}
			
			// Actualizamos y guardamos la cobranza
			actualizados = new ArrayList<ObraCobranza>();
			for (ObraCobranza item : cobranza) {
				if (idObraActual.longValue() != item.getIdObra().getId().longValue()) {
					item.setIdObra(obra);
					actualizados.add(item);
				}
			}
			
			if (actualizados != null && ! actualizados.isEmpty())
				this.ifzCobranza.saveOrUpdateList(actualizados);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar quitar la Factura de Cobranzas previas. Factura " + idFactura, e);
			throw e;
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// METODOS PRIVADOS
	// ----------------------------------------------------------------------------------------

	/*private void autoMessage(TopicEventosGP evento, Object target, Object referencia, Object atributos) {
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

	private void peticionAlmacenes(Long idObra, Long idSucursal, Integer tipoObra) {
		MensajeTopic msgTopic = null;
		String target = "";
		String referencia = "";
		String referenciaExtra = "";
		String atributos = "";
		
		try {
			target = (idObra != null && idObra >= 0L ? idObra.toString() : "0");
			referencia = (idSucursal != null && idSucursal >=0L ? idSucursal.toString() : "0");
			referenciaExtra = (tipoObra != null && tipoObra > 0 ? tipoObra.toString() : "0");
			msgTopic = new MensajeTopic(TopicEventosInventarios.ASIGNACION_ALMACENES, target, referencia, atributos, this.infoSesion);
			msgTopic.setReferenciaExtra(referenciaExtra);
			msgTopic.enviar();
		} catch (Exception e) { 
			log.error("Ocurrio un problema al enviar la peticion de Almacenes para asignacion en Obra\n" + this.getClass().getCanonicalName() + ".peticionAlmacenes(idObra, idSucursal, tipoObra)", e);
		} 
	}

	private String valueToString(Object value) {
		String resultado = "";
		
		if (value != null) {
			resultado = value.toString();
			resultado = ("".equals(resultado.trim()) ? "" : resultado.trim());
		}
		
		return resultado;
	}
	
	private long valueToLong(Object value) {
		String strValue = valueToString(value);
		strValue = (! "".equals(strValue.trim()) ? strValue.trim() : "0");
		return Long.valueOf(strValue);
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
		printLog("##############################    TOPIC GESTION PROYECTOS    ##############################");
	}

	private void printLog(String mensaje) {
		try {
			mensajeLog(mensaje);
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.info("TOPIC/GP Log\n\n" + mensaje);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Terminado, "OK\n" + mensaje);
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/GP. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}
	
	private void printLog(String mensaje, Throwable throwable) {
		try {
			if (mensaje != null && ! "".equals(mensaje.trim()))
				mensajeLog(mensaje);
			mensajeLog("############################## TOPIC GESTION PROYECTOS --- FIN : " + new Date());
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.error("TOPIC/GP Log\n\n" + mensaje + "\nEXCEPTION\n", throwable);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Error, mensaje + "\nEXCEPTION\n" + throwable.getMessage());
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/GP. TopicEstatus: " + this.idTopicEstatus, e);
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

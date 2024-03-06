package net.giro.contabilidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.beans.PolizasInterfaces;
import net.giro.contabilidad.beans.TransaccionesData;
import net.giro.contabilidad.logica.MensajeTransaccionRem;
import net.giro.contabilidad.logica.PolizasInterfacesRem;
import net.giro.contabilidad.logica.TransaccionesDataRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="msgAction")
public class MensajeTransaccionesAction implements Serializable, Runnable {
	private static Logger log = Logger.getLogger(MensajeTransaccionesAction.class);
	private static final long serialVersionUID = 1L;
	private LoginManager loginManager;
	private InitialContext ctx;
	// principal
	private MensajeTransaccionRem ifzMsg;
	private TransaccionesDataRem ifzTData; 
	private PolizasInterfacesRem ifzPolizas;
	// variables
	private List<MensajeTransaccion> listResults;
	private MensajeTransaccion pojoMsg;
	private List<TransaccionesData> listTData;
	private List<PolizasInterfaces> listPolizas;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private long idUsuario;
	private int paginacionTData;
	private int paginacionPolizas;
	// Busqueda principal
	private List<SelectItem> busquedaOpciones;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaPagina;
	// DEBUG
	private HashMap<String, String> paramsRequest;
	private boolean isDebug;
	
	
	public MensajeTransaccionesAction() {
		FacesContext facesContext = null;
		ValueExpression valExp = null;
		Application app = null;
		
		try {
			facesContext = FacesContext.getCurrentInstance();
	        app = facesContext.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(facesContext.getELContext());

			this.idUsuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId(); // lm.getLoginBean().getUsuario().getUsuarioId();
			this.paramsRequest = new HashMap<String, String>();
			
			Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("idOperacion", "Operacion"));
			this.busquedaOpciones.add(new SelectItem("nombrePersonaReferencia", "Persona/Negocio"));
			this.busquedaOpciones.add(new SelectItem("descripcionMoneda", "Moneda"));
			this.busquedaOpciones.add(new SelectItem("importe", "Monto"));
			this.busquedaCampo = this.busquedaOpciones.get(0).getValue().toString();
			this.busquedaValor = "";
			this.busquedaPagina = 1;

			this.ctx = new InitialContext();
			this.ifzMsg = (MensajeTransaccionRem) this.ctx.lookup("ejb:/Logica_Contabilidad//MensajeTransaccionFac!net.giro.contabilidad.logica.MensajeTransaccionRem");
			this.ifzTData = (TransaccionesDataRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesDataFac!net.giro.contabilidad.logica.TransaccionesDataRem");
			this.ifzPolizas = (PolizasInterfacesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//PolizasInterfacesFac!net.giro.contabilidad.logica.PolizasInterfacesRem");
			
			if (this.ifzMsg == null)
				log.error("No me pude conectar el EJB MensajeTransaccionRem");
			this.pojoMsg = new MensajeTransaccion();
		} catch (Exception e) {
			log.error("Error en Contabilidad.MensajeTransaccionesAction.constructor()", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = this.busquedaOpciones.get(0).getValue().toString();

			this.busquedaPagina = 1;
			if (this.listResults == null)
				this.listResults = new ArrayList<MensajeTransaccion>();
			this.listResults.clear();

			this.ifzMsg.orderBy("CASE estatus WHEN -1 THEN 2 WHEN 2 THEN 1 ELSE 0 END, id desc");
			this.listResults = this.ifzMsg.findLikeProperty(this.busquedaCampo, this.busquedaValor);
			if (this.listResults.isEmpty()) {
				log.info("ERROR 2 : Busqueda sin resultados");
				control(2);
				return;
			}
		} catch (Exception e) {
			log.error("Error en Contabilidad.MensajeTransaccionesAction.buscar", e);
			control(true);
		} finally {
			if (this.listResults == null)
				this.listResults = new ArrayList<MensajeTransaccion>();
			log.info(this.listResults.size() + " resultados encontrados");
		}
	}
	
	public void ver() {
		HashMap<String, String> params = new HashMap<String, String>();
		
		try {
			control();
			if (this.pojoMsg == null || this.pojoMsg.getId() == null || this.pojoMsg.getId() <= 0L) {
				log.info("ERROR INTERNO - No selecciono ningun mensaje");
				control("No pude recuperar el Mensaje seleccionado");
				return;
			}
			
			log.info("Recuperando TransaccionesData ... ");
			this.paginacionTData = 1;
			params.put("poliza", this.pojoMsg.getPoliza().toString());
			params.put("lote", this.pojoMsg.getLote().toString());
			this.ifzTData.orderBy("codigoTransaccion, idConcepto");
			this.listTData = this.ifzTData.findByProperties(params, 0);
			if (this.listTData == null)
				this.listTData = new ArrayList<TransaccionesData>();
			log.info(this.listTData.size() + " TransaccionesData recuperadas");

			log.info("Recuperando PolizasInterfaces ... ");
			params.clear();
			this.paginacionPolizas = 1;
			params.put("idInterfaz", this.pojoMsg.getLote().toString());
			params.put("folioPoliza", this.pojoMsg.getPoliza().toString());
			this.listPolizas = this.ifzPolizas.findByProperties(params, 0);
			if (this.listPolizas == null)
				this.listPolizas = new ArrayList<PolizasInterfaces>();
			log.info(this.listPolizas.size() + " PolizasInterfaces recuperadas");
		} catch (Exception e) {
			log.error("Error en Contabilidad.MensajeTransaccionesAction.verMensaje", e);
			control(true);
		} 
	}

	public void guardar() {
		try {
			control();
			log.info("Guardando mensaje ... ");
			if (this.pojoMsg != null) {
				log.info("ERROR INTERNO - Ningun mensaje creado");
				control("No se pudo guardar el Mensaje.\nMensaje sin valores");
				return;
			}
			
			if (this.pojoMsg.getId() == null || this.pojoMsg.getId() <= 0L) {
				log.info("ERROR INTERNO - No se puede guardar ningun mensaje");
				control("No se pudo guardar el Mensaje.\nNo se permite desde esta locacion");
				return;
			}

			this.ifzMsg.update(this.pojoMsg);
			log.info("El mensaje " + this.pojoMsg.getId() + " ha sido guardado");
		} catch (Exception e) {
			log.error("Error en Contabilidad.MensajeTransaccionesAction.guardar", e);
			control(true);
		} 
	}

	public void borrar() {
		int index = -1;
		
		try {
			control();
			log.info("Borrando mensaje seleccionado");
			if (this.pojoMsg == null) {
				log.info("ERROR INTERNO - Ningun mensaje seleccionado");
				control("No pude borrar el Mensaje seleccionado");
				return;
			}
			
			// Obtenemos indice en la lista
			index = this.listResults.indexOf(this.pojoMsg);
			
			if (this.pojoMsg.getId() != null && this.pojoMsg.getId() > 0L) {
				// Cambiamos estatus del mensaje en BD
				log.info("Cambiando estatus (eliminando) a mensaje " + this.pojoMsg.getId());
				this.pojoMsg.setFechaAnulacion(Calendar.getInstance().getTime());
				this.pojoMsg.setAnuladoPor(this.idUsuario);
				this.pojoMsg.setEstatusMensaje("CANCELADO - " + this.pojoMsg.getEstatusMensaje());
				this.pojoMsg.setEstatus(-1);
				this.ifzMsg.update(this.pojoMsg);
				log.info("El mensaje " + this.pojoMsg.getId() + " ha sido borrado");
				
				// Lanzo hilo para cancelar el efecto de la transaccion, dependiendo del codigo
				new Thread(this).run();
			}
			
			if (index >= 0 && index < this.listResults.size())
				this.listResults.set(index, this.pojoMsg);
			log.info("Mensaje seleccionado borrado");
		} catch (Exception e) {
			log.error("Error en Contabilidad.MensajeTransaccionesAction.borrar", e);
			control(true);
		} 
	}
	
	public void ejecutar() {
		try {
			if (this.pojoMsg == null) {
				log.info("ERROR INTERNO - No puedo ejecutar la Transaccion indicada. No selecciono ningun Mensaje.");
				control("No puedo ejecutar la Transaccion indicada");
				return;
			}
			
			if (this.pojoMsg.getEstatus() == 1) {
				log.info("El Mensaje " + this.pojoMsg.getId() + " se envia para Contabilizacion.");
				contabilizar();
				return;
			}
			
			if (this.pojoMsg.getEstatus() == 2) {
				log.info("La Mensaje " + this.pojoMsg.getId() + " ya ha terminado su flujo. Transaccion Contabilizada");
				control("Esta Transaccion ya ha sido contabilizada");
				return;
			}
			
			this.ifzMsg.enviarMensajeTransaccion(this.pojoMsg);
			log.info("Transaccion enviada. Mensaje " + this.pojoMsg.getId());
			control(false, 0, "Transaccion enviada");
		} catch (Exception e) {
			log.error("Error en Contabilidad.MensajeTransaccionesAction.ejecutar", e);
			control(true);
		}
	}
	
	public void contabilizar() {
		try {
			if (this.pojoMsg == null) {
				log.info("ERROR INTERNO - No puedo ejecutar la Transaccion indicada. No selecciono ningun Mensaje.");
				control("No puedo ejecutar la Transaccion indicada");
				return;
			}
			
			if (this.pojoMsg.getEstatus() != 1) {
				log.info("El Mensaje " + this.pojoMsg.getId() + " no se ha procesado. Se envia para Transaccion.");
				ejecutar();
				return;
			}
			
			this.ifzMsg.enviarMensajeTransaccion(this.pojoMsg);
			log.info("Transaccion enviada para contabilizar. Mensaje " + this.pojoMsg.getId());
			control(false, 0, "Transaccion enviada para Contabilizar");
		} catch (Exception e) {
			log.error("Error en Contabilidad.MensajeTransaccionesAction.contabilizar", e);
			control(true);
		}
	}
	
	public void control() {
		control(false, 0, "");
	}
	
	public void control(boolean value) {
		if (value) {
			control(true, 1, "");
			return;
		}
		
		control();
	}
	
	public void control(int tipoMensaje) {
		if (tipoMensaje == 0) {
			control();
			return;
		}
		
		if (tipoMensaje == 1) {
			control(true);
			return;
		}
	
		control(true, tipoMensaje, "");
	}
	
	public void control(String mensaje) {
		if (mensaje == null || "".equals(mensaje.trim())) {
			control();
			return;
		}
		
		control(true, -1, mensaje.trim());
	}
	
	public void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		mensaje = (mensaje == null || "".equals(mensaje.trim())) ? "" : mensaje.trim();
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
	}

	// ------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------

	public List<MensajeTransaccion> getListResults() {
		return listResults;
	}

	public void setListResults(List<MensajeTransaccion> listResults) {
		this.listResults = listResults;
	}

	public MensajeTransaccion getPojoMensaje() {
		return pojoMsg;
	}

	public void setPojoMensaje(MensajeTransaccion pojoMensaje) {
		this.pojoMsg = pojoMensaje;
	}

	public boolean getOperacion() {
		return operacionCancelada;
	}

	public void setOperacion(boolean operacion) {
		this.operacionCancelada = operacion;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<SelectItem> getBusquedaOpciones() {
		return busquedaOpciones;
	}

	public void setBusquedaOpciones(List<SelectItem> busquedaOpciones) {
		this.busquedaOpciones = busquedaOpciones;
	}

	public String getBusquedaCampo() {
		return busquedaCampo;
	}

	public void setBusquedaCampo(String busquedaCampo) {
		this.busquedaCampo = busquedaCampo;
	}

	public String getBusquedaValor() {
		return busquedaValor;
	}

	public void setBusquedaValor(String busquedaValor) {
		this.busquedaValor = busquedaValor;
	}

	public int getBusquedaPagina() {
		return busquedaPagina;
	}

	public void setBusquedaPagina(int busquedaPagina) {
		this.busquedaPagina = busquedaPagina;
	}

	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean debugging) {
		this.isDebug = debugging;
	}
	
	public String getMsgId() {
		if (this.pojoMsg != null && this.pojoMsg.getId() != null) 
			return this.pojoMsg.getId().toString();
		return "";
	}

	public void setMsgId(String value) {}

	public String getMsgIdTransaccion() {
		if (this.pojoMsg != null && this.pojoMsg.getIdTransaccion() != null) 
			return this.pojoMsg.getIdTransaccion().toString();
		return "";
	}

	public void setMsgIdTransaccion(String value) {}

	public String getMsgIdOperacion() {
		if (this.pojoMsg != null && this.pojoMsg.getIdOperacion() != null) 
			return this.pojoMsg.getIdOperacion().toString();
		return "";
	}

	public void setMsgIdOperacion(String value) {}

	public String getMsgIdMoneda() {
		if (this.pojoMsg != null && this.pojoMsg.getIdMoneda() != null) 
			return this.pojoMsg.getIdMoneda().toString();
		return "";
	}

	public void setMsgIdMoneda(String value) {}

	public String getMsgDescripcionMoneda() {
		if (this.pojoMsg != null && this.pojoMsg.getDescripcionMoneda() != null) 
			return this.pojoMsg.getDescripcionMoneda().toString();
		return "";
	}

	public void setMsgDescripcionMoneda(String value) {}

	public String getMsgImporte() {
		if (this.pojoMsg != null && this.pojoMsg.getImporte() != null) 
			return this.pojoMsg.getImporte().toString();
		return "";
	}

	public void setMsgImporte(String value) {}

	public String getMsgIdPersonaReferencia() {
		if (this.pojoMsg != null && this.pojoMsg.getIdPersonaReferencia() != null) 
			return this.pojoMsg.getIdPersonaReferencia().toString();
		return "";
	}

	public void setMsgIdPersonaReferencia(String value) {}

	public String getMsgNombrePersonaReferencia() {
		if (this.pojoMsg != null && this.pojoMsg.getNombrePersonaReferencia() != null) 
			return this.pojoMsg.getNombrePersonaReferencia().toString();
		return "";
	}

	public void setMsgNombrePersonaReferencia(String value) {}

	public String getMsgIdFormaPago() {
		if (this.pojoMsg != null && this.pojoMsg.getIdFormaPago() != null) 
			return this.pojoMsg.getIdFormaPago().toString();
		return "";
	}

	public void setMsgIdFormaPago(String value) {}

	public String getMsgReferenciaFormaPago() {
		if (this.pojoMsg != null && this.pojoMsg.getReferenciaFormaPago() != null) 
			return this.pojoMsg.getReferenciaFormaPago().toString();
		return "";
	}

	public void setMsgReferenciaFormaPago(String value) {}

	public String getMsgIdSucursal() {
		if (this.pojoMsg != null && this.pojoMsg.getIdSucursal() != null) 
			return this.pojoMsg.getIdSucursal().toString();
		return "";
	}

	public void setMsgIdSucursal(String value) {}

	public String getMsgPoliza() {
		if (this.pojoMsg != null && this.pojoMsg.getPoliza() != null) 
			return this.pojoMsg.getPoliza().toString();
		return "";
	}

	public void setMsgPoliza(String value) {}

	public String getMsgLote() {
		if (this.pojoMsg != null && this.pojoMsg.getLote() != null) 
			return this.pojoMsg.getLote().toString();
		return "";
	}

	public void setMsgLote(String value) {}

	public String getMsgEstatusMensaje() {
		if (this.pojoMsg != null && this.pojoMsg.getEstatusMensaje() != null) 
			return this.pojoMsg.getEstatusMensaje().toString();
		return "";
	}

	public void setMsgEstatusMensaje(String value) {}

	public List<TransaccionesData> getListTData() {
		return listTData;
	}

	public void setListTData(List<TransaccionesData> listTData) {
		this.listTData = listTData;
	}

	public int getPaginacionTData() {
		return paginacionTData;
	}

	public void setPaginacionTData(int paginacionTData) {
		this.paginacionTData = paginacionTData;
	}

	public List<PolizasInterfaces> getListPolizas() {
		return listPolizas;
	}

	public void setListPolizas(List<PolizasInterfaces> listPolizas) {
		this.listPolizas = listPolizas;
	}

	public int getPaginacionPolizas() {
		return paginacionPolizas;
	}

	public void setPaginacionPolizas(int paginacionPolizas) {
		this.paginacionPolizas = paginacionPolizas;
	}


	
	@Override
	public void run() {
		switch(this.pojoMsg.getIdTransaccion().intValue()) {
			case 1010: //
				log.info("Rollback Transaccion 1010");
				break;
			case 1011: //
				log.info("Rollback Transaccion 1011");
				break;
			case 1012: //
				log.info("Rollback Transaccion 1012");
				break;
			case 1013: //
				log.info("Rollback Transaccion 1013");
				break;
			case 1014: //
				log.info("Rollback Transaccion 1014");
				break;
			case 1015: //
				log.info("Rollback Transaccion 1015");
				break;
			case 1016: //
				log.info("Rollback Transaccion 1016");
				break;
			case 1017: //
				log.info("Rollback Transaccion 1017");
				break;
			case 1018: //
				log.info("Rollback Transaccion 1018");
				break;
			case 1019: //
				log.info("Rollback Transaccion 1019");
				break;
			case 1020: //
				log.info("Rollback Transaccion 1020");
				break;
			case 1021: //
				log.info("Rollback Transaccion 1021");
				break;
			case 1022: //
				log.info("Rollback Transaccion 1022");
				break;
			case 1023: //
				log.info("Rollback Transaccion 1023");
				break;
			case 1024: //
				log.info("Rollback Transaccion 1024");
				break;
			case 1025: //
				log.info("Rollback Transaccion 1025");
				break;
			case 1026: //
				log.info("Rollback Transaccion 1026");
				break;
			case 1027: //
				log.info("Rollback Transaccion 1027");
				break;
			case 1028: //
				log.info("Rollback Transaccion 1028");
				break;
			case 1029: //
				log.info("Rollback Transaccion 1029");
				break;
			case 1030: //
				log.info("Rollback Transaccion 1030");
				break;
			case 1031: //
				log.info("Rollback Transaccion 1031");
				break;
			default: break;
		}
		
	}
}

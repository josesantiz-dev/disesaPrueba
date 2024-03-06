package net.giro.contabilidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
public class MensajeTransaccionesAction implements Serializable {
	private static Logger log = Logger.getLogger(MensajeTransaccionesAction.class);
	private static final long serialVersionUID = 1L;
	private LoginManager loginManager;
	private InitialContext ctx;
	// principal
	private MensajeTransaccionRem ifzMensajes;
	private TransaccionesDataRem ifzTData; 
	private PolizasInterfacesRem ifzPolizas;
	// variables
	private List<MensajeTransaccion> listResults;
	private MensajeTransaccion pojoMensaje;
	private List<TransaccionesData> listTData;
	private List<PolizasInterfaces> listPolizas;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private boolean incluyeContabilizado;
	private long idUsuario;
	private int paginacionTData;
	private int paginacionPolizas;
	// Busqueda principal
	private List<SelectItem> busquedaOpciones;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaPagina;
	// Estatus Mensajes
	private List<SelectItem> tiposEstatusMensaje;
	private int estatusMensaje;
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
			for (Entry<String, String> item : facesContext.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("idTransaccion", "Transaccion"));
			this.busquedaOpciones.add(new SelectItem("idOperacion", "Operacion"));
			this.busquedaOpciones.add(new SelectItem("nombrePersonaReferencia", "Persona/Negocio"));
			this.busquedaOpciones.add(new SelectItem("descripcionMoneda", "Moneda"));
			this.busquedaOpciones.add(new SelectItem("importe", "Monto"));
			this.busquedaCampo = this.busquedaOpciones.get(0).getValue().toString();
			this.busquedaValor = "";
			this.busquedaPagina = 1;
			
			this.tiposEstatusMensaje = new ArrayList<SelectItem>(); // 0 - Sin ejecucion, 1 - Por Contabilizar, 2 - Contabilizado
			this.tiposEstatusMensaje.add(new SelectItem(-1, "Cancelado"));
			this.tiposEstatusMensaje.add(new SelectItem(0, "Sin ejecucion"));
			this.tiposEstatusMensaje.add(new SelectItem(1, "Por Contabilizar"));
			this.tiposEstatusMensaje.add(new SelectItem(2, "Contabilizado"));

			this.ctx = new InitialContext();
			this.ifzMensajes = (MensajeTransaccionRem) this.ctx.lookup("ejb:/Logica_Contabilidad//MensajeTransaccionFac!net.giro.contabilidad.logica.MensajeTransaccionRem");
			this.ifzTData = (TransaccionesDataRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesDataFac!net.giro.contabilidad.logica.TransaccionesDataRem");
			this.ifzPolizas = (PolizasInterfacesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//PolizasInterfacesFac!net.giro.contabilidad.logica.PolizasInterfacesRem");

			this.ifzMensajes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTData.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPolizas.setInfoSesion(this.loginManager.getInfoSesion());

			if (this.ifzMensajes == null)
				log.error("No me pude conectar el EJB MensajeTransaccionRem");
			this.pojoMensaje = new MensajeTransaccion();
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
			this.listResults = new ArrayList<MensajeTransaccion>();
			this.listResults = this.ifzMensajes.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.incluyeContabilizado, false, "id desc", 0);
			if (this.listResults == null || this.listResults.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Eventos", e);
		} finally {
			if (this.listResults == null)
				this.listResults = new ArrayList<MensajeTransaccion>();
			log.info(this.listResults.size() + " resultados encontrados");
		}
	}
	
	public void ver() {
		try {
			control();
			if (this.pojoMensaje == null || this.pojoMensaje.getId() == null || this.pojoMensaje.getId() <= 0L) {
				control(-1, "No pude recuperar el Mensaje seleccionado");
				return;
			}
			
			// Estatus de Mensaje ...
			log.info("Estatus de Mensaje ... ");
			this.estatusMensaje = this.pojoMensaje.getEstatus();
			
			// TransaccionesData ...
			log.info("Recuperando TransaccionesData ... ");
			this.listTData = this.ifzTData.findAll(this.pojoMensaje.getId(), "idOperacion, idConcepto, id");
			
			// PolizasInterfaces ... 
			log.info("Recuperando PolizasInterfaces ... ");
			this.listPolizas = this.ifzPolizas.findAll(this.pojoMensaje.getId(), "noLinea, id");
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los detalles del Evento seleccionado", e);
		} finally { 
			this.paginacionTData = 1;
			if (this.listTData == null)
				this.listTData = new ArrayList<TransaccionesData>();
			log.info(this.listTData.size() + " TransaccionesData recuperadas");
			
			this.paginacionPolizas = 1;
			if (this.listPolizas == null)
				this.listPolizas = new ArrayList<PolizasInterfaces>();
			log.info(this.listPolizas.size() + " PolizasInterfaces recuperadas");
		}
	}

	public void guardar() {
		try {
			control();
			log.info("Guardando mensaje ... ");
			if (this.pojoMensaje != null) {
				log.info("ERROR INTERNO - Ningun mensaje creado");
				control(-1, "No se pudo guardar el Mensaje.\nMensaje sin valores");
				return;
			}
			
			if (this.pojoMensaje.getId() == null || this.pojoMensaje.getId() <= 0L) {
				log.info("ERROR INTERNO - No se puede guardar ningun mensaje");
				control(-1, "No se pudo guardar el Mensaje.\nNo se permite desde esta locacion");
				return;
			}

			this.ifzMensajes.update(this.pojoMensaje);
			log.info("El mensaje " + this.pojoMensaje.getId() + " ha sido guardado");
		} catch (Exception e) {
			control("Ocurrio un problema al guardar los cambion en el Evento", e);
		} 
	}

	public void borrar() {
		int index = -1;
		
		try {
			control();
			log.info("Borrando mensaje seleccionado");
			if (this.pojoMensaje == null && this.pojoMensaje.getId() == null && this.pojoMensaje.getId() <= 0L) {
				log.info("ERROR INTERNO - Ningun mensaje seleccionado");
				control(-1, "No pude borrar el Mensaje seleccionado");
				return;
			}
			
			// Obtenemos indice en la lista
			index = this.listResults.indexOf(this.pojoMensaje);
			
				// Cambiamos estatus del mensaje en BD
			log.info("Cambiando estatus (eliminando) a mensaje " + this.pojoMensaje.getId());
			this.pojoMensaje.setEstatus(-1);
			this.pojoMensaje.setAnuladoPor(this.idUsuario);
			this.pojoMensaje.setFechaAnulacion(Calendar.getInstance().getTime());
			this.pojoMensaje.setEstatusMensaje("CANCELADO - " + this.pojoMensaje.getEstatusMensaje());
			this.ifzMensajes.update(this.pojoMensaje);
			log.info("El mensaje " + this.pojoMensaje.getId() + " ha sido borrado");
				
			// Lanzo hilo para cancelar el efecto de la transaccion, dependiendo del codigo
			//new Thread(this).run();
			
			if (index >= 0 && index < this.listResults.size())
				this.listResults.set(index, this.pojoMensaje);
			log.info("Mensaje seleccionado borrado");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Cancelar el evento indicado", e);
		} 
	}
	
	public void ejecutar() {
		try {
			if (this.pojoMensaje == null && this.pojoMensaje.getId() == null && this.pojoMensaje.getId() <= 0L) {
				log.info("ERROR INTERNO - No puedo ejecutar la Transaccion indicada. No selecciono ningun Mensaje.");
				control(-1, "No puedo ejecutar la Transaccion indicada");
				return;
			}
			
			// Aplicamos estatus si corresponde
			if (this.estatusMensaje != this.pojoMensaje.getEstatus()) {
				this.pojoMensaje.setEstatus(this.estatusMensaje);
				this.ifzMensajes.update(this.pojoMensaje);
				if (this.pojoMensaje.getEstatus() == 2)
					return;
			} 
			
			if (this.pojoMensaje.getEstatus() == 2) {
				log.info("La Mensaje " + this.pojoMensaje.getId() + " ya ha terminado su flujo. Transaccion Contabilizada");
				control(-1, "Esta Transaccion ya ha sido contabilizada");
				return;
			}
			
			this.ifzMensajes.enviarMensajeTransaccion(this.pojoMensaje);
			log.info("Transaccion enviada. Mensaje " + this.pojoMensaje.getId());
			control(false, 0, "Transaccion enviada", null);
		} catch (Exception e) { 
			control("Ocurrio un problema al lanzar el evento para Transaccionar", e);
		}
	}
	
	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n\n" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + " :: CONTABILIDAD :: " + tipoMensaje + " - " + mensaje, throwable);
	}

	// ------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------

	public boolean getPermisoAdmin() {
		return ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario()));
	}
	
	public void setPermisoAdmin(boolean value) {}
	
	public List<SelectItem> getTiposEstatusMensaje() {
		return tiposEstatusMensaje;
	}
	
	public void setTiposEstatusMensaje(List<SelectItem> tiposEstatusMensaje) {
		this.tiposEstatusMensaje = tiposEstatusMensaje;
	}
	
	public int getEstatusMensaje() {
		return estatusMensaje;
	}
	
	public void setEstatusMensaje(int estatusMensaje) {
		this.estatusMensaje = estatusMensaje;
	}
	
	public boolean isIncluyeContabilizado() {
		return incluyeContabilizado;
	}

	public void setIncluyeContabilizado(boolean incluyeContabilizado) {
		this.incluyeContabilizado = incluyeContabilizado;
	}
	
	public List<MensajeTransaccion> getListResults() {
		return listResults;
	}

	public void setListResults(List<MensajeTransaccion> listResults) {
		this.listResults = listResults;
	}

	public MensajeTransaccion getPojoMensaje() {
		return pojoMensaje;
	}

	public void setPojoMensaje(MensajeTransaccion pojoMensaje) {
		this.pojoMensaje = pojoMensaje;
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
		if (this.pojoMensaje != null && this.pojoMensaje.getId() != null) 
			return this.pojoMensaje.getId().toString();
		return "";
	}

	public void setMsgId(String value) {}

	public int getMsgEstatus() {
		if (this.pojoMensaje != null) 
			return this.pojoMensaje.getEstatus();
		return 0;
	}

	public void setMsgId(int value) {}

	public String getMsgIdTransaccion() {
		if (this.pojoMensaje != null && this.pojoMensaje.getIdTransaccion() != null) 
			return this.pojoMensaje.getIdTransaccion().toString();
		return "";
	}

	public void setMsgIdTransaccion(String value) {}

	public String getMsgIdOperacion() {
		if (this.pojoMensaje != null && this.pojoMensaje.getIdOperacion() != null) 
			return this.pojoMensaje.getIdOperacion().toString();
		return "";
	}

	public void setMsgIdOperacion(String value) {}

	public String getMsgDescOperacion() {
		if (this.pojoMensaje != null && this.pojoMensaje.getIdOperacion() != null) 
			return this.pojoMensaje.getDescripcionOperacion();
		return "";
	}

	public void setMsgDescOperacion(String value) {}

	public String getMsgIdMoneda() {
		if (this.pojoMensaje != null && this.pojoMensaje.getIdMoneda() != null) 
			return this.pojoMensaje.getIdMoneda().toString();
		return "";
	}

	public void setMsgIdMoneda(String value) {}

	public String getMsgDescripcionMoneda() {
		if (this.pojoMensaje != null && this.pojoMensaje.getDescripcionMoneda() != null) 
			return this.pojoMensaje.getDescripcionMoneda().toString();
		return "";
	}

	public void setMsgDescripcionMoneda(String value) {}

	public String getMsgImporte() {
		if (this.pojoMensaje != null && this.pojoMensaje.getImporte() != null) 
			return this.pojoMensaje.getImporte().toString();
		return "";
	}

	public void setMsgImporte(String value) {}

	public String getMsgIdPersonaReferencia() {
		if (this.pojoMensaje != null && this.pojoMensaje.getIdPersonaReferencia() != null) 
			return this.pojoMensaje.getIdPersonaReferencia().toString();
		return "";
	}

	public void setMsgIdPersonaReferencia(String value) {}

	public String getMsgNombrePersonaReferencia() {
		if (this.pojoMensaje != null && this.pojoMensaje.getNombrePersonaReferencia() != null) 
			return this.pojoMensaje.getNombrePersonaReferencia().toString();
		return "";
	}

	public void setMsgNombrePersonaReferencia(String value) {}

	public String getMsgIdFormaPago() {
		if (this.pojoMensaje != null && this.pojoMensaje.getIdFormaPago() != null) 
			return this.pojoMensaje.getIdFormaPago().toString();
		return "";
	}

	public void setMsgIdFormaPago(String value) {}

	public String getMsgDescFormaPago() {
		if (this.pojoMensaje != null && this.pojoMensaje.getIdFormaPago() != null) 
			return this.pojoMensaje.getDescripcionFormaPago();
		return "";
	}

	public void setMsgDescFormaPago(String value) {}

	public String getMsgReferenciaFormaPago() {
		if (this.pojoMensaje != null && this.pojoMensaje.getReferenciaFormaPago() != null) 
			return this.pojoMensaje.getReferenciaFormaPago().toString();
		return "";
	}

	public void setMsgReferenciaFormaPago(String value) {}

	public String getMsgIdSucursal() {
		if (this.pojoMensaje != null && this.pojoMensaje.getIdSucursal() != null) 
			return this.pojoMensaje.getIdSucursal().toString();
		return "";
	}

	public void setMsgIdSucursal(String value) {}

	public String getMsgDescSucursal() {
		if (this.pojoMensaje != null && this.pojoMensaje.getIdSucursal() != null) 
			return this.pojoMensaje.getDescripcionSucursal();
		return "";
	}

	public void setMsgDescSucursal(String value) {}

	public String getMsgPoliza() {
		if (this.pojoMensaje != null && this.pojoMensaje.getPoliza() != null) 
			return this.pojoMensaje.getPoliza().toString();
		return "";
	}

	public void setMsgPoliza(String value) {}

	public String getMsgLote() {
		if (this.pojoMensaje != null && this.pojoMensaje.getLote() != null) 
			return this.pojoMensaje.getLote().toString();
		return "";
	}

	public void setMsgLote(String value) {}

	public String getMsgEstatusMensaje() {
		if (this.pojoMensaje != null && this.pojoMensaje.getEstatusMensaje() != null) 
			return this.pojoMensaje.getEstatusMensaje().toString();
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

	/*@Override
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
	}*/
}

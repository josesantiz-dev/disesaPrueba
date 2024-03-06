package net.giro.cxc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.Provisiones;
import net.giro.cxc.logica.FacturaPagosRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.cxc.logica.ProvisionesRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosCXC;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.Meses;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="provAction")
public class ProvisionesAction implements Serializable {
	private static Logger log = Logger.getLogger(ProvisionesAction.class);
	private static final long serialVersionUID = 1L;
	private LoginManager loginManager;
	private ProvisionesRem ifzProvisiones;
	private List<Provisiones> listProvisiones;
	private Provisiones pojoProvision;
	private long idProvision; 
	private int paginacionProvisiones;
	//private int paginacionDetalle;
	// Agregar factura
	private double montoMaximoProvision;
	private double montoProvision;
	private String observaciones;
	private boolean provisionadaPreviamente;
	// Busqueda Facturas
	private FacturaRem ifzFacturas;
	private List<Factura> listFacturas;
	private Factura pojoFacturaSeleccionada;
	private List<SelectItem> opcionesBusquedaFacturas;
	private String campoBusquedaFacturas;
	private String valorBusquedaFacturas;
	private int paginacionBusquedaFacturas;
	// control
	private boolean operacionCancelada;
	private int tipoMensaje; 
	private String mensaje;
	// Perfiles
	private boolean perfilAdministrativo;
	// DEBUG
	private HashMap<String, String> paramsRequest;
	private boolean isDebug;
	private FacturaPagosRem ifzFactura;
	private InfoSesion infoSesion;
		
	public ProvisionesAction() {
		InitialContext ctx = null;
		ValueExpression valExp = null;
		FacesContext fc = null;
		Application app = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilAdministrativo = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
		    this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
		    	this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			ctx = new InitialContext();
			this.ifzProvisiones = (ProvisionesRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//ProvisionesFac!net.giro.cxc.logica.ProvisionesRem");
			this.ifzFacturas = (FacturaRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
            this.ifzFactura = (FacturaPagosRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosFac!net.giro.cxc.logica.FacturaPagosRem"); 
			
            this.ifzProvisiones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion());
            this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Busqueda Facturas
			this.opcionesBusquedaFacturas = new ArrayList<SelectItem>();
			this.opcionesBusquedaFacturas.add(new SelectItem("folioFactura", "Factura"));
			this.opcionesBusquedaFacturas.add(new SelectItem("nombreObra", "Obra"));
			this.opcionesBusquedaFacturas.add(new SelectItem("id", "ID"));
			nuevaBusquedaFacturas();
			
			nuevo();
			this.paginacionProvisiones = 1;
		} catch (Exception e) {
			log.error("\n\nError en Constructor CuentasPorCobrar.ProvisionesAction\n", e); 
		}
	}
	
	public void buscar() {
		try {
			control();
			this.listProvisiones = this.ifzProvisiones.findLikeProperty("id", "", 1000);
			if (this.listProvisiones == null || this.listProvisiones.isEmpty()) {
				control(2, " Busqueda sin resultados");
				return;
			}
			
			Collections.sort(this.listProvisiones, new Comparator<Provisiones>() {
				@Override
				public int compare(Provisiones o1, Provisiones o2) {
					return o2.getId().compareTo(o1.getId());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Provisiones", e); 
		} finally {
			this.paginacionProvisiones = 1;
			this.listProvisiones = (this.listProvisiones != null ? this.listProvisiones : new ArrayList<Provisiones>());
			log.info(this.listProvisiones.size() + " Provisiones encontradas");
		}
	}

	public void nuevo() {
		try {
			control();
			this.idProvision = 0L;
			this.pojoProvision = new Provisiones();
			this.listProvisiones = new ArrayList<Provisiones>();
			this.montoMaximoProvision = 100;
			nuevaBusquedaFacturas();
		} catch (Exception e) {
			control("Ocurrio un problema al intanciar una nueva Provision", e);
		}
	}

	public void editar() {
		try {
			control();
			this.pojoProvision = this.ifzProvisiones.findById(this.idProvision);
			if (this.pojoProvision == null || this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L) {
				control(-1, "No se pudo recuperar la Provision seleccionada.\nNo se puede editar");
				return;
			}
			
			/*this.totalProvision = 0;
			this.totalFactura = 0;
			this.listProvisiones = new ArrayList<Provisiones>();
			this.listProvisiones = this.ifzProvisiones.findByProperty("grupo", this.pojoProvisionMain.getGrupo());
			if (this.listProvisiones != null && ! this.listProvisiones.isEmpty()) {
				for (Provisiones var : this.listProvisiones) {
					this.totalProvision += var.getMonto();
					this.totalFactura += var.getIdFactura().getTotal().doubleValue();
				}
			}*/
		} catch (Exception e) {
			control("Ocurrio un problema al intentar editar la Provision seleccionada", e);
		}
	}

	public void guardar() {
		try {
			control();
			if (! validarProvision())
				return;
			
			if (this.pojoProvision == null || this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar actualizar la Provision seleccionada.");
				return;
			}

			// Guardamos la provision
			this.pojoProvision.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoProvision.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzProvisiones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProvisiones.update(this.pojoProvision);

			// Actualizamos en el listado de provisiones
			for (Provisiones item : this.listProvisiones) {
				if (this.pojoProvision.getId().longValue() == item.getId().longValue()) {
					item = this.pojoProvision;
					break;
				}
			}
			
			provisionar();
			nuevo();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Provision", e);
		}
	}

	public void provisionMensual() {
		MensajeTopic msgTopic = null;
		String comando = "";
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";

		try {
			control();
			msgTopic = new MensajeTopic(TopicEventosCXC.PROVISION_MENSUAL, target, referencia, atributos, this.loginManager.getInfoSesion());
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			control("Ocurrio un problema al intentar ejecutar la Provision Mensual :(\nEvento\n" + comando, e);
		}
	}
	
	public void borrar() {
		try {
			control();
			this.pojoProvision = this.ifzProvisiones.findById(this.idProvision);
			if (this.pojoProvision == null || this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L) {
				log.error("\n\nNo pude recuperar la Provision seleccionada. POJO nulo\n"); 
				control(-1, "No se pudo recuperar la Provision seleccionada.\nNo se puede eliminar");
				return;
			}
			
			if (! evaluaCancelarProvision()) {
				log.error("\n\nNo puedo cancelar la Provision indicada. No permitido por Evaluador\n"); 
				control(-1, "No puedo cancelar la Provision");
				return;
			}
			
			// Cancelamos la provision
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFacturas.cancelarProvision(this.pojoProvision.getIdFactura(), this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			// Borramos el registro de provision indicado
			this.ifzProvisiones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProvisiones.delete(this.pojoProvision);
			// Quitamos el registro del listado
			this.listProvisiones.remove(this.pojoProvision);
			this.pojoProvision = new Provisiones();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar borrar la Provision indicada", e); 
		}
	}
	
	public void borrarDetalle() {
		try {
			control();
			if (this.pojoProvision == null || this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L) {
				log.error("\n\nNo pude recuperar la Provision seleccionada. POJO nulo\n"); 
				control(-1, "No se pudo recuperar la Provision seleccionada.\nNo se puede eliminar");
				return;
			}
			
			if (! evaluaCancelarProvision()) {
				log.error("\n\nNo puedo cancelar la Provision indicada. No permitido por Evaluador\n"); 
				control(-1, "No puedo cancelar la Provision");
				return;
			}
			
			// Cancelamos la provision
			this.ifzFacturas.cancelarProvision(this.pojoProvision.getIdFactura(), this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			// Borramos el registro de provision indicado
			if (this.pojoProvision.getId() != null || this.pojoProvision.getId() <= 0L) {
				this.ifzProvisiones.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzProvisiones.delete(this.pojoProvision);
			}
			
			// Quitamos el registro del listado
			this.listProvisiones.remove(this.pojoProvision);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar quitar la Provision solicitada", e); 
		}
	}
	
	public void agregar() {
		try {
			control();
			if (this.pojoProvision == null || this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L) {
				log.error("\n\nNo pude recuperar la Provision seleccionada. POJO nulo\n"); 
				control(-1, "No se pudo recuperar la Provision seleccionada.\nNo se puede eliminar");
				return;
			}
			
			if (! comprobamosFacturaProvisionada()) 
				return;

			this.pojoProvision.setMonto(this.montoProvision);
			this.pojoProvision.setMontoOriginal(this.montoProvision);
			this.pojoProvision.setObservaciones(this.observaciones);
			this.pojoProvision.setGrupo(this.pojoProvision.getGrupo());
			this.pojoProvision.setEstatus(0);
			this.pojoProvision.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoProvision.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoProvision.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoProvision.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.listProvisiones = (this.listProvisiones != null ? this.listProvisiones : new ArrayList<Provisiones>());
			this.listProvisiones.add(this.pojoProvision);
			totalizar();
			
			this.pojoProvision = new Provisiones();
			this.montoProvision = 0;
			this.montoMaximoProvision = 100;
			this.observaciones = "";
		} catch (Exception e) {
			control("Ocurrio un problema al intentar añadir la Provision indicada", e); 
		}
	}
	
	public void reemplazarProvision() {
		try {
			control();
			for (Provisiones var : this.listProvisiones) {
				if (var.getIdFactura().getId().longValue() == this.pojoProvision.getIdFactura().getId().longValue()) {
					var.setMonto(this.pojoProvision.getMonto());
					var.setObservaciones(this.pojoProvision.getObservaciones());
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
					break;
				}
			}

			totalizar();
			this.pojoProvision = new Provisiones();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar reemplazar la Provision indicada", e);
		}
	}

	public void totalizar() {
		try {
			control();
			if (this.listProvisiones == null || this.listProvisiones.isEmpty()) {
				log.error("\n\nSin detalles para totalizar. Lista de Detalles vacia\n"); 
				return;
			}

			log.info("Totalizando ... ");
						
			log.info("Proceso terminado");
		} catch (Exception e) {
			control("Ocurrio un problema al totalizar las Provisiones", e);
		}
	}
	
	public boolean evaluaCancelarProvision() {
		return true;
	}

	public void provisionRetroactiva() { 
		Provisiones pojoProvision = null;
		List<Factura> listFacturas = null;
	    String MesesRetroactivo = "";
	    int mesActual = 0;
		Calendar cal = null;
		double provision = 0;
		int provisionadas = 0;
		int grupo = 0;
		String observaciones = "";
		Date fechaDesde  = null;
		Date fechaHasta = null;
		BigDecimal pagos = BigDecimal.ZERO;
		
		try {
			cal = Calendar.getInstance();
			MesesRetroactivo = Meses.fromOrdinal(cal.get(Calendar.MONTH)).toString().toUpperCase();
			
			switch (MesesRetroactivo) {
            case "ENERO":
            	     mesActual = 1;
                     break;
            case "FEBRERO":
            	     mesActual = 2;
                     break;
            case "MARZO":
       	             mesActual = 3;
       	         	 break;
            case "ABRIL":
            		mesActual = 4;
                     break;
            case "MAYO":
            		 mesActual = 5;
                     break;
            case "JUNIO":
            	     mesActual = 6;
                     break;
            case "JULIO":
            	 	 mesActual = 7;
                     break;
            case "AGOSTO":
            	 	 mesActual = 8;
                     break;
            case "SEPTIEMBRE":
            	 	 mesActual = 9;
                     break;
            case "OCTUBRE":
            	     mesActual = 10;
                     break;
            case "NOOVIEMBRE":
            	     mesActual = 11;
                     break;
            case "DICIEMBRE":
            	     mesActual = 12;
                     break;
          }
			for(int factor = 0; factor <= mesActual -1; factor++){
				cal.add(Calendar.DAY_OF_MONTH, (cal.get(Calendar.DAY_OF_MONTH) * -1));
				grupo = getGrupo(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
				observaciones = "PROVISION " + Meses.fromOrdinal(cal.get(Calendar.MONTH)).toString().toUpperCase();
				this.ifzFacturas.setInfoSesion(this.infoSesion);
				//del 1 de enero al ultimo dia del mes
				listFacturas = this.ifzFacturas.paraProvisionar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), "id desc"); //.provisionMensual(cal.get(Calendar.MONTH), 0);
				if (listFacturas == null || listFacturas.isEmpty()) {
					log.error("No se encontraron Facturas para Provisionar");
					return;
				}
						
				// Provisionamos Facturas encontradas
			    for (Factura item : listFacturas) {
					// Fecha Inicial
					cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
					fechaDesde = cal.getTime();
					// Fecha Final
					cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					fechaHasta = cal.getTime();
				
					pagos = this.ifzFactura.findLiquidado(item.getId(), fechaDesde, fechaHasta);
					if (item.getTotal().doubleValue() == pagos.doubleValue())
						continue;
				
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
			}

			control(-1, provisionadas + " Facturas Provisionadas");
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar procesar la Provision de Facturas");
		} 
	}

	private boolean validarProvision() {
		if (this.listProvisiones == null || this.listProvisiones.isEmpty()) {
			log.info("\n\nSin Facturas agregadas. Listado vacio\n"); 
			control(-1, "Debe añadir al menos una factura para provisionar");
			return false;
		}
		
		return true;
	}

	private boolean comprobamosFacturaProvisionada() {
		try {
			if (this.montoProvision <= 0) {
				log.info("ERROR 5 - El monto de provision debe ser mayor a cero");
				control(5, "El monto de provision debe ser mayor a cero");
				return false;
			}
			
			if (this.montoProvision > this.montoMaximoProvision) {
				log.info("ERROR 6 - El monto de provision no debe exceder el saldo de la factura");
				control(6, "El monto de provision no debe exceder el saldo de la factura");
				return false;
			}
			
			this.listProvisiones = (this.listProvisiones != null ? this.listProvisiones : new ArrayList<Provisiones>());
			for (Provisiones var : this.listProvisiones) {
				if (var.getIdFactura().getId().longValue() == this.pojoProvision.getIdFactura().getId().longValue()) {
					if (var.getMonto() != this.pojoProvision.getMonto()) {
						log.info("ERROR 3 - La factura indicada ya existe en el listado actual con un monto distinto");
						control(3, "La factura indicada ya existe en el listado actual con un monto distinto");
						return false;
					} else {
						log.info("ERROR 4 - La factura indicada ya existe en el listado actual con el mismo monto");
						control(4, "La factura indicada ya existe en el listado actual con el mismo monto");
						return false;
					}
				}
			}
			
			return true;
		} catch (Exception e) {
			if (this.listProvisiones == null || this.listProvisiones.isEmpty())
				return true;
			control("Ocurrio un problema al intentar verificar la factura en el listado actual", e);
			return false;
		}
	}
	
	private void provisionar() {
		Respuesta respuesta = null;
		
		try {
			control();
			// Enviamos mensaje a cola de transacciones
			log.info("Provisionando ... ");
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzFacturas.provisionar(this.pojoProvision.getIdFactura(), this.pojoProvision.getMonto(), this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al intentar Procesar la Provision para la factura: " + this.pojoProvision.getIdFactura().getFolioFactura());
				return;
			}
			log.info("Transaccion enviada");
			
			/*if (this.pojoProvision == null || this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L) {
				log.info("NO se pudo enviar la Transaccion. No selecciono registro de Provision");
				return;
			}
			
			for (Provisiones var : this.listProvisiones) {
				respuesta = this.ifzFacturas.provisionar(var.getIdFactura(), var.getMonto(), this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				if (respuesta.getErrores().getCodigoError() != 0L)
					msgLog += ((! "".equals(msgLog.trim())) ? "\n" : "") + var.getIdFactura().getFolioFactura();
			}
			
			if ("".equals(msgLog.trim())) {
				log.info("No se pudo Ejecutar la Transaccion de Provision para las siguientes facturas: " + msgLog);
				control(-1, "Ocurrio un problema al intentar Procesar la Provision para las siguientes facturas: " + msgLog);
				return;
			}
			log.info("Transaccion enviada");*/
		} catch (Exception e) {
			control("Ocurrio un problema al ejecutar el evento para Provision", e);
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
	
	private void control(boolean incompleta, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacionCancelada = incompleta;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "Ocurrio un problema no controlado al realizar la operacion");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	// ------------------------------------------------------------------------------------
	// Facturas
	// ------------------------------------------------------------------------------------

	public void nuevaBusquedaFacturas() {
		try {
			control();
			this.campoBusquedaFacturas = this.opcionesBusquedaFacturas.get(0).getValue().toString();
			this.valorBusquedaFacturas = "";
			this.paginacionBusquedaFacturas = 1;
			
			this.listFacturas = new ArrayList<Factura>();
			this.pojoFacturaSeleccionada = null;
			this.montoProvision = 0;
			this.observaciones = "";
		} catch (Exception e) {
			control("Ocurrio un problema al preparar la busqueda de Facturas", e);
		}
	}
	
	public void buscarFacturas() {
		try {
			control();
			if (this.campoBusquedaFacturas == null || "".equals(this.campoBusquedaFacturas.trim()))
				this.campoBusquedaFacturas = this.opcionesBusquedaFacturas.get(0).getValue().toString();

			this.listFacturas = this.ifzFacturas.findLikePropertySinProvision(this.campoBusquedaFacturas, this.valorBusquedaFacturas, 0, 0);
			if (this.listFacturas == null || this.listFacturas.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar las Facturas", e); 
		} finally {
			this.paginacionBusquedaFacturas = 1;
			this.listFacturas = (this.listFacturas != null ? this.listFacturas : new ArrayList<Factura>());
			log.info(this.listFacturas.size() + " Facturas encontradas");
		}
	}
	
	public void seleccionarFactura() {
		BigDecimal saldo = BigDecimal.ZERO;
		
		try {
			control();
			log.info("Seleccionando factura ... ");
			if (this.pojoFacturaSeleccionada == null || this.pojoFacturaSeleccionada.getId() == null) {
				log.error("\n\nNo pude recuperar la Factura seleccionada. POJO nulo\n"); 
				control(-1, "Debe seleccionar una Factura");
				return;
			}
			
			saldo = this.ifzFacturas.calcularSaldoAFecha(this.pojoFacturaSeleccionada.getId(), this.pojoFacturaSeleccionada.getFechaEmision());
			if (saldo != null && saldo.doubleValue() == 0) {
				control(-1, "La Factura seleccionada no se puede Provisionar.\nLa Factura ya ha sido Pagada");
				return;
			}

			this.montoProvision = 0;
			this.montoMaximoProvision = this.pojoFacturaSeleccionada.getTotal().doubleValue();
			if (saldo != null && saldo.doubleValue() > 0) {
				this.montoMaximoProvision = saldo.doubleValue();
				this.montoProvision = saldo.doubleValue();
			} 
			
			this.pojoProvision = new Provisiones();
			this.pojoProvision.setIdFactura(this.pojoFacturaSeleccionada);
			this.pojoProvision.setMonto(this.montoProvision);
			log.info("Proceso terminado");
			nuevaBusquedaFacturas();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la Factura seleccionada", e);
		} 
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
	
	// ------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------
	
	public long getProvisionId() {
		if (this.pojoProvision != null && this.pojoProvision.getId() != null && this.pojoProvision.getId() > 0L)
			return this.pojoProvision.getId();
		return 0;
	}
	
	public void setProvisionId(long value) {}
	
	public String getEncabezado() {
		if (this.pojoProvision != null && this.pojoProvision.getId() != null && this.pojoProvision.getId() > 0L) 
			return this.pojoProvision.getId().toString();
		return "";
	}
	
	public void setEncabezado(String value) {}
	
	public String getFacturaDescripcion() {
		if (this.pojoProvision != null && this.pojoProvision.getIdFactura() != null && this.pojoProvision.getIdFactura().getId() != null) 
			return this.pojoProvision.getIdFactura().getId() + " - " + this.pojoProvision.getIdFactura().getFolioFactura();
		return "";
	}
	
	public void setFacturaDescripcion(String value) {}

	public boolean getOperacion() {
		return operacionCancelada;
	}

	public void setOperacion(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
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

	public int getPaginacionProvisiones() {
		return paginacionProvisiones;
	}

	public void setPaginacionProvisiones(int paginacionProvisiones) {
		this.paginacionProvisiones = paginacionProvisiones;
	}

	/*public List<Provisiones> getListProvisionesAgrupados() {
		return listProvisionesAgrupados;
	}

	public void setListProvisionesAgrupados(List<Provisiones> listProvisionesAgrupados) {
		this.listProvisionesAgrupados = listProvisionesAgrupados;
	}

	public int getPaginacionDetalle() {
		return paginacionDetalle;
	}

	public void setPaginacionDetalle(int paginacionDetalle) {
		this.paginacionDetalle = paginacionDetalle;
	}*/
	
	public List<Provisiones> getListProvisiones() {
		return listProvisiones;
	}

	public void setListProvisiones(List<Provisiones> listProvisiones) {
		this.listProvisiones = listProvisiones;
	}

	public long getIdProvision() {
		return idProvision;
	}

	public void setIdProvision(long idProvision) {
		this.idProvision = idProvision;
	}

	/*public Provisiones getPojoProvision() {
		return pojoProvision;
	}

	public void setPojoProvision(Provisiones pojoProvision) {
		this.pojoProvision = pojoProvision;
	}*/

	public boolean getEsAdministrativo() {
		return perfilAdministrativo;
	}
	
	public void setEsAdministrativo(boolean perfilAdministrativo) {
		this.perfilAdministrativo = perfilAdministrativo;
	}

	public boolean isDebugging() {
		return isDebug;
	}
	
	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public List<Factura> getListFacturas() {
		return listFacturas;
	}

	public void setListFacturas(List<Factura> listFacturas) {
		this.listFacturas = listFacturas;
	}

	public Factura getPojoFacturaSeleccionada() {
		return pojoFacturaSeleccionada;
	}
	
	public void setPojoFacturaSeleccionada(Factura pojoFacturaSeleccionada) {
		this.pojoFacturaSeleccionada = pojoFacturaSeleccionada;
	}
	
	public List<SelectItem> getOpcionesBusquedaFacturas() {
		return opcionesBusquedaFacturas;
	}

	public void setOpcionesBusquedaFacturas(
			List<SelectItem> opcionesBusquedaFacturas) {
		this.opcionesBusquedaFacturas = opcionesBusquedaFacturas;
	}

	public String getCampoBusquedaFacturas() {
		return campoBusquedaFacturas;
	}

	public void setCampoBusquedaFacturas(String campoBusquedaFacturas) {
		this.campoBusquedaFacturas = campoBusquedaFacturas;
	}

	public String getValorBusquedaFacturas() {
		return valorBusquedaFacturas;
	}

	public void setValorBusquedaFacturas(String valorBusquedaFacturas) {
		this.valorBusquedaFacturas = valorBusquedaFacturas;
	}

	public int getPaginacionBusquedaFacturas() {
		return paginacionBusquedaFacturas;
	}

	public void setPaginacionBusquedaFacturas(int paginacionBusquedaFacturas) {
		this.paginacionBusquedaFacturas = paginacionBusquedaFacturas;
	}

	public double getMontoProvision() {
		return montoProvision;
	}

	public void setMontoProvision(double montoProvision) {
		this.montoProvision = montoProvision;
	}

	public double getTotalProvision() {
		if (this.pojoProvision != null && this.pojoProvision.getIdFactura() != null && this.pojoProvision.getIdFactura().getId() != null) 
			return this.pojoProvision.getMonto();
		return 0;
	}
	
	public void setTotalProvision( double TotalProvision){}
	
	public String getObservaciones() {
		if (this.pojoProvision != null && this.pojoProvision.getIdFactura() != null && this.pojoProvision.getIdFactura().getId() != null) 
			return this.pojoProvision.getObservaciones();
		return "";
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public boolean isProvisionadaPreviamente() {
		return provisionadaPreviamente;
	}
	
	public void setProvisionadaPreviamente(boolean provisionadaPreviamente) {
		this.provisionadaPreviamente = provisionadaPreviamente;
	}
	
	public double getMontoMaximoProvision() {
		return montoMaximoProvision;
	}
	
	public void setMontoMaximoProvision(double montoMaximoProvision) {
		this.montoMaximoProvision = montoMaximoProvision;
	}
	
	public double getTotalFactura() {
		if (this.pojoProvision != null && this.pojoProvision.getIdFactura() != null && this.pojoProvision.getIdFactura().getId() != null) 
			return this.pojoProvision.getIdFactura().getTotal().doubleValue();
		return 0;
	}
	
	public String getCliente() {
		if (this.pojoProvision != null && this.pojoProvision.getIdFactura() != null && this.pojoProvision.getIdFactura().getId() != null) 
			return this.pojoProvision.getIdFactura().getCliente();
		return "";
	}
	
	public void setCliente() {}
	
	public String getNombreObra() {
		if (this.pojoProvision != null && this.pojoProvision.getIdFactura() != null && this.pojoProvision.getIdFactura().getId() != null) 
			return this.pojoProvision.getIdFactura().getNombreObra();
	    return "";
	}
	
	public void setNombreObra(String nombreObra) {}
	
	public Date getFechaCreacion() {
		if (this.pojoProvision != null && this.pojoProvision.getIdFactura() != null && this.pojoProvision.getIdFactura().getId() != null) 
			return this.pojoProvision.getIdFactura().getFechaEmision();
		return null;
	}

	public void setFechaCreacion(Date fechaCreacion) {}
	
	public Date getFechaModificacion() {
		if (this.pojoProvision != null && this.pojoProvision.getIdFactura() != null && this.pojoProvision.getIdFactura().getId() != null) 
			return this.pojoProvision.getFechaCreacion();
	return null;
	}

	public void setFechaModificacion(Date fechaModificacion) {}
	
	public String getFacturaId() {
		if (this.pojoProvision != null && this.pojoProvision.getIdFactura() != null && this.pojoProvision.getIdFactura().getId() != null) 
			return this.pojoProvision.getIdFactura().getUuid(); 
		return "";
	}

	public void setFacturaId(String value) {}
}


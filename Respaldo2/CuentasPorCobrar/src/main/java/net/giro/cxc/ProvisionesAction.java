package net.giro.cxc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.Provisiones;
import net.giro.cxc.logica.FacturaRem;
import net.giro.cxc.logica.ProvisionesRem;
import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="provAction")
public class ProvisionesAction implements Serializable {
	private static Logger log = Logger.getLogger(ProvisionesAction.class);
	private static final long serialVersionUID = 1L;
	private LoginManager loginManager;
	private InitialContext ctx;

	private ProvisionesRem ifzProvisiones;
	private List<Provisiones> listProvisionesMain;
	private List<Provisiones> listProvisiones;
	private Provisiones pojoProvisionMain;
	private Provisiones pojoProvisionBorrar;
	private long usuarioId;
	private boolean perfilAdministrativo;
	private boolean operacionCancelada;
	private int tipoMensaje; 
	private String mensaje;
	private int paginacionDetalle;
	// Agregar factura
	private Provisiones pojoProvision;
	private double montoMaximoProvision;
	private double montoProvision;
	private double totalProvision;
	private double totalFactura;
	private String observaciones;
	private boolean provisionadaPreviamente;
	// Busqueda principal
	private int paginacionBusqueda;
	// Busqueda Facturas
	private FacturaRem ifzFacturas;
	private List<Factura> listFacturas;
	private FacturaExt pojoFactura;
	private Factura pojoFacturaSeleccionada;
	private List<SelectItem> opcionesBusquedaFacturas;
	private String campoBusquedaFacturas;
	private String valorBusquedaFacturas;
	private int paginacionBusquedaFacturas;
	// DEBUG
	private HashMap<String, String> paramsRequest;
	private boolean isDebug;
	
	
	public ProvisionesAction() {
		Map<String,String> params = null;
		ValueExpression valExp = null;
		FacesContext fc = null;
		Application app = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilAdministrativo = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			params = fc.getExternalContext().getRequestParameterMap();
		    this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : params.entrySet())
		    	this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			this.ctx = new InitialContext();
			this.ifzProvisiones = (ProvisionesRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//ProvisionesFac!net.giro.cxc.logica.ProvisionesRem");
			this.ifzFacturas = (FacturaRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");

			this.ifzProvisiones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion());

			this.montoMaximoProvision = 100;
			// Busqueda principal
			this.paginacionBusqueda = 1;
			// Busqueda Facturas
			this.opcionesBusquedaFacturas = new ArrayList<SelectItem>();
			this.opcionesBusquedaFacturas.add(new SelectItem("folioFactura", "Factura"));
			this.opcionesBusquedaFacturas.add(new SelectItem("nombreObra", "Obra"));
			this.opcionesBusquedaFacturas.add(new SelectItem("id", "ID"));
			this.campoBusquedaFacturas = this.opcionesBusquedaFacturas.get(0).getValue().toString();
			this.valorBusquedaFacturas = "";
			this.paginacionBusquedaFacturas = 1;

			nuevo();
		} catch (Exception e) {
			log.error("\n\nError en Constructor CuentasPorCobrar.ProvisionesAction\n", e); 
		}
	}
	
	
	public void buscar() {
		try {
			control();
			this.paginacionBusqueda = 1;
			if (this.listProvisiones == null)
				this.listProvisiones = new ArrayList<Provisiones>();
			this.listProvisiones.clear();
			
			this.listProvisiones = this.ifzProvisiones.findLikeProperty("id", "", 1000);
			if (this.listProvisiones.isEmpty()) {
				log.info("ERROR 2 - Busqueda sin resultados.");
				control(2);
				return;
			}
			
			Collections.sort(this.listProvisiones, new Comparator<Provisiones>() {
				@Override
				public int compare(Provisiones o1, Provisiones o2) {
					return o2.getId().compareTo(o1.getId());
				}
			});
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.buscar()\n", e); 
			control(true);
		} finally {
			if (this.listProvisiones == null)
				this.listProvisiones = new ArrayList<Provisiones>();
			log.info(this.listProvisiones.size() + " Provisiones encontradas");
		}
	}

	public void nuevo() {
		try {
			control();
			if (this.listProvisiones == null)
				this.listProvisiones = new ArrayList<Provisiones>();
			this.listProvisiones.clear();
			
			this.montoMaximoProvision = 100;
			this.pojoProvisionMain = new Provisiones();
			this.pojoProvision = new Provisiones();
			this.pojoProvisionBorrar = null;
			this.pojoFactura = null;
			
			nuevaBusquedaFacturas();
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.nuevo()\n", e); 
			control(true);
		}
	}

	public void editar() {
		try {
			control();
			if (this.pojoProvisionMain == null) {
				log.error("\n\nNo pude recuperar la Provision seleccionada. POJO nulo\n"); 
				control("No se pudo recuperar la Provision seleccionada.\nNo se puede editar");
				return;
			}

			this.pojoFactura = null;
			this.totalProvision = 0;
			this.totalFactura = 0;
			this.listProvisiones = this.ifzProvisiones.findByProperty("grupo", this.pojoProvisionMain.getGrupo());
			if (this.listProvisiones != null && ! this.listProvisiones.isEmpty()) {
				for (Provisiones var : this.listProvisiones) {
					this.totalProvision += var.getMonto();
					this.totalFactura += var.getIdFactura().getTotal().doubleValue();
				}
			}
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.editar()\n", e); 
			control(true);
		}
	}

	public void guardar() {
		try {
			control();
			if (! validarProvision())
				return;
			
			for (Provisiones item : this.listProvisiones) {
				item.setModificadoPor(this.usuarioId);
				item.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (item.getId() == null || item.getId() <= 0L) {
					item.setCreadoPor(this.usuarioId);
					item.setFechaCreacion(Calendar.getInstance().getTime());
				}
			}
			
			// Guardamos el listado de provisiones
			this.listProvisiones = this.ifzProvisiones.save(this.listProvisiones);
			if (this.listProvisiones != null && ! this.listProvisiones.isEmpty()) {
				for (Provisiones var : this.listProvisiones) {
					this.ifzFacturas.facturaProvisionada(var.getIdFactura(), this.usuarioId);
				}
			}
			
			provisionar();
			nuevo();
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.guardar()\n", e); 
			control(true);
		}
	}

	public void borrar() {
		try {
			control();
			if (this.pojoProvisionMain == null) {
				log.error("\n\nNo pude recuperar la Provision seleccionada. POJO nulo\n"); 
				control("No se pudo recuperar la Provision seleccionada.\nNo se puede eliminar");
				return;
			}
			
			if (! evaluaCancelarProvision()) {
				log.error("\n\nNo puedo cancelar la Provision indicada. No permitido por Evaluador\n"); 
				control("No puedo cancelar la Provision");
				return;
			}
			
			// Cancelamos la provision
			this.ifzFacturas.cancelarProvision(this.pojoProvisionMain.getIdFactura(), this.usuarioId);
			// Borramos el registro de provision indicado
			this.ifzProvisiones.delete(this.pojoProvisionMain);
			// Quitamos el registro del listado
			this.listProvisionesMain.remove(this.pojoProvisionMain);
			
			this.pojoProvisionMain = new Provisiones();
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.borrar()\n", e); 
			control(true);
		}
	}
	
	public void borrarDetalle() {
		try {
			control();
			if (this.pojoProvision == null) {
				log.error("\n\nNo pude recuperar la Provision seleccionada. POJO nulo\n"); 
				control("No se pudo recuperar la Provision seleccionada.\nNo se puede eliminar");
				return;
			}
			
			if (! evaluaCancelarProvision()) {
				log.error("\n\nNo puedo cancelar la Provision indicada. No permitido por Evaluador\n"); 
				control("No puedo cancelar la Provision");
				return;
			}
			
			// Cancelamos la provision
			this.ifzFacturas.cancelarProvision(this.pojoProvision.getIdFactura(), this.usuarioId);
			// Borramos el registro de provision indicado
			this.ifzProvisiones.delete(this.pojoProvision);
			// Quitamos el registro del listado
			this.listProvisiones.remove(this.pojoProvision);
			
			this.pojoProvision = null;
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.borrar()\n", e); 
			control(true);
		}
	}
	
	public void agregar() {
		try {
			control();
			if (this.pojoProvision == null) {
				log.error("\n\nNo pude recuperar la Provision seleccionada. POJO nulo\n"); 
				control("No se pudo recuperar la Provision seleccionada.\nNo se puede eliminar");
				return;
			}
			
			if (! comprobamosFacturaProvisionada()) 
				return;

			this.pojoProvision.setMonto(this.montoProvision);
			this.pojoProvision.setMontoOriginal(this.montoProvision);
			this.pojoProvision.setObservaciones(this.observaciones);
			this.pojoProvision.setGrupo(this.pojoProvisionMain.getGrupo());
			this.pojoProvision.setEstatus(0);
			this.pojoProvision.setCreadoPor(this.usuarioId);
			this.pojoProvision.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoProvision.setModificadoPor(this.usuarioId);
			this.pojoProvision.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.listProvisiones == null)
				this.listProvisiones = new ArrayList<Provisiones>();
			this.listProvisiones.add(this.pojoProvision);
			totalizar();
			
			this.pojoProvision = new Provisiones();
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.borrar()\n", e); 
			control(true);
		}
	}
	
	private boolean validarProvision() {
		if (this.listProvisiones == null || this.listProvisiones.isEmpty()) {
			log.info("\n\nSin Facturas agregadas. Listado vacio\n"); 
			control("Debe añadir al menos una factura para provisionar");
			return false;
		}
		
		return true;
	}

	private boolean comprobamosFacturaProvisionada() {
		try {
			if (this.montoProvision <= 0) {
				log.info("ERROR 5 - El monto de provision debe ser mayor a cero");
				control(5);
				return false;
			}
			
			if (this.montoProvision > this.montoMaximoProvision) {
				log.info("ERROR 6 - El monto de provision no debe exceder el saldo de la factura");
				control(6);
				return false;
			}
			
			if (this.listProvisiones == null)
				this.listProvisiones = new ArrayList<Provisiones>();
			
			for (Provisiones var : this.listProvisiones) {
				if (var.getIdFactura().getId().longValue() == this.pojoProvision.getIdFactura().getId().longValue()) {
					if (var.getMonto() != this.pojoProvision.getMonto()) {
						log.info("ERROR 3 - La factura indicada ya existe en el listado actual con un monto distinto");
						control(3);
						return false;
					} else {
						log.info("ERROR 4 - La factura indicada ya existe en el listado actual con el mismo monto");
						control(4);
						return false;
					}
				}
			}
			
			return true;
		} catch (Exception e) {
			if (this.listProvisiones == null || this.listProvisiones.isEmpty())
				return true;
			
			log.error("Ocurrio un problema al intentar comprobar la factura seleccionada en el listado", e);
			control("Ocurrio un problema al intentar verificar la factura en el listado actual");
			return false;
		}
	}
	
	public void reemplazarProvision() {
		try {
			control();
			for (Provisiones var : this.listProvisiones) {
				if (var.getIdFactura().getId().longValue() == this.pojoProvision.getIdFactura().getId().longValue()) {
					var.setMonto(this.pojoProvision.getMonto());
					var.setObservaciones(this.pojoProvision.getObservaciones());
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					break;
				}
			}

			totalizar();
			this.pojoProvision = new Provisiones();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar reemplazar la Provision indicada", e);
			control("Ocurrio un problema al intentar reemplazar la Provision indicada");
		}
	}
	
	private void provisionar() {
		Respuesta respuesta = null;
		String msgLog = "";
		
		try {
			control();
			// Enviamos mensaje a cola de transacciones
			log.info("Provisionando ... ");
			if (this.pojoProvisionMain == null || this.pojoProvisionMain.getId() == null || this.pojoProvisionMain.getId() <= 0L) {
				log.info("NO se pudo enviar la Transaccion. No selecciono registro de Provision");
				return;
			}
			
			for (Provisiones var : this.listProvisiones) {
				respuesta = this.ifzFacturas.provisionar(var.getIdFactura(), var.getMonto(), this.usuarioId);
				if (respuesta.getErrores().getCodigoError() != 0L)
					msgLog += ((! "".equals(msgLog.trim())) ? "\n" : "") + var.getIdFactura().getFolioFactura();
			}
			
			if ("".equals(msgLog.trim())) {
				log.info("No se pudo Ejecutar la Transaccion de Provision para las siguientes facturas: " + msgLog);
				control("Ocurrio un problema al intentar Procesar la Provision para las siguientes facturas: " + msgLog);
				return;
			}
			log.info("Transaccion envianda");
		} catch (Exception e) {
			log.error("Error en mensajeTransaccion", e);
			control(true);
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
			this.totalProvision = 0;
			for (Provisiones var : this.listProvisiones)
				this.totalProvision += var.getMonto();
			log.info("Proceso terminado");
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.totalizar()\n", e); 
			control(true);
		}
	}
	
	public boolean evaluaCancelarProvision() {
		return true;
	}
	
	public void control() {
		control(false, 0, "");
	}
	
	public void control(boolean operacionCancelada) {
		if (! operacionCancelada) {
			control();
			return;
		}
		
		control(true, 1, "");
	}
	
	public void control(int tipoMensaje) {
		if (tipoMensaje == 0) {
			control();
			return;
		}
		
		control(true, tipoMensaje, "");
	}
	
	public void control(String mensaje) {
		if (mensaje == null || "".equals(mensaje)) {
			control();
			return;
		}
		
		control(true, -1, mensaje);
	}
	
	public void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje;
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
			if (this.listFacturas == null)
				this.listFacturas = new ArrayList<Factura>();
			this.listFacturas.clear();
			
			this.pojoProvision = new Provisiones();
			this.pojoFacturaSeleccionada = null;
			this.montoProvision = 0;
			this.observaciones = "";
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.nuevaBusquedaFacturas()\n", e); 
			control(true);
		}
	}
	
	public void buscarFacturas() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		try {
			control();
			if (this.campoBusquedaFacturas == null || "".equals(this.campoBusquedaFacturas.trim()))
				this.campoBusquedaFacturas = this.opcionesBusquedaFacturas.get(0).getValue().toString();
			
			params.put(this.campoBusquedaFacturas, this.valorBusquedaFacturas);
			params.put("provisionada", "0");
			params.put("estatus", "1");

			this.ifzFacturas.orderBy("id desc");
			this.listFacturas = this.ifzFacturas.findLikeProperties(params);
			if (this.listFacturas == null || this.listFacturas.isEmpty()) {
				log.info("2 - Busqueda sin resultados. Buscando por " + this.campoBusquedaFacturas + ": " + this.valorBusquedaFacturas);
				control(2);
			}
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.buscarFacturas()\n", e); 
			control(true);
		} finally {
			if (this.listFacturas == null)
				this.listFacturas = new ArrayList<Factura>();
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
				control("Debe seleccionar una Factura");
				return;
			}
			
			saldo = this.ifzFacturas.findSaldoFactura(this.pojoFacturaSeleccionada);
			if (saldo != null && saldo.doubleValue() > 0) {
				this.montoMaximoProvision = saldo.doubleValue();
				this.montoProvision = saldo.doubleValue();
			} else {
				this.montoMaximoProvision = this.pojoFacturaSeleccionada.getTotal().doubleValue();
				this.montoProvision = 0;
			}
			
			this.pojoProvision = new Provisiones();
			this.pojoProvision.setIdFactura(this.pojoFacturaSeleccionada);
			log.info("Proceso terminado");
			nuevaBusquedaFacturas();
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorCobrar.ProvisionesAction.seleccionarFactura()\n", e); 
			control(true);
		} 
	}

	// ------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------
	
	public String getEncabezado() {
		if (this.pojoProvisionMain != null && this.pojoProvisionMain.getId() != null && this.pojoProvisionMain.getId() > 0L) 
			return this.pojoProvisionMain.getId().toString();
		return "";
	}
	
	public void setEncabezado(String value) {}
	
	public String getFacturaDescripcion() {
		if (this.pojoFactura != null && this.pojoFactura.getId() != null) 
			return this.pojoFactura.getId() + " - " + this.pojoFactura.getFolioFactura() + " - " + this.pojoFactura.getIdObra().getClienteNombre();
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

	public int getPaginacionBusqueda() {
		return paginacionBusqueda;
	}

	public void setPaginacionBusqueda(int paginacionBusqueda) {
		this.paginacionBusqueda = paginacionBusqueda;
	}

	public List<Provisiones> getListProvisiones() {
		return listProvisiones;
	}

	public void setListProvisiones(List<Provisiones> listProvisiones) {
		this.listProvisiones = listProvisiones;
	}

	public Provisiones getPojoProvisionMain() {
		return pojoProvisionMain;
	}

	public void setPojoProvisionMain(Provisiones pojoProvision) {
		this.pojoProvisionMain = pojoProvision;
	}

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

	public int getPaginacionDetalle() {
		return paginacionDetalle;
	}

	public void setPaginacionDetalle(int paginacionDetalle) {
		this.paginacionDetalle = paginacionDetalle;
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
	
	public FacturaExt getPojoFactura() {
		return pojoFactura;
	}

	public void setPojoFactura(FacturaExt pojoFactura) {
		this.pojoFactura = pojoFactura;
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

	public List<Provisiones> getListProvisionesMain() {
		return listProvisionesMain;
	}

	public void setListProvisionesMain(List<Provisiones> listProvisionesMain) {
		this.listProvisionesMain = listProvisionesMain;
	}

	public double getTotalProvision() {
		return totalProvision;
	}

	public void setTotalProvision(double totalProvision) {
		this.totalProvision = totalProvision;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Provisiones getPojoProvisionBorrar() {
		return pojoProvisionBorrar;
	}

	public void setPojoProvisionBorrar(Provisiones pojoProvisionBorrar) {
		this.pojoProvisionBorrar = pojoProvisionBorrar;
	}

	public Provisiones getPojoProvision() {
		return pojoProvision;
	}

	public void setPojoProvision(Provisiones pojoProvision) {
		this.pojoProvision = pojoProvision;
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
		return totalFactura;
	}
	


	public void setTotalFactura(double totalFactura) {
		this.totalFactura = totalFactura;
	}
}

package net.giro.cxc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.cxc.logica.FacturaPagosRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CatBancos;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.logica.CatBancosRem;
import net.giro.tyg.logica.CtasBancoRem;
import net.giro.tyg.logica.FormasPagosRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="facPagosAction")
public class FacturaPagosAction implements Serializable, Runnable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FacturaPagosAction.class);
	private InitialContext ctx;
	// Interfaces
	private FacturaPagosRem 	ifzFacPagos;
	private ConGrupoValoresRem 	ifzGpoVal;
	private FormasPagosRem		ifzFormasPagos;
	private CtasBancoRem 		ifzCtas;
	private CatBancosRem		ifzBancos;
	// Listas
	private List<FacturaPagosExt> listFacPagos;
	private List<FormasPagos> listFormasPago;
	private List<CtasBanco> listCuentas;
	private List<CatBancos> listBancos;
	// Listas auxiliares
	private List<SelectItem> listFormasPagoItems;
	private List<SelectItem> listCuentasItems;
	private List<SelectItem> listBancosItems;
	// POJO's
	private FacturaPagosExt pojoFacPago;
	private ConGrupoValores pojoGpoImpuestos;
	// Busqueda principal
	private List<SelectItem> tipoBusqueda;	
	private String valBusqueda;
	private String campoBusqueda;
	// Busqueda facturas
	private FacturaRem ifzFacturas;
	private List<Factura> listFacturas;
	private Factura pojoFacturaSeleccionada;
	private List<SelectItem> tipoBusquedaFacturas;	
	private String valBusquedaFacturas;
	private String campoBusquedaFacturas;
	// Variables de operacion
	private String resOperacion;
	private int tipoMensaje;
	private int numPagina;
	private int numPaginaFacturas;
    private long usuarioId;
    private boolean band;
    private Long idFacturaToDelete;
    // Variables del pago
    private BigDecimal facturaSaldo;
	// DEBUG
	private boolean isDebug;
	private Map<String,String> paramsRequest;
    
    
	public FacturaPagosAction() {
		FacesContext fc = null;
		Application app = null;
		LoginManager loginManager = null;
		Map<String,String> params = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = loginManager.getUsuarioResponsabilidad().getUsuario().getId();

			params = fc.getExternalContext().getRequestParameterMap();
		    if (this.paramsRequest == null)
		    	this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : params.entrySet())
		    	this.paramsRequest.put(item.getKey(), item.getValue());
		    // Comprobamos si requerimos levantar la variable DEBUG
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			this.ctx = new InitialContext();
			this.ifzFacturas 	= (FacturaRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacPagos 	= (FacturaPagosRem) 	this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosFac!net.giro.cxc.logica.FacturaPagosRem");
			this.ifzGpoVal 		= (ConGrupoValoresRem) 	this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzFormasPagos = (FormasPagosRem)		this.ctx.lookup("ejb:/Logica_TYG//FormasPagosFac!net.giro.tyg.logica.FormasPagosRem");
			this.ifzCtas 		= (CtasBancoRem)		this.ctx.lookup("ejb:/Logica_TYG//CtasBancoFac!net.giro.tyg.logica.CtasBancoRem");
			this.ifzBancos		= (CatBancosRem)		this.ctx.lookup("ejb:/Logica_TYG//CatBancosFac!net.giro.tyg.logica.CatBancosRem");
			//this.ifzConValores	= (ConValoresRem) 	 	this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
			this.ifzFacturas.showSystemOuts(false);
			this.ifzFacPagos.showSystemOuts(false);
			
			this.listFacPagos = new ArrayList<FacturaPagosExt>();
			this.listFormasPago = new ArrayList<FormasPagos>();
			
			// Busqueda principal
			this.tipoBusqueda = new ArrayList<SelectItem>();
			this.tipoBusqueda.add(new SelectItem("fac.folioFactura", "Factura"));
			this.tipoBusqueda.add(new SelectItem("fac.cliente", "Cliente"));
			this.tipoBusqueda.add(new SelectItem("model.id", "ID"));
			this.campoBusqueda = this.tipoBusqueda.get(0).getValue().toString();
			this.valBusqueda = "";
			
			// Busqueda facturas
			this.tipoBusquedaFacturas = new ArrayList<SelectItem>();
			this.tipoBusquedaFacturas.add(new SelectItem("folioFactura", "Folio"));
			this.tipoBusquedaFacturas.add(new SelectItem("cliente", "Cliente"));
			this.tipoBusquedaFacturas.add(new SelectItem("id", "ID"));
			this.valBusquedaFacturas = this.tipoBusquedaFacturas.get(0).getValue().toString();
			this.campoBusquedaFacturas = "";
			
			this.listFormasPagoItems = new ArrayList<SelectItem>();
		    this.listCuentasItems = new ArrayList<SelectItem>();
		    this.listBancosItems = new ArrayList<SelectItem>();

			this.facturaSaldo = new BigDecimal(0L);
			this.pojoFacPago = new FacturaPagosExt();
			this.pojoFacPago.setIdFactura(new FacturaExt());
			this.pojoFacPago.setFecha(Calendar.getInstance().getTime());
			this.pojoFacPago.setMonto(new BigDecimal(0L));
			this.pojoFacPago.setObservaciones("");
			
			// IMPUESTOS
			// ---------------------------------------------------------------------------------------------------------
			this.pojoGpoImpuestos = this.ifzGpoVal.findByName("SYS_IMPTOS");
			if (this.pojoGpoImpuestos == null || this.pojoGpoImpuestos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
			
			// Cargamos datos 
			fillLists();

			this.numPagina = 1;
			this.numPaginaFacturas = 1;
			control();
		} catch (Exception e) {
			log.error("Error en constructor FacturaPagosAction", e);
			control(true);
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if (this.campoBusqueda == null || "".equals(this.campoBusqueda))
				this.campoBusqueda = this.tipoBusqueda.get(0).getValue().toString();
			
			this.listFacPagos = this.ifzFacPagos.findLikePropertyExt(this.campoBusqueda, this.valBusqueda);
			if (this.listFacPagos.isEmpty()) {
				log.info("Error 2: Busqueda sin resultados");
				control(2);
				return;
			}
		} catch(Exception e) {
			log.error("ERROR INTERNO: Ocurrio un problema al realizar la busqueda de Pagos de Facturas", e);
			control("Ocurrio un problema al realizar la busqueda de Pagos de Facturas");
		}
	}
	
	public void nuevo() {
		try{
			control();
			this.facturaSaldo = BigDecimal.ZERO;
			this.pojoFacPago = new FacturaPagosExt();
			this.pojoFacPago.setFecha(Calendar.getInstance().getTime());
			this.pojoFacPago.setMonto(new BigDecimal(0L));
			this.pojoFacPago.setObservaciones("");
			
			saldo();
			if (this.listFacturas != null)
				this.listFacturas.clear();
		} catch(Exception e) {
			log.error("ERROR INTERNO: Ocurrio un problema al intentar generar un nuevo pago.", e);
			control("Ocurrio un problema al intentar generar un nuevo pago.");
		}
	}
		
	public void editar() {
		try {
			control();
			log.info("EDITAR :: " + this.pojoFacPago.getId());
			saldo();
			
			// recuperamos los impuestos
			/*this.listImpuestosGrid.clear();
			this.listImpuestosGridEliminados.clear();*/
		} catch (Exception e) {
			log.error("ERROR INTERNO: Ocurrio un problema al recuperar el Pago de Factura para editar.", e);
			control("Ocurrio un problema al recuperar el Pago de Factura para editar.");
		}
	}
		
	public void eliminar(){
		try {
			control();
			// recorremos la lista
			for(FacturaPagosExt var : this.listFacPagos){
				if(var.getId() == this.idFacturaToDelete) {
					// Actualizamos los datos del pojo en la lista
					var.setEstatus(1);
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Actualizamos en la BD
					this.ifzFacPagos.update(var);
					break;
				}
			}
		} catch (Exception e) {
			log.error("ERROR INTERNO: Ocurrio un problema al intentar eliminar el Pago de Factura.", e);
			control("Ocurrio un problema al intentar eliminar el Pago de Factura.");
		}
	}
		
	public void guardar() {
		try {
			control();
			// Validaciones
			if (this.pojoFacPago.getIdFactura() == null) {
				log.info("ERROR -1 - FACTURA NULL");
				control("No selecciono ninguna Factura");
				return;
			}
			
			if (this.pojoFacPago.getIdFormaPago() == null) {
				log.info("ERROR -2 - FORMA PAGO NULL");
				control("Debe indicar la Forma de Pago");
				return;
			}
			
			if (this.pojoFacPago.getIdCuentaDeposito() == null) {
				log.info("ERROR -3 - CUENTA DEPOSITO NULL");
				control("Debe indicar la Cuenta de Deposito");
				return;
			}
			
			if (this.pojoFacPago.getIdBancoOrigen() == null) {
				log.info("ERROR -4 - BANCO ORIGEN NULL");
				control("Debe indicar el Bando Origen");
				return;
			}
			
			if (this.pojoFacPago.getMonto().doubleValue() <= 0L) {
				log.info("ERROR 4 - Monto menor o igual a cero. No permitido");
				control(4);
				return;
			}
			
			if (this.pojoFacPago.getMonto().doubleValue() > this.facturaSaldo.doubleValue()) {
				log.info("ERROR 5 - Monto mayor al saldo. No permitido");
				control(5);
				return;
			}

			this.pojoFacPago.setModificadoPor(this.usuarioId);
			this.pojoFacPago.setFechaModificacion(Calendar.getInstance().getTime());
			if (this.pojoFacPago.getObservaciones() == null) this.pojoFacPago.setObservaciones("");
			
			if (this.pojoFacPago.getId() == null || this.pojoFacPago.getId() == 0L) {
				this.pojoFacPago.setCreadoPor(this.usuarioId);
				this.pojoFacPago.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD y añadimos a la lista
				this.pojoFacPago.setId(this.ifzFacPagos.save(this.pojoFacPago));
				this.listFacPagos.add(this.pojoFacPago);
			} else {
				// Actualizamos en la BD
				this.ifzFacPagos.update(this.pojoFacPago);
				
				// Actualizamos en la lista
				int index = -1;
				for(FacturaPagosExt var : this.listFacPagos){
					if(var.getId() == this.pojoFacPago.getId()) {
						index = this.listFacPagos.indexOf(var);
						break;
					}
				}
				
				if (index >= 0) 
					this.listFacPagos.set(index, this.pojoFacPago.getCopia());
			}
			
			// Enviamos transaccion
			enviarMensajeTransaccion();
		} catch (Exception e) {
			log.error("ERROR INTERNO: Ocurrio un problema al intentar Guardar el Pago.", e);
			control("Ocurrio un problema al intentar Guardar el Pago");
		}
	}

	public void fillLists() {
		// Formas de pagos
		this.listFormasPagoItems.clear();
		this.listFormasPago = this.ifzFormasPagos.findLikeColumnName("formaPago", "");
		for (FormasPagos var : this.listFormasPago) {
			this.listFormasPagoItems.add(new SelectItem(var.getId(), var.getId() + " - " + var.getFormaPago()));
		}
		
		// Cuentas Bancarias
		this.listCuentasItems.clear();
		this.listCuentas = this.ifzCtas.findLikeColumnName("numeroDeCuenta", "");
		for (CtasBanco var : listCuentas) {
			this.listCuentasItems.add(new SelectItem(var.getId(),  var.getNumeroDeCuenta() + " - " + var.getInstitucionBancaria().getNombreCorto()));
		}
		
		// Bancos
		this.listBancosItems.clear();
		this.listBancos = this.ifzBancos.findLikeColumnName("nombreCorto", "");
		for (CatBancos var : listBancos) {
			this.listBancosItems.add(new SelectItem(var.getId(), var.getId() + " - " + var.getNombreCorto()));
		}
	}
	
	public void saldo() {
		try {
			control();
			if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getIdFactura().getId() != null && this.pojoFacPago.getIdFactura().getId() > 0L) {
				this.facturaSaldo = this.ifzFacPagos.findSaldoByFactura(this.pojoFacPago.getIdFactura());
				this.facturaSaldo = this.facturaSaldo.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		} catch (Exception e) {
			log.error("ERROR INTERNO: Ocurrio un problema al intentar obtener el Saldo de la Factura", e);
			control("Ocurrio un problema al intentar obtener el Saldo de la Factura");
		}
	}

	public void enviarMensajeTransaccion() {
		// Lanzamos mensajeTransaccion en un hilo distinto al principal
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			control();
			if (this.pojoFacPago.getIdFactura().getProvisionada() != 1) {
				log.info("Factura sin provision. No ejecutamos ninguna Transaccion ");
				return;
			}
			
			log.info("Enviamos mensaje de Pago de Factura ... ");
			Respuesta respuesta = this.ifzFacPagos.enviarTransaccion(this.pojoFacPago, 1L);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("\nOcurrio un problema al intentar provisionar la Factura\n", (Throwable) respuesta.getBody().getValor("exception"));
				control(respuesta.getErrores().getDescError());
				return;
			}

			log.info("Factura Provisionada");
		} catch (Exception e) {
			log.error("ERROR INTERNO: No se pudo enviar la Factura a Provision", e);
			control("Ocurrio un problema al intentar enviar la Factura a Provision");
		}
	}

	private void control() {
		control(false, 0, "");
	}
	
	private void control(boolean value) {
		if (! value) {
			control(false, 0, "");
			return;
		} 

		control(true, 1, "ERROR");
	}
		
	private void control(int tipoMensaje) {
		if (tipoMensaje == 0) {
			control(false, 0, "");
			return;
		}
		
		control(true, tipoMensaje, "ERROR");
	}
	
	private void control(String mensaje) {
		if (mensaje == null || "".equals(mensaje.trim())) 
			control(false, 0, "");
		else
			control(true, -1, mensaje);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.band = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.resOperacion = mensaje;
	}

	// --------------------------------------------------------------------------------
	// BUSQUEDA FACTURAS
	// --------------------------------------------------------------------------------
	
	public void nuevaBusquedaFacturas() {
		this.campoBusquedaFacturas = this.tipoBusquedaFacturas.get(0).getValue().toString();
		this.valBusquedaFacturas = "";
		this.numPaginaFacturas = 1;

		this.pojoFacturaSeleccionada = null;
		if (this.listFacturas == null)
			this.listFacturas = new ArrayList<Factura>();
		this.listFacturas.clear();
	}
	
	public void buscarFacturas() {
		HashMap<String, Object> params = new HashMap<>();
		
		try {
			control();
			if (this.campoBusquedaFacturas == null || "".equals(this.campoBusquedaFacturas))
				this.campoBusquedaFacturas = this.tipoBusquedaFacturas.get(0).getValue().toString();
			
			params.put(this.campoBusquedaFacturas, this.valBusquedaFacturas);
			params.put("estatus", 1);
			
			this.ifzFacturas.orderBy("id DESC");
			this.listFacturas = this.ifzFacturas.findLikeProperties(params);
			if (this.listFacturas == null || this.listFacturas.isEmpty()) {
				log.info("Error 2 - Busqueda de Facturas sin resultado");
				control(2);
			}
		} catch(Exception e) {
			log.error("ERROR INTERNO: Ocurrio un problema al intentar realizar la busqueda de Facturas", e);
			control("Ocurrio un problema al intentar realizar la busqueda de Facturas");
		} finally {
			if (this.listFacturas == null)
				this.listFacturas = new ArrayList<Factura>();
			log.info(this.listFacturas.size() + " Facturas encontradas");
		}
	}
	
	public void seleccionarFactura() {
		try {
			control();
			log.info("Seleccionando factura ... ");
			if (this.pojoFacturaSeleccionada == null) {
				log.info("Ocurrio un problema al intentar recuperar la Factura seleccionada");
				control("Ocurrio un problema al intentar recuperar la Factura seleccionada");
				return;
			}
			
			// Extendemos la factura y la asignamos al Pago
			log.info("Extendemos la factura y la asignamos al Pago ... ");
			this.pojoFacPago.setIdFactura(this.ifzFacturas.convertir(this.pojoFacturaSeleccionada));
			
			// Obtenemos datos del Cliente/Beneficiario
			log.info("Obtenemos datos del Cliente/Beneficiario ... ");
			this.pojoFacPago.setIdBeneficiario(this.pojoFacPago.getIdFactura().getIdBeneficiario());
			this.pojoFacPago.setBeneficiario(this.pojoFacPago.getIdFactura().getBeneficiario());
			this.pojoFacPago.setTipoBeneficiario(this.pojoFacPago.getIdFactura().getTipoBeneficiario());
			
			// Obtenemos su saldo
			log.info("Obteniendo saldo de factura ... ");
			saldo();
			log.info("Saldo de Factura: " + this.facturaSaldo.doubleValue());
			
			if (this.facturaSaldo.doubleValue() <= 0) {
				this.pojoFacturaSeleccionada = null;
				this.pojoFacPago.setIdFactura(null);
				buscarFacturas();
				log.info("Factura sin saldo. La Factura seleccionada ya esta liquidada");
				control("La Factura seleccionada ya esta liquidada");
			} else {
				nuevaBusquedaFacturas();
				log.info("Factura seleccionada y asignada. Proceso terminado");
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar obtener datos de la factura seleccionada", e);
			control("Ocurrio un problema al intentar obtener datos de la factura seleccionada");
		} 
	}
	
	// ---------------------------------------
	// PROPIEDADES
	// ---------------------------------------
	
	public String getFacturaDescripcion() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getIdFactura().getId() != null && this.pojoFacPago.getIdFactura().getId() > 0L)
			return this.pojoFacPago.getIdFactura().getId() + " - " + this.pojoFacPago.getIdFactura().getFolioFactura() + " - " + this.pojoFacPago.getIdFactura().getNombreObra() + ", $ " + (new DecimalFormat("###,###,##0.00")).format(this.pojoFacPago.getIdFactura().getTotal());
		return "";
	}
	
	public void setFacturaDescripcion(String value) {}

	public String getFacturaCliente() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFactura() != null && this.pojoFacPago.getIdFactura().getId() != null && this.pojoFacPago.getIdFactura().getId() > 0L)
			return this.pojoFacPago.getIdFactura().getIdBeneficiario() + " - " + this.pojoFacPago.getIdFactura().getCliente();
		return "";
	}
	
	public void setFacturaCliente(String value) {}
	
	public BigDecimal getMonto() {
		if (this.pojoFacPago != null && this.pojoFacPago.getMonto() != null) 
			return this.pojoFacPago.getMonto();
		return BigDecimal.ZERO;
	}
	
	public void setMonto(BigDecimal value) {
		if (value == null) 
			value = new BigDecimal(0L);
		this.pojoFacPago.setMonto(value);
	}
	
	public long getFormaPago() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFormaPago() != null) 
			return this.pojoFacPago.getIdFormaPago().getId();
		return 0L;
	}
	
	public void setFormaPago(long idFormaPago) {
		if (idFormaPago <= 0)
			return;
		
		for (FormasPagos var : this.listFormasPago) {
			if (var.getId() == idFormaPago) {
				this.pojoFacPago.setIdFormaPago(var);
				break;
			}
		}
	}
		
	public long getCuentaBancaria() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdCuentaDeposito() != null) 
			return this.pojoFacPago.getIdCuentaDeposito().getId();
		return 0L;
	}
		
	public void setCuentaBancaria(long idCuentaBancaria) {
		if (idCuentaBancaria <= 0)
			return;
		
		for (CtasBanco var : this.listCuentas) {
			if (var.getId() == idCuentaBancaria) {
				this.pojoFacPago.setIdCuentaDeposito(var);
				break;
			}
		}
	}
	
	public long getBancoOrigen() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdBancoOrigen() != null) 
			return this.pojoFacPago.getIdBancoOrigen().getId();
		return 0L;
	}
	
	public void setBancoOrigen(long bancoOrigen) {
		if (bancoOrigen <= 0)
			return;
		
		for (CatBancos var : this.listBancos) {
			if (var.getId() == bancoOrigen) {
				this.pojoFacPago.setIdBancoOrigen(var);
				break;
			}
		}
	}
		
	public List<FacturaPagosExt> getListFacPagos() {
		return listFacPagos;
	}
	
	public void setListFacPagos(List<FacturaPagosExt> listFacPagos) {
		this.listFacPagos = listFacPagos;
	}
	
	public FacturaPagosExt getPojoFacPago() {
		return pojoFacPago;
	}
	
	public void setPojoFacPago(FacturaPagosExt pojoFacPago) {
		this.pojoFacPago = pojoFacPago;
	}
	
	public String getResOperacion() {
		return resOperacion;
	}
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}
	
	public int getTipoMensaje() {
		return tipoMensaje;
	}
	
	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}
	
	public List<SelectItem> getTipoBusqueda() {
		return tipoBusqueda;
	}
	
	public void setTipoBusqueda(List<SelectItem> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}
	
	public String getValBusqueda() {
		return valBusqueda;
	}
	
	public void setValBusqueda(String valBusqueda) {
		this.valBusqueda = valBusqueda;
	}
	
	public String getCampoBusqueda() {
		return campoBusqueda;
	}
	
	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}
		
	public boolean getBand() {
		return band;
	}
	
	public void setBand(boolean band) {
		this.band = band;
	}
	
	public List<FormasPagos> getListFormasPago() {
		return listFormasPago;
	}
	
	public void setListFormasPago(List<FormasPagos> listFormasPago) {
		this.listFormasPago = listFormasPago;
	}
		
	public List<Factura> getListaFacturas() {
		return listFacturas;
	}
	
	public void setListaProyObras(List<Factura> listaFacturas) {
		this.listFacturas = listaFacturas;
	}
	
	public List<SelectItem> getTipoBusquedaFacturas() {
		return tipoBusquedaFacturas;
	}
	
	public void setTipoBusquedaFacturas(List<SelectItem> tipoBusquedaFacturas) {
		this.tipoBusquedaFacturas = tipoBusquedaFacturas;
	}
	
	public String getValBusquedaFacturas() {
		return valBusquedaFacturas;
	}
	
	public void setValBusquedaFacturas(String valBusquedaFacturas) {
		this.valBusquedaFacturas = valBusquedaFacturas;
	}
	
	public String getCampoBusquedaFacturas() {
		return campoBusquedaFacturas;
	}
	
	public void setCampoBusquedaFacturas(String campoBusquedaFacturas) {
		this.campoBusquedaFacturas = campoBusquedaFacturas;
	}
	
	public int getNumPaginaFacturas() {
		return numPaginaFacturas;
	}
	
	public void setNumPaginaFacturas(int numPaginaFacturas) {
		this.numPaginaFacturas = numPaginaFacturas;
	}
	
	public Date getFecha() {
		return this.pojoFacPago != null && this.pojoFacPago.getFecha() != null ? this.pojoFacPago.getFecha() : new Date();
	}
	
	public void setFecha(Date fecha) {
		this.pojoFacPago.setFecha(fecha);
	}
		
	public String getObservaciones() {
		return this.pojoFacPago == null || this.pojoFacPago.getObservaciones() == null ? "" : this.pojoFacPago.getObservaciones();
	}
		
	public void setObservaciones(String observaciones) {
		this.pojoFacPago.setObservaciones(observaciones);
	}
	
 	public List<SelectItem> getListFormasPagoItems() {
		return listFormasPagoItems;
	}
 	
	public void setListFormasPagoItems(List<SelectItem> listFormasPagoItems) {
		this.listFormasPagoItems = listFormasPagoItems;
	}
	
	public List<SelectItem> getListCuentasItems() {
		return listCuentasItems;
	}
	
	public void setListCuentasItems(List<SelectItem> listCuentasItems) {
		this.listCuentasItems = listCuentasItems;
	}
	
	public List<SelectItem> getListBancosItems() {
		return listBancosItems;
	}
	
	public void setListBancosItems(List<SelectItem> listBancosItems) {
		this.listBancosItems = listBancosItems;
	}

	public BigDecimal getFacturaSaldo() {
		return facturaSaldo;
	}
	
	public void setFacturaSaldo(BigDecimal facturaSaldo) {
		this.facturaSaldo = facturaSaldo;
	}

	public Long getIdFacturaToDelete() {
		return idFacturaToDelete;
	}

	public void setIdFacturaToDelete(Long idFacturaToDelete) {
		this.idFacturaToDelete = idFacturaToDelete;
	}

	public Factura getPojoFacturaSeleccionada() {
		return pojoFacturaSeleccionada;
	}
	
	public void setPojoFacturaSeleccionada(Factura pojoFacturaSeleccionada) {
		this.pojoFacturaSeleccionada = pojoFacturaSeleccionada;
	}


	
	public boolean isDebugging() {
		return isDebug;
	}
	


	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
}

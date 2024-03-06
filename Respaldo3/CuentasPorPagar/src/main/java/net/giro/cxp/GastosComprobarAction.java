package net.giro.cxp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.CuentasBancariasRem;

public class GastosComprobarAction {
	private static Logger log = Logger.getLogger(GastosComprobarAction.class);
	private PropertyResourceBundle entornoProperties;
	private FacesContext facesContext;
	private HttpSession httpSession;
	private InitialContext ctx;
	private LoginManager loginManager;
	private PersonaExt pojoTerceros; // Tygpersonas 
	private ConGrupoValores pojoGpoValPersonas;
	private ConGrupoValores pojoGpoValTerceros;
	private PagosGastosExt pojoGtosComp; // MovimientosCuentas
	private PagosGastos pojoGastoComprobar; // MovimientosCuentas
	private CtasBancoExt pojoCtas;
	private PersonaExt pojoBeneficiarios; // Tygpersonas
	private PagosGastosRem ifzGtosComp; // MovimientosCuentasFacadeRemote
	private EmpleadoRem ifzEmpleado;
	private ReportesRem	ifzReportes;
	private PersonaRem ifzBenefi; // TygpersonasFacadeRemote
	private List<Persona> listBeneficiarios;
	private List<PagosGastosExt> listGtosComp;	
	private List<String>	listTipoBusqueda;
	private List<Area> listAreas;
	private ConGrupoValoresRem ifzGpoVal; 	
	private ConValoresRem ifzConValores; 	
	private ConGrupoValores pojoGpoValFormasPago; 
	private List<ConValores> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	private String nombreBeneficiario;
	private String terceros;
	private String beneficiario;
	private String tipoPersona;
	private String campoBusqueda;	
	private String valTipoBusqueda;
	private String sucursalesVisibles;
	private long usuarioId;
	private String usuario;
	private boolean mensajeError;
	private boolean esTransferencia;
	private boolean esCheque;
	private boolean esSpei;
	private boolean clicSalvar;
	private boolean gastoCancelado;
	private int numPagina;
	private String operacion;
	private String referenciaOperacion;
	// Sucursal
	private SucursalesRem ifzSucursal; 
	private List<Sucursal> listSucursales;
	private List<SelectItem> listSucursalesItems;
	private long idSucursal;
	// Cuentas Bancarias
	private CuentasBancariasRem ifzCtas; // CtasBancoFacadeRemote
	private List<CuentaBancaria> listCuentas;
	private List<SelectItem> listCuentasItems;
	private long idCuenta;
	// Busqueda de OBRAS
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> tiposBusquedaObra;
	private String campoBusquedaObra;
	private String valorBusquedaObra;
	private String tipoObra;
	private int tipoObraBusquedaObra;
	private int paginacionBusquedaObra;
	private boolean toggleBusquedaObraAvanzada;
	// perfiles
	private boolean perfilEgresos;
	private String perfilRequiereObra;
	// control
	private boolean band;
	private int tipoMensaje;
	private String mensaje;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;

	public GastosComprobarAction() {
		Application app = null;
		ValueExpression ve = null;
		long idGrupoValor = 0;
        
		try {
			this.facesContext =  FacesContext.getCurrentInstance();
			this.httpSession = (HttpSession) this.facesContext.getExternalContext().getSession(false);
			app = this.facesContext.getApplication();
			
			ve = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(facesContext.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId(); 
			this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : this.facesContext.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// Perfiles
			this.perfilRequiereObra = this.loginManager.getAutentificacion().getPerfil("REQUIERE_OBRA");

			// EJB's
			this.ctx = new InitialContext();
			//this.ifzAreas = (AreaRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//AreaFac!net.giro.cxp.logica.AreaRem");
			this.ifzGtosComp = (PagosGastosRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
			this.ifzBenefi = (PersonaRem) this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzSucursal = (SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzCtas = (CuentasBancariasRem) this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem ) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
	
			//this.ifzAreas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGtosComp.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzBenefi.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Inicializaciones
			//this.segNeg 			= 0L;
			//this.muestraSeg 		= false;
			this.pojoTerceros 		= new PersonaExt();
			this.pojoGpoValTerceros = new ConGrupoValores();
			this.pojoGpoValPersonas = new ConGrupoValores();
			this.pojoGtosComp 		= new PagosGastosExt();
			this.pojoCtas 			= new CtasBancoExt();
			this.pojoBeneficiarios	= new PersonaExt();
			//this.listTerceros 		= new ArrayList<Persona>();
			this.listGtosComp 		= new ArrayList<PagosGastosExt>();
			this.listSucursales 	= new ArrayList<Sucursal>();
			this.listCuentas 		= new ArrayList<CuentaBancaria>();
			this.listBeneficiarios 	= new ArrayList<Persona>();
			this.listTipoBusqueda	= new ArrayList<String>();
			this.listAreas 			= new ArrayList<Area>();
			this.numPagina = 1;
	
			this.gastoCancelado=false;
			this.mensajeError = false;
			this.clicSalvar=false;
			this.listTipoBusqueda.add("Beneficiario");	
			this.listTipoBusqueda.add("Descripción del gasto");
			this.valTipoBusqueda = this.listTipoBusqueda.get(0);
			
			// Busqueda Obras
			this.tiposBusquedaObra = new ArrayList<SelectItem>();
			this.tiposBusquedaObra.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaObra.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObra.add(new SelectItem("cliente", "Cliente"));
			this.tiposBusquedaObra.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
		
			ve = app.getExpressionFactory().createValueExpression(this.facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(this.facesContext.getELContext());

			// OPERACIONES CXP
			if (this.entornoProperties.getString("SYS_FORMAS_PAGO") == null || "".equals(this.entornoProperties.getString("SYS_FORMAS_PAGO")) )
				throw new Exception("No se encontro encontro el grupo SYS_FORMAS_PAGO en con_grupo_valores");
			else
				idGrupoValor = Long.valueOf(this.entornoProperties.getString("SYS_FORMAS_PAGO"));
			this.pojoGpoValFormasPago = this.ifzGpoVal.findById(idGrupoValor);
			
			// RELACIONES PERSONAS
			this.pojoGpoValPersonas = this.ifzGpoVal.findById(4L); // "SYS_RELPER");
			if (this.pojoGpoValPersonas == null || this.pojoGpoValPersonas.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
			
			cargarFormasDePago();
			cargarSucursales();
			cargarCuentas();
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar GastosComprobarAction", e);
		}
	}
	
	public void buscar(){
		try {
			control();
			if( "Beneficiario".equals(this.valTipoBusqueda))					
				this.listGtosComp = this.ifzGtosComp.findLikeGtosComprobar("beneficiario", this.campoBusqueda, "G", "G", 50, this.sucursalesVisibles);	
			else {
				if("Descripción del gasto".equals(this.valTipoBusqueda))
					this.listGtosComp = this.ifzGtosComp.findLikeGtosComprobar("concepto", this.campoBusqueda, "G", "G", 50, this.sucursalesVisibles);				
				else
					this.listGtosComp = this.ifzGtosComp.findLikeGtosComprobar("beneficiario", this.campoBusqueda, "G", "G", 50, this.sucursalesVisibles);
			}

			if (this.listGtosComp == null || this.listGtosComp.isEmpty())
				control(6, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar los Gastos a Comprobar", e);
		}
	}	
	
	public void nuevo() {
		try { 
			control();
			this.pojoGtosComp = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoBeneficiarios = new PersonaExt();
			this.pojoTerceros = new PersonaExt();
			this.idSucursal = 0L;
			this.idCuenta = 0L;
			this.beneficiario = "";
			this.terceros = "";			
			this.campoBusqueda = "";
			this.mensaje = "";
			this.tipoPersona = "N";	
			this.operacion = "C";//this.operacionValidate("C");
			this.esTransferencia = false;
			this.esCheque = true;
			this.esSpei = false;
			this.clicSalvar = false;			
			this.mensajeError = false;
			this.pojoObra = null;
		} catch (Exception e) {
			control("Ocurrio un problema al preparar un nuevo Gasto a Comprobar", e);
		}
	}

	public void editar() {
		try {
			control();
			//cargarSucursales();
			this.clicSalvar = true;	
			//this.cuenta = this.pojoGtosComp.getIdCuentaOrigen().getId() + " - " + this.pojoGtosComp.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto() + " - " + pojoGtosComp.getIdCuentaOrigen().getNumeroDeCuenta();
			this.beneficiario = this.pojoGtosComp.getIdBeneficiario().getId() + " - "+this.pojoGtosComp.getBeneficiario();
			this.tipoPersona = this.pojoGtosComp.getTipoBeneficiario(); 
			this.operacion = this.pojoGtosComp.getOperacion();
			this.idSucursal = this.pojoGtosComp.getIdSucursal().getId();
			this.idCuenta = this.pojoGtosComp.getIdCuentaDestino().getId();
			
			if ("T".equals(this.operacion)) {
				this.terceros= pojoGtosComp.getIdCuentaDestinoTerceros().getId() + " - " + pojoGtosComp.getIdCuentaDestinoTerceros().getNombre()+" "+ pojoGtosComp.getIdCuentaDestinoTerceros().getPrimerApellido() + " "+ pojoGtosComp.getIdCuentaDestinoTerceros().getSegundoApellido() + " " + pojoGtosComp.getIdCuentaDestinoTerceros().getNumeroCuenta();
				this.esTransferencia = true;	
			} else
				this.esTransferencia = false;
			this.esCheque = "C".equals(this.operacion); 
		} catch(Exception e) {			
			control("Ocurrio un problema al recuperar el Gasto a Comprobar seleccionado", e);
		}
	}	
	
	public void guardar() {
		Pattern pat = null;
		Matcher match = null;
		boolean registroNuevo = false;
		Respuesta resp = new Respuesta();
		
		try {
			control();
			if (this.idSucursal <= 0L) {
				control(1, "Debe seleccionar una Sucursal");
				return;
			}

			//this.operacion = operacionValidate(this.operacion);
			for (Sucursal var : this.listSucursales) {
				if (var.getId() != idSucursal)
					continue;
				this.pojoGtosComp.setIdSucursal(var);
				break;
			}

			// Cuenta Bancaria
			this.pojoCtas = this.ifzGtosComp.findCuentaBancariaById(this.idCuenta);
			this.pojoGtosComp.setIdCuentaOrigen(this.pojoCtas);
			
			if (! validaSaldoCuentaPropios()) {
				//this.operacion = operacionValidate(this.operacion);
				return; 
			}
			
			//validando beneficiario 
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.beneficiario);
			if (match.find())
				if (registroNuevo) 
					this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.pojoBeneficiarios = this.ifzBenefi.findById(Long.valueOf(match.group(1))); // findByIdPojoCompleto(Long.valueOf(match.group(1)));
				else
					if(this.pojoBeneficiarios.getId() != Long.valueOf(match.group(1)))
						this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.pojoBeneficiarios = this.ifzBenefi.findById(Long.valueOf(match.group(1))); // findByIdPojoCompleto(Long.valueOf(match.group(1)));
			this.pojoGtosComp.setIdBeneficiario(this.pojoBeneficiarios);
			
			//validando operacion para mostrar catalodo de Terceros
			if ("T".equals(this.operacion) || "S".equals(this.operacion)) {
				match = pat.matcher(this.beneficiario);			
				if (match.find())
					if(registroNuevo)				
						this.pojoTerceros = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), "P"); // this.pojoTerceros = this.ifzBenefi.findByIdPojoCompleto(Long.valueOf(match.group(1)));
					else
						if (this.pojoTerceros.getId() != Long.valueOf(match.group(1)))
							this.pojoTerceros = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), "P"); // this.pojoTerceros = this.ifzBenefi.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				
				this.pojoGtosComp.setIdCuentaDestinoTerceros(this.pojoTerceros);	
				this.pojoGtosComp.setBeneficiario(this.pojoTerceros.getNombre()+" "+(this.pojoTerceros.getPrimerApellido() != null ? this.pojoTerceros.getPrimerApellido():"")+" "+(this.pojoTerceros.getSegundoApellido() != null ? this.pojoTerceros.getSegundoApellido():""));
			}			
			
			this.nombreBeneficiario = this.pojoBeneficiarios.getPrimerNombre() + " " 
					+ ((this.pojoBeneficiarios.getSegundoNombre()   != null && ! "".equals(this.pojoBeneficiarios.getSegundoNombre().trim()))   ? this.pojoBeneficiarios.getSegundoNombre().trim()   : "") + " " 
					+ ((this.pojoBeneficiarios.getPrimerApellido()  != null && ! "".equals(this.pojoBeneficiarios.getPrimerApellido().trim()))  ? this.pojoBeneficiarios.getPrimerApellido().trim()  : "") + " " 
					+ ((this.pojoBeneficiarios.getSegundoApellido() != null && ! "".equals(this.pojoBeneficiarios.getSegundoApellido().trim())) ? this.pojoBeneficiarios.getSegundoApellido().trim() : "");
			this.pojoGtosComp.setBeneficiario(this.nombreBeneficiario);
			this.pojoGtosComp.setTipoBeneficiario(this.tipoPersona);
			this.pojoGtosComp.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			this.pojoGtosComp.setOperacion(this.operacion);
			if ("C".equals(this.operacion))
				this.pojoGtosComp.setNoCheque(Integer.parseInt(this.referenciaOperacion));
			else if ("T".equals(this.operacion))
				this.pojoGtosComp.setFolioAutorizacion(this.referenciaOperacion);
			this.clicSalvar = true;
			if (this.pojoGtosComp.getId() == null)
				registroNuevo = true;
			this.pojoGtosComp.setModificadoPor(this.usuarioId);
			this.pojoGtosComp.setFechaModificacion(Calendar.getInstance().getTime());

			if (registroNuevo) {
				this.pojoGtosComp.setCreadoPor(this.usuarioId);
				this.pojoGtosComp.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoGtosComp.setTipo("G");//gasto
				this.pojoGtosComp.setEstatus("G");//generado
				
				// Asignamos consecutivo
				/*int consecutivo = this.ifzGtosComp.findConsecutivoByBeneficiario(this.pojoGtosComp.getIdBeneficiario().getId(), "G", "G");
				this.pojoGtosComp.setConsecutivo(consecutivo);*/
				
				// Asignamos AREA
				this.pojoGtosComp.setArea((this.perfilRequiereObra == "S" && this.pojoObra != null ? "Obra" : "Oficina"));
				
				// Asignamos monto limite
				this.pojoGtosComp.setMontoLimite(0D);
				
				// Guardamos
				resp = this.ifzGtosComp.salvar(this.pojoGtosComp); 
				if (resp.getResultado() != -1) {
					this.pojoGtosComp.setId(Long.valueOf(resp.getReferencia()));
					this.mensaje="BIEN";
					if ("T".equals(operacion) || "S".equals(operacion)) {// para que no salga el cheque si es una transferencia
						this.mensaje = "";
					} 
				} else {
					if(! "BIEN".equals(resp.getRespuesta())) {
						this.mensaje = resp.getRespuesta();
						if ((resp.getRespuesta().indexOf("SE CANCELA EL FOLIO ") > -1) || (resp.getRespuesta().indexOf("SE CANCELAN LOS FOLIOS ") > -1))
							this.tipoMensaje = 2;
						else
							this.tipoMensaje = 1;
						control(this.tipoMensaje, this.mensaje);
						//this.operacion = operacionValidate(this.operacion);
						return;
					}
				}
				
				// Agregamos a la lista
				this.listGtosComp.add(this.pojoGtosComp);
			} else {
				//this.ifzGtosComp.update(this.pojoGtosComp);		
				for (PagosGastosExt var : this.listGtosComp) {
					if (var.getId().equals(this.pojoGtosComp.getId()))
						var = this.pojoGtosComp;
				}
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			this.pojoGastoComprobar = this.ifzGtosComp.convertir(this.pojoGtosComp);
			mensajeTransaccion();
		} catch(Exception e) {
			control(true, 5, "Ocurrio un problema al intentar guardar el Gasto a Comprobar", e);
			//this.operacion = operacionValidate(this.operacion);
		}
	}

	public void cancelar() {
		Respuesta resp = new Respuesta();
		
		try {
			control();
			resp = ifzGtosComp.cancelacion(this.pojoGtosComp, Calendar.getInstance().getTime());
			if (resp.getResultado() == -1) {
				control(1, resp.getRespuesta());
				return;
			} 

			for (PagosGastosExt var : this.listGtosComp) {				
				if (var.getId().equals(this.pojoGtosComp.getId())) {		
					var.setEstatus("X");
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Cancelar el Gasto a Comprobar", e);
		}	
	}
	
	public boolean validaSaldoCuentaPropios (){
		try{
			control();
			if (this.pojoGtosComp.getMonto() <= 0) {
				control(3, "Debe indicar el Monto");
				return false;
			}

			/* DESACTIVAMOS TEMPORALMENTE LA VALIDACION DE SALDO SUFICIENTE
			 * ------------------------------------------------------------
			if (this.pojoCtas.getId() != 0L){
				if(!ifzCtas.haySaldoSuficiente(pojoGtosComp.getMonto(), this.pojoCtas.getId())){
					tipoMensaje = 7;
					band = true;
					mensaje = "";
					return "ERROR";
				}
			}*/
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar el Saldo de la Cuenta indicada", e);
			return false;
		}
		
		return true;
	}
	
	public void mensajeTransaccion() {
		try {
			control();
			log.info("Enviando Transaccion ... ");
			this.ifzGtosComp.contabilizador(this.pojoGastoComprobar.getId());
			log.info("Transaccion enviada");
		} catch (Exception e) {
			control("Ocurrio un problema al enviar el evento de transaccion requerido", e);
		}
	}

	public void cancelarCheques() {
		Respuesta resp = null;
		
		try {
			resp = new Respuesta();
			resp = this.ifzGtosComp.salvar(this.pojoGtosComp);
			if(! "BIEN".equals(resp.getRespuesta()) && resp.getResultado() == -1) {
				control(1, resp.getRespuesta());
				//antes le mostraba el mensaje que me regresaba la funcion CANCELA_CHEQUES pero ahora solo le dire que se guardo		
				guardar();
			}
		} catch(Exception e) {
			control("Ocurrio un problema al intentar cancelar el Cheque", e);
		}
	}
	
	/*private String operacionValidate(String value) {
		boolean reverse = false;
		
		if ("".equals(value.trim())) return "";
		reverse = (value.trim().length() == 1);
		
		for (ConValores var : this.listFormasPago) {
			if (reverse && value.equals(var.getValor())) 
				return var.getDescripcion();
			if (value.equals(var.getDescripcion())) 
				return var.getValor();
		}
		
		return value.trim();
	}*/
	
	public void cambioTerceros(){
		Pattern pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		Matcher match = null;
		
		try {
			control();
			if ("S".equals(this.operacion)) {
				match = pat.matcher(this.beneficiario);
				if (match.find()) {
					this.pojoTerceros = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); 
					if (this.pojoTerceros == null) {
						control(1, "Debe indicar un Beneficiario");
						return;
					}
					
					if ((this.pojoTerceros.getNombre() == null || "".equals(this.pojoTerceros.getNombre())) && this.pojoTerceros.getPrimerApellido() == null || "".equals(this.pojoTerceros.getPrimerApellido())){
						control(1, "El nombre del Beneficiario no esta completo");
						return;
					}
					
					if (this.pojoTerceros.getBanco() == null) {
						control(1, "El Beneficiario no tiene capturado banco");
						return;
					}
					
					if (this.pojoTerceros.getClabeInterbancaria() == null || "".equals(this.pojoTerceros.getClabeInterbancaria().trim())) {
						control(1, "El Beneficiario no tiene capturada cuenta Clabe");
						return;
					}
				}
			}
		} catch(Exception e) {			
			control("Ocurrio un problema al validar el Baneficiario", e);
		}
	}
	
	public String evaluaSiCambioSucursal (){
		/*Pattern pat = null;
		Matcher match = null;*/
		
		try{
			/*muestraSeg = false;
			mensaje = "";
			pat = Pattern.compile("(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)");*/
			
			//validando sucursal
			/*match = pat.matcher(this.sucursal);
			if(match.find())
				if(this.pojoSucursal == null)
					this.pojoSucursal = this.ifzGtosComp.findSucursalById(Long.valueOf(match.group(1))); // this.ifzSucursal.findById(Long.valueOf(match.group(1)));
				else
					if(this.pojoSucursal.getId() != Long.valueOf(match.group(1)))
						this.pojoSucursal = this.ifzGtosComp.findSucursalById(Long.valueOf(match.group(1))); // this.ifzSucursal.findById(Long.valueOf(match.group(1)));
			*/
			/*if(pojoSucursal != null){
				this.listAreas = ifzAreas.findByProperty("regionId.clave", pojoSucursal.getId());
				if(listAreas != null && !listAreas.isEmpty())
					muestraSeg = true;
			}*/
		} catch(Exception e) {
			//log.error("error en evaluaSiCambioSucursal :: [" + match.group(1) + "]", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String evaluaCancelar(){
		try {
			 if("X".equals(this.pojoGtosComp.getEstatus())){
				 gastoCancelado = true;
				this.tipoMensaje = 4;
			} else {
				this.tipoMensaje = 0;
				gastoCancelado = false;
			}	
		} catch (Exception e) {			
			log.error("Error en el metodo evaluaCancelar.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String reporte() {
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		String nombreDocumento = "";
		
		try{
			if (this.pojoGtosComp != null && this.pojoGtosComp.getId() != null && this.pojoGtosComp.getId() > 0L) {
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("pagosGastosId", this.pojoGtosComp.getId());

				// Parametros para la ejecucion del reporte
				params = new HashMap<String, String>();
				params.put("idPrograma", 	  this.entornoProperties.getString("REPORTE_VIATICOS_ID"));
				params.put("nombreDocumento", this.entornoProperties.getString("REPORTE_VIATICOS_NOMBRE"));
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);

				// enviamos la solicitud de ejecucion
				Respuesta respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if(respuesta.getErrores().getCodigoError() == 0L){
					nombreDocumento = "VIATICOS-" + this.pojoGtosComp.getId() + "." + respuesta.getBody().getValor("formatoReporte");
					
					this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
					this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte")); 	
					this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
					
					/*this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));	
					this.httpSession.setAttribute("nombreDocumento", respuesta.getBody().getValor("nombreDocumento"));
					this.httpSession.setAttribute("formato", this.entornoProperties.getString("REPORTE_VIATICOS_FORMATO"));*/
				} else {
					this.mensaje = respuesta.getErrores().getDescError();
				}
			}
		} catch (Exception e) {
			this.mensaje = "error al ejecutar reporte";
			e.printStackTrace();
		}
		
		System.out.println( "mensaje " + mensaje);
		return "OK";
	}

	// METODOS DE ACCESO GENERALES
	public List<Persona> autoacompletaBeneficiario(Object obj) {
		try {
			control();
			this.ifzBenefi.setInfoSesion(this.loginManager.getInfoSesion());
			this.listBeneficiarios = this.ifzBenefi.findLikeClaveNombre("nombre", obj.toString(), this.pojoGpoValPersonas, this.tipoPersona, 100, false);
			return this.listBeneficiarios;
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Beneficiarios", e);
			return new ArrayList<Persona>();
		}
	}

	public void cambioBeneficiario(){
		try{
			this.beneficiario = "";
			this.tipoPersona = "N";
		} catch(Exception e) {			
			control("error en cambioBeneficiario", e);
		}
	}
	
   	private void cargarSucursales() {
   		try {
			if (this.listSucursales == null)
				this.listSucursales = new ArrayList<Sucursal>();
			this.listSucursales.clear();
			
			if (this.listSucursalesItems == null)
				this.listSucursalesItems = new ArrayList<SelectItem>();
			this.listSucursalesItems.clear();
			
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSucursales = this.ifzSucursal.findLikePropiedad("sucursal", ""); 
			if (this.listSucursales != null && ! this.listSucursales.isEmpty()) {
				for (Sucursal var : this.listSucursales)
					this.listSucursalesItems.add(new SelectItem(var.getId(), var.getSucursal()));
			}
		} catch (Exception e) {
			log.error("error en cargarSucursales", e);
		}
   	}
   	
	private void cargarFormasDePago() {
		try {
			if (this.listFormasPago == null)
				this.listFormasPago = new ArrayList<ConValores>();
			this.listFormasPago.clear();
			
			if (this.listFormasPagoItems == null)
				this.listFormasPagoItems = new ArrayList<SelectItem>();
			this.listFormasPagoItems.clear();
			
			this.listFormasPago = this.ifzConValores.findAll(this.pojoGpoValFormasPago);
			if (this.listFormasPago != null && ! this.listFormasPago.isEmpty()) {
				for (ConValores fp : this.listFormasPago) 
					this.listFormasPagoItems.add(new SelectItem(fp.getValor(), fp.getDescripcion()));
			}
		} catch(Exception e) {
			control("Ocurrio un problema al consultar las Formas de Pago", e);
		}
	}

	private void cargarCuentas() {
   		try {
			if (this.listCuentas == null)
				this.listCuentas = new ArrayList<CuentaBancaria>();
			this.listCuentas.clear();
			
			if (this.listCuentasItems == null)
				this.listCuentasItems = new ArrayList<SelectItem>();
			this.listCuentasItems.clear();

			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCuentas = this.ifzCtas.findLikeClaveNombreCuenta("", 0, this.sucursalesVisibles, 1L);
			if (this.listCuentas != null && ! this.listCuentas.isEmpty()) {
				for (CuentaBancaria var : this.listCuentas)
					this.listCuentasItems.add(new SelectItem(var.getId(), var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta()));
			}
		} catch (Exception e) {
			log.error("error en cargarCuentas", e);
		}
   	}
	
	@SuppressWarnings("unused")
	private boolean validaRequest(String param, String refValue) {
		String valParam = "";
		
		if (! this.isDebug)
			return false;
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		valParam = this.paramsRequest.get(param);
		if (NumberUtils.isNumber(valParam))
			return ("1".equals(valParam));
		else
			return (valParam.equals(refValue));
	}
	
	private void control() {
		this.band = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, 1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		this.band = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		
		log.error("\n\n" + this.usuario + " :: GASTOS A COMPROBAR :: " + this.tipoMensaje + " - " + this.mensaje, throwable);
		//backtracePrint();
	}
	
	// ----------------------------------------------------------
	// BUSQUEDA DE OBRAS
	// ----------------------------------------------------------
	
	public void nuevaBusquedaObras() {
		control();
		this.campoBusquedaObra = this.tiposBusquedaObra.get(0).getValue().toString();
		this.valorBusquedaObra = "";
		this.tipoObra = "O";
		this.toggleBusquedaObraAvanzada = false;
		this.paginacionBusquedaObra = 1;
		if (this.listObras == null)
			this.listObras = new ArrayList<Obra>();
		this.listObras.clear();
		this.pojoObra = null;
	}
	
	public void buscarObras(){
		try {
			control();
			if (this.campoBusquedaObra == null || "".equals(this.campoBusquedaObra))
				this.campoBusquedaObra = tiposBusquedaObra.get(0).getValue().toString();
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valorBusquedaObra, "", 0);
			if (this.listObras == null || this.listObras.isEmpty())
				control(12, "Busqueda sin resultados");
		} catch(Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
		} finally {
			this.paginacionBusquedaObra = 1;
			if (this.listObras == null)
				this.listObras = new ArrayList<Obra>();
		}
	}

	public void seleccionarObra() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(1, "Ocurrio un problema al recuperar la Obra seleccionada");
				return;
			}
			
			// Asignamos la obra al Gasto a Comprobar
			this.pojoGtosComp.setIdObra(this.pojoObra);
			nuevaBusquedaObras();
		} catch(Exception e) {
			log.error("Ocurrio un problema al asignar la Obra seleccionada al Gasto", e);
		}
	}
	
	public void quitarObra() {
		try {
			control();
			this.pojoGtosComp.setIdObra(null);
			nuevaBusquedaObras();
		} catch(Exception e) {
			log.error("Ocurrio un problema al quitar la Obra asignada", e);
		}
	}
	
	// ----------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------
	
	public String getTitulo() {
		if (this.pojoGtosComp != null && this.pojoGtosComp.getId() != null && this.pojoGtosComp.getId() > 0L)
			return "Editar Gasto a Comprobar " + this.pojoGtosComp.getId();
		return "Nuevo Gasto a Comprobar";
	}
	
	public void setTitulo(String value) {}
	
	public String getObra() {
		if (this.pojoGtosComp != null && this.pojoGtosComp.getIdObra() != null && this.pojoGtosComp.getIdObra().getId() != null && this.pojoGtosComp.getIdObra().getId() > 0L)
			return this.pojoGtosComp.getIdObra().getId() + " - " + this.pojoGtosComp.getIdObra().getNombre();
		return "";
	}
	
	public void setObra(String value) {}
	
   	public boolean getPermiteEditar() {
   		return true;
   	}
   	
   	public void setPermiteEditar(boolean value) {}
   	
	public ConGrupoValores getPojoGpoValTerceros() {
		return pojoGpoValTerceros;
	}

	public void setPojoGpoValTerceros(ConGrupoValores pojoGpoValTerceros) {
		this.pojoGpoValTerceros = pojoGpoValTerceros;
	}

	public ConGrupoValores getPojoGpoVal() {
		return pojoGpoValPersonas;
	}

	public void setPojoGpoVal(ConGrupoValores pojoGpoVal) {
		this.pojoGpoValPersonas = pojoGpoVal;
	}

	public PagosGastosExt getPojoGtosComp() {
		return pojoGtosComp;
	}

	public void setPojoGtosComp(PagosGastosExt pojoGtosComp) {
		this.pojoGtosComp = pojoGtosComp;
	}

	public long getPagosGastosId() {
		if (this.pojoGtosComp != null && this.pojoGtosComp.getId() != null && this.pojoGtosComp.getId() > 0L)
			return this.pojoGtosComp.getId();
		return 0L;
	}

	public void setPagosGastosId(long value) { }

	public Date getFecha() {
		if (this.pojoGtosComp != null && this.pojoGtosComp.getFecha() != null)
			return this.pojoGtosComp.getFecha();
		return Calendar.getInstance().getTime();
	}

	public void setFecha(Date fecha) {
		this.pojoGtosComp.setFecha(fecha);
	}

	public String getOperacion() {
		return this.operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getReferenciaOperacion() {
		return referenciaOperacion;
	}

	public void setReferenciaOperacion(String referenciaOperacion) {
		this.referenciaOperacion = referenciaOperacion;
	}

	public long getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public double getMonto() {
		return this.pojoGtosComp.getMonto() != null ? this.pojoGtosComp.getMonto() : 0;
	}

	public void setMonto(double monto) {
		this.pojoGtosComp.setMonto(monto);
	}

	public String getConcepto() {
		return this.pojoGtosComp.getConcepto()!=null ?  this.pojoGtosComp.getConcepto():"";
	}

	public void setConcepto(String concepto) {
		this.pojoGtosComp.setConcepto(concepto);
	}
	
	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getTipoPersona() {
		return this.tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
		this.beneficiario = "";
	}

	public long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public List<CuentaBancaria> getListCuentas() {
		return listCuentas;
	}

	public void setListCuentas(List<CuentaBancaria> listCuentas) {
		this.listCuentas = listCuentas;
	}

	public List<SelectItem> getListCuentasItems() {
		return listCuentasItems;
	}

	public void setListCuentasItems(List<SelectItem> listCuentasItems) {
		this.listCuentasItems = listCuentasItems;
	}

	public CtasBancoExt getPojoCtas() {
		return pojoCtas;
	}

	public void setPojoCtas(CtasBancoExt pojoCtas) {
		this.pojoCtas = pojoCtas;
	}

	public PersonaExt getPojoBeneficiarios() {
		return pojoBeneficiarios;
	}

	public void setPojoBeneficiarios(PersonaExt pojoBeneficiarios) {
		this.pojoBeneficiarios = pojoBeneficiarios;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}
	
	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}
	
	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}
	
	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public long getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public boolean isMensajeError() {
		return mensajeError;
	}
	
	public void setMensajeError(boolean mensajeError) {
		this.mensajeError = mensajeError;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public List<String> getListTipoBusqueda() {
		return listTipoBusqueda;
	}

	public void setListTipoBusqueda(List<String> listTipoBusqueda) {
		this.listTipoBusqueda = listTipoBusqueda;
	}

	public List<Persona> getListBeneficiarios() {
		return listBeneficiarios;
	}

	public void setListBeneficiarios(List<Persona> listBeneficiarios) {
		this.listBeneficiarios = listBeneficiarios;
	}

	public List<Sucursal> getListSucursales() {
		return listSucursales;
	}

	public void setListSucursales(List<Sucursal> listSucursales) {
		this.listSucursales = listSucursales;
	}

	public List<PagosGastosExt> getListGtosComp() {
		return listGtosComp;
	}

	public void setListGtosComp(List<PagosGastosExt> listGtosComp) {
		this.listGtosComp = listGtosComp;
	}

	public String getTerceros() {
		return terceros;
	}

	public void setTerceros(String terceros) {
		this.terceros = terceros;
	}

	public boolean isEsTransferencia() {
		return esTransferencia;
	}

	public void setEsTransferencia(boolean esTransferencia) {
		this.esTransferencia = esTransferencia;
	}

	public boolean isEsCheque() {
		return esCheque;
	}

	public void setEsCheque(boolean esCheque) {
		this.esCheque = esCheque;
	}

	public boolean isEsSpei() {
		return esSpei;
	}

	public void setEsSpei(boolean esSpei) {
		this.esSpei = esSpei;
	}

	public boolean isClicSalvar() {
		return clicSalvar;
	}

	public void setClicSalvar(boolean clicSalvar) {
		this.clicSalvar = clicSalvar;
	}

	public boolean isBand() {
		return band;
	}

	public void setBand(boolean band) {
		this.band = band;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public boolean isGastoCancelado() {
		return gastoCancelado;
	}

	public void setGastoCancelado(boolean gastoCancelado) {
		this.gastoCancelado = gastoCancelado;
	}		

	public List<Area> getListAreas() {
		return listAreas;
	}

	public void setListAreas(List<Area> listAreas) {
		this.listAreas = listAreas;
	}

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public List<SelectItem> getTiposBusquedaObra() {
		return tiposBusquedaObra;
	}

	public void setTiposBusquedaObra(List<SelectItem> tiposBusquedaObra) {
		this.tiposBusquedaObra = tiposBusquedaObra;
	}

	public String getCampoBusquedaObra() {
		return campoBusquedaObra;
	}

	public void setCampoBusquedaObra(String campoBusquedaObra) {
		this.campoBusquedaObra = campoBusquedaObra;
	}

	public String getValorBusquedaObra() {
		return valorBusquedaObra;
	}

	public void setValorBusquedaObra(String valorBusquedaObra) {
		this.valorBusquedaObra = valorBusquedaObra;
	}

	public String getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(String tipoObra) {
		this.tipoObra = tipoObra;
	}

	public int getPaginacionBusquedaObra() {
		return paginacionBusquedaObra;
	}

	public void setPaginacionBusquedaObra(int paginacionBusquedaObra) {
		this.paginacionBusquedaObra = paginacionBusquedaObra;
	}
	
	public boolean getToggleBusquedaObraAvanzada() {
		return this.toggleBusquedaObraAvanzada;
	}
	
	public void setToggleBusquedaObraAvanzada(boolean toggleBusquedaObraAvanzada) {
		this.toggleBusquedaObraAvanzada = toggleBusquedaObraAvanzada;
	}
	
	public boolean getBuscarAdministrativo() {
		return (this.tipoObraBusquedaObra == 4);
	}
	
	public void setBuscarAdministrativo(boolean tipoObraBusquedaObra) {
		this.tipoObraBusquedaObra = tipoObraBusquedaObra ? 4 : 0;
	}

	public boolean getEsAdministrativo() {
		return perfilEgresos;
	}
	
	public void setEsAdministrativo(boolean perfilEgresos) {
		this.perfilEgresos = perfilEgresos;
	}
	
	public String getPerfilRequiereObra() {
		return perfilRequiereObra;
	}

	public void setPerfilRequiereObra(String perfilRequiereObra) {
		this.perfilRequiereObra = perfilRequiereObra;
	}
	
	public List<SelectItem> getListFormasPagoItems() {
		return listFormasPagoItems;
	}

	public void setListFormasPagoItems(List<SelectItem> listFormasPagoItems) {
		this.listFormasPagoItems = listFormasPagoItems;
	}

	public List<SelectItem> getListSucursalesItems() {
		return listSucursalesItems;
	}

	public void setListSucursalesItems(List<SelectItem> listSucursalesItems) {
		this.listSucursalesItems = listSucursalesItems;
	}

	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
}

// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
//  VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//    1.1   | 2016-09-30 | Javier Tirado      | Se añade la carga de tipos de pago
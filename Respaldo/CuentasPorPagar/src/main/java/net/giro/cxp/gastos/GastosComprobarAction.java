package net.giro.cxp.gastos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.SucursalExt;
import net.giro.cxp.logica.AreaRem;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
//import net.giro.ne.logica.NQueryRem;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Area;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.logica.CtasBancoRem;

public class GastosComprobarAction {
	private static Logger log = Logger.getLogger(GastosComprobarAction.class);
	private PropertyResourceBundle entornoProperties;
	private FacesContext facesContext;
	private HttpSession httpSession;
	private InitialContext ctx;
	
	private PersonaExt pojoTerceros; // Tygpersonas 
	private ConGrupoValores pojoGpoValPersonas;
	private ConGrupoValores pojoGpoValTerceros;
	private PagosGastosExt pojoGtosComp; // MovimientosCuentas
	private PagosGastos pojoGastoComprobar; // MovimientosCuentas
	private CtasBancoExt pojoCtas;
	private SucursalExt pojoSucursal; // SucursalesView
	private PersonaExt pojoBeneficiarios; // Tygpersonas
	//private SpeiOutgoing pojoSpei;
	private PagosGastosRem ifzGtosComp; // MovimientosCuentasFacadeRemote
	private CtasBancoRem ifzCtas; // CtasBancoFacadeRemote
	private SucursalesRem ifzSucursal; // SucursalesViewFacadeRemote
	private PersonaRem ifzBenefi; // TygpersonasFacadeRemote
	private AreaRem 	ifzAreas; // --> Area::Model_RecHum
	//private NQueryRem ifzQuery; // NQueryFacadeRemote
	private ReportesRem	ifzReportes;
	//private SpeiOutgoingFacadeRemote ifzSpei;
	private List<Persona> listTerceros;
	private List<Persona> listBeneficiarios;
	private List<CtasBanco> listCuentas;
	private List<Sucursal> listSucursales;
	private List<PagosGastosExt> listGtosComp;	
	private List<String>	listTipoBusqueda;
	private List<Area> listAreas;
	private ConGrupoValoresRem ifzGpoVal; 	
	private ConValoresRem ifzConValores; 	
	private ConGrupoValores pojoGpoValFormasPago; 
	private List<ConValores> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	//private  Long valGpoPersonas;
	//private  Long valGpoTerceros;
	private Long segNeg;
	private String nombreBeneficiario;
	private String terceros;
	private String operacion;	
	private String beneficiario;
	private String tipoPersona;
	private String sucursal;	
	private String cuenta;
	private String campoBusqueda;	
	private String valTipoBusqueda;
	private String mensaje;
	private String sucursalesVisibles;
	private String segmento;
	private long usuarioId;
	private String usuario;
	private boolean band;
	private boolean mensajeError;
	private boolean esTransferencia;
	private boolean esCheque;
	private boolean esSpei;
	private boolean clicSalvar;
	private boolean gastoCancelado;
	private boolean muestraSeg;
	private int 	numPagina;
	private int 	tipoMensaje;
	// Busqueda de OBRAS
	private ObraRem ifzObras;
	private Obra pojoObra;
	private List<Obra> listObras;
	private List<String> tipoBusquedaObra;
	private String valTipoBusquedaObra;
	private String campoBusquedaObra;
	private String tipoObra;
	
	private String perfilRequiereObra;

	
	public GastosComprobarAction() throws NamingException, Exception{
		long idGrupoValor = 0;
		/*Hashtable<String, Object> p = new Hashtable<String, Object>();
        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        
		this.ctx = new InitialContext(p);*/
		this.ctx = new InitialContext();
		
		this.segNeg 			= 0L;
		this.muestraSeg 		= false;
		this.pojoTerceros 		= new PersonaExt();
		this.pojoGpoValTerceros = new ConGrupoValores();
		this.pojoGpoValPersonas = new ConGrupoValores();
		this.pojoGtosComp 		= new PagosGastosExt();		
		this.pojoSucursal 		= new SucursalExt();
		this.pojoCtas 			= new CtasBancoExt();
		this.pojoBeneficiarios	= new PersonaExt();
		this.listTerceros 		= new ArrayList<Persona>();
		this.listGtosComp 		= new ArrayList<PagosGastosExt>();
		this.listSucursales 	= new ArrayList<Sucursal>();
		this.listCuentas 		= new ArrayList<CtasBanco>();
		this.listBeneficiarios 	= new ArrayList<Persona>();
		this.listTipoBusqueda	= new ArrayList<String>();
		this.listAreas 			= new ArrayList<Area>();
		//this.pojoSpei = new SpeiOutgoing();
		
		this.numPagina = 1;

		this.ifzAreas 	 = (AreaRem) 		this.ctx.lookup("ejb:/Logica_CuentasPorPagar//AreaFac!net.giro.cxp.logica.AreaRem");
		this.ifzGtosComp = (PagosGastosRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
		this.ifzBenefi 	 = (PersonaRem) 	this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
		this.ifzObras 	 = (ObraRem) 		this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
		this.ifzSucursal = (SucursalesRem) 	this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
		//this.ifzQuery 	 = (NQueryRem) 		this.ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
		this.ifzCtas 	 = (CtasBancoRem) 	this.ctx.lookup("ejb:/Logica_TYG//CtasBancoFac!net.giro.tyg.logica.CtasBancoRem");
		this.ifzGpoVal 	 = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
		this.ifzConValores = (ConValoresRem ) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
		//this.ifzPolizas = (PolizasContablesRem) ctx.lookup("ejb:/Logica_Factoraje//PolizasContablesFac!net.giro.factoraje.logica.PolizasContablesRem");
		//this.ifzSpei = (SpeiOutgoingRem) ctx.lookup("ejb:/Logica_RecHum//AreasFac!net.giro.rh.logica.SpeiOutgoingRem");
		this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");

		gastoCancelado=false;
		mensaje = "";
		mensajeError = false;
		band=false;
		tipoMensaje = 0;
		clicSalvar=false;
		listTipoBusqueda.add("Beneficiario");	
		listTipoBusqueda.add("Descripción del gasto");
		valTipoBusqueda = listTipoBusqueda.get(0);
		
		tipoBusquedaObra = new ArrayList<String>();
		tipoBusquedaObra.add("Nombre");
		tipoBusquedaObra.add("Id de Obra");
		valTipoBusquedaObra = tipoBusquedaObra.get(0);
		campoBusquedaObra = "";
		tipoObra = "O";
	
		facesContext =  FacesContext.getCurrentInstance();
		httpSession =(HttpSession) facesContext.getExternalContext().getSession(false);
		
		Application app = facesContext.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager)ve.getValue(facesContext.getELContext());
		
		this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId(); // getLoginBean().getUsuario().getUsuarioId();
		this.usuario = lm.getUsuarioResponsabilidad().getUsuario().getUsuario();
		this.perfilRequiereObra = lm.getAutentificacion().getPerfil("REQUIERE_OBRA");
		//sucursalesVisibles = lm.getLoginBean().getSucursalesVisibles();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		this.entornoProperties = (PropertyResourceBundle)dato.getValue(facesContext.getELContext());
		
		if (entornoProperties.getString("SYS_FORMAS_PAGO") == null || "".equals(entornoProperties.getString("SYS_FORMAS_PAGO")) )
			throw new Exception("No se encontro encontro el grupo SYS_FORMAS_PAGO en con_grupo_valores");
		else
			idGrupoValor = Long.valueOf(entornoProperties.getString("SYS_FORMAS_PAGO"));
		this.pojoGpoValFormasPago = this.ifzGpoVal.findById(idGrupoValor);
		cargarFormasDePago();
			
		/*if ( entornoProperties.getString("SYS_RELPER") == null || "".equals(entornoProperties.getString("SYS_RELPER")) )
			throw new Exception("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
		else
			this.valGpoPersonas = Long.valueOf(entornoProperties.getString("SYS_RELPER")) ;
		
		this.pojoGpoValPersonas = this.ifzGpoVal.findById(valGpoPersonas);
		
		if (this.pojoGpoValPersonas==null){
			throw new Exception("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
		}
		
		
		if ( entornoProperties.getString("SYS_CUENTASTERCEROS") == null || "".equals(entornoProperties.getString("SYS_CUENTASTERCEROS")) )
			throw new Exception("No se encontro encontro el grupo SYS_CUENTASTERCEROS en con_grupo_valores");
		else
			this.valGpoTerceros = Long.valueOf(entornoProperties.getString("SYS_CUENTASTERCEROS")) ;
		
		this.pojoGpoValTerceros = this.ifzGpoVal.findById(valGpoTerceros);
		
		if (this.pojoGpoValTerceros==null){
			throw new Exception("No se encontro encontro el grupo SYS_CUENTASTERCEROS en con_grupo_valores");
		}
		
		dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		entornoProperties = (PropertyResourceBundle)dato.getValue(facesContext.getELContext());*/
	}
	

	public String buscar(){
		try {
			this.band = false;
			this.mensaje = "";
			this.tipoMensaje = 0;
			
			if( "Beneficiario".equals(this.valTipoBusqueda))					
				this.listGtosComp = this.ifzGtosComp.findLikeGtosComprobar("beneficiario", this.campoBusqueda, "G", "G", 50, this.sucursalesVisibles);	
			else {
				if("Descripción del gasto".equals(this.valTipoBusqueda))
					this.listGtosComp = this.ifzGtosComp.findLikeGtosComprobar("concepto", this.campoBusqueda, "G", "G", 50, this.sucursalesVisibles);				
				else
					this.listGtosComp = this.ifzGtosComp.findLikeGtosComprobar("beneficiario", this.campoBusqueda, "G", "G", 50, this.sucursalesVisibles);
			}

			if (listGtosComp.isEmpty()) {
				tipoMensaje = 6;
				band = true;
			}
		} catch (Exception e) {
			log.error("error en buscar", e);
			return "ERROR";
		}
		
		return "OK";
	}	
	
	public String nuevo() {
		try{	
			
			this.pojoGtosComp = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoSucursal = new SucursalExt();
			this.pojoBeneficiarios = new PersonaExt();
			this.pojoTerceros = new PersonaExt();
			//this.pojoSpei = new SpeiOutgoing();
			
			this.sucursal="";
			this.cuenta="";
			this.beneficiario="";
			this.terceros="";			
			this.campoBusqueda="";
			this.mensaje = "";
			this.tipoPersona="P";	
			this.operacion=this.operacionValidate("C");
			this.esTransferencia=false;
			this.esCheque=true;
			this.esSpei=false;
			this.clicSalvar=false;			
			this.mensajeError = false;
			this.segmento="";
			this.muestraSeg = false;
			this.pojoObra = null;
		}catch(Exception e){
			log.error("Error en el metodo nuevo.", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public String editar(){
		try{			
			this.clicSalvar = true;	
			this.sucursal = this.pojoGtosComp.getIdSucursal() + " - " + this.pojoGtosComp.getIdSucursal().getSucursal();
			this.cuenta = this.pojoGtosComp.getIdCuentaOrigen().getId() + " - " + this.pojoGtosComp.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto() + " - " + pojoGtosComp.getIdCuentaOrigen().getNumeroDeCuenta();
			this.beneficiario = this.pojoGtosComp.getIdBeneficiario().getId() + " - "+this.pojoGtosComp.getBeneficiario();
			this.tipoPersona = this.pojoGtosComp.getTipoBeneficiario(); // this.pojoGtosComp.getIdBeneficiario().getTipoPersona().toString();
			this.operacion = this.pojoGtosComp.getOperacion();
			
			if("T".equals(this.operacion)){
				this.terceros= pojoGtosComp.getIdCuentaDestinoTerceros().getId() + " - " + pojoGtosComp.getIdCuentaDestinoTerceros().getNombre()+" "+ pojoGtosComp.getIdCuentaDestinoTerceros().getPrimerApellido() + " "+ pojoGtosComp.getIdCuentaDestinoTerceros().getSegundoApellido() + " " + pojoGtosComp.getIdCuentaDestinoTerceros().getNumeroCuenta();
				this.esTransferencia=true;	
			}else
				this.esTransferencia=false;
			
			this.esCheque="C".equals(this.operacion); 
			
			/*
			this.pojoGtosComp = pojoGtosComp;
			this.sucursal=this.pojoGtosComp.getIdSucursal() + " - " +this.pojoGtosComp.getIdSucursal().getSucursal();
			this.cuenta=this.pojoGtosComp.getIdCuentaOrigen().getId() + " - " + this.pojoGtosComp.getIdCuentaOrigen().getNumeroDeCuenta();
			this.beneficiario=this.pojoGtosComp.getIdBeneficiario().getId() + " - "+this.pojoGtosComp.getBeneficiario();
			this.tipoPersona=this.pojoGtosComp.getIdBeneficiario().getTipoPersona().toString();
			this.operacion=this.pojoGtosComp.getOperacion();
			
			if("T".equals(this.operacion)){
				this.terceros=this.pojoGtosComp.getIdCuentaDestinoTerceros().getId() + " - " + this.pojoGtosComp.getIdCuentaDestinoTerceros().getNombre()+" "+ this.pojoGtosComp.getIdCuentaDestinoTerceros().getPrimerApellido() + " "+this.pojoGtosComp.getIdCuentaDestinoTerceros().getSegundoApellido();//+" "+ this.pojoGtosComp.getIdCuentaDestinoTerceros().getCuentaContable();
				this.esTransferencia=true;	
			}else
				this.esTransferencia=false;
			
			this.esCheque="C".equals(this.operacion);
			*/
		} catch(Exception e) {			
			log.error("error en editar", e);
			return "ERROR";
		}
		
		return "OK";
	}	
	
	public String guardar() {
		try{
			//ifzPolizas.PolizaTransferencias();
			Pattern pat = null;
			Matcher match = null;
			boolean  registroNuevo = false;
			Respuesta resp = new Respuesta();
			this.tipoMensaje = 0;
			this.mensaje = "";
			band = false;
			
			this.operacion = operacionValidate(this.operacion);

			if(this.pojoGtosComp.getId() == null)
				registroNuevo = true;
			
			this.pojoGtosComp.setModificadoPor(this.usuarioId);
			this.pojoGtosComp.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoGtosComp.setTipoBeneficiario(this.tipoPersona);
			
			if (this.pojoObra != null) {
				if (this.pojoGtosComp.getIdObra() == null) {
					this.pojoGtosComp.setIdObra(this.pojoObra);
				} else {
					if (this.pojoGtosComp.getIdObra().getId() != null && this.pojoGtosComp.getIdObra().getId() != this.pojoObra.getId())
						this.pojoGtosComp.setIdObra(this.pojoObra);
				}
			}

			//validando sucursal
			pat = Pattern.compile("(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.sucursal);
			if(match.find()) 
				if(registroNuevo)
					this.pojoSucursal = this.ifzGtosComp.findSucursalById(Long.valueOf(match.group(1))); // this.pojoSucursal = this.ifzSucursal.findById(Long.valueOf(match.group(1)));
				else
					if(this.pojoSucursal.getId() != Long.valueOf(match.group(1)))
						this.pojoSucursal = this.ifzGtosComp.findSucursalById(Long.valueOf(match.group(1))); // this.pojoSucursal = this.ifzSucursal.findById(Long.valueOf(match.group(1)));
			
			this.pojoGtosComp.setIdSucursal(this.pojoSucursal);
					
			//validando segmento
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.segmento);
			if(match.find())
				this.segNeg = Long.valueOf(match.group(1));
			this.pojoGtosComp.setSegmento(segNeg);
			
			//validando Cuenta
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.cuenta);
			if(match.find()) 
				if(registroNuevo)				
					this.pojoCtas = this.ifzGtosComp.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.pojoCtas = this.ifzCtas.findById(Long.valueOf(match.group(1)));
				else
					if (this.pojoCtas.getId() != Long.valueOf(match.group(1)))
						this.pojoCtas = this.ifzGtosComp.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.pojoCtas = this.ifzCtas.findById(Long.valueOf(match.group(1)));

			this.pojoGtosComp.setIdCuentaOrigen(this.pojoCtas);
			
			if (! "OK".equals(validaSaldoCuentaPropios())) {
				this.operacion = operacionValidate(this.operacion);
				return "ERROR";
			}
			
			//validando beneficiario
			match = pat.matcher(this.beneficiario);
			if(match.find())
				if(registroNuevo) 
					this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.pojoBeneficiarios = this.ifzBenefi.findById(Long.valueOf(match.group(1))); // findByIdPojoCompleto(Long.valueOf(match.group(1)));
				else
					if(this.pojoBeneficiarios.getId() != Long.valueOf(match.group(1)))
						this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.pojoBeneficiarios = this.ifzBenefi.findById(Long.valueOf(match.group(1))); // findByIdPojoCompleto(Long.valueOf(match.group(1)));
			
			this.pojoGtosComp.setIdBeneficiario(this.pojoBeneficiarios);
			
			/*List<PagosGastos> valida = this.ifzGtosComp.findLikeGtosPorComprobarPersona(this.pojoBeneficiarios, this.pojoSucursal, "G", "G", this.pojoBeneficiarios.getRelacionId().getDescripcion(), null, "", 10);
			if(valida != null && !valida.isEmpty()){
				band = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 8;
				return "ERROR";
				
			}*/
			
			//validando operacion para mostrar catalodo de Terceros
			if("T".equals(this.operacion) || "S".equals(this.operacion)){
				match = pat.matcher(this.beneficiario);			
				if(match.find())
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
			this.pojoGtosComp.setOperacion(this.operacion);
			this.clicSalvar = true;

			if( registroNuevo ){
				if("S".equals(this.operacion)){
					/*String sql = "select spei_autorizacion(3, current_date, "+usuarioId+", 6, "+this.pojoSucursal.getId()+", null, "+ this.pojoGtosComp.getMonto() +")";
					List<Object> listObj = this.ifzQuery.findNativeQuery(sql);
					String Id = String.valueOf(listObj.get(0).toString());
					if (Id.equals("")){
						this.mensaje = "ERROR DESCONOCIDO";
						return "OK";
					}else{
						if (Id.substring(0, 1).equals("0") || Id.substring(0, 1).equals("1")){
							CtasBanco pojoCtaSpei = new CtasBanco();
							pojoCtaSpei = this.ifzCtas.findAllById(Short.valueOf("659"));
							
							sql = "select spei_referencia()";
							listObj = this.ifzQuery.findNativeQuery(sql);
							String ref = String.valueOf(listObj.get(0).toString());
							if (ref.equals("")){
								this.mensaje = "ERROR DESCONOCIDO";
								return "OK";
							}else{
								this.mensaje = "NOT_IMPLEMENTED";*/
								/*this.pojoSpei.setNombreOrdenante(pojoCtaSpei.getId().getNombreLargo().substring(0, pojoCtaSpei.getCatBancoId().getNombreLargo().length()<40?pojoCtaSpei.getCatBancoId().getNombreLargo().length():40)); 
								this.pojoSpei.setIdTipoCuentaOrdenante("40");
								this.pojoSpei.setCuentaOrdenante(pojoCtaSpei.getClabe());
								this.pojoSpei.setRfcOrdenante(pojoCtaSpei.getEmpresa().getRfcEmp());
								this.pojoSpei.setNombreBeneficiario(this.nombreBeneficiario.substring(0, this.nombreBeneficiario.length()<40?this.nombreBeneficiario.length():40));
								this.pojoSpei.setIdTipoCuentaBeneficiario("40");
								this.pojoSpei.setCuentaBeneficiario(this.pojoTerceros.getClabe());
								this.pojoSpei.setRfcBeneficiario(this.pojoTerceros.getRfc());
								//this.pojoSpei.setIdInstitucionBen(this.pojoTerceros.getCatBancoId().getSpeiInstitucionesId().getIdInstitucion());
								this.pojoSpei.setMonto(this.pojoGtosComp.getMonto());
								this.pojoSpei.setIva(0D);
								this.pojoSpei.setReferenciaNumerica(Integer.parseInt(ref));
								this.pojoSpei.setIdTipoPago("1");
								this.pojoSpei.setStatus("");
								this.pojoSpei.setStatusOperacion(0);
								this.pojoSpei.setClaveRastreo("0");
								this.pojoSpei.setIdDevolucion(0);
								this.pojoSpei.setFechaCaptura(Calendar.getInstance().getTime());
								
								this.pojoSpei.setUsuarioId(this.usuarioId);
								this.pojoSpei.setTipoOperacion(1);
								this.pojoSpei.setApp(6);
								this.pojoSpei.setConceptoPago(this.pojoGtosComp.getConcepto().substring(0, this.pojoGtosComp.getConcepto().length()<250?this.pojoGtosComp.getConcepto().length():250)); 
								this.pojoSpei.setEnvioAutomatico(Id.substring(0, 1));*/
							/*}
						}else{
							this.mensaje = "ERROR: " + Id;
							return "OK";
						}
					}*/
				}
									
				this.pojoGtosComp.setCreadoPor(this.usuarioId);
				this.pojoGtosComp.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoGtosComp.setTipo("G");//gasto
				this.pojoGtosComp.setEstatus("G");//generado
				
				// Asignamos consecutivo
				int consecutivo = this.ifzGtosComp.findConsecutivoByBeneficiario(this.pojoGtosComp.getIdBeneficiario().getId(), "G", "G");
				this.pojoGtosComp.setConsecutivo(consecutivo);
				
				// Asignamos AREA
				this.pojoGtosComp.setArea((this.perfilRequiereObra == "S" && this.pojoObra != null ? "Obra" : "Oficina"));
				
				// Asignamos monto limite
				this.pojoGtosComp.setMontoLimite(0D);
				
				// Guardamos
				resp = this.ifzGtosComp.salvar(this.pojoGtosComp, true, 1L); // resp = this.ifzGtosComp.salvar(this.pojoGtosComp, true, null);
				
				//validando folio cheque
				if(resp.getResultado() != -1){
					this.pojoGtosComp.setId(Long.valueOf(resp.getReferencia()));
					mensaje="BIEN";
					if("S".equals(this.operacion)) {
						/*this.pojoSpei.setIdOperacion(this.pojoGtosComp.getPagosGastosId().intValue());
						this.pojoSpei .setIdSpeiOutgoing(ifzSpei.save(this.pojoSpei));
						this.pojoGtosComp.setIdSpeiOutgoing(this.pojoSpei);
						ifzGtosComp.update(pojoGtosComp);*/
					}
					
					if("T".equals(operacion) || "S".equals(operacion)) {// para que no salga el cheque si es una transferencia
						mensaje = "";
					} else {
						//impresion del cheque
						/*HashMap listaParam;
						byte[] datosRep = null;
					
						listaParam = new HashMap<String, String>();
						listaParam.put("folio", this.pojoGtosComp.getNoCheque());						
						listaParam.put("banco_id", this.pojoGtosComp.getIdCuentaOrigen()); // listaParam.put("banco_id", this.pojoGtosComp.getCuentaOrigen().getCtaBancoId());				
						datosRep = ifzReporte..generaReporte("cheque.jasper", listaParam);
						httpSession.setAttribute("reporte", datosRep);
						httpSession.setAttribute("tipoReporte", "pdf");*/
					}
				} else {
					if(! "BIEN".equals(resp.getRespuesta())) {
						band = true;// esta variable termine usandola como bandera gral
						this.mensaje = resp.getRespuesta();
						
						if((resp.getRespuesta().indexOf("SE CANCELA EL FOLIO ") > -1) || (resp.getRespuesta().indexOf("SE CANCELAN LOS FOLIOS ") > -1))
							this.tipoMensaje = 2;
						else
							this.tipoMensaje = 1;
						
						this.operacion = operacionValidate(this.operacion);
						return "ERROR";
					}
				}
				
				// Agregamos a la lista
				this.listGtosComp.add(this.pojoGtosComp);				
				
				if("S".equals(this.operacion)) {
					/*String sClave = "";									
					this.tipoMensaje=1;
					
					HashMap listaParam;
					byte[] datosRep = null;
					
					listaParam = new HashMap<String, String>();
					listaParam.put("id", this.pojoSpei.getIdSpeiOutgoing().longValue());						
					datosRep = ifzReporte.generaReporte("recibo_spei.jasper", listaParam);
					httpSession.setAttribute("reporte", datosRep);
					httpSession.setAttribute("tipoReporte", "pdf");
										
					for (int i= 0;  i <= 15; i++ ) {
						Thread.sleep(1000);
						String sql = "Select * From spei_obten_respuesta_gastos("+this.pojoSpei.getIdSpeiOutgoing().toString()+");";
						List<Object> listObj = this.ifzQuery.findNativeQuery(sql);
						sClave = String.valueOf(listObj.get(0).toString());
						
						if (!sClave.equals("OK")) {
							this.mensaje = "TRANSFERENCIA SPEI AUTORIZADA: " + sClave ;
							return "OK";							
						}
					}
					this.mensaje = "TRANSFERENCIA SPEI EN ESPERA DE AUTORIZACION";						
					return "OK";*/
				}
			} else {
				//this.ifzGtosComp.update(this.pojoGtosComp);		
				for(PagosGastosExt var:this.listGtosComp){
					if(var.getId().equals(this.pojoGtosComp.getId()))
						var = this.pojoGtosComp;
				}
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			this.pojoGastoComprobar = this.ifzGtosComp.convertir(this.pojoGtosComp);
			mensajeTransaccion();
		} catch(Exception e) {
			band=true;
			mensaje="";
			tipoMensaje=5;
			log.error("error en guardar", e);
			this.operacion = operacionValidate(this.operacion);
			return "ERROR";
		}
		
		return "OK";
	}

	public String cancelar(){
		try{
			Respuesta resp = new Respuesta();
			resp = ifzGtosComp.cancelacion(this.pojoGtosComp, Calendar.getInstance().getTime(),Short.valueOf(String.valueOf(this.usuarioId)), null);
				
			if(resp.getResultado() == -1){
				band = true;
				this.mensaje = resp.getRespuesta();
				this.tipoMensaje=1;
			} else {
				for(PagosGastosExt var:this.listGtosComp){				
					if(var.getId().equals(this.pojoGtosComp.getId())){		
						var.setEstatus("X");
						break;
					}
				}	
			}
		}catch(Exception e){
			band=true;
			mensaje="";
			tipoMensaje=5;
			log.error("error en cancelar", e);
			return "ERROR";
		}
		
		return "OK";	
	}
	
	public String validaSaldoCuentaPropios (){
		try{
			band = false;
			tipoMensaje = 0;
			mensaje = "";

			if(pojoGtosComp.getMonto() <= 0){
				tipoMensaje = 3;					
				band = true;	
				mensaje = "";
				return "ERROR";
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
		}catch(Exception e){
			log.error("Error en el metodo validaSaldoCuentaPropios.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public void mensajeTransaccion() {
		try {
			this.band = false;
			this.mensaje = "";
			this.tipoMensaje = 0;
			
			// Enviamos mensaje a cola de transacciones
			log.info("Enviando Transaccion ... ");
			if (this.pojoGastoComprobar == null || this.pojoGastoComprobar.getId() == null || this.pojoGastoComprobar.getId() <= 0L) {
				log.info("NO se pudo enviar la Transaccion. No selecciono registro de Registro Gasto");
				return;
			}
			
			Respuesta respuesta = this.ifzGtosComp.enviarTransaccion(this.pojoGastoComprobar, 1L);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("No se pudo enviar mensaje a la cola de transacciones." + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError(), (Throwable) respuesta.getBody().getValor("exception"));
				this.mensaje = respuesta.getErrores().getDescError();
				this.tipoMensaje = -1;
				this.band = true;
				return;
			}
			log.info("Transaccion envianda");
		} catch (Exception e) {
			this.band = true;
			this.mensaje = "";
			this.tipoMensaje = 5;
			log.error("error en mensajeTransaccion", e);
		}
	}
	
	public void cargarFormasDePago() {
		try {
			if (this.listFormasPagoItems == null)
				this.listFormasPagoItems = new ArrayList<SelectItem>();
			this.listFormasPagoItems.clear();
			
			this.listFormasPago = this.ifzConValores.findAll(this.pojoGpoValFormasPago);
			if (this.listFormasPago != null && ! this.listFormasPago.isEmpty()) {
				for (ConValores fp : this.listFormasPago) {
					this.listFormasPagoItems.add(new SelectItem(fp.getDescripcion()));
				}
			}
		} catch(Exception e) {
			log.error("error en cargarFormasDePago", e);
		}
	}
	
	public String cancelarCheques(){
		try{
			Respuesta resp = new Respuesta();
			resp = ifzGtosComp.salvar(this.pojoGtosComp, false, null);
			
			if(! "BIEN".equals(resp.getRespuesta()) && resp.getResultado() == -1){
				band = true;// esta variable termine usandola como bandera gral
				this.mensaje = resp.getRespuesta();
				this.tipoMensaje=1; 
				//antes le mostraba el mensaje que me regresaba la funcion CANCELA_CHEQUES pero ahora solo le dire que se guardo		
				guardar();
			}
		} catch(Exception e) {
			log.error("error en cancelarCheques", e);
			return "ERROR";
		}
		
		return "OK";
	}
			
	public void evaluaOperacion(){
		try{
			this.pojoGtosComp.setFolioAutorizacion("");
			this.pojoGtosComp.setNoCheque(null);
			terceros="";
			this.esTransferencia = "transferencia".equals(this.operacion.trim().toLowerCase());
			this.esCheque="cheque".equals(this.operacion.trim().toLowerCase());
			this.esSpei = "spei".equals(this.operacion.trim().toLowerCase());
		} catch(Exception e) {			
			log.error("error en evaluaOperacion", e);
			//return "ERROR";
		}
		
		//return "OK";
	}
	
	private String operacionValidate(String value) {
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
	}
	
	public String cambioBeneficiario(){
		try{
			this.beneficiario = "";
		} catch(Exception e) {			
			log.error("error en cambioBeneficiario", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String cambioTerceros(){
		Pattern pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		Matcher match = null;
		this.band = true;
		this.tipoMensaje=1;
		this.mensaje = "";
		
		try{
			if("S".equals(this.operacion)){
				match = pat.matcher(this.beneficiario);
				if(match.find()){		
					this.pojoTerceros=  this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzBenefi.findById(Long.valueOf(match.group(1)));
					if(this.pojoTerceros != null){
						if(this.pojoTerceros.getBanco() != null)
							if(this.pojoTerceros.getClabeInterbancaria() != null && !"".equals(this.pojoTerceros.getClabeInterbancaria()))
								//if(this.pojoTerceros.getCuentaBancaria() != null && !"".equals(this.pojoTerceros.getCuentaBancaria()))
									//if("F".equals(this.pojoTerceros.getPersonalidad()))
											if(this.pojoTerceros.getNombre() != null && !"".equals(this.pojoTerceros.getNombre()) && this.pojoTerceros.getPrimerApellido() != null && !"".equals(this.pojoTerceros.getPrimerApellido()))
												return "OK";
											else
												this.mensaje = "El nombre del Beneficiario no esta completo, verifique !!";
									/*else
										if(this.pojoTerceros.getNombre() != null && !"".equals(this.pojoTerceros.getNombre()))
											return "OK";
										else
											this.mensaje = "El nombre del Beneficiario no esta completo, verifique !!";*/
								//else
									//this.mensaje = "El Beneficiario no tiene capturada cuenta bancaria, verifique !!";
							else
								this.mensaje = "El Beneficiario no tiene capturada cuenta Clabe, verifique !!";
						else
							this.mensaje = "El Beneficiario no tiene capturado banco, verifique !!";
					}
				}
			}
		} catch(Exception e) {			
			log.error("error en cambioTerceros", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String evaluaSiCambioSucursal (){
		Pattern pat = null;
		Matcher match = null;
		
		try{
			muestraSeg = false;
			mensaje = "";
			pat = Pattern.compile("(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)");
			
			//validando sucursal
			match = pat.matcher(this.sucursal);
			if(match.find())
				if(this.pojoSucursal == null)
					this.pojoSucursal = this.ifzGtosComp.findSucursalById(Long.valueOf(match.group(1))); // this.ifzSucursal.findById(Long.valueOf(match.group(1)));
				else
					if(this.pojoSucursal.getId() != Long.valueOf(match.group(1)))
						this.pojoSucursal = this.ifzGtosComp.findSucursalById(Long.valueOf(match.group(1))); // this.ifzSucursal.findById(Long.valueOf(match.group(1)));
			/*if(pojoSucursal != null){
				this.listAreas = ifzAreas.findByProperty("regionId.clave", pojoSucursal.getId());
				if(listAreas != null && !listAreas.isEmpty())
					muestraSeg = true;
			}*/
		} catch(Exception e) {
			log.error("error en evaluaSiCambioSucursal :: [" + match.group(1) + "]", e);
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
	public List<Sucursal> autoacompletaSucursal(Object obj){
		try{
			this.listSucursales = this.ifzSucursal.findLikePropiedad("sucursal", obj.toString());
			//this.listSucursales=this.ifzSucursal.findLikeClaveSucursal(obj.toString(),20, this.sucursalesVisibles);
			return this.listSucursales;
		}
		catch(Exception e){
			log.error("error en autoacompletaSucursal", e);
			return new ArrayList<Sucursal>();
		}
	}

	public List<Area> autoacompletaSegmento(Object obj){
		try{
			this.listAreas = this.ifzAreas.findByProperty("descripcion", obj.toString()); // this.listAreas=this.ifzAreas.findLikeClaveSegmento(obj.toString(),20, this.sucursalesVisibles);
			return this.listAreas;
		}
		catch(Exception e){
			log.error("error en autoacompletaSucursal", e);
			return new ArrayList<Area>();
		}
	}
	
	public List<CtasBanco> autoacompletaCuenta(Object obj){
		try{
			this.listCuentas = this.ifzCtas.findLikeClaveNombreCuenta(obj.toString(), 20, this.sucursalesVisibles, 0);
			return this.listCuentas;
		}
		catch(Exception e){
			log.error("error en autoacompletaCuenta", e);
			return new ArrayList<CtasBanco>();
		}
	}
	
	public List<Persona> autoacompletaBeneficiario(Object obj){
		try{
			this.listBeneficiarios = this.ifzBenefi.findLikeClaveNombre("nombre", obj.toString(), this.pojoGpoValPersonas, this.tipoPersona, 20, false);	
			return this.listBeneficiarios;
		}
		catch(Exception e){
			log.error("error en autoacompletaBeneficiario", e);
			return new ArrayList<Persona>();
		}
	}
	
	public List<Persona> autoacompletaTerceros (Object obj){
		try{
			this.listTerceros=this.ifzBenefi.findLikePersonas(obj.toString(),20);
			return this.listTerceros;
		}
		catch(Exception e){
			log.error("error en autoacompletaTerceros", e);
			return new ArrayList<Persona>();
		}
	}
	
	// ----------------------------------------------------------
	// BUSQUEDA DE OBRAS
	// ----------------------------------------------------------
	
	public String buscarObras(){
		try{
			tipoMensaje=0;
			mensaje="";
			band = false;
			
			if("Id de Obra".equals(this.valTipoBusquedaObra))
				this.listObras = this.ifzObras.findLikeProperty("idObra", this.campoBusquedaObra);
			else
				this.listObras = this.ifzObras.findLikeProperty("nombre", this.campoBusquedaObra); 
			
			if(this.listObras.isEmpty()){
				tipoMensaje = 12;
			}
		} catch(Exception e) {
			tipoMensaje=9;
			mensaje="";
			band = true;
			log.error("error en buscar obras", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public void seleccionarObra() {
		try {
			//this.pojoObra.setTipoProyecto(this.tipoObra);
			this.listObras.clear();
			valTipoBusquedaObra = tipoBusquedaObra.get(0);
			campoBusquedaObra = "";
			tipoObra = "O";
		} catch(Exception e) {
			log.error("error en seleccionarObra", e);
		}
	}
		
	public String cambioObra(){
		try{
			this.tipoObra = "";
		}catch(Exception e){
			log.error("error en cambioObra", e);
			return "ERROR";
		}
		
		return "OK";
	}

	// ----------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------
	
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

	public Long getPagosGastosId() {
		return this.pojoGtosComp.getId() != null ? this.pojoGtosComp.getId() : 0;
	}

	public void setPagosGastosId(Long pagosGastosId) {
		this.pojoGtosComp.setId(pagosGastosId);
	}

	public Date getFecha() {
		return this.pojoGtosComp.getFecha() != null ? this.pojoGtosComp.getFecha() : Calendar.getInstance().getTime();
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

	public Integer getNoCheque() {
		return this.pojoGtosComp.getNoCheque() != null ? this.pojoGtosComp.getNoCheque() : 0;
	}

	public void setNoCheque(Integer noCheque) {
		this.pojoGtosComp.setNoCheque(noCheque);
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

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public CtasBancoExt getPojoCtas() {
		return pojoCtas;
	}

	public void setPojoCtas(CtasBancoExt pojoCtas) {
		this.pojoCtas = pojoCtas;
	}

	public SucursalExt getPojoSucursal() {
		return pojoSucursal;
	}

	public void setPojoSucursal(SucursalExt pojoSucursal) {
		this.pojoSucursal = pojoSucursal;
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

	public List<CtasBanco> getListCuentas() {
		return listCuentas;
	}

	public void setListCuentas(List<CtasBanco> listCuentas) {
		this.listCuentas = listCuentas;
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

	public String getFolioAutorizacion() {
		return pojoGtosComp.getFolioAutorizacion() != null ? pojoGtosComp.getFolioAutorizacion():"";
	}

	public void setFolioAutorizacion(String folioAutorizacion) {
		this.pojoGtosComp.setFolioAutorizacion(folioAutorizacion);
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public Long getSegNeg() {
		return segNeg;
	}

	public void setSegNeg(Long segNeg) {
		this.segNeg = segNeg;
	}

	public boolean isMuestraSeg() {
		return muestraSeg;
	}

	public void setMuestraSeg(boolean muestraSeg) {
		this.muestraSeg = muestraSeg;
	}

	public List<Area> getListAreas() {
		return listAreas;
	}

	public void setListAreas(List<Area> listAreas) {
		this.listAreas = listAreas;
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}

	public List<String> getTipoBusquedaObra() {
		return tipoBusquedaObra;
	}

	public void setTipoBusquedaObra(List<String> tipoBusquedaObra) {
		this.tipoBusquedaObra = tipoBusquedaObra;
	}

	public String getValTipoBusquedaObra() {
		return valTipoBusquedaObra;
	}

	public void setValTipoBusquedaObra(String valTipoBusquedaObra) {
		this.valTipoBusquedaObra = valTipoBusquedaObra;
	}

	public String getCampoBusquedaObra() {
		return campoBusquedaObra;
	}

	public void setCampoBusquedaObra(String campoBusquedaObra) {
		this.campoBusquedaObra = campoBusquedaObra;
	}

	public String getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(String tipoObra) {
		this.tipoObra = tipoObra;
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
}

// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
//  VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//    1.1   | 2016-09-30 | Javier Tirado      | Se añade la carga de tipos de pago
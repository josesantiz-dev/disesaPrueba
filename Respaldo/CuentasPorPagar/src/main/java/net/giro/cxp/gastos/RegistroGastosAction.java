package net.giro.cxp.gastos;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.*;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.Persona; 
import net.giro.clientes.logica.PersonaRem;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.NQueryRem;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConGrupoValores; // import cde.cont.admon.datos.ConGrupoValores;
import net.giro.plataforma.beans.ConValores; // import cde.cont.admon.datos.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem; // import cde.cont.admon.logica.ConValoresFacadeRemote;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.plataforma.Utilerias; // import cde.plataforma.Utilerias;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Area;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.FacturaImpuestos;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt; // import cde.tyg.admon.datos.Tygpersonas;
import net.giro.cxp.beans.ProgPagosDetalle;
import net.giro.cxp.beans.ProgPagosDetalleExt;
import net.giro.cxp.beans.SucursalExt;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.logica.CtasBancoRem;
import net.giro.cxp.logica.AreaRem;
import net.giro.cxp.logica.GastosImpuestoRem;
import net.giro.cxp.logica.PagosGastosDetImpuestosRem;
import net.giro.cxp.logica.PagosGastosDetRem;
import net.giro.cxp.logica.ProgPagosDetalleRem;
import net.giro.navegador.LoginManager;

public class RegistroGastosAction {
	private static Logger log = Logger.getLogger(RegistroGastosAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	
	private PagosGastosRem ifzGtosComp; // private MovimientosCuentasFacadeRemote ifzGtosComp;
	private ConGrupoValoresRem ifzGpoVal;	
	private CtasBancoRem ifzCtas; // private CtasBancoFacadeRemote 	ifzCtas;
	private GastosImpuestoRem ifzGastoImpuesto;
	private ConValoresRem ifzConValores;
	private PersonaRem ifzPersonas;
	private PagosGastosDetRem ifzComprobadoGto;
	private SucursalesRem ifzSucursal;
	private ProgPagosDetalleRem ifzProPagDet;
	private PagosGastosDetImpuestosRem ifzDesgloImpto;
	private NQueryRem ifzQuery;
	private AreaRem ifzAreas;
	@SuppressWarnings("unused")
	private ReportesRem ifzReporte;
	
	private List<Persona> listTerceros; // private List<PersonaExt> listTerceros;
	private List<Sucursal> listSucursales;	
	private List<SelectItem> listSucursalesItems;
	private List<PagosGastosDetImpuestosExt> lisImpuestosEliminados;
	private List<PagosGastosDetImpuestosExt> listDesgloseImpuestos;
	private List<PagosGastosDetImpuestosExt> listDesgloseRet;
	private List<GastosImpuestoExt> listImpuestosDelGasto;
	private List<GastosImpuestoExt> listRetDelGasto;
	private List<Persona> listBeneficiarios;
	private List<CtasBanco> listCuentas;
	private List<SelectItem> listCuentasItems;
	private List<Persona> listProveedores;
	private List<ConValores> listConceptoGasto;
	private List<PagosGastosExt> listRegistroGastos;
	private List<ConValores> listRetEncontradas;
	private List<ProgPagosDetalleExt> listProPagDet;
	private List<FacturaImpuestos> listFacturaConImpuestos; // private List<FacturaImpuestos> listFacturaConImpuestos;
	private List<Area> listAreas;
	//private List<Perfil> pojoPerfilesGastos;
	private PagosGastosExt pojoRegistroGasto;
	private PagosGastosDetExt pojoRegistroGastoComprobado;
	private PagosGastosDetImpuestosExt pojoDesgloImpto;
	private ConGrupoValores pojoGpoValPersonas;
	private ConGrupoValores pojoGpoValGasto;
	private ConGrupoValores pojoGpoValTerceros;
	private CtasBancoExt pojoCtas;
	private ConValores pojoConceptoGtos;
	private ConValores pojoNvaRet;
	private PersonaExt pojoBeneficiarios;
	private PersonaExt pojoProveedores;
	private PersonaExt pojoTerceros;
	private SucursalExt pojoSucursal;
	private FacturaImpuestos reg; 
	private Long segNeg;
	private Double totImpuestos;
	private Double totRetenciones;
	private Double totalMenosImpuestos;
	private Double totalFacturasReportadas;
	private Double totPago;
	private Double subtotalMasImpuestos;
	private Double impto;
	private Double imptoRet;
	private Double sum_impto;
	private Double sum_imptoRet;
	private String terceros;
	private String operacion;	
	private String sucursal;
	private String segmento;
	private String descripcionFactura;
	private String conceptoGasto;
	private String conceptoGasto2;
	private String tipoPersona;
	private String nombreProveedor;	
	private String nombreBeneficiario;
	private String beneficiario;		
	private String cuenta;	
	private String valTipoBusqueda;
	private String valBusqRet;
	private String campoBusqRet;
	private String campoBusqueda;
	private String perfilRequiereObra;
	private String resOperacion;
	private String sucursalesVisibles;
	private HashMap <Integer, String> mensajesInf;
	private String [] tipoBusqueda;
	private String [] listBusqRet;
	private byte[] fileSrc;
	private Long facturaId;
	private long usuarioId;
	@SuppressWarnings({ "rawtypes", "unused"}) 
	private HashMap listaParam;
	@SuppressWarnings("unused")
	private byte[] datosRep;
	private boolean encontroMismoGrupo;
	private boolean esTransferencia;
	private boolean existeRetencion;
	private boolean esCheque;
	private boolean esSpei;
	private boolean muestraSeg;
   	private int 	numPagina;
	private int 	tipoMensaje;
	private int 	tipoReporte;
	private boolean facturaConLlave;
	private ObraRem ifzObras;
	private Obra pojoObra;
	private List<Obra> listObras;
	private List<String> tipoBusquedaObra;
	private String valTipoBusquedaObra;
	private String campoBusquedaObra;
	private int tipoObra;
	private int paginacionObras;
	private String observaciones;
	private ConGrupoValores pojoGpoValFormasPago; 
	private List<ConValores> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	private boolean reqReferencia;
	private String referenciaOperacion;
	// ORDEN DE COMPRA
	private OrdenCompraRem ifzOrdenCompra;
	private List<OrdenCompraExt> listOrdenCompra;
	private OrdenCompraExt pojoOrdenCompra;
	private List<String> opcionesBusquedaOrdenCompra;
	private String campoBusquedaOrdenCompra;
	private String valorBusquedaOrdenCompra;
	private int paginacionOrdenCompra;
	// PERFILES
	private boolean perfilEgresos;
	private List<SelectItem> listObrasTiposItems;
	private int tipoObraBusquedaObra;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	
	public RegistroGastosAction() throws NamingException, Exception {
		PropertyResourceBundle plataformaProperties = null;
		PropertyResourceBundle msgProperties = null;
		ValueExpression valExp = null;
		FacesContext facesContext = null;
		Application app = null;
		String valPerfil = "";
		
		try {
			facesContext = FacesContext.getCurrentInstance();
	        app = facesContext.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(facesContext.getELContext());

			this.usuarioId = loginManager.getUsuarioResponsabilidad().getUsuario().getId(); // lm.getLoginBean().getUsuario().getUsuarioId();
			this.paramsRequest = new HashMap<String, String>();
			
			Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// EVALUACION DE PERFILES
			this.perfilRequiereObra = this.loginManager.getAutentificacion().getPerfil("REQUIERE_OBRA");
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
	   		this.ctx = new InitialContext();
	   		this.ifzPersonas 		= (PersonaRem) 					this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
	   		this.ifzGtosComp 		= (PagosGastosRem) 				this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
	   		this.ifzAreas 			= (AreaRem) 					this.ctx.lookup("ejb:/Logica_CuentasPorPagar//AreaFac!net.giro.cxp.logica.AreaRem");
	   		this.ifzGastoImpuesto 	= (GastosImpuestoRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
	   		this.ifzDesgloImpto 	= (PagosGastosDetImpuestosRem) 	this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
	   		
	   		this.ifzProPagDet 		= (ProgPagosDetalleRem) 		this.ctx.lookup("ejb:/Logica_CuentasPorPagar//ProgPagosDetalleFac!net.giro.cxp.logica.ProgPagosDetalleRem");
	   		this.ifzObras 			= (ObraRem) 					this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
	   		this.ifzGpoVal 			= (ConGrupoValoresRem) 			this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
	   		this.ifzConValores 		= (ConValoresRem) 				this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
	   		this.ifzSucursal 		= (SucursalesRem) 				this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
	   		this.ifzReporte 		= (ReportesRem) 				this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
	   		this.ifzQuery 			= (NQueryRem) 					this.ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
	   		this.ifzCtas 			= (CtasBancoRem) 				this.ctx.lookup("ejb:/Logica_TYG//CtasBancoFac!net.giro.tyg.logica.CtasBancoRem");
	   		
	   		this.ifzComprobadoGto = (PagosGastosDetRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzComprobadoGto.setInfoSecion(loginManager.getInfoSesion());

			this.ifzOrdenCompra = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOrdenCompra.setInfoSecion(loginManager.getInfoSesion());
			this.ifzOrdenCompra.showSystemOuts(false);
	   		
			this.segNeg = 0L;
			this.muestraSeg = false;
			this.mensajesInf = new HashMap<Integer, String>();
	   		
			this.pojoGpoValPersonas = new ConGrupoValores();
			this.pojoRegistroGasto = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoBeneficiarios= new PersonaExt();
			this.pojoConceptoGtos = new ConValores();
			this.pojoRegistroGastoComprobado = new PagosGastosDetExt();
			this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
			this.pojoConceptoGtos = new ConValores();
			this.pojoProveedores = new PersonaExt();
			this.pojoGpoValGasto = new ConGrupoValores();
			this.pojoSucursal = new SucursalExt();
			this.pojoTerceros = new PersonaExt();
			this.pojoGpoValTerceros = new ConGrupoValores();
			this.pojoNvaRet = new ConValores();	
			this.listRegistroGastos = new ArrayList<PagosGastosExt>();
			this.listTerceros = new ArrayList<Persona>();
			this.listSucursales = new ArrayList<Sucursal>();
			this.listCuentas = new ArrayList<CtasBanco>();
			this.listBeneficiarios = new ArrayList<Persona>();
			this.lisImpuestosEliminados = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>(); 
			this.listDesgloseRet= new ArrayList<PagosGastosDetImpuestosExt>();
			this.listRetEncontradas = new ArrayList<ConValores>();
			this.listImpuestosDelGasto = new ArrayList<GastosImpuestoExt>();
			this.listRetDelGasto = new ArrayList<GastosImpuestoExt>();		
			this.listProPagDet = new ArrayList<ProgPagosDetalleExt>();
			this.reg = new FacturaImpuestos();
			this.listAreas = new ArrayList<Area>();
			this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>(); 
			//this.pojoCatBan = new CatBancos();
			//this.pojoSpei = new SpeiOutgoing();	
			
			this.numPagina = 1;
			this.tipoMensaje = 0;
			this.cuenta = "";
			this.beneficiario = "";	
			this.tipoPersona = "P";
			this.operacion = "C";
			this.resOperacion = "";
			this.esTransferencia = false;
			this.esCheque = true;
			this.esSpei = false;
			this.encontroMismoGrupo = false;
			this.facturaConLlave = false;
			this.existeRetencion = false;
			this.totalFacturasReportadas = 0D;
			this.subtotalMasImpuestos=0D;
			this.totImpuestos=0D;
			this.totRetenciones=0D;
			this.totalMenosImpuestos=0D;
			this.totPago=0D;
			
			this.tipoBusqueda = new String[3];
			this.tipoBusqueda[0] = "Todos";
			this.tipoBusqueda[1] = "Persona"; // "Empleado";
			this.tipoBusqueda[2] = "Negocio"; // "Proveedor";
			this.valTipoBusqueda = this.tipoBusqueda[0];
			this.campoBusqueda = "";

			// Busqueda Obras
	   		this.tipoBusquedaObra = new ArrayList<String>();
	   		this.tipoBusquedaObra.add("Nombre");
	   		this.tipoBusquedaObra.add("Cliente");
	   		this.tipoBusquedaObra.add("Contrato");
	   		this.tipoBusquedaObra.add("Clave");
	   		this.campoBusquedaObra = this.tipoBusquedaObra.get(0);
	   		this.valTipoBusquedaObra = "";
	   		this.tipoObra = 0;
			this.paginacionObras = 1;
			
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			msgProperties = (PropertyResourceBundle) valExp.getValue(facesContext.getELContext());
						
			this.listObrasTiposItems = new ArrayList<SelectItem>();
			this.listObrasTiposItems.add(new SelectItem(1, msgProperties.getString("obrasTipo.obra")));
			this.listObrasTiposItems.add(new SelectItem(2, msgProperties.getString("obrasTipo.proyecto")));
			this.listObrasTiposItems.add(new SelectItem(3, msgProperties.getString("obrasTipo.ordenTrabajo")));
			if (this.perfilEgresos)
				this.listObrasTiposItems.add(new SelectItem(4, msgProperties.getString("obrasTipo.administrativa")));
			
			// Busqueda Orden de Compra
			this.opcionesBusquedaOrdenCompra = new ArrayList<String>();
			this.opcionesBusquedaOrdenCompra.add("Proveedor");
			this.opcionesBusquedaOrdenCompra.add("Id");
			this.campoBusquedaOrdenCompra = this.opcionesBusquedaOrdenCompra.get(0);
			this.valorBusquedaOrdenCompra = "";
			this.paginacionOrdenCompra = 1;
	   		
			this.listBusqRet = new String[3];
			this.listBusqRet[0] = "Clave";
			this.listBusqRet[1] = "Descripcion";
			this.listBusqRet[2] = "Cuenta Contable";
			this.tipoReporte = 1;
			this.valBusqRet = "";
			this.campoBusqRet = this.listBusqRet[0];
			
			// FORMAS DE PAGO
			this.pojoGpoValFormasPago = this.ifzGpoVal.findByName("SYS_FORMAS_PAGO");
			if (this.pojoGpoValFormasPago == null || this.pojoGpoValFormasPago.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_FORMAS_PAGO en con_grupo_valores");

			// MOVIMIENTOS GASTOS
			this.pojoGpoValGasto = this.ifzGpoVal.findByName("SYS_MOVGTOS");
			if (this.pojoGpoValGasto == null || this.pojoGpoValGasto.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");

			// RELACIONES PERSONAS
			this.pojoGpoValPersonas = this.ifzGpoVal.findById(4L); // valGpoPersonas
			//this.pojoGpoValPersonas = this.ifzGpoVal.findByName("SYS_RELPER");
			if (this.pojoGpoValPersonas == null || this.pojoGpoValPersonas.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");

			// CUENTAS TERCEROS
			/*this.pojoGpoValTerceros = this.ifzGpoVal.findByName("SYS_CUENTASTERCEROS");
			if (this.pojoGpoValTerceros == null || this.pojoGpoValTerceros.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_CUENTASTERCEROS en con_grupo_valores");*/
			
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			plataformaProperties = (PropertyResourceBundle) valExp.getValue(facesContext.getELContext());
			
			this.mensajesInf.put(-1, plataformaProperties.getString("mensaje.error.inesperado"));
			this.mensajesInf.put(1,  plataformaProperties.getString("mensaje.info.impuestoDuplicado"));
			this.mensajesInf.put(2,  plataformaProperties.getString("mensaje.info.faltanFacturas"));
			this.mensajesInf.put(3,  plataformaProperties.getString("mensaje.info.noEliminar"));
			this.mensajesInf.put(4,  plataformaProperties.getString("mensaje.info.noEditar"));
			this.mensajesInf.put(5,  plataformaProperties.getString("mensaje.info.imptosCero"));
			this.mensajesInf.put(6,  plataformaProperties.getString("mensaje.info.montoFacturaCero"));
			this.mensajesInf.put(7,  plataformaProperties.getString("mensaje.info.montoFacturaMenorImptos"));
			this.mensajesInf.put(10, plataformaProperties.getString("mensaje.info.noAgregar"));
			this.mensajesInf.put(11, plataformaProperties.getString("mensaje.info.noEditarGto"));
			this.mensajesInf.put(12, plataformaProperties.getString("navegacion.label.montoInvalido"));
			this.mensajesInf.put(13, plataformaProperties.getString("mensaje.info.busquedaVacia"));
			this.mensajesInf.put(14, plataformaProperties.getString("mensaje.info.noCancelar"));
			this.mensajesInf.put(15, plataformaProperties.getString("navegacion.label.saldoInsuficiente"));
			this.mensajesInf.put(16, plataformaProperties.getString("navegacion.label.retencionesPend"));
			this.mensajesInf.put(17, plataformaProperties.getString("navegacion.label.retDuplicada"));
			this.mensajesInf.put(18, plataformaProperties.getString("navegacion.label.mtoRet"));
			this.mensajesInf.put(19, plataformaProperties.getString("navegacion.label.gtoNoPro"));
			this.mensajesInf.put(20, plataformaProperties.getString("navegacion.label.gtoDupli"));
			this.mensajesInf.put(21, plataformaProperties.getString("navegacion.label.gtoNoRev"));
			this.mensajesInf.put(22, plataformaProperties.getString("navegacion.label.gtoMtoInva"));
			
			this.reqReferencia = true;
			//this.operacion = operacionValidate("");
			cargarFormasDePago();
			cargarSucursales();
			cargarCuentas();
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.RegistroGastosAction", e);
			this.ctx = null;
		}
  	}
   	

   	public String buscar() throws NamingException, SQLException, ParseException{
   		String tmp = null;
   		
   		try {
			if(this.valTipoBusqueda.equals(this.tipoBusqueda[0]))
				tmp = null;
			else if(this.valTipoBusqueda.equals(this.tipoBusqueda[1]))
				tmp = "P"; //"DE";
			else if(this.valTipoBusqueda.equals(this.tipoBusqueda[2]))
				tmp = "N"; // "EMP";
			else 
				tmp = null; // "PROV";
			
			this.listRegistroGastos = this.ifzGtosComp.findLikeBenefTipoPersonaExt(this.campoBusqueda, tmp, "P", "pago", 120, this.sucursalesVisibles);
			
			if(this.listRegistroGastos.isEmpty())
				this.resOperacion = mensajesInf.get(13);
			else
				this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			this.listRegistroGastos = new ArrayList<PagosGastosExt>();
			log.error("Error en el metodo buscar", e);
		}
   		
		return "OK";
   	}
   	
   	public String nuevo() {
		try{	
			this.pojoRegistroGasto = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoBeneficiarios = new PersonaExt();
			this.pojoSucursal = new SucursalExt();
			this.pojoTerceros = new PersonaExt();
			//this.pojoSpei = new SpeiOutgoing();
			
			this.listFacturaConImpuestos.clear();			
			this.lisImpuestosEliminados.clear();
			this.listDesgloseImpuestos.clear(); 
			this.listImpuestosDelGasto.clear();
			
			this.listRetEncontradas.clear();
			this.listDesgloseRet.clear();
			this.totRetenciones=0D;
			this.totPago= 0D;
			
			this.sucursal="";
			this.cuenta="";
			this.beneficiario="";			
			this.tipoPersona="P";
			this.operacion = ""; //this.listFormasPagoItems.get(0).getValue().toString();//this.operacionValidate("");
			this.esTransferencia=false;
			this.esSpei=false;
			this.esCheque=true;
			this.existeRetencion = false;
			this.totalFacturasReportadas =0D;
			this.segmento="";
			this.muestraSeg = false;
			this.observaciones = "";
			this.referenciaOperacion = "";
			this.pojoObra = null;
			this.pojoOrdenCompra = null;
			
			obrasBusqueda();
			ordenCompraBusqueda();
			agregar();
		}catch(Exception e){
			log.error("Error en el metodo nuevo.", e);
			return "ERROR";
		}
		
		return "OK";
	}

   	public void editar() {
   		try {
   			control();
   			log.info("RegistroGasto ... Preparando para editar");
   			if (this.pojoRegistroGasto == null) {
   	   			log.info("RegistroGasto ... No selecciono ningun registro");
   	   			control("No selecciono ningun registro");
   				return;
   			}
   			
   			this.operacion = this.pojoRegistroGasto.getOperacion();
   			if ("C".equals(this.operacion)) {
   				this.referenciaOperacion = this.pojoRegistroGasto.getNoCheque().toString();
			} else if ("T".equals(this.operacion)) {
				this.referenciaOperacion = this.pojoRegistroGasto.getFolioAutorizacion();
			} else {
				this.referenciaOperacion = "";
			}
   			
   			this.sucursal = this.pojoRegistroGasto.getIdSucursal().getId() + " - " + this.pojoRegistroGasto.getIdSucursal().getSucursal();
   			this.cuenta = this.pojoRegistroGasto.getIdCuentaOrigen().getId() + " - " + this.pojoRegistroGasto.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto() + " - " + this.pojoRegistroGasto.getIdCuentaOrigen().getNumeroDeCuenta();
   			this.beneficiario = this.pojoRegistroGasto.getIdBeneficiario().getId() + " - " + this.pojoRegistroGasto.getIdBeneficiario().getNombre();
   			this.tipoPersona = this.pojoRegistroGasto.getTipoBeneficiario();
			this.segmento = "";
   			
   			if (this.listFacturaConImpuestos == null)
   				this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>();
   			this.listFacturaConImpuestos.clear();
   			
   			// recuperamos detalle de Registro de Gastos
			log.info("RegistroGasto ... Recuperando detalles");
   			List<PagosGastosDetExt> listDetalles = this.ifzComprobadoGto.findByPagosGastosExt(this.pojoRegistroGasto, 0);
   			if (listDetalles != null && ! listDetalles.isEmpty()) {
   	   			for (PagosGastosDetExt var : listDetalles) {
   	   				this.reg = new FacturaImpuestos();
   	   				this.reg.setPojoFactura(var);
   	   	   			this.listFacturaConImpuestos.add(this.reg);
   	   			}
   			}

			log.info("RegistroGasto ... " + this.listFacturaConImpuestos.size() + " detalles recuperados");
			this.reg = new FacturaImpuestos();
			//desglosaImpuestos();
			actualizaTotalFaturas();
			log.info("RegistroGasto... Lista para editar");
   		} catch (Exception e) {
			//this.operacion = this.operacionValidate(this.operacion);
			log.error("Error en CuentasPorPagar.RegistroGastosAction.editar", e);
			control(true);
   		}
   	}
   	
	@SuppressWarnings("unchecked")
	public String guardar() throws NamingException, Exception{
		Pattern pat = null;
		Matcher match = null;
		boolean  registroNuevo = false;
		Respuesta resp = new Respuesta();
		
		try {
			control();
			this.segNeg = 0L;
			if(this.pojoRegistroGasto.getId() == null)
				registroNuevo = true;
			
			this.pojoRegistroGasto.setIdObra(new Obra());
			this.pojoRegistroGasto.setIdOrdenCompra(0L);
			this.pojoRegistroGasto.setModificadoPor(this.usuarioId);
			this.pojoRegistroGasto.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoRegistroGasto.setTipoBeneficiario(this.tipoPersona);
			
			if (this.pojoObra != null) {
				if (this.pojoRegistroGasto.getIdObra() == null) {
					this.pojoRegistroGasto.setIdObra(this.pojoObra);
				} else {
					if (this.pojoRegistroGasto.getIdObra().getId() != null && this.pojoRegistroGasto.getIdObra().getId() != this.pojoObra.getId())
						this.pojoRegistroGasto.setIdObra(this.pojoObra);
				}
			}
			
			if (this.totalFacturasReportadas == null || this.totalFacturasReportadas == 0) {
				//this.operacion = this.operacionValidate(this.operacion);
				log.info("ERROR 2 - Debe indicar al menos una factura");
				control(true, 2, this.mensajesInf.get(2));
				return "OK";
			}
			
			//validando Cuenta
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.cuenta);
			if (match.find()) {
				if (registroNuevo) {
					this.pojoCtas = this.ifzGtosComp.findCuentaBancariaById(Long.valueOf(match.group(1))); 
				} else {
					if (this.pojoCtas.getId() != Long.valueOf(match.group(1)))
						this.pojoCtas= this.ifzGtosComp.findCuentaBancariaById(Long.valueOf(match.group(1))); 
				}
			}

			// Asigno cuenta
			this.pojoRegistroGasto.setIdCuentaOrigen(this.pojoCtas);

			if (! "OK".equals(validaSaldoCuentaPropios())) {
				//this.operacion = this.operacionValidate(this.operacion);
				return "ERROR";
			}

			//validando sucursal
			pat = Pattern.compile("(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.sucursal);
			if (match.find()) {
				if(registroNuevo) {
					this.pojoSucursal = this.ifzGtosComp.findSucursalById(Long.valueOf(match.group(1))); 
				} else {
					if(this.pojoSucursal.getId() != Long.valueOf(match.group(1)))
						this.pojoSucursal = this.ifzGtosComp.findSucursalById(Long.valueOf(match.group(1))); 
				}
			}
			
			// Asigno sucursal
			this.pojoRegistroGasto.setIdSucursal(this.pojoSucursal);
			
			//validando segmento
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.segmento);
			if(match.find())
				this.segNeg = Long.valueOf(match.group(1));
			this.pojoRegistroGasto.setSegmento(segNeg);
			
			//validando operacion para mostrar catalodo de Terceros
			if("T".equals(this.operacion) || "S".equals(this.operacion)){
				match = pat.matcher(this.beneficiario);			
				if(match.find())
					if(registroNuevo)				
						this.pojoTerceros = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
					else
						if (this.pojoTerceros.getId() != Long.valueOf(match.group(1)))
							this.pojoTerceros = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				this.pojoRegistroGasto.setIdCuentaDestinoTerceros(this.pojoTerceros);	
				this.pojoRegistroGasto.setBeneficiario(this.pojoTerceros.getNombre()+" "+(this.pojoTerceros.getPrimerApellido() != null ? this.pojoTerceros.getPrimerApellido():"")+" "+(this.pojoTerceros.getSegundoApellido() != null ? this.pojoTerceros.getSegundoApellido():""));
			}

			//validando beneficiario
			match = pat.matcher(this.beneficiario);
			if (match.find()) {
				if(registroNuevo) {
					this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				} else {
					if(this.pojoBeneficiarios.getId() != Long.valueOf(match.group(1)))
						this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				}
			}
			
			// Asigno beneficiario
			this.pojoRegistroGasto.setIdBeneficiario(this.pojoBeneficiarios);
			if ("N".equals(this.tipoPersona)) {
				this.nombreBeneficiario = this.pojoBeneficiarios.getNombre();
			} else {
				this.nombreBeneficiario = this.pojoBeneficiarios.getPrimerNombre() + " " 
						+ ((this.pojoBeneficiarios.getSegundoNombre()   != null && ! "".equals(this.pojoBeneficiarios.getSegundoNombre().trim()))   ? this.pojoBeneficiarios.getSegundoNombre().trim()   : "") + " " 
						+ ((this.pojoBeneficiarios.getPrimerApellido()  != null && ! "".equals(this.pojoBeneficiarios.getPrimerApellido().trim()))  ? this.pojoBeneficiarios.getPrimerApellido().trim()  : "") + " " 
						+ ((this.pojoBeneficiarios.getSegundoApellido() != null && ! "".equals(this.pojoBeneficiarios.getSegundoApellido().trim())) ? this.pojoBeneficiarios.getSegundoApellido().trim() : "");
			}
			
			this.pojoRegistroGasto.setBeneficiario(this.nombreBeneficiario);
			this.pojoRegistroGasto.setOperacion(this.operacion);
			this.pojoRegistroGasto.setMonto(this.totalFacturasReportadas);
			
			// Asignacion de operacion
			if ("C".equals(this.operacion)) {
				this.pojoRegistroGasto.setNoCheque(Integer.parseInt(this.referenciaOperacion));
			} else if ("T".equals(this.operacion)) {
				this.pojoRegistroGasto.setFolioAutorizacion(this.referenciaOperacion);
			}
				
			if (registroNuevo) {
				if ("S".equals(this.operacion)) {
					String sql = "select spei_autorizacion(3, current_date, "+usuarioId+", 6, "+this.pojoSucursal.getId()+", null, "+this.totalFacturasReportadas+")";
					List<Object> listObj = this.ifzQuery.findNativeQuery(sql);
					String Id = String.valueOf(listObj.get(0).toString());
					if (Id.equals("")) {
						this.resOperacion = "ERROR DESCONOCIDO";
						//this.operacion = this.operacionValidate(this.operacion);
						return "OK";
					} else {
						/*if (Id.substring(0, 1).equals("0") || Id.substring(0, 1).equals("1")){
							CtasBanco pojoCtaSpei = new CtasBanco();
							pojoCtaSpei = this.ifzCtas.findAllById(Short.valueOf("659"));
							
							sql = "select spei_referencia()";
							listObj = this.ifzQuery.findNativeQuery(sql);
							String ref = String.valueOf(listObj.get(0).toString());
							if (ref.equals("")){
								this.resOperacion = "ERROR DESCONOCIDO";
								return "OK";
							}else{
								this.pojoSpei.setNombreOrdenante(pojoCtaSpei.getCatBancoId().getNombreLargo().substring(0, pojoCtaSpei.getCatBancoId().getNombreLargo().length()<40?pojoCtaSpei.getCatBancoId().getNombreLargo().length():40)); 
								this.pojoSpei.setIdTipoCuentaOrdenante("40");
								this.pojoSpei.setCuentaOrdenante(pojoCtaSpei.getClabe());
								this.pojoSpei.setRfcOrdenante(pojoCtaSpei.getEmpresa().getRfcEmp());
								this.pojoSpei.setNombreBeneficiario(this.nombreBeneficiario.substring(0, this.nombreBeneficiario.length()<40?this.nombreBeneficiario.length():40));
								this.pojoSpei.setIdTipoCuentaBeneficiario("40");
								this.pojoSpei.setCuentaBeneficiario(this.pojoTerceros.getClabe());
								this.pojoSpei.setRfcBeneficiario(this.pojoTerceros.getRfc());
								//this.pojoSpei.setIdInstitucionBen(this.pojoTerceros.getCatBancoId().getSpeiInstitucionesId().getIdInstitucion());
								this.pojoSpei.setMonto(this.totalFacturasReportadas);
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
								this.pojoSpei.setEnvioAutomatico(Id.substring(0, 1));
							}
						}else{
							this.resOperacion = "ERROR: " + Id;
							return "OK";
						}*/
						this.resOperacion = "NOT_IMPLEMENTED";
						//this.operacion = this.operacionValidate(this.operacion);
						return "OK";
					}
				}
				
				this.pojoRegistroGasto.setCreadoPor(this.usuarioId);
				this.pojoRegistroGasto.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoRegistroGasto.setTipo("P");//gasto "Pago"
				this.pojoRegistroGasto.setEstatus("C");//comprobado

				resp = ifzGtosComp.salvar(this.pojoRegistroGasto, true,null);
				
				//validando folio cheque					
				if(resp.getResultado() != -1){
					this.pojoRegistroGasto.setId(Long.valueOf(resp.getReferencia()));	
					
					/*if("S".equals(this.operacion)){
						//this.pojoSpei.setIdOperacion(this.pojoGtosComp.getId().intValue());
						//this.pojoSpei.setIdSpeiOutgoing(ifzSpei.save(this.pojoSpei));
						//this.pojoGtosComp.setIdSpeiOutgoing(this.pojoSpei);
						//this.ifzGtosComp.update(pojoGtosComp);
					}
					
					resOperacion="C";
					if("T".equals(this.operacion) || "S".equals(this.operacion))
					{							
						// Es un SPEI, genera el recibo.
						if ("S".equals(this.operacion)) {
							this.resOperacion = "S";
						}else
							this.resOperacion = "T"; //limpio el mensaje para que no salga el reporte de cheque por ser transferencia/spei
					}
					
					/ *if(perfil == null && !"S".equals(perfil)){
						List<ProgPagosDetalle> lista = null;
						if(! listFacturaConImpuestos.isEmpty()){
							for(FacturaImpuestos var: listFacturaConImpuestos){
								Double montoValue = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
								Long conceptoValue = var.getPojoFactura().getConceptoId().getId();
								lista = ifzProPagDet.findByMontoConceptoEstatus(montoValue, conceptoValue, "G");
								if(lista != null){
									lista.get(0).setEstatus("E");
									lista.get(0).setModificadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
									lista.get(0).setFechaModificacion(Calendar.getInstance().getTime());
									ifzProPagDet.update(lista.get(0));
								}
							}
						}
					}* /
					
					if(resOperacion.equals("C")){/ *
						//impresion del cheque
						listaParam = new HashMap<String, String>();
						listaParam.put("folio", this.pojoGtosComp.getNoCheque());						
						listaParam.put("banco_id", this.pojoGtosComp.getIdCuentaOrigen().getId());
						//datosRep = ifzReporte.generaReporte("cheque.jasper", listaParam);
						httpSession.setAttribute("reporte", datosRep);
						httpSession.setAttribute("tipoReporte", "pdf");* /
					}*/
				} else {
					if(! "BIEN".equals(resp.getRespuesta())){
						encontroMismoGrupo = true;// esta variable termine usandola como bandera gral
						this.resOperacion = resp.getRespuesta();
						
						if((resp.getRespuesta().indexOf("SE CANCELA EL FOLIO ") > -1) || (resp.getRespuesta().indexOf("SE CANCELAN LOS FOLIOS ") > -1))
							this.tipoMensaje = 9;
						else
							this.tipoMensaje = 8;
						
						//this.operacion = this.operacionValidate(this.operacion);
						return "ERROR";   							
					}
				}
				
				this.listRegistroGastos.add(this.pojoRegistroGasto);
			} else {
				// Actualiza Registro de Gastos
				this.ifzGtosComp.update(this.pojoRegistroGasto);

				// Actualizo en la LISTA
				for (PagosGastosExt var : this.listRegistroGastos) {
					if (var.getId().equals(this.pojoRegistroGasto.getId())) {
						var = this.pojoRegistroGasto;
						break;
					}
				}
			}
				
			//grabando las facturas en caso de que hayan agregado algunas
			if (! this.listFacturaConImpuestos.isEmpty()) {
				long idPagosGastosDet = 0L;
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					if (var.getPojoFactura().getId() == null) {
						var.getPojoFactura().setIdPagosGastos(this.pojoRegistroGasto);
						idPagosGastosDet = this.ifzComprobadoGto.save(var.getPojoFactura());
						var.getPojoFactura().setId(idPagosGastosDet);
						
						if (! var.getListImpuestos().isEmpty()) {
							for (PagosGastosDetImpuestosExt var2 : var.getListImpuestos()) {
								var2.setIdPagosGastosDet(var.getPojoFactura());

								if (var2.getId() == 0L)
									var2.setId(this.ifzDesgloImpto.save(var2));
								else
									this.ifzDesgloImpto.update(var2);
							}
						}
						
						if (! var.getListRetenciones().isEmpty()) {
							for (PagosGastosDetImpuestosExt var2 : var.getListRetenciones()) {
								var2.setIdPagosGastosDet(var.getPojoFactura());

								if (var2.getId() == 0L)
									var2.setId(this.ifzDesgloImpto.save(var2));
								else
									this.ifzDesgloImpto.update(var2);
							}
						}
					} else {
						var.getPojoFactura().setIdPagosGastos(this.pojoRegistroGasto);
						this.ifzComprobadoGto.update(var.getPojoFactura());
						
						if (! var.getListImpuestos().isEmpty()) {
							for (PagosGastosDetImpuestosExt var2 : var.getListImpuestos()) {
								var2.setIdPagosGastosDet(var.getPojoFactura());

								if (var2.getId() == 0L)
									var2.setId(this.ifzDesgloImpto.save(var2));
								else
									this.ifzDesgloImpto.update(var2);
							}
						}
						
						if (! var.getListRetenciones().isEmpty()) {
							for (PagosGastosDetImpuestosExt var2 : var.getListRetenciones()) {
								var2.setIdPagosGastosDet(var.getPojoFactura());

								if (var2.getId() == 0L)
									var2.setId(this.ifzDesgloImpto.save(var2));
								else
									this.ifzDesgloImpto.update(var2);
							}
						}
					}
				}

				//ifzGtosComp.mensajeSaldoCtas(pojoGtosComp, this.pojoCtas.getEmpresa().getId());
			}

			
			if( registroNuevo ) {			
				if("S".equals(this.operacion)) {
					/*listaParam = new HashMap<String, String>();
					listaParam.put("id", this.pojoSpei.getIdSpeiOutgoing().longValue());						
					datosRep = ifzReporte.generaReporte("recibo_spei.jasper", listaParam);
					httpSession.setAttribute("reporte", datosRep);
					httpSession.setAttribute("tipoReporte", "pdf");
					this.resOperacion = "S";
					
					String sClave;
					sClave="";									
					this.tipoMensaje=1;
										
					for (int i= 0;  i <= 15; i++ ) {
						Thread.sleep(1000);
						String sql = "Select * From spei_obten_respuesta_gastos("+this.pojoSpei.getIdSpeiOutgoing().toString()+");";
						List<Object> listObj = this.ifzQuery.findNativeQuery(sql);
						sClave = String.valueOf(listObj.get(0).toString());
						
						if (!sClave.equals("OK")) {
							this.resOperacion = "TRANSFERENCIA SPEI AUTORIZADA: " + sClave ;
							return "OK";							
						}
					}*/	
					this.resOperacion = "TRANSFERENCIA SPEI EN ESPERA DE AUTORIZACION";						
					return "OK";
				}
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			mensajeTransaccion();
		} catch(Exception e) {
			//this.operacion = this.operacionValidate(this.operacion);
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en CuentasPorPagar.RegistroGastosAction.guardar", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public String eliminar(){
		try {					
			this.listFacturaConImpuestos.remove(reg);				
			actualizaTotalFaturas();					
		} catch (Exception e) {			
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en el metodo eliminar.", e);
			return "ERROR";
		}

		return "OK";
	}

	public String agregar(){
		try {
			control();
			if (! this.isDebug && this.pojoRegistroGasto.getId() != null) { // por si el gasto ya tiene llave no sigan agregando comprobante hasta que hagan clic en nuevo
				log.info("ERROR 10 - " + this.mensajesInf.get(10));				  						
				this.facturaConLlave = true;
				control(true, 10, this.mensajesInf.get(10));
				return "ERROR";
			}
				
			this.conceptoGasto = "";
			this.conceptoGasto2 = "";
			this.nombreProveedor = "";
			this.descripcionFactura = "";
			this.observaciones = "";
			this.listDesgloseImpuestos.clear();
			this.listDesgloseRet.clear();
			this.listRetEncontradas.clear();
			this.listRetDelGasto.clear();
			this.totImpuestos = 0D;
			this.totRetenciones = 0D;
			this.totPago = 0D;
			this.totalMenosImpuestos = 0D;
			this.subtotalMasImpuestos = 0D;
			
			PersonaExt proveedor = new PersonaExt();
			Pattern pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			Matcher match = pat.matcher(this.beneficiario);
			if (match.find())
				proveedor = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona);

			this.pojoRegistroGastoComprobado = new PagosGastosDetExt();
			this.pojoRegistroGastoComprobado.setIdProveedor(proveedor);
			
			this.reg = new FacturaImpuestos();
			//this.reg.setPojoFactura(this.pojoComprobadoGto);
			this.pojoObra = null;
			this.obrasBusqueda();

			this.campoBusquedaOrdenCompra = this.opcionesBusquedaOrdenCompra.get(0);
			this.valorBusquedaOrdenCompra = "";
			this.paginacionOrdenCompra = 1;
			
			if (this.listOrdenCompra == null)
				this.listOrdenCompra = new ArrayList<OrdenCompraExt>();
			this.listOrdenCompra.clear();
			this.pojoOrdenCompra = new OrdenCompraExt();
		} catch (Exception e) {
			log.error("Error en agregar", e);
			control(true);
			return "ERROR";
		}
		
		return "OK";
	}

	public String reporte(){
   		try {
			/*listaParam = new HashMap<String, String>();
			if(this.tipoReporte == 1){
				listaParam.put("folio", this.pojoGtosComp.getNoCheque());
				listaParam.put("banco_id", this.pojoGtosComp.getIdCuentaOrigen().getId());
				datosRep = ifzReporte.ejecutaReporte(listaParam, "cheque.jasper");
			}else{
				listaParam.put("gasto_id", this.pojoGtosComp.getPagosGastosId().intValue());
				datosRep = ifzReporte.generaReporte("registro_pago.jasper", listaParam);
			}
			
			httpSession.setAttribute("reporte", datosRep);
			httpSession.setAttribute("tipoReporte", "pdf");
			this.resOperacion = "";	*/		
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en el metodo reporte.", e);
			return "ERROR";
		}
		return "OK";
	}

   	public String checaCancelado() throws NamingException, SQLException, ParseException{
   		if(this.pojoRegistroGasto.getEstatus().equals("X"))
			this.resOperacion = mensajesInf.get(14);
   		else
   			this.resOperacion = "";
   		return "OK";
   	}
   	
   	public void cargarSucursales() {
   		try {
			if (this.listSucursales == null)
				this.listSucursales = new ArrayList<Sucursal>();
			this.listSucursales.clear();
			
			if (this.listSucursalesItems == null)
				this.listSucursalesItems = new ArrayList<SelectItem>();
			this.listSucursalesItems.clear();
			
			this.listSucursales = this.ifzSucursal.buscarSucursales("sucursal", "");
			if (this.listSucursales != null && ! this.listSucursales.isEmpty()) {
				for (Sucursal var : this.listSucursales)
					this.listSucursalesItems.add(new SelectItem(var.getId() + " - " + var.getSucursal(), var.getSucursal()));
			}
		} catch (Exception e) {
			log.error("error en cargarSucursales", e);
		}
   	}
   	
   	public void cargarFormasDePago() {
		try {
			if (this.listFormasPago == null)
				this.listFormasPago = new ArrayList<ConValores>();
			this.listFormasPago.clear();
			
			if (this.listFormasPagoItems == null)
				this.listFormasPagoItems = new ArrayList<SelectItem>();
			this.listFormasPagoItems.clear();
			
			if (this.pojoGpoValFormasPago == null || this.pojoGpoValFormasPago.getId() <= 0L)
				return;
			
			this.listFormasPago = this.ifzConValores.findAll(this.pojoGpoValFormasPago);
			if (this.listFormasPago != null && ! this.listFormasPago.isEmpty()) {
				for (ConValores fp : this.listFormasPago)
					this.listFormasPagoItems.add(new SelectItem(fp.getValor(), fp.getDescripcion()));
			}
		} catch(Exception e) {
			log.error("error en cargarFormasDePago", e);
		}
	}

   	public void cargarCuentas() {
   		try {
			if (this.listCuentas == null)
				this.listCuentas = new ArrayList<CtasBanco>();
			this.listCuentas.clear();
			
			if (this.listCuentasItems == null)
				this.listCuentasItems = new ArrayList<SelectItem>();
			this.listCuentasItems.clear();
			
			this.listCuentas = this.ifzCtas.findLikeClaveNombreCuenta("", 0, this.sucursalesVisibles, 0);
			if (this.listCuentas != null && ! this.listCuentas.isEmpty()) {
				for (CtasBanco var : this.listCuentas)
					this.listCuentasItems.add(new SelectItem(var.getId() + " - " + var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta(), var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta()));
			}
		} catch (Exception e) {
			log.error("error en cargarCuentas", e);
		}
   	}
   	
	public String evaluaOperacion(){
		try{
			this.reqReferencia = true;
			/*
			 * Eliminamos el siguiente codigo, siempre debe pedir referencia
			 *
			this.reqReferencia = "transferencia".equals(this.operacion.trim().toLowerCase()) ? true : false; //"T".equals(this.operacion);
			this.reqReferencia = "cheque".equals(this.operacion.trim().toLowerCase()) ? true : false; //"C".equals(this.operacion);
			this.reqReferencia = "spei".equals(this.operacion.trim().toLowerCase()) ? true : false; //"S".equals(this.operacion);
			//this.esTransferencia = "transferencia".equals(this.operacion.trim().toLowerCase()) ? true : false; //"T".equals(this.operacion);
			//this.esCheque = "cheque".equals(this.operacion.trim().toLowerCase()) ? true : false; //"C".equals(this.operacion);
			//this.esSpei = "spei".equals(this.operacion.trim().toLowerCase()) ? true : false; //"S".equals(this.operacion);
			*/
			this.resOperacion = "";
			this.terceros = "";
			this.pojoRegistroGasto.setNoCheque(0);
			this.pojoRegistroGasto.setFolioAutorizacion("");
			this.beneficiario = "";
		} catch(Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en evaluaOperacion", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	private List<PagosGastosDetImpuestosExt> copiaListas(List<PagosGastosDetImpuestosExt> fuente){
   		List<PagosGastosDetImpuestosExt> resultado = new ArrayList<PagosGastosDetImpuestosExt>();
   		for(PagosGastosDetImpuestosExt pgi : fuente){
   			resultado.add( ((PagosGastosDetImpuestosExt) pgi.clone()));
   		}
   		return resultado;
   	}
   	
   	public String buscarRet(){
   		try{
   			resOperacion= "";
   			this.listRetEncontradas = ifzConValores.findLikeByProperty(this.campoBusqRet,this.valBusqRet, 5);
   			if(listRetEncontradas.isEmpty() || listRetEncontradas==null)
   				this.resOperacion = this.mensajesInf.get(13);
   		}catch(Exception e){
   			log.error("Error en metodo buscarRet", e);
   			this.resOperacion = mensajesInf.get(-1);
   		}
   		return "OK";
   	}
   	
	public void mensajeTransaccion() {
		try {
			// Enviamos mensaje a cola de transacciones
			log.info("Enviando Transaccion ... ");
			if (this.pojoRegistroGasto == null || this.pojoRegistroGasto.getId() == null || this.pojoRegistroGasto.getId() <= 0L) {
				log.info("NO se pudo enviar la Transaccion. No selecciono registro de Registro Gasto");
				return;
			}
			
			Respuesta respuesta = this.ifzGtosComp.enviarTransaccion(this.pojoRegistroGasto, 1L);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("No se pudo enviar mensaje a la cola de transacciones." + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError(), (Throwable) respuesta.getBody().getValor("exception"));
				control(respuesta.getErrores().getDescError());
				return;
			}
			log.info("Transaccion enviada");
		} catch (Exception e) {
			log.error("error en mensajeTransaccion", e);
			control(true);
		}
	}
	
   	public String cambioBeneficiario(){
		try{
			this.beneficiario = "";
		}catch(Exception e){			
			log.error("error en cambioBeneficiario", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String validaSaldoCuentaPropios() {
		try {
			control();
			if (totalFacturasReportadas == 0) {
				control(true, 12, this.mensajesInf.get(12));
				return "ERROR";
			}
			
			/* 
			 * VALIDACION TEMPORALMENTE DESACTIVADA: ASI LO PIDIO EL JEFE
			if (this.pojoCtas.getInstitucionBancaria().getId() != 0L) {
				if (! ifzCtas.haySaldoSuficiente(totalFacturasReportadas, this.pojoCtas.getId())) {
					control(true, 15, this.mensajesInf.get(15));
					return "ERROR";
				}
			}*/
		} catch (Exception e) {
			log.error("Error en el metodo validaSaldoCuentaPropios.", e);
			control(true);
			return "ERROR";
		}
		
		return "OK";
	}
   	
	public String guardarFactura(){
		boolean encontro = false;			
		boolean  registroNuevo = false;  
		Pattern pat = null;
		Matcher match = null;
		int ban = 0;
		
		try{
			log.info("Guardando Factura");
			control();
			
			if (! "OK".equals(validaMontoFactura()))
				return "ERROR";
			
			if (! "OK".equals(validaMontoImpuestos()))
				return "ERROR";
			
			if (! "OK".equals(validaGrupoImpuestos()))
				return "ERROR";
		
			if (! "OK".equals(validaMontoFacturaContraTotalImpuestos()))
				return "ERROR";
			
			if(this.totRetenciones <= 0 && !this.listRetDelGasto.isEmpty()){
				this.resOperacion = mensajesInf.get(16);
				return "OK";
			}
			
			pat = Pattern.compile("(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)");
			//validando sucursal
			log.info("Validando sucursal");
			match = pat.matcher(this.sucursal);
			match.find();
			Long agente = Long.valueOf(match.group(1));
			
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			//validando gasto
			log.info("Validando gasto");
			match = pat.matcher(this.conceptoGasto);			
			match.find();
			Long concepto = Long.valueOf(match.group(1));
			
			if(perfilRequiereObra == null && !"S".equals(perfilRequiereObra)){
   				this.listProPagDet = ifzProPagDet.findByAgenteEstatusMontoConcepto(agente.toString(), "G", concepto);
   				if(this.listProPagDet == null) {
   					log.error(mensajesInf.get(19));
   					this.resOperacion = mensajesInf.get(19);
   					return "ERROR";
   				}
			}
			
			for(Object obj: this.listProPagDet){
				ProgPagosDetalle ppd = (ProgPagosDetalle)((Object[])obj)[1];
				if(!"R".equals((String)((Object[])obj)[0]) && totPago.compareTo(ppd.getMonto().doubleValue()) != 0){
					ban = 3;
				}
				if("R".equals((String)((Object[])obj)[0]) && totPago.compareTo(ppd.getMonto().doubleValue()) != 0){
					ban = 2;
				}
				if(!"R".equals((String)((Object[])obj)[0]) && totPago.compareTo(ppd.getMonto().doubleValue()) == 0){
					ban = 1;
				}   						
				if("R".equals((String)((Object[])obj)[0]) && totPago.compareTo(ppd.getMonto().doubleValue()) == 0){
					ban = 0;
					break;
				}
			}
			
			if(ban == 1) {
				log.error(mensajesInf.get(21));
				this.resOperacion = mensajesInf.get(21);
				return "ERROR";
			}
			
			if(ban == 2) {
				log.error(mensajesInf.get(22));
				this.resOperacion = mensajesInf.get(22);
				return "ERROR";
			}
			
			if(ban == 3) {
				log.error(mensajesInf.get(19));
				this.resOperacion = mensajesInf.get(19);
				return "ERROR";
			}
			
			if (this.facturaId == null)
				analizarArchivo();
			control();
			if(this.pojoRegistroGastoComprobado.getId()==null)
				registroNuevo = true;
			
			this.pojoRegistroGastoComprobado.setModificadoPor(this.usuarioId);
			this.pojoRegistroGastoComprobado.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoRegistroGastoComprobado.setIdXml(this.facturaId);
			if (this.pojoRegistroGastoComprobado.getIdObra() == null)
				this.pojoRegistroGastoComprobado.setIdObra(new Obra());
			if (this.pojoRegistroGastoComprobado.getIdOrdenCompra() == null)
				this.pojoRegistroGastoComprobado.setIdOrdenCompra(new OrdenCompra());
			   					
			if(registroNuevo) {
				this.pojoConceptoGtos = this.ifzConValores.findById(concepto);
			} else {
				if (this.pojoConceptoGtos.getId() != concepto)
					this.pojoConceptoGtos = this.ifzConValores.findById(concepto);
			}
					
			this.pojoRegistroGastoComprobado.setIdConcepto(this.pojoConceptoGtos);
			
			//validando proveedor
			/*match = pat.matcher(this.nombreProveedor);			
			if(match.find())
				if(registroNuevo)				
					this.pojoProveedores = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findById(Long.valueOf(match.group(1)));
				else
					if (this.pojoProveedores.getPersonaId()!= Long.valueOf(match.group(1)))
						this.pojoProveedores= this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findById(Long.valueOf(match.group(1)));
			   					
			//this.pojoComprobadoGto.setProveedorId(this.pojoProveedores);
			 */
			  					
			this.pojoRegistroGastoComprobado.setSubtotal(totalMenosImpuestos);
			this.pojoRegistroGastoComprobado.setTotalImpuestos(this.totImpuestos);
			this.pojoRegistroGastoComprobado.setTotalRetenciones(totRetenciones);
			this.pojoRegistroGastoComprobado.setObservaciones(this.observaciones);

			log.info("Asignamos ID Registro Gasto");
			this.pojoRegistroGastoComprobado.setIdPagosGastos(this.pojoRegistroGasto); // asignar papa //this.pojoGtosComp.getId();
			
			if( this.pojoRegistroGasto.getId() == null) {
				//si el gasto ya existe guardo las facturas creadas, sino las meto a una lista
				// y las guardare hasta que creen un gasto. Ya que sí el gasto no existe no puedo grabar 
				//facturas hasta que me serciore que creen un gasto. Siempre que el gasto no tenga llave 
				//la factura tampoko tendra llave, por eso no pregunto si es nueva 	
				
				this.pojoRegistroGastoComprobado.setCreadoPor(this.usuarioId);
				this.pojoRegistroGastoComprobado.setFechaCreacion(Calendar.getInstance().getTime());				
				   	   						
				if(! this.listDesgloseImpuestos.isEmpty())
					for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
						var.setId(this.pojoRegistroGastoComprobado.getId());
					}
				
   				if(! this.listDesgloseRet.isEmpty())
					for( PagosGastosDetImpuestosExt var : this.listDesgloseRet){
						var.setId(this.pojoRegistroGastoComprobado.getId());
					} 	
			} else {
				// si el gasto existe ya puedo grabar facturas 
				if (registroNuevo) {
					this.pojoRegistroGastoComprobado.setCreadoPor(Short.valueOf(String.valueOf(this.usuarioId)));
					this.pojoRegistroGastoComprobado.setFechaCreacion(Calendar.getInstance().getTime());

					log.info("Guardo factura en BD");
					this.pojoRegistroGastoComprobado.setId(this.ifzComprobadoGto.save(this.pojoRegistroGastoComprobado));
					log.info("Factura guardada");
					
					if(! this.listDesgloseImpuestos.isEmpty()){
						for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
							var.setId(this.pojoRegistroGastoComprobado.getId());
							var.setId(this.ifzDesgloImpto.save(var));
						} 
					}

   					if(! this.listDesgloseRet.isEmpty()){
						for( PagosGastosDetImpuestosExt var : this.listDesgloseRet){
							var.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
							var.setId(this.ifzDesgloImpto.save(var));
						} 
   					}
				} else {
					log.info("Actualizo factura en BD");
					this.ifzComprobadoGto.update(this.pojoRegistroGastoComprobado);
					log.info("Factura actualizada");
					   	   						
					if(! this.listDesgloseImpuestos.isEmpty()){
						for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
							var.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
							
							if(var.getId()== null)
								var.setId(this.ifzDesgloImpto.save(var));
							else
								this.ifzDesgloImpto.update(var);
						}
					}
					
					if(! this.listDesgloseRet.isEmpty()){
						for( PagosGastosDetImpuestosExt var : this.listDesgloseRet){
							var.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);

							if(var.getId() == 0L)
								var.setId(this.ifzDesgloImpto.save(var));
							else
								this.ifzDesgloImpto.update(var);
						}
					}
				}
				
				//eliminando los pojos de impuestos de la base de datos,
				// ya que anteriormente solo los elimine de la memoria   					
				if(! this.lisImpuestosEliminados.isEmpty()){
					for(PagosGastosDetImpuestosExt var : lisImpuestosEliminados){
						if(var.getId() != 0L)
							this.ifzDesgloImpto.delete(var);
					}
					this.lisImpuestosEliminados.clear();
				}
			}  
			
			reg.setPojoFactura(this.pojoRegistroGastoComprobado);
			reg.setListImpuestos(copiaListas(listDesgloseImpuestos));
			reg.setListRetenciones(copiaListas(listDesgloseRet));
			  					
			encontro = false;
			if(! listFacturaConImpuestos.isEmpty()){
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					/*if(reg.getPojoFactura().getConceptoId().getId() == var.getPojoFactura().getConceptoId().getId()) {
						Double varmonto = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
						Double regmonto = ((reg.getPojoFactura().getSubtotal() + reg.getPojoFactura().getTotalImpuestos())- reg.getPojoFactura().getTotalRetenciones());
						
						if(varmonto.doubleValue() == regmonto.doubleValue()){
							encontro = true;
						}
					}*/
					
					if (var.equals(reg)) {
						var = reg;
						encontro = true;
					}
				}
			}
			/*if(perfil == null && !"S".equals(perfil)){
				if(! listFacturaConImpuestos.isEmpty()){
					for(FacturaImpuestos var : this.listFacturaConImpuestos){
						/*if(reg.getPojoFactura().getConceptoId().getValorId().compareTo(var.getPojoFactura().getConceptoId().getValorId()) == 0){
							Double varmonto = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
							Double regmonto = ((reg.getPojoFactura().getSubtotal() + reg.getPojoFactura().getTotalImpuestos())- reg.getPojoFactura().getTotalRetenciones());
							if(varmonto.doubleValue() == regmonto.doubleValue()){
								encontro = true;
								this.resOperacion = mensajesInf.get(20);
			   					return "ERROR";
							}
						}* /
						if(reg.getPojoFactura().getConceptoId().getId() == var.getPojoFactura().getConceptoId().getId()) {
							Double varmonto = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
							Double regmonto = ((reg.getPojoFactura().getSubtotal() + reg.getPojoFactura().getTotalImpuestos())- reg.getPojoFactura().getTotalRetenciones());
							
							if(varmonto.doubleValue() == regmonto.doubleValue()){
								encontro = true;
								this.resOperacion = mensajesInf.get(20);
			   					return "ERROR";
							}
						}
						
						if(var.equals(reg)){
							var = reg;
							encontro = true;
						}
					}
				}
		   	}*/
			   					
			if (! encontro)
				this.listFacturaConImpuestos.add(reg);
			
			actualizaTotalFaturas();			
			this.facturaId = null;
   		} catch(Exception e) {
   			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en guardarFactura", e);
			return "ERROR";
		}

		return "OK";
   	} 	
   	
   	public String evaluaSiCambioGasto() {
		try{
			log.info("evaluando si cambio Gasto. ConceptoGasto: " + this.conceptoGasto + ". ConceptoGasto2: " + this.conceptoGasto2);
			GeneraListaProveedores();
			this.existeRetencion = true;
			
			if (this.conceptoGasto2 == null)
				this.conceptoGasto2 = "";
			
			if ("".equals(this.conceptoGasto2) || ! this.conceptoGasto2.equals(this.conceptoGasto)) {
				log.info("Gasto diferente. Desglozo impuestos");
				this.nombreProveedor="";
				this.listDesgloseImpuestos.clear();
				this.listDesgloseRet.clear();
				this.listRetEncontradas.clear();
				this.conceptoGasto2 = this.conceptoGasto;					
				desglosaImpuestos();
			} else {
				log.info("Sin Gasto previo");
				this.conceptoGasto2 = this.conceptoGasto;
			}
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en evaluaSiCambioGasto", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String cancelarRegistroGasto(){
   		Respuesta rep = new Respuesta();
   		
		try {
			rep = this.ifzGtosComp.cancelacion(pojoRegistroGasto, Calendar.getInstance().getTime(), Short.valueOf(String.valueOf(this.usuarioId)),null);
			if(rep.getResultado() == 0){
				this.pojoRegistroGasto = (PagosGastosExt) rep.getObjeto();
				this.resOperacion = "";
				
				for(PagosGastosExt var : this.listRegistroGastos){
					if(var.getId().equals(this.pojoRegistroGasto.getId())){
						var.setEstatus("X");
						break;
					}						
				}
				
				if(perfilRequiereObra == null && !"S".equals(perfilRequiereObra)){
					List<PagosGastosDet> detalles = this.ifzComprobadoGto.findByProperty("pagosGastosId", this.pojoRegistroGasto);
					List<ProgPagosDetalle> lista = null;
					if(detalles != null && ! detalles.isEmpty()){
						for(PagosGastosDet var: detalles){
							Double montoValue = ((var.getSubtotal() + var.getTotalImpuestos())- var.getTotalRetenciones());
							Long conceptoValue = var.getIdConcepto(); //var.getConceptoId().getValorId();
							lista = ifzProPagDet.findByMontoConceptoEstatus(montoValue, conceptoValue, "E");
							if(lista != null){
								lista.get(0).setEstatus("G");
								lista.get(0).setModificadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
								lista.get(0).setFechaModificacion(Calendar.getInstance().getTime());
								ifzProPagDet.update(lista.get(0));
							}
						}
					}
				}
				
			}else
				this.resOperacion = rep.getRespuesta();
		}catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en el metodo cancelarRegistroGasto.", e);
			return "ERROR";
		}

		return "OK";
	}
   	
   	public String evaluaEditar(){
   		try {
			control();
			if (this.reg.getPojoFactura().getId() != null && ! this.isDebug) {
				this.facturaConLlave = true;
				/*this.tipoMensaje = 4;
				this.resOperacion = this.mensajesInf.get(4);*/
				log.info("ERROR 4 - Una vez creado el gasto No es posible editar el comprobante");
				control(true, 4, this.mensajesInf.get(4));
				return "ERROR";
			} 
			
			this.facturaConLlave = false;
			this.pojoObra = this.pojoRegistroGastoComprobado.getIdObra();
			if (this.pojoRegistroGastoComprobado.getIdOrdenCompra() != null && this.pojoRegistroGastoComprobado.getIdOrdenCompra().getId() != null && this.pojoRegistroGastoComprobado.getIdOrdenCompra().getId() > 0L)
				this.pojoOrdenCompra = this.ifzOrdenCompra.findExtById(this.pojoRegistroGastoComprobado.getIdOrdenCompra().getId());
			desglosaImpuestos();
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en el metodo evaluaEditar.", e);
			return "ERROR";
		}

		return "OK";
   	}
   	
   	public String evaluaEliminar(){
   		try {
   			
			if(reg.getPojoFactura().getId() != null){
				facturaConLlave = true;
				this.tipoMensaje = 3;
				resOperacion = this.mensajesInf.get(3);
			} else {
				this.tipoMensaje = 0;
				resOperacion = "";
				facturaConLlave = false;
			}
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en el metodo evaluaEliminar.", e);
			return "ERROR";
		}
   		
		return "OK";
   	}
   	
   	public String eliminarImpuestoDesglosado(){
		try{

			this.sum_impto=0D;
			this.sum_imptoRet=0D;
			this.impto=0D;
		
			this.lisImpuestosEliminados.add(this.pojoDesgloImpto);
			this.listDesgloseImpuestos.remove(this.pojoDesgloImpto);
			
			if(! this.listDesgloseImpuestos.isEmpty()){			
				for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
					if("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sum_imptoRet = Utilerias.redondear(sum_imptoRet + var.getValor().doubleValue(),2);	
					else	
						if("1".equals(var.getIdImpuesto().getAtributo4()))
							sum_impto = Utilerias.redondear(sum_impto + var.getValor().doubleValue(),2);	
				}
				totImpuestos=sum_impto;
				totRetenciones = sum_imptoRet;
				this.totalMenosImpuestos= Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
				this.totPago = Utilerias.redondear( this.subtotalMasImpuestos - sum_imptoRet,2);
				
				for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
					if("0".equals(var.getIdImpuesto().getAtributo4()) && !"AC".equals(var.getIdImpuesto().getTipoCuenta())){
						var.setValor(BigDecimal.valueOf(Utilerias.redondear(Utilerias.redondear(this.totalMenosImpuestos *((Double.valueOf(var.getIdImpuesto().getAtributo1())/ 100)),2),2)));
					}
				}
				sum_impto=0D;
				sum_imptoRet=0D;
				for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
					if("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sum_imptoRet = Utilerias.redondear(sum_imptoRet + var.getValor().doubleValue(),2);	
					else	
						sum_impto = Utilerias.redondear(sum_impto + var.getValor().doubleValue(),2);	
				}
				totImpuestos=sum_impto;
				totRetenciones = sum_imptoRet;
				this.totalMenosImpuestos= Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
				this.totPago = Utilerias.redondear( this.subtotalMasImpuestos - sum_imptoRet,2);
			} else {
				//sabemos que la lista de imptos quedo vacia porque entro a esta opcion y ponemos en cero el total de impuestos
				totImpuestos=0D;	
				totRetenciones = 0D;
				this.totalMenosImpuestos = this.subtotalMasImpuestos;
				this.totPago = this.subtotalMasImpuestos;
			}
		}catch(Exception e){
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en eliminarImpuestoDesglosado", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String cambioMontoImpuesto(){
		try{
			this.resOperacion = "";
			if(this.subtotalMasImpuestos!=0){
				this.sum_impto=0D;
				this.sum_imptoRet=0D;
				for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
					if(var.getValor() != null){
						if("AC".equals(var.getIdImpuesto().getTipoCuenta()))
							sum_imptoRet = Utilerias.redondear(sum_imptoRet + var.getValor().doubleValue(),2);
						else
							sum_impto = Utilerias.redondear(sum_impto + var.getValor().doubleValue(),2);
					}
				}
				
				totImpuestos=sum_impto;
				totRetenciones=sum_imptoRet;
				totPago=Utilerias.redondear(subtotalMasImpuestos - totRetenciones,2);	
				this.totalMenosImpuestos= Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);	
			}
		} catch(Exception e){
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en cambioMontoImpuesto", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String actualizaTotalFaturas(){
		BigDecimal totalAux = BigDecimal.ZERO;
		
		try{
			this.totalFacturasReportadas = 0D;
			this.tipoMensaje = 0;
			resOperacion = "";
			
			if(! listFacturaConImpuestos.isEmpty()){				
				for(FacturaImpuestos var: listFacturaConImpuestos) {
					if (var.getPojoFactura().getSubtotal() == null)
						continue;
					if (var.getPojoFactura().getTotalImpuestos() == null)
						continue;
					if (var.getPojoFactura().getTotalRetenciones() == null)
						continue;
					
					this.totalFacturasReportadas = this.totalFacturasReportadas.doubleValue() + ((var.getPojoFactura().getSubtotal().doubleValue() + var.getPojoFactura().getTotalImpuestos().doubleValue())- var.getPojoFactura().getTotalRetenciones().doubleValue());
					totalAux = new BigDecimal(this.totalFacturasReportadas).setScale(2, BigDecimal.ROUND_HALF_EVEN);
					this.totalFacturasReportadas = totalAux.doubleValue(); //Utilerias.redondear(totalFacturasReportadas, 2);
				}
			}
		} catch(Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en actualizaTotalFaturas", e);
			throw e;
		}
		
		return "OK";
	}
   	
   	public String validaGrupoImpuestos(){
		try{
			int contador =0;
			encontroMismoGrupo=false;
			resOperacion="";
			if(! this.listDesgloseImpuestos.isEmpty()){
				for( PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos){
					contador = 0;
					for( PagosGastosDetImpuestosExt var2 : this.listDesgloseImpuestos){
						if(!encontroMismoGrupo)
							if(!"0".equals(var2.getIdImpuesto().getAtributo2())) //porque el cero es general esos pueden existir N impuestos al mimos tiempo
								if( var1.getIdImpuesto().getAtributo2().equals(var2.getIdImpuesto().getAtributo2())){
									contador = contador + 1;
									if(contador == 2){
										encontroMismoGrupo=true;
										this.tipoMensaje=1;
										resOperacion = this.mensajesInf.get(1);
										return "ERROR";
									}
								}	
					}					
				}
			}
		}catch(Exception e){
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en validaGrupoImpuestos", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String validaMontoImpuestos(){
		try{
			encontroMismoGrupo=false;
			this.tipoMensaje = 0;
			resOperacion = "";
			
			if(! this.listDesgloseImpuestos.isEmpty()){
				for( PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos){
					if(var1.getValor() == null || var1.getValor() == BigDecimal.ZERO){
						encontroMismoGrupo = true;
						this.tipoMensaje=5;
						resOperacion = this.mensajesInf.get(5);
						return "ERROR";
					}
				}
			}
		}catch(Exception e){
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en validaMontoImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
   	public String validaMontoFactura(){
		try{
			encontroMismoGrupo=false;
			this.tipoMensaje = 0;
			resOperacion = "";
			
			if(this.subtotalMasImpuestos <= 0){
				encontroMismoGrupo = true;
				this.tipoMensaje=6;
				resOperacion = this.mensajesInf.get(6);
				return "ERROR";
			}
		}catch(Exception e){
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en validaMontoFactura", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
	public String validaMontoFacturaContraTotalImpuestos(){
		try{
			encontroMismoGrupo=false;
			Double sumaImpuestos = 0D;
			this.tipoMensaje = 0;
			resOperacion = this.mensajesInf.get(0);
			
			if(! this.listDesgloseImpuestos.isEmpty()){
				for( PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos){
					if(!"AC".equals(var1.getIdImpuesto().getTipoCuenta()))
						sumaImpuestos = sumaImpuestos + var1.getValor().doubleValue();
				}
			}
			
			if(sumaImpuestos > this.subtotalMasImpuestos){
				encontroMismoGrupo = true;
				this.tipoMensaje=7;
				resOperacion = this.mensajesInf.get(7);
				return "ERROR";
			}
		}catch(Exception e){
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en validaMontoFacturaContraTotalImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
	
	public String cancelarCheques(){
   		/*try{
   			Respuesta resp = new Respuesta();
   			resOperacion=""; 			
   			resp = ifzGtosComp.salvar(this.pojoGtosComp, false,null);
			
			
			if(! "BIEN".equals(resp.getRespuesta()) && resp.getResultado() == -1){
				
				encontroMismoGrupo = true;// esta variable termine usandola como bandera gral
				this.resOperacion = resp.getRespuesta();
				this.tipoMensaje = 8;
				
				//antes le mostraba el mensaje que me regresaba la funcion CANCELA_CHEQUES pero ahora solo le dire que se guardo		
				guardar();
				
			}
   		}catch(Exception e){
   			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en cancelarCheques", e);
			return "ERROR";
		}*/
		
		return "OK";
   	}

	public String desglosaImpuestos() {
		List<PagosGastosDetImpuestosExt> listDesgloseImpuestos_tmp = new ArrayList<PagosGastosDetImpuestosExt>();
		Pattern pat = null;
		Matcher match = null;
		long conceptoGtoId = 0;
		
		try { 
			this.sum_impto = 0D;
			this.impto = 0D;
			this.imptoRet = 0D;
			this.sum_imptoRet = 0D;
				
   			//validando gasto para que el pojo de gasto tenga valor correcto ya que a veces se quedaba con el valor del ultimo gasto que escogieron en otra factura
			log.info("Desglosando impuestos");
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			log.info("Validando gasto");
			match = pat.matcher(this.conceptoGasto);			
			if (match.find()) {
				conceptoGtoId = Long.valueOf(match.group(1));
				if (this.pojoConceptoGtos != null && conceptoGtoId != this.pojoConceptoGtos.getId())
					this.pojoConceptoGtos = this.ifzConValores.findById(conceptoGtoId);
			}

			log.info("Obteniendo impuestos");
			this.listImpuestosDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","DE", this.pojoConceptoGtos.getId());
			if (! this.listImpuestosDelGasto.isEmpty()) {
				//si viene vacio es posible que hayan cambiado el concepto de gasto o es la primera vez que se cargaran lo imptos al comprobante
				if (this.listDesgloseImpuestos.isEmpty()) {
					log.info("Generando impuestos");
					for (GastosImpuestoExt var : this.listImpuestosDelGasto) {
						if ("1".equals(var.getImpuestoId().getAtributo4())) {
							this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
							this.pojoDesgloImpto.setModificadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
							this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setCreadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
							this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
						
							this.impto = Utilerias.redondear(this.subtotalMasImpuestos - (Utilerias.redondear(this.subtotalMasImpuestos / ((Double.valueOf(var.getImpuestoId().getAtributo1()) / 100) + 1), 2)), 2);
							this.pojoDesgloImpto.setValor(new BigDecimal(impto));		
							this.sum_impto = Utilerias.redondear(this.sum_impto + this.pojoDesgloImpto.getValor().doubleValue(), 2);
							this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
						}
					}
					
					this.totImpuestos = this.sum_impto;
					this.totalMenosImpuestos = Utilerias.redondear(this.subtotalMasImpuestos - this.sum_impto, 2);
					
					for (GastosImpuestoExt var : this.listImpuestosDelGasto) {
						if ("0".equals(var.getImpuestoId().getAtributo4())) {
							this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
							this.pojoDesgloImpto.setModificadoPor(Long.valueOf(this.usuarioId));
							this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setCreadoPor(Long.valueOf(this.usuarioId));
							this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
						
							this.impto = Utilerias.redondear(Utilerias.redondear(this.totalMenosImpuestos * ((Double.valueOf(var.getImpuestoId().getAtributo1()) / 100)), 2), 2);
							this.pojoDesgloImpto.setValor(BigDecimal.valueOf(this.impto));		
							this.sum_impto = Utilerias.redondear(this.sum_impto + this.pojoDesgloImpto.getValor().doubleValue(), 2);
							this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
						}
					}
					
					this.totImpuestos = this.sum_impto;
					this.totalMenosImpuestos = Utilerias.redondear(this.subtotalMasImpuestos - this.sum_impto, 2);
					
					//Verifico si existen retenciones para el gasto
					log.info("Obteniendo retenciones");
					this.listRetDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","AC", this.pojoConceptoGtos.getId());
					if (! this.listRetDelGasto.isEmpty()) {
						log.info("Generando retenciones");
						for (GastosImpuestoExt var: this.listRetDelGasto) {
							this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();					
							this.pojoDesgloImpto.setModificadoPor(Long.valueOf(this.usuarioId));
							this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setCreadoPor(Long.valueOf(this.usuarioId));
							this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
							
							this.impto = Utilerias.redondear(Utilerias.redondear(this.totalMenosImpuestos *((Double.valueOf(var.getImpuestoId().getAtributo1())/ 100)),2),2);
							this.pojoDesgloImpto.setValor(BigDecimal.valueOf(this.impto));
							
							this.sum_imptoRet = Utilerias.redondear(this.sum_imptoRet + this.pojoDesgloImpto.getValor().doubleValue(), 2);
							this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
							this.totRetenciones = this.sum_imptoRet;
						}
					} else {
						this.listRetDelGasto.clear();
						this.listDesgloseRet.clear();
						log.info("Sin retenciones");
					}
				} else {// la lista si tenia elementos y hay que hacer el desglose imptos en base a los imptos que tiene actualmente la lista
					listDesgloseImpuestos_tmp.addAll(listDesgloseImpuestos);					
					this.listDesgloseImpuestos.clear();
					this.totalMenosImpuestos = this.subtotalMasImpuestos;

					log.info("Desglosando impuestos");
					for (PagosGastosDetImpuestosExt var : listDesgloseImpuestos_tmp) {		
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta())) {
							var.setValor(BigDecimal.ZERO);
							this.sum_imptoRet = Utilerias.redondear(this.sum_imptoRet + var.getValor().doubleValue(), 2);
						} else {
							if("0".equals(var.getIdImpuesto().getAtributo4())) {
								this.impto =Utilerias.redondear(Utilerias.redondear(this.totalMenosImpuestos *((Double.valueOf(var.getIdImpuesto().getAtributo1())/ 100)),2),2);
								var.setValor(BigDecimal.valueOf(this.impto));
								this.sum_impto = Utilerias.redondear(this.sum_impto + var.getValor().doubleValue(),2);
							} else if("1".equals(var.getIdImpuesto().getAtributo4())) {
								this.impto =Utilerias.redondear(this.subtotalMasImpuestos -(Utilerias.redondear(this.subtotalMasImpuestos /((Double.valueOf(var.getIdImpuesto().getAtributo1())/ 100)+1),2)),2);
								var.setValor(BigDecimal.valueOf(this.impto));
								this.sum_impto = Utilerias.redondear(this.sum_impto + var.getValor().doubleValue(),2);
							}
						}
						
						this.listDesgloseImpuestos.add(var);
					}
					
					this.totImpuestos = this.sum_impto;
					this.totRetenciones = this.sum_imptoRet;
				}
			} else {
				log.info("Sin impuestos asociados");
				//porque si no tiene impuestos asociados el gasto, esta propiedad se quedaba NULL y marcaba error
				this.totImpuestos = 0D;
				this.totRetenciones = 0D;
			}

			this.totalMenosImpuestos = Utilerias.redondear(this.subtotalMasImpuestos - this.sum_impto, 2);
			this.totPago = Utilerias.redondear(this.subtotalMasImpuestos - this.totRetenciones, 2);
		} catch(Exception e) {
			log.error("Error en desglosaImpuesto", e);
			this.resOperacion = this.mensajesInf.get(-1);
			return "ERROR";
		}
		
		return "OK";
	}
   	
	public String agregaRet(){
		try{
			resOperacion= "";
			Double mto_ret; 
			if(!listDesgloseImpuestos.isEmpty()){
				for(PagosGastosDetImpuestosExt var: listDesgloseImpuestos){
					if(var.getIdImpuesto().getId() == this.pojoNvaRet.getId()){
						this.resOperacion = mensajesInf.get(17);
						return "OK";
					}
				}
			}
			
			this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
			this.pojoDesgloImpto.setIdImpuesto(this.pojoNvaRet);
			this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
			mto_ret = Utilerias.redondear((subtotalMasImpuestos - this.totImpuestos) * (Double.valueOf(pojoNvaRet.getAtributo1())/ 100),2);
			this.totRetenciones = Utilerias.redondear(this.totRetenciones + mto_ret,2);
			this.pojoDesgloImpto.setValor(BigDecimal.valueOf(mto_ret));
			this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
			this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
			this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
			listDesgloseImpuestos.add(this.pojoDesgloImpto);
		} catch(Exception e) {
			log.error("Error en metodo agregaRet",e);
		}
		return "OK";
	}

	public List<Persona> autoacompletaTerceros (Object obj){
		try{
			this.listTerceros=this.ifzPersonas.findLikePersonas(obj.toString(), 20);
			return this.listTerceros;
		} catch(Exception e) {
			log.error("error en autoacompletaTerceros", e);
			return new ArrayList<Persona>();
		}
	}
	
	public String cambioTerceros(){
		Pattern pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		Matcher match = null;
		this.resOperacion = "";
		try{
			if("S".equals(this.operacion)){
				match = pat.matcher(this.beneficiario);
				if(match.find()){		
					this.pojoTerceros = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findById(Long.valueOf(match.group(1)));
					if(this.pojoTerceros != null){
						if(this.pojoTerceros.getBanco() != null)
							if(this.pojoTerceros.getClabeInterbancaria() != null && !"".equals(this.pojoTerceros.getClabeInterbancaria()))
								//if(this.pojoTerceros.getgetCuentaBancaria() != null && !"".equals(this.pojoTerceros.getCuentaBancaria()))
									//if("F".equals(this.pojoTerceros.getPersonalidad()))
										if(this.pojoTerceros.getNombre() != null && !"".equals(this.pojoTerceros.getNombre()) && this.pojoTerceros.getPrimerApellido() != null && !"".equals(this.pojoTerceros.getPrimerApellido()))
											return "OK";
										else
											this.resOperacion = "El nombre del Beneficiario no esta completo, verifique !!";
									/*else
										if(this.pojoTerceros.getNombre() != null && !"".equals(this.pojoTerceros.getNombre()))
											return "OK";
										else
											this.resOperacion = "El nombre del Beneficiario no esta completo, verifique !!";*/
								//else
									//this.resOperacion = "El Beneficiario no tiene capturada cuenta bancaria, verifique !!";
							else
								this.resOperacion = "El Beneficiario no tiene capturada cuenta Clabe, verifique !!";
						else
							this.resOperacion = "El Beneficiario no tiene capturado banco, verifique !!";
					}
				}
			}
		} catch(Exception e) {			
			log.error("error en cambioTerceros", e);
			return "ERROR";
		}

		return "OK";
	}
	
	public List<Area> autoacompletaSegmento(Object obj){
		try{
			this.listAreas = this.ifzAreas.findLikeClaveNombre(obj.toString()); 
			// this.listAreas = this.ifzAreas.findLikeClaveSegmento(obj.toString(),20, this.sucursalesVisibles);
			return this.listAreas;
		} catch(Exception e){
			log.error("error en autoacompletaSucursal", e);
			return new ArrayList<Area>();
		}
	}
	
	public List<Persona> autoacompletaBeneficiario(Object obj){
		try{
			this.listBeneficiarios = this.ifzPersonas.findLikeClaveNombre("nombre",obj.toString(),this.pojoGpoValPersonas,this.tipoPersona,20,false);	
			return this.listBeneficiarios;
		}
		catch(Exception e){
			log.error("error en autoacompletaBeneficiario", e);
			return new ArrayList<Persona>();
		}
	}
		   	
	public List<ConValores> autoacompletaConceptoGasto(Object obj){
		try {
			log.info("Recuperando conceptos de gastos");
			this.listConceptoGasto = this.ifzConValores.findLikeValorIdPropiedadGrupo(obj.toString() , this.pojoGpoValGasto, 20);
			return this.listConceptoGasto;
		} catch(Exception e){
			log.error("error en autoacompletaConceptoGasto", e);
			return new ArrayList<ConValores>();
		} finally {
			if (this.listConceptoGasto == null || this.listConceptoGasto.isEmpty())
				log.info("Ningun conceptos de gasto encontrado");
			else
				log.info(this.listConceptoGasto.size() + " conceptos de gasto encontrados");
		}
	}	
	
	public List<Persona> autoacompletaProveedor (Object obj){
		try{
			this.listProveedores = this.ifzPersonas.findLikeProveedor(obj.toString(), this.pojoGpoValPersonas, "PROV", this.pojoConceptoGtos.getId(), 20);
			return this.listProveedores;
		} catch(Exception e){
			log.error("error en autoacompletaProveedor", e);
			return new ArrayList<Persona>();
		}
	}
	
	public String GeneraListaProveedores (){
		Pattern pat = null;
		Matcher match = null;
		
		try{
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			//validando gasto para poder presentar  una lista de proveedores en base al gasto seleccionado
			match = pat.matcher(this.conceptoGasto);			
			if(match.find())							
				this.pojoConceptoGtos = this.ifzConValores.findById(Long.valueOf(match.group(1)));
		} catch(Exception e){
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en GeneraListaProveedores", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public FacturaImpuestos getReg() {
		return reg;
	}

	public void setReg(FacturaImpuestos reg) {
		this.reg = new FacturaImpuestos();
		this.listDesgloseImpuestos.clear();
		this.listDesgloseRet.clear();
		this.listRetDelGasto.clear();

		this.reg = reg;
		this.pojoRegistroGastoComprobado = this.reg.getPojoFactura();
		this.conceptoGasto = this.reg.getPojoFactura().getIdConcepto().getId() +" - "+ this.reg.getPojoFactura().getIdConcepto().getDescripcion();
		this.nombreProveedor = this.reg.getPojoFactura().getIdProveedor().getId() +" - "+ this.reg.getPojoFactura().getIdProveedor().getNombre(); //this.reg.getPojoFactura().getProveedorId().getPersonaId() +" - "+ this.reg.getPojoFactura().getProveedorId().getNombre() +" "+ (this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() : "") +" "+(this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() : "");
		this.descripcionFactura = this.reg.getPojoFactura().getIdConcepto().getDescripcion() + " - " + this.reg.getPojoFactura().getIdPagosGastos().getBeneficiario(); // + " - "+this.reg.getPojoFactura().getProveedorId().getNombre()+" " + (this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() : "")+" " +(this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() :"");
		this.subtotalMasImpuestos = Utilerias.redondear((this.reg.getPojoFactura().getSubtotal() + this.reg.getPojoFactura().getTotalImpuestos()), 2);
		this.totPago = Utilerias.redondear((this.subtotalMasImpuestos - this.reg.getPojoFactura().getTotalRetenciones()), 2);
		this.totalMenosImpuestos = this.reg.getPojoFactura().getSubtotal();
		this.totImpuestos = this.reg.getPojoFactura().getTotalImpuestos();
		this.totRetenciones = this.reg.getPojoFactura().getTotalRetenciones();
		this.observaciones = this.reg.getPojoFactura().getObservaciones();
		
		if (this.reg.getListImpuestos() != null && ! this.reg.getListImpuestos().isEmpty())
			this.listDesgloseImpuestos = copiaListas(reg.getListImpuestos());
		if (this.reg.getListRetenciones() != null && ! this.reg.getListRetenciones().isEmpty())
			this.listDesgloseRet = copiaListas(reg.getListRetenciones());
		
		if ((this.listDesgloseImpuestos == null || this.listDesgloseImpuestos.isEmpty()) && (this.listDesgloseRet == null || this.listDesgloseRet.isEmpty()))
			desglosaImpuestos();
		else
			this.conceptoGasto2 = this.conceptoGasto;
	}

	private void control() {
		control(false, 0 , "");
	}

	private void control(boolean operacionCancelada) {
		if (operacionCancelada)
			control(true, 1, "ERROR");
		else
			control(false, 0 , "");
	}

	private void control(String mensaje) {
		control(true, -1, mensaje);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.encontroMismoGrupo = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.resOperacion = mensaje;
	}

	// ------------------------------------------------------------------------------------------
	// CARGA DE ARCHIVO
	// ------------------------------------------------------------------------------------------
	
	public void nuevaCarga() {
		control();
		this.fileSrc = null;
	}
	
	public void uploadListener(UploadEvent event) throws Exception {
		try {
			control();
			UploadItem item = event.getUploadItem();
			this.fileSrc = ((item.isTempFile()) ? Files.readAllBytes(item.getFile().toPath()) : item.getData());
		} catch (Exception e) {
			log.error("Error en uploadListener", e);
			control(true);
		}
	}
	
	public void analizarArchivo() throws Exception {
		try {
			control();
			if (this.fileSrc == null) {
				this.facturaId = 0L;
				log.warn("No ha especificado ninguna factura (*.xml)");
				control("No ha especificado ninguna factura (*.xml)");
				return;
			} 

			this.facturaId = this.ifzComprobadoGto.analizaFactura(this.fileSrc);
			if (this.facturaId == null || this.facturaId <= 0L)
				this.facturaId = 0L;
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.CajaChicaAction.analizarArchivo(). No ha especificado ninguna factura (*.xml)", e);
			control(true);
		}
	}

	// ------------------------------------------------------------------------------------------
	// BUSQUEDA OBRAS
	// ------------------------------------------------------------------------------------------
	
	public void obrasBusqueda() {
		control();
		
		// Inicializaciones
		this.campoBusquedaObra = this.tipoBusquedaObra.get(0);
		this.valTipoBusquedaObra = "";
		this.tipoObraBusquedaObra = (int) this.listObrasTiposItems.get(0).getValue();
		this.paginacionObras = 1;
		
		if (this.listObras == null)
			this.listObras = new ArrayList<Obra>();
		this.listObras.clear();
		
		if (this.pojoObra == null)
			this.pojoObra = new Obra();
	}

	public String buscarObras(){
		try {
			control();
			if ("".equals(this.campoBusquedaObra) || "Nombre".equals(this.campoBusquedaObra))
				this.campoBusquedaObra = "nombre";
			else if("Cliente".equals(this.campoBusquedaObra))
				this.campoBusquedaObra = "nombreCliente";
			else if("Contrato".equals(this.campoBusquedaObra))
				this.campoBusquedaObra = "nombreContrato";
			else if("Clave".equals(this.campoBusquedaObra))
				this.campoBusquedaObra = "idObra";
			
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valTipoBusquedaObra, this.tipoObraBusquedaObra);
			if (this.listObras.isEmpty()) {
				log.info("OBRAS - Busqueda sin resultados");
				control(mensajesInf.get(13));
			} 
		} catch(Exception e) {
			log.error("error en buscar obras", e);
			control(this.mensajesInf.get(-1));
			return "ERROR";
		}
		
		return "OK";
	}

	public void seleccionarObra() {
		try {
			control();
			this.pojoRegistroGasto.setIdObra(this.pojoObra);
			obrasBusqueda();
			
			this.pojoOrdenCompra = null;
			ordenCompraBusqueda();
		} catch (Exception e) {
			log.error("error en seleccionarObra", e);
			control(true);
		}
	}
		
	public String cambioObra(){
		try {
			this.tipoObra = 1;
		} catch (Exception e) {
			log.error("error en cambioObra", e);
			return "ERROR";
		}
		
		return "OK";
	}

	// ------------------------------------------------------------------------------------------
	// BUSQUEDA ORDEN DE COMPRA
	// ------------------------------------------------------------------------------------------
	
	public void ordenCompraBusqueda() {
		control();
		
		// Inicializaciones
		this.campoBusquedaOrdenCompra = this.opcionesBusquedaOrdenCompra.get(0);
		this.valorBusquedaOrdenCompra = "";
		this.paginacionOrdenCompra = 1;
		
		if (this.listOrdenCompra == null)
			this.listOrdenCompra = new ArrayList<OrdenCompraExt>();
		this.listOrdenCompra.clear();
		
		if (this.pojoOrdenCompra == null)
			this.pojoOrdenCompra = new OrdenCompraExt();
		
		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
			this.encontroMismoGrupo = true;
			this.tipoMensaje = 1;
			this.resOperacion = "Debe seleccionar una Obra.";
		}
	}
	
	public void ordenCompraBuscar() {
		try {
			control();
			if ("".equals(this.campoBusquedaOrdenCompra))
				this.campoBusquedaOrdenCompra = this.opcionesBusquedaOrdenCompra.get(0);
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				log.info("ORDEN DE COMPRA - Debe seleccionar una Obra.");
				control("Debe seleccionar una Obra.");
				return;
			}	
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("idObra", this.pojoObra.getId().toString());
			if ("Proveedor".equals(this.campoBusquedaOrdenCompra))
				params.put("nombreProveedor", this.valorBusquedaOrdenCompra);
			if ("Id".equals(this.campoBusquedaOrdenCompra))
				params.put("id", this.valorBusquedaOrdenCompra);
			
			this.listOrdenCompra = this.ifzOrdenCompra.findExtLikeProperties(params, 100);
			if (this.listOrdenCompra == null || this.listOrdenCompra.isEmpty()) {
				log.info("ORDEN DE COMPRA - Busqueda sin resultados");
				control(mensajesInf.get(13));
			}
		} catch (Exception e) {
			log.error("ORDEN DE COMPRA - Error al buscar", e);
			control(true);
		}
	}
	
	public void ordenCompraSeleccionar() {
		// Asignamos la orden de compra al REGISTRO DE GASTO, si corresponde
		if (this.pojoOrdenCompra != null && this.pojoOrdenCompra.getId() != null && this.pojoOrdenCompra.getId() > 0L) {
			//this.pojoGtosComp.setIdOrdenCompra(this.pojoOrdenCompra.getId()); //this.pojoGtosComp.setIdOrdenCompra(this.pojoOrdenCompra);
			this.pojoRegistroGastoComprobado.setIdObra(this.pojoObra);
			this.pojoRegistroGastoComprobado.setIdOrdenCompra(this.ifzOrdenCompra.findById(this.pojoOrdenCompra.getId()));
		}
	}
	
	public String getOrdenCompraDescripcion() {
		if (this.pojoOrdenCompra != null && this.pojoOrdenCompra.getId()!= null && this.pojoOrdenCompra.getId()> 0L)
			return this.pojoOrdenCompra.getFolio() + " - " + this.pojoOrdenCompra.getIdProveedor().getNombre() + " - $ " + String.format("%1$,.2f", this.pojoOrdenCompra.getTotal());
		return "";
	}
	
	public void setOrdenCompraDescripcion(String value) {}
	
	// ------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------------

	public ConGrupoValores getPojoGpoValTerceros() {
		return pojoGpoValTerceros;
	}

	public void setPojoGpoValTerceros(ConGrupoValores pojoGpoValTerceros) {
		this.pojoGpoValTerceros = pojoGpoValTerceros;
	}

	public PersonaExt getPojoTerceros() {
		return pojoTerceros;
	}

	public void setPojoTerceros(PersonaExt pojoTerceros) {
		this.pojoTerceros = pojoTerceros;
	}

	public SucursalExt getPojoSucursal() {
		return pojoSucursal;
	}

	public void setPojoSucursal(SucursalExt pojoSucursal) {
		this.pojoSucursal = pojoSucursal;
	}

	public ConGrupoValores getPojoGpoValGasto() {
		return pojoGpoValGasto;
	}

	public void setPojoGpoValGasto(ConGrupoValores pojoGpoValGasto) {
		this.pojoGpoValGasto = pojoGpoValGasto;
	}

	public PersonaExt getPojoProveedores() {
		return pojoProveedores;
	}

	public void setPojoProveedores(PersonaExt pojoProveedores) {
		this.pojoProveedores = pojoProveedores;
	}

	public PagosGastosDetExt getPojoComprobadoGto() {
		return pojoRegistroGastoComprobado;
	}

	public void setPojoComprobadoGto(PagosGastosDetExt pojoComprobadoGto) {
		this.pojoRegistroGastoComprobado = pojoComprobadoGto;
	}
	
	public String getReferencia() {
		return this.pojoRegistroGastoComprobado.getReferencia() != null ? this.pojoRegistroGastoComprobado.getReferencia() : "";
	}

	public void setReferencia(String referencia) {
		this.pojoRegistroGastoComprobado.setReferencia(referencia);
	}

	public double getSubtotal() {
		return this.pojoRegistroGastoComprobado.getSubtotal() != null ? this.pojoRegistroGastoComprobado.getSubtotal() : 0;
	}

	public void setSubtotal(double subtotal) {
		this.pojoRegistroGastoComprobado.setSubtotal(subtotal);
	}
	
	public Date getFechaCompruebaGto() {
		if (this.pojoRegistroGastoComprobado.getFecha()!= null)
			return this.pojoRegistroGastoComprobado.getFecha();
		return Calendar.getInstance().getTime();
	}

	public void setFechaCompruebaGto(Date fechaCompruebaGto) {
		this.pojoRegistroGastoComprobado.setFecha(fechaCompruebaGto);
	}
	
	public boolean getFacturaCredito() {
		return this.pojoRegistroGastoComprobado.getEsCredito();
	}

	public void setFacturaCredito(boolean facturaCredito) {
		this.pojoRegistroGastoComprobado.setEsCredito(facturaCredito);
	}
	
	public Long getPagosGastosDetId() {
		return this.pojoRegistroGastoComprobado.getId() != null ? this.pojoRegistroGastoComprobado.getId() : 0;
	}

	public void setPagosGastosDetId(Long pagosGastosDetId) {
		this.pojoRegistroGastoComprobado.setId(pagosGastosDetId);
	}
	
	public Double getTotalImpuestos() {
		return this.pojoRegistroGastoComprobado.getTotalImpuestos() != null ? this.pojoRegistroGastoComprobado.getTotalImpuestos() : 0;
	}

	public void setTotalImpuestos(Double totalImpuestos) {
		this.pojoRegistroGastoComprobado.setTotalImpuestos(totalImpuestos);
	}
	
	public ConValores getPojoConceptoGtos() {
		return pojoConceptoGtos;
	}

	public void setPojoConceptoGtos(ConValores pojoConceptoGtos) {
		this.pojoConceptoGtos = pojoConceptoGtos;
	}

	public PagosGastosDetImpuestosExt getPojoDesgloImpto() {
		return pojoDesgloImpto;
	}

	public void setPojoDesgloImpto(PagosGastosDetImpuestosExt pojoDesgloImpto) {
		this.pojoDesgloImpto = pojoDesgloImpto;
	}

	public PagosGastosExt getPojoGtosComp() {
		return pojoRegistroGasto;
	}

	public void setPojoGtosComp(PagosGastosExt pojoGtosComp) {
		this.pojoRegistroGasto = pojoGtosComp;
	}
	
	public String getConcepto() {
		if (this.pojoRegistroGasto.getConcepto() != null) 
			return this.pojoRegistroGasto.getConcepto();
		return "";
	}

	public void setConcepto(String concepto) {
		this.pojoRegistroGasto.setConcepto(concepto);
	}
	
	public ConGrupoValores getPojoGpoValPersonas() {
		return pojoGpoValPersonas;
	}

	public void setPojoGpoValPersonas(ConGrupoValores pojoGpoValPersonas) {
		this.pojoGpoValPersonas = pojoGpoValPersonas;
	}

	public PersonaExt getPojoBeneficiarios() {
		return pojoBeneficiarios;
	}

	public void setPojoBeneficiarios(PersonaExt pojoBeneficiarios) {
		this.pojoBeneficiarios = pojoBeneficiarios;
	}

	public CtasBancoExt getPojoCtas() {
		return pojoCtas;
	}

	public void setPojoCtas(CtasBancoExt pojoCtas) {
		this.pojoCtas = pojoCtas;
	}
	
	public Date getFecha() {
		return this.pojoRegistroGasto.getFecha() != null ? this.pojoRegistroGasto.getFecha() : Calendar.getInstance().getTime();
	}

	public void setFecha(Date fecha) {
		this.pojoRegistroGasto.setFecha(fecha);
	}
		
	public Long getPagosGastosId() {
		return this.pojoRegistroGasto.getId() != null ? this.pojoRegistroGasto.getId() : 0;
	}

	public void setPagosGastosId(Long pagosGastosId) {
		this.pojoRegistroGasto.setId(pagosGastosId);
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
	
	public List<PagosGastosDetImpuestosExt> getListDesgloseImpuestos() {
		return listDesgloseImpuestos;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setListDesgloseImpuestos(List<PagosGastosDetImpuestosExt> listDesgloseImpuestos) {
		this.listDesgloseImpuestos = (List) ((ArrayList)listDesgloseImpuestos).clone();
	}

	public List<GastosImpuestoExt> getListImpuestosDelGasto() {
		return listImpuestosDelGasto;
	}

	public void setListImpuestosDelGasto(List<GastosImpuestoExt> listImpuestosDelGasto) {
		this.listImpuestosDelGasto = listImpuestosDelGasto;
	}

	public List<Persona> getListTerceros() {
		return listTerceros;
	}

	public void setListTerceros(List<Persona> listTerceros) {
		this.listTerceros = listTerceros;
	}

	public String getNombreBeneficiario() {
		return nombreBeneficiario;
	}

	public void setNombreBeneficiario(String nombreBeneficiario) {
		this.nombreBeneficiario = nombreBeneficiario;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public Double getTotalMenosImpuestos() {
		return totalMenosImpuestos;
	}

	public void setTotalMenosImpuestos(Double totalMenosImpuestos) {
		this.totalMenosImpuestos = totalMenosImpuestos;
	}

	public Double getTotalFacturasReportadas() {
		return totalFacturasReportadas;
	}

	public void setTotalFacturasReportadas(Double totalFacturasReportadas) {
		this.totalFacturasReportadas = totalFacturasReportadas;
	}

	public Double getImpto() {
		return impto;
	}

	public void setImpto(Double impto) {
		this.impto = impto;
	}

	public Double getSum_impto() {
		return sum_impto;
	}

	public void setSum_impto(Double sum_impto) {
		this.sum_impto = sum_impto;
	}

	public boolean isEncontroMismoGrupo() {
		return encontroMismoGrupo;
	}

	public void setEncontroMismoGrupo(boolean encontroMismoGrupo) {
		this.encontroMismoGrupo = encontroMismoGrupo;
	}

	public String getConceptoGasto() {
		return conceptoGasto;
	}

	public void setConceptoGasto(String conceptoGasto) {
		this.conceptoGasto = conceptoGasto;
	}
	
	public Double getSubtotalMasImpuestos() {
		return subtotalMasImpuestos;
	}

	public void setSubtotalMasImpuestos(Double subtotalMasImpuestos) {
		this.subtotalMasImpuestos = subtotalMasImpuestos;
		this.totPago = subtotalMasImpuestos;
	}

	public String getDescripcionFactura() {
		return descripcionFactura;
	}

	public void setDescripcionFactura(String descripcionFactura) {
		this.descripcionFactura = descripcionFactura;
	}

	public List<Persona> getListProveedores() {
		return listProveedores;
	}

	public void setListProveedores(List<Persona> listProveedores) {
		this.listProveedores = listProveedores;
	}

	public List<ConValores> getListConceptoGasto() {
		return listConceptoGasto;
	}

	public void setListConceptoGasto(List<ConValores> listConceptoGasto) {
		this.listConceptoGasto = listConceptoGasto;
	}
	
	public List<FacturaImpuestos> getListFacturaConImpuestos() {
		return listFacturaConImpuestos;
	}

	public void setListFacturaConImpuestos( List<FacturaImpuestos> listFacturaConImpuestos) {
		this.listFacturaConImpuestos = listFacturaConImpuestos;
	}
	
	public List<Sucursal> getListSucursales() {
		return listSucursales;
	}

	public void setListSucursales(List<Sucursal> listSucursales) {
		this.listSucursales = listSucursales;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
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

	public String getTerceros() {
		return terceros;
	}

	public void setTerceros(String terceros) {
		this.terceros = terceros;
	}
		
	public String getTipoPersona() {
		return this.tipoPersona;
	}
	
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
		this.beneficiario="";
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensje) {
		this.tipoMensaje = tipoMensje;
	}

	public boolean isFacturaConLlave() {
		return facturaConLlave;
	}

	public void setFacturaConLlave(boolean facturaConLlave) {
		this.facturaConLlave = facturaConLlave;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setTipoBusqueda(String [] tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String [] getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setListGastos(List<PagosGastosExt> listGastos) {
		this.listRegistroGastos = listGastos;
	}

	public List<PagosGastosExt> getListGastos() {
		return listRegistroGastos;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public Double getTotImpuestos() {
		return totImpuestos;
	}

	public void setTotImpuestos(Double totImpuestos) {
		this.totImpuestos = totImpuestos;
	}

	public Double getImptoRet() {
		return imptoRet;
	}

	public void setImptoRet(Double imptoRet) {
		this.imptoRet = imptoRet;
	}

	public Double getSum_imptoRet() {
		return sum_imptoRet;
	}

	public void setSum_imptoRet(Double sum_imptoRet) {
		this.sum_imptoRet = sum_imptoRet;
	}

	public List<GastosImpuestoExt> getListRetDelGasto() {
		return listRetDelGasto;
	}

	public void setListRetDelGasto(List<GastosImpuestoExt> listRetDelGasto) {
		this.listRetDelGasto = listRetDelGasto;
	}

	public boolean isExisteRetencion() {
		return existeRetencion;
	}

	public void setExisteRetencion(boolean existeRetencion) {
		this.existeRetencion = existeRetencion;
	}

	public List<PagosGastosDetImpuestosExt> getListDesgloseRet() {
		return listDesgloseRet;
	}

	public void setListDesgloseRet(List<PagosGastosDetImpuestosExt> listDesgloseRet) {
		this.listDesgloseRet = listDesgloseRet;
	}

	public Double getTotRetenciones() {
		return totRetenciones;
	}

	public void setTotRetenciones(Double totRetenciones) {
		this.totRetenciones = totRetenciones;
	}

	public Double getTotPago() {
		return totPago;
	}

	public void setTotPago(Double totPago) {
		this.totPago = totPago;
	}

	public String getValBusqRet() {
		return valBusqRet;
	}

	public void setValBusqRet(String valBusqRet) {
		this.valBusqRet = valBusqRet;
	}

	public String getCampoBusqRet() {
		return campoBusqRet;
	}

	public void setCampoBusqRet(String campoBusqRet) {
		this.campoBusqRet = campoBusqRet;
	}

	public String[] getListBusqRet() {
		return listBusqRet;
	}

	public void setListBusqRet(String[] listBusqRet) {
		this.listBusqRet = listBusqRet;
	}

	public List<ConValores> getListRetEncontradas() {
		return listRetEncontradas;
	}

	public void setListRetEncontradas(List<ConValores> listRetEncontradas) {
		this.listRetEncontradas = listRetEncontradas;
	}

	public ConValores getPojoNvaRet() {
		return pojoNvaRet;
	}

	public void setPojoNvaRet(ConValores pojoNvaRet) {
		this.pojoNvaRet = pojoNvaRet;
	}

	public void setTipoReporte(int tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public int getTipoReporte() {
		return tipoReporte;
	}

	public String generaPoliza(){
		/*try {
			this.pojoGtosComp=ifzGtosComp.findAllById(pojoGtosComp.getId());
			ifzGtosComp.mensajeSaldoCtas(pojoGtosComp, this.pojoGtosComp.getIdCuentaOrigen().getEmpresa().getId());
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return "OK";
	}

	public List<Area> getListAreas() {
		return listAreas;
	}

	public void setListAreas(List<Area> listAreas) {
		this.listAreas = listAreas;
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public boolean isMuestraSeg() {
		return muestraSeg;
	}

	public void setMuestraSeg(boolean muestraSeg) {
		this.muestraSeg = muestraSeg;
	}

	public Long getSegNeg() {
		return segNeg;
	}

	public void setSegNeg(Long segNeg) {
		this.segNeg = segNeg;
	}

	public ObraRem getIfzObras() {
		return ifzObras;
	}

	public void setIfzObras(ObraRem ifzObras) {
		this.ifzObras = ifzObras;
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

	public int getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(int tipoObra) {
		this.tipoObra = tipoObra;
	}
	
	public String getPerfilRequiereObra() {
		return perfilRequiereObra;
	}

	public void setPerfilRequiereObra(String perfilRequiereObra) {
		this.perfilRequiereObra = perfilRequiereObra;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public String getObraNombre() {
   		if (this.pojoObra != null && this.pojoObra.getNombre() != null && ! "".equals(this.pojoObra.getNombre()))
   	   		return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
   		return "";
	}
	
   	public void setObraNombre(String value) { }
   	
   	public int getObraNombreLength() {
   		if ("".equals(getObraNombre()))
   			return 0;
   		
		if (getObraNombre().length() <= 3)
			return 1;   		
		else if (getObraNombre().length() <= 10)
			return getObraNombre().length();
		else
			return getObraNombre().length() - 2;
	}
   	
   	public String getFacturaId() {
		if (this.facturaId != null && this.facturaId > 0L)
			return this.facturaId.toString();
		return "";
	}
	
	public void setFacturaId(String value) {}
	
	public List<SelectItem> getListFormasPagoItems() {
		return listFormasPagoItems;
	}

	public void setListFormasPagoItems(List<SelectItem> listFormasPagoItems) {
		this.listFormasPagoItems = listFormasPagoItems;
	}
   	
   	public boolean isReqReferencia() {
		return reqReferencia;
	}

	public void setReqReferencia(boolean reqReferencia) {
		this.reqReferencia = reqReferencia;
	}

	public String getReferenciaOperacion() {
		return referenciaOperacion;
	}

	public void setReferenciaOperacion(String referenciaOperacion) {
		this.referenciaOperacion = referenciaOperacion;
	}

	public List<OrdenCompraExt> getListOrdenCompra() {
		return listOrdenCompra;
	}
	
	public void setListOrdenCompra(List<OrdenCompraExt> listOrdenCompra) {
		this.listOrdenCompra = listOrdenCompra;
	}

	public OrdenCompraExt getPojoOrdenCompra() {
		return pojoOrdenCompra;
	}

	public void setPojoOrdenCompra(OrdenCompraExt pojoOrdenCompra) {
		this.pojoOrdenCompra = pojoOrdenCompra;
	}

	public List<String> getOpcionesBusquedaOrdenCompra() {
		return opcionesBusquedaOrdenCompra;
	}

	public void setOpcionesBusquedaOrdenCompra(List<String> opcionesBusquedaOrdenCompra) {
		this.opcionesBusquedaOrdenCompra = opcionesBusquedaOrdenCompra;
	}

	public String getCampoBusquedaOrdenCompra() {
		return campoBusquedaOrdenCompra;
	}

	public void setCampoBusquedaOrdenCompra(String campoBusquedaOrdenCompra) {
		this.campoBusquedaOrdenCompra = campoBusquedaOrdenCompra;
	}

	public String getValorBusquedaOrdenCompra() {
		return valorBusquedaOrdenCompra;
	}

	public void setValorBusquedaOrdenCompra(String valorBusquedaOrdenCompra) {
		this.valorBusquedaOrdenCompra = valorBusquedaOrdenCompra;
	}

	public int getPaginacionOrdenCompra() {
		return paginacionOrdenCompra;
	}

	public void setPaginacionOrdenCompra(int paginacionOrdenCompra) {
		this.paginacionOrdenCompra = paginacionOrdenCompra;
	}

	public int getPaginacionObras() {
		return paginacionObras;
	}

	public void setPaginacionObras(int paginacionObras) {
		this.paginacionObras = paginacionObras;
	}

	public boolean getPerfilAdministrativo() {
		return perfilEgresos;
	}
	
	public void setPerfilAdministrativo(boolean perfilEgresos) {
		this.perfilEgresos = perfilEgresos;
	}

	public List<SelectItem> getListObrasTiposItems() {
		return listObrasTiposItems;
	}
	
	public void setListObrasTiposItems(List<SelectItem> listObrasTiposItems) {
		this.listObrasTiposItems = listObrasTiposItems;
	}
	
	public int getTipoObraBusquedaObra() {
		return tipoObraBusquedaObra;
	}
	
	public void setTipoObraBusquedaObra(int tipoObraBusquedaObra) {
		this.tipoObraBusquedaObra = tipoObraBusquedaObra;
	}
	
	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public List<SelectItem> getListSucursalesItems() {
		return listSucursalesItems;
	}

	public void setListSucursalesItems(List<SelectItem> listSucursalesItems) {
		this.listSucursalesItems = listSucursalesItems;
	}

	public List<SelectItem> getListCuentasItems() {
		return listCuentasItems;
	}

	public void setListCuentasItems(List<SelectItem> listCuentasItems) {
		this.listCuentasItems = listCuentasItems;
	}
}

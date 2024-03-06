package net.giro.cxp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.FacturaImpuestos;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PagosGastosHolder;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.TiposGastoCXP;
import net.giro.cxp.logica.GastosImpuestoRem;
import net.giro.cxp.logica.PagosGastosDetImpuestosRem;
import net.giro.cxp.logica.PagosGastosDetRem;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.Utilerias;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.CuentasBancariasRem;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class ProvisionesCXPAction implements Serializable {
	private static Logger log = Logger.getLogger(ProvisionesCXPAction.class);
	private static final long serialVersionUID = 1L;
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
   	private HttpSession httpSession;
	private InitialContext ctx;
	private PagosGastosRem ifzProvisiones; 
	private PagosGastosDetRem ifzProvisionDetalles;
	private PagosGastosDetImpuestosRem ifzDesgloImpto;
	private ConGrupoValoresRem ifzGpoVal;	
	private CuentasBancariasRem ifzCtas; 
	private GastosImpuestoRem ifzGastoImpuesto;
	private ConValoresRem ifzConValores;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	private PersonaRem ifzPersonas;
	private SucursalesRem ifzSucursal;
	private List<Persona> listTerceros; 
	private List<Sucursal> listSucursales;	
	private List<SelectItem> listSucursalesItems;
	private List<PagosGastosDetImpuestosExt> lisImpuestosEliminados;
	private List<PagosGastosDetImpuestosExt> listDesgloseImpuestos;
	private List<PagosGastosDetImpuestosExt> listDesgloseRet;
	private List<GastosImpuestoExt> listImpuestosDelGasto;
	private List<GastosImpuestoExt> listRetDelGasto;
	private List<Persona> listBeneficiarios;
	private List<CuentaBancaria> listCuentas;
	private List<SelectItem> listCuentasItems;
	private List<Persona> listProveedores;
	private List<PagosGastos> listProvisiones;
	private List<PagosGastosHolder> listProvisionesItems;
	private List<ConValores> listRetEncontradas;
	private List<FacturaImpuestos> listFacturaConImpuestos;
	private List<FacturaImpuestos> listFacturaConImpuestosEliminadas;
	private Long pojoProvisionMain;
	private PagosGastosExt pojoProvision;
	private PagosGastosDetExt pojoProvisionComprobado;
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
	private FacturaImpuestos reg; 
	private Double subtotal;
	private Double totImpuestos;
	private Double totRetenciones;
	private Double subtotalMasImpuestos;
	private Double totalFacturasReportadas;
	private Double totPago;
	private String terceros;
	private String operacion;
	private String descripcionFactura;
	private String conceptoGasto;
	private String conceptoGastoPrevio;
	private String tipoPersona;
	private String nombreProveedor;	
	private String nombreBeneficiario;
	private String beneficiario;		
	private String cuenta;	
	private String valBusqRet;
	private String campoBusqRet;
	private String perfilRequiereObra;
	private String sucursalesVisibles;
	private String [] listBusqRet;
	private long usuarioId;
	private String usuario;
	private boolean esTransferencia;
	private boolean existeRetencion;
	private boolean esCheque;
	private boolean esSpei;
   	private int numPagina;
   	private int numPaginaFact;
	private int tipoReporte;
	private boolean facturaConLlave;
	private String observaciones;
	private ConGrupoValores pojoGpoValFormasPago; 
	private List<ConValores> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	private boolean reqReferencia;
	private String referenciaOperacion;
	// Busqueda Principal
	private List<SelectItem> tiposBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
	// Busqueda Obras
	// ------------------------------------------------
	private ObraRem ifzObras;
	private Obra pojoObra;
	private List<Obra> listObras;
	private List<SelectItem> listObrasTiposItems;
	private int tipoObraBusquedaObra;
	private List<SelectItem> tipoBusquedaObra;
	private String valTipoBusquedaObra;
	private String campoBusquedaObra;
	private int tipoObra;
	private int paginacionObras;
	public boolean avanzadaObra;
	// ORDEN DE COMPRA
	// ------------------------------------------------
	private OrdenCompraRem ifzOrdenCompra;
	private List<OrdenCompra> listOrdenCompra;
	private OrdenCompra pojoOrdenCompra;
	private List<SelectItem> listTiposBusquedaOrdenCompra;
	private String campoBusquedaOrdenCompra;
	private String valorBusquedaOrdenCompra;
	public boolean busquedaAvanzadaOrdenCompra;
	private int paginacionOrdenCompra;
	// Carga XML
	private FtpRem ifzFtp;
	private byte[] fileSrc;
	private String fileName;
	private boolean fileLoaded;
	private Long facturaId;
	private String facturaRfc;
	private String facturaEmisor;
	private String facturaTipo;
	private String facturaMoneda;
	private Double facturaTipoCambio;
	private Double facturaDescuento;
	private Double facturaSubtotal;
	private Double facturaTotal;
	private boolean facturaDescartada;
	private boolean facturaActualizar;
	private String prefijoFacturas;
	private String ftpDigitalizacionHost;
	private String ftpDigitalizacionPort;
	private String ftpDigitalizacionUser;
	private String ftpDigitalizacionPass;
	private String ftpDigitalizacionRuta;
	// BUSQUEDA CONCEPTOS
	private List<ConValores> listConceptoGasto;
	private ConValores pojoConcepto;
	private List<SelectItem> tiposBusquedaConceptos;
	private String campoBusquedaConceptos;
	private String valorBusquedaConceptos;
	private int pagBusquedaConceptos;
	private boolean avanzadaConceptos;
	// EMPLEADO-USUARIO
	private EmpleadoRem ifzEmpleado;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalSugerida;
	// PERFILES
	private boolean perfilEgresos;
	// mensajes
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	private boolean perfilPermiteEditar;

	public ProvisionesCXPAction() {
		PropertyResourceBundle msgProperties = null;
		ValueExpression valExp = null;
		FacesContext facesContext = null;
		Application app = null;
		String valPerfil = "";
		
		try {
			facesContext = FacesContext.getCurrentInstance();
	        app = facesContext.getApplication();

	        this.httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
	        
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(facesContext.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();

			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			msgProperties = (PropertyResourceBundle) valExp.getValue(facesContext.getELContext());

			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(facesContext.getELContext());
			
			Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// EVALUACION DE PERFILES
			this.perfilRequiereObra = this.loginManager.getAutentificacion().getPerfil("REQUIERE_OBRA");
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);

			valPerfil = this.loginManager.getAutentificacion().getPerfil("PROVISIONES_EDITAR");
			this.perfilPermiteEditar = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);

	   		this.ctx = new InitialContext();
	   		this.ifzPersonas 		= (PersonaRem) 					this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
	   		this.ifzProvisiones 	= (PagosGastosRem) 				this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
	   		this.ifzDesgloImpto 	= (PagosGastosDetImpuestosRem) 	this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
	   		this.ifzGastoImpuesto 	= (GastosImpuestoRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
	   		this.ifzGpoVal 			= (ConGrupoValoresRem) 			this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
	   		this.ifzConValores 		= (ConValoresRem) 				this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
	   		this.ifzSucursal 		= (SucursalesRem) 				this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
	   		this.ifzCtas 			= (CuentasBancariasRem) 		this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
	   		this.ifzFtp 			= (FtpRem) 						this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzProvisionDetalles 	= (PagosGastosDetRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzLocalidad 		= (LocalidadDAO) 				this.ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			this.ifzColonia 		= (ColoniaDAO) 					this.ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");		
			this.ifzEmpleado 		= (EmpleadoRem) 				this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzObras 			= (ObraRem) 					this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzOrdenCompra 	= (OrdenCompraRem) 				this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProvisiones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGastoImpuesto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProvisionDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenCompra.setInfoSesion(this.loginManager.getInfoSesion());

			// VALORES SUGERIDOS
			comprobarUsuario();
			
			this.ftpDigitalizacionHost = this.entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = this.entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = this.entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = this.entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta");
			this.prefijoFacturas = "CXP-PF-"; 
	   		
			this.pojoGpoValPersonas = new ConGrupoValores();
			this.pojoProvision = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoBeneficiarios= new PersonaExt();
			this.pojoConceptoGtos = new ConValores();
			this.pojoProvisionComprobado = new PagosGastosDetExt();
			this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
			this.pojoConceptoGtos = new ConValores();
			this.pojoProveedores = new PersonaExt();
			this.pojoGpoValGasto = new ConGrupoValores();
			this.pojoTerceros = new PersonaExt();
			this.pojoGpoValTerceros = new ConGrupoValores();
			this.pojoNvaRet = new ConValores();	
			this.listProvisiones = new ArrayList<PagosGastos>();
			this.listTerceros = new ArrayList<Persona>();
			this.listSucursales = new ArrayList<Sucursal>();
			this.listCuentas = new ArrayList<CuentaBancaria>();
			this.listBeneficiarios = new ArrayList<Persona>();
			this.lisImpuestosEliminados = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>(); 
			this.listDesgloseRet= new ArrayList<PagosGastosDetImpuestosExt>();
			this.listRetEncontradas = new ArrayList<ConValores>();
			this.listImpuestosDelGasto = new ArrayList<GastosImpuestoExt>();
			this.listRetDelGasto = new ArrayList<GastosImpuestoExt>();		
			this.reg = new FacturaImpuestos();
			this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>(); 
			this.listSucursalesItems = new ArrayList<SelectItem>();
			
			this.numPagina = 1;
			this.numPaginaFact = 1;
			this.tipoMensaje = 0;
			this.cuenta = "";
			this.beneficiario = "";	
			this.tipoPersona = "P";
			this.operacion = "C";
			this.mensaje = "";
			this.esTransferencia = false;
			this.esCheque = true;
			this.esSpei = false;
			this.operacionCancelada = false;
			this.facturaConLlave = false;
			this.existeRetencion = false;
			this.totalFacturasReportadas = 0D;
			this.subtotalMasImpuestos=0D;
			this.totImpuestos=0D;
			this.totRetenciones=0D;
			this.subtotal=0D;
			this.totPago=0D;

			// Busqueda principal
			// ----------------------------------------------------------------------
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("beneficiario", "Beneficiario"));
			this.tiposBusqueda.add(new SelectItem("concepto", "Concepto"));
			this.tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.fechaBusqueda = Calendar.getInstance().getTime();
			this.numPagina = 1;
	   		
	   		// BUSQUEDA CONCEPTOS
			// ----------------------------------------------------------------------
	   		this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
	   		this.tiposBusquedaConceptos.add(new SelectItem("descripcion", "Descripcion"));
	   		this.tiposBusquedaConceptos.add(new SelectItem("id", "ID"));
	   		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
	   		this.valorBusquedaConceptos = "";
	   		this.pagBusquedaConceptos = 1;
	   		this.avanzadaConceptos = false;
	   		
			// Busqueda Obras
			// ----------------------------------------------------------------------
			this.tipoBusquedaObra = new ArrayList<SelectItem>();
			this.tipoBusquedaObra.add(new SelectItem("nombre", "Nombre"));
			this.tipoBusquedaObra.add(new SelectItem("nombreCliente", "Cliente"));
			this.tipoBusquedaObra.add(new SelectItem("nombreContrato", "Contrato"));
			this.tipoBusquedaObra.add(new SelectItem("id", "ID"));
			this.campoBusquedaObra = this.tipoBusquedaObra.get(0).getValue().toString();
	   		this.valTipoBusquedaObra = "";
	   		this.tipoObra = 0;
			this.paginacionObras = 1;
			// ----------------------------------------------------------------------
			this.listObrasTiposItems = new ArrayList<SelectItem>();
			this.listObrasTiposItems.add(new SelectItem(1, msgProperties.getString("obrasTipo.obra")));
			this.listObrasTiposItems.add(new SelectItem(2, msgProperties.getString("obrasTipo.proyecto")));
			this.listObrasTiposItems.add(new SelectItem(3, msgProperties.getString("obrasTipo.ordenTrabajo")));
			if (this.perfilEgresos)
				this.listObrasTiposItems.add(new SelectItem(4, msgProperties.getString("obrasTipo.administrativa")));
			
			// Busqueda Orden de Compra
			// ----------------------------------------------------------------------
			this.listTiposBusquedaOrdenCompra = new ArrayList<SelectItem>();
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("folio", "Folio"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("nombreProveedor", "Proveedor"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("nombreSolicita", "Solicita"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("nombreContacto", "Contacto"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("id", "ID"));
			this.campoBusquedaOrdenCompra = this.listTiposBusquedaOrdenCompra.get(0).getValue().toString();
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
			if (this.pojoGpoValPersonas == null || this.pojoGpoValPersonas.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");

			this.reqReferencia = false;
			cargarFormasDePago();
			cargarCuentas();
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.ProvisionesAction", e);
			this.ctx = null;
		}
  	}
   	
   	public void buscar() {
   		String orderBy = "";
   		
   		try {
   			control();
   			if (this.campoBusqueda == null || "".equals(this.campoBusqueda.trim()))
   				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();

			orderBy = "fecha desc, id desc";
   			if ("fecha".equals(this.campoBusqueda))
   				orderBy = "id desc";
   			
   			this.listProvisionesItems = new ArrayList<PagosGastosHolder>();
			this.ifzProvisiones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listProvisiones = this.ifzProvisiones.findProvisionLikeProperty(this.campoBusqueda, ("fecha".equals(this.campoBusqueda) ? this.fechaBusqueda : this.valorBusqueda), "", true, orderBy, 0);
			if (this.listProvisiones == null || this.listProvisiones.isEmpty()) {
				control(13, "Busqueda sin resultados");
				return;
			}

			this.listProvisionesItems = this.ifzProvisiones.encapsularPagosGastos(this.listProvisiones);
			Collections.sort(this.listProvisionesItems, new Comparator<PagosGastosHolder>() {
				@Override
				public int compare(PagosGastosHolder o1, PagosGastosHolder o2) {
					return o2.getMes().ordinal() - o1.getMes().ordinal();
				}
			});
		} catch (Exception e) {
			this.listProvisiones = new ArrayList<PagosGastos>();
			control("Ocurrio un problema al consultar los Registros de Gastos", e);
		}
   	}
   	
   	public void nuevo() {
		try {	
			control();
			this.numPaginaFact = 1;
			this.pojoProvisionMain = 0L;
			this.pojoProvision = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoBeneficiarios = new PersonaExt();
			this.pojoTerceros = new PersonaExt();

			if (this.listFacturaConImpuestosEliminadas == null)
				this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.listFacturaConImpuestosEliminadas.clear();	
			this.listFacturaConImpuestos.clear();			
			this.lisImpuestosEliminados.clear();
			this.listDesgloseImpuestos.clear(); 
			this.listImpuestosDelGasto.clear();
			this.listRetEncontradas.clear();
			this.listDesgloseRet.clear();
			this.totRetenciones = 0D;
			this.totPago = 0D;
			
			this.cuenta = "";
			this.beneficiario = "";
			if (this.pojoEmpleadoUsuario != null && this.pojoEmpleadoUsuario.getId() != null && this.pojoEmpleadoUsuario.getId() > 0L)
				this.beneficiario = this.pojoEmpleadoUsuario.getPersona().getId() + " - " + this.pojoEmpleadoUsuario.getNombre();
			this.tipoPersona = "P";
			this.operacion = ""; 
			this.esTransferencia = false;
			this.esSpei = false;
			this.esCheque = true;
			this.existeRetencion = false;
			this.totalFacturasReportadas = 0D;
			this.observaciones = "";
			this.referenciaOperacion = "";

			cargarSucursales();
			setSucursal(this.idSucursalSugerida);
			//agregar();
		} catch (Exception e) {
			control("Ocurrio un poblema al incializar una nueva Provision", e);
		}
	}

   	public void editar() {
   		List<PagosGastosDetImpuestosExt> listImpuestos = null;
   		List<PagosGastosDetExt> listDetalles = null;
   		long idSucursal = 0;
   		
   		try {
   			control();
			this.numPaginaFact = 1;
   			log.info("RegistroGasto ... Preparando para editar");
   			this.pojoProvision = this.ifzProvisiones.findExtById(this.pojoProvisionMain);
   			if (this.pojoProvision == null) {
   	   			control(-1, "No selecciono ningun registro");
   				return;
   			}

   			idSucursal = this.pojoProvision.getIdSucursal().getId();
			cargarSucursales();
			setSucursal(idSucursal);
			
   			this.operacion = this.pojoProvision.getOperacion();
   			if ("C".equals(this.operacion)) {
   				this.referenciaOperacion = this.pojoProvision.getNoCheque().toString();
			} else if ("T".equals(this.operacion)) {
				this.referenciaOperacion = this.pojoProvision.getFolioAutorizacion();
			} else {
				this.referenciaOperacion = "";
			}
   			
   			this.cuenta = "";
			if (this.pojoProvision != null && this.pojoProvision.getIdCuentaOrigen() != null && this.pojoProvision.getIdCuentaOrigen().getId() > 0L) {
				this.cuenta = this.pojoProvision.getIdCuentaOrigen().getId() + " - " 
						+ this.pojoProvision.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto() + " - " 
						+ this.pojoProvision.getIdCuentaOrigen().getNumeroDeCuenta();
				this.pojoCtas = this.pojoProvision.getIdCuentaOrigen();
				log.info("RegistroGasto... Cuenta Origen recuperada");
			}
   			//this.cuenta = this.pojoProvision.getIdCuentaOrigen().getId() + " - " + this.pojoProvision.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto() + " - " + this.pojoProvision.getIdCuentaOrigen().getNumeroDeCuenta();
   			this.beneficiario = this.pojoProvision.getIdBeneficiario().getId() + " - " + this.pojoProvision.getIdBeneficiario().getNombre();
   			this.tipoPersona = this.pojoProvision.getTipoBeneficiario();
   			
   			if (this.listFacturaConImpuestos == null)
   				this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>();
   			this.listFacturaConImpuestos.clear();
			
			if (this.listFacturaConImpuestosEliminadas == null)
				this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.listFacturaConImpuestosEliminadas.clear();
   			
   			// recuperamos detalle de Registro de Gastos
			log.info("RegistroGasto ... Recuperando detalles");
   			listDetalles = this.ifzProvisionDetalles.findExtAll(this.pojoProvision.getId());
   			if (listDetalles != null && ! listDetalles.isEmpty()) {
   	   			for (PagosGastosDetExt var : listDetalles) {
   	   				this.reg = new FacturaImpuestos();
   	   				this.reg.setPojoFactura(var);

					try {
						listImpuestos = this.ifzDesgloImpto.findExtAll(var.getId());
						if (listImpuestos == null || listImpuestos.isEmpty()) {
							editarFactura();
							listImpuestos = copiaListas(this.listDesgloseImpuestos);
						}
						
						this.reg.setListImpuestos(listImpuestos); //.findLikePojoCompletoExt(var, 100));
					} catch (Exception e) {
						log.error("Error al obtener la lista de impuestos de la factura " + var.getReferencia() + " (" + var.getId() + ")", e);
					}
					
   	   	   			this.listFacturaConImpuestos.add(this.reg);
   	   			}
   			}

			log.info("RegistroGasto ... " + this.listFacturaConImpuestos.size() + " detalles recuperados");
			this.reg = new FacturaImpuestos();
			actualizaTotalFaturas();
			log.info("RegistroGasto... Lista para editar");
   		} catch (Exception e) {
   			control("Ocurrio un problema al intentar editar la Provision seleccionada", e);
   		}
   	}
   	
	public void guardar() {
		Respuesta resp = new Respuesta();
		Pattern pat = null;
		Matcher match = null;
		boolean  registroNuevo = false;
		boolean borrarXML = false;
		String fileName = "";
		
		try {
			control();
			if (this.pojoProvision.getId() == null)
				registroNuevo = true;
			
			this.pojoProvision.setIdObra(new Obra());
			this.pojoProvision.setIdOrdenCompra(0L);
			this.pojoProvision.setModificadoPor(this.usuarioId);
			this.pojoProvision.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoProvision.setTipoBeneficiario(this.tipoPersona);
			this.pojoProvision.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			
			/*if (this.pojoObra != null) {
				if (this.pojoProvision.getIdObra() == null) {
					this.pojoProvision.setIdObra(this.pojoObra);
				} else {
					if (this.pojoProvision.getIdObra().getId() != null && this.pojoProvision.getIdObra().getId() != this.pojoObra.getId())
						this.pojoProvision.setIdObra(this.pojoObra);
				}
			}*/
			
			if (this.listFacturaConImpuestos == null || this.listFacturaConImpuestos.isEmpty()) {
				control(2, "Debe indicar al menos una factura");
				return;
			}
			
			//validando Cuenta
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.cuenta);
			if (match.find()) {
				if (registroNuevo) {
					this.pojoCtas = this.ifzProvisiones.findCuentaBancariaById(Long.valueOf(match.group(1))); 
				} else {
					if (this.pojoCtas.getId() != Long.valueOf(match.group(1)))
						this.pojoCtas= this.ifzProvisiones.findCuentaBancariaById(Long.valueOf(match.group(1))); 
				}
			}

			// Asigno cuenta
			this.pojoProvision.setIdCuentaOrigen(this.pojoCtas);

			if (! validaSaldoCuentaPropios()) {
				//this.operacion = this.operacionValidate(this.operacion);
				return;
			}
			
			//validando operacion para mostrar catalodo de Terceros
			if ("T".equals(this.operacion) || "S".equals(this.operacion)) {
				match = pat.matcher(this.beneficiario);			
				if (match.find())
					if (registroNuevo)				
						this.pojoTerceros = this.ifzProvisiones.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
					else
						if (this.pojoTerceros.getId() != Long.valueOf(match.group(1)))
							this.pojoTerceros = this.ifzProvisiones.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				this.pojoProvision.setIdCuentaDestinoTerceros(this.pojoTerceros);	
				this.pojoProvision.setBeneficiario(this.pojoTerceros.getNombre()+" "+(this.pojoTerceros.getPrimerApellido() != null ? this.pojoTerceros.getPrimerApellido():"")+" "+(this.pojoTerceros.getSegundoApellido() != null ? this.pojoTerceros.getSegundoApellido():""));
			}

			//validando beneficiario
			match = pat.matcher(this.beneficiario);
			if (match.find()) {
				if (registroNuevo) {
					this.pojoBeneficiarios = this.ifzProvisiones.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				} else {
					if (this.pojoBeneficiarios.getId() != Long.valueOf(match.group(1)))
						this.pojoBeneficiarios = this.ifzProvisiones.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				}
			}
			
			// Asigno beneficiario
			this.pojoProvision.setIdBeneficiario(this.pojoBeneficiarios);
			if ("N".equals(this.tipoPersona)) {
				this.nombreBeneficiario = this.pojoBeneficiarios.getNombre();
			} else {
				this.nombreBeneficiario = this.pojoBeneficiarios.getPrimerNombre() + " " 
						+ ((this.pojoBeneficiarios.getSegundoNombre()   != null && ! "".equals(this.pojoBeneficiarios.getSegundoNombre().trim()))   ? this.pojoBeneficiarios.getSegundoNombre().trim()   : "") + " " 
						+ ((this.pojoBeneficiarios.getPrimerApellido()  != null && ! "".equals(this.pojoBeneficiarios.getPrimerApellido().trim()))  ? this.pojoBeneficiarios.getPrimerApellido().trim()  : "") + " " 
						+ ((this.pojoBeneficiarios.getSegundoApellido() != null && ! "".equals(this.pojoBeneficiarios.getSegundoApellido().trim())) ? this.pojoBeneficiarios.getSegundoApellido().trim() : "");
			}
			
			this.pojoProvision.setBeneficiario(this.nombreBeneficiario);
			this.pojoProvision.setOperacion(this.operacion);
			this.pojoProvision.setMonto(this.totalFacturasReportadas);
			
			// Asignacion de operacion
			if ("C".equals(this.operacion)) {
				this.pojoProvision.setNoCheque(Integer.parseInt(this.referenciaOperacion));
			} else if ("T".equals(this.operacion)) {
				this.pojoProvision.setFolioAutorizacion(this.referenciaOperacion);
			}

			this.ifzProvisiones.setInfoSesion(this.loginManager.getInfoSesion());
			if (registroNuevo) {
				this.pojoProvision.setCreadoPor(this.usuarioId);
				this.pojoProvision.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoProvision.setTipo("F");//provision de facturas
				this.pojoProvision.setEstatus("C");//comprobado

				this.ifzProvisiones.setInfoSesion(this.loginManager.getInfoSesion());
				resp = this.ifzProvisiones.salvar(this.pojoProvision);				
				if (resp.getResultado() != -1) {
					this.pojoProvision.setId(Long.valueOf(resp.getReferencia()));
				} else {
					if (! "BIEN".equals(resp.getRespuesta())) {
						control(-1, resp.getRespuesta());
						if ((resp.getRespuesta().indexOf("SE CANCELA EL FOLIO ") > -1) || (resp.getRespuesta().indexOf("SE CANCELAN LOS FOLIOS ") > -1))
							control(9, resp.getRespuesta());
						else
							control(8, resp.getRespuesta());
						return;   							
					}
				}
				
				this.listProvisiones.add(this.ifzProvisiones.convertir(this.pojoProvision));
			} else {
				// Actualiza Registro de Gastos
				this.ifzProvisiones.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzProvisiones.update(this.pojoProvision);

				// Actualizo en la LISTA
				for (PagosGastos var : this.listProvisiones) {
					if (this.pojoProvision.getId().longValue() == var.getId().longValue()) {
						this.listProvisiones.set(this.listProvisiones.indexOf(var), this.ifzProvisiones.findById(this.pojoProvision.getId()));
						break;
					}
				}
			}
				
			//grabando las facturas en caso de que hayan agregado algunas
			if (! this.listFacturaConImpuestos.isEmpty()) {
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					var.getPojoFactura().setIdPagosGastos(this.pojoProvision);
					if (var.getPojoFactura().getId() != null && var.getPojoFactura().getId() > 0L) 
						this.ifzProvisionDetalles.update(var.getPojoFactura());
					else 
						var.getPojoFactura().setId(this.ifzProvisionDetalles.save(var.getPojoFactura()));
					// Impuestos
					if (! var.getListImpuestos().isEmpty()) {
						for (PagosGastosDetImpuestosExt var2 : var.getListImpuestos()) {
							var2.setIdPagosGastosDet(var.getPojoFactura());
							if (var2.getId() == null || var2.getId() <= 0L)
								var2.setId(this.ifzDesgloImpto.save(var2));
							else
								this.ifzDesgloImpto.update(var2);
						}
					}
					// Retenciones
					if (! var.getListRetenciones().isEmpty()) {
						for (PagosGastosDetImpuestosExt var2 : var.getListRetenciones()) {
							var2.setIdPagosGastosDet(var.getPojoFactura());
							if (var2.getId() == null || var2.getId() <= 0L)
								var2.setId(this.ifzDesgloImpto.save(var2));
							else
								this.ifzDesgloImpto.update(var2);
						}
					}
				}
			}

			// Borramos facturas previamente eliminadas
			if (this.listFacturaConImpuestosEliminadas != null && ! this.listFacturaConImpuestosEliminadas.isEmpty()) {
				getFTPData();
				
				for (FacturaImpuestos var : this.listFacturaConImpuestosEliminadas) {
					// borramos XML (documento cargado) si corresponde
					if (var.getPojoFactura().getIdCfdi() != null && var.getPojoFactura().getIdCfdi() > 0L) {
						borrarXML = true;
						this.facturaId = var.getPojoFactura().getIdCfdi();
						for (FacturaImpuestos item : this.listFacturaConImpuestos) {
							if (item.getPojoFactura().getIdCfdi() != null && item.getPojoFactura().getIdCfdi() > 0L && item.getPojoFactura().getIdCfdi().longValue() == this.facturaId.longValue()) {
								borrarXML = false;
								break;
							}
						}
						
						if (borrarXML) {
							// Lo elimino de la BD
							log.info("Eliminando XML " + this.facturaId);
				    		this.ifzProvisionDetalles.eliminarFactura(this.facturaId);
							
							// Lo elimino fisicamente del servidor
							fileName = this.prefijoFacturas + var.getPojoFactura().getIdCfdi() + ".xml";
							this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
							if (! this.ifzFtp.delArchivo(this.ftpDigitalizacionRuta + fileName))
								log.warn("ERROR FTP - No se pudo Borrar la factura del servidor, ID: " + this.facturaId);
						}
						
						// Borro detalle (factura)
						this.ifzProvisionDetalles.delete(var.getPojoFactura().getId());
						this.facturaId = 0L;
					} else {
						// Borro detalle (factura)
						this.ifzProvisionDetalles.delete(var.getPojoFactura().getId());
					}
				}
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			mensajeTransaccion();
		} catch (Exception e) {
			control("Ocurrio un problma al intentar Guardar la Provision", e);
		}
	}

   	public void cancelarRegistroGasto() {
   		Respuesta rep = new Respuesta();
   		
		try {
			control();
   			this.pojoProvision = this.ifzProvisiones.findExtById(this.pojoProvisionMain);
			rep = this.ifzProvisiones.cancelacion(this.pojoProvision, Calendar.getInstance().getTime());
			if (rep.getResultado() != 0) {
				control(-1, rep.getRespuesta());
				return;
			}
			
			for (PagosGastos var : this.listProvisiones) {
				if (var.getId().longValue() == this.pojoProvision.getId().longValue()) {
					var.setEstatus("X");
					break;
				}
			}
			
			/*if (rep.getResultado() == 0) {
				this.pojoProvision = (PagosGastosExt) rep.getObjeto();
				this.mensaje = "";
				
				for (PagosGastos var : this.listRegistroGastos) {
					if (var.getId().equals(this.pojoProvision.getId())) {
						var.setEstatus("X");
						break;
					}						
				}*/
				
				/*if (this.perfilRequiereObra == null && !"S".equals(this.perfilRequiereObra)) {
					List<PagosGastosDet> detalles = this.ifzProvisionDetalles.findByProperty("pagosGastosId", this.pojoProvision);
					List<ProgPagosDetalle> lista = null;
					if (detalles != null && ! detalles.isEmpty()) {
						for (PagosGastosDet var: detalles) {
							Double montoValue = ((var.getSubtotal() + var.getTotalImpuestos())- var.getTotalRetenciones());
							Long conceptoValue = var.getIdConcepto(); //var.getConceptoId().getValorId();
							lista = this.ifzProPagDet.findByMontoConceptoEstatus(montoValue, conceptoValue, "E");
							if (lista != null) {
								lista.get(0).setEstatus("G");
								lista.get(0).setModificadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
								lista.get(0).setFechaModificacion(Calendar.getInstance().getTime());
								this.ifzProPagDet.update(lista.get(0));
							}
						}
					}
				}*/
			/*} else
				this.mensaje = rep.getRespuesta();*/
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cancelar la Provision.", e);
		}
	}
   	
	public void eliminar() {
		try {
			control();
			if (this.listFacturaConImpuestosEliminadas == null)
				this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.listFacturaConImpuestosEliminadas.add(this.reg);
			this.listFacturaConImpuestos.remove(this.reg);
			actualizaTotalFaturas();					
		} catch (Exception e) { 
			control("Ocurrio un problema al intentar Eliminar la Provision seleccionda.", e);
		}
	}

	public void reporte() {
   		try {
   			control();
			/*listaParam = new HashMap<String, String>();
			if (this.tipoReporte == 1) {
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
			control("Ocurrio un problema al ejecutar el reporte de Provision", e);
		}
	}

   	public void checaCancelado() {
   		control();
   		if (this.pojoProvision.getEstatus().equals("X"))
   			control(14, "El cheque ya ha sido cancelado");
   	}
   	
   	public void cargarSucursales() {
   		try {
			if (this.listSucursales == null)
				this.listSucursales = new ArrayList<Sucursal>();
			this.listSucursales.clear();
			
			if (this.listSucursalesItems == null)
				this.listSucursalesItems = new ArrayList<SelectItem>();
			this.listSucursalesItems.clear();
			
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSucursales = this.ifzSucursal.findAll(); // this.ifzSucursal.findLikePropiedad("sucursal", ""); //this.listSucursales = this.ifzSucursal.buscarSucursales("sucursal", "");
			if (this.listSucursales != null && ! this.listSucursales.isEmpty()) {
				for (Sucursal var : this.listSucursales)
					this.listSucursalesItems.add(new SelectItem(var.getId(), var.getSucursal()));
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

			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listFormasPago = this.ifzConValores.findAll(this.pojoGpoValFormasPago);
			if (this.listFormasPago != null && ! this.listFormasPago.isEmpty()) {
				for (ConValores fp : this.listFormasPago)
					this.listFormasPagoItems.add(new SelectItem(fp.getValor(), fp.getDescripcion()));
			}
		} catch (Exception e) {
			log.error("error en cargarFormasDePago", e);
		}
	}

   	public void cargarCuentas() {
   		try {
			if (this.listCuentas == null)
				this.listCuentas = new ArrayList<CuentaBancaria>();
			this.listCuentas.clear();
			
			if (this.listCuentasItems == null)
				this.listCuentasItems = new ArrayList<SelectItem>();
			this.listCuentasItems.clear();

			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCuentas = this.ifzCtas.findLikeClaveNombreCuenta("", 0, this.sucursalesVisibles, null);
			if (this.listCuentas != null && ! this.listCuentas.isEmpty()) {
				for (CuentaBancaria var : this.listCuentas)
					this.listCuentasItems.add(new SelectItem(var.getId() + " - " + var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta(), var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta()));
			}
		} catch (Exception e) {
			log.error("error en cargarCuentas", e);
		}
   	}
   	
	public void evaluaOperacion() {
		try {
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
			this.mensaje = "";
			this.terceros = "";
			this.pojoProvision.setNoCheque(0);
			this.pojoProvision.setFolioAutorizacion("");
			this.beneficiario = "";
		} catch (Exception e) {
			control("Ocurrio un problema con esta operacion", e);
		}
	}
	
	private List<PagosGastosDetImpuestosExt> copiaListas(List<PagosGastosDetImpuestosExt> fuente) {
   		List<PagosGastosDetImpuestosExt> resultado = new ArrayList<PagosGastosDetImpuestosExt>();
   		for (PagosGastosDetImpuestosExt pgi : fuente) {
   			resultado.add( ((PagosGastosDetImpuestosExt) pgi.clone()));
   		}
   		return resultado;
   	}
   	
   	public void buscarRet() {
   		try {
   			control();
   			this.listRetEncontradas = ifzConValores.findLikeByProperty(this.campoBusqRet,this.valBusqRet, 5);
   			if (this.listRetEncontradas == null || this.listRetEncontradas.isEmpty()) {
   				control(13, "Busqueda sin resultados");
   				return;
   			}
   		} catch (Exception e) {
   			control("Ocurrio un problema al consultar las Retenciones", e);
   		}
   	}
   	
	public void mensajeTransaccion() {
		try {
			control();
			log.info("Enviando Transaccion 1011 ... ");
			this.ifzProvisiones.contabilizador(this.pojoProvisionMain);
			log.info("Transaccion enviada");
		} catch (Exception e) {
			control("Ocurrio un problema al enviar evento Contable (1011)", e);
		}
	}
	
   	public String cambioBeneficiario() {
		try {
			this.beneficiario = "";
		} catch (Exception e) {			
			log.error("error en cambioBeneficiario", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public boolean validaSaldoCuentaPropios() {
		try {
			control();
			if ((this.listFacturaConImpuestos == null || this.listFacturaConImpuestos.isEmpty()) && this.totalFacturasReportadas == 0) {
				control(12, "Total no valido");
				return false;
			}
			
			/* 
			 * VALIDACION TEMPORALMENTE DESACTIVADA: ASI LO PIDIO EL JEFE
			if (this.pojoCtas.getInstitucionBancaria().getId() != 0L) {
				if (! ifzCtas.haySaldoSuficiente(totalFacturasReportadas, this.pojoCtas.getId())) {
					control(true, 15, this.mensajesInf.get(15));
					return;
				}
			}*/
		} catch (Exception e) {
			control("Ocurrio un problema al validar el Saldo", e);
			return false;
		}
		
		return true;
	}

	private String getGeneraNombreProveedor(PersonaExt prov) {
		if (prov == null)
			return "";
		
		return prov.getId() + " - " + prov.getNombre() + " " 
			+ (prov.getPrimerApellido() != null ? prov.getPrimerApellido() : "") + " " 
			+ (prov.getSegundoApellido() != null ? prov.getSegundoApellido() : "");
	}

	// --------------------------------------------------------------------------
	// Factura (Comprobacion)
	// --------------------------------------------------------------------------
	
	public void agregar() {
		PersonaExt proveedor = null;
		Matcher match = null;
		Pattern pat = null;
		
		try {
			control();
			if (this.pojoProvision.getId() != null && this.pojoProvision.getId() > 0L) {
				if (! this.perfilPermiteEditar && ! validaRequest("DETEDIT", "1")) {
					log.info("ERROR 10: No se permite añadir facturas.");
					control(10, "No se permite añadir facturas");
					return;
				}
			}

			reiniciaFactura();
			this.reg = new FacturaImpuestos();
			this.pojoProvisionComprobado = new PagosGastosDetExt();
			this.conceptoGasto = "";
			this.nombreProveedor = "";
			this.descripcionFactura = "";
			this.observaciones = "";
			this.listDesgloseImpuestos.clear();
			this.listDesgloseRet.clear();
			this.listRetEncontradas.clear();
			this.listRetDelGasto.clear();
			this.subtotalMasImpuestos = 0D;
			desglosaImpuestos();
			
			proveedor = new PersonaExt();
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.beneficiario);
			if (match.find()) {
				proveedor = this.ifzProvisiones.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona);
				this.pojoProvisionComprobado.setIdProveedor(proveedor);
				this.pojoProvisionComprobado.setTipoPersonaProveedor(this.tipoPersona);
			}

			obrasBusqueda();
			ordenCompraBusqueda();
			control();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar agregar una nueva Comprobacion", e);
		}
	}

	public void editarFactura() {
		String facturaFolio = "";

		try {
			control();
			if (this.listDesgloseImpuestos == null)
				this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseImpuestos.clear();
			
			if (this.listDesgloseRet == null)
				this.listDesgloseRet = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseRet.clear();
			
			if (this.reg.getPojoFactura().getIdCfdi() == null || this.reg.getPojoFactura().getIdCfdi() <= 0L)
				this.reg.getPojoFactura().setIdCfdi(0L);

			this.pojoProvisionComprobado = this.reg.getPojoFactura();
			if (this.pojoProvisionComprobado == null) {
				control(-1, "Ocurrio un problema al obtener la Factura de la Comprobacion seleccionada");
				return;
			}

			this.facturaConLlave = (this.pojoProvisionComprobado.getFacturado() == 0);
			this.facturaId = this.pojoProvisionComprobado.getIdCfdi();
			facturaFolio = this.pojoProvisionComprobado.getFacturaFolio();
			this.facturaRfc = this.pojoProvisionComprobado.getFacturaRfc();
			this.facturaEmisor = this.pojoProvisionComprobado.getNombreProveedor();
			this.facturaSubtotal = this.pojoProvisionComprobado.getSubtotal();
			this.totImpuestos = this.pojoProvisionComprobado.getTotalImpuestos();
			this.totRetenciones = this.pojoProvisionComprobado.getTotalRetenciones();
			this.facturaTotal = this.pojoProvisionComprobado.getTotal();
			this.facturaMoneda = "MXN";
			this.facturaTipoCambio = 1.0;
			if (this.facturaId.longValue() > 0L) {
				facturaFolio = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "factura");
				this.facturaTipo = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "tipo");
				this.facturaRfc = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "rfcEmisor");
				this.facturaEmisor = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "nombre");
				this.facturaMoneda = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "moneda");
				this.facturaTipoCambio = this.ifzProvisionDetalles.getFacturaTipoCambio(this.facturaId);
				this.facturaDescuento = this.ifzProvisionDetalles.getFacturaDescuento(this.facturaId);
				//this.facturaSubtotal = this.ifzProvisionDetalles.getFacturaSubtotal(this.facturaId);
				this.facturaTotal = this.ifzProvisionDetalles.getFacturaTotal(this.facturaId);
			}

			this.facturaActualizar = false;
			if ((this.facturaEmisor == null || "".equals(this.facturaEmisor.trim())) || (facturaFolio == null || "".equals(facturaFolio.trim())))
				this.facturaActualizar = true;

			this.conceptoGasto = "";
			if (this.pojoProvisionComprobado.getIdConcepto() != null && this.pojoProvisionComprobado.getIdConcepto().getId() > 0L) {
				this.pojoConceptoGtos = this.pojoProvisionComprobado.getIdConcepto();
				this.conceptoGasto = this.pojoConceptoGtos.getId() + " - " + this.pojoConceptoGtos.getDescripcion();
			}

			if (this.facturaEmisor != null && ! "".equals(this.facturaEmisor.trim())) 
				this.nombreProveedor = this.facturaEmisor;
			else
				this.nombreProveedor = getGeneraNombreProveedor(this.pojoProvisionComprobado.getIdProveedor());
			this.descripcionFactura = this.pojoProvisionComprobado.getIdConcepto().getDescripcion() + " - " + this.nombreProveedor;

			if (this.reg.getListImpuestos() != null && ! this.reg.getListImpuestos().isEmpty()) {
				this.listDesgloseImpuestos = copiaListas(this.reg.getListImpuestos());
				this.totImpuestos = this.reg.getTotalImpuestos();
			}
			
			if (this.reg.getListRetenciones() != null && ! this.reg.getListRetenciones().isEmpty()) {
				this.listDesgloseRet = copiaListas(this.reg.getListRetenciones());
				this.totRetenciones = this.reg.getTotalRetenciones();
			}

			this.subtotalMasImpuestos = this.facturaSubtotal + (this.totImpuestos - this.totRetenciones);
			this.subtotalMasImpuestos = Utilerias.redondear(this.subtotalMasImpuestos, 2);
			this.conceptoGastoPrevio = this.conceptoGasto;

			if (this.facturaTipo != null && "E".equals(this.facturaTipo)) {
				this.subtotal *= -1;
				this.totImpuestos *= -1;
				this.totRetenciones *= -1;
			}
			
			
			/*this.facturaId = this.reg.getPojoFactura().getIdXml();
			if (this.facturaId.longValue() > 0L) {
				facturaFolio = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "factura");
				this.facturaTipo = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "tipo");
				this.facturaRfc = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "rfcEmisor");
				this.facturaEmisor = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "nombre");
				this.facturaMoneda = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "moneda");
				this.facturaTipoCambio = this.ifzProvisionDetalles.getFacturaTipoCambio(this.facturaId);
				//this.facturaSubtotal = this.ifzProvisionDetalles.getFacturaSubtotal(this.facturaId);
				this.facturaTotal = this.ifzProvisionDetalles.getFacturaTotal(this.facturaId);
			}
	
			this.facturaActualizar = false;
			if ((this.facturaEmisor == null || "".equals(this.facturaEmisor.trim())) || (facturaFolio == null || "".equals(facturaFolio.trim())))
				this.facturaActualizar = true;
			
			this.pojoProvisionComprobado = this.reg.getPojoFactura();
			if (this.pojoProvisionComprobado.getIdConcepto() != null && this.pojoProvisionComprobado.getIdConcepto().getId() > 0L) {
				this.pojoConceptoGtos = this.pojoProvisionComprobado.getIdConcepto();
				this.conceptoGasto = this.pojoConceptoGtos.getId() + " - " + this.pojoConceptoGtos.getDescripcion();
			}
			//this.conceptoGasto2 = this.conceptoGasto;
			
			if ("".equals("".equals(this.facturaEmisor.trim()))) 
				this.nombreProveedor = this.facturaEmisor;
			else
				this.nombreProveedor = getGeneraNombreProveedor(this.pojoProvisionComprobado.getIdProveedor());
			this.descripcionFactura = this.pojoProvisionComprobado.getIdConcepto().getDescripcion() + " - " + this.nombreProveedor;
			
			this.subtotalMasImpuestos = this.pojoProvisionComprobado.getSubtotal() + this.pojoProvisionComprobado.getTotalImpuestos();
			this.totalMenosImpuestos = this.pojoProvisionComprobado.getSubtotal();
			this.totImpuestos = this.pojoProvisionComprobado.getTotalImpuestos();
			this.totRetenciones = this.pojoProvisionComprobado.getTotalRetenciones();
			
			if (this.facturaTipo != null && "E".equals(this.facturaTipo)) {
				this.totalMenosImpuestos *= -1;
				this.totImpuestos *= -1;
				this.totRetenciones *= -1;
			}

			this.facturaSubtotal = this.totalMenosImpuestos;
			this.totPago = Utilerias.redondear((this.totalMenosImpuestos + (this.totImpuestos - this.totRetenciones)), 2);
			this.observaciones = this.pojoProvisionComprobado.getObservaciones();
			this.listDesgloseImpuestos = copiaListas(this.reg.getListImpuestos());*/
		} catch (Exception e) {
			control("Ocurrio un problema al desglozar la Comprobacion seleccionada", e);
		} 
	}
	
	public void guardarFactura() {
		boolean encontro = false;			
		boolean  registroNuevo = false;  
		Pattern pat = null;
		Matcher match = null;
		Long concepto = 0L; 
		String[] splitted = null;
		String value = "";
		
		try {
			control();
			if (! validacionesFactura())
				return;
			
			log.info("Guardando Factura");
			this.pojoProvisionComprobado.setFacturado((this.facturaConLlave ? 0 : 1)); 
			this.pojoProvisionComprobado.setIdCfdi(this.facturaId);
			this.pojoProvisionComprobado.setNombreProveedor(this.facturaEmisor);
			this.pojoProvisionComprobado.setFacturaRfc(this.facturaRfc);
			this.pojoProvisionComprobado.setModificadoPor(this.usuarioId);
			this.pojoProvisionComprobado.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoProvisionComprobado.getFacturado() == 1 && (this.facturaId == null || this.facturaId <= 0L))
				this.pojoProvisionComprobado.setFacturado(0);

			if (this.pojoProvisionComprobado.getTipoPersonaProveedor() == null || "".equals(this.pojoProvisionComprobado.getTipoPersonaProveedor().trim()))
				this.pojoProvisionComprobado.setTipoPersonaProveedor(this.pojoProvisionComprobado.getIdProveedor().getTipoPersona() == 2L ? "N" : "P");
			
			if (this.facturaActualizar) {
				this.facturaActualizar = false;
				this.ifzProvisionDetalles.setFacturaProperty(this.facturaId, "nombre", this.facturaEmisor);
				
				if (this.pojoProvisionComprobado.getReferencia().contains("-")) {
					splitted = this.pojoProvisionComprobado.getReferencia().split("-");

					if (splitted.length > 2) {
						for (int i = 0; i <= (splitted.length - 2); i++)
							value += splitted[i] + "-";
						this.ifzProvisionDetalles.setFacturaProperty(this.facturaId, "serie", value);
						
						value = splitted[splitted.length - 1];
						this.ifzProvisionDetalles.setFacturaProperty(this.facturaId, "folio", value);
					} else {
						value = splitted[0];
						this.ifzProvisionDetalles.setFacturaProperty(this.facturaId, "serie", value);
						
						value = splitted[1];
						this.ifzProvisionDetalles.setFacturaProperty(this.facturaId, "folio", value);
					}
				} else {
					this.ifzProvisionDetalles.setFacturaProperty(this.facturaId, "serie", this.pojoProvisionComprobado.getReferencia());
				}
			}
			
			// validando sucursal
			/*log.info("Validando sucursal");
			pat = Pattern.compile("(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.sucursal);
			match.find();
			Long agente = Long.valueOf(match.group(1));*/
			
			/*if (this.perfilRequiereObra == null && !"S".equals(this.perfilRequiereObra)) {
   				this.listProPagDet = ifzProPagDet.findByAgenteEstatusMontoConcepto(agente.toString(), "G", concepto);
   				if (this.listProPagDet == null) {
   					log.error(mensajesInf.get(19));
   					this.resOperacion = this.mensajesInf.get(19);
   					return "ERROR";
   				}
			}*/
			
			/*for (Object obj: this.listProPagDet) {
				ProgPagosDetalle ppd = (ProgPagosDetalle)((Object[])obj)[1];
				if (! "R".equals((String)((Object[])obj)[0]) && totPago.compareTo(ppd.getMonto().doubleValue()) != 0) {
					ban = 3;
				}
				if ("R".equals((String)((Object[])obj)[0]) && totPago.compareTo(ppd.getMonto().doubleValue()) != 0) {
					ban = 2;
				}
				if (! "R".equals((String)((Object[])obj)[0]) && totPago.compareTo(ppd.getMonto().doubleValue()) == 0) {
					ban = 1;
				}   						
				if ("R".equals((String)((Object[])obj)[0]) && totPago.compareTo(ppd.getMonto().doubleValue()) == 0) {
					ban = 0;
					break;
				}
			}
			
			if (ban == 1) {
				control(21, "El Gasto que quiere registrar, no esta revisado");
				return;
			}
			
			if (ban == 2) {
				control(22, "No existe gasto revisado con ese monto");
				return;
			}
			
			if (ban == 3) {
				control(19, "El Gasto que quiere registrar, no esta programado");
				return;
			}*/
			
			if (this.facturaId == null)
				analizarArchivo();
			
			control();
			if (this.pojoProvisionComprobado.getId() == null)
				registroNuevo = true;
			
			this.pojoProvisionComprobado.setIdCfdi(this.facturaId);
			if (this.pojoProvisionComprobado.getIdObra() == null)
				this.pojoProvisionComprobado.setIdObra(new Obra());
			if (this.pojoProvisionComprobado.getIdOrdenCompra() == null)
				this.pojoProvisionComprobado.setIdOrdenCompra(new OrdenCompra());

			//validando gasto
			log.info("Validando gasto");
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.conceptoGasto);			
			match.find();
			concepto = Long.valueOf(match.group(1));
			
			if (registroNuevo) {
				this.pojoConceptoGtos = this.ifzConValores.findById(concepto);
			} else {
				if (this.pojoConceptoGtos.getId() != concepto)
					this.pojoConceptoGtos = this.ifzConValores.findById(concepto);
			}
					
			this.pojoProvisionComprobado.setIdConcepto(this.pojoConceptoGtos);

			// Comprobamos si es un egreso y asignamos en negativo
			if (this.facturaTipo != null && ("E".equals(this.facturaTipo) || "EGRESO".equals(this.facturaTipo.trim().toLowerCase()))) {
				this.subtotal *= -1;
				this.totImpuestos *= -1;
				this.totRetenciones *= -1;
			}
			
			this.pojoProvisionComprobado.setSubtotal(this.subtotal);
			this.pojoProvisionComprobado.setTotalImpuestos(this.totImpuestos);
			this.pojoProvisionComprobado.setTotalRetenciones(this.totRetenciones);
			this.pojoProvisionComprobado.setObservaciones(this.observaciones);

			log.info("Asignamos ID Registro Gasto");
			this.pojoProvisionComprobado.setIdPagosGastos(this.pojoProvision); // asignar papa //this.pojoGtosComp.getId();
			
			if (this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L) {
				//si el gasto ya existe guardo las facturas creadas, sino las meto a una lista
				// y las guardare hasta que creen un gasto. Ya que sí el gasto no existe no puedo grabar 
				//facturas hasta que me serciore que creen un gasto. Siempre que el gasto no tenga llave 
				//la factura tampoco tendra llave, por eso no pregunto si es nueva
				this.pojoProvisionComprobado.setCreadoPor(this.usuarioId);
				this.pojoProvisionComprobado.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoProvisionComprobado.setModificadoPor(this.usuarioId);
				this.pojoProvisionComprobado.setFechaModificacion(Calendar.getInstance().getTime());
				   	   						
				if (! this.listDesgloseImpuestos.isEmpty()) {
					for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos)
						var.setId(this.pojoProvisionComprobado.getId());
				}
				
   				if (! this.listDesgloseRet.isEmpty()) {
					for (PagosGastosDetImpuestosExt var : this.listDesgloseRet) 
						var.setId(this.pojoProvisionComprobado.getId());
				}
			} else {
				// si el gasto existe ya puedo grabar facturas 
				if (registroNuevo) {
					this.pojoProvisionComprobado.setCreadoPor(this.usuarioId);
					this.pojoProvisionComprobado.setFechaCreacion(Calendar.getInstance().getTime());
					this.pojoProvisionComprobado.setModificadoPor(this.usuarioId);
					this.pojoProvisionComprobado.setFechaModificacion(Calendar.getInstance().getTime());

					log.info("Guardo factura en BD");
					this.ifzProvisionDetalles.setInfoSesion(this.loginManager.getInfoSesion());
					this.pojoProvisionComprobado.setId(this.ifzProvisionDetalles.save(this.pojoProvisionComprobado));
					log.info("Factura guardada");
					
					if (! this.listDesgloseImpuestos.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
							var.setId(this.pojoProvisionComprobado.getId());
							var.setId(this.ifzDesgloImpto.save(var));
						} 
					}
					
   					if (! this.listDesgloseRet.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseRet) {
							var.setIdPagosGastosDet(this.pojoProvisionComprobado);
							var.setId(this.ifzDesgloImpto.save(var));
						} 
   					}
				} else {
					log.info("Actualizo factura en BD");
					this.pojoProvisionComprobado.setModificadoPor(this.usuarioId);
					this.pojoProvisionComprobado.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzProvisionDetalles.setInfoSesion(this.loginManager.getInfoSesion());
					this.ifzProvisionDetalles.update(this.pojoProvisionComprobado);
					log.info("Factura actualizada");
					
					if (! this.listDesgloseImpuestos.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
							var.setIdPagosGastosDet(this.pojoProvisionComprobado);
							
							if (var.getId() == null || var.getId() <= 0L) {
								var.setCreadoPor(this.usuarioId);
								var.setFechaCreacion(Calendar.getInstance().getTime());
								var.setModificadoPor(this.usuarioId);
								var.setFechaModificacion(Calendar.getInstance().getTime());
								
								this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
								var.setId(this.ifzDesgloImpto.save(var));
							} else {
								var.setModificadoPor(this.usuarioId);
								var.setFechaModificacion(Calendar.getInstance().getTime());
								
								this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
								this.ifzDesgloImpto.update(var);
							}
						}
					}
					
					if (! this.listDesgloseRet.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseRet) {
							var.setIdPagosGastosDet(this.pojoProvisionComprobado);
							
							if (var.getId() == null || var.getId() <= 0L) {
								var.setCreadoPor(this.usuarioId);
								var.setFechaCreacion(Calendar.getInstance().getTime());
								var.setModificadoPor(this.usuarioId);
								var.setFechaModificacion(Calendar.getInstance().getTime());
								
								this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
								var.setId(this.ifzDesgloImpto.save(var));
							} else {
								var.setModificadoPor(this.usuarioId);
								var.setFechaModificacion(Calendar.getInstance().getTime());
								
								this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
								this.ifzDesgloImpto.update(var);
							}
						}
					}
				}
				
				//eliminando los pojos de impuestos de la base de datos,
				// ya que anteriormente solo los elimine de la memoria   					
				if (! this.lisImpuestosEliminados.isEmpty()) {
					for (PagosGastosDetImpuestosExt var : this.lisImpuestosEliminados) {
						if (var.getId() != null && var.getId() > 0L)
							this.ifzDesgloImpto.delete(var.getId());
					}
					
					// Limpiamos listado
					this.lisImpuestosEliminados.clear();
				}
			}  
			
			this.reg.setPojoFactura(this.pojoProvisionComprobado);
			this.reg.setListImpuestos(copiaListas(this.listDesgloseImpuestos));
			this.reg.setListRetenciones(copiaListas(this.listDesgloseRet));
			  					
			encontro = false;
			if (! this.listFacturaConImpuestos.isEmpty()) {
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					/*if (reg.getPojoFactura().getConceptoId().getId() == var.getPojoFactura().getConceptoId().getId()) {
						Double varmonto = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
						Double regmonto = ((reg.getPojoFactura().getSubtotal() + reg.getPojoFactura().getTotalImpuestos())- reg.getPojoFactura().getTotalRetenciones());
						
						if (varmonto.doubleValue() == regmonto.doubleValue()) {
							encontro = true;
						}
					}*/
					
					if (var.equals(this.reg)) {
						var = this.reg;
						encontro = true;
					}
				}
			}
			/*if (perfil == null && !"S".equals(perfil)) {
				if (! listFacturaConImpuestos.isEmpty()) {
					for (FacturaImpuestos var : this.listFacturaConImpuestos) {
						/*if (reg.getPojoFactura().getConceptoId().getValorId().compareTo(var.getPojoFactura().getConceptoId().getValorId()) == 0) {
							Double varmonto = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
							Double regmonto = ((reg.getPojoFactura().getSubtotal() + reg.getPojoFactura().getTotalImpuestos())- reg.getPojoFactura().getTotalRetenciones());
							if (varmonto.doubleValue() == regmonto.doubleValue()) {
								encontro = true;
								this.resOperacion = mensajesInf.get(20);
			   					return "ERROR";
							}
						}* /
						if (reg.getPojoFactura().getConceptoId().getId() == var.getPojoFactura().getConceptoId().getId()) {
							Double varmonto = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
							Double regmonto = ((reg.getPojoFactura().getSubtotal() + reg.getPojoFactura().getTotalImpuestos())- reg.getPojoFactura().getTotalRetenciones());
							
							if (varmonto.doubleValue() == regmonto.doubleValue()) {
								encontro = true;
								this.resOperacion = mensajesInf.get(20);
			   					return "ERROR";
							}
						}
						
						if (var.equals(reg)) {
							var = reg;
							encontro = true;
						}
					}
				}
		   	}*/
			   					
			if (! encontro)
				this.listFacturaConImpuestos.add(this.reg);
			
			actualizaTotalFaturas();
			this.facturaDescartada = false;
			this.facturaId = null;
			control(false, 8, "Comprobacion agregada", null);
   		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar Factura (Comprobacion) indicada", e);
		}
   	}

	public void enviar() {
		double montoRegEgreso = 0;
		
		try {
   			control();
			if (! validarEnvio())
				return;
			
			// Generamos Registro de Egreso
			// -------------------------------------------------------------------------------------------------------
			this.pojoProvision.setId(0L);
			this.pojoProvision.setTipo("P");
			this.pojoProvision.setEstatus("C"); // comprobado
			this.pojoProvision.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoProvision.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoProvision.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoProvision.setFechaModificacion(Calendar.getInstance().getTime());
			// Guardamos como Registro de Egreso
			this.pojoProvision.setId(this.ifzProvisiones.save(this.pojoProvision));
			
			List<FacturaImpuestos> seleccionados = null; 
			// Generamos detalles de Registro de Egreso
			for (FacturaImpuestos var : this.listFacturaConImpuestos) {
				if (! var.isSeleccionado()) 
					continue;
				
				if (seleccionados == null)
					seleccionados = new ArrayList<FacturaImpuestos>();
				seleccionados.add(new FacturaImpuestos(var.getPojoFactura(), var.getListImpuestos(), var.getListRetenciones()));
				
				// Marcamos como enviado a Registro de Egresos (Gastos)
				var.getPojoFactura().setEstatus("E");
				var.getPojoFactura().setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				var.getPojoFactura().setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzProvisionDetalles.update(var.getPojoFactura());
			}
			
			if (seleccionados == null || seleccionados.isEmpty())
				return; 
			
			for (FacturaImpuestos fi : seleccionados) {
				// Instanciamos el detalle para el Registro de Egreso (Gasto)
				fi.getPojoFactura().setId(0L);
				fi.getPojoFactura().setIdPagosGastos(this.pojoProvision);
				fi.getPojoFactura().setIdObra(null);
				fi.getPojoFactura().setIdOrdenCompra(null);
				fi.getPojoFactura().setEstatus("");
				fi.getPojoFactura().setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				fi.getPojoFactura().setFechaCreacion(Calendar.getInstance().getTime());
				fi.getPojoFactura().setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				fi.getPojoFactura().setFechaModificacion(Calendar.getInstance().getTime());
				// Guardamos
				fi.getPojoFactura().setId(this.ifzProvisionDetalles.save(fi.getPojoFactura()));
				
				/*fi = new FacturaImpuestos(); 
				fi.setPojoFactura(fi.getPojoFactura());
				fi.setListImpuestos(var.getListImpuestos());
				fi.setListRetenciones(var.getListRetenciones());*/
				
				if (! fi.getListImpuestos().isEmpty()) {
					for (PagosGastosDetImpuestosExt var2 : fi.getListImpuestos()) {
						var2.setIdPagosGastosDet(fi.getPojoFactura());

						if (var2.getId() == null || var2.getId() <= 0L)
							var2.setId(this.ifzDesgloImpto.save(var2));
						else
							this.ifzDesgloImpto.update(var2);
					}
				}				
				
				if (! fi.getListRetenciones().isEmpty()) {
					for (PagosGastosDetImpuestosExt var2 : fi.getListRetenciones()) {
						var2.setIdPagosGastosDet(fi.getPojoFactura());

						if (var2.getId() == null || var2.getId() <= 0L)
							var2.setId(this.ifzDesgloImpto.save(var2));
						else
							this.ifzDesgloImpto.update(var2);
					}
				}
				
				montoRegEgreso += fi.getPojoFactura().getTotal();
			}
			
			this.pojoProvision.setMonto(montoRegEgreso);
			this.ifzProvisiones.update(this.pojoProvision);
			control(false, -1, "Provisiones Enviadas", null);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar enviar las Comprobaciones seleccionadas al Registro de Egreso", e);
		}
	}

	private boolean validarEnvio() {
		boolean ningunaSeleccionada = false;
		
		for (FacturaImpuestos var : this.listFacturaConImpuestos) {
			if (var.isSeleccionado()) {
				ningunaSeleccionada = true;
				break;
			}
		}
		
		if (! ningunaSeleccionada) {
			control(-1, "Debe seleccionar al menos una Factura");
			return false;
		}
		
		return true;
	}

	private boolean validacionesFactura() {
		double sumaImpuestos = 0;
		double porcentaje = 0;
		int contador = 0;
		
		if (this.facturaTotal > 0) {
			if (this.subtotalMasImpuestos <= 0) {
				control(6, "Monto de la factura no valido");
				return false;
			}
			
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					if (var1.getValor() == null || var1.getValor() == BigDecimal.ZERO) {
						porcentaje = Double.parseDouble(var1.getIdImpuesto().getAtributo1());
						if (porcentaje > 0) {
							control(5, "Montos no validos en impuestos");
							return false;
						}
					}
				}
				
				// Validamos impuesto duplicado
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					contador = 0;
					for (PagosGastosDetImpuestosExt var2 : this.listDesgloseImpuestos) {
						if (! operacionCancelada) {
							if (! "0".equals(var2.getIdImpuesto().getAtributo2())) { //porque el cero es general esos pueden existir N impuestos al mimos tiempo
								if (var1.getIdImpuesto().getAtributo2().equals(var2.getIdImpuesto().getAtributo2())) {
									contador = contador + 1;
									if (contador == 2) {
										control(1, "Impuesto duplicado");
										return false;
									}
								}
							}
						}
					}					
				}

				// Validamos monto impuestos contra total facturado
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					if (! "AC".equals(var1.getIdImpuesto().getTipoCuenta()))
						sumaImpuestos = sumaImpuestos + var1.getValor().doubleValue();
				}
				
				if (sumaImpuestos > this.subtotalMasImpuestos) {
					control(7, "No coindice el monto de Impuestos con el total de la Factura");
					return false;
				}
			}
		}
		
		if (this.totRetenciones <= 0 && ! this.listRetDelGasto.isEmpty()) {
			control(16, "Debe indicar el monto de las Retenciones");
			return false;
		}
		
		return true;
	}
	
   	public void evaluaEditar() {
   		try {
			/*control();
   			this.facturaConLlave = false;
			if (this.reg.getPojoFactura().getId() != null) {
				if (! validaRequest("DETEDIT", "1")) { //! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					this.facturaConLlave = true;
		   			control(4, "No se permite editar las facturas");
					return;
		   		}*/
				
				control();
	   			this.facturaConLlave = false;
				if (this.reg.getPojoFactura().getId() != null) {
					if (! this.perfilPermiteEditar && ! validaRequest("DETEDIT", "1")) { //! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
						this.facturaConLlave = true;
			   			control(11, "No se permite editar las facturas");
			   			return;
			   		}
				
				
				editarFactura();
			}
			
			editarFactura();
			/*if (this.reg.getPojoFactura().getId() != null && ! this.isDebug) {
				this.facturaConLlave = true;
				/ *this.tipoMensaje = 4;
				this.resOperacion = this.mensajesInf.get(4);* /
				log.info("ERROR 4 - Una vez creado el gasto No es posible editar el comprobante");
				control(true, 4, this.mensajesInf.get(4));
				return "ERROR";
			} 
			
			this.facturaConLlave = false;
			this.pojoObra = this.pojoRegistroGastoComprobado.getIdObra();
			if (this.pojoRegistroGastoComprobado.getIdOrdenCompra() != null && this.pojoRegistroGastoComprobado.getIdOrdenCompra().getId() != null && this.pojoRegistroGastoComprobado.getIdOrdenCompra().getId() > 0L)
				this.pojoOrdenCompra = this.pojoRegistroGastoComprobado.getIdOrdenCompra();
			desglosaImpuestos();*/
		} catch (Exception e) {
			control("Ocurrio un problema al verificar permisos para Editar.", e);
		}
   	}
   	
   	public void evaluaEliminar() {
   		try {
   			control();
			this.facturaConLlave = false;
			if (reg.getPojoFactura().getId() != null) {
				this.facturaConLlave = true;
				control(3, "No se permite eliminar");
			} 
		} catch (Exception e) {
			control("Ocurrio un problema al verificar permisos para Eliminar.", e);
		}
   	}
   	
   	public void eliminarImpuestoDesglosado() {
   		double sumImpuestos = 0;
   		double sumRetenciones = 0;
   		
		try {
			this.lisImpuestosEliminados.add(this.pojoDesgloImpto);
			this.listDesgloseImpuestos.remove(this.pojoDesgloImpto);
			this.subtotal = this.subtotalMasImpuestos;
			this.totImpuestos = sumImpuestos;
			this.totRetenciones = sumRetenciones;
			this.totPago = this.subtotal + (this.totImpuestos - this.totRetenciones);
			
			if (! this.listDesgloseImpuestos.isEmpty()) {			
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(),2);	
					else	
						if ("1".equals(var.getIdImpuesto().getAtributo4()))
							sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(),2);	
				}

				this.subtotal = this.subtotalMasImpuestos;
				this.totImpuestos = sumImpuestos;
				this.totRetenciones = sumRetenciones;
				this.totPago = this.subtotal + (this.totImpuestos - this.totRetenciones);
				
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("0".equals(var.getIdImpuesto().getAtributo4()) && !"AC".equals(var.getIdImpuesto().getTipoCuenta())) {
						var.setValor(BigDecimal.valueOf(Utilerias.redondear(Utilerias.redondear(this.subtotal *((Double.valueOf(var.getIdImpuesto().getAtributo1())/ 100)),2),2)));
					}
				}
				sumImpuestos=0D;
				sumRetenciones=0D;
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(),2);	
					else	
						sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(),2);	
				}

				this.subtotal = this.subtotalMasImpuestos;
				this.totImpuestos = sumImpuestos;
				this.totRetenciones = sumRetenciones;
				this.totPago = this.subtotal + (this.totImpuestos - this.totRetenciones);
			} 
		} catch (Exception e) {
			control("error en eliminarImpuestoDesglosado", e);
		} finally {
			this.subtotal = Utilerias.redondear(this.subtotal, 2);
			this.totImpuestos = Utilerias.redondear(this.totImpuestos, 2);
			this.totRetenciones = Utilerias.redondear(this.totRetenciones, 2);
			this.totPago = Utilerias.redondear(this.totPago, 2);
		}
	}
   	
   	public void cambioMontoImpuesto() {
   		double sumImpuestos = 0;
   		double sumRetenciones = 0;
   		
		try {
			control();
			if (this.subtotalMasImpuestos!=0) {
				sumImpuestos=0D;
				sumRetenciones=0D;
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getValor() != null) {
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
							sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(),2);
						else
							sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(),2);
					}
				}

				this.subtotal = this.subtotalMasImpuestos;
				this.totImpuestos = sumImpuestos;
				this.totRetenciones = sumRetenciones;
				this.totPago = this.subtotal + (this.totImpuestos - this.totRetenciones);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cambiar el Monto de Impuesto", e);
		} finally {
			this.subtotal = Utilerias.redondear(this.subtotal, 2);
			this.totImpuestos = Utilerias.redondear(this.totImpuestos, 2);
			this.totRetenciones = Utilerias.redondear(this.totRetenciones, 2);
			this.totPago = Utilerias.redondear(this.totPago, 2);
		}
	}
   	
   	public void actualizaTotalFaturas() {
   		double subtotal = 0;
		double impuestos = 0;
		double retenciones = 0;
		
		try {
			control();
			this.totalFacturasReportadas = 0D;
			if (this.listFacturaConImpuestos.isEmpty())
				return;
			
			for (FacturaImpuestos var: this.listFacturaConImpuestos) {
				if (var.getPojoFactura().getSubtotal() == null)
					var.getPojoFactura().setSubtotal(0D);
				if (var.getPojoFactura().getTotalImpuestos() == null)
					var.getPojoFactura().setTotalImpuestos(0D);
				if (var.getPojoFactura().getTotalRetenciones() == null)
					var.getPojoFactura().setTotalRetenciones(0D);
				
				subtotal += var.getPojoFactura().getSubtotal().doubleValue();
				impuestos += var.getPojoFactura().getTotalImpuestos().doubleValue();
				retenciones += var.getPojoFactura().getTotalRetenciones().doubleValue();
			}
			
			this.totalFacturasReportadas = subtotal + (impuestos - retenciones); 
			this.totalFacturasReportadas = (new BigDecimal(this.totalFacturasReportadas).setScale(2, BigDecimal.ROUND_HALF_EVEN)).doubleValue();
		} catch (Exception e) {
			control("Ocurrio un problema al calcular el total de lo Comprobado", e);
		}
		/*BigDecimal totalAux = BigDecimal.ZERO;
		
		try {
			control();
			this.totalFacturasReportadas = 0D;
			if (! listFacturaConImpuestos.isEmpty()) {				
				for (FacturaImpuestos var: listFacturaConImpuestos) {
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
		} catch (Exception e) {
			control("error en actualizaTotalFaturas", e);
		}*/
	}
   	
   	public boolean validaGrupoImpuestos() {
		int contador = 0;
		
		try {
			control();
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					contador = 0;
					for (PagosGastosDetImpuestosExt var2 : this.listDesgloseImpuestos) {
						if (! operacionCancelada) {
							if (! "0".equals(var2.getIdImpuesto().getAtributo2())) { //porque el cero es general esos pueden existir N impuestos al mimos tiempo
								if (var1.getIdImpuesto().getAtributo2().equals(var2.getIdImpuesto().getAtributo2())) {
									contador = contador + 1;
									if (contador == 2) {
										control(1, "Impuesto duplicado");
										return false;
									}
								}
							}
						}
					}					
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al validar los Impuestos", e);
			return false;
		}
		
		return true;
	}
   	
   	public boolean validaMontoImpuestos() {
   		double porcentaje = 0;
   		
		try {
			control();
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					if (var1.getValor() == null || var1.getValor() == BigDecimal.ZERO) {
						porcentaje = Double.parseDouble(var1.getIdImpuesto().getAtributo1());
						if (porcentaje > 0) {
							control(5, "Montos no validos en impuestos");
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al valida los Montos de Impuestos", e);
			return false;
		}
		
		return true;
	}
   	
   	public boolean validaMontoFactura() {
		try {
			control();
			if (this.subtotalMasImpuestos <= 0) {
				control(6, "Monto de la factura no valido");
				return false;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar validar el Monto de la Factura", e);
			return false;
		}
		
		return true;
	}
   	
	public boolean validaMontoFacturaContraTotalImpuestos() {
		Double sumaImpuestos = 0D;
		
		try {
			control();
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					if (! "AC".equals(var1.getIdImpuesto().getTipoCuenta()))
						sumaImpuestos = sumaImpuestos + var1.getValor().doubleValue();
				}
			}
			
			if (sumaImpuestos > this.subtotalMasImpuestos) {
				control(7, "No coindice el monto de Impuestos con el total de la Factura");
				return false;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al validar los montos de la Factura con los Impuestos", e);
			return false;
		}

		return true;
	}
	
	public void cancelarCheques() {
   		/*try {
   			Respuesta resp = new Respuesta();
   			resOperacion=""; 			
   			resp = ifzGtosComp.salvar(this.pojoGtosComp, false,null);
			
			if (! "BIEN".equals(resp.getRespuesta()) && resp.getResultado() == -1) {
				
				encontroMismoGrupo = true;// esta variable termine usandola como bandera gral
				this.resOperacion = resp.getRespuesta();
				this.tipoMensaje = 8;
				
				//antes le mostraba el mensaje que me regresaba la funcion CANCELA_CHEQUES pero ahora solo le dire que se guardo		
				guardar();
				
			}
   		} catch (Exception e) {
   			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en cancelarCheques", e);
		}*/
   	}

	public void desglosaImpuestos() {
		List<PagosGastosDetImpuestosExt> listDesgloseImpuestos_tmp = null;
   		double porcentajeImpuesto = 0;
   		double sumImpuestos = 0;
   		double sumRetenciones = 0;
   		double importeImpuesto = 0;
   		long idConcepto = 0;
   		// ----------------------------------------------------------------
   		double montoBaseImpuestos = 0;
   		boolean baseSubtotal = false;
   			
   		try {
			control();
			if (this.subtotalMasImpuestos == null)
				this.subtotalMasImpuestos = 0D;
			if (this.conceptoGasto == null)
				this.conceptoGasto = "";
			if (this.conceptoGastoPrevio == null)
				this.conceptoGastoPrevio = "";
			this.subtotal = 0D; //this.subtotalMasImpuestos;
			this.totImpuestos = sumImpuestos;
			this.totRetenciones = sumRetenciones;
			this.totPago = this.subtotalMasImpuestos; //this.totalMenosImpuestos + (this.totImpuestos - this.totRetenciones);
			montoBaseImpuestos = (this.facturaSubtotal > 0 ? this.facturaSubtotal : this.subtotalMasImpuestos);
			baseSubtotal = (this.facturaSubtotal > 0);

   			if (this.facturaConLlave) {
				this.listDesgloseImpuestos.clear();
				this.subtotal = this.subtotalMasImpuestos;
   			}
   			
   			if ("".equals(this.conceptoGasto.trim()) || this.subtotalMasImpuestos <= 0D) {
   				if (! this.listDesgloseImpuestos.isEmpty())
					this.lisImpuestosEliminados.addAll(this.listDesgloseImpuestos);
				this.listDesgloseImpuestos.clear();
				this.subtotal = this.subtotalMasImpuestos;
				this.totPago = this.subtotalMasImpuestos;
				return;
			}

   			// Validamos Concepto de Gastos
			if (! this.conceptoGastoPrevio.equals(this.conceptoGasto)) {
				this.conceptoGastoPrevio = this.conceptoGasto;
				if (! this.listDesgloseImpuestos.isEmpty())
					this.lisImpuestosEliminados.addAll(this.listDesgloseImpuestos);
				this.listDesgloseImpuestos.clear();
				
				idConcepto = matchToId(this.conceptoGasto, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
				if (idConcepto <= 0L) {
					this.listDesgloseImpuestos.clear();
					return;
				}

				// Recuperamos Concepto de Gastos
				this.pojoConceptoGtos = this.ifzConValores.findById(idConcepto);
				if (this.pojoConceptoGtos == null || this.pojoConceptoGtos.getId() <= 0L) {
					if (! this.listDesgloseImpuestos.isEmpty())
						this.lisImpuestosEliminados.addAll(this.listDesgloseImpuestos);
					this.listDesgloseImpuestos.clear();
					return;
				}
			}

			// Impuestos del Gasto (Egreso)
			this.listImpuestosDelGasto = this.ifzGastoImpuesto.findAllExt(this.pojoConceptoGtos.getId(), "");
			if (this.listImpuestosDelGasto != null && ! this.listImpuestosDelGasto.isEmpty()) {
				if (this.listDesgloseImpuestos == null || this.listDesgloseImpuestos.isEmpty()) {
					for (GastosImpuestoExt var : this.listImpuestosDelGasto) {
						this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
						this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
						this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoProvisionComprobado);
						this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
						this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());

						importeImpuesto = 0D;
						porcentajeImpuesto = Double.parseDouble(var.getImpuestoId().getAtributo1().trim());
						if (porcentajeImpuesto > 0) {
							porcentajeImpuesto = (porcentajeImpuesto / 100); 
							if (baseSubtotal) 
								importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto);  
							else 
								importeImpuesto = montoBaseImpuestos - (montoBaseImpuestos / (1 + porcentajeImpuesto));
							importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
	
							if ("AC".equals(var.getImpuestoId().getTipoCuenta())) {
								sumRetenciones += importeImpuesto;
								sumRetenciones = Utilerias.redondear(sumRetenciones, 2); 
							} else if ("DE".equals(var.getImpuestoId().getTipoCuenta())) {
								sumImpuestos += importeImpuesto;
								sumImpuestos = Utilerias.redondear(sumImpuestos, 2); 
							}
						}

						this.pojoDesgloImpto.setValor(new BigDecimal(importeImpuesto));	
						this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
					}

					this.facturaSubtotal = 0D;
					this.subtotal = montoBaseImpuestos - (sumImpuestos - sumRetenciones);
					if (baseSubtotal) 
						this.subtotal = montoBaseImpuestos;
					this.totImpuestos = sumImpuestos;
					this.totRetenciones = sumRetenciones;
					this.totPago = this.subtotal + (this.totImpuestos - this.totRetenciones);
					return;
				}
				
				// la lista si tenia elementos y hay que hacer el desglose imptos en base a los imptos que tiene actualmente la lista
				listDesgloseImpuestos_tmp = new ArrayList<PagosGastosDetImpuestosExt>();
				listDesgloseImpuestos_tmp.addAll(this.listDesgloseImpuestos);					
				this.listDesgloseImpuestos.clear();
				
				// Impuestos y Retenciones
				for (PagosGastosDetImpuestosExt var : listDesgloseImpuestos_tmp) {	
					importeImpuesto = 0;
					porcentajeImpuesto = Double.parseDouble(var.getIdImpuesto().getAtributo1().trim());
					if (porcentajeImpuesto > 0) {
						porcentajeImpuesto = (porcentajeImpuesto / 100); 
						if (baseSubtotal) 
							importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto);  
						else 
							importeImpuesto = montoBaseImpuestos - (montoBaseImpuestos / (1 + porcentajeImpuesto));
						importeImpuesto = Utilerias.redondear(importeImpuesto, 2);
						
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta())) {
							sumRetenciones += importeImpuesto;
							sumRetenciones = Utilerias.redondear(sumRetenciones, 2); 
						} else if ("DE".equals(var.getIdImpuesto().getTipoCuenta())) {
							sumImpuestos += importeImpuesto;
							sumImpuestos = Utilerias.redondear(sumImpuestos, 2); 
						}
					}

					var.setValor(new BigDecimal(importeImpuesto));
					this.listDesgloseImpuestos.add(var);
				}

				this.facturaSubtotal = 0D;
				this.subtotal = montoBaseImpuestos - (sumImpuestos - sumRetenciones);
				if (baseSubtotal) 
					this.subtotal = montoBaseImpuestos;
				this.totImpuestos = sumImpuestos;
				this.totRetenciones = sumRetenciones;
				this.totPago = this.subtotal + (this.totImpuestos - this.totRetenciones);
			}
			
			/*// Impuestos
			this.listImpuestosDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","DE", this.pojoConceptoGtos.getId());
			if (this.listImpuestosDelGasto != null && ! this.listImpuestosDelGasto.isEmpty()) {
				if (this.listDesgloseImpuestos.isEmpty()) {
					// Traslados
					for (GastosImpuestoExt var : this.listImpuestosDelGasto) {
						this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
						this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
						this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoProvisionComprobado);
						this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
						this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());

						importeImpuesto = 0D;
						porcentajeImpuesto = Double.parseDouble(var.getImpuestoId().getAtributo1().trim());
						if (porcentajeImpuesto > 0) {
							porcentajeImpuesto = (porcentajeImpuesto / 100); 
							importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto); 
							importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
						}
						
						sumImpuestos += importeImpuesto; 
						sumImpuestos = Utilerias.redondear(sumImpuestos, 4);
						this.pojoDesgloImpto.setValor(new BigDecimal(importeImpuesto));	
						this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
					}
					
					// Retenciones
					this.listRetDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","AC", this.pojoConceptoGtos.getId());
					if (! this.listRetDelGasto.isEmpty()) {
						for (GastosImpuestoExt var : this.listRetDelGasto) {
							this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
							this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoProvisionComprobado);
							this.pojoDesgloImpto.setValor(BigDecimal.ZERO);				
							this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
							this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
							this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());

							importeImpuesto = 0;
							porcentajeImpuesto = Double.parseDouble(var.getImpuestoId().getAtributo1().trim());
							if (porcentajeImpuesto > 0) {
								porcentajeImpuesto = (porcentajeImpuesto / 100); 
								importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto); 
								importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
							}

							sumRetenciones += importeImpuesto;
							sumRetenciones = Utilerias.redondear(sumRetenciones, 4);
							this.pojoDesgloImpto.setValor(new BigDecimal(importeImpuesto));
							this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
						}
					}

					this.facturaSubtotal = 0D;
					this.subtotal = this.subtotalMasImpuestos - (sumImpuestos - sumRetenciones);
					this.totImpuestos = sumImpuestos;
					this.totRetenciones = sumRetenciones;
					this.totPago = this.subtotal + (this.totImpuestos - this.totRetenciones);
					return;
				} 
				
				// la lista si tenia elementos y hay que hacer el desglose imptos en base a los imptos que tiene actualmente la lista
				listDesgloseImpuestos_tmp = new ArrayList<PagosGastosDetImpuestosExt>();
				listDesgloseImpuestos_tmp.addAll(this.listDesgloseImpuestos);					
				this.listDesgloseImpuestos.clear();
				
				// Impuestos y Retenciones
				for (PagosGastosDetImpuestosExt var : listDesgloseImpuestos_tmp) {	
					importeImpuesto = 0;
					porcentajeImpuesto = Double.parseDouble(var.getIdImpuesto().getAtributo1().trim());
					if (porcentajeImpuesto > 0) 
						porcentajeImpuesto = (porcentajeImpuesto / 100); //1 + (porcentajeImpuesto / 100);
					
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta())) {
						if (porcentajeImpuesto > 0) {
							importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto); // this.subtotalMasImpuestos - (this.subtotalMasImpuestos / porcentajeImpuesto);
							importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
						}

						sumRetenciones += importeImpuesto;
						sumRetenciones = Utilerias.redondear(sumRetenciones, 4); 
					} else if ("DE".equals(var.getIdImpuesto().getTipoCuenta())) {
						if (porcentajeImpuesto > 0) {
							importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto); // this.subtotalMasImpuestos - (this.subtotalMasImpuestos / porcentajeImpuesto);
							importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
						}
						
						sumImpuestos += importeImpuesto;
						sumImpuestos = Utilerias.redondear(sumImpuestos, 4); 
					}

					var.setValor(new BigDecimal(importeImpuesto));
					this.listDesgloseImpuestos.add(var);
				}

				this.facturaSubtotal = 0D;
				this.subtotal = this.subtotalMasImpuestos - (sumImpuestos - sumRetenciones);
				this.totImpuestos = sumImpuestos;
				this.totRetenciones = sumRetenciones;
				this.totPago = this.subtotal + (this.totImpuestos - this.totRetenciones);
			} */
		} catch (Exception e) {
			control("Ocurrio un problema al desglozar los impuestos", e);
		} finally {
   			if (! "".equals(this.conceptoGasto.trim()) && this.subtotalMasImpuestos > 0D) {
   				if (this.subtotal <= 0 && this.subtotalMasImpuestos > 0D)
   					this.subtotal = this.subtotalMasImpuestos;
				this.subtotal = Utilerias.redondear(this.subtotal, 2);
				this.totImpuestos = Utilerias.redondear(this.totImpuestos, 2);
				this.totRetenciones = Utilerias.redondear(this.totRetenciones, 2);
				this.totPago = Utilerias.redondear(this.totPago, 2);
				
				log.info("SUBTOTAL    : " + this.subtotal);
				log.info("IMPUESTOS   : " + this.totImpuestos);
				log.info("RETENCIONES : " + this.totRetenciones);
				log.info("TOTAL       : " + this.totPago);
   			}
		}
	}
	
	public void desglosaImpuestosOLD() {
		List<PagosGastosDetImpuestosExt> listDesgloseImpuestos_tmp = new ArrayList<PagosGastosDetImpuestosExt>();
		Pattern pat = null;
		Matcher match = null;
		long conceptoGtoId = 0;
   		double sumImpuestos = 0;
   		double sumRetenciones = 0;
   		double importeImpuesto = 0;
		
		try { 
			this.totImpuestos = 0D;
			this.totRetenciones = 0D;
			this.subtotal = Utilerias.redondear(this.subtotalMasImpuestos - sumImpuestos, 2);
			this.totPago = Utilerias.redondear(this.subtotalMasImpuestos - this.totRetenciones, 2);
			this.listDesgloseImpuestos.clear();
				
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
						//if ("1".equals(var.getImpuestoId().getAtributo4())) {
						this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
						this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
						this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoProvisionComprobado);
						this.pojoDesgloImpto.setModificadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
						this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
						this.pojoDesgloImpto.setCreadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
						this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
					
						importeImpuesto = Utilerias.redondear(this.subtotalMasImpuestos - (Utilerias.redondear(this.subtotalMasImpuestos / ((Double.valueOf(var.getImpuestoId().getAtributo1()) / 100) + 1), 2)), 2);
						this.pojoDesgloImpto.setValor(new BigDecimal(importeImpuesto));		
						sumImpuestos = Utilerias.redondear(sumImpuestos + this.pojoDesgloImpto.getValor().doubleValue(), 2);
						this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
						//}
					}
					
					this.totImpuestos = sumImpuestos;
					this.subtotal = Utilerias.redondear(this.subtotalMasImpuestos - sumImpuestos, 2);
					
					// Verifico si existen retenciones para el gasto
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
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoProvisionComprobado);
							
							importeImpuesto = Utilerias.redondear(Utilerias.redondear(this.subtotal *((Double.valueOf(var.getImpuestoId().getAtributo1())/ 100)),2),2);
							this.pojoDesgloImpto.setValor(BigDecimal.valueOf(importeImpuesto));
							
							sumRetenciones = Utilerias.redondear(sumRetenciones + this.pojoDesgloImpto.getValor().doubleValue(), 2);
							this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
							this.totRetenciones = sumRetenciones;
						}
					} else {
						this.listRetDelGasto.clear();
						this.listDesgloseRet.clear();
						log.info("Sin retenciones");
					}
				} else {// la lista si tenia elementos y hay que hacer el desglose imptos en base a los imptos que tiene actualmente la lista
					listDesgloseImpuestos_tmp.addAll(listDesgloseImpuestos);					
					this.listDesgloseImpuestos.clear();
					this.subtotal = this.subtotalMasImpuestos;

					log.info("Desglosando impuestos");
					for (PagosGastosDetImpuestosExt var : listDesgloseImpuestos_tmp) {		
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta())) {
							var.setValor(BigDecimal.ZERO);
							sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(), 2);
						} else {
							importeImpuesto = Utilerias.redondear(this.subtotalMasImpuestos -(Utilerias.redondear(this.subtotalMasImpuestos / ((Double.valueOf(var.getIdImpuesto().getAtributo1())/ 100)+1),2)),2);
							var.setValor(new BigDecimal(importeImpuesto));
							sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(),2);
						}
						
						this.listDesgloseImpuestos.add(var);
					}
					
					this.totImpuestos = sumImpuestos;
					this.totRetenciones = sumRetenciones;
					this.subtotal = Utilerias.redondear( this.subtotalMasImpuestos - sumImpuestos,2);
					this.totPago = Utilerias.redondear(this.subtotalMasImpuestos - this.totRetenciones,2);
				}
			} else {
				log.info("Sin impuestos asociados");
				//porque si no tiene impuestos asociados el gasto, esta propiedad se quedaba NULL y marcaba error
				this.totImpuestos = 0D;
				this.totRetenciones = 0D;
			}

			this.subtotal = Utilerias.redondear(this.subtotalMasImpuestos - sumImpuestos, 2);
			this.totPago = Utilerias.redondear(this.subtotalMasImpuestos - this.totRetenciones, 2);
		} catch (Exception e) {
			control("Ocurrio un problema al hacer el desglose de Impuestos", e);
		}
	}
   	
	public void agregaRet() {
		Double mto_ret; 
		
		try {
			control();
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getIdImpuesto().getId() == this.pojoNvaRet.getId()) {
						control(17, "Retencion duplicada");
						return;
					}
				}
			}

			mto_ret = Utilerias.redondear((this.subtotalMasImpuestos - this.totImpuestos) * (Double.valueOf(this.pojoNvaRet.getAtributo1())/ 100),2);
			this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
			this.pojoDesgloImpto.setIdImpuesto(this.pojoNvaRet);
			this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoProvisionComprobado);
			this.totRetenciones = Utilerias.redondear(this.totRetenciones + mto_ret,2);
			this.pojoDesgloImpto.setValor(BigDecimal.valueOf(mto_ret));
			this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
			this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
			this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
			this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar agregar la Retencion", e);
		}
	}

	public List<Persona> autoacompletaBeneficiario(Object obj) {
		try {
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listBeneficiarios = this.ifzPersonas.findLikeClaveNombre("nombre", obj.toString(), this.pojoGpoValPersonas, this.tipoPersona, 100, false);
			return this.listBeneficiarios;
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar los Beneficiarios", e);
			return new ArrayList<Persona>();
		}
		
		/*List<Empleado> listEmpleados = null;
		
		try {
			if (this.listBeneficiarios != null)
				this.listBeneficiarios.clear();

			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			listEmpleados = this.ifzEmpleado.findLike(obj.toString(), null, 0, 100);
			if (listEmpleados != null && ! listEmpleados.isEmpty()) {
				for (Empleado var : listEmpleados)
					this.listBeneficiarios.add(this.ifzPersonas.findById(var.getIdPersona()));
			}

			return this.listBeneficiarios;
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar los Beneficiarios", e);
			return new ArrayList<Persona>();
		}*/
	}
	
	public List<Persona> autoacompletaProveedor (Object obj) {
		try {
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listProveedores = this.ifzPersonas.findLikeProveedor(obj.toString(), this.pojoGpoValPersonas, "PROV", this.pojoConceptoGtos.getId(), 100);
			return this.listProveedores;
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Proveedores", e);
			return new ArrayList<Persona>();
		}
	}
	
	public void GeneraListaProveedores() {
		Pattern pat = null;
		Matcher match = null;
		
		try {
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			//validando gasto para poder presentar  una lista de proveedores en base al gasto seleccionado
			match = pat.matcher(this.conceptoGasto);			
			if (match.find())							
				this.pojoConceptoGtos = this.ifzConValores.findById(Long.valueOf(match.group(1)));
		} catch (Exception e) {
			control("ocurrio un problema al intentar generar la lista de Proveedores", e);
		}
	}
	
	private void editReg() {
		this.listDesgloseImpuestos.clear();
		this.listDesgloseRet.clear();
		this.listRetDelGasto.clear();
		
		if (this.reg == null)
			this.reg = new FacturaImpuestos();

		this.pojoProvisionComprobado = this.reg.getPojoFactura();
		this.conceptoGasto = this.reg.getPojoFactura().getIdConcepto().getId() +" - "+ this.reg.getPojoFactura().getIdConcepto().getDescripcion();
		this.nombreProveedor = this.reg.getPojoFactura().getIdProveedor().getId() +" - "+ this.reg.getPojoFactura().getIdProveedor().getNombre(); //this.reg.getPojoFactura().getProveedorId().getPersonaId() +" - "+ this.reg.getPojoFactura().getProveedorId().getNombre() +" "+ (this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() : "") +" "+(this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() : "");
		this.descripcionFactura = this.reg.getPojoFactura().getIdConcepto().getDescripcion() + " - " + this.reg.getPojoFactura().getIdPagosGastos().getBeneficiario(); // + " - "+this.reg.getPojoFactura().getProveedorId().getNombre()+" " + (this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() : "")+" " +(this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() :"");
		this.subtotalMasImpuestos = Utilerias.redondear((this.reg.getPojoFactura().getSubtotal() + this.reg.getPojoFactura().getTotalImpuestos()), 2);
		this.totPago = Utilerias.redondear((this.subtotalMasImpuestos - this.reg.getPojoFactura().getTotalRetenciones()), 2);
		this.subtotal = this.reg.getPojoFactura().getSubtotal();
		this.totImpuestos = this.reg.getPojoFactura().getTotalImpuestos();
		this.totRetenciones = this.reg.getPojoFactura().getTotalRetenciones();
		this.observaciones = this.reg.getPojoFactura().getObservaciones();
		
		if (this.reg.getListImpuestos() != null && ! this.reg.getListImpuestos().isEmpty())
			this.listDesgloseImpuestos = copiaListas(reg.getListImpuestos());
		if (this.reg.getListRetenciones() != null && ! this.reg.getListRetenciones().isEmpty())
			this.listDesgloseRet = copiaListas(reg.getListRetenciones());
		
		if ((this.listDesgloseImpuestos == null || this.listDesgloseImpuestos.isEmpty()) && (this.listDesgloseRet == null || this.listDesgloseRet.isEmpty()))
			desglosaImpuestos();
	}
	
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
	
	private boolean comprobarUsuario() {
		List<Empleado> listEmpleado = null;
		Long idEmpleado = null;
		
		try {
			if (this.loginManager == null)
				return false;
			
			// Detarminamos EMPLEADO de manera directa o por correo electronico
			idEmpleado = this.loginManager.getUsuarioResponsabilidad().getUsuario().getIdEmpleado();
			if (idEmpleado == null || idEmpleado <= 1L) {
				listEmpleado = this.ifzEmpleado.findByProperty("email", this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo(), false, false, "", 0);
				if (listEmpleado != null && listEmpleado.size() == 1)
					idEmpleado = listEmpleado.get(0).getId();
			}
			
			if (idEmpleado == null || idEmpleado <= 1L) {
				log.info("Usuario sin Empleado asociado");
				return false;
			}
			
			// Recuperamos empleado
			this.pojoEmpleadoUsuario = this.ifzEmpleado.findByIdExt(idEmpleado);
			if (this.pojoEmpleadoUsuario == null || this.pojoEmpleadoUsuario.getId() == null || this.pojoEmpleadoUsuario.getId() <= 0L) {
				log.info("No se encontro Empleado asociado al Usuario");
				return false;
			}
			
			// Recuperamos SUCURSAL base del Empleado
			this.idSucursalSugerida = this.pojoEmpleadoUsuario.getSucursal().getId();
		} catch (Exception e) {
			log.error("Ocurrio un problema en Inventarios.EntradasAlmacenAction.comprobarUsuario()", e);
			return false;
		}
		
		return true;
	}

	private long matchToId(String target, String patern) {
		Pattern pat = null;
		Matcher match = null;

		try {
			pat = Pattern.compile(patern);
			match = pat.matcher(target);
			if (match.find())
				return Long.valueOf(match.group(1));
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar el patron para recuperar el ID", e);
		}
		
		return 0;
	}
	
	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}

	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		log.error("\n\nPROVISIONES CXP :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n", throwable);
	}

	// --------------------------------------------------------------------------
	// Terceros
	// --------------------------------------------------------------------------
	
	public List<Persona> autoacompletaTerceros (Object obj) {
		try {
			this.listTerceros = this.ifzPersonas.findLikePersonas(obj.toString(), 20);
			return this.listTerceros;
		} catch (Exception e) {
			log.error("error en autoacompletaTerceros", e);
			return new ArrayList<Persona>();
		}
	}
	
	public void cambioTerceros() {
		Pattern pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		Matcher match = null;
		
		try {
			control();
			if ("S".equals(this.operacion)) {
				match = pat.matcher(this.beneficiario);
				if (! match.find()) 
					return;
				
				this.pojoTerceros = this.ifzProvisiones.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findById(Long.valueOf(match.group(1)));
				if (this.pojoTerceros == null) 
					return;
				
				if (this.pojoTerceros.getBanco() != null) {
					control(-1, "El Beneficiario no tiene capturado banco, verifique !!");
					return;
				}
				
				if (this.pojoTerceros.getClabeInterbancaria() != null && !"".equals(this.pojoTerceros.getClabeInterbancaria())) {
					if (this.pojoTerceros.getNombre() != null && !"".equals(this.pojoTerceros.getNombre()) && this.pojoTerceros.getPrimerApellido() != null && !"".equals(this.pojoTerceros.getPrimerApellido()))
						return;
					else
						control(-1, "El nombre del Beneficiario no esta completo, verifique !!");
				} else {
					control(-1, "El Beneficiario no tiene capturada cuenta Clabe, verifique !!");
				}
			}
		} catch (Exception e) {			
			control("error en cambioTerceros", e);
		}
	}
	
   	// ---------------------------------------------------------------------------
	// CONCEPTOS
   	// ---------------------------------------------------------------------------
	
	public void nuevaBusquedaConceptos() {
		control();
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
   		this.valorBusquedaConceptos = "";
   		this.pagBusquedaConceptos = 1;
   		this.avanzadaConceptos = false;
   		
   		if (this.listConceptoGasto == null)
   			this.listConceptoGasto = new ArrayList<ConValores>();
   		this.listConceptoGasto.clear();
   		this.pojoConcepto = new ConValores();
	}
	
	public void buscarConceptos() {
		String value = "";
		
		try {
			control();
			if (this.campoBusquedaConceptos == null || "".equals(this.campoBusquedaConceptos))
				this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
			value = this.valorBusquedaConceptos;
			value = (value == null) ? "" : value.trim();
			if (value.contains(" "))
				value = value.replace(" ", "%");
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listConceptoGasto = this.ifzConValores.findLikeProperty(this.campoBusquedaConceptos, value, this.pojoGpoValGasto, 0);
			if (this.listConceptoGasto == null || this.listConceptoGasto.isEmpty()) {
				control(13, "Busqueda sin resultados");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al buscar los Conceptos para Gastos", e);
		}
	}
	
	public void seleccionarConcepto() {
		try {
			control();
			if (this.pojoConcepto == null || this.pojoConcepto.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar el Concepto seleccionado");
				return;
			}
			
			this.conceptoGasto = this.pojoConcepto.getId() + " - " + this.pojoConcepto.getDescripcion();
			desglosaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al buscar los Conceptos para Gastos", e);
		} finally {
			nuevaBusquedaConceptos();
		}
	}

	public void toggleAvanzadaConceptos() {
		this.avanzadaConceptos = ! this.avanzadaConceptos;
	}
	
	// ------------------------------------------------------------------------------------------
	// CARGA DE ARCHIVO
	// ------------------------------------------------------------------------------------------
	
	public void nuevaCarga() {
		this.fileName = "";
		this.fileSrc = null;
		this.fileLoaded = false;
		if (this.isDebug) 
			log.info("\n\nCarga preparada :: " + this.fileLoaded + "\n\n");
	}
	
	public void uploadListener(UploadEvent event) throws Exception {
		UploadItem item = null;
		
		try {
			control();
			item = event.getUploadItem();
			if (this.fileName.equals(item.getFileName())) 
				return;
			this.fileName = item.getFileName();
			this.fileSrc = ((item.isTempFile()) ? Files.readAllBytes(item.getFile().toPath()) : item.getData());
			if (this.fileSrc != null)
				this.fileLoaded = true;
			if (this.isDebug) {
				log.info("\n\n------------------------------------------------------------------------"
						+ "\nArchivo cargado :: " + this.fileLoaded
						+ "\n------------------------------------------------------------------------"
						+ "\nTemp            :: " + item.isTempFile() 
						+ "\nFilename        :: " + item.getFileName() 
						+ "\nFilesize        :: " + item.getFileSize() 
						+ "\nFile            :: " + item.getFile() 
						+ "\nData            :: " + item.getData() 
						+ "\n------------------------------------------------------------------------");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el archivo cargado", e);
		}
	}
	
	public void analizarArchivo() throws Exception {
		SimpleDateFormat formatter = null;
		boolean emisorDesconocido = false;
		List<Persona> listProveedores = null;
		PersonaExt pojoProveedor = null;
		String tipoPersona = "";
		Respuesta respuesta = new Respuesta();
		String facturaFolio = "";
		String facturaFecha = "";
		String fileName = "";
		double saldoFactura = 0;
		// ------------------------------------------
		String comprobados = "";
		
		try {
			control();
			reiniciaFactura();
			if (this.fileSrc == null) {
				log.warn("VALIDACION - No ha especificado ninguna factura (*.xml)");
				control(-1, "No ha especificado ninguna factura (*.xml)");
				return;
			} 
			
			this.ifzProvisionDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzProvisionDetalles.cargaFacturaXML(this.fileSrc, TiposGastoCXP.Provision, this.pojoProvision.getId());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				reiniciaFactura();
				if (respuesta.getBody().getValor("comprobados") != null)
					comprobados = desglozaComprobados(respuesta.getBody().getValor("comprobados").toString());
				log.warn("ERROR INTERNO - Ocurrio un problema al intentar guardar la factura. " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError() + "\nNo se cargo la Factura." + comprobados);
				return;
			}
			
			// recupero los datos de la factura cargada
			this.facturaId = (Long) respuesta.getBody().getValor("idComprobante");
			this.facturaRfc = respuesta.getBody().getValor("comprobanteRfc").toString();
			this.facturaTipo = respuesta.getBody().getValor("comprobanteTipo").toString();
			this.facturaMoneda = respuesta.getBody().getValor("comprobanteMoneda").toString();
			this.facturaDescuento = Double.parseDouble(respuesta.getBody().getValor("comprobanteDescuento").toString());
			this.facturaSubtotal = Double.parseDouble(respuesta.getBody().getValor("comprobanteSubtotal").toString());
			this.facturaTotal = Double.parseDouble(respuesta.getBody().getValor("comprobanteTotal").toString());
			this.facturaTipoCambio = 1.0;
			if (respuesta.getBody().getValor("comprobanteTipoCambio") != null && ! "".equals(respuesta.getBody().getValor("comprobanteTipoCambio").toString()))
				this.facturaTipoCambio = Double.parseDouble(respuesta.getBody().getValor("comprobanteTipoCambio").toString());
			facturaFecha = respuesta.getBody().getValor("comprobanteFecha").toString();
			saldoFactura = Double.parseDouble(respuesta.getBody().getValor("comprobanteSaldo").toString());
			
			// Comprobamos con los detalles actuales
			for (FacturaImpuestos item : this.listFacturaConImpuestos) {
				if (item.getPojoFactura().getIdCfdi().longValue() == this.facturaId.longValue()) {
					reiniciaFactura();
					control(-1, "El Documento ya fue cargado previamente en esta Provision.");
					return;
				}
					//saldoFactura -= item.getPojoFactura().getTotal();
			}
			
			if (this.facturaTotal.doubleValue() > 0 && saldoFactura <= 0) {
				reiniciaFactura();
				if (respuesta.getBody().getValor("comprobados") != null)
					comprobados = desglozaComprobados(respuesta.getBody().getValor("comprobados").toString());
				log.warn("ERROR INTERNO - La Factura indicada ya fue saldada. ");
				control(-1, "El Documento ya ha sido saldado.\nNo se pueden Provisionar Comprobantes saldados." + comprobados);
				return;
			}
			
			if (respuesta.getBody().getValor("comprobanteRazonSocial") != null && ! "".equals(respuesta.getBody().getValor("comprobanteRazonSocial").toString().trim()))
				this.facturaEmisor = respuesta.getBody().getValor("comprobanteRazonSocial").toString();
			
			if (respuesta.getBody().getValor("comprobanteFactura") != null && ! "".equals(respuesta.getBody().getValor("comprobanteFactura").toString().trim()))
				facturaFolio = respuesta.getBody().getValor("comprobanteFactura").toString();
			
			// Comprobamos Persona/Negocio
			tipoPersona = "N";
			listProveedores = this.ifzPersonas.findLikeClaveNombre("rfc", this.facturaRfc, this.pojoGpoValPersonas, tipoPersona, 0, false);
			if (listProveedores == null || listProveedores.isEmpty()) {
				tipoPersona = "P";
				listProveedores = this.ifzPersonas.findLikeClaveNombre("rfc", this.facturaRfc, this.pojoGpoValPersonas, tipoPersona, 0, false);
				if (listProveedores == null || listProveedores.isEmpty()) {
					emisorDesconocido = true;
					tipoPersona = "N";
				}
			} 
			
			if (listProveedores != null && ! listProveedores.isEmpty()) {
				pojoProveedor = extenderPersona(listProveedores.get(0));
				pojoProveedor.setTipoPersona(("N".equals(tipoPersona) ? 2L : 1L));
				this.pojoProvisionComprobado.setIdProveedor(pojoProveedor);
				this.pojoProvisionComprobado.setTipoPersonaProveedor(tipoPersona);
				this.facturaEmisor = pojoProveedor.getNombre();
			}

			this.facturaActualizar = false;
			if ((this.facturaEmisor == null || "".equals(this.facturaEmisor.trim())) || (facturaFolio == null || "".equals(facturaFolio.trim())))
				this.facturaActualizar = true;

			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.pojoProvisionComprobado.setFecha(formatter.parse(facturaFecha));
			this.pojoProvisionComprobado.setTipoPersonaProveedor(tipoPersona);
			this.pojoProvisionComprobado.setNombreProveedor(this.facturaEmisor);
			this.pojoProvisionComprobado.setFacturaRfc(this.facturaRfc);
			this.pojoProvisionComprobado.setFacturaSerie(respuesta.getBody().getValor("comprobanteSerie").toString());
			this.pojoProvisionComprobado.setFacturaFolio(respuesta.getBody().getValor("comprobanteFolio").toString());
			this.pojoProvisionComprobado.setReferencia(facturaFolio);
			this.subtotalMasImpuestos = saldoFactura;
			// Aplicamos descento
			if (this.facturaDescuento > 0)
				this.facturaSubtotal -= this.facturaDescuento;
			
			// Compruebo el ID de la factura cargada
			if (this.facturaId == null || this.facturaId <= 0L) {
				reiniciaFactura();
				control(-1, "Ocurrio un problema al intentar guardar la factura");
				return;
			}

			getFTPData();
			if (validaRequest("FTP_LOCAL", "1")) {
				this.ftpDigitalizacionUser = "guess";
				this.ftpDigitalizacionPass = "guess";
				log.info("FTP ... " + this.ftpDigitalizacionHost + ":" + this.ftpDigitalizacionPort + ":" + this.ftpDigitalizacionUser + ":" + this.ftpDigitalizacionPass);
			}

			// Subimos fisicamente el archivo
			fileName = this.prefijoFacturas + this.facturaId + ".xml";
			log.info("Credenciales FTP ... ");
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			log.info("Subimos XML ... ");/*this.ftpDigitalizacionRuta*/
			if (! this.ifzFtp.put(this.fileSrc, this.ftpDigitalizacionRuta + fileName)) {
				log.warn("ERROR FTP - Ocurrio un problema al intentar guardar la factura en el servidor");
				control(-1, "Ocurrio un problema al intentar guardar la factura en el servidor");
			}
			log.info("FTP - Carga completa");
			if (emisorDesconocido) 
				control(-1, "El Emisor de la Factura no existe en Negocios/Personas. RFC " + this.facturaRfc);
			desglosaImpuestos();
			nuevaCarga();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar procesar y analizar el archivo cargado (Factura XML)", e);
		}
	}

	public void descargarArchivo() {
		String fileName = "";
		
		try {
			control();
			if (this.reg == null) {
				control(20, "No ha especificado ninguna factura (*.xml)");
				return;
			}

			if (this.reg.getPojoFactura().getIdCfdi() == null || this.reg.getPojoFactura().getIdCfdi() <= 0L)
				this.reg.getPojoFactura().setIdCfdi(0L);

			this.facturaId = this.reg.getPojoFactura().getIdCfdi();
			if (this.facturaId == null || this.facturaId <= 0L) {
				control(20, "No ha especificado ninguna factura (*.xml)");
				return;
			}

			this.facturaEmisor = this.ifzProvisionDetalles.getFacturaProperty(this.facturaId, "nombre");
			if (this.facturaEmisor == null || "".equals(this.facturaEmisor.trim()))
				this.facturaEmisor = "Emisor desconocido";

			this.ftpDigitalizacionHost = this.entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = this.entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = this.entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = this.entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta");
			
			if (validaRequest("FTP_LOCAL", "1")) {
				this.ftpDigitalizacionUser = "guess";
				this.ftpDigitalizacionPass = "guess";
				this.ftpDigitalizacionPort = "22";
				log.info("FTP ... " + this.ftpDigitalizacionHost + ":" + this.ftpDigitalizacionPort + ":" + this.ftpDigitalizacionUser + ":" + this.ftpDigitalizacionPass);
			}
			 
			// Inicializamos variables de archivos y recuperamos el archivo
			fileName = this.prefijoFacturas + this.facturaId + ".xml";
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			this.fileSrc = this.ifzFtp.get(this.ftpDigitalizacionRuta + fileName);
			if (this.fileSrc == null || this.fileSrc.length <= 0) {
				control(21, "No se encontro el archivo en el servidor");
				return;
			}

			// Ponemos los datos en session
			this.httpSession.setAttribute("nombreDocumento", fileName);
			this.httpSession.setAttribute("formato", "xml");
			this.httpSession.setAttribute("contenido", this.fileSrc);
		} catch (Exception e) {
    		control(9, "Ocurrio un problema al intentar Descargar el documento cargado");
		}
	}

	public void borrarXML() {
		try {
			control();
			if (this.facturaDescartada && this.facturaId != null && this.facturaId > 0L) {
				log.info("Eliminando XML " + this.facturaId);
	    		this.ifzProvisionDetalles.eliminarFactura(this.facturaId);
			}
		} catch (Exception e) {
    		log.error("Error en CuentasPorPagar.CajaChicaAction.borrarXML :: No pude borrar el XML " + this.facturaId + " cargado previamentes y que no se uso :(", e);
    		control("No pude borrar el XML " + this.facturaId + " cargado previamente y que no se uso", e);
		}
	}
	
	private void getFTPData() {
		this.ftpDigitalizacionHost = this.entornoProperties.getString("ftp.digitalizacion.host");
		this.ftpDigitalizacionPort = this.entornoProperties.getString("ftp.digitalizacion.port");
		this.ftpDigitalizacionUser = this.entornoProperties.getString("ftp.digitalizacion.user");
		this.ftpDigitalizacionPass = this.entornoProperties.getString("ftp.digitalizacion.password");
		this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta");
	}

	private void reiniciaFactura() {
		this.facturaId = 0L;
		this.facturaRfc = "";
		this.facturaEmisor = "";
		this.facturaTipo = "";
		this.facturaMoneda = "";
		this.facturaTipoCambio = 0D;
		this.facturaDescuento = 0D;
		this.facturaSubtotal = 0D;
		this.facturaTotal = 0D;
		this.facturaDescartada = true;
	}

	private String desglozaComprobados(String comprobados) {
		String resultados = "";
		String[] splitted = null;
		String[] aux = null;
		// --------------------------
		String tipoGasto = "";
		String idTarget = "";
		String monto = "";
		String tipoComprobante = "";
		
		try {
			splitted = comprobados.split(",");
			for (int index = 0; index < splitted.length; index ++) {
				aux = splitted[index].trim().split("\\|");
				tipoGasto = TiposGastoCXP.fromString(aux[0].trim()).name();
				idTarget = aux[1].trim();
				monto = (new DecimalFormat("###,###,##0.00")).format(Double.parseDouble(aux[2].trim()));
				if (aux.length > 3)
					tipoComprobante = aux[3].trim();
				
				if (! "".equals(resultados))
					resultados += "\n";
				
				// TIPO_GASTO ID por $ 0.00 (I)
				resultados += tipoGasto + " " + idTarget + " por $ " + monto;
				if (! "".equals(tipoComprobante))
					resultados += " (" + tipoComprobante + ")";
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al desglozar los registros comprobados", e);
			resultados = comprobados;
			comprobados = resultados.replace(",", "\n");
			comprobados = comprobados.replace("|", " - ");
		} finally {
			resultados = "\n\nComprobaciones previas:\n" + resultados;
		}
		
		return resultados;
	}
	
	private PersonaExt extenderPersona(Persona persona) {
		PersonaExt personaExt =  new PersonaExt();
		String res = "";
		
		res  = persona.getPrimerNombre() 	!= null && persona.getPrimerNombre().length() > 0? persona.getPrimerNombre() : "";
		res += persona.getSegundoNombre()	!= null && persona.getSegundoNombre().length() > 0 ? " " + persona.getSegundoNombre() : "";
		res += persona.getNombresPropios()	!= null && persona.getNombresPropios().length() > 0 ? " " + persona.getNombresPropios() : "";
		res += persona.getPrimerApellido()	!= null && persona.getPrimerApellido().length() > 0 ? " " + persona.getPrimerApellido() : "";
		res += persona.getSegundoApellido()	!= null && persona.getSegundoApellido().length() > 0 ? " " + persona.getSegundoApellido() : "";
		
		personaExt.setId(persona.getId());
		personaExt.setCreadoPor(persona.getCreadoPor());
		personaExt.setFechaCreacion(persona.getFechaCreacion());
		personaExt.setModificadoPor(persona.getModificadoPor());
		personaExt.setFechaModificacion(persona.getFechaModificacion());
		if (persona.getHomonimo() > 0)
			personaExt.setHomonimo(true);
		else
			personaExt.setHomonimo(false);
		personaExt.setNombre(persona.getNombre());
		personaExt.setPrimerNombre(persona.getPrimerNombre());
		personaExt.setSegundoNombre(persona.getSegundoNombre());
		personaExt.setNombresPropios(persona.getNombresPropios());
		personaExt.setPrimerApellido(persona.getPrimerApellido());
		personaExt.setSegundoApellido(persona.getSegundoApellido());
		personaExt.setNombreCompleto(res);
		personaExt.setRfc(persona.getRfc());
		personaExt.setSexo(persona.getSexo());
		personaExt.setFechaNacimiento(persona.getFechaNacimiento());
		if (persona.getEstadoCivil() != null)
			personaExt.setEstadoCivil(this.ifzConValores.findById(persona.getEstadoCivil()));
		if (persona.getLocalidad() != null)
			personaExt.setLocalidad(this.ifzLocalidad.findById(persona.getLocalidad()));
		if (persona.getNacionalidad () != null)
			personaExt.setNacionalidad(this.ifzConValores.findById(persona.getNacionalidad()));
		personaExt.setNumeroHijos(persona.getNumeroHijos());
		personaExt.setDomicilio(persona.getDomicilio());
		personaExt.setTelefono(persona.getTelefono());
		personaExt.setCorreo(persona.getCorreo());
		if (persona.getFinado() > 0) 
			personaExt.setFinado(true);
		else
			personaExt.setFinado(false);
		if (persona.getTipoSangre() != null)
			personaExt.setTipoSangre(this.ifzConValores.findById(persona.getTipoSangre()));
		if (persona.getColonia() != null)
			personaExt.setColonia(this.ifzColonia.findById(persona.getColonia()));
		personaExt.setTipoPersona(persona.getTipoPersona());
		personaExt.setNumeroCuenta(persona.getNumeroCuenta());
		personaExt.setClabeInterbancaria(persona.getClabeInterbancaria());

		return personaExt;
	}

	// ------------------------------------------------------------------------------------------
	// BUSQUEDA OBRAS
	// ------------------------------------------------------------------------------------------
	
	public void obrasBusqueda() {
		control();
		// Inicializaciones
		this.tipoObraBusquedaObra = (int) this.listObrasTiposItems.get(0).getValue();
		this.campoBusquedaObra = this.tipoBusquedaObra.get(0).getValue().toString();
		this.valTipoBusquedaObra = "";
		this.avanzadaObra = false;
		this.paginacionObras = 1;
		
		if (this.listObras == null)
			this.listObras = new ArrayList<Obra>();
		this.listObras.clear();
		
		if (this.pojoObra == null)
			this.pojoObra = new Obra();
	}

	public void buscarObras() {
		try {
			control();
			this.paginacionObras = 1;
			if (this.listObras != null)
				this.listObras.clear();

			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valTipoBusquedaObra, this.tipoObraBusquedaObra, false, "", 0);
			if (this.listObras == null || this.listObras.isEmpty()) 
				control(13, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Obras", e);
		} finally {
			if (this.listObras == null)
				this.listObras = new ArrayList<Obra>();
			log.info(this.listObras.size() + " Obras encontradas");
		}
	}

	public void seleccionarObra() {
		try {
			control();
			// Asignamos la Obra al REGISTRO DE GASTO, si corresponde
			this.pojoProvisionComprobado.setIdObra(this.pojoObra);
			obrasBusqueda();
			ordenCompraBusqueda();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		}
	}
		
	public void cambioObra() {
		try {
			this.tipoObra = 1;
		} catch (Exception e) {
			log.error("error en cambioObra", e);
		}
	}

	public void toggleAvanzadaObra() {
		this.avanzadaObra = ! this.avanzadaObra;
	}

	// ------------------------------------------------------------------------------------------
	// BUSQUEDA ORDEN DE COMPRA
	// ------------------------------------------------------------------------------------------
	
	public void ordenCompraBusqueda() {
		control();
		// Inicializaciones
		this.campoBusquedaOrdenCompra = this.listTiposBusquedaOrdenCompra.get(0).getValue().toString();
		this.valorBusquedaOrdenCompra = "";
		this.busquedaAvanzadaOrdenCompra = false;
		this.paginacionOrdenCompra = 1;
		
		if (this.listOrdenCompra == null)
			this.listOrdenCompra = new ArrayList<OrdenCompra>();
		this.listOrdenCompra.clear();
		this.pojoOrdenCompra = null;

		if (this.pojoProvisionComprobado == null || this.pojoProvisionComprobado.getIdObra() == null || this.pojoProvisionComprobado.getIdObra().getId() == null || this.pojoProvisionComprobado.getIdObra().getId() <= 0L) 
			control(-1, "Debe seleccionar una Obra");
	}
	
	public void ordenCompraBuscar() {
		try {
			control();
			if ("".equals(this.campoBusquedaOrdenCompra))
				this.campoBusquedaOrdenCompra = this.listTiposBusquedaOrdenCompra.get(0).getValue().toString();
			
			this.ifzOrdenCompra.setInfoSesion(this.loginManager.getInfoSesion());
			this.listOrdenCompra = this.ifzOrdenCompra.findLikeProperty(this.campoBusquedaOrdenCompra, this.valorBusquedaOrdenCompra, this.pojoProvisionComprobado.getIdObra().getId(), true, 0);
			if (this.listOrdenCompra == null || this.listOrdenCompra.isEmpty()) 
				control(13, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Ordenes de Compra de la Obra seleccionada", e);
		}
	}
	
	public void ordenCompraSeleccionar() {
		try {
			control();
			if (this.pojoOrdenCompra == null || this.pojoOrdenCompra.getId() == null || this.pojoOrdenCompra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Orden de Compra seleccionada");
				return;
			}
			
			// Asignamos la Orden de Compra al REGISTRO DE GASTO, si corresponde
			this.pojoProvisionComprobado.setIdOrdenCompra(this.pojoOrdenCompra);
			ordenCompraBusqueda();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Orden de Compra seleccionada", e);
		}
	}

	public void toggleBusquedaAvanzadaOrdenCompra() {
		this.busquedaAvanzadaOrdenCompra = ! this.busquedaAvanzadaOrdenCompra;
	}
	
	// ------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------------

	public boolean getPerfilAdministrativo() {
		return perfilEgresos;
	}
	
	public void setPerfilAdministrativo(boolean perfilEgresos) {
		this.perfilEgresos = perfilEgresos;
	}

	public FacturaImpuestos getReg() {
		return reg;
	}

	public void setReg(FacturaImpuestos reg) {
		this.reg = reg;
		editReg();
	}

	public boolean getBloqueado() {
		if (this.pojoProvision != null && this.pojoProvision.getId() != null && this.pojoProvision.getId() > 0L)
			return false;
		return true;
	}
	
	public void setBloqueado(boolean value) {}
	
   	public String getTitulo() {
   		if (this.pojoProvision.getId() != null && this.pojoProvision.getId() > 0L)
   			return "Provision " + this.pojoProvision.getId();
   		return "Nueva Provision";
   	}
   	
   	public void setTitulo(String value) { }

	public String getFacturaDescripcion() {
		String descripcion = "Seleccione factura";
		
		if (this.facturaId != null && this.facturaId > 0L) {
			descripcion = this.facturaId.toString();
			if (this.facturaEmisor != null && ! "".equals(this.facturaEmisor.trim()) && ! "Emisor desconocido".equals(this.facturaEmisor.trim()))
				descripcion += " - " + this.facturaRfc;
			if (this.facturaMoneda != null && "USD".equals(this.facturaMoneda.trim())) {
				descripcion += " - " + this.facturaMoneda;
				if (this.facturaTipoCambio != null && this.facturaTipoCambio > 0D)
					descripcion += " - T.C. " + this.facturaTipoCambio;
			}
		}
		
		return descripcion;
	}
	
	public void setFacturaDescripcion(String value) {}
	
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
		return pojoProvisionComprobado;
	}

	public void setPojoComprobadoGto(PagosGastosDetExt pojoComprobadoGto) {
		this.pojoProvisionComprobado = pojoComprobadoGto;
	}
	
	public String getReferencia() {
		return this.pojoProvisionComprobado.getReferencia() != null ? this.pojoProvisionComprobado.getReferencia() : "";
	}

	public void setReferencia(String referencia) {
		this.pojoProvisionComprobado.setReferencia(referencia);
	}

	public double getSubtotal() {
		return this.pojoProvisionComprobado.getSubtotal() != null ? this.pojoProvisionComprobado.getSubtotal() : 0;
	}

	public void setSubtotal(double subtotal) {
		this.pojoProvisionComprobado.setSubtotal(subtotal);
	}
	
	public Date getFechaCompruebaGto() {
		if (this.pojoProvisionComprobado.getFecha()!= null)
			return this.pojoProvisionComprobado.getFecha();
		return Calendar.getInstance().getTime();
	}

	public void setFechaCompruebaGto(Date fechaCompruebaGto) {
		this.pojoProvisionComprobado.setFecha(fechaCompruebaGto);
	}
	
	public boolean getFacturaCredito() {
		return this.pojoProvisionComprobado.getEsCredito();
	}

	public void setFacturaCredito(boolean facturaCredito) {
		this.pojoProvisionComprobado.setEsCredito(facturaCredito);
	}

	public boolean getManoObra() {
		return this.pojoProvisionComprobado.getManoObra();
	}

	public void setManoObra(boolean manoObra) {
		this.pojoProvisionComprobado.setManoObra(manoObra);
	}
	
	public Long getPagosGastosDetId() {
		return this.pojoProvisionComprobado.getId() != null ? this.pojoProvisionComprobado.getId() : 0;
	}

	public void setPagosGastosDetId(Long pagosGastosDetId) {
		this.pojoProvisionComprobado.setId(pagosGastosDetId);
	}
	
	public Double getTotalImpuestos() {
		return this.pojoProvisionComprobado.getTotalImpuestos() != null ? this.pojoProvisionComprobado.getTotalImpuestos() : 0;
	}

	public void setTotalImpuestos(Double totalImpuestos) {
		this.pojoProvisionComprobado.setTotalImpuestos(totalImpuestos);
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

	public Long getPojoGtosComp() {
		return pojoProvisionMain;
	}

	public void setPojoGtosComp(Long pojoGtosComp) {
		this.pojoProvisionMain = pojoGtosComp;
	}
	
	public String getConcepto() {
		if (this.pojoProvision.getConcepto() != null) 
			return this.pojoProvision.getConcepto();
		return "";
	}

	public void setConcepto(String concepto) {
		this.pojoProvision.setConcepto(concepto);
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
		return this.pojoProvision.getFecha() != null ? this.pojoProvision.getFecha() : Calendar.getInstance().getTime();
	}

	public void setFecha(Date fecha) {
		this.pojoProvision.setFecha(fecha);
	}
		
	public Long getPagosGastosId() {
		return this.pojoProvision.getId() != null ? this.pojoProvision.getId() : 0;
	}

	public void setPagosGastosId(Long pagosGastosId) {
		this.pojoProvision.setId(pagosGastosId);
	}

	public List<Persona> getListBeneficiarios() {
		return listBeneficiarios;
	}

	public void setListBeneficiarios(List<Persona> listBeneficiarios) {
		this.listBeneficiarios = listBeneficiarios;
	}

	public List<CuentaBancaria> getListCuentas() {
		return listCuentas;
	}

	public void setListCuentas(List<CuentaBancaria> listCuentas) {
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

	public double getDescuento() {
		if (this.facturaDescuento == null)
			this.facturaDescuento = 0D;
		return this.facturaDescuento;
	}

	public void setDescuento(double descuento) {
		this.facturaDescuento = descuento;
	}

	public Double getTotalMenosImpuestos() {
		return subtotal;
	}

	public void setTotalMenosImpuestos(Double totalMenosImpuestos) {
		this.subtotal = totalMenosImpuestos;
	}

	public Double getTotalFacturasReportadas() {
		return totalFacturasReportadas;
	}

	public void setTotalFacturasReportadas(Double totalFacturasReportadas) {
		this.totalFacturasReportadas = totalFacturasReportadas;
	}

	public boolean isEncontroMismoGrupo() {
		return operacionCancelada;
	}

	public void setEncontroMismoGrupo(boolean encontroMismoGrupo) {
		this.operacionCancelada = encontroMismoGrupo;
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
	
	public long getSucursal() {
		if (this.pojoProvision != null && this.pojoProvision.getIdSucursal() != null)
			return this.pojoProvision.getIdSucursal().getId();
		return 0L;
	}

	public void setSucursal(long idSucursal) {
		if (idSucursal > 0L && this.pojoProvision != null) {
			for (Sucursal var : this.listSucursales) {
				if (var.getId() != idSucursal)
					continue;
				this.pojoProvision.setIdSucursal(var);
				break;
			}
		}
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

	public boolean getFacturado() {
		return this.facturaConLlave;
	}
	
	public void setFacturado(boolean facturado) {
		this.facturaConLlave = facturado;
	}

	public int getFacturadoAux() {
		return (this.facturaConLlave ? 1 : 0);
	}
	
	public void setFacturadoAux(int facturado) {
		this.facturaConLlave = (facturado == 1);
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valorBusqueda = valTipoBusqueda;
	}

	public String getValTipoBusqueda() {
		return valorBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setListGastos(List<PagosGastos> listGastos) {
		this.listProvisiones = listGastos;
	}

	public List<PagosGastos> getListGastos() {
		return listProvisiones;
	}

	public void setListProvisionesItems(List<PagosGastosHolder> listProvisionesItems) {
		this.listProvisionesItems = listProvisionesItems;
	}

	public List<PagosGastosHolder> getListProvisionesItems() {
		return listProvisionesItems;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public Double getTotImpuestos() {
		return totImpuestos;
	}

	public void setTotImpuestos(Double totImpuestos) {
		this.totImpuestos = totImpuestos;
	}

	public List<GastosImpuestoExt> getListRetDelGasto() {
		return listRetDelGasto;
	}

	public void setListRetDelGasto(List<GastosImpuestoExt> listRetDelGasto) {
		this.listRetDelGasto = listRetDelGasto;
	}

	public Double getFacturaTotal() {
		return facturaTotal;
	}

	public void setFacturaTotal(Double facturaTotal) {
		this.facturaTotal = facturaTotal;
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

	public String generaPoliza() {
		/*try {
			this.pojoGtosComp=ifzGtosComp.findAllById(pojoGtosComp.getId());
			ifzGtosComp.mensajeSaldoCtas(pojoGtosComp, this.pojoGtosComp.getIdCuentaOrigen().getEmpresa().getId());
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return "OK";
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

	public ConValores getPojoConcepto() {
		return pojoConcepto;
	}
	
	public void setPojoConcepto(ConValores pojoConcepto) {
		this.pojoConcepto = pojoConcepto;
	}
	
	public List<SelectItem> getTiposBusquedaConceptos() {
		return tiposBusquedaConceptos;
	}
	
	public void setTiposBusquedaConceptos(List<SelectItem> tiposBusquedaConceptos) {
		this.tiposBusquedaConceptos = tiposBusquedaConceptos;
	}
	
	public String getCampoBusquedaConceptos() {
		return campoBusquedaConceptos;
	}
	
	public void setCampoBusquedaConceptos(String campoBusquedaConceptos) {
		this.campoBusquedaConceptos = campoBusquedaConceptos;
	}
	
	public String getValorBusquedaConceptos() {
		return valorBusquedaConceptos;
	}
	
	public void setValorBusquedaConceptos(String valorBusquedaConceptos) {
		this.valorBusquedaConceptos = valorBusquedaConceptos;
	}
	
	public int getPagBusquedaConceptos() {
		return pagBusquedaConceptos;
	}
	
	public void setPagBusquedaConceptos(int pagBusquedaConceptos) {
		this.pagBusquedaConceptos = pagBusquedaConceptos;
	}
	
	public boolean isAvanzadaConceptos() {
		return avanzadaConceptos;
	}
	
	public void setAvanzadaConceptos(boolean avanzadaConceptos) {
		this.avanzadaConceptos = avanzadaConceptos;
	}

	public String getFacturaEmisor() {
		return facturaEmisor;
	}

	public void setFacturaEmisor(String facturaEmisor) {
		this.facturaEmisor = facturaEmisor;
	}
	
	public String getFacturaMoneda() {
		return facturaMoneda;
	}

	public void setFacturaMoneda(String facturaMoneda) {
		this.facturaMoneda = facturaMoneda;
	}

	public Double getFacturaTipoCambio() {
		return facturaTipoCambio;
	}

	public void setFacturaTipoCambio(Double facturaTipoCambio) {
		this.facturaTipoCambio = facturaTipoCambio;
	}
	
	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}
	
	public int getNumPaginaFact() {
		return numPaginaFact;
	}

	public void setNumPaginaFact(int numPaginaFact) {
		this.numPaginaFact = numPaginaFact;
	}

	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}
	
	public String getObraNombre() {
   		if (this.pojoObra != null && this.pojoObra.getNombre() != null && ! "".equals(this.pojoObra.getNombre()))
   	   		return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
   		return "";
	}
	
   	public void setObraNombre(String value) { }

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
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

	public List<SelectItem> getTipoBusquedaObra() {
		return tipoBusquedaObra;
	}

	public void setTipoBusquedaObra(List<SelectItem> tipoBusquedaObra) {
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

	public int getPaginacionObras() {
		return paginacionObras;
	}

	public void setPaginacionObras(int paginacionObras) {
		this.paginacionObras = paginacionObras;
	}

	public boolean getBuscarAdministrativo() {
		return tipoObraBusquedaObra == 4;
	}
	
	public void setBuscarAdministrativo(boolean tipoObraBusquedaObra) {
		this.tipoObraBusquedaObra = tipoObraBusquedaObra ? 4 : 0;
	}
	
	public boolean isAvanzadaObra() {
		return avanzadaObra;
	}

	public void setAvanzadaObra(boolean avanzadaObra) {
		this.avanzadaObra = avanzadaObra;
	}

	public List<OrdenCompra> getListOrdenCompra() {
		return listOrdenCompra;
	}

	public void setListOrdenCompra(List<OrdenCompra> listOrdenCompra) {
		this.listOrdenCompra = listOrdenCompra;
	}

	public OrdenCompra getPojoOrdenCompra() {
		return pojoOrdenCompra;
	}

	public void setPojoOrdenCompra(OrdenCompra pojoOrdenCompra) {
		this.pojoOrdenCompra = pojoOrdenCompra;
	}

	public String getOrdenCompraDescripcion() {
		if (this.pojoProvisionComprobado.getIdOrdenCompra() != null && this.pojoProvisionComprobado.getIdOrdenCompra().getId()!= null && this.pojoProvisionComprobado.getIdOrdenCompra().getId()> 0L)
			return this.pojoProvisionComprobado.getIdOrdenCompra().getFolio() + " - " + this.pojoProvisionComprobado.getIdOrdenCompra().getNombreProveedor() + " - $ " + String.format("%1$,.2f", this.pojoProvisionComprobado.getIdOrdenCompra().getTotal());
		return "";
	}
	
	public void setOrdenCompraDescripcion(String value) {}

	public List<SelectItem> getListTiposBusquedaOrdenCompra() {
		return listTiposBusquedaOrdenCompra;
	}

	public void setListTiposBusquedaOrdenCompra(
			List<SelectItem> listTiposBusquedaOrdenCompra) {
		this.listTiposBusquedaOrdenCompra = listTiposBusquedaOrdenCompra;
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

	public boolean isBusquedaAvanzadaOrdenCompra() {
		return busquedaAvanzadaOrdenCompra;
	}

	public void setBusquedaAvanzadaOrdenCompra(boolean busquedaAvanzadaOrdenCompra) {
		this.busquedaAvanzadaOrdenCompra = busquedaAvanzadaOrdenCompra;
	}

	public int getPaginacionOrdenCompra() {
		return paginacionOrdenCompra;
	}

	public void setPaginacionOrdenCompra(int paginacionOrdenCompra) {
		this.paginacionOrdenCompra = paginacionOrdenCompra;
	}
}

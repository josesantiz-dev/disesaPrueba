package net.giro.cxp.gastos;

import java.math.BigDecimal;
import java.nio.file.Files;
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
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.logica.GastosImpuestoRem;
import net.giro.cxp.logica.PagosGastosDetImpuestosRem;
import net.giro.cxp.logica.PagosGastosDetRem;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.Utilerias;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class ComprobacionGastosAction {
	private static Logger log = Logger.getLogger(ComprobacionGastosAction.class);
	
	private PagosGastosDetImpuestosExt pojoDesgloImpto;
	private PagosGastosExt pojoReembolso; 
	private PagosGastosExt pojoDevolucion; 
	private Persona pojoProveedores;
	private ConGrupoValores pojoGpoValGasto;
	private ConGrupoValores pojoGpoValPersonas;	
	private PagosGastosExt pojoGtosComp;	
	private PersonaExt pojoBeneficiarios;
	private PagosGastosDetExt pojoComprobadoGto;
	private ConValores pojoConceptoGtos;
	private ConValores pojoNvaRet;
	
	private GastosImpuestoRem ifzGastoImpuesto;
	private ConValoresRem ifzConValores;
	private PagosGastosDetRem ifzComprobadoGto;
	private ConGrupoValoresRem ifzGpoVal;	
	private PagosGastosRem ifzGtosComp;
	private PersonaRem ifzPersonas;
	private ReportesRem ifzReporte;
	private PagosGastosDetImpuestosRem ifzDesgloImpto;
	private FtpRem ifzFtp;
	
	private List<GastosImpuestoExt>	listImpuestosDelGasto;
	private List<PagosGastosDetImpuestosExt> lisImpuestosEliminados;
	private List<PagosGastosDetImpuestosExt> listDesgloseImpuestos;
	private List<PagosGastosDetImpuestosExt> listDesgloseRet;
	private List<Persona> listProveedores;
	private List<ConValores> listConceptoGasto;
	private List<PagosGastosDetExt> listComprobadoGto;
	private List<PersonaExt> listBeneficiarios;
	private List<PagosGastosExt> listGtosComp;	
	private List<String>	listTipoBusqueda;
	private List<ConValores> listRetEncontradas;
	private List<GastosImpuestoExt>	listRetDelGasto;
	
	private String [] listBusqRet;
	
	private InitialContext ctx;
	private LoginManager loginManager;
	
	//private FacesContext facesContext;
	//private HttpSession httpSession;
	
	private Long valGpoGastos;
	//private Long valGpoPersonas;
	
	private Double totalFacturasReportadas;
	private Double subtotalMasImpuestos;
	private Double impto;
	private Double sum_impto;	
	private Double totRetenciones;
	private Double totPago;
	private Double imptoRet;
	private Double sum_imptoRet;	
	private Double totalMenosImpuestos;
	private Double montoComprobado;
	private String msjFinalizarComprobacion;
	private String nombreProveedor;
	private String conceptoGasto;
	private String conceptoGasto2;	
	private String campoBusqueda;	
	private String valTipoBusqueda;
	private String sucursalesVisibles;
	private String valBusqRet;
	private String campoBusqRet;
	private Double totImpuestos;
    	
    private String descripcionFactura;
    private long usuarioId;
	private String usuario;
    
	private HttpSession httpSession;
	//private FacesContext facesContext;
	PropertyResourceBundle entornoProperties;
	private boolean encontroMismoGrupo;
	private boolean existeRetencion;	
	private int numPagina;
	private int numPagina2;
	private int numPagina3;
	private int tipoMensaje;
	private String observaciones;	
	private boolean facturado;
	private String mensaje;
	
	private byte[] fileSrc; 
	//private String fileName;
	//private String fileExtension;
	private Long facturaId;
	private String ftpDigitalizacionHost;
	private String ftpDigitalizacionPort;
	private String ftpDigitalizacionUser;
	private String ftpDigitalizacionPass;
	private String ftpDigitalizacionRuta;
	private String prefijoFacturas;
	
	private String permiteComprobar;
	
	
	public ComprobacionGastosAction() throws NamingException, Exception{
   		FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
		ValueExpression dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		
		this.pojoConceptoGtos = new ConValores();
		this.pojoProveedores = new Persona();		
		this.pojoBeneficiarios = new PersonaExt();
		this.pojoGpoValGasto = new ConGrupoValores();
		this.pojoGpoValPersonas = new ConGrupoValores();
		this.pojoGtosComp = new PagosGastosExt();	
		this.pojoComprobadoGto = new PagosGastosDetExt();
		this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
		this.pojoNvaRet = new ConValores();	
		
		this.lisImpuestosEliminados = new ArrayList<PagosGastosDetImpuestosExt>();
		this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
		this.listDesgloseRet = new ArrayList<PagosGastosDetImpuestosExt>();
		this.listProveedores = new ArrayList<Persona>();
		this.listConceptoGasto = new ArrayList<ConValores>();
		this.listComprobadoGto = new ArrayList<PagosGastosDetExt>();
		this.listGtosComp = new ArrayList<PagosGastosExt>();			
		this.listBeneficiarios = new ArrayList<PersonaExt>();
		this.listTipoBusqueda = new ArrayList<String>();
		this.listImpuestosDelGasto = new ArrayList<GastosImpuestoExt>();
		this.listRetDelGasto = new ArrayList<GastosImpuestoExt>();
		this.listRetEncontradas = new ArrayList<ConValores>();

		this.ctx = new InitialContext();
   		this.ifzPersonas 		= (PersonaRem) 					this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
   		this.ifzGtosComp 		= (PagosGastosRem) 				this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
   		this.ifzComprobadoGto 	= (PagosGastosDetRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
   		this.ifzDesgloImpto 	= (PagosGastosDetImpuestosRem) 	this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
   		this.ifzGastoImpuesto 	= (GastosImpuestoRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
   		this.ifzConValores 		= (ConValoresRem) 				this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
   		this.ifzGpoVal 			= (ConGrupoValoresRem) 			this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
   		this.ifzReporte 		= (ReportesRem) 				this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
		this.ifzFtp 			= (FtpRem) 						this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
   		
		msjFinalizarComprobacion="";
	    
		this.montoComprobado=0D;
		this.subtotalMasImpuestos=0D;
		this.totalFacturasReportadas = 0D;
		this.totRetenciones=0D;
		this.totPago =0D;
		this.numPagina = 1;
		this.numPagina2 = 1;
		this.numPagina3=1;	
		this.tipoMensaje =0;			
		
		this.encontroMismoGrupo=false;
		this.existeRetencion=false;

		listTipoBusqueda.add("Todos");
		listTipoBusqueda.add("Persona");
		listTipoBusqueda.add("Negocio");
	    valTipoBusqueda = listTipoBusqueda.get(0);
		
		listBusqRet = new String[3];
   		listBusqRet[0] = "Clave";
   		listBusqRet[1] = "Descripcion";
   		listBusqRet[2] = "Cuenta Contable";
   		campoBusqRet = listBusqRet[0];
   		
   		valBusqRet = "";

        httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
		this.entornoProperties = (PropertyResourceBundle) dato.getValue(facesContext.getELContext());
		this.loginManager = (LoginManager) ve.getValue(facesContext.getELContext());
		this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
		this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
		//sucursalesVisibles = lm.getLoginBean().getSucursalesVisibles();
		
		this.ifzComprobadoGto.setInfoSecion(this.loginManager.getInfoSesion());

		this.ftpDigitalizacionHost = entornoProperties.getString("ftp.digitalizacion.host");
		this.ftpDigitalizacionPort = entornoProperties.getString("ftp.digitalizacion.port");
		this.ftpDigitalizacionUser = entornoProperties.getString("ftp.digitalizacion.user");
		this.ftpDigitalizacionPass = entornoProperties.getString("ftp.digitalizacion.password");
		this.ftpDigitalizacionRuta = entornoProperties.getString("ftp.digitalizacion.ruta");
		this.prefijoFacturas = "CXC-CG-"; 
		
		/*if (entornoProperties.getString("SYS_RELPER") == null || "".equals(entornoProperties.getString("SYS_RELPER"))) {
			throw new Exception("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
		} else {
			this.valGpoPersonas = Long.valueOf(entornoProperties.getString("SYS_RELPER"));
			this.pojoGpoValPersonas = this.ifzGpoVal.findById(valGpoPersonas);
			
			if (this.pojoGpoValPersonas == null) {
				throw new Exception("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
			}
		}*/
		
		if (this.entornoProperties.getString("SYS_MOVGTOS") == null || "".equals(this.entornoProperties.getString("SYS_MOVGTOS"))) {
			throw new Exception("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");
		} else {
			this.valGpoGastos = Long.valueOf(this.entornoProperties.getString("SYS_MOVGTOS")) ;
			this.pojoGpoValGasto = this.ifzGpoVal.findById(valGpoGastos);
			
			if (this.pojoGpoValGasto == null){
				throw new Exception("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");
			}
		}
		
		//permiteComprobar = "S";
		recuperarPerfiles();
	}
	
	
	public String eliminar(){
		try {
			this.tipoMensaje = 0;
			if( ! this.listDesgloseImpuestos.isEmpty()){
				for(PagosGastosDetImpuestosExt var: listDesgloseImpuestos){
					this.ifzDesgloImpto.delete(var);
				}
			}
			
			this.ifzComprobadoGto.delete(this.pojoComprobadoGto);
			this.listComprobadoGto.remove(this.pojoComprobadoGto);
			
			actualizaTotalFaturas();
		} catch (Exception e) {	
			this.tipoMensaje = 5;
			log.error("Error en el metodo eliminar.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String guardar (){
		boolean  registroNuevo = false;
		Pattern pat = null;
		Matcher match = null;
		
		this.tipoMensaje = 0;	
		encontroMismoGrupo = false;
		
		try{
			if (! "OK".equals(validaMontoFactura()))
				return "ERROR";
			
			if (! "OK".equals(validaMontoImpuestos()))
				return "ERROR";
			
			if (! "OK".equals(validaGrupoImpuestos()))
				return "ERROR";
		
			if (! "OK".equals(validaMontoFacturaContraTotalImpuestos()))
				return "ERROR";
			
			if (this.facturaId == null)
				analizarArchivo();

			if(this.pojoComprobadoGto.getId() == null)
				registroNuevo = true;

			this.pojoComprobadoGto.setModificadoPor(this.usuarioId);
			this.pojoComprobadoGto.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoComprobadoGto.setFacturado((short) (this.facturado ? 1 : 0));
			this.pojoComprobadoGto.setIdXml(this.facturaId);
			
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			
			//validando gasto
			match = pat.matcher(this.conceptoGasto);			
			if(match.find())
				if(registroNuevo)				
					this.pojoConceptoGtos = this.ifzConValores.findById(Long.valueOf(match.group(1)));
				else
					if (this.pojoConceptoGtos.getId() != Long.valueOf(match.group(1)))
						this.pojoConceptoGtos = this.ifzConValores.findById(Long.valueOf(match.group(1)));

			this.pojoComprobadoGto.setIdConcepto(this.pojoConceptoGtos);
				
			//validando proveedor
			/*match = pat.matcher(this.nombreProveedor);			
			if(match.find())
				if(registroNuevo)				
					this.pojoProveedores= this.ifzPersonas.findById(Long.valueOf(match.group(1)));
				else
					if (this.pojoProveedores.getId()!= Long.valueOf(match.group(1)))
						this.pojoProveedores= this.ifzPersonas.findById(Long.valueOf(match.group(1)));
			
			this.pojoComprobadoGto.setProveedorId(this.pojoProveedores);*/
			
			this.pojoComprobadoGto.setIdPagosGastos(this.pojoGtosComp);
			this.pojoComprobadoGto.setSubtotal(totalMenosImpuestos);
			this.pojoComprobadoGto.setTotalRetenciones(totRetenciones);
			this.pojoComprobadoGto.setObservaciones(this.observaciones);
	
			//eliminando los pojos de impuestos de la base de datos, ya que anteriormente solo los eliminaron de la memoria
			if(! this.lisImpuestosEliminados.isEmpty())
				for(PagosGastosDetImpuestosExt var : lisImpuestosEliminados){
					if(var.getId() != null)
						this.ifzDesgloImpto.delete(var);
				}

			if( registroNuevo ){
				this.pojoComprobadoGto.setCreadoPor(this.usuarioId);
				this.pojoComprobadoGto.setFechaCreacion(Calendar.getInstance().getTime());				
				
				this.pojoComprobadoGto.setId(this.ifzComprobadoGto.save(this.pojoComprobadoGto));
							
				this.listComprobadoGto.add(this.pojoComprobadoGto);						
				actualizaTotalFaturas();
				
				if(! this.listDesgloseImpuestos.isEmpty()){
					for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
						var.setIdPagosGastosDet(this.pojoComprobadoGto);
						Long id = this.ifzDesgloImpto.save(var);
						var.setId(id);
					}
				}
			} else {
				this.ifzComprobadoGto.update(this.pojoComprobadoGto);							
				actualizaTotalFaturas();
				
				if(! this.listDesgloseImpuestos.isEmpty()){
					for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
						var.setIdPagosGastosDet(this.pojoComprobadoGto);
						
						if(var.getId() == null) {
							Long id = this.ifzDesgloImpto.save(var);
							var.setId(id);
						} else							
							this.ifzDesgloImpto.update(var);
					}
				}
				
				for(PagosGastosDetExt var : this.listComprobadoGto){
					if(var.getIdPagosGastos().equals(this.pojoComprobadoGto.getIdPagosGastos()))
						var = this.pojoComprobadoGto;
				}								
			}
			
			this.facturaId = null;
		} catch(Exception e) {
			tipoMensaje=5;
			encontroMismoGrupo=true;			
			log.error("error en guardar", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String buscar(){
		try{
			tipoMensaje = 0;
			encontroMismoGrupo = false;
			
			if ("Persona".equals(this.valTipoBusqueda)) {
				this.listBeneficiarios = this.ifzGtosComp.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "P", "G", "'G','C'", 80, null, this.sucursalesVisibles);
			
			} else if ("Negocio".equals(this.valTipoBusqueda)) {
				this.listBeneficiarios = this.ifzGtosComp.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "N", "G", "'G','C'", 80, null, this.sucursalesVisibles);
			
			} else {
				this.listBeneficiarios = this.ifzGtosComp.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "", "G", "'G','C'", 80, null, this.sucursalesVisibles);
			}

			if(listBeneficiarios.isEmpty()){				
				tipoMensaje = 8;
				encontroMismoGrupo = true;	
				campoBusqueda = "";
			}
		} catch(Exception e) {
			this.tipoMensaje=5;
			log.error("error en buscar", e);
			return "ERROR";
		}
		
		return "OK";
	}	
	
	public String buscarRet(){
   		try{
   			this.tipoMensaje=0;
   			this.listRetEncontradas = ifzConValores.findLikeByProperty(this.campoBusqRet,this.valBusqRet, 5);
   			if(listRetEncontradas.isEmpty() || listRetEncontradas==null){
   				this.tipoMensaje= 8;
   			}
   		}catch(Exception e){
   			log.error("Error en metodo buscarRet", e);
   			this.tipoMensaje= 5;
   		}
   		
   		return "OK";
   	}
		
	public String detalles(){		
		try {
			this.tipoMensaje = 0;
			
			String tipoPersona = (this.pojoBeneficiarios.getTipoPersona() == null) ? "N" : (this.pojoBeneficiarios.getTipoPersona() == 1L ? "P" : "N");
			this.listGtosComp = this.ifzGtosComp.findLikeGtosPorComprobarPersonaExt(this.pojoBeneficiarios.getId(), 0L,"G","G", tipoPersona, null, "SI", 0);
			
			// Refrescamos perfiles
			recuperarPerfiles();
		} catch(Exception e) {
			this.tipoMensaje = 5;
			log.error("error en detalles", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	private void recuperarPerfiles() {
		if (this.loginManager != null) {
			this.permiteComprobar = this.loginManager.getAutentificacion().getPerfil("PERMITE_COMPROBAR_GASTOS");
		}
	}
	
	public String detallesComprobado(){
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		
		try{
			tipoMensaje = 0;
			encontroMismoGrupo = false;
			
			if("G".equals(this.pojoGtosComp.getEstatus())){				
				tipoMensaje = 6;
				encontroMismoGrupo = true;
				return "ERROR";
			}

			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idGasto", this.pojoGtosComp.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("REPORTE_VIATICOS_ID"));
			params.put("nombreDocumento", this.entornoProperties.getString("REPORTE_VIATICOS_NOMBRE"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("ftp_host"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.usuario);
			
			System.out.println("hola");
			Respuesta respuesta = this.ifzReporte.ejecutaReporte(params, paramsReporte);
			System.out.println("adios");

			if(respuesta.getErrores().getCodigoError() == 0L){
				this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));	
				this.httpSession.setAttribute("nombreDocumento", respuesta.getBody().getValor("nombreDocumento"));
				this.httpSession.setAttribute("formato", respuesta.getBody().getValor("nombreDocumento"));
			} else
				this.mensaje = respuesta.getErrores().getDescError();
			
			/*HashMap listaParam;
			byte[] datosRep = null;
			PagosGastosExt pojoPagosGastosExt = convertExt.PagosGastosToPagosGastosExt(this.pojoGtosComp);
			listaParam = new HashMap<String, String>();
			listaParam.put("persona_id", pojoPagosGastosExt.getIdBeneficiario().getId());
			//listaParam.put("persona_id", this.pojoGtosComp.getNoBeneficiario().getPersonaId().intValue());
			listaParam.put("gasto_id", this.pojoGtosComp.getId().intValue());
			
			datosRep = ifzReporte.generaReporte("comprobar_gasto_ind.jasper", listaParam);
			httpSession.setAttribute("reporte", datosRep);
			httpSession.setAttribute("tipoReporte", "pdf");*/
		}catch(Exception e){		
			this.tipoMensaje = 5;
			log.error("error en detallesComprobado", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public String agregar(){		
		try{
			this.facturaId 				= 0L;
			this.conceptoGasto			= "";
			this.conceptoGasto2			= "";
			this.nombreProveedor		= "";
			this.descripcionFactura		= "";
			this.encontroMismoGrupo 	= false;
			this.totalMenosImpuestos	= 0D;
			this.subtotalMasImpuestos	= 0D;
			this.totPago				= 0D;
			this.totRetenciones 		= 0D;
			this.totImpuestos 			= 0D;
			this.observaciones			= "";
			
			this.listDesgloseImpuestos.clear();	
			this.listDesgloseRet.clear();
			this.listRetEncontradas.clear();
			
			/*
			PersonaExt proveedor = new PersonaExt();
			Pattern pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			Matcher match = pat.matcher(this.beneficiario);
			
			if(match.find()) {
				proveedor = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona);
			} */
			
			this.pojoComprobadoGto = new PagosGastosDetExt();	
			this.pojoComprobadoGto.setIdProveedor(this.pojoBeneficiarios);
		}catch(Exception e){
			log.error("error en agregar", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String comprobar(){
		try{
			tipoMensaje=0;
			encontroMismoGrupo=false;
			
			if("C".equals(this.pojoGtosComp.getEstatus())){				
				tipoMensaje=7;
				encontroMismoGrupo=true;
				return "ERROR";
			}
			
			this.listComprobadoGto = this.ifzComprobadoGto.findLikePojoCompletoExt(this.pojoGtosComp.getId(), 0);
			actualizaTotalFaturas();
		}catch(Exception e){		
			this.tipoMensaje =5;
			log.error("error en comprobar", e);
			return "ERROR";
		}
	
		return "OK";
	}
	
	public String cerrarComprobante(){
		try{
			//porque si le hicieron cambios al pojo de la factura y no lo salvaron y 
			//vuelven a entrar se queda con los cambios que le hicieron, entonces mejor 
			//que le muestre el pojo tal cual esta en la BD por eso vuelvo a traerlo	
			this.tipoMensaje =0;
			this.listComprobadoGto = this.ifzComprobadoGto.findLikePojoCompletoExt(this.pojoGtosComp.getId(), 0);
			actualizaTotalFaturas();
		}catch(Exception e){	
			this.tipoMensaje =5;
			log.error("error en cerrarComprobante", e);
			return "ERROR";
		}
	
		return "OK";
	}
	
	public String evaluaFinalizar(){
		try{
			tipoMensaje=0;
			encontroMismoGrupo=false;
			
			if("C".equals(this.pojoGtosComp.getEstatus())){				
				tipoMensaje = 7;
				encontroMismoGrupo = true;			
			}
			
			if (this.listComprobadoGto.isEmpty()) {
				this.listComprobadoGto = this.ifzComprobadoGto.findLikePojoCompletoExt(this.pojoGtosComp.getId(), 0);
				actualizaTotalFaturas();
			}
		}catch(Exception e){	
			this.tipoMensaje =5;
			log.error("error en evaluaFinalizar", e);
			return "ERROR";
		}
	
		return "OK";
	}
	
	public String evaluaSiCambioGasto (){
		try{
			this.tipoMensaje =0;
			GeneraListaProveedores();
			this.existeRetencion = true;
			if(! "".equals(this.conceptoGasto2)){
				if(! this.conceptoGasto2.equals(this.conceptoGasto)){
					this.nombreProveedor="";
					this.listDesgloseImpuestos.clear();
					this.listDesgloseRet.clear();
					this.listRetEncontradas.clear();	
					desglosaImpuestos();
					this.conceptoGasto2 = conceptoGasto;
				}
			}
			else{
				conceptoGasto2 =conceptoGasto;
			}	
			
			
		}catch(Exception e){
			this.tipoMensaje =5;
			log.error("error en evaluaSiCambioGasto", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String validaGrupoImpuestos(){
		try{
			this.tipoMensaje = 0;
			this.encontroMismoGrupo = false;
			int contador = 0;
			
			if(! this.listDesgloseImpuestos.isEmpty()){
				for( PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos){
					contador=0;
					for( PagosGastosDetImpuestosExt var2 : this.listDesgloseImpuestos){
						if(!encontroMismoGrupo) {
							if(!"0".equals(var2.getIdImpuesto().getAtributo2())) { //porque el cero es general esos pueden existir N impuestos al mimos tiempo
								if( var1.getIdImpuesto().getAtributo2().equals(var2.getIdImpuesto().getAtributo2())){
									contador = contador + 1;
									if(contador == 2){
										encontroMismoGrupo=true;
										this.tipoMensaje=1;
										return "ERROR";
									}
								}
							}
						}
					}
				}
			}
		} catch(Exception e) {
			this.tipoMensaje =5;
			log.error("error en validaGrupoImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
  
	public String validaMontoImpuestos(){
		try{
			encontroMismoGrupo=false;
			this.tipoMensaje = 0;
			
			if(! this.listDesgloseImpuestos.isEmpty()){
				for( PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos){
					if(var1.getValor() == null || var1.getValor().doubleValue() <= 0){
						encontroMismoGrupo = true;
						this.tipoMensaje=2;
						return "ERROR";
					}  
				}
			}
			
		} catch(Exception e){
			this.tipoMensaje =5;
			log.error("error en validaMontoImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
   	public String validaMontoFactura(){
		try{
			encontroMismoGrupo=false;
			this.tipoMensaje = 0;
			
			if(this.subtotalMasImpuestos <= 0){
				encontroMismoGrupo = true;
				this.tipoMensaje=3;
				return "ERROR";
			}	
		}catch(Exception e){
			this.tipoMensaje =5;
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
			
			if(! this.listDesgloseImpuestos.isEmpty()){
				for( PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos){ 
					if(!"AC".equals(var1.getIdImpuesto().getTipoCuenta()))
						sumaImpuestos = sumaImpuestos + var1.getValor().doubleValue();
				}
			}
			
			if(sumaImpuestos > this.subtotalMasImpuestos){
				encontroMismoGrupo = true;
				this.tipoMensaje=4;
				return "ERROR";
			}
					   
				
		}catch(Exception e){		
			this.tipoMensaje =5;
			log.error("error en validaMontoFacturaContraTotalImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
   	public String desglosaImpuestos(){
   		try {
			this.tipoMensaje = 0;
			this.sum_impto=0D;
			this.impto=0D;
			this.imptoRet=0D;
			this.sum_imptoRet = 0D;
			Pattern pat = null;
   			Matcher match = null;
   			List<PagosGastosDetImpuestosExt> listDesgloseImpuestos_tmp = new ArrayList<PagosGastosDetImpuestosExt>();

   			//validando gasto para que el pojo de gasto tenga valor correcto ya que a veces se quedaba con el valor del ultimo gasto que escogieron en otra factura
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
								
			match = pat.matcher(this.conceptoGasto);			
			if(match.find())									
				this.pojoConceptoGtos= this.ifzConValores.findById(Long.valueOf(match.group(1)));
			
			this.listImpuestosDelGasto=this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","DE", this.pojoConceptoGtos.getId());
			
			if(! this.listImpuestosDelGasto.isEmpty()){
				if (listDesgloseImpuestos.isEmpty()){//si viene vacio es posible que hayan cambiado el concepto de gasto o
					// es la primera vez que se cargaran lo imptos al comprobante
					for( GastosImpuestoExt var : this.listImpuestosDelGasto){
						this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
						this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
						this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
						this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
						this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
				
						impto = Utilerias.redondear(subtotalMasImpuestos - (Utilerias.redondear(this.subtotalMasImpuestos /((Double.valueOf(var.getImpuestoId().getAtributo1())/ 100)+1),2)),2);
						this.pojoDesgloImpto.setValor(BigDecimal.valueOf(impto));		
						sum_impto = Utilerias.redondear(sum_impto + this.pojoDesgloImpto.getValor().doubleValue(), 2);
						this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
					}
					
					this.totImpuestos 		 = sum_impto;
					this.totalMenosImpuestos = Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
					
					//Verifico si existen retenciones para el gasto
					this.listRetDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","AC", this.pojoConceptoGtos.getId());
					if(! this.listRetDelGasto.isEmpty()){
						for(GastosImpuestoExt var: listRetDelGasto){
							pojoDesgloImpto = new PagosGastosDetImpuestosExt();					
							this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
							this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
							this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
							
							impto = Utilerias.redondear(Utilerias.redondear(this.totalMenosImpuestos *((Double.valueOf(var.getImpuestoId().getAtributo1())/ 100)),2),2);
							this.pojoDesgloImpto.setValor(BigDecimal.valueOf(impto));
							
							sum_imptoRet = Utilerias.redondear(sum_imptoRet + this.pojoDesgloImpto.getValor().doubleValue(),2);
							this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
						}
					}
					
					totRetenciones = Utilerias.redondear(sum_imptoRet,2);
					totPago= Utilerias.redondear(subtotalMasImpuestos - totRetenciones,2);
					totImpuestos=sum_impto;
					this.pojoComprobadoGto.setTotalImpuestos(totImpuestos);
					this.totalMenosImpuestos= Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
					/*else{
						this.listRetDelGasto.clear();
						this.listDesgloseRet.clear();
					}*/
				} else { // la lista si tenia elementos y hay que hacer el desglose imptos en base a los imptos que tiene actualmente la lista
					listDesgloseImpuestos_tmp.addAll(listDesgloseImpuestos);					
					this.listDesgloseImpuestos.clear();
					
					for( PagosGastosDetImpuestosExt var : listDesgloseImpuestos_tmp){		
						if("AC".equals(var.getIdImpuesto().getTipoCuenta())){
							var.setValor(BigDecimal.valueOf(0L));
							sum_imptoRet = Utilerias.redondear(sum_imptoRet + var.getValor().doubleValue(), 2);
						} else {
							impto = Utilerias.redondear(subtotalMasImpuestos - (Utilerias.redondear(this.subtotalMasImpuestos /((Double.valueOf(var.getIdImpuesto().getAtributo1())/ 100)+1),2)),2);
							var.setValor(BigDecimal.valueOf(impto));
							sum_impto = Utilerias.redondear(sum_impto + var.getValor().doubleValue(), 2);
						}
						
						this.listDesgloseImpuestos.add(var);
					}
					
					totImpuestos=sum_impto;
					this.pojoComprobadoGto.setTotalImpuestos(totImpuestos);
					totRetenciones = sum_imptoRet;
					this.totalMenosImpuestos= Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
					totPago = Utilerias.redondear(subtotalMasImpuestos - totRetenciones,2);
				}
			} else {
				//porque si no tiene impuestos asociados el gasto, esta propiedad se quedaba NULL y marcaba error
				totImpuestos=0D;
				this.pojoComprobadoGto.setTotalImpuestos(totImpuestos);
				totRetenciones=0D;
				this.totalMenosImpuestos= Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
				totPago = Utilerias.redondear( this.subtotalMasImpuestos - totRetenciones,2);
			}
		} catch(Exception e) {
			this.tipoMensaje = 5;
			log.error("Error en desglosaImpuesto",e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String revisarComprobacionGasto(){
		//HashMap listaParam;
		//byte[] datosRep = null;
		tipoMensaje = 0;
		encontroMismoGrupo = false;
		
		try{
			montoComprobado = 0D;
			//validando el monto comprobado contra el monto del gasto pendiente de comprobar
			for(PagosGastosDetExt var : this.listComprobadoGto){
				this.montoComprobado = this.montoComprobado + (var.getSubtotal() + var.getTotalImpuestos() - var.getTotalRetenciones());
			}
			
			//Si existe saldo a favor se debe realizar un Devolucion Pendiente			
			if((this.pojoGtosComp.getMonto() - this.montoComprobado) > 0) {
				this.pojoDevolucion = new PagosGastosExt();
				
				this.pojoDevolucion.setModificadoPor(this.usuarioId);
				this.pojoDevolucion.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoDevolucion.setCreadoPor(this.usuarioId);
				this.pojoDevolucion.setFechaCreacion(Calendar.getInstance().getTime());
				
				this.pojoDevolucion.setIdSucursal(this.pojoGtosComp.getIdSucursal());
				this.pojoDevolucion.setIdCuentaOrigen(this.pojoGtosComp.getIdCuentaOrigen());
				this.pojoDevolucion.setIdBeneficiario(this.pojoGtosComp.getIdBeneficiario());
				this.pojoDevolucion.setBeneficiario(this.pojoGtosComp.getBeneficiario());
				this.pojoDevolucion.setTipoBeneficiario(this.pojoGtosComp.getTipoBeneficiario());
				
				if("T".equals(this.pojoGtosComp.getOperacion())){								
					this.pojoDevolucion.setIdCuentaDestinoTerceros(this.pojoGtosComp.getIdCuentaDestinoTerceros());				
				}
				
				this.pojoDevolucion.setOperacion(this.pojoGtosComp.getOperacion());
				this.pojoDevolucion.setTipo("D");//devolucion
				this.pojoDevolucion.setEstatus("P");//pendiente
				this.pojoDevolucion.setSegmento(pojoGtosComp.getSegmento() != null ? pojoGtosComp.getSegmento():0);
				this.pojoDevolucion.setFecha(Calendar.getInstance().getTime());
				this.pojoDevolucion.setMonto(Utilerias.redondear((this.pojoGtosComp.getMonto() - this.montoComprobado),2));
				
				/*this.pojoDevolucion.setNoCheque(this.pojoGtosComp.getNoCheque());
				this.pojoDevolucion.setIdPagoMultiple(this.pojoGtosComp.getIdPagoMultiple());
				this.pojoDevolucion.setNota(this.pojoGtosComp.getNota());
				this.pojoDevolucion.setConcepto(this.pojoGtosComp.getConcepto());
				this.pojoDevolucion.setFolioAutorizacion(this.pojoGtosComp.getFolioAutorizacion());
				this.pojoDevolucion.setTipoCambio(this.pojoGtosComp.getTipoCambio());
				this.pojoDevolucion.setIdPagosGastosRef(this.pojoGtosComp.getIdPagosGastosRef());
				this.pojoDevolucion.setIdCuentaOrigenTerceros(this.pojoGtosComp.getIdCuentaOrigenTerceros());
				this.pojoDevolucion.setIdCuentaDestino(this.pojoGtosComp.getIdCuentaDestino());
				this.pojoDevolucion.setIdTiposMovimiento(this.pojoGtosComp.getIdTiposMovimiento());*/
				
				this.pojoDevolucion.setId(this.ifzGtosComp.save(this.pojoDevolucion));
				this.msjFinalizarComprobacion="Devolucion de Sobrante Pendiente";
			}	

			// si existe saldo negativo se debe realizar una Reembolso Pendiente
			if((this.pojoGtosComp.getMonto() - this.montoComprobado) < 0) {
				this.pojoReembolso = new PagosGastosExt();
				
				this.pojoReembolso.setModificadoPor(this.usuarioId);
				this.pojoReembolso.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoReembolso.setCreadoPor(this.usuarioId);
				this.pojoReembolso.setFechaCreacion(Calendar.getInstance().getTime());
				
				this.pojoReembolso.setIdSucursal(this.pojoGtosComp.getIdSucursal());
				this.pojoReembolso.setIdCuentaOrigen(this.pojoGtosComp.getIdCuentaOrigen());
				this.pojoReembolso.setIdBeneficiario(this.pojoGtosComp.getIdBeneficiario());
				this.pojoReembolso.setBeneficiario(this.pojoGtosComp.getBeneficiario());
				this.pojoReembolso.setTipoBeneficiario(this.pojoGtosComp.getTipoBeneficiario());
				
				if("T".equals(this.pojoGtosComp.getOperacion())){								
					this.pojoReembolso.setIdCuentaDestinoTerceros(this.pojoGtosComp.getIdCuentaDestinoTerceros());				
				}
				
				this.pojoReembolso.setOperacion(this.pojoGtosComp.getOperacion());
				this.pojoReembolso.setSegmento(pojoGtosComp.getSegmento() != null ? pojoGtosComp.getSegmento():0);
				this.pojoReembolso.setTipo("R"); // reembolso
				this.pojoReembolso.setEstatus("P"); // pendiente
				
				this.pojoReembolso.setFecha(Calendar.getInstance().getTime());
				this.pojoReembolso.setMonto(Utilerias.redondear((this.pojoGtosComp.getMonto() - this.montoComprobado),2));
				if(this.pojoReembolso.getMonto() < 0)
					this.pojoReembolso.setMonto(this.pojoReembolso.getMonto() * -1);
				
				/*this.pojoReembolso.setNoCheque(this.pojoGtosComp.getNoCheque());
				this.pojoReembolso.setIdPagoMultiple(this.pojoGtosComp.getIdPagoMultiple());
				this.pojoReembolso.setNota(this.pojoGtosComp.getNota());
				this.pojoReembolso.setConcepto(this.pojoGtosComp.getConcepto());
				this.pojoReembolso.setFolioAutorizacion(this.pojoGtosComp.getFolioAutorizacion());
				this.pojoReembolso.setTipoCambio(this.pojoGtosComp.getTipoCambio());
				this.pojoReembolso.setIdPagosGastosRef(this.pojoGtosComp.getIdPagosGastosRef());
				this.pojoReembolso.setIdCuentaOrigenTerceros(this.pojoGtosComp.getIdCuentaOrigenTerceros());
				this.pojoReembolso.setIdCuentaDestino(this.pojoGtosComp.getIdCuentaDestino());
				this.pojoReembolso.setIdTiposMovimiento(this.pojoGtosComp.getIdTiposMovimiento());*/
				
				this.pojoReembolso.setId(this.ifzGtosComp.save(this.pojoReembolso));
				this.msjFinalizarComprobacion="Reembolso Pendiente";
			}
			
			//comprobado	 		
			pojoGtosComp = ifzGtosComp.actualizar(this.pojoGtosComp, "C", Calendar.getInstance().getTime(), Short.valueOf(String.valueOf(this.usuarioId)), null);
			
			for(PagosGastosExt var: listGtosComp){
				if(var.getId().equals(pojoGtosComp.getId())){
					var.setEstatus("C");
					break;
				}					
			}
			
			if ("".equals(this.msjFinalizarComprobacion))
				this.msjFinalizarComprobacion="Finalizo Comprobacion";

			/*listaParam = new HashMap<String, String>();
			listaParam.put("persona_id", this.pojoGtosComp.getIdBeneficiario().intValue());
			//listaParam.put("persona_id", this.pojoGtosComp.getNoBeneficiario().getPersonaId().intValue());
			listaParam.put("gasto_id", this.pojoGtosComp.getId().intValue());
			
			datosRep = ifzReporte.generaReporte("comprobar_gasto_ind.jasper", listaParam);
			httpSession.setAttribute("reporte", datosRep);
			httpSession.setAttribute("tipoReporte", "pdf");*/
		} catch(Exception e) {
			tipoMensaje = 5;
			encontroMismoGrupo = true;
			msjFinalizarComprobacion = "";
			log.error("error en revisarComprobacionGasto", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public List<ConValores> autoacompletaConceptoGasto(Object obj){
		try{
			this.listConceptoGasto = this.ifzConValores.findLikeValorIdPropiedadGrupo(obj.toString() , this.pojoGpoValGasto, 20);
			return this.listConceptoGasto;
		}
		catch(Exception e){
			log.error("error en autoacompletaConceptoGasto", e);
			return new ArrayList<ConValores>();
		}
	}

	public List<Persona> autoacompletaProveedor (Object obj){
		try{
			this.listProveedores = this.ifzPersonas.findLikeProveedor(obj.toString(), this.pojoGpoValPersonas, "PROV", this.pojoConceptoGtos.getId(), 20);
			//this.listProveedores = this.ifzPersonas.findLikeProveedor(obj.toString(), this.pojoGpoValPersonas, "PROV", this.pojoConceptoGtos.getValor(), 20);
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
				this.pojoConceptoGtos= this.ifzConValores.findById(Long.valueOf(match.group(1)));
		}catch(Exception e){
			log.error("error en GeneraListaProveedores", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public String eliminarImpuestoDesglosado(){
		try{
			this.tipoMensaje = 0;
			this.sum_impto=0D;
			this.sum_imptoRet=0D;
			this.impto=0D;
		
			this.lisImpuestosEliminados.add(this.pojoDesgloImpto);
			this.listDesgloseImpuestos.remove(this.pojoDesgloImpto);
			
			if(! this.listDesgloseImpuestos.isEmpty()){				
				for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
					if("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sum_imptoRet = Utilerias.redondear(sum_imptoRet + var.getValor().doubleValue(), 2);	
					else	
						sum_impto = Utilerias.redondear(sum_impto + var.getValor().doubleValue(), 2);	
				}

				this.pojoComprobadoGto.setTotalImpuestos(sum_impto);
				this.pojoComprobadoGto.setTotalRetenciones(sum_imptoRet);
				
				totImpuestos=sum_impto;
				totRetenciones = sum_imptoRet;
				this.totalMenosImpuestos= Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
				this.totPago = Utilerias.redondear( this.subtotalMasImpuestos - sum_imptoRet,2);
				this.pojoComprobadoGto.setSubtotal(this.totalMenosImpuestos);
			} else {
				//sabemos que la lista de imptos quedo vacia porque entro a esta opcion y ponemos en cero el total de impuestos
				this.pojoComprobadoGto.setTotalImpuestos(0D);		
				this.pojoComprobadoGto.setTotalRetenciones(0D);
				this.pojoComprobadoGto.setSubtotal(this.subtotalMasImpuestos);
				totImpuestos=0D;	
				totRetenciones = 0D;
				this.totalMenosImpuestos = this.subtotalMasImpuestos;
				this.totPago = this.subtotalMasImpuestos;
			}
		} catch(Exception e) {
			this.tipoMensaje = 5;
			log.error("error en eliminarImpuestoDesglosado", e);
			return "ERROR";
		}
		
		return "OK";
	}
		
	public String cambioMontoImpuesto(){
		try{
			this.tipoMensaje = 0;
			if(this.subtotalMasImpuestos!=0){
				this.sum_impto = 0D;
				this.sum_imptoRet = 0D;
				
				for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
					if(var.getValor() != null){
						if("AC".equals(var.getIdImpuesto().getTipoCuenta()))
							sum_imptoRet = Utilerias.redondear(sum_imptoRet + var.getValor().doubleValue(),2);
						else
							sum_impto = Utilerias.redondear(sum_impto + var.getValor().doubleValue(),2);
					}
				}
				
				this.totImpuestos = sum_impto;
				this.pojoComprobadoGto.setTotalImpuestos(totImpuestos);
				this.totRetenciones = sum_imptoRet;
				this.totPago = Utilerias.redondear(subtotalMasImpuestos - totRetenciones,2);	
				this.totalMenosImpuestos = Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);	
			}
		}catch(Exception e){
			this.tipoMensaje = 5;
			log.error("error en cambioMontoImpuesto", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String agregaRet(){
		try{
			this.tipoMensaje= 0;
			this.encontroMismoGrupo= false;
			if(!listDesgloseImpuestos.isEmpty()){
				for(PagosGastosDetImpuestosExt var: listDesgloseImpuestos){
					if(var.getIdImpuesto().getId() == this.pojoNvaRet.getId()){
						this.tipoMensaje = 9;
						this.encontroMismoGrupo= true;
						return "OK";
					}
				}
			}
			
			this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
			this.pojoDesgloImpto.setIdImpuesto(this.pojoNvaRet);
			this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
			this.pojoDesgloImpto.setValor(BigDecimal.valueOf(0L));
			this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
			this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
			this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
			this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
			this.listRetEncontradas.clear();
				
		}catch(Exception e){
			this.tipoMensaje = 5;
			log.error("Error en metodo agregaRet",e);
		}

		return "OK";
	}

	public void nuevaCarga() {
		this.fileSrc = null;
		//this.fileName = "";
		//this.fileExtension = "";
	}
	
	public void uploadListener(UploadEvent event) throws Exception {
		try {
			this.encontroMismoGrupo = false;
			this.tipoMensaje = 0;
			this.mensaje = "";
			
			UploadItem item = event.getUploadItem();
			this.fileSrc = ((item.isTempFile()) ? Files.readAllBytes(item.getFile().toPath()) : item.getData());
			//this.fileName = item.getFileName(); 
			//this.fileExtension =  stripExtension(item.getFileName());
		} catch (Exception e) {
			this.encontroMismoGrupo = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en uploadListener", e);
			throw e;
		}
	}
	
	public void analizarArchivo() throws Exception {
		String fileName = "";
		
		try {
			this.encontroMismoGrupo = false;
			this.tipoMensaje = 0;
			this.mensaje = "";
			
			if (this.fileSrc == null) {
				this.facturaId = 0L;
				this.encontroMismoGrupo = true;
				this.tipoMensaje = 1;
				this.mensaje = "No ha especificado ninguna factura (*.xml)";
				log.warn("No ha especificado ninguna factura (*.xml)");
				return;
			} 

			this.facturaId = this.ifzComprobadoGto.analizaFactura(this.fileSrc);
			if (this.facturaId == null || this.facturaId <= 0L) {
				this.facturaId = 0L;
			}

			// Subimos fisicamente el archivo
			fileName = this.prefijoFacturas + this.facturaId + ".xml";
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			if (! this.ifzFtp.putArchivo(this.fileSrc, this.ftpDigitalizacionRuta + fileName)) {
				this.encontroMismoGrupo = true;
				this.tipoMensaje = -1;
				this.mensaje = "Ocurrio un problema al intentar guardar la factura en el servidor";
				log.warn(this.mensaje);
			}
		} catch (Exception e) {
			this.encontroMismoGrupo = true;
			this.tipoMensaje = 1;
			this.mensaje = "No ha especificado ninguna factura (*.xml)";
			log.error("Error en CuentasPorPagar.CajaChicaAction.analizarArchivo()", e);
			throw e;
		}
	}

	public void descargarArchivo() {
		String fileName = "";
		
		try {
			this.encontroMismoGrupo = false;
			this.tipoMensaje = 0;
			this.mensaje = "";
			
			if (this.facturaId == null || this.facturaId <= 0L) {
				this.encontroMismoGrupo = true;
				this.tipoMensaje = 20;
				this.mensaje = "No tiene asignada ninguna factura";
				return;
			}
			 
			// Inicializamos variables de archivos y recuperamos el archivo
			fileName = this.prefijoFacturas + this.facturaId + ".xml";
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			this.fileSrc = this.ifzFtp.getArchivo(this.ftpDigitalizacionRuta + fileName);
			if(this.fileSrc == null || this.fileSrc.length <= 0) {
				this.encontroMismoGrupo = true;
				this.tipoMensaje = 21;
				this.mensaje = "No se encontro el archivo en el servidor";
				return;
			}

			// Ponemos los datos en session
			this.httpSession.setAttribute("nombreDocumento", fileName);
			this.httpSession.setAttribute("formato", "xml");
			this.httpSession.setAttribute("contenido", this.fileSrc);
		} catch (Exception e) {
			this.encontroMismoGrupo = true;
			this.mensaje = "";
			this.tipoMensaje = 9;
    		log.error("Error en CuentasPorPagar.CajaChicaAction.descargarArchivo", e);
		}
	}
	
	public String actualizaTotalFaturas(){
		try{
			this.totalFacturasReportadas = 0D;
			this.tipoMensaje = 0;
			
			if(! listComprobadoGto.isEmpty()){
				for(PagosGastosDetExt var: listComprobadoGto){
					this.totalFacturasReportadas = Utilerias.redondear(this.totalFacturasReportadas + ((var.getSubtotal() + var.getTotalImpuestos())- var.getTotalRetenciones()),2);
				}
			}
		} catch(Exception e) {
			log.error("error en actualizaTotalFaturas", e);
			return "ERROR";
		}
		
		return "OK";
	}
		
	public PagosGastosDetImpuestosExt getPojoDesgloImpto() {
		return pojoDesgloImpto;
	}

	public void setPojoDesgloImpto(PagosGastosDetImpuestosExt pojoDesgloImpto) {
		this.pojoDesgloImpto = pojoDesgloImpto;
	}

	public PagosGastosExt getPojoReembolso() {
		return pojoReembolso;
	}

	public void setPojoReembolso(PagosGastosExt pojoReembolso) {
		this.pojoReembolso = pojoReembolso;
	}

	public PagosGastosExt getPojoDevolucion() {
		return pojoDevolucion;
	}

	public void setPojoDevolucion(PagosGastosExt pojoDevolucion) {
		this.pojoDevolucion = pojoDevolucion;
	}

	public Persona getPojoProveedores() {
		return pojoProveedores;
	}

	public void setPojoProveedores(Persona pojoProveedores) {
		this.pojoProveedores = pojoProveedores;
	}

	public ConGrupoValores getPojoGpoValPersonas() {
		return pojoGpoValPersonas;
	}

	public void setPojoGpoValPersonas(ConGrupoValores pojoGpoValPersonas) {
		this.pojoGpoValPersonas = pojoGpoValPersonas;
	}

	public ConGrupoValores getPojoGpoValGasto() {
		return pojoGpoValGasto;
	}

	public void setPojoGpoValGasto(ConGrupoValores pojoGpoValGasto) {
		this.pojoGpoValGasto = pojoGpoValGasto;
	}

	public ConValores getPojoConceptoGtos() {
		return pojoConceptoGtos;
	}

	public void setPojoConceptoGtos(ConValores pojoConceptoGtos) {
		this.pojoConceptoGtos = pojoConceptoGtos;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public ConGrupoValores getPojoGpoVal() {
		return pojoGpoValPersonas;
	}

	public void setPojoGpoVal(ConGrupoValores pojoGpoVal) {
		this.pojoGpoValPersonas = pojoGpoVal;
	}

	public PagosGastosDetExt getPojoComprobadoGto() {
		return pojoComprobadoGto;
	}

	public void setPojoComprobadoGto(PagosGastosDetExt pojoComprobadoGto) {
		try {
			this.pojoComprobadoGto = pojoComprobadoGto;

			//Persona pojoProveedor = this.ifzPersonas.findById(this.pojoComprobadoGto.getProveedorId());
			//this.nombreProveedor = pojoProveedor.getId() +" - "+ pojoProveedor.getNombre() +" "+ (pojoProveedor.getPrimerApellido() != null ? pojoProveedor.getPrimerApellido() : "") +" "+(pojoProveedor.getSegundoApellido() != null ? pojoProveedor.getSegundoApellido() : "");
			//this.descripcionFactura = this.pojoComprobadoGto.getConceptoId().getDescripcion() + " - " + this.pojoComprobadoGto.getProveedorId().getNombre()+" " + (this.pojoComprobadoGto.getProveedorId().getApellidoMaterno() != null ? this.pojoComprobadoGto.getProveedorId().getApellidoMaterno() : "")+" " +(this.pojoComprobadoGto.getProveedorId().getApellidoPaterno() != null ? this.pojoComprobadoGto.getProveedorId().getApellidoPaterno() :"");
			this.facturaId 				= this.pojoComprobadoGto.getIdXml();
			this.conceptoGasto 			= this.pojoComprobadoGto.getIdConcepto().getId() +" - "+ this.pojoComprobadoGto.getIdConcepto().getDescripcion();
			this.conceptoGasto2 		= this.conceptoGasto;
			this.nombreProveedor 		= this.pojoComprobadoGto.getIdProveedor().getId() +" - "+ this.pojoComprobadoGto.getIdProveedor().getNombre();
			this.descripcionFactura		= this.pojoComprobadoGto.getIdConcepto().getDescripcion() + " - " + this.nombreProveedor; // this.pojoComprobadoGto.getProveedorId().getNombre()+" " + (this.pojoComprobadoGto.getProveedorId().getApellidoMaterno() != null ? this.pojoComprobadoGto.getProveedorId().getApellidoMaterno() : "")+" " +(this.pojoComprobadoGto.getProveedorId().getApellidoPaterno() != null ? this.pojoComprobadoGto.getProveedorId().getApellidoPaterno() :"");
			this.subtotalMasImpuestos 	= Utilerias.redondear(this.pojoComprobadoGto.getSubtotal() + this.pojoComprobadoGto.getTotalImpuestos(),2);
			this.totalMenosImpuestos  	= Utilerias.redondear(this.pojoComprobadoGto.getSubtotal(),2);
			this.totRetenciones 	  	= Utilerias.redondear(this.pojoComprobadoGto.getTotalRetenciones(),2);
			this.totPago 			  	= Utilerias.redondear(this.subtotalMasImpuestos - this.totRetenciones,2);
			this.observaciones			= this.pojoComprobadoGto.getObservaciones();
			
			this.listDesgloseImpuestos 	= this.ifzDesgloImpto.findLikePojoCompletoExt(this.pojoComprobadoGto, 50);
			
			lisImpuestosEliminados.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getReferencia() {
		return this.pojoComprobadoGto.getReferencia() != null ? this.pojoComprobadoGto.getReferencia() : "";
	}

	public void setReferencia(String referencia) {
		this.pojoComprobadoGto.setReferencia(referencia);
	}

	public double getSubtotal() {
		return this.pojoComprobadoGto.getSubtotal() != null ? this.pojoComprobadoGto.getSubtotal() : 0;
	}

	public void setSubtotal(double subtotal) {
					
		this.pojoComprobadoGto.setSubtotal(subtotal);
	}

	public String getConceptoGasto() {
		return conceptoGasto;
	}

	public void setConceptoGasto(String conceptoGasto) {
		this.conceptoGasto = conceptoGasto;
	}

	public Date getFechaCompruebaGto() {
		return this.pojoComprobadoGto.getFecha()!= null ? this.pojoComprobadoGto.getFecha() : Calendar.getInstance().getTime();
	}

	public void setFechaCompruebaGto(Date fechaCompruebaGto) {
		this.pojoComprobadoGto.setFecha(fechaCompruebaGto);
	}
	
	public Long getPagosGastosDetId() {
		return this.pojoComprobadoGto.getId() != null ? this.pojoComprobadoGto.getId() : 0;
	}

	public void setPagosGastosDetId(Long pagosGastosDetId) {
		this.pojoComprobadoGto.setId(pagosGastosDetId);
	}
	
	public Double getTotalImpuestos() {
		return this.pojoComprobadoGto.getTotalImpuestos() != null ? this.pojoComprobadoGto.getTotalImpuestos() : 0;
	}

	public void setTotalImpuestos(Double totalImpuestos) {
		this.pojoComprobadoGto.setTotalImpuestos(totalImpuestos);
	}

	public Double getTotalMenosImpuestos() {
		return totalMenosImpuestos != null ? totalMenosImpuestos : 0;
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

	public PagosGastosExt getPojoGtosComp() {
		return pojoGtosComp;
	}

	public void setPojoGtosComp(PagosGastosExt pojoGtosComp) {
		this.pojoGtosComp = pojoGtosComp;
	}
	
	public Date getFecha() {
		return this.pojoGtosComp.getFecha() != null ? this.pojoGtosComp.getFecha() : Calendar.getInstance().getTime();
	}

	public void setFecha(Date fecha) {
		this.pojoGtosComp.setFecha(fecha);
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
	
	public PersonaExt getPojoBeneficiarios() {
		return pojoBeneficiarios;
	}

	public void setPojoBeneficiarios(PersonaExt pojoBeneficiarios) {
		this.pojoBeneficiarios = pojoBeneficiarios;
	}

	// METODOS DE ACCESO GENERALES
		
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
	
	public List<String> getListTipoBusqueda() {
		return listTipoBusqueda;
	}

	public void setListTipoBusqueda(List<String> listTipoBusqueda) {
		this.listTipoBusqueda = listTipoBusqueda;
	}

	public List<PersonaExt> getListBeneficiarios() {
		return listBeneficiarios;
	}

	public void setListBeneficiarios(List<PersonaExt> listBeneficiarios) {
		this.listBeneficiarios = listBeneficiarios;
	}

	public List<PagosGastosExt> getListGtosComp() {
		return listGtosComp;
	}

	public void setListGtosComp(List<PagosGastosExt> listGtosComp) {
		this.listGtosComp = listGtosComp;
	}

	public List<PagosGastosDetExt> getListComprobadoGto() {
		return listComprobadoGto;
	}

	public void setListComprobadoGto(List<PagosGastosDetExt> listComprobadoGto) {
		this.listComprobadoGto = listComprobadoGto;
	}

	public List<Persona> getListProveedores() {
		return listProveedores;
	}

	public void setListProveedores(List<Persona> listProveedores) {
		this.listProveedores = listProveedores;
	}

	public List<PagosGastosDetImpuestosExt> getListDesgloseImpuestos() {
		return listDesgloseImpuestos;
	}

	public void setListDesgloseImpuestos(List<PagosGastosDetImpuestosExt> listDesgloseImpuestos) {
		this.listDesgloseImpuestos = listDesgloseImpuestos;
	}

	public List<GastosImpuestoExt> getListImpuestosDelGasto() {
		return listImpuestosDelGasto;
	}

	public void setListImpuestosDelGasto(List<GastosImpuestoExt> listImpuestosDelGasto) {
		this.listImpuestosDelGasto = listImpuestosDelGasto;
	}

	public List<PagosGastosDetImpuestosExt> getLisImpuestosEliminados() {
		return lisImpuestosEliminados;
	}

	public void setLisImpuestosEliminados(List<PagosGastosDetImpuestosExt> lisImpuestosEliminados) {
		this.lisImpuestosEliminados = lisImpuestosEliminados;
	}

	public Double getMontoComprobado() {
		return montoComprobado;
	}

	public void setMontoComprobado(Double montoComprobado) {
		this.montoComprobado = montoComprobado;
	}

	public String getMsjFinalizarComprobacion() {
		return msjFinalizarComprobacion;
	}

	public void setMsjFinalizarComprobacion(String msjFinalizarComprobacion) {
		this.msjFinalizarComprobacion = msjFinalizarComprobacion;
	}

	public String getDescripcionFactura() {
		return descripcionFactura;
	}

	public void setDescripcionFactura(String descripcionFactura) {
		this.descripcionFactura = descripcionFactura;
	}

	public Double getSubtotalMasImpuestos() {
		return subtotalMasImpuestos;
	}

	public void setSubtotalMasImpuestos(Double subtotalMasImpuestos) {
		this.subtotalMasImpuestos = subtotalMasImpuestos;
	}

	public boolean isEncontroMismoGrupo() {
		return encontroMismoGrupo;
	}

	public void setEncontroMismoGrupo(boolean encontroMismoGrupo) {
		this.encontroMismoGrupo = encontroMismoGrupo;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getSucursalesVisibles() {
		return sucursalesVisibles;
	}

	public void setSucursalesVisibles(String sucursalesVisibles) {
		this.sucursalesVisibles = sucursalesVisibles;
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

	public List<ConValores> getListRetEncontradas() {
		return listRetEncontradas;
	}

	public void setListRetEncontradas(List<ConValores> listRetEncontradas) {
		this.listRetEncontradas = listRetEncontradas;
	}

	public List<GastosImpuestoExt> getListRetDelGasto() {
		return listRetDelGasto;
	}

	public void setListRetDelGasto(List<GastosImpuestoExt> listRetDelGasto) {
		this.listRetDelGasto = listRetDelGasto;
	}

	public List<PagosGastosDetImpuestosExt> getListDesgloseRet() {
		return listDesgloseRet;
	}

	public void setListDesgloseRet(List<PagosGastosDetImpuestosExt> listDesgloseRet) {
		this.listDesgloseRet = listDesgloseRet;
	}

	public Double getTotImpuestos() {
		return totImpuestos;
	}

	public void setTotImpuestos(Double totImpuestos) {
		this.totImpuestos = totImpuestos;
	}

	public ConValores getPojoNvaRet() {
		return pojoNvaRet;
	}

	public void setPojoNvaRet(ConValores pojoNvaRet) {
		this.pojoNvaRet = pojoNvaRet;
	}

	public boolean isExisteRetencion() {
		return existeRetencion;
	}

	public void setExisteRetencion(boolean existeRetencion) {
		this.existeRetencion = existeRetencion;
	}

	public String[] getListBusqRet() {
		return listBusqRet;
	}

	public void setListBusqRet(String[] listBusqRet) {
		this.listBusqRet = listBusqRet;
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

	public int getNumPagina2() {
		return numPagina2;
	}

	public void setNumPagina2(int numPagina2) {
		this.numPagina2 = numPagina2;
	}

	public int getNumPagina3() {
		return numPagina3;
	}

	public void setNumPagina3(int numPagina3) {
		this.numPagina3 = numPagina3;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public boolean isFacturado() {
		return facturado;
	}
	
	public void setFacturado(boolean facturado) {
		this.facturado = facturado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getFacturaId() {
		if (this.facturaId != null && this.facturaId > 0L)
			return this.facturaId.toString();
		return "";
	}
	
	public void setFacturaId(String value) {}

	public String getPermiteComprobar() {
		return permiteComprobar;
	}

	public void setPermiteComprobar(String permiteComprobar) {
		this.permiteComprobar = permiteComprobar;
	}
}

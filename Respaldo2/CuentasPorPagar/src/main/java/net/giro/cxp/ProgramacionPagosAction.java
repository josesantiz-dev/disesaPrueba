package net.giro.cxp;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.ProgPagosExt;
import net.giro.cxp.beans.ProgPagosDetalleExt;
import net.giro.cxp.beans.SucursalExt;
import net.giro.cxp.logica.ProgPagosDetalleRem;
import net.giro.cxp.logica.ProgPagosRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;

import org.apache.log4j.Logger;

public class ProgramacionPagosAction {
	private static Logger log = Logger.getLogger(ProgramacionPagosAction.class);
	private FacesContext facesContext;	
	private LoginManager loginManager;
	private InitialContext ctx;
	private HashMap <Integer, String> mensajesInf;
	
	private ProgPagosExt pojoProgPagos;
	private ProgPagosDetalleExt pojoProPagosDet;
	private SucursalExt pojoSucursal;
	private PersonaExt pojoBeneficiarios; //private Empresa pojoEmpresa;
	private ConValores pojoConceptoGtos;
	private ConGrupoValores pojoGpoValPersonas;
	private ConGrupoValores pojoGpoValGasto;
	private ProgPagosRem ifzProgPagos;
	private ProgPagosDetalleRem ifzProPagosDet;
	private SucursalesRem ifzSucursal;
	private ConValoresRem ifzConValores;
	private ConGrupoValoresRem ifzGpoVal;
	private PersonaRem ifzPersonas; // private EmpresasRem ifzCatEmpresas;
	private List<ProgPagosExt> listProgPagos;
	private List<Sucursal> listSucursales;
	private List<Sucursal> listSucursalesBusqueda;
	private List<ConValores> listConceptoGasto;
	private List<ProgPagosDetalleExt> listGastos;
	private List<ProgPagosDetalleExt> listGastosEditar;
	private List<ProgPagosDetalleExt> listGastosEditarAnt;
	private List<ProgPagosDetalleExt> listGastosEliminados;
	private List<Persona> listBeneficiarios; 
	private List<SelectItem> listSucursalesItems;
	private List<SelectItem> listSucursalesItemsBusqueda;
	private Double totalGastos;
	private Double monto;
	private Double total;
	private Double montoRevisado;
	private String detallesProgramacion;
	private String conceptoGasto;
	private String sucursal;
	private String sucursalBusqueda;
	private String resOperacion;
	private String valTipoBusqueda;
	private String tipoPersona;
	private String beneficiario; // empresa;
	private String nombreBeneficiario;
	private long usuarioId; 
	private int numPagina;
	private int numPaginagasto;	
	private boolean clicNuevo;
	private SimpleDateFormat formato;	
	// Busqueda Orden Compra
	private OrdenCompraRem ifzOrdenesCompra;
	private List<OrdenCompra> listBusquedaOrdenesCompra;
	private OrdenCompra pojoOrdenCompraSeleccionada;
	private List<String> tiposBusquedaOrdenesCompra;
	private String campoBusquedaOrdenesCompra;
	private String valorBusquedaOrdenesCompra;
	private int paginaBusquedaOrdenesCompra;
	private boolean band;
	private int tipoMensaje;
	
	
	public ProgramacionPagosAction() {
		Application app = null;
		ValueExpression valExp = null;
		PropertyResourceBundle plataformaProperties = null;
		
		try {
			this.facesContext = FacesContext.getCurrentInstance();
			app = this.facesContext.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(this.facesContext.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(this.facesContext.getELContext());
			
			this.pojoProgPagos = new ProgPagosExt();
			this.pojoProPagosDet = new ProgPagosDetalleExt();
			this.pojoConceptoGtos = new ConValores();
			this.pojoSucursal = new SucursalExt();
			this.pojoGpoValGasto = new ConGrupoValores();
			this.pojoGpoValPersonas = new ConGrupoValores();
			this.pojoBeneficiarios= new PersonaExt();
			this.listProgPagos = new ArrayList<ProgPagosExt>();
			this.listConceptoGasto = new ArrayList<ConValores>();
			this.listSucursales = new ArrayList<Sucursal>();
			this.listGastos = new ArrayList<ProgPagosDetalleExt>();
			this.listGastosEditar = new ArrayList<ProgPagosDetalleExt>();
			this.listGastosEditarAnt = new ArrayList<ProgPagosDetalleExt>();
			this.listGastosEliminados = new ArrayList<ProgPagosDetalleExt>();
			this.listBeneficiarios = new ArrayList<Persona>(); 
			
			this.mensajesInf = new HashMap<Integer, String>();
			this.formato = new SimpleDateFormat("dd/MMMMM/yyyy");

			this.ctx = new InitialContext();
			this.ifzProgPagos 	= (ProgPagosRem) 		this.ctx.lookup("ejb:/Logica_CuentasPorPagar//ProgPagosFac!net.giro.cxp.logica.ProgPagosRem");
			this.ifzProPagosDet = (ProgPagosDetalleRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//ProgPagosDetalleFac!net.giro.cxp.logica.ProgPagosDetalleRem");
			this.ifzConValores 	= (ConValoresRem) 		this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGpoVal 		= (ConGrupoValoresRem) 	this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzSucursal 	= (SucursalesRem)  		this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzPersonas 	= (PersonaRem) 			this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzOrdenesCompra = (OrdenCompraRem)	this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			
			this.ifzProgPagos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProPagosDet.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenesCompra.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.ifzOrdenesCompra.showSystemOuts(false);
			
			this.totalGastos=0D;
			this.numPaginagasto=1;		
			this.resOperacion="";
			this.valTipoBusqueda="Clave";
			this.numPagina=1;
			this.clicNuevo = false;
			this.tipoPersona = "P";
			this.sucursal = "";
			
			this.usuarioId = loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			//sucursalesVisibles = lm.getLoginBean().getSucursalesVisibles();
			
			// MOVIMIENTOS GASTOS
			this.pojoGpoValGasto = this.ifzGpoVal.findByName("SYS_MOVGTOS");
			if (this.pojoGpoValGasto == null || this.pojoGpoValGasto.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");
			
			valExp = app.getExpressionFactory().createValueExpression(this.facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			plataformaProperties = (PropertyResourceBundle) valExp.getValue(this.facesContext.getELContext());
			this.mensajesInf.put(-1, plataformaProperties.getString("mensaje.error.inesperado"));
			this.mensajesInf.put(1,  plataformaProperties.getString("mensaje.info.noEliminarProgPag"));
			this.mensajesInf.put(2,  plataformaProperties.getString("mensaje.info.noEditarProgPag"));			
			this.mensajesInf.put(3,  plataformaProperties.getString("mensaje.info.busquedaVacia"));
			this.mensajesInf.put(4,  plataformaProperties.getString("mensaje.info.faltanGastos"));
			this.mensajesInf.put(5,  plataformaProperties.getString("mensaje.info.errorFechas"));
			this.mensajesInf.put(6,  plataformaProperties.getString("navegacion.label.montoInvalido"));
			
			this.tiposBusquedaOrdenesCompra = new ArrayList<String>();
			this.tiposBusquedaOrdenesCompra.add("Clave");
			this.tiposBusquedaOrdenesCompra.add("Obra");
			this.tiposBusquedaOrdenesCompra.add("Proveedor");
			this.valorBusquedaOrdenesCompra = "";
			this.paginaBusquedaOrdenesCompra = 1;
			
			cargarSucursales();
		} catch (Exception e) {
			log.error("Error en constructor CuentasPorPagar.ProgramacionPagosAction.", e);
		}
	}
	
	
	public void buscar() {
		Pattern pat = null;
		Matcher match = null;	
		
		try {
			control();
			this.sucursal = "";
			this.clicNuevo = false;
			this.listProgPagos.clear();
			
			if (this.pojoSucursal != null)
				this.pojoSucursal = null;
			
			//validando sucursal
			pat = Pattern.compile("(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.sucursalBusqueda);
			if(match.find())
			   this.pojoSucursal = this.ifzProgPagos.findSucursalById(Long.valueOf(match.group(1)));

			this.listProgPagos = this.ifzProgPagos.findByProgramaciones(this.pojoSucursal.getId(), 1000);
			if (listProgPagos.isEmpty()) {				
				this.resOperacion = mensajesInf.get(3);
			}
		} catch (Exception e) {
			log.error("error al buscar", e);
			control(true);
		}
    }
	
	public void nuevo() {
		try {
			control();
			this.clicNuevo = true;
			this.pojoProgPagos 		= new ProgPagosExt();				
			this.pojoSucursal 		= new SucursalExt();
			this.pojoBeneficiarios 	= new PersonaExt();
							
			this.total			= 0D;
			this.montoRevisado 	= 0D;
			this.monto 			= 0D;				
			this.totalGastos	= 0D;
			//this.sucursal 		= "";
			this.beneficiario 	= ""; // empresa="";
			this.tipoPersona 	= "P";
			this.conceptoGasto 	= "";
			this.listGastos 	= new ArrayList<ProgPagosDetalleExt>();				
		} catch (Exception e) {
			log.error("Error en el metodo nuevo.", e);
			control(true);
		}
	}
	
	public void editar() {
		try {
			control();
			this.clicNuevo = false;
			this.sucursal = this.pojoProgPagos.getAgente().getId() + " - " + this.pojoProgPagos.getAgente().getSucursal();
			
			this.beneficiario = "";
			/*if(this.pojoProgPagos.getBeneficiarioId() != null){
				this.beneficiario = this.pojoProgPagos.getBeneficiarioId().getId() + " - " + this.pojoProgPagos.getBeneficiarioId().getNombre();//empresa = this.pojoProgPagos.getEmpresaId().getId()+ " - " +this.pojoProgPagos.getEmpresaId().getEmpresa();
			}*/
			/*if(this.pojoProgPagos.getEmpresaId() != null){
				this.beneficiario = this.pojoProgPagos.getEmpresaId().getId() + " - " + this.pojoProgPagos.getEmpresaId().getEmpresa();//empresa = this.pojoProgPagos.getEmpresaId().getId()+ " - " +this.pojoProgPagos.getEmpresaId().getEmpresa();
			}*/
			this.total = pojoProgPagos.getTotal().doubleValue();
			this.montoRevisado = pojoProgPagos.getMontoRevisado().doubleValue();
			this.detallesProgramacion = "Programación de Pagos  del  " + formato.format(pojoProgPagos.getDel())+ " al  "+ formato.format(pojoProgPagos.getAl()) + "  Sucursal:"+pojoProgPagos.getAgente().getSucursal(); ;
			this.totalGastos = pojoProgPagos.getTotal().doubleValue();
			
			this.listGastos = this.ifzProPagosDet.findByPropertyPojoCompletoMontoNoCero("progPagos.id", this.pojoProgPagos.getId());
			
			this.clicNuevo = "P".equals(this.pojoProgPagos.getEstatus());
		} catch (Exception e) {
			log.error("Error en el metodo editar.", e);
			control(true);
		}
	}

	public void editarGasto() {
		try {
			control();
			this.listGastosEditarAnt.clear();
			this.listGastosEditar = this.ifzProPagosDet.findByPropertyPojoCompletoMontoNoCero("progPagos.id", this.pojoProgPagos.getId());
			for (ProgPagosDetalleExt var : this.listGastosEditar) {
				ProgPagosDetalleExt tmpPPD = var.clone();
				if (tmpPPD.getMonto().doubleValue() > 0)
					this.listGastosEditarAnt.add(tmpPPD);
			} 
		} catch (Exception e) {			
			log.error("Error en el metodo editarGasto.", e);
			control(true);
		}
	}
	
	public void editarGastoDet() {
		Double montoAnt = 0D;
		Double montoAct = 0D;
		Double difMonto = 0D;
		
		try {
			control();
			if (this.listGastosEditar.size() == this.listGastosEditarAnt.size()){
				for (ProgPagosDetalleExt var : this.listGastosEditar)
					montoAct += var.getMonto().doubleValue();
				
				for (ProgPagosDetalleExt var : this.listGastosEditarAnt){
					if (var.getMontoOriginal() == null || var.getMontoOriginal().doubleValue() == 0)
						montoAnt += var.getMonto().doubleValue();
					else
						montoAnt += var.getMontoOriginal().doubleValue();
					//montoAnt += var.getMontoOriginal() == null ? var.getMonto().doubleValue() : var.getMontoOriginal().doubleValue();
				}
				
				if ((montoAct <= montoAnt)) {
					for (ProgPagosDetalleExt var : this.listGastosEditar){
						ProgPagosDetalleExt tmp = this.ifzProPagosDet.findById(var.getId());
						var.setMontoOriginal(tmp.getMontoOriginal() == null ? tmp.getMonto() : tmp.getMontoOriginal());
						var.setMonto(var.getMonto());
						var.setMontoRev(var.getMonto());
						var.setModificadoPor(this.usuarioId);
						var.setRestar(1L);
						var.setFechaModificacion(Calendar.getInstance().getTime());
						this.ifzProPagosDet.update(var);
						/*if(var.getControl() != null){
							listFinFdoRva = ifzFinFdoRva.findByProperty("contrato", var.getControl().getControl(), sucursalesVisibles);
							if(listFinFdoRva != null && !listFinFdoRva.isEmpty()){
								FiniquitoFondoReserva fin = listFinFdoRva.get(0);
								if(fin.getMonto().compareTo(BigDecimal.valueOf(var.getMonto())) > 0){
									fin.setMonto(BigDecimal.valueOf(var.getMonto()));
									ifzFinFdoRva.update(fin);
								}
							}
						}*/
					}
					
					this.pojoProgPagos.setModificadoPor(this.usuarioId);
					this.pojoProgPagos.setFechaModificacion(Calendar.getInstance().getTime());
					this.pojoProgPagos.setTotal(new BigDecimal(montoAct));
					this.pojoProgPagos.setMontoRevisado(new BigDecimal(montoAct));
					this.ifzProgPagos.update(this.pojoProgPagos);
				} else {
					control("Cambio de monto, mayor al permitido !!");
					return;
				}
			} else {
				/////////////////////// Si se borro un gasto, ponerlo en ceros para restar el monto
				for (ProgPagosDetalleExt varMonto : this.listGastosEditar){
					montoAct += varMonto.getMonto().doubleValue();
				}
				
				for (ProgPagosDetalleExt var1 : this.listGastosEditarAnt){
					for (ProgPagosDetalleExt var2 : this.listGastosEditar){
						if(var1.getId() == var2.getId()){
							difMonto += Math.abs(var1.getMonto().doubleValue() - var2.getMonto().doubleValue());
						}
					}	
				}
				
				if (difMonto <= 200) {
					for (ProgPagosDetalleExt var3 : this.listGastosEditarAnt){
						ProgPagosDetalleExt tmp = this.ifzProPagosDet.findById(var3.getId());
						var3.setMontoOriginal(tmp.getMontoOriginal() == null ? tmp.getMonto() : tmp.getMontoOriginal());
						int ban = 0;
						for (ProgPagosDetalleExt var4 : this.listGastosEditar){
							if(var3.getId() == var4.getId()){
								ban = 1;
							}
						}

						if (ban == 0) {
							var3.setMonto(new BigDecimal(0L));
							var3.setMontoRev(new BigDecimal(0L));
						} else {
							var3.setMonto(var3.getMonto());
							var3.setMontoRev(var3.getMonto());
						}
						
						var3.setModificadoPor(this.usuarioId);
						var3.setRestar(1L);
						var3.setFechaModificacion(Calendar.getInstance().getTime());
						this.ifzProPagosDet.update(var3);
					}
					
					this.pojoProgPagos.setModificadoPor(this.usuarioId);
					this.pojoProgPagos.setFechaModificacion(Calendar.getInstance().getTime());
					this.pojoProgPagos.setTotal(new BigDecimal(montoAct));
					this.pojoProgPagos.setMontoRevisado(new BigDecimal(montoAct));
					this.ifzProgPagos.update(this.pojoProgPagos);
				} else {
					control("Cambio de monto, mayor al permitido !!");
					return;
				}
			}
		} catch (Exception e) {
			log.error("Error en el metodo editarGastoDet.", e);
			control(true);
		}
	}
	
	public void guardar() {
		Pattern pat = null;
		Matcher match = null;
		boolean registroNuevo = false;
		
		try {
			control();
			this.resOperacion = "";
			if (this.pojoProgPagos.getId() == null)
				registroNuevo=true;
			
			if (! "OK".equals(revisarEstatusEditar()))
				return;
			
			if (this.pojoProgPagos.getDel().getTime() > this.pojoProgPagos.getAl().getTime()) {
				control(this.mensajesInf.get(5));
				return;
			}
			
			if (this.totalGastos == null || this.totalGastos == 0) {
				control(this.mensajesInf.get(4));
				return;
			}
			
			//validando sucursal
			pat = Pattern.compile("(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.sucursal);
			if (match.find()) {
				if (registroNuevo) {
					this.pojoSucursal = this.ifzProgPagos.findSucursalById(Long.valueOf(match.group(1))); 
				} else {
					if(this.pojoSucursal.getId() != Long.valueOf(match.group(1)))
						this.pojoSucursal = this.ifzProgPagos.findSucursalById(Long.valueOf(match.group(1))); 
				}
			}
			/*match = pat.matcher(this.sucursal);
			if(match.find())
				if(registroNuevo)
					this.pojoSucursal=this.ifzSucursal.findById(Long.valueOf(match.group(1)));
				else
					if(this.pojoSucursal.getId() != Long.valueOf(match.group(1)))
						this.pojoSucursal=this.ifzSucursal.findById(Long.valueOf(match.group(1)));
			
			this.pojoProgPagos.setAgente(this.pojoSucursal);*/

			//validando beneficiario
			/*match=pat.matcher(this.beneficiario);
			if(match.find())
				if(registroNuevo)
					this.pojoBeneficiarios = this.ifzProgPagos.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				else
					if(this.pojoBeneficiarios.getId() != Long.valueOf(match.group(1)))
						this.pojoBeneficiarios = this.ifzProgPagos.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
			
			this.pojoProgPagos.setBeneficiarioId(this.pojoBeneficiarios);
			this.pojoProgPagos.setTipoBeneficiario(this.tipoPersona);
			this.nombreBeneficiario = this.pojoBeneficiarios.getNombre() + " " + (this.pojoBeneficiarios.getPrimerApellido() != null ? this.pojoBeneficiarios.getPrimerApellido() : "") + " " +  (this.pojoBeneficiarios.getSegundoApellido() != null ? this.pojoBeneficiarios.getSegundoApellido(): "");
			*/
			//validando empresa
			/*match = pat.matcher(this.beneficiario); // this.empresa);
			if(match.find())
				if(registroNuevo)
					this.pojoEmpresa = this.ifzCatEmpresas.findById(Long.valueOf(match.group(1)));
				else
					if(this.pojoEmpresa == null || this.pojoEmpresa.getId() != Long.valueOf(match.group(1)))
						this.pojoEmpresa = this.ifzCatEmpresas.findById(Long.valueOf(match.group(1)));
			
			this.pojoProgPagos.setEmpresaId(this.pojoEmpresa);*/

			this.pojoProgPagos.setAgente(this.pojoSucursal);
			this.pojoProgPagos.setTotal(new BigDecimal(total));
			this.pojoProgPagos.setMontoRevisado(new BigDecimal(montoRevisado));	
			this.pojoProgPagos.setRevisadoPor(this.usuarioId);
			this.pojoProgPagos.setModificadoPor(this.usuarioId);
			this.pojoProgPagos.setFechaModificacion(Calendar.getInstance().getTime());

			if (registroNuevo) {			
				this.pojoProgPagos.setCreadoPor(this.usuarioId);
				this.pojoProgPagos.setFechaCreacion(Calendar.getInstance().getTime());				
				this.pojoProgPagos.setEstatus("P");//programado

				// Guardamos la programacion de pagos
				this.pojoProgPagos.setId(this.ifzProgPagos.save(this.pojoProgPagos));	
				
				//guardado los gastos
				for(ProgPagosDetalleExt var: this.listGastos){
					if(var.getId() == null){
						if (var.getMontoRev() == null)
							var.setMontoRev(var.getMonto());
						
						// Guardamos el detalle de la progracion de pagos
						var.setProgPagos(this.pojoProgPagos);
						this.ifzProPagosDet.save(var);
					}
				}
				
				this.listProgPagos.add(this.pojoProgPagos);
			} else {
				//guardado los gastos
				for(ProgPagosDetalleExt var : this.listGastos){
					if (var.getId() == null) {
						if (var.getMontoRev()== null)
							var.setMontoRev(var.getMonto());
						var.setProgPagos(this.pojoProgPagos);
						
						// Guardamos el detalle de la programacion de pagos
						this.ifzProPagosDet.save(var);
					} else {
						// Actualizamos el detalle de la programacion de pagos
						this.ifzProPagosDet.update(var);
					}
				}
				
				// Actualizamos la programacion de pagos
				this.ifzProgPagos.update(this.pojoProgPagos);
				
				for (ProgPagosExt var : this.listProgPagos) {
					if (var.getId() == this.pojoProgPagos.getId()) {
						var = this.pojoProgPagos;
						break;
					}
				}
			}
			
			//eliminando los gastos de la BD
			for (ProgPagosDetalleExt var : this.listGastosEliminados) {
				if (var.getId() != null)
					this.ifzProPagosDet.delete(var);
			}
			
			this.listGastosEliminados.clear();
		} catch(Exception e) {
			log.error("error en guardar", e);
			control(true);
		} finally {
			this.pojoSucursal = null;
		}
	}
	
	public void eliminar(){
		try {
			control();
			this.listGastos = this.ifzProPagosDet.findByPropertyPojoCompletoMontoNoCero("progPagos", this.pojoProgPagos.getId());
			for (ProgPagosDetalleExt var : this.listGastos) {
				if (var.getId() != null)
					this.ifzProPagosDet.delete(var);
			}
			
			this.ifzProgPagos.delete(this.pojoProgPagos);
			this.listProgPagos.remove(this.pojoProgPagos);
		} catch (Exception e) {			
			log.error("Error en el metodo eliminar.", e);
			control(true);
		}
	}
	
	public void agregarGasto(){
		Pattern pat = null;
		Matcher match = null;
		
		try {
			this.resOperacion = "";
			if (! "OK".equals(revisarEstatusEditar())) {
				control(true);
				return;
			}
			
			if (monto == null || monto <= 0) {
				control(this.mensajesInf.get(6));
				return;
			}
			
			//validando gasto
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.conceptoGasto);			
			if(match.find())
				this.pojoConceptoGtos = this.ifzConValores.findById(Long.valueOf(match.group(1)));
			
			//validando beneficiario
			match = pat.matcher(this.beneficiario);
			if(match.find()) {
				this.pojoBeneficiarios = this.ifzProgPagos.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona);
			}
			
			if (this.pojoConceptoGtos != null && this.pojoBeneficiarios != null) {
				this.pojoProPagosDet = new ProgPagosDetalleExt();	
				this.pojoProPagosDet.setTiposGastos(this.pojoConceptoGtos);
				this.pojoProPagosDet.setIdBeneficiario(this.pojoBeneficiarios);
				this.pojoProPagosDet.setTipoBeneficiario(this.tipoPersona);
				this.pojoProPagosDet.setMonto(new BigDecimal(this.monto));
				this.pojoProPagosDet.setEstatus("G");
				this.pojoProPagosDet.setModificadoPor(this.usuarioId);
				this.pojoProPagosDet.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoProPagosDet.setCreadoPor(this.usuarioId);
				this.pojoProPagosDet.setFechaCreacion(Calendar.getInstance().getTime());
				
				if (this.pojoOrdenCompraSeleccionada != null && this.pojoOrdenCompraSeleccionada.getId() != null && this.pojoOrdenCompraSeleccionada.getId() > 0L)
					this.pojoProPagosDet.setIdOrdenCompra(this.pojoOrdenCompraSeleccionada.getId());
				
				/*this.pojoProgPagos.setBeneficiarioId(this.pojoBeneficiarios);
				this.pojoProgPagos.setTipoBeneficiario(this.tipoPersona);
				this.nombreBeneficiario = this.pojoBeneficiarios.getNombre() + " " + (this.pojoBeneficiarios.getPrimerApellido() != null ? this.pojoBeneficiarios.getPrimerApellido() : "") + " " +  (this.pojoBeneficiarios.getSegundoApellido() != null ? this.pojoBeneficiarios.getSegundoApellido(): "");*/
				
				this.totalGastos   = this.totalGastos + this.monto;
				this.total 		   = this.totalGastos;
				this.montoRevisado = this.totalGastos;
				this.listGastos.add(this.pojoProPagosDet);
			}
			
			this.beneficiario 	= "";
			this.tipoPersona	= "P";
			this.conceptoGasto	= "";
			this.monto			= 0D;
			nuevaBusquedaOrdenCompra();
		} catch(Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en agregarGasto", e);
			control(true);
		} finally {
			this.pojoConceptoGtos = null;
			this.pojoBeneficiarios = null;
		}
	}
	
	public void eliminarGasto() {
		try {
			control();
			this.listGastosEliminados.add(this.pojoProPagosDet);
			this.listGastos.remove(this.pojoProPagosDet);
			this.totalGastos = this.totalGastos - this.pojoProPagosDet.getMonto().doubleValue();
			this.total = this.totalGastos;
			this.montoRevisado = this.totalGastos;
		} catch (Exception e) {			
			log.error("Error en el metodo eliminarGasto.", e);
			control(true);
		}
	}
	
	public void eliminarGastoEditar(){
		try {
			control();
			this.listGastosEditar.remove(this.pojoProPagosDet);
		} catch (Exception e) {			
			log.error("Error en el metodo eliminarGastoEditar.", e);
			control(true);
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
			
			this.listSucursales = this.ifzSucursal.buscarSucursales("sucursal", "");
			if (this.listSucursales != null && ! this.listSucursales.isEmpty()) {
				for (Sucursal var : this.listSucursales)
					this.listSucursalesItems.add(new SelectItem(var.getId() + " - " + var.getSucursal(), var.getSucursal()));
			}
		} catch (Exception e) {
			log.error("\n\nError en CuentasPorPagar.ProgramacionPagosAction.cargarSucursales()\n", e);
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

	public String revisarEstatusEditar(){
			try{
				this.resOperacion="";
				if("R".equals(pojoProgPagos.getEstatus())){
					this.resOperacion = mensajesInf.get(2);
					return "ERROR";
				}
						
			}catch(Exception e){
				this.resOperacion = this.mensajesInf.get(-1);
				log.error("error al revisarEstatusEditar", e);
				return "ERROR";
			}
		return "OK";
	}
	
	public String revisarEstatusEliminar(){
		try{
			resOperacion="";
			if("R".equals(pojoProgPagos.getEstatus())){
				this.resOperacion = mensajesInf.get(1);
			}
					
		}catch(Exception e){
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error al revisarEstatusEliminar", e);
			return "ERROR";
		}
		return "OK";
    }
	
	public List<Persona> autoacompletaBeneficiario(Object obj){
		try{
			this.listBeneficiarios = this.ifzPersonas.findLikeClaveNombre("nombre",obj.toString(), this.pojoGpoValPersonas, this.tipoPersona, 20, false);	
			return this.listBeneficiarios;
		}
		catch(Exception e){
			log.error("error en autoacompletaBeneficiario", e);
			return new ArrayList<Persona>();
		}
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

	public void mensajeTransaccion() {
		try {
			control();
			// Enviamos mensaje a cola de transacciones
			log.info("Enviando Transaccion ... ");
			/*if (this.pojoRegistroGasto == null || this.pojoRegistroGasto.getId() == null || this.pojoRegistroGasto.getId() <= 0L) {
				log.info("NO se pudo enviar la Transaccion. No selecciono registro de Registro Gasto");
				return;
			}
			
			this.ifzGtosComp.enviarTransaccion(this.pojoRegistroGasto.getId(), 1L);*/
			log.info("Transaccion envianda");
		} catch (Exception e) {
			log.error("error en mensajeTransaccion", e);
			control(true);
		}
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

	/*private void control(int tipoMensaje) {
		control(true, tipoMensaje, "");
	}*/

	private void control(String mensaje) {
		control(true, -1, mensaje);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.band = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.resOperacion = mensaje;
	}
	
	// -------------------------------------------------------------------------------------------
	// Busqueda ORDENES DE COMPRA
	// -------------------------------------------------------------------------------------------
	
	public void nuevaBusquedaOrdenCompra() {
		this.campoBusquedaOrdenesCompra = this.tiposBusquedaOrdenesCompra.get(0);
		this.valorBusquedaOrdenesCompra = "";
		
		this.paginaBusquedaOrdenesCompra = 1;
		this.pojoOrdenCompraSeleccionada = new OrdenCompra();
		if (this.listBusquedaOrdenesCompra == null)
			this.listBusquedaOrdenesCompra = new ArrayList<OrdenCompra>();
		this.listBusquedaOrdenesCompra.clear();
	}
	
	public void buscarOrdenesCompras() throws Exception {
		try {
			this.band = false;
			this.tipoMensaje = 0;
			this.resOperacion = "OK";
			
			if ("".equals(this.campoBusquedaOrdenesCompra))
				this.campoBusquedaOrdenesCompra = "id";
			if ("Clave".equals(this.campoBusquedaOrdenesCompra))
				this.campoBusquedaOrdenesCompra = "id";
			if ("Obra".equals(this.campoBusquedaOrdenesCompra))
				this.campoBusquedaOrdenesCompra = "nombreObra";
			if ("Proveedor".equals(this.campoBusquedaOrdenesCompra))
				this.campoBusquedaOrdenesCompra = "nombreProveedor";

			HashMap<String, String> params = new HashMap<String, String>();
			params.put(this.campoBusquedaOrdenesCompra, this.valorBusquedaOrdenesCompra);
			params.put("estatus", "0");
			this.listBusquedaOrdenesCompra = this.ifzOrdenesCompra.findLikeProperties(params, 0);
			//this.listBusquedaOrdenesCompra = this.ifzOrdenesCompra.findLikeProperty(this.campoBusquedaOrdenesCompra, this.valorBusquedaOrdenesCompra, 0);
			if(this.listBusquedaOrdenesCompra.isEmpty()){
				this.band = true;
				this.tipoMensaje = 2;
				this.resOperacion = "ERROR";
				return;
			}
    	} catch (Exception e) {
			this.band = true;
			this.tipoMensaje = 1;
			this.resOperacion = "ERROR";
    		log.error("Error en CuentasPorPagar.ProgramacionPagosAction.buscarOrdenesCompras", e);
    		throw e;
    	}
	}
	
	public void seleccionarOrdenCompra() {
		try {
			this.band = false;
			this.tipoMensaje = 0;
			this.resOperacion = "OK";
			
			this.campoBusquedaOrdenesCompra = this.tiposBusquedaOrdenesCompra.get(0);
			this.valorBusquedaOrdenesCompra = "";
			this.listBusquedaOrdenesCompra.clear();
			this.paginaBusquedaOrdenesCompra = 1;
			
			//nuevaBusquedaOrdenCompra();
		} catch (Exception e) {
			this.band = true;
			this.tipoMensaje = 1;
			this.resOperacion = "ERROR";
			log.error("Error en CuentasPorPagar.ProgramacionPagosAction.seleccionarOrdenCompra", e);
			throw e;
		}
	}

	// -------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------

	public String getOrdenCompraSeleccionada() {
		if (this.pojoOrdenCompraSeleccionada != null && this.pojoOrdenCompraSeleccionada.getId() != null && this.pojoOrdenCompraSeleccionada.getId() > 0L)
			return this.pojoOrdenCompraSeleccionada.getNombreProveedor();
		return "";
	}
	
	public void setOrdenCompraSeleccionada(String value) {}

	public ProgPagosExt getPojoProgPagos() {
		return pojoProgPagos;
	}
	
	public void setPojoProgPagos(ProgPagosExt pojoProgPagos) {
		this.pojoProgPagos = pojoProgPagos;
		
		/*this.clicNuevo = false;
		this.sucursal = this.pojoProgPagos.getAgente().getId() + " - " + this.pojoProgPagos.getAgente().getSucursal();
		
		this.beneficiario = "";
		/ *if(this.pojoProgPagos.getBeneficiarioId() != null){
			this.beneficiario = this.pojoProgPagos.getBeneficiarioId().getId() + " - " + this.pojoProgPagos.getBeneficiarioId().getNombre();//empresa = this.pojoProgPagos.getEmpresaId().getId()+ " - " +this.pojoProgPagos.getEmpresaId().getEmpresa();
		}* /
		/ *if(this.pojoProgPagos.getEmpresaId() != null){
			this.beneficiario = this.pojoProgPagos.getEmpresaId().getId() + " - " + this.pojoProgPagos.getEmpresaId().getEmpresa();//empresa = this.pojoProgPagos.getEmpresaId().getId()+ " - " +this.pojoProgPagos.getEmpresaId().getEmpresa();
		}* /
		this.total = pojoProgPagos.getTotal().doubleValue();
		this.montoRevisado = pojoProgPagos.getMontoRevisado().doubleValue();
		this.detallesProgramacion = "Programación de Pagos  del  " + formato.format(pojoProgPagos.getDel())+ " al  "+ formato.format(pojoProgPagos.getAl()) + "  Sucursal:"+pojoProgPagos.getAgente().getSucursal(); ;
		this.totalGastos = pojoProgPagos.getTotal().doubleValue();
		
		try {
			this.listGastos = this.ifzProPagosDet.findByPropertyPojoCompletoMontoNoCero("progPagos.id", this.pojoProgPagos.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.clicNuevo = "P".equals(this.pojoProgPagos.getEstatus());*/
	}
	
	public ProgPagosDetalleExt getPojoProPagosDet() {
		return pojoProPagosDet;
	}
	
	public void setPojoProPagosDet(ProgPagosDetalleExt pojoProPagosDet) {
		this.pojoProPagosDet = pojoProPagosDet;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}
	
	public String getResOperacion() {
		return resOperacion;
	}
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public List<ProgPagosExt> getListProgPagos() {
		return listProgPagos;
	}

	public void setListProgPagos(List<ProgPagosExt> listProgPagos) {
		this.listProgPagos = listProgPagos;
	}

	public List<Sucursal> getListSucursales() {
		return listSucursales;
	}

	public void setListSucursales(List<Sucursal> listSucursales) {
		this.listSucursales = listSucursales;
	}

	public List<ConValores> getListConceptoGasto() {
		return listConceptoGasto;
	}

	public void setListConceptoGasto(List<ConValores> listConceptoGasto) {
		this.listConceptoGasto = listConceptoGasto;
	}

	public String getObservaciones() {
		return pojoProgPagos.getObservaciones() != null ? pojoProgPagos.getObservaciones() : "" ;
	}

	public void setObservaciones(String observaciones) {
		this.pojoProgPagos.setObservaciones(observaciones);
	}

	public Double getMontoRevisado() {
		return montoRevisado != null ? montoRevisado : 0;
	}

	public void setMontoRevisado(Double montoRevisado) {
		this.montoRevisado = montoRevisado;
	}

	public Double getTotal() {
		return total != null ? total : 0;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Date getDel() {
		return pojoProgPagos.getDel() != null ? pojoProgPagos.getDel() : Calendar.getInstance().getTime();
	}

	public void setDel(Date del) {
		this.pojoProgPagos.setDel(del);
	}

	public Date getAl() {
		if (this.pojoProgPagos.getAl() != null)
			return this.pojoProgPagos.getAl();
		return Calendar.getInstance().getTime();
	}

	public void setAl(Date al) {
		this.pojoProgPagos.setAl(al);
	}

	public Long getProgPagoId() {
		if (this.pojoProgPagos.getId() != null) 
			return this.pojoProgPagos.getId();
		return 0L;
	}

	public void setProgPagoId(Long progPagoId) {
		this.pojoProgPagos.setId(progPagoId);
	}

	public String getConceptoGasto() {
		return conceptoGasto;
	}

	public void setConceptoGasto(String conceptoGasto) {
		this.conceptoGasto = conceptoGasto;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getSucursalBusqueda() {
		return sucursalBusqueda;
	}

	public void setSucursalBusqueda(String sucursalBusqueda) {
		this.sucursalBusqueda = sucursalBusqueda;
	}
	
	public Double getMonto() {
		return monto != null ? monto : 0 ;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public int getNumPaginagasto() {
		return numPaginagasto;
	}

	public void setNumPaginagasto(int numPaginagasto) {
		this.numPaginagasto = numPaginagasto;
	}

	public List<ProgPagosDetalleExt> getListGastos() {
		return listGastos;
	}

	public void setListGastos(List<ProgPagosDetalleExt> listGastos) {
		this.listGastos = listGastos;
	}
	
	public List<ProgPagosDetalleExt> getListGastosEditar() {
		return listGastosEditar;
	}

	public void setListGastosEditar(List<ProgPagosDetalleExt> listGastosEditar) {
		this.listGastosEditar = listGastosEditar;
	}

	public Double getTotalGastos() {
		return totalGastos;
	}

	public void setTotalGastos(Double totalGastos) {
		this.totalGastos = totalGastos;
	}

	public String getDetallesProgramacion() {
		return detallesProgramacion;
	}

	public void setDetallesProgramacion(String detallesProgramacion) {
		this.detallesProgramacion = detallesProgramacion;
	}

	public boolean isClicNuevo() {
		return clicNuevo;
	}

	public void setClicNuevo(boolean clicNuevo) {
		this.clicNuevo = clicNuevo;
	}
	
	public String getTipoPersona() {
		return tipoPersona;
	}
	
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
		this.beneficiario="";
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getNombreBeneficiario() {
		return nombreBeneficiario;
	}

	public void setNombreBeneficiario(String nombreBeneficiario) {
		this.nombreBeneficiario = nombreBeneficiario;
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

	public List<Sucursal> getListSucursalesBusqueda() {
		return listSucursalesBusqueda;
	}

	public void setListSucursalesBusqueda(List<Sucursal> listSucursalesBusqueda) {
		this.listSucursalesBusqueda = listSucursalesBusqueda;
	}

	public List<OrdenCompra> getListBusquedaOrdenesCompra() {
		return listBusquedaOrdenesCompra;
	}

	public void setListBusquedaOrdenesCompra(List<OrdenCompra> listBusquedaOrdenesCompra) {
		this.listBusquedaOrdenesCompra = listBusquedaOrdenesCompra;
	}

	public List<String> getTiposBusquedaOrdenesCompra() {
		return tiposBusquedaOrdenesCompra;
	}

	public void setTiposBusquedaOrdenesCompra(List<String> tiposBusquedaOrdenesCompra) {
		this.tiposBusquedaOrdenesCompra = tiposBusquedaOrdenesCompra;
	}

	public String getCampoBusquedaOrdenesCompra() {
		return campoBusquedaOrdenesCompra;
	}

	public void setCampoBusquedaOrdenesCompra(String campoBusquedaOrdenesCompra) {
		this.campoBusquedaOrdenesCompra = campoBusquedaOrdenesCompra;
	}

	public String getValorBusquedaOrdenesCompra() {
		return valorBusquedaOrdenesCompra;
	}

	public void setValorBusquedaOrdenesCompra(String valorBusquedaOrdenesCompra) {
		this.valorBusquedaOrdenesCompra = valorBusquedaOrdenesCompra;
	}

	public int getPaginaBusquedaOrdenesCompra() {
		return paginaBusquedaOrdenesCompra;
	}

	public void setPaginaBusquedaOrdenesCompra(int paginaBusquedaOrdenesCompra) {
		this.paginaBusquedaOrdenesCompra = paginaBusquedaOrdenesCompra;
	}

	public OrdenCompra getPojoOrdenCompraSeleccionada() {
		return pojoOrdenCompraSeleccionada;
	}

	public void setPojoOrdenCompraSeleccionada(OrdenCompra pojoOrdenCompraSeleccionada) {
		this.pojoOrdenCompraSeleccionada = pojoOrdenCompraSeleccionada;
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
	
	public List<SelectItem> getListSucursalesItems() {
		return listSucursalesItems;
	}

	public void setListSucursalesItems(List<SelectItem> listSucursalesItems) {
		this.listSucursalesItems = listSucursalesItems;
	}

	public List<SelectItem> getListSucursalesItemsBusqueda() {
		return listSucursalesItemsBusqueda;
	}

	public void setListSucursalesItemsBusqueda(List<SelectItem> listSucursalesItemsBusqueda) {
		this.listSucursalesItemsBusqueda = listSucursalesItemsBusqueda;
	}
}


//HISTORIAL DE MODIFICACIONES 
//-------------------------------------------------------------------------------------------------------------------
//VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
//-------------------------------------------------------------------------------------------------------------------
//  1.0   | 2016-09-20 | Javier Tirado      | Se corrigio el propiedad enviada a la busqueda para recuperar los gastos del elemento de la lista seleccionado 
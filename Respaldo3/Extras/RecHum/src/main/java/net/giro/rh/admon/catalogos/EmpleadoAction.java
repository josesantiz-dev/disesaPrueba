package net.giro.rh.admon.catalogos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.TimeZone;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.clientes.beans.Persona;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.beans.Categoria;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoBeneficiarioExt;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.beans.EmpleadoParienteExt;
import net.giro.rh.admon.beans.Puesto;
import net.giro.rh.admon.beans.PuestoCategoriaExt;
import net.giro.rh.admon.logica.AreaRem;
import net.giro.rh.admon.logica.CategoriaRem;
import net.giro.rh.admon.logica.EmpleadoBeneficiarioRem;
import net.giro.rh.admon.logica.EmpleadoContratoRem;
import net.giro.rh.admon.logica.EmpleadoParienteRem;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.rh.admon.logica.PuestoCategoriaRem;
import net.giro.rh.admon.logica.PuestoRem;
import net.giro.clientes.logica.ClientesRem;
import net.giro.comun.ExcepConstraint;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;

import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class EmpleadoAction implements Serializable {
	private static Logger log = Logger.getLogger(EmpleadoAction.class);
	private static final long serialVersionUID = 1L;
	private InitialContext ctx;
	
    private Integer usuarioId;	    
	private int numPagina;
	private int numPaginaPersonas;
	private int numPaginaFamiliares;
	private int numPaginaBeneficiarios;
	private String mensajeErrorBeneficiario;
	private boolean beneficiarioValidado;
	private int tipoMensaje;
	private boolean band;
	private String resOperacion;
	private String valorBusqueda;
	private EmpleadoRem 			ifzEmpleado;
	private ClientesRem 			ifzClientes; 
	private EmpleadoContratoRem 	ifzContrato;
	private SucursalesRem 			ifzSucursal;
	private EmpleadoParienteRem 	ifzEmpleadoPariente;
	private ConValoresRem 			ifzConValores;
	private AreaRem 				ifzArea;
	private CategoriaRem 			ifzCategorias;
	private PuestoRem 				ifzPuestos;
	private PuestoCategoriaRem 		ifzPuestosCategorias;
	private EmpleadoBeneficiarioRem ifzEmpleadoBeneficiario;
	private Empleado pojoEmpleadoMain;
	private EmpleadoExt pojoEmpleado;
	private Persona pojoPersona;
	private List<Empleado> listaEmpleadosGrid;
	private List<Persona> listaPersonasGrid;
	// Busqueda Personas
	private List<SelectItem> tipoBusquedaPersonas;	
	private String valBusquedaPersonas;
	private String campoBusquedaPersonas;
	//Valores para validacion
	private String nombreEmpleado;
	private Date fechaIngreso;
	private String numeroSeguridadSocial;
	private String emergencia;
	private String email;
	private boolean externo;
	// --------------- Los selects de nuevo/agregar
	// -----> Variables para el Select de Areas
	private List<SelectItem> listaCboAreas;
	private List<Area> listaAreas;
	private int idArea;
	private Area pojoArea;
	private String areaValidador;
	// -----> Variables para el Select de Puestos
	private List<SelectItem> listaCboPuestos;
	private List<Puesto> listaPuestos;
	private int idPuesto;
	private Puesto pojoPuesto;
	private String puestoValidador;
	// -----> Variables para el Select de Categorias
	private List<SelectItem> listaCboCategorias;
	private List<Categoria> listaCategorias;
	private int idCategoria;
	private Categoria pojoCategoria;
	private String categoriaValidador;
	//Sucursales
	private List<SelectItem> listaCboSucursales;
	private List<Sucursal> listaSucursales;
	// -----> Variables para familiares
	private EmpleadoParienteExt pojoFamiliar;
	private int idRelacionBeneficiario;
	private List<EmpleadoParienteExt> listaFamiliaresGrid;
	private List<EmpleadoParienteExt> listaFamiliaresEliminados;
	private Persona pojoFamiliarSeleccionado;
	private EmpleadoParienteExt pojoFamElimSeleccionado;
	private ConGrupoValoresRem ifzGpoVal;
	private long valGpoRelaciones;
	private List<ConValores> listaRelacionesFamiliares;
	private ConGrupoValores pojoGpoRelacion;
	private List<SelectItem> listaCboRelaciones;
	// Variables seleccion de FORMAS DE PAGO PARA EMPLEADOS
	private long valGpoFormasPagoEmpleados;
	private ConGrupoValores pojoFormasPagoEmpleados;
	private List<ConValores> listFormasPagoEmpleados;
	private List<SelectItem> listFormasPagoEmpleadosItems;
	private List<ConValores> listaRelacionesBeneficiarios;
	private List<SelectItem> listaCboRelacionesBeneficiarios;
	private int idRelacion;
	private ConValores pojoRelacion;
	private EmpleadoBeneficiarioExt pojoBeneficiario;
	private Persona pojoBeneficiarioSeleccionado;
	private List<EmpleadoBeneficiarioExt> listaBeneficiariosGrid;
	private List<EmpleadoBeneficiarioExt> listaBeneficiariosEliminados;
	// Campos para el contrato
	private List<EmpleadoContrato> listaContratosGrid;
	private EmpleadoContrato pojoContrato;
	private EmpleadoContrato pojoContratoBorrar;
	private long idPeridiocidadPago;
	private List<ConValores> listaPeridiocidad;
	private List<SelectItem> listaCboPeridiocidad;
	private long valGpoPeriodo;	
	private ConGrupoValores pojoGpoPeriodo;
	private int idDiaDescanso;
	private List<SelectItem> listaCboDias;
	private String horaEntrada;
	private String horaSalida;
	private String horaEntradaComplemento;
	private String horaSalidaComplemento;
	private boolean esContratoNuevo;
	private int contratosPaginacion;
	
	
 	public EmpleadoAction() throws NamingException,Exception {
		String ejbName;
		
		this.tipoMensaje = 0;
		this.band = false;
		this.numPagina = 1;
		this.numPaginaPersonas = 1;
		this.numPaginaFamiliares = 1;
		this.numPaginaBeneficiarios = 1;

		this.ctx = new InitialContext();
		
		ejbName = "ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem";
		this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem";
		this.ifzClientes =  (ClientesRem)ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_RecHum//AreaFac!net.giro.rh.admon.logica.AreaRem";
		this.ifzArea = (AreaRem) this.ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_RecHum//CategoriaFac!net.giro.rh.admon.logica.CategoriaRem";
		this.ifzCategorias = (CategoriaRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_RecHum//PuestoFac!net.giro.rh.admon.logica.PuestoRem";
		this.ifzPuestos = (PuestoRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_RecHum//PuestoCategoriaFac!net.giro.rh.admon.logica.PuestoCategoriaRem";
		this.ifzPuestosCategorias  = (PuestoCategoriaRem) this.ctx.lookup(ejbName);
		
		
		ejbName = "ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem";
		this.ifzContrato = (EmpleadoContratoRem) this.ctx.lookup(ejbName);
		
		ejbName="ejb:/Logica_RecHum//EmpleadoParienteFac!net.giro.rh.admon.logica.EmpleadoParienteRem";
		this.ifzEmpleadoPariente = (EmpleadoParienteRem) this.ctx.lookup(ejbName);
		
		ejbName="ejb:/Logica_RecHum//EmpleadoBeneficiarioFac!net.giro.rh.admon.logica.EmpleadoBeneficiarioRem";
		this.ifzEmpleadoBeneficiario = (EmpleadoBeneficiarioRem) this.ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem";
		this.ifzSucursal = (SucursalesRem) ctx.lookup(ejbName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
		this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		
		ejbName = "ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem";
		this.ifzConValores = (ConValoresRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem";
		this.ifzGpoVal = (ConGrupoValoresRem) 	this.ctx.lookup(ejbName);
		
		if ( entornoProperties.getString("SYS_RELACIONES") == null || "".equals(entornoProperties.getString("SYS_RELACIONES")) )
			throw new Exception("No se encontro encontro el grupo SYS_RELACIONES en con_grupo_valores");
		else
			this.valGpoRelaciones = Long.valueOf(entornoProperties.getString("SYS_RELACIONES")) ;
		
		if ( entornoProperties.getString("SYS_TIPO_PERIODO") == null || "".equals(entornoProperties.getString("SYS_TIPO_PERIODO")) )
			throw new Exception("No se encontro encontro el grupo SYS_TIPO_PERIODO en con_grupo_valores");
		else
			this.valGpoPeriodo = Long.valueOf(entornoProperties.getString("SYS_TIPO_PERIODO")) ;
		
		// FORMAS DE PAGO PARA EMPLEADOS
		if ( entornoProperties.getString("SYS_FORMA_PAGO_EMPLEADO") == null || "".equals(entornoProperties.getString("SYS_FORMA_PAGO_EMPLEADO")) )
			throw new Exception("No se encontro encontro el grupo SYS_FORMA_PAGO_EMPLEADO en con_grupo_valores");
		else
			this.valGpoFormasPagoEmpleados = Long.valueOf(entornoProperties.getString("SYS_FORMA_PAGO_EMPLEADO"));
		this.pojoFormasPagoEmpleados = this.ifzGpoVal.findById(this.valGpoFormasPagoEmpleados);
				
		this.pojoGpoRelacion = this.ifzGpoVal.findById(valGpoRelaciones);
		this.listaRelacionesFamiliares =  this.ifzConValores.buscaValorGrupo("descripcion", "", pojoGpoRelacion);
		this.listaRelacionesBeneficiarios = this.ifzConValores.buscaValorGrupo("descripcion", "", pojoGpoRelacion);
		
		this.pojoGpoPeriodo = this.ifzGpoVal.findById( valGpoPeriodo );
		this.listaPeridiocidad = this.ifzConValores.buscaValorGrupo("descripcion", "", pojoGpoPeriodo);
		
		this.pojoEmpleado = new EmpleadoExt();
		
		llenarTipoBusquedaPersonas();
		
		listaAreas = new ArrayList<Area>();
		listaCategorias = new ArrayList<Categoria>();
		listaPuestos = new ArrayList<Puesto>();
		listaFamiliaresGrid = new ArrayList<>();
		
		cargarAreas();
		cargarCboAreas();
		cargarCategorias();
		cargarCboCategorias();
		cargarPuestos();
		cargarCboPuestos();
		cargarSucursales();
		cargarFormasPagoEmpleados();
		
		listaFamiliaresGrid = new ArrayList<>();
		listaFamiliaresEliminados = new ArrayList<>();
		
		this.listaBeneficiariosGrid = new ArrayList<>();
		this.listaBeneficiariosEliminados = new ArrayList<>();
		this.pojoBeneficiario = new EmpleadoBeneficiarioExt();
		
		
		cargarDiasDescanso();
		
		cargarListaCboRelaciones();
		cargarListaCboRelacionesBeneficiarios();

		//para el contrato
		cargarListaCboPeridiocidad();
		
		pojoContrato = new EmpleadoContrato();
		esContratoNuevo = true;
		this.contratosPaginacion = 1;
	}
	

	public String buscar(){
		try{			
			this.resOperacion = "";
			
			if( "".equals(this.valorBusqueda) ){
				this.listaEmpleadosGrid = this.ifzEmpleado.findAll(); 
			} else {
				this.listaEmpleadosGrid = this.ifzEmpleado.findByNombreEmpleado(this.valorBusqueda.toLowerCase());
			}
			
			if (this.listaEmpleadosGrid.isEmpty()) {
				log.error("La busqueda no devolvio resultados");
				this.resOperacion = "La busqueda no devolvio resultados.";
				return "ERROR";
			}			
		} catch(Exception e) {
			log.error("error al buscar", e);
			this.resOperacion = "ERROR";
			return "ERROR";
		}
		
		this.resOperacion = "";
		return "OK";
	}

	public String nuevo(){
		try{			
			band=false;
			tipoMensaje=0;
			this.resOperacion = "";
			
			this.setIdArea(0);
			this.setIdPuesto(0);
			this.setNumeroSeguridadSocial("");
			this.setIdCategoria(0);
			this.setEmergencia("");
			
			areaValidador = "";
			nombreEmpleado = "";
			categoriaValidador="";
			
			this.fechaIngreso = Calendar.getInstance().getTime();			
			this.pojoEmpleado = new EmpleadoExt();			
			this.pojoContrato = new EmpleadoContrato();	
		} catch(Exception e) {
			band=true;
			tipoMensaje=1;
			this.resOperacion = "ERROR";
			log.error("Error en el metodo nuevo.", e);
			return "ERROR";
		}
		
		this.resOperacion = "OK";
		return "OK";
	}
	
	public String guardar(){
		this.resOperacion = "";
		this.band = false;
		
		try {
			if (this.pojoEmpleado.getId() == null) {
				if ( ! validaGuardarEmpleado() ){
					return "";
				}
				
				this.pojoEmpleado.setCreadoPor(this.usuarioId.longValue());
				this.pojoEmpleado.setFechaCreacion( Calendar.getInstance().getTime() );
				this.pojoEmpleado.setModificadoPor( this.usuarioId.longValue() );
				this.pojoEmpleado.setFechaModificacion( Calendar.getInstance().getTime() );
				this.pojoEmpleado.setFechaIngreso( this.fechaIngreso );
				this.pojoEmpleado.setEmail(this.email);
				
				this.pojoEmpleado.setNumeroSeguridadSocial( this.numeroSeguridadSocial );
				this.pojoEmpleado.setNombreCasoEmergencia( this.emergencia );
				
				this.pojoEmpleado.setExterno( this.isExterno()==true ? 1L : 0L );
				
				this.pojoEmpleado.setEstatus(0);
				
				this.pojoEmpleado.setFechaIngreso( this.fechaIngreso );
				this.pojoEmpleado.setNumeroSeguridadSocial( this.numeroSeguridadSocial );
				
				this.pojoEmpleado.setArea( this.pojoArea );
				this.pojoEmpleado.setPersona( this.pojoPersona );
				
				List<PuestoCategoriaExt> pce = this.ifzPuestosCategorias.findByPuestoCategoriaExt(this.idPuesto, this.idCategoria);
				if( pce.size() > 0 ){
					pojoEmpleado.setPuestoCategoria( pce.get(0) );
				}
				
				setDatosPojoEmpleado();
				
				this.pojoEmpleado.setId(this.ifzEmpleado.save(this.pojoEmpleado));
				this.pojoEmpleadoMain = this.ifzEmpleado.convertir(this.pojoEmpleado);
				
				if(this.listaEmpleadosGrid == null)
					this.listaEmpleadosGrid = new ArrayList<>();
				
				this.listaEmpleadosGrid.add(this.pojoEmpleadoMain);
			} else {
				this.pojoEmpleado.setArea( this.pojoArea );
				pojoEmpleado.setExterno( this.isExterno()==true ? 1L : 0L );
				
				List<PuestoCategoriaExt> pce = this.ifzPuestosCategorias.findByPuestoCategoriaExt(this.idPuesto, this.idCategoria);
				if( pce.size() > 0 ){
					pojoEmpleado.setPuestoCategoria( pce.get(0) );
				}
				
				this.pojoEmpleado.setFechaIngreso( this.fechaIngreso );
				this.pojoEmpleado.setNumeroSeguridadSocial( this.numeroSeguridadSocial );
				this.pojoEmpleado.setEmail(this.email);
				
				setDatosPojoEmpleado();
				
				this.ifzEmpleado.update(pojoEmpleado);
				this.pojoEmpleadoMain = this.ifzEmpleado.convertir(this.pojoEmpleado);

				for (Empleado var : this.listaEmpleadosGrid) {
					if (var.getId() == this.pojoEmpleadoMain.getId())
						var = this.pojoEmpleadoMain;
				}
			}

			this.band = true;
			this.resOperacion = "";
			log.info("Guardar finalizado");
			
			return "OK";
		} catch (Exception e) {
			band=false;
			tipoMensaje=1;
			this.resOperacion = "ERROR";
			log.error("error al guardar", e);
			return "ERROR";
		}
	}

	public String editar(){	
		this.resOperacion = "";
		
		this.pojoEmpleado = this.ifzEmpleado.convertir(this.pojoEmpleadoMain);
		//this.pojoPersona = pojoEmpleado.getPersona();
		this.pojoPersona = this.pojoEmpleado.getPersona(); 
		
		this.setDatosPojoEmpleado();
		
		nombreEmpleado = this.pojoEmpleado.getPersona().getNombre(); //pojoEmpleado.getNombre();
		
		this.pojoArea =  this.pojoEmpleado.getArea(); //pojoEmpleado.getArea();
		areaValidador = pojoArea.getDescripcion();
		this.setIdArea( this.pojoArea.getId().intValue() );
		
		this.pojoPuesto = this.pojoEmpleado.getPuestoCategoria().getPuesto(); //pojoEmpleado.getPuestoCategoria().getPuesto();
		puestoValidador = pojoPuesto.getDescripcion();
		this.setIdPuesto( this.pojoPuesto.getId().intValue() );
		
		this.pojoCategoria = this.pojoEmpleado.getPuestoCategoria().getCategoria(); //pojoEmpleado.getPuestoCategoria().getCategoria();
		categoriaValidador = pojoCategoria.getDescripcion();
		this.setIdCategoria( pojoCategoria.getId().intValue() );
		
		fechaIngreso = this.pojoEmpleado.getFechaIngreso();
		numeroSeguridadSocial = pojoEmpleado.getNumeroSeguridadSocial();
		emergencia = this.pojoEmpleado.getNombreCasoEmergencia();
		externo = this.pojoEmpleado.getExterno()==1L ? true : false;
		this.email = this.pojoEmpleado.getEmail();
		
		return "OK";
	}
	
	public String eliminar() {
		try {
			this.pojoEmpleadoMain.setFechaModificacion( Calendar.getInstance().getTime() );
			this.pojoEmpleadoMain.setFechaIngreso( Calendar.getInstance().getTime() );
			this.pojoEmpleadoMain.setEstatus(1);
			
			this.ifzEmpleado.update(this.pojoEmpleadoMain);
		} catch (Exception e) {
			log.error("Error en el metodo eliminar.", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public TimeZone getTimeZone() {  
		TimeZone timeZone = TimeZone.getDefault();  
		return timeZone;
	}
	
	public void cargarSucursales(){
		
		this.listaCboSucursales = null;
		this.listaCboSucursales = new ArrayList<>();
		
		try {
			this.listaSucursales = this.ifzSucursal.buscarSucursales("sucursal", "");
		} catch (ExcepConstraint e) {
			log.error("Error en RecHum.EmpleadoAction.cargarSucursales", e);
		}
		
		this.listaCboSucursales.add( new SelectItem(0, "" ) );
		
		for(Sucursal s:this.listaSucursales){
			this.listaCboSucursales.add( new SelectItem(s.getId(), s.getSucursal() ) );
		}
	}
	
	public void cargarDiasDescanso(){
		//Se iniciara con 1 en domingo
		this.listaCboDias = null;
		this.listaCboDias = new ArrayList<SelectItem>();
		this.listaCboDias.add( new SelectItem( "1" , "Domingo") );
		this.listaCboDias.add( new SelectItem( "2" , "Lunes") );
		this.listaCboDias.add( new SelectItem( "3" , "Martes") );
		this.listaCboDias.add( new SelectItem( "4" , "Miercoles") );
		this.listaCboDias.add( new SelectItem( "5" , "Jueves") );
		this.listaCboDias.add( new SelectItem( "6" , "Viernes") );
		this.listaCboDias.add( new SelectItem( "7" , "Sabado") );
	}
	
	public void cargarFormasPagoEmpleados() {
		try {
			this.band = false;
			this.tipoMensaje = 0;
			this.resOperacion = "";
			
			if (pojoFormasPagoEmpleados == null)
				return;
			
			if (this.listFormasPagoEmpleadosItems == null)
				this.listFormasPagoEmpleadosItems = new ArrayList<SelectItem>();
			this.listFormasPagoEmpleadosItems.clear();
			
			this.listFormasPagoEmpleados = this.ifzConValores.findAll(this.pojoFormasPagoEmpleados);
			if (this.listFormasPagoEmpleados != null && ! this.listFormasPagoEmpleados.isEmpty()) {
				for(ConValores var : this.listFormasPagoEmpleados) {
					this.listFormasPagoEmpleadosItems.add(new SelectItem(var.getId(), var.getValor()));
				}
			}
		} catch (Exception e) {
			this.band = false;
			this.tipoMensaje = 1;
			this.resOperacion = "ERROR";
			log.error("Error en cargarFormasPagoEmpleados", e);
		}
	}
		
	public void cargarListaCboPeridiocidad(){
		if (this.listaCboPeridiocidad == null)
			this.listaCboPeridiocidad = new ArrayList<SelectItem>();
		this.listaCboPeridiocidad.clear();
		
		for( ConValores cv: this.listaPeridiocidad ){
			this.listaCboPeridiocidad.add(new SelectItem(cv.getId(), cv.getValor()));
		}
	}
		
	public void cargarContratos(){
		if (this.listaContratosGrid == null)
			this.listaContratosGrid = new ArrayList<EmpleadoContrato>();
		this.listaContratosGrid.clear();
		
		this.listaContratosGrid = this.ifzContrato.findAllByIdEmpleado( this.pojoEmpleado.getId() );
	}

	// CONTRATOS
	// -----------------------------------------------------------------
	public void nuevoContrato(){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

		this.esContratoNuevo = true;
		this.pojoContrato = new EmpleadoContrato();
		this.pojoContrato.setTipoHorario(-1);
		this.horaEntrada = "00:00";
		this.horaSalida = "00:00";
		this.horaEntradaComplemento = "00:00";
		this.horaSalidaComplemento = "00:00";
		
		if (this.pojoContrato.getHoraEntrada() != null)
			this.horaEntrada = formatter.format(this.pojoContrato.getHoraEntrada());
		
		if (this.pojoContrato.getHoraEntrada() != null)
			this.horaSalida = formatter.format(this.pojoContrato.getHoraSalida());
		
		if (this.pojoContrato.getHoraEntradaComplemento() != null)
			this.horaEntradaComplemento = formatter.format(this.pojoContrato.getHoraEntradaComplemento());
		
		if (this.pojoContrato.getHoraSalidaComplemento() != null)
			this.horaSalidaComplemento = formatter.format(this.pojoContrato.getHoraSalidaComplemento());
		
		if (this.pojoContrato.getTipoHorario() == 1) {
			this.horaEntradaComplemento = "";
			this.horaSalidaComplemento = "";
		}
	}
	
	public void editarContrato() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		
		try {
			this.band = false;
			this.tipoMensaje = 0;
			this.resOperacion = "";
			
			if (this.pojoContrato == null || this.pojoContrato.getId() == null || this.pojoContrato.getId() <= 0L) {
				this.band = true;
				this.tipoMensaje = 0;
				this.resOperacion = "Ocurrio un problema al intentar recuperar el Contrato";
				return;
			}
			
			this.esContratoNuevo = false;
			this.idPeridiocidadPago = this.pojoContrato.getPeridiocidadPago();
			this.valGpoPeriodo = this.pojoContrato.getFormaPago();
			this.idDiaDescanso = this.pojoContrato.getDiaDescanso();
			this.horaEntrada = formatter.format(this.pojoContrato.getHoraEntrada());
			this.horaSalida = formatter.format(this.pojoContrato.getHoraSalida());
			if (this.pojoContrato.getTipoHorario() == 0) {
				this.horaEntradaComplemento = formatter.format(this.pojoContrato.getHoraEntradaComplemento());
				this.horaSalidaComplemento = formatter.format(this.pojoContrato.getHoraSalidaComplemento());
			}
		} catch (Exception e) {
			this.band = true;
			this.tipoMensaje = 1;
			this.resOperacion = "Error Inesperado";
			log.error("Error en RecHum.EmpleadoAction.editarContrato", e);
			return;
		}
	}
	
	public void guardarContrato() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		this.resOperacion = "Informacion registrada";
		this.band = false;
		
		try {
			if(! validarContrato()){
				this.band = true;
				return;
			}
			
			this.pojoContrato.setIdEmpleado( this.pojoEmpleado.getId() );
			this.pojoContrato.setHoraEntrada(formatter.parse(this.horaEntrada));
			this.pojoContrato.setHoraSalida(formatter.parse(this.horaSalida));
			if (this.pojoContrato.getTipoHorario() == 0) {
				this.pojoContrato.setHoraEntradaComplemento(formatter.parse(this.horaEntradaComplemento));
				this.pojoContrato.setHoraSalidaComplemento(formatter.parse(this.horaSalidaComplemento));
			}
			this.pojoContrato.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoContrato.setModificadoPor(this.usuarioId);

			if (this.pojoContrato.getId() == null || this.pojoContrato.getId() <= 0L) {
				this.pojoContrato.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoContrato.setCreadoPor(this.usuarioId);
				
				// Guardamos en la BD y asignamos ID.
				this.pojoContrato.setId( this.ifzContrato.save(this.pojoContrato));
			} else {
				// Actualizar en la BD.
				this.ifzContrato.update(this.pojoContrato);
			}
			
			cargarContratos();
		} catch (Exception e) {
			this.band = true;
			this.resOperacion = "Error al guardar";
			log.error("Error en RecHum.EmpleadoAction.guardarContrato", e);
			return;
		}
	}
	
	public void eliminarContrato() {
		this.band = false;
		this.tipoMensaje = 0;
		this.resOperacion = "";
		
		try {
			if (this.pojoContratoBorrar != null && this.pojoContratoBorrar.getId() != null && this.pojoContratoBorrar.getId() > 0L) {
				this.pojoContratoBorrar.setEstatus(1);
				this.pojoContratoBorrar.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoContratoBorrar.setModificadoPor(this.usuarioId);
				
				// Borramos el contrato de la BD
				this.ifzContrato.update(this.pojoContratoBorrar);
				
				// Recargamos los contratos
				cargarContratos();
			}
		} catch (Exception e) {
			this.band = true;
			this.resOperacion = "Error al eliminar contrato";
			log.error("Error en RecHum.EmpleadoAction.eliminarContrato", e);
			return;
		}
	}

	private boolean validarContrato() {
		//area de la validacion para el contrato
		if (this.pojoEmpleado == null || this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) {
			this.resOperacion = "Error al recuperar los datos del empleado.";
			return false;
		}
		
		if(this.pojoContrato.getFechaInicio() == null){
			this.resOperacion = "Debe indicar la fecha de inicio del contrato";
			return false;
		}
		
		if(this.pojoContrato.getPeridiocidadPago() <= 0){
			this.resOperacion = "Debe indicar la peridiocidad del pago";
			return false;
		}
		
		if(this.pojoContrato.getDeterminado() == 1){	//Es determinado, hay que incluir la fecha de termino: validar
			if(this.pojoContrato.getFechaFin() == null){
				this.resOperacion = "Debe indicar la fecha de termino del contrato";
				return false;
			}
			
			if(this.pojoContrato.getFechaInicio().compareTo(pojoContrato.getFechaFin()) >= 0 ){
				this.resOperacion = "La fecha de finalización debe ser mayor a la fecha de inicio";
				return false;
			}
		}
		
		if(this.pojoContrato.getDiaDescanso() <= 0){
			this.resOperacion = "Debe indicar el día de descanso";
			return false;
		}
		
		if(this.pojoContrato.getSueldo() <= 0 ){
			this.resOperacion = "Debe indicar el sueldo";
			return false;
		}
		
		if(this.pojoContrato.getCentroTrabajo().trim().equals("") ){
			this.resOperacion = "Indique el centro de trabajo";
			return false;
		}
		
		if(this.pojoContrato.getSueldoHora() == null || this.pojoContrato.getSueldoHora().doubleValue() <= 0 ){
			this.resOperacion = "Debe indicar el sueldo hora";
			return false;
		}
		
		if(this.pojoContrato.getSueldoHoraExtra() == null || this.pojoContrato.getSueldoHoraExtra().doubleValue() <= 0 ){
			this.resOperacion = "Debe indicar el sueldo hora extra";
			return false;
		}
		
		return true;
	}
	
	// BENEFICIARIOS
	// -----------------------------------------------------------------
	public void cargarBeneficiarios() {
		this.numPaginaBeneficiarios = 1;
		if (this.listaBeneficiariosGrid == null)
			this.listaBeneficiariosGrid = new ArrayList<EmpleadoBeneficiarioExt>();
		this.listaBeneficiariosGrid.clear();

		if (this.listaBeneficiariosEliminados == null)
			this.listaBeneficiariosEliminados = new ArrayList<EmpleadoBeneficiarioExt>();
		this.listaBeneficiariosEliminados.clear();
		
		this.listaBeneficiariosGrid = this.ifzEmpleadoBeneficiario.findByIdEmpleado(this.pojoEmpleado.getId());
	}
	
	public void guardarBeneficiario() {
		this.resOperacion = "OK";
		this.band = false;
		
		if(pojoBeneficiario.getPersona()==null){
			this.resOperacion = "No ha seleccionado beneficiario";
			return;
		}
		
		if(this.pojoBeneficiario.getPorcentaje() == null || this.pojoBeneficiario.getPorcentaje() == 0 ){
			this.resOperacion = "El porcentaje debe ser mayor a cero";
			return;
		}
		
		if( this.getTotalPorcentajeBeneficiario() < 0 ){
			this.resOperacion = "El porcentaje que intenta agregar excede el 100%";
			return;
		}
		
		if(this.pojoBeneficiario.getId()==null){	//nuevo beneficiario
			for( EmpleadoBeneficiarioExt e: this.listaBeneficiariosGrid ){
				if(this.pojoEmpleado.getPersona().getId() == e.getPersona().getId() ){
					this.resOperacion = "El empleado no puede ser beneficiario";
					return;
				}
			}
			
			for( EmpleadoBeneficiarioExt e: this.listaBeneficiariosGrid ){
				if(pojoBeneficiario.getPersona().getId() == e.getPersona().getId() ){
					this.resOperacion = "El beneficiario ya se encuentra en la lista";
					return;
				}
			}
			
			this.pojoBeneficiario.setCreadoPor( this.usuarioId.longValue() );
			this.pojoBeneficiario.setFechaCreacion( Calendar.getInstance().getTime() );
			this.pojoBeneficiario.setModificadoPor( this.usuarioId.longValue() );
			this.pojoBeneficiario.setEmpleado( this.pojoEmpleado );
			
			this.listaBeneficiariosGrid.add( this.pojoBeneficiario );
			
			try {
				this.pojoBeneficiario.setId( this.ifzEmpleadoBeneficiario.save( this.pojoBeneficiario ) );
				//this.pojoBeneficiario = new EmpleadoBeneficiarioExt();
			} catch (ExcepConstraint e) {
				this.resOperacion = "Error al guardar";
				log.error("Error en RecHum.EmpleadoAction.guardarBeneficiario", e);
			}
			
		}else{
			
			for( EmpleadoBeneficiarioExt e: this.listaBeneficiariosGrid ){
				if(this.pojoEmpleado.getPersona().getId() == e.getPersona().getId() ){
					this.resOperacion = "El empleado no puede ser beneficiario";
					return;
				}
			}
			
			try {
				this.ifzEmpleadoBeneficiario.update( this.pojoBeneficiario );
			} catch (ExcepConstraint e) {
				this.resOperacion = "Error al editar";
				log.error("Error en RecHum.EmpleadoAction.guardarBeneficiario", e);
			}
			
		}
		
		this.resOperacion = "";
		this.band = true;
		
		
	}
		
	public void editarBeneficiario(){
	}
		
	public double getTotalPorcentajeBeneficiario(){	//regresará el total del porcentaje que se ha ocupado para los beneficiarios
		double porcentaje=0;
		
		porcentaje += this.pojoBeneficiario.getPorcentaje()==null ? 0 : this.pojoBeneficiario.getPorcentaje();
		
		for(EmpleadoBeneficiarioExt ebe:this.listaBeneficiariosGrid){
			porcentaje+=ebe.getPorcentaje();
		}
		
		if(pojoBeneficiario.getId()!=null){
			porcentaje = porcentaje - this.pojoBeneficiario.getPorcentaje(); //descontar el porcetaje que tenia para sumar el nuevo
		}
		
		return 100 - porcentaje;
	}
		
	public void nuevoBeneficiario() {
		this.pojoBeneficiario = new EmpleadoBeneficiarioExt();
		this.pojoBeneficiarioSeleccionado = new Persona();
	}
		
	public void seleccionarBeneficiario() {
		this.band = false;
		this.resOperacion = "";
		
		if(this.pojoBeneficiarioSeleccionado == null || this.pojoBeneficiarioSeleccionado.getId() <= 0L) {
			this.band = true;
			this.resOperacion = "Ocurrio un problema al seleccionar la persona.";
			return;
		}	
		
		if (this.pojoBeneficiarioSeleccionado.getId() == this.pojoEmpleado.getPersona().getId()) {
			this.band = true;
			this.resOperacion ="No se permite seleccionar al mismo empleado como beneficiario.";
			nuevoBeneficiario();
		}
	}

	// -----------------------------------------------------------------
	
	public void cargarListaCboRelaciones(){
		
		this.listaCboRelaciones = null;
		this.listaCboRelaciones = new ArrayList<SelectItem>();
		
		for( ConValores cv: this.listaRelacionesFamiliares ){
			this.listaCboRelaciones.add( new SelectItem( cv.getId() , cv.getValor()  ) );
		}
	}
		
	public void cargarListaCboRelacionesBeneficiarios(){
		this.listaCboRelacionesBeneficiarios = null;
		this.listaCboRelacionesBeneficiarios = new ArrayList<SelectItem>();
		
		for( ConValores cv: this.listaRelacionesBeneficiarios ){
			this.listaCboRelacionesBeneficiarios.add( new SelectItem( cv.getId() , cv.getValor()  ) );
		}
		
	}
		
	public void nuevoFamiliar(){
		this.pojoFamiliar = new EmpleadoParienteExt();
	}
		
	public void cargarFamiliares(){
		this.numPaginaFamiliares = 1;
		if (this.listaFamiliaresGrid == null)
			this.listaFamiliaresGrid = new ArrayList<EmpleadoParienteExt>();
		this.listaFamiliaresGrid.clear();
		
		if (this.listaFamiliaresEliminados == null)
			this.listaFamiliaresEliminados = new ArrayList<EmpleadoParienteExt>();
		this.listaFamiliaresEliminados.clear();
		
		this.pojoFamElimSeleccionado = new EmpleadoParienteExt();
		this.listaFamiliaresGrid = this.ifzEmpleadoPariente.findByIdEmpleadoParentesco( this.pojoEmpleado.getId() );
	}
	
	public void eliminarBeneficiario(){
		try {
			this.ifzEmpleadoBeneficiario.delete( this.pojoBeneficiario );
		} catch (ExcepConstraint e) {
			log.error("Error en RecHum.EmpleadoAction.eliminarBeneficiario", e);
		}
		
		this.listaBeneficiariosEliminados.add( this.pojoBeneficiario );
		for( EmpleadoBeneficiarioExt ebe: this.listaBeneficiariosGrid){
			if( ebe.getId() == this.pojoBeneficiario.getId() ){
				this.listaBeneficiariosGrid.remove(ebe);
				break;
			}
		}
	}
	
	public void seleccionarFamiliar(){
		
		
	}
	
	public void eliminarFamiliar(){
		this.resOperacion = "";
		this.band = false;
		
		try {
			this.ifzEmpleadoPariente.delete( pojoFamElimSeleccionado );
			
			for( EmpleadoParienteExt fam:listaFamiliaresGrid ){
				if( fam.getPersona().getId() == pojoFamElimSeleccionado.getPersona().getId() ){
					this.listaFamiliaresGrid.remove( fam );
					break;
				}
			}
			
			this.band = true;
		} catch (ExcepConstraint e) {
			this.resOperacion = "Error al eliminar el familiar";
			log.error("Error en RecHum.EmpleadoAction.eliminarFamiliar", e);
		}
	}

	public void editarFamiliar(){
	}
	
	private boolean validarGuardarFamiliar(){
		if ( pojoFamElimSeleccionado.getPersona() == null ){
			this.resOperacion = "No se ha seleccionado persona";
			return false;
		}
		
		if( this.pojoFamElimSeleccionado.getPersona().getId() == this.pojoEmpleado.getPersona().getId() ){
			this.resOperacion = "No se puede agregar como familiar al empleado mismo";
			return false;
		}
		
		for(EmpleadoParienteExt e: this.listaFamiliaresGrid){
			if( this.pojoFamElimSeleccionado.getPersona().getId() == e.getPersona().getId() ){
				this.resOperacion = "El familiar que pretende agregar ya se encuentra en la lista";
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validarEditarFamiliar(){
		
		if ( pojoFamElimSeleccionado.getPersona() == null ){
			this.resOperacion = "No se ha seleccionado persona";
			return false;
		}
		
		return true;
	}
	
	public void guardarFamiliar(){		
		this.resOperacion = "OK";
		this.band = false;
		
		if( pojoFamElimSeleccionado.getId() == null ){			
			if(!validarGuardarFamiliar()){
				return;
			}
			
			pojoFamElimSeleccionado.setCreadoPor( this.usuarioId.longValue() );
			pojoFamElimSeleccionado.setFechaCreacion( Calendar.getInstance().getTime() );
			pojoFamElimSeleccionado.setModificadoPor( this.usuarioId.longValue() );
			pojoFamElimSeleccionado.setFechaModificacion( Calendar.getInstance().getTime() );
			pojoFamElimSeleccionado.setEmpleado( this.pojoEmpleado );			
			pojoFamElimSeleccionado.setRelacion( this.pojoRelacion );
			
			try {				
				this.pojoFamElimSeleccionado.setId( this.ifzEmpleadoPariente.save( pojoFamElimSeleccionado ) );
				this.listaFamiliaresGrid.add( pojoFamElimSeleccionado );				
				pojoFamElimSeleccionado = new EmpleadoParienteExt();				
			} catch (ExcepConstraint e1) {
				this.resOperacion = "Error al guardar el familiar";
				e1.printStackTrace();
				return;
			}			
		}else{
			if(!validarEditarFamiliar()){
				return;
			}
			
			pojoFamElimSeleccionado.setRelacion( this.pojoRelacion );
			
			try {
				this.ifzEmpleadoPariente.update( pojoFamElimSeleccionado );
				pojoFamElimSeleccionado = new EmpleadoParienteExt();
			} catch (ExcepConstraint e1) {
				this.resOperacion = "Error al editar el familiar";
				e1.printStackTrace();
				return;
			}
			
		}

		this.resOperacion = "";		
		this.band = true;
	}
	
	private boolean validaGuardarEmpleado(){

		if(this.pojoEmpleado.getPersona()==null){
			this.resOperacion = "Debe seleccionar una persona";
			return false;
		}
		
		if( this.pojoEmpleado.getSucursal() == null ){
			this.resOperacion = "Seleccione sucursal";
			return false;
		}

		if( this.idPuesto == 0 ){
			this.resOperacion = "Debe seleccionar Puesto";
			return false;
		}
		
		if(this.numeroSeguridadSocial.trim().equals("") ){
			this.resOperacion = "Introduzca número de Seguridad Social";
			return false;
		}
		
		//validar si este empleado ya ha sido dado de alta
		if( this.ifzEmpleado.findEmpleadoRepetido(  this.pojoEmpleado.getPersona().getId()  )   ){
			this.resOperacion = "Este empleado ya ha sido registrado";
			return false;
		}
		
		if( this.pojoArea == null ){
			this.resOperacion = "Debe seleccionar el área del empleado";
			return false;
		}
		
		if(this.fechaIngreso.toString().equals("")){
			this.resOperacion = "Debe seleccionar fecha de ingreso";
			return false;
		}

		if( this.idCategoria == 0 ){
			this.resOperacion = "Debe seleccionar Categoria";
			return false;
		}

		if(this.emergencia.trim().equals("") ){
			this.resOperacion = "Introduzca a quien llamar en caso de emergencia";
			return false;
		}
		
		
		
		return true;
	}
	
	private void setDatosPojoEmpleado(){

		this.pojoEmpleado.setHomonimo( this.pojoPersona.getHomonimo() );
		this.pojoEmpleado.setNombre( this.pojoPersona.getNombre() );
		this.pojoEmpleado.setPrimerNombre( this.pojoPersona.getPrimerNombre() );
		this.pojoEmpleado.setSegundoNombre( this.pojoPersona.getSegundoNombre() );
		this.pojoEmpleado.setNombresPropios( this.pojoPersona.getNombresPropios() );
		this.pojoEmpleado.setPrimerApellido( this.pojoPersona.getPrimerApellido() );
		this.pojoEmpleado.setSegundoApellido(this.pojoPersona.getSegundoApellido());
		
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getNumPaginaPersonas() {
		return numPaginaPersonas;
	}

	public void setNumPaginaPersonas(int numPaginaPersonas) {
		this.numPaginaPersonas = numPaginaPersonas;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public boolean isBand() {
		return band;
	}

	public void setBand(boolean band) {
		this.band = band;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}
	
	public Empleado getPojoEmpleadoMain() {
		return pojoEmpleadoMain;
	}

	public void setPojoEmpleadoMain(Empleado pojoEmpleadoMain) {
		this.pojoEmpleadoMain = pojoEmpleadoMain;
	}
	
	public EmpleadoExt getPojoEmpleado() {
		return pojoEmpleado;
	}

	public void setPojoEmpleado(EmpleadoExt pojoEmpleado) {
		
		this.pojoEmpleado = pojoEmpleado;
	}

	public List<Empleado> getListaEmpleadosGrid() {
		return listaEmpleadosGrid;
	}

	public void setListaEmpleadosGrid(List<Empleado> listaEmpleadosGrid) {
		this.listaEmpleadosGrid = listaEmpleadosGrid;
	}
		
	// ACCIONES PARA PERSONA
	// -----------------------------------------------------------------
	
	private void llenarTipoBusquedaPersonas(){

		tipoBusquedaPersonas = new ArrayList<SelectItem>();
		tipoBusquedaPersonas.add(new SelectItem("nombre", "Nombre"));
		tipoBusquedaPersonas.add(new SelectItem("id", "ID"));
		
		valBusquedaPersonas = tipoBusquedaPersonas.get(0).getDescription();
		
		campoBusquedaPersonas = "";
		
	}
	
	public void buscarPersonas(){
		int tipoPersonaFisica = 1;
		List<Persona> listaTotalPersonas = new ArrayList<>();
		
		try {
			listaPersonasGrid = new ArrayList<>();
			if( this.campoBusquedaPersonas.equals("nombre") ){
				if( this.valBusquedaPersonas.equals("") ){
					listaTotalPersonas = this.ifzClientes.buscarPersona("nombre", "");
				}else{
					listaTotalPersonas = this.ifzClientes.buscarPersona("nombre", this.valBusquedaPersonas);
				}
				
				for(Persona p: listaTotalPersonas){
					if(p.getTipoPersona() == tipoPersonaFisica){	//Si es una persona fisica
						this.listaPersonasGrid.add(p);
					}
				}	
			}else{
				if( this.valBusquedaPersonas.equals("") ){
					//this.listaPersonasGrid = this.ifzClientes.buscarPersona("id", "");
					listaTotalPersonas = this.ifzClientes.buscarPersona("id", "");
					
					for(Persona p: listaTotalPersonas){
						if(p.getTipoPersona() == tipoPersonaFisica){	//Si es una persona fisica
							this.listaPersonasGrid.add(p);
						}
					}
				}else{
					this.listaPersonasGrid = new ArrayList<Persona>();
					this.listaPersonasGrid.add( this.ifzClientes.buscarPersona( Long.valueOf( this.valBusquedaPersonas) ) );
				}
			}
		} catch (ExcepConstraint e) {
			log.error("Error en RecHum.EmpleadoAction.buscarPersonas", e);
		}
	}

	public List<Persona> getListaPersonasGrid() {
		return listaPersonasGrid;
	}

	public void setListaPersonasGrid(List<Persona> listaPersonasGrid) {
		this.listaPersonasGrid = listaPersonasGrid;
	}

	public void seleccionarPersona(){
		nombreEmpleado = pojoPersona.getNombre();
		this.pojoEmpleado.setPersona( this.pojoPersona );
	}

	public Persona getPojoPersona() {
		return pojoPersona;
	}

	public void setPojoPersona(Persona pojoPersona) {
		
		this.pojoPersona = pojoPersona;
	}

	public List<SelectItem> getTipoBusquedaPersonas() {
		return tipoBusquedaPersonas;
	}

	public void setTipoBusquedaPersonas(List<SelectItem> tipoBusquedaPersonas) {
		this.tipoBusquedaPersonas = tipoBusquedaPersonas;
	}

	public String getValBusquedaPersonas() {
		return valBusquedaPersonas;
	}

	public void setValBusquedaPersonas(String valBusquedaPersonas) {
		this.valBusquedaPersonas = valBusquedaPersonas;
	}

	public String getCampoBusquedaPersonas() {
		return campoBusquedaPersonas;
	}

	public void setCampoBusquedaPersonas(String campoBusquedaPersonas) {
		this.campoBusquedaPersonas = campoBusquedaPersonas;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}

	public int getIdArea() {
		return idArea;
	}

	public void setIdArea(int idArea) {
		areaValidador="";
		this.idArea = idArea;
		for(Area a: this.listaAreas){
			if ( a.getId() == idArea ){
				this.pojoArea = a;
				areaValidador = this.pojoArea.getDescripcion();
				break;
			}
		}
	}

	public int getIdPuesto() {
		return idPuesto;
	}

	public void setIdPuesto(int idPuesto) {

		this.idPuesto = idPuesto;
		
		for(Puesto p: this.listaPuestos){
			if ( p.getId() == idPuesto ){
				this.pojoPuesto = p;
				puestoValidador = this.pojoPuesto.getDescripcion();
				break;
			}
		}
		
		//Recargar las categorias que cumplan con el puesto
		this.filtrarCategorias(idPuesto);
		
	}
	
	private void filtrarCategorias(int idPuesto){
		if (idPuesto == 0 ) return;
		
		if (this.listaCboCategorias == null)
			this.listaCboCategorias = new ArrayList<SelectItem>();
		this.listaCboCategorias.clear();
		
		if (this.listaCategorias == null)
			this.listaCategorias = new ArrayList<Categoria>();
		this.listaCategorias.clear();
		
		List<PuestoCategoriaExt> listaPuestosCategorias = this.ifzPuestosCategorias.findByIdPuesto(idPuesto);
		for( PuestoCategoriaExt pc: listaPuestosCategorias ){
			this.listaCategorias.add(pc.getCategoria());
			this.listaCboCategorias.add( new SelectItem(pc.getCategoria().getId(), pc.getCategoria().getDescripcion()));
		}
	}
	
	public void cargaCategorias() {
		if (this.idPuesto > 0) {
			filtrarCategorias(this.idPuesto);
		}
	}

	public String getPuestoValidador() {
		return puestoValidador;
	}

	public void setPuestoValidador(String puestoValidador) {
		this.puestoValidador = puestoValidador;
	}

	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
		
		for(Categoria c: this.listaCategorias){
			if ( c.getId() == idCategoria ){
				this.pojoCategoria = c;
				categoriaValidador = this.pojoCategoria.getDescripcion();
				break;
			}
		}
	}

	public String getCategoriaValidador() {
		return categoriaValidador;
	}

	public void setCategoriaValidador(String categoriaValidador) {
		this.categoriaValidador = categoriaValidador;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getNumeroSeguridadSocial() {
		return numeroSeguridadSocial;
	}

	public void setNumeroSeguridadSocial(String numeroSeguridadSocial) {
		this.numeroSeguridadSocial = numeroSeguridadSocial;
	}

	public String getEmergencia() {
		return emergencia;
	}

	public void setEmergencia(String emergencia) {
		this.emergencia = emergencia;
	}

	public boolean isExterno() {
		return externo;
	}

	public void setExterno(boolean externo) {
		this.externo = externo;
	}
		
	//Metodos y setters para los combos del Nuevo/Agregar Empleado
	public void cargarAreas(){
		if ( this.listaAreas.isEmpty() ){
			this.listaAreas = this.ifzArea.findAllActivos();
		}
	}
	
	public void cargarCboAreas(){
		if (! this.listaAreas.isEmpty()){
			if (this.listaCboAreas == null)
				this.listaCboAreas = new ArrayList<SelectItem>();
			this.listaCboAreas.clear();
			
			for(Area a: this.listaAreas){
				this.listaCboAreas.add( new SelectItem( a.getId().toString(), a.getDescripcion() ) );
			}
		}
	}
	
	public void cargarCategorias(){
		if ( this.listaCategorias.isEmpty() ){
			this.listaCategorias = this.ifzCategorias.findAllActivos();
		}
	}
	
	public void cargarCboCategorias(){
		if(this.listaCboCategorias == null)
			this.listaCboCategorias = new ArrayList<SelectItem>();
		this.listaCboCategorias.clear();
	}
	
	public void cargarPuestos(){
		if ( this.listaPuestos.isEmpty() ){
			this.listaPuestos = this.ifzPuestos.findAllActivos();
		}
	}
	
	public void cargarCboPuestos(){
		if (! this.listaPuestos.isEmpty()) {
			if (this.listaCboPuestos == null)
				this.listaCboPuestos = new ArrayList<SelectItem>();
			this.listaCboPuestos.clear();
			
			for(Puesto p: this.listaPuestos){
				this.listaCboPuestos.add(new SelectItem(p.getId(), p.getDescripcion()));
			}
		}
	}
		
	public List<SelectItem> getListaCboAreas() {
		return listaCboAreas;
	}

	public void setListaCboAreas(List<SelectItem> listaCboAreas) {
		this.listaCboAreas = listaCboAreas;
	}

	public List<Area> getListaAreas() {
		return listaAreas;
	}

	public void setListaAreas(List<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}

	public Area getPojoArea() {
		return pojoArea;
	}

	public void setPojoArea(Area pojoArea) {
		this.pojoArea = pojoArea;
		areaValidador = this.pojoArea.getDescripcion();
	}

	public String getAreaValidador() {
		return areaValidador;
	}

	public void setAreaValidador(String areaValidador) {
		this.areaValidador = areaValidador;
	}

	public List<SelectItem> getListaCboPuestos() {
		return listaCboPuestos;
	}

	public void setListaCboPuestos(List<SelectItem> listaCboPuestos) {
		this.listaCboPuestos = listaCboPuestos;
	}

	public List<Puesto> getListaPuestos() {
		return listaPuestos;
	}

	public void setListaPuestos(List<Puesto> listaPuestos) {
		this.listaPuestos = listaPuestos;
	}

	public Puesto getPojoPuesto() {
		return pojoPuesto;
	}

	public void setPojoPuesto(Puesto pojoPuesto) {
		this.pojoPuesto = pojoPuesto;
	}

	public List<SelectItem> getListaCboCategorias() {
		return listaCboCategorias;
	}

	public void setListaCboCategorias(List<SelectItem> listaCboCategorias) {
		this.listaCboCategorias = listaCboCategorias;
	}

	public List<Categoria> getListaCategorias() {
		return listaCategorias;
	}

	public void setListaCategorias(List<Categoria> listaCategorias) {
		this.listaCategorias = listaCategorias;
	}

	public Categoria getPojoCategoria() {
		return pojoCategoria;
	}

	public void setPojoCategoria(Categoria pojoCategoria) {
		this.pojoCategoria = pojoCategoria;
	}

	public int getNumPaginaFamiliares() {
		return numPaginaFamiliares;
	}

	public void setNumPaginaFamiliares(int numPaginaFamiliares) {
		this.numPaginaFamiliares = numPaginaFamiliares;
	}

	public int getNumPaginaBeneficiarios() {
		return numPaginaBeneficiarios;
	}

	public void setNumPaginaBeneficiarios(int numPaginaBeneficiarios) {
		this.numPaginaBeneficiarios = numPaginaBeneficiarios;
	}

	public EmpleadoParienteExt getPojoFamiliar() {
		return pojoFamiliar;
	}

	public void setPojoFamiliar(EmpleadoParienteExt pojoFamiliar) {
		this.pojoFamiliar = pojoFamiliar;
	}

	public List<EmpleadoParienteExt> getListaFamiliaresGrid() {
		return listaFamiliaresGrid;
	}

	public void setListaFamiliaresGrid(List<EmpleadoParienteExt> listaFamiliaresGrid) {
		this.listaFamiliaresGrid = listaFamiliaresGrid;
	}

	public Persona getPojoFamiliarSeleccionado() {
		return pojoFamiliarSeleccionado;
	}

	public void setPojoFamiliarSeleccionado(Persona pojoFamiliarSeleccionado) {
		
		this.pojoFamElimSeleccionado.setPersona( pojoFamiliarSeleccionado );
		
		this.pojoFamiliarSeleccionado = pojoFamiliarSeleccionado;
	}

	public List<ConValores> getListRelacionesFamiliares() {
		return listaRelacionesFamiliares;
	}

	public void setListRelacionesFamiliares(List<ConValores> listRelacionesFamiliares) {
		this.listaRelacionesFamiliares = listRelacionesFamiliares;
	}

	public List<SelectItem> getListaCboRelaciones() {
		return listaCboRelaciones;
	}

	public void setListaCboRelaciones(List<SelectItem> listaCboRelaciones) {
		this.listaCboRelaciones = listaCboRelaciones;
	}

	public int getIdRelacion() {
		return idRelacion;
	}

	public void setIdRelacion(int idRelacion) {
		this.idRelacion = idRelacion;
		if(idRelacion==0)
			return;
		for(ConValores c: this.listaRelacionesFamiliares){
			if ( c.getId() == idRelacion ){
				this.pojoRelacion = c;
				if(this.pojoFamElimSeleccionado!=null)
					this.pojoFamElimSeleccionado.setRelacion( c );
				break;
			}
		}
	}
	
	public int getIdRelacionBeneficiario() {
		return idRelacionBeneficiario;
	}

	public void setIdRelacionBeneficiario(int idRelacionBeneficiario) {
		this.idRelacionBeneficiario = idRelacionBeneficiario;
		if(idRelacionBeneficiario==0)
			return;
		for(ConValores c: this.listaRelacionesBeneficiarios){
			if ( c.getId() == idRelacionBeneficiario ){
				if(this.pojoBeneficiario!=null){
					this.pojoBeneficiario.setRelacion( c );
				}
					
				break;
			}
		}
	}
	
	public EmpleadoParienteExt getPojoFamElimSeleccionado() {
		return pojoFamElimSeleccionado;
	}

	public void setPojoFamElimSeleccionado(EmpleadoParienteExt pojoFamElimSeleccionado) {
		this.pojoFamElimSeleccionado = pojoFamElimSeleccionado;
		this.setIdRelacion(  (int) pojoFamElimSeleccionado.getRelacion().getId() );
	}

	public List<EmpleadoBeneficiarioExt> getListaBeneficiariosGrid() {
		return listaBeneficiariosGrid;
	}

	public void setListaBeneficiariosGrid(List<EmpleadoBeneficiarioExt> listaBeneficiariosGrid) {
		this.listaBeneficiariosGrid = listaBeneficiariosGrid;
	}

	public EmpleadoBeneficiarioExt getPojoBeneficiario() {
		return pojoBeneficiario;
	}

	public void setPojoBeneficiario(EmpleadoBeneficiarioExt pojoBeneficiario) {
		this.pojoBeneficiario = pojoBeneficiario;
	}

	public Persona getPojoBeneficiarioSeleccionado() {
		return pojoBeneficiarioSeleccionado;
	}

	public void setPojoBeneficiarioSeleccionado(Persona pojoBeneficiarioSeleccionado) {
		this.pojoBeneficiarioSeleccionado = pojoBeneficiarioSeleccionado;
		this.pojoBeneficiario.setPersona( pojoBeneficiarioSeleccionado );
	}

	public String getMensajeErrorBeneficiario() {
		return mensajeErrorBeneficiario;
	}

	public void setMensajeErrorBeneficiario(String mensajeErrorBeneficiario) {
		this.mensajeErrorBeneficiario = mensajeErrorBeneficiario;
	}

	public boolean isBeneficiarioValidado() {
		return beneficiarioValidado;
	}

	public void setBeneficiarioValidado(boolean beneficiarioValidado) {
		this.beneficiarioValidado = beneficiarioValidado;
	}

	public EmpleadoContrato getPojoContrato() {
		return pojoContrato;
	}

	public void setPojoContrato(EmpleadoContrato pojoContrato) {
		this.pojoContrato = pojoContrato;
	}

	public long getIdPeridiocidadPago() {
		return idPeridiocidadPago;
	}

	public void setIdPeridiocidadPago(long idPeridiocidadPago) {
		this.idPeridiocidadPago = idPeridiocidadPago;
		for(ConValores p: this.listaPeridiocidad){
			if(p.getId() == idPeridiocidadPago){
				this.pojoContrato.setPeridiocidadPago(p.getId());
			}
		}
	}

	public int getIdDiaDescanso() {
		return idDiaDescanso;
	}

	public void setIdDiaDescanso(int idDiaDescanso) {
		this.idDiaDescanso = idDiaDescanso;
	}

	public List<SelectItem> getListaCboRelacionesBeneficiarios() {
		return listaCboRelacionesBeneficiarios;
	}

	public void setListaCboRelacionesBeneficiarios(List<SelectItem> listaCboRelacionesBeneficiarios) {
		this.listaCboRelacionesBeneficiarios = listaCboRelacionesBeneficiarios;
	}

	public List<SelectItem> getListaCboPeridiocidad() {
		return listaCboPeridiocidad;
	}

	public void setListaCboPeridiocidad(List<SelectItem> listaCboPeridiocidad) {
		this.listaCboPeridiocidad = listaCboPeridiocidad;
	}

	public List<SelectItem> getListaCboDias() {
		return listaCboDias;
	}

	public void setListaCboDias(List<SelectItem> listaCboDias) {
		this.listaCboDias = listaCboDias;
	}

	public List<EmpleadoContrato> getListaContratosGrid() {
		return listaContratosGrid;
	}

	public List<SelectItem> getListaCboSucursales() {
		return listaCboSucursales;
	}
	
	public void setListaCboSucursales(List<SelectItem> listaCboSucursales) {
		this.listaCboSucursales = listaCboSucursales;
	}
	
	public void setListaContratosGrid(List<EmpleadoContrato> listaContratosGrid) {
		this.listaContratosGrid = listaContratosGrid;
	}
	
	public String getClave(){
		return pojoEmpleado.getClave() == null ? "" : this.pojoEmpleado.getClave();
	}
	
	public void setClave(String clave){
		this.pojoEmpleado.setClave(clave);
	}
	
	public long getIdSucursal(){
		return this.pojoEmpleado.getSucursal() == null ? 0 : this.pojoEmpleado.getSucursal().getId();
	}
	
	public void setIdSucursal(long idSucursal){
		if(idSucursal > 0){
			for(Sucursal s:this.listaSucursales){
				if(s.getId()==idSucursal){
					this.pojoEmpleado.setSucursal(s);
					break;
				}
			}
		}	
	}

	public List<SelectItem> getListFormasPagoEmpleadosItems() {
		return listFormasPagoEmpleadosItems;
	}

	public void setListFormasPagoEmpleadosItems(List<SelectItem> listFormasPagoEmpleadosItems) {
		this.listFormasPagoEmpleadosItems = listFormasPagoEmpleadosItems;
	}

	public String getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public String getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}

	public String getHoraEntradaComplemento() {
		return horaEntradaComplemento;
	}

	public void setHoraEntradaComplemento(String horaEntradaComplemento) {
		this.horaEntradaComplemento = horaEntradaComplemento;
	}

	public String getHoraSalidaComplemento() {
		return horaSalidaComplemento;
	}

	public void setHoraSalidaComplemento(String horaSalidaComplemento) {
		this.horaSalidaComplemento = horaSalidaComplemento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmpleadoContrato getPojoContratoBorrar() {
		return pojoContratoBorrar;
	}

	public void setPojoContratoBorrar(EmpleadoContrato pojoContratoBorrar) {
		this.pojoContratoBorrar = pojoContratoBorrar;
	}

	public boolean isEsContratoNuevo() {
		return esContratoNuevo;
	}

	public void setEsContratoNuevo(boolean esContratoNuevo) {
		this.esContratoNuevo = esContratoNuevo;
	}

	public int getContratosPaginacion() {
		return contratosPaginacion;
	}

	public void setContratosPaginacion(int contratosPaginacion) {
		this.contratosPaginacion = contratosPaginacion;
	}
}

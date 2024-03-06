package net.giro.adp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.InsumoRow;
import net.giro.adp.beans.Insumos;
import net.giro.adp.beans.InsumosDetalles;
import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.adp.beans.InsumosExt;
import net.giro.adp.beans.InsumosTipo;
import net.giro.adp.beans.Obra;
import net.giro.adp.logica.InsumosDetallesRem;
import net.giro.adp.logica.InsumosRem;
import net.giro.adp.logica.ObraRem;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.comun.TipoInsumo;
import net.giro.inventarios.comun.TipoMaestro;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosGP;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;

import com.google.common.io.Files;

@ViewScoped
@ManagedBean(name="insumosAction")
public class ExplosionInsumosAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ExplosionInsumosAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	// Interfaces
	private InsumosRem ifzExpInsumos;
	private InsumosDetallesRem ifzInsumosDetalles;
	private ConGrupoValoresRem ifzGrupos;
	private ConValoresRem ifzConValores;
	private ReportesRem ifzReportes;
	// Listas 
	private List<Insumos> listExplosionInsumos;
	private List<InsumosDetallesExt> listMateriales;
	private List<InsumosDetallesExt> listManoDeObra;
	private List<InsumosDetallesExt> listHerramientas;
	private List<InsumosDetallesExt> listEquipos;
	private List<InsumosDetallesExt> listOtros;
	private List<InsumoRow> listProductos;
	private List<InsumosDetallesExt> listDetallesEliminados;
	private List<InsumosTipo> listInsumosItems;
	// POJO's
	private long idInsumo;
	private InsumosExt explosionInsumos;
	private ConGrupoValores pojoGpoMaestros;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	private int paginacionProductosNoEncontrados;
	private int paginacionMateriales;
	private int paginacionManoDeObra;
	private int paginacionHerramientas;
	private int paginacionEquipos;
	private int paginacionOtros;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private boolean incluirCanceladas;
	private int paginacionPrincipal;
	// Busqueda de obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	//private int valorOpcionBusquedaObras;
	private boolean buscarAdministrativo;
	private int paginacionObras;
	// Busqueda productos
	private ProductoRem ifzProducto;
	private List<Producto> listBusquedaProductos;
	private Producto pojoProducto;
	private List<SelectItem> tiposBusquedaProductos;
	private List<ConValores> listEspecialidades;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private long familiaBusquedaProductos;
	private int paginacionProductos;	
	private boolean permiteAgregarProducto;
	private double productoBusquedaCantidad;
	private double productoBusquedaPrecio;
	// control
    private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private byte[] fileSrc; 
	private String fileName;
	private String fileExtension;
	private int countOtros;
	private HashMap<String, String> insumosCellReference;
	private boolean escapeForInputMesssage;
	private boolean editable;
	// Moneda
	private MonedaDAO ifzMonedas;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private double tipoCambio;
	// PERMISOS
	private EmpleadoRem ifzEmpleados;
	private boolean permisoAgregar;
	private boolean permisoEditar;
	private boolean permisoBorrar;
	private boolean permisoImprimir;
	// Perfiles
	private boolean perfilAdministrativo;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;

	public ExplosionInsumosAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression dato = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			// Obtenemos el acceso
			dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) dato.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilAdministrativo = ((valPerfil != null && ! "".equals(valPerfil) && "S".equals(valPerfil)) ? true : false);
			
			ctx = new InitialContext();
			this.ifzExpInsumos = (InsumosRem) ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzInsumosDetalles = (InsumosDetallesRem) ctx.lookup("ejb:/Logica_GestionProyectos//InsumosDetallesFac!net.giro.adp.logica.InsumosDetallesRem");
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzProducto = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzGrupos = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzMonedas = (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			
			this.ifzExpInsumos.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzInsumosDetalles.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzProducto.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzGrupos.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzGrupos.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 

			this.ifzExpInsumos.showSystemOuts(false);
			this.ifzObras.showSystemOuts(false);
			
			// Inicializaciones
			this.explosionInsumos = new InsumosExt();
			this.listExplosionInsumos = new ArrayList<Insumos>();
			this.listObras = new ArrayList<Obra>();
			this.listMateriales = new ArrayList<InsumosDetallesExt>();
			this.listManoDeObra = new ArrayList<InsumosDetallesExt>();
			this.listHerramientas = new ArrayList<InsumosDetallesExt>();
			this.listEquipos = new ArrayList<InsumosDetallesExt>();
			this.listOtros = new ArrayList<InsumosDetallesExt>();
			this.listProductos = new ArrayList<InsumoRow>();
			this.listDetallesEliminados = new ArrayList<InsumosDetallesExt>();
			this.fileSrc = null;
			this.fileExtension = "";
			this.paginacionProductosNoEncontrados = 1;
			this.paginacionMateriales = 1;
			this.paginacionManoDeObra = 1;
			this.paginacionHerramientas = 1;
			this.paginacionEquipos = 1;
			this.paginacionOtros = 1;
			this.countOtros = 0;
			this.escapeForInputMesssage = true;
			
			// Busqueda principal
			this.paginacionPrincipal = 1;
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("nombreObra", "Obra"));
			this.tiposBusqueda.add(new SelectItem("idObra", "ID Obra"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			
			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
			
			// Busqueda productos
			this.tiposBusquedaProductos = new ArrayList<SelectItem>();
			this.tiposBusquedaProductos.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaProductos.add(new SelectItem("clave", "Codigo"));
			this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
			nuevaBusquedaProductos();
			
			// Obtenemos la lista de referencia de columnas para la carga de INSUMOS
			dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{insumos}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());
            Set<String> keys = this.entornoProperties.keySet();
            this.insumosCellReference = new HashMap<String, String>();
            for (String key : keys)
            	this.insumosCellReference.put(key.toUpperCase().trim(), this.entornoProperties.getString(key).toUpperCase().trim());
            
            // Obtenemos las propiedades de entorno del modulo
            dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());

			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;

			// Grupo de valores para MAESTROS de productos
			this.pojoGpoMaestros = this.ifzGrupos.findByName("SYS_CODE_NIVEL0");
			if (this.pojoGpoMaestros == null || this.pojoGpoMaestros.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_CODE_NIVEL0 (Maestros) en con_grupo_valores");
			
			// Grupo de valores para ESPECIALIDADES de productos
			this.pojoGpoEspecialidades = this.ifzGrupos.findByName("SYS_PRODUCTO_ESPECIALIDADES");
			if (this.pojoGpoEspecialidades == null || this.pojoGpoEspecialidades.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_PRODUCTO_ESPECIALIDADES en con_grupo_valores");

			// Grupo de valores para FAMILIAS de productos 
			this.pojoGpoFamilias = this.ifzGrupos.findByName("SYS_FAMILIA_PRODUCTO");
			if (this.pojoGpoFamilias == null || this.pojoGpoFamilias.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_FAMILIA_PRODUCTO en con_grupo_valores");

			this.permiteAgregarProducto = false;
			if ("ADMINISTRADOR JAVIITR".contains(loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()))
				this.permiteAgregarProducto = true;

			cargarMonedas();
			cargarFamilias();
			this.incluirCanceladas = false;
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
		} finally {
			establecerPermisos();
		}
	}
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			if (! this.isDebug)
				this.incluirCanceladas = false;
			
			this.paginacionPrincipal = 1;
			this.ifzExpInsumos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listExplosionInsumos = this.ifzExpInsumos.findLikeProperty(this.campoBusqueda, this.valorBusqueda, this.incluirCanceladas, 0);
			if (this.listExplosionInsumos == null || this.listExplosionInsumos.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la Busqueda de Insumos", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.editable = true;
			this.explosionInsumos = new InsumosExt();
			this.listMateriales = new ArrayList<InsumosDetallesExt>();
			this.listManoDeObra = new ArrayList<InsumosDetallesExt>();
			this.listHerramientas = new ArrayList<InsumosDetallesExt>();
			this.listEquipos = new ArrayList<InsumosDetallesExt>();
			this.listOtros = new ArrayList<InsumosDetallesExt>();
			this.countOtros = 0;
			

			this.tipoCambio = 1.0;
			if ((this.explosionInsumos.getIdMoneda() != this.loginManager.getInfoSesion().getEmpresa().getMonedaId()))
				this.tipoCambio = this.loginManager.getTipoCambioActual();
			this.explosionInsumos.setTipoCambio(this.tipoCambio);
			setIdMoneda(this.loginManager.getInfoSesion().getEmpresa().getMonedaId());
			
			subirExcel();
			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al intanciar una nueva Explosion de Insumos", e);
		}
	}
	
	public void editar() {
		/*List<InsumosDetalles> lista = null;
		List<InsumosDetallesExt> listAux = null;
		int detalles = 0;*/
		
		try {
			control();
			this.explosionInsumos = this.ifzExpInsumos.findByIdExt(this.idInsumo);
			if (this.explosionInsumos == null || this.explosionInsumos.getId() == null || this.explosionInsumos.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Explosion de Insumos indicada");
				return;
			}

    		controlLog("Validando permiso de escritura");
			this.editable = false;
    		validarPermisos();
			this.tipoCambio = this.explosionInsumos.getTipoCambio();

			// Recuperamos los detalles del insumo
			detallesExplosionInsumos();

			/*this.editable = false;
			this.countOtros = 0;
			this.listMateriales.clear();
			this.listManoDeObra.clear();
			this.listHerramientas.clear();
			this.listEquipos.clear();
			this.listOtros.clear();
			
    		controlLog("Validando permiso de escritura");
    		validarPermisos();
			this.tipoCambio = this.explosionInsumos.getTipoCambio();
			
			// Recuperamos los detalles del insumo
			controlLog("Obteniendo detalles de insumo");
			this.ifzInsumosDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			lista = this.ifzInsumosDetalles.findByProperty("idInsumo", this.explosionInsumos.getId(), 0);
			if (lista != null && ! lista.isEmpty()) {
				detalles = lista.size();
				controlLog("Extendiendo detalles de insumo");
				listAux = this.ifzInsumosDetalles.extenderInsumosDetalles(lista);
				Collections.sort(listAux, new Comparator<InsumosDetallesExt>() {
					@Override
					public int compare(InsumosDetallesExt o1, InsumosDetallesExt o2) {
						return o1.getIdProducto().getClave().compareTo(o2.getIdProducto().getClave());
					}
				});
				controlLog("Clasificando detalles de insumo");
				for (InsumosDetallesExt var : listAux) {
					if (var.getTipo() == 1) 
						this.listMateriales.add(var);
					else if (var.getTipo() == 2) 
						this.listManoDeObra.add(var);
					else if (var.getTipo() == 3) 
						this.listHerramientas.add(var);
					else if (var.getTipo() == 5) 
						this.listEquipos.add(var);
					else 
						this.listOtros.add(var);
				}
				
				this.countOtros = this.listOtros.size();
				this.listInsumosItems = new ArrayList<InsumosTipo>();
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Material.name(), this.explosionInsumos.getMontoMateriales(), this.listMateriales));
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.ManoDeObra.name(), this.explosionInsumos.getMontoManoDeObra(), this.listManoDeObra));
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Herramienta.name(), this.explosionInsumos.getMontoHerramientas(), this.listHerramientas));
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Equipo.name(), this.explosionInsumos.getMontoEquipos(), this.listEquipos));
				if (this.countOtros > 0)
					this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Otros.name(), this.explosionInsumos.getMontoOtros(), this.listOtros));
			}
			
			controlLog(detalles  + " detalles de insumo obtenidos");*/
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Explosion de Insumos", e);
		}
	}
	
	public void actualizar() {
		Producto producto = null;
		int indexInsumoDet = 0;
		double monto = 0;
		
		try {
			control();
			controlLog("Guardando insumos");

			monto = 0;
			controlLog("Guardando detalles: MATERIALES");
			for (InsumosDetallesExt var : this.listMateriales) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				monto += var.getMonto().doubleValue();
				var.setIdInsumos(this.explosionInsumos);
				
				producto = this.ifzProducto.convertir(var.getIdProducto());
				producto.setId(this.ifzProducto.save(producto));
				
				var.setIdProducto(this.ifzProducto.convertir(producto));
				var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				var.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumosDetalles.update(var);
			}
			
			this.explosionInsumos.setMontoMateriales(new BigDecimal(monto));
			controlLog("Guardando detalles: MATERIALES ... OK");
			monto = 0;
			
			controlLog("Guardando detalles: MANO DE OBRA");
			for (InsumosDetallesExt var : this.listManoDeObra) {
				indexInsumoDet = this.listManoDeObra.indexOf(var);
				monto += var.getMonto().doubleValue();
				var.setIdInsumos(this.explosionInsumos);
				
				producto = this.ifzProducto.convertir(var.getIdProducto());
				producto.setId(0L);
				producto.setClave("");
				producto.setEspecialidad(10002580l);
				producto.setDescEspecialidad("ACTIVOS");
				producto.setFamilia(10002595l);
				producto.setDescFamilia("OBRA EN PROCESO");
				producto.setSubfamilia(10002634l);
				producto.setDescSubfamilia("CHAMETLA");
				producto.setTipo(TipoMaestro.Administrativo.ordinal());
				producto.setId(this.ifzProducto.save(producto));
				
				var.setIdProducto(this.ifzProducto.convertir(producto));
				var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				var.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumosDetalles.update(var);
			}
			
			this.explosionInsumos.setMontoManoDeObra(new BigDecimal(monto));
			controlLog("Guardando detalles: MANO DE OBRA ... OK");
			monto = 0;
			
			controlLog("Guardando detalles: HERRAMIENTAS");
			for (InsumosDetallesExt var : this.listHerramientas) {
				indexInsumoDet = this.listHerramientas.indexOf(var);
				monto += var.getMonto().doubleValue();
				var.setIdInsumos(this.explosionInsumos);
				
				producto = this.ifzProducto.convertir(var.getIdProducto());
				producto.setId(0L);
				producto.setClave("");
				producto.setEspecialidad(10002580l);
				producto.setDescEspecialidad("ACTIVOS");
				producto.setFamilia(10002595l);
				producto.setDescFamilia("OBRA EN PROCESO");
				producto.setSubfamilia(10002634l);
				producto.setDescSubfamilia("CHAMETLA");
				producto.setTipo(TipoMaestro.Administrativo.ordinal());
				producto.setId(this.ifzProducto.save(producto));
				
				var.setIdProducto(this.ifzProducto.convertir(producto));
				var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				var.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumosDetalles.update(var);
			}
			
			this.explosionInsumos.setMontoHerramientas(new BigDecimal(monto));
			controlLog("Guardando detalles: HERRAMIENTAS ... OK");
			monto = 0;
			
			controlLog("Guardando detalles: EQUIPOS");
			for (InsumosDetallesExt var : this.listEquipos) {
				indexInsumoDet = this.listEquipos.indexOf(var);
				monto += var.getMonto().doubleValue();
				var.setIdInsumos(this.explosionInsumos);
				
				producto = this.ifzProducto.convertir(var.getIdProducto());
				producto.setId(0L);
				producto.setClave("");
				producto.setEspecialidad(10002580l);
				producto.setDescEspecialidad("ACTIVOS");
				producto.setFamilia(10002595l);
				producto.setDescFamilia("OBRA EN PROCESO");
				producto.setSubfamilia(10002634l);
				producto.setDescSubfamilia("CHAMETLA");
				producto.setTipo(TipoMaestro.Administrativo.ordinal());
				producto.setId(this.ifzProducto.save(producto));
				
				var.setIdProducto(this.ifzProducto.convertir(producto));
				var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				var.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumosDetalles.update(var);
			}
			
			this.explosionInsumos.setMontoEquipos(new BigDecimal(monto));
			controlLog("Guardando detalles: EQUIPOS ... OK");
			monto = 0;
			
			controlLog("Guardando detalles: OTROS");
			for (InsumosDetallesExt var : this.listOtros) {
				indexInsumoDet = this.listOtros.indexOf(var);
				monto += var.getMonto().doubleValue();
				var.setIdInsumos(this.explosionInsumos);
				
				producto = this.ifzProducto.convertir(var.getIdProducto());
				producto.setId(0L);
				producto.setClave("");
				producto.setEspecialidad(10002580l);
				producto.setDescEspecialidad("ACTIVOS");
				producto.setFamilia(10002595l);
				producto.setDescFamilia("OBRA EN PROCESO");
				producto.setSubfamilia(10002634l);
				producto.setDescSubfamilia("CHAMETLA");
				producto.setTipo(TipoMaestro.Administrativo.ordinal());
				producto.setId(this.ifzProducto.save(producto));
				
				var.setIdProducto(this.ifzProducto.convertir(producto));
				var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				var.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumosDetalles.update(var);
			}
			
			this.explosionInsumos.setMontoOtros(new BigDecimal(monto));
			controlLog("Guardando detalles: OTROS ... OK");
			monto = 0;
			
			this.explosionInsumos.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.explosionInsumos.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzExpInsumos.update(this.explosionInsumos);
			
			this.mensaje = "OK";
			controlLog("INSUMOS GUARDADOS");
		} catch (Exception e) {
			control("Ocurrio un problema al actualizar la Explosion de Insumos\n\nUltimo indice procesado " + indexInsumoDet, e);
		}
	}
	
	public void guardar() {
		List<Producto> listProductos = null;
		Insumos insumo = null;
		Producto prod = null;
		boolean esNuevo = false;
		int indexInsumoDet = 0;
		// ------------------------------------
		boolean reemplazar = false;
		long idInsumosNueva = 0;
		long idInsumosPrevia = 0;
		
		try {
			control();
			controlLog("Guardando insumos");
			// Comprobamos que no halla insumos con la obra indicada
			controlLog("Comprobamos insumos");
			if (comprobarInsumos()) {
				if (validaRequest("REPLACE") && "ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario()))
					reemplazar = true;
				if (! reemplazar) {
					control(5, "Ya existen insumos con la obra especificada");
					return;
				}
			}
			
			if (reemplazar) {
				// Cancelamos la Explosion de Insumos previa
				insumo = this.ifzExpInsumos.findActual(this.explosionInsumos.getIdObra().getId());
				insumo.setEstatus(1);
				insumo.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				insumo.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzExpInsumos.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzExpInsumos.update(insumo);
				idInsumosPrevia = insumo.getId();
				insumo = null;
			}
			
			// Comprobamos si no hay detalles para eliminar
			if (this.listDetallesEliminados != null && ! this.listDetallesEliminados.isEmpty()) {
				for (InsumosDetallesExt var : this.listDetallesEliminados) {
					if (var.getId() != null && var.getId() > 0L)
						this.ifzInsumosDetalles.delete(var.getId());
				}
				
				this.listDetallesEliminados.clear();
			}
			
			controlLog("Guardando insumo");
			this.explosionInsumos.setTipoCambio(this.tipoCambio);
			this.explosionInsumos.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			if (this.explosionInsumos.getId() == null || this.explosionInsumos.getId() <= 0L) {
				// Nuevo INSUMOS
				esNuevo = true;
				this.explosionInsumos.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.explosionInsumos.setFechaCreacion(Calendar.getInstance().getTime());
				this.explosionInsumos.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.explosionInsumos.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos el encabezado de los INSUMOS
				this.ifzExpInsumos.setInfoSesion(this.loginManager.getInfoSesion());
				this.explosionInsumos.setId(this.ifzExpInsumos.save(this.explosionInsumos));
			} else {
				// Actualizacion de INSUMOS
				this.explosionInsumos.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.explosionInsumos.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzExpInsumos.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzExpInsumos.update(this.explosionInsumos);
			}
			
			// Guardamos los detalles de los INSUMOS
			idInsumosNueva = this.explosionInsumos.getId();
			
			// MATERIALES
			controlLog("Guardando detalles: MATERIALES");
			this.ifzInsumosDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			for (InsumosDetallesExt var : this.listMateriales) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.explosionInsumos);
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());

					var.setId(this.ifzInsumosDetalles.save(var));
				} else {
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzInsumosDetalles.update(var);
				}
				
				// Actualizamos el producto
				controlLog("Actualizamo tipos producto: TipoMaestro.Inventario, TipoInsumo.Material");
				prod = this.ifzProducto.convertir(var.getIdProducto());
				prod = productoFromInsumo(prod, TipoMaestro.Inventario, TipoInsumo.Material);
				/*prod.setTipo(TipoMaestro.Inventario.ordinal());
				prod.setTipoInsumo(TipoInsumo.Material.ordinal());
				prod.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				prod.setFechaModificacion(Calendar.getInstance().getTime());*/
				
				listProductos = listProductos != null ? listProductos : new ArrayList<Producto>();
				listProductos.add(prod);
			}
			
			// MANO DE OBRA
			controlLog("Guardando detalles: MANO DE OBRA");
			this.ifzInsumosDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			for (InsumosDetallesExt var : this.listManoDeObra) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.explosionInsumos);
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					var.setId(this.ifzInsumosDetalles.save(var));
				} else {
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzInsumosDetalles.update(var);
				}
				
				// Actualizamos el producto
				controlLog("Actualizamo tipos producto: TipoMaestro.Inventario, TipoInsumo.ManoDeObra");
				prod = this.ifzProducto.convertir(var.getIdProducto());
				prod = productoFromInsumo(prod, TipoMaestro.Inventario, TipoInsumo.ManoDeObra);
				/*prod.setTipo(TipoMaestro.Inventario.ordinal());
				prod.setTipoInsumo(TipoInsumo.ManoDeObra.ordinal());
				prod.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				prod.setFechaModificacion(Calendar.getInstance().getTime());*/
				
				listProductos = listProductos != null ? listProductos : new ArrayList<Producto>();
				listProductos.add(prod);
			}
			
			// HERRAMIENTAS
			controlLog("Guardando detalles: HERRAMIENTAS");
			this.ifzInsumosDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			for (InsumosDetallesExt var : this.listHerramientas) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.explosionInsumos);
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					var.setId(this.ifzInsumosDetalles.save(var));
				} else {
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzInsumosDetalles.update(var);
				}
				
				// Actualizamos el producto
				controlLog("Actualizamo tipos producto: TipoMaestro.Inventario, TipoInsumo.Herramienta");
				prod = this.ifzProducto.convertir(var.getIdProducto());
				prod = productoFromInsumo(prod, TipoMaestro.Inventario, TipoInsumo.Herramienta);
				/*prod.setTipo(TipoMaestro.Inventario.ordinal());
				prod.setTipoInsumo(TipoInsumo.Herramienta.ordinal());
				prod.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				prod.setFechaModificacion(Calendar.getInstance().getTime());*/

				listProductos = listProductos != null ? listProductos : new ArrayList<Producto>();
				listProductos.add(prod);
			}
			
			// EQUIPOS
			controlLog("Guardando detalles: EQUIPOS");
			this.ifzInsumosDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			for (InsumosDetallesExt var : this.listEquipos) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.explosionInsumos);
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					var.setId(this.ifzInsumosDetalles.save(var));
				} else {
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzInsumosDetalles.update(var);
				}
				
				// Actualizamos el producto
				controlLog("Actualizamo tipos producto: TipoMaestro.Inventario, TipoInsumo.Equipo");
				prod = this.ifzProducto.convertir(var.getIdProducto());
				prod = productoFromInsumo(prod, TipoMaestro.Inventario, TipoInsumo.Equipo);
				/*prod.setTipo(TipoMaestro.Inventario.ordinal());
				prod.setTipoInsumo(TipoInsumo.Equipo.ordinal());
				prod.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				prod.setFechaModificacion(Calendar.getInstance().getTime());*/
				
				listProductos = listProductos != null ? listProductos : new ArrayList<Producto>();
				listProductos.add(prod);
			}
			
			// OTROS
			controlLog("Guardando detalles: OTROS");
			this.ifzInsumosDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			for (InsumosDetallesExt var : this.listOtros) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.explosionInsumos);
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					var.setId(this.ifzInsumosDetalles.save(var));
				} else {
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzInsumosDetalles.update(var);
				}
				
				// Actualizamos el producto
				controlLog("Actualizamo tipos producto: TipoMaestro.Inventario, TipoInsumo.Otros");
				prod = this.ifzProducto.convertir(var.getIdProducto());
				prod = productoFromInsumo(prod, TipoMaestro.Inventario, TipoInsumo.Otros);
				/*prod.setTipo(TipoMaestro.Inventario.ordinal());
				prod.setTipoInsumo(TipoInsumo.Otros.ordinal());
				prod.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				prod.setFechaModificacion(Calendar.getInstance().getTime());*/
				
				listProductos = listProductos != null ? listProductos : new ArrayList<Producto>();
				listProductos.add(prod);
			}
			
			if (listProductos != null && ! listProductos.isEmpty()) {
				this.ifzProducto.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzProducto.saveOrUpdateList(listProductos);
			}
			
			// Añadimos/Actualizamos a la lista
			controlLog("Añadimos/Actualizamos a la lista");
			insumo = this.ifzExpInsumos.findById(this.explosionInsumos.getId());
			if (esNuevo) 
				this.listExplosionInsumos.add(insumo);
			for (Insumos var : this.listExplosionInsumos) {
				if (this.explosionInsumos.getId().longValue() != var.getId().longValue())
					continue;
				var = insumo;
				break;
			}
			
			this.mensaje = "OK";
			controlLog("INSUMOS GUARDADOS");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Explosion de Insumos.\n\nUltimo indice procesado " + indexInsumoDet, e);
		} finally {
			if (reemplazar && idInsumosPrevia > 0L)
				actualizacionSuministrosInsumos(idInsumosNueva, idInsumosPrevia);
		}
	}
	
	public void eliminar() {
		Respuesta respuesta = null;
		Insumos explosionInsumos = null;
		
		try {
			control();
			if (this.idInsumo <= 0L) {
				control(-1, "No indico una Explosion de Insumos para Cancelar");
				return;
			}

			this.ifzExpInsumos.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzExpInsumos.cancelar(this.idInsumo);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				controlLog("CANCELAR EXPLOSION DE INSUMOS: " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, "Validacion de Cancelacion.\n" + respuesta.getErrores().getDescError() + ".\nNo se puede Cancelar");
				return;
			}

			// Buscamos la Explosion de Insumos en la lista
			explosionInsumos = (Insumos) respuesta.getBody().getValor("explosionInsumos");
			for (Insumos insumos : this.listExplosionInsumos) {
				if (insumos.getId() == explosionInsumos.getId()) {
					insumos = explosionInsumos;
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Cancelar la Explosion de Insumos", e);
		}
	}

	public void exportar() {
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			this.explosionInsumos = this.ifzExpInsumos.findByIdExt(this.idInsumo);
			if (this.explosionInsumos == null || this.explosionInsumos.getId() == null || this.explosionInsumos.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Explosion de Insumos indicada");
				return;
			}

			controlLog("Imprimiento reporte Explosion de Insumos ... ");
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idObra", this.explosionInsumos.getIdObra().getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  "189");
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				controlLog("ERROR INTERNO - " + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al intentar imprimir la Explosion de Insumos\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 
			
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "EXP-INSUMOS-O-" + this.explosionInsumos.getIdObra().getId() + "." + formatoDocumento;
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				controlLog("ERROR INTERNO - No se recupero el contenido de la Explosion de Insumos");
				control(-1, "Ocurrio un problema al intentar imprimir la Explosion de Insumos");
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			controlLog("Reporte Explosion de Insumos lanzado. Proceso terminado!");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la Explosion de Insumos", e);
		} 
	}
	
	public void reporte() {
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			this.explosionInsumos = this.ifzExpInsumos.findByIdExt(this.idInsumo);
			if (this.explosionInsumos == null || this.explosionInsumos.getId() == null || this.explosionInsumos.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Explosion de Insumos indicada");
				return;
			}

			controlLog("Imprimiento reporte Explosion de Insumos ... ");
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idObra", this.explosionInsumos.getIdObra().getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  "180");
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				controlLog("ERROR INTERNO - " + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al intentar imprimir la Explosion de Insumos\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 
			
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "OC-INSUMOS-O-" + this.explosionInsumos.getIdObra().getId() + "." + formatoDocumento;
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				controlLog("ERROR INTERNO - No se recupero el contenido de la Explosion de Insumos");
				control(-1, "Ocurrio un problema al intentar imprimir la Explosion de Insumos");
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			controlLog("Reporte Explosion de Insumos lanzado. Proceso terminado!");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la Explosion de Insumos", e);
		} 
	}

	private Producto productoFromInsumo(Producto prod, TipoMaestro maestro, TipoInsumo tipoInsumo) {
		if (prod == null)
			return null;
		prod.setTipo(maestro.ordinal());
		prod.setTipoInsumo(tipoInsumo.ordinal());
		prod.setIdEmpresa(prod.getIdEmpresa() > 0L ? prod.getIdEmpresa() : this.loginManager.getInfoSesion().getEmpresa().getId());
		prod.setIdMoneda(prod.getIdMoneda() > 0L ? prod.getIdMoneda() : this.loginManager.getInfoSesion().getEmpresa().getMonedaId());
		prod.setTipoCambio(prod.getTipoCambio() > 0 ? prod.getTipoCambio() : 1);
		if (this.loginManager.getInfoSesion().getEmpresa().getMonedaId() != prod.getIdMoneda() && prod.getTipoCambio() <= 1) 
			prod.setTipoCambio(this.loginManager.getTipoCambioActual()); 
		prod.setPrecioCompraPesos(prod.getPrecioCompra() * prod.getTipoCambio());
		prod.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
		prod.setFechaModificacion(Calendar.getInstance().getTime());
		return prod;
	}
	
	private void detallesExplosionInsumos() {
		List<InsumosDetalles> lista = null;
		List<InsumosDetallesExt> listAux = null;
		int detalles = 0;
		
		try {
			this.listMateriales = new ArrayList<InsumosDetallesExt>();
			this.listManoDeObra = new ArrayList<InsumosDetallesExt>();
			this.listHerramientas = new ArrayList<InsumosDetallesExt>();
			this.listEquipos = new ArrayList<InsumosDetallesExt>();
			this.listOtros = new ArrayList<InsumosDetallesExt>();
	
			// Recuperamos los detalles del insumo
			controlLog("Obteniendo detalles de Explosion de Insumos");
			this.ifzInsumosDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			lista = this.ifzInsumosDetalles.findByProperty("idInsumo", this.explosionInsumos.getId(), 0);
			if (lista != null && ! lista.isEmpty()) {
				detalles = lista.size();
				controlLog("Extendiendo y ordenando detalles de Explosion de Insumos");
				listAux = this.ifzInsumosDetalles.extenderInsumosDetalles(lista);
				Collections.sort(listAux, new Comparator<InsumosDetallesExt>() {
					@Override
					public int compare(InsumosDetallesExt o1, InsumosDetallesExt o2) {
						return o1.getIdProducto().getClave().compareTo(o2.getIdProducto().getClave());
					}
				});
				
				controlLog("Clasificando detalles de Explosion de Insumos");
				for (InsumosDetallesExt var : listAux) {
					if (var.getTipo() == 1) 
						this.listMateriales.add(var);
					else if (var.getTipo() == 2) 
						this.listManoDeObra.add(var);
					else if (var.getTipo() == 3) 
						this.listHerramientas.add(var);
					else if (var.getTipo() == 5) 
						this.listEquipos.add(var);
					else 
						this.listOtros.add(var);
				}
				
				this.countOtros = this.listOtros.size();
				this.listInsumosItems = new ArrayList<InsumosTipo>();
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Material.name(), this.explosionInsumos.getMontoMateriales(), this.listMateriales));
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.ManoDeObra.name(), this.explosionInsumos.getMontoManoDeObra(), this.listManoDeObra));
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Herramienta.name(), this.explosionInsumos.getMontoHerramientas(), this.listHerramientas));
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Equipo.name(), this.explosionInsumos.getMontoEquipos(), this.listEquipos));
				if (this.countOtros > 0)
					this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Otros.name(), this.explosionInsumos.getMontoOtros(), this.listOtros));
			}
			
			controlLog(detalles  + " detalles de Explosion de Insumos obtenidos");
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los detalles de la Explosion de Insumos", e);
		}
	}
	
	private void actualizacionSuministrosInsumos(Long insumoActual, Long insumoPrevio) {
		MensajeTopic msgTopic = null;
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			// Validamos datos
			controlLog("Validamos datos ... ");
			if ((insumoActual == null || insumoActual <= 0L) || (insumoPrevio == null || insumoPrevio <= 0L))
				return;

			controlLog("Preparamos evento ... ");
			target = insumoActual.toString();
			referencia = insumoPrevio.toString();

			controlLog("Lanzando evento ... ");
			msgTopic = new MensajeTopic(TopicEventosGP.INSUMOS_TRASPASAR_SUMINISTROS, target, referencia, atributos, this.loginManager.getInfoSesion());
			msgTopic.enviar();
			controlLog("Evento lanzado!");
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			control("Ocurrio un problema al lanzar la actualizacion de suministros entre Explosiones de Insumos (topic/GP:EI_AS)\n\n" + comando + "\n\n", e);
		}
	}

	private boolean validaRequest(String param) {
		return validaRequest(param, null);
	}
	
	private boolean validaRequest(String param, String refValue) {
		if (! this.isDebug)
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		if (refValue == null || "".equals(refValue.trim()))
			return true;
		
		return (refValue.trim().equals(this.paramsRequest.get(param).trim()));
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

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "Ocurrio un problema no controlado al realizar la operacion");
		this.escapeForInputMesssage = ! mensaje.contains("\n");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje + "\n");
	}

    // --------------------------------------------------------------------------------------
	// BUSQUEDA OBRAS
    // --------------------------------------------------------------------------------------
	
	public void nuevaBusquedaObras() {
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		//this.valorOpcionBusquedaObras = 0;
		this.buscarAdministrativo = false;
		this.paginacionObras = 1;
		
		this.listObras = new ArrayList<Obra>();
		this.pojoObra = null;
	}

	public void buscarObras() {
		int tipoObra = 0;
		
		try {
			control();
			tipoObra = this.buscarAdministrativo ? 4 : 0;
			this.campoBusquedaObras = (this.campoBusquedaObras != null && ! "".equals(this.campoBusquedaObras.trim())) ? this.campoBusquedaObras.trim() : this.tiposBusquedaObras.get(0).getValue().toString();
			this.valorBusquedaObras = (this.valorBusquedaObras != null && ! "".equals(this.valorBusquedaObras.trim())) ? this.valorBusquedaObras.trim() : "";
			/*if ("".equals(String.valueOf(this.valorOpcionBusquedaObras)))
				this.valorOpcionBusquedaObras = 0;
			
			if (this.campoBusquedaObras == null || "".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();

			tipoObra = 0;
			if (this.buscarAdministrativo)
				tipoObra = 4;

			this.paginacionObras = 1;*/
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, tipoObra, false, "", 0);
			if (this.listObras == null || this.listObras.isEmpty())
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
		} finally {
			this.paginacionObras = 1;
			this.listObras = this.listObras != null ? this.listObras : new ArrayList<Obra>();
		}
	}
	
	public void seleccionarObra() {
		try {
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Obra seleccionada");
				return;
			}
			
			this.tipoCambio = 1.0;
			this.explosionInsumos.setIdObra(this.pojoObra);
			setIdMoneda(this.pojoObra.getIdMoneda());
			if ((this.explosionInsumos.getIdMoneda() != this.loginManager.getInfoSesion().getEmpresa().getMonedaId()))
				this.tipoCambio = this.loginManager.getTipoCambioActual();
			this.explosionInsumos.setTipoCambio(this.tipoCambio);
			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
		}
	}

    // --------------------------------------------------------------------------------------
	// INSUMOS: ARCHIVO EXCEL
    // --------------------------------------------------------------------------------------
	
	public void subirExcel() {
		this.fileSrc = null;
		this.fileName = "";
		this.fileExtension = "";
		this.countOtros = 0;
	}
	
	public void uploadListener(FileUploadEvent event) throws Exception {
		try {
			control();
			this.fileSrc = event.getUploadedFile().getData();
			this.fileName = stripExtension(event.getUploadedFile().getName());
			this.fileExtension = Files.getFileExtension(event.getUploadedFile().getName());
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar el Libro de Explosion de Insumos", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void importar() throws Exception {
		Respuesta respuesta = null;
		String prodsNotFound = "";
		
		try {
			control();
			if (this.fileSrc == null)
				return;
			this.fileName = (this.fileName == null || "".equals(this.fileName.trim())) ? "archivo" : this.fileName.trim();
			this.fileExtension = (this.fileExtension == null || "".equals(this.fileExtension.trim())) ? "xls" : this.fileExtension.trim();
			
			controlLog("Procesando archivo: " + this.fileName);
			this.ifzExpInsumos.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzExpInsumos.importar(this.fileSrc, this.fileExtension, this.insumosCellReference);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al procesar la Explosion de Insumos.\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}

			this.explosionInsumos = (InsumosExt) respuesta.getBody().getValor("explosionInsumos");
			this.explosionInsumos.setNombreArchivo(this.fileName);
			this.listMateriales = (List<InsumosDetallesExt>) respuesta.getBody().getValor("materiales");
			this.listManoDeObra = (List<InsumosDetallesExt>) respuesta.getBody().getValor("manoDeObra");
			this.listHerramientas = (List<InsumosDetallesExt>) respuesta.getBody().getValor("herramientas");
			this.listEquipos = (List<InsumosDetallesExt>) respuesta.getBody().getValor("equipos");
			this.listOtros = (List<InsumosDetallesExt>) respuesta.getBody().getValor("otros");
			this.listProductos = (List<InsumoRow>) respuesta.getBody().getValor("productos");
			this.countOtros = this.listOtros.size();
			
			if (this.listProductos != null && ! this.listProductos.isEmpty()) {
				for (InsumoRow var : this.listProductos)
					prodsNotFound += "\n" + var.getClave() + " - " + var.getDescripcion();
				control(4, "Maestro NO Actualizado.\nNo se encontraton " + this.listProductos.size() + " productos" + prodsNotFound);
			}

			this.listInsumosItems = new ArrayList<InsumosTipo>();
			this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Material.name(), this.explosionInsumos.getMontoMateriales(), this.listMateriales));
			this.listInsumosItems.add(new InsumosTipo(TipoInsumo.ManoDeObra.name(), this.explosionInsumos.getMontoManoDeObra(), this.listManoDeObra));
			this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Herramienta.name(), this.explosionInsumos.getMontoHerramientas(), this.listHerramientas));
			this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Equipo.name(), this.explosionInsumos.getMontoEquipos(), this.listEquipos));
			if (this.countOtros > 0)
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Otros.name(), this.explosionInsumos.getMontoOtros(), this.listOtros));
		} catch (Exception e) {
			control("Ocurrio un problema al procesar la Explosion de Insumos", e);
		}
	}
	
	public boolean comprobarInsumos() {
		if (this.explosionInsumos.getIdObra() == null || this.explosionInsumos.getIdObra().getId() == null || this.explosionInsumos.getIdObra().getId() <= 0L)
			return true;
		return this.ifzExpInsumos.comprobarInsumo(this.explosionInsumos.getIdObra().getId());
	}
	
 	private String stripExtension(String str) {
		int pos = -1;
		
        if (str == null) 
        	return null;

        pos = str.lastIndexOf(".");
        if (pos == -1) 
        	return str;

        return str.substring(0, pos);
    }

    // --------------------------------------------------------------------------------------
	// Busqueda Productos
    // --------------------------------------------------------------------------------------
	
	public void nuevaBusquedaProductos() {
		control();
    	this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
		this.valorBusquedaProductos = "";
		this.familiaBusquedaProductos = 0;
		this.paginacionProductos = 1;
		
		this.listBusquedaProductos = new ArrayList<Producto>();
		this.pojoProducto = null;
		this.productoBusquedaCantidad = 1;
		this.productoBusquedaPrecio = 0;
    }
	
	public void buscarProductos() {
		int tipoMaestro = 0;
		
		try {
    		control();
			this.campoBusquedaProductos = (this.campoBusquedaProductos != null && ! "".equals(this.campoBusquedaProductos.trim())) ? this.campoBusquedaProductos.trim() : this.tiposBusquedaProductos.get(0).getValue().toString();
			this.valorBusquedaProductos = (this.valorBusquedaProductos != null && ! "".equals(this.valorBusquedaProductos.trim())) ? this.valorBusquedaProductos.trim() : "";
			if ("".equals(this.valorBusquedaProductos.trim())) {
	    		controlLog("Busqeueda de Productos. Sin criterio de busqueda");
				control(-1, "Debe indicar un criterio de busqueda");
				return;
			} 

			this.familiaBusquedaProductos = this.familiaBusquedaProductos > 0L ? this.familiaBusquedaProductos : 0L;
			if (this.explosionInsumos.getIdObra() != null && this.explosionInsumos.getIdObra().getId() != null && this.explosionInsumos.getIdObra().getId() > 0L)
				tipoMaestro = (this.explosionInsumos.getIdObra().getTipoObra() == 4 ? TipoMaestro.Administrativo.ordinal() : TipoMaestro.Inventario.ordinal());
			
			this.ifzProducto.setInfoSesion(this.loginManager.getInfoSesion());
			this.listBusquedaProductos = this.ifzProducto.findLikeProperty(this.campoBusquedaProductos, this.valorBusquedaProductos, this.familiaBusquedaProductos, tipoMaestro, false, false, "model.clave, model.id desc", 0);
			if (this.listBusquedaProductos == null || this.listBusquedaProductos.isEmpty()) 
	    		control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar los Productos", e);
    	} finally {
    		this.paginacionProductos = 1;
    		this.listBusquedaProductos = this.listBusquedaProductos != null ? this.listBusquedaProductos : new ArrayList<Producto>();
    	}
	}
	
	public void seleccionarProducto() throws Exception {
		InsumosDetallesExt pojoDetalle = null;
		ProductoExt producto = null;
		int tipoMaestro = 0;
		double importe = 0;
		
		try {
    		control();
    		// Validaciones
			if (this.pojoProducto == null || this.pojoProducto.getId() == null || this.pojoProducto.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Producto seleccionado");
				return;
			}
			
    		if (this.productoBusquedaCantidad <= 0) {
				control(-1, "Debe indicar la cantidad del Producto");
    			return;
    		}
    		
    		if (this.productoBusquedaPrecio <= 0) {
				control(-1, "Debe indicar el Precio Unitario del Producto");
    			return;
    		}
    		
    		if (this.listMateriales != null && ! this.listMateriales.isEmpty()) {
    			for (InsumosDetallesExt insumo : this.listMateriales) {
    				if (this.pojoProducto.getId().longValue() == insumo.getIdProducto().getId().longValue()) {
    					control(-1, "El Producto indicado ya existe en la Explosion de Insumos");
    					return;
    				}
    			}
    		}

    		// Extendemos el producto
			controlLog("Extendemos el producto seleccionado");
    		producto = this.ifzProducto.convertir(this.pojoProducto);
			if (this.explosionInsumos.getIdObra() != null && this.explosionInsumos.getIdObra().getId() != null && this.explosionInsumos.getIdObra().getId() > 0L)
				tipoMaestro = (this.explosionInsumos.getIdObra().getTipoObra() == 4 ? TipoMaestro.Administrativo.ordinal() : TipoMaestro.Inventario.ordinal());
			importe = this.productoBusquedaCantidad * this.productoBusquedaPrecio;

    		// Generamos el detalle de explosion de insumos
			controlLog("Generamos detalle de INSUMO del producto seleccionado");
			pojoDetalle = new InsumosDetallesExt();
			pojoDetalle.setIdInsumos(this.explosionInsumos);
			pojoDetalle.setTipo(tipoMaestro);
			pojoDetalle.setIdProducto(producto);
			pojoDetalle.setNombreProducto(producto.getDescripcion());
			pojoDetalle.setCantidad(this.productoBusquedaCantidad);
			pojoDetalle.setPrecioUnitario(this.productoBusquedaPrecio);
			pojoDetalle.setMonto(new BigDecimal(importe));
			pojoDetalle.setPorcentaje(0);
			pojoDetalle.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			pojoDetalle.setFechaCreacion(Calendar.getInstance().getTime());
			pojoDetalle.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			pojoDetalle.setFechaModificacion(Calendar.getInstance().getTime());

    		// Guardamos el detalle de explosion de insumos 
			controlLog("Guardamos el detalle de explosion de insumos");
			pojoDetalle.setId(this.ifzInsumosDetalles.save(pojoDetalle));
			nuevaBusquedaProductos();
			
			// Refrescamos los detalles de Explosion de insumos
			controlLog("Refrescamos los detalles de Explosion de insumos");
			this.listMateriales.add(pojoDetalle);
			Collections.sort(this.listMateriales, new Comparator<InsumosDetallesExt>() {
				@Override
				public int compare(InsumosDetallesExt o1, InsumosDetallesExt o2) {
					return o1.getIdProducto().getClave().compareTo(o2.getIdProducto().getClave());
				}
			});
			
			this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Material.name(), this.explosionInsumos.getMontoMateriales(), this.listMateriales));
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar el Producto seleccionado", e);
    	}
	}
	
	public void cargarFamilias() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<ConValores> listFamAux = null;
		List<ConValores> listAux = null;
		String valTipoMaestro = "";
		String idMaestro = "";
		
		try {
			this.listEspecialidades = new ArrayList<ConValores>();
			this.listFamilias = new ArrayList<ConValores>();
			this.listFamiliasItems = new ArrayList<SelectItem>();
			
			// Recupero MAESTROS
			controlLog("Cargando Maestros");
			valTipoMaestro = String.valueOf(TipoMaestro.Inventario.ordinal());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			listAux = this.ifzConValores.findAll(this.pojoGpoMaestros);
			for (ConValores var : listAux) {
				if (! var.getValor().equals(valTipoMaestro)) 
					continue;
				idMaestro = String.valueOf(var.getId());
				break;
			}

			// Recupero ESPECIALIDADES y en base a eso, recupero las FAMILIAS
			controlLog("Cargando Especialidades del Maestro INVENTARIOS");
			params.put("grupoValorId.id", this.pojoGpoEspecialidades.getId());
			params.put("atributo1", idMaestro);
			this.listEspecialidades = this.ifzConValores.findByProperties(params, 0);
			if (this.listEspecialidades != null && ! this.listEspecialidades.isEmpty()) {
				for (ConValores var : this.listEspecialidades) {
					params.put("grupoValorId.id", this.pojoGpoFamilias.getId());
					params.put("atributo1", String.valueOf(var.getId()));
					listFamAux = this.ifzConValores.findByProperties(params, 0);
					if (listFamAux == null || listFamAux.isEmpty())
						continue;
					this.listFamilias.addAll(listFamAux);
				}
			}
			
			// Ordeno familias por descripcion
			Collections.sort(this.listFamilias, new Comparator<ConValores>() {
			    	@Override
			        public int compare(ConValores o1, ConValores o2) {
			    		return o1.getDescripcion().compareTo(o2.getDescripcion());
			        }
			});
			
			// Generamos la lista auxiliar de familias
			if (this.listFamilias != null && ! this.listFamilias.isEmpty()) {
				controlLog("Generando lista de items (familias)");
				for (ConValores var : this.listFamilias) {
					if (var.getValor().trim().equals(var.getDescripcion().trim()))
						this.listFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion()));
					else
						this.listFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al cargar las familias", e);
		}
	}

    // --------------------------------------------------------------------------------------
	// Monedas
    // --------------------------------------------------------------------------------------

    private void cargarMonedas() {
		try {
			controlLog("Cargando Monedas");
			this.listMonedasItems = new ArrayList<SelectItem>();
			this.listMonedas = this.ifzMonedas.findAll();
			if (this.listMonedas != null && ! this.listMonedas.isEmpty()) {
				for (Moneda mon : this.listMonedas)
					this.listMonedasItems.add(new SelectItem(mon.getId(), mon.getNombre() + " (" + mon.getAbreviacion() + ")"));
			}
		} catch (Exception e) {
			control("Error en CuentasPorCobrar.FacturaAction.cargarMonedas", e);
		}
	}

    public void evaluaTipoCambio() {
    	if (this.explosionInsumos == null || this.explosionInsumos.getIdMoneda() <= 0L)
    		return;
    	if (getMostrarTipoCambio()) 
    		this.tipoCambio = this.loginManager.getTipoCambioActual();
    	else 
    		this.tipoCambio = 1.0;
    }

	// -----------------------------------------------------------------------------
	// PROPIEDADES
	// -----------------------------------------------------------------------------
    
    public boolean getPermisoAdmin() {
    	return "ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());
    }
    
    public void setPermisoAdmin(boolean value) {}

	public String getTitulo() {
		if (this.explosionInsumos != null && this.explosionInsumos.getId() != null && this.explosionInsumos.getId() > 0L)
			return "Explosion de Insumos " + this.explosionInsumos.getId();
		return "Nueva Explosion de Insumos";
	}
	
	public void setTitulo(String value) {}
	
	public boolean getImprimible() {
		if (this.explosionInsumos != null && this.explosionInsumos.getId() != null && this.explosionInsumos.getId() > 0L)
			return true;
		return false;
	}
	
	public void setImprimible(boolean value) {}

	public boolean getEditable() {
		return this.editable;
	}
	
	public void setEditable(boolean value) {}
	
	public boolean getMostrarTipoCambio() {
		if (! this.editable)
			return false;
		if (this.explosionInsumos != null && this.explosionInsumos.getIdMoneda() > 0L)
			return ! (this.explosionInsumos.getIdMoneda() == this.loginManager.getInfoSesion().getEmpresa().getMonedaId());
		return true;
	}
	
	public void setMostrarTipoCambio(boolean value) {}

	public long getIdMoneda() {
		return this.explosionInsumos.getIdMoneda();
	}
	
	public void setIdMoneda(long idMoneda) {
		this.explosionInsumos.setIdMoneda(idMoneda);
		if (this.listMonedas != null && ! this.listMonedas.isEmpty()) {
			for (Moneda moneda : this.listMonedas) {
				if (moneda.getId() == idMoneda) {
					this.explosionInsumos.setMoneda(moneda.getNombre());
					break;
				}
			}
		}
	}

	public double getTipoCambio() {
		return this.tipoCambio;
	}
	
	public void setTipoCambio(double value) {
		this.tipoCambio = value;
	}

	public List<SelectItem> getListMonedasItems() {
		return listMonedasItems;
	}

	public void setListMonedasItems(List<SelectItem> listMonedasItems) {
		this.listMonedasItems = listMonedasItems;
	}

	public double getInsumosPorcentaje() {
		double porcentaje = 0;
		
		if (this.listInsumosItems != null && ! this.listInsumosItems.isEmpty()) {
			for (InsumosTipo item : this.listInsumosItems)
				porcentaje += item.getPorcentaje();
		}
		
		return porcentaje;
	}
	
	public void setInsumosPorcentaje(double value) {}
	
	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}
	
	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}
	
	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public boolean isIncluirCanceladas() {
		return incluirCanceladas;
	}

	public void setIncluirCanceladas(boolean incluirCanceladas) {
		this.incluirCanceladas = incluirCanceladas;
	}
	
	public List<SelectItem> getTiposBusquedaObras() {
		return tiposBusquedaObras;
	}

	public void setTiposBusquedaObras(List<SelectItem> tiposBusquedaObras) {
		this.tiposBusquedaObras = tiposBusquedaObras;
	}
	
	public String getCampoBusquedaObras() {
		return campoBusquedaObras;
	}

	public void setCampoBusquedaObras(String campoBusquedaObras) {
		this.campoBusquedaObras = campoBusquedaObras;
	}
	
	public String getValorBusquedaObras() {
		return valorBusquedaObras;
	}

	public void setValorBusquedaObras(String valorBusquedaObras) {
		this.valorBusquedaObras = valorBusquedaObras;
	}

	/*public int getValorOpcionBusquedaObras() {
		return valorOpcionBusquedaObras;
	}

	public void setValorOpcionBusquedaObras(int valorOpcionBusquedaObras) {
		this.valorOpcionBusquedaObras = valorOpcionBusquedaObras;
	}*/
	
	public boolean isBand() {
		return operacionCancelada;
	}

	public void setBand(boolean band) {
		this.operacionCancelada = band;
	}
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public int getNumPagina() {
		return paginacionPrincipal;
	}

	public void setNumPagina(int numPagina) {
		this.paginacionPrincipal = numPagina;
	}

	public int getNumPaginaObras() {
		return paginacionObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.paginacionObras = numPaginaObras;
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
	
	public List<Insumos> getListInsumos() {
		return listExplosionInsumos;
	}

	public void setListInsumos(List<Insumos> listInsumos) {
		this.listExplosionInsumos = listInsumos;
	}

	public long getIdInsumo() {
		return idInsumo;
	}

	public void setIdInsumo(long idInsumo) {
		this.idInsumo = idInsumo;
	}
	
	public InsumosExt getPojoInsumo() {
		return explosionInsumos;
	}

	public void setPojoInsumo(InsumosExt pojoInsumo) {
		this.explosionInsumos = pojoInsumo;
	}
	
	public int getNumPaginaMateriales() {
		return paginacionMateriales;
	}

	public void setNumPaginaMateriales(int numPaginaMateriales) {
		this.paginacionMateriales = numPaginaMateriales;
	}

	public int getNumPaginaManoDeObra() {
		return paginacionManoDeObra;
	}

	public void setNumPaginaManoDeObra(int numPaginaManoDeObra) {
		this.paginacionManoDeObra = numPaginaManoDeObra;
	}

	public int getNumPaginaHerramientas() {
		return paginacionHerramientas;
	}

	public void setNumPaginaHerramientas(int numPaginaHerramientas) {
		this.paginacionHerramientas = numPaginaHerramientas;
	}
	
	public int getNumPaginaEquipos() {
		return paginacionEquipos;
	}

	public void setNumPaginaEquipos(int numPaginaEquipos) {
		this.paginacionEquipos = numPaginaEquipos;
	}
	
	public List<InsumosDetallesExt> getListMateriales() {
		return listMateriales;
	}

	public void setListMateriales(List<InsumosDetallesExt> listMateriales) {
		this.listMateriales = listMateriales;
	}
	
	public List<InsumosDetallesExt> getListManoDeObra() {
		return listManoDeObra;
	}

	public void setListManoDeObra(List<InsumosDetallesExt> listManoDeObra) {
		this.listManoDeObra = listManoDeObra;
	}
	
	public List<InsumosDetallesExt> getListHerramientas() {
		return listHerramientas;
	}

	public void setListHerramientas(List<InsumosDetallesExt> listHerramientas) {
		this.listHerramientas = listHerramientas;
	}
	
	public List<InsumosDetallesExt> getListEquipos() {
		return listEquipos;
	}

	public void setListEquipos(List<InsumosDetallesExt> listEquipos) {
		this.listEquipos = listEquipos;
	}
	
	public List<InsumoRow> getListProductos() {
		return listProductos;
	}

	public void setListProductos(List<InsumoRow> listProductos) {
		this.listProductos = listProductos;
	}

	public int getNumPaginaProductos() {
		return paginacionProductosNoEncontrados;
	}

	public void setNumPaginaProductos(int numPaginaProductos) {
		this.paginacionProductosNoEncontrados = numPaginaProductos;
	}
	
	public List<InsumosDetallesExt> getListOtros() {
		return listOtros;
	}

	public void setListOtros(List<InsumosDetallesExt> listOtros) {
		this.listOtros = listOtros;
	}

	public int getNumPaginaOtros() {
		return paginacionOtros;
	}

	public void setNumPaginaOtros(int numPaginaOtros) {
		this.paginacionOtros = numPaginaOtros;
	}

	public int getCountOtros() {
		return countOtros;
	}

	public void setCountOtros(int countOtros) {
		this.countOtros = countOtros;
	}

	public boolean isEscapeForInputMesssage() {
		return escapeForInputMesssage;
	}

	public void setEscapeForInputMesssage(boolean escapeForInputMesssage) {
		this.escapeForInputMesssage = escapeForInputMesssage;
	}

	public List<Producto> getListBusquedaProductos() {
		return listBusquedaProductos;
	}

	public void setListBusquedaProductos(List<Producto> listBusquedaProductos) {
		this.listBusquedaProductos = listBusquedaProductos;
	}

	public Producto getPojoProducto() {
		return pojoProducto;
	}

	public void setPojoProducto(Producto pojoProducto) {
		this.pojoProducto = pojoProducto;
	}

	public List<SelectItem> getTiposBusquedaProductos() {
		return tiposBusquedaProductos;
	}

	public void setTiposBusquedaProductos(List<SelectItem> tiposBusquedaProductos) {
		this.tiposBusquedaProductos = tiposBusquedaProductos;
	}

	public String getCampoBusquedaProductos() {
		return campoBusquedaProductos;
	}

	public void setCampoBusquedaProductos(String campoBusquedaProductos) {
		this.campoBusquedaProductos = campoBusquedaProductos;
	}

	public String getValorBusquedaProductos() {
		return valorBusquedaProductos;
	}

	public void setValorBusquedaProductos(String valorBusquedaProductos) {
		this.valorBusquedaProductos = valorBusquedaProductos;
	}

	public int getNumBusquedaPaginaProductos() {
		return paginacionProductos;
	}

	public void setNumBusquedaPaginaProductos(int numBusquedaPaginaProductos) {
		this.paginacionProductos = numBusquedaPaginaProductos;
	}

	public boolean isPermiteAgregarProducto() {
		return permiteAgregarProducto;
	}

	public void setPermiteAgregarProducto(boolean permiteAgregarProducto) {
		this.permiteAgregarProducto = permiteAgregarProducto;
	}

	public List<SelectItem> getListFamiliasItems() {
		return listFamiliasItems;
	}

	public void setListFamiliasItems(List<SelectItem> listFamiliasItems) {
		this.listFamiliasItems = listFamiliasItems;
	}

	public long getFamiliaBusquedaProductos() {
		return familiaBusquedaProductos;
	}

	public void setFamiliaBusquedaProductos(long familiaBusquedaProductos) {
		this.familiaBusquedaProductos = familiaBusquedaProductos;
	}

	public double getProductoBusquedaCantidad() {
		return productoBusquedaCantidad;
	}

	public double getProductoBusquedaPrecio() {
		return productoBusquedaPrecio;
	}

	public void setProductoBusquedaPrecio(double productoBusquedaPrecio) {
		this.productoBusquedaPrecio = productoBusquedaPrecio;
	}

	public void setProductoBusquedaCantidad(double productoBusquedaCantidad) {
		this.productoBusquedaCantidad = productoBusquedaCantidad;
	}

	public List<InsumosTipo> getListInsumosItems() {
		return listInsumosItems;
	}

	public void setListInsumosItems(List<InsumosTipo> listInsumosItems) {
		this.listInsumosItems = listInsumosItems;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public boolean isPerfilAdministrativo() {
		return perfilAdministrativo;
	}

	public void setPerfilAdministrativo(boolean perfilAdministrativo) {
		this.perfilAdministrativo = perfilAdministrativo;
	}

	public boolean isBuscarAdministrativo() {
		return buscarAdministrativo;
	}

	public void setBuscarAdministrativo(boolean buscarAdministrativo) {
		this.buscarAdministrativo = buscarAdministrativo;
	}

	// ----------------------------------------------------------------------
	// PERMISOS
	// ----------------------------------------------------------------------

	private void establecerPermisos() {
    	/*EmpleadoExt emp = null;
    	Long aux = 0L;
    	
    	this.permisoEscritura = false;
    	if (this.loginManager == null)
    		return;
    	
    	if ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario())) {
    		this.permisoEscritura = true;
    		return;
    	}
    	
    	aux = this.loginManager.getInfoSesion().getAcceso().getUsuario().getIdEmpleado();
    	if (aux == null || aux.longValue() <= 1)
    		return;
    	emp = this.ifzEmpleados.findByIdExt(aux);
    	if (! "".contains(emp.getPuestoCategoria().getIdPuesto().getId().toString()))
    		return;
    	
    	// Concedemos permisos de escritura
    	this.permisoEscritura = true;*/
    	this.permisoAgregar = true;
    	this.permisoEditar = true;
    	this.permisoBorrar = true;
    	this.permisoImprimir = true;
    }
    
    private void validarPermisos() {
    	/*Long aux = 0L;
    	
    	if (this.permisoEscritura)
    		return;
    	
    	if (this.loginManager == null) {
    		this.permisoEscritura = false;
    		return;
    	}
    	
    	if ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario())) {
    		this.permisoEscritura = true;
    		return;
    	}
    	
    	aux = this.loginManager.getInfoSesion().getAcceso().getUsuario().getIdEmpleado();
    	if (aux == null || aux.longValue() <= 1) {
    		this.permisoEscritura = false;
    		return;
    	}
    	
    	// Validamos el permisos de escritura
    	this.permisoEscritura = (aux.longValue() == this.pojoObraMain.getCreadoPor() || aux.longValue() == this.pojoObraMain.getIdResponsable().longValue());
    	*/
    }
    
	public boolean isPermisoAgregar() { return permisoAgregar; }

	public void setPermisoAgregar(boolean permisoAgregar) {}

	public boolean isPermisoEditar() { return permisoEditar; }

	public void setPermisoEditar(boolean permisoEditar) {}

	public boolean isPermisoBorrar() { return permisoBorrar; }

	public void setPermisoBorrar(boolean permisoBorrar) {}

	public boolean isPermisoImprimir() { return permisoImprimir; }

	public void setPermisoImprimir(boolean permisoImprimir) {}
	
	public boolean isPermisoEscritura() {
		return true;
	}

	public void setPermisoEscritura(boolean permisoEscritura) { }
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado mensajes a consola de carga de insumos detalles
 */
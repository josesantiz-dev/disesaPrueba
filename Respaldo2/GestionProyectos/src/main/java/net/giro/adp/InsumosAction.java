package net.giro.adp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Set;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.InsumoRow;
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
import net.giro.inventarios.beans.TipoInsumo;
import net.giro.inventarios.beans.TipoMaestro;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;

import com.google.common.io.Files;

@ViewScoped
@ManagedBean(name="insumosAction")
public class InsumosAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(InsumosAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx;
	// Interfaces
	private InsumosRem 			ifzInsumos;
	private InsumosDetallesRem 	ifzInsumosDet;
	private ObraRem 			ifzObras;
	private ProductoRem 		ifzProducto;
	private ConGrupoValoresRem 	ifzGpoVal;
	private ConValoresRem 		ifzConValores;
	private ReportesRem			ifzReportes;
	// Listas
	private List<InsumosExt> 		 listInsumos;
	private List<Obra>		 		 listObras;
	private List<InsumosDetallesExt> listMateriales;
	private List<InsumosDetallesExt> listManoDeObra;
	private List<InsumosDetallesExt> listHerramientas;
	private List<InsumosDetallesExt> listOtros;
	private List<InsumoRow> 		 listProductos;
	private List<InsumosDetallesExt> listDetallesEliminados;
	private List<InsumosTipo> listInsumosItems;
	// POJO's
	private InsumosExt 	pojoInsumo;
	private InsumosExt 	pojoInsumoEliminar;
	private Obra 		pojoObra;
	private ConGrupoValores pojoGpoMaestros;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Busqueda de obras
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorOpcionBusquedaObras;
	private int numPaginaObras;
	// Busqueda productos
	private List<Producto> listBusquedaProductos;
	private Producto pojoProducto;
	private List<SelectItem> tiposBusquedaProductos;
	private List<ConValores> listEspecialidades;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private long familiaBusquedaProductos;
	private int numBusquedaPaginaProductos;	
	private boolean permiteAgregarProducto;
	private double productoBusquedaCantidad;
	private double productoBusquedaPrecio;
	// control
    private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
    private long usuarioId;
    private String usuario;
	private int numPaginaMateriales;
	private int numPaginaManoDeObra;
	private int numPaginaHerramientas;
	private int numPaginaProductos;
	private int numPaginaOtros;
	private byte[] fileSrc; 
	private String fileName;
	private String fileExtension;
	private int countOtros;
	private HashMap<String, String> insumosCellReference = new HashMap<String, String>();
	private boolean escapeForInputMesssage;


	public InsumosAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression dato = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);

			// Obtenemos el acceso
			dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) dato.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			this.ctx = new InitialContext();
			this.ifzInsumos  	= (InsumosRem)  		this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzInsumosDet  = (InsumosDetallesRem)  this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosDetallesFac!net.giro.adp.logica.InsumosDetallesRem");
			this.ifzObras 	 	= (ObraRem) 	 		this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzProducto 	= (ProductoRem) 		this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzGpoVal 		= (ConGrupoValoresRem) 	this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores 	= (ConValoresRem) 		this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzReportes 	= (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			
			// Listas
			this.listInsumos = new ArrayList<InsumosExt>();
			this.listObras = new ArrayList<Obra>();
			this.listMateriales = new ArrayList<InsumosDetallesExt>();
			this.listManoDeObra = new ArrayList<InsumosDetallesExt>();
			this.listHerramientas = new ArrayList<InsumosDetallesExt>();
			this.listOtros = new ArrayList<InsumosDetallesExt>();
			this.listProductos = new ArrayList<InsumoRow>();
			this.listDetallesEliminados = new ArrayList<InsumosDetallesExt>();
			
			// POJO's
			this.pojoInsumo = new InsumosExt();
			this.pojoInsumoEliminar = new InsumosExt();

			this.ifzInsumos.setInfoSesion(loginManager.getInfoSesion());
			this.ifzInsumos.showSystemOuts(false);
			this.ifzObras.showSystemOuts(false);
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("nombreObra", "Nombre Obra"));
			this.tiposBusqueda.add(new SelectItem("idObra", "ID Obra"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			/*this.tiposBusqueda.add(new SelectItem("o.nombre", "Obra"));
			this.tiposBusqueda.add(new SelectItem("idObra", "ID Obra"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));*/
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			
			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "Clave"));
			this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
			this.valorBusquedaObras = "";
			this.valorOpcionBusquedaObras = 0;
			
			// Busqueda productos
			this.tiposBusquedaProductos = new ArrayList<SelectItem>();
			this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaProductos.add(new SelectItem("clave", "Clave"));
			this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
			this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
			this.valorBusquedaProductos = "";
			this.familiaBusquedaProductos = 0;
			this.numBusquedaPaginaProductos = 1;
			this.productoBusquedaCantidad = 0;
			this.productoBusquedaPrecio = 0;
			
			this.fileSrc = null;
			this.fileExtension = "";
			
			this.numPagina = 1;
			this.numPaginaObras = 1;
			this.numPaginaMateriales = 1;
			this.numPaginaManoDeObra = 1;
			this.numPaginaHerramientas = 1;
			this.numPaginaProductos = 1;
			this.numPaginaOtros = 1;
			this.countOtros = 0;
			this.escapeForInputMesssage = true;
			
			// Obtenemos la lista de referencia de columnas para la carga de INSUMOS
			dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{insumos}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());
            Set<String> keys = this.entornoProperties.keySet();
            for (String key : keys)
            	this.insumosCellReference.put(key.toUpperCase().trim(), this.entornoProperties.getString(key).toUpperCase().trim());
            
            // Obtenemos las propiedades de entorno del modulo
            dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());

			// Grupo de valores para MAESTROS de productos
			this.pojoGpoMaestros = this.ifzGpoVal.findByName("SYS_CODE_NIVEL0");
			if (this.pojoGpoMaestros == null || this.pojoGpoMaestros.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_CODE_NIVEL0 (Maestros) en con_grupo_valores");
			
			// Grupo de valores para ESPECIALIDADES de productos
			this.pojoGpoEspecialidades = this.ifzGpoVal.findByName("SYS_PRODUCTO_ESPECIALIDADES");
			if (this.pojoGpoEspecialidades == null || this.pojoGpoEspecialidades.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_PRODUCTO_ESPECIALIDADES en con_grupo_valores");

			// Grupo de valores para FAMILIAS de productos 
			this.pojoGpoFamilias = this.ifzGpoVal.findByName("SYS_FAMILIA_PRODUCTO");
			if (this.pojoGpoFamilias == null || this.pojoGpoFamilias.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_FAMILIA_PRODUCTO en con_grupo_valores");

			this.permiteAgregarProducto = false;
			if ("ADMINISTRADOR".equals(loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()))
				this.permiteAgregarProducto = true;

			cargarFamilias();
		} catch (Exception e) {
			this.ctx = null;
			log.error("Error en constructor InsumosAction", e);
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			
			if (this.listInsumos != null)
				this.listInsumos.clear();
			
			this.listInsumos = this.ifzInsumos.findLikePropertyExt(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listInsumos == null && this.listInsumos.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la Busqueda de Insumos", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.countOtros = 0;
			this.pojoInsumo = new InsumosExt();
			this.pojoInsumoEliminar = new InsumosExt();

			this.listMateriales.clear();
			this.listManoDeObra.clear();
			this.listHerramientas.clear();
			this.listOtros.clear();
			
			subirExcel();
			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al intanciar una nueva Explosion de Insumos", e);
		}
	}
	
	public void editar() {
		List<InsumosDetalles> lista = null;
		List<InsumosDetallesExt> listAux = null;
		int detalles = 0;
		
		try {
			control();
			this.countOtros = 0;
			this.listMateriales.clear();
			this.listManoDeObra.clear();
			this.listHerramientas.clear();
			this.listOtros.clear();
			
			// Recuperamos la obra
			log.info("Obteniendo Obra");
			this.pojoObra = this.pojoInsumo.getIdObra();
			
			// Recuperamos los detalles del insumo
			log.info("Obteniendo detalles de insumo");
			lista = this.ifzInsumosDet.findByProperty("idInsumo", this.pojoInsumo.getId(), 0);
			if (lista != null && ! lista.isEmpty()) {
				detalles = lista.size();
				log.info("Extendiendo detalles de insumo");
				listAux = this.ifzInsumosDet.extenderInsumosDetalles(lista);
				log.info("Clasificando detalles de insumo");
				for (InsumosDetallesExt var : listAux) {
					if (var.getTipo() == 1) {
						this.listMateriales.add(var);
					} else if (var.getTipo() == 2) {
						this.listManoDeObra.add(var);
					} else if (var.getTipo() == 3) {
						this.listHerramientas.add(var);
					} else {
						this.listOtros.add(var);
					} 
				}
				
				this.countOtros = this.listOtros.size();
				
				if (this.listInsumosItems == null)
					this.listInsumosItems = new ArrayList<InsumosTipo>();
				this.listInsumosItems.clear();
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Material.name(), this.pojoInsumo.getMontoMateriales(), this.listMateriales));
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.ManoDeObra.name(), this.pojoInsumo.getMontoManoDeObra(), this.listManoDeObra));
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Herramienta.name(), this.pojoInsumo.getMontoHerramientas(), this.listHerramientas));
				if (this.countOtros > 0)
					this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Otros.name(), this.pojoInsumo.getMontoOtros(), this.listOtros));
			}
			
			log.info(detalles  + " detalles de insumo obtenidos");
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
			log.info("Guardando insumos");

			monto = 0;
			log.info("Guardando detalles: MATERIALES");
			for (InsumosDetallesExt var : this.listMateriales) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				monto += var.getMonto().doubleValue();
				var.setIdInsumos(this.pojoInsumo);
				
				producto = this.ifzProducto.convertir(var.getIdProducto());
				/*producto.setId(0L);
				producto.setClave("");
				producto.setEspecialidad(10002580l);
				producto.setDescEspecialidad("ACTIVOS");
				producto.setFamilia(10002595l);
				producto.setDescFamilia("OBRA EN PROCESO");
				producto.setSubfamilia(10002634l);
				producto.setDescSubfamilia("CHAMETLA");
				producto.setTipo(TipoMaestro.Administrativo.ordinal());*/
				producto.setId(this.ifzProducto.save(producto));
				
				var.setIdProducto(this.ifzProducto.convertir(producto));
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumosDet.update(var);
			}
			
			this.pojoInsumo.setMontoMateriales(new BigDecimal(monto));
			log.info("Guardando detalles: MATERIALES ... OK");
			monto = 0;
			
			log.info("Guardando detalles: MANO DE OBRA");
			for (InsumosDetallesExt var : this.listManoDeObra) {
				indexInsumoDet = this.listManoDeObra.indexOf(var);
				monto += var.getMonto().doubleValue();
				var.setIdInsumos(this.pojoInsumo);
				
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
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumosDet.update(var);
			}
			
			this.pojoInsumo.setMontoManoDeObra(new BigDecimal(monto));
			log.info("Guardando detalles: MANO DE OBRA ... OK");
			monto = 0;
			
			log.info("Guardando detalles: HERRAMIENTAS");
			for (InsumosDetallesExt var : this.listHerramientas) {
				indexInsumoDet = this.listHerramientas.indexOf(var);
				monto += var.getMonto().doubleValue();
				var.setIdInsumos(this.pojoInsumo);
				
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
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumosDet.update(var);
			}
			
			this.pojoInsumo.setMontoHerramientas(new BigDecimal(monto));
			log.info("Guardando detalles: HERRAMIENTAS ... OK");
			monto = 0;
			
			log.info("Guardando detalles: OTROS");
			for (InsumosDetallesExt var : this.listOtros) {
				indexInsumoDet = this.listOtros.indexOf(var);
				monto += var.getMonto().doubleValue();
				var.setIdInsumos(this.pojoInsumo);
				
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
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumosDet.update(var);
			}
			
			this.pojoInsumo.setMontoOtros(new BigDecimal(monto));
			log.info("Guardando detalles: OTROS ... OK");
			monto = 0;
			
			this.pojoInsumo.setModificadoPor(this.usuarioId);
			this.pojoInsumo.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzInsumos.update(this.pojoInsumo);
			
			this.mensaje = "OK";
			log.info("INSUMOS GUARDADOS");
		} catch (Exception e) {
			control("Ocurrio un problema al actualizar la Explosion de Insumos\n\nUltimo indice procesado " + indexInsumoDet, e);
		}
	}
	
	public void guardar() {
		List<Producto> listProductos = null;
		Producto prod = null;
		boolean esNuevo = false;
		int indexInsumoDet = 0;
		
		try {
			control();
			log.info("Guardando insumos");
			// Comprobamos que no halla insumos con la obra indicada
			log.info("Comprobamos insumos");
			if (comprobarInsumos()) {
				control(5, "Ya existen insumos con la obra especificada");
				return;
			}
			
			// Comprobamos si no hay detalles para eliminar
			if (this.listDetallesEliminados != null && ! this.listDetallesEliminados.isEmpty()) {
				for (InsumosDetallesExt var : this.listDetallesEliminados) {
					if (var.getId() != null && var.getId() > 0L)
						this.ifzInsumosDet.delete(var.getId());
				}
				
				this.listDetallesEliminados.clear();
			}
			
			log.info("Guardando insumo");
			if (this.pojoInsumo.getId() == null || this.pojoInsumo.getId() <= 0L) {
				// Nuevo INSUMOS
				esNuevo = true;
				this.pojoInsumo.setCreadoPor(this.usuarioId);
				this.pojoInsumo.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoInsumo.setModificadoPor(this.usuarioId);
				this.pojoInsumo.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos el encabezado de los INSUMOS
				this.pojoInsumo.setId(this.ifzInsumos.save(this.pojoInsumo));
			} else {
				// Actualizacion de INSUMOS
				this.pojoInsumo.setModificadoPor(this.usuarioId);
				this.pojoInsumo.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzInsumos.update(this.pojoInsumo);
			}
			
			// Guardamos los detalles de los INSUMOS
			
			// MATERIALES
			log.info("Guardando detalles: MATERIALES");
			for (InsumosDetallesExt var : this.listMateriales) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.pojoInsumo);
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					var.setId(this.ifzInsumosDet.save(var));
				} else {
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzInsumosDet.update(var);
				}
				
				// Actualizamos el producto
				log.info("Actualizamo tipos producto: TipoMaestro.Inventario, TipoInsumo.Material");
				prod = this.ifzProducto.convertir(var.getIdProducto());
				prod.setTipo(TipoMaestro.Inventario.ordinal());
				prod.setTipoInsumo(TipoInsumo.Material.ordinal());
				prod.setModificadoPor(this.usuarioId);
				prod.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (listProductos == null)
					listProductos = new ArrayList<Producto>();
				listProductos.add(prod);
			}
			
			// MANO DE OBRA
			log.info("Guardando detalles: MANO DE OBRA");
			for (InsumosDetallesExt var : this.listManoDeObra) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.pojoInsumo);
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					var.setId(this.ifzInsumosDet.save(var));
				} else {
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzInsumosDet.update(var);
				}
				
				// Actualizamos el producto
				log.info("Actualizamo tipos producto: TipoMaestro.Inventario, TipoInsumo.ManoDeObra");
				prod = this.ifzProducto.convertir(var.getIdProducto());
				prod.setTipo(TipoMaestro.Inventario.ordinal());
				prod.setTipoInsumo(TipoInsumo.ManoDeObra.ordinal());
				prod.setModificadoPor(this.usuarioId);
				prod.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (listProductos == null)
					listProductos = new ArrayList<Producto>();
				listProductos.add(prod);
			}
			
			// HERRAMIENTAS
			log.info("Guardando detalles: HERRAMIENTAS");
			for (InsumosDetallesExt var : this.listHerramientas) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.pojoInsumo);
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					var.setId(this.ifzInsumosDet.save(var));
				} else {
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzInsumosDet.update(var);
				}
				
				// Actualizamos el producto
				log.info("Actualizamo tipos producto: TipoMaestro.Inventario, TipoInsumo.Herramienta");
				prod = this.ifzProducto.convertir(var.getIdProducto());
				prod.setTipo(TipoMaestro.Inventario.ordinal());
				prod.setTipoInsumo(TipoInsumo.Herramienta.ordinal());
				prod.setModificadoPor(this.usuarioId);
				prod.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (listProductos == null)
					listProductos = new ArrayList<Producto>();
				listProductos.add(prod);
			}
			
			// OTROS
			log.info("Guardando detalles: OTROS");
			for (InsumosDetallesExt var : this.listOtros) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.pojoInsumo);
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					var.setId(this.ifzInsumosDet.save(var));
				} else {
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzInsumosDet.update(var);
				}
				
				// Actualizamos el producto
				log.info("Actualizamo tipos producto: TipoMaestro.Inventario, TipoInsumo.Otros");
				prod = this.ifzProducto.convertir(var.getIdProducto());
				prod.setTipo(TipoMaestro.Inventario.ordinal());
				prod.setTipoInsumo(TipoInsumo.Otros.ordinal());
				prod.setModificadoPor(this.usuarioId);
				prod.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (listProductos == null)
					listProductos = new ArrayList<Producto>();
				listProductos.add(prod);
			}
			
			if (listProductos != null && ! listProductos.isEmpty()) {
				if (listProductos.size() <= 1000)
					this.ifzProducto.saveOrUpdateList(listProductos);
			}
			
			// Añadimos/Actualizamos a la lista
			log.info("Añadimos/Actualizamos a la lista");
			if (esNuevo) {
				this.listInsumos.add(this.pojoInsumo);
			} else {
				for (InsumosExt var : this.listInsumos) {
					if (var.getId() == this.pojoInsumo.getId()) {
						var = this.pojoInsumo;
						break;
					}
				}
			}
			
			this.mensaje = "OK";
			log.info("INSUMOS GUARDADOS");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Explosion de Insumos.\n\nUltimo indice procesado " + indexInsumoDet, e);
		} 
	}
	
	public void eliminar() {
		try {
			control();
			if (this.pojoInsumoEliminar != null && this.pojoInsumoEliminar.getId() != null && this.pojoInsumoEliminar.getId() > 0L) {
				// Buscamos el insumo en la lista
				for (InsumosExt var : this.listInsumos) {
					if (var.getId() == this.pojoInsumoEliminar.getId()) {
						// Modificamos los datos
						var.setEstatus(1);
						var.setFechaModificacion(Calendar.getInstance().getTime());
						var.setModificadoPor(this.usuarioId);
						
						// Actualizamos el insumo
						this.ifzInsumos.update(var);
						break;
					}
				}
			}
			
			this.pojoInsumoEliminar = null;
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
			if (this.pojoInsumo != null && this.pojoInsumo.getId() != null && this.pojoInsumo.getId() > 0L) {
				log.info("Imprimiento reporte Explosion de Insumos ... ");
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idObra", this.pojoInsumo.getIdObra().getId());

				// Parametros para la ejecucion del reporte
				params = new HashMap<String, String>();
				params.put("idPrograma", 	  "189");
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);

				respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					log.error("ERROR INTERNO - " + respuesta.getErrores().getDescError());
					control(-1, "Ocurrio un problema al intentar imprimir la Explosion de Insumos\nError " + respuesta.getErrores().getCodigoError());
					return;
				} 
				
				contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
				formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
				nombreDocumento = "EXP-INSUMOS-O-" + this.pojoInsumo.getIdObra().getId() + "." + formatoDocumento;
				
				if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
					log.error("ERROR INTERNO - No se recupero el contenido de la Ordenes de Compra de Exp Insumos");
					control(-1, "Ocurrio un problema al intentar imprimir la Explosion de Insumos");
					return;
				}
				
				this.httpSession.setAttribute("contenido", contenidoDocumento);
				this.httpSession.setAttribute("formato", formatoDocumento); 	
				this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
				log.info("Reporte Explosion de Insumos lanzado. Proceso terminado!");
			}
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
			if (this.pojoInsumo != null && this.pojoInsumo.getId() != null && this.pojoInsumo.getId() > 0L) {
				log.info("Imprimiento reporte Ordenes de Compra de Exp Insumos ... ");
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idObra", this.pojoInsumo.getIdObra().getId());

				// Parametros para la ejecucion del reporte
				params = new HashMap<String, String>();
				params.put("idPrograma", 	  "180");
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);

				respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					log.error("ERROR INTERNO - " + respuesta.getErrores().getDescError());
					control(-1, "Ocurrio un problema al intentar imprimir la Ordenes de Compra de Exp Insumos\nError " + respuesta.getErrores().getCodigoError());
					return;
				} 
				
				contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
				formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
				nombreDocumento = "OC-INSUMOS-O-" + this.pojoInsumo.getIdObra().getId() + "." + formatoDocumento;
				
				if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
					log.error("ERROR INTERNO - No se recupero el contenido de la Ordenes de Compra de Exp Insumos");
					control(-1, "Ocurrio un problema al intentar imprimir la Ordenes de Compra de Exp Insumos");
					return;
				}
				
				this.httpSession.setAttribute("contenido", contenidoDocumento);
				this.httpSession.setAttribute("formato", formatoDocumento); 	
				this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
				log.info("Reporte Ordenes de Compra de Exp Insumos lanzado. Proceso terminado!");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la Ordenes de Compra de Exp Insumos", e);
		} 
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
		
		log.error("\n\n EXPLOSION DE INSUMOS :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n" + this.mensajeDetalles, throwable);
		
		/*this.operacionCancelada = val1;
		this.tipoMensaje = val2;
		this.mensaje = val3;
		
		if (this.mensaje != null && this.mensaje.contains("\n"))
			this.mensaje = this.mensaje.replace("\n", "<br>");
		
		this.mensajeDetalles = "";
		if (t != null) {
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}*/
	}

    // --------------------------------------------------------------------------------------
	// BUSQUEDA OBRAS
    // --------------------------------------------------------------------------------------
	
	public void nuevaBusquedaObras() {
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
		this.valorBusquedaObras = "";
		this.valorOpcionBusquedaObras = 0;
		
		if (this.listObras != null)
			this.listObras.clear();
	}

	public void buscarObras() {
		try {
			control();
			if ("".equals(String.valueOf(this.valorOpcionBusquedaObras)))
				this.valorOpcionBusquedaObras = 0;
			
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
			
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.valorOpcionBusquedaObras);
			if (this.listObras == null || this.listObras.isEmpty()) {
				control(2, "Busqueda sin resultados");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
		}
	}
	
	public void seleccionarObra() {
		nuevaBusquedaObras();
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
	public void analizarExcel() throws Exception {
		Respuesta respuesta = null;
		String prodsNotFound = "";
		
		try {
			control();
			if (this.fileSrc == null)
				return;
			
			if (this.fileName == null || "".equals(this.fileName))
				this.fileName = "archivo";
			
			if (this.fileExtension == null || "".equals(this.fileExtension))
				this.fileExtension = "xls";
			
			log.info("Procesando archivo: " + this.fileName);
			respuesta = ifzInsumos.analizarExcel(this.fileSrc, this.fileExtension, this.insumosCellReference);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al procesar la Explosion de Insumos.\n\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				log.error(respuesta.getErrores().getCodigoError() + " - " + this.mensaje);
				if (this.mensaje.contains("\n")) {
					this.escapeForInputMesssage = false;
					this.mensaje = this.mensaje.replace("\n", "<br/>");
				}
				return;
			}

			this.pojoInsumo = (InsumosExt) respuesta.getBody().getValor("pojoInsumo");
			this.pojoInsumo.setNombreArchivo(this.fileName);
			this.listMateriales = (List<InsumosDetallesExt>) respuesta.getBody().getValor("materiales");
			this.listManoDeObra = (List<InsumosDetallesExt>) respuesta.getBody().getValor("manoDeObra");
			this.listHerramientas = (List<InsumosDetallesExt>) respuesta.getBody().getValor("herramientas");
			this.listOtros = (List<InsumosDetallesExt>) respuesta.getBody().getValor("otros");
			this.listProductos = (List<InsumoRow>) respuesta.getBody().getValor("productos");
			this.countOtros = this.listOtros.size();
			
			if (this.listProductos != null && ! this.listProductos.isEmpty()) {
				//log.error(this.tipoMensaje + " - Maestro NO actualizado. No se encontraron " + this.listProductos.size() + " productos");
				for (InsumoRow var : this.listProductos)
					prodsNotFound += "\n" + var.getClave() + " - " + var.getDescripcion();
				control(4, "Maestro NO Actualizado.\nNo se encontraton " + this.listProductos.size() + " productos" + prodsNotFound);
			}

			if (this.listInsumosItems == null)
				this.listInsumosItems = new ArrayList<InsumosTipo>();
			this.listInsumosItems.clear();
			this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Material.name(), this.pojoInsumo.getMontoMateriales(), this.listMateriales));
			this.listInsumosItems.add(new InsumosTipo(TipoInsumo.ManoDeObra.name(), this.pojoInsumo.getMontoManoDeObra(), this.listManoDeObra));
			this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Herramienta.name(), this.pojoInsumo.getMontoHerramientas(), this.listHerramientas));
			if (this.countOtros > 0)
				this.listInsumosItems.add(new InsumosTipo(TipoInsumo.Otros.name(), this.pojoInsumo.getMontoOtros(), this.listOtros));
		} catch (Exception e) {
			control("Ocurrio un problema al procesar la Explosion de Insumos", e);
		}
	}
	
	public boolean comprobarInsumos() {
		if (this.pojoInsumo.getIdObra() == null || this.pojoInsumo.getIdObra().getId() == null || this.pojoInsumo.getIdObra().getId() <= 0L)
			return true;
		return this.ifzInsumos.comprobarInsumo(this.pojoInsumo.getIdObra().getId());
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
		this.numPaginaProductos = 1;
		this.productoBusquedaCantidad = 0;
		this.productoBusquedaPrecio = 0;
		
		if (this.listBusquedaProductos != null)
			this.listBusquedaProductos.clear();
		this.pojoProducto = null;
    }
	
	public void buscarProductos() {
		HashMap<String, String> params = new HashMap<String, String>();
		String valorBusqueda = "";
		
		try {
    		control();
			if ("".equals(this.campoBusquedaProductos))
				this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
			
			if (this.valorBusquedaProductos == null || "".equals(this.valorBusquedaProductos)) {
	    		log.info("Busqeueda de Productos. Sin criterio de busqueda");
				control(-1, "Debe indicar un criterio de busqueda");
				return;
			} else {
				valorBusqueda = this.valorBusquedaProductos.trim();
				if (! "".equals(valorBusqueda) && valorBusqueda.contains(" "))
					valorBusqueda = valorBusqueda.replace(" ", "%");
			}

			if (this.familiaBusquedaProductos <= 0L)
				this.familiaBusquedaProductos = 0L;

			this.numPaginaProductos = 1;
			if (this.listBusquedaProductos == null)
				this.listBusquedaProductos = new ArrayList<Producto>();
			this.listBusquedaProductos.clear();

			log.info("Buscando Productos - " + this.campoBusquedaProductos + ": " + this.valorBusquedaProductos);
			params.put(this.campoBusquedaProductos, valorBusqueda);
			params.put("tipo", String.valueOf(TipoMaestro.Inventario.ordinal()));
			if (this.familiaBusquedaProductos > 0L) 
				params.put("familia", String.valueOf(this.familiaBusquedaProductos));
			params.put("estatus", "0");
			params.put("oculto", "0");
			
			this.listBusquedaProductos = this.ifzProducto.findLikeProperties(params, null, 0, this.campoBusquedaProductos);
			if (this.listBusquedaProductos.isEmpty()) {
	    		control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar los Productos", e);
    	}
	}
	
	public void seleccionarProducto() throws Exception {
		InsumosDetallesExt pojoDetalle = null;
		ProductoExt producto = null;
		double importe = 0;
		
		try {
    		control();
			if (this.pojoProducto != null) {
				producto = this.ifzProducto.convertir(this.pojoProducto);
				
				log.info("Generamos detalle de INSUMO del producto seleccionado");
				pojoDetalle = new InsumosDetallesExt();
				pojoDetalle.setIdInsumos(this.pojoInsumo);
				pojoDetalle.setIdProducto(producto);
				pojoDetalle.setNombreProducto(producto.getDescripcion());
				pojoDetalle.setCantidad(this.productoBusquedaCantidad);
				pojoDetalle.setPrecioUnitario(this.productoBusquedaPrecio);
				pojoDetalle.setMonto(new BigDecimal(importe));
				pojoDetalle.setPorcentaje(0);
				pojoDetalle.setTipo(TipoInsumo.Material.ordinal());
				pojoDetalle.setCreadoPor(this.usuarioId);
				pojoDetalle.setFechaCreacion(Calendar.getInstance().getTime());
				pojoDetalle.setModificadoPor(this.usuarioId);
				pojoDetalle.setFechaModificacion(Calendar.getInstance().getTime());

				pojoDetalle.setId(this.ifzInsumosDet.save(pojoDetalle));
				
				this.listMateriales.add(pojoDetalle);
			}
			
			nuevaBusquedaProductos();
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
			if (this.listEspecialidades == null)
				this.listEspecialidades = new ArrayList<ConValores>();
			this.listEspecialidades.clear();
			
			if (this.listFamilias == null)
				this.listFamilias = new ArrayList<ConValores>();
			this.listFamilias.clear();
			
			if (this.listFamiliasItems == null)
				this.listFamiliasItems = new ArrayList<SelectItem>();
			this.listFamiliasItems.clear();
			
			// Recupero MAESTROS
			log.info("Cargando Maestros");
			valTipoMaestro = String.valueOf(TipoMaestro.Inventario.ordinal());
			listAux = this.ifzConValores.findAll(this.pojoGpoMaestros);
			for (ConValores var : listAux) {
				if (! var.getValor().equals(valTipoMaestro)) 
					continue;
				idMaestro = String.valueOf(var.getId());
				break;
			}

			// Recupero ESPECIALIDADES y en base a eso, recupero las FAMILIAS
			log.info("Cargando Especialidades del Maestro INVENTARIOS");
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
				log.info("Generando lista de items (familias)");
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
	
	// -----------------------------------------------------------------------------
	// PROPIEDADES
	// -----------------------------------------------------------------------------

	public boolean getImprimible() {
		if (this.pojoInsumo != null && this.pojoInsumo.getId() != null && this.pojoInsumo.getId() > 0L)
			return true;
		return false;
	}
	
	public void setImprimible(boolean value) {}
	
	public String getTitulo() {
		if (this.pojoInsumo != null && this.pojoInsumo.getId() != null && this.pojoInsumo.getId() > 0L)
			return "Explosion de Insumos " + this.pojoInsumo.getId();
		return "Nueva Explosion de Insumos";
	}
	
	public void setTiutlo(String value) {}
	
	public double getInsumosPorcentaje() {
		double porcentaje = 0;
		
		if (this.listInsumosItems != null && ! this.listInsumosItems.isEmpty()) {
			for (InsumosTipo item : this.listInsumosItems)
				porcentaje += item.getPorcentaje();
		}
		
		return porcentaje;
	}
	
	public void setInsumosPorcentaje(double value) {}
	
	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

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

	public int getValorOpcionBusquedaObras() {
		return valorOpcionBusquedaObras;
	}

	public void setValorOpcionBusquedaObras(int valorOpcionBusquedaObras) {
		this.valorOpcionBusquedaObras = valorOpcionBusquedaObras;
	}
	
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
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
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
	
	public List<InsumosExt> getListInsumos() {
		return listInsumos;
	}

	public void setListInsumos(List<InsumosExt> listInsumos) {
		this.listInsumos = listInsumos;
	}
	
	public InsumosExt getPojoInsumo() {
		return pojoInsumo;
	}

	public void setPojoInsumo(InsumosExt pojoInsumo) {
		this.pojoInsumo = pojoInsumo;
	}
	
	public InsumosExt getPojoInsumoEliminar() {
		return pojoInsumoEliminar;
	}

	public void setPojoInsumoEliminar(InsumosExt pojoInsumoEliminar) {
		this.pojoInsumoEliminar = pojoInsumoEliminar;
	}

	public int getNumPaginaMateriales() {
		return numPaginaMateriales;
	}

	public void setNumPaginaMateriales(int numPaginaMateriales) {
		this.numPaginaMateriales = numPaginaMateriales;
	}

	public int getNumPaginaManoDeObra() {
		return numPaginaManoDeObra;
	}

	public void setNumPaginaManoDeObra(int numPaginaManoDeObra) {
		this.numPaginaManoDeObra = numPaginaManoDeObra;
	}

	public int getNumPaginaHerramientas() {
		return numPaginaHerramientas;
	}

	public void setNumPaginaHerramientas(int numPaginaHerramientas) {
		this.numPaginaHerramientas = numPaginaHerramientas;
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
	
	public List<InsumoRow> getListProductos() {
		return listProductos;
	}

	public void setListProductos(List<InsumoRow> listProductos) {
		this.listProductos = listProductos;
	}

	public int getNumPaginaProductos() {
		return numPaginaProductos;
	}

	public void setNumPaginaProductos(int numPaginaProductos) {
		this.numPaginaProductos = numPaginaProductos;
	}
	
	public List<InsumosDetallesExt> getListOtros() {
		return listOtros;
	}

	public void setListOtros(List<InsumosDetallesExt> listOtros) {
		this.listOtros = listOtros;
	}

	public int getNumPaginaOtros() {
		return numPaginaOtros;
	}

	public void setNumPaginaOtros(int numPaginaOtros) {
		this.numPaginaOtros = numPaginaOtros;
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
		return numBusquedaPaginaProductos;
	}

	public void setNumBusquedaPaginaProductos(int numBusquedaPaginaProductos) {
		this.numBusquedaPaginaProductos = numBusquedaPaginaProductos;
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

	public double getProductoBusquedaPrecio() {
		return productoBusquedaPrecio;
	}

	public void setProductoBusquedaPrecio(double productoBusquedaPrecio) {
		this.productoBusquedaPrecio = productoBusquedaPrecio;
	}

	public double getProductoBusquedaCantidad() {
		return productoBusquedaCantidad;
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado mensajes a consola de carga de insumos detalles
 */
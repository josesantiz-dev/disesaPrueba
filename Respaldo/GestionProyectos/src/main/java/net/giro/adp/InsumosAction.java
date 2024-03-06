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

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.adp.beans.InsumoRow;
import net.giro.adp.beans.InsumosDetalles;
import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.adp.beans.InsumosExt;
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
	private InitialContext ctx;
	// Interfaces
	private InsumosRem 			ifzInsumos;
	private InsumosDetallesRem 	ifzInsumosDet;
	private ObraRem 			ifzObras;
	private ProductoRem 		ifzProducto;
	private ConGrupoValoresRem 	ifzGpoVal;
	private ConValoresRem 		ifzConValores;
	// Listas
	private List<InsumosExt> 		 listInsumos;
	private List<Obra>		 		 listObras;
	private List<InsumosDetallesExt> listMateriales;
	private List<InsumosDetallesExt> listManoDeObra;
	private List<InsumosDetallesExt> listHerramientas;
	private List<InsumosDetallesExt> listOtros;
	private List<InsumoRow> 		 listProductos;
	private List<InsumosDetallesExt> listDetallesEliminados;
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
	private String mensaje;
	private int tipoMensaje;
    private long usuarioId;
	private int numPaginaMateriales;
	private int numPaginaManoDeObra;
	private int numPaginaHerramientas;
	private int numPaginaProductos;
	private int numPaginaOtros;
	private byte[] fileSrc; 
	private String fileName;
	private String fileExtension;
	private int countOtros;
	HashMap<String, String> insumosCellReference = new HashMap<String, String>();
	private boolean escapeForInputMesssage;


	public InsumosAction() {
		FacesContext fc = null;
		Application app = null;
		LoginManager loginManager = null;
		ValueExpression dato = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			this.ctx = new InitialContext();
			this.ifzInsumos  	= (InsumosRem)  		this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzInsumosDet  = (InsumosDetallesRem)  this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosDetallesFac!net.giro.adp.logica.InsumosDetallesRem");
			this.ifzObras 	 	= (ObraRem) 	 		this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzProducto 	= (ProductoRem) 		this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzGpoVal 		= (ConGrupoValoresRem) 	this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores 	= (ConValoresRem) 		this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
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

			// Obtenemos el acceso
			dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			loginManager = (LoginManager) dato.getValue(fc.getELContext());
			
			this.usuarioId = 0;
			this.usuarioId = (long) loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			this.ifzInsumos.setInfoSecion(loginManager.getInfoSesion());
			this.ifzInsumos.showSystemOuts(false);
			this.ifzObras.showSystemOuts(false);
			
			// Busqueda principal
			tiposBusqueda = new ArrayList<SelectItem>();
			tiposBusqueda.add(new SelectItem("nombreObra", "Obra"));
			tiposBusqueda.add(new SelectItem("id", "Clave"));
			campoBusqueda = tiposBusqueda.get(0).getDescription();
			valorBusqueda = "";
			
			// Busqueda obras
			tiposBusquedaObras = new ArrayList<SelectItem>();
			tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			tiposBusquedaObras.add(new SelectItem("id", "Clave"));
			campoBusquedaObras = tiposBusquedaObras.get(0).getDescription();
			valorBusquedaObras = "";
			valorOpcionBusquedaObras = 0;
			
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
            for(String key : keys)
            	this.insumosCellReference.put(key, this.entornoProperties.getString(key));
            
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
			this.operacionCancelada = false;
			this.mensaje = "";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = "id";
			
			if (this.listInsumos != null)
				this.listInsumos.clear();
			
			this.listInsumos = this.ifzInsumos.findLikePropertyExt(this.campoBusqueda, this.valorBusqueda, 80);
			if (this.listInsumos.isEmpty()) {
	    		this.operacionCancelada = true;
				this.tipoMensaje = 2;
				this.mensaje = "ERROR";
				return;
			}

			this.mensaje = "OK";
		} catch (Exception e) {
			this.operacionCancelada = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en buscar", e);
		}
	}
	
	public void nuevo() {
		try {
			this.operacionCancelada = false;
			this.mensaje = "";
			this.tipoMensaje = 0;

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
			this.operacionCancelada = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en buscar", e);
		}
	}
	
	public void editar() {
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
			List<InsumosDetalles> lista = this.ifzInsumosDet.findByProperty("idInsumo", this.pojoInsumo.getId(), 0);
			if (lista != null && ! lista.isEmpty()) {
				detalles = lista.size();
				log.info("Extendiendo detalles de insumo");
				List<InsumosDetallesExt> listAux = this.ifzInsumosDet.extenderInsumosDetalles(lista);

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
			}
			
			log.info(detalles  + " detalles de insumo obtenidos");
		} catch (Exception e) {
			control(true);
			log.error("Error en buscar", e);
		}
	}
	
	public void actualizar() {
		int indexInsumoDet = 0;
		Producto producto = null;
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
			this.pojoInsumo.setMontoMateriales(new BigDecimal(monto));

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
			
			this.pojoInsumo.setModificadoPor(this.usuarioId);
			this.pojoInsumo.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzInsumos.update(this.pojoInsumo);
			
			this.mensaje = "OK";
			log.info("INSUMOS GUARDADOS");
		} catch (Exception e) {
			control(true);
			log.error("Error en GestionProyectos.InsumosAction.guardar", e);
			log.info("Me quede en el indice " + indexInsumoDet);
		}
	}
	
	public void guardar() {
		boolean esNuevo = false;
		int indexInsumoDet = 0;
		
		try {
			control();
			log.info("Guardando insumos");
			// Comprobamos que no halla insumos con la obra indicada
			log.info("Comprobamos insumos");
			if(comprobarInsumos()) {
				control(5);
				log.error("Error al guardar: Ya existen insumos con la obra especificada");
				return;
			}
			
			// Comprobamos si no hay detalles para eliminar
			if (! this.listDetallesEliminados.isEmpty()) {
				for (InsumosDetallesExt var : this.listDetallesEliminados) {
					if(var.getId() != null || var.getId() > 0L) {
						this.ifzInsumosDet.delete(var.getId());
					}
				}
			}
			
			this.pojoInsumo.setModificadoPor(this.usuarioId);
			this.pojoInsumo.setFechaModificacion(Calendar.getInstance().getTime());

			log.info("Guardando insumo");
			if (this.pojoInsumo.getId() == null || this.pojoInsumo.getId() <= 0L) {
				// Nuevo INSUMOS
				esNuevo = true;
				this.pojoInsumo.setCreadoPor(this.usuarioId);
				this.pojoInsumo.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos el encabezado de los INSUMOS
				this.pojoInsumo.setId(this.ifzInsumos.save(this.pojoInsumo));
			} else {
				// Actualizacion de INSUMOS
				this.ifzInsumos.update(this.pojoInsumo);
			}
			
			// Guardamos los detalles de los INSUMOS
			
			// MATERIALES
			log.info("Guardando detalles: MATERIALES");
			for (InsumosDetallesExt var : this.listMateriales) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.pojoInsumo);
				
				if(var.getId() == null || var.getId() <= 0L) {
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
				var.getIdProducto().setTipo(TipoMaestro.Inventario.ordinal());
				var.getIdProducto().setTipoInsumo(TipoInsumo.Material.ordinal());
				this.ifzProducto.update(var.getIdProducto());
			}
			
			// MANO DE OBRA
			log.info("Guardando detalles: MANO DE OBRA");
			for (InsumosDetallesExt var : this.listManoDeObra) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.pojoInsumo);
				
				if(var.getId() == null || var.getId() <= 0L) {
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
				var.getIdProducto().setTipo(TipoMaestro.Inventario.ordinal());
				var.getIdProducto().setTipoInsumo(TipoInsumo.ManoDeObra.ordinal());
				this.ifzProducto.update(var.getIdProducto());
			}
			
			// HERRAMIENTAS
			log.info("Guardando detalles: HERRAMIENTAS");
			for (InsumosDetallesExt var : this.listHerramientas) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.pojoInsumo);
				
				if(var.getId() == null || var.getId() <= 0L) {
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
				var.getIdProducto().setTipo(TipoMaestro.Inventario.ordinal());
				var.getIdProducto().setTipoInsumo(TipoInsumo.Herramienta.ordinal());
				this.ifzProducto.update(var.getIdProducto());
			}
			
			// OTROS
			log.info("Guardando detalles: OTROS");
			for (InsumosDetallesExt var : this.listOtros) {
				indexInsumoDet = this.listMateriales.indexOf(var);
				var.setIdInsumos(this.pojoInsumo);
				
				if(var.getId() == null || var.getId() <= 0L) {
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
				var.getIdProducto().setTipo(TipoMaestro.Inventario.ordinal());
				var.getIdProducto().setTipoInsumo(TipoInsumo.Otros.ordinal());
				this.ifzProducto.update(var.getIdProducto());
			}
			
			// Añadimos/Actualizamos a la lista
			log.info("Añadimos/Actualizamos a la lista");
			if (esNuevo) {
				this.listInsumos.add(this.pojoInsumo);
			} else {
				for(InsumosExt var : this.listInsumos) {
					if (var.getId() == this.pojoInsumo.getId()) {
						var = this.pojoInsumo;
						break;
					}
				}
			}
			
			this.mensaje = "OK";
			log.info("INSUMOS GUARDADOS");
		} catch (Exception e) {
			log.error("Error en GestionProyectos.InsumosAction.guardar", e);
			log.info("Me quede en el indice " + indexInsumoDet);
			control(true);
		}
	}
	
	public void eliminar() {
		try {
			this.operacionCancelada = false;
			this.mensaje = "";
			this.tipoMensaje = 0;
			
			if(this.pojoInsumoEliminar != null && this.pojoInsumoEliminar.getId() != null && this.pojoInsumoEliminar.getId() > 0L) {
				// Buscamos el insumo en la lista
				for(InsumosExt var : this.listInsumos) {
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
			this.operacionCancelada = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en buscar", e);
		}
	}
	
	// BUSQUEDA OBRAS
	// ------------------------------------------------------------------------------
	public void nuevaBusquedaObras() {
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
		this.valorBusquedaObras = "";
		this.valorOpcionBusquedaObras = 0;
		
		if (this.listObras != null)
			this.listObras.clear();
	}

	public void buscarObras() {
		try {
			this.operacionCancelada = false;
			this.mensaje = "";
			this.tipoMensaje = 0;
			
			if ("".equals(String.valueOf(this.valorOpcionBusquedaObras)))
				this.valorOpcionBusquedaObras = 0;
			
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
			
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.valorOpcionBusquedaObras);
			if(this.listObras.isEmpty()){
	    		this.operacionCancelada = true;
				this.tipoMensaje = 2;
				this.mensaje = "ERROR";
			}

			this.mensaje = "OK";
		} catch (Exception e) {
			this.operacionCancelada = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en buscar", e);
		}
	}
	
	public void seleccionarObra() {
		nuevaBusquedaObras();
	}
	
	// INSUMOS: ARCHIVO EXCEL
	// -----------------------------------------------------------------------------
	public void subirExcel() {
		this.fileSrc = null;
		this.fileName = "";
		this.fileExtension = "";
		this.countOtros = 0;
	}
	
	public void uploadListener(FileUploadEvent event) throws Exception {
		try {
			this.operacionCancelada = false;
			this.tipoMensaje = 0;
			this.mensaje = "";
			
			this.fileSrc = event.getUploadedFile().getData();
			this.fileName = stripExtension(event.getUploadedFile().getName());
			this.fileExtension = Files.getFileExtension(event.getUploadedFile().getName());
		} catch (Exception e) {
			this.operacionCancelada = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en uploadListener", e);
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void analizarExcel() throws Exception {
		try {
			control();
			if(this.fileSrc == null)
				return;
			
			if(this.fileName == null || "".equals(this.fileName))
				this.fileName = "archivo";
			
			if(this.fileExtension == null || "".equals(this.fileExtension))
				this.fileExtension = "xls";
			
			log.info("Procesando archivo: " + this.fileName);
			Respuesta respuesta = ifzInsumos.analizarExcel(this.fileSrc, this.fileExtension, this.insumosCellReference);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(true, -1, respuesta.getErrores().getDescError());
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
			
			/*double montoOtros = 0;
			for(InsumosDetallesExt var : this.listOtros)
				montoOtros += var.getMonto().doubleValue();
			this.pojoInsumo.setMontoOtros(new BigDecimal(montoOtros));
			this.pojoInsumo.setTotal(new BigDecimal(this.pojoInsumo.getTotal().doubleValue() + montoOtros));*/
			
			if (this.listProductos != null && ! this.listProductos.isEmpty()) {
				log.error(this.tipoMensaje + " - Maestro NO actualizado. No se encontraron " + this.listProductos.size() + " productos");
				for (InsumoRow var : this.listProductos)
					log.info("---> Producto: " + var.getClave() + " - " + var.getDescripcion());
				control(4);
			}
		} catch (Exception e) {
			log.error("Error en GestionProyectos.InsumosAction.analizarExcel", e);
			control(true);
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
	
	private void control() {
		control(false, 0, "");
	}
	
	private void control(boolean value) {
		this.operacionCancelada = value;
		
		if (! value) {
			control(false, 0, "");
			return;
		} 

		control(true, 1, "ERROR");
	}
		
	private void control(int tipoMensaje) {
		if (tipoMensaje == 0) {
			control(false);
			return;
		}
		
		control(true, tipoMensaje, "ERROR");
	}
	
	private void control(boolean val1, int val2, String val3) {
		this.operacionCancelada = val1;
		this.tipoMensaje = val2;
		this.mensaje = val3;
	}
	
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
				control(true, -1, "Debe indicar un criterio de busqueda");
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
			if (this.familiaBusquedaProductos > 0L) params.put("familia", String.valueOf(this.familiaBusquedaProductos));
			
			this.listBusquedaProductos = this.ifzProducto.findLikeProperties(params, null, 0, this.campoBusquedaProductos);
			if(this.listBusquedaProductos.isEmpty()) {
				log.info("Buscando Productos. ERROR 2: Busqueda sin resultados");
	    		control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listBusquedaProductos != null) this.listBusquedaProductos.clear();
    		log.error("Error en Compras.RequisicionAction.buscarProductos", e);
    		control(true);
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
				
				/*if (producto.getId().equals(10028376L)) {
					this.productoBusquedaCantidad = 136.00;
					this.productoBusquedaPrecio = 52.99;
					importe = this.productoBusquedaCantidad * this.productoBusquedaPrecio;
				}*/
				
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
			if (this.listProductos != null) this.listProductos.clear();
    		log.error("Error en Compras.RequisicionAction.seleccionarProducto", e);
    		control(true);
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
				if (! var.getValor().equals(valTipoMaestro)) continue;
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
			log.error("Error al cargar las familias", e);
		}
	}
	
	// -----------------------------------------------------------------------------
	// PROPIEDADES
	// -----------------------------------------------------------------------------

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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado mensajes a consola de carga de insumos detalles
 */
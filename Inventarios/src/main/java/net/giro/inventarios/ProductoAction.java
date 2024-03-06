package net.giro.inventarios;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
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

import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.comun.TipoInsumo;
import net.giro.inventarios.comun.TipoMaestro;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.navegador.LoginManager;
import net.giro.navegador.comun.Permiso;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.logica.MonedaRem;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.richfaces.event.FileUploadEvent;

import com.google.common.io.Files;

@ViewScoped
@ManagedBean(name="productoAction")
public class ProductoAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ProductoAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private Permiso permisos;
	private HttpSession httpSession;
	private ReportesRem	ifzReportes;
    private String userPrefix;
    private int LIMITE_RESULTADOS;
	private int numPagina;
	private int tipoBusqueda;
	private String valorBusquedaProducto;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private ProductoRem ifzProductos;
	private boolean buscarEnAdministrativo;
	private double limiteInferiorActivos;
	// ----------------------------------------------------------------
	private List<Producto> listProductos;
	private Producto pojoProducto;
	private long idProducto;
	// Busqueda Productos
	private List<SelectItem> listaCampoBusquedaProductos;
	private String campoBusquedaProducto;
	private List<SelectItem> listaCboUnidadesMedida;
	private ConGrupoValores pojoGpoUnidadesMedida;
	private List<ConValores> listaUnidadesMedida;
	private long idUnidadMedida;
	private long idMoneda;
	private List<SelectItem> listaCboFamilias;
	private ConGrupoValores pojoGpoGastos;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	private ConGrupoValores pojoGpoSubfamilias;
	private List<ConValores> listaFamilias;
	private long idFamilia;
	private TipoMaestro tipoMaestroBusqueda;
	private TipoMaestro tipoMaestroExportar;
	private List<SelectItem> listTiposInsumos;
	// Subir archivos
	private byte[] fileSrc; 
	private String fileExtension;
	private int refSize;
	private List<Producto> productosProcesados;
	private LinkedHashMap<String, String> maestroCellReference = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, LinkedHashMap<Integer, LinkedHashMap<String, Object>>> listHojasMap = null;
	// Autogenerador de codigo producto
	private ConGrupoValores pojoGpoNivel0;
	private long idMaestro;
	private TipoMaestro valMaestro;
	private List<ConValores> listCodeMaestros;
	private List<SelectItem> listCodeMaestrosItems;
	private List<ConValores> listaCodeEspecialidades;
	private List<ConValores> listaCodeFamilias;
	private List<ConValores> listaCodeSubfamilias;
	private List<SelectItem> listaCodeEspecialidadesItems;
	private List<SelectItem> listaCodeFamiliasItems;
	private List<SelectItem> listaCodeSubfamiliasItems;
	private long idCodeEspecialidad;
	private long idCodeEspecialidadPrev;
	private long idCodeFamilia;
	private long idCodeFamiliaPrev;
	private long idCodeSubfamilia;
	private boolean mantenerCodigo;
	private String codigoOriginal;
	private String codigoPrevio;
	private long idEspecialidadActivo;
	private double precioPrevio;
	// Productos Caducos
	private List<Producto> listProductosCaducos;
	private int pagProdsCaducos;
	private boolean productoCaduco;
	private boolean editandoCaduco;
	private int caducoTipoMaestro;
	// Monedas
	private MonedaRem ifzMonedas;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private long idMonedaBase;
	// Catalogo
	private LinkedHashMap<String, String> catalogoCellReference;
	// PERFILES
	private boolean perfilEgresosOperacion;
	private boolean perfilCodificarEgresosOperacion;
	private boolean perfilCodificarInsumos;
	private double multiplicadorPrecioUnitario;
	private double multiplicadorPrecioVenta;
	// control
	private boolean operacionCompleta;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
 	public ProductoAction() {
		InitialContext ctx = null;
		HashMap<String, String> values = null;
		Set<String> keys = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
	
			values = new HashMap<String, String>();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{maestro}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			keys = this.entornoProperties.keySet();
	        for (String key : keys)
	        	values.put(key, this.entornoProperties.getString(key));
	        this.maestroCellReference = ordenaHashMap(values);
	        
	        values = new HashMap<String, String>();
	        ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{catalogo}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
	        for (String key : this.entornoProperties.keySet())
	        	values.put(key, this.entornoProperties.getString(key));
	        this.catalogoCellReference = ordenaHashMap(values);
	
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			this.operacionCompleta = true;
			this.numPagina = 1;
			
			this.LIMITE_RESULTADOS = 0;
			if (this.entornoProperties.containsKey("LIMITE_RESULTADOS"))
				this.LIMITE_RESULTADOS = Integer.parseInt(this.entornoProperties.getString("LIMITE_RESULTADOS"));

			this.multiplicadorPrecioUnitario = 1;
			if (this.entornoProperties.containsKey("precio.unitario")) {
				valPerfil = this.entornoProperties.getString("precio.unitario");
				this.multiplicadorPrecioUnitario = (valPerfil != null && ! "".equals(valPerfil.trim())) ? Double.parseDouble(valPerfil) : this.multiplicadorPrecioUnitario;
			}

			this.multiplicadorPrecioVenta = 1;
			if (this.entornoProperties.containsKey("precio.venta")) {
				valPerfil = this.entornoProperties.getString("precio.venta");
				this.multiplicadorPrecioVenta = (valPerfil != null && ! "".equals(valPerfil.trim())) ? Double.parseDouble(valPerfil) : this.multiplicadorPrecioVenta;
			}
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresosOperacion = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			log.info("Perfil EGRESOS_OPERACION: " + (this.perfilEgresosOperacion ? "SI" : "NO"));

			valPerfil = this.loginManager.getAutentificacion().getPerfil("CODIFICAR_EGRESOS");
			this.perfilCodificarEgresosOperacion = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			log.info("Perfil CODIFICAR_EGRESOS: " + (this.perfilCodificarEgresosOperacion ? "SI" : "NO"));

			valPerfil = this.loginManager.getAutentificacion().getPerfil("CODIFICAR_INSUMOS");
			this.perfilCodificarInsumos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			log.info("Perfil CODIFICAR_INSUMOS: " + (this.perfilCodificarInsumos ? "SI" : "NO"));

			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_LIMITE_INFERIOR_GASTOS");
			this.limiteInferiorActivos = ((valPerfil != null && ! "".equals(valPerfil)) ? Double.valueOf(valPerfil.trim()) : 5000);

			// Permisos
			valPerfil = (this.entornoProperties.containsKey("PRODUCTOS") ? this.entornoProperties.getString("PRODUCTOS") : "PRODUCTOS");
			valPerfil = (valPerfil != null && ! "".equals(valPerfil.trim())) ? valPerfil.trim() : "0";
			this.permisos = this.loginManager.getPermisos(this.loginManager.getIdAplicacion(), Long.parseLong(valPerfil));
			
			ctx = new InitialContext();
			this.ifzProductos = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGpoVal = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzMonedas = (MonedaRem) ctx.lookup("ejb:/Logica_TYG//MonedaFac!net.giro.tyg.logica.MonedaRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Grupo de valores para UNIDAD DE MEDIDA de productos 
			this.pojoGpoUnidadesMedida = this.ifzGpoVal.findByName("SYS_UNIDAD_MEDIDA");
			if (this.pojoGpoUnidadesMedida == null || this.pojoGpoUnidadesMedida.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_UNIDAD_MEDIDA en con_grupo_valores");
			
			// Grupo de valores para TIPO DE MAESTROS (Nivel Cero) de productos 
			this.pojoGpoNivel0 = this.ifzGpoVal.findByName("SYS_CODE_NIVEL0");
			if (this.pojoGpoNivel0 == null || this.pojoGpoNivel0.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_CODE_NIVEL0 en con_grupo_valores");
			else
				cargarNivel0();
			
			// Grupo de valores para ESPECIALIDADES de productos
			this.pojoGpoEspecialidades = this.ifzGpoVal.findByName("SYS_PRODUCTO_ESPECIALIDADES");
			if (this.pojoGpoEspecialidades == null || this.pojoGpoEspecialidades.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_PRODUCTO_ESPECIALIDADES en con_grupo_valores");

			// Grupo de valores para FAMILIAS de productos 
			this.pojoGpoFamilias = this.ifzGpoVal.findByName("SYS_FAMILIA_PRODUCTO");
			if (this.pojoGpoFamilias == null || this.pojoGpoFamilias.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_FAMILIA_PRODUCTO en con_grupo_valores");

			// Grupo de valores para PRODUCTO SUBFAMILIAS de productos 
			this.pojoGpoSubfamilias = this.ifzGpoVal.findByName("SYS_PRODUCTO_SUBFAMILIA");
			if (this.pojoGpoSubfamilias == null || this.pojoGpoSubfamilias.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_PRODUCTO_SUBFAMILIA en con_grupo_valores");
			
			// Grupo de valores para SYS_MOVGTOS (Gastos)
			this.pojoGpoGastos = this.ifzGpoVal.findByName("SYS_MOVGTOS");
			if (this.pojoGpoGastos == null || this.pojoGpoGastos.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");

			this.listProductos = new ArrayList<Producto>();
			this.pojoProducto = new Producto();

			// Prefijo por default para dar cabida a codigos caducos
			this.userPrefix = "SYS";

			this.listaUnidadesMedida = this.ifzConValores.buscaValorGrupo("descripcion", "", this.pojoGpoUnidadesMedida);
			this.listaFamilias = this.ifzConValores.buscaValorGrupo("descripcion", "", this.pojoGpoFamilias);
			
			// Busqueda principal :: OBSOLETA
			this.listaCampoBusquedaProductos = new ArrayList<SelectItem>();
			this.listaCampoBusquedaProductos.add(new SelectItem("*", "Coincidencia"));
			this.listaCampoBusquedaProductos.add(new SelectItem("descripcion", "Nombre"));
			this.listaCampoBusquedaProductos.add(new SelectItem("clave", "Clave"));
			this.listaCampoBusquedaProductos.add(new SelectItem("id", "ID"));
			this.campoBusquedaProducto = this.listaCampoBusquedaProductos.get(0).getValue().toString();
			this.valorBusquedaProducto = "";
			this.numPagina = 1;
			
			cargarCboUnidadesMedida();
			cargarTiposInsumos();
			
			this.listProductosCaducos = new ArrayList<Producto>();
			this.pagProdsCaducos = 1;
			this.valMaestro = TipoMaestro.Inventario;
			this.tipoMaestroExportar = TipoMaestro.Inventario;
			this.tipoMaestroBusqueda = TipoMaestro.Ninguno;
			this.idEspecialidadActivo = 10002580;
		} catch (Exception e) {
			log.error("Error en constructor Inventarios.ProductoAction", e);
		}
	}
	
	public void buscar() {
		TipoMaestro busquedaMaestro = null;
		
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			/*if (! this.permisos.getConsultar()) {
				control(-1, "No tiene permitido Consultar informacion");
				controlLog("301 - No tiene permitido Consultar informacion");
				return;
			}*/
			
			busquedaMaestro = this.buscarEnAdministrativo ? TipoMaestro.Administrativo : TipoMaestro.Inventario;
			this.campoBusquedaProducto = (this.campoBusquedaProducto != null && ! "".equals(this.campoBusquedaProducto.trim())) ? this.campoBusquedaProducto.trim() : this.listaCampoBusquedaProductos.get(0).getValue().toString();
			this.valorBusquedaProducto = (this.valorBusquedaProducto != null && ! "".equals(this.valorBusquedaProducto.trim())) ? this.valorBusquedaProducto.trim() : "";
			if ("".equals(this.valorBusquedaProducto)) {
				control(4, "Debe indicar el criterio de busqueda");
				return;
			}
			
			log.info("Buscando productos... " + this.campoBusquedaProducto + ": " + this.valorBusquedaProducto);
			this.listProductos = this.ifzProductos.findLikeProperty(this.campoBusquedaProducto, this.valorBusquedaProducto, 0L, busquedaMaestro.ordinal(), "", this.LIMITE_RESULTADOS);
			if (this.listProductos == null || this.listProductos.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Productos", e);
		} finally {
			this.numPagina = 1;
			this.listProductos = this.listProductos != null ? this.listProductos : new ArrayList<Producto>();
			log.info("Se encontraron " + this.listProductos.size() + " productos");
		}
	}

	public void buscarGuardar() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<Producto> lista = new ArrayList<Producto>();
		String codigo = "";
		
		try {
			control();
			log.info("Proceso para codificar productos");
			params.put("clave", "");
			params.put("tipo", TipoMaestro.Administrativo.ordinal());
			lista = this.ifzProductos.findByProperties(params, 0);
			if (lista == null || lista.isEmpty()) {
				log.info("Sin productos para codificar");
				control(2, "Busqueda sin resultados");
				return;
			}
			
			log.info("Se encontraron " + lista.size() + " productos para codificar");
			for (Producto var : lista) {
				log.info("Codificando producto " + var.getId() + " - " + lista.indexOf(var) + "/" + lista.size());
				codigo = "ACOPCH" + String.format("%1$05d", getConsecutivo("ACOPCH", "00001-99999"));
				var.setClave(codigo);
				var.setOrigenCodigo(this.idCodeEspecialidad + "-" + this.idCodeFamilia + "-" + this.idCodeSubfamilia);
				var.setEspecialidad(this.idCodeEspecialidad);
				var.setFamilia(this.idCodeFamilia);
				var.setSubfamilia(this.idCodeSubfamilia);
				this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzProductos.update(var);
				log.info("Producto " + var.getId() + " codificado: " + codigo);
			}
			
			log.info("Proceso terminado");
		} catch (Exception e) {
			control("Ocurrio un problema con proceso interno BuscarGuardar", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			if (! this.permisos.getEditar()) {
				control(-1, "No tiene permitido Añadir/Editar informacion");
				controlLog("301 - No tiene permitido Añadir/Editar informacion");
				return;
			}
			
			this.idCodeEspecialidad = -1;
			this.mantenerCodigo = false;
			this.codigoOriginal = "";
			this.codigoPrevio = "";
			initGeneradorListas();
			this.idProducto = 0L;
			this.pojoProducto = new Producto();
			this.pojoProducto.setClave("");
			this.pojoProducto.setOrigenCodigo("");
			cargarMonedas();
			setIdMoneda(0);
			setIdUnidadMedida(0);
		} catch (Exception e) {
			control("Ocurrio un problema al preparar el nuevo Producto", e);
		}
	}

	public void nuevoMaestroInventarios() {
		String tMaestro = String.valueOf(TipoMaestro.Inventario.ordinal());
		
		this.productoCaduco = false;
		this.valMaestro = TipoMaestro.Inventario;
		for (ConValores var : this.listCodeMaestros) {
			if (! tMaestro.equals(var.getValor())) 
				continue;
			this.idMaestro = var.getId();
			break;
		}
		
		if (this.pojoProducto != null)
			this.pojoProducto.setTipoInsumo(TipoInsumo.Material.ordinal());
		
		initGeneradorListas();
		generaCodigoProducto();
	}
	
	public void nuevoMaestroAdministrativo() {
		String tMaestro = String.valueOf(TipoMaestro.Administrativo.ordinal());
		
		this.productoCaduco = false;
		this.valMaestro = TipoMaestro.Administrativo;
		for (ConValores var : this.listCodeMaestros) {
			if (! tMaestro.equals(var.getValor())) 
				continue;
			this.idMaestro = var.getId();
			break;
		}
		
		if (this.pojoProducto != null)
			this.pojoProducto.setTipoInsumo(TipoInsumo.Material.ordinal());
		
		initGeneradorListas();
		generaCodigoCaduco();
	}
	
	public void editar() {
		String codigo = "";
		String origenCodigo = "";
		String tMaestro = "";
		
		try {
			control();
			this.pojoProducto = this.ifzProductos.findById(this.idProducto);
			if (this.pojoProducto == null || this.pojoProducto.getId() == null || this.pojoProducto.getId() <= 0L) {
				control(-1, "Debe indicar el Producto a editar");
				return;
			}

			cargarMonedas();
			this.mantenerCodigo = false;
			this.idCodeEspecialidad = -1;
			this.productoCaduco = false;
			this.codigoOriginal = "";
			this.codigoPrevio = this.pojoProducto.getClave();
			this.precioPrevio = this.pojoProducto.getPrecioCompra();
			
			if (this.pojoProducto.getTipo() == TipoMaestro.Administrativo.ordinal()) {
				tMaestro = String.valueOf(TipoMaestro.Administrativo.ordinal());
				this.valMaestro = TipoMaestro.Administrativo;
				for (ConValores var : this.listCodeMaestros) {
					if (! tMaestro.equals(var.getValor())) 
						continue;
					this.idMaestro = var.getId();
					break;
				}
			} else {
				tMaestro = String.valueOf(TipoMaestro.Inventario.ordinal());
				this.valMaestro = TipoMaestro.Inventario;
				for (ConValores var : this.listCodeMaestros) {
					if (! tMaestro.equals(var.getValor())) 
						continue;
					this.idMaestro = var.getId();
					break;
				}
			}

			initGeneradorListas();
			codigo = this.pojoProducto.getClave();
			origenCodigo = this.pojoProducto.getOrigenCodigo();
			
			obtenerOrigenCodigo();

			//if (! this.editandoCaduco)
			this.pojoProducto.setClave(codigo);
			this.pojoProducto.setOrigenCodigo(origenCodigo);
			this.setIdUnidadMedida(this.pojoProducto.getUnidadMedida());
			if (this.pojoProducto.getIdMoneda() <= 0L)
				this.setIdMoneda(getMonedaBaseId());
			else
				this.setIdMoneda(this.pojoProducto.getIdMoneda());
		} catch (Exception e) {
			control("Ocurrio un problema al preparar el Producto para editar", e);
		}
	}

	public void guardar() {
		try {
			control();
			if (! validaciones()) 
				return;
			
			if (this.codigoPrevio != null && ! "".equals(this.codigoPrevio))
				this.pojoProducto.setClavePrevia(this.codigoPrevio);
			if (this.mantenerCodigo && ! this.codigoOriginal.equals(this.pojoProducto.getClave()))
				this.pojoProducto.setClave(this.codigoOriginal);
			this.codigoOriginal = "";
			this.mantenerCodigo = false;

			if (this.precioPrevio > 0 && this.precioPrevio != this.pojoProducto.getPrecioCompra()) {
				this.pojoProducto.setIdOrdenCompra(0L);
				this.pojoProducto.setOrdenCompra("");
			}
			
			// Asignamos descripcion de unidad de medida, especialidad, familia y subfamilia
			this.pojoProducto.setDescUnidadMedida(getDescripcionFromList(this.listaUnidadesMedida, this.pojoProducto.getUnidadMedida()));
			this.pojoProducto.setDescEspecialidad(getDescripcionFromList(this.listaCodeEspecialidades, this.idCodeEspecialidad));
			this.pojoProducto.setDescFamilia(getDescripcionFromList(this.listaCodeFamilias, this.idCodeFamilia));
			this.pojoProducto.setDescSubfamilia(getDescripcionFromList(this.listaCodeSubfamilias, this.idCodeSubfamilia));
			this.pojoProducto.setTipo(this.valMaestro.ordinal());
			
			if (this.pojoProducto.getId() == null || this.pojoProducto.getId() <= 0L) {
				// Añado a la BD y a la lista
				this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoProducto.setId(this.ifzProductos.save(this.pojoProducto));
				
				if (this.valMaestro.ordinal() == this.pojoProducto.getTipo())
					this.listProductos.add(0, this.pojoProducto);
			} else {
				// Actualizo en la BD
				this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzProductos.update(this.pojoProducto);

				// Actualizamo en listado si corresponde
				for (Producto producto : this.listProductos) {
					if (this.idProducto == producto.getId().longValue()) {
						producto = this.pojoProducto;
						break;
					}
				}
			}
			
			// Guardamos o actualizamos copia en Gastos si corresponde
			if (this.valMaestro == TipoMaestro.Administrativo)
				sincronizaEnGastos(this.pojoProducto);
			
			if (this.editandoCaduco)
				this.productosCaducos();
			this.precioPrevio = 0;
			nuevo();
		} catch (Exception e) {
			control("Ocurrio un problema al guardar el Producto", e);
		} 
	}
	
	public void eliminar() {
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			if (! this.permisos.getBorrar()) {
				control(-1, "No tiene permitido Borrar/Eliminar informacion");
				controlLog("301 - No tiene permitido Borrar/Eliminar informacion");
				return;
			}
			
			this.pojoProducto = this.ifzProductos.findById(this.idProducto);
			if (this.pojoProducto == null || this.pojoProducto.getId() == null || this.pojoProducto.getId() <= 0L)
				return;
			// Cancelamos producto
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			this.pojoProducto = this.ifzProductos.cancelar(this.pojoProducto.getId());
			// Actualizamo en listado si corresponde
			for (Producto producto : this.listProductos) {
				if (this.idProducto == producto.getId().longValue()) {
					producto = this.pojoProducto;
					break;
				}
			}
			/*this.pojoProductoEliminar.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoProductoEliminar.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoProductoEliminar.setEstatus(1);
			
			// Actualizamos en la BD
			this.ifzProductos.update( this.pojoProductoEliminar );*/
			this.nuevo();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar al Producto indicado", e);
		}
	}

	public void calcularPrecios() {
		double precio = 0;
		if (this.pojoProducto == null)
			return;
		
		precio = this.pojoProducto.getPrecioCompra() * this.multiplicadorPrecioUnitario;
		this.pojoProducto.setPrecioUnitario(precio);

		precio = this.pojoProducto.getPrecioCompra() * this.multiplicadorPrecioVenta;
		this.pojoProducto.setPrecioVenta(precio);
	}
	
	private void cargarCboUnidadesMedida() {
		this.listaCboUnidadesMedida = new ArrayList<SelectItem>();
		for (ConValores cv : this.listaUnidadesMedida) {
			if (cv.getValor().equals(cv.getDescripcion()))
				this.listaCboUnidadesMedida.add(new SelectItem(cv.getId(), cv.getDescripcion()));
			else
				this.listaCboUnidadesMedida.add(new SelectItem(cv.getId(), cv.getDescripcion() + " (" + cv.getValor() + ")"));
		}
	}
	
	private void cargarTiposInsumos() {
		try {
			this.listTiposInsumos = new ArrayList<SelectItem>();
			for (TipoInsumo tipo : TipoInsumo.values()) {
				if (tipo.ordinal() > 0)
					this.listTiposInsumos.add(new SelectItem(tipo.ordinal(), tipo.toString()));
			}
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.cargarTiposInsumos()", e);
		}
	}
	
	private boolean validaciones() {
		int intento = 0;
		int maxIntentosClave = 3;
		boolean claveRepetida = false;

		// Validamos permiso de Lectura/Consulta
		if (! this.permisos.getEditar()) {
			control(-1, "No tiene permitido Añadir/Editar informacion");
			controlLog("301 - No tiene permitido Añadir/Editar informacion");
			return false;
		}
		
		log.info("Validando producto");
		if ("".equals(this.pojoProducto.getClave().trim())) {
			control(-10, "Debe indicar la clave del producto");
			return false;
		}
		
		if ("".equals(this.pojoProducto.getDescripcion().trim())) {
			control(-11, "Debe indicar la descripcion del producto");
			return false;
		}
		
		if (this.pojoProducto.getUnidadMedida() <= 0)  {
			control(-19, "Producto sin unidad de medida");
			return false;
		}
		
		this.pojoProducto.setFamilia(this.idCodeFamilia);
		if (this.pojoProducto.getFamilia() <= 0)  {
			control(-20, "Producto sin familia");
			return false;
		}
		
		if (this.pojoProducto.getExistencia() < 0) {
			control(-16, "La existensia debe ser mayor o igual a cero");
			return false;
		}

		if (this.pojoProducto.getMinimo() < 0) {
			control(-12, "El Minimo debe ser mayor o igual a cero");
			return false;
		}

		if (this.pojoProducto.getMaximo() < 0) {
			control(-13, "El Maximo debe ser mayor o igual a cero");
			return false;
		}
		
		if (this.pojoProducto.getMinimo() > this.pojoProducto.getMaximo()) {
			control(-17, "El Minimo debe ser menor al Maximo");
			return false;
		}

		if (this.pojoProducto.getPrecioCompra() == 0) {
			control(-14, "Debe indicar el precio de compra");
			return false;
		}
		
		this.pojoProducto.setIdEmpresa(this.pojoProducto.getIdEmpresa() > 0L ? this.pojoProducto.getIdEmpresa() : this.loginManager.getInfoSesion().getEmpresa().getId());
		this.pojoProducto.setIdMoneda(this.pojoProducto.getIdMoneda() > 0L ? this.pojoProducto.getIdMoneda() : this.idMonedaBase);
		this.pojoProducto.setTipoCambio(this.pojoProducto.getTipoCambio() > 0 ? this.pojoProducto.getTipoCambio() : 1);
		this.pojoProducto.setPrecioCompraPesos(this.pojoProducto.getPrecioCompra() * this.pojoProducto.getTipoCambio());
		if (this.idMonedaBase != this.pojoProducto.getIdMoneda()) 
			asignaTipoCambio();
		
		// Obtenemos descripcion y abreviatura de moneda elegida
		for (Moneda var : this.listMonedas) {
			if (var.getId() != this.pojoProducto.getIdMoneda())
				continue;
			this.pojoProducto.setDescMoneda(var.getNombre());
			this.pojoProducto.setDescMonedaAbreviatura(var.getAbreviacion());
			break;
		}

		// Validar la clave: Generamos la clave las veces que sea necesario
		if (this.pojoProducto.getTipoInsumo() <= 0)
			this.pojoProducto.setTipoInsumo(TipoInsumo.Material.ordinal());
		
		do {
			intento += 1;
			log.info("---> Valido clave de producto : " + this.pojoProducto.getClave());
			claveRepetida = this.ifzProductos.validarClaveProducto(this.pojoProducto);
			if (claveRepetida) {
				if (this.pojoProducto.getTipoInsumo() > TipoInsumo.Material.ordinal())
					intento = maxIntentosClave + 1;
				else
					generaCodigoProducto();
			}
		} while(claveRepetida && intento <= maxIntentosClave);
		if (claveRepetida) {
			control(-18, "Producto clave repetida");
			return false;
		}
		
		log.info("Producto validado");
		return true;
	}

	private Producto copiaProducto(Producto target) {
		Producto resultado = new Producto();
		
		try {
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(resultado, target);
		} catch (Exception e) {
			log.error("Ocurrio un problema al copia el pojo Producto, procedo con copia manual", e);
			resultado = new Producto();
			resultado.setId(target.getId());
			resultado.setClave(target.getClave());
			resultado.setDescripcion(target.getDescripcion());
			resultado.setUnidadMedida(target.getUnidadMedida());
			resultado.setEspecialidad(target.getEspecialidad());
			resultado.setFamilia(target.getFamilia());
			resultado.setSubfamilia(target.getSubfamilia());
			resultado.setPrecioCompra(target.getPrecioCompra());
			resultado.setPrecioVenta(target.getPrecioVenta());
			resultado.setPrecioUnitario(target.getPrecioUnitario());
			resultado.setPrecioCompraPesos(target.getPrecioCompraPesos());
			resultado.setIdMoneda(target.getIdMoneda());
			resultado.setTipoCambio(target.getTipoCambio());
			resultado.setExistencia(target.getExistencia());
			resultado.setMaximo(target.getMaximo());
			resultado.setMinimo(target.getMinimo());
			resultado.setPermiteExcedente(target.getPermiteExcedente());
			resultado.setOrigenCodigo(target.getOrigenCodigo());
			resultado.setEstatus(target.getEstatus());
			resultado.setCreadoPor(target.getCreadoPor());
			resultado.setFechaCreacion(target.getFechaCreacion());
			resultado.setModificadoPor(target.getModificadoPor());
			resultado.setFechaModificacion(target.getFechaModificacion());
			resultado.setDescEspecialidad(target.getDescEspecialidad());
			resultado.setDescFamilia(target.getDescFamilia());
			resultado.setDescSubfamilia(target.getDescSubfamilia());
			resultado.setDescUnidadMedida(target.getDescUnidadMedida());
			resultado.setDescMoneda(target.getDescMoneda());
			resultado.setDescMonedaAbreviatura(target.getDescMonedaAbreviatura());
			resultado.setTipoInsumo(target.getTipoInsumo());
			resultado.setTipo(target.getTipo());  
		} 
		
		return resultado;
	}
	
	private long getEspecialidadFromFamilia(long idFamilia) {
		ConValores valEspecialidad = null;
		ConValores valFamilia = null;
		long idEspecialidad = 0;
		long tipoMaestro = 0;
		
		try {
			valFamilia = this.ifzConValores.findById(idFamilia);
			if (valFamilia != null && valFamilia.getAtributo1() != null && ! "".equals(valFamilia.getAtributo1())) {
				idEspecialidad = Long.parseLong(valFamilia.getAtributo1());
				valEspecialidad = this.ifzConValores.findById(idEspecialidad);

				if (valEspecialidad != null && valEspecialidad.getAtributo1() != null && ! "".equals(valEspecialidad.getAtributo1()))
					tipoMaestro = Long.parseLong(valEspecialidad.getAtributo1());
				if (tipoMaestro != this.idMaestro)
					throw new Exception("NO_MATCH_MAESTRO");
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar obtener la Especialidad a partir de la familia " + idFamilia, e);
			idEspecialidad = -1;
		}
		
		return idEspecialidad;
	}
	
	private void cargarMonedas() {
		try {
			this.ifzMonedas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listMonedas = this.ifzMonedas.findAll();
			this.listMonedas = this.listMonedas != null ? this.listMonedas : new ArrayList<Moneda>();

			this.listMonedasItems = new ArrayList<SelectItem>();
			for (Moneda mon : this.listMonedas) {
				if (this.loginManager.getUsuarioEmpresa().getMoneda().equals(mon.getAbreviacion()))
					this.idMonedaBase = mon.getId();
				this.listMonedasItems.add(new SelectItem(mon.getId(), mon.getNombre() + " (" + mon.getAbreviacion() + ")"));
			}
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.cargarMonedas", e);
		}
	}

	public void asignaTipoCambio() {
		double tipoCambio = 1.0;
		
		if (this.pojoProducto == null || this.pojoProducto.getIdMoneda() <= 0L)
			return;
		
		tipoCambio = this.pojoProducto.getTipoCambio() > 1 ? this.pojoProducto.getTipoCambio() : this.loginManager.getTipoCambioActual();
		this.pojoProducto.setTipoCambio(tipoCambio); 
		this.pojoProducto.setPrecioCompraPesos(this.pojoProducto.getPrecioCompra() * tipoCambio);
	}
	
	private long getMonedaBaseId() {
		try {
			if (this.listMonedas == null)
				cargarMonedas();
			
			for (Moneda var : this.listMonedas) {
				if (! "MXN".equals(var.getAbreviacion())) 
					continue;
				return var.getId();
			}
		} catch (Exception e) {
			log.error("No se pudo obtener el ID de la moneda base (MXN)", e);
		}
		
		return 0L;
	}
	
	private LinkedHashMap<String, String> ordenaHashMap(HashMap<String, String> values) {
		LinkedHashMap<String, String> resultado = null;
		List<String> arr = null;
		
		arr = new ArrayList<String>();
		for (Entry<String, String> entry : values.entrySet()) 
			arr.add(entry.getValue());

		Collections.sort(arr);
		resultado = new LinkedHashMap<String, String>();
		for (String val : arr) {
			for (Entry<String, String> entry : values.entrySet()) {
				if (entry.getValue().equals(val)) {
					resultado.put(entry.getKey(), entry.getValue());
					break;
				}
			}
		}
		
		return resultado;
	}
	
	public void reiniciaListas() {
		this.productoCaduco = false;
		generaCodigoCaduco();
	}
	
	private void control() {
		this.operacionCompleta = true;
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(String value) { 
		control(-1, value); 
	}
	
	private void control(int tipo, String mensaje) {
		control(true, tipo, mensaje, null);
	}

	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}

	private void control(boolean incompleta, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacionCancelada = incompleta;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "Ocurrio un problema no controlado al realizar la operacion");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje + "\n");
	}
	
	private void sincronizaEnGastos(Producto pojoProducto) {
		List<ConValores> lista = null;
		ConValores pojoGasto = null;
	
		try {
			if (pojoProducto.getPrecioCompra() < this.limiteInferiorActivos)
				return;

			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			lista = this.ifzConValores.buscaValorGrupo("valor", String.valueOf(pojoProducto.getId()), this.pojoGpoGastos); 
			if (lista != null && ! lista.isEmpty()) {
				pojoGasto = lista.get(0);
				
				if (this.idEspecialidadActivo == pojoProducto.getEspecialidad()) { 
					log.info("  --- Actualizando copia en Gasto");
					pojoGasto.setDescripcion(pojoProducto.getDescripcion());
					pojoGasto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());
					// Actualizo en la BD
					this.ifzConValores.update(pojoGasto);
					log.info("  --- Copia en Gasto actualizado");
				} else if (this.idEspecialidadActivo != pojoProducto.getEspecialidad()) {
					log.info("  --- Eliminando copia en Gasto");
					this.ifzConValores.delete(pojoGasto);
					log.info("  --- Copia en Gasto Eliminada");
				}
			} else {
				if (pojoProducto.getEspecialidad() != this.idEspecialidadActivo) 
					return; 
				
				log.info("  --- Generando copia en Gasto");
				pojoGasto = new ConValores();
				pojoGasto.setValor(String.valueOf(pojoProducto.getId()));
				pojoGasto.setDescripcion(pojoProducto.getDescripcion());
				pojoGasto.setGrupoValorId(this.pojoGpoGastos);
				pojoGasto.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				pojoGasto.setFechaCreacion(Calendar.getInstance().getTime());
				pojoGasto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());
				// Guardo en la BD
				pojoGasto.setId(this.ifzConValores.save(pojoGasto));
				log.info("  --- Gasto generado");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al hacer una copia del producto en Gastos: " + pojoProducto.getId(), e);
		}
	}
	
	// -------------------------------------------------------------------
	// Importar MAESTRO
	// -------------------------------------------------------------------
	
	public void nuevaCarga() {
		this.fileSrc = null;
		this.fileExtension = "";
		this.refSize = 0;
		this.productosProcesados = null;
	}
		
	public void cargarMaestro(FileUploadEvent event) throws Exception {
		try {
			control();
			this.fileSrc = event.getUploadedFile().getData();
			this.fileExtension = Files.getFileExtension(event.getUploadedFile().getName());
			this.productosProcesados = null;
		} catch (Exception e) {
			control("Ocurrio un problema al cargar el archivo Maestro", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void importarMaestro() throws Exception {
		List<Producto> temp =  new ArrayList<Producto>(); // Almacena los productos procesados
		List<Producto> tempComp = new ArrayList<Producto>(); // Almacena los productos comprobados
		HashMap<Integer, String> mapNoProcesados = null;
		Respuesta respuesta = null;
		Producto prod = null;
		long idMoneda = 0;
		int size = 0;
		int inicio = 0;
		int limite = 0;
		int vueltas = 0;
		int index = 0;
		
		try {
			control();
			if (this.fileSrc == null)
				return;

			this.listProductos = new ArrayList<Producto>();
			this.refSize = this.refSize > 0 ? this.refSize : this.fileSrc.length;
			this.fileExtension = this.fileExtension != null && ! "".equals(this.fileExtension.trim()) ? this.fileExtension.trim() : "xls";
			if (this.isDebug) {
				importarCatalogo();
				return;
			}
			
			// Leemos archivo
			// -----------------------------------------------------------------------------------
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzProductos.analizarArchivo(this.fileSrc, this.fileExtension, this.pojoGpoUnidadesMedida, this.pojoGpoFamilias, this.maestroCellReference);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError());
				return;
			}
			
			// Verificamos si hay productos sin procesar
			mapNoProcesados = (HashMap<Integer, String>) respuesta.getBody().getValor("sinprocesar");
			if (! mapNoProcesados.isEmpty()) {
				log.error(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(4, respuesta.getErrores().getDescError());
				return;
			}
			
			// Recuperamos la lista de productos
			this.productosProcesados = (List<Producto>) respuesta.getBody().getValor("productos");
			if (this.productosProcesados == null || this.productosProcesados.isEmpty()) {
				log.error(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(4, respuesta.getErrores().getDescError());
				return;
			}
			
			if (idMoneda <= 0)
				idMoneda = this.loginManager.getUsuarioEmpresa().getMonedaId(); //getMonedaBase("MXN");
			
			// Comprobamos productos
			// -----------------------------------------------------------------------------------
			log.info("---> Comprobar " + this.productosProcesados.size() + " productos ... ");
			size = this.productosProcesados.size();
			vueltas = (int) Math.ceil(size * 0.001);
			if (vueltas > 1)
				log.info("     ---> Comprobacion separada en " + vueltas + " lotes de 1000 productos");
			
			tempComp.clear();
			for (int v = 0; v < vueltas; v++) {
				inicio = v * 1000;
				limite = inicio + 1000;
				limite = (limite > size) ? size : limite;

				temp.clear();
				prod = null;
				for (index = inicio; index < limite; index++) {
					prod = this.productosProcesados.get(index);
					prod.setIdMoneda(idMoneda);
					temp.add(prod);
				}
				
				// Comprobamos los productos contra la BD
				try {
					log.info("     ---> Comprobando lote " + (v + 1) + "/" + vueltas);
					temp = this.ifzProductos.comprobarProductos(temp);
					
					// Añadimos a la lista principal
					tempComp.addAll(temp);
				} catch (Exception ex) {
					log.error("Ocurrio un problema al comprobar los productos en el lote " + (v + 1) + "/" + vueltas, ex);
				}
			}
			
			this.productosProcesados.clear();
			this.productosProcesados.addAll(tempComp);
			
			// Guardamos productos
			// -----------------------------------------------------------------------------------
			log.info("Guardar " + this.productosProcesados.size() + " productos");
			size = this.productosProcesados.size();
			vueltas = (int) Math.ceil(size * 0.001);
			if (vueltas > 1)
				log.info("---> Guardado separado en " + vueltas + " lotes de 1000 productos");
			
			for (int v = 0; v < vueltas; v++) {
				inicio = v * 1000;
				limite = inicio + 1000;
				limite = (limite > size) ? size : limite;

				temp.clear();
				for (index = inicio; index < limite; index++)
					temp.add(this.productosProcesados.get(index));
				
				// Guardamos en la BD
				log.info(" ---> Guardando lote " + (v + 1) + "/" + vueltas);
				temp = this.ifzProductos.saveOrUpdateList(temp);
				
				// Añadimos a la lista principal
				this.listProductos.addAll(temp);
			}
			log.info(this.productosProcesados.size() + " productos guardados");

			nuevaCarga();
			this.operacionCompleta = true;
			this.tipoMensaje = 5;
		} catch (Exception e) {
			control("Ocurrio un problema al procesar el archivo Maestro", e);
		}
	}

	// -------------------------------------------------------------------
	// Exportar MAESTRO
	// -------------------------------------------------------------------
	
	public void nuevoExportar() {
		try {
			control();
			this.tipoMaestroExportar = TipoMaestro.Inventario;
			this.idFamilia = 0L;
			if (this.perfilEgresosOperacion)
				cargarMaestrosExportar();
			cargarFamiliasExportar();
		} catch (Exception e) {
			control("Ocurrio un problema al inicializar la exportacion de Maestro", e);
		}
	}
	
	public void exportarMaestro() {
		SimpleDateFormat formatter = null;
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			log.info("Exportando Maestro " + this.tipoMaestroExportar.toString());
			formatter = new SimpleDateFormat("yyMMddHHmm");
			nombreDocumento = "MAESTRO-[tipo]-[fecha].[extension]";
			
			// Parametros del reporte
			log.info("Exportar Maestro. Generando parametros ... ");
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idTipoMaestro", this.tipoMaestroExportar.ordinal());
			paramsReporte.put("idFamilia", this.idFamilia);
			log.info("\nParametros:\n" + paramsReporte.toString());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.maestro"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());

			log.info("Exportar Maestro. Generando reporte ... ");
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte, 35);
			if (respuesta.getErrores().getCodigoError() > 0L) {
				log.error("ERROR INTERNO: " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control("Ocurrio un problema al intentar Exportar el Maestro\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			} 

			log.info("Exportar Maestro. Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = nombreDocumento.replace("[tipo]", (this.tipoMaestroExportar == TipoMaestro.Inventario ? "INSUMOS" : this.tipoMaestroExportar == TipoMaestro.Administrativo ? "ADMINISTRATIVO" : "NA"));
			nombreDocumento = nombreDocumento.replace("[fecha]", formatter.format(Calendar.getInstance().getTime()));
			nombreDocumento = nombreDocumento.replace("[extension]", formatoDocumento);
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido del Maestro");
				control("Ocurrio un problema al intentar recuperar el Maestro");
				return;
			}
			
			log.info("Exportar Maestro. Lanzando reporte ... ");
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			log.info("Maestro exportado. Proceso finalizado");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar exportar el Maestro", e);
		} finally {
			this.idFamilia = 0;
		}
	}
	
	private void cargarMaestrosExportar() {
		try {
			this.idFamilia = 0L;
			this.listaFamilias = new ArrayList<ConValores>();
			this.listaCboFamilias = new ArrayList<SelectItem>();
			
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCodeMaestros = this.ifzConValores.findAll(this.pojoGpoNivel0);
			this.listCodeMaestros = this.listCodeMaestros != null ? this.listCodeMaestros : new ArrayList<ConValores>();
			
			Collections.sort(this.listCodeMaestros, new Comparator<ConValores>() {
		    	@Override
		        public int compare(ConValores o1, ConValores o2) {
		    		return o1.getValor().compareTo(o2.getValor());
		        }
			});

			this.listCodeMaestrosItems = new ArrayList<SelectItem>();
			for (ConValores var : this.listCodeMaestros)
				this.listCodeMaestrosItems.add(new SelectItem(Integer.parseInt(var.getValor()), var.getDescripcion()));
		} catch (Exception e) {
			log.error("ERROR en Inventarios.ProductoAction.cargarMaestrosExportar. No se pudo cargar el Nivel Cero (Tipos de Maestros)", e);
		}
	}

	public void cargarFamiliasExportar() {
		List<ConValores> listAux = null;
		String valTipoMaestro = "";
		
		try {
			this.listaFamilias = new ArrayList<ConValores>();
			this.listaCboFamilias = new ArrayList<SelectItem>();
			log.info("Cargando Maestros");
			valTipoMaestro = String.valueOf(this.tipoMaestroExportar.ordinal());
			listAux = this.ifzConValores.findAll(this.pojoGpoNivel0);
			for (ConValores var : listAux) {
				if (! var.getValor().equals(valTipoMaestro)) 
					continue;
				
				log.info("Cargando Familias");
				this.listaFamilias = this.ifzProductos.recuperarFamiliasByMaestro(var.getId());
				if (this.listaFamilias == null || this.listaFamilias.isEmpty())
					return;
				break;
			}
			
			log.info("Generando lista de items (familias)");
			for (ConValores var : this.listaFamilias) {
				if (var.getValor().trim().equals(var.getDescripcion().trim()))
					this.listaCboFamilias.add(new SelectItem(var.getId(), var.getDescripcion()));
				else
					this.listaCboFamilias.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
			}
		} catch (Exception e) {
			log.error("Error al cargar las familias", e);
		} finally {
			if (this.listaCboFamilias != null && ! this.listaCboFamilias.isEmpty()) 
				log.info(this.listaCboFamilias.size() + " items (familias) generados");
		}
	}

	// -------------------------------------------------------------------------------------
	// CATALOGO
	// -------------------------------------------------------------------------------------

	public void importarCatalogo() throws Exception {
		LinkedHashMap<Integer, LinkedHashMap<String, Object>> mapItems = new LinkedHashMap<Integer, LinkedHashMap<String,Object>>();
		LinkedHashMap<String,Object> item = null;
		ConGrupoValores grupo = null;
		long idGrupo = 0;
		
		try {
			control();
			if(this.fileSrc == null)
				return;

			this.listProductos = new ArrayList<Producto>();
			this.refSize = refSize > 0 ? refSize : this.fileSrc.length;
			this.fileExtension = this.fileExtension != null && ! "".equals(this.fileExtension.trim()) ? this.fileExtension.trim() : "xls";
			
			// Leemos archivo
			// -----------------------------------------------------------------------------------
			mapItems = leerCatalogo("SUBFAMILIAS");
			if (mapItems == null || mapItems.isEmpty()) 
				return;
			
			ConValores pojo = null;
			HashMap<String, String> params = new HashMap<String, String>();
			for (Entry<Integer, LinkedHashMap<String, Object>> var : mapItems.entrySet()) {
				item = var.getValue();
				if (item.get("OWNER") == null || "".equals(item.get("OWNER").toString()))
					continue;
				
				idGrupo = Long.parseLong(item.get("GRUPO").toString());
				if (grupo == null || (grupo.getId() != idGrupo))
					grupo = this.ifzGpoVal.findById(idGrupo);

				params.put("descripcion", item.get("DESCRIPCION").toString());
				params.put("atributo1", item.get("OWNER").toString());
				List<ConValores> lista = this.ifzConValores.findByGrupoNombreByParams(grupo.getNombre(), params);
				if (lista != null && ! lista.isEmpty()) {
					if (lista.size() == 1) {
						pojo = lista.get(0);
						if (pojo.getAtributo1() != null && ! pojo.getAtributo1().equals(item.get("OWNER").toString())) {
							pojo = new ConValores();
							pojo.setDescripcion(item.get("DESCRIPCION").toString());
							pojo.setAtributo1(item.get("OWNER").toString());
							pojo.setValor(item.get("VALOR").toString());
							pojo.setGrupoValorId(grupo);
							pojo.setFechaCreacion(Calendar.getInstance().getTime());
							pojo.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
							pojo.setFechaModificacion(Calendar.getInstance().getTime());
							pojo.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());

							log.info("---> Nueva SUBFAMILIA " + pojo.getDescripcion());
							this.ifzConValores.save(pojo);
						} else {
							pojo.setValor(item.get("VALOR").toString());
							pojo.setFechaModificacion(Calendar.getInstance().getTime());
							pojo.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
							log.info("---> Actualizo ID: " + pojo.getId());
							this.ifzConValores.update(pojo);
						}
					} else {
						log.info(lista.size() + " opciones para el Item " + var.getKey() + ": " + item.get("DESCRIPCION").toString());
						for (ConValores cv : lista) 
							log.info("---> Opcion ID: " + cv.getId());
					}
				} else {
					pojo = new ConValores();
					pojo.setDescripcion(item.get("DESCRIPCION").toString());
					pojo.setAtributo1(item.get("OWNER").toString());
					pojo.setValor(item.get("VALOR").toString());
					pojo.setGrupoValorId(grupo);
					pojo.setFechaCreacion(Calendar.getInstance().getTime());
					pojo.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					pojo.setFechaModificacion(Calendar.getInstance().getTime());
					pojo.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());

					log.info("---> Nueva SUBFAMILIA " + pojo.getDescripcion());
					this.ifzConValores.save(pojo);
				}
			}

			nuevaCarga();
			this.operacionCompleta = true;
			this.tipoMensaje = 5;
			log.info("---> Carga TERMINADA");
		} catch (Exception e) {
			control("Ocurrio un problema al procesar el archivo Maestro", e);
		}
	}
	
	private LinkedHashMap<Integer, LinkedHashMap<String, Object>> leerCatalogo(String sheetName) throws Exception {
		LinkedHashMap<Integer, LinkedHashMap<String, Object>> listItemsMap = new LinkedHashMap<Integer, LinkedHashMap<String,Object>>();
		LinkedHashMap<Integer, LinkedHashMap<String, Object>> resultado = new LinkedHashMap<Integer, LinkedHashMap<String,Object>>();
		LinkedHashMap<String, Object> mapItem = new LinkedHashMap<String, Object>();
		Iterator<Row> rowIterator;
		Workbook workbook;
		InputStream file;
		Sheet sheet;
		Row row;
		Cell celda;
		int indexRow = 0;
		
		try {
			this.listHojasMap = new LinkedHashMap<String, LinkedHashMap<Integer, LinkedHashMap<String, Object>>>();
			file = new ByteArrayInputStream(this.fileSrc);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			
			// Recorremos todas las hojas del libro
			for (int indexSheet = 1; indexSheet <= workbook.getNumberOfSheets(); indexSheet++) {
				log.info("Leyendo Hoja " + indexSheet + " (" + sheet.getSheetName() + ")");
				sheet = workbook.getSheetAt(indexSheet - 1);
				rowIterator = sheet.iterator();
				
				// Si no especificamos hoja requerida, tomamos la primera hoja
				if ((sheetName == null || "".equals(sheetName)) && indexSheet == 1)
					sheetName = sheet.getSheetName();
				
				if (! sheetName.equals(sheet.getSheetName()))
					continue;
				
				// Recorremos todas las filas para mostrar el contenido de cada celda
				listItemsMap = new LinkedHashMap<Integer, LinkedHashMap<String,Object>>();
				for (indexRow = 1; rowIterator.hasNext(); indexRow++) {
					row = rowIterator.next();
					if (indexRow == 1) continue;

					mapItem = new LinkedHashMap<String, Object>();
					for (Entry<String, String> item : this.catalogoCellReference.entrySet()) {
						celda = row.getCell(CellReference.convertColStringToIndex(item.getValue()));
						if (celda != null) {
							switch (celda.getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									mapItem.put(item.getKey(), getStringValue(celda.getNumericCellValue()));
									break;
								case Cell.CELL_TYPE_STRING:
									if (celda.getStringCellValue() == null || "".equals(celda.getStringCellValue().trim())) continue;
									mapItem.put(item.getKey(), celda.getStringCellValue().trim());
									break;
								case Cell.CELL_TYPE_FORMULA:
									mapItem.put(item.getKey(), getStringValue(celda));
									break;
								default: 
									log.info("Tipo Desconocido Hoja " + indexSheet + " (" + sheet.getSheetName() + ") fila " + indexRow);
									break;
							}
						}
					}

					mapItem.put("VALOR", getRango(mapItem.get("INICIO").toString(), mapItem.get("FIN").toString()));
					listItemsMap.put(indexRow, mapItem);
				} // fin for rows
				
				if (sheetName.equals(sheet.getSheetName()))
					resultado.putAll(listItemsMap);
				this.listHojasMap.put(sheet.getSheetName(), listItemsMap);
				log.info("Hoja " + indexSheet + " (" + sheet.getSheetName() + ") leida");
			} // fin sheets
			
			log.info("--- -- --> Items en " + sheetName + " :: " + listItemsMap.toString());
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.leerCatalogo(). No se pudo leer el archivo especificado", e);
			control("ERROR INTERNO - No se pudo leer el archivo especificado");
		}
		
		return resultado;
	}
	
	private String getStringValue(double value) {
		NumberFormat nf = DecimalFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		return nf.format(value).replace(",", "");
	}
	
	private String getStringValue(Cell celda) {
		DataFormatter defaultFormat = new DataFormatter();
		FormulaEvaluator fEval = null;
		String valStr = "";
		
		if (celda == null) 
			return valStr;
		
		if (Cell.CELL_TYPE_BLANK == celda.getCellType() || Cell.CELL_TYPE_ERROR == celda.getCellType()) 
			return valStr;
		
		try { 
			fEval = celda.getRow().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			valStr = defaultFormat.formatCellValue(celda, fEval);
			valStr = (valStr == null || "".equals(valStr.trim())) ? "" : valStr;
		} catch (Exception e) { 
			valStr = ""; 
		}
		
		return valStr;
	}
	
	private String getRango(String inicio, String fin) {
		inicio = inicio.substring(5);
		fin = fin.substring(5);
		
		return inicio + "-" + fin;
	}
	
	// -------------------------------------------------------------------------------------
	// PRODUCTOS CADUCOS
	// -------------------------------------------------------------------------------------
	
	public void productosCaducos() {
		HashMap<String,String> params = new HashMap<String, String>();
		List<Producto> lista = new ArrayList<Producto>();
		
		try {
			control();
			this.pagProdsCaducos = 1;
			this.editandoCaduco = false;
			this.listProductosCaducos = new ArrayList<Producto>();
			
			// Cargamos familias
			cargarCboFamilias();
			if (! this.getUserAdmin()) {
				this.caducoTipoMaestro = TipoMaestro.Inventario.ordinal();
				if (this.perfilCodificarEgresosOperacion)
					this.caducoTipoMaestro = TipoMaestro.Administrativo.ordinal();
			}
			
			/*params.put("clave-1", "IZSYS");
			params.put("clave-2", "IZSAN");
			params.put("clave-3", "IZADM");
			params.put("clave-3", "IZADM");*/
			params.put("clave", "IZ________");
			params.put("oculto", "0");
			
			//lista = this.ifzProductos.findLikeProperty("clave", "IZ", null, this.caducoTipoMaestro, 0);
			lista = this.ifzProductos.findLikeProperties(params, this.caducoTipoMaestro, 0, "clave");
			if (lista == null || lista.isEmpty()) {
				log.info("la busqueda no regreso resultados");
				control(2, "Busqueda sin resultados");
				return;
			}

			for (Producto var : lista) {
				if (var.getEstatus() != 0) continue;
				this.listProductosCaducos.add(var);
			}
			
			if (this.listProductosCaducos.isEmpty()) {
				log.info("la busqueda no regreso resultados");
				control(2, "Busqueda sin resultados");
				return;
			}
			
			log.info(this.listProductosCaducos.size() + " resultados encontrados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Productos Caducos", e);
		}
	}

	private void cargarCboFamilias() {
		this.listaCboFamilias = new ArrayList<SelectItem>();
		for (ConValores cv : this.listaFamilias) {
			if (cv.getValor().equals(cv.getDescripcion()))
				this.listaCboFamilias.add(new SelectItem(cv.getId(), cv.getDescripcion()));
			else
				this.listaCboFamilias.add(new SelectItem(cv.getId(), cv.getDescripcion() + " (" + cv.getValor() + ")"));
		}
	}

	// -------------------------------------------------------------------
	// CODIFICACION
	// -------------------------------------------------------------------
	
	private void cargarNivel0() {
		try {
			this.listCodeMaestrosItems = new ArrayList<SelectItem>();
			this.listCodeMaestros = this.ifzConValores.findAll(this.pojoGpoNivel0);
			if (this.listCodeMaestros != null && ! this.listCodeMaestros.isEmpty()) {
				this.listCodeMaestrosItems.add(new SelectItem(0L, "Todos"));
				
				Collections.sort(this.listCodeMaestros, new Comparator<ConValores>() {
				    	@Override
				        public int compare(ConValores o1, ConValores o2) {
				    		return o1.getValor().compareTo(o2.getValor());
				        }
				});
				
				for (ConValores var : this.listCodeMaestros) {
					if (! this.perfilEgresosOperacion && "1".equals(var.getValor()))
						this.idMaestro = var.getId();
					this.listCodeMaestrosItems.add(new SelectItem(Integer.parseInt(var.getValor()), var.getDescripcion()));
				}
			}
		} catch (Exception e) {
			log.error("ERROR en Inventarios.ProductoAction.cargarNivel0. No se pudo cargar el Nivel Cero (Tipos de Maestros)", e);
		}
	}
	
	public void initGeneradorListas() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<ConGrupoValores> pojoGrupo = null;
		
		if (this.productoCaduco) 
			return;
		
		// Inicializamos
		control();
		if (this.listaCodeEspecialidades == null)
			this.listaCodeEspecialidades = new ArrayList<ConValores>();
		if (this.listaCodeFamilias == null)
			this.listaCodeFamilias = new ArrayList<ConValores>();
		if (this.listaCodeSubfamilias == null)
			this.listaCodeSubfamilias = new ArrayList<ConValores>();
		
		if (this.listaCodeEspecialidadesItems == null)
			this.listaCodeEspecialidadesItems = new ArrayList<SelectItem>();
		if (this.listaCodeFamiliasItems == null)
			this.listaCodeFamiliasItems = new ArrayList<SelectItem>();
		if (this.listaCodeSubfamiliasItems == null)
			this.listaCodeSubfamiliasItems = new ArrayList<SelectItem>();
		
		if (this.idCodeEspecialidad == -1) {
			if (this.listaCodeEspecialidades != null) 
				this.listaCodeEspecialidades.clear();
			
			if (this.listaCodeEspecialidadesItems != null) 
				this.listaCodeEspecialidadesItems.clear();
			
			if (this.listaCodeFamiliasItems != null) 
				this.listaCodeFamiliasItems.clear();
			
			if (this.listaCodeSubfamiliasItems != null) 
				this.listaCodeSubfamiliasItems.clear();

			this.idCodeEspecialidad = 0;
			this.idCodeEspecialidadPrev = 0;
			this.idCodeFamilia = 0;
			this.idCodeFamiliaPrev = 0;
			this.idCodeSubfamilia = 0;
		}
				
		// ESPECIALIDADES
		if (this.listaCodeEspecialidades.isEmpty()) {
			log.error("CODIFICADOR ... Obteniendo especialidades");
			pojoGrupo = this.ifzGpoVal.findLikeClaveNombre("SYS_PRODUCTO_ESPECIALIDADES", 0);
			if (pojoGrupo != null && ! pojoGrupo.isEmpty()) {
				try {
					params.clear();
					params.put("grupoValorId.id", String.valueOf(pojoGrupo.get(0).getId()));
					params.put("atributo1", String.valueOf(this.idMaestro));
					this.listaCodeEspecialidades = this.ifzConValores.findByProperties(params, 0);
					if (this.listaCodeEspecialidades == null || this.listaCodeEspecialidades.isEmpty()) {
						log.info("Catalogo de ESPECIALIDADES vacio");
						control("El catalogo de ESPECIALIDADES esta vacio");
						return;
					}
					
					Collections.sort(this.listaCodeEspecialidades, new Comparator<ConValores>() {
					    	@Override
					        public int compare(ConValores o1, ConValores o2) {
					    		return o1.getDescripcion().compareTo(o2.getDescripcion());
					        }
					});
					
					for (ConValores var : this.listaCodeEspecialidades) 
						this.listaCodeEspecialidadesItems.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
				} catch (Exception e) {
					log.error("ERROR en Inventarios.ProductoAction.initGeneradorListas al intentar recuperar el catalogo de ESPECIALIDADES", e);
					control("ERROR INTERNO - No se pudo recuperar el catalogo de ESPECIALIDADES");
					return;
				}
			}

			log.error("CODIFICADOR ... " + this.listaCodeEspecialidades.size() + " Especialidades obtenidas");
			return;
		}

		if (this.idCodeEspecialidad <= 0) {
			if (! this.mantenerCodigo)
				this.pojoProducto.setClave(generaClave());
			this.pojoProducto.setOrigenCodigo("");
			return;
		}

		// FAMILIAS	
		if (this.idCodeEspecialidad != this.idCodeEspecialidadPrev) {
			log.error("CODIFICADOR ... Obteniendo Familias");
			if (! this.mantenerCodigo)
				this.pojoProducto.setClave("");
			this.pojoProducto.setOrigenCodigo("");
			this.idCodeEspecialidadPrev = this.idCodeEspecialidad;
			this.listaCodeFamiliasItems.clear();
			this.listaCodeSubfamiliasItems.clear();	
			pojoGrupo = this.ifzGpoVal.findLikeClaveNombre("SYS_FAMILIA_PRODUCTO", 0);
			if (pojoGrupo != null && ! pojoGrupo.isEmpty()) {
				try {
					params.clear();
					params.put("grupoValorId.id", String.valueOf(pojoGrupo.get(0).getId()));
					params.put("atributo1", String.valueOf(this.idCodeEspecialidad));
					this.listaCodeFamilias = this.ifzConValores.findByProperties(params, 0);
					if (this.listaCodeFamilias == null || this.listaCodeFamilias.isEmpty()) {
						log.info("Catalogo de FAMILIAS vacio");
						control("El catalogo de FAMILIAS esta vacio");
						return;
					}
					
					Collections.sort(this.listaCodeFamilias, new Comparator<ConValores>() {
					    	@Override
					        public int compare(ConValores o1, ConValores o2) {
					    		return o1.getDescripcion().compareTo(o2.getDescripcion());
					        }
					});
					
					for (ConValores var : this.listaCodeFamilias) 
						this.listaCodeFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
					
				} catch (Exception e) {
					log.error("ERROR en Inventarios.ProductoAction.initGeneradorListas al intentar recuperar el catalogo de FAMILIAS", e);
					control("ERROR INTERNO - No se pudo recuperar el catalogo de FAMILIAS");
					return;
				}
			}

			log.error("CODIFICADOR ... " + this.listaCodeFamilias.size() + " Familias obtenidas");
			return;
		}

		// SUBFAMILIAS
		if (this.idCodeFamilia != this.idCodeFamiliaPrev) {
			log.error("CODIFICADOR ... Obteniendo SubFamilias");
			if (! this.mantenerCodigo)
				this.pojoProducto.setClave("");
			this.pojoProducto.setOrigenCodigo("");
			this.idCodeFamiliaPrev = this.idCodeFamilia;
			this.listaCodeSubfamiliasItems.clear();

			if (this.valMaestro == TipoMaestro.Administrativo) {
				log.error("CODIFICADOR ... Ignoro SubFamilias (ADMINISTRATIVO)");
				this.generaCodigoProducto();
				return;
			}
			
			pojoGrupo = this.ifzGpoVal.findLikeClaveNombre("SYS_PRODUCTO_SUBFAMILIA", 0);
			if (pojoGrupo != null && ! pojoGrupo.isEmpty()) {
				try {
					params.clear();
					params.put("grupoValorId.id", String.valueOf(pojoGrupo.get(0).getId()));
					params.put("atributo1", String.valueOf(this.idCodeFamilia));
					this.listaCodeSubfamilias = this.ifzConValores.findByProperties(params, 0);
					if (this.listaCodeSubfamilias == null || this.listaCodeSubfamilias.isEmpty()) {
						log.info("Catalogo de SUBFAMILIAS vacio");
						control("El catalogo de SUBFAMILIAS esta vacio");
						return;
					}
					
					Collections.sort(this.listaCodeSubfamilias, new Comparator<ConValores>() {
					    	@Override
					        public int compare(ConValores o1, ConValores o2) {
					    		return o1.getDescripcion().compareTo(o2.getDescripcion());
					        }
					});
					
					for (ConValores var : this.listaCodeSubfamilias) {
						if (var.getValor().equals(var.getDescripcion()))
							this.listaCodeSubfamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion()));
						else
							this.listaCodeSubfamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
					}
					
				} catch (Exception e) {
					log.error("ERROR en Inventarios.ProductoAction.initGeneradorListas al intentar recuperar el catalogo de SUBFAMILIAS", e);
					control("ERROR INTERNO - No se pudo recuperar el catalogo de SUBFAMILIAS");
					return;
				}
			}

			log.error("CODIFICADOR ... " + this.listaCodeSubfamilias.size() + " SubFamilias obtenidas");
		}
	}
	
	public void generaCodigoProducto() {
		String codigo = "";
		String origenCodigo = "";
		String valor = "";
		int maxDigitos = 5;
		int consecutivo = 0;
		
		try {
			control();
			codigo = "";
			origenCodigo = "";
			if (this.pojoProducto == null) {
				control("Ocurrio un problema al intentar generar el codigo del producto. Producto no recuperado");
				this.pojoProducto = new Producto();
				return;
			}
			
			if (! this.productoCaduco && this.idCodeEspecialidad <= 0)
				return;
			
			if (this.productoCaduco) {
				log.info("Codificando producto caduco ... ");
				cargarFamiliasItems();
				codigo = generaClave();
				origenCodigo = "";
				log.info("Codificando producto caduco ... terminado :) " + codigo);
			} else {
				log.info("Codificando producto ... Obteniendo especialdad");
				valor = getValorFromList(this.listaCodeEspecialidades, this.idCodeEspecialidad, false);
				codigo += (valor != null && ! "".equals(valor)) ? valor : "";
				if (valor == null || "".equals(valor)) {
					log.info("Codificando producto caduco ... No tengo especialidad :(");
					return;
				}

				log.info("Codificando producto ... Obteniendo familia");
				valor = getValorFromList(this.listaCodeFamilias, this.idCodeFamilia, false);
				codigo += (valor != null && ! "".equals(valor)) ? valor : "";
				if (valor == null || "".equals(valor)) {
					log.info("Codificando producto caduco ... No tengo familia :(");
					return;
				}
				
				if (this.valMaestro == TipoMaestro.Administrativo) {
					// Codificamos solo con la familia
					log.info("Codificando producto ... Obteniendo consecutivo (ADMINISTRATIVO)");
					consecutivo = getConsecutivo(codigo);
					this.idCodeSubfamilia = 0L;
				} else {
					log.info("Codificando producto ... Obteniendo subfamilia");
					origenCodigo = getDescripcionFromList(this.listaCodeSubfamilias, this.idCodeSubfamilia);
					valor = getValorFromList(this.listaCodeSubfamilias, this.idCodeSubfamilia, true);
					if (valor != null && ! "".equals(valor) && (isNumber(valor) || isRange(valor))) {
						log.info("Codificando producto ... Obteniendo consecutivo (rango)");
						codigo += (! isNumber(valor) && ! isRange(valor) && ! codigo.contains(valor)) ? valor : "";
						consecutivo = getConsecutivo(codigo, valor);
					} else {
						log.info("Codificando producto ... Obteniendo consecutivo (numerico)");
						codigo += (valor != null && ! "".equals(valor)) ? valor : "";
						consecutivo = getConsecutivo(codigo);
					}
				}

				log.info("Codificando producto ... Formateando codigo");
				codigo = codigo + String.format("%1$0" + maxDigitos + "d", consecutivo);
				origenCodigo = this.idCodeEspecialidad + "-" + this.idCodeFamilia + "-" + this.idCodeSubfamilia;
				log.info("Codificando producto ... codigo obtenido :) " + codigo + " :: " + origenCodigo);
			}
		} catch (Exception e) {
			log.error("Error en Invetarios.ProductoAction.generaCodigoProducto: No pude codificar, genero codigo caduco", e);
			if ("LIMIT_RANGE".equals(e.getMessage()))
				control("LIMITE DE RANGO alcanzado para la subfamilia '" + origenCodigo + "' (" + valor + ").", e);
			else if ("UMBALANCE_RANGE".equals(e.getMessage()))
				control("Inconsistencia de Rango para la subfamilia '" + origenCodigo + "' (" + valor + ").", e);
			else
				control("Ocurrio un problema al intentar generar el codigo del producto. Genero codigo temporal", e);
			codigo = this.pojoProducto.getClave();
			if (codigo == null || "".equals(codigo) || ! codigo.startsWith("IZ"))
				codigo = generaClave();
			origenCodigo = "";
			log.info("Producto con codigo caduco ... terminado :( " + codigo, e);
		}

		// Asignamos valores
		log.info("Codificando producto ... Asignando codigo");
		if (! this.mantenerCodigo)
			this.pojoProducto.setClave(codigo);
		this.pojoProducto.setOrigenCodigo(origenCodigo);
		this.pojoProducto.setEspecialidad(this.idCodeEspecialidad);
		this.pojoProducto.setFamilia(this.idCodeFamilia);
		this.pojoProducto.setSubfamilia(this.idCodeSubfamilia);
		log.info("Codificando producto ... Terminado");
	}
	
	public void generaCodigoCaduco() {
		if (! this.productoCaduco) 
			initGeneradorListas();
		generaCodigoProducto();
	}
	
	public void conservaCodigo() {
		if (this.mantenerCodigo) {
			this.codigoOriginal = this.pojoProducto.getClave();
			log.info("Respaldo codigo de producto " + this.pojoProducto.getId() + ": " + this.codigoOriginal);
		} else {
			log.info("Borro el codigo respaldado de producto " + this.pojoProducto.getId() + ": " + this.codigoOriginal);
			this.codigoOriginal = "";
		}
	}
	
	private void cargarFamiliasItems() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<ConValores> listFamAux = null;
		
		try {
			this.listaCodeEspecialidades = new ArrayList<ConValores>();
			this.listaFamilias = new ArrayList<ConValores>();
			this.listaCodeFamiliasItems = new ArrayList<SelectItem>();

			log.info("Cargando Especialidades del Maestro " + this.valMaestro.toString());
			params.put("grupoValorId.id", this.pojoGpoEspecialidades.getId());
			params.put("atributo1", this.idMaestro);
			this.listaCodeEspecialidades = this.ifzConValores.findByProperties(params, 0);
			if (this.listaCodeEspecialidades != null && ! this.listaCodeEspecialidades.isEmpty()) {
				for (ConValores var : this.listaCodeEspecialidades) {
					params.put("grupoValorId.id", this.pojoGpoFamilias.getId());
					params.put("atributo1", String.valueOf(var.getId()));
					listFamAux = this.ifzConValores.findByProperties(params, 0);
					if (listFamAux == null || listFamAux.isEmpty())
						continue;
					this.listaFamilias.addAll(listFamAux);
				}
			}
			
			Collections.sort(this.listaFamilias, new Comparator<ConValores>() {
			    	@Override
			        public int compare(ConValores o1, ConValores o2) {
			    		return o1.getDescripcion().compareTo(o2.getDescripcion());
			        }
			});
			
			// Generamos la lista auxiliar de familias
			if (this.listaFamilias != null && ! this.listaFamilias.isEmpty()) {
				log.info("Generando lista de items (familias)");
				for (ConValores var : this.listaFamilias) {
					if (var.getValor().trim().equals(var.getDescripcion().trim()))
						this.listaCodeFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion()));
					else
						this.listaCodeFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
				}
			}
		} catch (Exception e) {
			log.error("Error al cargar las familias", e);
		} finally {
			if (this.listaCodeFamiliasItems != null && ! this.listaCodeFamiliasItems.isEmpty()) 
				log.info(this.listaCodeFamiliasItems.size() + " items (familias) generados");
		}
	}
	
	private void obtenerOrigenCodigo() {
		String[] splitted = null;
		String origenCodigo = "";

		this.idCodeEspecialidad = -1;
		this.idCodeFamilia = 0;
		this.idCodeSubfamilia = 0;
		
		origenCodigo = this.pojoProducto.getOrigenCodigo();
		if (origenCodigo != null && ! "".equals(origenCodigo) && origenCodigo.contains("-")) {
			splitted = origenCodigo.split("\\-");
			if (splitted.length == 3) {
				this.idCodeEspecialidad = Long.parseLong(splitted[0]);
				initGeneradorListas();
				
				this.idCodeFamilia = Long.parseLong(splitted[1]);
				initGeneradorListas();
				
				this.idCodeSubfamilia = Long.parseLong(splitted[2]);
				initGeneradorListas();
				return;
			} 
		} 
		
		this.idCodeEspecialidad = getEspecialidadFromFamilia(this.pojoProducto.getFamilia());
		if (this.idCodeEspecialidad > 0) {
			initGeneradorListas();
			this.idCodeFamilia = this.pojoProducto.getFamilia();
			initGeneradorListas();
		} 

		initGeneradorListas();
	}

	private String generaClave() {
		String resultado = "";
		int consecutivo = 0;
		
		resultado = "IZ" + this.userPrefix;
		consecutivo = getConsecutivo(resultado);
		resultado = resultado + String.format("%1$05d", consecutivo);
		
		return resultado;
	}

	private int getConsecutivo(String prefix) {
		List<Producto> lista = null;
		BigDecimal aux = BigDecimal.ZERO;
		int maximo = 1;

		log.info("Codificando producto ... Consultando por prefijo " + prefix);
		lista = this.ifzProductos.findLikeProperty("clave", prefix, 0);
		if (lista != null && ! lista.isEmpty()) {
			for (Producto var : lista) {
				aux = new BigDecimal(var.getClave().substring(prefix.length()).replaceAll("[^0-9]", ""));
				if (maximo < aux.intValue())
					maximo = aux.intValue();
			}
			
			maximo += 1;
		}
		
		return maximo;
	}
	
	private int getConsecutivo(String prefix, String pRango) throws Exception {
		List<Producto> lista = null;
		String[] splitted = null;
		int consecutivo = 0;
		int inicioRango = 0;
		int limiteRango = 0;
		
		if (pRango != null && ! "".equals(pRango.trim())) {
			log.info("Codificando producto ... Obteniendo limites de rango");
			if (pRango.contains("-")) {
				splitted = pRango.split("\\-");
				inicioRango = getNumber(splitted[0]);
				limiteRango = getNumber(splitted[1]);
				if (limiteRango <= inicioRango)
					throw new Exception("UMBALANCE_RANGE");
					
			} else {
				inicioRango = getNumber(pRango);
				limiteRango = 0;
			}
			log.info("Codificando producto ... Limites de rango obtenidos (" + inicioRango + "-" + limiteRango + ")");
		}
		
		// Buscamos producto por clave con el patron 'prefix' que corresponde a ESPECIALIDAD Y FAMILIA, y 'rango' que corresponde a la subfamilia
		log.info("Codificando producto ... Consultando por prefijo " + prefix + "% ... (RANGO)");
		lista = this.ifzProductos.findByClaveRango(prefix, inicioRango, limiteRango);
		if (lista != null && ! lista.isEmpty()) {
			log.info(lista.size() + " productos encontrados. Buscando ultimo numero usado ... ");
			for (Producto var : lista) {
				consecutivo = (new BigDecimal(var.getClave().substring(prefix.length()))).intValue();
				if (consecutivo > limiteRango)
					break;
			}

			log.info("Codificando producto ... Inicio de rango            : " + inicioRango);
			log.info("Codificando producto ... Consecutivo actual         : " + consecutivo);
			log.info("Codificando producto ... Consecutivo para codificar : " + (consecutivo + 1));
			log.info("Codificando producto ... Limite de rango            : " + limiteRango);
			
			consecutivo += 1;
			if (limiteRango > 0 && consecutivo > limiteRango)
				throw new Exception("LIMIT_RANGE");
		} else {
			consecutivo = inicioRango;
			log.info("Codificando producto ... Inicio de rango            : " + inicioRango);
			log.info("Codificando producto ... Consecutivo para codificar : " + consecutivo);
			log.info("Codificando producto ... Limite de rango            : " + limiteRango);
		}

		log.info("Codificando producto ... Consecutivo obtenido " + consecutivo);
		return consecutivo;
	}
	
	private String getValorFromList(List<ConValores> lista, long id, boolean validaContraDescripcion) {
		String resultado = "";
		String descripcion = "";
		
		try {
			if (lista == null || lista.isEmpty()) {
				log.info("La lista indicada es nula o vacia.");
				return "";
			}
			
			if (id <= 0){
				log.info("No indico el ID para poder obtener el valor de la lista.");
				return "";
			}
			
			for (ConValores var : lista) {
				if (var.getId() == id) {
					resultado = var.getValor();
					descripcion = var.getDescripcion();
					if (validaContraDescripcion) {
						if (resultado.trim().equals(descripcion.trim()))
							resultado = "";
					}
					break;
				}
			}
		} catch(Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el valor de la lista especificada", e);
			resultado = "";
		}
		
		return resultado;
	}
	
	private String getDescripcionFromList(List<ConValores> lista, long id) {
		String resultado = "";
		
		try {
			if (lista == null || lista.isEmpty()) {
				log.info("La lista indicada es nula o vacia.");
				return "";
			}
			
			if (id <= 0){
				log.info("No indico el ID para poder obtener el valor de la lista.");
				return "";
			}
			
			for (ConValores var : lista) {
				if (var.getId() == id) {
					resultado = var.getDescripcion();
					break;
				}
			}
		} catch(Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el valor de la lista especificada", e);
			resultado = "";
		}
		
		return resultado;
	}

	private boolean isNumber(String value) {
		BigDecimal test = BigDecimal.ZERO;
		
		if (value == null || "".equals(value))
			return false;
		
		try {
			test = new BigDecimal(value.trim());
			return (test != null && test.intValue() > 0);
		} catch (Exception e) {
			log.info("El valor " + value + " no es un numero");
			return false;
		}
	}
	
	private boolean isRange(String value) {
		BigDecimal test1 = BigDecimal.ZERO;
		BigDecimal test2 = BigDecimal.ZERO;
		String[] splitted = null;
		
		if (value == null || "".equals(value))
			return false;
		
		if (! value.contains("-")) 
			return false;
		
		try {
			splitted = value.split("\\-");
			test1 = new BigDecimal(splitted[0].trim());
			test2 = new BigDecimal(splitted[1].trim());
			return ((test1 != null && test1.intValue() >= 0) && (test2 != null && test2.intValue() > 0) && (test1.intValue() < test2.intValue()));
		} catch (Exception e) {
			log.info("El valor " + value + " no es un rango");
			return false;
		}
	}
	
	private int getNumber(String value) {
		BigDecimal test = BigDecimal.ZERO;
		
		if (value == null || "".equals(value))
			return 0;
		
		try {
			test = new BigDecimal(value.trim());
			return ((test != null && test.intValue() > 0) ? test.intValue() : 0);
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.getNumber(). No se pudo convertir a numero", e);
			return 0;
		}
	}
	
	// -------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------

	public boolean getUserAdmin() {
		return ("ADMINISTRADOR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()) ? true : false);
	}
	
	public void setUserAdmin(boolean value) {}
	
	public int getCaducoTipoMaestro() {
		return this.caducoTipoMaestro;
	}
	
	public void setCaducoTipoMaestro(int caducoTipoMaestro) {
		this.caducoTipoMaestro = caducoTipoMaestro;
	}
	
	public boolean getPerfilCodificador() {
		return (this.perfilCodificarEgresosOperacion || this.perfilCodificarInsumos);
	}
	
	public void setPerfilCodificador(boolean value) {}
	
	public boolean getCostoPesos() {
		if (this.pojoProducto != null && this.pojoProducto.getIdMoneda() > 0L) {
			for (Moneda var : this.listMonedas) {
				if (this.pojoProducto.getIdMoneda() != var.getId()) continue;
				if (! "MXN".equals(var.getAbreviacion())) return true;
			}
		}
		
		return false;
	}
	
	public void setCostoPesos(boolean value) {}
	
	public String getTituloProducto() {
		if (this.pojoProducto != null && this.pojoProducto.getId() != null && this.pojoProducto.getId() > 0L)
			return "Producto " + this.pojoProducto.getId() + ((this.valMaestro == TipoMaestro.Administrativo) ? " - ADMINISTRATIVO" : "");
		return "Nuevo Producto";
	}
	
	public void setTituloProducto(String value) {}
	
	public String getEncabezadoProducto() {
		if (this.pojoProducto != null && this.pojoProducto.getId() != null && this.pojoProducto.getId() > 0L)
			return this.pojoProducto.getClave() + " - " + this.pojoProducto.getDescripcion();
		return "Indique datos para Nuevo Producto" + ((this.valMaestro == TipoMaestro.Administrativo) ? " ADMINISTRATIVO" : "");
	}
	
	public void setEncabezadoProducto(String value) {}

	public double getPrecioCompra() {
		return this.pojoProducto.getPrecioCompra();
	}
	
	public void setPrecioCompra(double precioCompra) {
		this.pojoProducto.setPrecioCompra(precioCompra);
	}

	public double getPrecioUnitario() {
		return this.pojoProducto.getPrecioUnitario();
	}
	
	public void setPrecioUnitario(double precioUnitario) {
		this.pojoProducto.setPrecioUnitario(precioUnitario);
	}

	public double getPrecioVenta() {
		return this.pojoProducto.getPrecioVenta();
	}
	
	public void setPrecioVenta(double precioVenta) {
		this.pojoProducto.setPrecioVenta(precioVenta);
	}

	public boolean isPermiteExcedente(){
		return this.pojoProducto.getPermiteExcedente() == 1 ? true : false;
	}
	
	public void setPermiteExcedente(boolean permiteExcedente){
		this.pojoProducto.setPermiteExcedente( permiteExcedente==true ? 1 : 0 );
	}
	
	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(int tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getValorBusquedaProducto() {
		return valorBusquedaProducto;
	}

	public void setValorBusquedaProducto(String valorBusquedaProducto) {
		this.valorBusquedaProducto = valorBusquedaProducto;
	}

	public List<SelectItem> getListaCampoBusquedaProductos() {
		return listaCampoBusquedaProductos;
	}

	public void setListaCampoBusquedaProductos(List<SelectItem> tipoBusquedaProductos) {
		this.listaCampoBusquedaProductos = tipoBusquedaProductos;
	}
	
	public String getCampoBusquedaProducto() {
		return campoBusquedaProducto;
	}

	public void setCampoBusquedaProducto(String campoBusquedaProducto) {
		this.campoBusquedaProducto = campoBusquedaProducto;
	}

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	public Producto getPojoProducto() {
		return pojoProducto;
	}

	public void setPojoProducto(Producto pojoProducto) {
		this.pojoProducto = copiaProducto(pojoProducto);
	}
	
	public Producto getPojoProductoCaduco() {
		return pojoProducto;
	}

	public void setPojoProductoCaduco(Producto pojoProducto) {
		this.pojoProducto = copiaProducto(pojoProducto);
		this.editandoCaduco = true;

		for (ConValores var : this.listCodeMaestros) {
			if (! var.getValor().equals(String.valueOf(pojoProducto.getTipo()))) 
				continue;
			this.idMaestro = var.getId();
			this.valMaestro = TipoMaestro.Inventario;
			if ("2".equals(var.getValor()))
				this.valMaestro = TipoMaestro.Administrativo;
			break;
		}
	}

	public List<Producto> getListaProductosGrid() {
		return listProductos;
	}

	public void setListaProductosGrid(List<Producto> listaProductosGrid) {
		this.listProductos = listaProductosGrid;
	}

	public List<SelectItem> getListaCboUnidadesMedida() {
		return listaCboUnidadesMedida;
	}

	public void setListaCboUnidadesMedida(List<SelectItem> listaCboUnidadesMedida) {
		this.listaCboUnidadesMedida = listaCboUnidadesMedida;
	}

	public List<SelectItem> getListaCboFamilias() {
		return listaCboFamilias;
	}

	public void setListaCboFamilias(List<SelectItem> listaCboFamilias) {
		this.listaCboFamilias = listaCboFamilias;
	}

	public ConGrupoValores getPojoGpoUnidadesMedida() {
		return pojoGpoUnidadesMedida;
	}

	public void setPojoGpoUnidadesMedida(ConGrupoValores pojoGpoUnidadesMedida) {
		this.pojoGpoUnidadesMedida = pojoGpoUnidadesMedida;
	}

	public List<ConValores> getListaUnidadesMedida() {
		return listaUnidadesMedida;
	}

	public void setListaUnidadesMedida(List<ConValores> listaUnidadesMedida) {
		this.listaUnidadesMedida = listaUnidadesMedida;
	}

	public ConGrupoValores getPojoGpoFamilias() {
		return pojoGpoFamilias;
	}

	public void setPojoGpoFamilias(ConGrupoValores pojoGpoFamilias) {
		this.pojoGpoFamilias = pojoGpoFamilias;
	}

	public List<ConValores> getListaFamilias() {
		return listaFamilias;
	}

	public void setListaFamilias(List<ConValores> listaFamilias) {
		this.listaFamilias = listaFamilias;
	}

	public long getIdUnidadMedida() {
		return idUnidadMedida;
	}

	public void setIdUnidadMedida(long idUnidadMedida) {
		this.idUnidadMedida = idUnidadMedida;
		this.pojoProducto.setUnidadMedida(idUnidadMedida);
	}
	
	public long getIdMoneda() {
		return idMoneda;
	}
	
	public void setIdMoneda(long idMoneda) {
		this.idMoneda = idMoneda;
		this.pojoProducto.setIdMoneda(idMoneda);
	}
	
	public long getFamiliaId() {
		return idFamilia;
	}
	
	public void setFamiliaId(long idFamilia) {
		this.idFamilia = idFamilia;
	}

	public List<ConValores> getListaCodeEspecialidades() {
		return listaCodeEspecialidades;
	}
	
	public void setListaCodeEspecialidades(List<ConValores> listaCodeEspecialidades) {
		this.listaCodeEspecialidades = listaCodeEspecialidades;
	}
	
	public List<ConValores> getListaCodeFamilias() {
		return listaCodeFamilias;
	}
	
	public void setListaCodeFamilias(List<ConValores> listaCodeFamilias) {
		this.listaCodeFamilias = listaCodeFamilias;
	}
	
	public List<ConValores> getListaCodeSubfamilias() {
		return listaCodeSubfamilias;
	}
	
	public void setListaCodeSubfamilias(List<ConValores> listaCodeSubfamilias) {
		this.listaCodeSubfamilias = listaCodeSubfamilias;
	}
	
	public List<SelectItem> getListaCodeEspecialidadesItems() {
		return listaCodeEspecialidadesItems;
	}
	
	public void setListaCodeEspecialidadesItems(List<SelectItem> listaCodeEspecialidadesItems) {
		this.listaCodeEspecialidadesItems = listaCodeEspecialidadesItems;
	}
	
	public List<SelectItem> getListaCodeFamiliasItems() {
		return listaCodeFamiliasItems;
	}
	
	public void setListaCodeFamiliasItems(List<SelectItem> listaCodeFamiliasItems) {
		this.listaCodeFamiliasItems = listaCodeFamiliasItems;
	}
	
	public List<SelectItem> getListaCodeSubfamiliasItems() {
		return listaCodeSubfamiliasItems;
	}
	
	public void setListaCodeSubfamiliasItems(List<SelectItem> listaCodeSubfamiliasItems) {
		this.listaCodeSubfamiliasItems = listaCodeSubfamiliasItems;
	}

	public long getIdCodeEspecialidad() {
		return idCodeEspecialidad;
	}
	
	public void setIdCodeEspecialidad(long idCodeEspecialidad) {
		this.idCodeEspecialidad = idCodeEspecialidad;
	}
	
	public long getIdCodeFamilia() {
		return idCodeFamilia;
	}
	
	public void setIdCodeFamilia(long idCodeFamilia) {
		this.idCodeFamilia = idCodeFamilia;
	}
	
	public long getIdCodeSubfamilia() {
		return idCodeSubfamilia;
	}
	
	public void setIdCodeSubfamilia(long idCodeSubfamilia) {
		this.idCodeSubfamilia = idCodeSubfamilia;
	}
	
	public List<Producto> getListProductosCaducos() {
		return listProductosCaducos;
	}
	
	public void setListProductosCaducos(List<Producto> listProductosCaducos) {
		this.listProductosCaducos = listProductosCaducos;
	}
	
	public int getPagProdsCaducos() {
		return pagProdsCaducos;
	}
	
	public void setPagProdsCaducos(int pagProdsCaducos) {
		this.pagProdsCaducos = pagProdsCaducos;
	}
	
	public List<SelectItem> getListMonedasItems() {
		return listMonedasItems;
	}

	public void setListMonedasItems(List<SelectItem> listMonedasItems) {
		this.listMonedasItems = listMonedasItems;
	}

	public boolean isProductoCaduco() {
		return productoCaduco;
	}

	public void setProductoCaduco(boolean productoCaduco) {
		this.productoCaduco = productoCaduco;
	}

	public int getTipoMaestro() {
		return this.valMaestro.ordinal();
	}

	public void setTipoMaestro(int tipoMaestro) {
		this.valMaestro = TipoMaestro.values()[tipoMaestro];
	}

	public int getTipoMaestroExportar() {
		return this.tipoMaestroExportar.ordinal();
	}

	public void setTipoMaestroExportar(int tipoMaestro) {
		this.tipoMaestroExportar = TipoMaestro.values()[tipoMaestro];
	}

	public List<SelectItem> getListNivel0Items() {
		return listCodeMaestrosItems;
	}

	public void setListNivel0Items(List<SelectItem> listNivel0Items) {
		this.listCodeMaestrosItems = listNivel0Items;
	}

	public boolean isPerfilEgresosOperacion() {
		return perfilEgresosOperacion;
	}
	
	public void setPerfilEgresosOperacion(boolean perfilEgresosOperacion) {
		this.perfilEgresosOperacion = perfilEgresosOperacion;
	}
	
	public int getTipoMaestroBusquedaProducto() {
		return this.tipoMaestroBusqueda.ordinal();
	}
	
	public void setTipoMaestroBusquedaProducto(int tipoMaestro) {
		this.tipoMaestroBusqueda = TipoMaestro.values()[tipoMaestro];
	}
	
	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public boolean isPerfilCodificarEgresosOperacion() {
		return perfilCodificarEgresosOperacion;
	}
	
	public void setPerfilCodificarEgresosOperacion(boolean perfilCodificarEgresosOperacion) {
		this.perfilCodificarEgresosOperacion = perfilCodificarEgresosOperacion;
	}
	
	public boolean isPerfilCodificarInsumos() {
		return perfilCodificarInsumos;
	}
	
	public void setPerfilCodificarInsumos(boolean perfilCodificarInsumos) {
		this.perfilCodificarInsumos = perfilCodificarInsumos;
	}

	public boolean isEditandoCaduco() {
		return editandoCaduco;
	}
	
	public void setEditandoCaduco(boolean editandoCaduco) {
		this.editandoCaduco = editandoCaduco;
	}
	
	public boolean getRequiereSubfamilia() {
		return (this.valMaestro == TipoMaestro.Inventario);
	}
	
	public void setRequiereSubfamilia(boolean value) {}

	public List<SelectItem> getListTiposInsumos() {
		return listTiposInsumos;
	}

	public void setListTiposInsumos(List<SelectItem> listTiposInsumos) {
		this.listTiposInsumos = listTiposInsumos;
	}
	
	public boolean getRequiereTipoInsumo() {
		return (this.valMaestro == TipoMaestro.Inventario && getUserAdmin() && this.isDebug);
	}
	
	public void setRequiereTipoInsumo(boolean value) {}
	
	public boolean getPermiteCodificacionManual() {
		return (this.pojoProducto != null && this.pojoProducto.getTipoInsumo() > TipoInsumo.Material.ordinal());
	}
	
	public void setPermiteCodificacionManual(boolean value) {}

	public boolean getBuscarEnAdministrativo() {
		return buscarEnAdministrativo;
	}
	
	public void setBuscarEnAdministrativo(boolean buscarEnAdministrativo) {
		this.buscarEnAdministrativo = buscarEnAdministrativo;
	}

	public boolean isMantenerCodigo() {
		return mantenerCodigo;
	}

	public void setMantenerCodigo(boolean mantenerCodigo) {
		this.mantenerCodigo = mantenerCodigo;
	}
	
	public boolean getOperacionCompleta() {
		return operacionCompleta;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCompleta = operacionCompleta;
	}

	public boolean isOperacionCancelada() {
		return operacionCancelada;
	}

	public void setOperacionCancelada(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}

	// ----------------------------------------------------------------------
	// PERMISOS
	// ----------------------------------------------------------------------
	
	public boolean getLectura() { return this.permisos.getConsultar(); }

	public void setLectura(boolean value) {}
	
	public boolean getEscritura() { return this.permisos.getEditar(); }

	public void setEscritura(boolean value) {}
	
	public boolean getBorrar() { return this.permisos.getBorrar(); }

	public void setBorrar(boolean value) {}

	// ----------------------------------------------------------------------
	
	public boolean isPermisoConsultar() { return this.permisos.getConsultar(); }

	public void setPermisoConsultar(boolean value) {}
    
	public boolean isPermisoAgregar() { return this.permisos.getEditar(); }

	public void setPermisoAgregar(boolean value) {}

	public boolean isPermisoEditar() { return this.permisos.getEditar(); }

	public void setPermisoEditar(boolean value) {}

	public boolean isPermisoBorrar() { return this.permisos.getBorrar(); }

	public void setPermisoBorrar(boolean value) {}

	public boolean isPermisoImprimir() { return this.permisos.getConsultar(); }

	public void setPermisoImprimir(boolean value) {}

	public boolean isPermisoEscritura() { return this.permisos.getEditar(); }

	public void setPermisoEscritura(boolean value) { }
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-12 | Javier Tirado 	| Modifico el metodo analizarExcel: Envio lote de productos de 1000 en 1000 en caso de ser necesario
 *  									  con la lectura del archivo maestro
 */
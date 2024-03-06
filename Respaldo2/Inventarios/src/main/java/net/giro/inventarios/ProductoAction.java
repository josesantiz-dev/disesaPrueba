package net.giro.inventarios;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
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
import java.util.Map;
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
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.logica.MonedasValoresRem;

import org.apache.commons.beanutils.BeanUtils;
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
	private HttpSession httpSession;
	private InitialContext ctx;
	private ReportesRem	ifzReportes;
    private long usuarioId;
    private String usuario;
    private String userPrefix;
    private int LIMITE_RESULTADOS;
	private int numPagina;
	private int tipoMensaje;
	private boolean operacionCompleta;
	private int tipoBusqueda;
	private String mensaje;
	private String mensajeDetalles;
	private String valorBusquedaProducto;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private ProductoRem ifzProductos;
	private boolean buscarEnAdministrativo;
	private double limiteInferiorActivos;
	// Busqueda Productos
	private List<SelectItem> listaCampoBusquedaProductos;
	private String campoBusquedaProducto;
	private Producto pojoProducto;
	private Producto pojoProductoEliminar;
	private List<Producto> listaProductosGrid;
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
	// Productos Caducos
	private List<Producto> listProductosCaducos;
	private int pagProdsCaducos;
	private boolean productoCaduco;
	private boolean editandoCaduco;
	private int caducoTipoMaestro;
	// Monedas
	private MonedaDAO ifzMonedas;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private long idMonedaBase;
	// Monedas Valores
	private MonedasValoresRem ifzMonValores;
	// Catalogo
	private LinkedHashMap<String, String> catalogoCellReference = new LinkedHashMap<String, String>();
	// PERFILES
	private boolean perfilEgresosOperacion;
	private boolean perfilCodificarEgresosOperacion;
	private boolean perfilCodificarInsumos;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	
 	public ProductoAction() {
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
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
	
			values = new HashMap<String, String>();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{maestro}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			keys = this.entornoProperties.keySet();
	        for(String key : keys)
	        	values.put(key, this.entornoProperties.getString(key));
	        this.maestroCellReference = ordenaHashMap(values);
	        
	        values = new HashMap<String, String>();
	        ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{catalogo}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
	        for(String key : this.entornoProperties.keySet())
	        	values.put(key, this.entornoProperties.getString(key));
	        this.catalogoCellReference = ordenaHashMap(values);
	
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			this.operacionCompleta = true;
			this.numPagina = 1;
			
			this.LIMITE_RESULTADOS = 0;
			if (this.entornoProperties.containsKey("LIMITE_RESULTADOS"))
				this.LIMITE_RESULTADOS = Integer.parseInt(this.entornoProperties.getString("LIMITE_RESULTADOS"));
			
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
			
			this.ctx = new InitialContext();
			this.ifzProductos = (ProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzMonValores = (MonedasValoresRem) this.ctx.lookup("ejb:/Logica_TYG//MonedasValoresFac!net.giro.tyg.logica.MonedasValoresRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMonValores.setInfoSesion(this.loginManager.getInfoSesion());
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

			this.listaProductosGrid = new ArrayList<Producto>();
			this.pojoProducto = new Producto();

			// Prefijo por default para dar cabida a codigos caducos
			this.userPrefix = "SYS";

			this.listaUnidadesMedida = this.ifzConValores.buscaValorGrupo("descripcion", "", this.pojoGpoUnidadesMedida);
			this.listaFamilias = this.ifzConValores.buscaValorGrupo("descripcion", "", this.pojoGpoFamilias);
			
			cargarTipoBusquedaProductos();
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
		TipoMaestro busquedaMaestro = TipoMaestro.Inventario;
		
		try {
			control();
			if ("".equals(this.valorBusquedaProducto)) {
				log.info("debe indicar el criterio de busqueda");
				control(4);
				return;
			}
			
			if (this.buscarEnAdministrativo)
				busquedaMaestro = TipoMaestro.Administrativo;
			
			if (this.listaProductosGrid == null)
				this.listaProductosGrid = new ArrayList<Producto>();
			this.listaProductosGrid.clear();
			
			log.info("Buscando productos... " + this.campoBusquedaProducto + ": " + this.valorBusquedaProducto);
			if ("".equals(this.campoBusquedaProducto))
				this.campoBusquedaProducto = listaCampoBusquedaProductos.get(0).getValue().toString();

			this.listaProductosGrid = this.ifzProductos.findLikeProperty(this.campoBusquedaProducto, this.valorBusquedaProducto, 0L, busquedaMaestro.ordinal(), this.LIMITE_RESULTADOS);
			if (this.listaProductosGrid.isEmpty()) {
				log.info("la busqueda no regreso resultados");
				control(2);
				return;
			}
			control(true);
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.buscar", e);
			control(false);
		} finally {
			if (this.listaProductosGrid == null)
				this.listaProductosGrid = new ArrayList<Producto>();
			log.info("Se encontraron " + this.listaProductosGrid.size() + " productos");
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
				control(2);
				return;
			}
			
			log.info("Se encontraron " + lista.size() + " productos para codificar");
			for(Producto var : lista) {
				log.info("Codificando producto " + var.getId() + " - " + lista.indexOf(var) + "/" + lista.size());
				codigo = "ACOPCH" + String.format("%1$05d", getConsecutivo("ACOPCH", "00001-99999"));
				var.setClave(codigo);
				var.setOrigenCodigo(this.idCodeEspecialidad + "-" + this.idCodeFamilia + "-" + this.idCodeSubfamilia);
				var.setEspecialidad(this.idCodeEspecialidad);
				var.setFamilia(this.idCodeFamilia);
				var.setSubfamilia(this.idCodeSubfamilia);
				this.ifzProductos.update(var);
				log.info("Producto " + var.getId() + " codificado: " + codigo);
			}
			
			log.info("Proceso terminado");
			control(true);
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.buscar", e);
			control(false);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.idCodeEspecialidad = -1;
			this.mantenerCodigo = false;
			this.codigoOriginal = "";
			this.codigoPrevio = "";
			initGeneradorListas();
			this.pojoProducto = null;
			this.pojoProducto = new Producto();
			this.pojoProducto.setClave("");
			this.pojoProducto.setOrigenCodigo("");
			cargarMonedas();
			setIdMoneda(0);
			setIdUnidadMedida(0);
			control(true);
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.nuevo", e);
			control(false);
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
			if (this.pojoProducto == null) {
				control(false);
				return;
			}

			cargarMonedas();
			this.mantenerCodigo = false;
			this.idCodeEspecialidad = -1;
			this.productoCaduco = false;
			this.codigoOriginal = "";
			this.codigoPrevio = this.pojoProducto.getClave();
			
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
			control(true);
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.editar", e);
			control("Error al editar");
		}
	}

	public void guardar() {
		try {
			control();
			if(! validaGuardarProducto()) 
				return;
			
			if (this.codigoPrevio != null && ! "".equals(this.codigoPrevio))
				this.pojoProducto.setClavePrevia(this.codigoPrevio);
			
			if (this.mantenerCodigo && ! this.codigoOriginal.equals(this.pojoProducto.getClave()))
				this.pojoProducto.setClave(this.codigoOriginal);
			this.codigoOriginal = "";
			this.mantenerCodigo = false;

			if (this.pojoProducto.getIdMoneda() <= 0L) {
				this.pojoProducto.setIdMoneda(this.idMonedaBase);
				this.pojoProducto.setTipoCambio(1.0);
				this.pojoProducto.setPrecioCompraPesos(0);
			}
			
			if (this.idMonedaBase == this.pojoProducto.getIdMoneda() && this.pojoProducto.getTipoCambio() <= 0){
				this.pojoProducto.setTipoCambio(1.0);
				this.pojoProducto.setPrecioCompraPesos(0);
			}
			
			// Obtenemos descripcion y abreviatura de moneda elegida
			for (Moneda var : this.listMonedas) {
				if (var.getId() != this.pojoProducto.getIdMoneda())
					continue;
				this.pojoProducto.setDescMoneda(var.getNombre());
				this.pojoProducto.setDescMonedaAbreviatura(var.getAbreviacion());
				break;
			}
			
			// Asignamos descripcion de unidad de medida, especialidad, familia y subfamilia
			this.pojoProducto.setDescUnidadMedida(getDescripcionFromList(this.listaUnidadesMedida, this.pojoProducto.getUnidadMedida()));
			this.pojoProducto.setDescEspecialidad(getDescripcionFromList(this.listaCodeEspecialidades, this.idCodeEspecialidad));
			this.pojoProducto.setDescFamilia(getDescripcionFromList(this.listaCodeFamilias, this.idCodeFamilia));
			this.pojoProducto.setDescSubfamilia(getDescripcionFromList(this.listaCodeSubfamilias, this.idCodeSubfamilia));
			this.pojoProducto.setTipo(this.valMaestro.ordinal());
			if (this.pojoProducto.getTipoInsumo() <= 0)
				this.pojoProducto.setTipoInsumo(TipoInsumo.Material.ordinal());
			if (this.idMonedaBase != this.pojoProducto.getIdMoneda())
				asignaTipoCambio();
			this.pojoProducto.setModificadoPor(this.usuarioId);
			this.pojoProducto.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoProducto.getId() == null) {
				this.pojoProducto.setCreadoPor(this.usuarioId);
				this.pojoProducto.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Añado a la BD y a la lista
				this.pojoProducto.setId(this.ifzProductos.save(this.pojoProducto));
				
				if (this.valMaestro.ordinal() == this.pojoProducto.getTipo())
					this.listaProductosGrid.add(this.pojoProducto);
			} else {
				// Actualizo en la BD
				this.ifzProductos.update(this.pojoProducto);
				
				for (Producto var : this.listaProductosGrid) {
					if (this.valMaestro.ordinal() == var.getTipo())
						break;
					
					if (! var.getId().equals(this.pojoProducto.getId()))
						continue;
					var = this.pojoProducto;
					break;
				}
			}
			
			// Guardamos o actualizamos copia en Gastos si corresponde
			if (this.valMaestro == TipoMaestro.Administrativo)
				copiaActualizaEnGastos(this.pojoProducto);
			
			if (this.editandoCaduco)
				this.productosCaducos();
			control(true);
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.guardar", e);
			control("Error al guardar");
		} 
	}
	
	public void eliminar(){
		try {
			control();
			this.pojoProductoEliminar.setModificadoPor(this.usuarioId);
			this.pojoProductoEliminar.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoProductoEliminar.setEstatus(1);
			
			// Actualizamos en la BD
			this.ifzProductos.update( this.pojoProductoEliminar );
			this.nuevo();
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.eliminar", e);
			control("Error al eliminar");
		}
	}

	private void cargarTipoBusquedaProductos() {
		if (this.listaCampoBusquedaProductos == null)
			this.listaCampoBusquedaProductos = new ArrayList<SelectItem>();
		
		this.listaCampoBusquedaProductos.add(new SelectItem("descripcion", "Nombre"));
		this.listaCampoBusquedaProductos.add(new SelectItem("clave", "Clave"));
		this.listaCampoBusquedaProductos.add(new SelectItem("id", "ID"));
	}
	
	private void cargarCboUnidadesMedida() {
		listaCboUnidadesMedida = null;
		listaCboUnidadesMedida = new ArrayList<>();
		
		for (ConValores cv : this.listaUnidadesMedida) {
			if (cv.getValor().equals(cv.getDescripcion()))
				this.listaCboUnidadesMedida.add(new SelectItem(cv.getId(), cv.getDescripcion()));
			else
				this.listaCboUnidadesMedida.add(new SelectItem(cv.getId(), cv.getDescripcion() + " (" + cv.getValor() + ")"));
		}
	}
	
	private void cargarTiposInsumos() {
		try {
			if (this.listTiposInsumos == null)
				this.listTiposInsumos = new ArrayList<SelectItem>();
			this.listTiposInsumos.clear();
			
			for (TipoInsumo tipo : TipoInsumo.values()) {
				if (tipo.ordinal() > 0)
					this.listTiposInsumos.add(new SelectItem(tipo.ordinal(), tipo.toString()));
			}
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.cargarTiposInsumos()", e);
		}
	}
	
	public boolean validaGuardarProducto() {
		int intento = 0;
		int maxIntentosClave = 3;
		boolean claveRepetida = false;
		this.tipoMensaje = 0;

		log.info("Validando producto");
		if (this.pojoProducto.getClave().trim().equals("")) {
			log.info("---> Debe indicar la clave del producto");
			this.tipoMensaje = -10;
			return false;
		}
		
		if (this.pojoProducto.getDescripcion().trim().equals("")) {
			log.info("---> Debe indicar la descripcion del producto");
			this.tipoMensaje = -11;
			return false;
		}
		
		if (this.pojoProducto.getUnidadMedida() <= 0)  {
			log.info("---> Producto sin unidad de medida");
			this.tipoMensaje = -19;
			return false;
		}
		
		this.pojoProducto.setFamilia(this.idCodeFamilia);
		if (this.pojoProducto.getFamilia() <= 0)  {
			log.info("---> Producto sin familia");
			this.tipoMensaje = -20;
			return false;
		}
		
		if (this.pojoProducto.getExistencia() < 0) {
			log.info("---> La existensia debe ser mayor o igual a cero");
			this.tipoMensaje = -16;
			return false;
		}

		if (this.pojoProducto.getMinimo() < 0) {
			log.info("---> El Minimo debe ser mayor o igual a cero");
			this.tipoMensaje = -12;
			return false;
		}

		if (this.pojoProducto.getMaximo() < 0) {
			log.info("---> El Maximo debe ser mayor o igual a cero");
			this.tipoMensaje = -13;
			return false;
		}
		
		if (this.pojoProducto.getMinimo() > this.pojoProducto.getMaximo()) {
			log.info("---> El Minimo debe ser menor al Maximo");
			this.tipoMensaje = -17;
			return false;
		}

		if (this.pojoProducto.getIdMoneda() <= 0) {
			log.info("---> Debe indicar la moneda");
			this.tipoMensaje = -21;
			return false;
		}
		
		if (this.pojoProducto.getPrecioCompra() == 0) {
			this.tipoMensaje = -14;
			log.info("---> Debe indicar el precio de compra");
			return false;
		}

		if (this.pojoProducto.getPrecioVenta() == 0) {
			log.info("---> Debe indicar el precio de venta");
			this.tipoMensaje = -15;
			return false;
		}
		
		// Validar la clave: Generamos la clave las veces que sea necesario
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
			log.info("---> Producto clave repetida");
			this.tipoMensaje = -18;
			return false;
		}

		log.info("Producto validado");
		return true;
	}

	private Producto copiaProducto(Producto target) {
		Producto resultado = new Producto();
		
		try {
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
			if (this.listMonedasItems == null)
				this.listMonedasItems = new ArrayList<SelectItem>();
			this.listMonedasItems.clear();
			
			this.listMonedas = this.ifzMonedas.findAll();
			if (this.listMonedas != null && ! this.listMonedas.isEmpty()) {
				for (Moneda mon : this.listMonedas) {
					if ("MXN".equals(mon.getAbreviacion()))
						this.idMonedaBase = mon.getId();
					this.listMonedasItems.add(new SelectItem(mon.getId(), mon.getNombre() + " (" + mon.getAbreviacion() + ")"));
				}
			}
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.cargarMonedas", e);
		}
	}

	private long getMonedaBase(String monedaAbreviatura) {
		long idMoneda = 0L;
		
		if (this.listMonedas == null || this.listMonedas.isEmpty()) 
			cargarMonedas();
		
		for (Moneda var : this.listMonedas) {
			if (!monedaAbreviatura.equals(var.getAbreviacion()))
				continue;
			idMoneda = var.getId();
			break;
		}
		
		return idMoneda;
	}
	
	public void asignaTipoCambio() {
		Moneda monedaOrigen = null;
		Moneda monedaDestino = null;
		Double tipoCambio = 1.0;
		
		if (this.pojoProducto == null)
			return;
		
		if (this.pojoProducto.getIdMoneda() > 0L) {
			// Origen: MXN
			for (Moneda var : this.listMonedas) {
				if (! "MXN".equals(var.getAbreviacion())) 
					continue;
				monedaOrigen = var;
				break;
			}
			
			// Destino: Elegido
			for (Moneda var : this.listMonedas) {
				if (var.getId() != this.pojoProducto.getIdMoneda()) continue;
				monedaDestino = var;
				break;
			}
			
			if (monedaOrigen.getId() == monedaDestino.getId()) {
				monedaOrigen = null;
				monedaDestino = null;
			}
			
			tipoCambio = recuperaTipoCambioActual(monedaOrigen, monedaDestino);
			if (tipoCambio == null || tipoCambio <= 0.0) {
				log.warn("No se pudo recuperar el tipo de cambio para " + monedaDestino + " a " + monedaOrigen);
				tipoCambio = 1.0;
			}
			
			this.pojoProducto.setTipoCambio(tipoCambio); 
			this.pojoProducto.setPrecioCompraPesos(this.pojoProducto.getPrecioCompra() * tipoCambio);
		}
	}
	
	private Double recuperaTipoCambioActual(Moneda monedaOrigen, Moneda monedaDestino) {
		MonedasValores valor = null;
		Double tipoCambio = 1.0;

		try {
			if (monedaOrigen != null && monedaDestino != null) {
				valor = this.ifzMonValores.findActual(monedaOrigen, monedaDestino);
				tipoCambio = valor.getValor().doubleValue();
			}
		} catch (Exception e) {
			log.error("No se pudo obtener el Tipo de Cambio actual para la conversion de " + monedaOrigen + " a " + monedaDestino, e);
			tipoCambio = 1.0;
		}
		
		return tipoCambio;
	}
	
	private long getMonedaBaseId() {
			
		try {
			if (this.listMonedas == null)
				cargarMonedas();
			
			for (Moneda var : this.listMonedas) {
				if (! "MXN".equals(var.getAbreviacion())) continue;
				return var.getId();
			}
		} catch (Exception e) {
			log.error("No se pudo obtener el ID de la moneda base (MXN)", e);
		}
		
		return 0L;
	}
	
	private LinkedHashMap<String, String> ordenaHashMap(HashMap<String, String> values) {
		LinkedHashMap<String, String> resultado = new LinkedHashMap<String, String>();
		List<String> arr = new ArrayList<String>();
		
		for (Entry<String, String> entry: values.entrySet()) {
			arr.add(entry.getValue());
		}

		Collections.sort(arr);
		
		for(String val : arr) {
			for (Entry<String, String> entry: values.entrySet()) {
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
		this.operacionCompleta = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(boolean value) {
		if (! value)
			control(value, 1, "ERROR");
		else
			control(value, 0, null);
	}
	
	private void control(int tipoMensaje) {
		control(false, tipoMensaje, "ERROR");
	}
	
	private void control(String value) { 
		if (value == null || "".equals(value))
			control(false, 1, "ERROR");
		else
			control(false, -1, value); 
	}
	
	private void control(boolean operacionCompleta, int tipo, String mensaje) {
		control(operacionCompleta, tipo, mensaje, null);
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";
		
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		this.operacionCompleta = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null || "".equals(mensaje.trim())) ? codigo : mensaje.replace("\n", "<br/>") + ("<br/>" + codigo);
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		if (! this.operacionCompleta)
			log.error("\nPRODUCTO :: " + this.usuario + " :: " + codigo + "\n" + mensaje + "\n", throwable);
	}

	private void copiaActualizaEnGastos(Producto pojoProducto) {
		List<ConValores> lista = null;
		ConValores pojoGasto = null;
	
		try {
			if (pojoProducto.getPrecioCompra() < this.limiteInferiorActivos)
				return;
			
			lista = this.ifzConValores.buscaValorGrupo("valor", String.valueOf(pojoProducto.getId()), this.pojoGpoGastos); 
			if (lista != null && ! lista.isEmpty()) {
				pojoGasto = lista.get(0);
				
				if (this.idEspecialidadActivo == pojoProducto.getEspecialidad()) { 
					log.info("  --- Actualizando copia en Gasto");
					pojoGasto.setDescripcion(pojoProducto.getDescripcion());
					pojoGasto.setModificadoPor(this.usuarioId);
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
				pojoGasto.setCreadoPor(this.usuarioId);
				pojoGasto.setFechaCreacion(Calendar.getInstance().getTime());
				pojoGasto.setModificadoPor(this.usuarioId);
				pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());
				// Guardo en la BD
				pojoGasto.setId(this.ifzConValores.save(pojoGasto));
				log.info("  --- Gasto generado");
			}
		} catch (Exception e) {
			log.error("  --- Ocurrio un problema al hacer una copia del producto en Gastos: " + pojoProducto.getId(), e);
			control(true);
		}
	}
	
	// -------------------------------------------------------------------
	// Importar MAESTRO
	// -------------------------------------------------------------------
	
	public void nuevaCarga() {
		this.fileSrc = null;
		this.fileExtension = "";
		this.refSize = 0;
		if (this.productosProcesados != null)
			this.productosProcesados.clear();
		this.productosProcesados = null;
	}
		
	public void cargarMaestro(FileUploadEvent event) throws Exception {
		try {
			control();
			this.fileSrc = event.getUploadedFile().getData();
			this.fileExtension = Files.getFileExtension(event.getUploadedFile().getName());
			if (this.productosProcesados != null)
				this.productosProcesados.clear();
			this.productosProcesados = null;
			control(true);
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.uploadListener", e);
			control(false, 1, "Error al cargar");
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
			this.operacionCompleta = false;
			this.tipoMensaje = 0;
			this.mensaje = "";
					
			if (this.fileSrc == null)
				return;
			
			if (this.refSize == 0)
				this.refSize = this.fileSrc.length;
			
			if (this.fileExtension == null || "".equals(this.fileExtension))
				this.fileExtension = "xls";

			if (this.listaProductosGrid == null)
				this.listaProductosGrid = new ArrayList<Producto>();
			this.listaProductosGrid.clear();
			
			if (this.isDebug) {
				importarCatalogo();
				return;
			}
			
			// Leemos archivo
			// -----------------------------------------------------------------------------------
			respuesta = this.ifzProductos.analizarArchivo(this.fileSrc, this.fileExtension, this.pojoGpoUnidadesMedida, this.pojoGpoFamilias, this.maestroCellReference);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				this.tipoMensaje = -1;
				this.mensaje = respuesta.getErrores().getDescError();
				log.error(respuesta.getErrores().getCodigoError() + " - " + this.mensaje);
				return;
			}
			
			// Verificamos si hay productos sin procesar
			mapNoProcesados = (HashMap<Integer, String>) respuesta.getBody().getValor("sinprocesar");
			if (! mapNoProcesados.isEmpty()) {
				this.tipoMensaje = 4;
				this.mensaje = "ERROR";
				log.error(respuesta.getErrores().getCodigoError() + " - " + this.mensaje);
				return;
			}
			
			// Recuperamos la lista de productos
			this.productosProcesados = (List<Producto>) respuesta.getBody().getValor("productos");
			if (this.productosProcesados == null || this.productosProcesados.isEmpty()) {
				this.tipoMensaje = 4;
				this.mensaje = "ERROR";
				log.error(respuesta.getErrores().getCodigoError() + " - " + this.mensaje);
				return;
			}
			
			if (idMoneda <= 0)
				idMoneda = getMonedaBase("MXN");
			
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
				this.listaProductosGrid.addAll(temp);
			}
			log.info(this.productosProcesados.size() + " productos guardados");

			nuevaCarga();
			this.operacionCompleta = true;
			this.tipoMensaje = 5;
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.analizarExcel", e);
			control(false, 1, "Error en Inventarios.ProductoAction.analizarExcel al procesar el archivo Maestro");
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
			control(true);
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.nuevoExportar", e);
			control(false, 1, "Error en Inventarios.ProductoAction al inicializar la exportacion de Maestro");
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
			params.put("usuario", 		  this.usuario);

			log.info("Exportar Maestro. Generando reporte ... ");
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO: " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control("Ocurrio un problema al intentar Exportar el Maestro");
				return;
			} 

			log.info("Exportar Maestro. Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			if (this.tipoMaestroExportar == TipoMaestro.Inventario)
				nombreDocumento = "MAESTRO-INSUMOS-" + formatter.format(Calendar.getInstance().getTime());
			else if (this.tipoMaestroExportar == TipoMaestro.Administrativo)
				nombreDocumento = "MAESTRO-ADMINISTRATIVO-" + formatter.format(Calendar.getInstance().getTime());
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido del Maestro");
				control("Ocurrio un problema al intentar imprimir el Maestro");
				return;
			}

			log.info("Exportar Maestro. Lanzando reporte ... ");
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			log.info("Maestro exportado. Proceso finalizado");
			control(true);
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.exportarMaestro.", e);
			control(false, 1, "Ocurrio un problema al intentar exportar el Maestro");
		} finally {
			this.idFamilia = 0;
		}
		/*HashMap<String, Object> params = new HashMap<String, Object>();
		Respuesta resultado = new Respuesta();
		SimpleDateFormat formatter = null;
		String maestroNombre = "";
		byte[] contenido = null;
		
		try {
			control();
			log.info("Exportando Maestro " + this.tipoMaestroExportar.toString());
			formatter = new SimpleDateFormat("yyyyMMdd");
			maestroNombre = "MAESTRO-INSUMOS-" + formatter.format(Calendar.getInstance().getTime());
			params.put("tipo", this.tipoMaestroExportar.ordinal());
			if (this.idFamilia > 0) 
				params.put("familia", this.idFamilia);
			params.put("estatus", 0);
			params.put("oculto", 0);

			log.info("Generando reporte ... ");
			resultado = this.ifzProductos.exportarMaestro(params, "tipoInsumo, clave", maestroNombre);
			if (resultado.getErrores().getCodigoError() != 0) {
				control(false, -1, resultado.getErrores().getDescError());
				log.error(this.resOperacion);
				return;
			}

			log.info("Obteniendo reporte ... ");
			maestroNombre += ".xls";
			contenido = (byte[]) resultado.getBody().getValor("contenidoReporte");
			if (contenido == null || contenido.length <= 0) {
				log.info("Maestro Vacio. Sin datos para los filtro aplicados." + params.toString());
				control(false, -1, "El filtro no genero resultados.");
				return;
			}

			log.info("Lanzando reporte ... ");
			this.httpSession.setAttribute("contenido", contenido);
			this.httpSession.setAttribute("nombreDocumento", maestroNombre);
			this.httpSession.setAttribute("formato", "xls");
			log.info("Maestro exportado. Proceso finalizado");
			control(true);
		} catch (RollbackException rbe) {
			log.error("Error en Inventarios.ProductoAction.exportarMaestro.", rbe);
			control(false, 1, "No se pudo exportar el Maestro. El Servidor cerro la conexion");
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.exportarMaestro.", e);
			control(false, 1, "Error al exportar Maestro");
		}*/
	}
	
	private void cargarMaestrosExportar() {
		try {
			if (this.listCodeMaestrosItems == null)
				this.listCodeMaestrosItems = new ArrayList<SelectItem>();
			this.listCodeMaestrosItems.clear();
			
			if (this.listCodeMaestros == null)
				this.listCodeMaestros = new ArrayList<ConValores>();
			this.listCodeMaestros.clear();
			
			if (this.listaFamilias == null)
				this.listaFamilias = new ArrayList<ConValores>();
			this.listaFamilias.clear();
			
			if (this.listaCboFamilias == null)
				this.listaCboFamilias = new ArrayList<SelectItem>();
			this.listaCboFamilias.clear();
			
			this.idFamilia = 0L;
			this.listCodeMaestros = this.ifzConValores.findAll(this.pojoGpoNivel0);
			if (this.listCodeMaestros != null && ! this.listCodeMaestros.isEmpty()) {
				Collections.sort(this.listCodeMaestros, new Comparator<ConValores>() {
				    	@Override
				        public int compare(ConValores o1, ConValores o2) {
				    		return o1.getValor().compareTo(o2.getValor());
				        }
				});
				
				for (ConValores var : this.listCodeMaestros)
					this.listCodeMaestrosItems.add(new SelectItem(Integer.parseInt(var.getValor()), var.getDescripcion()));
			}
		} catch (Exception e) {
			log.error("ERROR en Inventarios.ProductoAction.cargarMaestrosExportar. No se pudo cargar el Nivel Cero (Tipos de Maestros)", e);
		}
	}

	public void cargarFamiliasExportar() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<ConValores> listFamAux = null;
		List<ConValores> listAux = null;
		String valTipoMaestro = "";
		String idMaestro = "";
		
		try {
			if (this.listaFamilias == null)
				this.listaFamilias = new ArrayList<ConValores>();
			this.listaFamilias.clear();
			
			if (this.listaCboFamilias == null)
				this.listaCboFamilias = new ArrayList<SelectItem>();
			this.listaCboFamilias.clear();
			
			log.info("Cargando Maestros");
			valTipoMaestro = String.valueOf(this.tipoMaestroExportar.ordinal());
			listAux = this.ifzConValores.findAll(this.pojoGpoNivel0);
			for (ConValores var : listAux) {
				if (! var.getValor().equals(valTipoMaestro)) continue;
				idMaestro = String.valueOf(var.getId());
				break;
			}

			log.info("Cargando Especialidades del Maestro " + this.tipoMaestroExportar.toString());
			params.put("grupoValorId.id", this.pojoGpoEspecialidades.getId());
			params.put("atributo1", idMaestro);
			listAux = this.ifzConValores.findByProperties(params, 0);
			if (listAux != null && ! listAux.isEmpty()) {
				for (ConValores var : listAux) {
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
						this.listaCboFamilias.add(new SelectItem(var.getId(), var.getDescripcion()));
					else
						this.listaCboFamilias.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
				}
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
		/*String codigo = "";
		String origenCodigo = "";
		int consecutivo = 0;
		Producto prod = null;
		long monedaId = 0;*/
		LinkedHashMap<String,Object> item = null;
		ConGrupoValores grupo = null;
		long idGrupo = 0;
		
		try {
			control();
			if(this.fileSrc == null)
				return;
			
			if (refSize == 0)
				refSize = this.fileSrc.length;
			
			if(this.fileExtension == null || "".equals(this.fileExtension))
				this.fileExtension = "xls";

			if(this.listaProductosGrid == null)
				this.listaProductosGrid = new ArrayList<Producto>();
			this.listaProductosGrid.clear();
			
			//monedaId = getMonedaBaseId();
			
			// Leemos archivo
			// -----------------------------------------------------------------------------------
			mapItems = leerCatalogo("SUBFAMILIAS");
			if (mapItems == null || mapItems.isEmpty()) {
				return;
			} 
			
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
							pojo.setCreadoPor(this.usuarioId);
							pojo.setFechaModificacion(Calendar.getInstance().getTime());
							pojo.setModificadoPor(this.usuarioId);

							log.info("---> Nueva SUBFAMILIA " + pojo.getDescripcion());
							this.ifzConValores.save(pojo);
						} else {
							pojo.setValor(item.get("VALOR").toString());
							pojo.setFechaModificacion(Calendar.getInstance().getTime());
							pojo.setModificadoPor(this.usuarioId);
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
					pojo.setCreadoPor(this.usuarioId);
					pojo.setFechaModificacion(Calendar.getInstance().getTime());
					pojo.setModificadoPor(this.usuarioId);

					log.info("---> Nueva SUBFAMILIA " + pojo.getDescripcion());
					this.ifzConValores.save(pojo);
				}
			}
			
			/*for (Entry<Integer, LinkedHashMap<String, Object>> var : mapItems.entrySet()) {
				item = var.getValue();
				if (item.get("DESCRIPCION") == null || "".equals(item.get("DESCRIPCION").toString()))
					continue;
				
				codigo = item.get("ESPECIALIDAD").toString() 
					   + item.get("FAMILIA").toString()
					   + item.get("SUBFAMILIA").toString();
				origenCodigo = item.get("ESPECIALIDAD_ID").toString() 
						+ "-" + item.get("FAMILIA_ID").toString()
						+ "-" + item.get("SUBFAMILIA_ID").toString();
				
				consecutivo = getConsecutivo(codigo);
				codigo = codigo + String.format("%1$05d", consecutivo);
				
				prod = this.ifzProductos.findByClave("");
				if (prod == null) {
					prod = new Producto();
					prod.setClave(codigo);
					prod.setDescripcion(item.get("DESCRIPCION").toString());
					prod.setOrigenCodigo(origenCodigo);
					prod.setEspecialidad((long) item.get("ESPECIALIDAD_ID"));
					prod.setFamilia((long) item.get("FAMILIA_ID"));
					prod.setSubfamilia((long) item.get("SUBFAMILIA_ID"));
					prod.setUnidadMedida((long) item.get("UM_ID"));
					prod.setDescEspecialidad(item.get("ESPECIALIDAD").toString());
					prod.setDescFamilia(item.get("FAMILIA").toString());
					prod.setDescSubfamilia(item.get("SUBFAMILIA").toString());
					prod.setDescUnidadMedida(item.get("UM").toString());
					prod.setIdMoneda(monedaId);
					prod.setDescMoneda("PESOS");
					prod.setDescMonedaAbreviatura("MXN");
					prod.setTipo(TipoMaestro.Administrativo.ordinal());
					prod.setTipoInsumo(TipoInsumo.Material.ordinal());
					prod.setCreadoPor(this.usuarioId);
					prod.setFechaCreacion(Calendar.getInstance().getTime());
					prod.setModificadoPor(this.usuarioId);
					prod.setFechaModificacion(Calendar.getInstance().getTime());
					
					prod.setId(this.ifzProductos.save(prod));
					this.listaProductosGrid.add(prod);
				}
			}*/

			nuevaCarga();
			this.operacionCompleta = true;
			this.tipoMensaje = 5;
			log.info("---> Carga TERMINADA");
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.analizarExcel", e);
			control(false, 1, "Error en Inventarios.ProductoAction.importarCatalogo al procesar el archivo Maestro");
		}
	}
	
	private LinkedHashMap<Integer, LinkedHashMap<String, Object>> leerCatalogo(String sheetName) throws Exception {
		LinkedHashMap<Integer, LinkedHashMap<String, Object>> listItemsMap = new LinkedHashMap<Integer, LinkedHashMap<String,Object>>();
		LinkedHashMap<Integer, LinkedHashMap<String, Object>> resultado = new LinkedHashMap<Integer, LinkedHashMap<String,Object>>();
		LinkedHashMap<String, Object> mapItem = new LinkedHashMap<String, Object>();
		/*HashMap<String, Long> mapUniMedida = new HashMap<String, Long>();
		HashMap<String, Long> mapEspecialidades = new HashMap<String, Long>();
		HashMap<String, Long> mapFamilias = new HashMap<String, Long>();
		HashMap<String, Long> mapSubfamilias = new HashMap<String, Long>();*/
		Iterator<Row> rowIterator;
		Workbook workbook;
		InputStream file;
		Sheet sheet;
		Row row;
		Cell celda;
		int indexRow = 0;
		/*int baseCols = 1;
		long idAux = 0;*/
		
		try {
			this.listHojasMap = new LinkedHashMap<String, LinkedHashMap<Integer, LinkedHashMap<String, Object>>>();
			file = new ByteArrayInputStream(this.fileSrc);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			
			/*this.valMaestro = TipoMaestro.Inventario;
			if (this.valMaestro == TipoMaestro.Administrativo)
				nuevoMaestroAdministrativo();
			else
				nuevoMaestroInventarios();*/
			
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
					
					/*mapItem.put("tipo", this.valMaestro.ordinal());

					celda = row.getCell(this.getColumnIndex("CODIGO", baseCols));
					if (celda != null && celda.getStringCellValue() != null && ! "".equals(celda.getStringCellValue().trim())) {
						mapItem.put("CODIGO", celda.getStringCellValue().trim());
					} else {
						listItemsMap.put(indexRow, mapItem);
						continue;
					}

					celda = row.getCell(this.getColumnIndex("DESCRIPCION", baseCols + 1));
					if (celda != null && celda.getStringCellValue() != null && ! "".equals(celda.getStringCellValue().trim())) {
						mapItem.put("DESCRIPCION", celda.getStringCellValue().trim());
					} else {
						listItemsMap.put(indexRow, mapItem);
						continue;
					}

					celda = row.getCell(this.getColumnIndex("UM", baseCols + 2));
					if (celda != null && celda.getStringCellValue() != null && ! "".equals(celda.getStringCellValue().trim())) {
						mapItem.put("UM", celda.getStringCellValue().trim());
						idAux = comprobarEnListado(mapUniMedida, this.pojoGpoUnidadesMedida, null, celda.getStringCellValue().trim());
						if (idAux > 0L)
							mapItem.put("UM_ID", idAux);
					} else {
						listItemsMap.put(indexRow, mapItem);
						continue;
					}

					celda = row.getCell(this.getColumnIndex("ESPECIALIDAD", baseCols + 3));
					if (celda != null && celda.getStringCellValue() != null && ! "".equals(celda.getStringCellValue().trim())) {
						mapItem.put("ESPECIALIDAD", celda.getStringCellValue().trim());
						idAux = comprobarEnListado(mapEspecialidades, this.pojoGpoEspecialidades, String.valueOf(this.idMaestro), celda.getStringCellValue().trim());
						if (idAux > 0L)
							mapItem.put("ESPECIALIDAD_ID", idAux);
					} else {
						listItemsMap.put(indexRow, mapItem);
						continue;
					}

					celda = row.getCell(this.getColumnIndex("FAMILIA", baseCols + 4));
					if (celda != null && celda.getStringCellValue() != null && ! "".equals(celda.getStringCellValue().trim())) {
						mapItem.put("FAMILIA", celda.getStringCellValue().trim());
						idAux = comprobarEnListado(mapFamilias, this.pojoGpoFamilias, String.valueOf(mapItem.get("ESPECIALIDAD_ID")), celda.getStringCellValue().trim());
						if (idAux > 0L)
							mapItem.pPIGTAILS	10002802	06001-06500	ITFIB06001	ITFIB06500	10000051
ut("FAMILIA_ID", idAux);
					} else {
						listItemsMap.put(indexRow, mapItem);
						continue;
					}

					celda = row.getCell(this.getColumnIndex("SUBFAMILIA", baseCols + 5));
					if (celda != null && celda.getStringCellValue() != null && ! "".equals(celda.getStringCellValue().trim())) {
						mapItem.put("SUBFAMILIA", celda.getStringCellValue().trim());
						idAux = comprobarEnListado(mapSubfamilias, this.pojoGpoSubfamilias, String.valueOf(mapItem.get("FAMILIA_ID")), celda.getStringCellValue().trim());
						if (idAux > 0L)
							mapItem.put("SUBFAMILIA_ID", idAux); 
					} else {
						listItemsMap.put(indexRow, mapItem);
						continue;
					}

					listItemsMap.put(indexRow, mapItem);*/
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
			if (this.listProductosCaducos == null)
				this.listProductosCaducos = new ArrayList<Producto>();
			this.listProductosCaducos.clear();
			
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
				control(2);
				return;
			}

			for (Producto var : lista) {
				if (var.getEstatus() != 0) continue;
				this.listProductosCaducos.add(var);
			}
			
			if (this.listProductosCaducos.isEmpty()) {
				log.info("la busqueda no regreso resultados");
				control(2);
				return;
			}
			
			log.info(this.listProductosCaducos.size() + " resultados encontrados");
			control(true);
		} catch (Exception e) {
			log.error("Error en Inventarios.ProductoAction.productosCaducos", e);
			control(false);
		}
	}

	private void cargarCboFamilias() {
		if (this.listaCboFamilias == null)
			this.listaCboFamilias = new ArrayList<SelectItem>();
		this.listaCboFamilias.clear();
		
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
			if (this.listCodeMaestrosItems == null)
				this.listCodeMaestrosItems = new ArrayList<SelectItem>();
			this.listCodeMaestrosItems.clear();
			
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
			if (this.pojoProducto == null)
				this.pojoProducto = new Producto();
			
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
			control(true);
		} catch (Exception e) {
			log.error("Error en Invetarios.ProductoAction.generaCodigoProducto: No pude codificar, genero codigo caduco", e);
			if ("LIMIT_RANGE".equals(e.getMessage()))
				control("LIMITE DE RANGO alcanzado para la subfamilia '" + origenCodigo + "' (" + valor + ").");
			else if ("UMBALANCE_RANGE".equals(e.getMessage()))
				control("Inconsistencia de Rango para la subfamilia '" + origenCodigo + "' (" + valor + ").");
			else
				control("Ocurrio un problema al intentar generar el codigo del producto. Genero codigo temporal");
			codigo = this.pojoProducto.getClave();
			if (codigo == null || "".equals(codigo) || ! codigo.startsWith("IZ"))
				codigo = generaClave();
			origenCodigo = "";
			log.info("Producto con codigo caduco ... terminado :( " + codigo);
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
			if (this.listaCodeEspecialidades == null)
				this.listaCodeEspecialidades = new ArrayList<ConValores>();
			this.listaCodeEspecialidades.clear();
			
			if (this.listaFamilias == null)
				this.listaFamilias = new ArrayList<ConValores>();
			this.listaFamilias.clear();
			
			if (this.listaCodeFamiliasItems == null)
				this.listaCodeFamiliasItems = new ArrayList<SelectItem>();
			this.listaCodeFamiliasItems.clear();

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
		
		/*
		if (this.listaCodeFamiliasItems == null)
			this.listaCodeFamiliasItems = new ArrayList<SelectItem>();
		this.listaCodeFamiliasItems.clear();
			
		for (ConValores cv : this.listaFamilias) {
			if (this.idMaestro <= 0)
				break;
			
			if (cv.getAtributo1().equals(String.valueOf(this.idMaestro)))
				continue;
			
			if (cv.getValor().equals(cv.getDescripcion()))
				this.listaCodeFamiliasItems.add(new SelectItem(cv.getId(), cv.getDescripcion()));
			else
				this.listaCodeFamiliasItems.add(new SelectItem(cv.getId(), cv.getDescripcion() + " (" + cv.getValor() + ")"));
		}*/
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
		
		if (! this.usuario.equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) {
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			this.userPrefix = this.entornoProperties.getString(this.usuario);
			if (this.userPrefix == null || "".equals(this.userPrefix))
				this.userPrefix = "ZXZ";
		}
		
		resultado = "IZ" + this.userPrefix;
		consecutivo = getConsecutivo(resultado);
		resultado = resultado + String.format("%1$05d", consecutivo);
		
		return resultado;
	}

	private int getConsecutivo(String prefix) {
		BigDecimal aux = BigDecimal.ZERO;
		int maximo = 1;

		log.info("Codificando producto ... Consultando por prefijo " + prefix);
		List<Producto> lista = this.ifzProductos.findLikeProperty("clave", prefix, 0);
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
		return ("ADMINISTRADOR".equals(this.usuario) ? true : false);
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
			return "- " + this.pojoProducto.getId() + ((this.valMaestro == TipoMaestro.Administrativo) ? " - ADMINISTRATIVO" : "");
		return "";
	}
	
	public void setTituloProducto(String value) {}
	
	public String getEncabezadoProducto() {
		if (this.pojoProducto != null && this.pojoProducto.getId() != null && this.pojoProducto.getId() > 0L)
			return this.pojoProducto.getClave() + " - " + this.pojoProducto.getDescripcion();
		return "Indique datos para Nuevo Producto" + ((this.valMaestro == TipoMaestro.Administrativo) ? " ADMINISTRATIVO" : "");
	}
	
	public void setEncabezadoProducto(String value) {}
	
	public double getPrecioVenta() {
		return this.pojoProducto.getPrecioCompra() * 1.7;
	}
	
	public void setPrecioVenta(double precioVenta) {
		this.pojoProducto.setPrecioVenta(precioVenta);
	}

	public double getPrecioUnitario() {
		return this.pojoProducto.getPrecioCompra() * 1.1;
	}
	
	public void setPrecioUnitario(double precioUnitario){
		this.pojoProducto.setPrecioUnitario(precioUnitario);
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

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public boolean getOperacionCompleta() {
		return operacionCompleta;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCompleta = operacionCompleta;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
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
		return listaProductosGrid;
	}

	public void setListaProductosGrid(List<Producto> listaProductosGrid) {
		this.listaProductosGrid = listaProductosGrid;
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

	public Producto getPojoProductoEliminar() {
		return pojoProductoEliminar;
	}

	public void setPojoProductoEliminar(Producto pojoProductoEliminar) {
		this.pojoProductoEliminar = pojoProductoEliminar;
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


	public String getMensajeDetalles() {
		return mensajeDetalles;
	}


	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-12 | Javier Tirado 	| Modifico el metodo analizarExcel: Envio lote de productos de 1000 en 1000 en caso de ser necesario
 *  									  con la lectura del archivo maestro
 */
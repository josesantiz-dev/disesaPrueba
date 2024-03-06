package net.giro.compras.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.compras.beans.Comparativa;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.logica.ComparativaDetalleRem;
import net.giro.compras.logica.ComparativaRem;
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.inventarios.comun.TipoMaestro;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.impresion.ReportesRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="defaultAction")
public class DefaultAction implements Serializable {
	private static Logger log = Logger.getLogger(DefaultAction.class);
	private static final long serialVersionUID = 1L;
	PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	HttpSession httpSession;
	private InitialContext ctx;
	// Variables de operacion
	private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	private boolean perfilEgresos;
	TipoMaestro tipoMaestro;
	// Busqueda principal
	private ObraRem ifzObras;
	private Obra pojoObra;
	private List<Obra> listObras;
	private String valorBusqueda;
	private int mainPaginacion;
	// Requisiciones
	RequisicionRem ifzRequisicion;
	RequisicionDetalleRem ifzReqDetalles;
	private List<Requisicion> listRequisiciones;
	private Requisicion pojoRequisicion;
	private int valorPaginacionRequisiciones;
	// Cotizaciones
	private CotizacionRem ifzCotizacion;
	CotizacionDetalleRem ifzCotDetalles;
	private List<Cotizacion> listCotizaciones;
	private Cotizacion pojoCotizacion;
	private int valorPaginacionCotizaciones;
	private int estatusCotizacion;
	// Comparativas
	ComparativaRem ifzComparativa;
	ComparativaDetalleRem ifzComDetalles;
	private List<Comparativa> listComparativas;
	private Comparativa pojoComparativa;
	private int valorPaginacionComparativas;
	// Ordenes de Compra
	OrdenCompraRem ifzOrdenes;
	OrdenCompraDetalleRem ifzOrdenDetalles;
	private List<OrdenCompra> listOrdenesCompra;
	private OrdenCompra pojoOrdenCompra;
	private int valorPaginacionOrdenesCompra;
	// Reportes
	ReportesRem	ifzReportes;
	// DEBUG
	boolean isDebug;
	HashMap<String, String> paramsRequest;
	
	
	public DefaultAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			
			Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey(), item.getValue());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			// EVALUACION DE PERFILES
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzRequisicion = (RequisicionRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalles = (RequisicionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzCotizacion = (CotizacionRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzCotDetalles = (CotizacionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzComparativa = (ComparativaRem) this.ctx.lookup("ejb:/Logica_Compras//ComparativaFac!net.giro.compras.logica.ComparativaRem");
			this.ifzComDetalles = (ComparativaDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//ComparativaDetalleFac!net.giro.compras.logica.ComparativaDetalleRem");
			this.ifzOrdenes = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOrdenDetalles = (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			
		} catch (Exception e) {
			log.error("Error en Compras.DefaultAction.constructor DefaultAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
    		control();
    		this.mainPaginacion = 1;
			log.info("Buscando en obras : " + this.valorBusqueda);
			this.listObras = this.ifzObras.findLike(this.valorBusqueda, this.perfilEgresos, false, "CASE model.estatus WHEN 0 THEN 1 ELSE 0 END, nombre", 0);
			if (this.listObras == null || this.listObras.isEmpty()) {
				log.info("Error 2 - Busqueda sin resultados");
	    		control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listObras != null) 
				this.listObras.clear();
    		log.error("Error en Compras.DefaultAction.buscar", e);
    		control(true);
    	} finally {
    		if (this.listObras != null && ! this.listObras.isEmpty())
    			log.info(this.listObras.size() + " resultados encontrados.");
    	}
	}
	
	public void verRequisiciones() {
		try {
    		control();
    		
    		if (this.listRequisiciones == null || this.listRequisiciones.isEmpty()) {
				log.info("Error 2 - Busqueda sin resultados");
	    		control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listRequisiciones != null) 
				this.listRequisiciones.clear();
    		log.error("Error en Compras.CotizacionAction.buscar", e);
    		control(true);
    	} finally {
    		this.valorPaginacionRequisiciones = 1;
    		if (this.listRequisiciones != null && ! this.listRequisiciones.isEmpty())
    			log.info(this.listRequisiciones.size() + " resultados encontrados.");
    	}
	}
	
	public void verCotizaciones() {
		try {
    		control();
			this.tipoMaestro = (this.pojoObra.isAdministrativa() ? TipoMaestro.Administrativo : TipoMaestro.Inventario);
    		log.info("Recuperando Cotizaciones por obra: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
			this.listCotizaciones = this.ifzCotizacion.findAll(this.pojoObra.getId(), 0, "CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.id desc");
    		if (this.listCotizaciones == null || this.listCotizaciones.isEmpty()) {
				log.info("Error 2 - Busqueda sin resultados");
	    		control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listCotizaciones != null) 
				this.listCotizaciones.clear();
    		log.error("Error en Compras.DefaultAction.verCotizaciones", e);
    		control(true);
    	} finally {
    		this.valorPaginacionCotizaciones = 1;
    		if (this.listCotizaciones != null && ! this.listCotizaciones.isEmpty())
    			log.info(this.listCotizaciones.size() + " resultados encontrados.");
    	}
	}
	
	public void verComparativas() {
		try {
    		control();
    		
    		if (this.listComparativas == null || this.listComparativas.isEmpty()) {
				log.info("Error 2 - Busqueda sin resultados");
	    		control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listComparativas != null) 
				this.listComparativas.clear();
    		log.error("Error en Compras.CotizacionAction.verComparativas", e);
    		control(true);
    	} finally {
    		this.valorPaginacionComparativas = 1;
    		if (this.listComparativas != null && ! this.listComparativas.isEmpty())
    			log.info(this.listComparativas.size() + " resultados encontrados.");
    	}
	}
	
	public void verOrdenesCompra() {
		try {
    		control();
    		
    		if (this.listOrdenesCompra == null || this.listOrdenesCompra.isEmpty()) {
				log.info("Error 2 - Busqueda sin resultados");
	    		control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listOrdenesCompra != null) 
				this.listOrdenesCompra.clear();
    		log.error("Error en Compras.CotizacionAction.verOrdenesCompra", e);
    		control(true);
    	} finally {
    		this.valorPaginacionOrdenesCompra = 1;
    		if (this.listOrdenesCompra != null && ! this.listOrdenesCompra.isEmpty())
    			log.info(this.listOrdenesCompra.size() + " resultados encontrados.");
    	}
	}
	
	private void control() { 
		this.operacion = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(boolean value) {
		if (value)
			control(value, 1, "ERROR");
		else
			control(value, 0, null);
	}
	
	private void control(int value) { 
		if (value <= 1)
			control(true, 1, "ERROR");
		else
			control(true, value, "ERROR"); 
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "": mensaje;
	}

	// ----------------------------------------------------------------------------------------------------------
	// REQUISICIONES 
	// ----------------------------------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------------------------------
	// COTIZACIONES 
	// ----------------------------------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------------------------------
	// COMPARATIVAS 
	// ----------------------------------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------------------------------
	// ORDENES DE COMPRA 
	// ----------------------------------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------------------------------
	// PROPIEDADES 
	// ----------------------------------------------------------------------------------------------------------

	public boolean isOperacion() {
		return operacion;
	}

	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
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

	public boolean isEsAdministrativo() {
		return perfilEgresos;
	}

	public void setEsAdministrativo(boolean esAdministrativo) {
		this.perfilEgresos = esAdministrativo;
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

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public int getValorPaginacion() {
		return mainPaginacion;
	}

	public void setValorPaginacion(int mainPaginacion) {
		this.mainPaginacion = mainPaginacion;
	}

	public List<Requisicion> getListRequisiciones() {
		return listRequisiciones;
	}

	public void setListRequisiciones(List<Requisicion> listRequisiciones) {
		this.listRequisiciones = listRequisiciones;
	}

	public Requisicion getPojoRequisicion() {
		return pojoRequisicion;
	}

	public void setPojoRequisicion(Requisicion pojoRequisicion) {
		this.pojoRequisicion = pojoRequisicion;
	}

	public int getValorPaginacionRequisiciones() {
		return valorPaginacionRequisiciones;
	}

	public void setValorPaginacionRequisiciones(int valorPaginacionRequisiciones) {
		this.valorPaginacionRequisiciones = valorPaginacionRequisiciones;
	}

	public List<Cotizacion> getListCotizaciones() {
		return listCotizaciones;
	}

	public void setListCotizaciones(List<Cotizacion> listCotizaciones) {
		this.listCotizaciones = listCotizaciones;
	}

	public Cotizacion getPojoCotizacion() {
		return pojoCotizacion;
	}

	public void setPojoCotizacion(Cotizacion pojoCotizacion) {
		this.pojoCotizacion = pojoCotizacion;
	}

	public int getValorPaginacionCotizaciones() {
		return valorPaginacionCotizaciones;
	}

	public void setValorPaginacionCotizaciones(int valorPaginacionCotizaciones) {
		this.valorPaginacionCotizaciones = valorPaginacionCotizaciones;
	}

	public int getEstatusCotizacion() {
		return estatusCotizacion;
	}

	public void setEstatusCotizacion(int estatusCotizacion) {
		this.estatusCotizacion = estatusCotizacion;
	}

	public List<Comparativa> getListComparativas() {
		return listComparativas;
	}

	public void setListComparativas(List<Comparativa> listComparativas) {
		this.listComparativas = listComparativas;
	}

	public Comparativa getPojoComparativa() {
		return pojoComparativa;
	}

	public void setPojoComparativa(Comparativa pojoComparativa) {
		this.pojoComparativa = pojoComparativa;
	}

	public int getValorPaginacionComparativas() {
		return valorPaginacionComparativas;
	}

	public void setValorPaginacionComparativas(int valorPaginacionComparativas) {
		this.valorPaginacionComparativas = valorPaginacionComparativas;
	}

	public List<OrdenCompra> getListOrdenesCompra() {
		return listOrdenesCompra;
	}

	public void setListOrdenesCompra(List<OrdenCompra> listOrdenesCompra) {
		this.listOrdenesCompra = listOrdenesCompra;
	}

	public OrdenCompra getPojoOrdenCompra() {
		return pojoOrdenCompra;
	}

	public void setPojoOrdenCompra(OrdenCompra pojoOrdenCompra) {
		this.pojoOrdenCompra = pojoOrdenCompra;
	}

	public int getValorPaginacionOrdenesCompra() {
		return valorPaginacionOrdenesCompra;
	}

	public void setValorPaginacionOrdenesCompra(int valorPaginacionOrdenesCompra) {
		this.valorPaginacionOrdenesCompra = valorPaginacionOrdenesCompra;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
}

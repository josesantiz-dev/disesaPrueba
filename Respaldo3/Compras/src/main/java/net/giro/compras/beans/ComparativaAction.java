package net.giro.compras.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.compras.beans.Comparativa;
import net.giro.compras.beans.ComparativaDetalle;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.logica.ComparativaDetalleRem;
import net.giro.compras.logica.ComparativaRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.inventarios.comun.TipoMaestro;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="comAction")
public class ComparativaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ComparativaAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	// Interfaces
	private ObraRem ifzObras;
	private ComparativaRem ifzComparativa;
	private ComparativaDetalleRem ifzComDetalles;
	private CotizacionRem ifzCotizaciones;
	private ReportesRem	ifzReportes;
	private OrdenCompraRem ifzOC;	
	// Busqueda principal
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> busquedaTipos;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaPaginacion;
	// Busqueda Comparativa
	private List<Comparativa> listComparativas;
	private Comparativa pojoComparativa;
	private List<SelectItem> busquedaTiposComparativa;	
	private String busquedaCampoComparativa;
	private String busquedaValorComparativa;
	private int busquedaPaginacionComparativa;
	// Busqueda Cotizaciones
	private List<Cotizacion> listCotizaciones;
	private Cotizacion pojoCotizacion;
	private List<SelectItem> busquedaTiposCotizacion;	
	private String busquedaCampoCotizacion;
	private String busquedaValorCotizacion;
	private int busquedaPaginacionCotizacion;
	private boolean avanzadaBusquedaCotizaciones;
	// Variables de operacion
	private List<ComparativaDetalle> listDetalles;
	private List<ComparativaDetalle> listDetallesBorrados;
	private ComparativaDetalle pojoDetalle;
	private int paginacionDetalles;
    private boolean operacionCancelada;
	private String mensaje;
	private int tipoMensaje;
    private boolean obraActiva;
    private List<SelectItem> listTiposComparativaItems;
	private boolean esAdministrativo;
	private TipoMaestro tipoMaestro;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public ComparativaAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		PropertyResourceBundle msgProperties = null;
		ValueExpression valExp = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			msgProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet()) 
				this.paramsRequest.put(item.getKey(), item.getValue());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			ctx = new InitialContext();			
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzComparativa = (ComparativaRem) ctx.lookup("ejb:/Logica_Compras//ComparativaFac!net.giro.compras.logica.ComparativaRem");
			this.ifzComDetalles = (ComparativaDetalleRem) ctx.lookup("ejb:/Logica_Compras//ComparativaDetalleFac!net.giro.compras.logica.ComparativaDetalleRem");
			this.ifzCotizaciones = (CotizacionRem) ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzOC = (OrdenCompraRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzComparativa.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzComDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOC.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());

			/* VALIDACION DE PERFILES */
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.esAdministrativo = ((valPerfil != null && "S".equals(valPerfil.trim())) ? true : false);
			
			// Busqueda Principal
			this.busquedaTipos = new ArrayList<SelectItem>();
			this.busquedaTipos.add(new SelectItem("*", "Coincidencia"));
			this.busquedaTipos.add(new SelectItem("nombre", "Obra"));
			this.busquedaTipos.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaTipos.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaTipos.add(new SelectItem("nombreResponsable", "Encargado"));
			this.busquedaTipos.add(new SelectItem("id", "ID"));
			this.busquedaCampo = this.busquedaTipos.get(0).getValue().toString();
			this.busquedaValor = "";
			this.busquedaPaginacion = 1;
			
			// Busqueda Comparativa
			this.busquedaTiposComparativa = new ArrayList<SelectItem>();
			this.busquedaTiposComparativa.add(new SelectItem("nombreObra", "Obra"));
			this.busquedaTiposComparativa.add(new SelectItem("tipo", "Tipo"));
			this.busquedaTiposComparativa.add(new SelectItem("id", "ID"));
			this.busquedaCampoComparativa = this.busquedaTipos.get(0).getValue().toString();
			this.busquedaValorComparativa = "";
			this.busquedaPaginacionComparativa = 1;
			
			// Busqueda Cotizaciones
			this.busquedaTiposCotizacion = new ArrayList<SelectItem>();
			this.busquedaTiposCotizacion.add(new SelectItem("*", "Coincidencia"));
			this.busquedaTiposCotizacion.add(new SelectItem("nombreProveedor", "Proveedor"));
			this.busquedaTiposCotizacion.add(new SelectItem("nombreComprador", "Comprador"));
			this.busquedaTiposCotizacion.add(new SelectItem("folio", "Folio"));
			this.busquedaTiposCotizacion.add(new SelectItem("id", "ID"));
			nuevaBusquedaCotizaciones();
			
			// Tipos de comparativas
			this.listTiposComparativaItems = new ArrayList<SelectItem>();
			this.listTiposComparativaItems.add(new SelectItem(1, msgProperties.getString("maestro.inventarios")));
			this.listTiposComparativaItems.add(new SelectItem(2, msgProperties.getString("maestro.administrativo")));
			
			this.pojoComparativa = new Comparativa();
			this.tipoMaestro = TipoMaestro.Inventario;
		} catch (Exception e) {
			log.error("Error en Compras.ComparativaAction.constructor CotizacionesAction", e);
		}
	}
	
	public void buscar() {
		String orderBy = "";
		
		try {
    		control();
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = this.busquedaTipos.get(0).getValue().toString();
			orderBy = "CASE model.estatus WHEN 0 THEN 1 ELSE 0 END";
			if (this.busquedaCampo.contains("*"))
				orderBy += ", model.nombre";
			else
				orderBy += ", " + this.busquedaCampo;
			
			log.info("Busqueda principal. " + this.busquedaCampo + ":" + this.busquedaValor);
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.esAdministrativo, orderBy, 0);
			if (this.listObras == null || this.listObras.isEmpty()) {
				log.info("ERROR 2 - Busqueda sin resultados");
	    		control(2, "Busqueda sin resultados");
				return;
			}
			log.info(this.listObras.size() + " resultados encontrados.");
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar las Comparativas", e);
			if (this.listObras != null) 
				this.listObras.clear();
    	} finally {
    		if (this.listObras != null && ! this.listObras.isEmpty())
    			log.info(this.listObras.size() + " resultados encontrados.");
    	}
	}

	public void ver() {
		try {
    		control();
    		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) 
    			return;
    		
    		// Inicializacion
			log.info("Obra seleccionada: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
    		this.busquedaPaginacionComparativa = 1;
    		this.listComparativas = new ArrayList<Comparativa>();
    		this.obraActiva = (this.pojoObra.getEstatus() != 0);
			this.tipoMaestro = (this.pojoObra.isAdministrativa() ? TipoMaestro.Administrativo : TipoMaestro.Inventario);
    		
    		// Recuperamos las Comparativas de la Obra
			log.info("Cargo Comparativas ... ");
			if (this.pojoObra.getId() != null && this.pojoObra.getId() > 0L) {
				this.ifzComparativa.OrderBy("model.id desc");
				this.listComparativas = this.ifzComparativa.findByProperty("idObra", this.pojoObra.getId(), 0);
			}

			if (this.listComparativas == null)
				this.listComparativas = new ArrayList<Comparativa>();
			log.info(this.listComparativas.size() + " Comparativas encontradas.");
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar las Cotizacion de la Comparativa indicada", e);
    	} 
	}

	public void nuevo() {
		try {
    		control();
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Obra seleccionada o no se selecciono ninguna Obra");
				return;
			}
			
			this.pojoComparativa = new Comparativa();
			this.pojoComparativa.setIdObra(this.pojoObra.getId());
			this.pojoComparativa.setTipo(this.tipoMaestro.ordinal());
			this.listDetalles = new ArrayList<ComparativaDetalle>();
    	} catch (Exception e) {
    		control("Pcurrio un problema al preparar una nueva Comparativa", e);
    	}
	}
	
	public void editar() throws Exception {
		try {
    		control();
    		if (this.pojoComparativa == null) {
        		log.info("No selecciono ninguna comparativa o el pojo es nulo");
    			control(-1, "No selecciono ninguna Comparativa");
    			return;
    		}
    		
    		// Inicializo lista de detalles
    		this.listDetalles = new ArrayList<ComparativaDetalle>();
    		// Recupero lista de detalles
    		log.info("Recupero las cotizaciones de la Comparativa elegida");
    		this.listDetalles = this.ifzComDetalles.findByProperty("idComparativa", this.pojoComparativa.getId(), 0);
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar la Comparativa seleccionada", e);
    	}
	}
	
	public void guardar() throws Exception {
		try {
    		control();
    		if (! validarComparativa()) 
    			return;
    		
    		this.pojoComparativa.setIdObra(this.pojoObra.getId());
    		this.pojoComparativa.setNombreObra(this.pojoObra.getNombre());
			this.pojoComparativa.setTipo(this.tipoMaestro.ordinal());
			this.pojoComparativa.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			
    		
    		// Guardamos el encabezado de la comparativa
    		log.info("Guardando Comparativa");
    		if (this.pojoComparativa.getId() == null || this.pojoComparativa.getId() <= 0L) {
        		this.pojoComparativa.setFecha(Calendar.getInstance().getTime());
        		this.pojoComparativa.setFechaCreacion(Calendar.getInstance().getTime());
        		this.pojoComparativa.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());

    			this.ifzComparativa.setInfoSesion(this.loginManager.getInfoSesion());
    			this.pojoComparativa.setId(this.ifzComparativa.save(this.pojoComparativa));
    		} else {
        		this.pojoComparativa.setFechaModificacion(Calendar.getInstance().getTime());
        		this.pojoComparativa.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());

    			this.ifzComparativa.setInfoSesion(this.loginManager.getInfoSesion());
    			this.ifzComparativa.update(this.pojoComparativa);
    		}
    		
    		// Quitamos los detalles borrados si corresponde
    		if (this.listDetallesBorrados != null && ! this.listDetallesBorrados.isEmpty()) {
        		log.info("Borrando detalles de Comparativa");
    			for (ComparativaDetalle det : this.listDetallesBorrados)
    				this.ifzComDetalles.delete(det.getId());
    			this.listDetallesBorrados.clear();
    		}
    		
    		// Guardamos los detalles
    		log.info("Guardando detalles de Comparativa");
    		for (ComparativaDetalle det : this.listDetalles) {
    			det.setIdComparativa(this.pojoComparativa.getId());
    			det.setFechaModificacion(Calendar.getInstance().getTime());
    			det.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
    			
        		if (det.getId() == null || det.getId() <= 0L) {
        			det.setFechaCreacion(Calendar.getInstance().getTime());
        			det.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());

        			this.ifzComDetalles.setInfoSesion(this.loginManager.getInfoSesion());
        			det.setId(this.ifzComDetalles.save(det));
        		} else {
        			this.ifzComDetalles.setInfoSesion(this.loginManager.getInfoSesion());
        			this.ifzComDetalles.update(det);
        		}
			}
    		log.info("Comparativa registrada");
    		
    		// Lanzamos el reporte
    		reporte();

    		nuevo();
    		ver();
    	} catch (Exception e) {
    		control("Ocurrio un problema al guardar la Comparativa", e);
    	} 
	}
	
	public void eliminar() {
		try {
    		control();
    		if (this.pojoComparativa != null && this.pojoComparativa.getId() != null && this.pojoComparativa.getId() > 0L) {
    			this.pojoComparativa.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
    			this.pojoComparativa.setFechaModificacion(Calendar.getInstance().getTime());
    			this.pojoComparativa.setEstatus(1);

    			this.ifzComparativa.setInfoSesion(this.loginManager.getInfoSesion());
    			this.ifzComparativa.update(this.pojoComparativa);
    			this.pojoComparativa = new Comparativa();
        		
    			this.ver();
    		}
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar eliminar la Comparativa indicada", e);
    	}
	}

	public void eliminarDetalle() {
		int index = -1;
		
		try {
    		control();
    		if (this.pojoDetalle == null)
    			return;
    		
    		if (this.listDetallesBorrados == null)
    			this.listDetallesBorrados = new ArrayList<ComparativaDetalle>();
    		
    		log.info("Borrando detalle");
    		index = this.listDetalles.indexOf(this.pojoDetalle);
    		if (this.pojoDetalle.getId() != null && this.pojoDetalle.getId() > 0L)
    			this.listDetallesBorrados.add(this.pojoDetalle);
    		this.listDetalles.remove(index);
    		this.pojoDetalle = null;
    		log.info("--- ---> Detalle borrado");
    	} catch (Exception e) {
    		control("Ocurrio un problema al quitar la Cotizacion indicada", e);
    	}
	}
	
	public void reporte() {
		byte[] contenidoReporte = null;
		String formatoReporte = "";
		String nombreReporte = "";
		
		try {
    		control();
    		log.info("Generando reporte Comparativa");
			if (this.pojoComparativa != null && this.pojoComparativa.getId() != null && this.pojoComparativa.getId() > 0L) {
				nombreReporte = "CO" + this.pojoComparativa.getId() + "-OB" + this.pojoComparativa.getIdObra() + ".xls";
				
				// Parametros del reporte
				HashMap<String, Object> paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idComparativa", this.pojoComparativa.getId());
				paramsReporte.put("idObra", this.pojoComparativa.getIdObra());

				// Parametros para la ejecucion del reporte
				HashMap<String, String>params = new HashMap<String, String>();
				params.put("idPrograma", 	  "169");
				params.put("nombreDocumento", nombreReporte);
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());

	    		log.info("Ejecutando reporte Comparativa");
				Respuesta respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if(respuesta.getErrores().getCodigoError() != 0L) {
					log.error("Error Interno - No se pudo ejecutar el reporte :: " + respuesta.getErrores().getDescError());
		    		control(-1, "Error Interno - No se pudo ejecutar el reporte");
					return;
				}
				
				// Recogemos reporte
	    		log.info("Recogemos contenido de reporte");
				formatoReporte = (String) respuesta.getBody().getValor("formatoReporte");
				contenidoReporte = (byte[]) respuesta.getBody().getValor("contenidoReporte");
				if (contenidoReporte == null) {
					log.error("Error Interno - No se pudo obtener el reporte para Comparativa ID " + this.pojoComparativa.getId());
		    		control(-1, "Error Interno - No se pudo recuperar el reporte para Comparativa #" + this.pojoComparativa.getId());
					return;
				}

				this.httpSession.setAttribute("nombreDocumento", nombreReporte); 
				this.httpSession.setAttribute("contenido", contenidoReporte);
				this.httpSession.setAttribute("formato", formatoReporte);
	    		log.info("Reporte ejecutado y lanzado");
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al lanzar el reporte de Comparativa", e);
    	}
	}
	
	private boolean validarComparativa() {
		log.info("Validando Comparativa");
		if (this.pojoComparativa.getDescripcion() == null || "".equals(this.pojoComparativa.getDescripcion())) {
			log.info("ERROR INTERNO - Debe indicar la descripcion de la Comparativa");
			control(-1, "Debe indicar la descripcion de la Comparativa");
			return false;
		}
		
		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
			log.info("ERROR INTERNO - No se pudo asignar la Obra a la Comparativa");
			control(-1, "Ocurrio un problema al asignar la Obra seleccionada");
			return false;
		}
		
		if (this.pojoComparativa.getTipo() <= 0) {
			log.info("ERROR INTERNO - Debe indicar el tipo de comparativa");
			control(-1, "Debe indicar el tipo de comparativa");
			return false;
		}
		
		if (this.listDetalles == null || this.listDetalles.isEmpty()) {
			log.info("ERROR INTERNO - Comparativa sin cotizaciones");
			control(-1, "Debe agregar al menos una Cotizacion");
			return false;
		}
			
		return true;
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

	private void control(boolean incompleta, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacionCancelada = incompleta;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "Ocurrio un problema no controlado al realizar la operacion");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	// --------------------------------------------------------------------------------------
	// BUSQUEDA COTIZACION
	// --------------------------------------------------------------------------------------
	
	public void nuevaBusquedaCotizaciones() {
		control();
    	this.busquedaCampoCotizacion = this.busquedaTiposCotizacion.get(0).getValue().toString();
		this.busquedaValorCotizacion = "";
		this.busquedaPaginacionCotizacion = 1;
		this.avanzadaBusquedaCotizaciones = false;
		
		this.listCotizaciones = new ArrayList<Cotizacion>();
    }
	
	public void buscarCotizaciones() throws Exception {
		try {
			control();
			if ("".equals(this.busquedaCampoCotizacion))
				this.busquedaCampoCotizacion = this.busquedaTiposCotizacion.get(0).getValue().toString();
			
			log.info("Buscando Cotizaciones. " + this.busquedaCampoCotizacion + ":" + this.busquedaValorCotizacion);
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCotizaciones = this.ifzCotizaciones.findLikeProperty(this.busquedaCampoCotizacion, this.busquedaValorCotizacion, this.pojoObra.getId(), 0, false, false, false, "CASE model.sistema WHEN 0 THEN 0 ELSE 1 END, model.fecha desc, CASE model.estatus WHEN 1 THEN 1 ELSE 0 END, model.id desc", 0);
			if (this.listCotizaciones == null || this.listCotizaciones.isEmpty()) 
				control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar las Cotizaciones", e);
    	} finally {
    		this.busquedaPaginacionCotizacion = 1;
			this.listCotizaciones = (this.listCotizaciones != null ? this.listCotizaciones : new ArrayList<Cotizacion>());
    	}
	}

	public void seleccionarCotizacion() throws Exception {
		List<OrdenCompra> listOrdenes = null;
		String folioOrdenCompra = "";
		
		try {
			control();
			log.info("Comprobamos Cotizacion");
			if (this.pojoCotizacion == null || this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				log.info("No se pudo obtener la Cotizacion seleccionada");
				control(-1, "No se pudo obtener la Cotizacion seleccionada");
				return;
			}
			
			// Comprobamos que la cotizacion no este agregada a la lista
			if (this.listDetalles == null)
				this.listDetalles = new ArrayList<ComparativaDetalle>();
			
			for (ComparativaDetalle det : this.listDetalles) {
				if (this.pojoCotizacion.getId().equals(det.getIdCotizacion().getId())) {
					nuevaBusquedaCotizaciones();
					log.info("La cotizacion ya exite en el listado");
					control(-1, "Cotizacion previamente añadida");
					return;
				}
			}
			
			// Obtenemos orden de compra si corresponde
			log.info("Comprobamos Orden de Compra");
			listOrdenes = this.ifzOC.findByProperty("idCotizacion.id", this.pojoCotizacion.getId(), 0);
			if (listOrdenes != null && ! listOrdenes.isEmpty())
				folioOrdenCompra = listOrdenes.get(0).getFolio();
			
			// generamos detalle de comparativa
			log.info("Generamos detalle");
			this.pojoDetalle = new ComparativaDetalle();
			this.pojoDetalle.setIdCotizacion(this.pojoCotizacion);
			this.pojoDetalle.setOrdenCompraFolio(folioOrdenCompra);
			this.pojoDetalle.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoDetalle.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoDetalle.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoDetalle.setFechaModificacion(Calendar.getInstance().getTime());
			this.listDetalles.add(this.pojoDetalle);

			nuevaBusquedaCotizaciones();
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar la Cotizacion seleccionada", e);
    	} finally {
			this.pojoDetalle = null;
    	}
	}

	public void toggleAvanzadaBusquedaCotizaciones() {
		this.avanzadaBusquedaCotizaciones = ! this.avanzadaBusquedaCotizaciones;
	}
	
	// --------------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------------
	
	public String getComparativaTitulo() {
		if (this.pojoComparativa != null && this.pojoComparativa.getId() != null && this.pojoComparativa.getId() > 0L)
			return (this.tipoMaestro == TipoMaestro.Administrativo ? "ADMINISTRATIVA " : "") + this.pojoComparativa.getId();
		return (this.tipoMaestro == TipoMaestro.Administrativo ? "ADMINISTRATIVA" : "");
	}
	
	public void setComparativaTitulo(String value) {}
	
	public String getHeader() {
		if (this.pojoComparativa != null && this.pojoComparativa.getId() != null && this.pojoComparativa.getId() > 0L)
			return this.pojoComparativa.getId().toString();
		return "";
	}
	
	public void setHeader(String value) {}
	
	public String getEncabezado() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		return "";
	}
	
	public void setEncabezado(String valur) {}
	
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

	public List<SelectItem> getBusquedaTipos() {
		return busquedaTipos;
	}

	public void setBusquedaTipos(List<SelectItem> busquedaTipos) {
		this.busquedaTipos = busquedaTipos;
	}

	public String getBusquedaCampo() {
		return busquedaCampo;
	}

	public void setBusquedaCampo(String busquedaCampo) {
		this.busquedaCampo = busquedaCampo;
	}

	public String getBusquedaValor() {
		return busquedaValor;
	}

	public void setBusquedaValor(String busquedaValor) {
		this.busquedaValor = busquedaValor;
	}

	public int getBusquedaPaginacion() {
		return busquedaPaginacion;
	}

	public void setBusquedaPaginacion(int busquedaPaginacion) {
		this.busquedaPaginacion = busquedaPaginacion;
	}

	public List<Comparativa> getListComparativas() {
		return listComparativas;
	}

	public void setListComparativas(List<Comparativa> listComparativas) {
		this.listComparativas = listComparativas;
	}

	public List<ComparativaDetalle> getListDetalles() {
		return listDetalles;
	}

	public void setListDetalles(List<ComparativaDetalle> listDetalles) {
		this.listDetalles = listDetalles;
	}

	public Comparativa getPojoComparativa() {
		return pojoComparativa;
	}

	public void setPojoComparativa(Comparativa pojoComparativa) {
		this.pojoComparativa = pojoComparativa;
	}

	public List<SelectItem> getBusquedaTiposComparativa() {
		return busquedaTiposComparativa;
	}

	public void setBusquedaTiposComparativa(
			List<SelectItem> busquedaTiposComparativa) {
		this.busquedaTiposComparativa = busquedaTiposComparativa;
	}

	public String getBusquedaCampoComparativa() {
		return busquedaCampoComparativa;
	}

	public void setBusquedaCampoComparativa(String busquedaCampoComparativa) {
		this.busquedaCampoComparativa = busquedaCampoComparativa;
	}

	public String getBusquedaValorComparativa() {
		return busquedaValorComparativa;
	}

	public void setBusquedaValorComparativa(String busquedaValorComparativa) {
		this.busquedaValorComparativa = busquedaValorComparativa;
	}

	public int getBusquedaPaginacionComparativa() {
		return busquedaPaginacionComparativa;
	}

	public void setBusquedaPaginacionComparativa(int busquedaPaginacionComparativa) {
		this.busquedaPaginacionComparativa = busquedaPaginacionComparativa;
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

	public List<SelectItem> getBusquedaTiposCotizacion() {
		return busquedaTiposCotizacion;
	}

	public void setBusquedaTiposCotizacion(List<SelectItem> busquedaTiposCotizacion) {
		this.busquedaTiposCotizacion = busquedaTiposCotizacion;
	}

	public String getBusquedaCampoCotizacion() {
		return busquedaCampoCotizacion;
	}

	public void setBusquedaCampoCotizacion(String busquedaCampoCotizacion) {
		this.busquedaCampoCotizacion = busquedaCampoCotizacion;
	}

	public String getBusquedaValorCotizacion() {
		return busquedaValorCotizacion;
	}

	public void setBusquedaValorCotizacion(String busquedaValorCotizacion) {
		this.busquedaValorCotizacion = busquedaValorCotizacion;
	}

	public int getBusquedaPaginacionCotizacion() {
		return busquedaPaginacionCotizacion;
	}

	public void setBusquedaPaginacionCotizacion(int busquedaPaginacionCotizacion) {
		this.busquedaPaginacionCotizacion = busquedaPaginacionCotizacion;
	}

	public boolean getOperacion() {
		return operacionCancelada;
	}

	public void setOperacion(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
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

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
	
	public boolean isObraActiva() {
		return obraActiva;
	}
	
	public void setObraActiva(boolean obraActiva) {
		this.obraActiva = obraActiva;
	}

	public List<SelectItem> getListTiposComparativaItems() {
		return listTiposComparativaItems;
	}

	public void setListTiposComparativaItems(
			List<SelectItem> listTiposComparativaItems) {
		this.listTiposComparativaItems = listTiposComparativaItems;
	}

	public int getPaginacionDetalles() {
		return paginacionDetalles;
	}

	public void setPaginacionDetalles(int paginacionDetalles) {
		this.paginacionDetalles = paginacionDetalles;
	}

	public ComparativaDetalle getPojoDetalle() {
		return pojoDetalle;
	}

	public void setPojoDetalle(ComparativaDetalle pojoDetalle) {
		this.pojoDetalle = pojoDetalle;
	}

	public boolean isEsAdministrativo() {
		return esAdministrativo;
	}

	public void setEsAdministrativo(boolean esAdministrativo) {
		this.esAdministrativo = esAdministrativo;
	}

	public boolean isAvanzadaBusquedaCotizaciones() {
		return avanzadaBusquedaCotizaciones;
	}

	public void setAvanzadaBusquedaCotizaciones(boolean avanzadaBusquedaCotizaciones) {
		this.avanzadaBusquedaCotizaciones = avanzadaBusquedaCotizaciones;
	}
}

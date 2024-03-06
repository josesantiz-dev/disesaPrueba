package net.giro.compras.logica;

import java.io.Serializable;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.adp.beans.ObraExt;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.dao.PersonaDAO;
import net.giro.clientes.logica.NegociosRem;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.CotizacionDetalleExt;
import net.giro.compras.beans.CotizacionExt;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.compras.beans.OrdenCompraDetalleImpuestos;
import net.giro.compras.beans.OrdenCompraDetalleImpuestosExt;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.compras.beans.OrdenCompraImpuestos;
import net.giro.compras.beans.OrdenCompraImpuestosExt;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.beans.RequisicionDetalleExt;
import net.giro.compras.beans.RequisicionExt;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.log4j.Logger;

public class ConvertExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ConvertExt.class);
	private OrdenCompraRem ifzOrdenCompras;
	private OrdenCompraDetalleRem ifzOrdenDetalle;
	private ObraRem ifzObras;
	private ProductoRem ifzProductos;
	private NegociosRem ifzNegocios;
	private PersonaDAO ifzPersonas;
	private ColoniaDAO ifzColonia;
	private LocalidadDAO ifzLocalidad;
	private ConValoresDAO ifzConValores;
	private CotizacionRem ifzCotizaciones;
	private RequisicionRem ifzRequisiciones;
	private EmpleadoRem ifzEmpleados;
	private String from;
	private boolean mostrarSystemOut;
	
	public ConvertExt() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(environment);
    		this.ifzCotizaciones = (CotizacionRem) ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
    		this.ifzRequisiciones = (RequisicionRem) ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
    		this.ifzOrdenCompras = (OrdenCompraRem)	ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
    		this.ifzOrdenDetalle = (OrdenCompraDetalleRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
    		this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
    		this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
    		this.ifzProductos = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
    		this.ifzPersonas = (PersonaDAO) ctx.lookup("ejb:/Model_Clientes//PersonaImp!net.giro.clientes.dao.PersonaDAO");
    		this.ifzNegocios = (NegociosRem) ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
    		this.ifzColonia = (ColoniaDAO) ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");
    		this.ifzLocalidad = (LocalidadDAO) ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
    		this.ifzConValores = (ConValoresDAO) ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
    		this.setFrom("Default");
    		this.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear Logica_Compras.ConvertExt", e);
		}
	}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public boolean getMostrarSystemOut() {
		return mostrarSystemOut;
	}

	public void setMostrarSystemOut(boolean mostrarSystemOut) {
		this.mostrarSystemOut = mostrarSystemOut;
	}

	// -------------------------------------------------------------------
	
	public Requisicion RequisicionExtToRequisicion(RequisicionExt pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: RequisicionExtToRequisicion");
		Requisicion pojoResult = new Requisicion();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setIdPropietario(pojoTarget.getIdPropietario());
			pojoResult.setNombrePropietario(pojoTarget.getNombrePropietario());
			pojoResult.setNombreSolicita(pojoTarget.getNombreSolicita());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setMoneda(pojoTarget.getMoneda());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setAutorizado(pojoTarget.getAutorizado());
			pojoResult.setIdUsuarioAutorizo(pojoTarget.getIdUsuarioAutorizo());
			pojoResult.setFechaAutorizacion(pojoTarget.getFechaAutorizacion());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setCerrada(pojoTarget.getCerrada());
			pojoResult.setSistema(pojoTarget.getSistema());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdObra(0L);
			pojoResult.setIdSolicita(0L);
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0L) {
				pojoResult.setIdObra(pojoTarget.getIdObra().getId());
				pojoResult.setIdSucursal(pojoTarget.getIdObra().getIdSucursal().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Solicita");
			if (pojoTarget.getIdSolicita() != null && pojoTarget.getIdSolicita().getId() > 0L) {
				pojoResult.setIdSolicita(pojoTarget.getIdSolicita().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Solicita terminado");
			}
		} catch (Exception e) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir RequisicionExtToRequisicion :: " + e.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir RequisicionExtToRequisicion", e);
			throw e;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: RequisicionExtToRequisicion :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public RequisicionExt RequisicionToRequisicionExt(Requisicion pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: RequisicionToRequisicionExt");
		RequisicionExt pojoResult = new RequisicionExt();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setIdPropietario(pojoTarget.getIdPropietario());
			pojoResult.setNombrePropietario(pojoTarget.getNombrePropietario());
			pojoResult.setNombreSolicita(pojoTarget.getNombreSolicita());
			pojoResult.setAutorizado(pojoTarget.getAutorizado());
			pojoResult.setIdUsuarioAutorizo(pojoTarget.getIdUsuarioAutorizo());
			pojoResult.setFechaAutorizacion(pojoTarget.getFechaAutorizacion());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setMoneda(pojoTarget.getMoneda());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setCerrada(pojoTarget.getCerrada());
			pojoResult.setSistema(pojoTarget.getSistema());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra() > 0L) {
				ObraExt pojoAux = this.ifzObras.findByIdExt(pojoTarget.getIdObra());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new ObraExt());
				pojoResult.setIdObra(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Comprador");
			if (pojoTarget.getIdPropietario() > 0L) {
				EmpleadoExt pojoAux = this.ifzEmpleados.findByIdExt(pojoTarget.getIdPropietario());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new EmpleadoExt());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Comprador terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Solicita");
			if (pojoTarget.getIdSolicita() > 0L) {
				EmpleadoExt pojoAux = this.ifzEmpleados.findByIdExt(pojoTarget.getIdSolicita());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new EmpleadoExt());
				pojoResult.setIdSolicita(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Solicita terminado");
			}
		} catch (Exception e) {
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir RequisicionToRequisicionExt", e);
			throw e;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: RequisicionToRequisicionExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public RequisicionDetalle RequisicionDetalleExtToRequisicionDetalle(RequisicionDetalleExt pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: RequisicionDetalleExtToRequisicionDetalle");
		RequisicionDetalle pojoResult = new RequisicionDetalle();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCantidadInicial(pojoTarget.getCantidadInicial());
			pojoResult.setCantidadOrdenCompra(pojoTarget.getCantidadOrdenCompra());
			pojoResult.setCantidadSuministrada(pojoTarget.getCantidadSuministrada());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdRequisicion(0L);
			pojoResult.setIdProducto(0L);
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Requisicion");
			if (pojoTarget.getIdRequisicion() != null && pojoTarget.getIdRequisicion().getId() != null && pojoTarget.getIdRequisicion().getId() > 0L) {
				pojoResult.setIdRequisicion(pojoTarget.getIdRequisicion().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Requisicion terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto");
			if (pojoTarget.getIdProducto() != null && pojoTarget.getIdProducto().getId() != null && pojoTarget.getIdProducto().getId() > 0L) {
				pojoResult.setIdProducto(pojoTarget.getIdProducto().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto terminado");
			}
		} catch (Exception e) {
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir RequisicionDetalleExtToRequisicionDetalle", e);
			throw e;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: RequisicionDetalleExtToRequisicionDetalle :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public RequisicionDetalleExt RequisicionDetalleToRequisicionDetalleExt(RequisicionDetalle pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: RequisicionDetalleToRequisicionDetalleExt");
		RequisicionDetalleExt pojoResult = new RequisicionDetalleExt();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCantidadInicial(pojoTarget.getCantidadInicial());
			pojoResult.setCantidadOrdenCompra(pojoTarget.getCantidadOrdenCompra());
			pojoResult.setCantidadSuministrada(pojoTarget.getCantidadSuministrada());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Requisicion");
			if (pojoTarget.getIdRequisicion() != null && pojoTarget.getIdRequisicion() > 0L) {
				RequisicionExt pojoAux = this.ifzRequisiciones.findExtById(pojoTarget.getIdRequisicion());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new RequisicionExt());
				pojoResult.setIdRequisicion(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Requisicion terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto");
			if (pojoTarget.getIdProducto() != null && pojoTarget.getIdProducto() > 0L) {
				ProductoExt pojoAux = this.ifzProductos.findExtById(pojoTarget.getIdProducto());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new ProductoExt());
				pojoResult.setIdProducto(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto terminado");
			}
		} catch (Exception e) {
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir RequisicionDetalleToRequisicionDetalleExt", e);
			throw e;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: RequisicionDetalleToRequisicionDetalleExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	// -------------------------------------------------------------------
	
	public Cotizacion CotizacionExtToCotizacion(CotizacionExt pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: CotizacionExtToCotizacion");
		Cotizacion pojoResult = new Cotizacion();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setFolio(pojoTarget.getFolio());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setFlete(pojoTarget.getFlete());
			pojoResult.setMargen(pojoTarget.getMargen());
			pojoResult.setTiempoEntrega(pojoTarget.getTiempoEntrega());
			pojoResult.setPorcentajeIva(pojoTarget.getPorcentajeIva());
			pojoResult.setSubtotal(pojoTarget.getSubtotal());
			pojoResult.setIva(pojoTarget.getIva());
			pojoResult.setTotal(pojoTarget.getTotal());
			pojoResult.setConsecutivoProveedor(pojoTarget.getConsecutivoProveedor());
			pojoResult.setTipoPersonaProveedor(pojoTarget.getTipoPersonaProveedor());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setNombreProveedor(pojoTarget.getNombreProveedor());
			pojoResult.setRfcProveedor(pojoTarget.getRfcProveedor());
			pojoResult.setNombreContacto(pojoTarget.getNombreContacto());
			pojoResult.setNombreComprador(pojoTarget.getNombreComprador());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setMoneda(pojoTarget.getMoneda());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setSistema(pojoTarget.getSistema());
			pojoResult.setCerrada(pojoTarget.getCerrada());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdObra(0L);
			pojoResult.setIdProveedor(0L);
			pojoResult.setIdContacto(0L);
			pojoResult.setIdComprador(0L);
			pojoResult.setIdRequisicion(0L);
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0L) {
				pojoResult.setIdObra(pojoTarget.getIdObra().getId());
				pojoResult.setIdSucursal(pojoTarget.getIdObra().getIdSucursal().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Proveedor");
			if (pojoTarget.getIdProveedor() != null && pojoTarget.getIdProveedor().getId() > 0L) {
				pojoResult.setIdProveedor(pojoTarget.getIdProveedor().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Proveedor terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Contacto");
			if (pojoTarget.getIdContacto() != null && pojoTarget.getIdContacto().getId() > 0L) {
				pojoResult.setIdContacto(pojoTarget.getIdContacto().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Contacto terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Comprador");
			if (pojoTarget.getIdComprador() != null && pojoTarget.getIdComprador().getId() > 0L) {
				pojoResult.setIdComprador(pojoTarget.getIdComprador().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Comprador terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Requisicion");
			if (pojoTarget.getIdRequisicion() != null && pojoTarget.getIdRequisicion().getId() != null && pojoTarget.getIdRequisicion().getId() > 0L) {
				pojoResult.setIdRequisicion(pojoTarget.getIdRequisicion().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Requisicion terminado");
			}
		} catch (Exception e) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir CotizacionExtToCotizacion :: " + e.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir CotizacionExtToCotizacion", e);
			throw e;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: CotizacionExtToCotizacion :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public CotizacionExt CotizacionToCotizacionExt(Cotizacion pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: CotizacionToCotizacionExt");
		CotizacionExt pojoResult = new CotizacionExt();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setFolio(pojoTarget.getFolio());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setFlete(pojoTarget.getFlete());
			pojoResult.setMargen(pojoTarget.getMargen());
			pojoResult.setTiempoEntrega(pojoTarget.getTiempoEntrega());
			pojoResult.setPorcentajeIva(pojoTarget.getPorcentajeIva());
			pojoResult.setSubtotal(pojoTarget.getSubtotal());
			pojoResult.setIva(pojoTarget.getIva());
			pojoResult.setTotal(pojoTarget.getTotal());
			pojoResult.setConsecutivoProveedor(pojoTarget.getConsecutivoProveedor());
			pojoResult.setTipoPersonaProveedor(pojoTarget.getTipoPersonaProveedor());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setNombreProveedor(pojoTarget.getNombreProveedor());
			pojoResult.setRfcProveedor(pojoTarget.getRfcProveedor());
			pojoResult.setNombreContacto(pojoTarget.getNombreContacto());
			pojoResult.setNombreComprador(pojoTarget.getNombreComprador());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setMoneda(pojoTarget.getMoneda());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setSistema(pojoTarget.getSistema());
			pojoResult.setCerrada(pojoTarget.getCerrada());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra() > 0L) {
				ObraExt pojoAux = this.ifzObras.findByIdExt(pojoTarget.getIdObra());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new ObraExt());
				pojoResult.setIdObra(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Proveedor");
			if (pojoTarget.getIdProveedor() != null && pojoTarget.getIdProveedor() > 0L) {
				if ("".equals(pojoTarget.getTipoPersonaProveedor()) || "P".equals(pojoTarget.getTipoPersonaProveedor())) {
					Persona pojoAux = this.ifzPersonas.findById(pojoTarget.getIdProveedor());
					pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new Persona());
					pojoResult.setIdProveedor(this.PersonaToPersonaExt(pojoAux));
				} else {
					Negocio pojoAux = this.ifzNegocios.findById(pojoTarget.getIdProveedor());
					pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new Negocio());
					pojoResult.setIdProveedor(this.NegocioToPersonaExt(pojoAux));
				}
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Proveedor terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Contacto");
			if (pojoTarget.getIdContacto() != null && pojoTarget.getIdContacto() > 0L) {
				Persona pojoAux = this.ifzPersonas.findById(pojoTarget.getIdContacto());
				pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new Persona());
				pojoResult.setIdContacto(this.PersonaToPersonaExt(pojoAux));
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Contacto terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Comprador");
			if (pojoTarget.getIdComprador() != null && pojoTarget.getIdComprador() > 0L) {
				EmpleadoExt pojoAux = this.ifzEmpleados.findByIdExt(pojoTarget.getIdComprador());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new EmpleadoExt());
				pojoResult.setIdComprador(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Comprador terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Requisicion");
			if (pojoTarget.getIdRequisicion() != null && pojoTarget.getIdRequisicion() > 0L) {
				RequisicionExt pojoAux = this.ifzRequisiciones.findExtById(pojoTarget.getIdRequisicion());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new RequisicionExt());
				pojoResult.setIdRequisicion(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Requisicion terminado");
			}
		} catch (Exception e) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir CotizacionToCotizacionExt :: " + e.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir CotizacionToCotizacionExt", e);
			throw e;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: CotizacionToCotizacionExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public CotizacionDetalle CotizacionDetalleExtToCotizacionDetalle(CotizacionDetalleExt pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: CotizacionDetalleExtToCotizacionDetalle");
		CotizacionDetalle pojoResult = new CotizacionDetalle();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCantidadInicial(pojoTarget.getCantidadInicial());
			pojoResult.setCantidadOrdenCompra(pojoTarget.getCantidadOrdenCompra());
			pojoResult.setCantidadSuministrada(pojoTarget.getCantidadSuministrada());
			pojoResult.setPrecioUnitario(pojoTarget.getPrecioUnitario());
			pojoResult.setImporte(pojoTarget.getImporte());
			pojoResult.setMargen(pojoTarget.getMargen());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setIdCotizacion(0L);
			pojoResult.setIdProducto(0L);
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cotizacion");
			if (pojoTarget.getIdCotizacion() != null && pojoTarget.getIdCotizacion().getId() != null && pojoTarget.getIdCotizacion().getId() > 0L) {
				pojoResult.setIdCotizacion(pojoTarget.getIdCotizacion().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cotizacion terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto");
			if (pojoTarget.getIdProducto() != null && pojoTarget.getIdProducto().getId() != null && pojoTarget.getIdProducto().getId() > 0L) {
				pojoResult.setIdProducto(pojoTarget.getIdProducto().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto terminado");
			}
		} catch (Exception e) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir CotizacionDetalleExtToCotizacionDetalle :: " + e.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir CotizacionDetalleExtToCotizacionDetalle", e);
			throw e;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: CotizacionDetalleExtToCotizacionDetalle :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public CotizacionDetalleExt CotizacionDetalleToCotizacionDetalleExt(CotizacionDetalle pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: CotizacionDetalleToCotizacionDetalleExt");
		CotizacionDetalleExt pojoResult = new CotizacionDetalleExt();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCantidadInicial(pojoTarget.getCantidadInicial());
			pojoResult.setCantidadOrdenCompra(pojoTarget.getCantidadOrdenCompra());
			pojoResult.setCantidadSuministrada(pojoTarget.getCantidadSuministrada());
			pojoResult.setPrecioUnitario(pojoTarget.getPrecioUnitario());
			pojoResult.setImporte(pojoTarget.getImporte());
			pojoResult.setMargen(pojoTarget.getMargen());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cotizacion");
			if (pojoTarget.getIdCotizacion() != null && pojoTarget.getIdCotizacion() > 0L) {
				CotizacionExt pojoAux = this.ifzCotizaciones.findExtById(pojoTarget.getIdCotizacion());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new CotizacionExt());
				pojoResult.setIdCotizacion(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cotizacion terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto");
			if (pojoTarget.getIdProducto() != null && pojoTarget.getIdProducto() != null && pojoTarget.getIdProducto() > 0L) {
				ProductoExt pojoAux = this.ifzProductos.findExtById(pojoTarget.getIdProducto());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new ProductoExt());
				pojoResult.setIdProducto(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto terminado");
			}
		} catch (Exception e) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir CotizacionDetalleToCotizacionDetalleExt :: " + e.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir CotizacionDetalleToCotizacionDetalleExt", e);
			throw e;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: CotizacionDetalleToCotizacionDetalleExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	// -------------------------------------------------------------------

	public OrdenCompra OrdenCompraExtToOrdenCompra(OrdenCompraExt pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " ::  OrdenCompraExtToOrdenCompra");
		OrdenCompra pojoResult = new OrdenCompra();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setFolio(pojoTarget.getFolio());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setAnticipo(pojoTarget.getAnticipo());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setMoneda(pojoTarget.getMoneda());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setPlazo(pojoTarget.getPlazo());
			pojoResult.setLugarEntrega(pojoTarget.getLugarEntrega());
			pojoResult.setTiempoEntrega(pojoTarget.getTiempoEntrega());
			pojoResult.setPorcentajeIva(pojoTarget.getPorcentajeIva());
			pojoResult.setSubtotal(pojoTarget.getSubtotal());
			pojoResult.setIva(pojoTarget.getIva());
			pojoResult.setTotal(pojoTarget.getTotal());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setNombreProveedor(pojoTarget.getNombreProveedor());
			pojoResult.setNombreSolicita(pojoTarget.getNombreSolicita());
			pojoResult.setNombreContacto(pojoTarget.getNombreContacto());
			pojoResult.setConsecutivoProveedor(pojoTarget.getConsecutivoProveedor());
			pojoResult.setTipoPersonaProveedor(pojoTarget.getTipoPersonaProveedor());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setFlete(pojoTarget.getFlete());
			pojoResult.setReferencia(pojoTarget.getReferencia());
			pojoResult.setAutorizado(pojoTarget.getAutorizado());
			pojoResult.setIdUsuarioAutorizo(pojoTarget.getIdUsuarioAutorizo());
			pojoResult.setFechaAutorizacion(pojoTarget.getFechaAutorizacion());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setBase(pojoTarget.getBase());
			pojoResult.setSistema(pojoTarget.getSistema());
			pojoResult.setCompleta(pojoTarget.getCompleta());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdObra(0L);
			pojoResult.setIdProveedor(0L);
			pojoResult.setIdSolicita(0L);
			pojoResult.setIdContacto(0L);
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cotizacion");
			if (pojoTarget.getIdCotizacion() != null && pojoTarget.getIdCotizacion().getId() != null && pojoTarget.getIdCotizacion().getId() > 0L) {
				pojoResult.setIdCotizacion(CotizacionExtToCotizacion(pojoTarget.getIdCotizacion()));
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cotizacion terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0L) {
				pojoResult.setIdObra(pojoTarget.getIdObra().getId());
				pojoResult.setIdSucursal(pojoTarget.getIdObra().getIdSucursal().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Proveedor");
			if (pojoTarget.getIdProveedor() != null && pojoTarget.getIdProveedor().getId() > 0L) {
				pojoResult.setIdProveedor(pojoTarget.getIdProveedor().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Proveedor terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Contacto");
			if (pojoTarget.getIdContacto() != null && pojoTarget.getIdContacto().getId() > 0L) {
				pojoResult.setIdContacto(pojoTarget.getIdContacto().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Contacto terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Solicita");
			if (pojoTarget.getIdSolicita() != null && pojoTarget.getIdSolicita().getId() > 0L) {
				pojoResult.setIdSolicita(pojoTarget.getIdSolicita().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Solicita terminado");
			}
			
			/*if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando LugarEntrega");
			if (pojoTarget.getLugarEntrega() != null && pojoTarget.getLugarEntrega().getId() > 0L) {
				pojoResult.setLugarEntrega(pojoTarget.getLugarEntrega().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando LugarEntrega terminado");
			}*/
		} catch (Exception ex) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraExtToOrdenCompra :: " + ex.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraExtToOrdenCompra", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: OrdenCompraExtToOrdenCompra :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public OrdenCompraExt OrdenCompraToOrdenCompraExt(OrdenCompra pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: OrdenCompraToOrdenCompraExt");
		OrdenCompraExt pojoResult = new OrdenCompraExt();
		boolean swNoCotizacion = true;
		
		try {
			if (pojoTarget == null)
				return null;
						
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setFolio(pojoTarget.getFolio());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setAnticipo(pojoTarget.getAnticipo());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setMoneda(pojoTarget.getMoneda());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setPlazo(pojoTarget.getPlazo());
			pojoResult.setLugarEntrega(pojoTarget.getLugarEntrega());
			pojoResult.setTiempoEntrega(pojoTarget.getTiempoEntrega());
			pojoResult.setPorcentajeIva(pojoTarget.getPorcentajeIva());
			pojoResult.setSubtotal(pojoTarget.getSubtotal());
			pojoResult.setIva(pojoTarget.getIva());
			pojoResult.setTotal(pojoTarget.getTotal());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setNombreProveedor(pojoTarget.getNombreProveedor());
			pojoResult.setNombreSolicita(pojoTarget.getNombreSolicita());
			pojoResult.setNombreContacto(pojoTarget.getNombreContacto());
			pojoResult.setConsecutivoProveedor(pojoTarget.getConsecutivoProveedor());
			pojoResult.setTipoPersonaProveedor(pojoTarget.getTipoPersonaProveedor());
			pojoResult.setAutorizado(pojoTarget.getAutorizado());
			pojoResult.setIdUsuarioAutorizo(pojoTarget.getIdUsuarioAutorizo());
			pojoResult.setFechaAutorizacion(pojoTarget.getFechaAutorizacion());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setFlete(pojoTarget.getFlete());
			pojoResult.setReferencia(pojoTarget.getReferencia());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setBase(pojoTarget.getBase());
			pojoResult.setSistema(pojoTarget.getSistema());
			pojoResult.setCompleta(pojoTarget.getCompleta());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cotizacion");
			if (pojoTarget.getIdCotizacion() != null && pojoTarget.getIdCotizacion().getId() != null && pojoTarget.getIdCotizacion().getId() > 0L) {
				swNoCotizacion = false;
				pojoResult.setIdCotizacion(CotizacionToCotizacionExt(pojoTarget.getIdCotizacion()));
				if (this.mostrarSystemOut) log.info("---- -- ----> ----------- Obra, Proveedor, Contacto from Cotizacion");
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cotizacion terminado");
			} 
			
			if (swNoCotizacion) {
				pojoResult.setIdCotizacion(new CotizacionExt());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cotizacion terminado");
				
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
				if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra() > 0L) {
					ObraExt pojoAux = this.ifzObras.findByIdExt(pojoTarget.getIdObra());
					pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new ObraExt());
					pojoResult.setIdObra(pojoAux);
					if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
				}
				
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Proveedor");
				if (pojoTarget.getIdProveedor() != null && pojoTarget.getIdProveedor() > 0L) {
					Persona pojoAux = this.ifzPersonas.findById(pojoTarget.getIdSolicita());
					pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new Persona());
					pojoResult.setIdProveedor(this.PersonaToPersonaExt(pojoAux));
					if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Proveedor terminado");
				}
				
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Contacto");
				if (pojoTarget.getIdContacto() != null && pojoTarget.getIdContacto() > 0L) {
					Persona pojoAux = this.ifzPersonas.findById(pojoTarget.getIdSolicita());
					pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new Persona());
					pojoResult.setIdContacto(this.PersonaToPersonaExt(pojoAux));
					if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Contacto terminado");
				}
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Solicita");
			if (pojoTarget.getIdSolicita() != null && pojoTarget.getIdSolicita() > 0L) {
				EmpleadoExt pojoAux = this.ifzEmpleados.findByIdExt(pojoTarget.getIdSolicita());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new EmpleadoExt());
				pojoResult.setIdSolicita(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Solicita terminado");
			}
		} catch (Exception ex) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraToOrdenCompraExt :: " + ex.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraToOrdenCompraExt", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: FacturaPagosExt To FacturaPagos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public OrdenCompraDetalle OrdenCompraDetalleExtToOrdenCompraDetalle(OrdenCompraDetalleExt pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: OrdenCompraDetalleExtToOrdenCompraDetalle");
		OrdenCompraDetalle pojoResult = new OrdenCompraDetalle();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCantidadSuministrada(pojoTarget.getCantidadSuministrada());
			pojoResult.setPrecioUnitario(pojoTarget.getPrecioUnitario());
			pojoResult.setImporte(pojoTarget.getImporte());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdOrdenCompra(0L);
			pojoResult.setIdProducto(0L);
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompra");
			if (pojoTarget.getIdOrdenCompra() != null && pojoTarget.getIdOrdenCompra().getId() != null && pojoTarget.getIdOrdenCompra().getId() > 0L) {
				pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompra terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto");
			if (pojoTarget.getIdProducto() != null && pojoTarget.getIdProducto().getId() != null && pojoTarget.getIdProducto().getId() > 0L) {
				pojoResult.setIdProducto(pojoTarget.getIdProducto().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto terminado");
			}
		} catch (Exception ex) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraDetalleExtToOrdenCompraDetalle :: " + ex.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraDetalleExtToOrdenCompraDetalle", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: OrdenCompraDetalleExtToOrdenCompraDetalle :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public OrdenCompraDetalleExt OrdenCompraDetalleToOrdenCompraDetalleExt(OrdenCompraDetalle pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: FacturaPagosExt To FacturaPagos");
		OrdenCompraDetalleExt pojoResult = new OrdenCompraDetalleExt();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCantidadSuministrada(pojoTarget.getCantidadSuministrada());
			pojoResult.setPrecioUnitario(pojoTarget.getPrecioUnitario());
			pojoResult.setImporte(pojoTarget.getImporte());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setComentariosCancelacion(pojoTarget.getComentariosCancelacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompra");
			if (pojoTarget.getIdOrdenCompra() != null && pojoTarget.getIdOrdenCompra() > 0L) {
				OrdenCompraExt pojoAux = this.ifzOrdenCompras.findExtById(pojoTarget.getIdOrdenCompra());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new OrdenCompraExt());
				pojoResult.setIdOrdenCompra(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompra terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto");
			if (pojoTarget.getIdProducto() != null && pojoTarget.getIdProducto() > 0L) {
				ProductoExt pojoAux = this.ifzProductos.findExtById(pojoTarget.getIdProducto());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new ProductoExt());
				pojoResult.setIdProducto(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto terminado");
			}
		} catch (Exception ex) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraDetalleToOrdenCompraDetalleExt :: " + ex.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraDetalleToOrdenCompraDetalleExt", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: FacturaPagosExt To FacturaPagos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public OrdenCompraImpuestos OrdenCompraImpuestosExtToOrdenCompraImpuestos(OrdenCompraImpuestosExt pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: OrdenCompraImpuestosExt To OrdenCompraImpuestos");
		OrdenCompraImpuestos pojoResult = new OrdenCompraImpuestos();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setPorcentaje(pojoTarget.getPorcentaje());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setAplicaEn(pojoTarget.getAplicaEn());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompraDetalle");
			if (pojoTarget.getIdOrdenCompra() != null && pojoTarget.getIdOrdenCompra().getId() != null && pojoTarget.getIdOrdenCompra().getId() > 0L) {
				pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompraDetalle terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Impuesto");
			if (pojoTarget.getIdImpuesto() != null && pojoTarget.getIdImpuesto().getId() > 0L) {
				pojoResult.setIdImpuesto(pojoTarget.getIdImpuesto().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Impuesto terminado");
			}
		} catch (Exception ex) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraImpuestosExt To OrdenCompraImpuestos :: " + ex.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraImpuestosExt To OrdenCompraImpuestos", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: OrdenCompraImpuestosExt To OrdenCompraImpuestos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public OrdenCompraImpuestosExt OrdenCompraImpuestosToOrdenCompraImpuestosExt(OrdenCompraImpuestos pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: OrdenCompraImpuestos To OrdenCompraImpuestosExt");
		OrdenCompraImpuestosExt pojoResult = new OrdenCompraImpuestosExt();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setPorcentaje(pojoTarget.getPorcentaje());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setAplicaEn(pojoTarget.getAplicaEn());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompraDetalle");
			if (pojoTarget.getIdOrdenCompra() > 0L) {
				OrdenCompraExt pojoAux = this.ifzOrdenCompras.findExtById(pojoTarget.getIdOrdenCompra());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new OrdenCompraExt());
				pojoResult.setIdOrdenCompra(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompraDetalle terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Impuesto");
			if (pojoTarget.getIdImpuesto() > 0L) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getIdImpuesto());
				pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new ConValores());
				pojoResult.setIdImpuesto(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Impuesto terminado");
			}
		} catch (Exception ex) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraImpuestos To OrdenCompraImpuestosExt :: " + ex.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraImpuestos To OrdenCompraImpuestosExt", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: OrdenCompraImpuestos To OrdenCompraImpuestosExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public OrdenCompraDetalleImpuestos OrdenCompraDetalleImpuestosExtToOrdenCompraDetalleImpuestos(OrdenCompraDetalleImpuestosExt pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: OrdenCompraDetalleImpuestosExt To OrdenCompraDetalleImpuestos");
		OrdenCompraDetalleImpuestos pojoResult = new OrdenCompraDetalleImpuestos();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setPorcentaje(pojoTarget.getPorcentaje());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompraDetalle");
			if (pojoTarget.getIdOrdenDetalle() != null && pojoTarget.getIdOrdenDetalle().getId() != null && pojoTarget.getIdOrdenDetalle().getId() > 0L) {
				pojoResult.setIdOrdenDetalle(pojoTarget.getIdOrdenDetalle().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompraDetalle terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Impuesto");
			if (pojoTarget.getIdImpuesto() != null && pojoTarget.getIdImpuesto().getId() > 0L) {
				pojoResult.setIdImpuesto(pojoTarget.getIdImpuesto().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Impuesto terminado");
			}
		} catch (Exception ex) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraDetalleImpuestosExt To OrdenCompraDetalleImpuestos :: " + ex.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraDetalleImpuestosExt To OrdenCompraDetalleImpuestos", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: OrdenCompraDetalleImpuestosExt To OrdenCompraDetalleImpuestos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public OrdenCompraDetalleImpuestosExt OrdenCompraDetalleImpuestosToOrdenCompraDetalleImpuestosExt(OrdenCompraDetalleImpuestos pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: OrdenCompraDetalleImpuestos To OrdenCompraDetalleImpuestosExt");
		OrdenCompraDetalleImpuestosExt pojoResult = new OrdenCompraDetalleImpuestosExt();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setPorcentaje(pojoTarget.getPorcentaje());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompraDetalle");
			if (pojoTarget.getIdOrdenDetalle() != null && pojoTarget.getIdOrdenDetalle() > 0L) {
				OrdenCompraDetalleExt pojoAux = this.ifzOrdenDetalle.findExtById(pojoTarget.getIdOrdenDetalle());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new OrdenCompraDetalleExt());
				pojoResult.setIdOrdenDetalle(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando OrdenCompraDetalle terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Impuesto");
			if (pojoTarget.getIdImpuesto() != null && pojoTarget.getIdImpuesto() > 0L) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getIdImpuesto());
				pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new ConValores());
				pojoResult.setIdImpuesto(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Impuesto terminado");
			}
		} catch (Exception ex) {
			log.info("Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraDetalleImpuestos To OrdenCompraDetalleImpuestosExt :: " + ex.getMessage());
			log.error("Error en Logica_Compras.ConvertExt :: ERROR al convertir OrdenCompraDetalleImpuestos To OrdenCompraDetalleImpuestosExt", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: OrdenCompraDetalleImpuestos To OrdenCompraDetalleImpuestosExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	// -------------------------------------------------------------------

	public Persona NegocioToPersona(Negocio pojoNegocio) {
		Persona pojoPersona = new Persona();
		
		try {
			pojoPersona.setId(pojoNegocio.getId());
			pojoPersona.setNombre(pojoNegocio.getNombre());
			pojoPersona.setRfc(pojoNegocio.getRfc());
			pojoPersona.setBanco(pojoNegocio.getBanco());
			pojoPersona.setClabeInterbancaria(pojoNegocio.getClabeInterbancaria());
			pojoPersona.setTipoPersona(0L);
		} catch (Exception e) {
			log.error("Logica_Compras.ConvertExt :: ERROR al convertir Negocio To Persona", e);
			throw e;
		}
		
		return pojoPersona;
	}
	
	public PersonaExt PersonaToPersonaExt(Persona pojoTarget) throws Exception {
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: From " + this.from + " :: PersonaToPersonaExt");
		
		PersonaExt pojoResult = new PersonaExt();
		pojoResult.setId(pojoTarget.getId());
		pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
		pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
		pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
		pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
		pojoResult.setHomonimo((pojoTarget.getHomonimo() > 0));
		pojoResult.setNombre(pojoTarget.getNombre());
		pojoResult.setPrimerNombre(pojoTarget.getPrimerNombre());
		pojoResult.setSegundoNombre(pojoTarget.getSegundoNombre());
		pojoResult.setNombresPropios(pojoTarget.getNombresPropios());
		pojoResult.setPrimerApellido(pojoTarget.getPrimerApellido());
		pojoResult.setSegundoApellido(pojoTarget.getSegundoApellido());
		pojoResult.setRfc(pojoTarget.getRfc());
		pojoResult.setSexo(pojoTarget.getSexo());
		pojoResult.setFechaNacimiento(pojoTarget.getFechaNacimiento());
		pojoResult.setNumeroHijos(pojoTarget.getNumeroHijos());
		pojoResult.setDomicilio(pojoTarget.getDomicilio());
		pojoResult.setTelefono(pojoTarget.getTelefono());
		pojoResult.setCorreo(pojoTarget.getCorreo());
		if (pojoTarget.getEstadoCivil() != null)
			pojoResult.setEstadoCivil(ifzConValores.findById(pojoTarget.getEstadoCivil()));
		if (pojoTarget.getLocalidad() != null)
			pojoResult.setLocalidad(ifzLocalidad.findById(pojoTarget.getLocalidad()));
		if (pojoTarget.getNacionalidad () != null)
			pojoResult.setNacionalidad(ifzConValores.findById(pojoTarget.getNacionalidad()));
		if (pojoTarget.getConyuge() != null)
			pojoResult.setConyuge(PersonaToPersonaExt(pojoTarget.getConyuge()));
		pojoResult.setFinado((pojoTarget.getFinado() > 0));
		if (pojoTarget.getTipoSangre() != null)
			pojoResult.setTipoSangre(ifzConValores.findById(pojoTarget.getTipoSangre()));
		if (pojoTarget.getColonia() != null)
			pojoResult.setColonia(ifzColonia.findById(pojoTarget.getColonia()));
		pojoResult.setTipoPersona(pojoTarget.getTipoPersona());
		
		String res = pojoTarget.getPrimerNombre() != null && pojoTarget.getPrimerNombre().length() > 0 ? pojoTarget.getPrimerNombre() : "";
		res += pojoTarget.getSegundoNombre()	!= null && pojoTarget.getSegundoNombre().length() > 0 ? " " + pojoTarget.getSegundoNombre() : "";
		res += pojoTarget.getNombresPropios()	!= null && pojoTarget.getNombresPropios().length() > 0 ? " " + pojoTarget.getNombresPropios() : "";
		res += pojoTarget.getPrimerApellido()	!= null && pojoTarget.getPrimerApellido().length() > 0 ? " " + pojoTarget.getPrimerApellido() : "";
		res += pojoTarget.getSegundoApellido()	!= null && pojoTarget.getSegundoApellido().length() > 0 ? " " + pojoTarget.getSegundoApellido() : "";
		
		pojoResult.setNombreCompleto(res);
		pojoResult.setNumeroCuenta(pojoTarget.getNumeroCuenta());
		pojoResult.setClabeInterbancaria(pojoTarget.getClabeInterbancaria());
		
		if (this.mostrarSystemOut)
			log.info("Logica_Compras.ConvertExt :: Persona To PersonaExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public PersonaExt NegocioToPersonaExt(Negocio pojoNegocio) throws Exception {
		try {
			return this.PersonaToPersonaExt(NegocioToPersona(pojoNegocio));
		} catch (Exception e) {
			log.error("Logica_Compras.ConvertExt :: ERROR al convertir NegocioToPersonaExt", e);
			throw e;
		}
	}
}

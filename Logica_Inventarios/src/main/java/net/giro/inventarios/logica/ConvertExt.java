package net.giro.inventarios.logica;

import java.io.Serializable;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.dao.ObraDAO;
import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.logica.ClientesRem;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.dao.SucursalDAO;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenExt;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.beans.TraspasoBodegaBodega;
import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.dao.ProductoDAO;
import net.giro.inventarios.dao.TraspasoBodegaBodegaDAO;

public class ConvertExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ConvertExt.class);
	private AlmacenMovimientosRem ifzMovimientos;
	private ObraDAO ifzObras;
	private TraspasoBodegaBodegaDAO ifzTraspasos;
	private ConValoresDAO ifzConValores;
	private ProductoDAO ifzProductos;
	private SucursalDAO ifzSucursal;
	private ClientesRem ifzClientes;
	private EmpleadoRem ifzEmpleado;
	private MonedaDAO ifzMonedas;
	
	public ConvertExt() {
		Hashtable<String, Object> environtment = null;
		InitialContext ctx = null;
		
		try {
			environtment = new Hashtable<String, Object>();
            environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(environtment);
    		this.ifzMovimientos = (AlmacenMovimientosRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
    		this.ifzEmpleado = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
    		this.ifzClientes = (ClientesRem) ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			this.ifzConValores= (ConValoresDAO) ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
            this.ifzSucursal = (SucursalDAO) ctx.lookup("ejb:/Model_Publico//SucursalImp!net.giro.ne.dao.SucursalDAO");
            this.ifzProductos = (ProductoDAO) ctx.lookup("ejb:/Model_Inventarios//ProductoImp!net.giro.inventarios.dao.ProductoDAO");
    		this.ifzTraspasos = (TraspasoBodegaBodegaDAO) ctx.lookup("ejb:/Model_Inventarios//TraspasoBodegaBodegaImp!net.giro.inventarios.dao.TraspasoBodegaBodegaDAO");
    		this.ifzObras = (ObraDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraImp!net.giro.adp.dao.ObraDAO");
    		this.ifzMonedas = (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear ConvertExt", e);
			ctx = null;
		}
	}
	
	// AlmacenTraspaso
	// ----------------------------------------------------------------------------------------------------
	public AlmacenTraspaso getPojo(AlmacenTraspasoExt extendido) {
		AlmacenTraspaso pojo = new AlmacenTraspaso();
		
		try {
			if (extendido == null)
				return pojo;
			pojo.setId(extendido.getId());
			pojo.setTipo(extendido.getTipo());
			pojo.setFecha(extendido.getFecha());
			pojo.setEntregaNombre(extendido.getEntregaNombre());
			pojo.setRecibeNombre(extendido.getRecibeNombre());
			pojo.setIdObra(extendido.getIdObra());
			pojo.setNombreObra(extendido.getNombreObra());
			pojo.setIdTraspaso(extendido.getIdTraspaso());
			pojo.setSolicitudCotizacion(extendido.getSolicitudCotizacion());
			pojo.setSolicitudRequisicion(extendido.getSolicitudRequisicion());
			pojo.setCompleto(extendido.getCompleto());
			pojo.setOwner(extendido.getOwner());
			pojo.setSistema(extendido.getSistema());
			pojo.setIdEmpresa(extendido.getIdEmpresa());
			pojo.setEstatus(extendido.getEstatus());
			pojo.setCreadoPor(extendido.getCreadoPor());
			pojo.setFechaCreacion(extendido.getFechaCreacion());
			pojo.setModificadoPor(extendido.getModificadoPor());
			pojo.setFechaModificacion(extendido.getFechaModificacion());
			pojo.setFolio(extendido.getFolio());
			pojo.setConsecutivo(extendido.getConsecutivo());
			pojo.setRecibido(extendido.getRecibido());
			pojo.setRecibidoPor(extendido.getRecibidoPor());
			pojo.setFechaRecibido(extendido.getFechaRecibido());
			pojo.setIdAlmacenOrigen(null);
			pojo.setIdAlmacenDestino(null);
			pojo.setEntrega(0);
			pojo.setRecibe(0);
			
			if (extendido.getAlmacenOrigen() != null && extendido.getAlmacenOrigen().getId() != null && extendido.getAlmacenOrigen().getId() > 0L)
				pojo.setIdAlmacenOrigen(extendido.getAlmacenOrigen());

			if (extendido.getAlmacenDestino() != null && extendido.getAlmacenDestino().getId() != null && extendido.getAlmacenDestino().getId() > 0L)
				pojo.setIdAlmacenDestino(extendido.getAlmacenDestino());
			
			if (extendido.getEntrega() != null && extendido.getEntrega().getId() != null && extendido.getEntrega().getId() > 0L)
				pojo.setEntrega(extendido.getEntrega().getId());
			
			if (extendido.getRecibe() != null && extendido.getRecibe().getId() != null && extendido.getRecibe().getId() > 0L)
				pojo.setRecibe(extendido.getRecibe().getId());
		} catch (Exception e) {
			throw e;
		}
		
		return pojo;
	}
	
	public AlmacenTraspasoExt getExtendido(AlmacenTraspaso pojo) {
		AlmacenTraspasoExt extendido = new AlmacenTraspasoExt();
		TraspasoBodegaBodega traspasoOrigenes = null;
		Obra obra = null;
		
		try {
			if (pojo == null)
				return extendido;
			extendido.setId(pojo.getId());
			extendido.setTipo(pojo.getTipo());
			extendido.setFecha(pojo.getFecha());
			extendido.setEntregaNombre(pojo.getEntregaNombre());
			extendido.setIdObra(pojo.getIdObra());
			extendido.setNombreObra(pojo.getNombreObra());
			extendido.setRecibeNombre(pojo.getRecibeNombre());
			extendido.setIdTraspaso(pojo.getIdTraspaso());
			extendido.setSolicitudCotizacion(pojo.getSolicitudCotizacion());
			extendido.setSolicitudRequisicion(pojo.getSolicitudRequisicion());
			extendido.setCompleto(pojo.getCompleto());
			extendido.setOwner(pojo.getOwner());
			extendido.setSistema(pojo.getSistema());
			extendido.setIdEmpresa(pojo.getIdEmpresa());
			extendido.setEstatus(pojo.getEstatus());
			extendido.setCreadoPor(pojo.getCreadoPor());
			extendido.setFechaCreacion(pojo.getFechaCreacion());
			extendido.setModificadoPor(pojo.getModificadoPor());
			extendido.setFechaModificacion(pojo.getFechaModificacion());
			extendido.setFolio(pojo.getFolio());
			extendido.setConsecutivo(pojo.getConsecutivo());
			extendido.setRecibido(pojo.getRecibido());
			extendido.setRecibidoPor(pojo.getRecibidoPor());
			extendido.setFechaRecibido(pojo.getFechaRecibido());
			
			if (pojo.getIdAlmacenOrigen() != null && pojo.getIdAlmacenOrigen().getId() != null && pojo.getIdAlmacenOrigen().getId() > 0L)
				extendido.setAlmacenOrigen(pojo.getIdAlmacenOrigen());
			
			if (pojo.getIdAlmacenDestino() != null && pojo.getIdAlmacenDestino().getId() != null && pojo.getIdAlmacenDestino().getId() > 0L)
				extendido.setAlmacenDestino(pojo.getIdAlmacenDestino());

			if (pojo.getEntrega() > 0)
				extendido.setEntrega(this.ifzEmpleado.findById(pojo.getEntrega()));
			
			if (pojo.getRecibe() > 0)
				extendido.setRecibe(this.ifzEmpleado.findById(pojo.getRecibe()));
			
			// Añado oriden y destino de obras si corresponde
			traspasoOrigenes = this.ifzTraspasos.findByTraspaso(pojo.getId());
			if (traspasoOrigenes != null && traspasoOrigenes.getId() != null && traspasoOrigenes.getId() > 0L) {
				extendido.setOrigenDestino(traspasoOrigenes);
				obra = this.ifzObras.findById(traspasoOrigenes.getIdObraOrigen());
				extendido.setObraOrigen(obra);
				obra = this.ifzObras.findById(traspasoOrigenes.getIdObraDestino());
				extendido.setObraDestino(obra);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return extendido;
	}
	
	public TraspasoDetalle getPojo(TraspasoDetalleExt extendido) {
		TraspasoDetalle pojo = new TraspasoDetalle();
		
		try {
			if (extendido == null)
				return pojo;
			pojo.setId(extendido.getId());
			pojo.setIdAlmacenTraspaso(extendido.getIdAlmacenTraspaso());
			pojo.setIdProducto(0L);
			pojo.setCantidad(extendido.getCantidad());
			pojo.setPrecioUnitario(extendido.getPrecioUnitario());
			pojo.setCantidadRecibida(extendido.getCantidadRecibida());
			pojo.setIdProductoPrevio(extendido.getIdProductoPrevio());
			pojo.setEstatus(extendido.getEstatus());
			pojo.setCreadoPor(extendido.getCreadoPor());
			pojo.setFechaCreacion(extendido.getFechaCreacion());
			pojo.setModificadoPor(extendido.getModificadoPor());
			pojo.setFechaModificacion(extendido.getFechaModificacion());

			if (extendido.getIdProducto() != null && extendido.getIdProducto().getId() != null && extendido.getIdProducto().getId() > 0L)
				pojo.setIdProducto(extendido.getIdProducto().getId());
		} catch (Exception e) {
			throw e;
		}
		
		return pojo;
	}

	public TraspasoDetalleExt getExtendido(TraspasoDetalle pojo) {
		TraspasoDetalleExt extendido = new TraspasoDetalleExt(); 
		
		try {
			if (pojo == null)
				return extendido;
			extendido.setId(pojo.getId());
			extendido.setIdAlmacenTraspaso(pojo.getIdAlmacenTraspaso());
			extendido.setCantidad(pojo.getCantidad());
			extendido.setPrecioUnitario(pojo.getPrecioUnitario());
			extendido.setCantidadRecibida(pojo.getCantidadRecibida());
			extendido.setIdProductoPrevio(pojo.getIdProductoPrevio());
			extendido.setEstatus(pojo.getEstatus());
			extendido.setCreadoPor(pojo.getCreadoPor());
			extendido.setFechaCreacion(pojo.getFechaCreacion());
			extendido.setModificadoPor(pojo.getModificadoPor());
			extendido.setFechaModificacion(pojo.getFechaModificacion());
			
			if (pojo.getIdProducto() > 0) {
				ProductoExt pojoAux = this.getExtendido(this.ifzProductos.findById(pojo.getIdProducto()));
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L) ? pojoAux: new ProductoExt();
				extendido.setIdProducto(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return extendido;
	}
	
	// AlmacenMovimientos
	// ----------------------------------------------------------------------------------------------------
	public AlmacenMovimientos getPojo(AlmacenMovimientosExt extendido) {
		AlmacenMovimientos pojo = new AlmacenMovimientos();
		
		try {
			if (extendido == null)
				return pojo;
			pojo.setId(extendido.getId());
			pojo.setIdAlmacen(extendido.getAlmacen());
			pojo.setTipo(extendido.getTipo());
			pojo.setTipoEntrada(extendido.getTipoEntrada());
			pojo.setFecha(extendido.getFecha());
			pojo.setEntregaNombre(extendido.getEntregaNombre());
			pojo.setRecibeNombre(extendido.getRecibeNombre());
			pojo.setIdObra(extendido.getIdObra());
			pojo.setNombreObra(extendido.getNombreObra());
			pojo.setIdOrdenCompra(extendido.getIdOrdenCompra());
			pojo.setFolioOrdenCompra(extendido.getFolioOrdenCompra());
			pojo.setIdTraspaso(extendido.getIdTraspaso());
			pojo.setOwner(extendido.getOwner());
			pojo.setSistema(extendido.getSistema());
			pojo.setIdEmpresa(extendido.getIdEmpresa());
			pojo.setEstatus(extendido.getEstatus());
			pojo.setCreadoPor(extendido.getCreadoPor());
			pojo.setFechaCreacion(extendido.getFechaCreacion());
			pojo.setModificadoPor(extendido.getModificadoPor());
			pojo.setFechaModificacion(extendido.getFechaModificacion());
			pojo.setFolio(extendido.getFolio());
			pojo.setConsecutivo(extendido.getConsecutivo());
			pojo.setIdSalida(0L);
			
			if (extendido.getFolioFactura() != null)
				pojo.setFolioFactura(extendido.getFolioFactura());
			
			if (extendido.getEntrega() != null)
				pojo.setEntrega(extendido.getEntrega().getId());
			
			if (extendido.getRecibe() != null)
				pojo.setRecibe(extendido.getRecibe().getId());
			
			if (extendido.getIdSalida() != null)
				pojo.setIdSalida(extendido.getIdSalida().getId());
		} catch (Exception e) {
			throw e;
		}
		
		return pojo;
	}
	
	public AlmacenMovimientosExt getExtendido(AlmacenMovimientos pojo) {
		AlmacenMovimientosExt extendido = new AlmacenMovimientosExt();
		
		try {
			if (pojo == null)
				return extendido;
			extendido.setId(pojo.getId());
			extendido.setAlmacen(pojo.getIdAlmacen());
			extendido.setTipo(pojo.getTipo());
			extendido.setTipoEntrada(pojo.getTipoEntrada());
			extendido.setFecha(pojo.getFecha());
			extendido.setIdOrdenCompra(pojo.getIdOrdenCompra());
			extendido.setFolioOrdenCompra(pojo.getFolioOrdenCompra());
			extendido.setIdTraspaso(pojo.getIdTraspaso());
			extendido.setIdObra(pojo.getIdObra());
			extendido.setNombreObra(pojo.getNombreObra());
			extendido.setEntregaNombre(pojo.getEntregaNombre());
			extendido.setRecibeNombre(pojo.getRecibeNombre());
			extendido.setOwner(pojo.getOwner());
			extendido.setSistema(pojo.getSistema());
			extendido.setIdEmpresa(pojo.getIdEmpresa());
			extendido.setEstatus(pojo.getEstatus());
			extendido.setCreadoPor(pojo.getCreadoPor());
			extendido.setFechaCreacion(pojo.getFechaCreacion());
			extendido.setModificadoPor(pojo.getModificadoPor());
			extendido.setFechaModificacion(pojo.getFechaModificacion());
			extendido.setFolio(pojo.getFolio());
			extendido.setConsecutivo(pojo.getConsecutivo());
			
			if (pojo.getFolioFactura() != null)
				extendido.setFolioFactura(pojo.getFolioFactura());
			
			/*if (pojoTarget.getIdAlmacen() != null && pojoTarget.getIdAlmacen().getId() != null && pojoTarget.getIdAlmacen().getId() > 0L) {
				Almacen pojoAux = this.ifzAlmacen.findById(pojoTarget.getIdAlmacen());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L) ? pojoAux: new Almacen();
				pojoResult.setAlmacen(pojoAux);
			}*/

			if (pojo.getEntrega() > 0) {
				Empleado pojoAux = this.ifzEmpleado.findById(pojo.getEntrega());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L) ? pojoAux: new Empleado();
				extendido.setEntrega(pojoAux);
			}
			
			if (pojo.getRecibe() > 0) {
				Empleado pojoAux = this.ifzEmpleado.findById(pojo.getRecibe());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L) ? pojoAux: new Empleado();
				extendido.setRecibe(pojoAux);
			}
			
			if (pojo.getIdSalida() > 0) {
				AlmacenMovimientosExt pojoAux = this.ifzMovimientos.findByIdExt(pojo.getIdSalida());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L) ? pojoAux: new AlmacenMovimientosExt();
				extendido.setIdSalida(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return extendido;
	}
	
	public MovimientosDetalle getPojo(MovimientosDetalleExt pojo) {
		MovimientosDetalle extendido = new MovimientosDetalle();
		
		try {
			if (pojo == null)
				return extendido;
			extendido.setId(pojo.getId());
			extendido.setIdAlmacenMovimiento(pojo.getIdAlmacenMovimiento());
			extendido.setIdProducto(pojo.getProducto().getId());
			extendido.setCantidad(pojo.getCantidad());
			extendido.setPrecioUnitario(pojo.getPrecioUnitario());
			extendido.setCantidadSolicitada(pojo.getCantidadSolicitada());
			extendido.setEjecutado(pojo.getEjecutado());
			extendido.setEstatus(pojo.getEstatus());
			extendido.setCreadoPor(pojo.getCreadoPor());
			extendido.setFechaCreacion(pojo.getFechaCreacion());
			extendido.setModificadoPor(pojo.getModificadoPor());
			extendido.setFechaModificacion(pojo.getFechaModificacion());
		} catch (Exception e) {
			throw e;
		}
		
		return extendido;
	}

	public MovimientosDetalleExt getExtendido(MovimientosDetalle pojo) {
		MovimientosDetalleExt extendido = new MovimientosDetalleExt();
		
		try {
			if (pojo == null)
				return extendido;
			extendido.setId(pojo.getId());
			extendido.setIdAlmacenMovimiento(pojo.getIdAlmacenMovimiento());
			extendido.setCantidad(pojo.getCantidad());
			extendido.setPrecioUnitario(pojo.getPrecioUnitario());
			extendido.setCantidadSolicitada(pojo.getCantidadSolicitada());
			extendido.setEjecutado(pojo.getEjecutado());
			extendido.setEstatus(pojo.getEstatus());
			extendido.setCreadoPor(pojo.getCreadoPor());
			extendido.setFechaCreacion(pojo.getFechaCreacion());
			extendido.setModificadoPor(pojo.getModificadoPor());
			extendido.setFechaModificacion(pojo.getFechaModificacion());
			
			if (pojo.getIdProducto() > 0) {
				ProductoExt pojoAux = this.getExtendido(this.ifzProductos.findById(pojo.getIdProducto()));
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L) ? pojoAux: new ProductoExt();
				extendido.setProducto(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return extendido;
	}
	
	// Almacen
	// ----------------------------------------------------------------------------------------------------
	public Almacen getPojo(AlmacenExt extendido) {
		Almacen pojo = new Almacen();
		
		try {
			if (extendido == null)
				return pojo;
			pojo.setId(extendido.getId());
			pojo.setNombre(extendido.getNombre());
			pojo.setIdentificador(extendido.getIdentificador());
			pojo.setTipo(extendido.getTipo());
			pojo.setIdSucursal(extendido.getSucursal().getId());
			pojo.setNombreSucursal(extendido.getNombreSucursal());
			pojo.setNombreEncargado(extendido.getNombreEncargado());
			pojo.setDomicilio(extendido.getDomicilio());
			pojo.setPermitirSBO(extendido.getPermitirSBO());
			pojo.setIdEmpresa(extendido.getIdEmpresa());
			pojo.setEstatus(extendido.getEstatus());
			pojo.setCreadoPor(extendido.getCreadoPor());
			pojo.setFechaCreacion(extendido.getFechaCreacion());
			pojo.setModificadoPor(extendido.getModificadoPor());
			pojo.setFechaModificacion(extendido.getFechaModificacion());
			
			if (extendido.getPojoDomicilio() != null && extendido.getPojoDomicilio().getId() > 0)
				pojo.setIdDomicilio(extendido.getPojoDomicilio().getId());
			
			if (extendido.getIdEncargado() != null && extendido.getIdEncargado().getId() != null && extendido.getIdEncargado().getId() > 0L)
				pojo.setIdEncargado(extendido.getIdEncargado().getId());
		} catch (Exception e) {
			throw e;
		}
		
		return pojo;
	}
	
	public AlmacenExt getExtendido(Almacen pojo) throws Exception {
		AlmacenExt extendido = new AlmacenExt();
		
		try {
			if (pojo == null)
				return extendido;
			extendido.setId(pojo.getId());
			extendido.setNombre(pojo.getNombre());
			extendido.setIdentificador(pojo.getIdentificador());
			extendido.setTipo(pojo.getTipo());
			extendido.setNombreSucursal(pojo.getNombreSucursal());
			extendido.setNombreEncargado(pojo.getNombreEncargado());
			extendido.setDomicilio(pojo.getDomicilio());
			extendido.setPermitirSBO(pojo.getPermitirSBO());
			extendido.setIdEmpresa(pojo.getIdEmpresa());
			extendido.setEstatus(pojo.getEstatus());
			extendido.setCreadoPor(pojo.getCreadoPor());
			extendido.setFechaCreacion(pojo.getFechaCreacion());
			extendido.setModificadoPor(pojo.getModificadoPor());
			extendido.setFechaModificacion(pojo.getFechaModificacion());
			
			if (pojo.getIdDomicilio() > 0) {
				DomicilioExt pojoAux = this.ifzClientes.buscarDomicilioExt(pojo.getIdDomicilio());
				pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux: new DomicilioExt());
				extendido.setPojoDomicilio(pojoAux);
			}
			
			if (pojo.getIdSucursal() > 0) {
				Sucursal pojoAux = this.ifzSucursal.findById(pojo.getIdSucursal());
				pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux: new Sucursal());
				extendido.setSucursal(pojoAux);
			}
			
			if (pojo.getIdEncargado() > 0L) {
				Empleado pojoAux = this.ifzEmpleado.findById(pojo.getIdEncargado());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux: new Empleado());
				extendido.setIdEncargado(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return extendido;
	}
	
	// Producto	
	// ----------------------------------------------------------------------------------------------------
	public Producto getPojo(ProductoExt extendido) {
		Producto pojo = new Producto();
		
		try {
			if (extendido == null)
				return pojo;
			pojo.setId(extendido.getId());
			pojo.setClave(extendido.getClave());
			pojo.setClavePrevia(extendido.getClavePrevia());
			pojo.setDescripcion(extendido.getDescripcion());
			pojo.setPrecioCompra(extendido.getPrecioCompra());
			pojo.setPrecioVenta(extendido.getPrecioVenta());
			pojo.setPrecioUnitario(extendido.getPrecioUnitario());
			pojo.setPrecioCompraPesos(extendido.getPrecioCompraPesos());
			pojo.setTipoCambio(extendido.getTipoCambio());
			pojo.setExistencia(extendido.getExistencia());
			pojo.setMaximo(extendido.getMaximo());
			pojo.setMinimo(extendido.getMinimo());
			pojo.setPermiteExcedente(extendido.getPermiteExcedente());
			pojo.setOrigenCodigo(extendido.getOrigenCodigo());
			pojo.setEstatus(extendido.getEstatus());
			pojo.setCreadoPor(extendido.getCreadoPor());
			pojo.setFechaCreacion(extendido.getFechaCreacion());
			pojo.setModificadoPor(extendido.getModificadoPor());
			pojo.setFechaModificacion(extendido.getFechaModificacion());
			pojo.setDescEspecialidad(extendido.getDescEspecialidad());
			pojo.setDescFamilia(extendido.getDescFamilia());
			pojo.setDescSubfamilia(extendido.getDescSubfamilia());
			pojo.setDescUnidadMedida(extendido.getDescUnidadMedida());
			pojo.setDescMoneda(extendido.getDescMoneda());
			pojo.setDescMonedaAbreviatura(extendido.getDescMonedaAbreviatura());
			pojo.setTipoInsumo(extendido.getTipoInsumo());
			pojo.setTipo(extendido.getTipo());
			pojo.setOculto(extendido.getOculto());
			pojo.setClaveSat(extendido.getClaveSat());
			pojo.setIdEmpresa(extendido.getIdEmpresa());
			pojo.setIdOrdenCompra(extendido.getIdOrdenCompra());
			pojo.setOrdenCompra(extendido.getOrdenCompra());
			pojo.setUnidadMedida(0L);
			pojo.setEspecialidad(0L);
			pojo.setFamilia(0L);
			pojo.setSubfamilia(0L);
			pojo.setIdMoneda(0L);

			if (extendido.getUnidadMedida() != null && extendido.getUnidadMedida().getId() > 0L)
				pojo.setUnidadMedida(extendido.getUnidadMedida().getId());

			if (extendido.getEspecialidad() != null && extendido.getEspecialidad().getId() > 0L)
				pojo.setEspecialidad(extendido.getEspecialidad().getId());

			if (extendido.getFamilia() != null && extendido.getFamilia().getId() > 0L)
				pojo.setFamilia(extendido.getFamilia().getId());

			if (extendido.getSubfamilia() != null && extendido.getSubfamilia().getId() > 0L)
				pojo.setSubfamilia(extendido.getSubfamilia().getId());

			if (extendido.getIdMoneda() != null && extendido.getIdMoneda().getId() > 0L)
				pojo.setIdMoneda(extendido.getIdMoneda().getId()); 
		} catch (Exception e) {
			throw e;
		}
		
		return pojo;
	}
	
	public ProductoExt getExtendido(Producto pojo) {
		ProductoExt extendido = new ProductoExt();

		try {
			if (pojo == null)	//si la busqueda regresa null, regresar el pojo recien creado, una vez ocurre esto, en la vista se decide si agregarlo o no
				return extendido;
			extendido.setId(pojo.getId());
			extendido.setClave(pojo.getClave());
			extendido.setClavePrevia(pojo.getClavePrevia());
			extendido.setDescripcion(pojo.getDescripcion());
			extendido.setPrecioCompra(pojo.getPrecioCompra());
			extendido.setPrecioVenta(pojo.getPrecioVenta());
			extendido.setPrecioUnitario(pojo.getPrecioUnitario());
			extendido.setPrecioCompraPesos(pojo.getPrecioCompraPesos());
			extendido.setTipoCambio(pojo.getTipoCambio());
			extendido.setExistencia(pojo.getExistencia());
			extendido.setMaximo(pojo.getMaximo());
			extendido.setMinimo(pojo.getMinimo());
			extendido.setPermiteExcedente(pojo.getPermiteExcedente());
			extendido.setOrigenCodigo(pojo.getOrigenCodigo());
			extendido.setEstatus(pojo.getEstatus());
			extendido.setCreadoPor(pojo.getCreadoPor());
			extendido.setFechaCreacion(pojo.getFechaCreacion());
			extendido.setModificadoPor(pojo.getModificadoPor());
			extendido.setFechaModificacion(pojo.getFechaModificacion());
			extendido.setDescEspecialidad(pojo.getDescEspecialidad());
			extendido.setDescFamilia(pojo.getDescFamilia());
			extendido.setDescSubfamilia(pojo.getDescSubfamilia());
			extendido.setDescUnidadMedida(pojo.getDescUnidadMedida());
			extendido.setDescMoneda(pojo.getDescMoneda());
			extendido.setDescMonedaAbreviatura(pojo.getDescMonedaAbreviatura());
			extendido.setTipoInsumo(pojo.getTipoInsumo());
			extendido.setTipo(pojo.getTipo());
			extendido.setOculto(pojo.getOculto());
			extendido.setClaveSat(pojo.getClaveSat());
			extendido.setIdEmpresa(pojo.getIdEmpresa());
			extendido.setIdOrdenCompra(pojo.getIdOrdenCompra());
			extendido.setOrdenCompra(pojo.getOrdenCompra());
			
			if (pojo.getUnidadMedida() > 0) {
				ConValores pojoAux = this.ifzConValores.findById(pojo.getUnidadMedida());
				pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new ConValores());
				extendido.setUnidadMedida(pojoAux);
			}

			if (pojo.getEspecialidad() > 0) {
				ConValores pojoAux = this.ifzConValores.findById(pojo.getEspecialidad());
				pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new ConValores());
				extendido.setEspecialidad(pojoAux);
			}
			
			if (pojo.getFamilia() > 0) {
				ConValores pojoAux = this.ifzConValores.findById(pojo.getFamilia());
				pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new ConValores());
				extendido.setFamilia(pojoAux);
			}
			
			if (pojo.getSubfamilia() > 0) {
				ConValores pojoAux = this.ifzConValores.findById(pojo.getFamilia());
				pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new ConValores());
				extendido.setSubfamilia(pojoAux);
			}
			
			if (pojo.getIdMoneda() > 0) {
				Moneda pojoAux = this.ifzMonedas.findById(pojo.getIdMoneda());
				pojoAux = (pojoAux != null && pojoAux.getId() > 0L ? pojoAux : new Moneda());
				extendido.setIdMoneda(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return extendido;
	}
	
	// AlmacenProducto
	// ----------------------------------------------------------------------------------------------------
	public AlmacenProducto getPojo(AlmacenProductoExt extendido) {
		AlmacenProducto pojo = new AlmacenProducto();
		
		try {
			if (extendido == null)
				return pojo;
			pojo.setId(extendido.getId());
			pojo.setIdAlmacen(extendido.getAlmacen());
			pojo.setIdProducto(extendido.getProducto().getId());
			pojo.setPrecioUnitario(extendido.getPrecioUnitario());
			pojo.setExistencia(extendido.getExistencia());
			pojo.setSolicitud(extendido.getSolicitud());
			pojo.setIdEmpresa(extendido.getIdEmpresa());
			pojo.setEstatus(extendido.getEstatus());
			pojo.setCreadoPor(extendido.getCreadoPor());
			pojo.setFechaCreacion(extendido.getFechaCreacion());
			pojo.setModificadoPor(extendido.getModificadoPor());
			pojo.setFechaModificacion(extendido.getFechaModificacion());
		} catch (Exception e) {
			throw e;
		}
		
		return pojo;
	}
	
	public AlmacenProductoExt getExtendido(AlmacenProducto pojo) {
		AlmacenProductoExt extendido = new AlmacenProductoExt();
		
		try {
			if (pojo == null)
				return extendido;
			extendido.setId(pojo.getId());
			extendido.setAlmacen(pojo.getIdAlmacen());
			extendido.setPrecioUnitario(pojo.getPrecioUnitario());
			extendido.setExistencia(pojo.getExistencia());
			extendido.setSolicitud(pojo.getSolicitud());
			extendido.setIdEmpresa(pojo.getIdEmpresa());
			extendido.setEstatus(pojo.getEstatus());
			extendido.setCreadoPor(pojo.getCreadoPor());
			extendido.setFechaCreacion(pojo.getFechaCreacion());
			extendido.setModificadoPor(pojo.getModificadoPor());
			extendido.setFechaModificacion(pojo.getFechaModificacion());

			if (pojo.getIdProducto() > 0) {
				Producto pojoAux = this.ifzProductos.findById(pojo.getIdProducto());
				pojoAux = (pojoAux != null && pojoAux.getId() != null && pojoAux.getId() > 0L ? pojoAux : new Producto());
				extendido.setProducto(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return extendido;
	}
}

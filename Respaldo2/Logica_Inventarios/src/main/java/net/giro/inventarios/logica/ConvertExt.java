package net.giro.inventarios.logica;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

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
import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.dao.AlmacenDAO;
import net.giro.inventarios.dao.ProductoDAO;


public class ConvertExt {
	private static Logger log = Logger.getLogger(ConvertExt.class);
	private InitialContext ctx = null;
	private AlmacenMovimientosRem ifzMovimientos;
	private ConValoresDAO ifzConValores;
	private ProductoDAO ifzProductos;
	private AlmacenDAO ifzAlmacen;
	private SucursalDAO ifzSucursal;
	private ClientesRem ifzClientes;
	private EmpleadoRem ifzEmpleado;
	private MonedaDAO ifzMonedas;
	 
	
	public ConvertExt() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		this.ctx = new InitialContext(p);
    		this.ifzMovimientos = (AlmacenMovimientosRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzConValores= (ConValoresDAO)  this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
            this.ifzProductos = (ProductoDAO) this.ctx.lookup("ejb:/Model_Inventarios//ProductoImp!net.giro.inventarios.dao.ProductoDAO");
            this.ifzAlmacen = (AlmacenDAO) this.ctx.lookup("ejb:/Model_Inventarios//AlmacenImp!net.giro.inventarios.dao.AlmacenDAO");
            this.ifzSucursal = (SucursalDAO) this.ctx.lookup("ejb:/Model_Publico//SucursalImp!net.giro.ne.dao.SucursalDAO");
    		this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
    		this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
    		this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear ConvertExt", e);
			ctx = null;
		}
	}
	
	//AlmacenTraspaso
	public AlmacenTraspaso AlmacenTraspasoExtToAlmacenTraspaso(AlmacenTraspasoExt pojoTarget) {
		AlmacenTraspaso pojoResult = new AlmacenTraspaso();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
			pojoResult.setEntregaNombre(pojoTarget.getEntregaNombre());
			pojoResult.setRecibeNombre(pojoTarget.getRecibeNombre());
			pojoResult.setRecibe(0);
			pojoResult.setCompleto(pojoTarget.getCompleto());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setSistema(pojoTarget.getSistema());
			pojoResult.setIdAlmacenOrigen(0L);
			pojoResult.setIdAlmacenDestino(0L);
			pojoResult.setEntrega(0);
			pojoResult.setRecibe(0);
			
			if (pojoTarget.getAlmacenOrigen() != null && pojoTarget.getAlmacenOrigen().getId() != null && pojoTarget.getAlmacenOrigen().getId() > 0L)
				pojoResult.setIdAlmacenOrigen(pojoTarget.getAlmacenOrigen().getId());

			if (pojoTarget.getAlmacenDestino() != null && pojoTarget.getAlmacenDestino().getId() != null && pojoTarget.getAlmacenDestino().getId() > 0L)
				pojoResult.setIdAlmacenDestino(pojoTarget.getAlmacenDestino().getId());
			
			if (pojoTarget.getEntrega() != null && pojoTarget.getEntrega().getId() != null && pojoTarget.getEntrega().getId() > 0L)
				pojoResult.setEntrega(pojoTarget.getEntrega().getId());
			
			if (pojoTarget.getRecibe() != null && pojoTarget.getRecibe().getId() != null && pojoTarget.getRecibe().getId() > 0L)
				pojoResult.setRecibe(pojoTarget.getRecibe().getId());
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public AlmacenTraspasoExt AlmacenTraspasoToAlmacenTraspasoExt(AlmacenTraspaso pojoTarget) {
		AlmacenTraspasoExt pojoResult = new AlmacenTraspasoExt();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
			pojoResult.setEntregaNombre(pojoTarget.getEntregaNombre());
			pojoResult.setRecibeNombre(pojoTarget.getRecibeNombre());
			pojoResult.setCompleto(pojoTarget.getCompleto());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setSistema(pojoTarget.getSistema());
			
			if (pojoTarget.getIdAlmacenOrigen() > 0)
				pojoResult.setAlmacenOrigen(this.ifzAlmacen.findById(pojoTarget.getIdAlmacenOrigen()));
			
			if (pojoTarget.getIdAlmacenDestino() > 0)
				pojoResult.setAlmacenDestino(this.ifzAlmacen.findById(pojoTarget.getIdAlmacenDestino()));

			if (pojoTarget.getEntrega() > 0)
				pojoResult.setEntrega(this.ifzEmpleado.findById(pojoTarget.getEntrega()));
			
			if (pojoTarget.getRecibe() > 0)
				pojoResult.setRecibe(this.ifzEmpleado.findById(pojoTarget.getRecibe()));
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public TraspasoDetalleExt TraspasoDetalleToTraspasoDetalleExt(TraspasoDetalle pojoTarget) {
		TraspasoDetalleExt pojoResult = new TraspasoDetalleExt(); 
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdAlmacenTraspaso(pojoTarget.getIdAlmacenTraspaso());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCantidadRecibida(pojoTarget.getCantidadRecibida());
			
			if (pojoTarget.getIdProducto()>0) {
				ProductoExt p = this.ProductoToProductoExt(this.ifzProductos.findById(pojoTarget.getIdProducto()));
				if (p == null)
					p = new ProductoExt();
				pojoResult.setIdProducto(p);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public TraspasoDetalle TraspasoDetalleExtToTraspasoDetalle(TraspasoDetalleExt pojoTarget) {
		TraspasoDetalle pojoResult = new TraspasoDetalle();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdAlmacenTraspaso(pojoTarget.getIdAlmacenTraspaso());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCantidadRecibida(pojoTarget.getCantidadRecibida());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());

			pojoResult.setIdProducto(0L);
			if (pojoTarget.getIdProducto() != null && pojoTarget.getIdProducto().getId() != null && pojoTarget.getIdProducto().getId() > 0L)
				pojoResult.setIdProducto(pojoTarget.getIdProducto().getId());
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	//AlmacenMovimientos
	public AlmacenMovimientos AlmacenMovimientosExtToAlmacenMovimientos(AlmacenMovimientosExt pojoTarget) {
		AlmacenMovimientos pojoResult = new AlmacenMovimientos();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdAlmacen(pojoTarget.getAlmacen().getId());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setTipoEntrada(pojoTarget.getTipoEntrada());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setIdObra(pojoTarget.getIdObra());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
			pojoResult.setIdTraspaso(pojoTarget.getIdTraspaso());
			pojoResult.setSistema(pojoTarget.getSistema());
			pojoResult.setIdSalida(0L);
			
			if (pojoTarget.getFolioFactura() != null)
				pojoResult.setFolioFactura(pojoTarget.getFolioFactura());
			
			if (pojoTarget.getEntrega() != null)
				pojoResult.setEntrega(pojoTarget.getEntrega().getId());
			
			if (pojoTarget.getRecibe() != null)
				pojoResult.setRecibe(pojoTarget.getRecibe().getId());
			
			if (pojoTarget.getIdSalida() != null)
				pojoResult.setIdSalida(pojoTarget.getIdSalida().getId());
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public AlmacenMovimientosExt AlmacenMovimientosToAlmacenMovimientosExt(AlmacenMovimientos pojoTarget) {
		AlmacenMovimientosExt pojoResult = new AlmacenMovimientosExt();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setTipoEntrada(pojoTarget.getTipoEntrada());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
			pojoResult.setIdTraspaso(pojoTarget.getIdTraspaso());
			pojoResult.setIdObra(pojoTarget.getIdObra());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setSistema(pojoTarget.getSistema());
			
			if (pojoTarget.getFolioFactura() != null)
				pojoResult.setFolioFactura(pojoTarget.getFolioFactura());
			
			if (pojoTarget.getIdAlmacen() > 0) {
				Almacen pojoAux = this.ifzAlmacen.findById(pojoTarget.getIdAlmacen());
				if (pojoAux == null)
					pojoAux = new Almacen();
				pojoResult.setAlmacen(pojoAux);
			}

			if (pojoTarget.getEntrega() > 0) {
				Empleado pojoAux = this.ifzEmpleado.findById(pojoTarget.getEntrega());
				if (pojoAux == null)
					pojoAux = new Empleado();
				pojoResult.setEntrega(pojoAux);
			}
			
			if (pojoTarget.getRecibe() > 0) {
				Empleado pojoAux = this.ifzEmpleado.findById(pojoTarget.getRecibe());
				if (pojoAux == null)
					pojoAux = new Empleado();
				pojoResult.setRecibe(pojoAux);
			}
			
			if (pojoTarget.getIdSalida() > 0) {
				AlmacenMovimientosExt pojoAux = this.ifzMovimientos.findByIdExt(pojoTarget.getIdSalida());
				if (pojoAux == null)
					pojoAux = new AlmacenMovimientosExt();
				pojoResult.setIdSalida(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	//Almacen
	public Almacen AlmacenExtToAlmacen(AlmacenExt pojoTarget) {
		Almacen pojoResult = new Almacen();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setNombre(pojoTarget.getNombre());
			pojoResult.setIdentificador(pojoTarget.getIdentificador());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setIdSucursal(pojoTarget.getSucursal().getId());
			pojoResult.setDomicilio(pojoTarget.getDomicilio());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			
			if (pojoTarget.getPojoDomicilio() != null && pojoTarget.getPojoDomicilio().getId() > 0)
				pojoResult.setIdDomicilio(pojoTarget.getPojoDomicilio().getId());
			
			if (pojoTarget.getIdEncargado() != null && pojoTarget.getIdEncargado().getId() != null && pojoTarget.getIdEncargado().getId() > 0L)
				pojoResult.setIdEncargado(pojoTarget.getIdEncargado().getId());
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public AlmacenExt AlmacenToAlmacenExt(Almacen pojoTarget) throws Exception {
		AlmacenExt pojoResult = new AlmacenExt();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setNombre(pojoTarget.getNombre());
			pojoResult.setIdentificador(pojoTarget.getIdentificador());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setDomicilio(pojoTarget.getDomicilio());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			
			if (pojoTarget.getIdDomicilio() > 0) {
				DomicilioExt pojoAux = this.ifzClientes.buscarDomicilioExt(pojoTarget.getIdDomicilio());
				if (pojoAux == null)
					pojoAux = new DomicilioExt();
				pojoResult.setPojoDomicilio(pojoAux);
			}
			
			if (pojoTarget.getIdSucursal() > 0) {
				Sucursal pojoAux = this.ifzSucursal.findById(pojoTarget.getIdSucursal());
				if (pojoAux == null)
					pojoAux = new Sucursal();
				pojoResult.setSucursal(pojoAux);
			}
			
			if (pojoTarget.getIdEncargado() != null && pojoTarget.getIdEncargado() > 0L) {
				Empleado pojoAux = this.ifzEmpleado.findById(pojoTarget.getIdEncargado());
				if (pojoAux == null)
					pojoAux = new Empleado();
				pojoResult.setIdEncargado(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	//Producto	
	public Producto ProductoExtToProducto(ProductoExt pojoTarget) {
		Producto pojoResult = new Producto();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setClave(pojoTarget.getClave());
			pojoResult.setClavePrevia(pojoTarget.getClavePrevia());
			pojoResult.setDescripcion(pojoTarget.getDescripcion());
			pojoResult.setPrecioCompra(pojoTarget.getPrecioCompra());
			pojoResult.setPrecioVenta(pojoTarget.getPrecioVenta());
			pojoResult.setPrecioUnitario(pojoTarget.getPrecioUnitario());
			pojoResult.setPrecioCompraPesos(pojoTarget.getPrecioCompraPesos());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setExistencia(pojoTarget.getExistencia());
			pojoResult.setMaximo(pojoTarget.getMaximo());
			pojoResult.setMinimo(pojoTarget.getMinimo());
			pojoResult.setPermiteExcedente(pojoTarget.getPermiteExcedente());
			pojoResult.setOrigenCodigo(pojoTarget.getOrigenCodigo());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setDescEspecialidad(pojoTarget.getDescEspecialidad());
			pojoResult.setDescFamilia(pojoTarget.getDescFamilia());
			pojoResult.setDescSubfamilia(pojoTarget.getDescSubfamilia());
			pojoResult.setDescUnidadMedida(pojoTarget.getDescUnidadMedida());
			pojoResult.setDescMoneda(pojoTarget.getDescMoneda());
			pojoResult.setDescMonedaAbreviatura(pojoTarget.getDescMonedaAbreviatura());
			pojoResult.setTipoInsumo(pojoTarget.getTipoInsumo());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setOculto(pojoTarget.getOculto());
			pojoResult.setClaveSat(pojoTarget.getClaveSat());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setUnidadMedida(0L);
			pojoResult.setEspecialidad(0L);
			pojoResult.setFamilia(0L);
			pojoResult.setSubfamilia(0L);
			pojoResult.setIdMoneda(0L);

			if (pojoTarget.getEspecialidad() != null && pojoTarget.getEspecialidad().getId() > 0L)
				pojoResult.setEspecialidad(pojoTarget.getEspecialidad().getId());

			if (pojoTarget.getFamilia() != null && pojoTarget.getFamilia().getId() > 0L)
				pojoResult.setFamilia(pojoTarget.getFamilia().getId());

			if (pojoTarget.getFamilia() != null && pojoTarget.getFamilia().getId() > 0L)
				pojoResult.setFamilia(pojoTarget.getFamilia().getId());

			if (pojoTarget.getSubfamilia() != null && pojoTarget.getSubfamilia().getId() > 0L)
				pojoResult.setSubfamilia(pojoTarget.getSubfamilia().getId());

			if (pojoTarget.getIdMoneda() != null && pojoTarget.getIdMoneda().getId() > 0L)
				pojoResult.setIdMoneda(pojoTarget.getIdMoneda().getId()); 
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public ProductoExt ProductoToProductoExt(Producto pojoTarget) {
		ProductoExt pojoResult = new ProductoExt();

		try {
			if (pojoTarget == null)	//si la busqueda regresa null, regresar el pojo recien creado, una vez ocurre esto, en la vista se decide si agregarlo o no
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setClave(pojoTarget.getClave());
			pojoResult.setClavePrevia(pojoTarget.getClavePrevia());
			pojoResult.setDescripcion(pojoTarget.getDescripcion());
			pojoResult.setPrecioCompra(pojoTarget.getPrecioCompra());
			pojoResult.setPrecioVenta(pojoTarget.getPrecioVenta());
			pojoResult.setPrecioUnitario(pojoTarget.getPrecioUnitario());
			pojoResult.setPrecioCompraPesos(pojoTarget.getPrecioCompraPesos());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setExistencia(pojoTarget.getExistencia());
			pojoResult.setMaximo(pojoTarget.getMaximo());
			pojoResult.setMinimo(pojoTarget.getMinimo());
			pojoResult.setPermiteExcedente(pojoTarget.getPermiteExcedente());
			pojoResult.setOrigenCodigo(pojoTarget.getOrigenCodigo());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setDescEspecialidad(pojoTarget.getDescEspecialidad());
			pojoResult.setDescFamilia(pojoTarget.getDescFamilia());
			pojoResult.setDescSubfamilia(pojoTarget.getDescSubfamilia());
			pojoResult.setDescUnidadMedida(pojoTarget.getDescUnidadMedida());
			pojoResult.setDescMoneda(pojoTarget.getDescMoneda());
			pojoResult.setDescMonedaAbreviatura(pojoTarget.getDescMonedaAbreviatura());
			pojoResult.setTipoInsumo(pojoTarget.getTipoInsumo());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setOculto(pojoTarget.getOculto());
			pojoResult.setClaveSat(pojoTarget.getClaveSat());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			
			if (pojoTarget.getEspecialidad() > 0) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getEspecialidad());
				if (pojoAux == null)
					pojoAux = new ConValores();
				pojoResult.setEspecialidad(pojoAux);
			}
			
			if (pojoTarget.getFamilia() > 0) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getFamilia());
				if (pojoAux == null)
					pojoAux = new ConValores();
				pojoResult.setFamilia(pojoAux);
			}
			
			if (pojoTarget.getSubfamilia() > 0) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getFamilia());
				if (pojoAux == null)
					pojoAux = new ConValores();
				pojoResult.setSubfamilia(pojoAux);
			}
				
			if (pojoTarget.getUnidadMedida() > 0) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getUnidadMedida());
				if (pojoAux == null)
					pojoAux = new ConValores();
				pojoResult.setUnidadMedida(pojoAux);
			}

			if (pojoTarget.getIdMoneda() > 0) {
				Moneda pojoAux = this.ifzMonedas.findById(pojoTarget.getIdMoneda());
				if (pojoAux == null)
					pojoAux = new Moneda();
				pojoResult.setIdMoneda(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	//AlmacenProducto
	public AlmacenProducto AlmacenProductoExtToAlmacenProducto(AlmacenProductoExt pojoTarget) {
		AlmacenProducto pojoResult = new AlmacenProducto();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdAlmacen(pojoTarget.getAlmacen().getId());
			pojoResult.setIdProducto(pojoTarget.getProducto().getId());
			pojoResult.setExistencia(pojoTarget.getExistencia());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public AlmacenProductoExt AlmacenProductoToAlmacenProductoExt(AlmacenProducto pojoTarget) {
		AlmacenProductoExt pojoResult = new AlmacenProductoExt();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setExistencia(pojoTarget.getExistencia());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());

			if (pojoTarget.getIdProducto() > 0) {
				Producto pojoAux = this.ifzProductos.findById(pojoTarget.getIdProducto());
				if (pojoAux == null)
					pojoAux = new Producto();
				pojoResult.setProducto(pojoAux);
			}
			
			if (pojoTarget.getIdAlmacen() > 0) {
				Almacen pojoAux = this.ifzAlmacen.findById(pojoTarget.getIdAlmacen());
				if (pojoAux == null)
					pojoAux = new Almacen();
				pojoResult.setAlmacen(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public MovimientosDetalleExt MovimientosDetalleToMovimientosDetalleExt(MovimientosDetalle pojoTarget) {
		MovimientosDetalleExt pojoResult = new MovimientosDetalleExt();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdAlmacenMovimiento(pojoTarget.getIdAlmacenMovimiento());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCantidadSolicitada(pojoTarget.getCantidadSolicitada());
			
			if (pojoTarget.getIdProducto() > 0) {
				ProductoExt pojoAux = this.ProductoToProductoExt(this.ifzProductos.findById(pojoTarget.getIdProducto()));
				if (pojoAux == null)
					pojoAux = new ProductoExt();
				pojoResult.setProducto(pojoAux);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public MovimientosDetalle MovimientosDetalleExtToMovimientosDetalle(MovimientosDetalleExt pojoTarget) {
		MovimientosDetalle pojoResult = new MovimientosDetalle();
		
		try {
			if (pojoTarget == null)
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdAlmacenMovimiento(pojoTarget.getIdAlmacenMovimiento());
			pojoResult.setIdProducto(pojoTarget.getProducto().getId());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCantidadSolicitada(pojoTarget.getCantidadSolicitada());
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
}

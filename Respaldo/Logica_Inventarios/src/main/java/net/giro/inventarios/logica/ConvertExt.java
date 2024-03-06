package net.giro.inventarios.logica;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.logica.ClientesRem;
import net.giro.comun.ExcepConstraint;
import net.giro.ne.dao.SucursalDAO;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.rh.admon.logica.EmpleadoRem;
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
	
	private ConValoresDAO ifzConValores;
	private ProductoDAO ifzProductos;
	//private ObraDAO ifzObras;
	private AlmacenDAO ifzAlmacen;
	private SucursalDAO ifzSucursal;
	private ClientesRem ifzClientes;
	private EmpleadoRem ifzEmpleado;
	private MonedaDAO ifzMonedas;
	 
	
	public ConvertExt() {
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
            
            //ConValores
            this.ifzConValores= (ConValoresDAO)  this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
            
            //Producto
            this.ifzProductos = (ProductoDAO) this.ctx.lookup("ejb:/Model_Inventarios//ProductoImp!net.giro.inventarios.dao.ProductoDAO");
            
            // Obra
            //this.ifzObras = (ObraDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraImp!net.giro.adp.dao.ObraDAO");
            
            // Almacen
            this.ifzAlmacen = (AlmacenDAO) this.ctx.lookup("ejb:/Model_Inventarios//AlmacenImp!net.giro.inventarios.dao.AlmacenDAO");
            
            //  Sucursal
            this.ifzSucursal = (SucursalDAO) this.ctx.lookup("ejb:/Model_Publico//SucursalImp!net.giro.ne.dao.SucursalDAO");
            
            //Clientes
    		this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
    		
    		// Empleados
    		this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
    		
    		// Monedas
    		this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear ConvertExt", e);
			ctx = null;
		}
	}
	
	//AlmacenTraspaso
	public AlmacenTraspaso AlmacenTraspasoExtToAlmacenTraspaso(AlmacenTraspasoExt pojoTarget){
		AlmacenTraspaso pojoResult = new AlmacenTraspaso();
		
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setFecha( pojoTarget.getFecha() );
			pojoResult.setCompleto(pojoTarget.getCompleto());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setIdAlmacenOrigen(0L);
			pojoResult.setIdAlmacenDestino(0L);
			pojoResult.setEntrega(0);
			pojoResult.setRecibe(0);
			
			if (pojoTarget.getAlmacenOrigen() != null && pojoTarget.getAlmacenOrigen().getId() != null && pojoTarget.getAlmacenOrigen().getId() > 0L)
				pojoResult.setIdAlmacenOrigen( pojoTarget.getAlmacenOrigen().getId() );

			if (pojoTarget.getAlmacenDestino() != null && pojoTarget.getAlmacenDestino().getId() != null && pojoTarget.getAlmacenDestino().getId() > 0L)
				pojoResult.setIdAlmacenDestino( pojoTarget.getAlmacenDestino().getId() );
			
			if (pojoTarget.getEntrega() != null && pojoTarget.getEntrega().getId() != null && pojoTarget.getEntrega().getId() > 0L){
				pojoResult.setEntrega( pojoTarget.getEntrega().getId() );
			}
			
			if(pojoTarget.getRecibe() != null && pojoTarget.getRecibe().getId() != null && pojoTarget.getRecibe().getId() > 0L)
				pojoResult.setRecibe( pojoTarget.getRecibe().getId());
		} catch(Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public AlmacenTraspasoExt AlmacenTraspasoToAlmacenTraspasoExt(AlmacenTraspaso pojoTarget){
		AlmacenTraspasoExt pojoResult = new AlmacenTraspasoExt();
		
		try{
			pojoResult.setId( pojoTarget.getId());
			pojoResult.setFecha( pojoTarget.getFecha() );
			pojoResult.setCompleto(pojoTarget.getCompleto());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			
			if(pojoTarget.getIdAlmacenOrigen() > 0)
				pojoResult.setAlmacenOrigen(this.ifzAlmacen.findById(pojoTarget.getIdAlmacenOrigen()) );
			
			if(pojoTarget.getIdAlmacenDestino() > 0)
				pojoResult.setAlmacenDestino(this.ifzAlmacen.findById( pojoTarget.getIdAlmacenDestino() ) );

			if(pojoTarget.getEntrega() > 0)
				pojoResult.setEntrega(this.ifzEmpleado.findById(pojoTarget.getEntrega()) );
			
			if(pojoTarget.getRecibe() > 0)
				pojoResult.setRecibe(this.ifzEmpleado.findById(pojoTarget.getRecibe()) );
		} catch(Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public TraspasoDetalleExt TraspasoDetalleToTraspasoDetalleExt(TraspasoDetalle pojoTarget){
		TraspasoDetalleExt pojoResult = new TraspasoDetalleExt(); 
		try{
			pojoResult.setId( pojoTarget.getId() );
			
			if(pojoTarget.getIdProducto()>0){
				ProductoExt p = this.ProductoToProductoExt( this.ifzProductos.findById(pojoTarget.getIdProducto()) );
				pojoResult.setProducto( p );
			}
				
			pojoResult.setIdAlmacenTraspaso(pojoTarget.getIdAlmacenTraspaso());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			// los datos para control
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
			pojoResult.setCantidadRecibida(pojoTarget.getCantidadRecibida());
			
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public TraspasoDetalle TraspasoDetalleExtToTraspasoDetalle(TraspasoDetalleExt pojoTarget){
		TraspasoDetalle pojoResult = new TraspasoDetalle();
		try{
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setIdAlmacenTraspaso(pojoTarget.getIdAlmacenTraspaso());
			
			// los datos para control
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCantidadRecibida(pojoTarget.getCantidadRecibida());
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );

			pojoResult.setIdProducto(0L);
			if (pojoTarget.getProducto() != null && pojoTarget.getProducto().getId() != null && pojoTarget.getProducto().getId() > 0L)
				pojoResult.setIdProducto(pojoTarget.getProducto().getId());
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	/*
	if (pojoTarget.getIdProducto() > 0)
		pojoResult.setProducto( this.ifzProductos.findById( pojoTarget.getIdProducto() ) );
	
	pojoResult.setCantidad( pojoTarget.getCantidad() );*/
	
	//AlmacenMovimientos
	public AlmacenMovimientos AlmacenMovimientosExtToAlmacenMovimientos(AlmacenMovimientosExt pojoTarget){
		AlmacenMovimientos pojoResult = new AlmacenMovimientos();
		try{
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setIdAlmacen( pojoTarget.getAlmacen().getId() );
			pojoResult.setTipo( pojoTarget.getTipo() );
			
			if (pojoTarget.getEntrega() != null ){
				pojoResult.setEntrega( pojoTarget.getEntrega().getId() );
			}
			
			if(pojoTarget.getRecibe() != null)
				pojoResult.setRecibe( pojoTarget.getRecibe().getId() );
			
			pojoResult.setFecha( pojoTarget.getFecha() );
			
			//pojoResult.setFolioFactura(pojoTarget.getFolioFactura());
			
			// los datos para control
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
			pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
			pojoResult.setIdTraspaso(pojoTarget.getIdTraspaso());
			pojoResult.setIdObra(pojoTarget.getIdObra());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public AlmacenMovimientosExt AlmacenMovimientosToAlmacenMovimientosExt(AlmacenMovimientos pojoTarget){
		AlmacenMovimientosExt pojoResult = new AlmacenMovimientosExt();
		try{
			pojoResult.setId( pojoTarget.getId() );

			pojoResult.setTipo( pojoTarget.getTipo() );
			
			
			if(pojoTarget.getIdAlmacen()>0)
				pojoResult.setAlmacen( this.ifzAlmacen.findById(pojoTarget.getIdAlmacen() ) );

			if(pojoTarget.getEntrega() > 0)
				pojoResult.setEntrega( this.ifzEmpleado.findById(pojoTarget.getEntrega()) );
			
			if(pojoTarget.getRecibe() > 0)
				pojoResult.setRecibe( this.ifzEmpleado.findById(pojoTarget.getRecibe()) );
			
			pojoResult.setFecha( pojoTarget.getFecha() );
			
			pojoResult.setFolioFactura(pojoTarget.getFolioFactura());
			
			
			// los datos para control
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
			pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
			pojoResult.setIdTraspaso(pojoTarget.getIdTraspaso());
			pojoResult.setIdObra(pojoTarget.getIdObra());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	//Almacen
	public Almacen AlmacenExtToAlmacen(AlmacenExt pojoTarget){
		Almacen pojoResult = new Almacen();
		try{
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setNombre( pojoTarget.getNombre() );
			pojoResult.setDomicilio( pojoTarget.getDomicilio() );
			
			pojoResult.setIdSucursal( pojoTarget.getSucursal().getId() );

			
			pojoResult.setTipo(pojoTarget.getTipo());
			
			if (pojoTarget.getPojoDomicilio() != null && pojoTarget.getPojoDomicilio().getId() > 0) {
				pojoResult.setIdDomicilio(pojoTarget.getPojoDomicilio().getId());
			}
			
			
			// los datos para control
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
			pojoResult.setIdentificador(pojoTarget.getIdentificador());
			
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public AlmacenExt AlmacenToAlmacenExt(Almacen pojoTarget){
		AlmacenExt pojoResult = new AlmacenExt();
		try{
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setNombre( pojoTarget.getNombre() );
			pojoResult.setDomicilio( pojoTarget.getDomicilio() );
			pojoResult.setTipo(pojoTarget.getTipo());
			
			if(pojoTarget.getIdDomicilio() > 0){
				log.info("pojoTarget.getIdDomicilio(): "+pojoTarget.getIdDomicilio());
				DomicilioExt pojoAux = this.ifzClientes.buscarDomicilioExt(pojoTarget.getIdDomicilio());
				if (pojoAux == null){
					pojoAux = new DomicilioExt();
					log.info("Convert Domicilio: "+pojoAux.getDomicilio());
				}
					
				pojoResult.setPojoDomicilio(pojoAux);
			}
			
			if(pojoTarget.getIdSucursal()>0)
				pojoResult.setSucursal( this.ifzSucursal.findById( pojoTarget.getIdSucursal() ) );
			
			// los datos para control
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
			pojoResult.setIdentificador(pojoTarget.getIdentificador());
		} catch(ExcepConstraint e) {
			try {
				throw e;
			} catch (ExcepConstraint e1) {
				e1.printStackTrace();
			}
		}
		return pojoResult;
	}
	
	//Producto	
	public Producto ProductoExtToProducto(ProductoExt pojoTarget){
		Producto pojoResult = new Producto();
		
		try{ 
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setClave(pojoTarget.getClave());
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
	
	public ProductoExt ProductoToProductoExt(Producto pojoTarget){
		ProductoExt pojoResult = new ProductoExt();

		try{
			if(pojoTarget == null)	//si la busqueda regresa null, regresar el pojo recien creado, una vez ocurre esto, en la vista se decide si agregarlo o no
				return pojoResult;
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setClave(pojoTarget.getClave());
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
			
			if (pojoTarget.getEspecialidad() > 0)
				pojoResult.setEspecialidad(this.ifzConValores.findById(pojoTarget.getEspecialidad()));
			
			if (pojoTarget.getFamilia() > 0)
				pojoResult.setFamilia(this.ifzConValores.findById(pojoTarget.getFamilia()));
			
			if (pojoTarget.getSubfamilia() > 0)
				pojoResult.setSubfamilia(this.ifzConValores.findById(pojoTarget.getSubfamilia()));
				
			if (pojoTarget.getUnidadMedida() > 0 )
				pojoResult.setUnidadMedida(this.ifzConValores.findById(pojoTarget.getUnidadMedida()));

			if (pojoTarget.getIdMoneda() > 0)
				pojoResult.setIdMoneda(this.ifzMonedas.findById(pojoTarget.getIdMoneda())); 
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	//AlmacenProducto
	public AlmacenProducto AlmacenProductoExtToAlmacenProducto(AlmacenProductoExt pojoTarget){
		AlmacenProducto pojoResult = new AlmacenProducto();
		try{
			pojoResult.setId( pojoTarget.getId() );
			
			pojoResult.setIdAlmacen( pojoTarget.getAlmacen().getId() );
			pojoResult.setIdProducto( pojoTarget.getProducto().getId() );
			
			pojoResult.setExistencia( pojoTarget.getExistencia() );
			
			// los datos para control
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public AlmacenProductoExt AlmacenProductoToAlmacenProductoExt(AlmacenProducto pojoTarget){
		AlmacenProductoExt pojoResult = new AlmacenProductoExt();
		try{
			pojoResult.setId( pojoTarget.getId() );

			if (pojoTarget.getIdProducto() > 0)
				pojoResult.setProducto( this.ifzProductos.findById( pojoTarget.getIdProducto() ) );
			
			if(pojoTarget.getIdAlmacen()>0)
				pojoResult.setAlmacen( this.ifzAlmacen.findById(pojoTarget.getIdAlmacen() ) );
			
			pojoResult.setExistencia( pojoTarget.getExistencia() );
			
			// los datos para control
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public MovimientosDetalleExt MovimientosDetalleToMovimientosDetalleExt(MovimientosDetalle pojoTarget){
		MovimientosDetalleExt pojoResult = new MovimientosDetalleExt();
		try{
			pojoResult.setId(pojoTarget.getId());
			
			pojoResult.setIdAlmacenMovimiento( pojoTarget.getIdAlmacenMovimiento() );
			
			if(pojoTarget.getIdProducto()>0)
				pojoResult.setProducto( this.ProductoToProductoExt(this.ifzProductos.findById(pojoTarget.getIdProducto())) );
			
			pojoResult.setCantidad(pojoTarget.getCantidad());
			// los datos para control
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
			pojoResult.setCantidadSolicitada(pojoTarget.getCantidadSolicitada());
			
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public MovimientosDetalle MovimientosDetalleExtToMovimientosDetalle(MovimientosDetalleExt pojoTarget){
		MovimientosDetalle pojoResult = new MovimientosDetalle();
		try{
			pojoResult.setId(pojoTarget.getId());
			
			pojoResult.setIdAlmacenMovimiento(pojoTarget.getIdAlmacenMovimiento() );
			pojoResult.setIdProducto(pojoTarget.getProducto().getId());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			
			// los datos para control
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
			pojoResult.setCantidadSolicitada(pojoTarget.getCantidadSolicitada());
			
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
}

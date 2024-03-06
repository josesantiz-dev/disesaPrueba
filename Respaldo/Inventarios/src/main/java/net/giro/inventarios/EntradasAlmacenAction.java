package net.giro.inventarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.adp.beans.Obra;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.log4j.Logger;
import org.richfaces.component.AbstractExtendedDataTable;

import com.google.gson.Gson;


@ManagedBean
@ViewScoped
public class EntradasAlmacenAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(EntradasAlmacenAction.class);	
	private InitialContext ctx;
	
	private AlmacenMovimientosRem 	ifzMovimientos;
	private MovimientosDetalleRem	ifzMovimientosDetalle;
	private AlmacenProductoRem		ifzAlmacenProducto;	
	private AlmacenRem 				ifzAlmacen;
	//private ObraRem 				ifzObras;
	private EmpleadoRem 			ifzEmpleado;
	private OrdenCompraRem			ifzOrdenCompra;
	private OrdenCompraDetalleRem	ifzOrdenCompraDetalle;
	private AlmacenTraspasoRem		ifzTraspasos;
	private TraspasoDetalleRem		ifzTraspasosDetalle;
    private Integer 				usuarioId;	    
	private int 					numPagina;
	private int 					numPaginaOrdenCompra;
	private int 					numPaginaObras;
	private int 					numPaginaOrdenCompras;	
	private boolean band;
	private String mensaje;
	private int tipoMensaje;
	private String valorBusqueda;
	private List<SelectItem> listaCampoBusqueda;
	private int campoBusqueda;
	private final int TIPO_MOVIMIENTO = 0;	//O al ser entrada
	private List<AlmacenMovimientosExt> listaMovimientosGrid;
	private AlmacenMovimientosExt pojoMovimiento; 	
	//private Obra pojoObra;
	private List<Empleado> 			listaEmpleados;
	private List<Almacen> 			listaAlmacenes;
	private List<SelectItem> 		listaCboAlmacenes;
	private List<SelectItem> 		listaCboEmpleadoRecibe;
	// Busqueda obras
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorOpcionBusquedaObras;	
	private List<Obra>		 		listObrasPrincipales;
	//	Busqueda Orden Compra
	private List<SelectItem> tiposBusquedaOC;	
	private int campoBusquedaOC;
	private String valorBusquedaOC;
	private OrdenCompraExt pojoOrdenCompra;
	private List<OrdenCompraExt> listaOrdenesCompra;
	private List<OrdenCompraDetalleExt> listaOrdenCompraDetalle;
	//	Buscando traspasos
	private List<SelectItem> tiposBusquedaTraspasos;
	private int campoBusquedaTraspasos;
	private String valorBusquedaTraspasos;
	private AlmacenTraspasoExt pojoTraspaso;
	private List<AlmacenTraspasoExt> listaTraspasos;
	private List<TraspasoDetalleExt> listaTraspasosDetalle;
	private boolean puedeBuscarTraspaso;	
	private boolean puedeEditar;
	private boolean operacionCompleta;
	private String resOperacion;	
	//Variables para la extendedDataTable
	private Collection<Object> selection;
    private List<MovimientosDetalleExt> selectionItems = new ArrayList<MovimientosDetalleExt>();	
	private double cantidadProductoSeleccionado;
	private int rowSeleccionado;
	private MovimientosDetalleExt movimientoSeleccionado;
	private List<MovimientosDetalleExt> listaProductosEntrada;	
	private int cantidadDetalles;
	private long almacenAnterior;	
	private boolean sePuedeGuardarMovimiento;	
	private List<TraspasoDetalleExt> listaTraspasoDetalle;
		
	
	public EntradasAlmacenAction() throws NamingException,Exception {		
		String ejbName;
		
		this.numPagina = 1;
		this.ctx = new InitialContext();

		ejbName = "ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem";
		this.ifzMovimientos = (AlmacenMovimientosRem) ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem";
		this.ifzMovimientosDetalle = (MovimientosDetalleRem) this.ctx.lookup(ejbName);

		//ejbName = "ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem";
		//this.ifzProductos = (ProductoRem) this.ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem";
		this.ifzAlmacen = (AlmacenRem) ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem";
		this.ifzAlmacenProducto = (AlmacenProductoRem) this.ctx.lookup(ejbName);
		
		ejbName =	"ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem";
		//this.ifzObras = (ObraRem) this.ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem";
		this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem";
		this.ifzOrdenCompra = (OrdenCompraRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem";
		this.ifzOrdenCompraDetalle = (OrdenCompraDetalleRem) this.ctx.lookup(ejbName);


		ejbName = "ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem";
		this.ifzTraspasos = (AlmacenTraspasoRem) ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem";
		this.ifzTraspasosDetalle = (TraspasoDetalleRem) ctx.lookup(ejbName);
		
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
		this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
		
		this.listaCampoBusqueda = null;
		this.listaCampoBusqueda = new ArrayList<SelectItem>();
		this.listaCampoBusqueda.add( new SelectItem("1", "Almacen" ) );
		//this.listaCampoBusqueda.add( new SelectItem("2", "Obra" ) );
		
		listaMovimientosGrid = new ArrayList<>();
		listaAlmacenes = new ArrayList<>();
		
		nuevaEntrada();
		
		llenarListaAlmacenes();
		
		// Busqueda obras
		tiposBusquedaObras = new ArrayList<SelectItem>();
		tiposBusquedaObras.add(new SelectItem("id", "Clave"));
		tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
		tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
		tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
		campoBusquedaObras = tiposBusquedaObras.get(0).getDescription();
		valorBusquedaObras = "";
		valorOpcionBusquedaObras = 1;

		this.listObrasPrincipales 	= new ArrayList<Obra>();

		this.listaEmpleados = this.ifzEmpleado.findAllActivos();
		
		cargarCboEmpleadoRecibe();
		
		this.tiposBusquedaOC = new ArrayList<>();
		tiposBusquedaOC.add( new SelectItem("1","Obra") );
		tiposBusquedaOC.add( new SelectItem("2","Proveedor") );
		valorBusquedaOC = "";
		
		this.listaOrdenesCompra = new ArrayList<>();		
		
		iniciarBusquedaTraspasos();
	}
	
	
	private void iniciarBusquedaTraspasos(){
		// Busqueda Traspasos
		this.tiposBusquedaTraspasos = new ArrayList<>();
		tiposBusquedaTraspasos.add( new SelectItem("1","Almacen Destino") );
		tiposBusquedaTraspasos.add( new SelectItem("2","Almacen Origen") );
		valorBusquedaTraspasos = "";
		this.listaTraspasos = new ArrayList<>();
	}
	
	private void llenarListaAlmacenes(){
		this.listaCboAlmacenes = null;
		this.listaCboAlmacenes = new ArrayList<SelectItem>();
		this.listaCboAlmacenes.add( new SelectItem( 0, ""  ) );
		this.listaAlmacenes = this.ifzAlmacen.findAllActivos();
		for( Almacen a: this.listaAlmacenes ){
			this.listaCboAlmacenes.add( new SelectItem( a.getId() , a.getNombre()  ) );
		}
	}
	
	private void cargarCboEmpleadoRecibe(){
		listaCboEmpleadoRecibe = null;
		listaCboEmpleadoRecibe = new ArrayList<>();

		this.listaCboEmpleadoRecibe.add( new SelectItem( "0" , "" ) );
		for( Empleado e: this.listaEmpleados ){
			this.listaCboEmpleadoRecibe.add( new SelectItem( e.getId() , e.getNombre() ) );
		}
	}
	
	private void iniciarBanderas(){
		this.band = false;
		this.mensaje = "";
		this.tipoMensaje = 0;
	}
	
	private void setBanderas(boolean band, int tipoMensaje, String mensaje){
		this.band = band;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje;
		
		log.info("Bandera: "+this.band+", Mensaje: "+this.mensaje+", Tipo: "+this.tipoMensaje);
		
	}
	
	public String buscarObras() {
    	return "";
    }
	
	public void nuevaBusquedaTraspaso(){
		// Busqueda Traspasos
		valorBusquedaTraspasos = "";
		this.resOperacion = "";
		this.listaTraspasos = new ArrayList<>();
		
		this.puedeBuscarTraspaso = true;
		
		if(this.pojoMovimiento.getAlmacen() == null){
			this.puedeBuscarTraspaso = false;
			this.resOperacion = "Seleccione almacén";
			this.tipoMensaje = 4;
		}else{
			if(this.pojoMovimiento.getAlmacen().getId() == 0){
				this.puedeBuscarTraspaso = false;
				this.resOperacion = "Seleccione almacén";
				this.tipoMensaje = 4;
			}	
		}
	}
	
	public void buscarTraspasos(){

		this.listaTraspasos.clear();
		
		iniciarBanderas();
		
		try {

			if ( this.valorBusquedaTraspasos.equals("") ){
				log.info("Buscar todos traspasos");
				this.listaTraspasos = this.ifzTraspasos.findExtByAlmacenOrdenCompleta(0, "");
				
			}else{
				if(this.campoBusquedaTraspasos==1){//buscar por obra
					log.info("Buscar por Almacen Destino: "+this.valorBusquedaTraspasos);
					this.listaTraspasos = this.ifzTraspasos.findExtByAlmacenOrdenCompleta(1, this.valorBusquedaTraspasos);
					
				}else{//buscar por obra
					log.info("Buscar por Almacen Origen: "+this.valorBusquedaTraspasos);
					this.listaTraspasos = this.ifzTraspasos.findExtByAlmacenOrdenCompleta(2, this.valorBusquedaTraspasos);
				}
			}
			
			if (this.listaTraspasos.isEmpty()) {
				setBanderas( true, 2, "ERROR" );
			}
			
		}catch (Exception e) {
			setBanderas( true, 1, "Error al buscar" );
    		log.error("Error en buscar", e);
    		
    	}
	}
	
	public void buscar() {
		try {
			listaMovimientosGrid.clear();
			iniciarBanderas();

			if ( this.valorBusqueda.equals("") ){
				log.info("Buscar todos");
				listaMovimientosGrid = this.ifzMovimientos.findExtByEspecificField("nombreAlmacen", this.valorBusqueda, this.TIPO_MOVIMIENTO);
			}else{
				if(this.campoBusqueda==1){//buscar por almacen
					log.info("Buscar por almacen: "+this.valorBusqueda);
					listaMovimientosGrid = this.ifzMovimientos.findExtByEspecificField("nombreAlmacen", this.valorBusqueda, this.TIPO_MOVIMIENTO);
				}else{//buscar por obra
					log.info("Buscar por obra: "+this.valorBusqueda);
					listaMovimientosGrid = this.ifzMovimientos.findExtByEspecificField("nombreObra", this.valorBusqueda, this.TIPO_MOVIMIENTO);
				}
			}
			
			if (this.listaMovimientosGrid.isEmpty()) {
				setBanderas( true, 2, "ERROR" );
			}
			
		} catch (Exception e) {
			setBanderas( true, 1, "Error al buscar" );
    		log.error("Error en buscar", e);
    	}
	}
	
	public void nuevaBusquedaOrdenCompra(){
		
	}

	public void seleccionarTraspaso(){
		
		log.info("Obteniendo traspaso a detalles");
		
		//Se tiene asigna el id de orden de compra a cero, para que no exista confusión en futuras validaciones
		this.pojoMovimiento.setIdOrdenCompra(0L);
		
		if(this.pojoTraspaso.getId()==null) return;
		
		this.pojoMovimiento.setIdTraspaso(pojoTraspaso.getId());
		
		agregarTraspasoDetalles(this.pojoTraspaso.getId());
		
		if(almacenAnterior==0){	//Asignar por primera vez el almacen
			this.setAlmacenAnterior(this.pojoMovimiento.getAlmacen().getId());
			log.info("Asignar almacen anterior: "+almacenAnterior);
		}
		
		
	}
	
	public void eliminarProductosGrid(){
		this.listaProductosEntrada.clear();
		this.almacenAnterior = 0;
	}
	
	private void agregarTraspasoDetalles(long idAlmacenTraspaso){
		
		listaTraspasoDetalle = this.ifzTraspasosDetalle.findDetallesExtById(idAlmacenTraspaso);
		
		this.listaProductosEntrada = new ArrayList<>();		//La lista del traspaso que se mostrará
		
		sePuedeGuardarMovimiento = false;
		
		for(TraspasoDetalleExt var: this.listaTraspasoDetalle){
			
			ProductoExt p = var.getProducto();
			
			MovimientosDetalleExt m = new MovimientosDetalleExt();
			
			m.setProducto(p);
			
			//cantidadAuxiliar fungirá como la cantidad que ya esta entregada y registrada
			// --> Cantidad de producto PENDIENTE
			m.setCantidad_auxiliar1( var.getCantidad() - var.getCantidadRecibida() );	
			
			log.info("Cantidad pendiente: "+m.getCantidad_auxiliar1() + ", ID: "+var.getProducto().getId());
			
			//asignamos al campo cantidad-solicitada el mismo valor que tiene cantidad
			m.setCantidadSolicitada(var.getCantidad());		
			
			m.setCantidad( var.getCantidad() - var.getCantidadRecibida()  );
			
			
			//Si la cantidad pendiente en alguno de los movimientos es mayor a cero, significa que se puede editar, por lo que se puede guardar
			if( m.getCantidad_auxiliar1() > 0){
				sePuedeGuardarMovimiento = true;
			}
			
			this.listaProductosEntrada.add(m);
			
		}
		
		this.pojoOrdenCompra = new OrdenCompraExt();
		
		
	}
	
	public void seleccionarOrdenCompra(){
		//pojoOrdenCompra
		//La relacion entre una entrada y la orden de compra es el campo id_orden_compra
		//esos detalles de compra, asignarlos a una lista de movimientos, es decir, crear un nuveo Objeto por cada item de la compra
		//para cada campo, asignar el valor "setCantidadSolicitada" con lo que tenga el campo "cantidad", para fines informativos.
		//Preguntar si no se necesita el campo "cantidad_solicitada"
		
		try {
			//Se tiene asigna el id de orden de compra a cero, para que no exista confusión en futuras validaciones
			this.pojoMovimiento.setIdTraspaso(0L);
			if(this.pojoOrdenCompra.getId()==null) return;
			
			this.pojoMovimiento.setIdOrdenCompra(this.pojoOrdenCompra.getId());
			this.listaOrdenCompraDetalle = this.ifzOrdenCompraDetalle.findExtByProperty("idOrdenCompra", this.pojoOrdenCompra.getId(), 50);
			this.listaProductosEntrada = new ArrayList<>();
			sePuedeGuardarMovimiento = false;
			
			// OrdenCompraDetalle
			for(OrdenCompraDetalleExt var: this.listaOrdenCompraDetalle){
				ProductoExt p = var.getIdProducto();
				MovimientosDetalleExt m = new MovimientosDetalleExt();
				m.setProducto(p);
				//cantidadAuxiliar fungirá como la cantidad que ya esta entregada y registrada
				// --> Cantidad de producto PENDIENTE
				m.setCantidad_auxiliar1( var.getCantidad() - var.getCantidadRecibida() );
				log.info("Cantidad pendiente: "+m.getCantidad_auxiliar1() + ", ID: "+var.getIdProducto());
				//asignamos al campo cantidad-solicitada el mismo valor que tiene cantidad
				m.setCantidadSolicitada(var.getCantidad());
				m.setCantidad( var.getCantidad() - var.getCantidadRecibida()  );
				//Si la cantidad pendiente en alguno de los movimientos es mayor a cero, significa que se puede editar, por lo que se puede guardar
				if( m.getCantidad_auxiliar1() > 0){
					sePuedeGuardarMovimiento = true;
				}
				
				this.listaProductosEntrada.add(m);
			}
			
			this.pojoOrdenCompra = new OrdenCompraExt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void buscarOrdenesCompra(){
		try {
			this.listaOrdenesCompra.clear();
			iniciarBanderas();

			if ( this.valorBusquedaOC.equals("") ){
				this.listaOrdenesCompra = this.ifzOrdenCompra.findNoCompletasExt("folio", "", 50);
			} else {
				if(this.campoBusquedaOC==1){//buscar por obra
					log.info("Buscar por Obra: "+this.valorBusquedaOC);
					this.listaOrdenesCompra = this.ifzOrdenCompra.findNoCompletasExt("nombreObra", this.valorBusquedaOC, 50);
				} else {//buscar por obra
					log.info("Buscar por Proveedor: "+this.valorBusquedaOC);
					this.listaOrdenesCompra = this.ifzOrdenCompra.findNoCompletasExt("nombreProveedor", this.valorBusquedaOC, 50);
				}
			}
			
			if (this.listaOrdenesCompra.isEmpty()) {
				setBanderas( true, 2, "ERROR" );
			}
		} catch (Exception e) {
			setBanderas( true, 1, "Error al buscar" );
    		log.error("Error en buscar", e);
    	}
	}
	
	public void setNumeroItems(int numeroItems){
		
	}
	
	public int getNumeroItems(){
		return this.listaProductosEntrada == null ? 0 : this.listaProductosEntrada.size();
	}
	
	public void eliminarDetallesGrid(){
		this.cantidadDetalles = 0;
		this.almacenAnterior = 0;
	}
	
	public void regresarAlmacen(){
		log.info("Almacen anterior:");
	}
	
	public void editar(){
		this.puedeEditar = false;
	}
	
	public void eliminar(){
		
	}
	
	public void guardar(){
		iniciarOperacion();
		
		try{
			this.pojoMovimiento.setTipo(this.TIPO_MOVIMIENTO);			
			this.pojoMovimiento.setModificadoPor(this.usuarioId);
			this.pojoMovimiento.setFechaModificacion(Calendar.getInstance().getTime());

			if( !validaGuardarMovimiento() ) return;
			
			if( this.pojoMovimiento.getId() == null ){	//Guardar movimiento
				this.pojoMovimiento.setCreadoPor(this.usuarioId);
				this.pojoMovimiento.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoMovimiento.setId( this.ifzMovimientos.save(this.pojoMovimiento) );
				
				for(MovimientosDetalleExt md: this.listaProductosEntrada){
					md.setIdAlmacenMovimiento(this.pojoMovimiento.getId());
					md.setCreadoPor(this.usuarioId);
					md.setFechaCreacion(Calendar.getInstance().getTime());
					md.setModificadoPor(this.usuarioId);
					md.setFechaModificacion(Calendar.getInstance().getTime());
				
					//Solo si se puede actualizar/guardar esta cantidad del movimiento
					if( md.getCantidad_auxiliar1() != 0 ){
						//Solo si la cantidad no es cero, guardar
						if( md.getCantidad() > 0 ){
							this.ifzMovimientosDetalle.save(md);
						}
					}
				}
				
				//Ahora, afectar las existencias
				List<MovimientosDetalle> listaDetalle = new ArrayList<>();
				for(MovimientosDetalleExt md:listaProductosEntrada){
					AlmacenProducto ap = this.ifzAlmacenProducto.findAlmacenProducto(this.pojoMovimiento.getAlmacen().getId(), md.getProducto().getId());
					
					// SOLO SI NO HAY CANTIDAD PENDIENTE
					if( md.getCantidad_auxiliar1() != 0 ){
						MovimientosDetalle d = new MovimientosDetalle();
						d.setId(0);
						d.setIdAlmacenMovimiento(0);
						d.setIdProducto(md.getProducto().getId());
						d.setCantidad(md.getCantidad());
						listaDetalle.add(d);
					}
					
					if(ap!=null){
						//Actualizamos la existencia del producto
						log.info("Existencia: "+ap.getExistencia()+", Cantidad: "+ md.getCantidad());
						ap.setExistencia( ap.getExistencia() + md.getCantidad() );
						this.ifzAlmacenProducto.update(ap);
					}
						
				}
				
				if( this.pojoMovimiento.getIdOrdenCompra() > 0 ){	//Hay una compra adjunta, lo que significa que es un movimiento correspondiente a una orden de compra
					actualizaBackOrder(this.pojoMovimiento.getId(), this.pojoMovimiento.getIdOrdenCompra(), listaDetalle);
				}else{
					if ( this.pojoMovimiento.getIdTraspaso() > 0 ){	//Hay un traspaso adjunto, el movimiento corresponde a un traspaso
						actualizarDetallesTraspaso(this.pojoMovimiento.getIdTraspaso(), listaDetalle );
					}
				}
				
				this.listaMovimientosGrid.add(this.pojoMovimiento);
			}
			
			nuevaEntrada();
			this.operacionCompleta = true;
			this.resOperacion = "";
			this.band = false;
			this.tipoMensaje = 0;
		} catch(Exception e1) {
			log.info("Error al agregar el pojo a la lista");
			e1.printStackTrace();
		}
	}
	
	private void actualizaBackOrder(long idEntrada, long idOrdenCompra, List<MovimientosDetalle> listaDetalle ) throws JMSException, NamingException {
		
		//String evento, String objeto, String id, String atributos
		
		Gson gson = new Gson();
		
		TopicoS salida = new TopicoS("backorder", "compras", String.valueOf( idOrdenCompra ), gson.toJson(listaDetalle) );
		
		String textBase =  gson.toJson(salida); //"{ objeto: 'compras', evento: 'backorder', id: '"+ String.valueOf( idEntrada ) +"', atributos: '"+idOrdenCompra+"' }";
		
		log.info("Backorder: "+textBase);
		
		InitialContext iniCtx = new InitialContext();
		Object tmp = iniCtx.lookup("ConnectionFactory");
		QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
		Connection conn = qcf.createQueueConnection();

		Topic topicCompras = (Topic) iniCtx.lookup("topic/INVENTARIOS");
		Session session = conn.createSession(false,
		QueueSession.AUTO_ACKNOWLEDGE);
		conn.start();
		
		MessageProducer sendtopic = session.createProducer(topicCompras);
		TextMessage tm = session.createTextMessage(textBase);
		
		sendtopic.send(tm);			// aqui traia un simbolo raro
		conn.stop();
		session.close();
		conn.close();
		
		log.info("logica Entradas Almacen: escribiendo en la cola de Inventarios...");
		
	}
	
	private void actualizarDetallesTraspaso(long idTraspaso, List<MovimientosDetalle> listaDetalle){
		log.info("Actualizando detalles de traspaso");
		
		//Traer la lista que corresponde a este traspaso
		List<TraspasoDetalleExt> lista = this.ifzTraspasosDetalle.findDetallesExtById(idTraspaso);
		
		//A var.cantidadRecibida se le suma la cantidad del detalle del movimiento
		for(TraspasoDetalleExt var:lista){
			double cantidad = getCantidadTraspaso(var.getProducto().getId(), listaDetalle);
			if (cantidad > 0){
				
				var.setCantidadRecibida(var.getCantidadRecibida() + cantidad);
				
				log.info("var.getCantidadRecibida: "+var.getCantidadRecibida());
				
				try {
					
					//estatus ?  1 transito : 2 completado
					if ( var.getCantidad() == var.getCantidadRecibida() )
						var.setEstatus(2);
					
					this.ifzTraspasosDetalle.update(var);
					log.info("Actualizano Producto: "+var.getProducto().getId() );
				} catch (ExcepConstraint e) {
					e.printStackTrace();
				}
			}
		}
		
		boolean actualizarEstatus = true;
		
		for(TraspasoDetalleExt var:lista){
			log.info(var.getCantidad() +", "+ var.getCantidadRecibida());
			if( var.getCantidad() != var.getCantidadRecibida() )
				actualizarEstatus = false;	//con alguno que este en false, ya no se actualiza el estatus del traspaso
		}
		
		if(actualizarEstatus){
			AlmacenTraspaso traspaso = this.ifzTraspasos.findById(idTraspaso) ;
			traspaso.setCompleto(1);
			try {
				log.info("Actualizando traspaso");
				this.ifzTraspasos.update(traspaso);
			} catch (ExcepConstraint e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	private double getCantidadTraspaso(long idProducto, List<MovimientosDetalle> listaDetalle ){
		for(MovimientosDetalle var: listaDetalle){
			log.info("Producto param: "+idProducto+", var.idProducto: "+var.getIdProducto());
			if(idProducto == var.getIdProducto()){
				return var.getCantidad();
			}
		}
		return 0;
	}
	
	private boolean validaGuardarMovimiento(){
		
		this.resOperacion = "";
		this.tipoMensaje = 0;
		
		if( this.pojoMovimiento.getAlmacen() == null ){
			//this.resOperacion = "Seleccione almacén";
			this.tipoMensaje = -10;
			return false;
		}
		
		if( this.pojoMovimiento.getRecibe() == null ){
			//this.resOperacion = "Indique el empleado que recibe";
			this.tipoMensaje = -11;
			return false;
		}
		
		if(this.pojoMovimiento.getIdOrdenCompra() > 0){
			if( this.pojoMovimiento.getFolioFactura().equals("") ){
				//this.resOperacion = "Debe ingresar el folio de la Factura";
				this.tipoMensaje = -12;
				return false;
			}
		}

		if( this.listaProductosEntrada.isEmpty() ){
			//this.resOperacion = "Debe existir por lo menos un producto en la lista";
			this.tipoMensaje = -13;
			return false;
		}
		
		if (!sePuedeGuardarMovimiento){
			//this.resOperacion = "Esta orden de compra se encuentra completa.";
			this.tipoMensaje = -14;
			return false;
		}
		
		//Revisar si alguno se encuentra en cero o si la cantidad recibida es mayor a la solicitada
		
		//revisar que la cantidad que estan registrando, mas la cantidad que ya se entregó, no supere a la solicitada
		for( MovimientosDetalleExt md: this.listaProductosEntrada){
			if( md.getCantidad()  >  md.getCantidadSolicitada()  ){
				//this.resOperacion = "La cantidad recibida no puede ser mayor a la solicitada";
				return false;
			}
			
		}
		
		return true;
	}
	
	public void nuevaEntrada(){
		this.puedeEditar = true;
		this.pojoMovimiento = new AlmacenMovimientosExt();
		this.pojoMovimiento.setIdTraspaso(0L);
		this.pojoMovimiento.setIdOrdenCompra(0L);
		this.listaProductosEntrada = new ArrayList<>();
		//this.pojoObra = new Obra();
	}

	private void iniciarOperacion(){
		this.operacionCompleta = false;
		this.resOperacion = "";
	}
	
	public void seleccionarObra(){
		
	}

	public void setIdAlmacen(long idAlmacen){
		this.pojoMovimiento.setAlmacen(this.ifzAlmacen.findById(idAlmacen));
	}
	
	public long getIdAlmacen(){
		return this.pojoMovimiento==null? 0 : pojoMovimiento.getAlmacen()==null ? 0 : pojoMovimiento.getAlmacen().getId();
	}

	public Date getFecha(){
		return this.pojoMovimiento.getFecha() == null ? Calendar.getInstance().getTime() : this.pojoMovimiento.getFecha(); 
	}
	
	public void setFecha(Date fecha){
		this.pojoMovimiento.setFecha(fecha);
	}
	
	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public boolean isBand() {
		return band;
	}

	public void setBand(boolean band) {
		this.band = band;
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

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public List<SelectItem> getListaCampoBusqueda() {
		return listaCampoBusqueda;
	}

	public void setListaCampoBusqueda(List<SelectItem> listaCampoBusqueda) {
		this.listaCampoBusqueda = listaCampoBusqueda;
	}

	public int getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(int campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public List<AlmacenMovimientosExt> getListaMovimientosGrid() {
		return listaMovimientosGrid;
	}

	public void setListaMovimientosGrid(List<AlmacenMovimientosExt> listaMovimientosGrid) {
		this.listaMovimientosGrid = listaMovimientosGrid;
	}

	public AlmacenMovimientosExt getPojoMovimiento() {
		return pojoMovimiento;
	}

	public void setPojoMovimiento(AlmacenMovimientosExt pojoMovimiento) {
		this.pojoMovimiento = pojoMovimiento;
		//this.pojoObra = this.pojoMovimiento.getObra();
		this.setIdEmpleadoRecibe(this.pojoMovimiento.getRecibe().getId());
		
		cargarDetalleMovimientos(this.pojoMovimiento.getId());
	}

	private void cargarDetalleMovimientos(long idAlmacenMovimiento){
		
		this.listaProductosEntrada = this.ifzMovimientosDetalle.findDetallesExtById(idAlmacenMovimiento);
	}
	/*
	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		log.info("setPojoObra: "+pojoObra.getNombre());
		this.pojoObra = pojoObra;
		this.pojoMovimiento.setObra(pojoObra);
	}*/

	public int getNumPaginaOrdenCompra() {
		return numPaginaOrdenCompra;
	}

	public void setNumPaginaOrdenCompra(int numPaginaOrdenCompra) {
		this.numPaginaOrdenCompra = numPaginaOrdenCompra;
	}

	public List<SelectItem> getListaCboAlmacenes() {
		return listaCboAlmacenes;
	}

	public void setListaCboAlmacenes(List<SelectItem> listaCboAlmacenes) {
		this.listaCboAlmacenes = listaCboAlmacenes;
	}

	public List<Almacen> getListaAlmacenes() {
		return listaAlmacenes;
	}

	public void setListaAlmacenes(List<Almacen> listaAlmacenes) {
		this.listaAlmacenes = listaAlmacenes;
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

	public List<Obra> getListObrasPrincipales() {
		return listObrasPrincipales;
	}

	public void setListObrasPrincipales(List<Obra> listObrasPrincipales) {
		this.listObrasPrincipales = listObrasPrincipales;
	}

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}

	public Collection<Object> getSelection() {
		return selection;
	}

	public void setSelection(Collection<Object> selection) {
		this.selection = selection;
	}

	public List<MovimientosDetalleExt> getSelectionItems() {
		return selectionItems;
	}

	public void setSelectionItems(List<MovimientosDetalleExt> selectionItems) {
		this.selectionItems = selectionItems;
	}

	public double getCantidadProductoSeleccionado() {
		return cantidadProductoSeleccionado;
	}

	public void setCantidadProductoSeleccionado(double cantidadProductoSeleccionado) {
		this.cantidadProductoSeleccionado = cantidadProductoSeleccionado;
	}

	public int getRowSeleccionado() {
		return rowSeleccionado;
	}

	public void setRowSeleccionado(int rowSeleccionado) {
		this.rowSeleccionado = rowSeleccionado;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public int getCantidadDetalles() {
		return cantidadDetalles;
	}

	public void setCantidadDetalles(int cantidadDetalles) {
		this.cantidadDetalles = cantidadDetalles;
	}

	public long getAlmacenAnterior() {
		return almacenAnterior;
	}

	public void setAlmacenAnterior(long almacenAnterior) {
		this.almacenAnterior = almacenAnterior;
	}

	public List<SelectItem> getListaCboEmpleadoRecibe() {
		return listaCboEmpleadoRecibe;
	}

	public void setListaCboEmpleadoRecibe(List<SelectItem> listaCboEmpleadoRecibe) {
		this.listaCboEmpleadoRecibe = listaCboEmpleadoRecibe;
	}

	public List<Empleado> getListaEmpleados() {
		return listaEmpleados;
	}

	public void setListaEmpleados(List<Empleado> listaEmpleados) {
		this.listaEmpleados = listaEmpleados;
	}

	public boolean isOperacionCompleta() {
		return operacionCompleta;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCompleta = operacionCompleta;
	}

	public String getValorBusquedaOC() {
		return valorBusquedaOC;
	}

	public void setValorBusquedaOC(String valorBusquedaOC) {
		this.valorBusquedaOC = valorBusquedaOC;
	}

	public List<OrdenCompraExt> getListaOrdenesCompra() {
		return listaOrdenesCompra;
	}

	public void setListaOrdenesCompra(List<OrdenCompraExt> listaOrdenesCompra) {
		this.listaOrdenesCompra = listaOrdenesCompra;
	}

	public int getNumPaginaOrdenCompras() {
		return numPaginaOrdenCompras;
	}

	public void setNumPaginaOrdenCompras(int numPaginaOrdenCompras) {
		this.numPaginaOrdenCompras = numPaginaOrdenCompras;
	}

	public List<SelectItem> getTiposBusquedaOC() {
		return tiposBusquedaOC;
	}

	public void setTiposBusquedaOC(List<SelectItem> tiposBusquedaOC) {
		this.tiposBusquedaOC = tiposBusquedaOC;
	}

	public int getCampoBusquedaOC() {
		return campoBusquedaOC;
	}

	public void setCampoBusquedaOC(int campoBusquedaOC) {
		this.campoBusquedaOC = campoBusquedaOC;
	}

	public OrdenCompraExt getPojoOrdenCompra() {
		return pojoOrdenCompra;
	}

	public void setPojoOrdenCompra(OrdenCompraExt pojoOrdenCompra) {
		this.pojoOrdenCompra = pojoOrdenCompra;
		log.info("Oden de compra seteada, ID: "+this.pojoOrdenCompra.getId());
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}
	
	public MovimientosDetalleExt getMovimientoSeleccionado() {
		return movimientoSeleccionado;
	}

	public void setMovimientoSeleccionado(MovimientosDetalleExt movimientoSeleccionado) {
		this.movimientoSeleccionado = movimientoSeleccionado;
	}

	public List<MovimientosDetalleExt> getListaProductosEntrada() {
		return listaProductosEntrada;
	}

	public void setListaProductosEntrada(List<MovimientosDetalleExt> listaProductosEntrada) {
		this.listaProductosEntrada = listaProductosEntrada;
	}
	
	public void setIdEmpleadoRecibe(long idEmpleado){
		if(idEmpleado > 0){
			this.pojoMovimiento.setRecibe(this.ifzEmpleado.findById(idEmpleado));
		}	
	}
	
	public long getIdEmpleadoRecibe(){
		return this.pojoMovimiento.getRecibe() == null ? 0 : this.pojoMovimiento.getRecibe().getId() == null ? 0 : this.pojoMovimiento.getRecibe().getId();
	}
	
	public void selectionListener(AjaxBehaviorEvent event) {
		
		if(!this.puedeEditar) return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
		
        AbstractExtendedDataTable dataTable = (AbstractExtendedDataTable) event.getComponent();
        Object originalKey = dataTable.getRowKey();
        selectionItems.clear();
        //int row=0;
        for (Object selectionKey : selection) {
            dataTable.setRowKey(selectionKey);
            //row++;
            if (dataTable.isRowAvailable()) {
            	
            	movimientoSeleccionado = (MovimientosDetalleExt) dataTable.getRowData();
            	log.info("Elemento seleccionado: "+movimientoSeleccionado.getProducto().getId()+", Producto: "+movimientoSeleccionado.getProducto().getDescripcion() + ", Cant:"+movimientoSeleccionado.getCantidad());
            	
            }
        }
        dataTable.setRowKey(originalKey);
    }
	
	public void validaCantidadProducto(){
		//int row = 0;
		
		if(!this.puedeEditar) return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
		/*
		if(listaProductosEntrada.isEmpty()) return;
		
		MovimientosDetalleExt movimiento = new MovimientosDetalleExt() ;
		for(MovimientosDetalleExt md:this.listaProductosEntrada){
			if(row==this.rowSeleccionado){
				log.info("Row: "+row);
				//movimiento = this.listaProductosEntrada.get(row);
				//log.info("Cantidad Producto: "+cantidadProductoSeleccionado + " Row: "+rowSeleccionado+", Cant en Movimiento:"+movimiento.getCantidad());
			}
			row++;
		}
		*/
		
	}

	public List<SelectItem> getTiposBusquedaTraspasos() {
		return tiposBusquedaTraspasos;
	}

	public void setTiposBusquedaTraspasos(List<SelectItem> tiposBusquedaTraspasos) {
		this.tiposBusquedaTraspasos = tiposBusquedaTraspasos;
	}

	public int getCampoBusquedaTraspasos() {
		return campoBusquedaTraspasos;
	}

	public void setCampoBusquedaTraspasos(int campoBusquedaTraspasos) {
		this.campoBusquedaTraspasos = campoBusquedaTraspasos;
	}

	public String getValorBusquedaTraspasos() {
		return valorBusquedaTraspasos;
	}

	public void setValorBusquedaTraspasos(String valorBusquedaTraspasos) {
		this.valorBusquedaTraspasos = valorBusquedaTraspasos;
	}

	public AlmacenTraspasoExt getPojoTraspaso() {
		return pojoTraspaso;
	}

	public void setPojoTraspaso(AlmacenTraspasoExt pojoTraspaso) {
		this.pojoTraspaso = pojoTraspaso;
		log.info("Traspaso seteado, ID: "+this.pojoTraspaso.getId());
	}

	public List<AlmacenTraspasoExt> getListaTraspasos() {
		return listaTraspasos;
	}

	public void setListaTraspasos(List<AlmacenTraspasoExt> listaTraspasos) {
		this.listaTraspasos = listaTraspasos;
	}

	public List<TraspasoDetalleExt> getListaTraspasosDetalle() {
		return listaTraspasosDetalle;
	}

	public void setListaTraspasosDetalle(List<TraspasoDetalleExt> listaTraspasosDetalle) {
		this.listaTraspasosDetalle = listaTraspasosDetalle;
	}

	public boolean isPuedeBuscarTraspaso() {
		return puedeBuscarTraspaso;
	}

	public void setPuedeBuscarTraspaso(boolean puedeBuscarTraspaso) {
		this.puedeBuscarTraspaso = puedeBuscarTraspaso;
	}
}

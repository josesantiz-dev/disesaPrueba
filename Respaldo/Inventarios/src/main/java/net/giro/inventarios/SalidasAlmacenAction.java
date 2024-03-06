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
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.navegador.LoginManager;

import org.apache.log4j.Logger;
import org.richfaces.component.AbstractExtendedDataTable;


@ManagedBean
@ViewScoped
public class SalidasAlmacenAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SalidasAlmacenAction.class);
	private InitialContext ctx;
	
	private AlmacenMovimientosRem 	ifzMovimientos;
	private MovimientosDetalleRem	ifzMovimientosDetalle;
	private ProductoRem 			ifzProductos;
	private AlmacenRem 				ifzAlmacen;
	private ObraRem 				ifzObras;
	private AlmacenProductoRem		ifzAlmacenProducto;
	private EmpleadoRem 			ifzEmpleado;
    private Integer usuarioId;	    
	private int numPagina;
	private int numPaginaObras;
	private int numPaginaOrdenCompra;
	private int numPaginaProductos;
	private boolean operacionCompleta;
	private String resOperacion;
	private boolean band;
	private String mensaje;
	private int tipoMensaje;
	private boolean puedeEditar;
	private String valorBusqueda;
	private List<SelectItem> listaCampoBusqueda;
	private int campoBusqueda;
	private final int TIPO_MOVIMIENTO = 1;	//1 al ser salida
	private List<AlmacenMovimientosExt> listaMovimientosGrid;
	private AlmacenMovimientosExt pojoMovimientoEditar;
	private int currentIndex;
	private long almacenAnterior;
	private List<Almacen> listaAlmacenes;
	private List<SelectItem> listaCboAlmacenes;
	private List<Empleado> listaEmpleados;
	private List<SelectItem> listaCboEmpleadoEntrega;
	private List<SelectItem> listaCboEmpleadoRecibe;
	// Busqueda obras
	private List<SelectItem> 		tiposBusquedaObras;	
	private String 					campoBusquedaObras;
	private String 					valorBusquedaObras;
	private int 					valorOpcionBusquedaObras;
	private List<Obra>		 		listObrasPrincipales;
	//Variables para esta el movimiento de salida
	private List<MovimientosDetalleExt> listaProductosSalida;
	private int numeroItems;
	//Busqueda Productos
	private List<Producto> 			listaBusquedaProductos; 
	private List<SelectItem> 		tiposBusquedaProductos;	
	private String 					campoBusquedaProductos;
	private String 					valorBusquedaProductos;
	private int 					valorOpcionBusquedaProductos;
	private Producto 				pojoProductoBusqueda;
	//Variables para el movimiento
	private AlmacenMovimientosExt			pojoMovimiento; 
	private Obra 							pojoObra;
	private Empleado 						pojoEmpleadoEntrega;
	private Empleado 						pojoEmpleadoRecibe;
	private MovimientosDetalleExt			pojoMovimientoDetalleEliminar;
	MovimientosDetalleExt productoSalidaSeleccionado;
	private MovimientosDetalleExt movimientoSeleccionado;
	private double cantidadProductoSeleccionado;
	private int rowSeleccionado;
	//private boolean cantidadInsuficienteProducto;
	private Collection<Object> selection;
    private List<MovimientosDetalleExt> selectionItems = new ArrayList<MovimientosDetalleExt>();
    
	
	public SalidasAlmacenAction() throws NamingException,Exception {
		String ejbName;
		
		this.numPagina = 1;
		this.ctx = new InitialContext();

		ejbName = "ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem";
		this.ifzMovimientos = (AlmacenMovimientosRem) ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem";
		this.ifzMovimientosDetalle = (MovimientosDetalleRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem";
		this.ifzProductos = (ProductoRem) this.ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem";
		this.ifzAlmacen = (AlmacenRem) ctx.lookup(ejbName);
		
		ejbName =	"ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem";
		this.ifzObras = (ObraRem) this.ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem";
		this.ifzProductos = (ProductoRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem";
		this.ifzAlmacenProducto = (AlmacenProductoRem) this.ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem";
		this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup(ejbName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
		this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
		
		//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		//PropertyResourceBundle entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());
		
		this.productoSalidaSeleccionado = new MovimientosDetalleExt();
		
		this.listaCampoBusqueda = null;
		this.listaCampoBusqueda = new ArrayList<SelectItem>();
		this.listaCampoBusqueda.add( new SelectItem("1", "Almacen" ) );
		//this.listaCampoBusqueda.add( new SelectItem("2", "Obra" ) );
		
		listaMovimientosGrid = new ArrayList<>();
		listaAlmacenes = new ArrayList<>();
		
		nuevoMovimiento();
		llenarListaAlmacenes();
		iniciarBusquedaObras();
		nuevaBusquedaProductos();
		
		this.listaEmpleados = this.ifzEmpleado.findAllActivos();
		
		this.cargarCboEmpleadoEntrega();
		this.cargarCboEmpleadoRecibe();
	}
	

	public void buscar(){
		listaMovimientosGrid.clear();
		iniciarBanderas();
		
		try {

			if (this.valorBusqueda.equals("")) {
				log.info("Buscar todos");
				listaMovimientosGrid = this.ifzMovimientos.findExtByEspecificField("nombreAlmacen", this.valorBusqueda, this.TIPO_MOVIMIENTO);
			} else {
				if(this.campoBusqueda==1){//buscar por almacen
					log.info("Buscar por almacen: "+this.valorBusqueda);
					listaMovimientosGrid = this.ifzMovimientos.findExtByEspecificField("nombreAlmacen", this.valorBusqueda, this.TIPO_MOVIMIENTO);
				} else {//buscar por obra
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
    		throw e;
    	}
	}
	
	public void nuevoMovimiento(){
		this.puedeEditar = true;
		this.pojoMovimiento = new AlmacenMovimientosExt();
		this.pojoObra = new Obra();
		this.pojoEmpleadoEntrega = new Empleado();
		this.pojoEmpleadoRecibe = new Empleado();

		this.listaProductosSalida = new ArrayList<>();
		this.numeroItems = 0;
		
		/*
		ProductoExt pe = this.ifzProductos.findExtByClave("R001");
		MovimientosDetalleExt m = new MovimientosDetalleExt();
		m.setProducto(pe);
		
		this.listaProductosSalida.add(m);

		pe = new ProductoExt();
		pe = this.ifzProductos.findExtByClave("ICAB0005");
		m = new MovimientosDetalleExt();
		m.setProducto(pe);
		this.listaProductosSalida.add(m);
		*/
		
		/*
		this.listaProductosSalida.clear();
		*/
	}

	public void editar(){
		this.puedeEditar = false;
	}

	public void guardar(){
		iniciarOperacion();
		
		try{
			if( this.pojoMovimiento.getId() == null ){	//Guardar movimiento
				if( !validaGuardarMovimiento() ) return;
				
				log.info("Almacen: "+pojoMovimiento.getAlmacen().getId());
				
				this.pojoMovimiento.setCreadoPor(this.usuarioId);
				this.pojoMovimiento.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoMovimiento.setModificadoPor(this.usuarioId);
				this.pojoMovimiento.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoMovimiento.setTipo(this.TIPO_MOVIMIENTO);
				this.pojoMovimiento.setIdObra(this.pojoObra.getId());
				this.pojoMovimiento.setNombreObra(this.pojoObra.getNombre());
				
				this.pojoMovimiento.setId(  this.ifzMovimientos.save(this.pojoMovimiento) );
				
				for(MovimientosDetalleExt md: this.listaProductosSalida) {
					md.setIdAlmacenMovimiento(this.pojoMovimiento.getId());
					md.setCreadoPor(this.usuarioId);
					md.setFechaCreacion(Calendar.getInstance().getTime());
					md.setModificadoPor(this.usuarioId);
					md.setFechaModificacion(Calendar.getInstance().getTime());
					
					this.ifzMovimientosDetalle.save(md);
				}
				
				//Ahora, afectar las existencias
				for(MovimientosDetalleExt md:listaProductosSalida){
					AlmacenProducto ap = this.ifzAlmacenProducto.findAlmacenProducto(this.pojoMovimiento.getAlmacen().getId(), md.getProducto().getId());
					
					if(ap!=null){
						//Actualizamos la existencia del producto
						log.info("Existencia: "+ap.getExistencia()+", Cantidad: "+ md.getCantidad() + ", CantidadExistente: "+md.getCantidadSolicitada());
						ap.setExistencia( ap.getExistencia() - md.getCantidad() );
						this.ifzAlmacenProducto.update(ap);
					}	
				}
				
				this.listaMovimientosGrid.add(this.pojoMovimiento);
				
			}
			
			nuevoMovimiento();
			this.operacionCompleta = true;
			this.resOperacion = "";
			this.band = false;
			this.tipoMensaje = 0;
		} catch (ExcepConstraint e) {
			this.operacionCompleta = false;
			this.resOperacion = "Error al guardar";
			e.printStackTrace();
		}
	}
	
	public void eliminar(){
		log.info("Eliminar");
		for( MovimientosDetalleExt md: this.listaProductosSalida ){
			log.info("pojoMovimientoDetalleEliminar: "+this.pojoMovimientoDetalleEliminar.getProducto().getClave()  +", " +md.getProducto().getClave() ) ;
			if(md.getProducto().getClave().equals( this.pojoMovimientoDetalleEliminar.getProducto().getClave() )){
				this.listaProductosSalida.remove( md );
				break;
			}
		}
		
		if(this.listaProductosSalida.size() == 0){
			this.almacenAnterior = 0;
			this.numeroItems = 0;
		}
	}
	
	private void iniciarBusquedaObras(){
		// Busqueda obras
		tiposBusquedaObras = new ArrayList<SelectItem>();
		tiposBusquedaObras.add(new SelectItem("nombre", "Nombre"));
		tiposBusquedaObras.add(new SelectItem("Clave", "Clave"));
		campoBusquedaObras = tiposBusquedaObras.get(0).getDescription();
		valorBusquedaObras = "";
		valorOpcionBusquedaObras = 1;

		this.listObrasPrincipales 	= new ArrayList<Obra>();
	}

	public void verificarProducto(){
		
	}
	
	private void llenarListaAlmacenes(){
		this.listaCboAlmacenes = null;
		this.listaCboAlmacenes = new ArrayList<SelectItem>();
		this.listaAlmacenes = this.ifzAlmacen.findAllActivos();
		
		this.listaCboAlmacenes.add( new SelectItem("0", "" ) );
		for( Almacen a: this.listaAlmacenes ){
			this.listaCboAlmacenes.add( new SelectItem( a.getId() , a.getNombre()  ) );
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
		
	public void buscarProductos(){
		this.iniciarBanderas();
		
		this.listaBusquedaProductos.clear();
		if ("".equals(this.valorBusquedaProductos)) {
			this.listaBusquedaProductos = this.ifzProductos.findAll();
		} else {
			if(this.campoBusquedaProductos.equals("nombre")) {	//por nombre
				log.info("Buscar por nombre");
				this.listaBusquedaProductos = this.ifzProductos.findByNombre(this.valorBusquedaProductos);
			} else { //por ID
				log.info("Buscar por clave");
				ProductoExt p = this.ifzProductos.findExtByClave(valorBusquedaProductos);
				if (p.getId() != null)
					this.listaBusquedaProductos.add( this.ifzProductos.findById(p.getId()) );
			}
			
			if (this.listaBusquedaProductos.isEmpty()) {
				log.info("la busqueda no regreso resultados");
			}
		}
	}
	
	public void seleccionarProducto(){
		this.operacionCompleta = false;
		
		if(this.pojoProductoBusqueda.getId()!=null){
			if( !validaAgregarProducto() ) return;
			
			ProductoExt p =  this.ifzProductos.findExtById(pojoProductoBusqueda.getId());
			productoSalidaSeleccionado = new MovimientosDetalleExt();
			productoSalidaSeleccionado.setProducto(p);
			this.listaProductosSalida.add( productoSalidaSeleccionado );
			
			this.pojoProductoBusqueda = new Producto();
			this.operacionCompleta = true;
			this.numeroItems = this.listaProductosSalida.size();
			
			if(almacenAnterior==0){	//Asignar por primera vez el almacen
				this.setAlmacenAnterior(this.pojoMovimiento.getAlmacen().getId());
				log.info("Asignar almacen anterior: "+almacenAnterior);
			}
		}
	}
	
	public void eliminarProductosGrid(){
		this.listaProductosSalida.clear();
		this.numeroItems = 0;
		this.almacenAnterior = 0;
	}
	
	public void regresarAlmacen(){
		log.info("Almacen anterior:"+this.getIdAlmacen());
	}
	
	private boolean validaAgregarProducto(){
		
		this.resOperacion = "";
		this.tipoMensaje = 0;
		
		if(isProductoEnLista( )){
			//this.resOperacion = "El producto ya se encuentra en la lista";
			this.tipoMensaje = -10;
			log.info(resOperacion);
			return false;
		}
		
		return true;
	}
	
	private boolean isProductoEnLista(){
		for(MovimientosDetalleExt p: this.listaProductosSalida){
			if( this.pojoProductoBusqueda.getClave().equals( p.getProducto().getClave() )  ){
				return true;
			}
		}
		return false;
	}
	
	public void validaCabeceraSalida(){
		this.resOperacion = "";
		this.operacionCompleta = true;
		
		this.tipoMensaje = 0;
		
		if(this.pojoMovimiento.getAlmacen()==null){
			//this.resOperacion = "Seleccione Almacen";
			this.tipoMensaje = -13;
			log.info("validaCabeceraSalida(): "+resOperacion );
			this.operacionCompleta = false;
			return;
		}
		
		this.nuevaBusquedaProductos();
		
	}
	
	private void nuevaBusquedaProductos(){
		// Busqueda Productos
		tiposBusquedaProductos = new ArrayList<SelectItem>();
		tiposBusquedaProductos.add(new SelectItem("nombre", "Nombre"));
		tiposBusquedaProductos.add(new SelectItem("clave", "Clave"));
		campoBusquedaObras = tiposBusquedaProductos.get(0).getDescription();
		valorBusquedaObras = "";
		valorOpcionBusquedaObras = 1;
		this.listaBusquedaProductos = new ArrayList<>();
	}
	
	public String buscarObras() {
    	try {
			System.out.println("----------> buscarObras <----------");
			iniciarBanderas();
			
			if ("".equals(this.valorOpcionBusquedaObras))
				this.valorOpcionBusquedaObras = 1;
			
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = "id";
			
			this.listObrasPrincipales = this.ifzObras.findObraPrincipalLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.valorOpcionBusquedaObras, (this.pojoObra.getId() == null ? 0 : this.pojoObra.getId()));
			if(this.listObrasPrincipales.isEmpty()){
	    		setBanderas(true, 2, "Error");
	    		return this.mensaje;
			}

			this.mensaje = "OK";
    	} catch (Exception e) {
    		this.band = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en buscarObras", e);
    	}
    	
    	return this.mensaje;
    }
	
	public void buscarOrdenCompra(){
		
	}

	private void iniciarOperacion(){
		this.operacionCompleta = false;
		this.resOperacion = "";
	}
		
	private boolean validaGuardarMovimiento(){
		
		this.resOperacion = "";
		
		this.tipoMensaje = 0;
		if(this.pojoObra == null){
			//this.resOperacion = "Seleccione Obra";
			this.tipoMensaje = -11;
			return false;
		}
		
		if(this.pojoObra.getId() == null){
			//this.resOperacion = "Seleccione Obra";
			this.tipoMensaje = -12;
			return false;
		}
		
		if( this.pojoMovimiento.getAlmacen() == null ){
			//this.resOperacion = "Seleccione almacén";
			this.tipoMensaje = -13;
			return false;
		}
		
		if( this.pojoEmpleadoEntrega.getId() == null ){
			//this.resOperacion = "Indique el empleado que entrega";
			this.tipoMensaje = -14;
			return false;
		}
		
		if( this.pojoEmpleadoRecibe.getId() == null ){
			//this.resOperacion = "Indique el empleado que recibe";
			this.tipoMensaje = -15;
			return false;
		}
		
		if( this.pojoEmpleadoRecibe.getId().compareTo(this.pojoEmpleadoEntrega.getId()) == 0 ){
			//this.resOperacion = "La persona que entrega y que recibe deben ser diferentes";
			this.tipoMensaje = -16;
			return false;
		}

		if( this.listaProductosSalida.isEmpty() ){
			this.resOperacion = "Debe existir por lo menos un producto en la lista";
			this.tipoMensaje = -17;
			return false;
		}
		
		//Revisar si alguno se encuentra en cero
		for( MovimientosDetalleExt md: this.listaProductosSalida){
			if ( md.getCantidad() == 0 ){
				//this.resOperacion = "La cantidad de producto no puede ser cero.";
				this.tipoMensaje = -18;
				return false;
			}
		}
		
		//Si en alguno de los productos se encuentra que la cantidad es mayor a la que hay en almacen, salir
		for( MovimientosDetalleExt md: this.listaProductosSalida){
			if ( md.getCantidad() > md.getCantidadSolicitada() ){
				this.resOperacion = "No existe suficiente cantidad de producto.";
				this.tipoMensaje = -19;
				return false;
			}
		}
		
		return true;
	}
	
	public void seleccionarObra() {
		
	}
	
	private void cargarCboEmpleadoEntrega(){
		listaCboEmpleadoEntrega = null;
		listaCboEmpleadoEntrega = new ArrayList<>();
		this.listaCboEmpleadoEntrega.add( new SelectItem( "0" , "" ) );
		for( Empleado e: this.listaEmpleados ){
			this.listaCboEmpleadoEntrega.add( new SelectItem( e.getId() , e.getNombre() ) );
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
	
	public void setIdEmpleadoEntrega(long idEmpleado){
		if(idEmpleado > 0){
			this.pojoEmpleadoEntrega = this.ifzEmpleado.findById(idEmpleado);
			this.pojoMovimiento.setEntrega(this.pojoEmpleadoEntrega);
		}
	}
	
	public Date getFecha(){
		return this.pojoMovimiento.getFecha() == null ? Calendar.getInstance().getTime() : this.pojoMovimiento.getFecha(); 
	}
	
	public void setFecha(Date fecha){
		this.pojoMovimiento.setFecha(fecha);
	}
	
	public long getIdEmpleadoEntrega(){
		return this.pojoEmpleadoEntrega == null ? 0 : this.pojoEmpleadoEntrega.getId() == null ? 0 : this.pojoEmpleadoEntrega.getId(); 
	}
	
	public void setIdEmpleadoRecibe(long idEmpleado){
		if(idEmpleado > 0){
			this.pojoEmpleadoRecibe = this.ifzEmpleado.findById(idEmpleado);
			this.pojoMovimiento.setRecibe(pojoEmpleadoRecibe);
		}	
	}
	
	public long getIdEmpleadoRecibe(){
		return this.pojoEmpleadoRecibe == null ? 0 : this.pojoEmpleadoRecibe.getId() == null ? 0 : this.pojoEmpleadoRecibe.getId();
	}
	
	public void setIdAlmacen(long idAlmacen){
		this.pojoMovimiento.setAlmacen(this.ifzAlmacen.findById(idAlmacen));
	}
	
	public long getIdAlmacen(){
		return this.pojoMovimiento==null? 0 : pojoMovimiento.getAlmacen()==null ? 0 : pojoMovimiento.getAlmacen().getId();
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

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
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
		this.setIdEmpleadoEntrega(this.pojoMovimiento.getEntrega().getId());
		this.setIdEmpleadoRecibe(this.pojoMovimiento.getRecibe().getId());
		
		cargarDetalleMovimientos(this.pojoMovimiento.getId());
		
	}

	private void cargarDetalleMovimientos(long idAlmacenMovimiento){
		this.listaProductosSalida = this.ifzMovimientosDetalle.findDetallesExtById(idAlmacenMovimiento);
	}
	
	/*
	public Obra getPojoObra() {
		return pojoObra;
	}
	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
		//this.pojoMovimiento.setObra(pojoObra);
	}*/

	public List<Almacen> getListaAlmacenes() {
		return listaAlmacenes;
	}

	public void setListaAlmacenes(List<Almacen> listaAlmacenes) {
		this.listaAlmacenes = listaAlmacenes;
	}

	public List<SelectItem> getListaCboAlmacenes() {
		return listaCboAlmacenes;
	}

	public void setListaCboAlmacenes(List<SelectItem> listaCboAlmacenes) {
		this.listaCboAlmacenes = listaCboAlmacenes;
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

	public int getNumPaginaOrdenCompra() {
		return numPaginaOrdenCompra;
	}

	public void setNumPaginaOrdenCompra(int numPaginaOrdenCompra) {
		this.numPaginaOrdenCompra = numPaginaOrdenCompra;
	}

	public List<MovimientosDetalleExt> getListaProductosSalida() {
		return listaProductosSalida;
	}

	public void setListaProductosSalida(List<MovimientosDetalleExt> listaProductosSalida) {
		this.listaProductosSalida = listaProductosSalida;
	}

	public List<Producto> getListaBusquedaProductos() {
		return listaBusquedaProductos;
	}

	public void setListaBusquedaProductos(List<Producto> listaBusquedaProductos) {
		this.listaBusquedaProductos = listaBusquedaProductos;
	}

	public int getNumPaginaProductos() {
		return numPaginaProductos;
	}

	public void setNumPaginaProductos(int numPaginaProductos) {
		this.numPaginaProductos = numPaginaProductos;
	}

	public Producto getPojoProductoBusqueda() {
		return pojoProductoBusqueda;
	}

	public void setPojoProductoBusqueda(Producto pojoProductoBusqueda) {
		this.pojoProductoBusqueda = pojoProductoBusqueda;
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

	public int getValorOpcionBusquedaProductos() {
		return valorOpcionBusquedaProductos;
	}

	public void setValorOpcionBusquedaProductos(int valorOpcionBusquedaProductos) {
		this.valorOpcionBusquedaProductos = valorOpcionBusquedaProductos;
	}

	public boolean isOperacionCompleta() {
		return operacionCompleta;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCompleta = operacionCompleta;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public AlmacenMovimientosExt getPojoMovimientoEditar() {
		return pojoMovimientoEditar;
	}

	public void setPojoMovimientoEditar(AlmacenMovimientosExt pojoMovimientoEditar) {
		this.pojoMovimientoEditar = pojoMovimientoEditar;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}


	public Empleado getPojoEmpleadoEntrega() {
		return pojoEmpleadoEntrega;
	}

	public void setPojoEmpleadoEntrega(Empleado pojoEmpleadoEntrega) {
		this.pojoEmpleadoEntrega = pojoEmpleadoEntrega;
	}

	public Empleado getPojoEmpleadoRecibe() {
		return pojoEmpleadoRecibe;
	}

	public void setPojoEmpleadoRecibe(Empleado pojoEmpleadoRecibe) {
		this.pojoEmpleadoRecibe = pojoEmpleadoRecibe;
	}

	public List<SelectItem> getListaCboEmpleadoEntrega() {
		return listaCboEmpleadoEntrega;
	}

	public void setListaCboEmpleadoEntrega(List<SelectItem> listaCboEmpleadoEntrega) {
		this.listaCboEmpleadoEntrega = listaCboEmpleadoEntrega;
	}

	public List<SelectItem> getListaCboEmpleadoRecibe() {
		return listaCboEmpleadoRecibe;
	}

	public void setListaCboEmpleadoRecibe(List<SelectItem> listaCboEmpleadoRecibe) {
		this.listaCboEmpleadoRecibe = listaCboEmpleadoRecibe;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public MovimientosDetalleExt getPojoMovimientoDetalleEliminar() {
		return pojoMovimientoDetalleEliminar;
	}

	public void setPojoMovimientoDetalleEliminar(MovimientosDetalleExt pojoMovimientoDetalleEliminar) {
		log.info("Seleccionado");
		this.pojoMovimientoDetalleEliminar = pojoMovimientoDetalleEliminar;
	}

	public int getNumeroItems() {
		return numeroItems;
	}

	public void setNumeroItems(int numeroItems) {
		this.numeroItems = numeroItems;
	}

	public int getRowSeleccionado() {
		return rowSeleccionado;
	}

	public void setRowSeleccionado(int rowSeleccionado) {
		log.info("row: "+rowSeleccionado);
		this.rowSeleccionado = rowSeleccionado;
	}

	public double getCantidadProductoSeleccionado() {
		return cantidadProductoSeleccionado;
	}

	public void setCantidadProductoSeleccionado(double cantidadProductoSeleccionado) {
		this.cantidadProductoSeleccionado = cantidadProductoSeleccionado;
	}

	public Collection<Object> getSelection() {
		return selection;
	}

	public void setSelection(Collection<Object> selection) {
		this.selection = selection;
	}

	public long getAlmacenAnterior() {
		return almacenAnterior;
	}

	public void setAlmacenAnterior(long almacenAnterior) {
		this.almacenAnterior = almacenAnterior;
	}
	

	public void selectionListener(AjaxBehaviorEvent event) {
		
		if(!this.puedeEditar) return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
		
        AbstractExtendedDataTable dataTable = (AbstractExtendedDataTable) event.getComponent();
        Object originalKey = dataTable.getRowKey();
        selectionItems.clear();
        //int row = 0;
        for (Object selectionKey : selection) {
            dataTable.setRowKey(selectionKey);
            //row++;
            if (dataTable.isRowAvailable()) {
            	movimientoSeleccionado = (MovimientosDetalleExt) dataTable.getRowData();
            	log.info("Elemento seleccionado: "+movimientoSeleccionado.getProducto().getId()+", Producto: "+movimientoSeleccionado.getProducto().getDescripcion() + ", Cant:"+movimientoSeleccionado.getCantidad());
                cantidadProductoSeleccionado = this.ifzAlmacenProducto.findCantidadEnAlmacen(this.pojoMovimiento.getAlmacen().getId(), movimientoSeleccionado.getProducto().getId());
            	log.info("Cantidad del producto en Almacen: "+cantidadProductoSeleccionado);
            	
            	//asignar la cantidad que existe en almacen al produdcto en el campo: cantidadSolicitada
            	this.setCantidadExistente(movimientoSeleccionado.getProducto().getClave(), cantidadProductoSeleccionado);
            }
        }
        dataTable.setRowKey(originalKey);
    }
	
	private void setCantidadExistente(String claveProducto, double cantidadExistente){
		for( MovimientosDetalleExt md: this.listaProductosSalida ){
			if( md.getProducto().getClave().equals( claveProducto ) ){
				md.setCantidadSolicitada(cantidadExistente);
				break;
			}
		}
	}
	
	public void validaCantidadProducto(){
		int row = 0;
		
		this.tipoMensaje = 0;
		
		log.info("validaCantidadProducto()");
		
		if(!this.puedeEditar) return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
		
		if(listaProductosSalida.isEmpty()) return;
		
		MovimientosDetalleExt movimiento = new MovimientosDetalleExt() ;
		for(MovimientosDetalleExt md:this.listaProductosSalida){
			if(md.getProducto().getClave().equals(this.movimientoSeleccionado.getProducto().getClave())){
				movimiento = md;
				log.info("Revisar: Almacen: "+this.pojoMovimiento.getAlmacen().getId()+", Producto: "+md.getProducto().getId());
				cantidadProductoSeleccionado = this.ifzAlmacenProducto.findCantidadEnAlmacen(this.pojoMovimiento.getAlmacen().getId(), md.getProducto().getId());
				log.info("Cantidad Producto: "+cantidadProductoSeleccionado + " Row: "+row+", Cant en Movimiento:"+movimiento.getCantidad());
			}
			row++;
		}
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}
}

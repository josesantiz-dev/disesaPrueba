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

import org.apache.log4j.Logger;
import org.richfaces.component.AbstractExtendedDataTable;

import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;


@ManagedBean
@ViewScoped
public class TraspasosAction implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TraspasosAction.class);

	private InitialContext ctx;

	private AlmacenTraspasoRem		ifzTraspaso;
	private TraspasoDetalleRem		ifzTraspasoDetalle;
	private AlmacenRem 				ifzAlmacen;
	private ProductoRem 			ifzProductos;
	private AlmacenProductoRem		ifzAlmacenProducto;
	private EmpleadoRem 			ifzEmpleado;

    private Integer usuarioId;	    
	private int numPagina;
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
	
	private List<Almacen> listaAlmacenes;
	private List<SelectItem> listaCboAlmacenSalida;
	private List<SelectItem> listaCboAlmacenDestino;
	
	private List<Empleado> listaEmpleados;
	private List<SelectItem> listaCboEmpleadoEntrega;
	private List<SelectItem> listaCboEmpleadoRecibe;
	
	private AlmacenTraspasoExt pojoTraspaso;
	private TraspasoDetalleExt pojoTraspasoDetalleEliminar;
	private List<AlmacenTraspasoExt> listaTraspasoGrid;
	private List<TraspasoDetalleExt> listaTraspasoDetalleGrid;
	
	private List<TraspasoDetalleExt> listaDetalleCantidades;	//Lista para respaldar las cantidades de productos
	
	//Busqueda Productos
	private List<Producto> 			listaBusquedaProductos; 
	private List<SelectItem> 		tiposBusquedaProductos;	
	private String 					campoBusquedaProductos;
	private String 					valorBusquedaProductos;
	private int 					valorOpcionBusquedaProductos;
	private Producto 				pojoProductoBusqueda;
	
	//Variables para esta el movimiento de salida
	private TraspasoDetalleExt traspasoSeleccionado;
	private int cantidadProductoDetalles;
	
	//Variables para la extendedDataTable
	private Collection<Object> selection;
	private List<MovimientosDetalleExt> selectionItems;
	private double cantidadProductoSeleccionado;
	private int rowSeleccionado;
	
	private long almacenAnterior;
		
	
	public TraspasosAction() throws NamingException,Exception {
		String ejbName;
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
		
		this.numPagina = 1;
		this.ctx = new InitialContext();

		ejbName = "ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem";
		this.ifzTraspaso = (AlmacenTraspasoRem) ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem";
		this.ifzTraspasoDetalle = (TraspasoDetalleRem) ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem";
		this.ifzAlmacen = (AlmacenRem) ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem";
		this.ifzProductos = (ProductoRem) this.ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem";
		this.ifzAlmacenProducto = (AlmacenProductoRem) this.ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem";
		this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup(ejbName);
		
		this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
		
		//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		//PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		
		this.cargarOpcionesBusqueda();
		
		this.selectionItems = new ArrayList<MovimientosDetalleExt>();			//Para cuando se seleccione el row del ExtendedDataTable
		this.listaEmpleados = this.ifzEmpleado.findAllActivos();
		this.listaAlmacenes = this.ifzAlmacen.findAllActivos();
		
		this.cargarCboEmpleadoEntrega();
		this.cargarCboEmpleadoRecibe();
		this.cargarCboAlmacenSalida();
		this.cargarCboAlmacenDestino();

		this.listaTraspasoGrid = new ArrayList<>();
		this.nuevoTraspaso();
		this.nuevaBusquedaProductos();
	}

	
	public void buscar(){
		this.listaTraspasoGrid = new ArrayList<>();
		this.listaTraspasoGrid.clear();
		
		iniciarBanderas();
		
		try {
			if ( this.valorBusqueda.equals("") ){
				this.listaTraspasoGrid = this.ifzTraspaso.findAllExt();
			}else{
				if(this.campoBusqueda==1){//buscar por almacen origen
					log.info("Buscar por almacen origen: "+this.valorBusqueda);
					this.listaTraspasoGrid = this.ifzTraspaso.findExtByAlmacenOrigen(this.valorBusqueda);
				}else{//buscar por almacen destino
					log.info("Buscar por almacen destino: "+this.valorBusqueda);
					this.listaTraspasoGrid = this.ifzTraspaso.findExtByAlmacenDestino(this.valorBusqueda);
				}
			}
			
			if (this.listaTraspasoGrid.isEmpty()) {
				setBanderas( true, 2, "" );
			}
			
		}catch (Exception e) {
			setBanderas( true, 1, "Error al buscar" );
    		log.error("Error en buscar", e);
    		throw e;
    	}
	}

	public void validaCabeceraTraspaso(){
		this.resOperacion = "";
		this.operacionCompleta = true;

		if(this.pojoTraspaso.getAlmacenOrigen()==null){
			this.tipoMensaje = -10;
			this.resOperacion = "Debe seleccionar almacén de Salida";
			log.info("resOperacion: " + resOperacion);
			this.operacionCompleta = false;
			return;
		}
		
		/* if(this.pojoTraspaso.getAlmacenDestino()==null){
			this.resOperacion = "Debe seleccionar almacén Destino";
			log.info("resOperacion: "+resOperacion);
			this.operacionCompleta = false;
			return;
		} */
		
		this.nuevaBusquedaProductos();
	}
	
	private void nuevaBusquedaProductos(){
		// Busqueda Productos
		tiposBusquedaProductos = new ArrayList<SelectItem>();
		tiposBusquedaProductos.add(new SelectItem("nombre", "Nombre"));
		tiposBusquedaProductos.add(new SelectItem("clave", "Clave"));
		this.listaBusquedaProductos = new ArrayList<>();
	}
	
	public void buscarProductos() {
		this.iniciarBanderas();
		this.band = false;
		this.resOperacion = "";
		this.tipoMensaje = 0;
		
		this.listaBusquedaProductos.clear();
		if ("".equals(this.valorBusquedaProductos)) {
			List<AlmacenProductoExt> listaExt = this.ifzAlmacenProducto.findExistentesExt("", this.pojoTraspaso.getAlmacenOrigen().getId(), "");
			
			for(AlmacenProductoExt var: listaExt){
				if(var.getProducto() != null)
					this.listaBusquedaProductos.add(var.getProducto());
			}
		} else {
			if(this.campoBusquedaProductos.equals("nombre")) {	//por nombre
				//this.listaBusquedaProductos = this.ifzProductos.findByNombre(this.valorBusquedaProductos);
				List<AlmacenProductoExt> listaExt = this.ifzAlmacenProducto.findExistentesExt("descripcion", this.pojoTraspaso.getAlmacenOrigen().getId(), this.valorBusquedaProductos);
				for(AlmacenProductoExt var: listaExt){
					if(var.getProducto() != null)
						this.listaBusquedaProductos.add(var.getProducto());
				}
			} else { //por ID
				log.info("Buscar por clave");
				AlmacenProductoExt ape = this.ifzAlmacenProducto.findProductoExistenteExt("clave", this.pojoTraspaso.getAlmacenOrigen().getId(), this.valorBusquedaProductos);
				
				if(ape != null)
					this.listaBusquedaProductos.add(ape.getProducto());
			}
		}
		
		if (this.listaBusquedaProductos.isEmpty()) {
			log.info("la busqueda no regreso resultados");
			this.tipoMensaje = -20;
			this.band = true;
			this.resOperacion = "No hay productos que coincidan con la búsqueda";
		}
	}

	public void seleccionarProducto(){

		this.operacionCompleta = false;
		
		if(this.pojoProductoBusqueda.getId()!=null){
			
			if( !validaAgregarProducto() ) return;
			
			TraspasoDetalleExt td = new TraspasoDetalleExt();
			
			ProductoExt p =  this.ifzProductos.findExtById(pojoProductoBusqueda.getId()); 
			
			td.setProducto(p);
			
			this.listaTraspasoDetalleGrid.add( td ); 
			
				
			TraspasoDetalleExt td2 = new TraspasoDetalleExt();	//Se crea otro objeto para evitar problemas de clonacion en Java
			td2.setProducto(p);
			this.listaDetalleCantidades.add(td2);	//la lista de respaldo. En el campo cantidad, se guardará la cantidad que existe de producto
			
			this.pojoProductoBusqueda = new Producto();
			
			this.operacionCompleta = true;
			
			cantidadProductoDetalles = this.listaTraspasoDetalleGrid.size();
			
			if(almacenAnterior==0){	//Asignar por primera vez el almacen
				this.setAlmacenAnterior(this.pojoTraspaso.getAlmacenOrigen().getId());
				log.info("Asignar almacen anterior: "+almacenAnterior);
			}
			
		}
		
		log.info("seleccionarProducto() --> this.operacionCompleta: "+this.operacionCompleta);
		
	}

	private boolean isProductoEnLista(){
		for(TraspasoDetalleExt p: this.listaTraspasoDetalleGrid){
			if( this.pojoProductoBusqueda.getClave().equals( p.getProducto().getClave() )  ){
				return true;
			}
		}
		return false;
	}
	
	private boolean validaAgregarProducto(){
			
			this.resOperacion = "";
			
			this.tipoMensaje = 0;
			
			if(isProductoEnLista( )){
				//this.resOperacion = "El producto ya se encuentra en la lista";
				this.tipoMensaje = -11;
				log.info(resOperacion);
				return false;
			}
			
			return true;
		}
	
	public void guardar(){
		iniciarOperacion();
		
		try {
			//int guardar = 0;
			this.pojoTraspaso.setEstatus(0);
			this.pojoTraspaso.setModificadoPor(this.usuarioId);
			this.pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
			
			if( this.pojoTraspaso.getId() == null ) {
				if (! validaGuardarTraspaso()) return;
				
				log.info("Entrega: "+this.pojoTraspaso.getEntrega().getId() + ", Recibe: "+this.pojoTraspaso.getRecibe().getId());
				
				this.pojoTraspaso.setCreadoPor(this.usuarioId);
				this.pojoTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
				
				//almacen: 1-> principal, 2-> obra
				//Se verifican los almacenes involucrados en el movimiento:
				//Si el almacén origen corresponde a una obra y el destino es un almacén principal, el registro del traspaso se marca como "Devolución".
				//AlmacenTraspaso: tipo 1 = traspaso, tipo 2 = devolucion
				if (this.pojoTraspaso.getAlmacenOrigen().getTipo() == 2 && this.pojoTraspaso.getAlmacenDestino().getTipo() == 1 ) {
					this.pojoTraspaso.setTipo(2);
				} else {
					this.pojoTraspaso.setTipo(1);
				}
				
				this.pojoTraspaso.setId(this.ifzTraspaso.save(this.pojoTraspaso));
				
				for(TraspasoDetalleExt td:this.listaTraspasoDetalleGrid) {
					td.setIdAlmacenTraspaso(this.pojoTraspaso.getId());
					td.setCreadoPor(this.usuarioId);
					td.setEstatus(1);
					td.setFechaCreacion(Calendar.getInstance().getTime());
					td.setModificadoPor(this.usuarioId);
					td.setFechaModificacion(Calendar.getInstance().getTime());
					td.setEstatus(1);	//en transito
					
					this.ifzTraspasoDetalle.save(td);
					
					double cantidadProducto = this.getCantidadProductoExistencias(td.getProducto().getClave());
					
					log.info("Producto: "+td.getProducto().getClave()+", Cantidad: "+td.getCantidad()+", Cant. Almacen: "+cantidadProducto);
					
					//Identificar donde restar el producto
					AlmacenProducto almacenSalida = this.ifzAlmacenProducto.findAlmacenProducto(this.pojoTraspaso.getAlmacenOrigen().getId(), td.getProducto().getId());
					//Identificar donde agregar el producto
					//AlmacenProducto almacenEntrada = this.ifzAlmacenProducto.findAlmacenProducto(this.pojoTraspaso.getAlmacenDestino().getId(), td.getProducto().getId());
					
					if(almacenSalida != null) {
						almacenSalida.setExistencia(almacenSalida.getExistencia() - td.getCantidad());
						//almacenEntrada.setExistencia( almacenEntrada.getExistencia() + td.getCantidad() );	//Se elimina de aqui el proceso de entrada a almacen
						
						this.ifzAlmacenProducto.update(almacenSalida);
						//this.ifzAlmacenProducto.update(almacenEntrada);					//Se elimina de aqui el proceso de entrada a almacen
					}
				}
				
				try {
					this.listaTraspasoGrid.add(this.pojoTraspaso);
				} catch (Exception e1) {
					log.info("Error al agregar el pojo a la lista");
					e1.printStackTrace();
				}
			}
			
			nuevoTraspaso();
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

	private boolean validaGuardarTraspaso() {
		this.resOperacion = "";
		this.tipoMensaje = 0;

		if( this.pojoTraspaso.getAlmacenOrigen() == null ){
			//this.resOperacion = "Seleccione Almacén de Salida";
			this.tipoMensaje = -10;
			return false;
		} else {
			if(this.pojoTraspaso.getAlmacenOrigen().getId()== null || this.pojoTraspaso.getAlmacenOrigen().getId() == 0){
				//this.resOperacion = "Seleccione Almacén de Salida";
				this.tipoMensaje = -10;
				return false;
			}
		}

		if( this.pojoTraspaso.getAlmacenDestino() == null ){
			//this.resOperacion = "Seleccione Almacén Destino";
			this.tipoMensaje = -12;
			return false;
		} else {
			if(this.pojoTraspaso.getAlmacenDestino().getId()== null || this.pojoTraspaso.getAlmacenDestino().getId() == 0){
				//this.resOperacion = "Seleccione Almacén Destino";
				this.tipoMensaje = -12;
				return false;
			}
		}
		
		if( this.pojoTraspaso.getAlmacenOrigen().getId().compareTo( this.pojoTraspaso.getAlmacenDestino().getId() ) == 0){
			//this.resOperacion = "El almacén de salida no puede ser el mismo de Destino";
			this.tipoMensaje = -13;
			return false;
		}
		
		if( this.pojoTraspaso.getEntrega() == null ){
			//this.resOperacion = "Indique el empleado que entrega";
			this.tipoMensaje = -14;
			return false;
		}
		
		if( this.pojoTraspaso.getEntrega().getId() == 0 ){
			//this.resOperacion = "Indique el empleado que entrega";
			this.tipoMensaje = -14;
			return false;
		}
		
		if( this.pojoTraspaso.getRecibe() == null ){
			//this.resOperacion = "Indique el empleado que recibe";
			this.tipoMensaje = -15;
			return false;
		}
		
		if( this.pojoTraspaso.getRecibe().getId().compareTo(this.pojoTraspaso.getEntrega().getId()) == 0 ){
			//this.resOperacion = "El empleado que entrega no puede ser el mismo que recibe";
			this.tipoMensaje = -16;
			return false;
		}
		
		if( this.listaTraspasoDetalleGrid.isEmpty() ){
			//this.resOperacion = "Debe existir por lo menos un producto en la lista";
			this.tipoMensaje = -17;
			return false;
		}
		
		//Revisar si alguno se encuentra en cero
		for( TraspasoDetalleExt d: this.listaTraspasoDetalleGrid){
			if ( d.getCantidad() == 0 ){
				//this.resOperacion = "Indique cantidad de "+d.getProducto().getDescripcion() + " a entregar.";
				this.tipoMensaje = -18;
				return false;
			}
		}
		
		//revisar si existe suficiente
		/*
		for( TraspasoDetalleExt d: this.listaTraspasoDetalleGrid){
			double cantidadProducto = this.ifzAlmacenProducto.findCantidadEnAlmacen(this.pojoTraspaso.getAlmacenOrigen().getId(), d.getProducto().getId());
			if ( d.getCantidad() > cantidadProducto ){
				this.resOperacion = "No existe suficiente cantidad de "+d.getProducto().getDescripcion() + " en almacen "+this.pojoTraspaso.getAlmacenOrigen().getNombre();
				return false;
			}
		}
		*/
		//por último, validar las cantidades existentes contra las ingresadas
		for( TraspasoDetalleExt d: this.listaTraspasoDetalleGrid){
			double cantidadProducto = this.getCantidadProductoExistencias(d.getProducto().getClave());
			if ( d.getCantidad() > cantidadProducto ){
				this.resOperacion = "Cantidad de producto Insuficiente";
				this.tipoMensaje = -19;
				return false;
			}
		}
		
		return true;
	}
	
	
	public void nuevoTraspaso(){
		this.puedeEditar = true;
		this.pojoTraspaso = new AlmacenTraspasoExt();
		this.listaTraspasoDetalleGrid = new ArrayList<>();
		cantidadProductoDetalles = 0;
		listaDetalleCantidades = new ArrayList<>();
	}
	
	private void iniciarOperacion(){
		this.operacionCompleta = false;
		this.resOperacion = "";
	}
	
	public void editar(){
		this.puedeEditar = false;
	}
	
	public void eliminar(){
		for( TraspasoDetalleExt td: this.listaTraspasoDetalleGrid ){
			if(td.getProducto().getClave().equals( this.pojoTraspasoDetalleEliminar.getProducto().getClave() )){
				this.listaTraspasoDetalleGrid.remove( td );
				break;
			}
		}
		
		if(this.listaTraspasoDetalleGrid.size() == 0){
			this.almacenAnterior = 0;
			this.cantidadProductoDetalles = 0;
		}
	}
	

	public void eliminarProductosGrid(){
		this.listaTraspasoDetalleGrid.clear();
		this.cantidadProductoDetalles = 0;
		this.almacenAnterior = 0;
	}
	
	public void regresarAlmacen(){
		log.info("Almacen anterior:");
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
	
	private void cargarOpcionesBusqueda(){
		if (this.listaCampoBusqueda == null)
			this.listaCampoBusqueda = new ArrayList<SelectItem>();
		this.listaCampoBusqueda.clear();
		
		this.listaCampoBusqueda.add( new SelectItem("1", "Almacen Origen" ) );
		this.listaCampoBusqueda.add( new SelectItem("2", "Almacen Destino" ) );
	}
	
	private void cargarCboAlmacenSalida() {
		if (this.listaCboAlmacenSalida == null)
			this.listaCboAlmacenSalida = new ArrayList<SelectItem>();
		this.listaCboAlmacenSalida.clear();
		
		for(Almacen a : this.listaAlmacenes) {
			this.listaCboAlmacenSalida.add(new SelectItem(a.getId(), a.getNombre()));
		}
	}
	
	public void cargarCboAlmacenDestino() {
		if (this.listaCboAlmacenDestino == null)
			this.listaCboAlmacenDestino = new ArrayList<SelectItem>();
		this.listaCboAlmacenDestino.clear();

		for(Almacen a : this.listaAlmacenes) {
			if (this.getIdAlmacenSalida() > 0 && this.getIdAlmacenSalida() == a.getId()) continue;
			if (a.getTipo() != 1)  continue;
			this.listaCboAlmacenDestino.add( new SelectItem(a.getId(), a.getNombre()));
		}
	}
	
	private void cargarCboEmpleadoEntrega(){
		if (this.listaCboEmpleadoEntrega == null)
			this.listaCboEmpleadoEntrega = new ArrayList<SelectItem>();
		this.listaCboEmpleadoEntrega.clear();
		
		for(Empleado e : this.listaEmpleados ){
			this.listaCboEmpleadoEntrega.add(new SelectItem(e.getId(), e.getNombre()));
		}
	}
	
	private void cargarCboEmpleadoRecibe(){
		if (this.listaCboEmpleadoRecibe == null)
			this.listaCboEmpleadoRecibe = new ArrayList<SelectItem>();
		this.listaCboEmpleadoRecibe.clear();

		for(Empleado e : this.listaEmpleados ){
			this.listaCboEmpleadoRecibe.add(new SelectItem(e.getId(), e.getNombre()));
		}
	}

	//  --------------------------------------------------------------------------------------------------------------------------------------------
	public void setIdEmpleadoEntrega(long idEmpleado){
		if(idEmpleado > 0){
			for(Empleado e:this.listaEmpleados){
				if(e.getId()==idEmpleado){
					this.pojoTraspaso.setEntrega(e);
					break;
				}
			}
		}
	}
	
	public long getIdEmpleadoEntrega(){
		return this.pojoTraspaso.getEntrega() == null ? 0 : this.pojoTraspaso.getEntrega().getId() == null ? 0 : this.pojoTraspaso.getEntrega().getId(); 
	}
	
	public void setIdEmpleadoRecibe(long idEmpleado){
		if(idEmpleado > 0){
			for(Empleado e:this.listaEmpleados){
				if(e.getId()==idEmpleado){
					this.pojoTraspaso.setRecibe(e);
					break;
				}
			}
		}	
	}
	
	public long getIdEmpleadoRecibe(){
		return this.pojoTraspaso.getRecibe() == null ? 0 : this.pojoTraspaso.getRecibe().getId() == null ? 0 : this.pojoTraspaso.getRecibe().getId();
	}

	public Date getFecha(){
		return this.pojoTraspaso.getFecha() == null ? Calendar.getInstance().getTime() : this.pojoTraspaso.getFecha(); 
	}
	
	public void setFecha(Date fecha){
		this.pojoTraspaso.setFecha(fecha);
	}

	public long getIdAlmacenSalida(){
		return this.pojoTraspaso==null? 0 : pojoTraspaso.getAlmacenOrigen()==null ? 0 : pojoTraspaso.getAlmacenOrigen().getId();
	}
	
	public void setIdAlmacenSalida(long idAlmacen){
		if(idAlmacen > 0){
			for(Almacen a:this.listaAlmacenes){
				if(a.getId()==idAlmacen){
					log.info("Almacen origen: "+idAlmacen);
					this.pojoTraspaso.setAlmacenOrigen(a);
					break;
				}
			}
		}
	}
	
	public long getIdAlmacenDestino(){
		return this.pojoTraspaso==null? 0 : pojoTraspaso.getAlmacenDestino()==null ? 0 : pojoTraspaso.getAlmacenDestino().getId();
	}
	
	public void setIdAlmacenDestino(long idAlmacen){
		if(idAlmacen > 0){
			for(Almacen a:this.listaAlmacenes){
				if(a.getId()==idAlmacen){
					this.pojoTraspaso.setAlmacenDestino(a);
					break;
				}
			}
		}
	}
	
	//  --------------------------------------------------------------------------------------------------------------------------------------------

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

	public int getNumPaginaProductos() {
		return numPaginaProductos;
	}

	public void setNumPaginaProductos(int numPaginaProductos) {
		this.numPaginaProductos = numPaginaProductos;
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

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
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

	public List<Almacen> getListaAlmacenes() {
		return listaAlmacenes;
	}

	public void setListaAlmacenes(List<Almacen> listaAlmacenes) {
		this.listaAlmacenes = listaAlmacenes;
	}

	public List<SelectItem> getListaCboAlmacenSalida() {
		return listaCboAlmacenSalida;
	}

	public void setListaCboAlmacenSalida(List<SelectItem> listaCboAlmacenSalida) {
		this.listaCboAlmacenSalida = listaCboAlmacenSalida;
	}

	public List<SelectItem> getListaCboAlmacenDestino() {
		return listaCboAlmacenDestino;
	}

	public void setListaCboAlmacenDestino(List<SelectItem> listaCboAlmacenDestino) {
		this.listaCboAlmacenDestino = listaCboAlmacenDestino;
	}

	public List<Empleado> getListaEmpleados() {
		return listaEmpleados;
	}

	public void setListaEmpleados(List<Empleado> listaEmpleados) {
		this.listaEmpleados = listaEmpleados;
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

	public AlmacenTraspasoExt getPojoTraspaso() {
		return pojoTraspaso;
	}

	public void setPojoTraspaso(AlmacenTraspasoExt pojoTraspaso) {
		this.pojoTraspaso = pojoTraspaso;

		this.setIdAlmacenSalida(pojoTraspaso.getAlmacenOrigen().getId());
		this.setIdAlmacenDestino(pojoTraspaso.getAlmacenDestino().getId());

		this.setIdEmpleadoEntrega(pojoTraspaso.getEntrega().getId());
		this.setIdEmpleadoRecibe(pojoTraspaso.getRecibe().getId());
		
		cargarDetalleTraspaso(this.pojoTraspaso.getId());
	}
	
	private void cargarDetalleTraspaso(long idAlmacenMovimiento){
		this.listaTraspasoDetalleGrid = this.ifzTraspasoDetalle.findDetallesExtById(idAlmacenMovimiento);
	}

	public List<AlmacenTraspasoExt> getListaTraspasoGrid() {
		return listaTraspasoGrid;
	}

	public void setListaTraspasoGrid(List<AlmacenTraspasoExt> listaTraspasoGrid) {
		this.listaTraspasoGrid = listaTraspasoGrid;
	}

	public List<TraspasoDetalleExt> getListaTraspasoDetalleGrid() {
		return listaTraspasoDetalleGrid;
	}

	public void setListaTraspasoDetalleGrid(List<TraspasoDetalleExt> listaTraspasoDetalleGrid) {
		this.listaTraspasoDetalleGrid = listaTraspasoDetalleGrid;
	}

	public List<Producto> getListaBusquedaProductos() {
		return listaBusquedaProductos;
	}

	public void setListaBusquedaProductos(List<Producto> listaBusquedaProductos) {
		this.listaBusquedaProductos = listaBusquedaProductos;
	}

	public List<SelectItem> getTiposBusquedaProductos() {
		return tiposBusquedaProductos;
	}

	public void setTiposBusquedaProductos(List<SelectItem> tiposBusquedaProductos) {
		this.tiposBusquedaProductos = tiposBusquedaProductos;
	}

	public String getValorBusquedaProductos() {
		return valorBusquedaProductos;
	}

	public void setValorBusquedaProductos(String valorBusquedaProductos) {
		this.valorBusquedaProductos = valorBusquedaProductos;
	}

	public Producto getPojoProductoBusqueda() {
		return pojoProductoBusqueda;
	}

	public void setPojoProductoBusqueda(Producto pojoProductoBusqueda) {
		this.pojoProductoBusqueda = pojoProductoBusqueda;
	}

	public String getCampoBusquedaProductos() {
		return campoBusquedaProductos;
	}

	public void setCampoBusquedaProductos(String campoBusquedaProductos) {
		this.campoBusquedaProductos = campoBusquedaProductos;
	}

	public int getValorOpcionBusquedaProductos() {
		return valorOpcionBusquedaProductos;
	}

	public void setValorOpcionBusquedaProductos(int valorOpcionBusquedaProductos) {
		this.valorOpcionBusquedaProductos = valorOpcionBusquedaProductos;
	}

	public TraspasoDetalleExt getPojoTraspasoDetalleEliminar() {
		return pojoTraspasoDetalleEliminar;
	}

	public void setPojoTraspasoDetalleEliminar(TraspasoDetalleExt pojoTraspasoDetalleEliminar) {
		this.pojoTraspasoDetalleEliminar = pojoTraspasoDetalleEliminar;
	}
	
	public int getCantidadProductoDetalles() {
		return cantidadProductoDetalles;
	}

	public void setCantidadProductoDetalles(int cantidadProductoDetalles) {
		this.cantidadProductoDetalles = cantidadProductoDetalles;
	}

	public Collection<Object> getSelection() {
		return selection;
	}

	public void setSelection(Collection<Object> selection) {
		this.selection = selection;
	}

	public TraspasoDetalleExt getTraspasoSeleccionado() {
		return this.traspasoSeleccionado;
	}

	public void setTraspasoSeleccionado(TraspasoDetalleExt traspasoSeleccionado) {
		this.traspasoSeleccionado = traspasoSeleccionado;
	}

	public int getRowSeleccionado() {
		return rowSeleccionado;
	}

	public void setRowSeleccionado(int rowSeleccionado) {
		this.rowSeleccionado = rowSeleccionado;
	}

	public double getCantidadProductoSeleccionado() {
		return cantidadProductoSeleccionado;
	}

	public void setCantidadProductoSeleccionado(double cantidadProductoSeleccionado) {
		this.cantidadProductoSeleccionado = cantidadProductoSeleccionado;
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
        int row=0;
        for (Object selectionKey : selection) {
            dataTable.setRowKey(selectionKey);
            if (dataTable.isRowAvailable()) {
            	traspasoSeleccionado = (TraspasoDetalleExt) dataTable.getRowData();
            	log.info("Elemento seleccionado: "+traspasoSeleccionado.getProducto().getId()+", Producto: "+traspasoSeleccionado.getProducto().getDescripcion() + ", Cant:"+traspasoSeleccionado.getCantidad());
                rowSeleccionado = row;
            	cantidadProductoSeleccionado = this.ifzAlmacenProducto.findCantidadEnAlmacen(this.pojoTraspaso.getAlmacenOrigen().getId(), traspasoSeleccionado.getProducto().getId());
            	log.info("Cantidad del producto en Almacen: "+cantidadProductoSeleccionado);
            	this.setCantidadExistente(traspasoSeleccionado.getProducto().getClave(), cantidadProductoSeleccionado);
            }
            row++;
        }
        dataTable.setRowKey(originalKey);
    }
	

	public void validaCantidadProducto(){
		int row = 0;
		
		if(!this.puedeEditar) return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
		
		if(listaTraspasoDetalleGrid.isEmpty()) return;
		
		TraspasoDetalleExt movimiento = new TraspasoDetalleExt() ;
		for(TraspasoDetalleExt md:this.listaTraspasoDetalleGrid){
			if(md.getProducto().getClave().equals(this.traspasoSeleccionado.getProducto().getClave())){
				movimiento = this.listaTraspasoDetalleGrid.get(row);
				log.info("Cantidad Producto: "+cantidadProductoSeleccionado + " Row: "+row+", Cant en Movimiento:"+movimiento.getCantidad());
			}
			row++;
		}
	}

	private void setCantidadExistente(String claveProducto, double cantidadExistente){
		for( TraspasoDetalleExt td: this.listaDetalleCantidades ){
			if( td.getProducto().getClave().equals( claveProducto ) ){
				log.info("Encontrado: "+claveProducto+", Cant. "+cantidadExistente);
				td.setCantidad(cantidadExistente);
				break;
			}
		}
	}
	
	private double getCantidadProductoExistencias(String claveProducto){
		for( TraspasoDetalleExt td: this.listaDetalleCantidades ){
			if( td.getProducto().getClave().equals( claveProducto ) ){
				return td.getCantidad();
			}
		}
		
		return 0;
	}
}

// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA    |        AUTOR       | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//    2.0  | 2016-10-19  | Javier Tirado      | Modifico metodo cargarCboAlmacenDestino. Filtro solo alamcenes principales

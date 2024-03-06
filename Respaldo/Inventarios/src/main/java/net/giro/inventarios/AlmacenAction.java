package net.giro.inventarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.logica.ClientesRem;
import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Sucursal;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.AlmacenExt;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.respuesta.Respuesta;

@ViewScoped
@ManagedBean(name="almacenAction")
public class AlmacenAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AlmacenAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	//INTERFACES
	private AlmacenRem ifzAlmacen;
	private SucursalesRem ifzSucursal;
	private ClientesRem ifzClientes;
	private ObraRem ifzObras;
    private Integer usuarioId;
	private int numPagina;
	private int numPaginaObras;
	private boolean operacionCompleta;
	private String resOperacion;
	private String tipoBusqueda;
	private String valorBusqueda;
	private List<SelectItem> listaCampoBusqueda;	
	private int campoBusqueda;
	private AlmacenExt pojoAlmacen;
	private AlmacenExt pojoAlmacenEliminar;
	private List<AlmacenExt> listaAlmacenesGrid;
	private long idSucursal;
	private List<SelectItem> listaCboSucursales;
	private List<Sucursal> listaSucursales;
	private DomicilioExt pojoDomicilioExt;
	private Estado pojoEstado;
	private Municipio pojoMunicipio;
	private Localidad pojoLocalidad;
	private Colonia pojoColonia;
	private boolean puedeEditar;
	// Domicilio
	private String busquedaMunicipio;
	private String busquedaLocalidad;
	private String busquedaColonia;
	private int numPaginaMunicipio;
	private int numPaginaLocalidad; 
	private int numPaginaColonia;
	private List<SelectItem> listCatDomicilios1;
	private List<SelectItem> listCatDomicilios2;
	private List<SelectItem> listEstado;
	private List<Municipio> listMunicipios;
	private List<Localidad> listLocalidades;
	private List<Colonia> listColonias;
	private List<Estado> listTmpEstado;
	private List<ConValores> listTmpCatDomicilios1;
	private List<ConValores> listTmpCatDomicilios2;
	private boolean band;
	private String mensaje;
	private int tipoMensaje;
	private int resultadoValidacion;
	// Busqueda obras
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorOpcionBusquedaObras;
	private List<Obra> listObrasPrincipales;
	private Obra pojoObra;
	
	public AlmacenAction() throws NamingException,Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = null;

		try {
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = (int) this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
	
			this.ctx = new InitialContext();
			this.ifzSucursal = (SucursalesRem) ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzAlmacen = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
	
			this.puedeEditar = true;
			this.operacionCompleta = true;
			this.numPagina = 1;
			this.numPaginaMunicipio = 1;
			this.numPaginaLocalidad = 1; 
			this.numPaginaColonia = 1;
			this.pojoAlmacen = new AlmacenExt();
			this.pojoObra = new Obra();
			this.listaAlmacenesGrid = new ArrayList<>();
			this.listaSucursales = new ArrayList<>();
			this.listCatDomicilios1 = new ArrayList<SelectItem>();
			this.listCatDomicilios2 = new ArrayList<SelectItem>();
			this.listEstado	= new ArrayList<SelectItem>();
			this.listTmpCatDomicilios1 = new ArrayList<ConValores>();
			this.listTmpCatDomicilios2 = new ArrayList<ConValores>();
	
			cargarTipoBusqueda();
			iniciarBusquedaObras();
			cargarSucursales();
			cargaEstados();
			nuevoDomicilio();
		} catch (Exception e) {
			log.error("Error en constructor Inventarios.AlmacenAction", e);
		}
	}
	

	public void buscar() {
		this.operacionCompleta = true;
		this.resOperacion = "";
		
		try {
			if("".equals(this.tipoBusqueda)) 
				this.tipoBusqueda = "id";
			
			this.listaAlmacenesGrid = this.ifzAlmacen.findExtLikeProperty(this.tipoBusqueda, this.valorBusqueda);
			if (this.listaAlmacenesGrid.isEmpty()) {
				this.operacionCompleta = false;
				this.resOperacion = "ERROR";
				this.tipoMensaje = 2;
				log.info("la busqueda no regreso resultados");
			}
		} catch (Exception e) {
    		this.band = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en editar", e);
		}
	}

	public void nuevo(){
		this.pojoAlmacen = new AlmacenExt();
		this.pojoDomicilioExt = new DomicilioExt();
	}

	public void editar(){
		try {
    		this.band = false;
			this.mensaje = "";
			this.tipoMensaje = 0;
			
			if (this.pojoAlmacen.getPojoDomicilio() == null) {
				nuevoDomicilio();
			} else {
				this.pojoDomicilioExt = this.pojoAlmacen.getPojoDomicilio();
				desglosaDomicilio();
			}

			this.mensaje = "OK";
    	} catch (Exception e) {
    		this.band = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en editar", e);
    		throw e;
    	}
	}

	public void guardar() throws Exception{
		this.operacionCompleta = false;
		this.resOperacion = "";
		this.tipoMensaje = 0;
		
		try {
			if(this.pojoAlmacen.getId()==null) {
				if( validaGuardarAlmacen() == false) return;
				
				log.info("Paso validacion");
				
				//guardar
				this.pojoAlmacen.setCreadoPor(this.usuarioId);
				this.pojoAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoAlmacen.setModificadoPor(this.usuarioId);
				this.pojoAlmacen.setFechaModificacion(Calendar.getInstance().getTime());
				
				//this.pojoAlmacen.setId( this.ifzAlmacen.save( this.pojoAlmacen ) );
				this.pojoAlmacen.setId( this.ifzAlmacen.save( this.pojoAlmacen ) );

				this.listaAlmacenesGrid.add(this.pojoAlmacen);
			} else {
				if( validaGuardarAlmacen() == false) return;
				
				//editar
				this.pojoAlmacen.setModificadoPor(this.usuarioId);
				this.pojoAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
				
				//guardarDomicilio();
				this.ifzAlmacen.update(this.pojoAlmacen);
			}
			
			this.nuevo();
			this.nuevoDomicilio();
			this.operacionCompleta = true;
		} catch (ExcepConstraint e) {
			this.operacionCompleta = false;
			this.resOperacion = "Error al guardar";
			e.printStackTrace();
		}
	}
	
	public void eliminar(){
		try{
			
			this.pojoAlmacenEliminar.setModificadoPor(this.usuarioId);
			this.pojoAlmacenEliminar.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoAlmacenEliminar.setEstatus(1);
			this.ifzAlmacen.update( this.pojoAlmacenEliminar );
			
		} catch (ExcepConstraint e) {
			this.operacionCompleta = false;
			this.resOperacion = "Error al eliminar";
			e.printStackTrace();
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
	
	public String buscarObras() {
    	try {
			System.out.println("----------> buscarObras <----------");
			iniciarBanderas();
			
			if ("".equals(this.valorOpcionBusquedaObras))
				this.valorOpcionBusquedaObras = 1;
			
			if ("".equals(this.campoBusquedaObras)){
				this.listObrasPrincipales = this.ifzObras.findObraPrincipalLikeProperty("id", this.valorBusquedaObras, this.valorOpcionBusquedaObras, (this.pojoObra.getId() == null ? 0 : this.pojoObra.getId()));
			} else {
				this.listObrasPrincipales = this.ifzObras.findObraPrincipalLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.valorOpcionBusquedaObras, (this.pojoObra.getId() == null ? 0 : this.pojoObra.getId()));
			}
			
			if(this.listObrasPrincipales.isEmpty()){
	    		setBanderas(true, 2, "Error");
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
	
	public void seleccionarObra(){
		
	}

	public void nuevoDomicilio() {
		
		
    	if (this.pojoDomicilioExt == null) {
    		System.out.println("----------> nuevoDomicilio <----------");
			this.pojoDomicilioExt = new DomicilioExt();
			this.pojoDomicilioExt.setEstatus(true);
			//this.pojoDomicilioExt.setPersona(this);
			this.pojoDomicilioExt.setDomicilio("");
			this.pojoDomicilioExt.setCatDomicilio1(0L);
			this.pojoDomicilioExt.setCatDomicilio2(0L);
			this.pojoDomicilioExt.setCatDomicilio3(0L);
			this.pojoDomicilioExt.setCatDomicilio4(0L);
			this.pojoDomicilioExt.setCatDomicilio5(0L);
			this.pojoDomicilioExt.setDescripcionDomicilio1("");
			this.pojoDomicilioExt.setDescripcionDomicilio2("");
			this.pojoDomicilioExt.setDescripcionDomicilio3("");
			this.pojoDomicilioExt.setDescripcionDomicilio4("");
			this.pojoDomicilioExt.setDescripcionDomicilio5("");
			
			desglosaDomicilio();
		}else{
			log.info("----------> Domicilio a actualizar:  <----------");
			desglosaDomicilio() ;
		}
	}
	
	public void desglosaDomicilio() {
		
    	try {
        	this.pojoColonia = null;
    		this.pojoLocalidad = null;
    		this.pojoMunicipio = null;
    		this.pojoEstado = null;
    		
    		if(this.pojoDomicilioExt != null) {
    			if (this.pojoDomicilioExt.getColonia() != null && this.pojoDomicilioExt.getColonia().getId() > 0) 
    				this.pojoColonia = this.pojoDomicilioExt.getColonia();
    			
    			if (this.pojoColonia != null)
    				this.pojoLocalidad = this.pojoColonia.getLocalidad();
    			
    			if(this.pojoLocalidad != null)
    				this.pojoMunicipio = this.pojoLocalidad.getMunicipio();
    			
    			if (this.pojoMunicipio != null)
    				this.pojoEstado = this.pojoMunicipio.getEstado();
    		}
			
			cargaTiposDomicilio();
		} catch (Exception e) {
			log.error("Error en metodo desglosaDomicilio",e);
		}
    }
	
	private void cargaTiposDomicilio() {
		HashMap<String, String> params = null;
		
		//log.info("----------> cargaTiposDomicilio <----------");
		
		try {
    		this.operacionCompleta = true;
			this.resOperacion = "";
			
			if(listTmpCatDomicilios1 == null)
				listTmpCatDomicilios1 = new ArrayList<ConValores>();
			else
				listTmpCatDomicilios1.clear();
			
			if(listTmpCatDomicilios2 == null)
				listTmpCatDomicilios2 = new ArrayList<ConValores>();
			else
				listTmpCatDomicilios2.clear();
			
			if(listCatDomicilios1==null) 
				listCatDomicilios1 = new ArrayList<SelectItem>();
			else
				listCatDomicilios1.clear();
			
			if(listCatDomicilios2==null) 
				listCatDomicilios2 = new ArrayList<SelectItem>();
			else
				listCatDomicilios2.clear();
			
			params = new HashMap<String, String>();
			params.put("atributo2", "1");
			cargaTipoDom(params, listCatDomicilios1, listTmpCatDomicilios1);
			
			log.info("cargaTiposDomicilio: listCatDomicilios1: "+listCatDomicilios1.size());
			
			
			params.clear();
			params.put("atributo2", "2");
			cargaTipoDom(params, listCatDomicilios2, listTmpCatDomicilios2);
		} catch (Exception e) {
    		this.operacionCompleta = false;
			this.resOperacion = "ERROR";
			log.error("Error en el metodo cargaTiposDomicilio", e);
		}
	}
	
	/*public net.giro.clientes.plataforma.InfoSesion convertToClientesInfoSesion(net.giro.plataforma.InfoSesion infoSesion) {
		net.giro.clientes.plataforma.InfoSesion result = new net.giro.clientes.plataforma.InfoSesion();
		
		try {
			result.setAcceso(infoSesion.getAcceso());
			result.setResponsabilidad(infoSesion.getResponsabilidad());
			result.setUltimaModificacion(infoSesion.getUltimaModificacion());
		} catch (Exception e) {
			throw e;
		}
		
		return result;
	}*/
	
	private void cargaTipoDom(HashMap<String, String> params, List<SelectItem> items, List<ConValores> listDom){
		try {
			this.operacionCompleta = true;
			this.resOperacion = "";
			
			listDom.addAll(ifzClientes.buscarDomicilios(params));

			for(ConValores cv : listDom)
				items.add(new SelectItem(cv.getId(), cv.getValor()));
		} catch (Exception e) {
			this.operacionCompleta = false;
			this.resOperacion = "ERROR";
			log.error("Error en el metodo cargaTipoDom", e);
		}
	}
	
	public boolean cargaEstados() {
    	try {
			System.out.println("----------> estatusObras <----------");
    		this.operacionCompleta = true;
			
			

			this.listEstado = new ArrayList<SelectItem>();
			this.listTmpEstado = this.ifzClientes.buscarEstados();
			
			if (this.listTmpEstado.isEmpty()) {
				this.operacionCompleta = true;
				//this.tipoMensaje = -1;
				this.resOperacion = "No se encontro ningun estado.";
				return false;
			}

			this.listEstado.clear();
			for(Estado e : listTmpEstado){
				listEstado.add(new SelectItem(e.getId(), e.getNombre()));
			}
			
			this.resOperacion = "OK";
		} catch (Exception e) {
			this.operacionCompleta = false;
			this.resOperacion = "ERROR";
			log.error("Error en eliminar", e);
			return false;
		}
    	
    	return true;
    }
	
	public void limpiaMunicipio(){
		if(listMunicipios != null)
			listMunicipios.clear();
		else
			listMunicipios = new ArrayList<Municipio>();
	}
	
	public void limpiaLocalidades(){
		if(listLocalidades != null)
			listLocalidades.clear();
		else
			listLocalidades = new ArrayList<Localidad>();
	}
	
	public void limpiaColonias(){
		if(listColonias != null)
			listColonias.clear();
		else
			listColonias = new ArrayList<Colonia>();
	}
	
	public void cambioEstadoDom(){
		try {
			if(listMunicipios != null)
				listMunicipios.clear();
			
			if(listLocalidades != null)
				listLocalidades.clear();
			
			if(listColonias != null)
				listColonias.clear();
			
			pojoMunicipio = null;
			pojoLocalidad = null;
			pojoColonia = null;
		} catch (Exception e) {
			log.error("Error en metodo cambioEstadoDom", e);
		}
	}
	
	private void cargarTipoBusqueda(){
		if(this.listaCampoBusqueda == null)
			this.listaCampoBusqueda = new ArrayList<SelectItem>();
		
		this.listaCampoBusqueda.add( new SelectItem("nombre", "Nombre" ) );
		this.listaCampoBusqueda.add( new SelectItem("id", "ID" ) );
	}
	
	public void cargarSucursales(){
		this.listaCboSucursales = null;
		this.listaCboSucursales = new ArrayList<>();
		
		try {
			this.listaSucursales = this.ifzSucursal.buscarSucursales("sucursal", "");
		} catch (ExcepConstraint e) {
			log.info("Error obteniendo sucursales");
			e.printStackTrace();
		}
		
		for(Sucursal s:this.listaSucursales){
			this.listaCboSucursales.add( new SelectItem(s.getId(), s.getSucursal() ) );
		}
	}
		
	public String buscarMunicipios() throws Exception {
    	try {
			System.out.println("----------> buscarMunicipios <----------");
    		this.operacionCompleta = true;
			this.resOperacion = "";
			
			this.listMunicipios = ifzClientes.buscarMunicipio(this.pojoEstado, this.busquedaMunicipio);
			if(this.listMunicipios.isEmpty()){
	    		setBanderas(true, 2, "Error");
	    		return this.resOperacion;
			}
			this.resOperacion = "OK";
    	} catch (Exception e) {
    		this.operacionCompleta = false;
			this.resOperacion = "ERROR";
    		log.error("Error en buscarMunicipios", e);
    		throw e;
    	}
    	
    	return this.resOperacion;
    }
    
    public String buscarLocalidades() throws Exception {
    	try {
			System.out.println("----------> buscarLocalidades <----------");
    		this.operacionCompleta = false;
			this.resOperacion = "";
			
			this.listLocalidades = this.ifzClientes.buscarLocalidad(this.pojoMunicipio, this.busquedaLocalidad);
			if(this.listLocalidades.isEmpty()){
	    		setBanderas(true, 2, "Error");
	    		return this.resOperacion;
			}
			this.operacionCompleta = true;
			this.resOperacion = "OK";
    	} catch (Exception e) {
    		this.operacionCompleta = false;
			this.resOperacion = "ERROR";
    		log.error("Error en buscarLocalidades", e);
    		throw e;
    	}
    	
    	return this.resOperacion;
    }
    
    public String buscarColonias() throws Exception {
    	try {
			System.out.println("----------> buscarColonias <----------");
    		this.operacionCompleta = false;
    		this.resOperacion = "";
			
			this.listColonias = this.ifzClientes.buscarColonia(this.pojoLocalidad, this.busquedaColonia);
			if(this.listColonias.isEmpty()){
	    		setBanderas(true, 2, "Error");
	    		return this.resOperacion;
			}
			this.resOperacion = "OK";
    	} catch (Exception e) {
			this.resOperacion = "ERROR";
			this.operacionCompleta = false;
    		log.error("Error en buscarColonias", e);
    		throw e;
    	}
    	
    	return this.resOperacion;
    }
	
	private boolean validaGuardarAlmacen() throws Exception {
		try {
			log.info("Validacion iniciada...");
			
			this.tipoMensaje = 0;
			
			log.info("this.pojoAlmacen.getNombre().trim(): "+this.pojoAlmacen.getNombre().trim().length());
			
			if(this.pojoAlmacen.getNombre().trim().equals("")){
				this.resOperacion = "Indique el nombre del almacen";
				this.tipoMensaje = -10;
				return false;
			}
			
			log.info("Validando nombre OK");
			
			if(this.pojoAlmacen.getIdentificador().trim().equals("")){
				this.resOperacion = "Indique identificador para almacen";
				
				this.tipoMensaje = -11;
				
				return false;
			}

			log.info("Validando getIdentificador OK");
			log.info("Domicilio: "+pojoDomicilioExt.getDomicilio());
			
			if(this.pojoDomicilioExt.getDomicilio() == null){
				this.resOperacion = "Debe indicar el domicilio";
				this.tipoMensaje = -12;
				return false;
			}
			
			if(this.pojoDomicilioExt.getDomicilio().trim().equals("")){
				this.resOperacion = "Debe indicar el domicilio";
				this.tipoMensaje = -12;
				return false;
			}
			
			// Comprobamos almacen principal
			if (this.pojoAlmacen.getTipo() == 1) {
				if (this.ifzAlmacen.comprobarPrincipal(Long.valueOf(this.pojoAlmacen.getSucursal().getId()), this.pojoAlmacen.getId())) {
					this.resOperacion = "La sucursal seleccionada ya tiene un alamcen principal asignado.";
					this.tipoMensaje = -13;
					return false;
				}
			}

			
			// Comprobamos nombre almacen
			if (this.ifzAlmacen.comprobarNombre(this.pojoAlmacen.getNombre(), this.pojoAlmacen.getId())) {
				this.resOperacion = "Ya existe un almacen con este nombre asignado.";
				this.tipoMensaje = -14;
				return false;
			}
			log.info("Validando domicilio OK");
			
			return true;
		} catch (ExcepConstraint e) {
			this.operacionCompleta = false;
			this.resOperacion = "Error al guardar";
			throw e;
		}
	}

	public boolean guardarDomicilio(){
		System.out.println("----------> guardarDomicilio <----------");
		this.operacionCompleta = true;
		Respuesta respuesta = new Respuesta();
		this.band = false;
		this.mensaje = "";
		this.tipoMensaje = 0;
		
		try {
			if(! comprobarDireccion() || pojoColonia == null) {
				this.band = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = -12;
				this.resOperacion = "Direccion no valida.";
				return false;
			}
			
			this.pojoDomicilioExt.setPrincipal(true);
			this.pojoDomicilioExt.setEstatus(true);
			this.pojoDomicilioExt.setColonia(pojoColonia);
			
			log.info("PojoDomicilio.id(): "+this.pojoDomicilioExt.getId());
			respuesta = this.ifzClientes.salvar(this.pojoDomicilioExt);
			if(respuesta.getErrores().getCodigoError() > 0){
				this.mensaje = respuesta.getErrores().getDescError();
			} else {
				this.pojoDomicilioExt = (DomicilioExt) respuesta.getBody().getValor("domicilio");
				this.pojoAlmacen.setPojoDomicilio(pojoDomicilioExt);
				log.info("pojoDomicilioExt: "+pojoDomicilioExt.getDescripcionDomicilio1());
			}
		} catch (Exception e) {
			this.band = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en el metodo guardarDomicilio", e);
		}
		
		return ! this.band;
	}
	
	public boolean comprobarDireccion() {
		if ("".equals(this.pojoDomicilioExt.getDescripcionDomicilio1().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio2().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio3().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio4().trim()) && 
				"".equals(this.pojoDomicilioExt.getDescripcionDomicilio5().trim()))
			return false;
		
		return true;
	}
	
	// ------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------------

	public AlmacenExt getPojoAlmacen() {
		return pojoAlmacen;
	}

	public void setPojoAlmacen(AlmacenExt pojoAlmacen) {
		this.pojoAlmacen = pojoAlmacen;
		//this.idSucursal = this.pojoAlmacen.getSucursal().getId();
		this.setIdSucursal(this.pojoAlmacen.getSucursal().getId());
		//log.info("Setting Domicilio: "+pojoAlmacen.getPojoDomicilio().getDomicilio());
		
		if( this.pojoAlmacen.getPojoDomicilio() != null ){
			this.pojoDomicilioExt = pojoAlmacen.getPojoDomicilio();
		}
			
		
	}
	
	public Long getIdEstadoDom() {
		return pojoEstado != null && pojoEstado.getId() > 0 ? pojoEstado.getId() : 0L;
	}
	
	private Estado getEstadoById(Long id){
		for(Estado e : this.listTmpEstado){
			if(e.getId() == id.longValue()) 
				return e;
		}
		
		return null;
	}
	
	public void setIdEstadoDom(Long idEstadoDom) {
		try {
			pojoEstado = getEstadoById(idEstadoDom);
		} catch (Exception e) {
			log.error("Error en metodo setIdEstadoDom",e);
		}
	}
	
	public long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(long idSucursal) {
		this.idSucursal = idSucursal;
		for(Sucursal s:this.listaSucursales){
			if(s.getId()==idSucursal){
				this.pojoAlmacen.setSucursal(s);
			}
		}
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

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
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

	public List<AlmacenExt> getListaAlmacenesGrid() {
		return listaAlmacenesGrid;
	}

	public void setListaAlmacenesGrid(List<AlmacenExt> listaAlmacenesGrid) {
		this.listaAlmacenesGrid = listaAlmacenesGrid;
	}

	public List<SelectItem> getListaCboSucursales() {
		return listaCboSucursales;
	}

	public void setListaCboSucursales(List<SelectItem> listaCboSucursales) {
		this.listaCboSucursales = listaCboSucursales;
	}

	public AlmacenExt getPojoAlmacenEliminar() {
		return pojoAlmacenEliminar;
	}

	public void setPojoAlmacenEliminar(AlmacenExt pojoAlmacenEliminar) {
		this.pojoAlmacenEliminar = pojoAlmacenEliminar;
	}

	public int getNumPaginaMunicipio() {
		return numPaginaMunicipio;
	}

	public void setNumPaginaMunicipio(int numPaginaMunicipio) {
		this.numPaginaMunicipio = numPaginaMunicipio;
	}

	public int getNumPaginaLocalidad() {
		return numPaginaLocalidad;
	}

	public void setNumPaginaLocalidad(int numPaginaLocalidad) {
		this.numPaginaLocalidad = numPaginaLocalidad;
	}

	public int getNumPaginaColonia() {
		return numPaginaColonia;
	}

	public void setNumPaginaColonia(int numPaginaColonia) {
		this.numPaginaColonia = numPaginaColonia;
	}

	public String getBusquedaMunicipio() {
		return busquedaMunicipio;
	}

	public void setBusquedaMunicipio(String busquedaMunicipio) {
		this.busquedaMunicipio = busquedaMunicipio;
	}

	public String getBusquedaLocalidad() {
		return busquedaLocalidad;
	}

	public void setBusquedaLocalidad(String busquedaLocalidad) {
		this.busquedaLocalidad = busquedaLocalidad;
	}

	public String getBusquedaColonia() {
		return busquedaColonia;
	}

	public void setBusquedaColonia(String busquedaColonia) {
		this.busquedaColonia = busquedaColonia;
	}

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}

	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}

	public List<Colonia> getListColonias() {
		return listColonias;
	}

	public void setListColonias(List<Colonia> listColonias) {
		this.listColonias = listColonias;
	}

	public List<Estado> getListTmpEstado() {
		return listTmpEstado;
	}

	public void setListTmpEstado(List<Estado> listTmpEstado) {
		this.listTmpEstado = listTmpEstado;
	}

	public List<ConValores> getListTmpCatDomicilios1() {
		return listTmpCatDomicilios1;
	}

	public void setListTmpCatDomicilios1(List<ConValores> listTmpCatDomicilios1) {
		this.listTmpCatDomicilios1 = listTmpCatDomicilios1;
	}

	public List<ConValores> getListTmpCatDomicilios2() {
		return listTmpCatDomicilios2;
	}

	public void setListTmpCatDomicilios2(List<ConValores> listTmpCatDomicilios2) {
		this.listTmpCatDomicilios2 = listTmpCatDomicilios2;
	}

	public DomicilioExt getPojoDomicilio() {
		return pojoDomicilioExt;
	}

	public void setPojoDomicilio(DomicilioExt pojoDomicilio) {
		this.pojoDomicilioExt = pojoDomicilio;
	}

	public Colonia getPojoColonia() {
		return pojoColonia;
	}

	public void setPojoColonia(Colonia pojoColonia) {
		this.pojoColonia = pojoColonia;
	}

	public Municipio getPojoMunicipio() {
		return pojoMunicipio;
	}

	public void setPojoMunicipio(Municipio pojoMunicipio) {
		this.pojoMunicipio = pojoMunicipio;
		this.pojoLocalidad = null;
		this.pojoColonia = null;
	}
	
	public Localidad getPojoLocalidad() {
		return pojoLocalidad;
	}

	public void setPojoLocalidad(Localidad pojoLocalidad) {
		this.pojoLocalidad = pojoLocalidad;
		this.pojoColonia = null;
	}
	
	private ConValores getValorById(Long id, List<ConValores> lista){
		for(ConValores cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		
		return null;
	}

	public List<SelectItem> getListEstado() {
		return listEstado;
	}

	public void setListEstado(List<SelectItem> listEstado) {
		this.listEstado = listEstado;
	}

	public List<SelectItem> getListCatDomicilios1() {
		return listCatDomicilios1;
	}

	public void setListCatDomicilios1(List<SelectItem> listCatDomicilios1) {
		this.listCatDomicilios1 = listCatDomicilios1;
	}

	public List<SelectItem> getListCatDomicilios2() {
		return listCatDomicilios2;
	}

	public void setListCatDomicilios2(List<SelectItem> listCatDomicilios2) {
		this.listCatDomicilios2 = listCatDomicilios2;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}
	
	public Long getIdDom1() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio1() != null ? this.pojoDomicilioExt.getCatDomicilio1() : 0L;
		
	}
		
	public void setIdDom1(Long idDom1) {
		this.pojoDomicilioExt.setCatDomicilio1(getValorById(idDom1, this.listTmpCatDomicilios1).getId());
	}
	
	public Long getIdDom2() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio2() != null ? this.pojoDomicilioExt.getCatDomicilio2() : 0L;
	}
		
	public void setIdDom2(Long idDom2) {
		this.pojoDomicilioExt.setCatDomicilio2(getValorById(idDom2, this.listTmpCatDomicilios1).getId());
	}
		
	public Long getIdDom3() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio3() != null ? this.pojoDomicilioExt.getCatDomicilio3() : 0L;
	}
			
	public void setIdDom3(Long idDom3) {
		this.pojoDomicilioExt.setCatDomicilio3(getValorById(idDom3, this.listTmpCatDomicilios1).getId());
	}
		
	public Long getIdDom4() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio4() != null ? this.pojoDomicilioExt.getCatDomicilio4() : 0L;
	}
		
	public void setIdDom4(Long idDom4) {
		this.pojoDomicilioExt.setCatDomicilio4(getValorById(idDom4, this.listTmpCatDomicilios2).getId());
	}
	
	public Long getIdDom5() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio5() != null ? this.pojoDomicilioExt.getCatDomicilio5() : 0L;
	}
	
	public void setIdDom5(Long idDom5) {
		this.pojoDomicilioExt.setCatDomicilio5(getValorById(idDom5, listTmpCatDomicilios2).getId());
	}

	public Estado getPojoEstado() {
		return pojoEstado;
	}

	public void setPojoEstado(Estado pojoEstado) {
		this.pojoEstado = pojoEstado;
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
	
	public long getIdObra(){
		return 0L;
	}
	
	public void setIdObra(int idObra){
		log.info("Seteando obra");
	}

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}

	public List<Obra> getListObrasPrincipales() {
		return listObrasPrincipales;
	}

	public void setListObrasPrincipales(List<Obra> listObrasPrincipales) {
		this.listObrasPrincipales = listObrasPrincipales;
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

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}
	
	public void setTipoAlmacen(boolean tipo){
		if (tipo == true) 		//1, principal
			this.pojoAlmacen.setTipo(1);
		else
			this.pojoAlmacen.setTipo(2);	//2, obra
	}
	
	public boolean isTipoAlmacen(){
		return this.pojoAlmacen.getTipo() == 1 ? true : false;
	}

	public int getResultadoValidacion() {
		return resultadoValidacion;
	}

	public void setResultadoValidacion(int resultadoValidacion) {
		this.resultadoValidacion = resultadoValidacion;
	}
}

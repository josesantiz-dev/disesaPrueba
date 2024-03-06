package net.giro.adp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.adp.logica.ObraRem;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="almObraAction")
public class AlmacenesObrasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AlmacenesObrasAction.class);
	
	private InitialContext ctx; 
	private LoginManager loginManager; 
	//private HttpSession httpSession;
	
	// Interfaces
	private ObraRem ifzObras;
	//private ReportesRem	ifzReportes;
	private ObraAlmacenesRem ifzObraAlmacenes;
	private AlmacenRem ifzAlmacenes;
	
	private List<Obra> listObras;
	private List<ObraAlmacenes> listObraAlmacenes;
	private List<Almacen> listAlmacenes;
	
	private List<SelectItem> listAlmacenesItems;
	private List<SelectItem> listTiposAlmacenesItems;

	private Obra pojoObra;
	private ObraAlmacenes pojoObraAlmacen;
	private ObraAlmacenes pojoObraAlmacenBorrar;
	private Almacen pojoAlmacen;
	private long almacenId;
	private int tipoAlmacen;
	private boolean almacenPrincipal;
	private int obraAlmacenesPagina;
	
	// Busqueda
	private List<SelectItem> busquedaOpciones;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaTipo;
	private int busquedaPaginas;
	
	// Variables de control
	private boolean incompleto;
	private int tipoMensaje;
	private String mensaje;
    private long usuarioId;
    //private String usuario;
    //private double porcentajeIva; 
    private boolean permiteModificar;
    private double valObraCancelada;

	
	public AlmacenesObrasAction() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			//this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzObraAlmacenes = (ObraAlmacenesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzAlmacenes = (AlmacenRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			
			// Inicializaciones
			this.usuarioId = 0;
			//this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			//this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());
			this.usuarioId = (long) this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			//this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			//this.porcentajeIva = Double.valueOf(this.loginManager.getAutentificacion().getPerfil("VALOR_IVA"));
			this.obraAlmacenesPagina = 1;
			
			this.ifzObras.showSystemOuts(false);
			this.ifzObraAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObraAlmacenes.showSystemOuts(false);
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "Clave"));
			this.busquedaCampo = busquedaOpciones.get(0).getDescription();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			
			this.listTiposAlmacenesItems = new ArrayList<SelectItem>();
			this.listTiposAlmacenesItems.add(new SelectItem(1, "Obra"));
			this.listTiposAlmacenesItems.add(new SelectItem(2, "General"));
			
			this.almacenId = 0;
			cargaAlmacenes();
			
			this.valObraCancelada = 0L;
		} catch (Exception e) {
			this.ctx = null;
			log.error("Error en constructor CotizacionesAction", e);
		}
	}
	
	
	private void control(boolean procesoInterrumpido, int tipo, String mensaje) {
		this.incompleto = procesoInterrumpido;
		this.tipoMensaje = tipo;
		this.mensaje = mensaje;
	}
	
	public void buscar() {
		try {
			control(false, 0, "");
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = "nombre";
			
			if (this.listObras != null)
				this.listObras.clear();

			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo);
			if (this.listObras.isEmpty()) {
				control(true, 2, "ERROR");
				return;
			}
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.buscar", e);
    	}
	}
	
	public void ver() {
		try {
			control(false, 0, "");
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}
			
			this.permiteModificar = true;
			if(this.pojoObra.getEstatus().equals(10000798L)) 
				this.permiteModificar = false;

			if (this.listObraAlmacenes == null)
				this.listObraAlmacenes = new ArrayList<ObraAlmacenes>();
			this.listObraAlmacenes.clear();
			
			this.ifzObraAlmacenes.orderBy("almacenPrincipal DESC, nombreAlmacen");
			this.listObraAlmacenes = this.ifzObraAlmacenes.findByProperty("idObra", this.pojoObra.getId(), 120);
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.ver", e);
    	}
	}
	
	public void nuevo() {
		try {
			control(false, 0, "");

			this.pojoObraAlmacen = new ObraAlmacenes();
	    	this.pojoObraAlmacenBorrar = null;
	    	this.pojoAlmacen = null;
	    	this.almacenId = 0;
	    	this.tipoAlmacen = 0;
	    	this.almacenPrincipal = false;
	    	
	    	cargaAlmacenes();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.nuevo", e);
    	}
	}
	
	public void editar() {
		try {
			control(false, 0, "");

	    	this.almacenId = this.pojoObraAlmacen.getIdAlmacen();
	    	this.tipoAlmacen = this.pojoObraAlmacen.getTipo();
	    	this.almacenPrincipal = (this.pojoObraAlmacen.getAlmacenPrincipal() == 1) ? true : false;
	    	
	    	cargaAlmacenes();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.editar", e);
    	}
	}
	
	public void guardar() {
		try {
			control(false, 0, "");

			if (this.listObraAlmacenes == null) 
        		this.listObraAlmacenes = new ArrayList<ObraAlmacenes>();
			
        	if (this.pojoObraAlmacen == null)
        		this.pojoObraAlmacen = new ObraAlmacenes();
        	
        	if (! validarAlmacenObra()) 
				return;
        	
        	// Asignamos nuestro almacen
        	if (this.almacenId > 0 && this.listAlmacenes != null && ! this.listAlmacenes.isEmpty()) {
        		for (Almacen var : this.listAlmacenes) {
	    			if (var.getId() == this.almacenId) {
    					this.pojoAlmacen = var;
    					break;
    				}
	    		}
        	}
        	
    		this.pojoObraAlmacen.setIdObra(this.pojoObra.getId());
    		this.pojoObraAlmacen.setNombreObra(this.pojoObra.getNombre());
    		this.pojoObraAlmacen.setIdAlmacen(this.pojoAlmacen.getId());
    		this.pojoObraAlmacen.setNombreAlmacen(this.pojoAlmacen.getNombre());
    		this.pojoObraAlmacen.setTipo(this.tipoAlmacen);
    		this.pojoObraAlmacen.setAlmacenPrincipal((almacenPrincipal ? 1 : 0));
    		this.pojoObraAlmacen.setModificadoPor(this.usuarioId);
    		this.pojoObraAlmacen.setFechaModificacion(Calendar.getInstance().getTime());
        	/*
    		// Comprobamos que solo existe un almacen marcado como principal
        	if (! comprobarAlmacenPrincipalDeObra())
        		return;*/
    		
        	if (this.pojoObraAlmacen.getId() == null || this.pojoObraAlmacen.getId() <= 0L) {
	        	this.pojoObraAlmacen.setCreadoPor(this.usuarioId);
        		this.pojoObraAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
        		
	        	// Guardamos en la BD
        		this.pojoObraAlmacen.setId(this.ifzObraAlmacenes.save(this.pojoObraAlmacen));
        		
        		// Agregamos a la LISTA
        		this.listObraAlmacenes.add(this.pojoObraAlmacen);
        	} else {
        		// Actualizamos en la BD
        		this.ifzObraAlmacenes.update(this.pojoObraAlmacen);
        		
        		// Actualizamos en la LISTA
        		for(ObraAlmacenes var : this.listObraAlmacenes) {
	        		if ((long) var.getId() == (long) this.pojoObraAlmacen.getId()) {
	            		var.setIdAlmacen(this.pojoObraAlmacen.getIdAlmacen());
	            		var.setNombreAlmacen(this.pojoObraAlmacen.getNombreAlmacen());
		        		var.setTipo(this.pojoObraAlmacen.getTipo());
		        		var.setModificadoPor(this.usuarioId);
		        		var.setFechaModificacion(Calendar.getInstance().getTime());
	        			break;
	        		}
	        	}
        	}
        	
        	nuevo();
        	ver();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.guardar", e);
    	}
	}
	
	public void eliminar() {
		try {
			control(false, 0, "");

			if(this.pojoObraAlmacenBorrar != null) {
	    		// Borramos de la BD si corresponde
	    		if (this.pojoObraAlmacenBorrar.getId() != null && this.pojoObraAlmacenBorrar.getId() > 0L)
	    			this.ifzObraAlmacenes.delete(this.pojoObraAlmacenBorrar.getId());

	    		// Borramos de la lista
	    		this.listObraAlmacenes.remove(this.pojoObraAlmacenBorrar);
	    	}

        	nuevo();
	    	ver();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.eliminar", e);
    	}
	}

    public boolean cargaAlmacenes() {
    	try {
    		control(false, 0, "");
			
    		if (this.listAlmacenes == null)
    			this.listAlmacenes = new ArrayList<Almacen>();
    		this.listAlmacenes.clear();
    		
    		if (this.listAlmacenesItems == null)
    			this.listAlmacenesItems = new ArrayList<SelectItem>();
    		this.listAlmacenesItems.clear();
    		
			this.listAlmacenes = this.ifzAlmacenes.findByProperty("nombre", "");
			if (this.listAlmacenes.isEmpty()) {
	    		control(true, -1, "No se encontro ningun almacen para Obras/Proyectos");
				return false;
			}

			for (Almacen var : this.listAlmacenes) {
				this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getId() + "-" + var.getNombre()));
			}
    	} catch (Exception e) {
    		control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.cargaAlmacenes", e);
			return false;
    	}
    	
		return true;
    }

    public boolean comprobarTipoAlmacen() {
    	try {
    		control(false, 0, "");
			
    		// Comprobamos tipo de almacen
    		if (this.almacenId > 0L) {
    			for (Almacen var : this.listAlmacenes) {
	    			if (var.getId() == this.almacenId) {
	    				if (var.getTipo() == 1 && this.tipoAlmacen == 1) {
	    		    		control(true, 4, "ERROR");
		    				return false;
	    				} 
	    			}
	    		}
    		}
    		
	    	return true;
    	} catch (Exception e) {
    		control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.comprobarAlmacenObra", e);
    		return false;
    	}
    }

    public boolean comprobarAlmacenPrincipalDeObra() {
    	if (this.almacenPrincipal) {
        	for (ObraAlmacenes item : this.listObraAlmacenes) {
    			if (! item.getIdAlmacen().equals(this.almacenId) && item.getAlmacenPrincipal() == 1) {
    				control(true, 16, "ERROR");
    	    		return false;
    			}
    		}
    	}
    	
    	return true;
    }
    
    public boolean validarAlmacenObra() {
    	try {
    		control(false, 0, "");
			
    		// Comprobamos tipo de almacen
    		if (! comprobarTipoAlmacen()) 
    			return false;
    		
    		// Comprobamos que el almacen asignado no exista en la Obra/Proyecto.
        	for(ObraAlmacenes var : this.listObraAlmacenes) {
        		if ((long) var.getIdAlmacen() == this.almacenId) {
        			if (this.pojoObraAlmacen != null && this.pojoObraAlmacen.getId() != null && this.pojoObraAlmacen.getId() > 0L && var.getId().equals(this.pojoObraAlmacen.getId()))
        				continue;
        			
        			control(true, 10, "ERROR");
	        		return false;
        		}
        	}
        	
        	if (! comprobarAlmacenPrincipalDeObra())
        		return false;
    		
	    	return true;
    	} catch (Exception e) {
    		control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.comprobarAlmacenObra", e);
    		return false;
    	}
    }
    
    public void reemplazarAlmacenPrincipal() {
    	try {
    		control(false, 0, "");
    		
	    	for (ObraAlmacenes item : this.listObraAlmacenes) {
	    		if (item.getId().equals(this.pojoObraAlmacen.getId())) continue;
	    		item.setAlmacenPrincipal(0);
	    		this.ifzObraAlmacenes.update(item);
			}
	    	
	    	this.guardar();
    	} catch (Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.reemplazarAlmacenPrincipal", e);
    	}
    }
    
	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

	public long getAlmacenId() {
		/*if (this.pojoAlmacen != null && this.pojoAlmacen.getId() != null && this.pojoAlmacen.getId() > 0L)
			return this.pojoAlmacen.getId();
		return 0;*/
		return this.almacenId;
	}

	public void setAlmacenId(long almacenId) {
		this.almacenId = almacenId;
		
		// Recuperamos el almacen
		for (Almacen var : this.listAlmacenes) {
			if (almacenId == var.getId()) { 
				if (var.getTipo() == 1 && this.tipoAlmacen == 0) 
					this.tipoAlmacen = 2;
				break;
			}
		}
	}
	
	public boolean getEsAlmacenGeneral() {
		if (this.pojoAlmacen != null && this.pojoAlmacen.getId() != null && this.pojoAlmacen.getId() > 0L)
			return this.pojoAlmacen.getTipo() == 1 ? true : false;
		return false;
	}

	public void setEsAlmacenGeneral(boolean value) {}

    public boolean getAlmacenPrincipal() {
    	return this.almacenPrincipal;
    	/*if (this.pojoObraAlmacen == null) 
    		this.pojoObraAlmacen = new ObraAlmacenes();
    	return (this.pojoObraAlmacen.getAlmacenPrincipal() == 1 ? true : false);*/
    }
    
    public void setAlmacenPrincipal(boolean almacenPrincipal) {
    	this.almacenPrincipal = almacenPrincipal;
    	/*if (this.pojoObraAlmacen == null) 
    		this.pojoObraAlmacen = new ObraAlmacenes();
    	this.pojoObraAlmacen.setAlmacenPrincipal((almacenPrincipal ? 1 : 0));*/
    }
    
	public boolean getOperacion() {
		return incompleto;
	}

	public void setOperacion(boolean incompleto) {
		this.incompleto = incompleto;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}	
	
	public List<SelectItem> getBusquedaOpciones() {
		return busquedaOpciones;
	}

	public void setBusquedaOpciones(List<SelectItem> busquedaOpciones) {
		this.busquedaOpciones = busquedaOpciones;
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

	public int getBusquedaTipo() {
		return busquedaTipo;
	}

	public void setBusquedaTipo(int busquedaTipo) {
		this.busquedaTipo = busquedaTipo;
	}
	
	public int getBusquedaPaginas() {
		return busquedaPaginas;
	}

	public void setBusquedaPaginas(int busquedaPaginas) {
		this.busquedaPaginas = busquedaPaginas;
	}

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
	
	public ObraAlmacenes getPojoObraAlmacen() {
		if (pojoObraAlmacen == null)
			pojoObraAlmacen = new ObraAlmacenes();
		return pojoObraAlmacen;
	}
	
	public void setPojoObraAlmacen(ObraAlmacenes pojoObraAlmacen) {
		this.pojoObraAlmacen = pojoObraAlmacen;
	}
	
	public ObraAlmacenes getPojoObraAlmacenBorrar() {
		return pojoObraAlmacenBorrar;
	}

	public void setPojoObraAlmacenBorrar(ObraAlmacenes pojoObraAlmacenBorrar) {
		this.pojoObraAlmacenBorrar = pojoObraAlmacenBorrar;
	}
	
	public List<SelectItem> getListAlmacenesItems() {
		return listAlmacenesItems;
	}

	public void setListAlmacenesItems(List<SelectItem> listAlmacenesItems) {
		this.listAlmacenesItems = listAlmacenesItems;
	}
	
	public List<ObraAlmacenes> getListObraAlmacenes() {
		return listObraAlmacenes;
	}

	public void setListObraAlmacenes(List<ObraAlmacenes> listObraAlmacenes) {
		this.listObraAlmacenes = listObraAlmacenes;
	}

	public int getObraAlmacenesPagina() {
		return obraAlmacenesPagina;
	}

	public void setObraAlmacenesPagina(int obraAlmacenesPagina) {
		this.obraAlmacenesPagina = obraAlmacenesPagina;
	}

	public List<SelectItem> getListTiposAlmacenesItems() {
		return listTiposAlmacenesItems;
	}

	public void setListTiposAlmacenesItems(List<SelectItem> listTiposAlmacenesItems) {
		this.listTiposAlmacenesItems = listTiposAlmacenesItems;
	}

	public int getTipoAlmacen() {
		return tipoAlmacen;
	}

	public void setTipoAlmacen(int tipoAlmacen) {
		this.tipoAlmacen = tipoAlmacen;
	}

	public boolean isPermiteModificar() {
		return permiteModificar;
	}

	public void setPermiteModificar(boolean permiteModificar) {
		this.permiteModificar = permiteModificar;
	}

	public double getValObraCancelada() {
		return valObraCancelada;
	}

	public void setValObraCancelada(double valObraCancelada) {
		this.valObraCancelada = valObraCancelada;
	}
}

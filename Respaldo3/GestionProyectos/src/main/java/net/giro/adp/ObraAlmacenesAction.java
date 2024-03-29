package net.giro.adp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="almObraAction")
public class ObraAlmacenesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ObraAlmacenesAction.class);
	private LoginManager loginManager; 
	private InitialContext ctx; 
	// Interfaces
	// Obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	// Obras Almacenes
	private ObraAlmacenesRem ifzObraAlmacenes;
	private List<ObraAlmacenes> listObraAlmacenes;
	private ObraAlmacenes pojoObraAlmacenBorrar;
	private ObraAlmacenes pojoObraAlmacen;
	private boolean almacenPrincipal;
	private int obraAlmacenesPagina;
	// Almacenes
	private AlmacenRem ifzAlmacenes;
	private List<Almacen> listAlmacenes;
	private Almacen pojoAlmacen;
	private List<SelectItem> listAlmacenesItems;
	private boolean almacenesAdministrativos;
	// Busqueda principal
	private List<SelectItem> busquedaOpciones;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaTipo;
	private int busquedaPaginas;
	private boolean busquedaAdministrativas;
	// Variables de control
	private boolean incompleto;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
    private long usuarioId;
    private boolean permiteModificar;
	// PERMISOS
	private EmpleadoRem ifzEmpleados;
	private boolean permisoAgregar;
	private boolean permisoEditar;
	private boolean permisoBorrar;
	private boolean permisoImprimir;
    // PERFILES
	private boolean PERFIL_ADMINISTRATIVO;

	
	public ObraAlmacenesAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String resValue = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			// Inicializaciones
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();

			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			//this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzObraAlmacenes = (ObraAlmacenesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzAlmacenes = (AlmacenRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			
			this.ifzObras.showSystemOuts(false);
			this.ifzObraAlmacenes.showSystemOuts(false);
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObraAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("*", "Coincidencia"));
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "ID"));
			this.busquedaCampo = this.busquedaOpciones.get(0).getValue().toString();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			this.obraAlmacenesPagina = 1;
			
			//this.almacenId = 0;
			cargaAlmacenes();
		} catch (Exception e) {
			this.ctx = null;
			log.error("Error en constructor CotizacionesAction", e);
		} finally {
			establecerPermisos();
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = "nombre";
			
			if (this.listObras != null)
				this.listObras.clear();

			this.busquedaTipo = 0;
			if (this.busquedaAdministrativas)
				this.busquedaTipo = 4;

			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo, false, "", 0);
			if (this.listObras.isEmpty()) {
				control(true, 2, "La busqueda no regreso resultados");
				return;
			}
		} catch(Exception e) {
			control(true, -1, "Ocurrio un problema al consultar las Obras", e);
    	}
	}
	
	public void ver() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}
    		log.info("Validando permiso de escritura");
    		validarPermisos();
			
			this.permiteModificar = true;
			if(this.pojoObra.getEstatus().equals(10000798L)) 
				this.permiteModificar = false;

			if (this.listObraAlmacenes == null)
				this.listObraAlmacenes = new ArrayList<ObraAlmacenes>();
			this.listObraAlmacenes.clear();

			this.ifzObraAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObraAlmacenes = this.ifzObraAlmacenes.findAll(this.pojoObra.getId(), null); //.findByProperty("idObra.id", this.pojoObra.getId(), 120);
		} catch(Exception e) {
    		control(true, -1, "Ocurrio un problema al consultas los Almacenes de la Obra", e);
    	}
	}
	
	public void nuevo() {
		try {
			control();
			this.pojoObraAlmacen = new ObraAlmacenes();
	    	this.pojoObraAlmacenBorrar = null;
	    	this.pojoAlmacen = null;
	    	this.almacenPrincipal = false;

			this.almacenesAdministrativos = false;
			if (this.pojoObra.getTipoObra() == 4)
				this.almacenesAdministrativos = true;
	    	
	    	cargaAlmacenes();
		} catch(Exception e) {
			control(true, -1, "Ocurrio un problema al generar una nuevo Registro", e);
    	}
	}
	
	public void editar() {
		try {
			control();
			this.ifzAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
	    	this.pojoAlmacen = this.ifzAlmacenes.findById(this.pojoObraAlmacen.getIdAlmacen());
	    	this.almacenPrincipal = (this.pojoObraAlmacen.getAlmacenPrincipal() == 1) ? true : false;

			this.almacenesAdministrativos = false;
			if (this.pojoObra.getTipoObra() == 4)
				this.almacenesAdministrativos = true;
			
	    	cargaAlmacenes();
		} catch(Exception e) {
			control(true, -1, "Ocurrio un problema al intentar editar el registro", e);
    	}
	}
	
	public void guardar() {
		try {
			control();
        	if (! validarAlmacenObra()) 
				return;
        	
    		this.pojoObraAlmacen.setIdObra(this.pojoObra);
    		this.pojoObraAlmacen.setNombreObra(this.pojoObra.getNombre());
    		this.pojoObraAlmacen.setIdAlmacen(this.pojoAlmacen.getId());
    		this.pojoObraAlmacen.setNombreAlmacen(this.pojoAlmacen.getNombre());
    		this.pojoObraAlmacen.setTipo(this.pojoAlmacen.getTipo());
    		this.pojoObraAlmacen.setAlmacenPrincipal((this.almacenPrincipal ? 1 : 0));

        	if (this.pojoObraAlmacen.getId() == null || this.pojoObraAlmacen.getId() <= 0L) {
	        	this.pojoObraAlmacen.setCreadoPor(this.usuarioId);
        		this.pojoObraAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
        		this.pojoObraAlmacen.setModificadoPor(this.usuarioId);
        		this.pojoObraAlmacen.setFechaModificacion(Calendar.getInstance().getTime());
        		
	        	// Guardamos en la BD
        		this.pojoObraAlmacen.setId(this.ifzObraAlmacenes.save(this.pojoObraAlmacen));
        		
        		// Agregamos a la LISTA
        		this.listObraAlmacenes.add(this.pojoObraAlmacen);
        	} else {
        		this.pojoObraAlmacen.setModificadoPor(this.usuarioId);
        		this.pojoObraAlmacen.setFechaModificacion(Calendar.getInstance().getTime());
        		
        		// Actualizamos en la BD
        		this.ifzObraAlmacenes.update(this.pojoObraAlmacen);
        		
        		// Actualizamos en la LISTA
        		for(ObraAlmacenes var : this.listObraAlmacenes) {
	        		if (var.getId().longValue() == this.pojoObraAlmacen.getId().longValue()) {
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
			control(true, -1, "Ocurrio un problema al guardar el Almacen en la Obra", e);
    	}
	}
	
	public void eliminar() {
		try {
			control();
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
			control(true, -1, "Ocurrio un problema al intentar Eliminar el registro", e);
    	}
	}

	private void control() {
		control(false, 0, "", null);
	}
	
	private void control(boolean val1, int val2, String val3) {
		this.incompleto = val1;
		this.tipoMensaje = val2;
		this.mensaje = val3;
		this.mensajeDetalles = "";
	}
	
	private void control(boolean val1, int val2, String val3, Throwable t) {
		this.incompleto = val1;
		this.tipoMensaje = val2;
		this.mensaje = val3;
		
		if (this.mensaje != null && this.mensaje.contains("\n"))
			this.mensaje = this.mensaje.replace("\n", "<br>");
		
		this.mensajeDetalles = "";
		if (t != null) {
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// ALMACENES
	// -------------------------------------------------------------------------------------------------------------------

    private void cargaAlmacenes() {
    	List<Almacen> listaAlmacenesFull = null;
    	
    	try {
    		control();
    		if (this.listAlmacenes == null)
    			this.listAlmacenes = new ArrayList<Almacen>();
    		this.listAlmacenes.clear();
    		
    		if (this.listAlmacenesItems == null)
    			this.listAlmacenesItems = new ArrayList<SelectItem>();
    		this.listAlmacenesItems.clear();

			this.ifzAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
    		listaAlmacenesFull = this.ifzAlmacenes.findAll();
    		if (listaAlmacenesFull != null && ! listaAlmacenesFull.isEmpty()) {
    			for (Almacen var : listaAlmacenesFull) {
    				if (this.almacenesAdministrativos && var.getTipo() > 2) {
    					this.listAlmacenes.add(var);
    				} else if (! this.almacenesAdministrativos && var.getTipo() <= 2) {
    					this.listAlmacenes.add(var);
    				}
    			}
    			
    			Collections.sort(this.listAlmacenes, new Comparator<Almacen>() {
    				@Override
    				public int compare(Almacen o1, Almacen o2) {
    					return o1.getNombre().compareTo(o2.getNombre());
    				}
    			});
    		}

			if (this.listAlmacenes == null || this.listAlmacenes.isEmpty()) {
	    		control(true, -1, "No se encontro ningun Almacen disponible");
				return;
			}
			
			for (Almacen var : this.listAlmacenes)
				this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getNombre()));
    	} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al consultar los Almacenes", e);
    	}
    }

    public boolean comprobarAlmacenPrincipalDeObra() {
    	if (this.almacenPrincipal) {
        	for (ObraAlmacenes item : this.listObraAlmacenes) {
    			if (item.getIdAlmacen() != this.pojoAlmacen.getId().longValue() && item.getAlmacenPrincipal() == 1) {
    				control(true, 16, "Ya existe un Almacen marcado como Principal");
    	    		return false;
    			}
    		}
    	}
    	
    	return true;
    }
    
    public boolean validarAlmacenObra() {
    	try {
    		control();
			if (this.listObraAlmacenes == null) 
        		this.listObraAlmacenes = new ArrayList<ObraAlmacenes>();
			
        	if (this.pojoObraAlmacen == null)
        		this.pojoObraAlmacen = new ObraAlmacenes();
        	
        	if (this.pojoAlmacen == null || this.pojoAlmacen.getId() == null || this.pojoAlmacen.getId() <= 0L) {
        		control(true, -1, "Debe seleccionar un Almacen");
        		return false;
        	}
        	
    		// Comprobamos que el almacen asignado no exista en la Obra/Proyecto.
        	for(ObraAlmacenes var : this.listObraAlmacenes) {
        		if (var.getIdAlmacen() == this.pojoAlmacen.getId().longValue()) {
        			if (this.pojoObraAlmacen != null && this.pojoObraAlmacen.getId() != null && this.pojoObraAlmacen.getId() > 0L && var.getId().longValue() == this.pojoObraAlmacen.getId().longValue())
        				continue;
        			
        			control(true, 10, "El Almacen indicado ya existe en la Obra");
	        		return false;
        		}
        	}
        	
        	if (! comprobarAlmacenPrincipalDeObra())
        		return false;
    		
	    	return true;
    	} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al validar los datos para a�adir a la Obra el Almacen seleccionado", e);
    		return false;
    	}
    }
    
    public void reemplazarAlmacenPrincipal() {
    	try {
    		control();
	    	for (ObraAlmacenes item : this.listObraAlmacenes) {
	    		if (this.pojoObraAlmacen.getId().longValue() == item.getId().longValue()) 
	    			continue;
	    		item.setAlmacenPrincipal(0);
	    		this.ifzObraAlmacenes.update(item);
			}
	    	
	    	this.guardar();
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar Reemplazar el Almacen marcado como principal", e);
    	}
    }

	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

    public String getAlmacenIdentificador() {
    	if (this.pojoAlmacen != null && this.pojoAlmacen.getId() != null && this.pojoAlmacen.getId() > 0L)
    		return this.pojoAlmacen.getIdentificador();
    	return "";
    }
    
    public void setAlmacenIdentificador(String value) {}

    public String getAlmacenTipo() {
    	if (this.pojoAlmacen != null && this.pojoAlmacen.getId() != null && this.pojoAlmacen.getId() > 0L)
    		return (this.pojoAlmacen.getTipo() == 1 || this.pojoAlmacen.getTipo() == 3 ? "General" : "Obra");
    	return "";
    }
    
    public void setAlmacenTipo(String value) {}

    public boolean getDeshabilitaPrincipal() {
    	if (this.pojoAlmacen != null && this.pojoAlmacen.getId() != null && this.pojoAlmacen.getId() > 0L)
    		return (this.pojoAlmacen.getTipo() == 2 || this.pojoAlmacen.getTipo() == 4);
    	return false;
    }
    
    public void setDeshabilitaPrincipal(boolean value) {}
    
	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public long getAlmacenId() {
		if (this.pojoAlmacen != null && this.pojoAlmacen.getId() != null && this.pojoAlmacen.getId() > 0L)
			return this.pojoAlmacen.getId();
		return 0;
	}

	public void setAlmacenId(long almacenId) {
		if (almacenId <= 0L)
			return;
		
		// Recuperamos el almacen
		for (Almacen var : this.listAlmacenes) {
			if (almacenId == var.getId()) { 
				this.pojoAlmacen = var;
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

	public boolean isPermiteModificar() {
		return permiteModificar;
	}

	public void setPermiteModificar(boolean permiteModificar) {
		this.permiteModificar = permiteModificar;
	}

	public boolean getPerfilAdministrativo() {
		return PERFIL_ADMINISTRATIVO;
	}
	
	public void setPerfilAdministrativo(boolean perfilAdministrativo) {
		PERFIL_ADMINISTRATIVO = perfilAdministrativo;
	}

	public boolean getBusquedaAdministrativas() {
		return busquedaAdministrativas;
	}

	public void setBusquedaAdministrativas(boolean busquedaAdministrativas) {
		this.busquedaAdministrativas = busquedaAdministrativas;
	}

	// ----------------------------------------------------------------------
	// PERMISOS
	// ----------------------------------------------------------------------

    private void establecerPermisos() {
    	this.permisoAgregar = true;
    	this.permisoEditar = true;
    	this.permisoBorrar = true;
    	this.permisoImprimir = true;
    }
    
    private void validarPermisos() {
    	this.permisoAgregar = true;
    	this.permisoEditar = true;
    	this.permisoBorrar = true;
    	this.permisoImprimir = true;
    }
    
	public boolean isPermisoEscritura() {
		return true;
	}

	public void setPermisoEscritura(boolean permisoAgregar) {}

	public boolean isPermisoAgregar() {
		return permisoAgregar;
	}

	public void setPermisoAgregar(boolean permisoAgregar) {
		this.permisoAgregar = permisoAgregar;
	}

	public boolean isPermisoEditar() {
		return permisoEditar;
	}

	public void setPermisoEditar(boolean permisoEditar) {
		this.permisoEditar = permisoEditar;
	}

	public boolean isPermisoBorrar() {
		return permisoBorrar;
	}

	public void setPermisoBorrar(boolean permisoBorrar) {
		this.permisoBorrar = permisoBorrar;
	}

	public boolean isPermisoImprimir() {
		return permisoImprimir;
	}

	public void setPermisoImprimir(boolean permisoImprimir) {
		this.permisoImprimir = permisoImprimir;
	}
}

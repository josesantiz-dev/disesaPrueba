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
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.adp.logica.ObraEmpleadoRem;
import net.giro.adp.logica.ObraRem;
import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="empObraAction")
public class EmpleadosObrasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(EmpleadosObrasAction.class);
	private InitialContext ctx; 
	private LoginManager loginManager; 
	// Obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	// Obras Empleados
	private ObraEmpleadoRem ifzObraEmpleados;
	private List<ObraEmpleadoExt> listObraEmpleados;
	private ObraEmpleadoExt pojoObraEmpleado;
	private int numPaginaObraEmpleados;
	// Busqueda
	private List<SelectItem> busquedaOpciones;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaTipo;
	private int busquedaPaginas;
	private boolean busquedaAdministrativas;
	// Busqueda empleados
	private EmpleadoRem ifzEmpleados;
	private List<Empleado> listEmpleados;
	private Empleado pojoEmpleado;
	private List<SelectItem> busquedaEmpleadosOpciones;	
	private String busquedaEmpleadosCampo;
	private String busquedaEmpleadosValor;
	private int busquedaEmpleadosPagina;
	// Variables de control
	private boolean incompleto;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
    private long usuarioId;
    private boolean permiteModificar;
    private double valObraCancelada;
    // PERFILES
	private boolean PERFIL_ADMINISTRATIVO;
    
	
	public EmpleadosObrasAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String resValue = "";
		//long valGpo = 0;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();

			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObraEmpleados = (ObraEmpleadoRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraEmpleadoFac!net.giro.adp.logica.ObraEmpleadoRem");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			//this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			//this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
			//this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			//this.porcentajeIva = Double.valueOf(this.loginManager.getAutentificacion().getPerfil("VALOR_IVA"));
			//this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());

			this.ifzObras.showSystemOuts(false);
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "ID"));
			this.busquedaCampo = busquedaOpciones.get(0).getDescription();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			
			// Busquera empleados
			this.busquedaEmpleadosOpciones = new ArrayList<SelectItem>();
			this.busquedaEmpleadosOpciones.add(new SelectItem("nombre", "Nombre"));
			this.busquedaEmpleadosOpciones.add(new SelectItem("id", "Clave"));
			this.busquedaEmpleadosCampo = this.busquedaEmpleadosOpciones.get(0).getDescription();
			this.busquedaEmpleadosValor = "";
			this.busquedaEmpleadosPagina = 1;
			
			this.valObraCancelada = 10000798L;
			this.numPaginaObraEmpleados = 1;
		} catch (Exception e) {
			this.ctx = null;
			log.error("Error en constructor GestionProyectos.EmpleadosObrasAction", e);
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = "nombre";
			
			if (this.listObras == null)
				this.listObras = new ArrayList<Obra>();
			this.listObras.clear();

			this.busquedaTipo = 0;
			if (this.busquedaAdministrativas)
				this.busquedaTipo = 4;
			
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo);
			if (this.listObras.isEmpty()) {
				control(true, 2, "ERROR");
				return;
			}
		} catch(Exception e) {
			control(true, 1, e.getMessage(), e);
    		log.error("Error en GestionProyectos.EmpleadosObrasAction.buscar", e);
    	}
	}
	
	public void ver() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}
			
			this.permiteModificar = true;
			if(this.pojoObra.getEstatus().equals(10000798L)) 
				this.permiteModificar = false;

			this.listObraEmpleados = this.ifzObraEmpleados.findByPropertyExt("idObra", this.pojoObra.getId(), 0);
			if (this.listObraEmpleados != null && ! this.listObraEmpleados.isEmpty()) {
				Collections.sort(this.listObraEmpleados, new Comparator<ObraEmpleadoExt>() {
			    	@Override
			        public int compare(ObraEmpleadoExt o1, ObraEmpleadoExt o2) {
			    		return o1.getEmpleadoNombreApellidos().compareTo(o2.getEmpleadoNombreApellidos());
			        }
				});
			}
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.EmpleadosObrasAction.ver", e);
			control(true, 1, e.getMessage(), e);
    	}
	}
	
	public void eliminar() {
		try {
			control();

			if(this.pojoObraEmpleado != null && this.pojoObraEmpleado.getId() > 0L) {
				// Eliminamos de la BD
				this.ifzObraEmpleados.delete(this.pojoObraEmpleado.getId());
				
				// Eliminamos de la lista
				this.listObraEmpleados.remove(this.pojoObraEmpleado);
			}
	    	
	    	ver();
		} catch(Exception e) {
			control(true, 1, e.getMessage(), e);
    		log.error("Error en GestionProyectos.EmpleadosObrasAction.eliminar", e);
    	}
	}

    public void nuevaBusquedaEmpleados() {
    	this.busquedaEmpleadosCampo = this.busquedaEmpleadosOpciones.get(0).getDescription();
		this.busquedaEmpleadosValor = "";
		
    	this.pojoObraEmpleado = new ObraEmpleadoExt();
    	if (this.listEmpleados == null)
    		this.listEmpleados = new ArrayList<Empleado>();
    	this.listEmpleados.clear();
    	this.pojoEmpleado = new Empleado();
    }
    
    public String buscarEmpleados() throws Exception {
    	try {
    		control();
			
			if ("".equals(this.busquedaEmpleadosCampo))
				this.busquedaEmpleadosCampo = "id";
			
			this.listEmpleados = this.ifzEmpleados.findLikeProperty(this.busquedaEmpleadosCampo, this.busquedaEmpleadosValor, 0);
			if(this.listEmpleados.isEmpty()){
				control(true, 2, "ERROR");
				return this.mensaje;
			}

			this.mensaje = "OK";
    	} catch (Exception e) {
    		control(true, 1, e.getMessage(), e);
    		log.error("Error en buscarEmpleados", e);
    	}
    	
    	return this.mensaje;
    }

    public void seleccionarEmpleado() throws Exception {
    	EmpleadoExt pojoExt = null;
    	boolean existe = false;
    	
    	try {
    		control();
			
        	if (this.listObraEmpleados == null) 
        		this.listObraEmpleados = new ArrayList<ObraEmpleadoExt>();
        	
        	for(ObraEmpleadoExt var : this.listObraEmpleados) {
        		if ((long) var.getEmpleadoId() == (long) this.pojoEmpleado.getId()) {
        			existe = true;
        			break;
        		}
        	}
        	
        	if (! existe) {
    			log.info("Extiendo responsable (Empleado) seleccionado");
    			pojoExt = this.ifzEmpleados.convertir(this.pojoEmpleado);
    			
        		this.pojoObraEmpleado = new ObraEmpleadoExt();
        		this.pojoObraEmpleado.setCreadoPor(this.usuarioId);
        		this.pojoObraEmpleado.setFechaCreacion(Calendar.getInstance().getTime());
        		this.pojoObraEmpleado.setModificadoPor(this.usuarioId);
        		this.pojoObraEmpleado.setFechaModificacion(Calendar.getInstance().getTime());
        		this.pojoObraEmpleado.setIdEmpleado(pojoExt);
        		this.pojoObraEmpleado.setIdObra(this.ifzObras.findByIdExt(this.pojoObra.getId()));
        		
        		// Guardamos en la BD
        		this.pojoObraEmpleado.setId(this.ifzObraEmpleados.save(this.pojoObraEmpleado));
        		
        		// Agregamos a la lista
        		this.listObraEmpleados.add(this.pojoObraEmpleado);
        	} else {
        		log.info("El Empleado seleccionado ya existe en la lista");
        		control(true, -1, "Este empleado ya existe en " + (this.pojoObra.getTipoObra() == 1 ? "la obra" : "el proyecto"));
        	}
        	
        	nuevaBusquedaEmpleados();
    	} catch (Exception e) {
    		log.error("Error en seleccionarEmpleado", e);
    		control(true, -1, "ERROR");
    	}
    }

	private void control() {
		control(false, 0, "", null);
	}
	
	private void control(boolean procesoInterrumpido, int tipo, String mensaje) {
		control(procesoInterrumpido, tipo, mensaje, null);
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
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

	public boolean getScrolling() {
		if (this.listObraEmpleados != null && ! this.listObraEmpleados.isEmpty() && this.listObraEmpleados.size() > 9)
			return true;
		return false;
	}
	
	public void setScrolling(boolean value) {}
	
	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
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

	public List<ObraEmpleadoExt> getListObraEmpleados() {
		return listObraEmpleados;
	}

	public void setListObraEmpleados(List<ObraEmpleadoExt> listObraEmpleados) {
		this.listObraEmpleados = listObraEmpleados;
	}

	public ObraEmpleadoExt getPojoObraEmpleado() {
		if (pojoObraEmpleado == null) pojoObraEmpleado = new ObraEmpleadoExt();
		return pojoObraEmpleado;
	}

	public void setPojoObraEmpleado(ObraEmpleadoExt pojoObraEmpleado) {
		this.pojoObraEmpleado = pojoObraEmpleado;
	}

	public int getNumPaginaObraEmpleados() {
		return numPaginaObraEmpleados;
	}

	public void setNumPaginaObraEmpleados(int numPaginaObraEmpleados) {
		this.numPaginaObraEmpleados = numPaginaObraEmpleados;
	}

	public List<Empleado> getListEmpleados() {
		return listEmpleados;
	}

	public void setListEmpleados(List<Empleado> listEmpleados) {
		this.listEmpleados = listEmpleados;
	}

	public Empleado getPojoEmpleado() {
		if (pojoEmpleado == null) pojoEmpleado = new Empleado();
		return pojoEmpleado;
	}

	public void setPojoEmpleado(Empleado pojoEmpleado) {
		this.pojoEmpleado = pojoEmpleado;
	}

	public List<SelectItem> getBusquedaEmpleadosOpciones() {
		return busquedaEmpleadosOpciones;
	}

	public void setBusquedaEmpleadosOpciones(List<SelectItem> busquedaEmpleadosOpciones) {
		this.busquedaEmpleadosOpciones = busquedaEmpleadosOpciones;
	}

	public String getBusquedaEmpleadosCampo() {
		return busquedaEmpleadosCampo;
	}

	public void setBusquedaEmpleadosCampo(String busquedaEmpleadosCampo) {
		this.busquedaEmpleadosCampo = busquedaEmpleadosCampo;
	}

	public String getBusquedaEmpleadosValor() {
		return busquedaEmpleadosValor;
	}

	public void setBusquedaEmpleadosValor(String busquedaEmpleadosValor) {
		this.busquedaEmpleadosValor = busquedaEmpleadosValor;
	}

	public int getBusquedaEmpleadosPagina() {
		return busquedaEmpleadosPagina;
	}

	public void setBusquedaEmpleadosPagina(int busquedaEmpleadosPagina) {
		this.busquedaEmpleadosPagina = busquedaEmpleadosPagina;
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
}

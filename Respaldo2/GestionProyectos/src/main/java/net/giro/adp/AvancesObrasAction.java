package net.giro.adp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
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
import net.giro.adp.beans.ObraAvance;
import net.giro.adp.logica.ObraAvanceRem;
import net.giro.adp.logica.ObraRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="avanceAction")
public class AvancesObrasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AvancesObrasAction.class);	
	private InitialContext ctx; 
	private LoginManager loginManager; 
	// Obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;	
	// Obras Avances
	private ObraAvanceRem ifzObraAvance;
	private List<ObraAvance> listObraAvances;
	private List<ObraAvance> listObraAvancesEliminados;
	private ObraAvance pojoObraAvance;
	private ObraAvance pojoObraAvanceBorrar;
	private int paginaObraAvance;
	// Busqueda
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
	private String usuario;
    private double avance;
    private double restante;
    // PERFILES
	private boolean PERFIL_ADMINISTRATIVO;
	
	
	public AvancesObrasAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String resValue = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);

			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObraAvance = (ObraAvanceRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAvanceFac!net.giro.adp.logica.ObraAvanceRem");
			
			this.ifzObras.showSystemOuts(false);
			this.ifzObraAvance.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "ID"));
			this.busquedaCampo = this.busquedaOpciones.get(0).getValue().toString();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
		} catch (Exception e) {
			this.ctx = null;
			log.error("Error en constructor GestionProyectos.AlmacenesObrasAction", e);
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

			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo);
			if (this.listObras.isEmpty()) {
				control(true, 2, "ERROR");
				return;
			}
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.buscar", e);
			control(true, 1, "ERROR", e);
    	}
	}
	
	public void ver() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}

			if(this.listObraAvances == null)
				this.listObraAvances = new ArrayList<ObraAvance>();
			this.listObraAvances.clear();
			
			this.ifzObraAvance.orderBy("fecha DESC");
			this.listObraAvances = this.ifzObraAvance.findByProperty("idObra.id", this.pojoObra.getId(), 0);
			totalizar();
			nuevo();
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.ver", e);
			control(true, 1, "ERROR", e);
    	}
	}
	
	public void nuevo() {
		try {
			control();

			this.pojoObraAvance = null;
			this.pojoObraAvance = new ObraAvance();
			this.pojoObraAvanceBorrar = null;
			totalizar();
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.nuevo", e);
			control(true, 1, "ERROR", e);
    	}
	}
	
	public void agregar() {
		double porcentajeTotal = 0;
		
		try {
			control();
			if (this.pojoObraAvance != null) {
				if (this.pojoObraAvance.getPorcentaje() <= 0 || this.pojoObraAvance.getPorcentaje() > 100) {
					control(true, 7, "El valor del porcentaje debe ser mayor a 0 y menor a 100");
					log.error("Error en el metodo agregarObraAvance: " + this.mensaje);
					return;
				}
				
				// Inicializamos el listado si hace falta
				if (this.listObraAvances == null)
					this.listObraAvances = new ArrayList<ObraAvance>();
				
				// Comprobamos el porcentaje total actual mas el nuevo porcentaje
				for (ObraAvance var : this.listObraAvances)
					porcentajeTotal += var.getPorcentaje();
					
				if ((porcentajeTotal + this.pojoObraAvance.getPorcentaje()) > 100) {
					control(true, 14, "El porcentaje total supera el 100 %.");
					return;
				}

				// Asignamos la Obra y lo agregamos a la lista
				this.pojoObraAvance.setIdObra(this.pojoObra);
				if(this.listObraAvances.isEmpty())
					this.listObraAvances.add(this.pojoObraAvance);
				else
					this.listObraAvances.add(0, this.pojoObraAvance);
				totalizar();
			}
			
			nuevo();
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.editar", e);
			control(true, 1, "ERROR", e);
    	}
	}
	
	public void guardar() {
		try {
			control();
			// Borramos los avances que corrrespondan
			if (this.listObraAvancesEliminados != null && ! this.listObraAvancesEliminados.isEmpty()) {
				for(ObraAvance var : this.listObraAvancesEliminados) {
					if (var.getId() != null && var.getId() > 0L)
						this.ifzObraAvance.delete(var.getId());
				}
				
				this.listObraAvancesEliminados.clear();
			}
			
			if (this.listObraAvances != null && ! this.listObraAvances.isEmpty()) {
				for (ObraAvance var : this.listObraAvances) {
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Guardamos o actualizamos en la BD segun corresponda
					if (var.getId() == null || var.getId() <= 0L) {
						var.setCreadoPor(this.usuarioId);
						var.setFechaCreacion(Calendar.getInstance().getTime());
						
						var.setId(this.ifzObraAvance.save(var));
					} else {
						this.ifzObraAvance.update(var);
					}
				}
			}
        	
        	nuevo();
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.guardar", e);
			control(true, 1, "ERROR", e);
    	}
	}
	
	public void eliminar() {
		try {
			control();
			if (this.pojoObraAvanceBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoObraAvanceBorrar.getId() != null && this.pojoObraAvanceBorrar.getId() > 0L)
					this.ifzObraAvance.delete(this.pojoObraAvanceBorrar.getId());
				
				// Borramos de la lista
				this.listObraAvances.remove(this.pojoObraAvanceBorrar);
				totalizar();
			}
	    	
	    	nuevo();
		} catch(Exception e) {
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.eliminar", e);
			control(true, 1, "ERROR", e);
    	}
	}

	public void totalizar() {
		this.avance = 0;
		this.restante = 100;
		
		if (this.listObraAvances == null || this.listObraAvances.isEmpty())
			return;
		
		for (ObraAvance var : this.listObraAvances)
			this.avance += var.getPorcentaje();
		this.restante = this.restante - this.avance;
		if (this.restante < 0) {
			this.avance += (this.restante * -1);
			this.restante = 0;
		}
	}
	
	private void control() {
		this.incompleto = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}
	
	private void control(boolean procesoInterrumpido, int tipo, String mensaje) {
		control(procesoInterrumpido, tipo, mensaje, null);
	}
	
	private void control(boolean procesoInterrumpido, int tipo, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.incompleto = procesoInterrumpido;
		this.tipoMensaje = tipo;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		if (this.incompleto)
			log.error("\n\nTRASPASOS :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n", throwable);
	}
	
	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

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
	
	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
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
	
	public int getPaginaObraAvance() {
		return paginaObraAvance;
	}

	public void setPaginaObraAvance(int paginaObraAvance) {
		this.paginaObraAvance = paginaObraAvance;
	}
	
	public List<ObraAvance> getListObraAvances() {
		return listObraAvances;
	}

	public void setListObraAvances(List<ObraAvance> listObraAvances) {
		this.listObraAvances = listObraAvances;
	}

	public ObraAvance getPojoObraAvance() {
		if (this.pojoObraAvance == null) 
			this.pojoObraAvance = new ObraAvance();
		return this.pojoObraAvance;
	}

	public void setPojoObraAvance(ObraAvance pojoObraAvance) {
		this.pojoObraAvance = pojoObraAvance;
	}

	public ObraAvance getPojoObraAvanceBorrar() {
		return pojoObraAvanceBorrar;
	}

	public void setPojoObraAvanceBorrar(ObraAvance pojoObraAvanceBorrar) {
		this.pojoObraAvanceBorrar = pojoObraAvanceBorrar;
	}

	public double getAvance() {
		return avance;
	}
	
	public void setAvance(double avance) {
		this.avance = avance;
	}
	
	public double getRestante() {
		return restante;
	}

	public void setRestante(double restante) {
		this.restante = restante;
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

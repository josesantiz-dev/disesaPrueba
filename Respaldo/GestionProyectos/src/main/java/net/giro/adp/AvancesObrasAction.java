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
	// Variables de control
	private boolean incompleto;
	private int tipoMensaje;
	private String mensaje;
    private long usuarioId;
	
	
	public AvancesObrasAction() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObraAvance = (ObraAvanceRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAvanceFac!net.giro.adp.logica.ObraAvanceRem");
			
			// Inicializaciones
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = 0;
			this.usuarioId = (long) this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			//this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			//valPerfil = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			//this.porcentajeIva = ((valPerfil != null && "S".equals(valPerfil)) ? Double.valueOf(valPerfil) : 0);
			
			this.ifzObras.showSystemOuts(false);
			this.ifzObraAvance.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObraAvance.showSystemOuts(false);
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "Clave"));
			this.busquedaCampo = busquedaOpciones.get(0).getDescription();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
		} catch (Exception e) {
			this.ctx = null;
			log.error("Error en constructor GestionProyectos.AlmacenesObrasAction", e);
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

			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
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

			if(this.listObraAvances == null)
				this.listObraAvances = new ArrayList<ObraAvance>();
			this.listObraAvances.clear();
			
			this.ifzObraAvance.orderBy("fecha DESC");
			this.listObraAvances = this.ifzObraAvance.findByProperty("idObra.id", this.pojoObra.getId(), 0);
			nuevo();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.ver", e);
    	}
	}
	
	public void nuevo() {
		try {
			control(false, 0, "");

			this.pojoObraAvance = null;
			this.pojoObraAvance = new ObraAvance();
			this.pojoObraAvanceBorrar = null;
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.nuevo", e);
    	}
	}
	
	public void agregar() {
		try {
			control(false, 0, "");

			if(this.pojoObraAvance != null) {
				if(this.pojoObraAvance.getPorcentaje() <= 0 || this.pojoObraAvance.getPorcentaje() > 100) {
					control(true, 7, "El valor del porcentaje debe ser mayor a 0 y menor a 100");
					log.error("Error en el metodo agregarObraAvance: " + this.mensaje);
					return;
				}
				
				// Inicializamos el listado si hace falta
				if(this.listObraAvances == null)
					this.listObraAvances = new ArrayList<ObraAvance>();
				
				// Comprobamos el porcentaje total actual mas el nuevo porcentaje
				double porcentajeTotal = 0;
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
			}
			
			nuevo();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.editar", e);
    	}
	}
	
	public void guardar() {
		try {
			control(false, 0, "");

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
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.guardar", e);
    	}
	}
	
	public void eliminar() {
		try {
			control(false, 0, "");

			if(this.pojoObraAvanceBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoObraAvanceBorrar.getId() != null && this.pojoObraAvanceBorrar.getId() > 0L)
					this.ifzObraAvance.delete(this.pojoObraAvanceBorrar.getId());
				
				// Borramos de la lista
				this.listObraAvances.remove(this.pojoObraAvanceBorrar);
			}
	    	
	    	nuevo();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.AlmacenesObrasAction.eliminar", e);
    	}
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
		if (pojoObraAvance == null) pojoObraAvance = new ObraAvance();
		return pojoObraAvance;
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
}

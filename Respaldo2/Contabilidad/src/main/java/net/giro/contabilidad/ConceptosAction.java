package net.giro.contabilidad;

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

import net.giro.contabilidad.beans.Conceptos;
import net.giro.contabilidad.logica.ConceptosRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="conceptosAction")
public class ConceptosAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ConceptosAction.class);
	private InitialContext ctx;
	
	// Interfaces
	private ConceptosRem ifzConceptos;
	// Listas
	private List<Conceptos> listConceptos;
	// POJO's
	private Conceptos pojoConcepto;
	private Conceptos pojoConceptoBorrar;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Variables de operacion
	private LoginManager loginManager;
    private long usuarioId = 0;
    
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
    
    
    public ConceptosAction() {
    	try {
			FacesContext fc 		= FacesContext.getCurrentInstance();
			Application app 		= fc.getApplication();
			ValueExpression ve 		= app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			
			this.loginManager 		= (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId 			= this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzConceptos = (ConceptosRem) this.ctx.lookup("ejb:/Logica_Contabilidad//ConceptosFac!net.giro.contabilidad.logica.ConceptosRem");
			this.ifzConceptos.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Listas
			this.listConceptos = new ArrayList<Conceptos>();
			
			// POJO's
			this.pojoConcepto = new Conceptos();
			this.pojoConceptoBorrar = null;
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
    	} catch (Exception e) {
			log.error("Error en constructor ConceptosAction", e);
			this.ctx = null;
    	}
    }
    
    
    public void buscar() throws Exception {
    	try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = "id";

			// Realizamos la busqueda
			this.ifzConceptos.orderBy("estatus, descripcion"); 
			this.listConceptos = this.ifzConceptos.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listConceptos.isEmpty()) {
	    		this.operacion = true;
				this.tipoMensaje = 2;
				this.mensaje = "ERROR";
				return;
			}
    	} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor ConceptosAction.buscar", e);
			throw e;
    	}
    }
    
    public void nuevo() {
    	try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoConcepto = new Conceptos();
			this.pojoConceptoBorrar = null;
    	} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			log.error("Error en constructor ConceptosAction.nuevo", e);
			throw e;
    	}
    }
    
    public void editar() {
    	try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
    	} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor ConceptosAction.editar", e);
			throw e;
    	}
    }
    
    public void borrar() throws Exception {
    	try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoConceptoBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoConceptoBorrar.getId() != null && this.pojoConceptoBorrar.getId() > 0L)
					this.ifzConceptos.delete(this.pojoConceptoBorrar.getId());
				
				// Borramos del listado
				this.listConceptos.remove(pojoConceptoBorrar);
				
				this.pojoConceptoBorrar = null;
			}
    	} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor ConceptosAction.borrar", e);
			throw e;
    	}
    }
    
    public void cancelar() throws Exception {
    	try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			int index = -1;
			if (this.pojoConceptoBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoConceptoBorrar.getId() != null && this.pojoConceptoBorrar.getId() > 0L) {
					index = this.listConceptos.indexOf(this.pojoConceptoBorrar);
					this.pojoConceptoBorrar = this.ifzConceptos.cancelar(this.pojoConceptoBorrar);
				}
				
				if (index >= 0)
					this.listConceptos.set(index, this.pojoConceptoBorrar);
			}
    	} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor ConceptosAction.borrar", e);
			throw e;
    	}
    }
    
    public void salvar() throws Exception {
    	try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoConcepto != null) {
				this.pojoConcepto.setModificadoPor(this.usuarioId);
				this.pojoConcepto.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoConcepto.getId() == null || this.pojoConcepto.getId() <= 0L) {
					this.pojoConcepto.setCreadoPor(this.usuarioId);
					this.pojoConcepto.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoConcepto.setId(this.ifzConceptos.save(this.pojoConcepto));
					
					// Agregamos a la lista
					this.listConceptos.add(this.pojoConcepto);
				} else {
					// Actualizamos en la BD
					this.ifzConceptos.update(this.pojoConcepto);
					
					// Actualizamos en el listado
					for (Conceptos var : this.listConceptos) {
						if (this.pojoConcepto.getId().equals(var.getId())) {
							var = this.pojoConcepto;
							break;
						}
					}
				}
			}
    	} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor ConceptosAction.salvar", e);
			throw e;
    	}
    }

    /* 
     * ----------------------------------------------------------------------------------------
     * PROPIEDADES
     * ----------------------------------------------------------------------------------------
     */
	    
    public List<Conceptos> getListConceptos() {
		return listConceptos;
	}

	public void setListConceptos(List<Conceptos> listConceptos) {
		this.listConceptos = listConceptos;
	}

	public Conceptos getPojoConcepto() {
		return pojoConcepto;
	}

	public void setPojoConcepto(Conceptos pojoConcepto) {
		this.pojoConcepto = pojoConcepto;
	}

	public Conceptos getPojoConceptoBorrar() {
		return pojoConceptoBorrar;
	}

	public void setPojoConceptoBorrar(Conceptos pojoConceptoBorrar) {
		this.pojoConceptoBorrar = pojoConceptoBorrar;
	}

	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public boolean isOperacion() {
		return operacion;
	}

	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
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
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 30/05/2016 | Javier Tirado	| Creacion de ConceptosAction
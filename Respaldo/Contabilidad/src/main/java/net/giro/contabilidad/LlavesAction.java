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

import net.giro.contabilidad.beans.Llaves;
import net.giro.contabilidad.logica.LlavesRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="llavesAction")
public class LlavesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LlavesAction.class);
	private InitialContext ctx;
	
	// Interfaces
	private LlavesRem ifzLlaves;
	private List<Llaves> listLlaves;
	private Llaves pojoLlave;
	private Llaves pojoLlaveBorrar;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Variables de operacion
    private long usuarioId;
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	private boolean posicionOcupada = false;
	
	
	public LlavesAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		LoginManager lm = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			lm = (LoginManager) ve.getValue(fc.getELContext());
			
			this.ctx = new InitialContext();
			this.ifzLlaves = (LlavesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//LlavesFac!net.giro.contabilidad.logica.LlavesRem");
			

			this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId();
			//this.usuario = lm.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			//this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());

			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------
			
			// Listas
			this.listLlaves = new ArrayList<Llaves>();
			
			// POJO's
			this.pojoLlave = new Llaves();
			this.pojoLlaveBorrar = new Llaves();
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
		} catch (Exception e) {
			log.error("Error en constructor LlavesAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = tiposBusqueda.get(0).getValue().toString();
			
			this.ifzLlaves.orderBy("posicion, descripcion");
			this.listLlaves = this.ifzLlaves.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listLlaves.isEmpty()) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda vacia");
				return;
			}
		} catch (Exception e) {
			log.error("Error en LlavesAction.buscar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void nuevo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoLlave = new Llaves();
			this.pojoLlave.setCampoId("getId");
			this.pojoLlave.setCampoDescripcion("getDescripcion");
			this.pojoLlaveBorrar = null;
		} catch (Exception e) {
			log.error("Error en LlavesAction.buscar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void editar() {
		this.posicionOcupada = false;
	}
	
	public void guardar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoLlave != null) {
				comprobarPosicion();
				if (this.posicionOcupada) {
					log.error("Posicion " + this.pojoLlave.getPosicion() + " ocupada");
					this.operacion = true;
					this.mensaje = "ERROR";
					this.tipoMensaje = 9;
					return;
				}
				
				if (this.pojoLlave.getCampoId() == null || "".equals(this.pojoLlave.getCampoId()))
					this.pojoLlave.setCampoId("getId");
				if (this.pojoLlave.getCampoDescripcion() == null || "".equals(this.pojoLlave.getCampoDescripcion()))
					this.pojoLlave.setCampoDescripcion("getDescripcion");
				this.pojoLlave.setModificadoPor(this.usuarioId);
				this.pojoLlave.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoLlave.getId() == null || this.pojoLlave.getId() <= 0L) {
					this.pojoLlave.setCreadoPor(this.usuarioId);
					this.pojoLlave.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoLlave.setId(this.ifzLlaves.save(this.pojoLlave));
					// Agregamos a la lista
					this.listLlaves.add(this.pojoLlave);
				} else {
					// Actualizamos en la BD
					this.ifzLlaves.update(this.pojoLlave);
				}
			}
		} catch (Exception e) {
			log.error("Error en LlavesAction.guardar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void borrar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoLlaveBorrar != null && this.pojoLlaveBorrar.getId() > 0L) {
				// Borramos de la bd
				this.ifzLlaves.delete(this.pojoLlaveBorrar.getId());
				
				// Borramos de la lista
				this.listLlaves.remove(this.pojoLlaveBorrar);
			}
		} catch (Exception e) {
			log.error("Error en LlavesAction.borrar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}

	public void comprobarPosicion() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			this.posicionOcupada = false;
			if (this.pojoLlave != null && this.pojoLlave.getPosicion() > 0) {
				if(! this.ifzLlaves.comprobarPosicion(this.pojoLlave.getId(), this.pojoLlave.getPosicion())) {
					log.error("Posicion " + this.pojoLlave.getPosicion() + " ocupada");
					this.operacion = true;
					this.posicionOcupada = true;
					this.mensaje = "ERROR";
					this.tipoMensaje = 9;
					return;
				}
			}
		} catch (Exception e) {
			log.error("Error en LlavesAction.comprobarPosicion", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	
	public List<Llaves> getListLlaves() {
		return listLlaves;
	}

	public void setListLlaves(List<Llaves> listLlaves) {
		this.listLlaves = listLlaves;
	}

	public Llaves getPojoLlave() {
		return pojoLlave;
	}

	public void setPojoLlave(Llaves pojoLlave) {
		this.pojoLlave = pojoLlave;
	}

	public Llaves getPojoLlaveBorrar() {
		return pojoLlaveBorrar;
	}

	public void setPojoLlaveBorrar(Llaves pojoLlaveBorrar) {
		this.pojoLlaveBorrar = pojoLlaveBorrar;
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

	public boolean isPosicionOcupada() {
		return posicionOcupada;
	}

	public void setPosicionOcupada(boolean posicionOcupada) {
		this.posicionOcupada = posicionOcupada;
	}
}

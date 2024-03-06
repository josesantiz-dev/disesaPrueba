package net.giro.rh.admon.catalogos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.navegador.LoginManager;
import net.giro.publico.respuesta.Respuesta;
import net.giro.rh.admon.beans.EmpleadoNomina;
import net.giro.rh.admon.logica.EmpleadoNominaRem;

@ViewScoped
@ManagedBean(name="nominaAction")
public class EmpleadosNominaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(EmpleadosNominaAction.class);
	
	private InitialContext ctx;
	private EmpleadoNominaRem ifzEmpleadoNomina;
	LoginManager loginManager;
	private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	private Date fechaDesde;
	private Date fechaHasta;
	private boolean recalcularNomina;
	
	
	public EmpleadosNominaAction() {
		try {
			FacesContext fc 		= FacesContext.getCurrentInstance();
			Application app 		= fc.getApplication();
			ValueExpression ve 		= app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			//ValueExpression dato 	= app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			
			//this.entornoProperties 	= (PropertyResourceBundle) dato.getValue(fc.getELContext());
			this.loginManager 		= (LoginManager) ve.getValue(fc.getELContext());
			//this.usuarioId 			= (int) this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			//this.usuario 			= this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			this.recalcularNomina = false;

			// Interfaces
			this.ctx = new InitialContext();
			this.ifzEmpleadoNomina = (EmpleadoNominaRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoNominaFac!net.giro.rh.admon.logica.EmpleadoNominaRem");
			this.ifzEmpleadoNomina.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleadoNomina.showSystemOuts(false);
			/*
			// Listas
			this.listEmpleadoNomina = new ArrayList<EmpleadoNomina>();
			
			// POJO's
			this.pojoEmpleadoNomina = new EmpleadoNomina();
			this.pojoEmpleadoNominaBorrar = null;
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("id", "Clave"));
			this.tiposBusqueda.add(new SelectItem("nombre", "Nombre"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;*/
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoNominaAction", e);
			this.ctx = null;
		}
	}
	

	private void control() { 
		control(false, 0, "");
	}
	
	private void control(boolean value) {
		if (value)
			control(value, 1, "ERROR");
		else
			control(value, 0, null);
	}
	
	private void control(String value) { 
		if (value == null || "".equals(value))
			control(true, 1, "ERROR");
		else
			control(true, -1, value); 
	}
	
	private void control(int value) { 
		if (value <= 1)
			control(true, 1, "ERROR");
		else
			control(true, value, "ERROR"); 
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "": mensaje;
	}
	
	public void buscar() throws Exception {
		try {
			control();
			/*if ("".equals(this.campoBusqueda))
				this.campoBusqueda = "id";

			// Realizamos la busqueda
			this.ifzEmpleadoNomina.orderBy("estatus, id DESC");
			this.listEmpleadoNomina = this.ifzEmpleadoNomina.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 0);
			if (this.listEmpleadoNomina.isEmpty()) {
				log.info("ERROR 2 - Busqueda sin resultados");
				control(2);
				return;
			}*/
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoNominaAction.buscar", e);
			control(true);
			throw e;
		}
	}
	
	public void nuevo() {
		try {
			control();
			/*this.pojoEmpleadoNomina = new EmpleadoNomina();
			this.pojoEmpleadoNominaBorrar = null;*/
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoNominaAction.nuevo", e);
			control(true);
			throw e;
		}
	}
	
	public void editar() {
		try {
			control();
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoNominaAction.editar", e);
			control(true);
			throw e;
		}
	}
	
	public void borrar() throws Exception {
		try {
			control();
			/*if (this.pojoEmpleadoNominaBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoEmpleadoNominaBorrar.getId() != null && this.pojoEmpleadoNominaBorrar.getId() > 0L)
					this.ifzEmpleadoNomina.delete(this.pojoEmpleadoNominaBorrar.getId());
				
				// Borramos del listado
				this.listEmpleadoNomina.remove(pojoEmpleadoNominaBorrar);

				this.pojoEmpleadoNominaBorrar = null;
			}*/
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoNominaAction.borrar", e);
			control(true);
			throw e;
		}
	}

	public void cancelar() throws Exception {
    	try {
			control();
			/*int index = -1;
			if (this.pojoEmpleadoNominaBorrar != null) {
				// Cancelamos en la BD si corresponde
				if (this.pojoEmpleadoNominaBorrar.getId() != null && this.pojoEmpleadoNominaBorrar.getId() > 0L) {
					index = this.listEmpleadoNomina.indexOf(this.pojoEmpleadoNominaBorrar);
					this.pojoEmpleadoNominaBorrar = this.ifzEmpleadoNomina.cancelar(this.pojoEmpleadoNominaBorrar);
				}
				
				// Reemplazamos en la lista si corresponde
				if (index >= 0)
					this.listEmpleadoNomina.set(index, this.pojoEmpleadoNominaBorrar);

				this.pojoEmpleadoNominaBorrar = null;
			}*/
    	} catch (Exception e) {
			log.error("Error en constructor EmpleadoNominaAction.cancelar", e);
			control(true);
			throw e;
    	}
    }
	
	public void salvar() throws Exception {
		try {
			control();
			if (! validarCalculoNomina()) 
				return;
			
			// Generamos las registros de nomina
			this.ifzEmpleadoNomina.setInfoSesion(this.loginManager.getInfoSesion());
			Respuesta res = this.ifzEmpleadoNomina.generarNomina(this.fechaDesde, this.fechaHasta, this.recalcularNomina);
			if (res.getErrores().getCodigoError() > 0L) {
				if (res.getErrores().getErrores() != null && ! res.getErrores().getErrores().isEmpty() && res.getErrores().getErrores().get(0).getCodigoError() == 5L) {
					log.info("ERROR 5 - No se encontraron asistencias en el periodo especificado");
					control(5);
					return;
				} 
				
				log.info("ERROR INTERNO: " + res.getErrores().getCodigoError() + " - " + res.getErrores().getDescError());
				control(res.getErrores().getCodigoError() + " - " + res.getErrores().getDescError());
				return;
			}

			@SuppressWarnings({ "unchecked" })
			List<EmpleadoNomina> lista = (List<EmpleadoNomina>) res.getBody().getValor("nomina");
			if (lista == null || lista.isEmpty()) {
				log.info("ERROR 6 - Nomina generada. Recalcular?");
				control(6);
				return;
			}
			
			this.fechaDesde = null;
			this.fechaHasta = null;
			this.recalcularNomina = false;
		} catch (Exception e) {
			log.error("Error en constructor EmpleadoNominaAction.salvar", e);
			control(true);
		}
	}

	public boolean validarCalculoNomina() {
		try {
			if (this.fechaHasta == null || this.fechaDesde == null) {
				log.info("ERROR 2 - Debe indicar el rango de fecha. Fecha inicio y fecha fin");
				control(true);
				return false;
			}

			if (! this.fechaHasta.after(this.fechaDesde)) {
				SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
				if (! formatter.format(this.fechaDesde).equals(formatter.format(this.fechaHasta))) {
					log.info("ERROR 4 - Fecha de inicio es mayor a la fecha final");
					control(4);
					return false;
				}
			}
			
			if (! this.recalcularNomina) {
				List<EmpleadoNomina> regExistentes = this.ifzEmpleadoNomina.findByDates(this.fechaDesde, this.fechaHasta);
				if (regExistentes != null && ! regExistentes.isEmpty()) {
					log.info("ERROR 6 - Nomina generada. Recalcular?");
					control(6);
					return false;
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.EmpleadosNominaAction.validarCalculoNomina", e);
			control(true);
			return false;
		}
		
		return true;
	}
	
	public void confirmaRecalculo() {
		try {
			this.recalcularNomina = true;
			this.salvar();
			this.recalcularNomina = false;
		} catch (Exception e) {
			log.error("Error en RecHum.EmpleadosNominaAction.confirmaRecalculo", e);
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------------
	
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
	
	public Date getFechaDesde() {
		return fechaDesde;
	}
	
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	public boolean isRecalcularNomina() {
		return recalcularNomina;
	}

	public void setRecalcularNomina(boolean recalcularNomina) {
		this.recalcularNomina = recalcularNomina;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaAction
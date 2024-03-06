package net.giro.contabilidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.OperacionesIntegradasSql;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.logica.OperacionesIntegradasSqlRem;
import net.giro.contabilidad.logica.TransaccionesRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="opinSqlAction")
public class OperacionesIntegradasSqlAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OperacionesIntegradasSqlAction.class);
	
	private InitialContext ctx;
	
	// Interfaces
	private OperacionesIntegradasSqlRem ifzOperIntSql;
	private TransaccionesRem ifzTransacciones;
	
	// Busqueda principal
	private List<OperacionesIntegradasSql> listOperIntSqls;
	private OperacionesIntegradasSql pojoOperIntSql;
	private OperacionesIntegradasSql pojoOperIntSqlBorrar;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	
	// Busqueda Transacciones
	private List<Transacciones> listBusquedaTransacciones;
	private List<SelectItem> tiposBusquedaTransacciones;
	private String campoBusquedaTransacciones;
	private String valorBusquedaTransacciones;
	private int numPaginaTransacciones;
	
	// Variables de operacion
	LoginManager loginManager;
	PropertyResourceBundle entornoProperties;
	int usuarioId = 0;
	String usuario = "";

	private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	
	
	public OperacionesIntegradasSqlAction() {
		try {
			FacesContext fc 		= FacesContext.getCurrentInstance();
			Application app 		= fc.getApplication();
			ValueExpression dato 	= app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			ValueExpression ve 		= app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);

			this.entornoProperties 	= (PropertyResourceBundle) dato.getValue(fc.getELContext());
			this.loginManager 		= (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId 			= (int) this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario 			= this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

			// Interfaces
			this.ctx = new InitialContext();
			this.ifzOperIntSql = (OperacionesIntegradasSqlRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesIntegradasSqlFac!net.giro.contabilidad.logica.OperacionesIntegradasSqlRem");
			this.ifzOperIntSql.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOperIntSql.showSystemOuts(false);
			this.ifzTransacciones = (TransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesFac!net.giro.contabilidad.logica.TransaccionesRem");
			this.ifzTransacciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTransacciones.showSystemOuts(false);
			
			// Listas
			this.listOperIntSqls = new ArrayList<OperacionesIntegradasSql>();
			
			// POJO's
			this.pojoOperIntSql = new OperacionesIntegradasSql();
			this.pojoOperIntSqlBorrar = null;
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("id", "Clave"));
			this.tiposBusqueda.add(new SelectItem("nombre", "Nombre"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda Transaccion
			this.listBusquedaTransacciones  = new ArrayList<Transacciones>();
			this.tiposBusquedaTransacciones = new ArrayList<SelectItem>();
			this.tiposBusquedaTransacciones.add(new SelectItem("id", "Clave"));
			this.tiposBusquedaTransacciones.add(new SelectItem("descripcion", "Descripcion"));
			this.campoBusquedaTransacciones = this.tiposBusquedaTransacciones.get(0).getValue().toString();
			this.valorBusquedaTransacciones = "";
			this.numPaginaTransacciones = 1;
		} catch (Exception e) {
			log.error("Error en constructor OperacionesIntegradasSqlAction", e);
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
			this.ifzOperIntSql.orderBy("id DESC");
			this.listOperIntSqls = this.ifzOperIntSql.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 0);
			
			// Comprobamos resultados
			if (this.listOperIntSqls.isEmpty()) {
				this.operacion = true;
				this.tipoMensaje = 2;
				this.mensaje = "ERROR";
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor OperacionesIntegradasSqlAction.buscar", e);
			throw e;
		}
	}
	
	public void nuevo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoOperIntSql = new OperacionesIntegradasSql();
			this.pojoOperIntSqlBorrar = null;
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			log.error("Error en constructor OperacionesIntegradasSqlAction.nuevo", e);
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
			log.error("Error en constructor OperacionesIntegradasSqlAction.editar", e);
			throw e;
		}
	}
	
	public void borrar() throws Exception {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoOperIntSqlBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoOperIntSqlBorrar.getId() != null && this.pojoOperIntSqlBorrar.getId() > 0L)
					this.ifzOperIntSql.delete(this.pojoOperIntSqlBorrar.getId());
				
				// Borramos del listado
				this.listOperIntSqls.remove(pojoOperIntSqlBorrar);

				this.pojoOperIntSqlBorrar = null;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor OperacionesIntegradasSqlAction.borrar", e);
			throw e;
		}
	}

	public void cancelar() throws Exception {
    	try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			/*int index = -1;
			if (this.pojoOperacionesIntegradasSqlBorrar != null) {
				// Cancelamos en la BD si corresponde
				if (this.pojoOperacionesIntegradasSqlBorrar.getId() != null && this.pojoOperacionesIntegradasSqlBorrar.getId() > 0L) {
					index = this.listOperacionesIntegradasSqls.indexOf(this.pojoOperacionesIntegradasSqlBorrar);
					this.pojoOperacionesIntegradasSqlBorrar = this.ifzOperacionesIntegradasSqls.cancelar(this.pojoOperacionesIntegradasSqlBorrar);
				}
				
				// Reemplazamos en la lista si corresponde
				if (index >= 0)
					this.listOperacionesIntegradasSqls.set(index, this.pojoOperacionesIntegradasSqlBorrar);

				this.pojoOperacionesIntegradasSqlBorrar = null;
			}*/
    	} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor OperacionesIntegradasSqlAction.cancelar", e);
			throw e;
    	}
    }
	
	public void salvar() throws Exception {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoOperIntSql != null) {
				this.pojoOperIntSql.setModificadoPor(this.usuarioId);
				this.pojoOperIntSql.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoOperIntSql.getId() == null || this.pojoOperIntSql.getId() <= 0L) {
					this.pojoOperIntSql.setCreadoPor(this.usuarioId);
					this.pojoOperIntSql.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoOperIntSql.setId(this.ifzOperIntSql.save(this.pojoOperIntSql));
					
					// Agregamos a la lista
					this.listOperIntSqls.add(this.pojoOperIntSql);
				} else {
					// Actualizamos en la BD
					this.ifzOperIntSql.update(this.pojoOperIntSql);
					
					// Actualizamos en el listado
					for (OperacionesIntegradasSql var : this.listOperIntSqls) {
						if (this.pojoOperIntSql.getId().equals(var.getId())) {
							var = this.pojoOperIntSql;
							break;
						}
					}
				}
			}			
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor OperacionesIntegradasSqlAction.salvar", e);
			throw e;
		}
	}
	

	public void nuevaBusquedaTransacciones() {
		if (this.listBusquedaTransacciones == null)
			this.listBusquedaTransacciones = new ArrayList<Transacciones>();
		this.listBusquedaTransacciones.clear();
		
		this.campoBusquedaTransacciones = this.tiposBusquedaTransacciones.get(0).getValue().toString();
		this.valorBusquedaTransacciones = "";
		this.numPaginaTransacciones = 1;
	}
	
	public void buscarTransacciones() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaTransacciones))
				this.campoBusquedaTransacciones = tiposBusquedaTransacciones.get(0).getValue().toString();
			
			this.listBusquedaTransacciones = this.ifzTransacciones.findLikeProperty(this.campoBusquedaTransacciones, this.valorBusquedaTransacciones, 120);
			
			if (this.listBusquedaTransacciones == null || this.listBusquedaTransacciones.isEmpty()) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda vacia");
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasSqlAction.buscarTransacciones", e);
		}
	}
	
	public void seleccionarTransaccion() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			nuevaBusquedaTransacciones();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasSqlAction.seleccionarTransaccion", e);
		}
	}
	
	
	public String getOperacionIntegrada() {
		if (this.pojoOperIntSql != null && this.pojoOperIntSql.getIdOperacionIntegradaTransaccion() != null && this.pojoOperIntSql.getIdOperacionIntegradaTransaccion().getId() != null && this.pojoOperIntSql.getIdOperacionIntegradaTransaccion().getId() > 0L)
			return this.pojoOperIntSql.getIdOperacionIntegradaTransaccion().toString();
		return "";
	}
	
	public void setOperacionIntegrada(String value) {}
	
	/* 
	 * ----------------------------------------------------------------------------------------
	 * PROPIEDADES
	 * ----------------------------------------------------------------------------------------
	 */
		
	public List<OperacionesIntegradasSql> getListOperIntSqls() {
		return listOperIntSqls;
	}

	public void setListOperIntSqls(List<OperacionesIntegradasSql> listOperIntSqls) {
		this.listOperIntSqls = listOperIntSqls;
	}

	public OperacionesIntegradasSql getPojoOperIntSql() {
		return pojoOperIntSql;
	}

	public void setPojoOperIntSql(OperacionesIntegradasSql pojoOperIntSql) {
		this.pojoOperIntSql = pojoOperIntSql;
	}

	public OperacionesIntegradasSql getPojoOperIntSqlBorrar() {
		return pojoOperIntSqlBorrar;
	}

	public void setPojoOperacionesIntegradasSqlBorrar(OperacionesIntegradasSql pojoOperIntSqlBorrar) {
		this.pojoOperIntSqlBorrar = pojoOperIntSqlBorrar;
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


	
	public List<Transacciones> getListBusquedaTransacciones() {
		return listBusquedaTransacciones;
	}
	


	public void setListBusquedaTransacciones(List<Transacciones> listBusquedaTransacciones) {
		this.listBusquedaTransacciones = listBusquedaTransacciones;
	}

	public List<SelectItem> getTiposBusquedaTransacciones() {
		return tiposBusquedaTransacciones;
	}

	public void setTiposBusquedaTransacciones(List<SelectItem> tiposBusquedaTransacciones) {
		this.tiposBusquedaTransacciones = tiposBusquedaTransacciones;
	}

	public String getCampoBusquedaTransacciones() {
		return campoBusquedaTransacciones;
	}

	public void setCampoBusquedaTransacciones(String campoBusquedaTransacciones) {
		this.campoBusquedaTransacciones = campoBusquedaTransacciones;
	}

	public String getValorBusquedaTransacciones() {
		return valorBusquedaTransacciones;
	}

	public void setValorBusquedaTransacciones(String valorBusquedaTransacciones) {
		this.valorBusquedaTransacciones = valorBusquedaTransacciones;
	}

	public int getNumPaginaTransacciones() {
		return numPaginaTransacciones;
	}

	public void setNumPaginaTransacciones(int numPaginaTransacciones) {
		this.numPaginaTransacciones = numPaginaTransacciones;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 02/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasSqlAction
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

import net.giro.contabilidad.beans.Conceptos;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.logica.ConceptosRem;
import net.giro.contabilidad.logica.OperacionesIntegradasConceptosSqlRem;
import net.giro.contabilidad.logica.TransaccionesRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="tranconSqlAction")
public class TransaccionConceptosSqlAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TransaccionConceptosSqlAction.class);
	
	private InitialContext ctx;
	
	// Interfaces
	private OperacionesIntegradasConceptosSqlRem ifzTransConceptosSql;
	private TransaccionesRem ifzTransacciones;
	private ConceptosRem ifzConceptos;
	
	// Busqueda principal
	private List<OperacionesIntegradasConceptosSql> listTransConceptosSql;
	private OperacionesIntegradasConceptosSql pojoTransConceptosSql;
	private OperacionesIntegradasConceptosSql pojoTransConceptosSqlBorrar;
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
	
	// Busqueda Conceptos
	private List<Conceptos> listBusquedaConceptos;
	private List<SelectItem> tiposBusquedaConceptos;
	private String campoBusquedaConceptos;
	private String valorBusquedaConceptos;
	private int numPaginaConceptos;
	
	// Variables de operacion
	LoginManager loginManager;
	PropertyResourceBundle entornoProperties;
	int usuarioId = 0;
	String usuario = "";

	private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	
	
	public TransaccionConceptosSqlAction() {
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
			this.ifzTransConceptosSql = (OperacionesIntegradasConceptosSqlRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionConceptosSqlFac!net.giro.contabilidad.logica.TransaccionConceptosSqlRem");
			this.ifzTransConceptosSql.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTransConceptosSql.showSystemOuts(false);
			this.ifzTransacciones = (TransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesFac!net.giro.contabilidad.logica.TransaccionesRem");
			this.ifzTransacciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTransacciones.showSystemOuts(false);
			this.ifzConceptos = (ConceptosRem) this.ctx.lookup("ejb:/Logica_Contabilidad//ConceptosFac!net.giro.contabilidad.logica.ConceptosRem");
			this.ifzConceptos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConceptos.showSystemOuts(false);
			
			// Listas
			this.listTransConceptosSql = new ArrayList<OperacionesIntegradasConceptosSql>();
			
			// POJO's
			this.pojoTransConceptosSql = new OperacionesIntegradasConceptosSql();
			this.pojoTransConceptosSqlBorrar = null;
			
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
			
			// Busqueda Conceptos
			this.listBusquedaConceptos  = new ArrayList<Conceptos>();
			this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
			this.tiposBusquedaConceptos.add(new SelectItem("id", "Clave"));
			this.tiposBusquedaConceptos.add(new SelectItem("descripcion", "Descripcion"));
			this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
			this.valorBusquedaConceptos = "";
			this.numPaginaConceptos = 1;
		} catch (Exception e) {
			log.error("Error en constructor TransaccionConceptosSqlAction", e);
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
			this.ifzTransConceptosSql.orderBy("estatus, id DESC");
			this.listTransConceptosSql = this.ifzTransConceptosSql.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 0);
			
			// Comprobamos resultados
			if (this.listTransConceptosSql.isEmpty()) {
				this.operacion = true;
				this.tipoMensaje = 2;
				this.mensaje = "ERROR";
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor TransaccionConceptosSqlAction.buscar", e);
			throw e;
		}
	}
	
	public void nuevo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoTransConceptosSql = new OperacionesIntegradasConceptosSql();
			this.pojoTransConceptosSqlBorrar = null;
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			log.error("Error en constructor TransaccionConceptosSqlAction.nuevo", e);
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
			log.error("Error en constructor TransaccionConceptosSqlAction.editar", e);
			throw e;
		}
	}
	
	public void borrar() throws Exception {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoTransConceptosSqlBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoTransConceptosSqlBorrar.getId() != null && this.pojoTransConceptosSqlBorrar.getId() > 0L)
					this.ifzTransConceptosSql.delete(this.pojoTransConceptosSqlBorrar.getId());
				
				// Borramos del listado
				this.listTransConceptosSql.remove(pojoTransConceptosSqlBorrar);

				this.pojoTransConceptosSqlBorrar = null;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor TransaccionConceptosSqlAction.borrar", e);
			throw e;
		}
	}

	public void cancelar() throws Exception {
    	try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			/*int index = -1;
			if (this.pojoTransConceptosSqlBorrar != null) {
				// Cancelamos en la BD si corresponde
				if (this.pojoTransConceptosSqlBorrar.getId() != null && this.pojoTransConceptosSqlBorrar.getId() > 0L) {
					index = this.listTransConceptosSql.indexOf(this.pojoTransConceptosSqlBorrar);
					this.pojoTransConceptosSqlBorrar = this.ifzTransConceptosSql.cancelar(this.pojoTransConceptosSqlBorrar);
				}
				
				// Reemplazamos en la lista si corresponde
				if (index >= 0)
					this.listTransConceptosSql.set(index, this.pojoTransConceptosSqlBorrar);

				this.pojoTransConceptosSqlBorrar = null;
			}*/
    	} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor TransaccionConceptosSqlAction.cancelar", e);
			throw e;
    	}
    }
	
	public void salvar() throws Exception {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoTransConceptosSql != null) {
				this.pojoTransConceptosSql.setModificadoPor(this.usuarioId);
				this.pojoTransConceptosSql.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoTransConceptosSql.getId() == null || this.pojoTransConceptosSql.getId() <= 0L) {
					this.pojoTransConceptosSql.setCreadoPor(this.usuarioId);
					this.pojoTransConceptosSql.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoTransConceptosSql.setId(this.ifzTransConceptosSql.save(this.pojoTransConceptosSql));
					
					// Agregamos a la lista
					this.listTransConceptosSql.add(this.pojoTransConceptosSql);
				} else {
					// Actualizamos en la BD
					this.ifzTransConceptosSql.update(this.pojoTransConceptosSql);
					
					// Actualizamos en el listado
					for (OperacionesIntegradasConceptosSql var : this.listTransConceptosSql) {
						if (this.pojoTransConceptosSql.getId().equals(var.getId())) {
							var = this.pojoTransConceptosSql;
							break;
						}
					}
				}
			}			
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en constructor TransaccionConceptosSqlAction.salvar", e);
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


	public void nuevaBusquedaConceptos() {
		if (this.listBusquedaConceptos == null)
			this.listBusquedaConceptos = new ArrayList<Conceptos>();
		this.listBusquedaConceptos.clear();
		
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
		this.valorBusquedaConceptos = "";
		this.numPaginaConceptos = 1;
	}
	
	public void buscarConceptos() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaConceptos))
				this.campoBusquedaConceptos = tiposBusquedaConceptos.get(0).getValue().toString();
			
			this.listBusquedaConceptos = this.ifzConceptos.findLikeProperty(this.campoBusquedaConceptos, this.valorBusquedaConceptos, 120);
			if (this.listBusquedaConceptos == null || this.listBusquedaConceptos.isEmpty()) {
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
			log.error("Error en OperacionesIntegradasSqlAction.buscarConceptos", e);
		}
	}
	
	public void seleccionarConcepto() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			nuevaBusquedaConceptos();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesIntegradasSqlAction.seleccionarConcepto", e);
		}
	}
	
	
	/* 
	 * ----------------------------------------------------------------------------------------
	 * PROPIEDADES
	 * ----------------------------------------------------------------------------------------
	 */
		
	public List<OperacionesIntegradasConceptosSql> getListTransaccionConceptosSqls() {
		return listTransConceptosSql;
	}

	public void setListTransaccionConceptosSqls(List<OperacionesIntegradasConceptosSql> listTransConceptosSql) {
		this.listTransConceptosSql = listTransConceptosSql;
	}

	public OperacionesIntegradasConceptosSql getPojoTransaccionConceptosSql() {
		return pojoTransConceptosSql;
	}

	public void setPojoTransaccionConceptosSql(OperacionesIntegradasConceptosSql pojoTransConceptosSql) {
		this.pojoTransConceptosSql = pojoTransConceptosSql;
	}

	public OperacionesIntegradasConceptosSql getPojoTransaccionConceptosSqlBorrar() {
		return pojoTransConceptosSqlBorrar;
	}

	public void setPojoTransaccionConceptosSqlBorrar(OperacionesIntegradasConceptosSql pojoTransConceptosSqlBorrar) {
		this.pojoTransConceptosSqlBorrar = pojoTransConceptosSqlBorrar;
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


	public List<Conceptos> getListBusquedaConceptos() {
		return listBusquedaConceptos;
	}


	public void setListBusquedaConceptos(List<Conceptos> listBusquedaConceptos) {
		this.listBusquedaConceptos = listBusquedaConceptos;
	}


	public List<SelectItem> getTiposBusquedaConceptos() {
		return tiposBusquedaConceptos;
	}


	public void setTiposBusquedaConceptos(List<SelectItem> tiposBusquedaConceptos) {
		this.tiposBusquedaConceptos = tiposBusquedaConceptos;
	}


	public String getCampoBusquedaConceptos() {
		return campoBusquedaConceptos;
	}


	public void setCampoBusquedaConceptos(String campoBusquedaConceptos) {
		this.campoBusquedaConceptos = campoBusquedaConceptos;
	}


	public String getValorBusquedaConceptos() {
		return valorBusquedaConceptos;
	}


	public void setValorBusquedaConceptos(String valorBusquedaConceptos) {
		this.valorBusquedaConceptos = valorBusquedaConceptos;
	}


	public int getNumPaginaConceptos() {
		return numPaginaConceptos;
	}


	public void setNumPaginaConceptos(int numPaginaConceptos) {
		this.numPaginaConceptos = numPaginaConceptos;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| DD/MM/YYYY | Javier Tirado	| Creacion de TransaccionConceptosSqlAction
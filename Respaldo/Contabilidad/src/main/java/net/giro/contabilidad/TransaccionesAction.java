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
import net.giro.contabilidad.beans.OperacionesExt;
import net.giro.contabilidad.beans.TransaccionConceptos;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.beans.TransaccionesExt;
import net.giro.contabilidad.logica.ConceptosRem;
import net.giro.contabilidad.logica.OperacionesRem;
import net.giro.contabilidad.logica.TransaccionConceptosRem;
import net.giro.contabilidad.logica.TransaccionesRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="transAction")
public class TransaccionesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TransaccionesAction.class);
	private InitialContext ctx;
	// Interfaces
	private TransaccionesRem ifzTransacciones;
	private TransaccionConceptosRem ifzTransConceptos;
	// Listas
	private List<TransaccionesExt> listTransacciones;
	private List<TransaccionConceptos> listTransConceptos;
	private List<TransaccionConceptos> listTransConceptosBorrados;
	// POJO's
	private TransaccionesExt pojoTransaccion;
	private TransaccionesExt pojoTransaccionBorrar;
	private TransaccionConceptos pojoTransConcepto;
	private TransaccionConceptos pojoTransConceptoBorrar;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	private int numPaginaTransConceptos;
	// Busqueda Operaciones
	private OperacionesRem ifzOperaciones;
	private List<OperacionesExt> listOperaciones;
	private List<SelectItem> tiposBusquedaOperaciones;	
	private String campoBusquedaOperaciones;
	private String valorBusquedaOperaciones;
	private int numPaginaOperaciones;
	// Busqueda Conceptos
	private ConceptosRem ifzConceptos;
	private List<Conceptos> listConceptos;
	private List<SelectItem> tiposBusquedaConceptos;	
	private String campoBusquedaConceptos;
	private String valorBusquedaConceptos;
	private int numPaginaConceptos;
	// Variables de operacion
    private long usuarioId;
    //private String usuario;
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	private boolean codigoOcupado;
	
	
	public TransaccionesAction() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();

			this.usuarioId = 0;
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId();
			
			// EJB
			this.ctx = new InitialContext();
			this.ifzTransacciones 	= (TransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesFac!net.giro.contabilidad.logica.TransaccionesRem");
			this.ifzOperaciones 	= (OperacionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesFac!net.giro.contabilidad.logica.OperacionesRem");
			this.ifzTransConceptos 	= (TransaccionConceptosRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionConceptosFac!net.giro.contabilidad.logica.TransaccionConceptosRem");
			this.ifzConceptos 		= (ConceptosRem) this.ctx.lookup("ejb:/Logica_Contabilidad//ConceptosFac!net.giro.contabilidad.logica.ConceptosRem");
			
			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------
			
			this.listTransacciones 	= new ArrayList<TransaccionesExt>();
			this.listOperaciones 	= new ArrayList<OperacionesExt>();
			this.listTransConceptos = new ArrayList<TransaccionConceptos>();
			this.listConceptos 		= new ArrayList<Conceptos>();
			this.pojoTransaccion = new TransaccionesExt();
			this.pojoTransaccionBorrar = new TransaccionesExt();
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			this.numPaginaTransConceptos = 1;
			
			// Busqueda operaciones
			this.tiposBusquedaOperaciones = new ArrayList<SelectItem>();
			this.tiposBusquedaOperaciones.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaOperaciones.add(new SelectItem("id", "ID"));
			this.campoBusquedaOperaciones = this.tiposBusquedaOperaciones.get(0).getValue().toString();
			this.valorBusquedaOperaciones = "";
			this.numPaginaOperaciones = 1;
			
			// Busqueda Conceptos
			this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
			this.tiposBusquedaConceptos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaConceptos.add(new SelectItem("id", "ID"));
			this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
			this.valorBusquedaConceptos = "";
			this.numPaginaConceptos = 1;
		} catch (Exception e) {
			log.error("Error en constructor TransaccionesAction", e);
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
			
			this.ifzTransacciones.orderBy("codigo");
			this.listTransacciones = this.ifzTransacciones.findExtLikeProperty(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listTransacciones.isEmpty()) {
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
			log.error("Error en TransaccionesAction.buscar", e);
		}
	}
	
	public void nuevo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoTransaccion = new TransaccionesExt();
			this.pojoTransaccionBorrar = null;
			
			if (this.listTransConceptos == null)
				this.listTransConceptos = new ArrayList<TransaccionConceptos>();
			this.listTransConceptos.clear();

			if (this.listTransConceptosBorrados == null)
				this.listTransConceptosBorrados = new ArrayList<TransaccionConceptos>();
			this.listTransConceptosBorrados.clear();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesAction.buscar", e);
		}
	}
	
	public void editar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.listTransConceptos == null)
				this.listTransConceptos = new ArrayList<TransaccionConceptos>();
			this.listTransConceptos.clear();

			if (this.listTransConceptosBorrados == null)
				this.listTransConceptosBorrados = new ArrayList<TransaccionConceptos>();
			this.listTransConceptosBorrados.clear();
			
			// cargamos los conceptos asignados a la transaccion
			this.listTransConceptos = this.ifzTransConceptos.findByProperty("idTransaccion.id", this.pojoTransaccion.getId(), 120);
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesAction.editar", e);
		}
	}
	
	public void guardar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoTransaccion != null) {
				comprobarCodigo();
				if (this.codigoOcupado) {
					this.operacion = true;
					this.mensaje = "ERROR";
					this.tipoMensaje = 9;
					log.error("El codigo [" + this.pojoTransaccion.getCodigo().toString() + "] ya existe");
					return;
				}
				
				this.pojoTransaccion.setModificadoPor(this.usuarioId);
				this.pojoTransaccion.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoTransaccion.getId() == null || this.pojoTransaccion.getId() <= 0L) {
					this.pojoTransaccion.setCreadoPor(this.usuarioId);
					this.pojoTransaccion.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoTransaccion.setId(this.ifzTransacciones.save(this.pojoTransaccion));
					// Agregamos a la lista
					this.listTransacciones.add(this.pojoTransaccion);
				} else {
					// Actualizamos en la BD
					this.ifzTransacciones.update(this.pojoTransaccion);
					
					// Actualizamo en la lista
					for(TransaccionesExt var : this.listTransacciones) {
						if (var.getId().equals(this.pojoTransaccion.getId())) {
							var.setDescripcion(this.pojoTransaccion.getDescripcion());
							var.setIdOperacion(this.pojoTransaccion.getIdOperacion());
							var.setModificadoPor(this.pojoTransaccion.getModificadoPor());
							var.setFechaModificacion(this.pojoTransaccion.getFechaModificacion());
							break;
						}
					}
				}
				
				// Quitamos los conceptos borrados si corresponde
				if (! this.listTransConceptosBorrados.isEmpty()) {
					for(TransaccionConceptos var : this.listTransConceptosBorrados) {
						if (var.getId() != null && var.getId() > 0L)
							this.ifzTransConceptos.delete(var.getId());
					}
					
					this.listTransConceptosBorrados.clear();
				}
				
				if (! this.listTransConceptos.isEmpty()) {
					// Obtenemos el pojo de la transaccion y la asignamos al pojo Transaccion-Concepto
					Transacciones pojoAux = this.ifzTransacciones.findById(this.pojoTransaccion.getId());
					for(TransaccionConceptos var : this.listTransConceptos) {
						// Asignamos la transaccion
						var.setIdTransaccion(pojoAux);
						
						// Guardamos en la BD
						if (var.getId() == null || var.getId() <= 0L)
							var.setId(this.ifzTransConceptos.save(var));
						else
							this.ifzTransConceptos.update(var);
					}
					
					this.listTransConceptos.clear();
				}
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesAction.guardar", e);
		}
	}
	
	public void borrar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoTransaccionBorrar != null && this.pojoTransaccionBorrar.getId() > 0L) {
				// Borramos de la bd
				this.ifzTransacciones.delete(this.pojoTransaccionBorrar.getId());
				
				// Borramos de la lista
				this.listTransacciones.remove(this.pojoTransaccionBorrar);
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesAction.borrar", e);
		}
	}
	
	public void comprobarCodigo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			this.codigoOcupado = false;
			if (this.pojoTransaccion != null && this.pojoTransaccion.getCodigo() > 0L) {
				if(! this.ifzTransacciones.comprobarCodigo(this.pojoTransaccion.getId(), this.pojoTransaccion.getCodigo())) {
					this.operacion = true;
					this.codigoOcupado = true;
					this.mensaje = "ERROR";
					this.tipoMensaje = 9;
					log.error("El codigo [" + this.pojoTransaccion.getCodigo().toString() + "] ya existe");
					return;
				}
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en LlavesAction.comprobarPosicion", e);
		}
	}
	
	public void nuevoTransaccionConcepto() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			this.pojoTransConcepto = new TransaccionConceptos();
			this.pojoTransConceptoBorrar = null;
			
			nuevaBusquedaConceptos();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesAction.nuevoTransaccionConcepto", e);
		}
	}
	
	public void borrarTransaccionConcepto() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoTransConceptoBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoTransConceptoBorrar.getId() != null && this.pojoTransConceptoBorrar.getId() > 0L) {
					this.ifzTransConceptos.delete(this.pojoTransConceptoBorrar.getId()); 
					//this.listTransConceptosBorrados.add(this.pojoTransConceptoBorrar);
				}
				
				// Borramos de la lista
				this.listTransConceptos.remove(this.pojoTransConceptoBorrar);
				this.pojoTransConceptoBorrar = null;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesAction.borrarTransaccionConcepto", e);
		}
	}

	// --------------------------------------------------
	// BUSQUEDA OPERACIONES
	// --------------------------------------------------
	
	public void nuevaBusquedaOperaciones() {
		if (this.listOperaciones == null)
			this.listOperaciones = new ArrayList<OperacionesExt>();
		this.listOperaciones.clear();
		
		this.campoBusquedaOperaciones = tiposBusquedaOperaciones.get(0).getValue().toString();
		this.valorBusquedaOperaciones = "";
		numPaginaOperaciones = 1;
	}
	
	public void buscarOperaciones() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaOperaciones))
				this.campoBusquedaOperaciones = tiposBusquedaOperaciones.get(0).getValue().toString();
			
			this.listOperaciones = this.ifzOperaciones.findExtLikeProperty(this.campoBusquedaOperaciones, this.valorBusquedaOperaciones, 120);
			
			if (this.listOperaciones == null || this.listOperaciones.isEmpty()) {
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
			log.error("Error en TransaccionesAction.buscarAplicaciones", e);
		}
	}
	
	public void seleccionarOperacion() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoTransConcepto != null) {
				
			}

			nuevaBusquedaOperaciones();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesAction.seleccionarOperacion", e);
		}
	}

	// --------------------------------------------------
	// BUSQUEDA CONCEPTOS
	// --------------------------------------------------
	
	public void nuevaBusquedaConceptos() {
		if (this.listConceptos == null)
			this.listConceptos = new ArrayList<Conceptos>();
		this.listConceptos.clear();
		
		this.campoBusquedaConceptos = tiposBusquedaConceptos.get(0).getValue().toString();
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
			
			this.listConceptos = this.ifzConceptos.findLikeProperty(this.campoBusquedaConceptos, this.valorBusquedaConceptos, 120);
			
			if (this.listConceptos == null || this.listConceptos.isEmpty()) {
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
			log.error("Error en AsignacionGruposAction.buscarConceptos", e);
		}
	}
	
	public void seleccionarConcepto() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			this.pojoTransConcepto.setId(0L);
			this.pojoTransConcepto.setCreadoPor(this.usuarioId);
			this.pojoTransConcepto.setModificadoPor(this.usuarioId);
			this.pojoTransConcepto.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoTransConcepto.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoTransaccion.getId() != null && this.pojoTransaccion.getId() > 0L) {
				// Obtenemos el pojo de la transaccion y la asignamos al pojo Transaccion-Concepto
				Transacciones pojoAux = this.ifzTransacciones.findById(this.pojoTransaccion.getId());
				if (pojoAux == null || pojoAux.getId() == null || pojoAux.getId() <= 0L) {
					this.operacion = true;
					this.mensaje = "No existe Transaccion seleccionada";
					this.tipoMensaje = -1;
					return;
				}
				
				// Asignamos la transaccion
				this.pojoTransConcepto.setIdTransaccion(pojoAux);
				
				// Lo guardamos en la BD
				this.pojoTransConcepto.setId(this.ifzTransConceptos.save(this.pojoTransConcepto));
			} 
			
			// Lo agregamos al listado
			this.listTransConceptos.add(this.pojoTransConcepto);
			this.pojoTransConcepto = null;
			
			nuevaBusquedaConceptos();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en AsignacionGruposAction.seleccionarConcepto", e);
		}
	}

	// --------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------
	
	public String getTransaccionDescripcion() {
		if (this.pojoTransaccion != null && this.pojoTransaccion.getId() != null && pojoTransaccion.getId() > 0L)
			return pojoTransaccion.getId() + " - " + this.pojoTransaccion.getOperacionNombre();
		return "";
	}
	
	public void setTransaccionDescripcion(String value) {}
	
	public List<OperacionesExt> getListOperaciones() {
		return listOperaciones;
	}

	public void setListOperaciones(List<OperacionesExt> listOperaciones) {
		this.listOperaciones = listOperaciones;
	}

	public List<TransaccionesExt> getListTransacciones() {
		return listTransacciones;
	}

	public void setListTransacciones(List<TransaccionesExt> listAplicaciones) {
		this.listTransacciones = listAplicaciones;
	}

	public TransaccionesExt getPojoTransaccion() {
		return pojoTransaccion;
	}

	public void setPojoTransaccion(TransaccionesExt pojoTransaccion) {
		this.pojoTransaccion = pojoTransaccion;
	}

	public TransaccionesExt getPojoTransaccionBorrar() {
		return pojoTransaccionBorrar;
	}

	public void setPojoTransaccionBorrar(TransaccionesExt pojoTransaccionBorrar) {
		this.pojoTransaccionBorrar = pojoTransaccionBorrar;
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

	public List<SelectItem> getTiposBusquedaOperaciones() {
		return tiposBusquedaOperaciones;
	}

	public void setTiposBusquedaOperaciones(List<SelectItem> tiposBusquedaOperaciones) {
		this.tiposBusquedaOperaciones = tiposBusquedaOperaciones;
	}

	public String getCampoBusquedaOperaciones() {
		return campoBusquedaOperaciones;
	}

	public void setCampoBusquedaOperaciones(String campoBusquedaOperaciones) {
		this.campoBusquedaOperaciones = campoBusquedaOperaciones;
	}

	public String getValorBusquedaOperaciones() {
		return valorBusquedaOperaciones;
	}

	public void setValorBusquedaOperaciones(String valorBusquedaOperaciones) {
		this.valorBusquedaOperaciones = valorBusquedaOperaciones;
	}

	public int getNumPaginaOperaciones() {
		return numPaginaOperaciones;
	}

	public void setNumPaginaOperaciones(int numPaginaOperaciones) {
		this.numPaginaOperaciones = numPaginaOperaciones;
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



	public List<TransaccionConceptos> getListTransConceptos() {
		return listTransConceptos;
	}

	public void setListTransConceptos(List<TransaccionConceptos> listTransConceptos) {
		this.listTransConceptos = listTransConceptos;
	}
	
	public List<Conceptos> getListConceptos() {
		return listConceptos;
	}
	


	public void setListConceptos(List<Conceptos> listConceptos) {
		this.listConceptos = listConceptos;
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


	public TransaccionConceptos getPojoTransConcepto() {
		return pojoTransConcepto;
	}


	public void setPojoTransConcepto(TransaccionConceptos pojoTransConcepto) {
		this.pojoTransConcepto = pojoTransConcepto;
	}


	public TransaccionConceptos getPojoTransConceptoBorrar() {
		return pojoTransConceptoBorrar;
	}


	public void setPojoTransConceptoBorrar(TransaccionConceptos pojoTransConceptoBorrar) {
		this.pojoTransConceptoBorrar = pojoTransConceptoBorrar;
	}


	public int getNumPaginaTransConceptos() {
		return numPaginaTransConceptos;
	}


	public void setNumPaginaTransConceptos(int numPaginaTransConceptos) {
		this.numPaginaTransConceptos = numPaginaTransConceptos;
	}


	public boolean isCodigoOcupado() {
		return codigoOcupado;
	}


	public void setCodigoOcupado(boolean codigoOcupado) {
		this.codigoOcupado = codigoOcupado;
	}
}

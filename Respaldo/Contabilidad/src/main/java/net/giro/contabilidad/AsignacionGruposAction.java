package net.giro.contabilidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.AsignacionGruposExt;
import net.giro.contabilidad.beans.Conceptos;
import net.giro.contabilidad.beans.Grupos;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.logica.AsignacionGruposRem;
import net.giro.contabilidad.logica.ConceptosRem;
import net.giro.contabilidad.logica.GruposRem;
import net.giro.contabilidad.logica.TransaccionesRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.logica.FormasPagosRem;

@ViewScoped
@ManagedBean(name="asigGruposAction")
public class AsignacionGruposAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AsignacionGruposAction.class);
	private InitialContext ctx;	
	// Interfaces
	private AsignacionGruposRem ifzAsigGrupos;
	private TransaccionesRem ifzTransacciones;
	private GruposRem ifzGrupos;
	private ConceptosRem ifzConceptos;
	private FormasPagosRem ifzFormasPago;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	// Listas
	private List<AsignacionGruposExt> listAsigGrupos;
	private List<Transacciones> listTransacciones;
	private List<Grupos> listGrupos;
	private List<Conceptos> listConceptos;
	private List<FormasPagos> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	private List<ConValores> listTiposPolizas;
	private List<SelectItem> listTiposPolizasItems;
	// POJO's
	private AsignacionGruposExt pojoAsigGrupo;
	private AsignacionGruposExt pojoAsigGrupoBorrar;
	private Transacciones pojoTransaccion;
	private Grupos pojoGrupo;
	private ConGrupoValores pojoGpoTiposPolizas;	
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;	
	// Busqueda Transacciones
	private List<SelectItem> tiposBusquedaTransacciones;	
	private String campoBusquedaTransacciones;
	private String valorBusquedaTransacciones;
	private int numPaginaTransacciones;	
	// Busqueda Grupos
	private List<SelectItem> tiposBusquedaGrupos;	
	private String campoBusquedaGrupos;
	private String valorBusquedaGrupos;
	private int numPaginaGrupos;	
	// Busqueda Conceptos
	private List<SelectItem> tiposBusquedaConceptos;	
	private String campoBusquedaConceptos;
	private String valorBusquedaConceptos;
	private int numPaginaConceptos;	
	// Variables de operacion
    private long usuarioId;
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	private String toGrupo;
	private long formaPago;
	private long tipoPoliza;
	
	
	public AsignacionGruposAction() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();

			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId();
			
			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------

			this.ctx = new InitialContext();
			this.ifzAsigGrupos = (AsignacionGruposRem) this.ctx.lookup("ejb:/Logica_Contabilidad//AsignacionGruposFac!net.giro.contabilidad.logica.AsignacionGruposRem");
			this.ifzTransacciones = (TransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesFac!net.giro.contabilidad.logica.TransaccionesRem");
			this.ifzGrupos = (GruposRem) this.ctx.lookup("ejb:/Logica_Contabilidad//GruposFac!net.giro.contabilidad.logica.GruposRem");
			this.ifzConceptos = (ConceptosRem) this.ctx.lookup("ejb:/Logica_Contabilidad//ConceptosFac!net.giro.contabilidad.logica.ConceptosRem");
			this.ifzFormasPago = (FormasPagosRem) this.ctx.lookup("ejb:/Logica_TYG//FormasPagosFac!net.giro.tyg.logica.FormasPagosRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			
			this.ifzAsigGrupos.setInfoSesion(lm.getInfoSesion());
			this.ifzAsigGrupos.showSystemOuts(false);
			this.ifzTransacciones.setInfoSesion(lm.getInfoSesion());
			this.ifzTransacciones.showSystemOuts(false);
			this.ifzGrupos.setInfoSesion(lm.getInfoSesion());
			this.ifzGrupos.showSystemOuts(false);
			this.ifzConceptos.setInfoSesion(lm.getInfoSesion());
			this.ifzConceptos.showSystemOuts(false);
			
			// Listas
			this.listAsigGrupos = new ArrayList<AsignacionGruposExt>();
			this.listTransacciones = new ArrayList<Transacciones>();
			this.listGrupos = new ArrayList<Grupos>();
			this.listConceptos = new ArrayList<Conceptos>();
			this.listFormasPago = new ArrayList<FormasPagos>();
			this.listFormasPagoItems = new ArrayList<SelectItem>();
			
			// POJO's
			this.pojoAsigGrupo = new AsignacionGruposExt();
			this.pojoAsigGrupoBorrar = new AsignacionGruposExt();
			this.pojoTransaccion = new Transacciones();
			this.pojoGrupo = new Grupos();
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda Transaccion
			this.tiposBusquedaTransacciones = new ArrayList<SelectItem>();
			this.tiposBusquedaTransacciones.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaTransacciones.add(new SelectItem("codigo", "Codigo"));
			this.tiposBusquedaTransacciones.add(new SelectItem("id", "ID"));
			this.campoBusquedaTransacciones = this.tiposBusquedaTransacciones.get(0).getValue().toString();
			this.valorBusquedaTransacciones = "";
			this.numPaginaTransacciones = 1;
			
			// Busqueda Grupo
			this.tiposBusquedaGrupos = new ArrayList<SelectItem>();
			this.tiposBusquedaGrupos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaGrupos.add(new SelectItem("id", "ID"));
			this.campoBusquedaGrupos = this.tiposBusquedaGrupos.get(0).getValue().toString();
			this.valorBusquedaGrupos = "";
			this.numPaginaGrupos = 1;
			
			// Busqueda Conceptos
			this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
			this.tiposBusquedaConceptos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaConceptos.add(new SelectItem("id", "ID"));
			this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
			this.valorBusquedaConceptos = "";
			this.numPaginaConceptos = 1;
			
			// CARGAMOS LOS TIPOS DE POLIZAS (SYS_TIPO_POLIZA)
			// ---------------------------------------------------------------------------------------------------------
			this.pojoGpoTiposPolizas = this.ifzGpoVal.findByName("SYS_TIPO_POLIZA");
			if (this.pojoGpoTiposPolizas == null || this.pojoGpoTiposPolizas.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_TIPO_POLIZA en con_grupo_valores");
		} catch (Exception e) {
			log.error("Error en constructor AsignacionGruposAction", e);
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
			
			this.ifzAsigGrupos.orderBy("tr.codigo, model.idFormaPago, model.id desc");
			this.listAsigGrupos = this.ifzAsigGrupos.findExtLikeProperty(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listAsigGrupos.isEmpty()) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda vacia");
				return;
			}
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.buscar", e);
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

			this.pojoAsigGrupo = new AsignacionGruposExt();
			this.pojoAsigGrupoBorrar = null;
			this.formaPago = 0L;
			this.tipoPoliza = 0L;
			
			cargarFormasPagos();
			cargarTiposPolizas();
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.nuevo", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void editar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.formaPago = this.pojoAsigGrupo.getFormaPagoId();
			this.tipoPoliza = Long.parseLong(this.pojoAsigGrupo.getTipoPoliza().getValor());
			this.pojoAsigGrupoBorrar = null;
			
			cargarFormasPagos();
			cargarTiposPolizas();
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.editar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void guardar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.tipoPoliza <= 0L) {
				return;
			}
			
			for(ConValores var : this.listTiposPolizas) {
				if (this.tipoPoliza == Long.parseLong(var.getValor())) {
					this.pojoAsigGrupo.setTipoPoliza(var);
					break;
				}
			}

			if (this.pojoAsigGrupo != null) {
				this.pojoAsigGrupo.setModificadoPor(this.usuarioId);
				this.pojoAsigGrupo.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoAsigGrupo.getId() == null || this.pojoAsigGrupo.getId() <= 0L) {
					this.pojoAsigGrupo.setCreadoPor(this.usuarioId);
					this.pojoAsigGrupo.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoAsigGrupo.setId(this.ifzAsigGrupos.save(this.pojoAsigGrupo));
					// Agregamos a la lista
					this.listAsigGrupos.add(this.pojoAsigGrupo);
				} else {
					// Actualizamos en la BD
					this.ifzAsigGrupos.update(this.pojoAsigGrupo);
				}
			}
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.guardar", e);
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

			if (this.pojoAsigGrupoBorrar != null && this.pojoAsigGrupoBorrar.getId() > 0L) {
				// Borramos de la bd
				this.ifzAsigGrupos.delete(this.pojoAsigGrupoBorrar.getId());
				
				// Borramos de la lista
				this.listAsigGrupos.remove(this.pojoAsigGrupoBorrar);
			}
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.borrar", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	// Busqueda Transacciones
	// ----------------------------------------------------------------------------------
	public void nuevaBusquedaTransacciones() {
		this.campoBusquedaTransacciones = this.tiposBusquedaTransacciones.get(0).getValue().toString();
		this.valorBusquedaTransacciones = "";
		this.numPaginaTransacciones = 1;
		
		this.pojoTransaccion = null;
		if (this.listTransacciones == null)
			this.listTransacciones = new ArrayList<Transacciones>();
		this.listTransacciones.clear();
	}
	
	public void buscarTransacciones() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaTransacciones))
				this.campoBusquedaTransacciones = tiposBusquedaTransacciones.get(0).getValue().toString();
			
			this.listTransacciones = this.ifzTransacciones.findLikeProperty(this.campoBusquedaTransacciones, this.valorBusquedaTransacciones, 120);
			if (this.listTransacciones == null || this.listTransacciones.isEmpty()) {
				log.info("ERROR 2 - Busqueda vacia");
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				return;
			}
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.buscarTransacciones", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void seleccionarTransaccion() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoTransaccion != null && this.pojoTransaccion.getId() != null && this.pojoTransaccion.getId() > 0L)
				this.pojoAsigGrupo.setIdTransaccion(this.pojoTransaccion);
			
			nuevaBusquedaTransacciones();
		} catch (Exception e) {
			if ("EXISTE_LLAVE".equals(e.getMessage())) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 4;
				return;
			}
			
			log.error("Error en AsignacionGruposAction.seleccionarTransaccion", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}

	// Busqueda Grupos
	// ----------------------------------------------------------------------------------
	public void nuevaBusquedaGrupos() {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		this.campoBusquedaGrupos = this.tiposBusquedaGrupos.get(0).getValue().toString();
		this.valorBusquedaGrupos = "";
		this.numPaginaGrupos = 1;
		
		if (this.listGrupos == null)
			this.listGrupos = new ArrayList<Grupos>();
		this.listGrupos.clear();
		
		this.pojoGrupo = null;
		this.toGrupo = params.get("grupo");
	}
	
	public void buscarGrupos() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaGrupos))
				this.campoBusquedaGrupos = tiposBusquedaGrupos.get(0).getValue().toString();
			
			this.listGrupos = this.ifzGrupos.findLikeProperty(this.campoBusquedaGrupos, this.valorBusquedaGrupos, 120);
			if (this.listGrupos == null || this.listGrupos.isEmpty()) {
				log.info("ERROR 2 - Busqueda vacia");
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				return;
			}
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.buscarGrupos", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void seleccionarGrupo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoGrupo != null && this.pojoGrupo.getId() != null && this.pojoGrupo.getId() > 0L) {
				if ("debito".equals(this.toGrupo)) 
					this.pojoAsigGrupo.setIdGrupoDebito(this.pojoGrupo);
				
				if ("credito".equals(this.toGrupo))
					this.pojoAsigGrupo.setIdGrupoCredito(this.pojoGrupo);
			}
			
			this.toGrupo = "";
			nuevaBusquedaGrupos();
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.seleccionarGrupo", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}

	// Busqueda Conceptos
	// ----------------------------------------------------------------------------------
	public void nuevaBusquedaConceptos() {
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
		this.valorBusquedaConceptos = "";
		this.numPaginaConceptos = 1;
		
		if (this.listConceptos == null)
			this.listConceptos = new ArrayList<Conceptos>();
		this.listConceptos.clear();
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
				log.info("ERROR 2 - Busqueda vacia");
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				return;
			}
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.buscarConceptos", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void seleccionarConcepto() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			nuevaBusquedaConceptos();
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.seleccionarConcepto", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	// OTROS
	// ----------------------------------------------------------------------------------
	public void cargarFormasPagos() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.listFormasPagoItems == null)
				this.listFormasPagoItems = new ArrayList<SelectItem>();
			this.listFormasPagoItems.clear();
			
			this.listFormasPago = this.ifzFormasPago.findLikeColumnName("formaPago", "");
			if (!this.listFormasPago.isEmpty()) {
				for (FormasPagos var : this.listFormasPago) {
					this.listFormasPagoItems.add(new SelectItem(var.getId(), var.getId() + " - " + var.getFormaPago()));
				}
			}
		} catch (Exception e) {
			log.error("Error en AsignacionGruposAction.cargarFormasPagos", e);
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
		}
	}
	
	public void cargarTiposPolizas() {
		try {
			if (this.listTiposPolizas == null)
				this.listTiposPolizas = new ArrayList<ConValores>();
			this.listTiposPolizas.clear();
			
			if (this.listTiposPolizasItems == null)
				this.listTiposPolizasItems = new ArrayList<SelectItem>();
			this.listTiposPolizasItems.clear();
			
			// Cargamos la lista de tipos de poliza
			this.listTiposPolizas = this.ifzConValores.buscaValorGrupo("descripcion", "", this.pojoGpoTiposPolizas);
			if(! this.listTiposPolizas.isEmpty()) {
				for (ConValores var : this.listTiposPolizas) {
					this.listTiposPolizasItems.add(new SelectItem(var.getValor(), var.getId() + " - " + var.getDescripcion()));
				}
			}
		} catch (Exception e) {
			log.error("Error al cargar las familias", e);
			throw e;
		}
	}
	
	// ----------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------
	
	public List<AsignacionGruposExt> getListAsigGrupos() {
		return listAsigGrupos;
	}

	public void setListAsigGrupos(List<AsignacionGruposExt> listAsigGrupos) {
		this.listAsigGrupos = listAsigGrupos;
	}

	public List<Transacciones> getListTransacciones() {
		return listTransacciones;
	}

	public void setListTransacciones(List<Transacciones> listTransacciones) {
		this.listTransacciones = listTransacciones;
	}

	public List<Grupos> getListGrupos() {
		return listGrupos;
	}

	public void setListGrupos(List<Grupos> listGrupos) {
		this.listGrupos = listGrupos;
	}

	public AsignacionGruposExt getPojoAsigGrupo() {
		return pojoAsigGrupo;
	}

	public void setPojoAsigGrupo(AsignacionGruposExt pojoAsigGrupo) {
		this.pojoAsigGrupo = pojoAsigGrupo;
	}

	public AsignacionGruposExt getPojoAsigGrupoBorrar() {
		return pojoAsigGrupoBorrar;
	}

	public void setPojoAsigGrupoBorrar(AsignacionGruposExt pojoAsigGrupoBorrar) {
		this.pojoAsigGrupoBorrar = pojoAsigGrupoBorrar;
	}

	public Transacciones getPojoTransaccion() {
		return pojoTransaccion;
	}

	public void setPojoTransaccion(Transacciones pojoTransaccion) {
		this.pojoTransaccion = pojoTransaccion;
	}

	public Grupos getPojoGrupo() {
		return pojoGrupo;
	}

	public void setPojoGrupo(Grupos pojoGrupo) {
		this.pojoGrupo = pojoGrupo;
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

	public List<SelectItem> getTiposBusquedaGrupos() {
		return tiposBusquedaGrupos;
	}

	public void setTiposBusquedaGrupos(List<SelectItem> tiposBusquedaGrupos) {
		this.tiposBusquedaGrupos = tiposBusquedaGrupos;
	}

	public String getCampoBusquedaGrupos() {
		return campoBusquedaGrupos;
	}

	public void setCampoBusquedaGrupos(String campoBusquedaGrupos) {
		this.campoBusquedaGrupos = campoBusquedaGrupos;
	}

	public String getValorBusquedaGrupos() {
		return valorBusquedaGrupos;
	}

	public void setValorBusquedaGrupos(String valorBusquedaGrupos) {
		this.valorBusquedaGrupos = valorBusquedaGrupos;
	}

	public int getNumPaginaGrupos() {
		return numPaginaGrupos;
	}

	public void setNumPaginaGrupos(int numPaginaGrupos) {
		this.numPaginaGrupos = numPaginaGrupos;
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
	
	public List<SelectItem> getListFormasPagoItems() {
		return listFormasPagoItems;
	}
	
	public void setListFormasPagoItems(List<SelectItem> listFormasPagoItems) {
		this.listFormasPagoItems = listFormasPagoItems;
	}
	
	public long getFormaPago() {
		return formaPago;
	}
	
	public void setFormaPago(long formaPago) {
		this.formaPago = formaPago;
		
		// Recuperamos el pojo FormaPago si corresponde
		if (this.formaPago > 0L) {
			for (FormasPagos var : this.listFormasPago) {
				if (this.formaPago == var.getId()) {
					this.pojoAsigGrupo.setIdFormaPago(var);
					break;
				}
			}
		}
	}

	public long getTipoPoliza() {
		/*if (this.pojoAsigGrupo != null && this.pojoAsigGrupo.getIdTipoPoliza() != null) 
			return Long.parseLong(this.pojoAsigGrupo.getIdTipoPoliza().getValor());
		return 0L;*/
		return tipoPoliza;
	}
	
	public void setTipoPoliza(long tipoPoliza) {
		//this.pojoAsigGrupo.setIdTipoPoliza(tipoPoliza);
		this.tipoPoliza = tipoPoliza;
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

	public List<Conceptos> getListConceptos() {
		return listConceptos;
	}

	public void setListConceptos(List<Conceptos> listConceptos) {
		this.listConceptos = listConceptos;
	}

	public List<SelectItem> getListTiposPolizasItems() {
		return listTiposPolizasItems;
	}

	public void setListTiposPolizasItems(List<SelectItem> listTiposPolizasItems) {
		this.listTiposPolizasItems = listTiposPolizasItems;
	}
}
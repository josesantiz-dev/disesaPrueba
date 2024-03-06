package net.giro.cxp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.cxp.beans.GastosExt;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.logica.GastosImpuestoRem;
import net.giro.cxp.logica.GastosRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;

import org.apache.log4j.Logger;

public class GastosAction {
	private static Logger log = Logger.getLogger(GastosAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	private GastosRem ifzGastos;
	private ConValoresRem ifzConValores;
	private ConGrupoValoresRem ifzGpoValores;
	private GastosImpuestoRem ifzGastoImpuesto;
	private ConGrupoValores pojoGpoGastos;
	private ConGrupoValores pojoGpoImpuestos; 
	private ConGrupoValores pojoGpoTipoEgresos; 
	private GastosExt pojoGasto;
	private ConValores pojoImpuesto;
	//private ConValores pojoTipoEgreso;
	private GastosImpuestoExt pojoGastoImpuesto;
	private List<GastosImpuestoExt>	listImpuestosGrid;
	private List<GastosImpuestoExt>	listImpuestosGridEliminados;
	private List<GastosExt> listGastos;
	// Busqueda principal
	//private String[] tipoBusqueda;
	private List<SelectItem> tiposBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	//private List<String> tipoBusqueda;
	// Tipos de Egresos
	private List<ConValores> listTipoEgresos;
	private List<SelectItem> listTipoEgresosItems;
	// Impuestos
	private List<ConValores> listImpuestos;
	private List<SelectItem> listImpuestosItems;
	//private String nombreImpuesto;
    private String mensaje;		
    private long usuarioId;	    
	private int numPagina;
	private int numPaginaImpuesto;
	private int tipoMensaje;
	private boolean operacionCancelada;
	private Long valGpoTipoEgresos;
	private List<String> tiposEgreso;
	private String valTipoEgreso;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public GastosAction() throws NamingException,Exception {
		ValueExpression valExp = null;
		FacesContext fc = null;
		Application app = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;

			this.ctx = new InitialContext();
			this.ifzGastos 		  = (GastosRem)	 		 this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosFac!net.giro.cxp.logica.GastosRem");
			this.ifzGpoValores 	  = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores 	  = (ConValoresRem) 	 this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGastoImpuesto = (GastosImpuestoRem)	 this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
			
			this.ifzGastos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGastoImpuesto.setInfoSesion(this.loginManager.getInfoSesion());
			
			// GASTOS 
			// ----------------------------------------------------------------------------
			this.pojoGpoGastos = this.ifzGpoValores.findByName("SYS_MOVGTOS");
			if (this.pojoGpoGastos == null || this.pojoGpoGastos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");

			// IMPUESTOS 
			// ----------------------------------------------------------------------------
			this.pojoGpoImpuestos = this.ifzGpoValores.findByName("SYS_IMPTOS");
			if (this.pojoGpoImpuestos == null || this.pojoGpoImpuestos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");

			// TIPOS EGRESO 
			// ----------------------------------------------------------------------------
			this.pojoGpoTipoEgresos = this.ifzGpoValores.findByName("SYS_TIPO_EGRESO");
			if (this.pojoGpoTipoEgresos == null || this.pojoGpoTipoEgresos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_TIPO_EGRESO en con_grupo_valores");
			
			this.pojoGasto = new GastosExt();
			//this.pojoImpuesto = new ConValores();
			//this.pojoTipoEgreso = new ConValores();
			this.listImpuestosGridEliminados = new ArrayList<GastosImpuestoExt>();
			this.listImpuestosGrid = new ArrayList<GastosImpuestoExt>();
			this.listGastos = new ArrayList<GastosExt>();
			
			this.listTipoEgresos = new ArrayList<ConValores>();
			this.listTipoEgresosItems = new ArrayList<SelectItem>();
			
			this.listImpuestos = new ArrayList<ConValores>();
			this.listImpuestosItems = new ArrayList<SelectItem>();
			
			this.numPaginaImpuesto=1;
			this.mensaje = "";
			//this.nombreImpuesto = "";
			this.tipoMensaje = 0;
			this.operacionCancelada = false;

			// Busqueda principal
			// ----------------------------------------------------------------------
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("descripcion", "Gasto"));
			this.tiposBusqueda.add(new SelectItem("valorId", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			// Busqueda principal
			/*this.tipoBusqueda = new ArrayList<String>();
			this.tipoBusqueda.add("Nombre del Gasto");
			this.tipoBusqueda.add("ID");
			this.campoBusqueda = tipoBusqueda.get(0);
			this.valorBusqueda = "";*/
			
			this.tiposEgreso = new ArrayList<String>();
			this.tiposEgreso.add("Item 1");
			this.tiposEgreso.add("Item 2");
			this.tiposEgreso.add("Item 3");
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar instanciar GastionAction", e);
			this.ctx = null;
		}
	}
	

	public void buscar() {
		try {
			control();
			log.info("Buscando GASTOS ... ");
			this.ifzGastos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listGastos = this.ifzGastos.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 0);
			if (this.listGastos == null || this.listGastos.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}

			log.info("Ordenando Gastos ... ");
			Collections.sort(this.listGastos, new Comparator<GastosExt>() {
		    	@Override
		        public int compare(GastosExt o1, GastosExt o2) {
		    		String desc1 = "";
		    		String desc2 = "";
		    		int c = 0;
		    		
		    		if (o1.getIdTipoEgreso() != null && o1.getIdTipoEgreso().getId() > 0L)
		    			desc1 = o1.getIdTipoEgreso().getDescripcion();
		    		if (o2.getIdTipoEgreso() != null && o2.getIdTipoEgreso().getId() > 0L)
		    			desc2 = o2.getIdTipoEgreso().getDescripcion();
		    		c = desc1.compareTo(desc2);

		    		if (c == 0)
		    			c = o1.getDescripcion().compareTo(o2.getDescripcion());
		    		return c;
		        }
			});
			log.info("Busqueda terminada");
		} catch(Exception e) {
			control("Ocurrio un problema al consultar el catalogo de Egresos (Gastos)", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.pojoGasto = new GastosExt();
			this.pojoGasto.setIdGrupo(this.pojoGpoGastos);
			this.tiposEgreso.clear();
			this.listImpuestosGrid.clear();
			this.listImpuestosGridEliminados.clear();
			
			cargarTiposDeEgresos();
			cargarImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al preparar la pantalla para un nuevo Egreso (Gasto)", e);
		}
	}
	
	public void editar() {
		try {
			control();
			if (this.pojoGasto == null || this.pojoGasto.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Egreso (Gasto) seleccionado");
				return;
			}
			
			// Recuperamos la lista de impuestos asigandos al GASTO.
			this.listImpuestosGrid = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId", "", this.pojoGasto.getId());
			
			cargarTiposDeEgresos();
			cargarImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Egreso (Gasto) seleccionado", e);
		}
	}

	public void guardar() {
		try {
			control();
			
			// Eliminamos los impuestos si corresponde
			if (this.listImpuestosGridEliminados != null && ! this.listImpuestosGridEliminados.isEmpty()) {
				for (GastosImpuestoExt var :  this.listImpuestosGridEliminados) {
					if (var.getId() == null || var.getId() <= 0L) 
						continue;
					this.ifzGastoImpuesto.delete(var);
				}
				this.listImpuestosGridEliminados.clear();
			}
			
			if (this.listImpuestosGrid != null && ! this.listImpuestosGrid.isEmpty())
				this.pojoGasto.setTieneImpuestos(true);

			this.ifzGastos.setInfoSesion(this.loginManager.getInfoSesion());
			if (this.pojoGasto.getId() <= 0L) { 
				//this.pojoGasto.setIdGrupo(this.pojoGpoGastos);
				//this.pojoGasto.setIdTipoEgreso(this.pojoTipoEgreso);		
				this.pojoGasto.setCreadoPor(this.usuarioId);
				this.pojoGasto.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoGasto.setModificadoPor(this.usuarioId);
				this.pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD y agregamos a la lista
				this.pojoGasto.setId(this.ifzGastos.save(this.pojoGasto));
				this.listGastos.add(0, this.pojoGasto);
			} else {
				//this.pojoGasto.setIdTipoEgreso(this.pojoTipoEgreso);
				this.pojoGasto.setModificadoPor(this.usuarioId);
				this.pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Actualizamos en la BD y en la lista
				this.ifzGastos.update(this.pojoGasto);
				
				for (GastosExt var : this.listGastos) {
					if(var.getId() == this.pojoGasto.getId()) {
						var = this.pojoGasto;
						break;
					}
				}
			}
			
			// Asignamos el ID del gasto a los Impuestos si corresponde
			if (! this.listImpuestosGrid.isEmpty()) {
				for (GastosImpuestoExt var : this.listImpuestosGrid) {
					if (var.getId() == null || var.getId() <= 0L) {
						var.setGastoId(this.pojoGasto.getId());
						var.setModificadoPor(this.usuarioId);
						var.setFechaModificacion(Calendar.getInstance().getTime());
						
						var.setId(this.ifzGastoImpuesto.save(var));
					}
				}
			}
		} catch(Exception e) {
			control("Ocurrio un problema al intentar guardar el Egreso (Gasto)", e);
		}
	}
	
	public void eliminar() {
		try {
			control();
			if (this.pojoGasto != null && this.pojoGasto.getId() > 0L) {
				this.ifzGastos.delete(this.pojoGasto);
				this.listGastos.remove(this.pojoGasto);
				this.listImpuestosGridEliminados.clear();
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Egreso (Gasto) seleccionado", e);
		}
	}
	
	public void agregarImpuesto() {
		try {
			control();
			// validamos la el impuesto
			if (this.pojoImpuesto == null || this.pojoImpuesto.getId() <= 0L) {
				control(-1, "Debe seleccionar un Impuesto del listado");
				return;
			}

			this.pojoGastoImpuesto = new GastosImpuestoExt();
			this.pojoGastoImpuesto.setImpuestoId(this.pojoImpuesto);
			this.pojoGastoImpuesto.setCreadoPor(this.usuarioId);
			this.pojoGastoImpuesto.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoGastoImpuesto.setModificadoPor(this.usuarioId);
			this.pojoGastoImpuesto.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.listImpuestosGrid == null)
				this.listImpuestosGrid = new ArrayList<GastosImpuestoExt>();
			this.listImpuestosGrid.add(this.pojoGastoImpuesto);
			this.pojoGastoImpuesto = null;
			this.pojoImpuesto = null;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar agregar el impuesto seleccionado", e);
		}
	}

	public void eliminarImpuesto() {		
		try {
			control();
			if (this.pojoGastoImpuesto != null) {
				this.listImpuestosGrid.remove(this.pojoGastoImpuesto);
				this.listImpuestosGridEliminados.add(this.pojoGastoImpuesto);
			}
		} catch(Exception e) {
			control("Ocurrio un problema al intentar eliminar el Impuesto indicado", e);
		}
	}

	private boolean validaRequest(String param) {
		return validaRequest(param, null);
	}
	
	private boolean validaRequest(String param, String refValue) {
		if (! this.isDebug)
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		if (refValue == null || "".equals(refValue.trim()))
			return true;
		
		return (refValue.trim().equals(this.paramsRequest.get(param).trim()));
	}
	
	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n\n" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + " :: GASTOS - CXP :: " + this.tipoMensaje + " - " + mensaje, throwable);
	}

	// --------------------------------------------------------------------------
	// TIPOS DE EGRESOS 
	// --------------------------------------------------------------------------
	
	private void cargarTiposDeEgresos() {
		try{
			control();
			log.info("Buscando Tipos de Egreso ... ");
			if (this.listTipoEgresos != null)
				this.listTipoEgresos.clear();
			if (this.listTipoEgresosItems == null)
				this.listTipoEgresosItems = new ArrayList<SelectItem>();
			this.listTipoEgresosItems.clear();
			
			this.listTipoEgresos = this.ifzConValores.findAll(this.pojoGpoTipoEgresos, "descripcion", 0);
			if (this.listTipoEgresos != null && ! this.listTipoEgresos.isEmpty()) {
				for (ConValores item : this.listTipoEgresos)
					this.listTipoEgresosItems.add(new SelectItem(item.getId(), (this.isDebug ? item.getId() + " - " : "") + item.getDescripcion()));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el catalogo de Tipos de Egresos", e);
		} finally {
			if (this.listTipoEgresos == null)
				this.listTipoEgresos = new ArrayList<ConValores>();
			log.info(this.listTipoEgresos.size() + " Tipos de Egreso encontrados");
		}
	}

	// --------------------------------------------------------------------------
	// IMPUESTOS
	// --------------------------------------------------------------------------
	
	private void cargarImpuestos() {
		boolean incluyeContables = false;
		
		try {
			control();
			incluyeContables = validaRequest("CONTABLE");
			log.info("Buscando impuestos ... ");
			if (this.listImpuestos != null)
				this.listImpuestos.clear();

			if (this.listImpuestosItems == null)
				this.listImpuestosItems = new ArrayList<SelectItem>();
			this.listImpuestosItems.clear();

			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listImpuestos = this.ifzConValores.findAll(this.pojoGpoImpuestos, "descripcion", 0);//findLikeValorIdPropiedadGrupo(obj.toString(), this.pojoGpoImpuestos, 1000);
			if (this.listImpuestos != null && ! this.listImpuestos.isEmpty()) {
				for (ConValores item : this.listImpuestos) {
					if (! incluyeContables && item.getAtributo5() != null && "S".equals(item.getAtributo5()))
						continue;
					this.listImpuestosItems.add(new SelectItem(item.getId(), (incluyeContables ? item.getId() + " - " : "") + item.getDescripcion() + (incluyeContables ? " (" + item.getValor() + ")" : "")));
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Impuestos", e);
		} 
	}
	
	// --------------------------------------------------------------------------
	// PROPIEDADES 
	// --------------------------------------------------------------------------
	
	public String getTitulo() {
		if (this.pojoGasto != null && this.pojoGasto.getId() > 0L)
			return String.valueOf(this.pojoGasto.getId());
		return "";
	}
	
	public void setTitulo(String value) {}
	
	public GastosExt getPojoValores() {
		return this.pojoGasto;
	}
	
	public void setPojoValores(GastosExt pojoValores) {
		this.pojoGasto = pojoValores;
	}
	
	public GastosImpuestoExt getPojoGastoImpuesto() {
		return pojoGastoImpuesto;
	}
	
	public void setPojoGastoImpuesto(GastosImpuestoExt pojoGastoImpuesto) {
		this.pojoGastoImpuesto = pojoGastoImpuesto;
	}
	
	public ConValores getPojoImpuesto() {
		return pojoImpuesto;
	}
	
	public void setPojoImpuesto(ConValores pojoImpuesto) {
		this.pojoImpuesto = pojoImpuesto;
	}
	
	public Long getValorId() {	
		return pojoGasto.getId() != 0L ? this.pojoGasto.getId() : 0;
	}
	
	public void setValorId(Long valorId) {
		this.pojoGasto.setId(valorId);
	}
	
	public Date getFechaModificacion() {
		return pojoGasto.getFechaModificacion();	
	}
	
	public void setFechaModificacion(Date fechaModificacion) {
		this.pojoGasto.setFechaModificacion(fechaModificacion);
	}
	
	public Long getModificadoPor() {
		return pojoGasto.getModificadoPor();
	}
	
	public void setModificadoPor(Long modificadoPor) {
		this.pojoGasto.setModificadoPor(modificadoPor);
	}
	
	public Date getFechaCreacion() {
		return pojoGasto.getFechaCreacion();
	}
	
	public void setFechaCreacion(Date fechaCreacion) {
		this.pojoGasto.setFechaCreacion(fechaCreacion);
	}
	
	public Long getCreadoPor() {
		return pojoGasto.getCreadoPor();
	}
	
	public void setCreadoPor(Long creadoPor) {
		this.pojoGasto.setCreadoPor(creadoPor);
	}
	
	public String getDescripcion() {
		if (this.pojoGasto != null && this.pojoGasto.getDescripcion() != null)
			return this.pojoGasto.getDescripcion();
		return "";
	}
	
	public void setDescripcion(String descripcion) {
		if (descripcion == null)
			return;
		this.pojoGasto.setDescripcion(descripcion);
	}
	
	public ConGrupoValores getGrupoValorId() {
		return this.pojoGasto.getIdGrupo();
	}

	public void setGrupoValorId(ConGrupoValores grupoValorId) {
		this.pojoGasto.setIdGrupo(grupoValorId);
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
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public List<GastosExt> getListValores() {
		return listGastos;
	}

	public void setListValores(List<GastosExt> listGastos) {
		this.listGastos = listGastos;
	}

	public List<SelectItem> getListImpuestosItems() {
		return listImpuestosItems;
	}

	public void setListImpuestosItems(List<SelectItem> listImpuestosItems) {
		this.listImpuestosItems = listImpuestosItems;
	}

	public int getNumPaginaImpuesto() {
		return numPaginaImpuesto;
	}

	public void setNumPaginaImpuesto(int numPaginaImpuesto) {
		this.numPaginaImpuesto = numPaginaImpuesto;
	}

	public List<GastosImpuestoExt> getListImpuestosGrid() {
		return listImpuestosGrid;
	}

	public void setListImpuestosGrid(List<GastosImpuestoExt> listImpuestosGrid) {
		this.listImpuestosGrid = listImpuestosGrid;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public boolean isBand() {
		return operacionCancelada;
	}

	public void setBand(boolean band) {
		this.operacionCancelada = band;
	}

	public List<String> getTiposEgreso() {
		return tiposEgreso;
	}

	public void setTiposEgreso(List<String> tiposEgreso) {
		this.tiposEgreso = tiposEgreso;
	}

	public String getValTipoEgreso() {
		return valTipoEgreso;
	}

	public void setValTipoEgreso(String valTipoEgreso) {
		this.valTipoEgreso = valTipoEgreso;
	}

	public ConGrupoValores getPojoGpoTipoEgresos() {
		return pojoGpoTipoEgresos;
	}
	
	public void setPojoGpoTipoEgresos(ConGrupoValores pojoGpoTipoEgresos) {
		this.pojoGpoTipoEgresos = pojoGpoTipoEgresos;
	}

	public long getIdTipoEgreso() {
		if (this.pojoGasto != null && this.pojoGasto.getIdTipoEgreso() != null && this.pojoGasto.getIdTipoEgreso().getId() > 0L)
			return this.pojoGasto.getIdTipoEgreso().getId();
		return 0L;
	}
	
	public void setIdTipoEgreso(long idTipoEgreso) {
		if (idTipoEgreso <= 0L)
			return;
		
		for (ConValores item : this.listTipoEgresos) {
			if (idTipoEgreso != item.getId())
				continue;
			this.pojoGasto.setIdTipoEgreso(item);
			break;
		}
	}
	
	public Long getValGpoTipoEgresos() {
		return valGpoTipoEgresos;
	}
	
	public void setValGpoTipoEgresos(Long valGpoTipoEgresos) {
		this.valGpoTipoEgresos = valGpoTipoEgresos;
	}

	public List<SelectItem> getListTipoEgresosItems() {
		return listTipoEgresosItems;
	}

	public void setListTipoEgresosItems(List<SelectItem> listTipoEgresosItems) {
		this.listTipoEgresosItems = listTipoEgresosItems;
	}

	public long getIdImpuesto() {
		if (this.pojoImpuesto != null && this.pojoImpuesto.getId() > 0L)
			return this.pojoImpuesto.getId();
		return 0L;
	}
	
	public void setIdImpuesto(long idImpuesto) {
		if (idImpuesto <= 0L)
			return;
		
		for (ConValores item : this.listImpuestos) {
			if (idImpuesto != item.getId())
				continue;
			this.pojoImpuesto = item;
			break;
		}
	}

	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}
	
	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  3.1	| 23/01/2017 | Javier Tirado 			 | Quito la propiedad CuentaContable de la pantalla. Ya no es requerida.
 */
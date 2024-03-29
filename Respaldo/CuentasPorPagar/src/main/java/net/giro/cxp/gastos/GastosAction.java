package net.giro.cxp.gastos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
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
	private ConValores pojoTipoEgreso;
	private GastosImpuestoExt pojoGastoImpuesto;
	private List<GastosImpuestoExt>	listImpuestosGrid;
	private List<GastosImpuestoExt>	listImpuestosGridEliminados;
	private List<GastosExt> listGastos;
	private List<ConValores>	listImpuestos;
	private List<ConValores>	listTipoEgresos;
	// Busqueda principal
	private List<String> tipoBusqueda;
	private String campoBusqueda;	
	private String valorBusqueda;
	private String nombreImpuesto;
    private String mensaje;		
    private long usuarioId;	    
	private int numPagina;
	private int numPaginaImpuesto;
	private int tipoMensaje;
	private boolean operacionCancelada;
	private Long valGpoTipoEgresos;
	private List<String> tiposEgreso;
	private String valTipoEgreso;
	
	
	public GastosAction() throws NamingException,Exception {
		ValueExpression valExp = null;
		FacesContext fc = null;
		Application app = null;
		LoginManager lm = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			lm = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId();

			this.ctx = new InitialContext();
			this.ifzGastos 		  = (GastosRem)	 		 this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosFac!net.giro.cxp.logica.GastosRem");
			this.ifzGpoValores 	  = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores 	  = (ConValoresRem) 	 this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGastoImpuesto = (GastosImpuestoRem)	 this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
			
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
			this.pojoImpuesto = new ConValores();
			this.pojoTipoEgreso = new ConValores();
			this.listImpuestosGridEliminados = new ArrayList<GastosImpuestoExt>();
			this.listImpuestosGrid = new ArrayList<GastosImpuestoExt>();
			this.listImpuestos = new ArrayList<ConValores>();
			this.listGastos = new ArrayList<GastosExt>();
			this.listTipoEgresos = new ArrayList<ConValores>();
			this.numPagina = 1;
			this.numPaginaImpuesto=1;
			this.mensaje = "";
			this.nombreImpuesto = "";
			this.tipoMensaje = 0;
			this.operacionCancelada = false;
			// Busqueda principal
			this.tipoBusqueda = new ArrayList<String>();
			this.tipoBusqueda.add("Nombre del Gasto");
			this.tipoBusqueda.add("ID");
			this.campoBusqueda = tipoBusqueda.get(0);
			this.valorBusqueda = "";
			
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
		String campo = "";
		
		try {
			control();
			log.info("Buscando GASTOS ... ");
			campo = "descripcion";
			if (this.campoBusqueda != null && "ID".equals(this.campoBusqueda))
				campo = "valorId";

			this.listGastos = this.ifzGastos.findLikeProperty(campo, this.valorBusqueda, 0);
			if (this.listGastos == null || this.listGastos.isEmpty()) {
				log.warn("ERROR 2 : Busqueda sin resultados");
				control(2);
				return;
			}/*

			log.info(this.listGastos.size() + " GASTOS encontrados. Determinando impuestos ... ");
			for (GastosExt var : this.listGastos) {
				try {
					if (! (this.ifzGastoImpuesto.findByPropertyPojoCompleto("gastoId", "", var.getId())).isEmpty())
						var.setTieneImpuestos(true);
				} catch (Exception ex) { }
			}

			log.info("Impuestos determinados. Ordenando ... ");*/
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
			log.error("error al buscar", e);
			control(true);
		}
	}
	
	public void nuevo(){
		try {
			control();
			this.pojoGasto = new GastosExt();
			this.pojoTipoEgreso = new ConValores();
			this.nombreImpuesto = "";
			this.tiposEgreso.clear();
			this.listImpuestosGrid.clear();
			this.listImpuestosGridEliminados.clear();
		} catch (Exception e) {
			log.error("Error en el metodo CXP.GastosAction.nuevo()", e);
			control(true);
		}
	}
	
	public void editar() {
		try {
			control();
			this.nombreImpuesto = "";
			if (this.pojoGasto != null && this.pojoGasto.getId() > 0L) {
				// Recuperamos la lista de impuestos asigandos al GASTO.
				this.listImpuestosGrid = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId", "", this.pojoGasto.getId());
				
				// Recuperamos el pojo del tipo de egreso asignado al GASTO.
				this.pojoTipoEgreso = this.pojoGasto.getIdTipoEgreso();
				if (this.pojoTipoEgreso == null)
					this.pojoTipoEgreso = new ConValores();
			}
		} catch (Exception e) {
			log.error("Error en el metodo CXP.GastopnAction.editar().", e);
			control(true);
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
			
			if (this.pojoGasto.getId() <= 0L) { 
				this.pojoGasto.setIdGrupo(this.pojoGpoGastos);
				this.pojoGasto.setIdTipoEgreso(this.pojoTipoEgreso);		
				this.pojoGasto.setCreadoPor(this.usuarioId);
				this.pojoGasto.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoGasto.setModificadoPor(this.usuarioId);
				this.pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD y agregamos a la lista
				this.pojoGasto.setId(this.ifzGastos.save(this.pojoGasto));
				this.listGastos.add(this.pojoGasto);
			} else {
				this.pojoGasto.setIdTipoEgreso(this.pojoTipoEgreso);
				this.pojoGasto.setModificadoPor(this.usuarioId);
				this.pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Actualizamos en la BD y en la lista
				this.ifzGastos.update(this.pojoGasto);
				for (GastosExt var : this.listGastos){
					if(var.getId() == this.pojoGasto.getId()) {
						var = this.pojoGasto;
						break;
					}
				}
			}
			
			// Asignamos el ID del gasto a los Impuestos si corresponde
			if (! this.listImpuestosGrid.isEmpty()){
				for (GastosImpuestoExt var : this.listImpuestosGrid) {
					if (var.getId() == null || var.getId() <= 0L) {
						var.setGastoId(this.pojoGasto.getId());
						var.setModificadoPor(this.usuarioId);
						var.setFechaModificacion(Calendar.getInstance().getTime());
						var.setId(this.ifzGastoImpuesto.save(var));
					}
				}
			}
			
			this.pojoTipoEgreso = new ConValores();
			this.nombreImpuesto="";
		} catch(Exception e) {
			log.error("Error en CXP.GastosAction.guardar(). No se pudo guardar el gasto", e);
			control(true);
		}
	}
	
	public void eliminar() {
		try {
			control();
			if (this.pojoGasto != null && this.pojoGasto.getId() > 0L) {
				this.listImpuestosGrid = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId", "DE", this.pojoGasto.getId());
				if (! this.listImpuestosGrid.isEmpty()) {
					for (GastosImpuestoExt var : this.listImpuestosGrid)					
						this.ifzGastoImpuesto.delete(var);
				}
				
				this.ifzGastos.delete(this.pojoGasto);			
				this.listGastos.remove(this.pojoGasto);
				this.listImpuestosGridEliminados.clear();
			}
		} catch (Exception e) {
			log.error("Error en el metodo CXP.GastosAction.eliminar()", e);
			control(true);
		}
	}
	
	public void agregarImpuesto() {
		Pattern pat = null;
		Matcher match = null;
		
		try {
			control();
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			
			// validamos la el impuesto
			match = pat.matcher(this.nombreImpuesto);
			if(match.find())
				this.pojoImpuesto = this.ifzConValores.findById(Long.valueOf(match.group(1)));
			if (this.pojoImpuesto == null) {
				log.error("ERROR INTERNO - No se pudo recuperar el impuesto indicado: " + this.nombreImpuesto);
				control("No se pudo recuperar el Impuesto indicado");
				return;
			}

			this.pojoGastoImpuesto = new GastosImpuestoExt();
			this.pojoGastoImpuesto.setImpuestoId(this.pojoImpuesto);
			this.pojoGastoImpuesto.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoGastoImpuesto.setCreadoPor(this.usuarioId);
			this.pojoGastoImpuesto.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoGastoImpuesto.setModificadoPor(this.usuarioId);
			
			this.listImpuestosGrid.add(this.pojoGastoImpuesto);
			this.nombreImpuesto = "";
		} catch (Exception e) {
			log.error("Error en CXP.GastosAction.agregarImpuesto(). No se pudo agregar el impuesto indicado: " + this.nombreImpuesto, e);
			control(true);
		}
	}

	public void eliminarImpuesto(){		
		try {
			control();
			if (this.pojoGastoImpuesto != null) {
				this.listImpuestosGrid.remove(this.pojoGastoImpuesto);
				this.listImpuestosGridEliminados.add(this.pojoGastoImpuesto);
			}
		} catch(Exception e) {
			log.error("Error en CXP.GastosAction.eliminarImpuesto(). No se pudo eliminar el Impuesto indicado", e);
			control(true);
		}
	}
	
	public List<ConValores> autoacompletaImpto(Object obj){		
		try {
			control();
			log.info("Buscando impuestos ... ");
			if (this.listImpuestos == null)
				this.listImpuestos = new ArrayList<ConValores>();
			if (! this.listImpuestos.isEmpty())
				this.listImpuestos.clear();
			
			this.listImpuestos = this.ifzConValores.findLikeValorIdPropiedadGrupo(obj.toString(), this.pojoGpoImpuestos, 1000);
		} catch (Exception e) {
			log.error("Error en CXP.GastosAction.autoacompletaImpto().", e);
			control(true);
		} finally {
			if (this.listImpuestos == null)
				this.listImpuestos = new ArrayList<ConValores>();
			log.info(this.listImpuestos.size() + " Impuestos encontrados");
		}

		return this.listImpuestos;
	}
	
	public List<ConValores> autoacompletaTiposEgreso(Object obj){		
		try{
			control();
			log.info("Buscando Tipos de Egreso ... ");
			if (this.listTipoEgresos == null)
				this.listTipoEgresos = new ArrayList<ConValores>();
			if (! this.listTipoEgresos.isEmpty())
				this.listTipoEgresos.clear();
			
			this.listTipoEgresos = this.ifzConValores.findLikeValorIdPropiedadGrupo(obj.toString(), this.pojoGpoTipoEgresos, 1000);
		} catch (Exception e) {
			log.error("Error en CXP.GastosAction.autoacompletaTiposEgreso().", e);
			control(true);
		} finally {
			if (this.listTipoEgresos == null)
				this.listTipoEgresos = new ArrayList<ConValores>();
			log.info(this.listTipoEgresos.size() + " Tipos de Egreso encontrados");
		}
		
		return this.listTipoEgresos;
	}

	private void control() {
		control(false);
	}

	private void control(boolean operacionCancelada) {
		control(false, 0, "");
		if (operacionCancelada)
			control(true, 1, "ERROR");
	}
	
	private void control(int tipoMensaje) {
		control(true, tipoMensaje, "ERROR");
	}
	
	private void control(String mensaje) {
		control(false, 0, "");
		if (mensaje != null && ! "".equals(mensaje))
			control(true, -1, mensaje);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje;
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
	
	public ConGrupoValores getPojoGpoImpuestos() {
		return pojoGpoImpuestos;
	}
	
	public void setPojoGpoImpuestos(ConGrupoValores pojoGpoImpuestos) {
		this.pojoGpoImpuestos = pojoGpoImpuestos;
	}

	public ConGrupoValores getPojoGpoVal() {
		return pojoGpoGastos;
	}
	
	public void setPojoGpoVal(ConGrupoValores pojoGpoVal) {
		this.pojoGpoGastos = pojoGpoVal;
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
		return pojoGasto.getDescripcion()!= null ? this.pojoGasto.getDescripcion() : "";
	}
	
	public void setDescripcion(String descripcion) {
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
	
	public long getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
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

	public List<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(List<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getNombreImpuesto() {
		return nombreImpuesto;
	}

	public void setNombreImpuesto(String nombreImpuesto) {
		this.nombreImpuesto = nombreImpuesto;
	}

	public List<ConValores> getListImpuestos() {
		return listImpuestos;
	}

	public void setListImpuestos(List<ConValores> listImpuestos) {
		this.listImpuestos = listImpuestos;
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

	public Long getGastosImpuestoId() {
		return pojoGastoImpuesto.getId()!= null ? pojoGastoImpuesto.getId(): 0;
	}

	public void setGastosImpuestoId(Long gastosImpuestoId) {
		this.pojoGastoImpuesto.setId(gastosImpuestoId);
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
	
	public ConValores getPojoTipoEgreso() {
		return pojoTipoEgreso;
	}
	
	public void setPojoTipoEgreso(ConValores pojoTipoEgreso) {
		this.pojoTipoEgreso = pojoTipoEgreso;
	}
	
	public Long getValGpoTipoEgresos() {
		return valGpoTipoEgresos;
	}
	
	public void setValGpoTipoEgresos(Long valGpoTipoEgresos) {
		this.valGpoTipoEgresos = valGpoTipoEgresos;
	}

	public List<ConValores> getListTipoEgresos() {
		return listTipoEgresos;
	}

	public void setListTipoEgresos(List<ConValores> listTipoEgresos) {
		this.listTipoEgresos = listTipoEgresos;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSI�N |    FECHA   | 			 AUTOR 			 | DESCRIPCI�N 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  3.1	| 23/01/2017 | Javier Tirado 			 | Quito la propiedad CuentaContable de la pantalla. Ya no es requerida.
 */
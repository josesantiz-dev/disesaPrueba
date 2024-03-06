package net.giro.cxc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.cxc.beans.ConceptoFacturacion;
import net.giro.cxc.beans.ConceptoFacturacionImpuestosExt;
import net.giro.cxc.logica.ConceptoFacturacionImpuestosRem;
import net.giro.cxc.logica.ConceptoFacturacionRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;


@ViewScoped
@ManagedBean(name="conceptosAction")
public class ConceptosAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ConceptosAction.class);
	private InitialContext ctx;
	// Interfaces
	private ConceptoFacturacionRem 			ifzConceptos;
	private ConGrupoValoresRem 				ifzGpoVal;
	private ConceptoFacturacionImpuestosRem ifzConceptoImpuestos;
	private ConValoresRem 					ifzConValores;
	// Listas 
	private List<ConceptoFacturacion> 			  listConceptos;
	private List<ConValores>					  listImpuestos;
	private List<ConceptoFacturacionImpuestosExt> listImpuestosGrid;
	private List<ConceptoFacturacionImpuestosExt> listImpuestosGridEliminados;
	// POJO's
	private ConceptoFacturacion 			pojoConcepto;
	private ConceptoFacturacionImpuestosExt pojoConceptoImpuesto;
	private ConGrupoValores 				pojoGpoImpuestos;
	private ConValores 						pojoImpuesto;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// operacion
	private boolean procesoCancelado;
	private int tipoMensaje;
	private String mensaje;
    private long usuarioId;
	// Impuestos
	private String nombreImpuesto;
	private int numPaginaImpuesto;
	// ClaveSat
	private ConGrupoValores pojoGpoClaveSat;
	private List<ConValores> listClaveSat;
	private List<SelectItem> listClaveSatItems;
	
	
	public ConceptosAction() {
		FacesContext fc = null;
		Application app = null;
		LoginManager loginManager = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			this.ctx = new InitialContext();
			this.ifzConceptos			= (ConceptoFacturacionRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//ConceptoFacturacionFac!net.giro.cxc.logica.ConceptoFacturacionRem");
			this.ifzConceptoImpuestos 	= (ConceptoFacturacionImpuestosRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//ConceptoFacturacionImpuestosFac!net.giro.cxc.logica.ConceptoFacturacionImpuestosRem");
			this.ifzGpoVal 				= (ConGrupoValoresRem) 				this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores			= (ConValoresRem) 	 				this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
			this.ifzConceptoImpuestos.showSystemOuts(false);
						
			this.listConceptos 				 = new ArrayList<ConceptoFacturacion>();
			this.listImpuestos 				 = new ArrayList<ConValores>();
			this.listImpuestosGrid 			 = new ArrayList<ConceptoFacturacionImpuestosExt>();
			this.listImpuestosGridEliminados = new ArrayList<ConceptoFacturacionImpuestosExt>();

			this.pojoConcepto = new ConceptoFacturacion();
			this.pojoConcepto.setCuentaContable("");
			this.pojoConcepto.setDescripcion("");
			
			this.pojoImpuesto = new ConValores();
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("descripcion", "Concepto"));
			this.tiposBusqueda.add(new SelectItem("cuentaContable", "Cuenta Contable"));
			this.tiposBusqueda.add(new SelectItem("id", "Clave"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// CARGAMOS LOS METODOS DE PAGO PARA FACTURAS
			// ---------------------------------------------------------------------------------------------------------
			this.pojoGpoImpuestos = this.ifzGpoVal.findByName("SYS_IMPTOS");
			if (this.pojoGpoImpuestos == null || this.pojoGpoImpuestos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
			this.numPaginaImpuesto = 1;
			
			// CARGAMOS LAS CLAVES DEL SAT
			this.pojoGpoClaveSat = this.ifzGpoVal.findByName("SYS_CLAVE_SAT");
			if (this.pojoGpoClaveSat == null || this.pojoGpoClaveSat.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_CLAVE_SAT en con_grupo_valores");
			cargarClavesSat();
		} catch (Exception e) {
			log.error("Error en constructor ConceptosAction", e);
		}
	}

	
	public void buscar() {
		try {
			control();
			if (this.campoBusqueda == null || "".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			
			this.listConceptos = this.ifzConceptos.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 0);
			if (this.listConceptos == null || this.listConceptos.isEmpty()) {
				log.info("ERROR 2 - Busqueda sin resultados");
				control(2);
			}
		} catch(Exception e) {
			log.error("error al buscar", e);
			control(true);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.pojoConcepto = new ConceptoFacturacion();
			this.nombreImpuesto = "";
			this.listImpuestosGrid.clear();
			this.listImpuestosGridEliminados.clear();
		} catch (Exception e) {
			log.error("Error en el metodo nuevo.", e);
			control(true);
		}
	}

	public void editar() {
		try {
			control();
			this.nombreImpuesto = "";
			this.pojoImpuesto = null;
			this.pojoConceptoImpuesto = null;
			this.listImpuestos.clear();
			this.listImpuestosGrid.clear();
			this.listImpuestosGridEliminados.clear();

			// recuperamos los impuestos
			this.listImpuestosGrid = this.ifzConceptoImpuestos.findByPropertyExt("idConceptoFacturacion", this.pojoConcepto.getId());
		} catch (Exception e) {
			log.error("Error en el metodo editar.", e);
			control(true);
		}
	}

	public void guardar() {
		try {
			control();
			if (! validar())
				return;
			
			// Quitamos de la BD los impuestos eliminados de la lista
			if (! listImpuestosGridEliminados.isEmpty()) {
				for(ConceptoFacturacionImpuestosExt var : this.listImpuestosGridEliminados) {
					if (var.getId() != null && var.getId() > 0L) {
						this.ifzConceptoImpuestos.delete(var.getId());
					}
				}
				
				// Limpiamos el listado
				this.listImpuestosGridEliminados.clear();
			}

			this.pojoConcepto.setModificadoPor(this.usuarioId);
			this.pojoConcepto.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoConcepto.getId() == null || this.pojoConcepto.getId() == 0L) { // getValorId() == null){				
				this.pojoConcepto.setCreadoPor(this.usuarioId);
				this.pojoConcepto.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD
				this.pojoConcepto.setId(this.ifzConceptos.save(this.pojoConcepto));
				
				// Guardamos en la lista
				this.listConceptos.add(this.pojoConcepto);

				// Guardamos los impuestos
				if (! this.listImpuestosGrid.isEmpty()) {
					for(ConceptoFacturacionImpuestosExt var:  this.listImpuestosGrid) {
						if(var.getId() == null) {
							var.setIdConceptoFacturacion(this.pojoConcepto);
							var.setId(this.ifzConceptoImpuestos.save(var));
						}
					}
				}
			} else {
				// Actualizamos en la BD
				this.ifzConceptos.update(this.pojoConcepto);
				
				// Actualizamos en la lista
				for(ConceptoFacturacion var : this.listConceptos){
					if(var.getId() == this.pojoConcepto.getId())
						var = this.pojoConcepto;
				}
				
				// Actualizamos los impuestos
				if (! this.listImpuestosGrid.isEmpty()) {
					for (ConceptoFacturacionImpuestosExt var : this.listImpuestosGrid) {
						if (var.getId() == null) {
							var.setIdConceptoFacturacion(this.pojoConcepto);
							var.setId(this.ifzConceptoImpuestos.save(var));
						}
					}
				}
			}
		} catch(Exception e) {
			log.error("error al guardar", e);
			control(true);
		}
	}
	
	public void eliminar(){
		try {
			control();
			this.nombreImpuesto = "";

			this.pojoConcepto.setEstatus(1);
			this.pojoConcepto.setModificadoPor(this.usuarioId);
			this.pojoConcepto.setFechaModificacion(Calendar.getInstance().getTime());
			
			// Actualizamos en la BD
			this.ifzConceptos.update(this.pojoConcepto);
			
			// Actualizamos en la lista
			for (ConceptoFacturacion var : this.listConceptos){
				if (var.getId() == this.pojoConcepto.getId())
					var = this.pojoConcepto;
			}
			
			this.pojoImpuesto = null;
			this.pojoConceptoImpuesto = null;
			
			// Limpiamos listas
			this.listImpuestosGrid.clear();
			this.listImpuestosGridEliminados.clear();
		} catch (Exception e) {
			log.error("Error en el metodo eliminar.", e);
			control(true);
		}
	}
	
	private boolean validar() {
		if (Strings.isNullOrEmpty(this.pojoConcepto.getDescripcion())) {
			log.error("VALIDACION :: No establecio la Descripcion del Concepto");
			control("Debe indicar la Descripcion del Concepto");
			return false;
		}
		
		if (Strings.isNullOrEmpty(this.pojoConcepto.getCuentaContable())) {
			log.error("VALIDACION :: No establecio la Cuenta Contable del Concepto");
			control("Debe indicar la Cuenta Contable del Concepto");
			return false;
		}
		
		return true;
	}
	
	private void cargarClavesSat() {
		try {
    		control();
			if (this.listClaveSatItems == null)
				this.listClaveSatItems = new ArrayList<SelectItem>();
			this.listClaveSatItems.clear();
			
			this.listClaveSat = this.ifzConValores.findAll(this.pojoGpoClaveSat);
			if (this.listClaveSat != null && ! this.listClaveSat.isEmpty()) {
				for (ConValores var : this.listClaveSat) {
					this.listClaveSatItems.add(new SelectItem(var.getValor(), var.getValor() + " - " + var.getDescripcion()));
				}
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.cargarFormasPagos", e);
    		control(true);
		}
	}

	private void control() {
		control(false, 0, "");
	}
	
	private void control(boolean value) {
		this.procesoCancelado = value;
		
		if (! value) {
			control(false, 0, "");
			return;
		} 

		control(true, 1, "Ha Ocurrido un error interno. Consulte a su Administrador");
	}
		
	private void control(int tipoMensaje) {
		if (tipoMensaje == 0) {
			control(false);
			return;
		}
		
		control(true, tipoMensaje, "ERROR");
	}
	
	private void control(String mensaje) {
		if (mensaje == null || "".equals(mensaje.trim())) 
			control();
		else
			control(true, -1, mensaje);
	}
	
	private void control(boolean procesoIncompleto, int tipoMensaje, String mensaje) {
		this.procesoCancelado = procesoIncompleto;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje;
	}

	// ---------------------------------------
	// IMPUESTOS
	// ---------------------------------------

	public List<ConValores> autoacompletaImpuestos(String obj) {
		try {
			this.listImpuestos = this.ifzConValores.findLikeValorIdPropiedadGrupo(obj.toString(), this.pojoGpoImpuestos, 100);
			return this.listImpuestos;
		} catch (Exception e) {
			return new ArrayList<ConValores>();
		}
	}

	public void agregarImpuesto() {
		Pattern pat = null;
		Matcher match = null;
		
		try {
			control();
			// validamos la el impuesto
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.nombreImpuesto);
			
			if (match.find())
				this.pojoImpuesto = this.ifzConValores.findById(Long.valueOf(match.group(1)));

			if (this.pojoImpuesto != null) {
				this.pojoConceptoImpuesto = new ConceptoFacturacionImpuestosExt();
				this.pojoConceptoImpuesto.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoConceptoImpuesto.setCreadoPor(Short.valueOf(String.valueOf(this.usuarioId)));
				this.pojoConceptoImpuesto.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoConceptoImpuesto.setModificadoPor(Short.valueOf(String.valueOf(this.usuarioId)));
				this.pojoConceptoImpuesto.setIdImpuesto(this.pojoImpuesto);
				
				this.listImpuestosGrid.add(this.pojoConceptoImpuesto);
				this.numPaginaImpuesto = 1;
			}
			
			this.nombreImpuesto = "";
			this.pojoImpuesto = null;
			this.pojoConceptoImpuesto = null;
		} catch (Exception e) {
			log.error("error al agregarImpuesto", e);
			control(true);
		}
	}

	public void eliminarImpuesto() {
		try {
			control();
			this.nombreImpuesto = "";
			this.listImpuestosGrid.remove(this.pojoConceptoImpuesto);
			this.listImpuestosGridEliminados.add(this.pojoConceptoImpuesto);
			
			this.pojoImpuesto = null;
			this.pojoConceptoImpuesto = null;
		} catch(Exception e) {
			log.error("error al eliminarImpuesto", e);
			control(true);
		}
	}
	
	// ---------------------------------------
	// PROPIEDADES
	// ---------------------------------------
	
	public List<SelectItem> getTipoBusqueda() {
		return tiposBusqueda;
	}

	public void setTipoBusqueda(List<SelectItem> tipoBusqueda) {
		this.tiposBusqueda = tipoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public List<ConceptoFacturacion> getListConceptos() {
		return listConceptos;
	}

	public void setListConceptos(List<ConceptoFacturacion> listConceptos) {
		this.listConceptos = listConceptos;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
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

	public ConceptoFacturacion getPojoConcepto() {
		return pojoConcepto;
	}

	public void setPojoConcepto(ConceptoFacturacion pojoConcepto) {
		this.pojoConcepto = pojoConcepto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<ConceptoFacturacionImpuestosExt> getListImpuestosGrid() {
		return listImpuestosGrid;
	}

	public void setListImpuestosGrid(List<ConceptoFacturacionImpuestosExt> listImpuestosGrid) {
		this.listImpuestosGrid = listImpuestosGrid;
	}

	public int getNumPaginaImpuesto() {
		return numPaginaImpuesto;
	}

	public void setNumPaginaImpuesto(int numPaginaImpuesto) {
		this.numPaginaImpuesto = numPaginaImpuesto;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public ConValores getPojoImpuesto() {
		return pojoImpuesto;
	}

	public void setPojoImpuesto(ConValores pojoImpuesto) {
		this.pojoImpuesto = pojoImpuesto;
	}

	public boolean getBand() {
		return procesoCancelado;
	}

	public void setBand(boolean band) {
		this.procesoCancelado = band;
	}
	
	public String getDescripcion() {
		return this.pojoConcepto.getDescripcion();
	}

	public void setDescripcion(String descripcion) {
		this.pojoConcepto.setDescripcion(descripcion);
	}

	public String getCuentaContable() {
		return this.pojoConcepto.getCuentaContable();
	}

	public void setCuentaContable(String cuentaContable) {
		this.pojoConcepto.setCuentaContable(cuentaContable);
	}

	public String getClaveSat() {
		if (this.pojoConcepto != null && this.pojoConcepto.getClaveSat() != null)
			return this.pojoConcepto.getClaveSat().toString();
		return "";
	}

	public void setClaveSat(String claveSat) {
		this.pojoConcepto.setClaveSat(0L);
		if (claveSat != null && ! "".equals(claveSat))
			this.pojoConcepto.setClaveSat(Long.valueOf(claveSat));
	}

	public ConceptoFacturacionImpuestosExt getPojoConceptoImpuesto() {
		return pojoConceptoImpuesto;
	}

	public void setPojoConceptoImpuesto(ConceptoFacturacionImpuestosExt pojoConceptoImpuesto) {
		this.pojoConceptoImpuesto = pojoConceptoImpuesto;
	}


	
	public List<SelectItem> getListClaveSatItems() {
		return listClaveSatItems;
	}
	


	public void setListClaveSatItems(List<SelectItem> listClaveSatItems) {
		this.listClaveSatItems = listClaveSatItems;
	}
}

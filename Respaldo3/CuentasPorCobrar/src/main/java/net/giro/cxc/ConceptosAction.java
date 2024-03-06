package net.giro.cxc;

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
	private LoginManager loginManager;
	private InitialContext ctx;
	// Interfaces
	private ConceptoFacturacionRem 			ifzConceptos;
	private ConGrupoValoresRem 				ifzGpoValores;
	private ConceptoFacturacionImpuestosRem ifzConceptoImpuestos;
	private ConValoresRem 					ifzConValores;
	// Listas 
	private List<ConceptoFacturacion> 			  listConceptos;
	private List<ConceptoFacturacionImpuestosExt> listImpuestosGrid;
	private List<ConceptoFacturacionImpuestosExt> listImpuestosGridEliminados;
	// POJO's
	private ConceptoFacturacion 			pojoConcepto;
	private ConceptoFacturacionImpuestosExt pojoConceptoImpuesto;
	private ConGrupoValores 				pojoGpoImpuestos;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// operacion
	private boolean procesoCancelado;
	private int tipoMensaje;
	private String mensaje;
	// Impuestos
	private List<ConValores> listImpuestos;
	private List<SelectItem> listImpuestosItems;
	private ConValores pojoImpuesto;
	//private String nombreImpuesto;
	private int numPaginaImpuesto;
	// ClaveSat
	private ConGrupoValores grupoClaveSat;
	private List<ConValores> listClaveSat;
	private List<SelectItem> listClaveSatItems;
	// Unidad Medida
	private ConGrupoValores grupoUnidadesMedidas;
	private List<ConValores> listUnidadesMedidas;
	private List<SelectItem> listUnidadesMedidasItems;
	private long idUnidadMedida;
	private long idUnidadMedidaSugerido;
	
	public ConceptosAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			
			this.ctx = new InitialContext();
			this.ifzConceptos			= (ConceptoFacturacionRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//ConceptoFacturacionFac!net.giro.cxc.logica.ConceptoFacturacionRem");
			this.ifzConceptoImpuestos 	= (ConceptoFacturacionImpuestosRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//ConceptoFacturacionImpuestosFac!net.giro.cxc.logica.ConceptoFacturacionImpuestosRem");
			this.ifzGpoValores 			= (ConGrupoValoresRem) 				this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores			= (ConValoresRem) 	 				this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
			this.ifzConceptos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConceptoImpuestos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			
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
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusqueda.add(new SelectItem("claveUnidadMedida", "Unidad Medida"));
			this.tiposBusqueda.add(new SelectItem("claveSat", "Clave SAT"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// CARGAMOS LOS METODOS DE PAGO PARA FACTURAS
			// ---------------------------------------------------------------------------------------------------------
			this.pojoGpoImpuestos = this.ifzGpoValores.findByName("SYS_IMPTOS");
			if (this.pojoGpoImpuestos == null || this.pojoGpoImpuestos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
			this.numPaginaImpuesto = 1;
			
			// CARGAMOS LAS CLAVES DEL SAT
			// ---------------------------------------------------------------------------------------------------------
			this.grupoClaveSat = this.ifzGpoValores.findByName("SYS_CLAVE_SAT");
			if (this.grupoClaveSat == null || this.grupoClaveSat.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_CLAVE_SAT en con_grupo_valores");
			cargarClavesSat();
			
			// CARGAMOS UNIDADES DE MEDIDA
			// ---------------------------------------------------------------------------------------------------------
			this.grupoUnidadesMedidas = this.ifzGpoValores.findByName("SYS_UNIDAD_MEDIDA_SAT");
			if (this.grupoUnidadesMedidas == null || this.grupoUnidadesMedidas.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_UNIDAD_MEDIDA_SAT en con_grupo_valores");
			cargarUnidadesMedida();
		} catch (Exception e) {
			log.error("Error en constructor ConceptosAction", e);
		}
	}

	
	public void buscar() {
		try {
			control();
			if (this.campoBusqueda == null || "".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.listConceptos = this.ifzConceptos.findLikeProperty(this.campoBusqueda, this.valorBusqueda, false, "id desc", 0);
			if (this.listConceptos == null || this.listConceptos.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch(Exception e) {
			control("error al buscar", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.pojoConcepto = new ConceptoFacturacion();
			this.pojoConcepto.setCuentaContable("0000");
			this.pojoImpuesto = null;
			this.listImpuestosGrid.clear();
			this.listImpuestosGridEliminados.clear();
			this.idUnidadMedida = this.idUnidadMedidaSugerido;
			cargarImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar instanciar la pantalla para un nuevo Concepto.", e);
		}
	}

	public void editar() {
		try {
			control();
			this.pojoImpuesto = null;
			this.pojoConceptoImpuesto = null;
			this.listImpuestos.clear();
			this.listImpuestosGrid.clear();
			this.listImpuestosGridEliminados.clear();
			this.idUnidadMedida = this.pojoConcepto.getIdUnidadMedida();

			// recuperamos los impuestos
			this.listImpuestosGrid = this.ifzConceptoImpuestos.findByPropertyExt("idConceptoFacturacion", this.pojoConcepto.getId());
			cargarImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el Concepto seleccionado.", e);
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

			this.pojoConcepto.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			this.pojoConcepto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoConcepto.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoConcepto.getId() == null || this.pojoConcepto.getId() == 0L) { // getValorId() == null){				
				this.pojoConcepto.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
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
				for (ConceptoFacturacion var : this.listConceptos) {
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
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar el Concepto", e);
		}
	}
	
	public void eliminar(){
		try {
			control();
			this.pojoConcepto.setEstatus(1);
			this.pojoConcepto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
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
			control("Ocurrio un problema al intentar eliminar el Concepto indicado.", e);
		}
	}
	
	private boolean validar() {
		if (Strings.isNullOrEmpty(this.pojoConcepto.getDescripcion())) {
			control(-1, "Debe indicar la Descripcion del Concepto");
			return false;
		}
		
		if (Strings.isNullOrEmpty(this.pojoConcepto.getCuentaContable())) {
			control(-1, "Debe indicar la Cuenta Contable del Concepto");
			return false;
		}
		
		if (this.idUnidadMedida <= 0L) {
			control(-1, "Debe seleccionar la Unidad de Medida para el Concepto");
			return false;
		}
		
		if (this.pojoConcepto.getIdUnidadMedida() == null || this.idUnidadMedida != this.pojoConcepto.getIdUnidadMedida().longValue()) {
			for (ConValores var : this.listUnidadesMedidas) {
				if (this.idUnidadMedida == var.getId()) {
					this.pojoConcepto.setIdUnidadMedida(var.getId());
					this.pojoConcepto.setClaveUnidadMedida(var.getValor());
					break;
				}
			}
		}
		
		return true;
	}
	
	private void cargarClavesSat() {
		try {
    		control();
			if (this.listClaveSatItems == null)
				this.listClaveSatItems = new ArrayList<SelectItem>();
			this.listClaveSatItems.clear();
			
			this.listClaveSat = this.ifzConValores.findAll(this.grupoClaveSat, "", 0);
			if (this.listClaveSat != null && ! this.listClaveSat.isEmpty()) {
				for (ConValores var : this.listClaveSat) {
					this.listClaveSatItems.add(new SelectItem(var.getValor(),  var.getDescripcion() + " (" + var.getValor() + ")"));
				}
			}
		} catch (Exception e) {
			control("Error en CuentasPorCobrar.ConceptosAction.cargarClavesSat", e);
		}
	}
	
	private void cargarUnidadesMedida() {
		try {
    		control();
			if (this.listUnidadesMedidasItems == null)
				this.listUnidadesMedidasItems = new ArrayList<SelectItem>();
			this.listUnidadesMedidasItems.clear();
			
			this.listUnidadesMedidas = this.ifzConValores.findAll(this.grupoUnidadesMedidas);
			if (this.listUnidadesMedidas != null && ! this.listUnidadesMedidas.isEmpty()) {
				for (ConValores var : this.listUnidadesMedidas) {
					if ("XLT".equals(var.getValor()))
						this.idUnidadMedidaSugerido = var.getId();
					this.listUnidadesMedidasItems.add(new SelectItem(var.getId(), var.getValor() + " - " + var.getDescripcion()));
				}
			}
		} catch (Exception e) {
			control("Error en CuentasPorCobrar.ConceptosAction.cargarUnidadesMedida", e);
		}
	}

	private void control() {
		this.procesoCancelado = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null, null, null);
	}

	/*private void control(int tipoMensaje, String mensaje, String campo, String valor) {
		control(true, tipoMensaje, mensaje, null, campo, valor);
	}*/
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable, "", "");
	}
	
	private void control(boolean procesoCancelado, int tipoMensaje, String mensaje, Throwable throwable, String campo, String valor) {
		mensaje = ((mensaje == null || "".equals(mensaje.trim())) ? "Ocurrio un problema al intentar procesar la accion" : mensaje);
		campo = (campo != null && ! "".equals(campo.trim()) ? campo : "CAMPO-NA");
		valor = (valor != null && ! "".equals(valor.trim()) ? valor : "VALOR-NA");
		
		this.procesoCancelado = procesoCancelado;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n\n CONCEPTOS CXC :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + campo + ": " + valor + "\n" + this.tipoMensaje + ": " + mensaje + "\n", throwable);
	}

	// ---------------------------------------
	// IMPUESTOS
	// ---------------------------------------

	private void cargarImpuestos() {
		try {
			control();
			log.info("Buscando impuestos ... ");
			if (this.listImpuestos != null)
				this.listImpuestos.clear();

			if (this.listImpuestosItems == null)
				this.listImpuestosItems = new ArrayList<SelectItem>();
			this.listImpuestosItems.clear();
			
			this.listImpuestos = this.ifzConValores.findAll(this.pojoGpoImpuestos, "descripcion", 0);//findLikeValorIdPropiedadGrupo(obj.toString(), this.pojoGpoImpuestos, 1000);
			if (this.listImpuestos != null && ! this.listImpuestos.isEmpty()) {
				for (ConValores item : this.listImpuestos) {
					if (item.getAtributo5() != null && "S".equals(item.getAtributo5()))
						continue;
					this.listImpuestosItems.add(new SelectItem(item.getId(), item.getDescripcion()));
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar los Impuestos.", e);
		} 
	}
	
	/*public List<ConValores> autoacompletaImpuestos(String obj) {
		try {
			this.listImpuestos = this.ifzConValores.findLikeValorIdPropiedadGrupo(obj.toString(), this.pojoGpoImpuestos, 100);
			return this.listImpuestos;
		} catch (Exception e) {
			return new ArrayList<ConValores>();
		}
	}*/

	public void agregarImpuesto() {
		try {
			control();
			if (this.pojoImpuesto != null) {
				this.pojoConceptoImpuesto = new ConceptoFacturacionImpuestosExt();
				this.pojoConceptoImpuesto.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoConceptoImpuesto.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoConceptoImpuesto.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoConceptoImpuesto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoConceptoImpuesto.setIdImpuesto(this.pojoImpuesto);
				
				this.listImpuestosGrid.add(this.pojoConceptoImpuesto);
				this.numPaginaImpuesto = 1;
			}
			
			this.pojoImpuesto = null;
			this.pojoConceptoImpuesto = null;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar agregar el Impuesto", e);
		}
	}

	public void eliminarImpuesto() {
		try {
			control();
			this.listImpuestosGrid.remove(this.pojoConceptoImpuesto);
			this.listImpuestosGridEliminados.add(this.pojoConceptoImpuesto);
			
			this.pojoImpuesto = null;
			this.pojoConceptoImpuesto = null;
		} catch(Exception e) {
			control("Ocurrio un problema al intentar eliminar el Impuesto", e);
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
			return this.pojoConcepto.getClaveSat();
		return "";
	}

	public void setClaveSat(String claveSat) {
		this.pojoConcepto.setClaveSat(claveSat);
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

	public List<SelectItem> getListUnidadesMedidasItems() {
		return listUnidadesMedidasItems;
	}

	public void setListUnidadesMedidasItems(
			List<SelectItem> listUnidadesMedidasItems) {
		this.listUnidadesMedidasItems = listUnidadesMedidasItems;
	}

	public long getIdUnidadMedida() {
		return idUnidadMedida;
	}

	public void setIdUnidadMedida(long idUnidadMedida) {
		this.idUnidadMedida = idUnidadMedida;
	}

	public List<SelectItem> getListImpuestosItems() {
		return listImpuestosItems;
	}

	public void setListImpuestosItems(List<SelectItem> listImpuestosItems) {
		this.listImpuestosItems = listImpuestosItems;
	}
}

package cde.publico.ne;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ImpuestoEquivalencia;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ImpuestoEquivalenciaRem;

import org.apache.log4j.Logger;

public class ImpuestoEquivalenciaAction {
	private static Logger log = Logger.getLogger(ImpuestoEquivalenciaAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	private ImpuestoEquivalenciaRem ifzImpEquivalencia;
	private List<ImpuestoEquivalencia> listEquivalencias;
	private ImpuestoEquivalencia pojoEquivalencia;
	private ImpuestoEquivalencia pojoEquivalenciaBorrar;
	private List<SelectItem> listTiposEquivalencias;
    //private long usuarioId;
    private String usuario;
    // Busqueda principal
    private List<SelectItem> listTiposBusqueda;
    private String campoBusqueda;
    private String valorBusqueda;
	private int paginacion;
	// Impuestos
	private ConGrupoValoresRem ifzGruposValores;
	//private ConValoresRem ifzConValores;
	private ConGrupoValores pojoGrupoImpuestos; 
	//private List<ConValores> listImpuestos;
	private List<SelectItem> listImpuestosItems;
	private List<SelectItem> listImpuestosContablesItems;
	// control
	private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	

	public ImpuestoEquivalenciaAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			//this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			this.ctx = new InitialContext();
			this.ifzImpEquivalencia = (ImpuestoEquivalenciaRem) this.ctx.lookup("ejb:/Logica_Publico//ImpuestoEquivalenciaFac!net.giro.plataforma.logica.ImpuestoEquivalenciaRem");
			this.ifzGruposValores 	= (ConGrupoValoresRem) 		this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			//this.ifzConValores		= (ConValoresRem) 			this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
			// Grupo de valores para SYS_IMPTOS (Impuestos) de gastos
			this.pojoGrupoImpuestos = this.ifzGruposValores.findByName("SYS_IMPTOS");
			if (this.pojoGrupoImpuestos == null || this.pojoGrupoImpuestos.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_IMPTOS (Impuestos) en con_grupo_valores");
		} catch (Exception e) {
			
		}
	}

	
	public void buscar() {
		try {
			control();
			
			this.listEquivalencias = this.ifzImpEquivalencia.findLikeProperty("", "", 0);
			if (this.listEquivalencias == null || this.listEquivalencias.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al consultar las Equivalencias", e);
		}
	}

	public void nuevo() {
		try {
			control();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar generar una nueva Equivalencia", e);
		}
	}
	
	public void guardar() {
		try {
			control();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar Guardar la Equivalencia indicada", e);
		} 
	}

	public void eliminar(){
		try {
			control();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar Eliminar la Equivalencia selecionada", e);
		}
	}
	
	private void control() {
		this.operacion = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
		}
		
		log.error("\n\nEQUIVALENCIA IMPUESTOS :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n", throwable);
	}

	// -------------------------------------------------------
	// IMPUESTOS
	// -------------------------------------------------------
	
	public void cargarImpuestos() {
		
	}
	
	public void filtrarImpuestos() {
		
	}
	
	public void filtrarImpuestosContables() {
		
	}
	
	// -------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------
	
	public List<ImpuestoEquivalencia> getListEquivalencias() {
		return listEquivalencias;
	}

	public void setListEquivalencias(List<ImpuestoEquivalencia> listEquivalencias) {
		this.listEquivalencias = listEquivalencias;
	}

	public ImpuestoEquivalencia getPojoEquivalencia() {
		return pojoEquivalencia;
	}

	public void setPojoEquivalencia(ImpuestoEquivalencia pojoEquivalencia) {
		this.pojoEquivalencia = pojoEquivalencia;
	}

	public ImpuestoEquivalencia getPojoEquivalenciaBorrar() {
		return pojoEquivalenciaBorrar;
	}

	public void setPojoEquivalenciaBorrar(
			ImpuestoEquivalencia pojoEquivalenciaBorrar) {
		this.pojoEquivalenciaBorrar = pojoEquivalenciaBorrar;
	}

	public List<SelectItem> getListTiposEquivalencias() {
		return listTiposEquivalencias;
	}

	public void setListTiposEquivalencias(List<SelectItem> listTiposEquivalencias) {
		this.listTiposEquivalencias = listTiposEquivalencias;
	}

	public List<SelectItem> getListTiposBusqueda() {
		return listTiposBusqueda;
	}

	public void setListTiposBusqueda(List<SelectItem> listTiposBusqueda) {
		this.listTiposBusqueda = listTiposBusqueda;
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

	public int getPaginacion() {
		return paginacion;
	}

	public void setPaginacion(int paginacion) {
		this.paginacion = paginacion;
	}

	public List<SelectItem> getListImpuestosItems() {
		return listImpuestosItems;
	}

	public void setListImpuestosItems(List<SelectItem> listImpuestosItems) {
		this.listImpuestosItems = listImpuestosItems;
	}

	public List<SelectItem> getListImpuestosContablesItems() {
		return listImpuestosContablesItems;
	}

	public void setListImpuestosContablesItems(
			List<SelectItem> listImpuestosContablesItems) {
		this.listImpuestosContablesItems = listImpuestosContablesItems;
	}

	public boolean isOperacion() {
		return operacion;
	}

	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}

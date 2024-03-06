package net.giro.adp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
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

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraContratos;
import net.giro.adp.logica.ObraContratosRem;
import net.giro.adp.logica.ObraRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="contratoAction")
public class ObraContratosAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ObraContratosAction.class);
	private InitialContext ctx; 
	private LoginManager loginManager; 
	// Obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	// Obras Contratos
	private ObraContratosRem ifzContratos;
	private List<ObraContratos> listObraContratos;
	private ObraContratos pojoObraContrato;
	private ObraContratos pojoObraContratoBorrar;
	private int paginaObraContratos;
	// Subcontratantes
	private ConGrupoValores pojoGpoSubcontratantes;
	private List<ConValores> listSubcontratantes;
	private List<SelectItem> listSubcontratantesItems;
	private long subcontratanteId;
	// ConValores
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	// Busqueda
	private List<SelectItem> busquedaOpciones;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaTipo;
	private int busquedaPaginas;
	private boolean busquedaAdministrativas;
	// Variables de control
	private boolean incompleto;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
    private long usuarioId;
    private boolean permiteModificar;
	// PERMISOS
	private EmpleadoRem ifzEmpleados;
	private boolean permisoAgregar;
	private boolean permisoEditar;
	private boolean permisoBorrar;
	private boolean permisoImprimir;
    // PERFILES
	private boolean PERFIL_ADMINISTRATIVO;
	
	public ObraContratosAction() {
		PropertyResourceBundle entornoProperties = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		String resValue = "";
		long valGpo = 0;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();

			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzContratos = (ObraContratosRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraContratosFac!net.giro.adp.logica.ObraContratosRem");
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());

			this.ifzObras.showSystemOuts(false);
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzContratos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("*", "Coincidencia"));
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "ID"));
			this.busquedaCampo = busquedaOpciones.get(0).getValue().toString();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			
			// GRUPO SUBCONTRATANTE
			if (entornoProperties.getString("SYS_SUBCONTRATANTE") == null || "".equals(entornoProperties.getString("SYS_SUBCONTRATANTE")) ) {
				throw new Exception("No se encontro encontro el grupo SYS_SUBCONTRATANTE en con_grupo_valores");
			} else {
				valGpo = Long.valueOf(entornoProperties.getString("SYS_SUBCONTRATANTE"));
				this.pojoGpoSubcontratantes = this.ifzGpoVal.findById(valGpo);
				if (this.pojoGpoSubcontratantes == null) throw new Exception("No se encontro encontro el grupo SYS_SUBCONTRATANTE en con_grupo_valores");
			}
			
			cargaSubcontratantes();
			this.paginaObraContratos = 1;
		} catch (Exception e) {
			this.ctx = null;
			log.error("Error en constructor GestionProyectos.ContratosObrasAction", e);
		} finally {
			establecerPermisos();
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = "nombre";
			
			if (this.listObras != null)
				this.listObras.clear();

			this.busquedaTipo = 0;
			if (this.busquedaAdministrativas)
				this.busquedaTipo = 4;

			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo, false, "", 0);
			if (this.listObras.isEmpty()) {
				control(true, 2, "ERROR");
				return;
			}
		} catch(Exception e) {
			control(true, 1, e.getMessage(), e);
    		log.error("Error en GestionProyectos.ContratosObrasAction.buscar", e);
    	}
	}
	
	public void ver() {
		try {
			control();
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}
    		log.info("Validando permiso de escritura");
    		validarPermisos();
			
			this.permiteModificar = true;
			if(this.pojoObra.getEstatus().equals(10000798L)) 
				this.permiteModificar = false;

			if(this.listObraContratos == null)
				this.listObraContratos = new ArrayList<ObraContratos>();
			this.listObraContratos.clear();
			this.paginaObraContratos = 1;

			this.ifzContratos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzContratos.orderBy("nombreSubcontratante");
			this.listObraContratos = this.ifzContratos.findByProperty("idObra.id", this.pojoObra.getId(), 0);
		} catch(Exception e) {
			control(true, 1, e.getMessage(), e);
    		log.error("Error en GestionProyectos.ContratosObrasAction.ver", e);
    	}
	}
	
	public void nuevo() {
		try {
			control();
			this.pojoObraContrato = new ObraContratos();
			this.pojoObraContratoBorrar = null;
			cargaSubcontratantes();
		} catch(Exception e) {
			control(true, 1, e.getMessage(), e);
    		log.error("Error en GestionProyectos.ContratosObrasAction.nuevo", e);
    	}
	}
	
	public void editar() {
		try {
			control();
    		log.info("Validando permiso de escritura");
    		validarPermisos();
			this.pojoObraContratoBorrar = null;
			cargaSubcontratantes();
			this.subcontratanteId = this.pojoObraContrato.getIdSubcontratante();
		} catch(Exception e) {
			control(true, 1, e.getMessage(), e);
    		log.error("Error en GestionProyectos.ContratosObrasAction.editar", e);
    	}
	}
	
	public void guardar() {
		try {
			control();
			if (! this.validarObraContrato())
				return;
			
			// Empresa subcontratante
			this.pojoObraContrato.setIdSubcontratante(this.subcontratanteId);
			for (ConValores var : this.listSubcontratantes) {
				if (this.subcontratanteId == var.getId()) {
					this.pojoObraContrato.setNombreSubcontratante(var.getValor());
					break;
				}
			}
			
			this.pojoObraContrato.setIdObra(this.ifzObras.findById(this.pojoObra.getId()));
			this.pojoObraContrato.setModificadoPor(this.usuarioId);
			this.pojoObraContrato.setFechaModificacion(Calendar.getInstance().getTime());
				
			// Guardamos o actualizamos en la BD segun corresponda
			this.ifzContratos.setInfoSesion(this.loginManager.getInfoSesion());
			if (this.pojoObraContrato.getId() == null || this.pojoObraContrato.getId() <= 0L) {
				this.pojoObraContrato.setCreadoPor(this.usuarioId);
				this.pojoObraContrato.setFechaCreacion(Calendar.getInstance().getTime());
				
				this.pojoObraContrato.setId(this.ifzContratos.save(this.pojoObraContrato));
			} else {
				this.ifzContratos.update(this.pojoObraContrato);
			}
			
			// Recargamos el listado de contratos
			ver();
        	nuevo();
		} catch(Exception e) {
			control(true, 1, e.getMessage(), e);
    		log.error("Error en GestionProyectos.ContratosObrasAction.guardar", e);
    	}
	}
	
	public void eliminar() {
		try {
			control();

			if(this.pojoObraContratoBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoObraContratoBorrar.getId() != null && this.pojoObraContratoBorrar.getId() > 0L)
					this.ifzContratos.delete(this.pojoObraContratoBorrar.getId());
				
				// Borramos de la lista
				this.listObraContratos.remove(this.pojoObraContratoBorrar);
			}
	    	
	    	nuevo();
		} catch(Exception e) {
			control(true, 1, e.getMessage(), e);
    		log.error("Error en GestionProyectos.ContratosObrasAction.eliminar", e);
    	}
	}
    
	public boolean cargaSubcontratantes() {
    	try {
    		control();
			this.subcontratanteId = 0L;
			if (this.listSubcontratantes == null)
				this.listSubcontratantes = new ArrayList<ConValores>();
			this.listSubcontratantes.clear();

			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSubcontratantes = this.ifzConValores.findAll(this.pojoGpoSubcontratantes);
			if (this.listSubcontratantes.isEmpty()) {
	    		control(true, -1, "No se encontro ningun Subcontratante.");
				return false;
			}

			if (this.listSubcontratantesItems == null)
				this.listSubcontratantesItems = new ArrayList<SelectItem>();
			this.listSubcontratantesItems.clear();
			
			for (ConValores var : this.listSubcontratantes) {
				this.listSubcontratantesItems.add(new SelectItem(var.getId(), var.getValor()));
			}
    	} catch (Exception e) {
    		control(true, 1, e.getMessage(), e);
    		log.error("Error en GestionProyectos.ContratosObrasAction.cargaSubcontratantes", e);
    		return false;
    	}
    	
    	return true;
    }
	
	public boolean validarObraContrato() {
		if (this.pojoObraContrato == null) {
			log.error("ERROR INTERNO: POJO ObraContrato nulo!");
			control(true, -1, "Ocurrio un problema al intentar Guardar el Contrato.\nConsulte a su Administrador.");
			return false;
		}
		
		if(this.pojoObraContrato.getMonto().doubleValue() <= 0) {
			log.error("Error en salvarObraContrato: " + this.mensaje);
			control(true, 8, "Monto menor o igual a cero");
			return false;
		}
		
		if (validarNumero(this.pojoObraContrato.getMonto().toString(), 10, 2)) {
			log.error("Error en salvarObraContrato: " + this.mensaje);
			control(true, -1, "El monto ingresado no es valido\nDebe ser de maximo 10 digitos con maximo 2 decimales");
			return false;
		}
		
		if(this.pojoObraContrato.getIva().doubleValue() <= 0) {
			log.error("Error en salvarObraContrato: " + this.mensaje);
			control(true, 9, "IVA menor o igual a cero");
			return false;
		}
		
		if (validarNumero(this.pojoObraContrato.getIva().toString(), 8, 2)) {
			log.error("Error en salvarObraContrato: " + this.mensaje);
			control(true, -1, "El Iva ingresado no es valido\nDebe ser de maximo 8 digitos con maximo 2 decimales");
			return false;
		}
		
		return true;
	}
	
	public boolean validarNumero(String number, int digitos, int decimales) {
		number = number.replace(",", "");
		
		if (number.contains(".")) {
			if (number.length() > digitos)
				return false;

			if (number.length() > decimales)
				return false;
		} else {
			if (number.length() > digitos)
				return false;
		}
		
		
		return true;
	}

	private void control() {
		control(false, 0, "", null);
	}

	private void control(boolean procesoInterrumpido, int tipo, String mensaje) {
		control(procesoInterrumpido, tipo, mensaje, null);
	}

	private void control(boolean val1, int val2, String val3, Throwable t) {
		this.incompleto = val1;
		this.tipoMensaje = val2;
		this.mensaje = val3;
		
		if (this.mensaje != null && this.mensaje.contains("\n"))
			this.mensaje = this.mensaje.replace("\n", "<br>");
		
		this.mensajeDetalles = "";
		if (t != null) {
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public boolean getOperacion() {
		return incompleto;
	}

	public void setOperacion(boolean incompleto) {
		this.incompleto = incompleto;
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
	
	public List<SelectItem> getBusquedaOpciones() {
		return busquedaOpciones;
	}

	public void setBusquedaOpciones(List<SelectItem> busquedaOpciones) {
		this.busquedaOpciones = busquedaOpciones;
	}

	public String getBusquedaCampo() {
		return busquedaCampo;
	}

	public void setBusquedaCampo(String busquedaCampo) {
		this.busquedaCampo = busquedaCampo;
	}

	public String getBusquedaValor() {
		return busquedaValor;
	}

	public void setBusquedaValor(String busquedaValor) {
		this.busquedaValor = busquedaValor;
	}

	public int getBusquedaTipo() {
		return busquedaTipo;
	}

	public void setBusquedaTipo(int busquedaTipo) {
		this.busquedaTipo = busquedaTipo;
	}
	
	public int getBusquedaPaginas() {
		return busquedaPaginas;
	}

	public void setBusquedaPaginas(int busquedaPaginas) {
		this.busquedaPaginas = busquedaPaginas;
	}

	public List<Obra> getListObras() {
		return listObras;
	}
	
	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}
	
	public Obra getPojoObra() {
		return pojoObra;
	}
	
	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public List<ObraContratos> getListObraContratos() {
		return listObraContratos;
	}

	public void setListObraContratos(List<ObraContratos> listObraContratos) {
		this.listObraContratos = listObraContratos;
	}

	public ObraContratos getPojoObraContrato() {
		if (pojoObraContrato == null) pojoObraContrato = new ObraContratos();
		return pojoObraContrato;
	}

	public void setPojoObraContrato(ObraContratos pojoObraContrato) {
		this.pojoObraContrato = pojoObraContrato;
	}

	public ObraContratos getPojoObraContratoBorrar() {
		return pojoObraContratoBorrar;
	}

	public void setPojoObraContratoBorrar(ObraContratos pojoObraContratoBorrar) {
		this.pojoObraContratoBorrar = pojoObraContratoBorrar;
	}

	public int getPaginaObraContratos() {
		return paginaObraContratos;
	}

	public void setPaginaObraContratos(int paginaObraContratos) {
		this.paginaObraContratos = paginaObraContratos;
	}
	
	public List<SelectItem> getListSubcontratantesItems() {
		return listSubcontratantesItems;
	}

	public void setListSubcontratantesItems(List<SelectItem> listSubcontratantesItems) {
		this.listSubcontratantesItems = listSubcontratantesItems;
	}

	public long getSubcontratanteId() {
		return subcontratanteId;
	}

	public void setSubcontratanteId(long subcontratanteId) {
		this.subcontratanteId = subcontratanteId;
	}
	
	public boolean isPermiteModificar() {
		return permiteModificar;
	}
	
	public void setPermiteModificar(boolean permiteModificar) {
		this.permiteModificar = permiteModificar;
	}

	public boolean getPerfilAdministrativo() {
		return PERFIL_ADMINISTRATIVO;
	}
	
	public void setPerfilAdministrativo(boolean perfilAdministrativo) {
		PERFIL_ADMINISTRATIVO = perfilAdministrativo;
	}

	public boolean getBusquedaAdministrativas() {
		return busquedaAdministrativas;
	}

	public void setBusquedaAdministrativas(boolean busquedaAdministrativas) {
		this.busquedaAdministrativas = busquedaAdministrativas;
	}

	// ----------------------------------------------------------------------
	// PERMISOS
	// ----------------------------------------------------------------------

    private void establecerPermisos() {
    	this.permisoAgregar = true;
    	this.permisoEditar = true;
    	this.permisoBorrar = true;
    	this.permisoImprimir = true;
    }
    
    private void validarPermisos() {
    	this.permisoAgregar = true;
    	this.permisoEditar = true;
    	this.permisoBorrar = true;
    	this.permisoImprimir = true;
    }
    
	public boolean isPermisoEscritura() {
		return true;
	}

	public void setPermisoEscritura(boolean permisoAgregar) {}

	public boolean isPermisoAgregar() {
		return permisoAgregar;
	}

	public void setPermisoAgregar(boolean permisoAgregar) {
		this.permisoAgregar = permisoAgregar;
	}

	public boolean isPermisoEditar() {
		return permisoEditar;
	}

	public void setPermisoEditar(boolean permisoEditar) {
		this.permisoEditar = permisoEditar;
	}

	public boolean isPermisoBorrar() {
		return permisoBorrar;
	}

	public void setPermisoBorrar(boolean permisoBorrar) {
		this.permisoBorrar = permisoBorrar;
	}

	public boolean isPermisoImprimir() {
		return permisoImprimir;
	}

	public void setPermisoImprimir(boolean permisoImprimir) {
		this.permisoImprimir = permisoImprimir;
	}
}

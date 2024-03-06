package net.giro.adp;

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

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraContratos;
import net.giro.adp.logica.ObraContratosRem;
import net.giro.adp.logica.ObraRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;

@ViewScoped
@ManagedBean(name="contratoAction")
public class ContratosObrasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ContratosObrasAction.class);
	private InitialContext ctx; 
	private LoginManager loginManager; 
	private PropertyResourceBundle entornoProperties;
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
	// Variables de control
	private boolean incompleto;
	private int tipoMensaje;
	private String mensaje;
    private long usuarioId;
    private boolean permiteModificar;
    private double valObraCancelada;
	
	public ContratosObrasAction() {
		long valGpo = 0;
		
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzContratos = (ObraContratosRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraContratosFac!net.giro.adp.logica.ObraContratosRem");
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
			// Inicializaciones
			this.usuarioId = 0;
			//this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = (long) this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			//this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			//this.porcentajeIva = Double.valueOf(this.loginManager.getAutentificacion().getPerfil("VALOR_IVA"));
			this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());

			this.ifzObras.showSystemOuts(false);
			this.ifzContratos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzContratos.showSystemOuts(false);
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "Clave"));
			this.busquedaCampo = busquedaOpciones.get(0).getDescription();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			
			// GRUPO SUBCONTRATANTE
			if ( entornoProperties.getString("SYS_SUBCONTRATANTE") == null || "".equals(entornoProperties.getString("SYS_SUBCONTRATANTE")) ) {
				throw new Exception("No se encontro encontro el grupo SYS_SUBCONTRATANTE en con_grupo_valores");
			} else {
				valGpo = Long.valueOf(entornoProperties.getString("SYS_SUBCONTRATANTE"));
				this.pojoGpoSubcontratantes = this.ifzGpoVal.findById(valGpo);
				if (this.pojoGpoSubcontratantes == null) throw new Exception("No se encontro encontro el grupo SYS_SUBCONTRATANTE en con_grupo_valores");
			}
			
			cargaSubcontratantes();
			
			this.valObraCancelada = 10000798L;
			this.paginaObraContratos = 1;
		} catch (Exception e) {
			this.ctx = null;
			log.error("Error en constructor GestionProyectos.ContratosObrasAction", e);
		}
	}
	
	
	private void control(boolean procesoInterrumpido, int tipo, String mensaje) {
		this.incompleto = procesoInterrumpido;
		this.tipoMensaje = tipo;
		this.mensaje = mensaje;
	}

	public void buscar() {
		try {
			control(false, 0, "");
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = "nombre";
			
			if (this.listObras != null)
				this.listObras.clear();

			//this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo);
			if (this.listObras.isEmpty()) {
				control(true, 2, "ERROR");
				return;
			}
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.ContratosObrasAction.buscar", e);
    	}
	}
	
	public void ver() {
		try {
			control(false, 0, "");
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}
			
			this.permiteModificar = true;
			if(this.pojoObra.getEstatus().equals(10000798L)) 
				this.permiteModificar = false;

			if(this.listObraContratos == null)
				this.listObraContratos = new ArrayList<ObraContratos>();
			this.listObraContratos.clear();
			this.paginaObraContratos = 1;
			
			this.ifzContratos.orderBy("nombreSubcontratante");
			this.listObraContratos = this.ifzContratos.findByProperty("idObra.id", this.pojoObra.getId(), 0);
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.ContratosObrasAction.ver", e);
    	}
	}
	
	public void nuevo() {
		try {
			control(false, 0, "");

			this.pojoObraContrato = new ObraContratos();
			this.pojoObraContratoBorrar = null;
			
			cargaSubcontratantes();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.ContratosObrasAction.nuevo", e);
    	}
	}
	
	public void editar() {
		try {
			control(false, 0, "");

			this.pojoObraContratoBorrar = null;
			cargaSubcontratantes();
			
			this.subcontratanteId = this.pojoObraContrato.getIdSubcontratante();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.ContratosObrasAction.editar", e);
    	}
	}
	
	public void guardar() {
		try {
			control(false, 0, "");

			if (this.pojoObraContrato != null) {
				if (this.validarObraContrato())
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
				if (this.pojoObraContrato.getId() == null || this.pojoObraContrato.getId() <= 0L) {
					this.pojoObraContrato.setCreadoPor(this.usuarioId);
					this.pojoObraContrato.setFechaCreacion(Calendar.getInstance().getTime());
					
					this.pojoObraContrato.setId(this.ifzContratos.save(this.pojoObraContrato));
				} else {
					this.ifzContratos.update(this.pojoObraContrato);
				}
				
				// Recargamos el listado de contratos
				ver();
			}
        	
        	nuevo();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.ContratosObrasAction.guardar", e);
    	}
	}
	
	public void eliminar() {
		try {
			control(false, 0, "");

			if(this.pojoObraContratoBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoObraContratoBorrar.getId() != null && this.pojoObraContratoBorrar.getId() > 0L)
					this.ifzContratos.delete(this.pojoObraContratoBorrar.getId());
				
				// Borramos de la lista
				this.listObraContratos.remove(this.pojoObraContratoBorrar);
			}
	    	
	    	nuevo();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.ContratosObrasAction.eliminar", e);
    	}
	}
    
	public boolean cargaSubcontratantes() {
    	try {
    		control(false, 0, "");
			
			this.subcontratanteId = 0L;
			if (this.listSubcontratantes == null)
				this.listSubcontratantes = new ArrayList<ConValores>();
			this.listSubcontratantes.clear();
			
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
    		control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.ContratosObrasAction.cargaSubcontratantes", e);
    		return false;
    	}
    	
    	return true;
    }
	
	public boolean validarObraContrato() {
		if(this.pojoObraContrato.getMonto().doubleValue() <= 0) {
			control(true, 8, "Monto menor o igual a cero");
			log.error("Error en salvarObraContrato: " + this.mensaje);
			return false;
		}
		
		if (validarNumero(this.pojoObraContrato.getMonto().toString(), 10, 2)) {
			control(true, -1, "El monto ingresado no es valido\nDebe ser de maximo 10 digitos con maximo 2 decimales");
			log.error("Error en salvarObraContrato: " + this.mensaje);
			return false;
		}
		
		if(this.pojoObraContrato.getIva().doubleValue() <= 0) {
			control(true, 9, "IVA menor o igual a cero");
			log.error("Error en salvarObraContrato: " + this.mensaje);
			return false;
		}
		
		if (validarNumero(this.pojoObraContrato.getIva().toString(), 8, 2)) {
			control(true, -1, "El Iva ingresado no es valido\nDebe ser de maximo 8 digitos con maximo 2 decimales");
			log.error("Error en salvarObraContrato: " + this.mensaje);
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
	
	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

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

	public double getValObraCancelada() {
		return valObraCancelada;
	}

	public void setValObraCancelada(double valObraCancelada) {
		this.valObraCancelada = valObraCancelada;
	}
}

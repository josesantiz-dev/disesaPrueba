package net.giro.rh.admon.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.beans.EmpleadoFiniquitoExt;
import net.giro.rh.admon.logica.EmpleadoFiniquitoRem;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="finiquitoAction")
public class FiniquitoAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FiniquitoAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	private long usuarioId;
	private String usuario;
	private boolean operacionCompleta;
	private int tipoMensaje;
	private String mensaje;
	private String valorBusqueda;
	private int numPagina;
	private EmpleadoFiniquitoRem ifzFiniquito;
	private EmpleadoFiniquitoExt pojoFiniquito;
	private EmpleadoFiniquitoExt pojoFiniquitoBorrar;
	private List<EmpleadoFiniquitoExt> listaFiniquitosGrid;
	private EmpleadoExt pojoEmpleado;
	private List<Empleado> listaSolicitadoPor;
	private List<SelectItem> listaCboSolicitadoPor;
	private boolean activarEmpleado;
	// Busqueda empleados
	private EmpleadoRem ifzEmpleado;
	private List<Empleado> listaEmpleadosGrid;
	private Empleado pojoEmpleadoBusqueda;
	private List<SelectItem> tiposBusquedaEmpleados;
	private String campoBusquedaEmpleados;
	private String valorBusquedaEmpleados;
	private int numPaginaEmpleados;
	private int tipoBusquedaEmpleados;
	// Perfiles
	private boolean permiteVoBo;
	private boolean permiteAprobar;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	
	public FiniquitoAction() throws NamingException,Exception {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		String valPerfil = "";
		Map<String, String> params = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			params = fc.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());			
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
	
			this.ctx = new InitialContext();
			this.ifzFiniquito = (EmpleadoFiniquitoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFiniquitoFac!net.giro.rh.admon.logica.EmpleadoFiniquitoRem");
			this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			
			this.ifzFiniquito.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			
			// PERFILES
			this.permiteVoBo = false;
			valPerfil = this.loginManager.getPerfil("FINIQUITOS_VOBO");
			if (valPerfil != null && "S".equals(valPerfil.trim().toUpperCase()))
				this.permiteVoBo = true;

			this.permiteAprobar = false;
			valPerfil = this.loginManager.getPerfil("FINIQUITOS_APROBACION");
			if (valPerfil != null && "S".equals(valPerfil.trim().toUpperCase()))
				this.permiteAprobar = true;
			
			this.pojoFiniquito = new EmpleadoFiniquitoExt();
			this.listaFiniquitosGrid = new ArrayList<>();
			this.listaEmpleadosGrid = new ArrayList<>();
			this.operacionCompleta = false;
			this.numPagina = 1;
			
			// Busqueda Empleados
			this.tiposBusquedaEmpleados = new ArrayList<SelectItem>();
			this.tiposBusquedaEmpleados.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaEmpleados.add(new SelectItem("clave", "Clave"));
			this.tiposBusquedaEmpleados.add(new SelectItem("id", "ID"));
			this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();
			this.valorBusquedaEmpleados = "";
			this.numPaginaEmpleados = 1;
			
			cargarListaSolicitadoPor();
			cargarListaCboSolicitadoPor();
		} catch (Exception e) {
			log.error("Ocurrio un problema, no se pudo intancias FiniquitoAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ( "".equals(this.valorBusqueda) ){
				this.listaFiniquitosGrid = this.ifzFiniquito.findAllExt();
			} else {
				this.listaFiniquitosGrid = this.ifzFiniquito.findByEmpleadoExt(this.valorBusqueda);	//busqueda por nombre de empleado
			}
			
			// Comprobamos resultados
			if (this.listaFiniquitosGrid.isEmpty()) {
				control(2);
				return;
			}
			
			Collections.sort(this.listaFiniquitosGrid, new Comparator<EmpleadoFiniquitoExt>() {
				@Override
				public int compare(EmpleadoFiniquitoExt o1, EmpleadoFiniquitoExt o2) {
					return o2.getId().compareTo(o1.getId());
				}
				
			});
		} catch(Exception e) {
			control(e);
		}
	}

	public void nuevo() {
		control();
		this.pojoFiniquito = new EmpleadoFiniquitoExt();
		this.pojoEmpleadoBusqueda = new Empleado();
	}

	public void editar() {
		control();
		this.operacionCompleta = true;
	}
	
	public void guardar(){
		try {
			control();
			if (! validaGuardarFiniquito())
				return;
			
			if (this.pojoFiniquito.getId() == null) {
				this.pojoFiniquito.setCreadoPor(this.usuarioId);
				this.pojoFiniquito.setFechaCreacion(Calendar.getInstance().getTime());
				
				//actualizar el estatus del empleado a baja --> 2 cuando Aprobacion{estatus} sea true
				if( this.pojoFiniquito.getEstatus() == 1 )
					bajaEmpleado(this.pojoFiniquito.getIdEmpleado().getId());
				
				this.pojoFiniquito.setId(this.ifzFiniquito.save(this.pojoFiniquito));
				this.listaFiniquitosGrid.add(0, this.pojoFiniquito);
			} else {
				if (this.pojoFiniquito.getEstatus() == 1)
					bajaEmpleado(this.pojoFiniquito.getIdEmpleado().getId());
				
				this.ifzFiniquito.update(this.pojoFiniquito);				
			}
			log.info("Guardado");
		} catch (Exception e) {
			control(e);
		}
	}

	public void eliminar() {
		List<EmpleadoFiniquito> listFiniquitos = null;
				
		try {
			control();
			// Comprobamos finiquito
			if (this.pojoFiniquitoBorrar == null || this.pojoFiniquitoBorrar.getId() == null || this.pojoFiniquitoBorrar.getId() <= 0L)
				return;
			
			if (! "ADMINISTRADOR".equals(this.usuario)) {
				log.warn("Temporalmente solo el ADMIN puede eliminar FINIQUITOS!");
				return;
			}
			
			// Borramos finiquito
			log.info("Borrando finiquito " + this.pojoFiniquitoBorrar.getId() + " ... ");
			this.ifzFiniquito.delete(this.pojoFiniquitoBorrar);
			this.listaFiniquitosGrid.remove(this.pojoFiniquitoBorrar);
			log.info("Finiquito borrado!.");
			
			// Comprobamos otros finiquitos para activar a Empleado si corresponde
			listFiniquitos = this.ifzFiniquito.findByProperty("idEmpleado", this.pojoFiniquitoBorrar.getIdEmpleado().getId());
			if (listFiniquitos == null || listFiniquitos.isEmpty()) {
				// Activamos empleado
				log.info("Activando empleado " + this.pojoFiniquitoBorrar.getIdEmpleado().getId() + " ... ");
				this.pojoFiniquitoBorrar.getIdEmpleado().setEstatus(0);
				this.ifzEmpleado.update(this.pojoFiniquitoBorrar.getIdEmpleado());
				log.info("Empleado activado!");
			}
		} catch(Exception e) {
			control("Ocurrio un problema al intentar borrar el Finiquito seleccionado", e);
		}
	}
	
	private void cargarListaSolicitadoPor() {
		listaSolicitadoPor = this.ifzEmpleado.findAllActivos();
	}
	
	private void cargarListaCboSolicitadoPor() {
		listaCboSolicitadoPor = null;
		this.listaCboSolicitadoPor = null;
		this.listaCboSolicitadoPor = new ArrayList<SelectItem>();
		
		for (Empleado e : this.listaSolicitadoPor) {
			this.listaCboSolicitadoPor.add(new SelectItem(e.getId().toString(), e.getNombre()));
		}
	}

	public void bajaEmpleado(long idEmpleado){
		try {
			Empleado e = this.ifzEmpleado.findById(idEmpleado);
			e.setEstatus(2);
			this.ifzEmpleado.update(e);
		} catch (Exception e) {
			control("Error dando de baja al empleado", e);
		}
	}
	
	private boolean validaGuardarFiniquito() {
		/*if (this.pojoFiniquito.getCreadoPor() > 0L && this.pojoFiniquito.getCreadoPor() != this.usuarioId) {
			control("No puede modificar registro.\nUsted no es el propietario del registro");
			return false;
		}*/

		if (this.pojoFiniquito.getIdEmpleado() == null) {
			control("Debe seleccionar empleado");
			return false;
		}
		
		if (this.pojoFiniquito.getFechaSolicitudBaja() == null) {
			control("Debe indicar fecha de Solicitud de Baja");
			return false;
		}
		
		if (this.pojoFiniquito.getFechaElaboracionEnvio() != null) {
			if (this.pojoFiniquito.getFechaSolicitudBaja().compareTo(this.pojoFiniquito.getFechaElaboracionEnvio()) > 0) {
				control("La Fecha Elaboracion y Envío Finiquito debe ser mayor o igual a la Fecha Solicitud Baja");
				return false;
			}
		}
		
		if (this.pojoFiniquito.getMonto() <= 0) {
			if (this.pojoFiniquito.getVoBoRh() == 1) {
				control("Ingrese el monto del finiquito");
				return false;
			}
		}
		
		// Visto Bueno
		vistoBueno();
		
		// Aprobacion
		aprobacion();
		
		this.pojoFiniquito.setModificadoPor(this.usuarioId);
		this.pojoFiniquito.setFechaModificacion(Calendar.getInstance().getTime());
		
		return true;
	}
	
	private void vistoBueno() {
		if (this.pojoFiniquito != null) {
			if (this.pojoFiniquito.getVoBoRh() == 1 && (this.pojoFiniquito.getVoBoRhPor() == null || this.pojoFiniquito.getVoBoRhPor() <= 0L)) {
				this.pojoFiniquito.setVoBoRhPor(this.usuarioId);
				this.pojoFiniquito.setVoBoRhFecha(Calendar.getInstance().getTime());
			}
		}
	}
	
	private void aprobacion() {
		if (this.pojoFiniquito != null) {
			if (this.pojoFiniquito.getAprobacion() == 1 && (this.pojoFiniquito.getAprobacionPor() == null || this.pojoFiniquito.getAprobacionPor() <= 0L)) {
				this.pojoFiniquito.setAprobacionPor(this.usuarioId);
				this.pojoFiniquito.setAprobacionFecha(Calendar.getInstance().getTime());
			}
		}
	}
	
	private void control() {
		control(true, 0, "", null);
	}

	private void control(int tipoMensaje) {
		control(false, tipoMensaje, null, null);
	}

	private void control(String mensaje) {
		control(false, -1, mensaje, null);
	}

	private void control(Throwable t) {
		control(false, 1, "Ocurrio un problema al realizar la operacion.\nContacte a su Administrador.\nCodigo 501", t);
	}

	private void control(String mensaje, Throwable t) {
		control(false, 1, mensaje, t);
	}
	
	private void control(boolean operacionCompleta, int tipoMensaje, String mensaje, Throwable t) {
		if (mensaje == null)
			mensaje = "";
		
		this.operacionCompleta = operacionCompleta;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.trim().contains("\n") ? mensaje.trim().replace("\n", "<br>") : mensaje.trim();
		
		if (t != null) 
			log.error("ERROR INTERNO - " + this.mensaje, t);
	}

	// ----------------------------------------------------------------------------------------------
	// BUSQUEDA EMPLEADOS
	// ----------------------------------------------------------------------------------------------
	
	public void nuevaBusquedaEmpleados() {
		control();
		if (this.listaEmpleadosGrid == null)
			this.listaEmpleadosGrid = new ArrayList<Empleado>();
		this.listaEmpleadosGrid.clear();
		this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();
		this.valorBusquedaEmpleados = "";
		this.numPaginaEmpleados = 1;
		this.tipoBusquedaEmpleados = 1;
	}
	
	public void buscarEmpleados() {
		try {
			control();
			if (this.listaEmpleadosGrid == null)
				this.listaEmpleadosGrid = new ArrayList<Empleado>();
			this.listaEmpleadosGrid.clear();
			
			this.listaEmpleadosGrid = this.ifzEmpleado.findLike(this.valorBusquedaEmpleados, "nombre", 0);
			if (this.listaEmpleadosGrid == null || this.listaEmpleadosGrid.isEmpty()) {
				control(2);
				return;
			}
		} catch (Exception e) {
			control(e);
		}
	}
	
	public void seleccionarEmpleado() {
		nuevaBusquedaEmpleados();
	}
	
	// ----------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------------------
	
	public boolean getFechaBloqueada() {
		if (this.pojoFiniquito != null && this.pojoFiniquito.getId() != null && this.pojoFiniquito.getId() > 0L)
			return true;
		return false;
	}
	
	public void setFechaBloqueada(boolean value) {}
	
	public String getTitulo() {
		if (this.pojoFiniquito != null && this.pojoFiniquito.getId() != null && this.pojoFiniquito.getId() > 0L)
			return "Finiquito " + this.pojoFiniquito.getId();
		return "Nuevo Finiquito";
	}
	
	public void setTitulo(String value) { }
	
	public int getNivelAcceso() {
		if (this.permiteAprobar && this.permiteVoBo)
			return 3;
		if (this.permiteAprobar)
			return 2;
		if (this.permiteVoBo)
			return 1;
		return 0;
	}
	
	public void setNivelAcesso(int nivel) {}
	
	public String getEmpleado() {
		if (this.pojoFiniquito != null && this.pojoFiniquito.getIdEmpleado() != null && this.pojoFiniquito.getIdEmpleado().getId() != null && this.pojoFiniquito.getIdEmpleado().getId() > 0L)
			return this.pojoFiniquito.getIdEmpleado().getId() + " - " + this.pojoFiniquito.getIdEmpleado().getNombre();
		return "";
	}
	
	public void setEmpleado(String value) {}
	
	public long getSolicitadoPor(){
		return this.pojoFiniquito.getSolicitadoPor();
	}
	
	public void setSolicitadoPor(long idEmpleado){
		this.pojoFiniquito.setSolicitadoPor( idEmpleado );
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

	public boolean isOperacionCompleta() {
		return operacionCompleta;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCompleta = operacionCompleta;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public EmpleadoFiniquitoExt getPojoFiniquito() {
		return pojoFiniquito;
	}

	public void setPojoFiniquito(EmpleadoFiniquitoExt pojoFiniquito) {
		this.pojoFiniquito = pojoFiniquito;
	}

	public List<EmpleadoFiniquitoExt> getListaFiniquitosGrid() {
		return listaFiniquitosGrid;
	}

	public void setListaFiniquitosGrid(List<EmpleadoFiniquitoExt> listaFiniquitosGrid) {
		this.listaFiniquitosGrid = listaFiniquitosGrid;
	}

	public EmpleadoExt getPojoEmpleado() {
		return pojoEmpleado;
	}

	public void setPojoEmpleado(EmpleadoExt pojoEmpleado) {
		this.pojoEmpleado = pojoEmpleado;
	}

	public boolean getFirmaRenuncia(){
		return this.pojoFiniquito.getFirmaRenuncia() == 0 ? false : true;
	}
	
	public void setFirmaRenuncia(boolean firmaRenuncia) {
		this.pojoFiniquito.setFirmaRenuncia((firmaRenuncia) ? 1 : 0);
	}
	
	public boolean getVoBoRH(){
		return this.pojoFiniquito.getVoBoRh() == 1 ? true : false;
	}

	public void setVoBoRH(boolean voBoRh) {
		this.pojoFiniquito.setVoBoRh((voBoRh) ? 1 : 0);
	}
	
	public boolean getAprobacion(){
		return this.pojoFiniquito.getAprobacion() == 1 ? true : false;
	}
	
	public void setAprobacion(boolean aprobacion) {
		this.pojoFiniquito.setAprobacion((aprobacion) ? 1 : 0);
	}

	public List<SelectItem> getListaCboSolicitadoPor() {
		return listaCboSolicitadoPor;
	}

	public void setListaCboSolicitadoPor(List<SelectItem> listaCboSolicitadoPor) {
		this.listaCboSolicitadoPor = listaCboSolicitadoPor;
	}
	
	public Date getFechaElaboracionEnvio(){
		return pojoFiniquito.getFechaElaboracionEnvio() == null ? Calendar.getInstance().getTime() : pojoFiniquito.getFechaElaboracionEnvio();
	}
	
	public void setFechaElaboracionEnvio(Date fechaElaboracionEnvio){
		this.pojoFiniquito.setFechaElaboracionEnvio(fechaElaboracionEnvio);
	}
	
	public Date getFechaSolicitudBaja(){
		return pojoFiniquito.getFechaSolicitudBaja() == null ? Calendar.getInstance().getTime() : pojoFiniquito.getFechaSolicitudBaja();
	}
	
	public void setFechaSolicitudBaja(Date fechaSolicitudBaja){
		this.pojoFiniquito.setFechaSolicitudBaja(fechaSolicitudBaja);
	}

	public List<SelectItem> getTiposBusquedaEmpleados() {
		return tiposBusquedaEmpleados;
	}

	public void setTiposBusquedaEmpleados(List<SelectItem> tiposBusquedaEmpleados) {
		this.tiposBusquedaEmpleados = tiposBusquedaEmpleados;
	}

	public String getCampoBusquedaEmpleados() {
		return campoBusquedaEmpleados;
	}

	public void setCampoBusquedaEmpleados(String campoBusquedaEmpleados) {
		this.campoBusquedaEmpleados = campoBusquedaEmpleados;
	}

	public String getValorBusquedaEmpleados() {
		return valorBusquedaEmpleados;
	}

	public void setValorBusquedaEmpleados(String valorBusquedaEmpleados) {
		this.valorBusquedaEmpleados = valorBusquedaEmpleados;
	}

	public int getTipoBusquedaEmpleados() {
		return tipoBusquedaEmpleados;
	}

	public void setTipoBusquedaEmpleados(int tipoBusquedaEmpleados) {
		this.tipoBusquedaEmpleados = tipoBusquedaEmpleados;
	}

	public int getNumPaginaEmpleados() {
		return numPaginaEmpleados;
	}

	public void setNumPaginaEmpleados(int numPaginaEmpleados) {
		this.numPaginaEmpleados = numPaginaEmpleados;
	}

	public List<Empleado> getListaEmpleadosGrid() {
		return listaEmpleadosGrid;
	}

	public void setListaEmpleadosGrid(List<Empleado> listaEmpleadosGrid) {
		this.listaEmpleadosGrid = listaEmpleadosGrid;
	}

	public Empleado getPojoEmpleadoBusqueda() {
		return pojoEmpleadoBusqueda;
	}

	public void setPojoEmpleadoBusqueda(Empleado pojoEmpleadoBusqueda) {
		this.pojoEmpleadoBusqueda = pojoEmpleadoBusqueda;
		this.pojoFiniquito.setIdEmpleado(this.ifzEmpleado.findByIdExt(pojoEmpleadoBusqueda.getId()));
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public boolean getPermiteVoBo() {
		return permiteVoBo;
	}

	public void setPermiteVoBo(boolean permiteVoBo) {
		this.permiteVoBo = permiteVoBo;
	}

	public boolean getPermiteAprobar() {
		return permiteAprobar;
	}

	public void setPermiteAprobar(boolean permiteAprobar) {
		this.permiteAprobar = permiteAprobar;
	}
	


	public boolean isActivarEmpleado() {
		return activarEmpleado;
	}
	


	public void setActivarEmpleado(boolean activarEmpleado) {
		this.activarEmpleado = activarEmpleado;
	}
	
	public boolean isDebugging() { 
		if (this.paramsRequest.containsKey("DEBUG")) 
			return true;
		return this.isDebug;
	}
	
	public void setDebugging(boolean isDebug) { }


	public EmpleadoFiniquitoExt getPojoFiniquitoBorrar() {
		return pojoFiniquitoBorrar;
	}


	public void setPojoFiniquitoBorrar(EmpleadoFiniquitoExt pojoFiniquitoBorrar) {
		this.pojoFiniquitoBorrar = pojoFiniquitoBorrar;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 * VERSIÓN |  FECHA  | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	 1.0	| 2016-09-20 | Javier Tirado 			 | Se creo el metodo editar
 *  3.1  | 2017-01-24 | Javier Tirado 			 | Valido que el monto del finiquito solo es requerido para VoBo.
 */
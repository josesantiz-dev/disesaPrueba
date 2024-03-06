package net.giro.rh.beans;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
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
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.logica.EmpleadoContratoRem;
import net.giro.rh.admon.logica.EmpleadoFiniquitoRem;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="finiquitoAction")
public class FiniquitoAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FiniquitoAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	private Empleado usuarioEmpleado;
	private boolean operacionCompleta;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private String valorBusqueda;
	private int numPagina;
	private EmpleadoFiniquitoRem ifzFiniquito;
	private EmpleadoFiniquito pojoFiniquito;
	private EmpleadoFiniquito pojoFiniquitoBorrar;
	private List<EmpleadoFiniquito> listaFiniquitosGrid;
	private EmpleadoExt pojoEmpleado;
	private List<Empleado> listaSolicitadoPor;
	private List<SelectItem> listaCboSolicitadoPor;
	private boolean voBoRh;
	private boolean aprobacion;
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
	// Contratos
	private EmpleadoContratoRem ifzContratos;
	// Perfiles
	private boolean permiteConsultar;
	private boolean permiteSolicitud;
	private boolean permiteVoBo;
	private boolean permiteAprobar;
	private boolean edicionBloqueada;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public FiniquitoAction() throws NamingException,Exception {
		PropertyResourceBundle prop = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		String valPerfil = "";
		Map<String, String> params = null;
		Long idEmpleado = 0L;
		List<String> puestosValidos = null;
		
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

			// Recuperamos puesto requerido (No obligatorio)
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			prop = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			valPerfil = prop.getString("PUESTOS.SOLICITUD.FINIQUITO");
			if (valPerfil != null && ! "".equals(valPerfil.trim())) {
				puestosValidos = new ArrayList<String>();
				if (valPerfil.contains(",")) {
					String[] splitted = valPerfil.split(",");
					for (String val : splitted)
						puestosValidos.add(val);
				} else {
					puestosValidos.add(valPerfil);
				}
			}

			this.ctx = new InitialContext();
			this.ifzFiniquito = (EmpleadoFiniquitoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFiniquitoFac!net.giro.rh.admon.logica.EmpleadoFiniquitoRem");
			this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzContratos = (EmpleadoContratoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			
			this.ifzFiniquito.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzContratos.setInfoSesion(this.loginManager.getInfoSesion());
			
			idEmpleado =this.loginManager.getInfoSesion().getAcceso().getUsuario().getIdEmpleado();
			if (idEmpleado == null)
				idEmpleado = 0L;
			this.usuarioEmpleado = this.ifzEmpleado.findById(idEmpleado);
			
			// El hecho de tener acceso solicitar finiquitos
			this.permiteSolicitud = true;
			
			/*this.permiteSolicitud = false;
			valPerfil = this.loginManager.getPerfil("FINIQUITOS_SOLICITUD");
			if (valPerfil != null && "S".equals(valPerfil.trim().toUpperCase()))
				this.permiteSolicitud = true;*/

			this.permiteVoBo = false;
			valPerfil = this.loginManager.getPerfil("FINIQUITOS_VOBO");
			if (valPerfil != null && "S".equals(valPerfil.trim().toUpperCase()))
				this.permiteVoBo = true;

			this.permiteAprobar = false;
			valPerfil = this.loginManager.getPerfil("FINIQUITOS_APROBACION");
			if (valPerfil != null && "S".equals(valPerfil.trim().toUpperCase()))
				this.permiteAprobar = true;
			
			// Con cualquier permiso anterior debe permitir la consulta de finiquitos
			this.permiteConsultar = false;
			if (this.permiteSolicitud || this.permiteVoBo || this.permiteAprobar)
				this.permiteConsultar = true;
			
			this.pojoFiniquito = new EmpleadoFiniquito();
			this.listaFiniquitosGrid = new ArrayList<EmpleadoFiniquito>();
			this.listaEmpleadosGrid = new ArrayList<Empleado>();
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
			if (! this.permiteConsultar) {
				control(-1, "No tiene permitido consultar los Finiquitos");
				return;
			}
			
			if ("".equals(this.valorBusqueda))
				this.listaFiniquitosGrid = this.ifzFiniquito.findAll();
			else
				this.listaFiniquitosGrid = this.ifzFiniquito.findByEmpleado(this.valorBusqueda); // busqueda por nombre de empleado
			
			// Comprobamos resultados
			this.numPagina = 1;
			if (this.listaFiniquitosGrid == null || this.listaFiniquitosGrid.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
			
			Collections.sort(this.listaFiniquitosGrid, new Comparator<EmpleadoFiniquito>() {
				@Override
				public int compare(EmpleadoFiniquito o1, EmpleadoFiniquito o2) {
					return o2.getId().compareTo(o1.getId());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Finiquitos", e);
		}
	}

	public void nuevo() {
		control();
		if (! this.permiteSolicitud) {
			control(-1, "No tiene permitido generar Solicitudes");
			return;
		}

		this.edicionBloqueada = false;
		this.voBoRh = false;
		this.aprobacion = false;
		this.pojoFiniquito = new EmpleadoFiniquito();
		this.pojoEmpleadoBusqueda = new Empleado();
		if (this.permiteSolicitud && this.usuarioEmpleado != null)
			this.pojoFiniquito.setSolicitadoPor(this.usuarioEmpleado.getId());
	}

	public void editar() {
		try {
			control();
			if (! this.permiteConsultar) {
				control(-1, "No tiene permitido consultar o editar los Finiquitos");
				return;
			}
			
			this.voBoRh = this.pojoFiniquito.getVoBoRh() == 1;
			this.aprobacion = this.pojoFiniquito.getAprobacion() == 1;
			
			this.edicionBloqueada = false;
			if (this.aprobacion && ! this.permiteAprobar)
				this.edicionBloqueada = true;
			if (this.edicionBloqueada && "ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario()))
				this.edicionBloqueada = false;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar editar el Finiquito seleccionado", e);
		}
	}
	
	public void guardar() {
		try {
			control();
			if (! validaGuardarFiniquito())
				return;
			
			// Visto Bueno
			this.pojoFiniquito.setVoBoRh(this.voBoRh ? 1 : 0);
			if (this.pojoFiniquito.getVoBoRh() == 1 && this.pojoFiniquito.getVoBoRhPor() <= 0L) {
				this.pojoFiniquito.setVoBoRhPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoFiniquito.setVoBoRhFecha(Calendar.getInstance().getTime());
			} 
			
			// Aprobacion
			this.pojoFiniquito.setAprobacion(this.aprobacion ? 1 : 0);
			if (this.pojoFiniquito.getAprobacion() == 1 && this.pojoFiniquito.getAprobacionPor() <= 0L) {
				this.pojoFiniquito.setAprobacionPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoFiniquito.setAprobacionFecha(Calendar.getInstance().getTime());
			}

			if (this.pojoFiniquito.getId() == null || this.pojoFiniquito.getId() <= 0L) {
				this.pojoFiniquito.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoFiniquito.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoFiniquito.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoFiniquito.setFechaModificacion(Calendar.getInstance().getTime());

				this.ifzFiniquito.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoFiniquito.setId(this.ifzFiniquito.save(this.pojoFiniquito));
				this.listaFiniquitosGrid.add(0, this.pojoFiniquito);
			} else {
				this.pojoFiniquito.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoFiniquito.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzFiniquito.setInfoSesion(this.loginManager.getInfoSesion());
				if (this.pojoFiniquito.getEstatus() == 0 && this.pojoFiniquito.getAprobacion() == 1)
					this.pojoFiniquito = this.ifzFiniquito.finiquitar(this.pojoFiniquito);
				else
					this.ifzFiniquito.update(this.pojoFiniquito);

				// Actualizo en lista si corresponde
				if (this.listaFiniquitosGrid != null && ! this.listaFiniquitosGrid.isEmpty()) {
					for (EmpleadoFiniquito item : this.listaFiniquitosGrid) {
						if (this.pojoFiniquito.getId().longValue() != item.getId().longValue()) 
							continue;
						this.listaFiniquitosGrid.set(this.listaFiniquitosGrid.indexOf(item), this.pojoFiniquito);
						break;
					}
				}
			}
			
			log.info("Finiquito Guardado");
			nuevo();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar el Finiquito", e);
		}
	}

	public void cancelar() {
		try {
			control();
			// Comprobamos finiquito
			if (this.pojoFiniquitoBorrar == null || this.pojoFiniquitoBorrar.getId() == null || this.pojoFiniquitoBorrar.getId() <= 0L) {
				control(-1, "No se pudo cancelar el Finiquito.\nOcurrio un problema al recuperar el Finiquitos seleccionado.");
				return;
			}
			
			if (this.pojoFiniquitoBorrar.getAprobacion() == 1) {
				control(-1, "No se permite Cancelar Finiquitos aprobados.");
				return;
			}
			
			// Borramos finiquito
			log.info("Cancelando finiquito " + this.pojoFiniquitoBorrar.getId() + " ... ");
			this.ifzFiniquito.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFiniquito.cancelar(this.pojoFiniquitoBorrar);
			log.info("Finiquito cancelado!.");
			
			// Actualizamos el finiquito en el listado si corresponde
			if (this.listaFiniquitosGrid != null && ! this.listaFiniquitosGrid.isEmpty()) {
				int index = -1;
				log.info("Actualizando finiquito " + this.pojoFiniquitoBorrar.getId() + " en lista ... ");
				for (EmpleadoFiniquito item : this.listaFiniquitosGrid) {
					if (this.pojoFiniquitoBorrar.getId().longValue() != item.getId().longValue()) 
						continue;
					index = this.listaFiniquitosGrid.indexOf(item);
					break;
				}
				
				if (index >= 0) {
					this.listaFiniquitosGrid.set(index, this.pojoFiniquitoBorrar);
					log.info("Finiquito actualizado");
				}
			}
			
			// Comprobamos otros finiquitos para activar a Empleado si corresponde
			/*List<EmpleadoFiniquito> listFiniquitos = this.ifzFiniquito.findByProperty("idEmpleado", this.pojoFiniquitoBorrar.getIdEmpleado().getId());
			if (listFiniquitos == null || listFiniquitos.isEmpty()) {
				// Activamos empleado
				log.info("Activando empleado " + this.pojoFiniquitoBorrar.getIdEmpleado().getId() + " ... ");
				this.pojoFiniquitoBorrar.getIdEmpleado().setEstatus(0);
				this.ifzEmpleado.update(this.pojoFiniquitoBorrar.getIdEmpleado());
				log.info("Empleado activado!");
			}*/
		} catch(Exception e) {
			control("Ocurrio un problema al intentar borrar el Finiquito seleccionado", e);
		}
	}
	
	private void cargarListaSolicitadoPor() {
		listaSolicitadoPor = this.ifzEmpleado.findAll();
	}
	
	private void cargarListaCboSolicitadoPor() {
		listaCboSolicitadoPor = null;
		this.listaCboSolicitadoPor = null;
		this.listaCboSolicitadoPor = new ArrayList<SelectItem>();
		
		for (Empleado e : this.listaSolicitadoPor) {
			this.listaCboSolicitadoPor.add(new SelectItem(e.getId().toString(), e.getNombre()));
		}
	}

	private boolean validaGuardarFiniquito() {
		if (this.pojoFiniquito == null) {
			control(-1, "Ocurrio un problema con el Finiquito. No se puede guardar");
			return false;
		}
		
		if (this.pojoFiniquito.getAprobacion() == 1 && this.pojoFiniquito.getAprobacionPor() > 0L) {
			control(-1, "Este Finiquito ya ha sido Aprobado previamente");
			return false;
		}

		if (this.pojoFiniquito.getIdEmpleado() == null) {
			control(-1, "Debe seleccionar Empleado");
			return false;
		}
		
		if (this.pojoFiniquito.getFechaSolicitudBaja() == null) {
			control(-1, "Debe indicar fecha de Solicitud de Baja");
			return false;
		}
		
		/* quito validacion para comparar la fecha de solicitud contra la fecha de elaboracion de finiquito
		 * se valido con Angelica Romero y Rosaura Mesta el 2018-06-21
		if (this.pojoFiniquito.getFechaElaboracionEnvio() != null) {
			if (this.pojoFiniquito.getFechaSolicitudBaja().compareTo(this.pojoFiniquito.getFechaElaboracionEnvio()) > 0) {
				control(-1, "La Fecha Elaboracion y Envío Finiquito debe ser mayor o igual a la Fecha Solicitud Baja");
				return false;
			}
		}*/
		
		if (this.pojoFiniquito.getMonto() <= 0) {
			if (this.pojoFiniquito.getVoBoRh() == 1) {
				control(-1, "Ingrese el monto del Finiquito");
				return false;
			}
		}
		
		return true;
	}
	
	private void control() {
		this.operacionCompleta = true;
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}

	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje))
			mensaje = "Ocurrio un problema al ejecutar el proceso.";
		this.operacionCompleta = false;
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = sw.toString();
		}
		
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: FINIQUITOS :: " + this.tipoMensaje + " - " + this.mensaje + "\n" + this.mensajeDetalles, throwable);
	}

	// ----------------------------------------------------------------------------------------------
	// BUSQUEDA EMPLEADOS
	// ----------------------------------------------------------------------------------------------
	
	public void nuevaBusquedaEmpleados() {
		control();
		this.numPaginaEmpleados = 1;
		if (this.listaEmpleadosGrid == null)
			this.listaEmpleadosGrid = new ArrayList<Empleado>();
		this.listaEmpleadosGrid.clear();
		this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();
		this.valorBusquedaEmpleados = "";
		this.tipoBusquedaEmpleados = 1;
	}
	
	public void buscarEmpleados() {
		try {
			control();
			if (this.listaEmpleadosGrid == null)
				this.listaEmpleadosGrid = new ArrayList<Empleado>();
			this.listaEmpleadosGrid.clear();

			this.numPaginaEmpleados = 1;
			this.listaEmpleadosGrid = this.ifzEmpleado.findLike(this.valorBusquedaEmpleados, "nombre", 0, 0);
			if (this.listaEmpleadosGrid == null || this.listaEmpleadosGrid.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Empleados", e);
		}
	}
	
	public void seleccionarEmpleado() {
		EmpleadoContrato contrato = null;
		
		try {
			control();
			// Comprobamos estatus Contrato
			contrato = this.ifzContratos.findContrato(this.pojoEmpleadoBusqueda.getId());
			if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L) {
				control(-1, "El Empleado seleccionado no tiene un contrato valido");
				return;
			}
			
			// Comprobamos finiquito pendiente
			if (this.ifzFiniquito.comprobarFiniquitos(this.pojoEmpleadoBusqueda.getId())) {
				control(-1, "El Empleado ya tiene una solicitud de Finiquito");
				return;
			}
			
			// Asigno el Empleado al Finiquito
			this.pojoFiniquito.setIdEmpleado(this.pojoEmpleadoBusqueda);
			this.pojoFiniquito.setFechaIngreso(this.pojoEmpleadoBusqueda.getFechaIngreso());
			this.pojoFiniquito.setIdContrato(contrato.getId());
			nuevaBusquedaEmpleados();
		} catch (Exception e) {	
			control("Ocurrio un problema al intentar recuperar el Empleado seleccionado", e);
		}
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
		if ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario()))
			return 7;
		/*if (this.permiteSolicitud && this.permiteVoBo && this.permiteAprobar) // 111
			return 7;
		if (this.permiteVoBo && this.permiteAprobar) // 110
			return 6;
		if (this.permiteSolicitud && this.permiteAprobar) // 101
			return 5;*/
		if (this.permiteAprobar) // 100
			return 7; // 4
		/*if (this.permiteSolicitud && this.permiteVoBo) // 011
			return 3;*/
		if (this.permiteVoBo) // 010
			return 2;
		if (this.permiteSolicitud) // 001
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
		this.pojoFiniquito.setSolicitadoPor(idEmpleado);
	}
	
	public long getUsuarioId() {
		return this.loginManager.getInfoSesion().getAcceso().getUsuario().getId();
	}

	public void setUsuarioId(long usuarioId) {}

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

	public EmpleadoFiniquito getPojoFiniquito() {
		return pojoFiniquito;
	}

	public void setPojoFiniquito(EmpleadoFiniquito pojoFiniquito) {
		this.pojoFiniquito = pojoFiniquito;
	}

	public List<EmpleadoFiniquito> getListaFiniquitosGrid() {
		return listaFiniquitosGrid;
	}

	public void setListaFiniquitosGrid(List<EmpleadoFiniquito> listaFiniquitosGrid) {
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
	
	public boolean getVoBoRH() {
		return this.voBoRh;
	}

	public void setVoBoRH(boolean voBoRh) {
		this.voBoRh = voBoRh;
	}

	public String getFechaVoBoRH() {
		if (this.pojoFiniquito != null && this.pojoFiniquito.getVoBoRh() == 1 && this.pojoFiniquito.getVoBoRhFecha() != null)
			return (new SimpleDateFormat("dd-MMM-yyyy")).format(this.pojoFiniquito.getVoBoRhFecha());
		return "";
	}
	
	public void setFechaVoBoRH(String value) {}

	public boolean getAprobacion(){
		return this.aprobacion;
	}
	
	public void setAprobacion(boolean aprobacion) {
		if (this.permiteAprobar) {
			if (! aprobacion && this.pojoFiniquito.getEstatus() == 2)
				return;
			this.aprobacion = aprobacion;
		}
	}
	
	public String getFechaAprobacion() {
		if (this.pojoFiniquito != null && this.pojoFiniquito.getAprobacion() == 1 && this.pojoFiniquito.getAprobacionFecha() != null)
			return (new SimpleDateFormat("dd-MMM-yyyy")).format(this.pojoFiniquito.getAprobacionFecha());
		return "";
	}
	
	public void setFechaAprobacion(String value) {}

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
		//this.pojoFiniquito.setIdEmpleado(this.ifzEmpleado.findByIdExt(pojoEmpleadoBusqueda.getId()));
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public boolean isPermiteConsultar() {
		return permiteConsultar;
	}

	public void setPermiteConsultar(boolean permiteConsultar) {
		this.permiteConsultar = permiteConsultar;
	}

	public boolean isPermiteSolicitud() {
		return permiteSolicitud;
	}

	public void setPermiteSolicitud(boolean permiteSolicitud) {
		this.permiteSolicitud = permiteSolicitud;
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

	public EmpleadoFiniquito getPojoFiniquitoBorrar() {
		return pojoFiniquitoBorrar;
	}

	public void setPojoFiniquitoBorrar(EmpleadoFiniquito pojoFiniquitoBorrar) {
		this.pojoFiniquitoBorrar = pojoFiniquitoBorrar;
	}

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public boolean isOperacionCancelada() {
		return operacionCancelada;
	}

	public void setOperacionCancelada(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
	}

	public boolean isEdicionBloqueada() {
		return edicionBloqueada;
	}

	public void setEdicionBloqueada(boolean edicionBloqueada) {
		this.edicionBloqueada = edicionBloqueada;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  1.0   | 2016-09-20 | Javier Tirado 		 	 | Se creo el metodo editar
 *    3.1   | 2017-01-24 | Javier Tirado 		 	 | Valido que el monto del finiquito solo es requerido para VoBo.
 */
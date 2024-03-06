package cde.publico.ubicacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.respuesta.Respuesta;
import net.giro.ubicacion.logica.ColoniasRem;

import org.apache.log4j.Logger;

public class ColoniasAction implements Serializable {
	private static final long serialVersionUID = 985652410351472793L;
	private Logger log = Logger.getLogger(ColoniasAction.class);
	private LoginManager loginManager;
	private ColoniasRem ifzColonias;
	private List<Colonia> listColonias;
	private Colonia pojoColonia;
	// -----------------------------------------
	private List<Localidad> listLocalidades;
	private Localidad pojoLocalidad;
	private int numPaginaLocalidades;
	private String valorBusquedaLocalidades;
	// -----------------------------------------
	private List<Municipio> listMunicipios;
	private Municipio pojoMunicipio;
	private int numPaginaMunicipios;
	private String valorBusquedaMunicipios;
	// -----------------------------------------
	private List<SelectItem> listTmpEstados;
	private List<Estado> listEstados;
	private Estado pojoEstado;
	// -----------------------------------------
	private int numPagina;
	private Boolean puedeEditar;
	private String tipoBusqueda;
	private String valorBusqueda;
	// control
	private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	
	public ColoniasAction() throws Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = null;
		Context ctx = null;

		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			
			ctx = this.loginManager.getCtx();
			this.ifzColonias = (ColoniasRem) ctx.lookup("ejb:/Logica_Publico//ColoniasFac!net.giro.ubicacion.logica.ColoniasRem");
			this.ifzColonias.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.listColonias = new ArrayList<Colonia>();
			this.listMunicipios = new ArrayList<Municipio>();
			this.listLocalidades = new ArrayList<Localidad>();
			this.pojoColonia = new Colonia();
			this.pojoMunicipio = new Municipio();
			this.pojoLocalidad = new Localidad();
			this.pojoEstado = new Estado();
			this.tipoBusqueda = "";
			this.valorBusqueda = "";
			this.numPagina = 1;
			this.puedeEditar = true;
			cargaEstados();
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
		}
	}
	
	public void buscar() {
		try {
			control();
			this.listColonias = this.ifzColonias.buscarColonias(this.tipoBusqueda, this.valorBusqueda);
			if (this.listColonias == null || this.listColonias.isEmpty())
				control(-1, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Colonias", e);
		}
	}
	
	public void nuevo() {
		control();
		this.pojoColonia = new Colonia();
		this.pojoEstado = null;
		this.pojoMunicipio = null;
		this.pojoLocalidad = null;
	}
	
	public void editar() {
		try {
			control();
			if (this.pojoColonia == null || this.pojoColonia.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Colonia seleccionada");
				return;
			}

			this.pojoLocalidad = (this.pojoColonia.getLocalidad() != null && this.pojoColonia.getLocalidad().getId() > 0L ? this.pojoColonia.getLocalidad() : null);
			this.pojoMunicipio = (this.pojoLocalidad != null && this.pojoLocalidad.getMunicipio() != null && this.pojoLocalidad.getMunicipio().getId() > 0L ? this.pojoLocalidad.getMunicipio() : null);
			this.pojoEstado = (this.pojoMunicipio != null && this.pojoMunicipio.getEstado() != null && this.pojoMunicipio.getEstado().getId() > 0L ? this.pojoMunicipio.getEstado() : null);
			if (this.pojoEstado == null) {
				this.pojoMunicipio = null;
				this.pojoLocalidad = null;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar editar la Colonia indicada", e);
		}
	}

	public void guardar() {
		Respuesta respuesta = null;
		long id = 0L;
		
		try {
			control();
			if (this.pojoLocalidad == null || this.pojoLocalidad.getId() <= 0L) {
				control(-1, "Ocurrio un problema al asignar la Localidad o no es valida");
				return;
			}
			
			id = this.pojoColonia.getId();
			this.pojoColonia.setLocalidad(this.pojoLocalidad);
			
			respuesta = this.ifzColonias.salvar(this.pojoColonia);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, respuesta.getErrores().getDescError());
				return;
			}

			this.pojoColonia = (Colonia) respuesta.getBody().getValor("colonia");
			if (id > 0) {
				for (Colonia colonia : this.listColonias) {
					if (id == colonia.getId()) {
						this.listColonias.set(this.listColonias.indexOf(colonia), this.pojoColonia);
						return;
					}
				}
			}

			this.listColonias.add(0, this.pojoColonia);
		} catch (Exception e) {
			control("Ocurrio un problema al guardar la Colonia", e);
		}		
	}
	
	public void eliminar() {
		try {
			control();
			this.ifzColonias.eliminar(this.pojoColonia);
			this.listColonias.remove(this.pojoColonia);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar la Colonia", e);
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
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n\n" + this.getClass().getCanonicalName() + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "\n" + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	// -------------------------------------------------------------------------------------------------------------
	// ESTADOS
	// -------------------------------------------------------------------------------------------------------------
	
	public void limpiaEstados() {
		control();
		this.pojoLocalidad = new Localidad();
		this.pojoMunicipio = new Municipio();
	}
	
	@SuppressWarnings("unchecked")
	public void cargaEstados() {
		Respuesta respuesta = null;
		
		try {
			control();
			this.listTmpEstados = new ArrayList<SelectItem>();
			respuesta = this.ifzColonias.cargarEstados();
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al cargar los Estados");
				return;
			}
			
			this.listEstados = (List<Estado>) respuesta.getBody().getValor("listEstados");
			if (this.listEstados != null && ! this.listEstados.isEmpty()) {
				for (Estado pojoEstadoAux : this.listEstados)
					this.listTmpEstados.add(new SelectItem(pojoEstadoAux.getId(), pojoEstadoAux.getNombre()));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar los Estados.", e);
		}
	}

	private void seleccionarEstado(long idEstado) {
		control();
		this.pojoEstado = null;
		if (idEstado <= 0L || (this.listEstados == null || this.listEstados.isEmpty()))
			return;
		
		for (Estado estado : this.listEstados) {
			if (idEstado == estado.getId()) {
				this.pojoEstado = estado;
				break;
			}
		}
		
		if (this.pojoMunicipio != null && this.pojoMunicipio.getId() > 0L && this.pojoMunicipio.getEstado().getId() != this.pojoEstado.getId()) {
			this.pojoMunicipio = null;
			this.pojoLocalidad = null;
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------
	// MUNICIPIOS
	// -------------------------------------------------------------------------------------------------------------

	public void limpiaMunicipios() {
		control();
		this.listMunicipios = new ArrayList<Municipio>();
	}
	
	@SuppressWarnings("unchecked")
	public void buscarMunicipios() {
		Respuesta respuesta = null;
		
		try {
			control();
			respuesta = this.ifzColonias.buscarMunicipios(this.pojoEstado, this.valorBusquedaMunicipios);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, respuesta.getErrores().getDescError());
				return;
			}
			
			this.listMunicipios = (List<Municipio>) respuesta.getBody().getValor("municipios");
			if (this.listMunicipios == null)
				this.listMunicipios = new ArrayList<Municipio>();
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Municipios del Estado seleccionado", e);
		}
	}

	public void seleccionarMunicipio() {
		try {
			control();
			this.pojoLocalidad = null;
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Municipio seleccionado", e);
		}
	}

	// -------------------------------------------------------------------------------------------------------------
	// LOCALIDADES
	// -------------------------------------------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	public void buscarLocalidades() {
		Respuesta respuesta = null;
		
		try {
			control();
			respuesta = this.ifzColonias.buscarLocalidades(this.pojoMunicipio, this.valorBusquedaLocalidades);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, respuesta.getErrores().getDescError());
				return;
			}
			
			this.listLocalidades = (List<Localidad>) respuesta.getBody().getValor("localidades");
			if (this.listMunicipios == null)
				this.listLocalidades = new ArrayList<Localidad>();
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Localidades del Municipio seleccionado", e);
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------
	
	public long getIdEstado() {
		if (this.pojoEstado != null && this.pojoEstado.getId() > 0L)
			return this.pojoEstado.getId();
		return 0L;
	}
	
	public void setIdEstado(long idEstado) {
		seleccionarEstado(idEstado);
	}
	
	public String getMunicipio() {
		if (this.pojoMunicipio != null && this.pojoMunicipio.getId() > 0L)
			return this.pojoMunicipio.getId() + " - " + this.pojoMunicipio.getNombre();
		return "";
	}
	
	public void setMunicipio(String value) {}
	
	public String getLocalidad() {
		if (this.pojoLocalidad != null && this.pojoLocalidad.getId() > 0L)
			return this.pojoLocalidad.getId() + " - " + this.pojoLocalidad.getNombre();
		return "";
	}
	
	public void setLocalidad(String value) {}

	public boolean getOperacion() {
		return operacion;
	}

	public void setResOperacion(boolean operacion) {
		this.operacion = operacion;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
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

	public Boolean getPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(Boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public List<Colonia> getListColonias() {
		return listColonias;
	}

	public void setListColonias(List<Colonia> listColonias) {
		this.listColonias = listColonias;
	}

	public Colonia getPojoColonia() {
		return pojoColonia;
	}

	public void setPojoColonia(Colonia pojoColonia) {
		this.pojoColonia = pojoColonia;
	}

	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}

	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}

	public Localidad getPojoLocalidad() {
		return pojoLocalidad;
	}

	public void setPojoLocalidad(Localidad pojoLocalidad) {
		this.pojoLocalidad = pojoLocalidad;
	}

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public Municipio getPojoMunicipio() {
		return pojoMunicipio;
	}

	public void setPojoMunicipio(Municipio pojoMunicipio) {
		this.pojoMunicipio = pojoMunicipio;
	}

	public List<SelectItem> getListTmpEstados() {
		return listTmpEstados;
	}

	public void setListTmpEstados(List<SelectItem> listTmpEstados) {
		this.listTmpEstados = listTmpEstados;
	}

	public int getNumPaginaLocalidades() {
		return numPaginaLocalidades;
	}

	public void setNumPaginaLocalidades(int numPaginaLocalidades) {
		this.numPaginaLocalidades = numPaginaLocalidades;
	}

	public int getNumPaginaMunicipios() {
		return numPaginaMunicipios;
	}

	public void setNumPaginaMunicipios(int numPaginaMunicipios) {
		this.numPaginaMunicipios = numPaginaMunicipios;
	}

	public String getValorBusquedaLocalidades() {
		return valorBusquedaLocalidades;
	}

	public void setValorBusquedaLocalidades(String valorBusquedaLocalidades) {
		this.valorBusquedaLocalidades = valorBusquedaLocalidades;
	}

	public String getValorBusquedaMunicipios() {
		return valorBusquedaMunicipios;
	}

	public void setValorBusquedaMunicipios(String valorBusquedaMunicipios) {
		this.valorBusquedaMunicipios = valorBusquedaMunicipios;
	}
}

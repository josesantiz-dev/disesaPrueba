package cde.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.LocalidadesRem;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.respuesta.Respuesta;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;

@KeepAlive
public class LocalidadesAction {
	Logger log = Logger.getLogger(LocalidadesAction.class);
	private LoginManager loginManager;
	private Context ctx;
	@SuppressWarnings("unused")
	private Object lookup;	
	private String resOperacion;
	private String problemInesp;
	private long numPagina;
	private long numPaginaMunicipios;
	private List<Estado> listEstados;
	private List<Municipio> listMunicipios;
	private List<Localidad> listLocalidades;	
	private List<SelectItem> listTmpMunicipios;
	private List<SelectItem> listTmpEstados;
	private List<SelectItem> listZonas;
	private List<ConValores> listTmpZonas;
	private List<ConValores> listTmpGradoMarginalidad;
	private List<SelectItem> listGradoMarginalidad;
	private Estado pojoEstado;
	private Municipio pojoMunicipio;
	private Localidad pojoLocalidad;
	private String tipoBusqueda;
	private String valorBusqueda;
	private String valorBusquedaMunicipios;
	private LocalidadesRem ifzLocalidades;
	
	public LocalidadesAction() throws Exception {
		PropertyResourceBundle propPlataforma = null;
		ValueExpression ve = null;
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
		this.ctx = this.loginManager.getCtx();
		
		ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle) ve.getValue(fc.getELContext());
		this.problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		
		this.ifzLocalidades = (LocalidadesRem) this.ctx.lookup("ejb:/Logica_Publico//LocalidadesFac!net.giro.plataforma.logica.LocalidadesRem");
		this.ifzLocalidades.setInfoSesion(this.loginManager.getInfoSesion());
	
		this.numPagina = 1;
		this.numPaginaMunicipios = 1;
		
		this.listEstados = new ArrayList<Estado>();
		this.listMunicipios = new ArrayList<Municipio>();	
		this.listLocalidades = new ArrayList<Localidad>();
		
		this.listTmpEstados = new ArrayList<SelectItem>();
		this.listTmpMunicipios = new ArrayList<SelectItem>();
		
		this.pojoEstado = new Estado();
		this.pojoLocalidad = new Localidad();
		this.pojoMunicipio = new Municipio();

		cargaZonas();
		cargaGradoMarginalidad();
		cargarEstados();
	}
	
	@SuppressWarnings("unchecked")
	public void buscar() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzLocalidades.buscarLocalidades(this.tipoBusqueda, this.valorBusqueda);
			if (respuesta.getErrores().getCodigoError() == 0L) {
				this.listLocalidades = (List<Localidad>) respuesta.getBody().getValor("listLocalidades");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al buscar", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarMunicipios() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzLocalidades.buscarMunicipios(this.pojoEstado, this.valorBusquedaMunicipios);
			if (respuesta.getErrores().getCodigoError() == 0L) {
				this.listMunicipios = (List<Municipio>) respuesta.getBody().getValor("listMunicipios");
			} else 
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al buscar municipios", e);
		}
	}
	
	public void cargarMunicipios() {
		
	}
	
	@SuppressWarnings("unchecked")
	public void cargarEstados() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = ifzLocalidades.cargarEstados();
			if (respuesta.getErrores().getCodigoError() == 0L) {
				this.listEstados = (List<Estado>) respuesta.getBody().getValor("listEstados");
				if (this.listTmpEstados == null)
					this.listTmpEstados = new ArrayList<SelectItem>();
				else
					this.listTmpEstados.clear();
				
				for (Estado pojoEstadoAux : this.listEstados) {
					SelectItem selectItem = new SelectItem(pojoEstadoAux.getId(), pojoEstadoAux.getNombre());
					this.listTmpEstados.add(selectItem);
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al cargar estados", e);
			this.resOperacion = problemInesp;
		}
	}

	public void cargaZonas(){
		try {
			if (this.listTmpZonas != null && ! this.listTmpZonas.isEmpty())
				return;

			this.listZonas = new ArrayList<SelectItem>();
			this.listTmpZonas = this.ifzLocalidades.buscarZonas();
			
			for (ConValores cv : this.listTmpZonas)
				this.listZonas.add(new SelectItem(cv.getId(), cv.getValor()));
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaZonas", e);
		}
	}

	public void cargaGradoMarginalidad(){
		try {
			if (this.listTmpGradoMarginalidad != null && ! this.listTmpGradoMarginalidad.isEmpty())
				return;

			this.listGradoMarginalidad = new ArrayList<SelectItem>();
			this.listTmpGradoMarginalidad = this.ifzLocalidades.buscarGradoMarginalidad();
			
			for (ConValores cv : this.listTmpGradoMarginalidad)
				this.listGradoMarginalidad.add(new SelectItem(cv.getId(), cv.getValor()));
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaGradoMarginalidad", e);
		}
	}
	
	public void guardar() {
		try {
			this.resOperacion = "";
			this.pojoLocalidad.setMunicipio(this.pojoMunicipio);
			Respuesta respuesta = ifzLocalidades.guardarLocalidad(this.pojoLocalidad);
			if (respuesta.getErrores().getCodigoError() == 0L) {
				long id = this.pojoLocalidad.getId();
				
				this.pojoLocalidad = (Localidad) respuesta.getBody().getValor("pojoLocalidad");
				
				if (id == 0L)
					this.listLocalidades.add(this.pojoLocalidad);
				else {
					List<Localidad> listLocalidadesAux = new ArrayList<Localidad>();
					
					listLocalidadesAux.add(this.pojoLocalidad);
					
					for (Localidad pojoLocalidadAux : this.listLocalidades) {
						if (pojoLocalidadAux.getId() != id)
							listLocalidadesAux.add(pojoLocalidadAux);
					}
					
					this.listLocalidades = listLocalidadesAux;
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al guardar", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void guardarMunicipio() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzLocalidades.guardarMunicipio(this.pojoMunicipio);
			if (respuesta.getErrores().getCodigoError() == 0L) {
				long id = this.pojoMunicipio.getId();
				this.pojoMunicipio = (Municipio) respuesta.getBody().getValor("pojoMunicipio");
				if (id == 0L)
					this.listMunicipios.add(this.pojoMunicipio);
				else{
					List<Municipio> listMunicipiosAux = new ArrayList<Municipio>();
					listMunicipiosAux.add(this.pojoMunicipio);
					for (Municipio pojoMunicipioAux : this.listMunicipios) {
						if (pojoMunicipioAux.getId() != id)
							listMunicipiosAux.add(pojoMunicipioAux);
					}
					
					this.listMunicipios = listMunicipiosAux;
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al guardarMunicipio", e);
			this.resOperacion = this.problemInesp;
		}
	}
	
	public void eliminar() {
		
	}
	
	public void editar() {
		this.resOperacion = "";
		try {
			this.pojoMunicipio = this.pojoLocalidad.getMunicipio();
		} catch (Exception e) {
			log.error("Error al editar", e);
			this.resOperacion = this.problemInesp;
		}
	}
	
	public void nuevo() {
		this.resOperacion = "";
		try {
			this.pojoLocalidad = new Localidad();
			this.pojoMunicipio = new Municipio();
		} catch (Exception e) {
			log.error("Error en nuevo" ,e);
			this.resOperacion = this.problemInesp;
		}
	}
	
	public void nuevoMunicipio() {
		this.resOperacion = "";
		try {
			this.pojoMunicipio = new Municipio();
		} catch (Exception e) {
			log.error("Error en nuevoMunicipio", e);
			this.resOperacion = this.problemInesp;
		}
	}

	private ConValores getValorById(Long id, List<ConValores> lista){
		for(ConValores cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}
	
	public void seleccionarMunicipio() {
		this.resOperacion = "";
	}
	
	public long getPojoEstadoAux() {
		return this.pojoEstado != null ? this.pojoEstado.getId() : 0L;
	}
	
	public void setPojoEstadoAux(long idEstado) {
		this.pojoEstado = new Estado();
		this.pojoEstado.setId(idEstado);
		
		for (Estado pojoEstadoAux : listEstados) {
			if (pojoEstadoAux.getId() == idEstado) {
				this.pojoEstado = pojoEstadoAux;
			}
		}
	}
	
	public long getPojoEstadoMunicipioAux() {
		return this.pojoMunicipio.getEstado() != null ? this.pojoMunicipio.getEstado().getId() : 0L;
	}
	
	public void setPojoEstadoMunicipioAux(long idEstado) {
		this.pojoMunicipio.setEstado(new Estado());
		this.pojoMunicipio.getEstado().setId(idEstado);
		
		for (Estado pojoEstadoAux : this.listEstados) {
			if (pojoEstadoAux.getId() == idEstado) {
				this.pojoMunicipio.setEstado(pojoEstadoAux);
			}
		}
	}
	
	public String getResOperacion() {
		return resOperacion;
	}
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}
	
	public long getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(long numPagina) {
		this.numPagina = numPagina;
	}
	
	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}
	
	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}
	
	public List<SelectItem> getListTmpMunicipios() {
		return listTmpMunicipios;
	}
	
	public void setListTmpMunicipios(List<SelectItem> listTmpMunicipios) {
		this.listTmpMunicipios = listTmpMunicipios;
	}

	public Localidad getPojoLocalidad() {
		return pojoLocalidad;
	}

	public void setPojoLocalidad(Localidad pojoLocalidad) {
		this.pojoLocalidad = pojoLocalidad;
	}

	public Municipio getPojoMunicipio() {
		return pojoMunicipio;
	}

	public void setPojoMunicipio(Municipio pojoMunicipio) {
		this.pojoMunicipio = pojoMunicipio;
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

	public List<SelectItem> getListTmpEstados() {
		return listTmpEstados;
	}

	public void setListTmpEstados(List<SelectItem> listTmpEstados) {
		this.listTmpEstados = listTmpEstados;
	}

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public long getNumPaginaMunicipios() {
		return numPaginaMunicipios;
	}

	public void setNumPaginaMunicipios(long numPaginaMunicipios) {
		this.numPaginaMunicipios = numPaginaMunicipios;
	}

	public String getValorBusquedaMunicipios() {
		return valorBusquedaMunicipios;
	}

	public void setValorBusquedaMunicipios(String valorBusquedaMunicipios) {
		this.valorBusquedaMunicipios = valorBusquedaMunicipios;
	}
	
	public List<SelectItem> getListZonas() {
		return listZonas;
	}
	
	public void setListZonas(List<SelectItem> listZonas) {
		this.listZonas = listZonas;
	}

	public Long getIdZona() {
		return this.pojoLocalidad.getZona()!=null ? this.pojoLocalidad.getZona().getId() : -1l;
	}

	public void setIdZona(Long idZonaEconomica) {
		this.pojoLocalidad.setZona(getValorById(idZonaEconomica, listTmpZonas));
	}
	
	public List<SelectItem> getListGradoMarginalidad() {
		return listGradoMarginalidad;
	}

	public void setListGradoMarginalidad(List<SelectItem> listGradoMarginalidad) {
		this.listGradoMarginalidad = listGradoMarginalidad;
	}
	
	public Long getIdGradoMarginalidad() {
		return 0L;//return this.pojoLocalidad.getGradoMarginalidad() !=null ? this.pojoLocalidad.getGradoMarginalidad().getId() : -1l;
	}
	
	public void setIdGradoMarginalidad(Long idGradoMarginalidad) {
		//this.pojoLocalidad.setGradoMarginalidad(getValorById(idGradoMarginalidad, listTmpGradoMarginalidad));
	}
}

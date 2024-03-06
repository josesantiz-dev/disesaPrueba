package cde.publico.ubicacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

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
	private Context ctx;
	
	private ColoniasRem ifzColonias;
	private List<Colonia> listColonias;
	private List<Localidad> listLocalidades;
	private List<Municipio> listMunicipios;
	private List<Estado> listEstados;
	private List<SelectItem> listTmpEstados;
	private Colonia pojoColonia;
	private Localidad pojoLocalidad;
	private Municipio pojoMunicipio;
	private Estado pojoEstado;
	private int numPagina;
	private int numPaginaLocalidades;
	private int numPaginaMunicipios;
	private Boolean puedeEditar;
	private String resOperacion;
	private String problemInesp;
	private String tipoBusqueda;
	private String valorBusqueda;
	private String valorBusquedaLocalidades;
	private String valorBusquedaMunicipios;
	
	
	public ColoniasAction() throws Exception {
		ValueExpression ve = null;
		PropertyResourceBundle propPlataforma = null;
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
		this.ctx = loginManager.getCtx();
				
		//obtengo los posibles mensajes a mostrar al usuario
		ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle) ve.getValue(fc.getELContext());
		this.problemInesp = propPlataforma.getString("mensajes.error.inesperado");
		
		this.listColonias = new ArrayList<Colonia>();
		this.listMunicipios = new ArrayList<Municipio>();
		this.listLocalidades = new ArrayList<Localidad>();
		this.pojoColonia = new Colonia();
		this.pojoMunicipio = new Municipio();
		this.pojoLocalidad = new Localidad();
		this.pojoEstado = new Estado();
		this.numPagina = 1;
		this.tipoBusqueda = "";
		this.valorBusqueda = "";
		this.puedeEditar = true;
		
		this.ifzColonias = (ColoniasRem) this.ctx.lookup("ejb:/Logica_Publico//ColoniasFac!net.giro.ubicacion.logica.ColoniasRem");
		this.ifzColonias.setInfoSesion(this.loginManager.getInfoSesion());
		
		cargaEstados();
	}
	
	
	public void buscar() {
		try {
			this.listColonias = this.ifzColonias.buscarColonias(this.tipoBusqueda, this.valorBusqueda);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarLocalidades() {
		try {
			Respuesta respuesta = this.ifzColonias.buscarLocalidades(this.pojoMunicipio, this.valorBusquedaLocalidades);
			if (respuesta.getErrores().getCodigoError() == 0L) {
				this.listLocalidades = (List<Localidad>) respuesta.getBody().getValor("listLocalidades");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error al buscar localidades", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarMunicipios() {
		try {
			Respuesta respuesta = this.ifzColonias.buscarMunicipios(this.pojoEstado, this.valorBusquedaMunicipios);
			if (respuesta.getErrores().getCodigoError() == 0L) {
				this.listMunicipios = (List<Municipio>) respuesta.getBody().getValor("listMunicipios");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al buscar municipios", e);
		}
	}

	public void nuevo() {
		this.pojoColonia = new Colonia();
	}
	
	public void guardar() {
		try {
			this.resOperacion = "";
			long id = this.pojoColonia.getId();
			this.pojoColonia.setLocalidad(this.pojoLocalidad);
			
			Respuesta respuesta = this.ifzColonias.salvar(this.pojoColonia);
			
			if (respuesta.getErrores().getCodigoError() == 0L) {
				this.pojoColonia = (Colonia) respuesta.getBody().getValor("pojoColonia");
				
				if (id == 0)
					this.listColonias.add(this.pojoColonia);
				else {
					List<Colonia> listColoniasAux = new ArrayList<Colonia>();
					listColoniasAux.add(this.pojoColonia);
					
					for (Colonia pojoColoniaAux : this.listColonias)
						if (pojoColoniaAux.getId() == id)
							listColoniasAux.add(pojoColoniaAux);
					
					this.listColonias = listColoniasAux;
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}		
	}
	
	public void editar() {
		if (this.pojoColonia.getLocalidad() != null) {
			if (this.pojoColonia.getLocalidad().getId() > 0 && this.pojoColonia.getLocalidad().getMunicipio() != null) {
				this.pojoLocalidad = this.pojoColonia.getLocalidad();
				this.pojoMunicipio = this.pojoColonia.getLocalidad().getMunicipio();
				this.pojoEstado = this.pojoMunicipio.getEstado();
			}
		}
	}
	
	public void eliminar() {
		try {
			this.ifzColonias.eliminar(this.pojoColonia);
			this.listColonias.remove(this.pojoColonia);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar", e);
		}
	}
	
	public void limpiaEstados() {
		this.pojoLocalidad = new Localidad();
		this.pojoMunicipio = new Municipio();
	}
	
	public void limpiaMunicipios() {
		if (this.listMunicipios != null)
			this.listMunicipios.clear();
		else
			this.listMunicipios = new ArrayList<Municipio>();
	}
	
	public void seleccionarMunicipio() {
		try {
			this.pojoLocalidad = new Localidad();
		} catch (Exception e) {
			log.error("Error al seleccionar municipio", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaEstados() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = ifzColonias.cargarEstados();
			if (respuesta.getErrores().getCodigoError() == 0L) {
				this.listEstados = (List<Estado>) respuesta.getBody().getValor("listEstados");
				
				if (this.listTmpEstados != null)
					this.listTmpEstados.clear();
				else
					this.listTmpEstados = new ArrayList<SelectItem>();
				
				for (Estado pojoEstadoAux : this.listEstados)
					this.listTmpEstados.add(new SelectItem(pojoEstadoAux.getId(), pojoEstadoAux.getNombre()));
			} else
				this.resOperacion = this.problemInesp;
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo cargaZonasEconomicas", e);
		}
	}
	
	public Long getIdPojoEstado() {
		return pojoEstado.getId();
	}
	
	public void setIdPojoEstado(Long id) {
		for (Estado pojoEstadoAux : this.listEstados) {
			if (pojoEstadoAux.getId() == id) {
				this.pojoEstado = pojoEstadoAux;
			}
		}
	}
	
	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
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

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public List<SelectItem> getListTmpEstados() {
		return listTmpEstados;
	}

	public void setListTmpEstados(List<SelectItem> listTmpEstados) {
		this.listTmpEstados = listTmpEstados;
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

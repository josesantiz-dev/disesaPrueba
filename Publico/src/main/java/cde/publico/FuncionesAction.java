package cde.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.Funcion;
import net.giro.respuesta.Respuesta;




import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;

@KeepAlive
public class FuncionesAction {
	Logger log = Logger.getLogger(FuncionesAction.class);
	private LoginManager loginManager;
	
	private AdministracionRem 	ifzAdministracion;
	private List<Funcion> 	listFunciones;
	private List<SelectItem> listTmpFunciones;
	private List<Aplicacion> listTmpAplicaciones;
	private List<SelectItem> listAplicaciones;
	private Funcion		pojoFuncion;
	private	Aplicacion	pojoAplicacion;
	private List<Funcion> listValores;
	private String busquedaAplicacion;
	private String busquedaFuncion;
	private String tipoFuncion;
	private String resOperacion;
	private String problemInesp;
	private Long usuarioId;
	private int	numPagina;
	private boolean nuevoReg;
	private String[] tiposFunciones;
	private String busquedaVacia;
	private String campoBusqueda;
	private List<String> tipoBusqueda;
	private String valTipoBusqueda;
	
	
	public FuncionesAction() throws Exception {
		setTipoBusqueda(new ArrayList<String>());
		tipoBusqueda.add("nombre");
		numPagina = 1;
		pojoAplicacion=new Aplicacion();
		pojoFuncion = new Funcion();
		valTipoBusqueda="";
		
		listTmpFunciones = new ArrayList<SelectItem>();
		listValores = new ArrayList<Funcion>();
		listFunciones = new ArrayList<Funcion>();
		listAplicaciones=new ArrayList<SelectItem>();
		listTmpAplicaciones=new ArrayList<Aplicacion>();
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");

		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		InitialContext ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();

		ifzAdministracion = (AdministracionRem)ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
		ifzAdministracion.setInfoSesion(loginManager.getInfoSesion());
		
		tipoFuncion = "Catalogo";
		tiposFunciones = new String[3];
		tiposFunciones[0] = "Catalogo";
		tiposFunciones[1] = "Operacion";
		tiposFunciones[2] = "Reporte";
		
		cargarSelectAplicaciones();
	}
	
	
	public void guardar(){
		int icono = 1;
		try {
			this.resOperacion = "";
			long id = pojoFuncion.getId();
			
			if(tiposFunciones[0].equals(this.tipoFuncion))
				icono = 1;
			else if(tiposFunciones[1].equals(this.tipoFuncion))
				icono = 2;
			else if(tiposFunciones[2].equals(this.tipoFuncion))
				icono = 3;
			else
				icono = 0;
			this.pojoFuncion.setIcono((long) icono);
			
			Respuesta respuesta = this.ifzAdministracion.salvar(pojoFuncion);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoFuncion = (Funcion)respuesta.getBody().getValor("pojoFuncion");
				if(id == 0)
					this.listFunciones.add(this.pojoFuncion);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscar(){
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzAdministracion.buscarFuncion( this.campoBusqueda, this.valTipoBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.listFunciones = (List<Funcion>)respuesta.getBody().getValor("listFunciones");
				if (this.listFunciones != null && this.listFunciones.isEmpty()) 
					this.resOperacion = busquedaVacia;
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		}catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
		
	}

	public void editar(){
		try{
		nuevoReg = false;
		cargarSelectAplicaciones();
		int icono = (int) (this.pojoFuncion.getIcono() != null ? this.pojoFuncion.getIcono() : 1);
		switch(icono){
			case 1: this.tipoFuncion = tiposFunciones[0]; break;
			case 2: this.tipoFuncion = tiposFunciones[1]; break;
			case 3: this.tipoFuncion = tiposFunciones[2]; break;
			default: this.tipoFuncion = tiposFunciones[0]; break;
		}

		}catch (Exception e) {

	
		this.resOperacion = this.problemInesp;
		log.error("error al editar", e);
	}
	}
	
	public void nuevo() {
		try {			
			this.pojoFuncion = new Funcion();
			cargarSelectAplicaciones();
			resOperacion = "";
		} catch (Exception e) {
			log.error("Error en el metodo nuevo.", e);
		}
	}
	
	public void eliminar() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzAdministracion.eliminar(pojoFuncion);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listFunciones.remove(pojoFuncion);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminaFuncion", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarSelectAplicaciones(){
		Respuesta respuesta = ifzAdministracion.autocompletarAplicacion();
		if(respuesta.getErrores().getCodigoError() == 0L){
			listTmpAplicaciones = (List<Aplicacion>)respuesta.getBody().getValor("listAplicaciones");
			
			Collections.sort(this.listTmpAplicaciones, new Comparator<Aplicacion>() {
		    	@Override
		        public int compare(Aplicacion o1, Aplicacion o2) {
		    		return o1.getAplicacion().compareTo(o2.getAplicacion());
		        }
			});
			
			listAplicaciones.clear();
			for (Aplicacion i : listTmpAplicaciones) {
				listAplicaciones.add(new SelectItem(i.getId(), i.getAplicacion()));
			}
		} else
			this.resOperacion = respuesta.getErrores().getDescError();
	}

	private Aplicacion getAplicacionById(Long id, List<Aplicacion> lista){
		for(Aplicacion cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}

	public void setSuggAplicacion(Long aplicacion) {
		this.pojoFuncion.setAplicacion(getAplicacionById(aplicacion, this.listTmpAplicaciones));
	}

	public Long getSuggAplicacion() {
		return pojoFuncion.getAplicacion() != null ? pojoFuncion.getAplicacion().getId() : -1L;
	}
		
	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}
	
	public void setId(String id) { /* no hace nada */}

	public String getId() {
		return this.pojoFuncion.getId() <= 0 ? String.valueOf(this.pojoFuncion.getId()) : "";
	}

	public void setNombre(String nombre) {
		this.pojoFuncion.setNombre(nombre);
	}

	public String getNombre() {
		return this.pojoFuncion.getNombre() != null ? this.pojoFuncion.getNombre() : "";
	}

	public void setForma(String forma) {
		this.pojoFuncion.setForma(forma);
	}

	public String getForma() {
		return this.pojoFuncion.getForma() != null ? this.pojoFuncion.getForma() : "";
	}

	public void setDescripcion(String descripcion) {
		this.pojoFuncion.setDescripcion(descripcion);
	}

	public String getDescripcion() {
		return this.pojoFuncion.getDescripcion() != null ? this.pojoFuncion.getDescripcion() : "";
	}

	public void setPojoAplicacion(Aplicacion pojoAplicacion) {
		this.pojoAplicacion = pojoAplicacion;
	}

	public Aplicacion getPojoAplicacion() {
		return pojoAplicacion;
	}

	public List<SelectItem> getListAplicaciones() {
		return listAplicaciones;
	}

	public void setListAplicaciones(List<SelectItem> listAplicaciones) {
		this.listAplicaciones = listAplicaciones;
	}

	public List<Funcion> getListValores() {
		return listValores;
	}

	public void setListValores(List<Funcion> listValores) {
		this.listValores = listValores;
	}

	public List<Aplicacion> getListTmpAplicaciones() {
		return listTmpAplicaciones;
	}

	public void setListTmpAplicaciones(List<Aplicacion> listTmpAplicaciones) {
		this.listTmpAplicaciones = listTmpAplicaciones;
	}

	public List<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(List<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getBusquedaVacia() {
		return busquedaVacia;
	}

	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public List<Funcion> getListFunciones() {
		return listFunciones;
	}

	public void setListFunciones(List<Funcion> listFunciones) {
		this.listFunciones = listFunciones;
	}

	public List<SelectItem> getListTmpFunciones() {
		return listTmpFunciones;
	}

	public void setListTmpFunciones(List<SelectItem> listTmpFunciones) {
		this.listTmpFunciones = listTmpFunciones;
	}

	public Funcion getPojoFuncion() {
		return pojoFuncion;
	}

	public void setPojoFuncion(Funcion pojoFuncion) {
		this.pojoFuncion = pojoFuncion;
	}

	public String getBusquedaAplicacion() {
		return busquedaAplicacion;
	}

	public void setBusquedaAplicacion(String busquedaAplicacion) {
		this.busquedaAplicacion = busquedaAplicacion;
	}

	public String getBusquedaFuncion() {
		return busquedaFuncion;
	}

	public void setBusquedaFuncion(String busquedaFuncion) {
		this.busquedaFuncion = busquedaFuncion;
	}

	public String getTipoFuncion() {
		return tipoFuncion;
	}

	public void setTipoFuncion(String tipoFuncion) {
		this.tipoFuncion = tipoFuncion;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public boolean isNuevoReg() {
		return nuevoReg;
	}

	public void setNuevoReg(boolean nuevoReg) {
		this.nuevoReg = nuevoReg;
	}

	public String[] getTiposFunciones() {
		return tiposFunciones;
	}

	public void setTiposFunciones(String[] tiposFunciones) {
		this.tiposFunciones = tiposFunciones;
	}
}

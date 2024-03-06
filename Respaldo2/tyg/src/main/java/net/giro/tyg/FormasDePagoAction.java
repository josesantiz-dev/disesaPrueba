package net.giro.tyg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.logica.TygRem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;

@KeepAlive
public class FormasDePagoAction  {
	Logger log = Logger.getLogger(FormasDePagoAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	private TygRem ifzTyg;
	private List<FormasPagos> listValores;	
	private FormasPagos pojoFormasDePago;
	private String problemInesp;
	private String resOperacion;
	private int numPagina;
	private boolean registroSeleccionado;
	private String valTipoBusqueda;
	private String valorBusqueda;
	private String campoBusqueda;
	private String ctaBancariaDep;
	private String busquedaVacia;

	
	public FormasDePagoAction() throws Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression valExp = null;
		PropertyResourceBundle propPlataforma = null;

		valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
		
		valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		
		valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
		busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");

		this.listValores = new ArrayList<FormasPagos>();
		this.pojoFormasDePago = new FormasPagos();
		this.valTipoBusqueda = "formaPago";
		this.numPagina = 1;
	
		this.ctx = new InitialContext();
		this.ifzTyg = (TygRem) this.ctx.lookup("ejb:/Logica_TYG//TygFacade!net.giro.tyg.logica.TygRem");
		this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
	}
	
	
	@SuppressWarnings("unchecked")
	public void buscar() {
		try {			
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.buscarFormasPagos(this.valTipoBusqueda, this.valorBusqueda);
			if (respuesta.getErrores().getCodigoError() != 0) {
				this.resOperacion = respuesta.getErrores().getDescError();
				return;
			} 
			
			this.listValores = (List<FormasPagos>) respuesta.getBody().getValor("listFormasPago");
			if (this.listValores != null && this.listValores.isEmpty()) {
				this.resOperacion = busquedaVacia;
				return;
			}

			Collections.sort(this.listValores, new Comparator<FormasPagos>() {
				@Override
				public int compare(FormasPagos o1, FormasPagos o2) {
					return o1.getClaveSat().compareTo(o2.getClaveSat());
				}
			});
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public String nuevo() {	
		try {
			resOperacion= "";
			this.pojoFormasDePago = new FormasPagos();		
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("error al nuevo", e);
			return "ERROR";
		}
		return "OK";
	}

	public String editar(){
		this.registroSeleccionado=true;
		return "OK";
	}

	public void guardar() {
		Long id = 0L;
		
		try {
			this.resOperacion = "";
			if (this.pojoFormasDePago.getId() == null)
				this.pojoFormasDePago.setId(0L);
			id = this.pojoFormasDePago.getId();
			this.ifzTyg.setInfoSesion(this.loginManager.getInfoSesion());
			
			Respuesta respuesta = this.ifzTyg.salvar(this.pojoFormasDePago);
			if (respuesta.getErrores().getCodigoError() != 0) {
				this.resOperacion = respuesta.getErrores().getDescError();
				return;
			}
			
			this.pojoFormasDePago = (FormasPagos) respuesta.getBody().getValor("pojoFormasPagos");
			if (id <= 0)
				this.listValores.add(0, this.pojoFormasDePago);
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}	
	}

	public void eliminar() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.eliminarFormasPago(this.pojoFormasDePago);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listValores.remove(this.pojoFormasDePago);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar.", e);
		}
	}

	// ---------------------------------------------------------
	// PROPIEDADES
	// ---------------------------------------------------------
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public boolean isRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(boolean registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public FormasPagos getPojoFormasDePago() {
		return pojoFormasDePago;
	}

	public void setPojoFormasDePago(FormasPagos pojoFormasDePago) {
		this.pojoFormasDePago = pojoFormasDePago;
	}

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public List<FormasPagos> getListValores() {
		return listValores;
	}

	public void setListValores(List<FormasPagos> listValores) {
		this.listValores = listValores;
	}

	public String getCtaBancariaDep() {
		return ctaBancariaDep;
	}

	public void setCtaBancariaDep(String ctaBancariaDep) {
		this.ctaBancariaDep = ctaBancariaDep;
	}
}

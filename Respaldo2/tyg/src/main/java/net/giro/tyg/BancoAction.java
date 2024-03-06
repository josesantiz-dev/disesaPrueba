package net.giro.tyg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.PropertyResourceBundle;

import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.logica.TygRem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.*;


@KeepAlive
public class BancoAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BancoAction.class);
	private LoginManager loginManager;

	// el pojobanco viene siendo lo que es nuestra clase que se ocupara para
	// poder instanciar los objetos de la misma
	private Banco pojoBanco;

	/*
	 * Interface que nos trae los metodos de la interface tygRem que la misma
	 * hace referencia a la clase que que implementa todos los metodos qu8e
	 * seran ocupados aqui
	 */
	private TygRem ifzTyg;

	private boolean registroSeleccionado;
	private List<Banco> listBancos;
	private List<String> tipoBusqueda;
	private String campoBusqueda;
	private String valTipoBusqueda;
	private String resOperacion;
	private String problemInesp;
	private String ctaBancariaDep;
	private long usuarioId;
	private int numPagina;
	private String busquedaVacia;
	

	public BancoAction() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			PropertyResourceBundle propPlataforma = (PropertyResourceBundle) dato.getValue(fc.getELContext());
			
			loginManager = (LoginManager) ve.getValue(fc.getELContext());
			usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
			problemInesp = propPlataforma.getString("mensaje.error.inesperado");
			dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
			propPlataforma = (PropertyResourceBundle) dato.getValue(fc.getELContext());
			busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");		
			pojoBanco = new Banco();
			listBancos = new ArrayList<Banco>();
			this.numPagina = 1;
			tipoBusqueda = new ArrayList<String>();
			tipoBusqueda.add("nombreCorto");
			tipoBusqueda.add("nombreLargo");		
			valTipoBusqueda = "nombreCorto";

			InitialContext ctx = new InitialContext(); // loginManager.getCtx();
			// parte del codigo que nos dira a donde esta haciendo referencia la interfaz
			ifzTyg = (TygRem) ctx.lookup("ejb:/Logica_TYG//TygFacade!net.giro.tyg.logica.TygRem");
			ifzTyg.setInfoSesion(loginManager.getInfoSesion());
		} catch (Exception e) {
			log.error("Error en BancoAction", e);
		}
	}

	// metodo nuevo
	public void nuevo() {
		try {
			this.pojoBanco = new Banco();
			this.registroSeleccionado = false;
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo nuevo.", e);
		}
	}

	// metodo eliminar
	public void eliminar() {		
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.eliminarBancos(this.pojoBanco);
			if(respuesta.getErrores().getCodigoError() == 0)
				this.listBancos.remove(this.pojoBanco);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar.", e);
		}
	}

	/*
	 * metodo guardar guarda cada uno de los datos que requiere el pojo o que
	 * contiene haciendo ciertas validaciones
	 */
	public long guardar() {
		try {
			long id = this.pojoBanco.getId();
			this.resOperacion = "";

			Respuesta respuesta = ifzTyg.salvar(pojoBanco);
			if(respuesta.getErrores().getCodigoError() == 0){
				pojoBanco = (Banco)respuesta.getBody().getValor("pojoBanco");
				if(id <= 0)
					this.listBancos.add(0, pojoBanco);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}
		
		return 1;
	}

	@SuppressWarnings("unchecked")
	public void buscar() {
		try {
			this.resOperacion = "";
			
			Respuesta respuesta = this.ifzTyg.buscarBanco(this.valTipoBusqueda,this.campoBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0){
				this.listBancos = (List<Banco>)respuesta.getBody().getValor("listBancos");
				if (this.listBancos != null && this.listBancos.isEmpty()) 
					this.resOperacion = busquedaVacia;
			}else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public String editar() {
		this.registroSeleccionado = true;
		return "OK";
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public boolean isRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(boolean registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public Banco getPojoBanco() {
		return pojoBanco;
	}

	public void setPojoBanco(Banco pojoBanco) {
		this.pojoBanco = pojoBanco;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public List<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(List<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
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

	public String getCtaBancariaDep() {
		return ctaBancariaDep;
	}

	public void setCtaBancariaDep(String ctaBancariaDep) {
		this.ctaBancariaDep = ctaBancariaDep;
	}

	public List<Banco> getListBancos() {
		return listBancos;
	}

	public void setListBancos(List<Banco> listBancos) {
		this.listBancos = listBancos;
	}
}

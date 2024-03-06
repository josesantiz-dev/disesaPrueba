package cde.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import net.giro.clientes.beans.CtasBancoExt;
import net.giro.clientes.beans.EstructuraImportacionCuentaExt;
import net.giro.clientes.logica.EstructuraPagoRem;
import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.util.clientes.Errores;

public class EstructuraPagoAction implements Serializable{
	private static final long serialVersionUID = -6616766086782903344L;
	
	Logger log = Logger.getLogger(EstructuraPagoAction.class);
	
	private Context ctx;
	@SuppressWarnings("unused")
	private Object lookup;	
	LoginManager loginManager;
	
	private int	numPagina;
	
	@SuppressWarnings("unused")
	private HttpSession httpSession;
	
	private String resOperacion;
	private String problemInesp;
	@SuppressWarnings("unused")
	private String busquedaVacia;
	
	@SuppressWarnings("unused")
	private String ftpHost;
	@SuppressWarnings("unused")
	private String ftpUser;
	@SuppressWarnings("unused")
	private String ftpPassword;
	@SuppressWarnings("unused")
	private String ftpRuta;
	@SuppressWarnings("unused")
	private String ftpPort;
	
	@SuppressWarnings("unused")
	private String problemGetFtp;
	@SuppressWarnings("unused")
	private String problemPutFtp;
	
	EstructuraPagoRem ifzEstructuraPago;
	
	List<EstructuraImportacionCuentaExt> listEstructuras;
	List<CtasBancoExt> listCuentasBanco;
	
	List<SelectItem> listTmpCuentasBanco;
	
	EstructuraImportacionCuentaExt pojoEstructuraImportacion;
	
	
	
	public EstructuraPagoAction() throws Exception{
		try{
			ctx = new InitialContext();		
			
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			loginManager = (LoginManager)ve.getValue(fc.getELContext());
			ctx = loginManager.getCtx();
			
			//obtengo los posibles mensajes a mostrar al usuario
			ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
			problemInesp = propPlataforma.getString("mensajes.error.inesperado");
			busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");
			
			ValueExpression datoEntorno = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			PropertyResourceBundle propEntorno = (PropertyResourceBundle)datoEntorno.getValue(fc.getELContext());
			
			ftpHost = propEntorno.getString("ftp_host");
			ftpUser = propEntorno.getString("ftp_user");
			ftpPassword = propEntorno.getString("ftp_password");
			ftpRuta = propEntorno.getString("ftp_ruta");
			ftpPort = propEntorno.getString("ftp_port");

			problemGetFtp = propPlataforma.getString("mensajes.error.obtenerArchivoFpt");
			problemPutFtp = propPlataforma.getString("mensajes.error.enviarArchivoFpt");
			
			ifzEstructuraPago = (EstructuraPagoRem)ctx.lookup("ejb:/Logica_Clientes//EstructuraPagoFac!net.giro.clientes.logica.EstructuraPagoRem");
			ifzEstructuraPago.setInfoSesion(loginManager.getInfoSesion());

			numPagina = 1;
			
			listEstructuras = new ArrayList<EstructuraImportacionCuentaExt>();
			listCuentasBanco = new ArrayList<CtasBancoExt>();
			
			listTmpCuentasBanco = new ArrayList<SelectItem>();
			
			pojoEstructuraImportacion = new EstructuraImportacionCuentaExt();
			
			cargarCuentasBanco();
		} catch (Exception e) {
			log.error("Error al crear contexto de recuperacion cartera", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarEstructurasImportacion(){
		try{
			this.resOperacion = "";
			
			Respuesta respuesta = ifzEstructuraPago.cargarEstructurasImportacion();
				
			if(respuesta.getErrores().getCodigoError() == 0L){
				listEstructuras = (List<EstructuraImportacionCuentaExt>) respuesta.getBody().getValor("listEstructuras");
			} else 
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al cargar estructuras de importacion", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void editarEstructura(){
		
	}
	
	public void guardarEstructuraImportacion(){
		try{
			this.resOperacion = "";
			long id = pojoEstructuraImportacion.getId();
			
			Respuesta respuesta = ifzEstructuraPago.guardarEstructuraImportacion(pojoEstructuraImportacion);
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoEstructuraImportacion = (EstructuraImportacionCuentaExt) respuesta.getBody().getValor("pojoEstructuraImportacion");
				
				if(id <= 0L) {
					listEstructuras.add(pojoEstructuraImportacion);
				} else {
					for(EstructuraImportacionCuentaExt var : listEstructuras){
						if(var.getId() == id){
							listEstructuras.remove(var);
							break;
						}
					}
					
					this.pojoEstructuraImportacion = (EstructuraImportacionCuentaExt) respuesta.getBody().getValor("pojoEstructuraImportacion");
					this.listEstructuras.add(0, pojoEstructuraImportacion);
				}
			} else {
				this.resOperacion = Errores.descError.get(respuesta.getErrores().getErrores().get(0).getCodigoError()); 
				//this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			log.error("Error al guardar estructura de importacion", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void eliminarEstructuraImportacion(){
		try{
			this.resOperacion = "";
			
			Respuesta respuesta = ifzEstructuraPago.eliminarEstructuraImportacion(pojoEstructuraImportacion);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listEstructuras.remove(pojoEstructuraImportacion);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al eliminar estructura de importacion", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarCuentasBanco(){
		try{
			this.resOperacion = "";
			
			Respuesta respuesta = ifzEstructuraPago.listarCtasBancos();
			if(respuesta.getErrores().getCodigoError() == 0L){
				listCuentasBanco = (List<CtasBancoExt>) respuesta.getBody().getValor("listCuentas");
				
				if(listTmpCuentasBanco == null)
					listTmpCuentasBanco = new ArrayList<SelectItem>();
				
				if(listTmpCuentasBanco.size() > 0)
					listTmpCuentasBanco.clear();
				
				for(CtasBancoExt cuenta : listCuentasBanco){
					listTmpCuentasBanco.add(new SelectItem(cuenta.getId(), cuenta.getInstitucionBancaria().getNombreCorto() + " " + cuenta.getNumeroDeCuenta()));
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			
		}
	}
	
	public void nuevoEstructura(){
		pojoEstructuraImportacion = new EstructuraImportacionCuentaExt();
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public List<EstructuraImportacionCuentaExt> getListEstructuras() {
		return listEstructuras;
	}

	public void setListEstructuras(
			List<EstructuraImportacionCuentaExt> listEstructuras) {
		this.listEstructuras = listEstructuras;
	}

	public EstructuraImportacionCuentaExt getPojoEstructuraImportacion() {
		return pojoEstructuraImportacion;
	}

	public void setPojoEstructuraImportacion(
			EstructuraImportacionCuentaExt pojoEstructuraImportacion) {
		this.pojoEstructuraImportacion = pojoEstructuraImportacion;
	}

	public List<SelectItem> getListTmpCuentasBanco() {
		return listTmpCuentasBanco;
	}

	public void setListTmpCuentasBanco(List<SelectItem> listTmpCuentasBanco) {
		this.listTmpCuentasBanco = listTmpCuentasBanco;
	}
}

package net.giro.cxp.cajachica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.Gastos;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.SucursalExt;
import net.giro.cxp.logica.CajaChicaRem;
import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.logica.CtasBancoRem;

import org.apache.log4j.Logger;

public class CajaChicaMovimientosAction implements Serializable{
	private static final long serialVersionUID = 1L;
	
	Logger log = Logger.getLogger(CajaChicaMovimientosAction.class);
	
	private Context ctx;	
	LoginManager loginManager;
    private Integer usuarioId;

	List<String> tipoBusqueda;
	String valorBusqueda;

	List<String> listTipoBusquedaCuentasBanco;
	String tipoBusquedaCuentasBanco;
	String valorBusquedaCuentasBanco;
	
	String tipoBusquedaSucursales;
	String valorBusquedaSucursales;
	
	String tipoBusquedaPersonas;
	String valorBusquedaPersonas;
	
	String resOperacion;
	String problemInesp;
	String busquedaVacia;
	
	String tipoPersona;
	
	int numPagina;
	
	List<PagosGastosExt> listMovimientosCuentas;
	List<Gastos> listGastos;
	List<CtasBancoExt> listBusquedaCuentasBanco;
	List<PersonaExt> listBusquedaPersonas;
	List<SucursalExt> listBusquedaSucursales;
	
	PagosGastosExt pojoMovimientosCuentas;
	Gastos pojoGastos;
	
	CajaChicaRem ifzCajaChica;
	CtasBancoRem ifzCtas;
	
	int numPaginaBusquedaCuentasBanco;
	int numPaginaBusquedaPersonas;
	int numPaginaBusquedaSucursales;
	@SuppressWarnings("unused")
	private int tipoMensaje;

	// Agregadas temporalmente para ver funcionar la vista
	String encontroMismoGrupo;
	String mensaje;
	String campoBusqueda;
	
	CtasBancoExt pojoCtasBancoExt;
	
	public CajaChicaMovimientosAction() throws Exception{
		try{
			ctx = new InitialContext();
			
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			loginManager = (LoginManager)ve.getValue(fc.getELContext());
			ctx = loginManager.getCtx();
			
			ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
			problemInesp = propPlataforma.getString("mensaje.error.inesperado");
			busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");
			
			this.usuarioId = (int) loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			this.pojoMovimientosCuentas = new PagosGastosExt();			
			this.pojoMovimientosCuentas.setOperacion("2");
			this.pojoGastos = new Gastos();
			
			this.listMovimientosCuentas = new ArrayList<PagosGastosExt>();
			this.listGastos = new ArrayList<Gastos>();
			this.listBusquedaCuentasBanco = new ArrayList<CtasBancoExt>();
			this.listBusquedaPersonas = new ArrayList<PersonaExt>();
			this.listBusquedaSucursales = new ArrayList<SucursalExt>();
			
			// Busqueda principal
			this.tipoBusqueda = new ArrayList<String>();
			this.tipoBusqueda.add("Fecha");
			this.tipoBusqueda.add("Beneficiario");
			this.tipoBusqueda.add("Cuenta bancaria");
			this.campoBusqueda = tipoBusqueda.get(0);
			
			// Busqueda Cuentas Bancarias
			this.listTipoBusquedaCuentasBanco = new ArrayList<String>();
			this.listTipoBusquedaCuentasBanco.add("Numero de cuenta");
			this.listTipoBusquedaCuentasBanco.add("CLABE");
			this.tipoBusquedaCuentasBanco = "";
			this.valorBusquedaCuentasBanco = "";
			this.pojoCtasBancoExt = new CtasBancoExt();

			tipoBusquedaSucursales = "sucursal";
			
			numPagina = 1;
			numPaginaBusquedaCuentasBanco = 1;
			numPaginaBusquedaPersonas = 1;
			
			this.ifzCajaChica = (CajaChicaRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//CajaChicaFac!net.giro.cxp.logica.CajaChicaRem");
			this.ifzCtas = (CtasBancoRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//CtasBancoFac!net.giro.tyg.logica.CtasBancoRem");
		} catch (Exception e) {
			log.error("Error al crear contexto", e);
		}
	}

	public String getEncontroMismoGrupo() {
		return encontroMismoGrupo;
	}

	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

 	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}
	
	@SuppressWarnings("unchecked")
	public void buscar(){
		try{
			this.resOperacion = "";
			
			String objetoBusqueda = "";
			if(this.campoBusqueda.equals("Beneficiario"))
				objetoBusqueda = "beneficiario";
			else if (this.campoBusqueda.equals("Fecha"))
				objetoBusqueda = "fecha"; // objetoBusqueda = new Date(); // valorBusquedaFecha;
			else if (this.campoBusqueda.equals("Cuenta bancaria"))
				objetoBusqueda = "numeroDeCuenta"; // objetoBusqueda = valorBusquedaCuenta;

			System.out.println("---> VISTA: buscar :: objetoBusqueda: " + objetoBusqueda + " :: valorBusqueda: " + this.valorBusqueda);
			Respuesta respuesta = this.ifzCajaChica.buscarMovimientosCuentas(objetoBusqueda, this.valorBusqueda);
			//Respuesta respuesta = ifzCajaChica.buscarMovimientosCuentas(tipoBusqueda, objetoBusqueda);
			
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listMovimientosCuentas = (List<PagosGastosExt>) respuesta.getBody().getValor("listMovimientosCuentas");
			else 
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e){
			log.error("Error al buscar movimientos", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarCuentasBanco(){
		try{
			//String campoBusqueda = ("Numero Cuenta".equals(this.tipoBusquedaCuentasBanco)) ? "numeroDeCuenta" : ("CLABE".equals(this.tipoBusquedaCuentasBanco) ? "clabe" : "numeroDeCuenta");
			System.out.println("---------------------------------------------------------------");
			System.out.println("---> VISTA: BUSCAR CUENTAS BANCARIAS");
			System.out.println("---> tipoBusquedaCuentasBanco  : " + this.tipoBusquedaCuentasBanco);
			System.out.println("---> valorBusquedaCuentasBanco : " + this.valorBusquedaCuentasBanco);
			Respuesta respuesta = this.ifzCajaChica.buscarCuentasBanco(this.tipoBusquedaCuentasBanco, this.valorBusquedaCuentasBanco);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.listBusquedaCuentasBanco = (List<CtasBancoExt>) respuesta.getBody().getValor("listCuentasBanco");
				System.out.println("---> listBusquedaCuentasBanco : " + this.listBusquedaCuentasBanco.size());
			} else {
				this.resOperacion = respuesta.getErrores().getDescError();
				System.out.println("---> ERROR : " + this.resOperacion);
			}
		} catch (Exception e) {
			log.error("Error al buscar cuentas de banco", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarPersonas(){
		try{
			Respuesta respuesta = ifzCajaChica.buscarPersonas(tipoBusquedaPersonas, valorBusquedaPersonas);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listBusquedaPersonas = (List<PersonaExt>) respuesta.getBody().getValor("listPersonas");
			} else 
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al buscar personas", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarSucursales(){
		try{
			Respuesta respuesta = ifzCajaChica.buscarSucursales(tipoBusquedaSucursales, valorBusquedaSucursales);
			
			if(respuesta.getErrores().getCodigoError() == 0L)
				listBusquedaSucursales = (List<SucursalExt>) respuesta.getBody().getValor("listSucursales");
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al buscar sucursales", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void editar(){
		
	}
	
	public void eliminar(){
		
	}
	
	public String cancelar() {
		try{
			Respuesta resp = new Respuesta();
			
			System.out.println("---> Cancelar");
			System.out.println("---> Beneficiario     : " + pojoMovimientosCuentas.getBeneficiario());
			System.out.println("---> TipoBeneficiario : " + pojoMovimientosCuentas.getTipoBeneficiario());
			
			resp = this.ifzCajaChica.cancelacion(this.pojoMovimientosCuentas, Calendar.getInstance().getTime(),Short.valueOf(String.valueOf(this.usuarioId)), null);
				
			if(resp.getResultado() == -1){
				this.mensaje = resp.getRespuesta();
				this.tipoMensaje=1;
			} else {
				for(PagosGastosExt var:this.listMovimientosCuentas){				
					if(var.getId().equals(this.pojoMovimientosCuentas.getId())){		
						var.setEstatus("X");					
						break;
					}
				}	
			}
		} catch(Exception e) {
			mensaje = "";
			tipoMensaje = 5;
			log.error("error en cancelar", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public void guardar(){
		try{
			Respuesta res;
			boolean  registroNuevo = false;
			
			this.tipoMensaje = 0;
			
			if(this.pojoMovimientosCuentas.getId() == null)
				registroNuevo = true;
			
			this.pojoMovimientosCuentas.setModificadoPor(Short.valueOf(String.valueOf(this.usuarioId)));
			this.pojoMovimientosCuentas.setFechaModificacion(Calendar.getInstance().getTime());
			
			//NOTA, nunca es operacion "C" o "G", siempre es "2"...
			if (this.pojoMovimientosCuentas.getOperacion() == "C") {
				this.pojoMovimientosCuentas.setConcepto("Reposicion de Caja chica");
			} else if(this.pojoMovimientosCuentas.getOperacion()== "G") {
				this.pojoMovimientosCuentas.setConcepto("Comprobacion de Caja Chica");
			} else {
				this.pojoMovimientosCuentas.setConcepto("Reposicion y Comprobacion de Caja Chica");
			}

			if(registroNuevo){	
				this.pojoMovimientosCuentas.setCreadoPor(Short.valueOf(String.valueOf(this.usuarioId)));
				this.pojoMovimientosCuentas.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoMovimientosCuentas.setTipo("M");
				this.pojoMovimientosCuentas.setEstatus("G");
				
				res = this.ifzCajaChica.salvar(this.pojoMovimientosCuentas,false, null);
				
				if(res.getResultado() == 0){
					this.pojoMovimientosCuentas.setId(Long.valueOf(res.getReferencia()));
					this.listMovimientosCuentas.add(this.pojoMovimientosCuentas);
				}
				
				if (res.getResultado() == 5) {
					this.tipoMensaje = 5;
				}
			} else {
				this.ifzCajaChica.actualizar(this.pojoMovimientosCuentas, "G", Calendar.getInstance().getTime(), Short.valueOf(String.valueOf(this.usuarioId)), null);
				
				for(PagosGastosExt var : this.listMovimientosCuentas){
					if(var.getId().equals(this.pojoMovimientosCuentas.getId())){
						var = this.pojoMovimientosCuentas;
						break;
					}
				}
			}
		}catch(Exception e){
			mensaje="";
			tipoMensaje = 3;
			log.error("error al guardar", e);
		}
	}
	
	public void nuevo(){
		try{
			this.pojoMovimientosCuentas = new PagosGastosExt();
			this.pojoMovimientosCuentas.setOperacion("2");
		} catch (Exception e) {
			
		}
	}

	public String evaluaCancelar(){
   		return "OK";
   	}
	
	public void seleccionarCuentaBanco(){
		System.out.println("---> VISTA: seleccionarCuentaBanco: " + this.pojoMovimientosCuentas.getIdCuentaOrigen().getNumeroDeCuenta());
	}
	
	public void seleccionarCuentaBancoBusqueda(){
		
	}
	
	public void seleccionarPersona(){
		System.out.println("---> VISTA: seleccionarPersona: " + this.pojoMovimientosCuentas.getIdBeneficiario().getNombre());
	}
	
	public void seleccionarSucursal(){
		System.out.println("---> VISTA: seleccionarSucursal: " + this.pojoMovimientosCuentas.getIdSucursal().getSucursal());
	}

	public List<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(List<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public List<PagosGastosExt> getListMovimientosCuentas() {
		return listMovimientosCuentas;
	}

	public void setListMovimientosCuentas(List<PagosGastosExt> listMovimientosCuentas) {
		this.listMovimientosCuentas = listMovimientosCuentas;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public PagosGastosExt getPojoMovimientosCuentas() {
		return this.pojoMovimientosCuentas;
	}

	public void setPojoMovimientosCuentas(PagosGastosExt pojoMovimientosCuentas) {
		this.pojoMovimientosCuentas = pojoMovimientosCuentas;
		this.tipoPersona = this.pojoMovimientosCuentas.getTipoBeneficiario();
	}

	public List<CtasBancoExt> getListBusquedaCuentasBanco() {
		return this.listBusquedaCuentasBanco;
	}

	public void setListBusquedaCuentasBanco(List<CtasBancoExt> listBusquedaCuentasBanco) {
		this.listBusquedaCuentasBanco = listBusquedaCuentasBanco;
	}

	public int getNumPaginaBusquedaCuentasBanco() {
		return numPaginaBusquedaCuentasBanco;
	}

	public void setNumPaginaBusquedaCuentasBanco(int numPaginaBusquedaCuentasBanco) {
		this.numPaginaBusquedaCuentasBanco = numPaginaBusquedaCuentasBanco;
	}

	public String getTipoBusquedaPersonas() {
		return tipoBusquedaPersonas;
	}

	public void setTipoBusquedaPersonas(String tipoBusquedaPersonas) {
		this.tipoBusquedaPersonas = tipoBusquedaPersonas;
	}

	public String getValorBusquedaPersonas() {
		return valorBusquedaPersonas;
	}

	public void setValorBusquedaPersonas(String valorBusquedaPersonas) {
		this.valorBusquedaPersonas = valorBusquedaPersonas;
	}

	public List<PersonaExt> getListBusquedaPersonas() {
		return listBusquedaPersonas;
	}

	public void setListBusquedaPersonas(List<PersonaExt> listBusquedaPersonas) {
		this.listBusquedaPersonas = listBusquedaPersonas;
	}

	public int getNumPaginaBusquedaPersonas() {
		return numPaginaBusquedaPersonas;
	}

	public void setNumPaginaBusquedaPersonas(int numPaginaBusquedaPersonas) {
		this.numPaginaBusquedaPersonas = numPaginaBusquedaPersonas;
	}

	public List<SucursalExt> getListBusquedaSucursales() {
		return listBusquedaSucursales;
	}

	public void setListBusquedaSucursales(List<SucursalExt> listBusquedaSucursales) {
		this.listBusquedaSucursales = listBusquedaSucursales;
	}

	public String getValorBusquedaSucursales() {
		return valorBusquedaSucursales;
	}

	public void setValorBusquedaSucursales(String valorBusquedaSucursales) {
		this.valorBusquedaSucursales = valorBusquedaSucursales;
	}

	public int getNumPaginaBusquedaSucursales() {
		return numPaginaBusquedaSucursales;
	}

	public void setNumPaginaBusquedaSucursales(int numPaginaBusquedaSucursales) {
		this.numPaginaBusquedaSucursales = numPaginaBusquedaSucursales;
	}
	
	public List<String> getListTipoBusquedaCuentasBanco() {
		return this.listTipoBusquedaCuentasBanco;
	}
	
	public void setListTipoBusquedaCuentasBanco(List<String> listTipoBusquedaCuentasBanco) {
		this.listTipoBusquedaCuentasBanco = listTipoBusquedaCuentasBanco;
	}
	
	public String getTipoBusquedaCuentasBanco() {
		return this.tipoBusquedaCuentasBanco;
	}

	public void setTipoBusquedaCuentasBanco(String tipoBusquedaCuentasBanco) {
		this.tipoBusquedaCuentasBanco = tipoBusquedaCuentasBanco;
	}

	public String getValorBusquedaCuentasBanco() {
		return this.valorBusquedaCuentasBanco;
	}

	public void setValorBusquedaCuentasBanco(String valorBusquedaCuentasBanco) {
		this.valorBusquedaCuentasBanco = valorBusquedaCuentasBanco;
	}
	
	public CtasBancoExt getPojoCtasBancoExt() {
		return this.pojoCtasBancoExt;
	}
	
	public void setPojoCtasBancoExt(CtasBancoExt pojoCtasBancoExt) {
		this.pojoCtasBancoExt = pojoCtasBancoExt;
	}
	
	public String getTipoPersona() {
		return this.tipoPersona;
	}
	
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	
	public String cambioBeneficiario() {
		this.pojoMovimientosCuentas.setIdBeneficiario(null);
		return "OK";
	}
}

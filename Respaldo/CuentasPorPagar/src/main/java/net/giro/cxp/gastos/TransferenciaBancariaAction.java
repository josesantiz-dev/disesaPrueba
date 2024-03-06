package net.giro.cxp.gastos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.logica.CtasBancoRem;
import net.giro.tyg.logica.MonedasValoresRem;

import org.apache.log4j.Logger;

public class TransferenciaBancariaAction {
	private static Logger log = Logger.getLogger(TransferenciaBancariaAction.class);
	
	private ConGrupoValores pojoGpoVal; 
	private PersonaExt pojoValores;
	private Persona pojoValoresDestino;
	private PagosGastosExt pojoMovCtas;
	private CtasBancoExt pojoCtasBanco;
	private CtasBancoExt pojoCtasBancoDestino;
	private Moneda pojoMoneda;
	
	private CtasBancoRem ifzBancos;
	private PagosGastosRem ifzMovCta;
	private MonedasValoresRem ifzTipoCambio;
	//private MonedaRem ifzMoneda;
	private PersonaRem ifzPersonas;
	//private PolizasContablesFacadeRemote	ifzPolizas;
	
	private List<Persona>	listValores;
	private List<PagosGastosExt> listMovCtas;
	private List<CtasBanco> listBancos;
	private List<MonedasValores> listTipoCambio;
	
	private List<String>	tipoBusqueda;
	
	private InitialContext ctx;
	
	private String campoBusqueda;	
	private String valTipoBusqueda;
    private String suggCtaBancaria;
    private String suggCtaDestino;
    private String suggCtaPropios;
    private String tipoDestino;
    private String varOp;
	private String busquedaVacia;
	private String invalidMonto;
	private String errorCta;
	private String msgError;
	private String suggMoneda;
	private String invalidTipoCambio;
	private String sucursalesVisibles;
	private String problemInesp;
	private String invalidSaldo;
	
    private long usuarioId;	
    private double varMonto;
    private double tipoCambio;
 
    private boolean regNuevo;
	private boolean mensajeError;
	private boolean muestraPropios;
	private boolean muestraTercerosOrigen;
	private boolean muestraPropiosOrigen;
	private boolean muestraSalvar;
	private boolean muestraTipoCambio;

	private int numPagina;

	public TransferenciaBancariaAction() throws NamingException,Exception{
		this.ctx = new InitialContext();
		
		this.pojoValores = new PersonaExt();
		this.pojoMovCtas = new PagosGastosExt();
		this.pojoCtasBanco = new CtasBancoExt();
		this.pojoGpoVal = new ConGrupoValores();
		
		listValores = new ArrayList<Persona>();
		listMovCtas = new ArrayList<PagosGastosExt>();
		
		this.numPagina = 1;

		this.ifzPersonas 	= (PersonaRem) 			this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
		this.ifzMovCta 		= (PagosGastosRem)		this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
		this.ifzBancos 		= (CtasBancoRem) 		this.ctx.lookup("ejb:/Logica_TYG//CtasBancoFac!net.giro.tyg.logica.CtasBancoRem");
		this.ifzTipoCambio 	= (MonedasValoresRem)	this.ctx.lookup("ejb:/Logica_TYG//MonedasValoresFac!net.giro.tyg.logica.MonedasValoresRem");
		//this.ifzMoneda 		= (MonedaRem) 			this.ctx.lookup("ejb:/Logica_TYG//MonedaFac!net.giro.tyg.logica.MonedaRem");
		//this.this.ifzPolizas = (PolizasContablesFacadeRemote) PortableRemoteObject.narrow(lookup, PolizasContablesFacadeRemote.class);
		
		mensajeError = false;
		this.muestraPropiosOrigen = true;
		this.muestraPropios = true;
		this.muestraTercerosOrigen = false;
		this.muestraSalvar = false;
		this.muestraTipoCambio = false;
		this.msgError = "";
        this.tipoDestino = "1";
		tipoBusqueda = new ArrayList<String>();
		tipoBusqueda.add("Fecha");
		tipoBusqueda.add("Cuenta Origen");
		tipoBusqueda.add("Cuenta Destino");
		tipoBusqueda.add("Referencia");
		this.valTipoBusqueda = this.tipoBusqueda.get(1);
	
		this.regNuevo = false;
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager)ve.getValue(fc.getELContext());
		ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		
		this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId(); //lm.getLoginBean().getUsuario().getUsuarioId();
		//sucursalesVisibles = lm.getLoginBean().getSucursalesVisibles();
		
		/*PropertyResourceBundle propPlataforma = (PropertyResourceBundle)ve.getValue(fc.getELContext());
		busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");
		invalidMonto = 	propPlataforma.getString("navegacion.label.montoInvalido");
		invalidSaldo = propPlataforma.getString("navegacion.label.saldoInsuficiente");
		invalidTipoCambio = 	propPlataforma.getString("navegacion.label.tipoCambioInvalido");
		errorCta = 	propPlataforma.getString("navegacion.label.errorCta");
		
		ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle)ve.getValue(fc.getELContext());

		problemInesp 	=	propPlataforma.getString("mensajes.error.inesperado");*/
	}
	
	public String nuevo(){
		try{
			this.pojoMovCtas = new PagosGastosExt();
			this.mensajeError = false;	
			this.regNuevo = true;
			this.muestraTipoCambio = false;
			this.tipoCambio = 0;
			this.tipoDestino = "1";
			this.varOp = "2";
			this.varMonto = Double.valueOf(0);
			this.muestraSalvar = true;
			this.suggCtaBancaria = "";
			this.suggCtaDestino = "";
			this.suggCtaPropios = "";
		}catch(Exception e){
			log.error("Error en el metodo nuevo.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String buscar(){
		try{
			this.listMovCtas.clear();
			if( "Fecha".equals(this.valTipoBusqueda))					
				this.listMovCtas = this.ifzMovCta.findTransferenciasExt("fecha",this.campoBusqueda,"T",0, this.sucursalesVisibles);	
			
			else if("Cuenta Origen".equals(this.valTipoBusqueda))
				if("1".equals(this.tipoDestino))
					this.listMovCtas = this.ifzMovCta.findTransferenciasExt("cuentaOrigen",this.campoBusqueda,"T",0, this.sucursalesVisibles);	
				else
					this.listMovCtas =this.ifzMovCta.findTransferenciasExt("cuentaOrigenTerceros",this.campoBusqueda,"T",0, this.sucursalesVisibles);

			else if("Cuenta Destino".equals(this.valTipoBusqueda))
				this.listMovCtas =this.ifzMovCta.findTransferenciasExt("cuentaDestino",this.campoBusqueda,"T",0, this.sucursalesVisibles);
			
			else if("Referencia".equals(this.valTipoBusqueda))
				this.listMovCtas =this.ifzMovCta.findTransferenciasExt("Referencia",this.campoBusqueda,"T",0, this.sucursalesVisibles);
			
			if (this.listMovCtas.isEmpty())
				this.msgError = this.busquedaVacia;
			else
				this.msgError = "";
		} catch(Exception e) {		
			this.msgError = this.problemInesp;
			log.error("error al buscar", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String validaMonto (){
		try{
			if(muestraPropiosOrigen == true){
				msgError = "";
				validaCtaOrigen();
				if(varMonto <= 0)
					msgError = this.invalidMonto;
				if (this.pojoCtasBanco.getId() != 0L)
					if(!ifzBancos.haySaldoSuficiente(varMonto, this.pojoCtasBanco.getId()))
						msgError = this.invalidSaldo;
			}
		} catch(Exception e) {
			this.msgError = this.problemInesp;
			log.error("Error en el metodo valida monto.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String validaCtaOrigen (){
		try{
			Pattern pat = null;
			Matcher match = null;
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			
			//validando Cuenta origen
			match = pat.matcher(suggCtaBancaria);			
			if(match.find()){
				if(regNuevo)
					if(this.muestraPropiosOrigen == true)
						this.pojoCtasBanco = this.ifzMovCta.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzBancos.findAllById(Short.valueOf(match.group(1)));
					else
						this.pojoValores = this.ifzMovCta.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				else
					if(this.muestraPropiosOrigen == true)
						if (this.pojoCtasBanco.getId() != Short.valueOf(match.group(1)))
							this.pojoCtasBanco = this.ifzMovCta.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzBancos.findAllById(Short.valueOf(match.group(1)));
					else
						if (this.pojoValores.getId() != Long.valueOf(match.group(1)))
							this.pojoValores = this.ifzMovCta.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
			}
		}catch(Exception e){
			this.msgError= this.problemInesp;
			log.error("Error en el metodo validaCtaOrigen.", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public void validaCtaDestino(){
		try{
			Pattern pat = null;
			Matcher match = null;
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			this.msgError = "";
			
			//validando Cuenta destino
			match = pat.matcher(suggCtaDestino);			
			if(match.find()){
				if(regNuevo)
					if(this.muestraPropios == true)
						this.pojoCtasBancoDestino = this.ifzMovCta.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzBancos.findAllById(Short.valueOf(match.group(1)));
					else
						this.pojoValoresDestino = this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				else
					if(this.muestraPropios == true)
						if (this.pojoCtasBancoDestino.getId() != Short.valueOf(match.group(1)))
							this.pojoCtasBancoDestino = this.ifzMovCta.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzBancos.findAllById(Short.valueOf(match.group(1)));
					else
						if (this.pojoValoresDestino.getId() != Long.valueOf(match.group(1)))
							this.pojoValoresDestino = this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				
				if (muestraPropios == true && muestraPropiosOrigen == true)
					if(pojoCtasBanco.getId() == pojoCtasBancoDestino.getId())
						this.msgError = this.errorCta;
			}
		} catch(Exception e) {
			this.msgError = this.problemInesp;
			log.error("Error en el metodo validaCtaDestino.", e);
		}
	}

	public String validaMoneda(){
		try {
			this.msgError = "";
			validaCtaOrigen();
			validaCtaDestino();
			
			if ("".equals(msgError)){
				if (this.muestraPropiosOrigen && pojoCtasBancoDestino != null && pojoCtasBanco != null) {
					if(pojoCtasBancoDestino.getMoneda() != null && pojoCtasBanco.getMoneda() != null)
						muestraTipoCambio = pojoCtasBanco.getMoneda().getId() != pojoCtasBancoDestino.getMoneda().getId();	
					else
						muestraTipoCambio = false;
				} /*else if (pojoCtasBancoDestino != null && pojoValores != null) {
					if(pojoCtasBancoDestino.getMoneda() != null && pojoValores.getMonedaId() != null)
						muestraTipoCambio = !pojoValores.getMonedaId().getClave().equals(pojoCtasBancoDestino.getMoneda().getId());
					else
						muestraTipoCambio = false;
				}*/
			}
		}catch(Exception e){
			this.msgError= this.problemInesp;
			log.error("Error en el metodo validaMoneda.", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public String sugiereTipoCambio(){
		try{
			if(this.muestraPropiosOrigen && this.pojoCtasBanco.getMoneda() != null){
					if (this.pojoCtasBanco.getMoneda().getId() != 1 ){
						if(pojoCtasBancoDestino != null && pojoCtasBanco != null && muestraTipoCambio){
							this.listTipoCambio = ifzTipoCambio.findMonedaAFecha(pojoCtasBanco.getMoneda().getId(), this.pojoMovCtas.getFecha());
							if(!listTipoCambio.isEmpty())
								this.tipoCambio = listTipoCambio.get(0).getValor().doubleValue();
							else
								this.tipoCambio = 0;
						}
					}
			} /*else if(this.muestraTercerosOrigen && this.pojoValores.getMonedaId() != null) {
					if(this.pojoValores.getMonedaId().getClave().intValue() != Integer.valueOf("1")){
						if(pojoCtasBancoDestino != null && pojoValores != null && muestraTipoCambio){
							this.pojoMoneda = ifzMoneda.findById(Short.valueOf(this.pojoValores.getMonedaId().getClave()));
							this.listTipoCambio = ifzTipoCambio.findMonedaAFecha(pojoMoneda, this.pojoMovCtas.getFecha());
						}
						if(!listTipoCambio.isEmpty())
							this.tipoCambio = listTipoCambio.get(0).getValor().doubleValue();
						else
							this.tipoCambio = 0;
					}
				
			}*/
		}catch(Exception e){
			this.msgError= this.problemInesp;
			log.error("Error en el metodo sugiereTipoCambio ", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public String cancelaTrans (){
		Respuesta resp = new Respuesta();
		try{
			resp = ifzMovCta.cancelacion(pojoMovCtas, Calendar.getInstance().getTime(), Short.valueOf(String.valueOf(usuarioId)), null);
			if (resp.getResultado()== 0)
				this.msgError="";
		}catch(Exception e){
			this.msgError = this.problemInesp;
			log.error("Error en el metodo cancelar.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String checaTipo(){
		this.suggCtaBancaria = "";
		this.suggCtaDestino = "";
		this.muestraTipoCambio = false;
		
		try{
			if("3".equals(this.pojoMovCtas.getOperacion())){
				this.muestraPropiosOrigen = false;
				this.muestraTercerosOrigen = true;
				varOp = "3";
				
			} else if("T".equals(this.pojoMovCtas.getOperacion())){
				this.muestraPropiosOrigen = true;
				this.muestraTercerosOrigen = false;
				varOp = "2";
				
			} else if ("2".equals(varOp)){
				this.muestraPropiosOrigen = true;
				this.muestraTercerosOrigen = false;
				
			} else if ("3".equals(varOp)){
				this.muestraPropiosOrigen = false;
				this.muestraTercerosOrigen = true;
			}
			
			this.msgError="";	
		} catch(Exception e) {
			this.msgError=this.problemInesp;
			log.error("Error en el metodo checaTipo", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public List<CtasBanco> autoacompletaCuenta (Object obj){
		try{
			this.listBancos = this.ifzBancos.findLikeClaveNombreCuenta(obj.toString(), 20, this.sucursalesVisibles, null);
			this.msgError="";
			return this.listBancos;
		}catch(Exception re){
			this.msgError=this.problemInesp;
			log.error("Error en autoacompletaCuenta "+ re);
			return new ArrayList<CtasBanco>();
		}
	}
	
	public List<Persona> autoacompletaTerceros (Object obj){
		try{
			this.listValores = this.ifzPersonas.findLikePersonas(obj.toString(),20);
			this.msgError="";
			return this.listValores;
		}catch(RuntimeException re){
			this.msgError=this.problemInesp;
			log.error("Error en autocompletaTerceros "+ re);
			return new ArrayList<Persona>();
		}
	}
	
	public String guardar (){
		Respuesta resp;
		
		try{
			this.msgError= "";
			validaMonto();
			
			if ("".equals(this.msgError)){
				if(this.pojoMovCtas.getId() == null)
					regNuevo=true;
				
				this.pojoMovCtas.setModificadoPor(this.usuarioId);
				this.pojoMovCtas.setFechaModificacion(Calendar.getInstance().getTime());
				
				//validando Cuenta origen
				validaCtaOrigen();
				//validando Cuenta destino
				validaCtaDestino();
				if ("".equals(msgError)){
					if (this.muestraPropiosOrigen == true)
						pojoMovCtas.setIdCuentaOrigen(pojoCtasBanco);
					else
						pojoMovCtas.setIdCuentaOrigenTerceros(pojoValores);
						
					pojoMovCtas.setIdCuentaDestino(pojoCtasBancoDestino);
					
					if (this.muestraTercerosOrigen)
						pojoMovCtas.setOperacion("3");
					else
						pojoMovCtas.setOperacion("T");
					
					if (this.muestraTipoCambio){
						if(this.tipoCambio <= 0){
							msgError= invalidTipoCambio;
							return "OK";
						}
					} else
						tipoCambio = 1;
					
					pojoMovCtas.setMonto(varMonto);
					pojoMovCtas.setTipoCambio(tipoCambio);
					pojoMovCtas.setCreadoPor(this.usuarioId);
					pojoMovCtas.setFechaCreacion(Calendar.getInstance().getTime());
					pojoMovCtas.setTipo("T");
					pojoMovCtas.setEstatus("G");

					resp = ifzMovCta.salvar(pojoMovCtas, true, null);
					if (resp.getResultado() == 0){
						this.listMovCtas.add(pojoMovCtas);
						//ifzPolizas.contabilizar(pojoMovCtas, pojoMovCtas.getCuentaOrigen().getEmpresa().getClave());
						msgError = "";
						regNuevo = false;
						muestraSalvar = false;
					} else 
						msgError = this.problemInesp;
				}
			}
		}catch(Exception e){
			this.msgError=this.problemInesp;
			log.error("error al guardar", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String editar(){
		this.muestraSalvar = false;
		this.regNuevo = false;
		return "OK";
	}
	
	public ConGrupoValores getPojoGpoVal() {
		return pojoGpoVal;
	}
	
	public void setPojoGpoVal(ConGrupoValores pojoGpoVal) {
		this.pojoGpoVal = pojoGpoVal;
	}
	
	public PersonaExt getPojoValores() {
		return pojoValores;
	}
	
	public void setPojoValores(PersonaExt pojoValores) {
		this.pojoValores = pojoValores;
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
	
	public long getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public boolean isregNuevo() {
		return regNuevo;
	}
	
	public void setregNuevo(boolean regNuevo) {
		this.regNuevo = regNuevo;
	}
	
	public boolean isMensajeError() {
		return mensajeError;
	}
	
	public void setMensajeError(boolean mensajeError) {
		this.mensajeError = mensajeError;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public List<Persona> getListValores() {
		return listValores;
	}

	public void setListValores(List<Persona> listValores) {
		this.listValores = listValores;
	}

	public List<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(List<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public Long getPagosGastosId() {
		return pojoMovCtas.getId() != null ? pojoMovCtas.getId():0;
	}

	public void setPagosGastosId(Long pagosGastosId) {
		this.pojoMovCtas.setId(pagosGastosId);
	}

	public CtasBancoExt getCuentaOrigen() {
		return pojoMovCtas.getIdCuentaOrigen() != null ? pojoMovCtas.getIdCuentaOrigen():null;
	}

	public void setCuentaOrigen(CtasBancoExt cuentaOrigen) {
		this.pojoMovCtas.setIdCuentaOrigen(cuentaOrigen);
	}

	public Date getFecha() {
		return pojoMovCtas.getFecha();
	}

	public void setFecha(Date fecha) {
		this.pojoMovCtas.setFecha(fecha);
	}

	public String getTipo() {
		return pojoMovCtas.getTipo() != null ? pojoMovCtas.getTipo():"";
	}

	public void setTipo(String tipo) {
		this.pojoMovCtas.setTipo(tipo);
	}

	public String getEstatus() {
		return pojoMovCtas.getEstatus() != null ? pojoMovCtas.getEstatus():"";
	}

	public void setEstatus(String estatus) {
		this.pojoMovCtas.setEstatus(estatus);
	}

	public Double getMonto() {
		return pojoMovCtas.getMonto() != null ? pojoMovCtas.getMonto():0;
	}

	public void setMonto(Double monto) {
		this.pojoMovCtas.setMonto(monto);
	}

	public String getOperacion() {
		return pojoMovCtas.getOperacion() != null ? pojoMovCtas.getOperacion():"";
	}

	public void setOperacion(String operacion) {
		this.pojoMovCtas.setOperacion(operacion);
	}

	public CtasBancoExt getCuentaDestino() {
		return pojoMovCtas.getIdCuentaDestino() != null ? pojoMovCtas.getIdCuentaDestino():null;
	}

	public void setCuentaDestino(CtasBancoExt cuentaDestino) {
		this.pojoMovCtas.setIdCuentaDestino(cuentaDestino);
	}

	public PersonaExt getCuentaDestinoTerceros() {
		return this.pojoMovCtas.getIdCuentaDestinoTerceros() != null ? pojoMovCtas.getIdCuentaDestinoTerceros():null;
	}

	public void setCuentaDestinoTerceros(PersonaExt cuentaDestinoTerceros) {
		this.pojoMovCtas.setIdCuentaDestinoTerceros(cuentaDestinoTerceros);
	}
	
	public Long getPagosGastosIdRef() {
		return pojoMovCtas.getIdPagosGastosRef() != null ? pojoMovCtas.getIdPagosGastosRef():0;
	}

	public void setPagosGastosIdRef(Long pagosGastosIdRef) {
		this.pojoMovCtas.setIdPagosGastosRef(pagosGastosIdRef);
	}

	public ConValores getTiposMovtoId() {
		return pojoMovCtas.getIdTiposMovimiento() != null ? pojoMovCtas.getIdTiposMovimiento():null;
	}

	public void setTiposMovtoId(ConValores tiposMovtoId) {
		this.pojoMovCtas.setIdTiposMovimiento(tiposMovtoId);
	}

	public String getConcepto() {
		return pojoMovCtas.getConcepto() != null ? pojoMovCtas.getConcepto():"";
	}

	public void setConcepto(String concepto) {
		this.pojoMovCtas.setConcepto(concepto);
	}

	public String getFolioAutorizacion() {
		return pojoMovCtas.getFolioAutorizacion() != null ? pojoMovCtas.getFolioAutorizacion():"";
	}

	public void setFolioAutorizacion(String folioAutorizacion) {
		this.pojoMovCtas.setFolioAutorizacion(folioAutorizacion);
	}

	public PagosGastosExt getPojoMovCtas() {
		return pojoMovCtas;
	}

	public void setPojoMovCtas(PagosGastosExt pojoMovCtas) {
		String tmp1 = null;
		String tmp2 = null;
		this.pojoMovCtas = pojoMovCtas;
		tmp1 = this.pojoMovCtas.getIdCuentaOrigen() != null ? this.pojoMovCtas.getIdCuentaOrigen().getId()+ " - " + this.pojoMovCtas.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto() : this.pojoMovCtas.getIdCuentaOrigenTerceros().getId()+ " - " + this.pojoMovCtas.getIdCuentaOrigenTerceros().getNombre()+" "+this.pojoMovCtas.getIdCuentaOrigenTerceros().getPrimerApellido()+" "+this.pojoMovCtas.getIdCuentaOrigenTerceros().getSegundoApellido();
		tmp2 = this.pojoMovCtas.getIdCuentaDestino() != null ? this.pojoMovCtas.getIdCuentaDestino().getId()+ " - " + this.pojoMovCtas.getIdCuentaDestino().getInstitucionBancaria().getNombreCorto() : this.pojoMovCtas.getIdCuentaDestinoTerceros().getId()+ " - " + this.pojoMovCtas.getIdCuentaDestinoTerceros().getNombre()+" "+this.pojoMovCtas.getIdCuentaDestinoTerceros().getPrimerApellido()+" "+this.pojoMovCtas.getIdCuentaDestinoTerceros().getSegundoApellido();
		
		checaTipo();
		this.suggCtaBancaria = tmp1;
		this.suggCtaDestino = tmp2;
		
		if ("X".equals(pojoMovCtas.getEstatus()))
			this.mensajeError = true;
		else
			this.mensajeError = false;
		this.varMonto = pojoMovCtas.getMonto();
	}

	public long getCreadoPor() {
		return pojoMovCtas.getCreadoPor();
	}

	public void setCreadoPor(long creadoPor) {
		this.pojoMovCtas.setCreadoPor(creadoPor);
	}

	public Date getFechaCreacion() {
		return pojoMovCtas.getFechaCreacion();
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.pojoMovCtas.setFechaCreacion(fechaCreacion);
	}

	public long getModificadoPor() {
		return pojoMovCtas.getModificadoPor();
	}

	public void setModificadoPor(long modificadoPor) {
		this.pojoMovCtas.setModificadoPor(modificadoPor);
	}

	public Date getFechaModificacion() {
		return pojoMovCtas.getFechaModificacion();
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.pojoMovCtas.setFechaModificacion(fechaModificacion);
	}

	public CtasBancoExt getPojoCtasBanco() {
		return pojoCtasBanco;
	}

	public void setPojoCtasBanco(CtasBancoExt pojoCtasBanco) {
		this.pojoCtasBanco = pojoCtasBanco;
	}

	public List<CtasBanco> getListBancos() {
		return listBancos;
	}

	public void setListBancos(List<CtasBanco> listBancos) {
		this.listBancos = listBancos;
	}

	public String getSuggCtaBancaria() {
		return suggCtaBancaria;
	}

	public void setSuggCtaBancaria(String suggCtaBancaria) {
		this.suggCtaBancaria = suggCtaBancaria;
	}
	
	public long getCtaBancoId() {
		return pojoCtasBanco.getId() != 0L ? pojoCtasBanco.getId() : 0L;
	}

	public void setCtaBancoId(long ctaBancoId) {
		this.pojoCtasBanco.setId(ctaBancoId);
	}

	public String getCinsncorto() {
		return pojoCtasBanco.getInstitucionBancaria().getNombreCorto() != null ? pojoCtasBanco.getInstitucionBancaria().getNombreCorto() :"" ;
	}

	public void setCinsncorto(String cinsncorto) {
		this.pojoCtasBanco.getInstitucionBancaria().setNombreCorto(cinsncorto);
	}

	public boolean isMuestraPropios() {
		return muestraPropios;
	}

	public void setMuestraPropios(boolean muestraPropios) {
		this.muestraPropios = muestraPropios;
	}

	public String getSuggCtaDestino() {
		return suggCtaDestino;
	}

	public void setSuggCtaDestino(String suggCtaDestino) {
		this.suggCtaDestino = suggCtaDestino;
	}

	public String getSuggCtaPropios() {
		return suggCtaPropios;
	}

	public void setSuggCtaPropios(String suggCtaPropios) {
		this.suggCtaPropios = suggCtaPropios;
	}

	public CtasBancoExt getPojoCtasBancoDestino() {
		return pojoCtasBancoDestino;
	}

	public void setPojoCtasBancoDestino(CtasBancoExt pojoCtasBancoDestino) {
		this.pojoCtasBancoDestino = pojoCtasBancoDestino;
	}

	public boolean isMuestraSalvar() {
		return muestraSalvar;
	}

	public void setMuestraSalvar(boolean muestraSalvar) {
		this.muestraSalvar = muestraSalvar;
	}

	public List<PagosGastosExt> getListMovCtas() {
		return listMovCtas;
	}

	public void setListMovCtas(List<PagosGastosExt> listMovCtas) {
		this.listMovCtas = listMovCtas;
	}

	public String getTipoDestino() {
		return tipoDestino;
	}

	public void setTipoDestino(String tipoDestino) {
		this.tipoDestino = tipoDestino;
	}

	public double getVarMonto() {
		return varMonto;
	}

	public void setVarMonto(double varMonto) {
		this.varMonto = varMonto;
	}

	public String getmsgError() {
		return msgError;
	}

	public void setmsgError(String msgError) {
		this.msgError = msgError;
	}

	public boolean isMuestraTercerosOrigen() {
		return muestraTercerosOrigen;
	}

	public void setMuestraTercerosOrigen(boolean muestraTercerosOrigen) {
		this.muestraTercerosOrigen = muestraTercerosOrigen;
	}

	public boolean isMuestraPropiosOrigen() {
		return muestraPropiosOrigen;
	}

	public void setMuestraPropiosOrigen(boolean muestraPropiosOrigen) {
		this.muestraPropiosOrigen = muestraPropiosOrigen;
	}

	public Persona getPojoValoresDestino() {
		return pojoValoresDestino;
	}

	public void setPojoValoresDestino(Persona pojoValoresDestino) {
		this.pojoValoresDestino = pojoValoresDestino;
	}

	public String getVarOp() {
		return varOp;
	}

	public void setVarOp(String varOp) {
		this.varOp = varOp;
	}

	public String getBusquedaVacia() {
		return busquedaVacia;
	}

	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}

	public String getInvalidMonto() {
		return invalidMonto;
	}

	public void setInvalidMonto(String invalidMonto) {
		this.invalidMonto = invalidMonto;
	}

	public String getErrorCta() {
		return errorCta;
	}

	public void setErrorCta(String errorCta) {
		this.errorCta = errorCta;
	}

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

	public String getSuggMoneda() {
		return suggMoneda;
	}

	public void setSuggMoneda(String suggMoneda) {
		this.suggMoneda = suggMoneda;
	}
	
	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public boolean isMuestraTipoCambio() {
		return muestraTipoCambio;
	}

	public void setMuestraTipoCambio(boolean muestraTipoCambio) {
		this.muestraTipoCambio = muestraTipoCambio;
	}

	public List<MonedasValores> getListTipoCambio() {
		return listTipoCambio;
	}

	public void setListTipoCambio(List<MonedasValores> listTipoCambio) {
		this.listTipoCambio = listTipoCambio;
	}

	public Moneda getPojoMoneda() {
		return pojoMoneda;
	}

	public void setPojoMoneda(Moneda pojoMoneda) {
		this.pojoMoneda = pojoMoneda;
	}

	public String getInvalidTipoCambio() {
		return invalidTipoCambio;
	}

	public void setInvalidTipoCambio(String invalidTipoCambio) {
		this.invalidTipoCambio = invalidTipoCambio;
	}

	public String getInvalidSaldo() {
		return invalidSaldo;
	}

	public void setInvalidSaldo(String invalidSaldo) {
		this.invalidSaldo = invalidSaldo;
	}
}

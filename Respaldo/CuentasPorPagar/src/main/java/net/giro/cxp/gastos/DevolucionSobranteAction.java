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
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.logica.CtasBancoRem;

import org.apache.log4j.Logger;

public class DevolucionSobranteAction {
	private static Logger log = Logger.getLogger(DevolucionSobranteAction.class);
	private ConGrupoValores pojoGpoVal; 
	private PersonaExt pojoValores;
	private PagosGastosExt pojoMovCtas;
	private PagosGastosExt pojoMovCtasDev;
	private CtasBancoExt pojoCtasBanco;
	private ConGrupoValores pojoGpoValPersonas;	
	private PersonaExt pojoPersona;

	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private CtasBancoRem ifzBancos;
	private PagosGastosRem ifzMovCta;
	//private PersonaRem ifzPersonas;
	
	private List<ConValores> listValores;
	private List<PersonaExt> listCtasTerceros;
	private List<PagosGastosExt> listMovCtas;
	private List<CtasBanco> listBancos;
	private List<PersonaExt> listPersonas;
	
	private List<String>	tipoBusqueda;
			
	private InitialContext ctx;
	
	
	private String campoBusqueda;	
	private String valTipoBusqueda;
    private String beneficiario;
    private String tipoBeneficiario; 
    private String suggCtaBancaria;
    private String suggCtaTercero;
    private String tipoCta;
    private String resOperacion;
    private String operacion;
    private String sucursalesVisibles;
    private String problemInesp;
	private String busquedaVacia;
    
    private long usuarioId;	
	private Long valGpo;
	
	private double varMonto;
	private Date varFecha;
	
	private boolean mensajeError;
	private boolean muestraSalvar;
	private boolean msgSalvar;
	private boolean esTransferencia;

	private int numPagina;
	private int numPaginaSec;
	
	private ConGrupoValores pojoGpoValFormasPago; 
	private List<ConValores> listFormasPago;
	private List<SelectItem> listFormasPagoItems;

	
	public DevolucionSobranteAction() throws NamingException,Exception{
		long idGrupoValor = 0;
		
		this.ctx = new InitialContext();
		this.pojoGpoValPersonas = new ConGrupoValores();
		this.pojoValores = new PersonaExt();
		this.pojoMovCtas = new PagosGastosExt();
		this.pojoCtasBanco = new CtasBancoExt();
		this.pojoGpoVal = new ConGrupoValores();
		
		listValores = new ArrayList<ConValores>();
		listCtasTerceros = new ArrayList<PersonaExt>();
		listMovCtas = new ArrayList<PagosGastosExt>();
		
		this.numPagina = 1;
		this.numPaginaSec = 1;
	
		this.ifzGpoVal 	 = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
		this.ifzConValores = (ConValoresRem) 	this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
		this.ifzBancos 	 = (CtasBancoRem) 		this.ctx.lookup("ejb:/Logica_TYG//CtasBancoFac!net.giro.tyg.logica.CtasBancoRem");
		this.ifzMovCta 	 = (PagosGastosRem) 	this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
		//this.ifzPersonas = (PersonaRem) 		ctx.lookup("ejb:/Logica_CuentasPorPagar//PersonaFac!net.giro.clientes.logica.PersonaRem");
		
		mensajeError = false;
		this.muestraSalvar = true;
		
		tipoBusqueda = new ArrayList<String>();
		tipoBusqueda.add("Persona");	
		tipoBusqueda.add("Negocio");
		valTipoBusqueda=tipoBusqueda.get(0);
		
		this.operacion = "Deposito";
		this.suggCtaTercero="";
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager)ve.getValue(fc.getELContext());
		
		this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId(); // usuarioId = lm.getLoginBean().getUsuario().getUsuarioId();
		//sucursalesVisibles = lm.getLoginBean().getSucursalesVisibles();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());
			
		if ( entornoProperties.getString("SYS_RELPER") == null || "".equals(entornoProperties.getString("SYS_RELPER")) )
			throw new Exception("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
		else
			this.valGpo = Long.valueOf(entornoProperties.getString("SYS_RELPER")) ;
		
		this.pojoGpoValPersonas = this.ifzGpoVal.findById(valGpo);
		
		if (this.pojoGpoValPersonas==null){
			this.pojoGpoValPersonas = new ConGrupoValores();
			this.pojoGpoValPersonas.setId(this.valGpo);
			//throw new Exception("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
		}
		//obtiene grupo de cuentas terceros
		if ( entornoProperties.getString("SYS_CUENTASTERCEROS") == null ||"".equals(entornoProperties.getString("SYS_CUENTASTERCEROS")) )
			throw new Exception("No se encontro encontro el grupo SYS_CUENTASTERCEROS en con_grupo_valores");
		else
			this.valGpo = Long.valueOf(entornoProperties.getString("SYS_CUENTASTERCEROS")) ;
		
		this.pojoGpoVal = this.ifzGpoVal.findById(valGpo);
		if (this.pojoGpoVal==null){
			this.pojoGpoVal = new ConGrupoValores();
			this.pojoGpoVal.setId(this.valGpo);
			throw new Exception("No se encontro encontro el grupo SYS_CUENTASTERCEROS en con_grupo_valores");
		}
		
		if (entornoProperties.getString("SYS_FORMAS_PAGO") == null || "".equals(entornoProperties.getString("SYS_FORMAS_PAGO")) )
			throw new Exception("No se encontro encontro el grupo SYS_FORMAS_PAGO en con_grupo_valores");
		else
			idGrupoValor = Long.valueOf(entornoProperties.getString("SYS_FORMAS_PAGO"));
		this.pojoGpoValFormasPago = this.ifzGpoVal.findById(idGrupoValor);
		cargarFormasDePago();
		
		/*ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)ve.getValue(fc.getELContext());

		problemInesp 	=	propPlataforma.getString("mensajes.error.inesperado");
		
		ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		
		propPlataforma = (PropertyResourceBundle)ve.getValue(fc.getELContext());
		busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");*/
	}
	
	
	public void cargarFormasDePago() {
		try {
			if (this.listFormasPagoItems == null)
				this.listFormasPagoItems = new ArrayList<SelectItem>();
			this.listFormasPagoItems.clear();
			
			this.listFormasPago = this.ifzConValores.findAll(this.pojoGpoValFormasPago);
			if (this.listFormasPago != null && ! this.listFormasPago.isEmpty()) {
				for (ConValores fp : this.listFormasPago) {
					this.listFormasPagoItems.add(new SelectItem(fp.getDescripcion()));
				}
			}
		} catch(Exception e) {
			log.error("error en cargarFormasDePago", e);
		}
	}

	private String operacionValidate(String value) {
		boolean reverse = false;
		
		if ("".equals(value.trim())) return "";
		reverse = (value.trim().length() == 1);
		
		for (ConValores var : this.listFormasPago) {
			if (reverse && value.equals(var.getValor())) 
				return var.getDescripcion();
			if (value.equals(var.getDescripcion())) 
				return var.getValor();
		}
		
		return value.trim();
	}
	
	public List<CtasBanco> autoacompletaCuenta (Object obj){
		try{
			this.listBancos=this.ifzBancos.findLikeClaveNombreCuenta(obj.toString(), 20, this.sucursalesVisibles, null);
			return this.listBancos;
		}catch(Exception re){
			log.error("Error en autocompletaCuenta");
			return new ArrayList<CtasBanco>();
		}
		
	}
	
	public String guardar() {
		try {
			Pattern pat = null;
			Matcher match = null;
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			Double mtoTmp = 0D;
			
			this.operacion = operacionValidate(this.operacion);
			
			//validando Cuenta destino
			match = pat.matcher(suggCtaBancaria);
			if(match.find())
				this.pojoCtasBanco = this.ifzMovCta.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzBancos.findAllById(Short.valueOf(match.group(1)));
			
			if("T".equals(this.operacion)) {
				match = pat.matcher(suggCtaTercero);
				if(match.find())
					this.pojoValores = this.ifzMovCta.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findById(Long.valueOf(match.group(1)));
			}
			
			if(this.pojoCtasBanco != null) {
				this.pojoMovCtas.setOperacion(this.operacion);
				this.pojoMovCtas.setModificadoPor(this.usuarioId);
				this.pojoMovCtas.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoMovCtas.setIdCuentaDestino(pojoCtasBanco);
				
				if("T".equals(this.operacion))
					this.pojoMovCtas.setIdCuentaOrigenTerceros(pojoValores);
				
				if (varMonto <= pojoMovCtas.getMonto()) {
					mtoTmp = pojoMovCtas.getMonto();
					this.pojoMovCtas.setFecha(varFecha);
					this.pojoMovCtas.setMonto(varMonto);
					
					this.pojoMovCtas = ifzMovCta.actualizar(this.pojoMovCtas,"G",Calendar.getInstance().getTime() , Short.valueOf(String.valueOf(this.usuarioId)), null);
					this.listMovCtas.remove(pojoMovCtas);
						
					this.muestraSalvar = false;
					this.msgSalvar = true;
				}
				
				if(varMonto < mtoTmp) {
					this.pojoMovCtasDev = new PagosGastosExt();
					varMonto = mtoTmp - varMonto;
					this.pojoMovCtasDev.setMonto(varMonto);
					this.pojoMovCtasDev.setModificadoPor(this.usuarioId);
					this.pojoMovCtasDev.setFechaModificacion(Calendar.getInstance().getTime());
					this.pojoMovCtasDev.setCreadoPor(this.usuarioId);
					this.pojoMovCtasDev.setFechaCreacion(Calendar.getInstance().getTime());
					this.pojoMovCtasDev.setFecha(varFecha);
					this.pojoMovCtasDev.setIdSucursal(this.pojoMovCtas.getIdSucursal());
					this.pojoMovCtasDev.setIdCuentaOrigen(this.pojoMovCtas.getIdCuentaOrigen());
					this.pojoMovCtasDev.setIdBeneficiario(this.pojoMovCtas.getIdBeneficiario());
					if("T".equals(this.pojoMovCtas.getOperacion()))								
						this.pojoMovCtasDev.setIdCuentaDestinoTerceros(this.pojoMovCtas.getIdCuentaDestinoTerceros());				
					else
						this.pojoMovCtasDev.setIdCuentaDestino(this.pojoMovCtas.getIdCuentaDestino());
					this.pojoMovCtasDev.setBeneficiario(this.pojoMovCtas.getBeneficiario());
					this.pojoMovCtasDev.setOperacion(this.pojoMovCtas.getOperacion());
					this.pojoMovCtasDev.setTipo("D");
					this.pojoMovCtasDev.setEstatus("P");
					
					// Guardamos
					this.pojoMovCtasDev.setId(this.ifzMovCta.save(this.pojoMovCtasDev));
				}
				
				this.suggCtaBancaria = "";
				this.suggCtaTercero = "";
			}
			this.resOperacion= "";
		} catch(Exception e) {
			this.resOperacion= this.problemInesp;
			log.error("error al guardar", e);
			this.operacion = operacionValidate(this.operacion);
			return "ERROR";
		}

		return "OK";
	}
	
	public void evaluaOperacion(){
		try{
			this.esTransferencia = "transferencia".equals(this.operacion.trim().toLowerCase());
			//this.esTransferencia = "T".equals(this.operacion);
		}catch(Exception e){			
			log.error("error en evaluaOperacion", e);
			//return "ERROR";
		}
		
		//return "OK";
	}
	
	public String buscar(){
		try{
			System.out.println("valTipoBusqueda: " + valTipoBusqueda);
			if("".equals(valTipoBusqueda))
				this.listPersonas = this.ifzMovCta.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "", "D", "'P'", 80, Calendar.getInstance().getTime(), this.sucursalesVisibles); // this.ifzPersonas.findLikePersonasConGastos(this.campoBusqueda,this.pojoGpoValPersonas,"","D","'P'",80,Calendar.getInstance().getTime(),this.sucursalesVisibles);
			else if( "Persona".equals(this.valTipoBusqueda))					
				this.listPersonas = this.ifzMovCta.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "P", "D", "'P'", 80, Calendar.getInstance().getTime(), this.sucursalesVisibles); // this.ifzPersonas.findLikePersonasConGastos(this.campoBusqueda,this.pojoGpoValPersonas,"P","D","'P'",80,Calendar.getInstance().getTime(),this.sucursalesVisibles);	
			else if("Negocio".equals(this.valTipoBusqueda))
				this.listPersonas = this.ifzMovCta.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "N", "D", "'P'", 80, Calendar.getInstance().getTime(), this.sucursalesVisibles); // this.ifzPersonas.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas,"N","D","'P'",80,Calendar.getInstance().getTime(),this.sucursalesVisibles);
			
			if(this.listPersonas.isEmpty())
				this.resOperacion = this.busquedaVacia;
			else 
				this.resOperacion="";
			
			System.out.println("this.listPersonas: " + this.listPersonas.size());
		} catch(Exception e) {
			this.resOperacion= this.problemInesp;
			log.error("error al buscar", e);
			return "ERROR";
		}

		return "OK";
	}
	
	public String detallesDev(){		
		try{
			if( "Persona".equals(this.valTipoBusqueda))
				this.listMovCtas = this.ifzMovCta.findLikeGtosPorComprobarPersonaExt(this.pojoPersona.getId(), null, "D", "P", "P", null, "SI", 100);
			else
				this.listMovCtas = this.ifzMovCta.findLikeGtosPorComprobarPersonaExt(this.pojoPersona.getId(), null, "D", "P", "N", null, "SI", 100);
			
		}catch(Exception e){			
			log.error("error en detallesDev", e);
			return "ERROR";
		}
		
		return "OK";
	}
		
	public List<PersonaExt> autoacompletaCuentaTercero (Object obj){
		try{
			this.listCtasTerceros = this.ifzMovCta.findLikePersonasConGastos(obj, this.pojoGpoVal, "P", "D", "'P'", 80, Calendar.getInstance().getTime(), this.sucursalesVisibles); //this.ifzPersonas.findLikePersonas(obj.toString(), 20);
			return this.listCtasTerceros;
		}catch(RuntimeException re){
			log.error("Error en autocompletaCuentaTercero");
			return new ArrayList<PersonaExt>();
		}
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

	public List<ConValores> getListValores() {
		return listValores;
	}

	public void setListValores(List<ConValores> listValores) {
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

	public Date getFecha() {
		return pojoMovCtas.getFecha();
	}

	public void setFecha(Date fecha) {
		this.pojoMovCtas.setFecha(fecha);
	}

	public Double getMonto() {
		return pojoMovCtas.getMonto() != null ? pojoMovCtas.getMonto():0;
	}

	public void setMonto(Double monto) {
		this.pojoMovCtas.setMonto(monto);
	}

	public CtasBancoExt getCuentaDestino() {
		return pojoMovCtas.getIdCuentaDestino() != null ? pojoMovCtas.getIdCuentaDestino():null;
	}

	public void setCuentaDestino(CtasBancoExt cuentaDestino) {
		this.pojoMovCtas.setIdCuentaDestino(cuentaDestino);
	}

	public PagosGastosExt getPojoMovCtas() {
		return pojoMovCtas;
	}

	public void setPojoMovCtas(PagosGastosExt pojoMovCtas) {
		this.pojoMovCtas = pojoMovCtas;
		varMonto = pojoMovCtas.getMonto();
		this.tipoBeneficiario = pojoMovCtas.getTipoBeneficiario();
		this.muestraSalvar = true;
			
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

	public String getbeneficiario() {
		return beneficiario;
	}

	public void setbeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}
	
	public Long getCtaBancoId() {
		return pojoCtasBanco.getId() != 0L ? pojoCtasBanco.getId():0;
	}

	public void setCtaBancoId(Long ctaBancoId) {
		this.pojoCtasBanco.setId(ctaBancoId);
	}

	public String getCinsncorto() {
		return pojoCtasBanco.getInstitucionBancaria().getNombreCorto() != null ? pojoCtasBanco.getInstitucionBancaria().getNombreCorto() :"" ;
	}

	public void setCinsncorto(String cinsncorto) {
		this.pojoCtasBanco.getInstitucionBancaria().setNombreCorto(cinsncorto);
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

	public PersonaExt getNoBeneficiario() {
		return this.pojoMovCtas.getIdBeneficiario() != null ? this.pojoMovCtas.getIdBeneficiario() : null;
	}

	public void setNoBeneficiario(PersonaExt noBeneficiario) {
		this.pojoMovCtas.setIdBeneficiario(noBeneficiario);
	}

	public String getSuggCtaBancaria() {
		return suggCtaBancaria;
	}

	public void setSuggCtaBancaria(String suggCtaBancaria) {
		this.suggCtaBancaria = suggCtaBancaria;
	}

	public List<PersonaExt> getListPersonas() {
		return listPersonas;
	}

	public void setListPersonas(List<PersonaExt> listPersonas) {
		this.listPersonas = listPersonas;
	}

	public ConGrupoValores getPojoGpoValPersonas() {
		return pojoGpoValPersonas;
	}

	public void setPojoGpoValPersonas(ConGrupoValores pojoGpoValPersonas) {
		this.pojoGpoValPersonas = pojoGpoValPersonas;
	}

	public PersonaExt getPojoPersona() {
		return pojoPersona;
	}

	public void setPojoPersona(PersonaExt pojoPersona) {
		this.pojoPersona = pojoPersona;
	}

	public int getNumPaginaSec() {
		return numPaginaSec;
	}

	public void setNumPaginaSec(int numPaginaSec) {
		this.numPaginaSec = numPaginaSec;
	}

	public double getVarMonto() {
		return varMonto;
	}

	public void setVarMonto(double varMonto) {
		this.varMonto = varMonto;
	}

	public Date getVarFecha() {
		return varFecha;
	}

	public void setVarFecha(Date varFecha) {
		this.varFecha = varFecha;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}

	public String getTipoCta() {
		return tipoCta;
	}

	public void setTipoCta(String tipoCta) {
		this.tipoCta = tipoCta;
	}

	public List<PersonaExt> getListCtasTerceros() {
		return listCtasTerceros;
	}

	public void setListCtasTerceros(List<PersonaExt> listCtasTerceros) {
		this.listCtasTerceros = listCtasTerceros;
	}

	public PagosGastosExt getPojoMovCtasDev() {
		return pojoMovCtasDev;
	}

	public void setPojoMovCtasDev(PagosGastosExt pojoMovCtasDev) {
		this.pojoMovCtasDev = pojoMovCtasDev;
	}

	public boolean isMsgSalvar() {
		return msgSalvar;
	}

	public void setMsgSalvar(boolean msgSalvar) {
		this.msgSalvar = msgSalvar;
	}

	public String getBusquedaVacia() {
		return busquedaVacia;
	}

	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}
	
	public String getOperacion() {
		return this.operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion=operacion;
	}

	public boolean isEsTransferencia() {
		return esTransferencia;
	}

	public void setEsTransferencia(boolean esTransferencia) {
		this.esTransferencia = esTransferencia;
	}

	public String getSuggCtaTercero() {
		return suggCtaTercero;
	}

	public void setSuggCtaTercero(String suggCtaTercero) {
		this.suggCtaTercero = suggCtaTercero;
	}

	public List<SelectItem> getListFormasPagoItems() {
		return listFormasPagoItems;
	}

	public void setListFormasPagoItems(List<SelectItem> listFormasPagoItems) {
		this.listFormasPagoItems = listFormasPagoItems;
	}
}

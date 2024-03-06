package net.giro.cxp.gastos;

import java.math.BigDecimal;
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

import org.apache.log4j.Logger;

import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Cheques;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.logica.ChequesRem;
import net.giro.tyg.logica.CtasBancoRem;

public class GenerarReembolsoAction {
	private PersonaExt pojoValores;
	private ConGrupoValores pojoGpoVal; 
	private PagosGastosExt pojoMovCtas;
	private ConGrupoValores pojoGpoValPersonas;	
	private PersonaExt pojoPersona;
	private CtasBancoExt pojoCtasBanco;
	
	private ConGrupoValoresRem ifzGpoVal;
	private PagosGastosRem ifzMovCta;
	private PersonaRem ifzPersonas;
	private CtasBancoRem ifzCtas;
	//private ReportesRem ifzReporte;
	
	private List<PagosGastosExt> listMovCtas;
	private List<PersonaExt> listPersonas;
	private List<CtasBanco> listCuentas;
	private List<Persona> listValores;
	
	private List<String>	tipoBusqueda;
			
	private InitialContext ctx;
	
	private String campoBusqueda;	
	private String valTipoBusqueda;
	private String suggCtaBancaria;
	private String suggCtaTercero;
	private String tipoBeneficiario;
	private String operacion;
	private String mensaje;
	private String problemInesp;
	private String invalidMonto;
	private String sucursalesVisibles;
	private String invalidSaldo;
	
	//private HttpSession httpSession;
    
    private long usuarioId;
	private Long valGpo;
	
	private double varMonto;
	private Date varFecha;
	
	private boolean muestraSalvar;
	private boolean esCheque;
	private boolean esTerceros;

	private int numPagina;
	private int numPaginaSec;
	private String busquedaVacia;
	private static Logger log = Logger.getLogger(GenerarReembolsoAction.class);

	public GenerarReembolsoAction() throws NamingException,Exception{
		this.ctx = new InitialContext();
		this.pojoGpoValPersonas = new ConGrupoValores();
		this.pojoValores = new PersonaExt();
		this.pojoMovCtas = new PagosGastosExt();
		this.pojoCtasBanco = new CtasBancoExt();
		this.pojoGpoVal = new ConGrupoValores();
		this.listMovCtas = new ArrayList<PagosGastosExt>();
		this.listCuentas = new ArrayList<CtasBanco>();
		this.listPersonas = new ArrayList<PersonaExt>();
		
		this.numPagina = 1;
		this.numPaginaSec = 1;
		
		this.ifzGpoVal 		= (ConGrupoValoresRem)	ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
		this.ifzMovCta 		= (PagosGastosRem)		ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
		this.ifzPersonas 	= (PersonaRem)			ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
		this.ifzCtas		= (CtasBancoRem)		ctx.lookup("ejb:/Logica_TYG//CtasBancoFac!net.giro.tyg.logica.CtasBancoRem");
		//this.ifzReporte 	= (ReportesRem) 		ctx.lookup("ejb:/Logica_Factoraje//ReportesFac!net.giro.factoraje.logica.ReportesRem");
		
		this.muestraSalvar = true;
		this.esCheque = false;
		this.esTerceros = false;
		this.mensaje = "";
		
		tipoBusqueda = new ArrayList<String>();
		tipoBusqueda.add("Persona");	
		tipoBusqueda.add("Negocio");
		valTipoBusqueda = tipoBusqueda.get(0);
		operacion = "E";
		
		this.suggCtaTercero = "";
		
		FacesContext fc = FacesContext.getCurrentInstance();
		//httpSession =(HttpSession) fc.getExternalContext().getSession(false);
		
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager)ve.getValue(fc.getELContext());
		
		this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId(); //usuarioId = lm.getLoginBean().getUsuario().getUsuarioId();
		//sucursalesVisibles = lm.getLoginBean().getSucursalesVisibles();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());
			
		if ( entornoProperties.getString("SYS_RELPER") == null || "".equals(entornoProperties.getString("SYS_RELPER")) )
			throw new Exception("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
		else
			this.valGpo = Long.valueOf(entornoProperties.getString("SYS_RELPER")) ;
		
		this.pojoGpoValPersonas = this.ifzGpoVal.findById(valGpo);
		
		if (this.pojoGpoValPersonas == null){
			this.pojoGpoValPersonas = new ConGrupoValores();
			this.pojoGpoValPersonas.setId(this.valGpo);
			//throw new Exception("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
		}
		
		if ( entornoProperties.getString("SYS_CUENTASTERCEROS") == null ||"".equals(entornoProperties.getString("SYS_CUENTASTERCEROS")) )
			throw new Exception("No se encontro encontro el grupo SYS_CUENTASTERCEROS en con_grupo_valores");
		else
			this.valGpo = Long.valueOf(entornoProperties.getString("SYS_CUENTASTERCEROS")) ;
				
		this.pojoGpoVal = this.ifzGpoVal.findById(valGpo);
		if (this.pojoGpoVal == null){
			this.pojoGpoVal = new ConGrupoValores();
			this.pojoGpoVal.setId(this.valGpo);
			//throw new Exception("No se encontro encontro el grupo SYS_CUENTASTERCEROS en con_grupo_valores");
		}
		
		
		/*ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)ve.getValue(fc.getELContext());

		problemInesp 	=	propPlataforma.getString("mensajes.error.inesperado");
		
		ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		
		propPlataforma = (PropertyResourceBundle)ve.getValue(fc.getELContext());
		invalidMonto = 	propPlataforma.getString("navegacion.label.montoInvalido");
		invalidSaldo = propPlataforma.getString("navegacion.label.saldoInsuficiente");
		busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");*/
	}
	
	public List<Persona> autoacompletaTerceros (Object obj){
		try{
			this.listValores =this.ifzPersonas.findLikePersonas(obj.toString(),20);
			return this.listValores;
		}catch(RuntimeException re){
			log.error("Error en autoacompletaTerceros");
			return new ArrayList<Persona>();
		}
	}
	
	public String validaMonto (){
		try{
			mensaje = "";
			/*
			 * DESACTIVAMOS LA VALIDACION DE SALDO TEMPORALMENTE: ASI LO PIDIO EL JEFE
			if(varMonto == 0)
				mensaje = invalidMonto;
			if (this.pojoCtasBanco.getId() != 0L){
				if (!ifzCtas.haySaldoSuficiente(varMonto, this.pojoCtasBanco.getId()))
						mensaje = invalidSaldo;
			}*/
		}catch(Exception e){
			mensaje = problemInesp;
			log.error("Error en el metodo valida monto.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	@SuppressWarnings("unchecked")
	public String guardar (){
		try{
			this.mensaje = "";
			Pattern pat = null;
			Matcher match = null;
			
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		
			//validando Cuenta destino
			match = pat.matcher(suggCtaBancaria);			
			if(match.find())
				this.pojoCtasBanco = this.ifzMovCta.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzCtas.findAllById(Short.valueOf(match.group(1)));
			
			match = pat.matcher(suggCtaTercero);
			if ("3".equals(this.operacion)){
				if (match.find())
					this.pojoValores = this.ifzMovCta.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				if (pojoCtasBanco != null && pojoValores != null){
					validaMonto();
					if ("".equals(this.mensaje)){
						this.pojoMovCtas.setFecha(varFecha);
						this.pojoMovCtas.setOperacion(operacion);
						this.pojoMovCtas.setIdCuentaOrigen(pojoCtasBanco);
						this.pojoMovCtas.setIdCuentaDestinoTerceros(pojoValores);
						this.pojoMovCtas = this.ifzMovCta.actualizar(pojoMovCtas, "G",Calendar.getInstance().getTime(), Short.valueOf(String.valueOf(this.usuarioId)), null);
						this.listMovCtas.remove(pojoMovCtas);
						this.muestraSalvar = false;
					}
				}
			}
			else if (pojoCtasBanco != null){
				validaMonto();
				
				if ("".equals(this.mensaje)){
					this.pojoMovCtas.setModificadoPor(this.usuarioId);
					this.pojoMovCtas.setFechaModificacion(Calendar.getInstance().getTime());
					this.pojoMovCtas.setEstatus("G");
					this.pojoMovCtas.setFecha(varFecha);
					this.pojoMovCtas.setOperacion(operacion);
					this.pojoMovCtas.setIdCuentaOrigen(pojoCtasBanco);
	
					if ("C".equals(this.operacion)){
						//validando folio cheque
						NQueryRem ifzEditorSQL;
						List<Object> resultadoSQL = new ArrayList<Object>();
	
						ifzEditorSQL = (NQueryRem) this.ctx.lookup("NQueryRem");
						
						//parametros enviados:  select cancela_cheque(id cta bancaria, folio de cheque, true SI cancela folio, Id usuario)
						resultadoSQL =  ifzEditorSQL.findNativeQuery("select cancela_cheques(" + pojoMovCtas.getIdCuentaOrigen().getId() + "," + pojoMovCtas.getNoCheque() + "," + true +","+  pojoMovCtas.getCreadoPor() +")");
						
						if(! resultadoSQL.isEmpty()){
							for(Object var: resultadoSQL){
								if(! "BIEN".equals(var)){
									mensaje=var.toString();		
									return "OK";
								}	
							}
						}
						
						Cheques pojoCheque = new Cheques();
						ChequesRem ifzCheques;
						ifzCheques = (ChequesRem) this.ctx.lookup("ChequesRem");
						
						//insertando el cheque 
						pojoCheque.setBancoId(pojoMovCtas.getIdCuentaOrigen().getId());
						pojoCheque.setControl("");
						pojoCheque.setMinistracion(0L);
						pojoCheque.setEstatus("E");
						pojoCheque.setTipo("T");
						pojoCheque.setFecha(pojoMovCtas.getFecha());
						pojoCheque.setImporte(new BigDecimal(pojoMovCtas.getMonto()));
						pojoCheque.setFolio(String.valueOf( pojoMovCtas.getNoCheque()));
						pojoCheque.setCreadoPor((long) pojoMovCtas.getCreadoPor());
						pojoCheque.setFechaCreacion(Calendar.getInstance().getTime());
						pojoCheque.setModificadoPor((long) pojoMovCtas.getModificadoPor());
						pojoCheque.setFechaModificacion(Calendar.getInstance().getTime());
						ifzCheques.save(pojoCheque);
						ifzMovCta.update(pojoMovCtas);
						this.listMovCtas.remove(pojoMovCtas);
						
						//impresion del cheque
						/*HashMap listaParam;
						byte[] datosRep = null;
						listaParam = new HashMap<String, String>();
						listaParam.put("folio", this.pojoMovCtas.getNoCheque());						
						listaParam.put("banco_id", this.pojoMovCtas.getIdCuentaOrigen().getId());	
						datosRep = this.ifzReporte.generarReporte("cheque.jasper", listaParam);
						httpSession.setAttribute("reporte", datosRep);
						httpSession.setAttribute("tipoReporte", "pdf");*/
					}
					
					this.pojoMovCtas = ifzMovCta.actualizar(pojoMovCtas, "G",Calendar.getInstance().getTime(), Short.valueOf(String.valueOf(this.usuarioId)), null);
					this.listMovCtas.remove(pojoMovCtas);
					this.muestraSalvar = false;
				}
			}
			
			if("C".equals(this.operacion)){
				this.mensaje= "C";
			} else
				mensaje = "";
		}catch(Exception e){
			mensaje=this.problemInesp;
			log.error("error al guardar", e);
			return "ERROR";
		}
		
		return "OK"; 
	}
	
	public String cancelarCheques(){
   		try{
   			Respuesta resp = new Respuesta();
   			 			
   			resp = ifzMovCta.salvar(this.pojoMovCtas, false, null);
				
			if(! "BIEN".equals(resp.getRespuesta()) && resp.getResultado() == -1){
				guardar();	
			}
   		}catch(Exception e){
			log.error("error en cancelarCheques", e);
			return "ERROR";
		}
		
		return "OK";
   	}
	
	public String buscar(){
		try{
			System.out.println("valTipoBusqueda: " + valTipoBusqueda);
			if("".equals(valTipoBusqueda))
				this.listPersonas = this.ifzMovCta.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "", "R", "'A'", 80, Calendar.getInstance().getTime(), this.sucursalesVisibles); // this.ifzPersonas.findLikePersonasConGastos(this.campoBusqueda,this.pojoGpoValPersonas,"","R","'A'",80,Calendar.getInstance().getTime(),this.sucursalesVisibles);
			else if( "Persona".equals(this.valTipoBusqueda))					
				this.listPersonas = this.ifzMovCta.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "P", "R", "'A'", 80, Calendar.getInstance().getTime(), this.sucursalesVisibles); // this.ifzPersonas.findLikePersonasConGastos(this.campoBusqueda,this.pojoGpoValPersonas,"P","R","'A'",80,Calendar.getInstance().getTime(),this.sucursalesVisibles);	
			else if("Negocio".equals(this.valTipoBusqueda))
				this.listPersonas = this.ifzMovCta.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "N", "R", "'A'", 80, Calendar.getInstance().getTime(), this.sucursalesVisibles); // this.ifzPersonas.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas,"N","R","'A'",80,Calendar.getInstance().getTime(),this.sucursalesVisibles);
			
			if(this.listPersonas.isEmpty())
				mensaje = this.busquedaVacia;
			else
				mensaje = "";
			
			System.out.println("this.listPersonas: " + this.listPersonas.size());
		} catch(Exception e) {
			mensaje = problemInesp;
			log.error("error al buscar", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String checaOper(){
		try{
			if("C".equals(this.operacion)){
				this.esTerceros = false;
				this.esCheque = true;
			}
			else if ("3".equals(this.operacion)){
				this.esTerceros = true;
				this.esCheque = false;
			}
			else if ("E".equals(this.operacion)){
				this.esCheque = false;
				this.esTerceros = false;
			}
		}catch (Exception e) {
			
		}
		return "OK";
	}
	
	public String detallesReembolso(){		
		try{		
			if("Persona".equals(this.tipoBeneficiario))
				this.listMovCtas = this.ifzMovCta.findLikeGtosPorComprobarPersonaExt(this.pojoPersona.getId(),null,"R","A", "P",Calendar.getInstance().getTime(), "SI", 100);
			else
				this.listMovCtas = this.ifzMovCta.findLikeGtosPorComprobarPersonaExt(this.pojoPersona.getId(),null,"R","A", "N",Calendar.getInstance().getTime(), "SI", 100);
			
		}catch(Exception e){			
			log.error("error en detallesReembolso", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public List<CtasBanco> autoacompletaCuenta(Object obj){
		try{
			this.listCuentas=this.ifzCtas.findLikeClaveNombreCuenta(obj.toString(),20, this.sucursalesVisibles, null);
			return this.listCuentas;
		}catch(Exception re){
			log.error("Error en autoacompletaCuenta");
			return new ArrayList<CtasBanco>();
		}
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
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
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

	public PersonaExt getIdBeneficiario() {
		return this.pojoMovCtas.getIdBeneficiario() != null ? this.pojoMovCtas.getIdBeneficiario() : null;
	}

	public void setIdBeneficiario(PersonaExt noBeneficiario) {
		this.pojoMovCtas.setIdBeneficiario(noBeneficiario);
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

	public String getSuggCtaBancaria() {
		return suggCtaBancaria;
	}

	public void setSuggCtaBancaria(String suggCtaBancaria) {
		this.suggCtaBancaria = suggCtaBancaria;
	}
	
	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}
	
	public String getOperacion() {
		return this.operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion=operacion;
	}

	public CtasBancoRem getIfzCtas() {
		return ifzCtas;
	}

	public void setIfzCtas(CtasBancoRem ifzCtas) {
		this.ifzCtas = ifzCtas;
	}

	public List<CtasBanco> getListCuentas() {
		return listCuentas;
	}

	public void setListCuentas(List<CtasBanco> listCuentas) {
		this.listCuentas = listCuentas;
	}
	
	public Integer getNoCheque() {
		return this.pojoMovCtas.getNoCheque() != null ? this.pojoMovCtas.getNoCheque() : 0;
	}

	public void setNoCheque(Integer noCheque) {
		this.pojoMovCtas.setNoCheque(noCheque);
	}

	public boolean isEsCheque() {
		return esCheque;
	}

	public void setEsCheque(boolean esCheque) {
		this.esCheque = esCheque;
	}

	public CtasBancoExt getPojoCtasBanco() {
		return pojoCtasBanco;
	}

	public void setPojoCtasBanco(CtasBancoExt pojoCtasBanco) {
		this.pojoCtasBanco = pojoCtasBanco;
	}

	public boolean isEsTerceros() {
		return esTerceros;
	}

	public void setEsTerceros(boolean esTerceros) {
		this.esTerceros = esTerceros;
	}

	public String getSuggCtaTercero() {
		return suggCtaTercero;
	}

	public void setSuggCtaTercero(String suggCtaTercero) {
		this.suggCtaTercero = suggCtaTercero;
	}

	public ConGrupoValores getPojoGpoVal() {
		return pojoGpoVal;
	}

	public void setPojoGpoVal(ConGrupoValores pojoGpoVal) {
		this.pojoGpoVal = pojoGpoVal;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

	public String getInvalidMonto() {
		return invalidMonto;
	}

	public void setInvalidMonto(String invalidMonto) {
		this.invalidMonto = invalidMonto;
	}

	public String getBusquedaVacia() {
		return busquedaVacia;
	}

	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}
	
	public String getInvalidSaldo() {
		return invalidSaldo;
	}

	public void setInvalidSaldo(String invalidSaldo) {
		this.invalidSaldo = invalidSaldo;
	}
}

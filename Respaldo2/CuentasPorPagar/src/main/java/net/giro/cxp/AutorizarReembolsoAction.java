package net.giro.cxp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;

import org.apache.log4j.Logger;

public class AutorizarReembolsoAction {
private static Logger log = Logger.getLogger(AutorizarReembolsoAction.class);
	
	private ConValores pojoValores;
	private PagosGastosExt pojoMovCtas;
	private ConGrupoValores pojoGpoValPersonas;	
	private PersonaExt pojoPersona;

	//private ConGrupoValoresRem ifzGpoVal;
	private PagosGastosRem ifzMovCta;
	//private PersonaRem ifzPersonas;
	
	private List<PagosGastosExt> listMovCtas;
	private List<PersonaExt> listPersonas;
	
	private List<String>	tipoBusqueda;
			
	private InitialContext ctx;
	private LoginManager loginManager;
	private String campoBusqueda;	
	private String valTipoBusqueda;
	private String resOperacion;
	private String problemInesp;
	private String busquedaVacia;
	private String sucursalesVisibles;
    
    private long usuarioId;	
	//private Long valGpo;
	
	private double varMonto;
	private Date varFecha;
	
	private boolean muestraSalvar;
	private boolean msgSalvar;

	private int numPagina;
	private int numPaginaSec;

	public AutorizarReembolsoAction() throws NamingException,Exception{
		this.pojoGpoValPersonas = new ConGrupoValores();
		this.pojoValores = new ConValores();
		this.pojoMovCtas = new PagosGastosExt();
		this.listMovCtas = new ArrayList<PagosGastosExt>();
		
		this.numPagina = 1;
		this.numPaginaSec = 1;

		this.muestraSalvar = true;
		
		tipoBusqueda = new ArrayList<String>();
		tipoBusqueda.add("Persona");	
		tipoBusqueda.add("Negocio");
		valTipoBusqueda = tipoBusqueda.get(0);	
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		this.loginManager = (LoginManager)ve.getValue(fc.getELContext());
		
		this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId(); // usuarioId = lm.getLoginBean().getUsuario().getUsuarioId();
		//sucursalesVisibles = lm.getLoginBean().getSucursalesVisibles();
		
		//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		//PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());
			
		/*if ( entornoProperties.getString("SYS_RELPER") == null || "".equals(entornoProperties.getString("SYS_RELPER")) )
			throw new Exception("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
		else
			this.valGpo = Long.valueOf(entornoProperties.getString("SYS_RELPER")) ;
		
		this.pojoGpoValPersonas = this.ifzGpoVal.findById(valGpo);
		
		if (this.pojoGpoValPersonas==null){
			throw new Exception("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");
		}*/
		
		/*dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());

		problemInesp = propPlataforma.getString("mensajes.error.inesperado");
		
		ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		
		propPlataforma = (PropertyResourceBundle)ve.getValue(fc.getELContext());
		busquedaVacia = propPlataforma.getString("mensaje.info.busquedaVacia");*/

		this.ctx = new InitialContext();
		//this.ifzGpoVal 	 = 	(ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
		this.ifzMovCta 	 = 	(PagosGastosRem)	 ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
		//this.ifzPersonas = 	(PersonaRem)		 ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
		this.ifzMovCta.setInfoSesion(this.loginManager.getInfoSesion());
	}
	
	public String guardar (){
		try{
			if (varMonto < pojoMovCtas.getMonto().intValue())
				this.pojoMovCtas.setMonto(varMonto);
			this.pojoMovCtas.setFecha(varFecha);
			this.ifzMovCta.setInfoSesion(this.loginManager.getInfoSesion());
			this.pojoMovCtas = ifzMovCta.actualizar(pojoMovCtas, "A", Calendar.getInstance().getTime());
			this.listMovCtas.remove(pojoMovCtas);
			this.muestraSalvar = false;
			this.msgSalvar = true;
			resOperacion = "";
		}catch(Exception e){
			resOperacion = problemInesp;
			log.error("error al guardar", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String buscar(){
		try{
			System.out.println("valTipoBusqueda: " + valTipoBusqueda);
			if("".equals(valTipoBusqueda))
				this.listPersonas = this.ifzMovCta.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "", "R", "'P'", 80, Calendar.getInstance().getTime(), this.sucursalesVisibles); // this.ifzPersonas.findLikePersonasConGastos(this.campoBusqueda,this.pojoGpoValPersonas,"","R","'P'",80,null,this.sucursalesVisibles);
			else if( "Persona".equals(this.valTipoBusqueda))					
				this.listPersonas = this.ifzMovCta.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "P", "R", "'P'", 80, Calendar.getInstance().getTime(), this.sucursalesVisibles); // this.ifzPersonas.findLikePersonasConGastos(this.campoBusqueda,this.pojoGpoValPersonas,"P","R","'P'",80,null,this.sucursalesVisibles);	
			else if("Negocio".equals(this.valTipoBusqueda))
				this.listPersonas = this.ifzMovCta.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas, "N", "R", "'P'", 80, Calendar.getInstance().getTime(), this.sucursalesVisibles); // this.ifzPersonas.findLikePersonasConGastos(this.campoBusqueda, this.pojoGpoValPersonas,"N","R","'P'",80,null,this.sucursalesVisibles);
			
			if (this.listPersonas.isEmpty())
				resOperacion= this.busquedaVacia;
			else
				resOperacion="";
			
			System.out.println("this.listPersonas: " + this.listPersonas.size());
		}catch(Exception e){
			resOperacion = problemInesp;
			log.error("error al buscar", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String detallesReembolso(){		
		try{
			if("Persona".equals(valTipoBusqueda))
				this.listMovCtas = this.ifzMovCta.findLikeGtosPorComprobarPersonaExt(this.pojoPersona.getId(), null, "R", "P", "P", null, "SI", 100);
			else
				this.listMovCtas = this.ifzMovCta.findLikeGtosPorComprobarPersonaExt(this.pojoPersona.getId(), null, "R", "P", "N", null, "SI", 100);
			resOperacion = "";
		}catch(Exception e){
			resOperacion = problemInesp;
			log.error("error en detallesReembolso", e);
			return "ERROR";
		}
		
		return "OK";
	}
		
	public ConValores getPojoValores() {
		return pojoValores;
	}
	
	public void setPojoValores(ConValores pojoValores) {
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
		return pojoMovCtas.getIdCuentaDestino() != null ? pojoMovCtas.getIdCuentaDestino() : null;
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

	public PersonaExt getNoBeneficiario() {
		return this.pojoMovCtas.getIdBeneficiario() != null ? this.pojoMovCtas.getIdBeneficiario() : null;
	}

	public void setNoBeneficiario(PersonaExt noBeneficiario) {
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

	public boolean isMsgSalvar() {
		return msgSalvar;
	}

	public void setMsgSalvar(boolean msgSalvar) {
		this.msgSalvar = msgSalvar;
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

	public String getBusquedaVacia() {
		return busquedaVacia;
	}

	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}

	public String getSucursalesVisibles() {
		return sucursalesVisibles;
	}

	public void setSucursalesVisibles(String sucursalesVisibles) {
		this.sucursalesVisibles = sucursalesVisibles;
	}
}

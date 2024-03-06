package cde.publico.ne;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;

import org.apache.log4j.Logger;

public class ImpuestosAction {
	private static Logger log = Logger.getLogger(ImpuestosAction.class);
	private InitialContext ctx;

	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private ConGrupoValores pojoGpoImpuestos; 
	private ConValores pojoValores;
	private List<ConValores> listImpuestos;
	private List<String> tipoBusqueda;
	private List<String> tipoOperacion;
	//private Long valGpo;
	private String resOperacion;
	private String campoBusqueda;	
	private String valTipoBusqueda;
    private long usuarioId;
	private int numPagina;
	@SuppressWarnings("unused")
	private boolean desglosar;
	

	public ImpuestosAction() throws NamingException,Exception{
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager)ve.getValue(fc.getELContext());

		//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		//PropertyResourceBundle entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());
		
		this.pojoValores = new ConValores();
		this.listImpuestos = new ArrayList<ConValores>();
		

		this.ctx = new InitialContext();
		this.ifzGpoVal 		= (ConGrupoValoresRem) 	this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
		this.ifzConValores 	= (ConValoresRem) 		this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
		
		this.tipoOperacion = new ArrayList<String>();
		this.tipoOperacion.add("Aumenta");
		this.tipoOperacion.add("Disminuye");   
		
		this.tipoBusqueda = new ArrayList<String>();
		this.tipoBusqueda.add("Nombre del Impuesto");
		this.tipoBusqueda.add("Clave");
		this.valTipoBusqueda = this.tipoBusqueda.get(0);
		this.numPagina = 1;
				
		this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId();

		// Grupo de valores para SYS_IMPTOS (Impuestos) de gastos
		this.pojoGpoImpuestos = this.ifzGpoVal.findByName("SYS_IMPTOS");
		if (this.pojoGpoImpuestos == null || this.pojoGpoImpuestos.getId() <= 0L)
			throw new Exception("No se encontro encontro el grupo SYS_IMPTOS (Impuestos) en con_grupo_valores");
		
		/*if ( entornoProperties.getString("SYS_IMPTOS") == null || "".equals(entornoProperties.getString("SYS_IMPTOS")) )
			throw new Exception("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
		else
			this.valGpo = Long.valueOf(entornoProperties.getString("SYS_IMPTOS")) ;
		
		System.out.println("Buscando ConGrupoValores (SYS_IMPTOS): " + this.valGpo);
		this.pojoGpoVal = this.ifzGpoVal.findById(valGpo);
		System.out.println("----> ConGrupoValores (SYS_IMPTOS): " + this.pojoGpoVal.getId());
		
		if (this.pojoGpoVal == null){
			//this.pojoGpoVal = new ConGrupoValores();
			//this.pojoGpoVal.setId(this.valGpo);
			throw new Exception("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
		}*/
		this.resOperacion="";
	}
	
	public String nuevo (){
		try{
			this.pojoValores = new ConValores();
			resOperacion="";
		}catch(Exception e){
			log.error("Error en el metodo nuevo.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String eliminar(){
		try {
			this.ifzConValores.delete(this.pojoValores);
			this.listImpuestos.remove(this.pojoValores);
		} catch (Exception e) {
			log.error("Error en el metodo eliminar.", e);
			return "ERROR";
		}

		return "OK";
	}
	
	public String guardar() {
		try{
			resOperacion="";
			this.pojoValores.setModificadoPor(this.usuarioId);
			this.pojoValores.setFechaModificacion(Calendar.getInstance().getTime());
			if(this.pojoValores.getId() == 0L){
				this.pojoValores.setCreadoPor(this.usuarioId);
				this.pojoValores.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoValores.setGrupoValorId(this.pojoGpoImpuestos);

				// GUARDAMOS
				this.pojoValores.setId(this.ifzConValores.save(this.pojoValores));

				this.listImpuestos.add(this.pojoValores);
				this.resOperacion = "Información Registrada.";
			} else {
				this.ifzConValores.update(this.pojoValores);
				for (ConValores var : this.listImpuestos) {
					if(var.getId() == this.pojoValores.getId()) {
						var = this.pojoValores;
						this.resOperacion = "Información Registrada.";
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("error al guardar", e);
			resOperacion="Ocurrio un problema inesperado, verifique la informacion e intente nuevamente";
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String buscar(){
		try{
			resOperacion="";
			
			if( "Clave".equals(this.valTipoBusqueda))
				this.listImpuestos = this.ifzConValores.buscaValorGrupo("valorId", this.campoBusqueda, this.pojoGpoImpuestos);
			else 
				if("Nombre del Impuesto".equals(this.valTipoBusqueda))						
					this.listImpuestos = this.ifzConValores.buscaValorGrupo("descripcion", this.campoBusqueda, this.pojoGpoImpuestos);	
				else
					this.listImpuestos = this.ifzConValores.buscaValorGrupo("valorId", "",this.pojoGpoImpuestos); // this.ifzConValores.findAll(this.pojoGpoVal);
			
			if(listImpuestos.isEmpty()){
				resOperacion="La búsqueda no regresó resultados";
			}
		}catch(Exception e){
			log.error(e.getStackTrace());
			return "ERROR";
		}

		return "OK";
	}
			
	public ConGrupoValores getPojoGpoVal() {
		return pojoGpoImpuestos;
	}

	public void setPojoGpoVal(ConGrupoValores pojoGpoVal) {
		this.pojoGpoImpuestos = pojoGpoVal;
	}

	public ConValores getPojoValores() {
		return pojoValores;
	}

	public void setPojoValores(ConValores pojoValores) {
		this.pojoValores = pojoValores;
	}

	public Long getValorId() {	
		return pojoValores.getId() != 0L ? this.pojoValores.getId() : 0;	
	}

	public void setValorId(Long valorId) {
		this.pojoValores.setId(valorId);
	}

	public String getValor() {
		return pojoValores.getValor()!= null ? this.pojoValores.getValor() :"";
	}

	public void setValor(String valor) {		
		this.pojoValores.setValor(valor);
	}
	
	public String getDescripcion() {
		return pojoValores.getDescripcion()!= null ? this.pojoValores.getDescripcion() : "";
	}

	public void setDescripcion(String descripcion) {
		this.pojoValores.setDescripcion(descripcion);
	}

	public Double getAtributo1() {
		double tmp = 0;
		try {
			tmp = pojoValores.getAtributo1()!= null ? Double.valueOf(this.pojoValores.getAtributo1()) : 0D;
		} catch (Exception e) {
			log.error("Error en el Get de atributo1",e);
		}
		return tmp;
	}
	
	public void setAtributo1(Double atributo1) {
		this.pojoValores.setAtributo1(atributo1.toString());
	}

	public String getAtributo2() {
		return this.pojoValores.getAtributo2() != null ? this.pojoValores.getAtributo2() : "0";
	}

	public void setAtributo2(String atributo2) {
		this.pojoValores.setAtributo2(atributo2);
	}

	public String getAtributo3() {
		return this.pojoValores.getAtributo3() != null ? this.pojoValores.getAtributo3() : "0";
	}

	public void setAtributo3(String atributo3) {
		this.pojoValores.setAtributo3(atributo3);
	}

	public ConGrupoValores getGrupoValorId() {
		return this.pojoValores.getGrupoValorId();
	}

	public void setGrupoValorId(ConGrupoValores grupoValorId) {
		this.pojoValores.setGrupoValorId(grupoValorId);
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
	
	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public List<ConValores> getListImpuestos() {
		return listImpuestos;
	}

	public void setListImpuestos(List<ConValores> listImpuestos) {
		this.listImpuestos = listImpuestos;
	}

	public List<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(List<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public String getTipoCuenta() {
		String tipoCuenta= this.pojoValores.getTipoCuenta() != null ? this.pojoValores.getTipoCuenta() : "";
		
		if("DE".equals(tipoCuenta ))
			tipoCuenta="Aumenta";	// se desglosa el impto y se suma al subtotal
		else				
			tipoCuenta="Disminuye"; // se desglosa el impto y se resta al total
		
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		if ("Aumenta".equals(tipoCuenta ))
			tipoCuenta="DE";
		else
			if ("Disminuye".equals(tipoCuenta ))
				tipoCuenta="AC";
			else				
				tipoCuenta="";
		
		this.pojoValores.setTipoCuenta(tipoCuenta);
	}

	public List<String> getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(List<String> tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public boolean isDesglosar() {
		boolean desgloce = this.pojoValores.getAtributo4() == null ? false:(this.pojoValores.getAtributo4() == "1" ? true:false);
		return desgloce;
	}

	public void setDesglosar(boolean desglosar) {
		if(desglosar == true)
			this.pojoValores.setAtributo4("1");
		else
			this.pojoValores.setAtributo4("0");
		this.desglosar = desglosar;
	}
}

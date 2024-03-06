package cde.publico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Sucursal;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.seguridad.beans.Perfil;
import net.giro.plataforma.seguridad.beans.PerfilValor;
import net.giro.plataforma.seguridad.beans.PerfilValorGral;
import net.giro.plataforma.seguridad.beans.Responsabilidad;
import net.giro.plataforma.seguridad.beans.Usuario;
import net.giro.publico.respuesta.Respuesta;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlCalendar;


@KeepAlive
public class PerfilesValoresAction{
	private static Logger log = Logger.getLogger(PerfilesValoresAction.class);
	
	private AdministracionRem ifzAdministracion;
	
	private PerfilValorGral pojoPerfilesGral; 
	private List<PerfilValorGral> 	listValores;
	private List<Responsabilidad> 	listResponsabilidades;
	private List<Usuario> 	listUsuarios;
	private List<ConValores> 	listTerminales;
	private List<Empresa> listEmpresas;
	private List<Sucursal> listSucursales;
	@SuppressWarnings("unused")
	private Context ctx;
	private LoginManager loginManager;
	private HtmlPanelGrid pnlComponentes;
	
    private String resOperacion;
    private String problemInesp;
    private String busquedaVacia;
    private String tipoDatoIndefinido; 
    
    private String 	valorString;
    private Boolean valorBoolean;
    private Date 	valorDate;
    private boolean nivelTerminal;
    private boolean nivelUsuario;
    private boolean nivelResponsabilidad;
    private boolean nivelPuesto;
    private boolean nivelSucursal;
    private boolean nivelEmpresa;
    private boolean nivelSitio;
    private boolean hayRegistros;
    
    private String valDatoTerminal;
    private String valDatoPerfil;
    private String valDatoResponsabilidad;
    private String valDatoUsuario;
    private String valDatoPuesto;
    private String valDatoSucursal;
    private String valDatoEmpresa;
    
    private int numPagina;
    
    @SuppressWarnings("unused")
	private Long usuarioId;
    
    SimpleDateFormat dateFormat = null;
    HashMap <Integer, String> param;
    Pattern pat = null;
	Matcher match = null;
    
    public PerfilesValoresAction() throws NamingException,Exception{
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		InitialContext ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		busquedaVacia = propPlataforma.getString("mensajes.info.busquedaVacia");
		tipoDatoIndefinido = propPlataforma.getString("mensaje.error.tipoDatoIndefinido");
		
		this.ifzAdministracion =  (AdministracionRem)ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
		ifzAdministracion.setInfoSesion(loginManager.getInfoSesion());
		
		this.pojoPerfilesGral = new PerfilValorGral();
		listValores = new ArrayList<PerfilValorGral>();
		this.numPagina = 1;
		hayRegistros = false;   
		pat = Pattern.compile("(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)");
		pnlComponentes = new HtmlPanelGrid();
    }
    
    public void nuevo(){}
    
    public void guardar(){
    	try {
			for(PerfilValorGral pvg:this.listValores){
				if(!pvg.isEditado())
					continue;
				if(pvg.getPojoTerminal() != null){
					pvg.getPojoTerminal().setValorNivel(Long.valueOf(param.get(1)));
					procesaValor(pvg.getPojoTerminal(),	pvg.getPojoPerfil(), 1, Long.valueOf(param.get(1)));
				}
				if(pvg.getPojoUsuario() != null){
					pvg.getPojoUsuario().setValorNivel(Long.valueOf(param.get(2)));
					procesaValor(pvg.getPojoUsuario(),	pvg.getPojoPerfil(), 2, Long.valueOf(param.get(2)));
				}
				if(pvg.getPojoResponsabilidad() != null){
					pvg.getPojoResponsabilidad().setValorNivel(Long.valueOf(param.get(4)));
					procesaValor(pvg.getPojoResponsabilidad(), pvg.getPojoPerfil(), 4, Long.valueOf(param.get(4)));
				}
				if(pvg.getPojoPuesto() != null){
					pvg.getPojoPuesto().setValorNivel(Long.valueOf(param.get(8)));
					procesaValor(pvg.getPojoPuesto(),	pvg.getPojoPerfil(), 8, Long.valueOf(param.get(8)));
				}
				if(pvg.getPojoSucursal() != null){
					pvg.getPojoSucursal().setValorNivel(Long.valueOf(param.get(16)));
					procesaValor(pvg.getPojoSucursal(),		pvg.getPojoPerfil(), 16, Long.valueOf(param.get(16)));
				}
				if(pvg.getPojoEmpresa() != null){
					pvg.getPojoEmpresa().setValorNivel(Long.valueOf(param.get(32)));
					procesaValor(pvg.getPojoEmpresa(), pvg.getPojoPerfil(), 32, Long.valueOf(param.get(32)));
				}
				if(pvg.getPojoSitio() != null)
					procesaValor(pvg.getPojoSitio(), 	pvg.getPojoPerfil(), 64, 0L);
			}
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error al guardar",e);
		}
    }
    
    public void guardarValor(){
    	String valor = null;
    	String descripcion = null;
    	try {
    		valor = getVariableResultado(pojoPerfilesGral.getPojoPerfil().getTipoComponente());
    		descripcion = getDescripcionValor(pojoPerfilesGral.getPojoPerfil(), valor);
    		switch(pojoPerfilesGral.getTipoSeleccion()){
	    		case 1: pojoPerfilesGral.getPojoTerminal().setValorPerfil(valor);
						pojoPerfilesGral.getDescripciones().put("1", descripcion);
						break;
		    	case 2: pojoPerfilesGral.getPojoUsuario().setValorPerfil(valor);
		    			pojoPerfilesGral.getDescripciones().put("2", descripcion);
		    			break;
		    	case 4: pojoPerfilesGral.getPojoResponsabilidad().setValorPerfil(valor);
		    			pojoPerfilesGral.getDescripciones().put("4", descripcion);
		    			break;
		    	case 8: pojoPerfilesGral.getPojoPuesto().setValorPerfil(valor);
		    			pojoPerfilesGral.getDescripciones().put("8", descripcion);
		    			break;
		    	case 16: pojoPerfilesGral.getPojoSucursal().setValorPerfil(valor);
		    			pojoPerfilesGral.getDescripciones().put("16", descripcion);
		    			break;
		    	case 32:pojoPerfilesGral.getPojoEmpresa().setValorPerfil(valor);
		    			pojoPerfilesGral.getDescripciones().put("32", descripcion);
		    			break;
		    	case 64:pojoPerfilesGral.getPojoSitio().setValorPerfil(valor);
		    			pojoPerfilesGral.getDescripciones().put("64", descripcion);
		    			break;
    		}
    		pojoPerfilesGral.setEditado(true);
    		
    		resOperacion = "";
		} catch (Exception e) {
			resOperacion = problemInesp;
			log.error("Error al guardar el valor", e);
		}
    }
    
    private String getDescripcionValor(Perfil perfil, String id){
    	ConValores valor = null;
    	String res = "";
    	try {
    		if(id == null)
    			return "";
    		
    		 
			
			if("comboGrupo".equals(perfil.getTipoComponente()) && perfil.getComponenteSrc() != null){
				Respuesta respuesta = ifzAdministracion.findConValoresById(Long.valueOf(id));
				valor = (ConValores) respuesta.getBody().getValor("pojoValor");
				res = valor != null ? valor.getValor() : "";
			}else if("check".equals(perfil.getTipoComponente())){
				res = "S".equals(id) ? "Habilitado" : "Deshabilitado" ;
			}else
				res = id;
		} catch (Exception e) {
			log.error("Error en metodo getDescripcionValor al guardar el valor", e);
		}
		return res;
    }
    
    private void procesaValor(PerfilValor perVal, Perfil perfil, long nivel, Long valNivel){
    	try {
    			this.resOperacion = "";
		    	if(perVal.getId() > 0 && "".equals(perVal.getValorPerfil())){
		    		Respuesta respuesta = this.ifzAdministracion.eliminar(perVal);
		    		if(respuesta.getErrores().getCodigoError() > 0L)
		    			this.resOperacion = respuesta.getErrores().getDescError();
		    	}
		    	else if(perVal.getId() > 0 && !"".equals(perVal.getValorPerfil())){ 
		    		perVal.setPerfil(perfil);
		    		perVal.setNivel(nivel);
		    		perVal.setValorNivel(valNivel);
		    		
		    		Respuesta respuesta = this.ifzAdministracion.salvar(perVal);
		    		if(respuesta.getErrores().getCodigoError() == 0L)
		    			perVal = (PerfilValor) respuesta.getBody().getValor("pojoPerfilValor");
		    		else
		    			this.resOperacion = respuesta.getErrores().getDescError();
		    	}else if(perVal.getValorPerfil() != null && perVal.getValorPerfil() != ""){
		    		Respuesta respuesta = this.ifzAdministracion.salvar(perVal);
		    		if(respuesta.getErrores().getCodigoError() > 0L)
		    			this.resOperacion = respuesta.getErrores().getDescError();
		    	}
		} catch (Exception e) {
			resOperacion = problemInesp;
			log.error("Error al guardar el valor", e);
		}
    }
    
    @SuppressWarnings("unchecked")
	public void buscar(){
    	this.param = new HashMap <Integer, String>();
    	try {
    		if(this.nivelTerminal && !"".equals(this.valDatoTerminal))
    			agregaParametro(1, this.valDatoTerminal);
    		if(this.nivelUsuario && !"".equals(this.valDatoUsuario))
    			agregaParametro(2, this.valDatoUsuario);
    		if(this.nivelResponsabilidad && !"".equals(this.valDatoResponsabilidad))
    			agregaParametro(4, this.valDatoResponsabilidad);
    		if(this.nivelPuesto && !"".equals(this.valDatoPuesto))
    			agregaParametro(8, this.valDatoPuesto);
    		if(this.nivelSucursal && !"".equals(this.valDatoSucursal))
    			agregaParametro(16, this.valDatoSucursal);
    		if(this.nivelEmpresa && !"".equals(this.valDatoEmpresa))
    			agregaParametro(32, this.valDatoEmpresa);
    		if(this.nivelSitio)
    			param.put(64, "0");
    		
    		if(param.size() > 0){
    			Respuesta respuesta = this.ifzAdministracion.findByNivelPerfil(this.valDatoPerfil, this.param);
    			if(respuesta.getErrores().getCodigoError() == 0L)
    				this.listValores = (List<PerfilValorGral>) respuesta.getBody().getValor("listPerfilValorGrl");
    			else{
    				this.resOperacion = respuesta.getErrores().getDescError();
    				this.listValores = new ArrayList<PerfilValorGral>();
    			}
    		} else
    			this.listValores = new ArrayList<PerfilValorGral>();
			this.resOperacion = this.listValores.isEmpty() ? this.busquedaVacia:"";
			this.hayRegistros = !this.listValores.isEmpty();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error al buscar",e);
		}
    }
    
    public void construyeComponente(long tipo) {
    	construyeComponente((int)tipo);
    }
    
    @SuppressWarnings("unchecked")
	public void construyeComponente(int tipo){
		HtmlCalendar htmCal = null;
		HtmlOutputText htmOutput = null;
		HtmlSelectOneMenu htmCombo = null;
		HtmlInputText htmInput = null;
		HtmlSelectBooleanCheckbox htmCheck = null;
		UISelectItem item = null;
		
		List<ConValores> listValores = null;
		
		FacesContext facesContext;
		ExpressionFactory expressionFactory;
		ValueExpression valExpression = null;
		
		try {
			resOperacion = "";
			pojoPerfilesGral.setTipoSeleccion(tipo);
			facesContext = FacesContext.getCurrentInstance();
			expressionFactory = facesContext.getApplication().getExpressionFactory();
			
			if(this.pnlComponentes.getChildren() != null)
				this.pnlComponentes.getChildren().clear();
			
			this.pnlComponentes.getAttributes().put("columns", 2);
			htmOutput = new HtmlOutputText();
			htmOutput.setValue("check".equals(pojoPerfilesGral.getPojoPerfil().getTipoComponente()) ? "" : "Valor");
			htmOutput.getAttributes().put("styleClass", "Titulo");
			this.pnlComponentes.getChildren().add(htmOutput);
			if("datetime".equals(pojoPerfilesGral.getPojoPerfil().getTipoComponente())){
				htmCal = new HtmlCalendar();
				if(pojoPerfilesGral.getPojoPerfil().getComponenteSrc() != null && !"".equals(pojoPerfilesGral.getPojoPerfil().getComponenteSrc())){
					htmCal.setDatePattern(pojoPerfilesGral.getPojoPerfil().getComponenteSrc());
					dateFormat = new SimpleDateFormat(pojoPerfilesGral.getPojoPerfil().getComponenteSrc());
				}else
					dateFormat = new SimpleDateFormat("dd/MM/yyy");

				valExpression = expressionFactory.createValueExpression(facesContext.getELContext(), "#{PerfilesValoresAction.valorDate}", Date.class);
				
				if(getCampoDato(tipo) != null)
					htmCal.setValue(dateFormat.parseObject(getCampoDato(tipo)));
				htmCal.setValueExpression("value", valExpression);
				this.pnlComponentes.getChildren().add(htmCal);
			}else if("comboList".equals(pojoPerfilesGral.getPojoPerfil().getTipoComponente())){
				htmCombo = new HtmlSelectOneMenu();
				valExpression = expressionFactory.createValueExpression(facesContext.getELContext(), "#{PerfilesValoresAction.valorString}", String.class);
				if(pojoPerfilesGral.getPojoPerfil().getComponenteSrc() != null && !"".equals(pojoPerfilesGral.getPojoPerfil().getComponenteSrc())){
					for(String s : pojoPerfilesGral.getPojoPerfil().getComponenteSrc().split(",")){
						item = new UISelectItem();
					    item.setItemLabel(s);
					    item.setItemValue(s);
					    htmCombo.getChildren().add(item);
					}
				}
				if(getCampoDato(tipo) != null)
					htmCombo.setValue(getCampoDato(tipo));
				htmCombo.setValueExpression("value", valExpression);
				this.pnlComponentes.getChildren().add(htmCombo);
			}else if("comboGrupo".equals(pojoPerfilesGral.getPojoPerfil().getTipoComponente())){
				htmCombo = new HtmlSelectOneMenu();
				valExpression = expressionFactory.createValueExpression(facesContext.getELContext(), "#{PerfilesValoresAction.valorString}", String.class);
				
				
				if(pojoPerfilesGral.getPojoPerfil().getComponenteSrc() != null && !"".equals(pojoPerfilesGral.getPojoPerfil().getComponenteSrc())){
					Respuesta respuesta = ifzAdministracion.findConValoresByGrupoNombre(pojoPerfilesGral.getPojoPerfil().getComponenteSrc());
					listValores = (List<ConValores>) respuesta.getBody().getValor("listValores");
					for(ConValores c : listValores){
						item = new UISelectItem();
					    item.setItemLabel(c.getValor());
					    item.setItemValue(c.getId());
					    htmCombo.getChildren().add(item);
					}
				}

				if(getCampoDato(tipo) != null)
					htmCombo.setValue(getCampoDato(tipo));
				htmCombo.setValueExpression("value", valExpression);
				this.pnlComponentes.getChildren().add(htmCombo);
			}else if("comboBean".equals(pojoPerfilesGral.getPojoPerfil().getTipoComponente())){
				/*htmCombo = new HtmlSelectOneMenu();
				valExpression = expressionFactory.createValueExpression(facesContext.getELContext(), getCampoExpresion(tipo), String.class);
				if(pojoPerfilesGral.getPojoPerfil().getComponenteSrc() != null && !"".equals(pojoPerfilesGral.getPojoPerfil().getComponenteSrc())){
					for(String s : pojoPerfilesGral.getPojoPerfil().getComponenteSrc().split(",")){
						item = new UISelectItem();
					    item.setItemLabel(str);
					    item.setItemValue(str);
					    htmCombo.getChildren().add(item);
					}
				}
				
				if(ifzConValores == null){
					this.lookup = this.ctx.lookup("ConValoresFacade/remote");
					this.ifzConValores = (ConValoresFacadeRemote)PortableRemoteObject.narrow(this.lookup, ConValoresFacadeRemote.class);
				}
				
				if(pojoPerfilesGral.getPojoPerfil().getComponenteSrc() != null && !"".equals(pojoPerfilesGral.getPojoPerfil().getComponenteSrc())){
					listValores = ifzConValores.findByGrupoNombre(pojoPerfilesGral.getPojoPerfil().getComponenteSrc());
					for(ConValores c : listValores){
						item = new UISelectItem();
					    item.setItemLabel(c.getValor());
					    item.setItemValue(c.getValorId().toString());
					    htmCombo.getChildren().add(item);
					}
				}

				if(getCampoDato(tipo) != null)
					htmCombo.setValue(getCampoDato(tipo));
				htmCombo.setValueExpression("value", valExpression);
				this.pnlComponentes.getChildren().add(htmCombo);*/
			}else if("input".equals(pojoPerfilesGral.getPojoPerfil().getTipoComponente())){
				htmInput = new HtmlInputText();
				valExpression = expressionFactory.createValueExpression(facesContext.getELContext(), "#{PerfilesValoresAction.valorString}", String.class);
				if(getCampoDato(tipo) != null)
					htmInput.setValue(getCampoDato(tipo));
				htmInput.setValueExpression("value", valExpression);
				this.pnlComponentes.getChildren().add(htmInput);
			}else if("check".equals(pojoPerfilesGral.getPojoPerfil().getTipoComponente())){
				htmCheck = new HtmlSelectBooleanCheckbox();
				htmCheck.setId("cbHabilitar");
				
				HtmlPanelGroup pnlGrp = new HtmlPanelGroup();
				HtmlOutputLabel lbl = new HtmlOutputLabel();
				lbl.setId("lblHabilitar");
				lbl.setValue("Habilitar");
				lbl.setStyleClass("Titulo");
				lbl.setFor("cbHabilitar");
				valExpression = expressionFactory.createValueExpression(facesContext.getELContext(), "#{PerfilesValoresAction.valorBoolean}", Boolean.class);
				
				htmCheck.setValue(getCampoDato(tipo) != null && "S".equals(getCampoDato(tipo)));
				htmCheck.setValueExpression("value", valExpression);
				
				pnlGrp.getChildren().add(htmCheck);
				pnlGrp.getChildren().add(lbl);
				this.pnlComponentes.getChildren().add(pnlGrp);
			}else{
				resOperacion = tipoDatoIndefinido;
			}
			
		} catch (Exception e) {
			resOperacion = problemInesp;
			log.error("Error al generar el componente JSP", e);
		}
    }
    
    private String getVariableResultado(String tipoDato){
    	if("check".equals(tipoDato))
    		return valorBoolean ? "S" : "N";
    	else if("datetime".equals(tipoDato)){
    		if(pojoPerfilesGral.getPojoPerfil().getComponenteSrc() != null && !"".equals(pojoPerfilesGral.getPojoPerfil().getComponenteSrc()))
    			dateFormat = new SimpleDateFormat(pojoPerfilesGral.getPojoPerfil().getComponenteSrc());
    		else
    			dateFormat = new SimpleDateFormat("dd/MM/yyy");
    		return dateFormat.format(valorDate);
    	}else
    		return valorString;
    }
    
    private String getCampoDato(int tipo){
    	switch(tipo){
    		case 1:  return pojoPerfilesGral.getPojoTerminal().getValorPerfil();
	    	case 2:  return pojoPerfilesGral.getPojoUsuario().getValorPerfil();
	    	case 4:  return pojoPerfilesGral.getPojoResponsabilidad().getValorPerfil();
	    	case 8:  return pojoPerfilesGral.getPojoPuesto().getValorPerfil();
	    	case 16:  return pojoPerfilesGral.getPojoSucursal().getValorPerfil();
	    	case 32:  return pojoPerfilesGral.getPojoEmpresa().getValorPerfil();
	    	case 64:  return pojoPerfilesGral.getPojoSitio().getValorPerfil();
	    	default: return null;
    	}
    }
    
    public void agregaParametro(int nivel, String val){
    	String valorNivel = null;
    	try {
			match = pat.matcher(val);
			match.find();
			valorNivel = match.group(1);
			param.put(nivel, valorNivel); 
		} catch (Exception e) {
			log.info("Error en el metodo agregaParametro", e);
		}
    }
    
    @SuppressWarnings("unchecked")
	public List<ConValores> autoacompletaTerminales(Object obj){
    	try {
    		Respuesta respuesta = ifzAdministracion.findConValoresByGrupoNombre("SYS_TERMINALES");
    		if(respuesta.getErrores().getCodigoError() == 0L){
    			listTerminales = (List<ConValores>) respuesta.getBody().getValor("listValores");
        		if(listTerminales.isEmpty())
        			listTerminales = new ArrayList<ConValores>();
    		} else
    			this.resOperacion = respuesta.getErrores().getDescError();
    	} catch (Exception e) {
			log.error("Error en metodo autoacompletaTerminales", e);
			return new ArrayList<ConValores>();
		}
		return listTerminales;
	}
    
    @SuppressWarnings("unchecked")
	public List<Usuario> autoacompletaUsuarios(Object obj){
    	try {
    		Respuesta respuesta = this.ifzAdministracion.findUsuariosLikeClaveUsuario(String.valueOf(obj));
    		if(respuesta.getErrores().getCodigoError() == 0L){
    			listUsuarios = (List<Usuario>) respuesta.getBody().getValor("listUsuarios");
        		if(listUsuarios.isEmpty())
        			listUsuarios = new ArrayList<Usuario>();
    		} else
    			this.resOperacion = respuesta.getErrores().getDescError();
    	} catch (Exception e) {
			log.error("Error en metodo autoacompletaUsuarios", e);
			return new ArrayList<Usuario>();
		}
		return listUsuarios;
	}
    @SuppressWarnings("unchecked")
	public List<Empresa> autoacompletaEmpresa(Object obj){
    	try {
    		listEmpresas = new ArrayList<Empresa>();
    		Respuesta respuesta = this.ifzAdministracion.findEmpresasLikeClaveUsuario(String.valueOf(obj));
    		if(respuesta.getErrores().getCodigoError() == 0L){
    			listEmpresas = (List<Empresa>)respuesta.getBody().getValor("listEmpresas");
        		if(listEmpresas.isEmpty())
        			listEmpresas = new ArrayList<Empresa>();
    		} else
    			this.resOperacion = respuesta.getErrores().getDescError();
    	} catch (Exception e) {
			log.error("Error en metodo autoacompletaEmpresa", e);
			return new ArrayList<Empresa>();
		}
		return listEmpresas;
	}
    @SuppressWarnings("unchecked")
	public List<Sucursal> autoacompletaSucursales(Object obj){
    	try {
    		listSucursales = new ArrayList<Sucursal>();
    		Respuesta respuesta = this.ifzAdministracion.findSucursalesLikeClaveUsuario(String.valueOf(obj));
    		if(respuesta.getErrores().getCodigoError() == 0L){
    			listSucursales = (List<Sucursal>) respuesta.getBody().getValor("listSucursales");
        		if(listSucursales.isEmpty())
        			listSucursales = new ArrayList<Sucursal>();
    		} else
    			this.resOperacion = respuesta.getErrores().getDescError();
    	} catch (Exception e) {
			log.error("Error en metodo autoacompletaSucursales", e);
			return new ArrayList<Sucursal>();
		}
		return listSucursales;
	}
    
    
    
    @SuppressWarnings("unchecked")
	public List<Responsabilidad> autoacompletaResponsabilidades(Object obj){
    	try {
    		Respuesta respuesta = this.ifzAdministracion.findResponsabilidadesLikeClaveUsuario(String.valueOf(obj));
    		if(respuesta.getErrores().getCodigoError() == 0L){
    			listResponsabilidades = (List<Responsabilidad>) respuesta.getBody().getValor("listResponsabilidades");
        		if(listResponsabilidades.isEmpty())
        			listResponsabilidades = new ArrayList<Responsabilidad>();
    		} else
    			this.resOperacion = respuesta.getErrores().getDescError();
    	} catch (Exception e) {
			log.error("Error en metodo autoacompletaResponsabilidades", e);
			return new ArrayList<Responsabilidad>();
		}
		return listResponsabilidades;
	}
    
   
    
	public void setPojoPerfilesGral(PerfilValorGral pojoPerfilesGral) {
		this.pojoPerfilesGral = pojoPerfilesGral;
	}

	public PerfilValorGral getPojoPerfilesGral() {
		return pojoPerfilesGral;
	}

	public void setListValores(List<PerfilValorGral> listValores) {
		this.listValores = listValores;
	}

	public List<PerfilValorGral> getListValores() {
		return listValores;
	}

	public boolean isNivelTerminal() {
		return nivelTerminal;
	}

	public void setNivelTerminal(boolean nivelTerminal) {
		this.nivelTerminal = nivelTerminal;
	}
	
	public boolean isNivelUsuario() {
		return nivelUsuario;
	}

	public void setNivelUsuario(boolean nivelUsuario) {
		this.nivelUsuario = nivelUsuario;
	}

	public void setNivelSucursal(boolean nivelSucursal) {
		this.nivelSucursal = nivelSucursal;
	}

	public boolean isNivelSucursal() {
		return nivelSucursal;
	}

	public boolean isNivelPuesto() {
		return nivelPuesto;
	}

	public void setNivelPuesto(boolean nivelPuesto) {
		this.nivelPuesto = nivelPuesto;
	}

	public boolean isNivelEmpresa() {
		return nivelEmpresa;
	}

	public void setNivelEmpresa(boolean nivelEmpresa) {
		this.nivelEmpresa = nivelEmpresa;
	}

	public boolean isNivelSitio() {
		return nivelSitio;
	}

	public void setNivelSitio(boolean nivelSitio) {
		this.nivelSitio = nivelSitio;
	}
	public String getValDatoTerminal() {
		return valDatoTerminal;
	}

	public void setValDatoTerminal(String valDatoTerminal) {
		this.valDatoTerminal = valDatoTerminal;
	}
	public String getValDatoUsuario() {
		return valDatoUsuario;
	}

	public void setValDatoUsuario(String valDatoUsuario) {
		this.valDatoUsuario = valDatoUsuario;
	}

	public String getValDatoPuesto() {
		return valDatoPuesto;
	}

	public void setValDatoPuesto(String valDatoPuesto) {
		this.valDatoPuesto = valDatoPuesto;
	}

	public String getValDatoSucursal() {
		return valDatoSucursal;
	}

	public void setValDatoSucursal(String valDatoSucursal) {
		this.valDatoSucursal = valDatoSucursal;
	}

	public String getValDatoEmpresa() {
		return valDatoEmpresa;
	}

	public void setValDatoEmpresa(String valDatoEmpresa) {
		this.valDatoEmpresa = valDatoEmpresa;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setValDatoPerfil(String valDatoPerfil) {
		this.valDatoPerfil = valDatoPerfil;
	}

	public String getValDatoPerfil() {
		return valDatoPerfil;
	}

	public void setHayRegistros(boolean hayRegistros) {
		this.hayRegistros = hayRegistros;
	}

	public boolean isHayRegistros() {
		return hayRegistros;
	}

	public void setNivelResponsabilidad(boolean nivelResponsabilidad) {
		this.nivelResponsabilidad = nivelResponsabilidad;
	}

	public boolean isNivelResponsabilidad() {
		return nivelResponsabilidad;
	}

	public void setValDatoResponsabilidad(String valDatoResponsabilidad) {
		this.valDatoResponsabilidad = valDatoResponsabilidad;
	}

	public String getValDatoResponsabilidad() {
		return valDatoResponsabilidad;
	}

	public void setPnlComponentes(HtmlPanelGrid pnlComponentes) {
		this.pnlComponentes = pnlComponentes;
	}

	public HtmlPanelGrid getPnlComponentes() {
		return pnlComponentes;
	}

	public void setValorString(String valorString) {
		this.valorString = valorString;
	}

	public String getValorString() {
		return valorString;
	}

	public void setValorBoolean(Boolean valorBoolean) {
		this.valorBoolean = valorBoolean;
	}

	public Boolean getValorBoolean() {
		return valorBoolean;
	}

	public void setValorDate(Date valorDate) {
		this.valorDate = valorDate;
	}

	public Date getValorDate() {
		return valorDate;
	}
}

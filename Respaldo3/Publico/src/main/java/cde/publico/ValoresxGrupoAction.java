package cde.publico;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.Atributo;
import net.giro.plataforma.MiMapa;
import net.giro.plataforma.ReversaUtil;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.*;
import org.richfaces.component.html.HtmlCalendar;

@KeepAlive
@SuppressWarnings("rawtypes")
public class ValoresxGrupoAction implements Serializable {
	private static final long serialVersionUID = 8430415521429345440L;
	private static Logger log = Logger.getLogger(ValoresxGrupoAction.class);
	private Context ctx;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConVal;
	private ConGrupoValores pojoGpoVal;
	private ConValores	pojoConValores;
	private List<ConValores> listValores; 
	private List<String>     listAnios;
	private List<Atributo>	listErrores;
	private HtmlPanelGrid pnlComponentes;
	private ExpressionFactory expressionFactory;
	Pattern pat = null;
	Matcher match = null;
	private MiMapa miMapa;
	private String suggGrupo;
	private String suggGrupoBusqueda;
	private String valor;
    private String resOperacion;
    private String problemInesp;
    private String busquedaVacia;
    private String grupo ;
    private int	numPagina;
    private LoginManager loginManager;
    private PropertyResourceBundle propPlataforma;
    
    public ValoresxGrupoAction() throws NamingException, LoginException{
    	
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application app = facesContext.getApplication();
		HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
		this.grupo = request.getParameter("GRUPO");
		this.expressionFactory = app.getExpressionFactory();
		ValueExpression dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)dato.getValue(facesContext.getELContext());
		ctx = loginManager.getCtx();
		
		listValores=new ArrayList<ConValores>();
    	listErrores = new ArrayList<Atributo>();
		this.numPagina = 1;
		pojoConValores = new ConValores();
		
		this.ifzGpoVal = (ConGrupoValoresRem)	
				ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
		ifzGpoVal.setInfoSesion(loginManager.getInfoSesion());
		this.ifzConVal = (ConValoresRem)	
				ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
		ifzConVal.setInfoSesion(loginManager.getInfoSesion());
    	
		dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle)dato.getValue(facesContext.getELContext());

		problemInesp = propPlataforma.getString("mensajes.error.inesperado");
		busquedaVacia =  propPlataforma.getString("mensajes.info.busquedaVacia");
		 
		this.listAnios = new ArrayList<String>();
		for(int i = 1980; i <= Calendar.getInstance().get(Calendar.YEAR) + 1; i++)
			this.listAnios.add(String.valueOf(i));
		this.miMapa = new MiMapa(); 
		if (grupo != null) {
			if ( grupo.equals(""))
					log.error("parametro grupo en blanco no es valido");
			Respuesta respuesta = this.ifzGpoVal.findByProperty("nombre", this.grupo);
			if(respuesta.getErrores().getCodigoError() == 0L){
				@SuppressWarnings("unchecked")
				List<ConGrupoValores> listTmpGrupos = (List<ConGrupoValores>) respuesta.getBody().getValor("listGrupoValores");
				//List<ConGrupoValores> listTmpGrupos = this.ifzGpoVal.findByColumnName("nombre", this.grupo);	 
				if (listTmpGrupos.size() == 1)
					pojoGpoVal = listTmpGrupos.get(0); 
			} else{
				this.resOperacion = respuesta.getErrores().getDescError();
				log.info("no se encontro el grupo " + this.grupo);
			}
		} else
			log.info("grupo viene nulo");
    }

    
    public String getTituloGrupo() {
	   return propPlataforma.getString("valores") + " " + (grupo == null ? "" : grupo);
    }
    
    public void nuevo(){
		this.pojoConValores = new ConValores();
		this.suggGrupo = "";
		this.resOperacion = "";
		cargaAtributos();
    }
    
    @SuppressWarnings("unchecked")
	public void buscar(){
    	try {
    		this.resOperacion = "";
    		Respuesta respuesta = this.ifzConVal.findLikeClaveValorGrupo(this.valor, this.pojoGpoVal, 0);
    		if(respuesta.getErrores().getCodigoError() == 0L){
    			this.listValores = (List<ConValores>) respuesta.getBody().getValor("listValores");
    			if(this.listValores.isEmpty())
    				this.resOperacion = busquedaVacia;
    		} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en metodo buscar", e);
		}
    }
    
    public void guardar(){
    	try {
    		this.resOperacion = "";
			this.pojoConValores.setGrupoValorId(this.pojoGpoVal);
			long id = pojoConValores.getId();
			
			//si no pudo salvar fue por un formato invalido
			if(! salvaAtributos())
				return;
			
			Respuesta respuesta = ifzConVal.salvar(pojoConValores);
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
				
				if(id <= 0L) {
					this.listValores.add(this.pojoConValores);
					// init
					//agregaLote3();
					// end
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error en metodo guardar", e);
			this.resOperacion = this.problemInesp;
		}
    }
    
    public void agregaLote1() {
    	Respuesta respuesta = null;

		this.pojoConValores.setDescripcion("GASTOS DE OPERACION OFICINA LA PAZ");
		this.pojoConValores.setValor("GOP");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);
		
		this.pojoConValores.setDescripcion("GASTOS DE OPERACION OFICINA LOS CABOS");
		this.pojoConValores.setValor("GOC");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);
		
		this.pojoConValores.setDescripcion("SUELDOS Y SALARIOS");
		this.pojoConValores.setValor("SS");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);
		
		this.pojoConValores.setDescripcion("SUBCONTRATO LOS CABOS");
		this.pojoConValores.setValor("SC");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);
		
		this.pojoConValores.setDescripcion("SUBCONTRATO LA PAZ");
		this.pojoConValores.setValor("SP");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);
		
		this.pojoConValores.setDescripcion("SUBCONTRATO LORETO");
		this.pojoConValores.setValor("SL");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);
    }
    
    public void agregaLote2() {
    	Respuesta respuesta = null;

		this.pojoConValores.setDescripcion("ACTIVO FIJO-MANTENIMIENTO MAQUINARIA");
		this.pojoConValores.setValor("MM");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MOBILIARIO DE OFICINA");
		this.pojoConValores.setValor("MO");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("EQUIPO DE OFICINA");
		this.pojoConValores.setValor("EO");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("EQUIPO DE COMPUTO");
		this.pojoConValores.setValor("EC");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ADQUISICIÓN DE INMUEBLES EN LA PAZ- EDIFICIO");
		this.pojoConValores.setValor("AI");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("EQUIPO DE TRABAJO");
		this.pojoConValores.setValor("ET");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("COMPRA DE TERRENO");
		this.pojoConValores.setValor("CT");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("OBRA EN PROCESO");
		this.pojoConValores.setValor("OP");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("INVENTARIO FINAL");
		this.pojoConValores.setValor("IN");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ANTICIPO DE CONTRIBUCIONES");
		this.pojoConValores.setValor("AC");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("CONTRIBUCIONES A FAVOR");
		this.pojoConValores.setValor("CF");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("OTROS GASTOS");
		this.pojoConValores.setValor("OG");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SUBSIDIO AL EMPLEO");
		this.pojoConValores.setValor("SE");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("OTROS INGRESOS");
		this.pojoConValores.setValor("OI");
		this.pojoConValores.setAtributo1("10002580");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LA PAZ");
		this.pojoConValores.setValor("LP");
		this.pojoConValores.setAtributo1("10002581");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("IMPUESTOS Y CUOTAS");
		this.pojoConValores.setValor("IC");
		this.pojoConValores.setAtributo1("10002581");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GASTOS FINANCIEROS");
		this.pojoConValores.setValor("GF");
		this.pojoConValores.setAtributo1("10002581");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("NO DEDUCIBLES LPZ");
		this.pojoConValores.setValor("NP");
		this.pojoConValores.setAtributo1("10002581");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PTU POR PAGAR");
		this.pojoConValores.setValor("PT");
		this.pojoConValores.setAtributo1("10002581");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LOS CABOS");
		this.pojoConValores.setValor("SJ");
		this.pojoConValores.setAtributo1("10002582");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("NO DEDUCIBLES LOS CABOS");
		this.pojoConValores.setValor("NS");
		this.pojoConValores.setAtributo1("10002582");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("QUINCENAL");
		this.pojoConValores.setValor("QU");
		this.pojoConValores.setAtributo1("10002583");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SEMANAL");
		pojoConValores.setValor("SE");
		this.pojoConValores.setAtributo1("10002583");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("VARIOS");
		this.pojoConValores.setValor("VA");
		this.pojoConValores.setAtributo1("10002583");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LOS CABOS");
		this.pojoConValores.setValor("SJ");
		this.pojoConValores.setAtributo1("10002584");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LA PAZ");
		this.pojoConValores.setValor("LP");
		this.pojoConValores.setAtributo1("10002585");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LORETO");
		this.pojoConValores.setValor("LT");
		this.pojoConValores.setAtributo1("10002586");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);
    }
    
    public void agregaLote3() {
    	Respuesta respuesta = null;

		this.pojoConValores.setDescripcion("UTILITARIO");
		this.pojoConValores.setValor("UT");
		this.pojoConValores.setAtributo1("10002587");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PICK UPS");
		this.pojoConValores.setValor("PU");
		this.pojoConValores.setAtributo1("10002587");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("REMOLQUES");
		this.pojoConValores.setValor("RE");
		this.pojoConValores.setAtributo1("10002587");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("CAMIONES");
		this.pojoConValores.setValor("CA");
		this.pojoConValores.setAtributo1("10002587");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PIEZAS Y ACCESORIOS  VEHÍCULOS");
		this.pojoConValores.setValor("PA");
		this.pojoConValores.setAtributo1("10002587");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO MAQUINARIA");
		this.pojoConValores.setValor("MM");
		this.pojoConValores.setAtributo1("10002588");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MOBILIARIO DE OFICINA");
		this.pojoConValores.setValor("MO");
		this.pojoConValores.setAtributo1("10002589");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("EQUIPO DE OFICINA");
		this.pojoConValores.setValor("EO");
		this.pojoConValores.setAtributo1("10002590");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SOFTWARE");
		this.pojoConValores.setValor("SW");
		this.pojoConValores.setAtributo1("10002591");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("EQUIPO DE COMPUTO");
		this.pojoConValores.setValor("EC");
		this.pojoConValores.setAtributo1("10002591");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("IMPRESORAS");
		this.pojoConValores.setValor("IM");
		this.pojoConValores.setAtributo1("10002591");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PLOTTERS");
		this.pojoConValores.setValor("PL");
		this.pojoConValores.setAtributo1("10002591");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LA PAZ");
		this.pojoConValores.setValor("LPZ");
		this.pojoConValores.setAtributo1("10002592");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LOS CABOS");
		this.pojoConValores.setValor("SJC");
		this.pojoConValores.setAtributo1("10002592");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MAQUINARIA Y EQUIPO MAYOR");
		this.pojoConValores.setValor("ME");
		this.pojoConValores.setAtributo1("10002593");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LA PAZ");
		this.pojoConValores.setValor("LPZ");
		this.pojoConValores.setAtributo1("10002594");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LOS CABOS");
		this.pojoConValores.setValor("SJC");
		this.pojoConValores.setAtributo1("10002594");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LORETO");
		this.pojoConValores.setValor("LTO");
		this.pojoConValores.setAtributo1("10002594");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("CHAMETLA");
		this.pojoConValores.setValor("CH");
		this.pojoConValores.setAtributo1("10002595");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("EL ZACATAL");
		this.pojoConValores.setValor("ZA");
		this.pojoConValores.setAtributo1("10002595");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LA PAZ");
		this.pojoConValores.setValor("LPZ");
		this.pojoConValores.setAtributo1("10002596");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LOS CABOS");
		this.pojoConValores.setValor("SJC");
		this.pojoConValores.setAtributo1("10002596");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PAGO PROV.ISR");
		this.pojoConValores.setValor("PP");
		this.pojoConValores.setAtributo1("10002597");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PAGO DE LO INDEBIDO");
		this.pojoConValores.setValor("PI");
		this.pojoConValores.setAtributo1("10002598");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("OTROS GASTOS");
		this.pojoConValores.setValor("OG");
		this.pojoConValores.setAtributo1("10002599");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ACREEDITAMIENTOS");
		this.pojoConValores.setValor("AC");
		this.pojoConValores.setAtributo1("10002600");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("OTROS INGRESOS");
		this.pojoConValores.setValor("OI");
		this.pojoConValores.setAtributo1("10002601");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GASTOS DE OPERACIÓN SIN IVA TASA 0% LA PAZ");
		this.pojoConValores.setValor("GS");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("HONORARIOS LA PAZ");
		this.pojoConValores.setValor("HO");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ARRENDAMIENTO LORETO");
		this.pojoConValores.setValor("ARL");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ARRENDAMIENTO LA PAZ");
		this.pojoConValores.setValor("ARP");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("TELEFONOS LA PAZ");
		this.pojoConValores.setValor("TE");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("TELEFONOS LORETO");
		this.pojoConValores.setValor("TEL");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO EQUIPO DE TRANSPORTE LA PAZ");
		this.pojoConValores.setValor("MET");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO EQUIPO DE COMPUTO LA PAZ");
		this.pojoConValores.setValor("MEC");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO EDIFICIO LA PAZ");
		this.pojoConValores.setValor("ME");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("COMBUSTIBLE");
		this.pojoConValores.setValor("CO");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("UNIFORMES");
		this.pojoConValores.setValor("UN");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GASTOS CAFETERIA LA PAZ");
		this.pojoConValores.setValor("GC");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GASTOS DE VIAJE LA PAZ");
		this.pojoConValores.setValor("GV");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MENSAJERIA Y PAQUETERIA LA PAZ");
		this.pojoConValores.setValor("MP");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PROPAGANDA Y PUBLICIDAD LA PAZ");
		this.pojoConValores.setValor("PP");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PAPELERIA LA PAZ");
		this.pojoConValores.setValor("PA");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("AGUA POTABLE LA PAZ");
		this.pojoConValores.setValor("AP");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ENERGIA ELECTRICA LA PAZ");
		this.pojoConValores.setValor("EE");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SEGURIDAD LA PAZ");
		this.pojoConValores.setValor("SE");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("RENTA DE EQUIPO LA PAZ");
		this.pojoConValores.setValor("RE");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("COMISION DE GASOLINA");
		this.pojoConValores.setValor("CG");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SEGUROS Y FIANZAS LA PAZ");
		this.pojoConValores.setValor("SF");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SERVICIO SANITARIO LA PAZ");
		this.pojoConValores.setValor("SS");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("CAPACITACIÓN LA PAZ");
		this.pojoConValores.setValor("CA");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ARTICULOS DE LIMPIEZA LA PAZ");
		this.pojoConValores.setValor("AL");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO EQUIPO DE OFICINA LA PAZ");
		this.pojoConValores.setValor("MEO");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANIOBRAS LA PAZ");
		this.pojoConValores.setValor("MA");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MUEBLES DEPARTAMENTOS LA PAZ");
		this.pojoConValores.setValor("MD");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO EQUIPO DE TRABAJO MENOR LA PAZ");
		this.pojoConValores.setValor("MEM");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("DIVERSOS LA PAZ");
		this.pojoConValores.setValor("DI");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GASTOS NAVIDEÑOS Y DE ANIVERSARIO LA PAZ");
		this.pojoConValores.setValor("GN");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PRUEBAS ELECTRICAS  LA PAZ");
		this.pojoConValores.setValor("PE");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("HERRAMIENTAS Y EQUIPO MENOR LA PAZ");
		this.pojoConValores.setValor("HE");
		this.pojoConValores.setAtributo1("10002602");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("IMSS");
		this.pojoConValores.setValor("IMS");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("RCV");
		this.pojoConValores.setValor("RCV");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("INFONAVIT");
		this.pojoConValores.setValor("IF");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("IEPS");
		this.pojoConValores.setValor("IEP");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ACTUALIZACIONES");
		this.pojoConValores.setValor("AC");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("RECARGOS");
		this.pojoConValores.setValor("RE");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("TENENCIA");
		this.pojoConValores.setValor("TE");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("IMPUESTO SOBRE NOMINA 2.5");
		this.pojoConValores.setValor("IN");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ALTA PADRON VEHICULAR");
		this.pojoConValores.setValor("AP");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("REVISTA VEHICULAR");
		this.pojoConValores.setValor("RV");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LICENCIA TRANSPORTE DE PERSONAL");
		this.pojoConValores.setValor("LT");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("LICENCIA GIROS COMERCIALES");
		this.pojoConValores.setValor("LG");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("TARIFA DE USO DE AEROPUERTO (TUA");
		this.pojoConValores.setValor("UA");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("IVA POR PAGAR");
		this.pojoConValores.setValor("IPP");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ISR PAGADO");
		this.pojoConValores.setValor("ISP");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ISR DEL EJERCICIO");
		this.pojoConValores.setValor("ISE");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PTU DEL EJERCICIO");
		this.pojoConValores.setValor("PTU");
		this.pojoConValores.setAtributo1("10002603");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("COMISIONES BANCARIAS");
		this.pojoConValores.setValor("CB");
		this.pojoConValores.setAtributo1("10002604");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("INTERES PRESTAMOS");
		this.pojoConValores.setValor("IP");
		this.pojoConValores.setAtributo1("10002604");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("COMISIONES PROVEEDORES");
		this.pojoConValores.setValor("CP");
		this.pojoConValores.setAtributo1("10002604");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("INTERES MONETARIO");
		this.pojoConValores.setValor("IM");
		this.pojoConValores.setAtributo1("10002604");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PERDIDA EN TIPO DE CAMBIO");
		this.pojoConValores.setValor("PTC");
		this.pojoConValores.setAtributo1("10002604");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ERROR EN RFC");
		this.pojoConValores.setValor("ER");
		this.pojoConValores.setAtributo1("10002605");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MULTAS SAT");
		this.pojoConValores.setValor("MS");
		this.pojoConValores.setAtributo1("10002605");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("NO INDISPENSABLE P/ ACTIVIDAD");
		this.pojoConValores.setValor("NI");
		this.pojoConValores.setAtributo1("10002605");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SIN REQUISITOS FISCALES");
		this.pojoConValores.setValor("SR");
		this.pojoConValores.setAtributo1("10002605");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SIN COMPROBANTE FISCAL");
		this.pojoConValores.setValor("SC");
		this.pojoConValores.setAtributo1("10002605");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("DIFERENCIA EN FACTURA");
		this.pojoConValores.setValor("DF");
		this.pojoConValores.setAtributo1("10002605");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PTU POR PAGAR");
		this.pojoConValores.setValor("PP");
		this.pojoConValores.setAtributo1("10002606");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GASTOS DE OPERACIÓN SIN IVA TASA 0% LOS CABOS");
		this.pojoConValores.setValor("GS");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("HONORARIOS LOS CABOS");
		this.pojoConValores.setValor("HO");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ARRENDAMIENTO LOS CABOS");
		this.pojoConValores.setValor("ARS");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("TELEFONOS LOS CABOS");
		this.pojoConValores.setValor("TE");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO EQUIPO DE TRANSPORTE LOS CABOS");
		this.pojoConValores.setValor("MET");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO EQUIPO DE COMPUTO LOS CABOS");
		this.pojoConValores.setValor("MEC");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO EDIFICIO LOS CABOS");
		this.pojoConValores.setValor("ME");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GASTOS CAFETERIA LOS CABOS");
		this.pojoConValores.setValor("GC");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GASTOS DE VIAJE LOS CABOS");
		this.pojoConValores.setValor("GV");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MENSAJERIA Y PAQUETERIA LOS CABOS");
		this.pojoConValores.setValor("MP");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PROPAGANDA Y PUBLICIDAD LOS CABOS");
		this.pojoConValores.setValor("PP");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PAPELERIA LOS CABOS");
		this.pojoConValores.setValor("PA");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("AGUA POTABLE LOS CABOS");
		this.pojoConValores.setValor("AP");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ENERGIA ELECTRICA LOS CABOS");
		this.pojoConValores.setValor("EE");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SEGURIDAD LOS CABOS");
		this.pojoConValores.setValor("SE");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("RENTA DE EQUIPO LOS CABOS");
		this.pojoConValores.setValor("RE");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SEGUROS Y FIANZAS LOS CABOS");
		this.pojoConValores.setValor("SF");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SERVICIO SANITARIO LOS CABOS");
		this.pojoConValores.setValor("SS");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("CAPACITACIÓN LOS CABOS");
		this.pojoConValores.setValor("CA");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ARTICULOS DE LIMPIEZA LOS CABOS");
		this.pojoConValores.setValor("AL");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO EQUIPO DE OFICINA LOS CABOS");
		this.pojoConValores.setValor("MEO");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANIOBRAS LOS CABOS");
		this.pojoConValores.setValor("MA");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MUEBLES DEPARTAMENTOS LOS CABOS");
		this.pojoConValores.setValor("MD");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MANTENIMIENTO EQUIPO DE TRABAJO MENOR LOS CABOS");
		this.pojoConValores.setValor("MEM");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("DIVERSOS LOS CABOS");
		this.pojoConValores.setValor("DI");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GASTOS NAVIDEÑOS Y DE ANIVERSARIO LOS CABOS");
		this.pojoConValores.setValor("GN");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PRUEBAS ELECTRICAS  LOS CABOS");
		this.pojoConValores.setValor("PE");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("HERRAMIENTAS Y EQUIPO MENOR LOS CABOS");
		this.pojoConValores.setValor("HE");
		this.pojoConValores.setAtributo1("10002607");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ERROR EN RFC");
		this.pojoConValores.setValor("ER");
		this.pojoConValores.setAtributo1("10002608");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("MULTAS SAT");
		this.pojoConValores.setValor("MS");
		this.pojoConValores.setAtributo1("10002608");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("NO INDISPENSABLE P/ ACTIVIDAD");
		this.pojoConValores.setValor("NI");
		this.pojoConValores.setAtributo1("10002608");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SIN REQUISITOS FISCALES");
		this.pojoConValores.setValor("SR");
		this.pojoConValores.setAtributo1("10002608");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SIN COMPROBANTE FISCAL");
		this.pojoConValores.setValor("SC");
		this.pojoConValores.setAtributo1("10002608");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("DIFERENCIA EN FACTURA");
		this.pojoConValores.setValor("DF");
		this.pojoConValores.setAtributo1("10002608");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SEPTIMO DIA");
		this.pojoConValores.setValor("SD");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("BONO DE PUNTUALIDAD");
		this.pojoConValores.setValor("BP");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("BONO DE ASISTENCIA");
		this.pojoConValores.setValor("BA");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("HORAS EXTRAS");
		this.pojoConValores.setValor("HE");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PRIMA DOMINICAL");
		this.pojoConValores.setValor("PD");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("DIA DE DESCANSO/FESTIVO");
		this.pojoConValores.setValor("DD");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("DESPENSA");
		this.pojoConValores.setValor("DE");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PRIMA DE ANTIGUEDAD");
		this.pojoConValores.setValor("PA");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("INDEMINIZACIONES");
		this.pojoConValores.setValor("IN");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GRATIFICACIONES");
		this.pojoConValores.setValor("GR");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("COMISIONES");
		this.pojoConValores.setValor("CO");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PREVISION SOCIAL");
		this.pojoConValores.setValor("PS");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("VACACIONES");
		this.pojoConValores.setValor("VA");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PRIMA VACACIONAL");
		this.pojoConValores.setValor("PV");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("AGUINALDO");
		this.pojoConValores.setValor("AG");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SERVICIO MEDICO");
		this.pojoConValores.setValor("SM");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("AYUDA PARA GASTOS FUNERARIOS");
		this.pojoConValores.setValor("AGF");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("APORTACIONES PARA EL PLAN DE JUBILACION");
		this.pojoConValores.setValor("APJ");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ALIMENTO PARA EL PERSONAL");
		this.pojoConValores.setValor("AP");
		this.pojoConValores.setAtributo1("10002609");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SEPTIMO DIA");
		this.pojoConValores.setValor("SD");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("BONO DE PUNTUALIDAD");
		this.pojoConValores.setValor("BP");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("BONO DE ASISTENCIA");
		this.pojoConValores.setValor("BA");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("HORAS EXTRAS");
		this.pojoConValores.setValor("HE");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PRIMA DOMINICAL");
		this.pojoConValores.setValor("PD");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("DIA DE DESCANSO/FESTIVO");
		this.pojoConValores.setValor("DD");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("DESPENSA");
		this.pojoConValores.setValor("DE");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PRIMA DE ANTIGUEDAD");
		this.pojoConValores.setValor("PA");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("INDEMINIZACIONES");
		this.pojoConValores.setValor("IN");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GRATIFICACIONES");
		this.pojoConValores.setValor("GR");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("COMISIONES");
		this.pojoConValores.setValor("CO");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PREVISION SOCIAL");
		this.pojoConValores.setValor("PS");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("VACACIONES");
		this.pojoConValores.setValor("VA");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("PRIMA VACACIONAL");
		this.pojoConValores.setValor("PV");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("AGUINALDO");
		this.pojoConValores.setValor("AG");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("SERVICIO MEDICO");
		this.pojoConValores.setValor("SM");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("AYUDA PARA GASTOS FUNERARIOS");
		this.pojoConValores.setValor("AGF");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("APORTACIONES PARA EL PLAN DE JUBILACION");
		this.pojoConValores.setValor("APJ");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("ALIMENTO PARA EL PERSONAL");
		this.pojoConValores.setValor("AP");
		this.pojoConValores.setAtributo1("10002610");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("VARIOS SUELDOS Y SALARIOS");
		this.pojoConValores.setValor("RI");
		this.pojoConValores.setAtributo1("10002611");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GERENCIA SJC");
		this.pojoConValores.setValor("TT");
		this.pojoConValores.setAtributo1("10002612");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GERENCIA LPZ");
		this.pojoConValores.setValor("LA");
		this.pojoConValores.setAtributo1("10002613");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);

		this.pojoConValores.setDescripcion("GERENCIA LORETO");
		this.pojoConValores.setValor("IO");
		this.pojoConValores.setAtributo1("10002614");
		this.pojoConValores.setId(0);
		respuesta = this.ifzConVal.salvar(pojoConValores);
		pojoConValores = (ConValores) respuesta.getBody().getValor("pojoValores");
		this.listValores.add(this.pojoConValores);
    }
    
    public void eliminar(){
    	try {
    		this.resOperacion = "";
    		Respuesta respuesta = this.ifzConVal.delete(this.pojoConValores);
    		if(respuesta.getErrores().getCodigoError() == 0L)
    			this.listValores.remove(this.pojoConValores);
    		else
    			this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error en metodo eliminar", e);
			this.resOperacion = this.problemInesp;
		}
    }
    
    @SuppressWarnings("unchecked")
	private boolean salvaAtributos() {
    	ReversaUtil objValor = null;
		ReversaUtil objGrp = null;
    	SimpleDateFormat dateFormat = null;
    	Object tmp = null;
    	Object tipo = null;
    	Object format = null;
    	Object ejemplo = null;
    	List<String> param = new ArrayList<String>();
    	Pattern p = null;
    	Matcher m = null;
    	boolean resultado = true;
    	Atributo att = null;
		this.listErrores.clear();
		
		try {
			objValor = new ReversaUtil<ConValores, Object>(this.pojoConValores);
			objGrp = new ReversaUtil<ConGrupoValores, Object>(this.pojoGpoVal);
		} catch(Exception e) {
			
		}
		
		for(int i = 1 ; i < 15 ; i++){
			tmp = this.miMapa.get("atributo" + i);
			tipo = objGrp.getPropiedad("getAtributo" + i + "Tipo");
			
			format = objGrp.getPropiedad("getAtributo" + i + "Formato");
			if("date".equals(tipo)){
				if(format != null && !"".equals(format.toString()))
					dateFormat = new SimpleDateFormat(format.toString());
				else
					dateFormat = new SimpleDateFormat("dd/MM/yyy");
				
				if( tmp!=null )
					tmp = dateFormat.format((Date)tmp);
			}else if("check".equals(tipo) && tmp !=null)
				tmp = Boolean.valueOf(tmp.toString()) ? "S" : "F";
			else if("input".equals(tipo) && tmp !=null && format != null && !"".equals(format) && objGrp.getPropiedad("getAtributo" + i + "Etiqueta") != null){
				p = Pattern.compile(format.toString());
				m = p.matcher(tmp.toString());
				ejemplo = objGrp.getPropiedad("getAtributo" + i + "FormatoEjemplo");
				if(!m.find()){
					att = new Atributo();
					att.setAtributo1(objGrp.getPropiedad("getAtributo" + i + "Etiqueta").toString());
					att.setAtributo2("Formato invalido");
					att.setAtributo3(ejemplo);
					this.listErrores.add(att);
					if(resultado){
						resultado = false;
						this.resOperacion = "formato";
					}
				}
			}
			
			param.clear();
			param.add(tmp != null ? tmp.toString() : "");
			objValor.ejecutaMetodoVoid("setAtributo"+i, param);
    	}
		
		return resultado;
    }
    
	@SuppressWarnings("null")
	public void cargaAtributos(){
		HtmlPanelGroup htmPn2 = null;
    	HtmlInputText htmInput = null;
		HtmlCalendar htmCal = null;
		HtmlOutputText htmOutput = null;
		HtmlSelectOneMenu htmCombo = null;
		HtmlSelectBooleanCheckbox  htmCheck = null;
		HtmlOutputLabel htmLabel = null;
		UISelectItem item = null;
		
		ValueExpression valExpression = null;
		
		SimpleDateFormat dateFormat = null;
		
		String tmpEtiqueta = null;
		String tmpTipo = null;
		String tmpSrc = null;
		String tmpFormato = null;
		//String tmpTamaño = null;
		String tmpDato = null;
		
		Object tmp = null;
		
    	ReversaUtil objGrp;
    	ReversaUtil objValor;
    	
    	try {
    		this.miMapa.clear();
    		this.pnlComponentes.getChildren().clear();
    		this.pnlComponentes.getAttributes().put("columns", 2);
    		pnlComponentes.getAttributes().put("columns", 2);
    		pnlComponentes.getAttributes().put("columnClasses", "colIZqConVal, colDerConVal");
    		objGrp = new ReversaUtil<ConGrupoValores, Object>(this.pojoGpoVal);
    		objValor = new ReversaUtil<ConValores, Object>(this.pojoConValores);
    		
    		Application app = FacesContext.getCurrentInstance().getApplication();
    		this.expressionFactory = app.getExpressionFactory();
    		
    		for(int i = 1 ; i < 21 ; i++){
    			try {
	    			tmp = objGrp.getPropiedad("getAtributo" + i + "Etiqueta");
	    			tmpEtiqueta = tmp != null ? tmp.toString() : "";
	    			tmp = objGrp.getPropiedad("getAtributo" + i + "Tipo");
	    			tmpTipo = tmp != null ? tmp.toString() : "input";
	    			tmp = objGrp.getPropiedad("getAtributo" + i + "SrcDato");
	    			tmpSrc = tmp != null ? tmp.toString() : "";
	    			tmp = objGrp.getPropiedad("getAtributo" + i + "Formato");
	    			tmpFormato = tmp != null ? tmp.toString() : "";
	    			//tmp = objGrp.getPropiedad("getAtributo" + i + "Tamano");
	    			//tmpTamaño = tmp != null ? tmp.toString() : "";
	    			tmp = objValor.getPropiedad("getAtributo" + i);
	    			tmpDato = tmp != null ? tmp.toString() : "";
	    			
	    			if("".equals(tmpEtiqueta) )
	    				continue;
	    			
	    			if("date".equals(tmpTipo)){
	    				//defino su label
	    				htmOutput = new HtmlOutputText();
	    				htmOutput.setId("outPut" + i);
	        			htmOutput.setValue(tmpEtiqueta);
	        			htmOutput.getAttributes().put("styleClass", "Titulo");
	        			pnlComponentes.getChildren().add(htmOutput);
	    				
	    				htmCal = new HtmlCalendar();
	    				htmCal.setId("datCal" + i);
	    				if(!"".equals(tmpFormato)){
	    					htmCal.setDatePattern(tmpFormato);
	    					dateFormat = new SimpleDateFormat(tmpFormato);
	    				}else
	    					dateFormat = new SimpleDateFormat("dd/MM/yyy");
	
	    				miMapa.put("atributo" + i, "".equals(tmpDato) ? null : dateFormat.parseObject(tmpDato));
	    				
	    				valExpression = expressionFactory.createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{valoresxGrupoAction.miMapa['"+"atributo" + i + "']}", Date.class);
	    				htmCal.setValue("".equals(tmpDato) ? null : dateFormat.parseObject(tmpDato));
	    				htmCal.setValueExpression("value", valExpression);
	    				pnlComponentes.getChildren().add(htmCal);
	    			}else if("combo".equals(tmpTipo)){
	    				List<ConValores> listItems = null;
	    				//defino su label
	    				htmOutput = new HtmlOutputText();
	    				htmOutput.setId("outPut" + i);
	        			htmOutput.setValue(tmpEtiqueta);
	        			htmOutput.getAttributes().put("styleClass", "Titulo");
	        			pnlComponentes.getChildren().add(htmOutput);
	        			
	    				if(!"".equals(tmpSrc)){
	    					htmCombo = new HtmlSelectOneMenu();
	    					htmCombo.setId("cmb" + i);
	    					miMapa.put("atributo" + i, tmpDato);
	    					if(tmpSrc.contains(",")){//si fue un listado separado por comas
	    						for(String s : tmpSrc.split(",")){
	    							item = new UISelectItem();
	        					    item.setItemLabel(s);
	        					    item.setItemValue(s);
	        					    htmCombo.getChildren().add(item);
	    						}
	    					}else{//llama un metodo especifico del bean para obtener los resultados
	    						listItems = ifzConVal.findByGrupoNombre(tmpSrc);
	    						for(ConValores con : listItems) {
	    						    item = new UISelectItem();
	    						    if (! con.getDescripcion().equals(con.getValor()))
	    						    	item.setItemLabel(con.getDescripcion() + " (" + con.getValor() + ")");
	    						    else
	    						    	item.setItemLabel(con.getValor());
	    						    item.setItemDescription(con.getDescripcion());
	    						    item.setItemValue(con.getId());
	    						    htmCombo.getChildren().add(item);
	    						}
	    					}
	    					valExpression = expressionFactory.createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{valoresxGrupoAction.miMapa['"+"atributo" + i + "']}", String.class);
	    					htmCombo.setValue(tmpDato);
	    					htmCombo.setValueExpression("value", valExpression);
	        				pnlComponentes.getChildren().add(htmCombo);
	    				}else{
	    					htmOutput = new HtmlOutputText();
	    					htmOutput.setId("outPut" + i);
	    	    			htmOutput.setValue("Sin Origen de datos");
	    	    			htmOutput.getAttributes().put("styleClass", "Titulo");
	    	    			pnlComponentes.getChildren().add(htmOutput);
	    				}
	    			}else if("comboData".equals(tmpTipo)){
						@SuppressWarnings("unused")
						List<ConValores> listItems = null;
	    				//defino su label
	    				htmOutput = new HtmlOutputText();
	    				htmOutput.setId("outPut" + i);
	        			htmOutput.setValue(tmpEtiqueta);
	        			htmOutput.getAttributes().put("styleClass", "Titulo");
	        			pnlComponentes.getChildren().add(htmOutput);
	        			
	    				if(!"".equals(tmpSrc)){
	    					htmCombo = new HtmlSelectOneMenu();
	    					htmCombo.setId("cmb" + i);
	    					miMapa.put("atributo" + i, tmpDato); 
	    					List<UISelectItem> lista = null;
    						for(UISelectItem r : lista) { 
    						    htmCombo.getChildren().add(r);
    						}
	    					valExpression = expressionFactory.createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{valoresxGrupoAction.miMapa['"+"atributo" + i + "']}", String.class);
	    					htmCombo.setValue(tmpDato);
	    					htmCombo.setValueExpression("value", valExpression);
	        				pnlComponentes.getChildren().add(htmCombo);
	    				}else{
	    					htmOutput = new HtmlOutputText();
	    					htmOutput.setId("outPut" + i);
	    	    			htmOutput.setValue("Sin Origen de datos");
	    	    			htmOutput.getAttributes().put("styleClass", "Titulo");
	    	    			pnlComponentes.getChildren().add(htmOutput);
	    				}
	    			} else if("input".equals(tmpTipo)){
	    				//defino su label
	    				htmOutput = new HtmlOutputText();
	    				htmOutput.setId("outPut" + i);
	        			htmOutput.setValue(tmpEtiqueta);
	        			htmOutput.getAttributes().put("styleClass", "Titulo");
	        			pnlComponentes.getChildren().add(htmOutput);
	    				
	        			htmInput = new HtmlInputText();
	
	    				miMapa.put("atributo" + i, tmpDato);
	    				
	    				valExpression = expressionFactory.createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{valoresxGrupoAction.miMapa['"+"atributo" + i + "']}", String.class);
	    				htmInput.setValue(tmpDato);
	    				htmInput.setId("inPut" + i);
	    				htmInput.setValueExpression("value", valExpression);
	    				htmInput.getChildren().add(htmInput);
	    				pnlComponentes.getChildren().add(htmInput);
	    			}else if("check".equals(tmpTipo)){
	    				//defino su label
	    				htmOutput = new HtmlOutputText();
	    				htmOutput.setId("outPut" + i);
	        			htmOutput.setValue("");
	        			pnlComponentes.getChildren().add(htmOutput);
	        			
	        			htmPn2 = new HtmlPanelGroup();
	        			htmPn2.setId("pnlCbx" + i);
	        			
	        			htmCheck = new HtmlSelectBooleanCheckbox();
	        			htmCheck.setId("cbxAtributo" + i);
	        			
	        			htmLabel = new HtmlOutputLabel();
	        			htmLabel.setId("lble" + i);
	        			htmLabel.setFor("cbxAtributo" + i);
	        			htmLabel.setValue(tmpEtiqueta);
	        			htmLabel.getAttributes().put("styleClass", "Titulo");
	        			
	        			
	    				miMapa.put("atributo" + i, tmpDato);
	    				
	    				valExpression = expressionFactory.createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{valoresxGrupoAction.miMapa['"+"atributo" + i + "']}", Boolean.class);
	    				htmCheck.setValue("S".equals(tmpDato));
	    				htmCheck.setValueExpression("value", valExpression);
	    				htmPn2.getChildren().add(htmCheck);
	    				htmPn2.getChildren().add(htmLabel);
	    				
	    				pnlComponentes.getChildren().add(htmPn2);
	    			}
    			} catch (Exception e) {
					log.info("no fue posible cargar el atributo " + i, e);
				}
    		}
		} catch (Exception e) {
			log.error("Error al carga los atributos", e);
		}
    }
    
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setSuggGrupo(String suggGrupo) {
		this.suggGrupo = suggGrupo;
	}

	public String getSuggGrupo() {
		return suggGrupo;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public void setListValores(List<ConValores> listValores) {
		this.listValores = listValores;
	}

	public List<ConValores> getListValores() {
		return listValores;
	}

	public void setPojoConValores(ConValores pojoConValores) {
		this.pojoConValores = pojoConValores;
		this.suggGrupo = this.pojoConValores.getGrupoValorId().getId() + " - " + this.pojoConValores.getGrupoValorId().getNombre();
	}

	public ConValores getPojoConValores() {
		return pojoConValores;
	}
	
	public void setNuevoReg(String nuevo) { /* nanai */}
	
	public String getNuevoReg() {
		return this.pojoConValores.getId() > 0 ? "Nuevo Registro" : this.pojoConValores.getId() + " - " + this.pojoConValores.getValor();
	}

	public void setClave(String clave) { /* nanai */ }

	public String getClave() {
		return  (this.pojoConValores.getId() > 0 ? Objects.toString(this.pojoConValores.getId()) : "");
	}

	public void setListAnios(List<String> listAnios) {
		this.listAnios = listAnios;
	}

	public List<String> getListAnios() {
		return listAnios;
	}

	public void setSuggGrupoBusqueda(String suggGrupoBusqueda) {
		this.suggGrupoBusqueda = suggGrupoBusqueda;
	}

	public String getSuggGrupoBusqueda() {
		return suggGrupoBusqueda;
	}

	public void setPnlComponentes(HtmlPanelGrid pnlComponentes) {
		this.pnlComponentes = pnlComponentes;
	}

	public HtmlPanelGrid getPnlComponentes() {
		return pnlComponentes;
	}

	public void setMiMapa(MiMapa miMapa) {
		this.miMapa = miMapa;
	}

	public MiMapa getMiMapa() {
		return miMapa;
	}

	public void setListErrores(List<Atributo> listErrores) {
		this.listErrores = listErrores;
	}

	public List<Atributo> getListErrores() {
		return listErrores;
	}

	public ConGrupoValores getPojoGpoVal() {
		return pojoGpoVal;
	}

	public void setPojoGpoVal(ConGrupoValores pojoGpoVal) {
		this.pojoGpoVal = pojoGpoVal;
	}
}
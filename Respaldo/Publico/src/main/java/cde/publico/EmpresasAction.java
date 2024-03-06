package cde.publico;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;

import net.giro.clientes.logica.ClientesRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.EmpresaCertificado;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.beans.MonedaTYGExt;
import net.giro.plataforma.logica.EmpresaCertificadoRem;
import net.giro.plataforma.logica.EmpresasRem;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.publico.respuesta.Respuesta;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.ssl.Base64;
import org.apache.log4j.Logger;
//import org.apache.commons.codec.binary.Base64;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;


@KeepAlive
public class EmpresasAction {
	Logger log = Logger.getLogger(EmpresasAction.class);
	
	private Context ctx;
	private LoginManager loginManager;
	
	private String resOperacion;
	private String problemInesp;
	
	long numPagina;
	
	private List<Empresa> listEmpresas;
	private List<MonedaTYGExt> listMonedas;
	private List<ConValores> listCatDomicilios1;
	private List<ConValores> listCatDomicilios2;
	private List<SelectItem> listTmpCatDomicilios1;
	private List<SelectItem> listTmpCatDomicilios2;
	private List<SelectItem> listTmpMonedas;
	
	private Empresa pojoEmpresa;
	
	private String tipoBusqueda;
	private String valorBusqueda;
	
	private EmpresasRem ifzEmpresas;
	
	// Domicilio empresa
	private ClientesRem ifzClientes;
	private List<Estado> listEstados;
	private List<Municipio> listMunicipios;
	private List<Localidad> listLocalidades;
	private List<Colonia> listColonias;
	private List<SelectItem> listEstadosItems;
	private Estado pojoEstado;
	private Municipio pojoMunicipio;
	private Localidad pojoLocalidad;
	private Colonia pojoColonia;
	private String busquedaMunicipio;
	private String busquedaLocalidad;
	private String busquedaColonia;
	private int numPaginaMunicipio;
	private int numPaginaLocalidad; 
	private int numPaginaColonia;
	
	
	// Empresa Certificado
	private EmpresaCertificadoRem ifzEmpCertificado;
	private EmpresaCertificado pojoEmpCertificado;
	private String certificadoFilename;
	private String llaveFilename;
	private byte[] fileSrc; 
	private String fileName;
	private String fileTarget;
	private String certificado64;
	private String llave64;
	
	/*
	public void inicializaEstadoDom(){
		try {
			pojoLocalidad = null;
			pojoDomicilioExt.setColonia(null);
			
			if(listLocalidades != null)
				listLocalidades.clear();
			
			if(listColonias != null)
				listColonias.clear();
		} catch (Exception e) {
			log.error("Error en metodo inicializaEstadoDom", e);
		}
	}
	
	
	
	public List<SelectItem> getListEstado() {
		return listEstado;
	}
		
	public void setListEstado(List<SelectItem> listEstado) {
		this.listEstado = listEstado;
	}*/
	
	
	public EmpresasAction() throws Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		ctx = loginManager.getCtx();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		
		this.ifzEmpresas = (EmpresasRem) this.ctx.lookup("ejb:/Logica_Publico//EmpresasFac!net.giro.plataforma.logica.EmpresasRem");
		this.ifzEmpresas.setInfoSesion(this.loginManager.getInfoSesion());
		
		this.ifzEmpCertificado = (EmpresaCertificadoRem) this.ctx.lookup("ejb:/Logica_Publico//EmpresaCertificadoFac!net.giro.plataforma.logica.EmpresaCertificadoRem");
		this.ifzEmpCertificado.setInfoSesion(this.loginManager.getInfoSesion());
		
		this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
		this.ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
	
		numPagina = 1;
		tipoBusqueda = "empresa";
		
		listEmpresas = new ArrayList<Empresa>();
		listMonedas = new ArrayList<MonedaTYGExt>();
		
		pojoEmpresa = new Empresa();
		
		cargaEstados();
		cargaTiposDomicilio();
		cargaMonedas();
	}
	
	
	@SuppressWarnings("unchecked")
	public void buscar() {
		try{
			this.resOperacion = "";
			
			ifzEmpresas.orderBy("empresa");
			Respuesta respuesta = ifzEmpresas.buscarEmpresas(tipoBusqueda, valorBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0L){
				listEmpresas = (List<Empresa>) respuesta.getBody().getValor("listEmpresas");
			} else {
				this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			log.error("Error al buscar", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaMonedas(){
		try{
			if(listTmpMonedas == null)
				listTmpMonedas = new ArrayList<SelectItem>();
			else
				listTmpMonedas.clear();
			
			Respuesta respuesta = ifzEmpresas.cargarMonedas();
			if(respuesta.getErrores().getCodigoError() == 0L){
				listMonedas = (List<MonedaTYGExt>) respuesta.getBody().getValor("listMonedas");
				for(MonedaTYGExt pojoMoneda : listMonedas){
					SelectItem item = new SelectItem(pojoMoneda.getId(), pojoMoneda.getNombre());
					listTmpMonedas.add(item);
				}
			} else {
				this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al cargar monedas");
		}
	}
	
	public void guardar(){
		try{
			this.resOperacion = "";
			
			if (this.pojoColonia != null)
				this.pojoEmpresa.setColonia(this.pojoColonia);
				
			this.pojoEmpresa.setRfc(this.pojoEmpresa.getRfc().toUpperCase());
			Respuesta respuesta = ifzEmpresas.guardarEmpresa(pojoEmpresa);
			if(respuesta.getErrores().getCodigoError() == 0L){
				long id = pojoEmpresa.getId();
				pojoEmpresa = (Empresa) respuesta.getBody().getValor("pojoEmpresa");
				
				if(id == 0L)
					listEmpresas.add(pojoEmpresa);
				else {
					List<Empresa> listEmpresasAux = new ArrayList<Empresa>();
					listEmpresasAux.add(pojoEmpresa);
					
					for(Empresa pojoEmpresaAux : listEmpresas){
						if(pojoEmpresaAux.getId() != id)
							listEmpresasAux.add(pojoEmpresaAux);
					}
					
					listEmpresas = listEmpresasAux;
				}
			} else {
				this.resOperacion = respuesta.getErrores().getDescError();
			}
		} catch (Exception e){
			log.error("Error al guardar", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void eliminar(){
		
	}
	
	public void editar(){
		inicializaDomicilio();
	}
	
	public void nuevo(){
		try{
			pojoEmpresa = new Empresa();
			
			inicializaDomicilio();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al cargar nueva empresa");
		}
	}
	
	// Empresa Certificado
	// ------------------------------------------------------------------------------------
	
	public void cargarDatosFacturacion() {
		try {
			this.resOperacion = "";
			
			if (this.pojoEmpresa != null) {
				this.pojoEmpCertificado = new EmpresaCertificado();
				List<EmpresaCertificado> lista = this.ifzEmpCertificado.findByProperty("idEmpresa.id", this.pojoEmpresa.getId(), 0);
				if (lista != null & ! lista.isEmpty()) {
					this.pojoEmpCertificado = lista.get(0);
					this.certificadoFilename = this.pojoEmpCertificado.getCertificado();
					this.llaveFilename = this.pojoEmpCertificado.getLlave();
					//this.pojoEmpCertificado.setPalabra(new String(Base64.decodeBase64(this.pojoEmpCertificado.getPalabra().getBytes())));
					//this.pojoEmpCertificado.setUsuarioTimbre(new String(Base64.decodeBase64(this.pojoEmpCertificado.getUsuarioTimbre().getBytes())));
					//this.pojoEmpCertificado.setClaveTimbre(new String(Base64.decodeBase64(this.pojoEmpCertificado.getClaveTimbre().getBytes())));
				}
			}
		} catch (Exception e) {
			this.resOperacion = "ERROR";
			log.error("Error en EmpresasAction.cargarDatosFacturacion", e);
		}
	}
	
	public void guardarDatosFacturacion() {
		try {
			this.resOperacion = "";
			
			if (this.pojoEmpCertificado != null) {
				// Asignamos la empresa
				this.pojoEmpCertificado.setIdEmpresa(this.pojoEmpresa);
				this.pojoEmpCertificado.setModificadoPor((int) this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoEmpCertificado.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Carga de certificados
				Respuesta resCarga = this.ifzEmpCertificado.cargaCertificado(
						this.pojoEmpCertificado.getCertificado(), 
						this.pojoEmpCertificado.getPalabra(), 
						this.certificado64, 
						this.llave64, 
						this.pojoEmpCertificado.getUsuarioTimbre(), 
						this.pojoEmpCertificado.getClaveTimbre()
				);
				
				if (resCarga.getErrores().getCodigoError() == 0L) {
					this.pojoEmpCertificado.setNoCertificado((String) resCarga.getBody().getValor("noCertificado"));
					this.pojoEmpCertificado.setCertificadoData((byte[]) resCarga.getBody().getValor("certificadoData"));
					this.pojoEmpCertificado.setCertificadoDataPem((byte[]) resCarga.getBody().getValor("certificadoDataPem"));
					this.pojoEmpCertificado.setLlaveData((byte[]) resCarga.getBody().getValor("llaveData"));
					this.pojoEmpCertificado.setLlaveDataPem((byte[]) resCarga.getBody().getValor("llaveDataPem"));
					this.pojoEmpCertificado.setLlaveDataEnc((byte[]) resCarga.getBody().getValor("llaveDataEnc"));
				} else {
					this.resOperacion = "ERROR_cargaCertificado";
					return;
				}
				
				// Convertimos a Base64 las claves
				//this.pojoEmpCertificado.setPalabra(new String(Base64.encodeBase64(this.pojoEmpCertificado.getPalabra().getBytes())));
				//this.pojoEmpCertificado.setUsuarioTimbre(new String(Base64.encodeBase64(this.pojoEmpCertificado.getUsuarioTimbre().getBytes())));
				//this.pojoEmpCertificado.setClaveTimbre(new String(Base64.encodeBase64(this.pojoEmpCertificado.getClaveTimbre().getBytes())));
				
				// Guardamos en la BD segun corresponda
				if (this.pojoEmpCertificado.getId() == null || this.pojoEmpCertificado.getId() <= 0L) {
					this.pojoEmpCertificado.setCreadoPor((int) this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
					this.pojoEmpCertificado.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoEmpCertificado.setId(this.ifzEmpCertificado.save(this.pojoEmpCertificado));
				} else {
					// Actualizamos en la BD
					this.ifzEmpCertificado.update(this.pojoEmpCertificado);
				}
			}
		} catch (Exception e) {
			this.resOperacion = "ERROR_cargaCertificado";
			log.error("Error en EmpresasAction.cargarDatosFacturacion", e);
		}
	}
	
	public void nuevoUploadFile() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
		String valTarget = params.get("fileTarget");
		
		if (valTarget == null || valTarget.isEmpty())
			valTarget = "";
		
		this.fileSrc = null;
		this.fileName = "";
		this.fileTarget = valTarget;
	}
	
	public void uploadListener(UploadEvent event) throws Exception {
		try {
			this.resOperacion = "";
			
			UploadItem item = event.getUploadItem();
			this.fileSrc = ((item.isTempFile()) ? Files.readAllBytes(item.getFile().toPath()) : item.getData());// event.getUploadItem().getData();
			this.fileName = event.getUploadItem().getFileName();
		} catch (Exception e) {
			this.resOperacion = "ERROR";
			log.error("Error en ObrasAction.uploadListener", e);
			throw e;
		}
	}
	
	public void analizarArchivo() throws Exception {
		try {
			this.resOperacion = "";
			
			if(this.fileSrc != null && this.fileSrc.length > 0) {
				if (this.pojoEmpCertificado != null) {
					if (this.fileTarget.equals("certificado")) {
						this.certificado64 = new String(Base64.encodeBase64(this.fileSrc)); 
						this.certificadoFilename = this.fileName;
						this.pojoEmpCertificado.setCertificado(this.fileName);
					} else if (this.fileTarget.equals("llave")) {
						this.llave64 = new String(Base64.encodeBase64(this.fileSrc));
						this.llaveFilename = this.fileName;
						this.pojoEmpCertificado.setLlave(this.fileName);
					}
				}
			}
			
			nuevoUploadFile();
		} catch (Exception e) {
			this.resOperacion = "ERROR_analizarArchivo";
			log.error("Error en analizarArchivo", e);
			throw e;
		}
	}
	
	public String getExtension() {
		if ("certificado".equals(this.fileTarget))
			return "cer";
		if ("llave".equals(this.fileTarget))
			return "key";
		return "";
	}
	
	public String getMensajeValidacion() {
		if ("certificado".equals(this.fileTarget))
			return "Solo son permitidos archivo *.cer";
		if ("llave".equals(this.fileTarget))
			return "Solo son permitidos archivo *.key";
		return "";
	}
	
	// DOMICILIO
	// ------------------------------------------------------------------------------------

    public String buscarMunicipios() throws Exception {
    	try {
    		this.resOperacion = "";
			this.listMunicipios = ifzClientes.buscarMunicipio(this.pojoEstado, this.busquedaMunicipio);

			this.resOperacion = "OK";
    	} catch (Exception e) {
    		this.resOperacion = "ERROR";
    		log.error("Error en buscarMunicipios", e);
    		throw e;
    	}
    	
    	return this.resOperacion;
    }
    
    public String buscarLocalidades() throws Exception {
    	try {
    		this.resOperacion = "";
			
			this.listLocalidades = this.ifzClientes.buscarLocalidad(this.pojoMunicipio, this.busquedaLocalidad);

			this.resOperacion = "OK";
    	} catch (Exception e) {
    		this.resOperacion = "ERROR";
    		log.error("Error en buscarLocalidades", e);
    		throw e;
    	}
    	
    	return this.resOperacion;
    }
    
    public String buscarColonias() throws Exception {
    	try {
			this.resOperacion = "";
			
			this.listColonias = this.ifzClientes.buscarColonia(this.pojoLocalidad, this.busquedaColonia);

			this.resOperacion = "OK";
    	} catch (Exception e) {
			this.resOperacion = "ERROR";
    		log.error("Error en buscarColonias", e);
    		throw e;
    	}
    	
    	return this.resOperacion;
    }
    
	public void inicializaDomicilio(){
    	this.pojoColonia = null;
		this.pojoLocalidad = null;
		this.pojoMunicipio = null;
		this.pojoEstado = null;
		
		cargaEstados();
		
		if(pojoEmpresa.getDomicilio1() == null)
			pojoEmpresa.setDomicilio1(new ConValores());
		if(pojoEmpresa.getDomicilio2() == null)
			pojoEmpresa.setDomicilio2(new ConValores());
		if(pojoEmpresa.getDomicilio3() == null)
			pojoEmpresa.setDomicilio3(new ConValores());
		if(pojoEmpresa.getDomicilio4() == null)
			pojoEmpresa.setDomicilio4(new ConValores());
		if(pojoEmpresa.getDomicilio5() == null)
			pojoEmpresa.setDomicilio5(new ConValores());
		
		if(this.pojoEmpresa.getColonia() != null) {
			if (this.pojoEmpresa.getColonia().getId() > 0) 
				this.pojoColonia = this.pojoEmpresa.getColonia();
			
			if (this.pojoColonia != null)
				this.pojoLocalidad = this.pojoColonia.getLocalidad();
			
			if(this.pojoLocalidad != null)
				this.pojoMunicipio = this.pojoLocalidad.getMunicipio();
			
			if (this.pojoMunicipio != null)
				this.pojoEstado = this.pojoMunicipio.getEstado();
		}
	}
	
	public void cargaTiposDomicilio(){
		HashMap<String, String> params = null;
		try {
			if(listCatDomicilios1 == null)
				listCatDomicilios1 = new ArrayList<ConValores>();
			else
				listCatDomicilios1.clear();
			
			if(listCatDomicilios2 == null)
				listCatDomicilios2 = new ArrayList<ConValores>();
			else
				listCatDomicilios2.clear();
			
			if(listTmpCatDomicilios1==null) 
				listTmpCatDomicilios1 = new ArrayList<SelectItem>();
			else
				listTmpCatDomicilios1.clear();
			if(listTmpCatDomicilios2==null) 
				listTmpCatDomicilios2 = new ArrayList<SelectItem>();
			else
				listTmpCatDomicilios2.clear();
			
			params = new HashMap<String, String>();
			params.put("atributo2", "1");
			cargaTipoDom(params, listTmpCatDomicilios1, listCatDomicilios1);
			params.clear();
			params.put("atributo2", "2");
			cargaTipoDom(params, listTmpCatDomicilios2, listCatDomicilios2);
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaTiposDomicilio", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargaTipoDom(HashMap<String, String> params, List<SelectItem> items, List<ConValores> listDom){
		try {
			Respuesta respuesta = ifzEmpresas.buscarDomicilios(params);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listDom = (List<ConValores>) respuesta.getBody().getValor("listValores");			
				for(ConValores cv : listDom)
					items.add(new SelectItem(cv.getId(), cv.getValor()));
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
			
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaTipoDom", e);
		}
		
	}
	
	public boolean cargaEstados() {
    	try {
			this.resOperacion = "";

    		if (this.listEstadosItems == null)
    			this.listEstadosItems = new ArrayList<SelectItem>();
    		this.listEstadosItems.clear();
    		
			this.listEstados = this.ifzClientes.buscarEstados();
			
			if (this.listEstados.isEmpty()) {
				this.resOperacion = "No se encontro ningun estado.";
				return false;
			}

			for(Estado e : this.listEstados){
				this.listEstadosItems.add(new SelectItem(e.getId(), e.getNombre()));
			}
		} catch (Exception e) {
			this.resOperacion = "ERROR";
			log.error("Error en cargaEstados", e);
			return false;
		}
    	
    	return true;
    }

	public void limpiaLocalidades(){
		if(listLocalidades != null)
			listLocalidades.clear();
		else
			listLocalidades = new ArrayList<Localidad>();
	}
			
	public void limpiaMunicipio(){
		if(listMunicipios != null)
			listMunicipios.clear();
		else
			listMunicipios = new ArrayList<Municipio>();
	}
			
	public void limpiaColonias(){
		if(listColonias != null)
			listColonias.clear();
		else
			listColonias = new ArrayList<Colonia>();
	}

	private Estado getEstadoById(Long id){
		for(Estado e : this.listEstados){
			if(e.getId() == id.longValue()) 
				return e;
		}
		
		return null;
	}
	
	public void cambioEstadoDom(){
		try {
			if(this.listMunicipios != null)
				this.listMunicipios.clear();
			
			if(this.listLocalidades != null)
				this.listLocalidades.clear();
			
			if(this.listColonias != null)
				this.listColonias.clear();
			
			this.pojoMunicipio = null;
			this.pojoLocalidad = null;
			this.pojoColonia = null;
		} catch (Exception e) {
			log.error("Error en metodo cambioEstadoDom", e);
		}
	}
	
	/*public net.giro.clientes.plataforma.InfoSesion convertToClientesInfoSesion(net.giro.plataforma.InfoSesion infoSesion) {
		net.giro.clientes.plataforma.InfoSesion result = new net.giro.clientes.plataforma.InfoSesion();
		
		try {
			result.setAcceso(infoSesion.getAcceso());
			result.setResponsabilidad(infoSesion.getResponsabilidad());
			result.setUltimaModificacion(infoSesion.getUltimaModificacion());
		} catch (Exception e) {
			throw e;
		}
		
		return result;
	}*/
	
	// ------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------
	
	public Long getIdEstadoDom() {
		return pojoEstado != null && pojoEstado.getId() > 0 ? pojoEstado.getId() : 0L;
	}
		
	public void setIdEstadoDom(Long idEstadoDom) {
		try {
			pojoEstado = getEstadoById(idEstadoDom);
		} catch (Exception e) {
			log.error("Error en metodo setIdEstadoDom",e);
		}
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

	public long getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(long numPagina) {
		this.numPagina = numPagina;
	}

	public List<Empresa> getListEmpresas() {
		return listEmpresas;
	}

	public void setListEmpresas(List<Empresa> listEmpresas) {
		this.listEmpresas = listEmpresas;
	}

	public Empresa getPojoEmpresa() {
		return pojoEmpresa;
	}

	public void setPojoEmpresa(Empresa pojoEmpresa) {
		this.pojoEmpresa = pojoEmpresa;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public List<SelectItem> getListTmpCatDomicilios1() {
		return listTmpCatDomicilios1;
	}

	public void setListTmpCatDomicilios1(List<SelectItem> listTmpCatDomicilios1) {
		this.listTmpCatDomicilios1 = listTmpCatDomicilios1;
	}

	public List<SelectItem> getListTmpCatDomicilios2() {
		return listTmpCatDomicilios2;
	}

	public void setListTmpCatDomicilios2(List<SelectItem> listTmpCatDomicilios2) {
		this.listTmpCatDomicilios2 = listTmpCatDomicilios2;
	}

	public List<SelectItem> getListTmpMonedas() {
		return listTmpMonedas;
	}

	public void setListTmpMonedas(List<SelectItem> listTmpMonedas) {
		this.listTmpMonedas = listTmpMonedas;
	}

	public EmpresaCertificado getPojoEmpCertificado() {
		return pojoEmpCertificado;
	}

	public void setPojoEmpCertificado(EmpresaCertificado pojoEmpCertificado) {
		this.pojoEmpCertificado = pojoEmpCertificado;
	}

	public String getCertificadoFilename() {
		return certificadoFilename;
	}

	public void setCertificadoFilename(String certificadoFilename) {
		this.certificadoFilename = certificadoFilename;
	}

	public String getLlaveFilename() {
		return llaveFilename;
	}

	public void setLlaveFilename(String llaveFilename) {
		this.llaveFilename = llaveFilename;
	}


	
	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}
	


	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}
	


	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}
	


	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}
	


	public List<Colonia> getListColonias() {
		return listColonias;
	}
	


	public void setListColonias(List<Colonia> listColonias) {
		this.listColonias = listColonias;
	}
	


	public List<SelectItem> getListEstadosItems() {
		return listEstadosItems;
	}
	


	public void setListEstadosItems(List<SelectItem> listEstadosItems) {
		this.listEstadosItems = listEstadosItems;
	}
	


	public Municipio getPojoMunicipio() {
		return pojoMunicipio;
	}
	


	public void setPojoMunicipio(Municipio pojoMunicipio) {
		this.pojoMunicipio = pojoMunicipio;
	}
	


	public Localidad getPojoLocalidad() {
		return pojoLocalidad;
	}
	


	public void setPojoLocalidad(Localidad pojoLocalidad) {
		this.pojoLocalidad = pojoLocalidad;
	}
	


	public Colonia getPojoColonia() {
		return pojoColonia;
	}
	


	public void setPojoColonia(Colonia pojoColonia) {
		this.pojoColonia = pojoColonia;
	}


	public String getBusquedaMunicipio() {
		return busquedaMunicipio;
	}


	public void setBusquedaMunicipio(String busquedaMunicipio) {
		this.busquedaMunicipio = busquedaMunicipio;
	}


	public String getBusquedaLocalidad() {
		return busquedaLocalidad;
	}


	public void setBusquedaLocalidad(String busquedaLocalidad) {
		this.busquedaLocalidad = busquedaLocalidad;
	}


	public String getBusquedaColonia() {
		return busquedaColonia;
	}


	public void setBusquedaColonia(String busquedaColonia) {
		this.busquedaColonia = busquedaColonia;
	}


	public int getNumPaginaMunicipio() {
		return numPaginaMunicipio;
	}


	public void setNumPaginaMunicipio(int numPaginaMunicipio) {
		this.numPaginaMunicipio = numPaginaMunicipio;
	}


	public int getNumPaginaLocalidad() {
		return numPaginaLocalidad;
	}


	public void setNumPaginaLocalidad(int numPaginaLocalidad) {
		this.numPaginaLocalidad = numPaginaLocalidad;
	}


	public int getNumPaginaColonia() {
		return numPaginaColonia;
	}


	public void setNumPaginaColonia(int numPaginaColonia) {
		this.numPaginaColonia = numPaginaColonia;
	}


	public Estado getPojoEstado() {
		return pojoEstado;
	}


	public void setPojoEstado(Estado pojoEstado) {
		this.pojoEstado = pojoEstado;
	}
}

package cde.publico.ne;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.beans.SubfamiliaImpuestos;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.logica.SubfamiliaImpuestosRem;

import org.apache.log4j.Logger;

public class SubfamiliaImpuestosAction implements Serializable {
	private static Logger log = Logger.getLogger(SubfamiliaImpuestosAction.class);
	private static final long serialVersionUID = 1L;
	private InitialContext ctx;

	private SubfamiliaImpuestosRem ifzSubfamiliaImpuestos;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;
	private String campoBusqueda;	
	private String valorBusqueda;
	private int numPagina;
	// control
	private List<SubfamiliaImpuestos> listSubfamiliasImpuestos;
	private SubfamiliaImpuestos pojoSubfamiliaImpuestos;
	private SubfamiliaImpuestos pojoSubfamiliaImpuestosBorrar;
	private ConGrupoValores gpoMaestros; 
	private ConGrupoValores gpoEspecialidades; 
	private ConGrupoValores gpoFamilias; 
	private ConGrupoValores gpoSubfamilias; 
	private ConGrupoValores gpoImpuestos; 
    private long usuarioId;
	//private boolean desglosar;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// Subfamilias
	private List<ConValores> listSubfamilias;
	private List<SelectItem> listSubfamiliasItems;
	private long idSubfamilia;
	private String tipoMaestro;
	// Impuestos
	private List<ConValores> listImpuestos;
	private List<SelectItem> listImpuestosItems;
	private List<SelectItem> listAplicaItems;
	private long idImpuesto;
	private double porcentajeImpuesto;
	private int aplicaEn;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;


	public SubfamiliaImpuestosAction() throws Exception{
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		LoginManager lm = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			lm = (LoginManager) valExp.getValue(fc.getELContext());
			
			this.listImpuestos = new ArrayList<ConValores>();
			
			this.ctx = new InitialContext();
			this.ifzSubfamiliaImpuestos = (SubfamiliaImpuestosRem) this.ctx.lookup("ejb:/Logica_Publico//SubfamiliaImpuestosFac!net.giro.plataforma.logica.SubfamiliaImpuestosRem");
			this.ifzGpoVal 				= (ConGrupoValoresRem) 	this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores 			= (ConValoresRem) 		this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("descSubfamilia", "Subfamilia"));
			this.tiposBusqueda.add(new SelectItem("descImpuesto", "Impuesto"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			this.listAplicaItems = new ArrayList<SelectItem>();
			this.listAplicaItems.add(new SelectItem(0, "IVA"));
			this.listAplicaItems.add(new SelectItem(1, "SubTotal"));
			
					
			this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId();
			
			Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;

			// Grupo de valores para MAESTROS de productos
			this.gpoMaestros = this.ifzGpoVal.findByName("SYS_CODE_NIVEL0");
			if (this.gpoMaestros == null || this.gpoMaestros.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_CODE_NIVEL0 (Maestros) en con_grupo_valores");
			
			// Grupo de valores para ESPECIALIDADES de productos
			this.gpoEspecialidades = this.ifzGpoVal.findByName("SYS_PRODUCTO_ESPECIALIDADES");
			if (this.gpoEspecialidades == null || this.gpoEspecialidades.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_PRODUCTO_ESPECIALIDADES en con_grupo_valores");

			// Grupo de valores para FAMILIAS de productos 
			this.gpoFamilias = this.ifzGpoVal.findByName("SYS_FAMILIA_PRODUCTO");
			if (this.gpoFamilias == null || this.gpoFamilias.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_FAMILIA_PRODUCTO en con_grupo_valores");

			// Grupo de valores para SUBFAMILIAS de productos 
			this.gpoSubfamilias = this.ifzGpoVal.findByName("SYS_PRODUCTO_SUBFAMILIA");
			if (this.gpoSubfamilias == null || this.gpoSubfamilias.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_PRODUCTO_SUBFAMILIA en con_grupo_valores");

			// Grupo de valores para SYS_IMPTOS (Impuestos) de gastos
			this.gpoImpuestos = this.ifzGpoVal.findByName("SYS_IMPTOS");
			if (this.gpoImpuestos == null || this.gpoImpuestos.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_IMPTOS (Impuestos) en con_grupo_valores");
			this.mensaje="";
			
			this.tipoMaestro = "2";
			if (this.paramsRequest.containsKey("MAESTRO"))
				this.tipoMaestro = this.paramsRequest.get("MAESTRO");
			nuevo();
			cargarSubfamilias();
			cargarImpuestos();
		} catch (Exception e) {
			log.error("Error en Publico.SubfamiliaImpuestosAction", e);
			this.ctx = null;
		}
	}

	
	public void buscar(){
		try {
			control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			
			this.listSubfamiliasImpuestos = this.ifzSubfamiliaImpuestos.findLikeProperty(this.campoBusqueda, this.valorBusqueda);
			if (this.listSubfamiliasImpuestos == null || this.listSubfamiliasImpuestos.isEmpty()) {
				log.info("ERROR 2 - Busqueda sin resultados");
				control("La búsqueda no regresó resultados");
			}
		} catch (Exception e) {
			log.error("Error en el metodo buscar.", e);
			control(true);
		}
	}
	
	public void nuevo() {
		try{
			control();
			this.pojoSubfamiliaImpuestos = new SubfamiliaImpuestos();
			this.idSubfamilia = 0L;
			this.idImpuesto = 0L;
			this.porcentajeImpuesto = 0;
			this.aplicaEn = -1;
			/*cargarSubfamilias();
			cargarImpuestos();*/
		} catch (Exception e) {
			log.error("Error en el metodo nuevo.", e);
			control(true);
		}
	}
	
	public void editar() {
		try{
			control();
			this.idSubfamilia = 0L;
			this.idImpuesto = 0L;
			this.porcentajeImpuesto = 0;
			this.aplicaEn = -1;
			/*cargarSubfamilias();
			cargarImpuestos();*/
			
			if (this.pojoSubfamiliaImpuestos != null && this.pojoSubfamiliaImpuestos.getId() != null && this.pojoSubfamiliaImpuestos.getId() > 0L) {
				this.idSubfamilia = this.pojoSubfamiliaImpuestos.getIdSubfamilia();
				this.idImpuesto = this.pojoSubfamiliaImpuestos.getIdImpuesto();
				this.porcentajeImpuesto = this.pojoSubfamiliaImpuestos.getPorcentaje().doubleValue();
				this.aplicaEn = this.pojoSubfamiliaImpuestos.getAplicaEn();
			}
		} catch (Exception e) {
			log.error("Error en el metodo editar.", e);
			control(true);
		}
	}
	
	public void guardar() {
		try{
			control();
			log.info("Guardando SubFamilia Impuesto");
			if (! validamosSubfamiliaImpuesto())
				return;

			log.info("Recuperando SubFamilia");
			for (ConValores sub : this.listSubfamilias) {
				if (this.idSubfamilia != sub.getId())
					continue;

				this.pojoSubfamiliaImpuestos.setIdSubfamilia(sub.getId());
				this.pojoSubfamiliaImpuestos.setDescSubfamilia(sub.getDescripcion());
				break;
			}

			log.info("Recuperando Impuesto");
			for (ConValores imp : this.listImpuestos) {
				if (this.idImpuesto != imp.getId())
					continue;

				this.pojoSubfamiliaImpuestos.setIdImpuesto(imp.getId());
				this.pojoSubfamiliaImpuestos.setDescImpuesto(imp.getDescripcion());
				break;
			}
			
			this.pojoSubfamiliaImpuestos.setPorcentaje(new BigDecimal(this.porcentajeImpuesto));
			this.pojoSubfamiliaImpuestos.setAplicaEn(this.aplicaEn);
			this.pojoSubfamiliaImpuestos.setValor(BigDecimal.ZERO);
			this.pojoSubfamiliaImpuestos.setModificadoPor(this.usuarioId);
			this.pojoSubfamiliaImpuestos.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.pojoSubfamiliaImpuestos.getId() == null || this.pojoSubfamiliaImpuestos.getId() <= 0L) {
				this.pojoSubfamiliaImpuestos.setCreadoPor(this.usuarioId);
				this.pojoSubfamiliaImpuestos.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD y añadimos al listado
				this.ifzSubfamiliaImpuestos.save(this.pojoSubfamiliaImpuestos);
				this.listSubfamiliasImpuestos.add(this.pojoSubfamiliaImpuestos);
			} else {
				// Actualizamos en la BD 
				this.ifzSubfamiliaImpuestos.update(this.pojoSubfamiliaImpuestos);
				
				for (SubfamiliaImpuestos var : this.listSubfamiliasImpuestos) {
					if (var.getId().equals(this.pojoSubfamiliaImpuestos.getId())) {
						var = this.pojoSubfamiliaImpuestos;
						break;
					}
				}
			}
			
			log.info("SubFamilia Impuesto guardado");
		} catch (Exception e) {
			log.error("error al guardar", e);
			control(true);
		}
	}

	public void eliminar() {
		try {
			control();
			if (this.pojoSubfamiliaImpuestos != null) {
				if (this.pojoSubfamiliaImpuestos.getId() != null && this.pojoSubfamiliaImpuestos.getId() > 0L)
					this.ifzSubfamiliaImpuestos.delete(this.pojoSubfamiliaImpuestos.getId());
				this.listSubfamiliasImpuestos.remove(this.pojoSubfamiliaImpuestos);
			}
		} catch (Exception e) {
			log.error("Error en el metodo eliminar.", e);
			control(true);
		}
	}

	private boolean validamosSubfamiliaImpuesto() {
		List<SubfamiliaImpuestos> listAux = new ArrayList<SubfamiliaImpuestos>();
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		if (this.idSubfamilia <= 0L) {
			log.info("VALIDACION - No selecciono una Subfamilia");
			control("Debe seleccionar una subfamilia");
			return false;
		}
		
		if (this.idImpuesto <= 0L) {
			log.info("VALIDACION - No selecciono un Impuesto");
			control("Debe seleccionar un Impuesto");
			return false;
		}
		
		try {
			log.info("VALIDACION - Comprobando Subfamilia-Impuesto ... ");
			params.put("idSubfamilia", this.idSubfamilia);
			params.put("idImpuesto", this.idImpuesto);
			listAux = this.ifzSubfamiliaImpuestos.findByProperties(params);
			if (listAux != null && ! listAux.isEmpty()) {
				if (this.pojoSubfamiliaImpuestos.getId() != null && this.pojoSubfamiliaImpuestos.getId() > 0L) {
					if (listAux.size() == 1 && listAux.get(0).getId().longValue() == this.pojoSubfamiliaImpuestos.getId().longValue()) {
						return true;
					}
				}
				
				log.info("VALIDACION - Ya existe el impuesto en la subfamilia seleccionada");
				control("Ya existe el impuesto en la subfamilia seleccionada");
				return false;
			}
		} catch (Exception e) {
			log.info("VALIDACION - Ya existe el impuesto en la subfamilia seleccionada", e);
			control("Ya existe el impuesto en la subfamilia seleccionada");
			return false;
		}
		
		return true;
	}
	
	public void cargarSubfamilias() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<ConValores> listMaestros = null;
		List<ConValores> listEspecialidades = null;
		List<ConValores> listFamilias = null;
		List<ConValores> listSub = null;
		List<ConValores> listAux = null;
		String idMaestro = "";
		
		try {
			// Inicializamos
			if (listMaestros == null)
				listMaestros = new ArrayList<ConValores>();
			listMaestros.clear();
			
			if (listEspecialidades == null)
				listEspecialidades = new ArrayList<ConValores>();
			listEspecialidades.clear();
			
			if (listFamilias == null)
				listFamilias = new ArrayList<ConValores>();
			listFamilias.clear();
			
			if (this.listSubfamilias == null)
				this.listSubfamilias = new ArrayList<ConValores>();
			this.listSubfamilias.clear();
			
			if (this.listSubfamiliasItems == null)
				this.listSubfamiliasItems = new ArrayList<SelectItem>();
			this.listSubfamiliasItems.clear();
			
			log.info("Cargando Maestros");
			listAux = this.ifzConValores.findAll(this.gpoMaestros);
			if (listAux == null || listAux.isEmpty()) {
				log.info("Lista de maestros vacio");
				return;
			}

			log.info("Obtengo ID de maestro");
			for (ConValores var : listAux) {
				if (! var.getValor().trim().equals(this.tipoMaestro)) 
					continue;
				idMaestro = String.valueOf(var.getId());
				break;
			}
			listAux.clear();

			log.info("Cargando Especialidades ... ");
			params.put("grupoValorId.id", this.gpoEspecialidades.getId());
			params.put("atributo1", idMaestro);
			listEspecialidades = ifzConValores.findByProperties(params, 0);
			if (listEspecialidades == null || listEspecialidades.isEmpty()) {
				log.info("Lista de Especialidades vacia");
				return;
			}
			
			for (ConValores var : listEspecialidades) {
				// Busco familias por especialidad 
				log.info("Busco familias de la especialidad: " + var.getDescripcion());
				params.put("grupoValorId.id", this.gpoFamilias.getId());
				params.put("atributo1", String.valueOf(var.getId()));
				listAux = this.ifzConValores.findByProperties(params, 0);
				if (listAux == null || listAux.isEmpty()) {
					log.info("Especialidad sin familia");
					continue;
				}
				
				for (ConValores fam : listAux) {
					// Busco subfamilias por familia 
					log.info("Busco subfamilias de la familia: " + fam.getDescripcion());
					params.put("grupoValorId.id", this.gpoSubfamilias.getId());
					params.put("atributo1", String.valueOf(fam.getId()));
					listSub = this.ifzConValores.findByProperties(params, 0);
					if (listSub == null || listSub.isEmpty()) {
						log.info("Familia sin subfamilias");
						continue;
					}
					
					this.listSubfamilias.addAll(listSub);
				}
			}
			
			Collections.sort(this.listSubfamilias, new Comparator<ConValores>() {
			    	@Override
			        public int compare(ConValores o1, ConValores o2) {
			    		return o1.getDescripcion().compareTo(o2.getDescripcion());
			        }
			});
			
			// Generamos la lista auxiliar de familias
			if (this.listSubfamilias != null && ! this.listSubfamilias.isEmpty()) {
				log.info("Generando lista de items (Subfamilias)");
				for (ConValores var : this.listSubfamilias) {
					if (var.getDescripcion() == null)
						continue;
					this.listSubfamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion()));
				}
			}
		} catch (Exception e) {
			log.error("Error en Publico.SubfamiliaImpuestosAction.cargarSubfamilias(). No se pudo cargar las Subfamilias", e);
		} finally {
			if (this.listSubfamiliasItems != null && ! this.listSubfamiliasItems.isEmpty()) 
				log.info(this.listSubfamiliasItems.size() + " items (Subfamilias) generados");
		}
	}
	
	public void cargarImpuestos() {
		try {
			if (this.listImpuestos == null)
				this.listImpuestos = new ArrayList<ConValores>();
			this.listImpuestos.clear();
			
			if (this.listImpuestosItems == null)
				this.listImpuestosItems = new ArrayList<SelectItem>();
			this.listImpuestosItems.clear();
			
			// Cargamos la lista de familias
			log.info("Cargando lista de familias");
			this.listImpuestos = this.ifzConValores.buscaValorGrupo("descripcion", "", this.gpoImpuestos);
			if (this.listImpuestos != null && ! this.listImpuestos.isEmpty()) {
				// Generamos la lista auxiliar de familias
				log.info("Generando lista de items (Impuestos)");
				for (ConValores var : this.listImpuestos) {
					if (var.getDescripcion() == null)
						continue;
					this.listImpuestosItems.add(new SelectItem(var.getId(), var.getDescripcion()));
				}
			}
		} catch (Exception e) {
			log.error("Error en Publico.SubfamiliaImpuestosAction.cargarImpuestos(). No se pudo cargar los impuestos", e);
		} finally {
			if (this.listImpuestosItems != null && ! this.listImpuestosItems.isEmpty()) 
				log.info(this.listImpuestosItems.size() + " items (UnidadesMedida) generados");
		}
	}
	
	public void recuperaPorcentajeImpuesto() {
		try {
			control();
			for (ConValores imp : this.listImpuestos) {
				if (this.idImpuesto != imp.getId())
					continue;
				this.porcentajeImpuesto = (new BigDecimal(imp.getAtributo1())).doubleValue();
				break;
			}
		} catch (Exception e) {
			log.info("ERROR INTERNO - No se pudo recuperar el Porcentaje del Impuesto seleccionado");
			control("Ocurrio un problema al recuperar el porcentaje del impuesto seleccionado");
			this.porcentajeImpuesto = 0;
		}
	}
	
	private void control() { 
		control(false, 0, "");
	}
	
	private void control(boolean value) {
		if (value)
			control(value, 1, "ERROR");
		else
			control(value, 0, null);
	}
	
	private void control(String value) { 
		if (value == null || "".equals(value.trim()))
			control(true, 1, "ERROR");
		else
			control(true, -1, value); 
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje;
	}

	// -----------------------------------------------------------------------------------------
	// PROPIEDADES
	// -----------------------------------------------------------------------------------------

	public String getEncabezado() {
		if (this.pojoSubfamiliaImpuestos != null && this.pojoSubfamiliaImpuestos.getId() != null && this.pojoSubfamiliaImpuestos.getId() > 0L)
			return this.pojoSubfamiliaImpuestos.getId() + " - " + this.pojoSubfamiliaImpuestos.getDescSubfamilia();
		return "Seleccione datos";
	}
	
	public void setEncabezado(String value) {}

	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public List<SubfamiliaImpuestos> getListSubfamiliasImpuestos() {
		return listSubfamiliasImpuestos;
	}

	public void setListSubfamiliasImpuestos(List<SubfamiliaImpuestos> listSubfamiliasImpuestos) {
		this.listSubfamiliasImpuestos = listSubfamiliasImpuestos;
	}

	public SubfamiliaImpuestos getPojoSubfamiliaImpuestos() {
		return pojoSubfamiliaImpuestos;
	}

	public void setPojoSubfamiliaImpuestos(SubfamiliaImpuestos pojoSubfamiliaImpuestos) {
		this.pojoSubfamiliaImpuestos = pojoSubfamiliaImpuestos;
	}

	public SubfamiliaImpuestos getPojoSubfamiliaImpuestosBorrar() {
		return pojoSubfamiliaImpuestosBorrar;
	}

	public void setPojoSubfamiliaImpuestosBorrar(SubfamiliaImpuestos pojoSubfamiliaImpuestosBorrar) {
		this.pojoSubfamiliaImpuestosBorrar = pojoSubfamiliaImpuestosBorrar;
	}

	public boolean isOperacion() {
		return operacionCancelada;
	}

	public void setOperacion(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<SelectItem> getListSubfamiliasItems() {
		return listSubfamiliasItems;
	}

	public void setListSubfamiliasItems(List<SelectItem> listSubfamiliasItems) {
		this.listSubfamiliasItems = listSubfamiliasItems;
	}

	public long getIdSubfamilia() {
		return idSubfamilia;
	}

	public void setIdSubfamilia(long idSubfamilia) {
		this.idSubfamilia = idSubfamilia;
	}

	public List<SelectItem> getListImpuestosItems() {
		return listImpuestosItems;
	}

	public void setListImpuestosItems(List<SelectItem> listImpuestosItems) {
		this.listImpuestosItems = listImpuestosItems;
	}

	public long getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(long idImpuesto) {
		this.idImpuesto = idImpuesto;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public List<SelectItem> getListAplicaItems() {
		return listAplicaItems;
	}

	public void setListAplicaItems(List<SelectItem> listAplicaItems) {
		this.listAplicaItems = listAplicaItems;
	}


	
	public double getPorcentajeImpuesto() {
		return porcentajeImpuesto;
	}
	


	public void setPorcentajeImpuesto(double porcentajeImpuesto) {
		this.porcentajeImpuesto = porcentajeImpuesto;
	}
	


	public int getAplicaEn() {
		return aplicaEn;
	}
	


	public void setAplicaEn(int aplicaEn) {
		this.aplicaEn = aplicaEn;
	}
}

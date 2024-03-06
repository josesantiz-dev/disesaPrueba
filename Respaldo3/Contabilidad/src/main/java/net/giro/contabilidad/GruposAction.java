package net.giro.contabilidad;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.EjecutaBean;
import net.giro.contabilidad.beans.Grupos;
import net.giro.contabilidad.beans.GruposCuentas;
import net.giro.contabilidad.beans.Llaves;
import net.giro.contabilidad.logica.AsignacionGruposRem;
import net.giro.contabilidad.logica.GruposCuentasRem;
import net.giro.contabilidad.logica.GruposRem;
import net.giro.contabilidad.logica.LlavesRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="gruposAction")
public class GruposAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(GruposAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	// Interfaces
	private GruposRem ifzGrupos;
	private AsignacionGruposRem ifzAsigGpo;
	private LlavesRem ifzLlaves;
	private GruposCuentasRem ifzGruposCuentas;
	// Listas
	private List<Grupos> listGrupos;
	private long idGrupo;
	private Grupos pojoGrupo;
	//private Grupos pojoGrupoBorrar;
	private List<Llaves> listLlaves;
	private List<Llaves> listGrupoLlaves;
	private Llaves pojoLlave;
	private Llaves pojoLlaveBorrar;
	private List<GruposCuentas> listLlavesValores;
	private GruposCuentas pojoLlaveValor;
	private GruposCuentas pojoLlaveValorBorrar;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Busqueda Llaves
	private List<SelectItem> tiposBusquedaLlaves;	
	private String campoBusquedaLlaves;
	private String valorBusquedaLlaves;
	private int numPaginaLlaves;
	// Busqueda Dinamica
	private List<SelectItem> listValoresDinamicos;
	private SelectItem pojoValorDinamico;
	private List<SelectItem> tiposBusquedaDinamica;	
	private String campoBusquedaDinamica;
	private String valorBusquedaDinamica;
	private int numPaginaDinamica;
	// Variables de operacion
    private long usuarioId;
    private String usuario;
    private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private long permiteBorrar;
	private boolean permitirLlavesRepetidas;
	private int numPaginaGrupoLlaves;
	private int numPaginaGrupoLlavesValores;
	private boolean permiteBorrarGrupo;
	// Llave
	private int llaveNumero;
	private String llaveTipo;
	private String llaveEjecucion;
	private String llaveCampoId;
	private String llaveCampoDescripcion;
	private HashMap<Integer, String> valorLlaveDescripcion;
	// Debug
	private HashMap<String, String> paramsRequest;
	private boolean isDebug;

	public GruposAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = this.paramsRequest.containsKey("DEBUG") ? true : false;
			
			this.ctx = new InitialContext();
			this.ifzGrupos = (GruposRem) this.ctx.lookup("ejb:/Logica_Contabilidad//GruposFac!net.giro.contabilidad.logica.GruposRem");
			this.ifzLlaves = (LlavesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//LlavesFac!net.giro.contabilidad.logica.LlavesRem");
			this.ifzAsigGpo = (AsignacionGruposRem) this.ctx.lookup("ejb:/Logica_Contabilidad//AsignacionGruposFac!net.giro.contabilidad.logica.AsignacionGruposRem");
			this.ifzGruposCuentas = (GruposCuentasRem) this.ctx.lookup("ejb:/Logica_Contabilidad//GruposCuentasFac!net.giro.contabilidad.logica.GruposCuentasRem");

			this.ifzGrupos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzLlaves.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAsigGpo.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGruposCuentas.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Listas
			this.listGrupos= new ArrayList<Grupos>();
			
			// POJO's
			this.pojoGrupo = new Grupos();
			this.pojoLlaveValor = new GruposCuentas();
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda Llaves
			this.tiposBusquedaLlaves = new ArrayList<SelectItem>();
			this.tiposBusquedaLlaves.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaLlaves.add(new SelectItem("id", "ID"));
			this.campoBusquedaLlaves = this.tiposBusquedaLlaves.get(0).getValue().toString();
			this.valorBusquedaLlaves = "";
			this.numPaginaLlaves = 1;
			
			// Busqueda Dinamica
			this.tiposBusquedaDinamica = new ArrayList<SelectItem>();
			//this.tiposBusquedaDinamica.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaDinamica.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaDinamica.add(new SelectItem("id", "ID"));
			this.campoBusquedaDinamica = this.tiposBusquedaDinamica.get(0).getValue().toString();
			this.valorBusquedaDinamica = "";
			this.numPaginaDinamica = 1;
			
			this.permitirLlavesRepetidas = false;
			this.numPaginaGrupoLlavesValores = 1;
		} catch (Exception e) {
			log.error("Error en constructor GruposAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();

			this.numPagina = 1;
			this.ifzGrupos.orderBy("descripcion");
			this.listGrupos = this.ifzGrupos.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 0);
			if (this.listGrupos.isEmpty()) {
				control(2, "Busqueda vacia");
				return;
			}

			if (validaRequest("REACOMODA")) {
				List<List<GruposCuentas>> valoresActualizables = new ArrayList<List<GruposCuentas>>();
				List<GruposCuentas> valoresActualizable = null;
				List<Grupos> gruposActualizables = new ArrayList<Grupos>();
				Grupos grupoActualizable = new Grupos();
				
				for (Grupos item : this.listGrupos) {
					log.info("Grupo " + (this.listGrupos.indexOf(item) + 1) + "/" + this.listGrupos.size());
					log.info("----------------------------------------------------------------------------");
					this.listLlavesValores = this.ifzGruposCuentas.findByProperty("idGrupo", item, 0);
					if (this.listLlavesValores != null && ! this.listLlavesValores.isEmpty()) {
						this.pojoGrupo = new Grupos();
						BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
						BeanUtils.copyProperties(this.pojoGrupo, item);
						
						log.info("---> Reacomodando valores ... ");
						reacomodaValoresLlaves();
						log.info("---> Reacomodando llaves ... ");
						reacomodaLlaves();
						
						for (GruposCuentas cuenta : this.listLlavesValores)
							cuenta.setIdGrupo(this.pojoGrupo);

						grupoActualizable = new Grupos();
						BeanUtils.copyProperties(grupoActualizable, this.pojoGrupo);
						gruposActualizables.add(grupoActualizable);
						
						valoresActualizable = new ArrayList<GruposCuentas>();
						valoresActualizable.addAll(this.listLlavesValores);
						valoresActualizables.add(valoresActualizable);
					}
				}

				log.info("---> Guardando Llaves ... ");
				this.ifzGrupos.saveOrUpdateList(gruposActualizables);
				log.info("---> Guardando Valores ... ");
				for (List<GruposCuentas> list : valoresActualizables) {
					log.info("---> Guardando valores " + valoresActualizables.indexOf(list) + "/" + valoresActualizables.size());
					this.ifzGruposCuentas.saveOrUpdateList(list);
				}
				log.info("----------------------------------------------------------------------------");
				log.info("Terminado!");
				
				this.paramsRequest.remove("REACOMODA");
				buscar();
			}
		} catch (Exception e) {
			control("Error en GruposAction.buscar", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.pojoGrupo = new Grupos();
			this.idGrupo = 0;
			
			if (this.listGrupoLlaves == null)
				this.listGrupoLlaves = new ArrayList<Llaves>();
			this.listGrupoLlaves.clear();
			
			if (this.listLlavesValores == null)
				this.listLlavesValores = new ArrayList<GruposCuentas>();
			this.listLlavesValores.clear();
		} catch (Exception e) {
			control("Error en GruposAction.nuevo", e);
		}
	}
	
	public void editar() {
		try {
			control();
			this.pojoGrupo = this.ifzGrupos.findById(this.idGrupo);
			if (this.pojoGrupo == null || this.pojoGrupo.getId() == null || this.pojoGrupo.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar el Grupo seleccionado");
				return;
			}
			
			generarListaLlavesFromGrupo();
			verLlavesValores();
			this.permiteBorrarGrupo = this.comprobarUsoGrupo(this.pojoGrupo.getId());
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el Grupo seleccionado", e);
		}
	}
	
	public void guardar() {
		try {
			control();
			if (this.pojoGrupo != null) {
				this.pojoGrupo.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
				this.pojoGrupo.setModificadoPor(this.usuarioId);
				this.pojoGrupo.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoGrupo.getId() == null || this.pojoGrupo.getId() <= 0L) {
					this.pojoGrupo.setCreadoPor(this.usuarioId);
					this.pojoGrupo.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoGrupo.setId(this.ifzGrupos.save(this.pojoGrupo));
					// Agregamos a la lista
					this.listGrupos.add(this.pojoGrupo);
				} else {
					// Actualizamos en la BD
					this.ifzGrupos.update(this.pojoGrupo);
					for (Grupos gpo : this.listGrupos) {
						if (gpo.getId().longValue() == this.pojoGrupo.getId().longValue()) {
							gpo = this.pojoGrupo;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar el Grupo", e);
		}
	}
	
	public void borrar() {
		try {
			control();
			this.pojoGrupo = this.ifzGrupos.findById(this.idGrupo);
			if (this.pojoGrupo == null || this.pojoGrupo.getId() == null || this.pojoGrupo.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar el Grupo indicado para borrar");
				return;
			}
			
			if (this.pojoGrupo != null && this.pojoGrupo.getId() > 0L) {
				if (! this.comprobarUsoGrupo(this.pojoGrupo.getId())) {
					this.operacion = true;
					this.mensaje = "ERROR";
					this.tipoMensaje = 5;
					return;
				}
				
				// Borramos de la bd
				this.ifzGrupos.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoGrupo.setModificadoPor(this.usuarioId);
				this.ifzGrupos.delete(this.pojoGrupo);
				
				// Borramos de la lista
				this.listGrupos.remove(this.pojoGrupo);
				this.idGrupo = 0;
			}
		} catch (Exception e) {
			control("Error en GruposAction.borrar", e);
		}
	}
	
	public boolean comprobarUsoGrupo(long grupoId) {
		try {
			// Comprobamos el grupos debito
			if ((this.ifzAsigGpo.findByProperty("idGrupoDebito.id",  grupoId, 120).size()) > 0)
				return false;
			
			// Comprobamos en grupos credito
			if ((this.ifzAsigGpo.findByProperty("idGrupoCredito.id", grupoId, 120).size()) > 0)
				return false;
		} catch (Exception e) {
			log.error("Error al comprobar el GRUPO en AsignacionGrupos", e);
			return false;
		}
		
		return true;
	}

	private boolean validaRequest(String param) {
		return validaRequest(param, null);
	}
	
	private boolean validaRequest(String param, String refValue) {
		if (! this.isDebug)
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		if (refValue == null || "".equals(refValue.trim()))
			return true;
		
		return (refValue.trim().equals(this.paramsRequest.get(param).trim()));
	}

	/**
	 * Reacomoda el pojo Llave en el espacio asignado en el pojo Grupo de acuerdo a la propiedad posicion de la Llave
	 */
	private void reacomodaLlaves() {
		Grupos aux = new Grupos();
		
		try {
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(aux, this.pojoGrupo);
			
			this.pojoGrupo = new Grupos();
			this.pojoGrupo.setId(aux.getId());
			this.pojoGrupo.setDescripcion(aux.getDescripcion());
			this.pojoGrupo.setIdEmpresa(aux.getIdEmpresa());
			this.pojoGrupo.setEstatus(aux.getEstatus());
			this.pojoGrupo.setCreadoPor(aux.getCreadoPor());
			this.pojoGrupo.setFechaCreacion(aux.getFechaCreacion());
			this.pojoGrupo.setModificadoPor(aux.getModificadoPor());
			this.pojoGrupo.setFechaModificacion(aux.getFechaModificacion());

			if (aux.getLlave1() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave1());
			if (aux.getLlave2() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave2());
			if (aux.getLlave3() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave3());
			if (aux.getLlave4() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave4());
			if (aux.getLlave5() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave5());
			if (aux.getLlave6() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave6());
			if (aux.getLlave7() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave7());
			if (aux.getLlave8() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave8());
			if (aux.getLlave9() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave9());
			if (aux.getLlave10() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave10());
			if (aux.getLlave11() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave11());
			if (aux.getLlave12() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave12());
			if (aux.getLlave13() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave13());
			if (aux.getLlave14() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave14());
			if (aux.getLlave15() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave15());
			if (aux.getLlave16() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave16());
			if (aux.getLlave17() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave17());
			if (aux.getLlave18() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave18());
			if (aux.getLlave19() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave19());
			if (aux.getLlave20() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave20());
			if (aux.getLlave21() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave21());
			if (aux.getLlave22() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave22());
			if (aux.getLlave23() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave23());
			if (aux.getLlave24() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave24());
			if (aux.getLlave25() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave25());
			if (aux.getLlave26() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave26());
			if (aux.getLlave27() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave27());
			if (aux.getLlave28() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave28());
			if (aux.getLlave29() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave29());
			if (aux.getLlave30() != null)
				this.pojoGrupo = llaveToIndex(this.pojoGrupo, aux.getLlave30());
		} catch (Exception e) {
			control("Ocurrio un problema al reorganizar las llaves asignadas al Grupo", e);
		}
	}

	/**
	 * Metodo para comprobar si la posicion asignada (llaveValor) corresponde con la posicion de la Llave
	 */
	private void reacomodaValoresLlaves() {
		if (this.listLlavesValores == null || this.listLlavesValores.isEmpty())
			return;

		log.info("Reacomodando valores de llaves ... ");
		for (GruposCuentas valor : this.listLlavesValores) {
			log.info("validando " + this.listLlavesValores.indexOf(valor) + "/" + this.listLlavesValores.size());
			if (valor.getValorLlave1() != null && valor.getValorLlave1() > 0L && valor.getIdGrupo().getLlave1() != null && valor.getIdGrupo().getLlave1().getPosicion() != 1) 
				valor = llaveValorToIndex(valor, 1, valor.getIdGrupo().getLlave1().getPosicion());
			if (valor.getValorLlave2() != null && valor.getValorLlave2() > 0L && valor.getIdGrupo().getLlave2() != null && valor.getIdGrupo().getLlave2().getPosicion() != 2) 
				valor = llaveValorToIndex(valor, 2, valor.getIdGrupo().getLlave2().getPosicion());
			if (valor.getValorLlave3() != null && valor.getValorLlave3() > 0L && valor.getIdGrupo().getLlave3() != null && valor.getIdGrupo().getLlave3().getPosicion() != 3) 
				valor = llaveValorToIndex(valor, 3, valor.getIdGrupo().getLlave3().getPosicion());
			if (valor.getValorLlave4() != null && valor.getValorLlave4() > 0L && valor.getIdGrupo().getLlave4() != null && valor.getIdGrupo().getLlave4().getPosicion() != 4) 
				valor = llaveValorToIndex(valor, 4, valor.getIdGrupo().getLlave4().getPosicion());
			if (valor.getValorLlave5() != null && valor.getValorLlave5() > 0L && valor.getIdGrupo().getLlave5() != null && valor.getIdGrupo().getLlave5().getPosicion() != 5) 
				valor = llaveValorToIndex(valor, 5, valor.getIdGrupo().getLlave5().getPosicion());
			if (valor.getValorLlave6() != null && valor.getValorLlave6() > 0L && valor.getIdGrupo().getLlave6() != null && valor.getIdGrupo().getLlave6().getPosicion() != 6) 
				valor = llaveValorToIndex(valor, 6, valor.getIdGrupo().getLlave6().getPosicion());
			if (valor.getValorLlave7() != null && valor.getValorLlave7() > 0L && valor.getIdGrupo().getLlave7() != null && valor.getIdGrupo().getLlave7().getPosicion() != 7) 
				valor = llaveValorToIndex(valor, 7, valor.getIdGrupo().getLlave7().getPosicion());
			if (valor.getValorLlave8() != null && valor.getValorLlave8() > 0L && valor.getIdGrupo().getLlave8() != null && valor.getIdGrupo().getLlave8().getPosicion() != 8) 
				valor = llaveValorToIndex(valor, 8, valor.getIdGrupo().getLlave8().getPosicion());
			if (valor.getValorLlave9() != null && valor.getValorLlave9() > 0L && valor.getIdGrupo().getLlave9() != null && valor.getIdGrupo().getLlave9().getPosicion() != 9) 
				valor = llaveValorToIndex(valor, 9, valor.getIdGrupo().getLlave9().getPosicion());
			if (valor.getValorLlave10() != null && valor.getValorLlave10() > 0L && valor.getIdGrupo().getLlave10() != null && valor.getIdGrupo().getLlave10().getPosicion() != 10) 
				valor = llaveValorToIndex(valor, 10, valor.getIdGrupo().getLlave10().getPosicion());
			if (valor.getValorLlave11() != null && valor.getValorLlave11() > 0L && valor.getIdGrupo().getLlave11() != null && valor.getIdGrupo().getLlave11().getPosicion() != 11) 
				valor = llaveValorToIndex(valor, 11, valor.getIdGrupo().getLlave11().getPosicion());
			if (valor.getValorLlave12() != null && valor.getValorLlave12() > 0L && valor.getIdGrupo().getLlave12() != null && valor.getIdGrupo().getLlave12().getPosicion() != 12) 
				valor = llaveValorToIndex(valor, 12, valor.getIdGrupo().getLlave12().getPosicion());
			if (valor.getValorLlave13() != null && valor.getValorLlave13() > 0L && valor.getIdGrupo().getLlave13() != null && valor.getIdGrupo().getLlave13().getPosicion() != 13) 
				valor = llaveValorToIndex(valor, 13, valor.getIdGrupo().getLlave13().getPosicion());
			if (valor.getValorLlave14() != null && valor.getValorLlave14() > 0L && valor.getIdGrupo().getLlave14() != null && valor.getIdGrupo().getLlave14().getPosicion() != 14) 
				valor = llaveValorToIndex(valor, 14, valor.getIdGrupo().getLlave14().getPosicion());
			if (valor.getValorLlave15() != null && valor.getValorLlave15() > 0L && valor.getIdGrupo().getLlave15() != null && valor.getIdGrupo().getLlave15().getPosicion() != 15) 
				valor = llaveValorToIndex(valor, 15, valor.getIdGrupo().getLlave15().getPosicion());
			if (valor.getValorLlave16() != null && valor.getValorLlave16() > 0L && valor.getIdGrupo().getLlave16() != null && valor.getIdGrupo().getLlave16().getPosicion() != 16) 
				valor = llaveValorToIndex(valor, 16, valor.getIdGrupo().getLlave16().getPosicion());
			if (valor.getValorLlave17() != null && valor.getValorLlave17() > 0L && valor.getIdGrupo().getLlave17() != null && valor.getIdGrupo().getLlave17().getPosicion() != 17) 
				valor = llaveValorToIndex(valor, 17, valor.getIdGrupo().getLlave17().getPosicion());
			if (valor.getValorLlave18() != null && valor.getValorLlave18() > 0L && valor.getIdGrupo().getLlave18() != null && valor.getIdGrupo().getLlave18().getPosicion() != 18) 
				valor = llaveValorToIndex(valor, 18, valor.getIdGrupo().getLlave18().getPosicion());
			if (valor.getValorLlave19() != null && valor.getValorLlave19() > 0L && valor.getIdGrupo().getLlave19() != null && valor.getIdGrupo().getLlave19().getPosicion() != 19) 
				valor = llaveValorToIndex(valor, 19, valor.getIdGrupo().getLlave19().getPosicion());
			if (valor.getValorLlave20() != null && valor.getValorLlave20() > 0L && valor.getIdGrupo().getLlave20() != null && valor.getIdGrupo().getLlave20().getPosicion() != 20) 
				valor = llaveValorToIndex(valor, 20, valor.getIdGrupo().getLlave20().getPosicion());
			if (valor.getValorLlave21() != null && valor.getValorLlave21() > 0L && valor.getIdGrupo().getLlave21() != null && valor.getIdGrupo().getLlave21().getPosicion() != 21) 
				valor = llaveValorToIndex(valor, 21, valor.getIdGrupo().getLlave21().getPosicion());
			if (valor.getValorLlave22() != null && valor.getValorLlave22() > 0L && valor.getIdGrupo().getLlave22() != null && valor.getIdGrupo().getLlave22().getPosicion() != 22) 
				valor = llaveValorToIndex(valor, 22, valor.getIdGrupo().getLlave22().getPosicion());
			if (valor.getValorLlave23() != null && valor.getValorLlave23() > 0L && valor.getIdGrupo().getLlave23() != null && valor.getIdGrupo().getLlave23().getPosicion() != 23) 
				valor = llaveValorToIndex(valor, 23, valor.getIdGrupo().getLlave23().getPosicion());
			if (valor.getValorLlave24() != null && valor.getValorLlave24() > 0L && valor.getIdGrupo().getLlave24() != null && valor.getIdGrupo().getLlave24().getPosicion() != 24) 
				valor = llaveValorToIndex(valor, 24, valor.getIdGrupo().getLlave24().getPosicion());
			if (valor.getValorLlave25() != null && valor.getValorLlave25() > 0L && valor.getIdGrupo().getLlave25() != null && valor.getIdGrupo().getLlave25().getPosicion() != 25) 
				valor = llaveValorToIndex(valor, 25, valor.getIdGrupo().getLlave25().getPosicion());
			if (valor.getValorLlave26() != null && valor.getValorLlave26() > 0L && valor.getIdGrupo().getLlave26() != null && valor.getIdGrupo().getLlave26().getPosicion() != 26) 
				valor = llaveValorToIndex(valor, 26, valor.getIdGrupo().getLlave26().getPosicion());
			if (valor.getValorLlave27() != null && valor.getValorLlave27() > 0L && valor.getIdGrupo().getLlave27() != null && valor.getIdGrupo().getLlave27().getPosicion() != 27) 
				valor = llaveValorToIndex(valor, 27, valor.getIdGrupo().getLlave27().getPosicion());
			if (valor.getValorLlave28() != null && valor.getValorLlave28() > 0L && valor.getIdGrupo().getLlave28() != null && valor.getIdGrupo().getLlave28().getPosicion() != 28) 
				valor = llaveValorToIndex(valor, 28, valor.getIdGrupo().getLlave28().getPosicion());
			if (valor.getValorLlave29() != null && valor.getValorLlave29() > 0L && valor.getIdGrupo().getLlave29() != null && valor.getIdGrupo().getLlave29().getPosicion() != 29) 
				valor = llaveValorToIndex(valor, 29, valor.getIdGrupo().getLlave29().getPosicion());
			if (valor.getValorLlave30() != null && valor.getValorLlave30() > 0L && valor.getIdGrupo().getLlave30() != null && valor.getIdGrupo().getLlave30().getPosicion() != 30) 
				valor = llaveValorToIndex(valor, 30, valor.getIdGrupo().getLlave30().getPosicion());
		}
		log.info("Reacomodando valores de llaves ... Terminado!");
	}
	
	/**
	 * Metodo para intercambiar la llave y el valor de la llave
	 * @param item
	 * @param posicionActual numero de llave original
	 * @param posicionNueva  numero de llave nueva
	 * @return
	 */
	private GruposCuentas llaveValorToIndex(GruposCuentas item, int posicionActual, int posicionNueva) {
		log.info("Moviendo posicion de VALOR: " + posicionActual + " --> " + posicionNueva + " ... ");
		switch (posicionActual) {
			case 1:
				switch (posicionNueva) {
					case 1: break;
					case 2: item.setValorLlave2(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 3: item.setValorLlave3(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 4: item.setValorLlave4(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 5: item.setValorLlave5(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 6: item.setValorLlave6(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 7: item.setValorLlave7(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 8: item.setValorLlave8(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 9: item.setValorLlave9(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 10: item.setValorLlave10(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 11: item.setValorLlave11(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 12: item.setValorLlave12(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 13: item.setValorLlave13(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 14: item.setValorLlave14(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 15: item.setValorLlave15(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 16: item.setValorLlave16(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 17: item.setValorLlave17(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 18: item.setValorLlave18(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 19: item.setValorLlave19(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 20: item.setValorLlave20(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 21: item.setValorLlave21(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 22: item.setValorLlave22(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 23: item.setValorLlave23(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 24: item.setValorLlave24(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 25: item.setValorLlave25(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 26: item.setValorLlave26(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 27: item.setValorLlave27(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 28: item.setValorLlave28(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 29: item.setValorLlave29(item.getValorLlave1()); item.setValorLlave1(null); break;
					case 30: item.setValorLlave30(item.getValorLlave1()); item.setValorLlave1(null); break;
					default: break;
				}
			break;
	
			case 2: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 2: break;
					case 3: item.setValorLlave3(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 4: item.setValorLlave4(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 5: item.setValorLlave5(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 6: item.setValorLlave6(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 7: item.setValorLlave7(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 8: item.setValorLlave8(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 9: item.setValorLlave9(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 10: item.setValorLlave10(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 11: item.setValorLlave11(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 12: item.setValorLlave12(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 13: item.setValorLlave13(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 14: item.setValorLlave14(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 15: item.setValorLlave15(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 16: item.setValorLlave16(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 17: item.setValorLlave17(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 18: item.setValorLlave18(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 19: item.setValorLlave19(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 20: item.setValorLlave20(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 21: item.setValorLlave21(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 22: item.setValorLlave22(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 23: item.setValorLlave23(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 24: item.setValorLlave24(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 25: item.setValorLlave25(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 26: item.setValorLlave26(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 27: item.setValorLlave27(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 28: item.setValorLlave28(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 29: item.setValorLlave29(item.getValorLlave2()); item.setValorLlave2(null); break;
					case 30: item.setValorLlave30(item.getValorLlave2()); item.setValorLlave2(null); break;
					default: break;
				}
			break;
	
			case 3: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 2: item.setValorLlave2(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 3: break;
					case 4: item.setValorLlave4(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 5: item.setValorLlave5(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 6: item.setValorLlave6(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 7: item.setValorLlave7(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 8: item.setValorLlave8(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 9: item.setValorLlave9(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 10: item.setValorLlave10(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 11: item.setValorLlave11(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 12: item.setValorLlave12(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 13: item.setValorLlave13(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 14: item.setValorLlave14(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 15: item.setValorLlave15(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 16: item.setValorLlave16(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 17: item.setValorLlave17(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 18: item.setValorLlave18(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 19: item.setValorLlave19(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 20: item.setValorLlave20(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 21: item.setValorLlave21(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 22: item.setValorLlave22(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 23: item.setValorLlave23(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 24: item.setValorLlave24(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 25: item.setValorLlave25(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 26: item.setValorLlave26(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 27: item.setValorLlave27(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 28: item.setValorLlave28(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 29: item.setValorLlave29(item.getValorLlave3()); item.setValorLlave3(null); break;
					case 30: item.setValorLlave30(item.getValorLlave3()); item.setValorLlave3(null); break;
					default: break;
				}
			break;
	
			case 4: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 2: item.setValorLlave2(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 3: item.setValorLlave3(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 4: break;
					case 5: item.setValorLlave5(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 6: item.setValorLlave6(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 7: item.setValorLlave7(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 8: item.setValorLlave8(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 9: item.setValorLlave9(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 10: item.setValorLlave10(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 11: item.setValorLlave11(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 12: item.setValorLlave12(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 13: item.setValorLlave13(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 14: item.setValorLlave14(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 15: item.setValorLlave15(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 16: item.setValorLlave16(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 17: item.setValorLlave17(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 18: item.setValorLlave18(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 19: item.setValorLlave19(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 20: item.setValorLlave20(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 21: item.setValorLlave21(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 22: item.setValorLlave22(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 23: item.setValorLlave23(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 24: item.setValorLlave24(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 25: item.setValorLlave25(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 26: item.setValorLlave26(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 27: item.setValorLlave27(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 28: item.setValorLlave28(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 29: item.setValorLlave29(item.getValorLlave4()); item.setValorLlave4(null); break;
					case 30: item.setValorLlave30(item.getValorLlave4()); item.setValorLlave4(null); break;
					default: break;
				}
			break;
	
			case 5: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 2: item.setValorLlave2(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 3: item.setValorLlave3(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 4: item.setValorLlave4(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 5: break;
					case 6: item.setValorLlave6(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 7: item.setValorLlave7(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 8: item.setValorLlave8(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 9: item.setValorLlave9(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 10: item.setValorLlave10(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 11: item.setValorLlave11(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 12: item.setValorLlave12(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 13: item.setValorLlave13(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 14: item.setValorLlave14(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 15: item.setValorLlave15(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 16: item.setValorLlave16(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 17: item.setValorLlave17(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 18: item.setValorLlave18(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 19: item.setValorLlave19(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 20: item.setValorLlave20(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 21: item.setValorLlave21(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 22: item.setValorLlave22(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 23: item.setValorLlave23(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 24: item.setValorLlave24(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 25: item.setValorLlave25(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 26: item.setValorLlave26(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 27: item.setValorLlave27(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 28: item.setValorLlave28(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 29: item.setValorLlave29(item.getValorLlave5()); item.setValorLlave5(null); break;
					case 30: item.setValorLlave30(item.getValorLlave5()); item.setValorLlave5(null); break;
					default: break;
				}
			break;
	
			case 6: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 2: item.setValorLlave2(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 3: item.setValorLlave3(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 4: item.setValorLlave4(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 5: item.setValorLlave5(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 6: break;
					case 7: item.setValorLlave7(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 8: item.setValorLlave8(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 9: item.setValorLlave9(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 10: item.setValorLlave10(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 11: item.setValorLlave11(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 12: item.setValorLlave12(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 13: item.setValorLlave13(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 14: item.setValorLlave14(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 15: item.setValorLlave15(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 16: item.setValorLlave16(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 17: item.setValorLlave17(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 18: item.setValorLlave18(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 19: item.setValorLlave19(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 20: item.setValorLlave20(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 21: item.setValorLlave21(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 22: item.setValorLlave22(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 23: item.setValorLlave23(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 24: item.setValorLlave24(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 25: item.setValorLlave25(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 26: item.setValorLlave26(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 27: item.setValorLlave27(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 28: item.setValorLlave28(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 29: item.setValorLlave29(item.getValorLlave6()); item.setValorLlave6(null); break;
					case 30: item.setValorLlave30(item.getValorLlave6()); item.setValorLlave6(null); break;
					default: break;
				}
			break;
	
			case 7: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 2: item.setValorLlave2(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 3: item.setValorLlave3(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 4: item.setValorLlave4(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 5: item.setValorLlave5(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 6: item.setValorLlave6(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 7: break;
					case 8: item.setValorLlave8(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 9: item.setValorLlave9(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 10: item.setValorLlave10(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 11: item.setValorLlave11(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 12: item.setValorLlave12(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 13: item.setValorLlave13(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 14: item.setValorLlave14(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 15: item.setValorLlave15(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 16: item.setValorLlave16(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 17: item.setValorLlave17(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 18: item.setValorLlave18(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 19: item.setValorLlave19(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 20: item.setValorLlave20(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 21: item.setValorLlave21(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 22: item.setValorLlave22(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 23: item.setValorLlave23(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 24: item.setValorLlave24(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 25: item.setValorLlave25(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 26: item.setValorLlave26(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 27: item.setValorLlave27(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 28: item.setValorLlave28(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 29: item.setValorLlave29(item.getValorLlave7()); item.setValorLlave7(null); break;
					case 30: item.setValorLlave30(item.getValorLlave7()); item.setValorLlave7(null); break;
					default: break;
				}
			break;
	
			case 8: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 2: item.setValorLlave2(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 3: item.setValorLlave3(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 4: item.setValorLlave4(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 5: item.setValorLlave5(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 6: item.setValorLlave6(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 7: item.setValorLlave7(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 8: break;
					case 9: item.setValorLlave9(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 10: item.setValorLlave10(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 11: item.setValorLlave11(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 12: item.setValorLlave12(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 13: item.setValorLlave13(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 14: item.setValorLlave14(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 15: item.setValorLlave15(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 16: item.setValorLlave16(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 17: item.setValorLlave17(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 18: item.setValorLlave18(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 19: item.setValorLlave19(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 20: item.setValorLlave20(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 21: item.setValorLlave21(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 22: item.setValorLlave22(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 23: item.setValorLlave23(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 24: item.setValorLlave24(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 25: item.setValorLlave25(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 26: item.setValorLlave26(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 27: item.setValorLlave27(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 28: item.setValorLlave28(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 29: item.setValorLlave29(item.getValorLlave8()); item.setValorLlave8(null); break;
					case 30: item.setValorLlave30(item.getValorLlave8()); item.setValorLlave8(null); break;
					default: break;
				}
			break;
	
			case 9: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 2: item.setValorLlave2(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 3: item.setValorLlave3(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 4: item.setValorLlave4(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 5: item.setValorLlave5(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 6: item.setValorLlave6(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 7: item.setValorLlave7(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 8: item.setValorLlave8(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 9: break;
					case 10: item.setValorLlave10(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 11: item.setValorLlave11(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 12: item.setValorLlave12(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 13: item.setValorLlave13(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 14: item.setValorLlave14(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 15: item.setValorLlave15(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 16: item.setValorLlave16(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 17: item.setValorLlave17(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 18: item.setValorLlave18(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 19: item.setValorLlave19(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 20: item.setValorLlave20(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 21: item.setValorLlave21(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 22: item.setValorLlave22(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 23: item.setValorLlave23(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 24: item.setValorLlave24(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 25: item.setValorLlave25(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 26: item.setValorLlave26(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 27: item.setValorLlave27(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 28: item.setValorLlave28(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 29: item.setValorLlave29(item.getValorLlave9()); item.setValorLlave9(null); break;
					case 30: item.setValorLlave30(item.getValorLlave9()); item.setValorLlave9(null); break;
					default: break;
				}
			break;
	
			case 10: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 2: item.setValorLlave2(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 3: item.setValorLlave3(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 4: item.setValorLlave4(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 5: item.setValorLlave5(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 6: item.setValorLlave6(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 7: item.setValorLlave7(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 8: item.setValorLlave8(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 9: item.setValorLlave9(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 10: break;
					case 11: item.setValorLlave11(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 12: item.setValorLlave12(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 13: item.setValorLlave13(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 14: item.setValorLlave14(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 15: item.setValorLlave15(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 16: item.setValorLlave16(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 17: item.setValorLlave17(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 18: item.setValorLlave18(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 19: item.setValorLlave19(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 20: item.setValorLlave20(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 21: item.setValorLlave21(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 22: item.setValorLlave22(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 23: item.setValorLlave23(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 24: item.setValorLlave24(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 25: item.setValorLlave25(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 26: item.setValorLlave26(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 27: item.setValorLlave27(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 28: item.setValorLlave28(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 29: item.setValorLlave29(item.getValorLlave10()); item.setValorLlave10(null); break;
					case 30: item.setValorLlave30(item.getValorLlave10()); item.setValorLlave10(null); break;
					default: break;
				}
			break;
	
			case 11: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 2: item.setValorLlave2(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 3: item.setValorLlave3(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 4: item.setValorLlave4(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 5: item.setValorLlave5(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 6: item.setValorLlave6(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 7: item.setValorLlave7(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 8: item.setValorLlave8(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 9: item.setValorLlave9(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 10: item.setValorLlave10(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 11: break;
					case 12: item.setValorLlave12(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 13: item.setValorLlave13(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 14: item.setValorLlave14(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 15: item.setValorLlave15(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 16: item.setValorLlave16(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 17: item.setValorLlave17(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 18: item.setValorLlave18(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 19: item.setValorLlave19(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 20: item.setValorLlave20(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 21: item.setValorLlave21(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 22: item.setValorLlave22(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 23: item.setValorLlave23(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 24: item.setValorLlave24(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 25: item.setValorLlave25(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 26: item.setValorLlave26(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 27: item.setValorLlave27(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 28: item.setValorLlave28(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 29: item.setValorLlave29(item.getValorLlave11()); item.setValorLlave11(null); break;
					case 30: item.setValorLlave30(item.getValorLlave11()); item.setValorLlave11(null); break;
					default: break;
				}
			break;
	
			case 12: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 2: item.setValorLlave2(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 3: item.setValorLlave3(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 4: item.setValorLlave4(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 5: item.setValorLlave5(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 6: item.setValorLlave6(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 7: item.setValorLlave7(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 8: item.setValorLlave8(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 9: item.setValorLlave9(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 10: item.setValorLlave10(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 11: item.setValorLlave11(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 12: break;
					case 13: item.setValorLlave13(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 14: item.setValorLlave14(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 15: item.setValorLlave15(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 16: item.setValorLlave16(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 17: item.setValorLlave17(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 18: item.setValorLlave18(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 19: item.setValorLlave19(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 20: item.setValorLlave20(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 21: item.setValorLlave21(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 22: item.setValorLlave22(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 23: item.setValorLlave23(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 24: item.setValorLlave24(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 25: item.setValorLlave25(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 26: item.setValorLlave26(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 27: item.setValorLlave27(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 28: item.setValorLlave28(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 29: item.setValorLlave29(item.getValorLlave12()); item.setValorLlave12(null); break;
					case 30: item.setValorLlave30(item.getValorLlave12()); item.setValorLlave12(null); break;
					default: break;
				}
			break;
	
			case 13: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 2: item.setValorLlave2(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 3: item.setValorLlave3(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 4: item.setValorLlave4(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 5: item.setValorLlave5(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 6: item.setValorLlave6(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 7: item.setValorLlave7(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 8: item.setValorLlave8(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 9: item.setValorLlave9(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 10: item.setValorLlave10(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 11: item.setValorLlave11(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 12: item.setValorLlave12(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 13: break;
					case 14: item.setValorLlave14(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 15: item.setValorLlave15(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 16: item.setValorLlave16(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 17: item.setValorLlave17(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 18: item.setValorLlave18(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 19: item.setValorLlave19(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 20: item.setValorLlave20(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 21: item.setValorLlave21(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 22: item.setValorLlave22(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 23: item.setValorLlave23(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 24: item.setValorLlave24(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 25: item.setValorLlave25(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 26: item.setValorLlave26(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 27: item.setValorLlave27(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 28: item.setValorLlave28(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 29: item.setValorLlave29(item.getValorLlave13()); item.setValorLlave13(null); break;
					case 30: item.setValorLlave30(item.getValorLlave13()); item.setValorLlave13(null); break;
					default: break;
				}
			break;
	
			case 14: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 2: item.setValorLlave2(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 3: item.setValorLlave3(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 4: item.setValorLlave4(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 5: item.setValorLlave5(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 6: item.setValorLlave6(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 7: item.setValorLlave7(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 8: item.setValorLlave8(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 9: item.setValorLlave9(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 10: item.setValorLlave10(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 11: item.setValorLlave11(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 12: item.setValorLlave12(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 13: item.setValorLlave13(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 14: break;
					case 15: item.setValorLlave15(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 16: item.setValorLlave16(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 17: item.setValorLlave17(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 18: item.setValorLlave18(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 19: item.setValorLlave19(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 20: item.setValorLlave20(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 21: item.setValorLlave21(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 22: item.setValorLlave22(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 23: item.setValorLlave23(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 24: item.setValorLlave24(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 25: item.setValorLlave25(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 26: item.setValorLlave26(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 27: item.setValorLlave27(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 28: item.setValorLlave28(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 29: item.setValorLlave29(item.getValorLlave14()); item.setValorLlave14(null); break;
					case 30: item.setValorLlave30(item.getValorLlave14()); item.setValorLlave14(null); break;
					default: break;
				}
			break;
	
			case 15: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 2: item.setValorLlave2(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 3: item.setValorLlave3(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 4: item.setValorLlave4(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 5: item.setValorLlave5(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 6: item.setValorLlave6(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 7: item.setValorLlave7(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 8: item.setValorLlave8(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 9: item.setValorLlave9(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 10: item.setValorLlave10(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 11: item.setValorLlave11(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 12: item.setValorLlave12(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 13: item.setValorLlave13(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 14: item.setValorLlave14(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 15: break;
					case 16: item.setValorLlave16(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 17: item.setValorLlave17(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 18: item.setValorLlave18(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 19: item.setValorLlave19(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 20: item.setValorLlave20(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 21: item.setValorLlave21(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 22: item.setValorLlave22(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 23: item.setValorLlave23(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 24: item.setValorLlave24(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 25: item.setValorLlave25(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 26: item.setValorLlave26(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 27: item.setValorLlave27(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 28: item.setValorLlave28(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 29: item.setValorLlave29(item.getValorLlave15()); item.setValorLlave15(null); break;
					case 30: item.setValorLlave30(item.getValorLlave15()); item.setValorLlave15(null); break;
					default: break;
				}
			break;
	
			case 16: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 2: item.setValorLlave2(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 3: item.setValorLlave3(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 4: item.setValorLlave4(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 5: item.setValorLlave5(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 6: item.setValorLlave6(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 7: item.setValorLlave7(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 8: item.setValorLlave8(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 9: item.setValorLlave9(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 10: item.setValorLlave10(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 11: item.setValorLlave11(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 12: item.setValorLlave12(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 13: item.setValorLlave13(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 14: item.setValorLlave14(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 15: item.setValorLlave15(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 16: break;
					case 17: item.setValorLlave17(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 18: item.setValorLlave18(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 19: item.setValorLlave19(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 20: item.setValorLlave20(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 21: item.setValorLlave21(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 22: item.setValorLlave22(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 23: item.setValorLlave23(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 24: item.setValorLlave24(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 25: item.setValorLlave25(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 26: item.setValorLlave26(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 27: item.setValorLlave27(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 28: item.setValorLlave28(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 29: item.setValorLlave29(item.getValorLlave16()); item.setValorLlave16(null); break;
					case 30: item.setValorLlave30(item.getValorLlave16()); item.setValorLlave16(null); break;
					default: break;
				}
			break;
	
			case 17: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 2: item.setValorLlave2(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 3: item.setValorLlave3(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 4: item.setValorLlave4(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 5: item.setValorLlave5(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 6: item.setValorLlave6(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 7: item.setValorLlave7(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 8: item.setValorLlave8(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 9: item.setValorLlave9(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 10: item.setValorLlave10(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 11: item.setValorLlave11(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 12: item.setValorLlave12(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 13: item.setValorLlave13(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 14: item.setValorLlave14(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 15: item.setValorLlave15(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 16: item.setValorLlave16(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 17: break;
					case 18: item.setValorLlave18(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 19: item.setValorLlave19(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 20: item.setValorLlave20(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 21: item.setValorLlave21(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 22: item.setValorLlave22(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 23: item.setValorLlave23(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 24: item.setValorLlave24(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 25: item.setValorLlave25(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 26: item.setValorLlave26(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 27: item.setValorLlave27(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 28: item.setValorLlave28(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 29: item.setValorLlave29(item.getValorLlave17()); item.setValorLlave17(null); break;
					case 30: item.setValorLlave30(item.getValorLlave17()); item.setValorLlave17(null); break;
					default: break;
				}
			break;
	
			case 18: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 2: item.setValorLlave2(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 3: item.setValorLlave3(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 4: item.setValorLlave4(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 5: item.setValorLlave5(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 6: item.setValorLlave6(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 7: item.setValorLlave7(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 8: item.setValorLlave8(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 9: item.setValorLlave9(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 10: item.setValorLlave10(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 11: item.setValorLlave11(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 12: item.setValorLlave12(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 13: item.setValorLlave13(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 14: item.setValorLlave14(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 15: item.setValorLlave15(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 16: item.setValorLlave16(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 17: item.setValorLlave17(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 18: break;
					case 19: item.setValorLlave19(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 20: item.setValorLlave20(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 21: item.setValorLlave21(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 22: item.setValorLlave22(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 23: item.setValorLlave23(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 24: item.setValorLlave24(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 25: item.setValorLlave25(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 26: item.setValorLlave26(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 27: item.setValorLlave27(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 28: item.setValorLlave28(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 29: item.setValorLlave29(item.getValorLlave18()); item.setValorLlave18(null); break;
					case 30: item.setValorLlave30(item.getValorLlave18()); item.setValorLlave18(null); break;
					default: break;
				}
			break;
	
			case 19: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 2: item.setValorLlave2(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 3: item.setValorLlave3(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 4: item.setValorLlave4(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 5: item.setValorLlave5(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 6: item.setValorLlave6(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 7: item.setValorLlave7(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 8: item.setValorLlave8(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 9: item.setValorLlave9(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 10: item.setValorLlave10(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 11: item.setValorLlave11(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 12: item.setValorLlave12(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 13: item.setValorLlave13(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 14: item.setValorLlave14(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 15: item.setValorLlave15(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 16: item.setValorLlave16(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 17: item.setValorLlave17(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 18: item.setValorLlave18(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 19: break;
					case 20: item.setValorLlave20(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 21: item.setValorLlave21(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 22: item.setValorLlave22(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 23: item.setValorLlave23(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 24: item.setValorLlave24(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 25: item.setValorLlave25(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 26: item.setValorLlave26(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 27: item.setValorLlave27(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 28: item.setValorLlave28(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 29: item.setValorLlave29(item.getValorLlave19()); item.setValorLlave19(null); break;
					case 30: item.setValorLlave30(item.getValorLlave19()); item.setValorLlave19(null); break;
					default: break;
				}
			break;
	
			case 20: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 2: item.setValorLlave2(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 3: item.setValorLlave3(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 4: item.setValorLlave4(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 5: item.setValorLlave5(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 6: item.setValorLlave6(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 7: item.setValorLlave7(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 8: item.setValorLlave8(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 9: item.setValorLlave9(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 10: item.setValorLlave10(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 11: item.setValorLlave11(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 12: item.setValorLlave12(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 13: item.setValorLlave13(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 14: item.setValorLlave14(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 15: item.setValorLlave15(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 16: item.setValorLlave16(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 17: item.setValorLlave17(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 18: item.setValorLlave18(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 19: item.setValorLlave19(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 20: break;
					case 21: item.setValorLlave21(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 22: item.setValorLlave22(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 23: item.setValorLlave23(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 24: item.setValorLlave24(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 25: item.setValorLlave25(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 26: item.setValorLlave26(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 27: item.setValorLlave27(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 28: item.setValorLlave28(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 29: item.setValorLlave29(item.getValorLlave20()); item.setValorLlave20(null); break;
					case 30: item.setValorLlave30(item.getValorLlave20()); item.setValorLlave20(null); break;
					default: break;
				}
			break;
	
			case 21: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 2: item.setValorLlave2(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 3: item.setValorLlave3(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 4: item.setValorLlave4(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 5: item.setValorLlave5(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 6: item.setValorLlave6(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 7: item.setValorLlave7(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 8: item.setValorLlave8(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 9: item.setValorLlave9(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 10: item.setValorLlave10(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 11: item.setValorLlave11(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 12: item.setValorLlave12(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 13: item.setValorLlave13(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 14: item.setValorLlave14(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 15: item.setValorLlave15(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 16: item.setValorLlave16(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 17: item.setValorLlave17(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 18: item.setValorLlave18(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 19: item.setValorLlave19(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 20: item.setValorLlave20(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 21: break;
					case 22: item.setValorLlave22(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 23: item.setValorLlave23(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 24: item.setValorLlave24(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 25: item.setValorLlave25(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 26: item.setValorLlave26(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 27: item.setValorLlave27(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 28: item.setValorLlave28(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 29: item.setValorLlave29(item.getValorLlave21()); item.setValorLlave21(null); break;
					case 30: item.setValorLlave30(item.getValorLlave21()); item.setValorLlave21(null); break;
					default: break;
				}
			break;
	
			case 22: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 2: item.setValorLlave2(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 3: item.setValorLlave3(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 4: item.setValorLlave4(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 5: item.setValorLlave5(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 6: item.setValorLlave6(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 7: item.setValorLlave7(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 8: item.setValorLlave8(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 9: item.setValorLlave9(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 10: item.setValorLlave10(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 11: item.setValorLlave11(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 12: item.setValorLlave12(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 13: item.setValorLlave13(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 14: item.setValorLlave14(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 15: item.setValorLlave15(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 16: item.setValorLlave16(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 17: item.setValorLlave17(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 18: item.setValorLlave18(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 19: item.setValorLlave19(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 20: item.setValorLlave20(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 21: item.setValorLlave21(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 22: break;
					case 23: item.setValorLlave23(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 24: item.setValorLlave24(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 25: item.setValorLlave25(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 26: item.setValorLlave26(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 27: item.setValorLlave27(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 28: item.setValorLlave28(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 29: item.setValorLlave29(item.getValorLlave22()); item.setValorLlave22(null); break;
					case 30: item.setValorLlave30(item.getValorLlave22()); item.setValorLlave22(null); break;
					default: break;
				}
			break;
	
			case 23: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 2: item.setValorLlave2(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 3: item.setValorLlave3(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 4: item.setValorLlave4(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 5: item.setValorLlave5(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 6: item.setValorLlave6(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 7: item.setValorLlave7(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 8: item.setValorLlave8(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 9: item.setValorLlave9(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 10: item.setValorLlave10(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 11: item.setValorLlave11(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 12: item.setValorLlave12(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 13: item.setValorLlave13(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 14: item.setValorLlave14(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 15: item.setValorLlave15(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 16: item.setValorLlave16(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 17: item.setValorLlave17(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 18: item.setValorLlave18(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 19: item.setValorLlave19(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 20: item.setValorLlave20(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 21: item.setValorLlave21(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 22: item.setValorLlave22(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 23: break;
					case 24: item.setValorLlave24(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 25: item.setValorLlave25(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 26: item.setValorLlave26(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 27: item.setValorLlave27(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 28: item.setValorLlave28(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 29: item.setValorLlave29(item.getValorLlave23()); item.setValorLlave23(null); break;
					case 30: item.setValorLlave30(item.getValorLlave23()); item.setValorLlave23(null); break;
					default: break;
				}
			break;
	
			case 24: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 2: item.setValorLlave2(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 3: item.setValorLlave3(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 4: item.setValorLlave4(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 5: item.setValorLlave5(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 6: item.setValorLlave6(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 7: item.setValorLlave7(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 8: item.setValorLlave8(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 9: item.setValorLlave9(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 10: item.setValorLlave10(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 11: item.setValorLlave11(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 12: item.setValorLlave12(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 13: item.setValorLlave13(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 14: item.setValorLlave14(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 15: item.setValorLlave15(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 16: item.setValorLlave16(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 17: item.setValorLlave17(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 18: item.setValorLlave18(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 19: item.setValorLlave19(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 20: item.setValorLlave20(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 21: item.setValorLlave21(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 22: item.setValorLlave22(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 23: item.setValorLlave23(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 24: break;
					case 25: item.setValorLlave25(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 26: item.setValorLlave26(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 27: item.setValorLlave27(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 28: item.setValorLlave28(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 29: item.setValorLlave29(item.getValorLlave24()); item.setValorLlave24(null); break;
					case 30: item.setValorLlave30(item.getValorLlave24()); item.setValorLlave24(null); break;
					default: break;
				}
			break;
	
			case 25: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 2: item.setValorLlave2(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 3: item.setValorLlave3(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 4: item.setValorLlave4(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 5: item.setValorLlave5(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 6: item.setValorLlave6(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 7: item.setValorLlave7(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 8: item.setValorLlave8(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 9: item.setValorLlave9(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 10: item.setValorLlave10(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 11: item.setValorLlave11(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 12: item.setValorLlave12(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 13: item.setValorLlave13(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 14: item.setValorLlave14(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 15: item.setValorLlave15(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 16: item.setValorLlave16(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 17: item.setValorLlave17(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 18: item.setValorLlave18(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 19: item.setValorLlave19(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 20: item.setValorLlave20(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 21: item.setValorLlave21(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 22: item.setValorLlave22(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 23: item.setValorLlave23(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 24: item.setValorLlave24(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 25: break;
					case 26: item.setValorLlave26(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 27: item.setValorLlave27(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 28: item.setValorLlave28(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 29: item.setValorLlave29(item.getValorLlave25()); item.setValorLlave25(null); break;
					case 30: item.setValorLlave30(item.getValorLlave25()); item.setValorLlave25(null); break;
					default: break;
				}
			break;
	
			case 26: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 2: item.setValorLlave2(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 3: item.setValorLlave3(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 4: item.setValorLlave4(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 5: item.setValorLlave5(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 6: item.setValorLlave6(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 7: item.setValorLlave7(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 8: item.setValorLlave8(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 9: item.setValorLlave9(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 10: item.setValorLlave10(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 11: item.setValorLlave11(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 12: item.setValorLlave12(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 13: item.setValorLlave13(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 14: item.setValorLlave14(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 15: item.setValorLlave15(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 16: item.setValorLlave16(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 17: item.setValorLlave17(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 18: item.setValorLlave18(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 19: item.setValorLlave19(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 20: item.setValorLlave20(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 21: item.setValorLlave21(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 22: item.setValorLlave22(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 23: item.setValorLlave23(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 24: item.setValorLlave24(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 25: item.setValorLlave25(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 26: break;
					case 27: item.setValorLlave27(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 28: item.setValorLlave28(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 29: item.setValorLlave29(item.getValorLlave26()); item.setValorLlave26(null); break;
					case 30: item.setValorLlave30(item.getValorLlave26()); item.setValorLlave26(null); break;
					default: break;
				}
			break;
	
			case 27: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 2: item.setValorLlave2(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 3: item.setValorLlave3(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 4: item.setValorLlave4(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 5: item.setValorLlave5(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 6: item.setValorLlave6(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 7: item.setValorLlave7(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 8: item.setValorLlave8(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 9: item.setValorLlave9(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 10: item.setValorLlave10(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 11: item.setValorLlave11(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 12: item.setValorLlave12(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 13: item.setValorLlave13(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 14: item.setValorLlave14(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 15: item.setValorLlave15(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 16: item.setValorLlave16(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 17: item.setValorLlave17(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 18: item.setValorLlave18(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 19: item.setValorLlave19(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 20: item.setValorLlave20(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 21: item.setValorLlave21(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 22: item.setValorLlave22(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 23: item.setValorLlave23(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 24: item.setValorLlave24(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 25: item.setValorLlave25(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 26: item.setValorLlave26(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 27: break;
					case 28: item.setValorLlave28(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 29: item.setValorLlave29(item.getValorLlave27()); item.setValorLlave27(null); break;
					case 30: item.setValorLlave30(item.getValorLlave27()); item.setValorLlave27(null); break;
					default: break;
				}
			break;
	
			case 28: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 2: item.setValorLlave2(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 3: item.setValorLlave3(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 4: item.setValorLlave4(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 5: item.setValorLlave5(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 6: item.setValorLlave6(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 7: item.setValorLlave7(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 8: item.setValorLlave8(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 9: item.setValorLlave9(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 10: item.setValorLlave10(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 11: item.setValorLlave11(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 12: item.setValorLlave12(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 13: item.setValorLlave13(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 14: item.setValorLlave14(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 15: item.setValorLlave15(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 16: item.setValorLlave16(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 17: item.setValorLlave17(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 18: item.setValorLlave18(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 19: item.setValorLlave19(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 20: item.setValorLlave20(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 21: item.setValorLlave21(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 22: item.setValorLlave22(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 23: item.setValorLlave23(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 24: item.setValorLlave24(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 25: item.setValorLlave25(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 26: item.setValorLlave26(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 27: item.setValorLlave27(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 28: break;
					case 29: item.setValorLlave29(item.getValorLlave28()); item.setValorLlave28(null); break;
					case 30: item.setValorLlave30(item.getValorLlave28()); item.setValorLlave28(null); break;
					default: break;
				}
			break;
	
			case 29: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 2: item.setValorLlave2(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 3: item.setValorLlave3(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 4: item.setValorLlave4(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 5: item.setValorLlave5(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 6: item.setValorLlave6(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 7: item.setValorLlave7(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 8: item.setValorLlave8(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 9: item.setValorLlave9(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 10: item.setValorLlave10(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 11: item.setValorLlave11(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 12: item.setValorLlave12(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 13: item.setValorLlave13(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 14: item.setValorLlave14(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 15: item.setValorLlave15(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 16: item.setValorLlave16(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 17: item.setValorLlave17(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 18: item.setValorLlave18(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 19: item.setValorLlave19(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 20: item.setValorLlave20(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 21: item.setValorLlave21(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 22: item.setValorLlave22(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 23: item.setValorLlave23(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 24: item.setValorLlave24(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 25: item.setValorLlave25(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 26: item.setValorLlave26(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 27: item.setValorLlave27(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 28: item.setValorLlave28(item.getValorLlave29()); item.setValorLlave29(null); break;
					case 29: break;
					case 30: item.setValorLlave30(item.getValorLlave29()); item.setValorLlave29(null); break;
					default: break;
				}
			break;
	
			case 30: 
				switch (posicionNueva) {
					case 1: item.setValorLlave1(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 2: item.setValorLlave2(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 3: item.setValorLlave3(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 4: item.setValorLlave4(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 5: item.setValorLlave5(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 6: item.setValorLlave6(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 7: item.setValorLlave7(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 8: item.setValorLlave8(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 9: item.setValorLlave9(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 10: item.setValorLlave10(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 11: item.setValorLlave11(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 12: item.setValorLlave12(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 13: item.setValorLlave13(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 14: item.setValorLlave14(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 15: item.setValorLlave15(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 16: item.setValorLlave16(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 17: item.setValorLlave17(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 18: item.setValorLlave18(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 19: item.setValorLlave19(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 20: item.setValorLlave20(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 21: item.setValorLlave21(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 22: item.setValorLlave22(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 23: item.setValorLlave23(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 24: item.setValorLlave24(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 25: item.setValorLlave25(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 26: item.setValorLlave26(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 27: item.setValorLlave27(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 28: item.setValorLlave28(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 29: item.setValorLlave29(item.getValorLlave30()); item.setValorLlave30(null); break;
					case 30: break;
					default: break;
				}
			break;
	
			default: break;
		}
		
		log.info("Moviendo posicion de VALOR: " + posicionActual + " --> " + posicionNueva + " ... OK!");
		return item;
	}
	
	private Grupos llaveToIndex(Grupos grupo, Llaves llave) {
		if (llave.getPosicion() == 1)
			grupo.setLlave1(llave);
		else if (llave.getPosicion() == 2)
			grupo.setLlave2(llave);
		else if (llave.getPosicion() == 3)
			grupo.setLlave3(llave);
		else if (llave.getPosicion() == 4)
			grupo.setLlave4(llave);
		else if (llave.getPosicion() == 5)
			grupo.setLlave5(llave);
		else if (llave.getPosicion() == 6)
			grupo.setLlave6(llave);
		else if (llave.getPosicion() == 7)
			grupo.setLlave7(llave);
		else if (llave.getPosicion() == 8)
			grupo.setLlave8(llave);
		else if (llave.getPosicion() == 9)
			grupo.setLlave9(llave);
		else if (llave.getPosicion() == 10)
			grupo.setLlave10(llave);
		else if (llave.getPosicion() == 11)
			grupo.setLlave11(llave);
		else if (llave.getPosicion() == 12)
			grupo.setLlave12(llave);
		else if (llave.getPosicion() == 13)
			grupo.setLlave13(llave);
		else if (llave.getPosicion() == 14)
			grupo.setLlave14(llave);
		else if (llave.getPosicion() == 15)
			grupo.setLlave15(llave);
		else if (llave.getPosicion() == 16)
			grupo.setLlave16(llave);
		else if (llave.getPosicion() == 17)
			grupo.setLlave17(llave);
		else if (llave.getPosicion() == 18)
			grupo.setLlave18(llave);
		else if (llave.getPosicion() == 19)
			grupo.setLlave19(llave);
		else if (llave.getPosicion() == 20)
			grupo.setLlave20(llave);
		else if (llave.getPosicion() == 21)
			grupo.setLlave21(llave);
		else if (llave.getPosicion() == 22)
			grupo.setLlave22(llave);
		else if (llave.getPosicion() == 23)
			grupo.setLlave23(llave);
		else if (llave.getPosicion() == 24)
			grupo.setLlave24(llave);
		else if (llave.getPosicion() == 25)
			grupo.setLlave25(llave);
		else if (llave.getPosicion() == 26)
			grupo.setLlave26(llave);
		else if (llave.getPosicion() == 27)
			grupo.setLlave27(llave);
		else if (llave.getPosicion() == 28)
			grupo.setLlave28(llave);
		else if (llave.getPosicion() == 29)
			grupo.setLlave29(llave);
		else if (llave.getPosicion() == 30)
			grupo.setLlave30(llave);
		
		return grupo;
	}
	
	private void control() {
		this.operacion = false;
		this.mensaje = "OK";
		this.tipoMensaje = 0;
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Se ha producido un error desconocido";
		
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		log.error("\nGRUPOS :: " + this.usuario + "\n" + mensaje + "\n" + this.mensajeDetalles, throwable);
		//backtracePrint();
	}

	// ------------------------------------------------------------------------
	// Llaves
	// ------------------------------------------------------------------------
	
	public void nuevaBusquedaLlaves() {
		if (this.listLlaves == null)
			this.listLlaves = new ArrayList<Llaves>();
		this.listLlaves.clear();
		this.pojoLlave = null;
		this.campoBusquedaLlaves = this.tiposBusquedaLlaves.get(0).getValue().toString();
		this.valorBusquedaLlaves = "";
		this.numPaginaLlaves = 1;
	}
	
	public void buscarLlaves() {
		try {
			control();
			if ("".equals(this.campoBusquedaLlaves))
				this.campoBusquedaLlaves = tiposBusquedaLlaves.get(0).getValue().toString();
			this.listLlaves = this.ifzLlaves.findLikeProperty(this.campoBusquedaLlaves, this.valorBusquedaLlaves, 120);
			if (this.listLlaves == null || this.listLlaves.isEmpty()) 
				control(2, "Busqueda vacia");
		} catch (Exception e) {
			control("Error en GruposAction.buscarLlaves", e);
		}
	}
	
	public void seleccionarLlave() {
		try {
			control();
			agregarValorLlave();
			nuevaBusquedaLlaves();
		} catch (Exception e) {
			if ("EXISTE_LLAVE".equals(e.getMessage()))
				control(4, "Ya existe la Llave indicada");
			else
				control("Error en GruposAction.seleccionarLlave", e);
		}
	}
	
	public void agregarValorLlave() throws Exception {
		if (this.pojoLlave == null || this.pojoLlave.getId() == null || this.pojoLlave.getId() <= 0L) {
			this.permiteBorrar = 0;
			return;
		}
		
		switch (this.pojoLlave.getPosicion()) {
			case  1: this.pojoGrupo.setLlave1( this.pojoLlave); break;
			case  2: this.pojoGrupo.setLlave2( this.pojoLlave); break;
			case  3: this.pojoGrupo.setLlave3( this.pojoLlave); break;
			case  4: this.pojoGrupo.setLlave4( this.pojoLlave); break;
			case  5: this.pojoGrupo.setLlave5( this.pojoLlave); break;
			case  6: this.pojoGrupo.setLlave6( this.pojoLlave); break;
			case  7: this.pojoGrupo.setLlave7( this.pojoLlave); break;
			case  8: this.pojoGrupo.setLlave8( this.pojoLlave); break;
			case  9: this.pojoGrupo.setLlave9( this.pojoLlave); break;
			case 10: this.pojoGrupo.setLlave10(this.pojoLlave); break;
			case 11: this.pojoGrupo.setLlave11(this.pojoLlave); break;
			case 12: this.pojoGrupo.setLlave12(this.pojoLlave); break;
			case 13: this.pojoGrupo.setLlave13(this.pojoLlave); break;
			case 14: this.pojoGrupo.setLlave14(this.pojoLlave); break;
			case 15: this.pojoGrupo.setLlave15(this.pojoLlave); break;
			case 16: this.pojoGrupo.setLlave16(this.pojoLlave); break;
			case 17: this.pojoGrupo.setLlave17(this.pojoLlave); break;
			case 18: this.pojoGrupo.setLlave18(this.pojoLlave); break;
			case 19: this.pojoGrupo.setLlave19(this.pojoLlave); break;
			case 20: this.pojoGrupo.setLlave20(this.pojoLlave); break;
			case 21: this.pojoGrupo.setLlave21(this.pojoLlave); break;
			case 22: this.pojoGrupo.setLlave22(this.pojoLlave); break;
			case 23: this.pojoGrupo.setLlave23(this.pojoLlave); break;
			case 24: this.pojoGrupo.setLlave24(this.pojoLlave); break;
			case 25: this.pojoGrupo.setLlave25(this.pojoLlave); break;
			case 26: this.pojoGrupo.setLlave26(this.pojoLlave); break;
			case 27: this.pojoGrupo.setLlave27(this.pojoLlave); break;
			case 28: this.pojoGrupo.setLlave28(this.pojoLlave); break;
			case 29: this.pojoGrupo.setLlave29(this.pojoLlave); break;
			case 30: this.pojoGrupo.setLlave30(this.pojoLlave); break;
			default: break;
		}
		
		generarListaLlavesFromGrupo();
	}
	
	public boolean comprobarLlave(long llaveId) throws Exception {
		boolean existe = false;
		
		if (this.permitirLlavesRepetidas)
			return true;

		// Ciclo para determinar si la llave que queremos guardar ya existe en alguna columna anterior
		for (Llaves var : this.listGrupoLlaves) {
			if (var.getId().longValue() == llaveId) {
				existe = false;
				break;
			}
		}
		
		if (existe) 
			throw new Exception("EXISTE_LLAVE");
		return true;
	}
	
	public void borrarLlaveGrupo() {
		try {
			control();
			if (! this.permiteBorrarGrupo) {
				control(5, "No se permite borrar Grupo");
				return;
			}
			
			if (this.pojoLlaveBorrar != null && this.pojoLlaveBorrar.getId() > 0L) {
				this.listGrupoLlaves.remove(this.pojoLlaveBorrar);
				
				actualizarGrupoLlavesFromLista();
				generarListaLlavesFromGrupo();
			}
		} catch (Exception e) {
			control("Error en GruposAction.borrar", e);
		}
	}
	
	public void generarListaLlavesFromGrupo() {
		if (this.listGrupoLlaves == null)
			this.listGrupoLlaves = new ArrayList<Llaves>();
		this.listGrupoLlaves.clear();
		
		if (this.pojoGrupo.getLlave1() != null && this.pojoGrupo.getLlave1().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave1());
			this.permiteBorrar = this.pojoGrupo.getLlave1().getId();
		} if (this.pojoGrupo.getLlave2() != null && this.pojoGrupo.getLlave2().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave2());
			this.permiteBorrar = this.pojoGrupo.getLlave2().getId();
		} if (this.pojoGrupo.getLlave3() != null && this.pojoGrupo.getLlave3().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave3());
			this.permiteBorrar = this.pojoGrupo.getLlave3().getId();
		} if (this.pojoGrupo.getLlave4() != null && this.pojoGrupo.getLlave4().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave4());
			this.permiteBorrar = this.pojoGrupo.getLlave4().getId();
		} if (this.pojoGrupo.getLlave5() != null && this.pojoGrupo.getLlave5().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave5());
			this.permiteBorrar = this.pojoGrupo.getLlave5().getId();
		} if (this.pojoGrupo.getLlave6() != null && this.pojoGrupo.getLlave6().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave6());
			this.permiteBorrar = this.pojoGrupo.getLlave6().getId();
		} if (this.pojoGrupo.getLlave7() != null && this.pojoGrupo.getLlave7().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave7());
			this.permiteBorrar = this.pojoGrupo.getLlave7().getId();
		} if (this.pojoGrupo.getLlave8() != null && this.pojoGrupo.getLlave8().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave8());
			this.permiteBorrar = this.pojoGrupo.getLlave8().getId();
		} if (this.pojoGrupo.getLlave9() != null && this.pojoGrupo.getLlave9().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave9());
			this.permiteBorrar = this.pojoGrupo.getLlave9().getId();
		} if (this.pojoGrupo.getLlave10() != null && this.pojoGrupo.getLlave10().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave10());
			this.permiteBorrar = this.pojoGrupo.getLlave10().getId();
		} if (this.pojoGrupo.getLlave11() != null && this.pojoGrupo.getLlave11().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave11());
			this.permiteBorrar = this.pojoGrupo.getLlave11().getId();
		} if (this.pojoGrupo.getLlave12() != null && this.pojoGrupo.getLlave12().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave12());
			this.permiteBorrar = this.pojoGrupo.getLlave12().getId();
		} if (this.pojoGrupo.getLlave13() != null && this.pojoGrupo.getLlave13().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave13());
			this.permiteBorrar = this.pojoGrupo.getLlave13().getId();
		} if (this.pojoGrupo.getLlave14() != null && this.pojoGrupo.getLlave14().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave14());
			this.permiteBorrar = this.pojoGrupo.getLlave14().getId();
		} if (this.pojoGrupo.getLlave15() != null && this.pojoGrupo.getLlave15().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave15());
			this.permiteBorrar = this.pojoGrupo.getLlave15().getId();
		} if (this.pojoGrupo.getLlave16() != null && this.pojoGrupo.getLlave16().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave16());
			this.permiteBorrar = this.pojoGrupo.getLlave16().getId();
		} if (this.pojoGrupo.getLlave17() != null && this.pojoGrupo.getLlave17().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave17());
			this.permiteBorrar = this.pojoGrupo.getLlave17().getId();
		} if (this.pojoGrupo.getLlave18() != null && this.pojoGrupo.getLlave18().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave18());
			this.permiteBorrar = this.pojoGrupo.getLlave18().getId();
		} if (this.pojoGrupo.getLlave19() != null && this.pojoGrupo.getLlave19().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave19());
			this.permiteBorrar = this.pojoGrupo.getLlave19().getId();
		} if (this.pojoGrupo.getLlave20() != null && this.pojoGrupo.getLlave20().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave20());
			this.permiteBorrar = this.pojoGrupo.getLlave20().getId();
		} if (this.pojoGrupo.getLlave21() != null && this.pojoGrupo.getLlave21().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave21());
			this.permiteBorrar = this.pojoGrupo.getLlave21().getId();
		} if (this.pojoGrupo.getLlave22() != null && this.pojoGrupo.getLlave22().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave22());
			this.permiteBorrar = this.pojoGrupo.getLlave22().getId();
		} if (this.pojoGrupo.getLlave23() != null && this.pojoGrupo.getLlave23().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave23());
			this.permiteBorrar = this.pojoGrupo.getLlave23().getId();
		} if (this.pojoGrupo.getLlave24() != null && this.pojoGrupo.getLlave24().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave24());
			this.permiteBorrar = this.pojoGrupo.getLlave24().getId();
		} if (this.pojoGrupo.getLlave25() != null && this.pojoGrupo.getLlave25().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave25());
			this.permiteBorrar = this.pojoGrupo.getLlave25().getId();
		} if (this.pojoGrupo.getLlave26() != null && this.pojoGrupo.getLlave26().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave26());
			this.permiteBorrar = this.pojoGrupo.getLlave26().getId();
		} if (this.pojoGrupo.getLlave27() != null && this.pojoGrupo.getLlave27().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave27());
			this.permiteBorrar = this.pojoGrupo.getLlave27().getId();
		} if (this.pojoGrupo.getLlave28() != null && this.pojoGrupo.getLlave28().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave28());
			this.permiteBorrar = this.pojoGrupo.getLlave28().getId();
		} if (this.pojoGrupo.getLlave29() != null && this.pojoGrupo.getLlave29().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave29());
			this.permiteBorrar = this.pojoGrupo.getLlave29().getId();
		} if (this.pojoGrupo.getLlave30() != null && this.pojoGrupo.getLlave30().getId() > 0L) {
			this.listGrupoLlaves.add(this.pojoGrupo.getLlave30());		
			this.permiteBorrar = this.pojoGrupo.getLlave30().getId();
		} 
	}
	
	public void actualizarGrupoLlavesFromLista() {
		this.pojoGrupo.setLlave1(null);
		this.pojoGrupo.setLlave2(null);
		this.pojoGrupo.setLlave3(null);
		this.pojoGrupo.setLlave4(null);
		this.pojoGrupo.setLlave5(null);
		this.pojoGrupo.setLlave6(null);
		this.pojoGrupo.setLlave7(null);
		this.pojoGrupo.setLlave8(null);
		this.pojoGrupo.setLlave9(null);
		this.pojoGrupo.setLlave10(null);
		this.pojoGrupo.setLlave11(null);
		this.pojoGrupo.setLlave12(null);
		this.pojoGrupo.setLlave13(null);
		this.pojoGrupo.setLlave14(null);
		this.pojoGrupo.setLlave15(null);
		this.pojoGrupo.setLlave16(null);
		this.pojoGrupo.setLlave17(null);
		this.pojoGrupo.setLlave18(null);
		this.pojoGrupo.setLlave19(null);
		this.pojoGrupo.setLlave20(null);
		this.pojoGrupo.setLlave21(null);
		this.pojoGrupo.setLlave22(null);
		this.pojoGrupo.setLlave23(null);
		this.pojoGrupo.setLlave24(null);
		this.pojoGrupo.setLlave25(null);
		this.pojoGrupo.setLlave26(null);
		this.pojoGrupo.setLlave27(null);
		this.pojoGrupo.setLlave28(null);
		this.pojoGrupo.setLlave29(null);
		this.pojoGrupo.setLlave30(null);
		
		int index = 0;
		for (Llaves var : this.listGrupoLlaves) {
			index = var.getPosicion(); //index++;
			if(index ==  1) { this.pojoGrupo.setLlave1(var);  continue; }
			if(index ==  2) { this.pojoGrupo.setLlave2(var);  continue; }
			if(index ==  3) { this.pojoGrupo.setLlave3(var);  continue; }
			if(index ==  4) { this.pojoGrupo.setLlave4(var);  continue; }
			if(index ==  5) { this.pojoGrupo.setLlave5(var);  continue; }
			if(index ==  6) { this.pojoGrupo.setLlave6(var);  continue; }
			if(index ==  7) { this.pojoGrupo.setLlave7(var);  continue; }
			if(index ==  8) { this.pojoGrupo.setLlave8(var);  continue; }
			if(index ==  9) { this.pojoGrupo.setLlave9(var);  continue; }
			if(index == 10) { this.pojoGrupo.setLlave10(var); continue; }
			if(index == 11) { this.pojoGrupo.setLlave11(var); continue; }
			if(index == 12) { this.pojoGrupo.setLlave12(var); continue; }
			if(index == 13) { this.pojoGrupo.setLlave13(var); continue; }
			if(index == 14) { this.pojoGrupo.setLlave14(var); continue; }
			if(index == 15) { this.pojoGrupo.setLlave15(var); continue; }
			if(index == 16) { this.pojoGrupo.setLlave16(var); continue; }
			if(index == 17) { this.pojoGrupo.setLlave17(var); continue; }
			if(index == 18) { this.pojoGrupo.setLlave18(var); continue; }
			if(index == 19) { this.pojoGrupo.setLlave19(var); continue; }
			if(index == 20) { this.pojoGrupo.setLlave20(var); continue; }
			if(index == 21) { this.pojoGrupo.setLlave21(var); continue; }
			if(index == 22) { this.pojoGrupo.setLlave22(var); continue; }
			if(index == 23) { this.pojoGrupo.setLlave23(var); continue; }
			if(index == 24) { this.pojoGrupo.setLlave24(var); continue; }
			if(index == 25) { this.pojoGrupo.setLlave25(var); continue; }
			if(index == 26) { this.pojoGrupo.setLlave26(var); continue; }
			if(index == 27) { this.pojoGrupo.setLlave27(var); continue; }
			if(index == 28) { this.pojoGrupo.setLlave28(var); continue; }
			if(index == 29) { this.pojoGrupo.setLlave29(var); continue; }
			if(index == 30) { this.pojoGrupo.setLlave30(var); continue; }
		}
	}
	
	// ------------------------------------------------------------------------
	// Llaves Valores
	// ------------------------------------------------------------------------
	
	public void verLlavesValores() {
		try {
			control();
			this.pojoGrupo = this.ifzGrupos.findById(this.idGrupo);
			if (this.pojoGrupo == null || this.pojoGrupo.getId() == null || this.pojoGrupo.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar el Grupo seleccionado");
				return;
			}

			this.numPaginaGrupoLlavesValores = 1;
			this.listLlavesValores = this.ifzGruposCuentas.findByProperty("idGrupo", this.pojoGrupo, 0);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar los valores previamente asignados al Grupo", e);
		} finally {
			if (this.listLlavesValores == null)
				this.listLlavesValores = new ArrayList<GruposCuentas>();
		}
	}
	
	public void nuevoLlaveValor() {
		try {
			control();
			this.pojoLlaveValor = new GruposCuentas();
			this.pojoLlaveValor.setIdGrupo(this.pojoGrupo);
		} catch (Exception e) {
			control("Error en GruposAction.nuevoGrupoCuentas", e);
		}
	}
	
	public void salvarLlaveValor() {
		int index = -1;
		
		try {
			control();
			if (this.pojoLlaveValor != null) {
				index = this.listLlavesValores.indexOf(this.pojoLlaveValor);
				this.pojoLlaveValor.setModificadoPor(this.usuarioId);
				this.pojoLlaveValor.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoLlaveValor.getId() == null || this.pojoLlaveValor.getId() <= 0L) {
					this.pojoLlaveValor.setCreadoPor(this.usuarioId);
					this.pojoLlaveValor.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoLlaveValor.setId(this.ifzGruposCuentas.save(this.pojoLlaveValor));
				} else {
					// Actualizamos en la BD
					this.ifzGruposCuentas.update(this.pojoLlaveValor);
				}

				// Lo agregamos a la lista
				if (index >= 0 && index <= this.listLlavesValores.size())
					this.listLlavesValores.set(index, this.pojoLlaveValor);
				else
					this.listLlavesValores.add(this.pojoLlaveValor);
			}
		} catch (Exception e) {
			control("Error en GruposAction.salvarLlaveValor", e);
		}
	}
	
	public void borrarLlaveValor() {
		try {
			control();
			if (this.pojoLlaveValorBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoLlaveValorBorrar.getId() != null && this.pojoLlaveValorBorrar.getId() > 0L)
					this.ifzGruposCuentas.delete(this.pojoLlaveValorBorrar.getId());
				
				// Borramos del listado
				this.listLlavesValores.remove(this.pojoLlaveValorBorrar);
				this.pojoLlaveValorBorrar = null;
			}
		} catch (Exception e) {
			control("Error en GruposAction.borrarLlaveValor", e);
		}
	}
	
	// ------------------------------------------------------------------------
	// Busqueda DINAMICA
	// ------------------------------------------------------------------------
	
	public void nuevaBusquedaDinamica() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
		String llaveColumn = params.get("llaveColumn");
		
		if ("1".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave1().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave1().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave1().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave1().getCampoDescripcion();
		} else if ("2".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave2().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave2().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave2().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave2().getCampoDescripcion();
		} else if ("3".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave3().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave3().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave3().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave3().getCampoDescripcion();
		} else if ("4".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave4().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave4().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave4().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave4().getCampoDescripcion();
		} else if ("5".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave5().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave5().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave5().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave5().getCampoDescripcion();
		} else if ("6".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave6().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave6().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave6().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave6().getCampoDescripcion();
		} else if ("7".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave7().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave7().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave7().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave7().getCampoDescripcion();
		} else if ("8".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave8().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave8().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave8().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave8().getCampoDescripcion();
		} else if ("9".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave9().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave9().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave9().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave9().getCampoDescripcion();
		} else if ("10".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave10().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave10().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave10().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave10().getCampoDescripcion();
		} else if ("11".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave11().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave11().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave11().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave11().getCampoDescripcion();
		} else if ("12".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave12().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave12().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave12().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave12().getCampoDescripcion();
		} else if ("13".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave13().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave13().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave13().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave13().getCampoDescripcion();
		} else if ("14".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave14().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave14().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave14().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave14().getCampoDescripcion();
		} else if ("15".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave15().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave15().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave15().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave15().getCampoDescripcion();
		} else if ("16".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave16().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave16().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave16().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave16().getCampoDescripcion();
		} else if ("17".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave17().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave17().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave17().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave17().getCampoDescripcion();
		} else if ("18".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave18().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave18().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave18().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave18().getCampoDescripcion();
		} else if ("19".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave19().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave19().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave19().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave19().getCampoDescripcion();
		} else if ("20".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave20().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave20().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave20().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave20().getCampoDescripcion();
		} else if ("21".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave21().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave21().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave21().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave21().getCampoDescripcion();
		} else if ("22".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave22().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave22().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave22().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave22().getCampoDescripcion();
		} else if ("23".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave23().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave23().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave23().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave23().getCampoDescripcion();
		} else if ("24".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave24().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave24().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave24().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave24().getCampoDescripcion();
		} else if ("25".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave25().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave25().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave25().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave25().getCampoDescripcion();
		} else if ("26".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave26().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave26().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave26().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave26().getCampoDescripcion();
		} else if ("27".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave27().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave27().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave27().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave27().getCampoDescripcion();
		} else if ("28".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave28().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave28().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave28().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave28().getCampoDescripcion();
		} else if ("29".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave29().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave29().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave29().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave29().getCampoDescripcion();
		} else if ("30".equals(llaveColumn)) {
			this.llaveNumero = Integer.valueOf(llaveColumn);
			this.llaveTipo = this.pojoGrupo.getLlave30().getTipo(); 
			this.llaveEjecucion = this.pojoGrupo.getLlave30().getValorTipo();
			this.llaveCampoId = this.pojoGrupo.getLlave30().getCampoId();
			this.llaveCampoDescripcion = this.pojoGrupo.getLlave30().getCampoDescripcion();
		} else {
			this.llaveNumero = 0;
			this.llaveTipo = ""; 
			this.llaveEjecucion = "";
			return;
		}
		
		if (this.listValoresDinamicos == null)
			listValoresDinamicos = new ArrayList<SelectItem>();
		this.listValoresDinamicos.clear();

		this.campoBusquedaDinamica = this.tiposBusquedaDinamica.get(0).getValue().toString();
		this.valorBusquedaDinamica = "";
		this.numPaginaDinamica = 1;
	}
	
	public void busquedaDinamica() {
		LinkedHashMap<Object, String> listaValores = null;
		EjecutaBean exec = null;
		String[] parts = null;
		char[] arrayChar = null;
		String pEjb = "";
		String pMetodo = "";
		String pClasesParametros = "";
		String pValoresParametros = "";
		int veces = 0;
		
		try {
			control();
			exec = new EjecutaBean(this.loginManager.getInfoSesion());
			if (this.llaveCampoId != null && !"".equals(this.llaveCampoId))
				exec.setCampoId(this.llaveCampoId);
			if (this.llaveCampoDescripcion != null && !"".equals(this.llaveCampoDescripcion))
				exec.setCampoDescripcion(this.llaveCampoDescripcion);
			
			if (this.listValoresDinamicos == null)
				listValoresDinamicos = new ArrayList<SelectItem>();
			this.listValoresDinamicos.clear();
			
			if ("B".equals(this.llaveTipo)) {
				// Ejecutamos BEAN dinamico
				parts = this.llaveEjecucion.split("-");
				pEjb = parts[0];
				pMetodo = parts[1];
				
				if (parts.length > 2)
					pClasesParametros = parts[2];
				if (parts.length > 3)
					pValoresParametros = parts[3];
				
				if (! "".equals(pClasesParametros) && ! "".equals(pValoresParametros) && pClasesParametros.trim().split(",").length == pValoresParametros.trim().split(",").length) {
					if (pValoresParametros.contains("?")) {
						arrayChar = pValoresParametros.toCharArray();
						for (int i = 0; i< arrayChar.length; i++)
							veces += (arrayChar[i] == '?') ? 1 : 0;
					}
					
					if (veces > 0) {
						parts = pValoresParametros.split(",");
						for (int i = 0; i < parts.length; i++) {
							if (parts[i].trim().contains("?")) {
								if (veces == 1) {
									if ("".equals(this.valorBusquedaDinamica))
										this.valorBusquedaDinamica = " ";
									pValoresParametros = pValoresParametros.replace(parts[i].trim(), this.valorBusquedaDinamica);
									break;
								} else {
									// do nothing
								}
							}
						}
					}
				}
				
				// Mandamos ejecutar el bean
				log.info("EJB: " + pEjb + " Metodo: " + pMetodo + " Parametros: " + pClasesParametros + " Valores: " + pValoresParametros);
				listaValores = exec.ejecutaBean(pEjb, pMetodo, pClasesParametros, pValoresParametros);
			} else if ("S".equals(this.llaveTipo)) {
				// Ejecutamos SQL dinamico
				listaValores = exec.ejecutaQuery(this.llaveEjecucion);
			}
			
			if (listaValores != null && ! listaValores.isEmpty()) {
				for (Entry<Object, String> e : listaValores.entrySet()) {
					if (pMetodo.contains("busquedaDinamica")) {
						this.listValoresDinamicos.add(new SelectItem(e.getKey(), e.getValue()));
						continue;
					}
					
					// Comprobar filtro de informacion
					if (this.valorBusquedaDinamica != null && !"".equals(this.valorBusquedaDinamica)) {
						if ("id".equals(this.campoBusquedaDinamica) && e.getKey().toString().contains(this.valorBusquedaDinamica))
							this.listValoresDinamicos.add(new SelectItem(e.getKey(), e.getValue()));
						
						if ("descripcion".equals(this.campoBusquedaDinamica) && e.getValue().toLowerCase().contains(this.valorBusquedaDinamica.toLowerCase()))
							this.listValoresDinamicos.add(new SelectItem(e.getKey(), e.getValue()));
					} else {
						this.listValoresDinamicos.add(new SelectItem(e.getKey(), e.getValue()));
					}
				}
			}
			
			if (this.listValoresDinamicos.isEmpty()) {
				control(2, "Busqueda dinamica vacia");
				return;
			}
		} catch (Exception e) {
			control("Error en GruposAction.busquedaDinamica", e);
		}
	}
	
	public void seleccionarValorDinamico() {
		try {
			control();
			if (this.pojoValorDinamico != null) {
				if (this.llaveNumero == 1)  	 this.pojoLlaveValor.setValorLlave1((long)  this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 2)  this.pojoLlaveValor.setValorLlave2((long)  this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 3)  this.pojoLlaveValor.setValorLlave3((long)  this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 4)  this.pojoLlaveValor.setValorLlave4((long)  this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 5)  this.pojoLlaveValor.setValorLlave5((long)  this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 6)  this.pojoLlaveValor.setValorLlave6((long)  this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 7)  this.pojoLlaveValor.setValorLlave7((long)  this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 8)  this.pojoLlaveValor.setValorLlave8((long)  this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 9)  this.pojoLlaveValor.setValorLlave9((long)  this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 10) this.pojoLlaveValor.setValorLlave10((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 11) this.pojoLlaveValor.setValorLlave11((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 12) this.pojoLlaveValor.setValorLlave12((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 13) this.pojoLlaveValor.setValorLlave13((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 14) this.pojoLlaveValor.setValorLlave14((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 15) this.pojoLlaveValor.setValorLlave15((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 16) this.pojoLlaveValor.setValorLlave16((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 17) this.pojoLlaveValor.setValorLlave17((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 18) this.pojoLlaveValor.setValorLlave18((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 19) this.pojoLlaveValor.setValorLlave19((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 20) this.pojoLlaveValor.setValorLlave20((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 21) this.pojoLlaveValor.setValorLlave21((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 22) this.pojoLlaveValor.setValorLlave22((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 23) this.pojoLlaveValor.setValorLlave23((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 24) this.pojoLlaveValor.setValorLlave24((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 25) this.pojoLlaveValor.setValorLlave25((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 26) this.pojoLlaveValor.setValorLlave26((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 27) this.pojoLlaveValor.setValorLlave27((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 28) this.pojoLlaveValor.setValorLlave28((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 29) this.pojoLlaveValor.setValorLlave29((long) this.pojoValorDinamico.getValue());
				else if (this.llaveNumero == 30) this.pojoLlaveValor.setValorLlave30((long) this.pojoValorDinamico.getValue());
				
				//this.pojoLlaveValor.addValorLlaveDescripcion((long) this.getPojoValorDinamico().getValue(), this.getPojoValorDinamico().getDescription());
				if (this.valorLlaveDescripcion == null)
					this.valorLlaveDescripcion = new HashMap<Integer, String>();
				this.valorLlaveDescripcion.put(this.llaveNumero, this.getPojoValorDinamico().getLabel());
				this.pojoValorDinamico = null;
			}
		} catch (Exception e) {
			control("Error en GruposAction.seleccionarLlaveValor", e);
		}
	}
	
	public void quitarValorDinamico() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = null;
		String llaveColumn = "";

		params = fc.getExternalContext().getRequestParameterMap();
		llaveColumn = params.get("llaveColumn");
		
		if ("1".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave1(null);
		else if ("2".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave2(null);
		else if ("3".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave3(null);
		else if ("4".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave4(null);
		else if ("5".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave5(null);
		else if ("6".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave6(null);
		else if ("7".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave7(null);
		else if ("8".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave8(null);
		else if ("9".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave9(null);
		else if ("10".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave10(null);
		else if ("11".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave11(null);
		else if ("12".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave12(null);
		else if ("13".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave13(null);
		else if ("14".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave14(null);
		else if ("15".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave15(null);
		else if ("16".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave16(null);
		else if ("17".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave17(null);
		else if ("18".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave18(null);
		else if ("19".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave19(null);
		else if ("20".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave20(null);
		else if ("21".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave21(null);
		else if ("22".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave22(null);
		else if ("23".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave23(null);
		else if ("24".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave24(null);
		else if ("25".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave25(null);
		else if ("26".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave26(null);
		else if ("27".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave27(null);
		else if ("28".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave28(null);
		else if ("29".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave29(null);
		else if ("30".equals(llaveColumn)) 
			this.pojoLlaveValor.setValorLlave30(null);
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -----------------------------------------------------------------------------------------------------------------------------
	
	public double getGrupoId() {
		if (this.pojoGrupo != null && this.pojoGrupo.getId() != null && this.pojoGrupo.getId() > 0L)
			return this.pojoGrupo.getId();
		return 0;
	}
	
	public void setGrupoId(double value) {}

	public String getTituloLlave1() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave1() != null && this.pojoGrupo.getLlave1().getId() != null && this.pojoGrupo.getLlave1().getId() > 0L)
			return this.pojoGrupo.getLlave1().getDescripcion();
		return "";
	}
	
	public void setTituloLlave1(String value) {}
	
	public String getTituloLlave2() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave2() != null && this.pojoGrupo.getLlave2().getId() != null && this.pojoGrupo.getLlave2().getId() > 0L)
			return this.pojoGrupo.getLlave2().getDescripcion();
		return "";
	}
	
	public void setTituloLlave2(String value) {}
	
	public String getTituloLlave3() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave3() != null && this.pojoGrupo.getLlave3().getId() != null && this.pojoGrupo.getLlave3().getId() > 0L)
			return this.pojoGrupo.getLlave3().getDescripcion();
		return "";
	}
	
	public void setTituloLlave3(String value) {}
	
	public String getTituloLlave4() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave4() != null && this.pojoGrupo.getLlave4().getId() != null && this.pojoGrupo.getLlave4().getId() > 0L)
			return this.pojoGrupo.getLlave4().getDescripcion();
		return "";
	}
	
	public void setTituloLlave4(String value) {}
	
	public String getTituloLlave5() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave5() != null && this.pojoGrupo.getLlave5().getId() != null && this.pojoGrupo.getLlave5().getId() > 0L)
			return this.pojoGrupo.getLlave5().getDescripcion();
		return "";
	}
	
	public void setTituloLlave5(String value) {}
	
	public String getTituloLlave6() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave6() != null && this.pojoGrupo.getLlave6().getId() != null && this.pojoGrupo.getLlave6().getId() > 0L)
			return this.pojoGrupo.getLlave6().getDescripcion();
		return "";
	}
	
	public void setTituloLlave6(String value) {}
	
	public String getTituloLlave7() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave7() != null && this.pojoGrupo.getLlave7().getId() != null && this.pojoGrupo.getLlave7().getId() > 0L)
			return this.pojoGrupo.getLlave7().getDescripcion();
		return "";
	}
	
	public void setTituloLlave7(String value) {}
	
	public String getTituloLlave8() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave8() != null && this.pojoGrupo.getLlave8().getId() != null && this.pojoGrupo.getLlave8().getId() > 0L)
			return this.pojoGrupo.getLlave8().getDescripcion();
		return "";
	}
	
	public void setTituloLlave8(String value) {}
	
	public String getTituloLlave9() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave9() != null && this.pojoGrupo.getLlave9().getId() != null && this.pojoGrupo.getLlave9().getId() > 0L)
			return this.pojoGrupo.getLlave9().getDescripcion();
		return "";
	}
	
	public void setTituloLlave9(String value) {}
	
	public String getTituloLlave10() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave10() != null && this.pojoGrupo.getLlave10().getId() != null && this.pojoGrupo.getLlave10().getId() > 0L)
			return this.pojoGrupo.getLlave10().getDescripcion();
		return "";
	}
	
	public void setTituloLlave10(String value) {}
	
	public String getTituloLlave11() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave11() != null && this.pojoGrupo.getLlave11().getId() != null && this.pojoGrupo.getLlave11().getId() > 0L)
			return this.pojoGrupo.getLlave11().getDescripcion();
		return "";
	}
	
	public void setTituloLlave11(String value) {}
	
	public String getTituloLlave12() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave12() != null && this.pojoGrupo.getLlave12().getId() != null && this.pojoGrupo.getLlave12().getId() > 0L)
			return this.pojoGrupo.getLlave12().getDescripcion();
		return "";
	}
	
	public void setTituloLlave12(String value) {}
	
	public String getTituloLlave13() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave13() != null && this.pojoGrupo.getLlave13().getId() != null && this.pojoGrupo.getLlave13().getId() > 0L)
			return this.pojoGrupo.getLlave13().getDescripcion();
		return "";
	}
	
	public void setTituloLlave13(String value) {}
	
	public String getTituloLlave14() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave14() != null && this.pojoGrupo.getLlave14().getId() != null && this.pojoGrupo.getLlave14().getId() > 0L)
			return this.pojoGrupo.getLlave14().getDescripcion();
		return "";
	}
	
	public void setTituloLlave14(String value) {}
	
	public String getTituloLlave15() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave15() != null && this.pojoGrupo.getLlave15().getId() != null && this.pojoGrupo.getLlave15().getId() > 0L)
			return this.pojoGrupo.getLlave15().getDescripcion();
		return "";
	}
	
	public void setTituloLlave15(String value) {}
	
	public String getTituloLlave16() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave16() != null && this.pojoGrupo.getLlave16().getId() != null && this.pojoGrupo.getLlave16().getId() > 0L)
			return this.pojoGrupo.getLlave16().getDescripcion();
		return "";
	}
	
	public void setTituloLlave16(String value) {}
	
	public String getTituloLlave17() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave17() != null && this.pojoGrupo.getLlave17().getId() != null && this.pojoGrupo.getLlave17().getId() > 0L)
			return this.pojoGrupo.getLlave17().getDescripcion();
		return "";
	}
	
	public void setTituloLlave17(String value) {}
	
	public String getTituloLlave18() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave18() != null && this.pojoGrupo.getLlave18().getId() != null && this.pojoGrupo.getLlave18().getId() > 0L)
			return this.pojoGrupo.getLlave18().getDescripcion();
		return "";
	}
	
	public void setTituloLlave18(String value) {}
	
	public String getTituloLlave19() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave19() != null && this.pojoGrupo.getLlave19().getId() != null && this.pojoGrupo.getLlave19().getId() > 0L)
			return this.pojoGrupo.getLlave19().getDescripcion();
		return "";
	}
	
	public void setTituloLlave19(String value) {}
	
	public String getTituloLlave20() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave20() != null && this.pojoGrupo.getLlave20().getId() != null && this.pojoGrupo.getLlave20().getId() > 0L)
			return this.pojoGrupo.getLlave20().getDescripcion();
		return "";
	}
	
	public void setTituloLlave20(String value) {}
	
	public String getTituloLlave21() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave21() != null && this.pojoGrupo.getLlave21().getId() != null && this.pojoGrupo.getLlave21().getId() > 0L)
			return this.pojoGrupo.getLlave21().getDescripcion();
		return "";
	}
	
	public void setTituloLlave21(String value) {}
	
	public String getTituloLlave22() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave22() != null && this.pojoGrupo.getLlave22().getId() != null && this.pojoGrupo.getLlave22().getId() > 0L)
			return this.pojoGrupo.getLlave22().getDescripcion();
		return "";
	}
	
	public void setTituloLlave22(String value) {}
	
	public String getTituloLlave23() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave23() != null && this.pojoGrupo.getLlave23().getId() != null && this.pojoGrupo.getLlave23().getId() > 0L)
			return this.pojoGrupo.getLlave23().getDescripcion();
		return "";
	}
	
	public void setTituloLlave23(String value) {}
	
	public String getTituloLlave24() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave24() != null && this.pojoGrupo.getLlave24().getId() != null && this.pojoGrupo.getLlave24().getId() > 0L)
			return this.pojoGrupo.getLlave24().getDescripcion();
		return "";
	}
	
	public void setTituloLlave24(String value) {}
	
	public String getTituloLlave25() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave25() != null && this.pojoGrupo.getLlave25().getId() != null && this.pojoGrupo.getLlave25().getId() > 0L)
			return this.pojoGrupo.getLlave25().getDescripcion();
		return "";
	}
	
	public void setTituloLlave25(String value) {}
	
	public String getTituloLlave26() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave26() != null && this.pojoGrupo.getLlave26().getId() != null && this.pojoGrupo.getLlave26().getId() > 0L)
			return this.pojoGrupo.getLlave26().getDescripcion();
		return "";
	}
	
	public void setTituloLlave26(String value) {}
	
	public String getTituloLlave27() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave27() != null && this.pojoGrupo.getLlave27().getId() != null && this.pojoGrupo.getLlave27().getId() > 0L)
			return this.pojoGrupo.getLlave27().getDescripcion();
		return "";
	}
	
	public void setTituloLlave27(String value) {}
	
	public String getTituloLlave28() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave28() != null && this.pojoGrupo.getLlave28().getId() != null && this.pojoGrupo.getLlave28().getId() > 0L)
			return this.pojoGrupo.getLlave28().getDescripcion();
		return "";
	}
	
	public void setTituloLlave28(String value) {}
	
	public String getTituloLlave29() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave29() != null && this.pojoGrupo.getLlave29().getId() != null && this.pojoGrupo.getLlave29().getId() > 0L)
			return this.pojoGrupo.getLlave29().getDescripcion();
		return "";
	}
	
	public void setTituloLlave29(String value) {}
	
	public String getTituloLlave30() {
		if (this.pojoGrupo != null && this.pojoGrupo.getLlave30() != null && this.pojoGrupo.getLlave30().getId() != null && this.pojoGrupo.getLlave30().getId() > 0L)
			return this.pojoGrupo.getLlave30().getDescripcion();
		return "";
	}
	
	public void setTituloLlave30(String value) {}

	public String getGrupoDescripcion() {
		if (this.pojoGrupo != null)
			return this.pojoGrupo.getId() + " - " + this.pojoGrupo.getDescripcion();
		return "";
	}
	
	public void setGrupoDescripcion(String grupoDescripcion) {}
	
	public List<Grupos> getListGrupos() {
		return listGrupos;
	}

	public void setListGrupos(List<Grupos> listGrupos) {
		this.listGrupos = listGrupos;
	}

	public long getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(long idGrupo) {
		this.idGrupo = idGrupo;
	}

	public Grupos getPojoGrupo() {
		return pojoGrupo;
	}

	public void setPojoGrupo(Grupos pojoGrupo) {
		this.pojoGrupo = pojoGrupo;
		/*try {
			if (this.pojoGrupo == null)
				this.pojoGrupo = new Grupos();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.pojoGrupo, pojoGrupo);
		} catch (Exception e) {
			this.pojoGrupo = pojoGrupo;
		}*/
	}

	/*public Grupos getPojoGrupoBorrar() {
		return pojoGrupoBorrar;
	}

	public void setPojoGrupoBorrar(Grupos pojoGrupoBorrar) {
		this.pojoGrupoBorrar = pojoGrupoBorrar;
	}*/

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

	public boolean isOperacion() {
		return operacion;
	}

	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public long getPermiteBorrar() {
		return permiteBorrar;
	}

	public void setPermiteBorrar(long permiteBorrar) {
		this.permiteBorrar = permiteBorrar;
	}

	public List<SelectItem> getTiposBusquedaLlaves() {
		return tiposBusquedaLlaves;
	}
	
	public void setTiposBusquedaLlaves(List<SelectItem> tiposBusquedaLlaves) {
		this.tiposBusquedaLlaves = tiposBusquedaLlaves;
	}
	
	public String getCampoBusquedaLlaves() {
		return campoBusquedaLlaves;
	}
	
	public void setCampoBusquedaLlaves(String campoBusquedaLlaves) {
		this.campoBusquedaLlaves = campoBusquedaLlaves;
	}
	
	public String getValorBusquedaLlaves() {
		return valorBusquedaLlaves;
	}
	
	public void setValorBusquedaLlaves(String valorBusquedaLlaves) {
		this.valorBusquedaLlaves = valorBusquedaLlaves;
	}
	
	public int getNumPaginaLlaves() {
		return numPaginaLlaves;
	}
	
	public void setNumPaginaLlaves(int numPaginaLlaves) {
		this.numPaginaLlaves = numPaginaLlaves;
	}

	public List<Llaves> getListLlaves() {
		return listLlaves;
	}

	public void setListLlaves(List<Llaves> listLlaves) {
		this.listLlaves = listLlaves;
	}

	public Llaves getPojoLlave() {
		return pojoLlave;
	}

	public void setPojoLlave(Llaves pojoLlave) {
		this.pojoLlave = pojoLlave;
	}

	public List<Llaves> getListLlavesGrupo() {
		return listGrupoLlaves;
	}

	public void setListLlavesGrupo(List<Llaves> listLlavesGrupo) {
		this.listGrupoLlaves = listLlavesGrupo;
	}

	public Llaves getPojoLlaveBorrar() {
		return pojoLlaveBorrar;
	}

	public void setPojoLlaveBorrar(Llaves pojoLlaveBorrar) {
		this.pojoLlaveBorrar = pojoLlaveBorrar;
	}
	
	public int getNumPaginaGrupoLlaves() {
		return numPaginaGrupoLlaves;
	}
	
	public void setNumPaginaGrupoLlaves(int numPaginaGrupoLlaves) {
		this.numPaginaGrupoLlaves = numPaginaGrupoLlaves;
	}

	public boolean isPermiteBorrarGrupo() {
		return permiteBorrarGrupo;
	}

	public void setPermiteBorrarGrupo(boolean permiteBorrarGrupo) {
		this.permiteBorrarGrupo = permiteBorrarGrupo;
	}
	
	public GruposCuentas getPojoLlaveValor() {
		return pojoLlaveValor;
	}

	public void setPojoLlaveValor(GruposCuentas pojoLlaveValor) {
		this.pojoLlaveValor = pojoLlaveValor;
	}

	public GruposCuentas getPojoLlaveValorBorrar() {
		return pojoLlaveValorBorrar;
	}

	public void setPojoLlaveValorBorrar(GruposCuentas pojoLlaveValorBorrar) {
		this.pojoLlaveValorBorrar = pojoLlaveValorBorrar;
	}

	public List<GruposCuentas> getListLlavesValores() {
		return listLlavesValores;
	}

	public void setListLlavesValores(List<GruposCuentas> listLlavesValores) {
		this.listLlavesValores = listLlavesValores;
	}

	public List<SelectItem> getListValoresDinamicos() {
		return listValoresDinamicos;
	}

	public void setListValoresDinamicos(List<SelectItem> listValoresDinamicos) {
		this.listValoresDinamicos = listValoresDinamicos;
	}

	public SelectItem getPojoValorDinamico() {
		return pojoValorDinamico;
	}

	public void setPojoValorDinamico(SelectItem pojoValorDinamico) {
		this.pojoValorDinamico = pojoValorDinamico;
	}
	
	public List<SelectItem> getTiposBusquedaDinamica() {
		return tiposBusquedaDinamica;
	}

	public void setTiposBusquedaDinamica(List<SelectItem> tiposBusquedaDinamica) {
		this.tiposBusquedaDinamica = tiposBusquedaDinamica;
	}

	public String getCampoBusquedaDinamica() {
		return campoBusquedaDinamica;
	}

	public void setCampoBusquedaDinamica(String campoBusquedaDinamica) {
		this.campoBusquedaDinamica = campoBusquedaDinamica;
	}

	public String getValorBusquedaDinamica() {
		return valorBusquedaDinamica;
	}

	public void setValorBusquedaDinamica(String valorBusquedaDinamica) {
		this.valorBusquedaDinamica = valorBusquedaDinamica;
	}

	public int getNumPaginaDinamica() {
		return numPaginaDinamica;
	}

	public void setNumPaginaDinamica(int numPaginaDinamica) {
		this.numPaginaDinamica = numPaginaDinamica;
	}

	public int getNumPaginaGrupoLlavesValores() {
		return numPaginaGrupoLlavesValores;
	}

	public void setNumPaginaGrupoLlavesValores(int numPaginaGrupoLlavesValores) {
		this.numPaginaGrupoLlavesValores = numPaginaGrupoLlavesValores;
	}
	
	public boolean isDebugging() { 
		if (this.paramsRequest.containsKey("DEBUG")) 
			return true;
		return this.isDebug;
	}
	
	public void setDebugging(boolean value) { }
}

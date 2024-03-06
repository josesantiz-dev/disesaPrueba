package net.giro.contabilidad;

import java.io.Serializable;
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
	private List<Llaves> listLlaves;
	private List<Llaves> listLlavesGrupo;
	private List<GruposCuentas> listLlavesValores;
	// POJO's
	private Grupos pojoGrupo;
	private Grupos pojoGrupoBorrar;
	private Llaves pojoLlave;
	private Llaves pojoLlaveBorrar;
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
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
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

			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------
			
			this.ctx = new InitialContext();
			this.ifzGrupos = (GruposRem) this.ctx.lookup("ejb:/Logica_Contabilidad//GruposFac!net.giro.contabilidad.logica.GruposRem");
			this.ifzLlaves = (LlavesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//LlavesFac!net.giro.contabilidad.logica.LlavesRem");
			this.ifzAsigGpo = (AsignacionGruposRem) this.ctx.lookup("ejb:/Logica_Contabilidad//AsignacionGruposFac!net.giro.contabilidad.logica.AsignacionGruposRem");
			this.ifzGruposCuentas = (GruposCuentasRem) this.ctx.lookup("ejb:/Logica_Contabilidad//GruposCuentasFac!net.giro.contabilidad.logica.GruposCuentasRem");
						
			// Listas
			this.listGrupos= new ArrayList<Grupos>();
			
			// POJO's
			this.pojoGrupo = new Grupos();
			this.pojoGrupoBorrar = new Grupos();
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
			
			// Busqueda Llaves
			this.tiposBusquedaDinamica = new ArrayList<SelectItem>();
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
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = tiposBusqueda.get(0).getValue().toString();

			this.ifzGrupos.orderBy("descripcion");
			this.listGrupos = this.ifzGrupos.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listGrupos.isEmpty()) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda vacia");
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.buscar", e);
		}
	}
	
	public void nuevo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoGrupo = new Grupos();
			this.pojoGrupoBorrar = null;
			
			if (this.listLlavesGrupo == null)
				this.listLlavesGrupo = new ArrayList<Llaves>();
			this.listLlavesGrupo.clear();
			
			if (this.listLlavesValores == null)
				this.listLlavesValores = new ArrayList<GruposCuentas>();
			this.listLlavesValores.clear();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.nuevo", e);
		}
	}
	
	public void editar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			generarListaLlavesFromGrupo();
			this.permiteBorrarGrupo = this.comprobarUsoGrupo(this.pojoGrupo.getId());
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.editar", e);
		}
	}
	
	public void guardar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoGrupo != null) {
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
				}
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.guardar", e);
		}
	}
	
	public void borrar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoGrupoBorrar != null && this.pojoGrupoBorrar.getId() > 0L) {
				if (! this.comprobarUsoGrupo(this.pojoGrupoBorrar.getId())) {
					this.operacion = true;
					this.mensaje = "ERROR";
					this.tipoMensaje = 5;
					return;
				}
				
				// Borramos de la bd
				this.ifzGrupos.delete(this.pojoGrupoBorrar.getId());
				
				// Borramos de la lista
				this.listGrupos.remove(this.pojoGrupoBorrar);
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.borrar", e);
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

	// ------------------------------------------------------------------------
	// Llaves
	// ------------------------------------------------------------------------
	
	public void nuevaBusquedaLlaves() {
		if (this.listLlaves == null)
			this.listLlaves = new ArrayList<Llaves>();
		this.listLlaves.clear();
		
		this.pojoLlave = null;
		
		this.campoBusquedaLlaves = tiposBusquedaLlaves.get(0).getValue().toString();
		this.valorBusquedaLlaves = "";
		numPaginaLlaves = 1;
	}
	
	public void buscarLlaves() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaLlaves))
				this.campoBusquedaLlaves = tiposBusquedaLlaves.get(0).getValue().toString();
			
			this.listLlaves = this.ifzLlaves.findLikeProperty(this.campoBusquedaLlaves, this.valorBusquedaLlaves, 120);
			
			if (this.listLlaves == null || this.listLlaves.isEmpty()) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda vacia");
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.buscarLlaves", e);
		}
	}
	
	public void seleccionarLlave() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			agregarValorLlave();
			nuevaBusquedaLlaves();
		} catch (Exception e) {
			if ("EXISTE_LLAVE".equals(e.getMessage())) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 4;
				return;
			}
			
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.seleccionarLlave", e);
		}
	}

	public void agregarValorLlave() throws Exception {
		if (this.pojoLlave == null || this.pojoLlave.getId() == null || this.pojoLlave.getId() <= 0L) {
			this.permiteBorrar = 0;
			return;
		}

		boolean agregado = false;
		if(! agregado && (this.pojoGrupo.getLlave1() == null || this.pojoGrupo.getLlave1().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(1, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave1(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave2() == null || this.pojoGrupo.getLlave2().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(2, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave2(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave3() == null || this.pojoGrupo.getLlave3().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(3, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave3(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave4() == null || this.pojoGrupo.getLlave4().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(4, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave4(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave5() == null || this.pojoGrupo.getLlave5().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(5, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave5(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave6() == null || this.pojoGrupo.getLlave6().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(6, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave6(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave7() == null || this.pojoGrupo.getLlave7().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(7, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave7(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave8() == null || this.pojoGrupo.getLlave8().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(8, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave8(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave9() == null || this.pojoGrupo.getLlave9().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(9, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave9(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave10() == null || this.pojoGrupo.getLlave10().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(10, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave10(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave11() == null || this.pojoGrupo.getLlave11().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(11, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave11(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave12() == null || this.pojoGrupo.getLlave12().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(12, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave12(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave13() == null || this.pojoGrupo.getLlave13().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(13, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave13(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave14() == null || this.pojoGrupo.getLlave14().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(14, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave14(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave15() == null || this.pojoGrupo.getLlave15().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(15, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave15(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave16() == null || this.pojoGrupo.getLlave16().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(16, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave16(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave17() == null || this.pojoGrupo.getLlave17().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(17, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave17(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave18() == null || this.pojoGrupo.getLlave18().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(18, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave18(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave19() == null || this.pojoGrupo.getLlave19().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(19, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave19(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave20() == null || this.pojoGrupo.getLlave20().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(20, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave20(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave21() == null || this.pojoGrupo.getLlave21().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(21, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave21(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave22() == null || this.pojoGrupo.getLlave22().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(22, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave22(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave23() == null || this.pojoGrupo.getLlave23().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(23, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave23(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave24() == null || this.pojoGrupo.getLlave24().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(24, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave24(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave25() == null || this.pojoGrupo.getLlave25().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(25, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave25(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave26() == null || this.pojoGrupo.getLlave26().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(26, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave26(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave27() == null || this.pojoGrupo.getLlave27().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(27, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave27(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave28() == null || this.pojoGrupo.getLlave28().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(28, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave28(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave29() == null || this.pojoGrupo.getLlave29().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(29, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave29(this.pojoLlave);
				agregado = true;
			}
		}

		if(! agregado && (this.pojoGrupo.getLlave30() == null || this.pojoGrupo.getLlave30().equals(0L))) {
			if (comprobarLlave(this.pojoLlave.getId())) { // comprobarLlaveOld(30, this.pojoLlave.getId())) {
				this.pojoGrupo.setLlave30(this.pojoLlave);
				agregado = true;
			}
		}
		
		generarListaLlavesFromGrupo();
	}
	
	public boolean comprobarLlave(long llaveId) throws Exception {
		if (this.permitirLlavesRepetidas)
			return true;

		// Ciclo para determinar si la llave que queremos guardar ya existe en alguna columna anterior
		boolean existe = false;
		for (Llaves var : this.listLlavesGrupo) {
			if (var.getId().equals(llaveId)) {
				existe = false;
				break;
			}
		}
		
		if (existe) throw new Exception("EXISTE_LLAVE");
		
		return true;
	}
	
	public void borrarLlaveGrupo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (! this.permiteBorrarGrupo) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 5;
				return;
			}
			
			if (this.pojoLlaveBorrar != null && this.pojoLlaveBorrar.getId() > 0L) {
				this.listLlavesGrupo.remove(this.pojoLlaveBorrar);
				
				actualizarGrupoLlavesFromLista();
				generarListaLlavesFromGrupo();
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.borrar", e);
		}
	}
	
	public void generarListaLlavesFromGrupo() {
		if (this.listLlavesGrupo == null)
			this.listLlavesGrupo = new ArrayList<Llaves>();
		this.listLlavesGrupo.clear();
		
		if(this.pojoGrupo.getLlave1() != null && this.pojoGrupo.getLlave1().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave1());
			this.permiteBorrar = this.pojoGrupo.getLlave1().getId();
		} if(this.pojoGrupo.getLlave2() != null && this.pojoGrupo.getLlave2().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave2());
			this.permiteBorrar = this.pojoGrupo.getLlave2().getId();
		}  if(this.pojoGrupo.getLlave3() != null && this.pojoGrupo.getLlave3().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave3());
			this.permiteBorrar = this.pojoGrupo.getLlave3().getId();
		}  if(this.pojoGrupo.getLlave4() != null && this.pojoGrupo.getLlave4().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave4());
			this.permiteBorrar = this.pojoGrupo.getLlave4().getId();
		}  if(this.pojoGrupo.getLlave5() != null && this.pojoGrupo.getLlave5().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave5());
			this.permiteBorrar = this.pojoGrupo.getLlave5().getId();
		}  if(this.pojoGrupo.getLlave6() != null && this.pojoGrupo.getLlave6().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave6());
			this.permiteBorrar = this.pojoGrupo.getLlave6().getId();
		}  if(this.pojoGrupo.getLlave7() != null && this.pojoGrupo.getLlave7().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave7());
			this.permiteBorrar = this.pojoGrupo.getLlave7().getId();
		}  if(this.pojoGrupo.getLlave8() != null && this.pojoGrupo.getLlave8().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave8());
			this.permiteBorrar = this.pojoGrupo.getLlave8().getId();
		}  if(this.pojoGrupo.getLlave9() != null && this.pojoGrupo.getLlave9().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave9());
			this.permiteBorrar = this.pojoGrupo.getLlave9().getId();
		}  if(this.pojoGrupo.getLlave10() != null && this.pojoGrupo.getLlave10().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave10());
			this.permiteBorrar = this.pojoGrupo.getLlave10().getId();
		}  if(this.pojoGrupo.getLlave11() != null && this.pojoGrupo.getLlave11().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave11());
			this.permiteBorrar = this.pojoGrupo.getLlave11().getId();
		}  if(this.pojoGrupo.getLlave12() != null && this.pojoGrupo.getLlave12().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave12());
			this.permiteBorrar = this.pojoGrupo.getLlave12().getId();
		}  if(this.pojoGrupo.getLlave13() != null && this.pojoGrupo.getLlave13().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave13());
			this.permiteBorrar = this.pojoGrupo.getLlave13().getId();
		}  if(this.pojoGrupo.getLlave14() != null && this.pojoGrupo.getLlave14().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave14());
			this.permiteBorrar = this.pojoGrupo.getLlave14().getId();
		}  if(this.pojoGrupo.getLlave15() != null && this.pojoGrupo.getLlave15().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave15());
			this.permiteBorrar = this.pojoGrupo.getLlave15().getId();
		}  if(this.pojoGrupo.getLlave16() != null && this.pojoGrupo.getLlave16().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave16());
			this.permiteBorrar = this.pojoGrupo.getLlave16().getId();
		}  if(this.pojoGrupo.getLlave17() != null && this.pojoGrupo.getLlave17().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave17());
			this.permiteBorrar = this.pojoGrupo.getLlave17().getId();
		}  if(this.pojoGrupo.getLlave18() != null && this.pojoGrupo.getLlave18().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave18());
			this.permiteBorrar = this.pojoGrupo.getLlave18().getId();
		}  if(this.pojoGrupo.getLlave19() != null && this.pojoGrupo.getLlave19().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave19());
			this.permiteBorrar = this.pojoGrupo.getLlave19().getId();
		}  if(this.pojoGrupo.getLlave20() != null && this.pojoGrupo.getLlave20().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave20());
			this.permiteBorrar = this.pojoGrupo.getLlave20().getId();
		}  if(this.pojoGrupo.getLlave21() != null && this.pojoGrupo.getLlave21().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave21());
			this.permiteBorrar = this.pojoGrupo.getLlave21().getId();
		}  if(this.pojoGrupo.getLlave22() != null && this.pojoGrupo.getLlave22().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave22());
			this.permiteBorrar = this.pojoGrupo.getLlave22().getId();
		}  if(this.pojoGrupo.getLlave23() != null && this.pojoGrupo.getLlave23().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave23());
			this.permiteBorrar = this.pojoGrupo.getLlave23().getId();
		}  if(this.pojoGrupo.getLlave24() != null && this.pojoGrupo.getLlave24().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave24());
			this.permiteBorrar = this.pojoGrupo.getLlave24().getId();
		}  if(this.pojoGrupo.getLlave25() != null && this.pojoGrupo.getLlave25().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave25());
			this.permiteBorrar = this.pojoGrupo.getLlave25().getId();
		}  if(this.pojoGrupo.getLlave26() != null && this.pojoGrupo.getLlave26().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave26());
			this.permiteBorrar = this.pojoGrupo.getLlave26().getId();
		}  if(this.pojoGrupo.getLlave27() != null && this.pojoGrupo.getLlave27().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave27());
			this.permiteBorrar = this.pojoGrupo.getLlave27().getId();
		}  if(this.pojoGrupo.getLlave28() != null && this.pojoGrupo.getLlave28().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave28());
			this.permiteBorrar = this.pojoGrupo.getLlave28().getId();
		}  if(this.pojoGrupo.getLlave29() != null && this.pojoGrupo.getLlave29().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave29());
			this.permiteBorrar = this.pojoGrupo.getLlave29().getId();
		}  if(this.pojoGrupo.getLlave30() != null && this.pojoGrupo.getLlave30().getId() > 0L) {
			this.listLlavesGrupo.add(this.pojoGrupo.getLlave30());		
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
		for (Llaves var : this.listLlavesGrupo) {
			index++;
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
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
						
			if (this.listLlavesValores == null)
				this.listLlavesValores = new ArrayList<GruposCuentas>();
			this.listLlavesValores.clear();
			
			this.listLlavesValores = this.ifzGruposCuentas.findByProperty("idGrupo", this.pojoGrupo, 0);
			this.numPaginaGrupoLlavesValores = 1;
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.editar", e);
		}
	}
	
	public void nuevoLlaveValor() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			this.pojoLlaveValor = new GruposCuentas();
			this.pojoLlaveValor.setIdGrupo(this.pojoGrupo);
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.nuevoGrupoCuentas", e);
		}
	}
	
	public void salvarLlaveValor() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			int index = -1;
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
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.salvarLlaveValor", e);
		}
	}
	
	public void borrarLlaveValor() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoLlaveValorBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoLlaveValorBorrar.getId() != null && this.pojoLlaveValorBorrar.getId() > 0L)
					this.ifzGruposCuentas.delete(this.pojoLlaveValorBorrar.getId());
				
				// Borramos del listado
				this.listLlavesValores.remove(this.pojoLlaveValorBorrar);
				this.pojoLlaveValorBorrar = null;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.borrarLlaveValor", e);
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
		EjecutaBean exec = new EjecutaBean();
		String[] parts = null;
		char[] arrayChar = null;
		String pEjb = "";
		String pMetodo = "";
		String pClasesParametros = "";
		String pValoresParametros = "";
		int veces = 0;
		
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
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
				for(Entry<Object, String> e : listaValores.entrySet()) {
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
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda dinamica vacia");
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.busquedaDinamica", e);
		}
	}
	
	public void seleccionarValorDinamico() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
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
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en GruposAction.seleccionarLlaveValor", e);
		}
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -----------------------------------------------------------------------------------------------------------------------------
	
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

	public Grupos getPojoGrupo() {
		return pojoGrupo;
	}

	public void setPojoGrupo(Grupos pojoGrupo) {
		try {
			if (this.pojoGrupo == null)
				this.pojoGrupo = new Grupos();
			BeanUtils.copyProperties(this.pojoGrupo, pojoGrupo);
		} catch (Exception e) {
			this.pojoGrupo = pojoGrupo;
		}
	}

	public Grupos getPojoGrupoBorrar() {
		return pojoGrupoBorrar;
	}

	public void setPojoGrupoBorrar(Grupos pojoGrupoBorrar) {
		this.pojoGrupoBorrar = pojoGrupoBorrar;
	}

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
		return listLlavesGrupo;
	}

	public void setListLlavesGrupo(List<Llaves> listLlavesGrupo) {
		this.listLlavesGrupo = listLlavesGrupo;
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
}

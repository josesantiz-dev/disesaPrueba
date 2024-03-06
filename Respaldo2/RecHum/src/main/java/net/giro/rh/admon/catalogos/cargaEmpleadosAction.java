package net.giro.rh.admon.catalogos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;

import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.beans.Categoria;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoRow;
import net.giro.rh.admon.beans.Puesto;
import net.giro.rh.admon.beans.PuestoCategoria;
import net.giro.rh.admon.logica.AreaRem;
import net.giro.rh.admon.logica.CategoriaRem;
import net.giro.rh.admon.logica.EmpleadoContratoRem;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.rh.admon.logica.PuestoCategoriaRem;
import net.giro.rh.admon.logica.PuestoRem;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.logica.BancosRem;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;

@ViewScoped
@ManagedBean(name="cargaEmp")
public class cargaEmpleadosAction implements Serializable, Runnable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(cargaEmpleadosAction.class);
	private PropertyResourceBundle empleadosProperties;
	private InitialContext ctx;
	
	// EJB
	private PersonaRem ifzPersonas;
	private EmpleadoRem ifzEmpleados;
	private EmpleadoContratoRem ifzContratos;
	private ConGrupoValoresRem ifzConGrupoValores;
	private ConValoresRem ifzConValores;
	// Sucursales
	private SucursalesRem ifzSucursales;
	HashMap<Long, String> listSucursales;
	// Bancos
	private BancosRem ifzBancos;
	HashMap<Long, String> listBancos;
	// Areas
	private AreaRem ifzAreas;
	HashMap<Long, String> listAreas;
	// Puestos
	private PuestoRem ifzPuestos;
	HashMap<Long, String> listPuestos;
	// Categorias
	private CategoriaRem ifzCategorias;
	HashMap<Long, String> listCategorias;
	// PuestoCategorias
	private PuestoCategoriaRem ifzPuestoCategorias;
	HashMap<Long, String> listPuestoCategorias;
	// Periodicidad
	private ConGrupoValores pojoGpoPeriodicidad;
	HashMap<Long, String> listPeriodicidad;
	// Formas de Pago
	private ConGrupoValores pojoGpoFormasPago;
	HashMap<Long, String> listFormasDePago;
	// Control
	private LoginManager loginManager;
	private long usuarioId;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	// CARGA
	private HashMap<String, String> layoutEmpleados;
	private HashMap<String, String> listHorarios;
	private List<String> listRequeridos;
	private int maxCargas;
	private String fileName;
	private byte[] fileSrc; 
	private List<String> bitacora;
	private boolean procesando;
	private LinkedHashMap<String, byte[]> listLayout;
	private LinkedHashMap<String, String> listErrores;
	private boolean escapeForInputMesssage;
	private boolean tieneRegistrosIncompletos;
	private boolean continuarConRegistrosIncompletos;
	// Listas
	private List<Persona> listPersonas;
	private List<Empleado> listEmpleados;
	private List<EmpleadoContrato> listContratos;
	List<EmpleadoRow> listEmpleadoRow;
	List<EmpleadoRow> listErroresRow;
	// Resultados
	private String resRegistros;
	private int sumArchivos;
	private int sumPersonas;
	private int sumEmpleados;
	private int sumContratos;
	private int registrosTotales;
	
	
	public cargaEmpleadosAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		Map<String, String> params = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			// Recopilacion de parametros de vista: GET
			this.paramsRequest = new HashMap<String, String>();
			params = fc.getExternalContext().getRequestParameterMap();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// Propiedades de inicio de sesion
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId  = (long) this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			// Propiedades y carga de layout para empleados
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{cargaEmpleados}", PropertyResourceBundle.class);
			this.empleadosProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			
			// Numero de cargas permitidas
			this.maxCargas = 1;
			if (this.isDebug && this.paramsRequest.containsKey("MULTI_CARGA")) {
				if (this.paramsRequest.get("MULTI_CARGA") == null || "".equals(this.paramsRequest.get("MULTI_CARGA")))
					this.maxCargas = 0;
				else
					this.maxCargas = Integer.valueOf(this.paramsRequest.get("MULTI_CARGA"));
			}
            
			// EJB's
			this.ctx = new InitialContext();
			this.ifzPersonas = (PersonaRem) 				this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzEmpleados = (EmpleadoRem) 				this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzContratos = (EmpleadoContratoRem) 		this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			this.ifzAreas = (AreaRem) 						this.ctx.lookup("ejb:/Logica_RecHum//AreaFac!net.giro.rh.admon.logica.AreaRem");
			this.ifzPuestos = (PuestoRem) 					this.ctx.lookup("ejb:/Logica_RecHum//PuestoFac!net.giro.rh.admon.logica.PuestoRem");
			this.ifzCategorias = (CategoriaRem) 			this.ctx.lookup("ejb:/Logica_RecHum//CategoriaFac!net.giro.rh.admon.logica.CategoriaRem");
			this.ifzPuestoCategorias = (PuestoCategoriaRem) this.ctx.lookup("ejb:/Logica_RecHum//PuestoCategoriaFac!net.giro.rh.admon.logica.PuestoCategoriaRem");
			this.ifzBancos = (BancosRem)					this.ctx.lookup("ejb:/Logica_TYG//BancosFac!net.giro.tyg.logica.BancosRem");
			this.ifzSucursales = (SucursalesRem) 			this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzConGrupoValores = (ConGrupoValoresRem) 	this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem"); 
			this.ifzConValores = (ConValoresRem) 			this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem"); 
			
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzContratos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAreas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPuestos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCategorias.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPuestoCategorias.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzBancos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSucursales.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConGrupoValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			
			// GRUPO FORMAS DE PAGO
			this.pojoGpoFormasPago = this.ifzConGrupoValores.findByName("SYS_FORMA_PAGO_EMPLEADO");
			/*listGrupos = this.ifzConGrupoValores.findLikeClaveNombre("SYS_FORMA_PAGO_EMPLEADO", 0);
			if (listGrupos != null && ! listGrupos.isEmpty())
				this.pojoGpoFormasPago = listGrupos.get(0);*/

			// GRUPO PERIODICIDAD
			this.pojoGpoPeriodicidad = this.ifzConGrupoValores.findByName("SYS_TIPO_PERIODO");
			/*listGrupos = this.ifzConGrupoValores.findLikeClaveNombre("SYS_TIPO_PERIODO", 0);
			if (listGrupos != null && ! listGrupos.isEmpty())
				this.pojoGpoPeriodicidad = listGrupos.get(0);*/
			
			this.escapeForInputMesssage = true;
			this.listPersonas = new ArrayList<Persona>();
			this.listEmpleados = new ArrayList<Empleado>();
			this.listContratos = new ArrayList<EmpleadoContrato>();
		} catch (Exception e) {
			log.error("Error en constructor RecHum.CargaEmpleadosAction", e);
			this.ctx = null;
		}
	}
	
	
	public void nuevo() {
		try {
			control();
			
			//Bitacora
			iniciarBitacora();
			// Carga de archivos
			this.fileName = "";
			this.fileSrc = null;
		} catch (Exception e) {
			control(true);
			log.error("Ocurrio un error en RecHum.CargaEmpleadosAction.nuevo", e);
		}
	}
	
	public void carga(FileUploadEvent event) {
		try {
			nuevo();
			
			// Recuperamos el nuevo archivo
			this.fileName = event.getUploadedFile().getName();
			this.fileSrc = event.getUploadedFile().getData();
			if (this.listLayout == null)
				this.listLayout = new LinkedHashMap<String, byte[]>();
			this.listLayout.put(this.fileName, this.fileSrc);
		} catch (Exception e) {
			control(true);
			log.error("Ocurrio un error en RecHum.CargaEmpleadosAction.carga", e);
		}
	}
	
	public void procesar() {
		try {
			control();
			iniciarBitacora();
			
			 // Validamos archivo
			if (this.listLayout == null || this.listLayout.isEmpty()) {
				control("Falta indicar el archivo");
				return;
			}

			this.procesando = true;
			this.tieneRegistrosIncompletos = false;
			bitacora("Preparando... ", false); 
			
			// Lanzamos hilo con el procesamiento del archivo o archivos.
			// Esto desocupa el hilo principal y podemos usarlo para reportar el avance.
			new Thread(this).start();
		} catch (Exception e) {  
			control(true);
			log.error("Ocurrio un error en RecHum.CargaEmpleadosAction.procesar", e);
		}
	}
	
	@Override
	public void run() {
		boolean multiCarga = false;
		
		try {
			control();
			this.sumArchivos = this.listLayout.size();
			multiCarga = (this.sumArchivos > 1);
			
			// Ciclo para procesar todos los archivos que se hallan subido
			for (Entry<String, byte[]> entry : this.listLayout.entrySet()) {
				this.fileName = entry.getKey();
				this.fileSrc = entry.getValue();
				
				log.info("---> Procesando... " + this.fileName + " <---");
				if (multiCarga) bitacora("---> Procesando... " + this.fileName + " <---", false); 
				this.doProcesar();
				this.fileName = (multiCarga) ? "" : this.fileName;
				this.fileSrc = (multiCarga) ? null : this.fileSrc;
			}
			
			if (this.sumContratos > 0) {
				this.resRegistros = "<br>Se generaron " + this.sumContratos + " contratos";
				if (multiCarga)
					this.resRegistros += " para los " + this.sumArchivos + " archivos indicados";
			}
			
			if (this.tieneRegistrosIncompletos)
				control("Carga realizada satisfactoriamente.<br>Sin embargo se encontraron " + this.listErroresRow.size() + " con informacion incompleta.");
			this.tieneRegistrosIncompletos = false;
			
			log.info("---------------------------------------------------------");
			log.info(String.format("%1$6s Registros leidos", this.registrosTotales));
			log.info("---------------------------------------------------------");
			log.info(String.format("%1$6s Registros sin procesar", this.listErroresRow.size()));
			log.info(String.format("%1$6s Personas  registradas", this.sumPersonas));
			log.info(String.format("%1$6s Empleados registrados", this.sumEmpleados));
			log.info(String.format("%1$6s Contratos registrados", this.sumContratos));
			log.info("---------------------------------------------------------");
			log.info(this.listErrores.toString());
			log.info("---------------------------------------------------------");
			this.listLayout.clear();
			this.listErrores.clear();
		} catch (Exception e) {
			control(true);
			log.error("Ocurrio un error en RecHum.CargaEmpleadosAction.run", e);
		} finally {
			this.procesando = false;
		}
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	private void doProcesar() {
		List<Empleado> listExiste = null;
		List<EmpleadoContrato> listExisteContrato = null;
		HashMap<String, Object> params = new HashMap<String, Object>();
		Persona pojoPersona = null;
		Empleado pojoEmpleado = null;
		EmpleadoContrato pojoContrato = null;
		int colCount = 1;
		int noCodeCount = 0;
		String codeVal = "";
		String prefix = "";
		String claves = "";
		
		try {
			control();
			if (this.listPersonas == null)
				this.listPersonas = new ArrayList<Persona>();
			this.listPersonas.clear();
			if (this.listEmpleados == null)
				this.listEmpleados = new ArrayList<Empleado>();
			this.listEmpleados.clear();
			if (this.listContratos == null)
				this.listContratos = new ArrayList<EmpleadoContrato>();
			this.listContratos.clear();
			bitacora("Preparando... OK", true); 
			
			// Comprobando archivo
			bitacora("Comprobando archivo... ", false); 
			if(this.fileSrc == null || this.fileSrc.length <= 0) {
				bitacora("Comprobando archivo... ERROR", true); 
				bitacora("Carga fallida...", false);  
				control(true);
				return;
			}
			bitacora("Comprobando archivo... OK", true); 
			
			
			// Estructura y datos
			bitacora("Comprobando estructura... ", false);
			Respuesta result = this.ifzEmpleados.procesarLayout(this.fileSrc, this.layoutEmpleados, this.listRequeridos);
			if (result.getErrores().getCodigoError() != 0L) {
				log.error("Error al realizar PROCESO DE LECTURA DE ARCHIVO");
				bitacora("Comprobando estructura... ERROR", true); 
				bitacora("Carga fallida...", false);
				control(result.getErrores().getDescError());
				return;
			}
			
			// Recuperamos resultados
			this.listEmpleadoRow = (List<EmpleadoRow>) result.getBody().getValor("procesados");
			this.listErroresRow = (List<EmpleadoRow>) result.getBody().getValor("sin_procesar");
			this.listErrores = (LinkedHashMap<String, String>) result.getBody().getValor("mensajes");
			this.registrosTotales = (int) result.getBody().getValor("registros");
			
			// Validamos
			if (this.listErroresRow != null && this.listErroresRow.size() > 0) {
				this.escapeForInputMesssage = false;
				bitacora("Comprobando estructura... ERROR", true); 
				
				if (! this.continuarConRegistrosIncompletos) {
					bitacora("Carga fallida...", false);
					log.error("Error al realizar VALIDACION DE PRODUCTOS");
					
					if (this.listErroresRow.size() == 1) {
						this.mensaje = "El registro " + this.listErroresRow.get(0).getClave() + " no tiene la informacion completa.";
					} else {
						this.mensaje += "Existen registros que no tienen la informacion completa.";
						
						for (EmpleadoRow var : this.listErroresRow) {
							prefix = ((this.listErroresRow.indexOf(var) == 0) ? "" : ", ");
							if (colCount == 6) {
								prefix = ((this.listErroresRow.indexOf(var) == 0) ? "" : "<br>");
								colCount = 1;
							}
	
							claves += prefix + var.getClave();
							if (this.listErroresRow.indexOf(var) == 30) 
								claves += "|";
							colCount += 1;
						}
						
						if (this.listErroresRow.size() <= 30) 
							this.mensaje += "<br>Registros:<br>" + claves;
						else
							this.mensaje += "<br>Mas de 30 registros con informacion faltante.";
					}
	
					claves = this.mensaje.replace("<br>", ", ").replace("|", "");
					if (this.mensaje.contains("|")) this.mensaje = this.mensaje.substring(0, this.mensaje.indexOf("|"));
					log.error("--->" + claves);
					control(this.mensaje);
					return;
				} else {
					this.tieneRegistrosIncompletos = true;
					bitacora("Comprobando estructura... ERROR -> Continuando", true); 
				}
			} else {
				bitacora("Comprobando estructura... OK", true);
			}
			
			// PASO 1. Guardamos personas
			pojoEmpleado = null;
			pojoPersona = null;
			noCodeCount = 1;
			params.clear();
			bitacora("Guardando personas... ", false); 
			for (EmpleadoRow var : this.listEmpleadoRow) {
				codeVal = var.getClave();
				if (codeVal == null || "".equals(codeVal.trim())) {
					codeVal = String.format("NC-%1$05d", noCodeCount);
					noCodeCount += 1;
				}
				
				log.info("-----> Procesando persona " + codeVal);
				
				try {
					params.put("clave", var.getClave());
					params.put("nombre", generaNombre(var.getApellidoPaterno(), var.getApellidoMaterno(), var.getPrimerNombre(), var.getSegundoNombre()));
					params.put("estatus", 0);
					listExiste = this.ifzEmpleados.findByProperties(params);
					if (listExiste != null && ! listExiste.isEmpty()) {
						pojoEmpleado = listExiste.get(0);
						var.setIdEmpleado(pojoEmpleado.getId());
						var.setIdPersona(pojoEmpleado.getIdPersona());
						pojoPersona = getPersonaFromList(pojoEmpleado.getIdPersona());
						pojoPersona = comparaPersona(pojoPersona, var);
					} 
					
					if (pojoPersona == null) {
						log.info("-----> Procesando persona " + codeVal + " :: nuevo ");
						pojoPersona = getPersonaFromRow(pojoPersona, var);
						pojoPersona.setId(this.ifzPersonas.save(pojoPersona));
						var.setIdPersona(pojoPersona.getId());
						var.setIdEmpleado(0L);
						var.setNuevaPersona(true);
						
						// Añado a lista
						this.listPersonas.add(pojoPersona);
						this.sumPersonas += 1;
					} 
					
					pojoEmpleado = null;
					pojoPersona = null;
				} catch (Exception e) {
					this.listErrores.put(codeVal, "Error al guardar PERSONA " + codeVal + ": " + e.getMessage());
					log.error("Error al guardar PERSONA " + codeVal, e);
					bitacora("Guardando personas... ERROR", true); 
					bitacora("Carga fallida...", false);  
					control(true);
					return;
				}
			}
			bitacora("Guardando personas... OK", true); 
			
			// PASO 2. Generamos empleados
			pojoEmpleado = null;
			pojoPersona = null;
			noCodeCount = 1;
			bitacora("Generamos empleados... ", false); 
			for (EmpleadoRow var : this.listEmpleadoRow) {
				codeVal = var.getClave();
				if (codeVal == null || "".equals(codeVal.trim())) {
					codeVal = String.format("NC-%1$05d", noCodeCount);
					noCodeCount += 1;
				}
				
				log.info("-----> Procesando empleado " + codeVal);
				
				try {
					pojoPersona = getPersonaFromList(var.getIdPersona());
					
					if (var.getIdEmpleado() != null && var.getIdEmpleado() > 0L) {
						pojoEmpleado = getEmpleadoFromList(var.getIdEmpleado());
						pojoEmpleado = comparaEmpleado(pojoEmpleado, pojoPersona.getId(), var);
					} 
					
					if (pojoEmpleado == null) {
						log.info("-----> Procesando empleado " + codeVal + " :: nuevo ");
						pojoEmpleado = getEmpleadoFromRow(pojoEmpleado, pojoPersona.getId(), pojoPersona.getNombre(), var);
						pojoEmpleado.setIdEmpresa(1L);
						pojoEmpleado.setId(this.ifzEmpleados.save(pojoEmpleado));
						var.setIdEmpleado(pojoEmpleado.getId());
						var.setNuevoEmpleado(true);
						
						// Añadimos a la lista
						this.listEmpleados.add(pojoEmpleado);
						this.sumEmpleados += 1;
					} 
	
					pojoEmpleado = null;
					pojoPersona = null;
				} catch (Exception e) {
					this.listErrores.put(codeVal, "Error al guardar EMPLEADO " + codeVal + ": " + e.getMessage()); 
					log.error("Error al guardar EMPLEADO " + codeVal, e);
					bitacora("Generamos empleados... ERROR", true); 
					bitacora("Carga fallida...", false);  
					control(true);
					return;
				}
			}
			bitacora("Generamos empleados... OK", true); 
			
			// PASO 3. Generamos contratos
			pojoContrato = null;
			pojoEmpleado = null;
			pojoPersona = null;
			noCodeCount = 1;
			bitacora("Generamos contratos... ", false); 
			for (EmpleadoRow var : this.listEmpleadoRow) {
				codeVal = var.getClave();
				if (codeVal == null || "".equals(codeVal.trim())) {
					codeVal = String.format("NC-%1$05d", noCodeCount);
					noCodeCount += 1;
				}
				
				log.info("-----> Procesando contrato " + codeVal);
				
				try {
					pojoEmpleado = getEmpleadoFromList(var.getIdEmpleado());
					
					listExisteContrato = this.ifzContratos.contratoValido(var.getIdEmpleado());
					if (listExisteContrato != null && ! listExisteContrato.isEmpty()) {
						pojoContrato = listExisteContrato.get(0);
						var.setIdContrato(pojoContrato.getId());
						var.setIdEmpleado(pojoEmpleado.getId());
					}
					
					if (pojoContrato != null) {
						pojoContrato = getContratoFromList(var.getIdContrato());
						pojoContrato = comparaContrato(pojoContrato, var);
					}
					
					if (pojoContrato == null) {
						log.info("-----> Procesando contrato " + codeVal + " :: nuevo ");
						pojoContrato = getContratoFromRow(pojoContrato, var);
						pojoContrato.setId(this.ifzContratos.save(pojoContrato));
						var.setIdContrato(pojoContrato.getId());
						var.setNuevoContrato(true);
						
						// Añadimos a la lista
						this.listContratos.add(pojoContrato);
						this.sumContratos += 1;
						
						try {
							this.ifzContratos.cancelarContratosPrevios(pojoContrato.getIdEmpleado().getId(), pojoContrato.getId(), this.usuarioId);
						} catch (Exception re) {
							log.error("Ocurrio un problema al cancelar los Contratos Previos para el Empledo " + pojoContrato.getIdEmpleado(), re);
						}
					} 
					
					pojoContrato = null;
				} catch (Exception e) {
					this.listErrores.put(codeVal, "Error al guardar CONTRATO para el EMPLEADO " + codeVal + ": " + e.getMessage());
					log.error("Error al guardar CONTRATO para EMPLEADO " + codeVal, e);
					bitacora("Generamos contratos... ERROR", true); 
					bitacora("Carga fallida...", false);  
					control(true);
					return;
				}
			}
			bitacora("Generamos contratos... OK", true); 
		} catch (Exception e) {
			this.listErrores.put(this.fileName, e.getMessage()); 
			bitacora("Carga fallida...", false);  
			log.error("Ocurrio un error en RecHum.CargaEmpleadosAction.doProcesar", e);
			control(true);
			dropValues();
		} 
	}
	
	private long getIdBanco(String value) {
		List<Banco> listAux = null;
		String nombre = "";
		Long id = 0L;

		try {
			if (value == null || "".equals(value.trim()))
				return 0L;
			
			value = value.trim();
			if (this.listBancos == null)
				this.listBancos = new HashMap<Long, String>();
			
			if (this.listBancos.containsValue(value)) {
				for (Entry<Long, String> var : this.listBancos.entrySet()) {
					if (value.equals(var.getValue()))
						return var.getKey();
				}
			}
			
			listAux = this.ifzBancos.findByColumnName("nombreCorto", value);
			if (listAux != null && ! listAux.isEmpty()) {
				id = listAux.get(0).getId();
				nombre = listAux.get(0).getNombreCorto();
				
				id = (id == null || id <= 0L) ? 0L : id;
				nombre = (nombre == null || "".equals(nombre)) ? "" : nombre.trim();
				
				if (id > 0L && ! "".equals(nombre)) {
					this.listBancos.put(id, nombre);
					return id;
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.cargaEmpleadosAction.getIdBanco", e);
		}
		
		return 0L;
	}
	
	private long getIdSucursal(String value) {
		List<Sucursal> listAux = null;
		String nombre = "";
		Long id = 0L;
		
		try {
			if (value == null || "".equals(value.trim()))
				return 0L;
			
			value = value.trim();
			if (this.listSucursales == null)
				this.listSucursales = new HashMap<Long, String>();
			
			if (this.listSucursales.containsValue(value)) {
				for (Entry<Long, String> var : this.listSucursales.entrySet()) {
					if (value.equals(var.getValue()))
						return var.getKey();
				}
			}
			
			listAux = this.ifzSucursales.findLikeColumnName("sucursal", value);
			if (listAux != null && ! listAux.isEmpty()) {
				id = listAux.get(0).getId();
				nombre = listAux.get(0).getSucursal();
				
				id = (id == null || id <= 0L) ? 0L : id;
				nombre = (nombre == null || "".equals(nombre)) ? "" : nombre.trim();
				
				if (id > 0L && ! "".equals(nombre)) {
					this.listSucursales.put(id, nombre);
					return id;
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.cargaEmpleadosAction.getIdSucursal", e);
		}
		
		return 0L;
	}
	
	private long getIdArea(String value) {
		List<Area> listAux = null;
		String nombre = "";
		Long id = 0L;

		try {
			if (value == null || "".equals(value.trim()))
				return 0L;
			
			value = value.trim();
			if (this.listAreas == null)
				this.listAreas = new HashMap<Long, String>();
			
			if (this.listAreas.containsValue(value)) {
				for (Entry<Long, String> var : this.listAreas.entrySet()) {
					if (value.equals(var.getValue()))
						return var.getKey();
				}
			}
			
			listAux = this.ifzAreas.findByProperty("descripcion", value);
			if (listAux != null && ! listAux.isEmpty()) {
				id = listAux.get(0).getId();
				nombre = listAux.get(0).getDescripcion();
	
				id = (id == null || id <= 0L) ? 0L : id;
				nombre = (nombre == null || "".equals(nombre)) ? "" : nombre.trim();
				
				if (id > 0L && ! "".equals(nombre)) {
					this.listAreas.put(id, nombre);
					return id;
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.cargaEmpleadosAction.getIdArea", e);
		}
		
		return 0L;
	}
	
	private long getIdPuesto(String value) {
		List<Puesto> listAux = null;
		String nombre = "";
		Long id = 0L;

		try {
			if (value == null || "".equals(value.trim()))
				return 0L;
			
			value = value.trim();
			if (this.listPuestos == null)
				this.listPuestos = new HashMap<Long, String>();
			
			if (this.listPuestos.containsValue(value)) {
				for (Entry<Long, String> var : this.listPuestos.entrySet()) {
					if (value.equals(var.getValue()))
						return var.getKey();
				}
			}
			
			listAux = this.ifzPuestos.findByProperty("descripcion", value);
			if (listAux != null && ! listAux.isEmpty()) {
				id = listAux.get(0).getId();
				nombre = listAux.get(0).getDescripcion();
	
				id = (id == null || id <= 0L) ? 0L : id;
				nombre = (nombre == null || "".equals(nombre)) ? "" : nombre.trim();
				
				if (id > 0L && ! "".equals(nombre)) {
					this.listPuestos.put(id, nombre);
					return id;
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.cargaEmpleadosAction.getIdPuesto", e);
		}
		
		return 0L;
	}
	
	private long getIdCategoria(String value) {
		List<Categoria> listAux = null;
		String nombre = "";
		Long id = 0L;

		try {
			if (value == null || "".equals(value.trim()))
				return 0L;
			
			value = value.trim();
			if (this.listCategorias == null)
				this.listCategorias = new HashMap<Long, String>();
			
			if (this.listCategorias.containsValue(value)) {
				for (Entry<Long, String> var : this.listCategorias.entrySet()) {
					if (value.equals(var.getValue()))
						return var.getKey();
				}
			}
			
			listAux = this.ifzCategorias.findByProperty("descripcion", value);
			if (listAux != null && ! listAux.isEmpty()) {
				id = listAux.get(0).getId();
				nombre = listAux.get(0).getDescripcion();
	
				id = (id == null || id <= 0L) ? 0L : id;
				nombre = (nombre == null || "".equals(nombre)) ? "" : nombre.trim();
				
				if (id > 0L && ! "".equals(nombre)) {
					this.listCategorias.put(id, nombre);
					return id;
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.cargaEmpleadosAction.getIdCategoria", e);
		}
		
		return 0L;
	}
	
	private long getIdPuestoCategoria(Long puesto, Long categoria) {
		List<PuestoCategoria> listAux = null;
		String nombre = "";
		Long id = 0L;

		try {
			puesto = (puesto != null && puesto > 0L) ? puesto : 0L;
			categoria = (categoria != null && categoria > 0L) ? categoria : 0L;
			if (puesto <= 0L || categoria <= 0L)
				return 0L;
				
			if (this.listPuestoCategorias == null)
				this.listPuestoCategorias = new HashMap<Long, String>();

			nombre = puesto + "-" + categoria;
			if (this.listPuestoCategorias.containsValue(nombre)) {
				for (Entry<Long, String> var : this.listPuestoCategorias.entrySet()) {
					if (nombre.equals(var.getValue()))
						return var.getKey();
				}
			}
			
			listAux = this.ifzPuestoCategorias.findByPuestoCategoria(puesto, categoria);
			if (listAux != null && ! listAux.isEmpty()) {
				id = listAux.get(0).getId();
				nombre = puesto + "-" + categoria;

				id = (id == null || id <= 0L) ? 0L : id;
				nombre = (nombre == null || "".equals(nombre)) ? "" : nombre.trim();
				
				if (id > 0L && ! "".equals(nombre)) {
					this.listPuestoCategorias.put(id, nombre);
					return id;
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.cargaEmpleadosAction.getIdPuestoCategoria", e);
		}
		
		return 0L;
	}
	
	private long getIdPeriodicidad(String value) {
		List<ConValores> listAux = null;
		String nombre = "";
		Long id = 0L;

		try {
			if (value == null || "".equals(value.trim()))
				return 0L;
			
			value = value.trim();
			if (this.listPeriodicidad == null)
				this.listPeriodicidad = new HashMap<Long, String>();

			if (this.listPeriodicidad.containsValue(value)) {
				for (Entry<Long, String> var : this.listPeriodicidad.entrySet()) {
					if (nombre.equals(var.getValue()))
						return var.getKey();
				}
			}
			
			listAux = this.ifzConValores.buscaValorGrupo("valor", value, this.pojoGpoPeriodicidad); //.findByProperty("valor", nombre);
			if (listAux != null && ! listAux.isEmpty()) {
				id = listAux.get(0).getId();
				nombre = listAux.get(0).getValor();

				id = (id == null || id <= 0L) ? 0L : id;
				nombre = (nombre == null || "".equals(nombre)) ? "" : nombre.trim();
				
				if (id > 0L && ! "".equals(nombre)) {
					this.listPeriodicidad.put(id, nombre);
					return id;
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.cargaEmpleadosAction.getIdPuestoCategoria", e);
		}
		
		return 0L;
	}
	
	private long getIdFormaDePago(String value) {
		List<ConValores> listAux = null;
		String nombre = "";
		Long id = 0L;

		try {
			value = (value != null && ! "".equals(value)) ? value.trim() : "";
			if (value == "")
				return 0L;
			
			if (this.listFormasDePago == null)
				this.listFormasDePago = new HashMap<Long, String>();

			if (this.listFormasDePago.containsValue(value)) {
				for (Entry<Long, String> var : this.listFormasDePago.entrySet()) {
					if (nombre.equals(var.getValue()))
						return var.getKey();
				}
			}
			
			listAux = this.ifzConValores.buscaValorGrupo("valor", value, this.pojoGpoFormasPago); //.findByProperty("valor", nombre);
			if (listAux != null && ! listAux.isEmpty()) {
				id = listAux.get(0).getId();
				nombre = listAux.get(0).getValor();

				id = (id == null || id <= 0L) ? 0L : id;
				nombre = (nombre == null || "".equals(nombre)) ? "" : nombre.trim();
				
				if (id > 0L && ! "".equals(nombre)) {
					this.listFormasDePago.put(id, nombre);
					return id;
				}
			}
		} catch (Exception e) {
			log.error("Error en RecHum.cargaEmpleadosAction.getIdPuestoCategoria", e);
		}
		
		return 0L;
	}
	
	private Persona getPersonaFromList(Long id) {
		Persona pojo = null;
		
		for (Persona var : this.listPersonas) {
			if (id.longValue() == var.getId()) {
				pojo = var;
				break;
			}
		}
		
		if (pojo == null) {
			pojo = this.ifzPersonas.findById(id);
			if (pojo != null)
				this.listPersonas.add(pojo);
		}
		
		return pojo;
	}
	
	private Empleado getEmpleadoFromList(Long id) {
		Empleado pojo = null;
		
		for (Empleado var : this.listEmpleados) {
			if (id.longValue() == var.getId().longValue()) {
				pojo = var;
				break;
			}
		}
		
		if (pojo == null) {
			pojo = this.ifzEmpleados.findById(id);
			if (pojo != null)
				this.listEmpleados.add(pojo);
		}
		
		return pojo;
	}
	
	private EmpleadoContrato getContratoFromList(Long id) {
		EmpleadoContrato pojo = null;
		
		for (EmpleadoContrato var : this.listContratos) {
			if (id.longValue() == var.getId().longValue()) {
				pojo = var;
				break;
			}
		}
		
		if (pojo == null) {
			pojo = this.ifzContratos.findById(id);
			if (pojo != null)
				this.listContratos.add(pojo);
		}
		
		return pojo;
	}
	
	private void actualizaPersonaInList(Persona pojo) {
		if (pojo == null)
			return;
		
		for (Persona var : this.listPersonas) {
			if (pojo.getId() == var.getId()) {
				var = pojo;
				break;
			}
		}
	}
	
	private void actualizaEmpleadoInList(Empleado pojo) {
		if (pojo == null)
			return;
		
		for (Empleado var : this.listEmpleados) {
			if (pojo.getId() == var.getId()) {
				var = pojo;
				break;
			}
		}
	}
	
	private void actualizaContratoInList(EmpleadoContrato pojo) {		
		for (EmpleadoContrato var : this.listContratos) {
			if (pojo.getId() == var.getId()) {
				var = pojo;
				break;
			}
		}
	}
	
	private Persona comparaPersona(Persona pojoPersona, EmpleadoRow pojoRegistro) {
		String nombre = "";
		
		if (pojoPersona == null || pojoPersona.getId() <= 0L)
			return null;
		
		if (pojoRegistro == null)
			return null;
		
		// NOMBRE
		nombre = generaNombre(pojoRegistro.getApellidoPaterno(), pojoRegistro.getApellidoMaterno(), pojoRegistro.getPrimerNombre(), pojoRegistro.getSegundoNombre());
		if (! nombre.equals(pojoPersona.getNombre()))
			return null;
		
		// RFC
		if (pojoRegistro.getRfc() != null && ! "".equals(pojoRegistro.getRfc())) {
			if (pojoPersona.getRfc() != null && ! "".equals(pojoPersona.getRfc())) {
				if (! pojoPersona.getRfc().equals(pojoRegistro.getRfc()))
					return null;
			} 
		}
		
		// Actualizamos los datos de la persona con los del documento y lo actializamos en la lista
		pojoPersona = getPersonaFromRow(pojoPersona, pojoRegistro);
		actualizaPersonaInList(pojoPersona);
		
		return pojoPersona;
	}
	
	private Empleado comparaEmpleado(Empleado pojoEmpleado, Long idPersona, EmpleadoRow pojoRegistro) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		boolean actualizar = false;
		String nombre = "";
		
		if (pojoEmpleado == null || pojoEmpleado.getId() == null || pojoEmpleado.getId() <= 0L)
			return null;
		
		if (idPersona == null || idPersona <= 0L)
			return null;
		
		if (pojoRegistro == null)
			return null;
		
		// Campos determinantes para nuevo empleado
		if (! idPersona.equals(pojoEmpleado.getIdPersona()))
			return null;
		
		if (! pojoEmpleado.getNumeroSeguridadSocial().equals(pojoRegistro.getNss()))
			return null;
		
		// Campos actualizables
		nombre = generaNombre(pojoRegistro.getApellidoPaterno(), pojoRegistro.getApellidoMaterno(), pojoRegistro.getPrimerNombre(), pojoRegistro.getSegundoNombre());
		if (! nombre.equals(pojoEmpleado.getNombre())) {
			actualizar = true;
			pojoEmpleado.setNombre(nombre);
			pojoEmpleado.setPrimerApellido(pojoRegistro.getApellidoPaterno());
			pojoEmpleado.setSegundoApellido(pojoRegistro.getApellidoMaterno());
			pojoEmpleado.setPrimerNombre(pojoRegistro.getPrimerNombre());
			pojoEmpleado.setSegundoNombre(pojoRegistro.getSegundoNombre());
		}
		
		if (! pojoEmpleado.getClave().equals(pojoRegistro.getClave()))  {
			actualizar = true;
			pojoEmpleado.setClave(pojoRegistro.getClave());
		}
		
		if (! pojoEmpleado.getIdSucursal().equals(getIdSucursal(pojoRegistro.getSucursal()))) {
			actualizar = true;
			pojoEmpleado.setIdSucursal(getIdSucursal(pojoRegistro.getSucursal()));
		}
		
		if (! pojoEmpleado.getNombreSucursal().equals(pojoRegistro.getSucursal())) {
			actualizar = true;
			pojoEmpleado.setNombreSucursal(pojoRegistro.getSucursal());
		}
		
		if (! pojoEmpleado.getIdArea().equals(getIdArea(pojoRegistro.getArea()))) {
			actualizar = true;
			pojoEmpleado.setIdArea(getIdArea(pojoRegistro.getArea()));
		}
		
		if (! pojoEmpleado.getAreaDescripcion().equals(pojoRegistro.getArea())) {
			actualizar = true;
			pojoEmpleado.setAreaDescripcion(pojoRegistro.getArea());
		}
		
		if (! pojoEmpleado.getIdPuestoCategoria().equals(getIdPuestoCategoria(getIdPuesto(pojoRegistro.getPuesto()), getIdCategoria(pojoRegistro.getCategoria())))) {
			actualizar = true;
			pojoEmpleado.setIdPuestoCategoria(getIdPuestoCategoria(getIdPuesto(pojoRegistro.getPuesto()), getIdCategoria(pojoRegistro.getCategoria())));
		}
		
		if (! pojoEmpleado.getPuestoDescripcion().equals(pojoRegistro.getPuesto())) {
			actualizar = true;
			pojoEmpleado.setPuestoDescripcion(pojoRegistro.getPuesto());
		}
		
		if (! pojoEmpleado.getCategoriaDescripcion().equals(pojoRegistro.getCategoria())) {
			actualizar = true;
			pojoEmpleado.setCategoriaDescripcion(pojoRegistro.getCategoria());
		}
		
		if (! formatter.format(pojoEmpleado.getFechaIngreso()).equals(formatter.format(pojoRegistro.getFechaIngreso()))) {
			actualizar = true;
			pojoEmpleado.setFechaIngreso(pojoRegistro.getFechaIngreso());
		}

		// Actualizamos los datos del empleado con los del documento y lo refrescamos en la lista
		try {
			if (actualizar) {
				pojoEmpleado.setModificadoPor(this.usuarioId);
				pojoEmpleado.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzEmpleados.update(pojoEmpleado);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar actualizar el Empleado " + pojoEmpleado.getId());
		} finally {
			actualizaEmpleadoInList(pojoEmpleado);
		}
		
		return pojoEmpleado;
	}
	
	private EmpleadoContrato comparaContrato(EmpleadoContrato pojoContrato, EmpleadoRow pojoRegistro) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		boolean actualizar = false;
		BigDecimal val1 = BigDecimal.ZERO;
		BigDecimal val2 = BigDecimal.ZERO;
		
		val1.setScale(2);
		val2.setScale(2);
		
		if (pojoContrato == null || pojoContrato.getId() == null || pojoContrato.getId() <= 0L)
			return null;
		
		if (pojoRegistro == null)
			return null;

		// Campos determinantes para nuevo Contrato
		if (! pojoContrato.getIdEmpleado().equals(pojoRegistro.getIdEmpleado()))
			return null;
		
		if (pojoContrato.getDeterminado() != getValorTipoContrato(pojoRegistro.getContratoTipo()))
			return null;
		
		if (! formatter.format(pojoContrato.getFechaInicio()).equals(formatter.format(pojoRegistro.getFechaIngreso())))
			return null;
		
		if (! formatter.format(pojoContrato.getFechaFin()).equals(formatter.format(pojoRegistro.getFechaTermino())))
			return null;
		
		if (pojoContrato.getPeridiocidadPago() != (int) getIdPeriodicidad(pojoRegistro.getPeriodicidadPago()))
			return null;
		
		if (pojoContrato.getSueldo() != pojoRegistro.getSueldo().doubleValue())
			return null;
		
		val1 = new BigDecimal(pojoRegistro.getSueldoHora());
		val2 = pojoContrato.getSueldoHora();
		if (val1.compareTo(val2) != 0)
			return null;

		// Campos actualizables
		if (pojoContrato.getDiaDescanso() != getValorDiaDescanso(pojoRegistro.getDiaDescanso())) {
			actualizar = true;
			pojoContrato.setDiaDescanso(getValorDiaDescanso(pojoRegistro.getDiaDescanso()));
		}
		
		if (! pojoContrato.getFormaPago().equals(getIdFormaDePago(pojoRegistro.getModalidadPago()))) {
			actualizar = true;
			pojoContrato.setFormaPago(getIdFormaDePago(pojoRegistro.getModalidadPago()));
		}
		
		if (! pojoContrato.getCentroTrabajo().equals(pojoRegistro.getCentroTrabajo())) {
			actualizar = true;
			pojoContrato.setCentroTrabajo(pojoRegistro.getCentroTrabajo());
		}
		
		val1 = new BigDecimal(pojoRegistro.getSueldoHoraExtra());
		val2 = pojoContrato.getSueldoHoraExtra();
		if (val1.compareTo(val2) != 0){
			actualizar = true;
			pojoContrato.setSueldoHoraExtra(val1);
		}

		val1 = new BigDecimal(pojoRegistro.getDescuentoInfonavit());
		if (pojoContrato.getDescuentoInfonavit() != pojoRegistro.getDescuentoInfonavit().doubleValue()){
			actualizar = true;
			pojoContrato.setDescuentoInfonavit(val1.doubleValue());
		}

		try {
			// Actualizamos los datos del contrato con los del documento y lo refrescamos en la lista
			if (actualizar) {
				pojoContrato.setModificadoPor(this.usuarioId);
				pojoContrato.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzContratos.update(pojoContrato);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar actualizar el Contrato " + pojoContrato.getId());
		} finally {
			actualizaContratoInList(pojoContrato);
		}
		
		return pojoContrato;
	}
	
	private Persona getPersonaFromRow(Persona pojoPersona, EmpleadoRow pojoRegistro) {
		if (pojoPersona == null) {
			pojoPersona = new Persona();
			pojoPersona.setCreadoPor(this.usuarioId);
			pojoPersona.setFechaCreacion(Calendar.getInstance().getTime());
		}
		
		pojoPersona.setPrimerApellido(pojoRegistro.getApellidoPaterno());
		pojoPersona.setSegundoApellido(pojoRegistro.getApellidoMaterno());
		pojoPersona.setPrimerNombre(pojoRegistro.getPrimerNombre());
		pojoPersona.setSegundoNombre(pojoRegistro.getSegundoNombre());
		pojoPersona.setNombre(generaNombre(pojoRegistro.getApellidoPaterno(), pojoRegistro.getApellidoMaterno(), pojoRegistro.getPrimerNombre(), pojoRegistro.getSegundoNombre()));
		pojoPersona.setRfc(pojoRegistro.getRfc());
		pojoPersona.setNumeroCuenta(pojoRegistro.getNumeroCuenta());
		pojoPersona.setBanco(getIdBanco(pojoRegistro.getBanco()));
		pojoPersona.setTipoPersona(1L);
		
		pojoPersona.setModificadoPor(this.usuarioId);
		pojoPersona.setFechaModificacion(Calendar.getInstance().getTime());
		
		return pojoPersona;
	}
	
	private Empleado getEmpleadoFromRow(Empleado pojoEmpleado, Long idPersona, String nombrePersona, EmpleadoRow pojoRegistro) {
		if (pojoEmpleado == null) {
			pojoEmpleado = new Empleado();
			pojoEmpleado.setExterno(0L);
			pojoEmpleado.setEmail("");
			pojoEmpleado.setCreadoPor(this.usuarioId);
			pojoEmpleado.setFechaCreacion(Calendar.getInstance().getTime());
		}
		
		pojoEmpleado.setClave(pojoRegistro.getClave());
		pojoEmpleado.setFechaIngreso(pojoRegistro.getFechaIngreso());
		pojoEmpleado.setNumeroSeguridadSocial(pojoRegistro.getNss());

		pojoEmpleado.setIdPersona(idPersona);
		pojoEmpleado.setNombre(nombrePersona);
		pojoEmpleado.setPrimerApellido(pojoRegistro.getApellidoPaterno());
		pojoEmpleado.setSegundoApellido(pojoRegistro.getApellidoMaterno());
		pojoEmpleado.setPrimerNombre(pojoRegistro.getPrimerNombre());
		pojoEmpleado.setSegundoNombre(pojoRegistro.getSegundoNombre());
		
		pojoEmpleado.setIdSucursal(getIdSucursal(pojoRegistro.getSucursal()));
		pojoEmpleado.setNombreSucursal(pojoRegistro.getSucursal());

		pojoEmpleado.setIdArea(getIdArea(pojoRegistro.getArea()));
		pojoEmpleado.setAreaDescripcion(pojoRegistro.getArea());
		
		pojoEmpleado.setIdPuestoCategoria(getIdPuestoCategoria(getIdPuesto(pojoRegistro.getPuesto()), getIdCategoria(pojoRegistro.getCategoria())));
		pojoEmpleado.setPuestoDescripcion(pojoRegistro.getPuesto());
		pojoEmpleado.setCategoriaDescripcion(pojoRegistro.getCategoria());

		pojoEmpleado.setModificadoPor(this.usuarioId);
		pojoEmpleado.setFechaModificacion(Calendar.getInstance().getTime());
		
		return pojoEmpleado;
	}
	
	private EmpleadoContrato getContratoFromRow(EmpleadoContrato pojoContrato, EmpleadoRow pojoRegistro) {
		if (pojoContrato == null) {
			pojoContrato = new EmpleadoContrato();
			pojoContrato.setCreadoPor(this.usuarioId);
			pojoContrato.setFechaCreacion(Calendar.getInstance().getTime());
		}
		
		pojoContrato.setIdEmpleado(this.ifzEmpleados.findById(pojoRegistro.getIdEmpleado()));
		pojoContrato.setDeterminado(getValorTipoContrato(pojoRegistro.getContratoTipo()));
		pojoContrato.setDiaDescanso(getValorDiaDescanso(pojoRegistro.getDiaDescanso()));
		pojoContrato.setFormaPago(getIdFormaDePago(pojoRegistro.getModalidadPago()));
		
		if ("OFICINA".equals(pojoRegistro.getHorarioTipo())) {
			pojoContrato.setTipoHorario(0);
			pojoContrato.setHoraEntrada(getHoraFromHorario("OFICINA_ENTRADA1"));
			pojoContrato.setHoraSalida(getHoraFromHorario("OFICINA_SALIDA1"));
			pojoContrato.setHoraEntradaComplemento(getHoraFromHorario("OFICINA_ENTRADA2"));
			pojoContrato.setHoraSalidaComplemento(getHoraFromHorario("OFICINA_SALIDA2"));
		} else { // OBRA
			pojoContrato.setTipoHorario(1);
			pojoContrato.setHoraEntrada(getHoraFromHorario("OBRA_ENTRADA"));
			pojoContrato.setHoraSalida(getHoraFromHorario("OBRA_SALIDA"));
		}
		
		pojoContrato.setCentroTrabajo(pojoRegistro.getCentroTrabajo());
		pojoContrato.setSueldo(pojoRegistro.getSueldo());
		pojoContrato.setSueldoHora(new BigDecimal(pojoRegistro.getSueldoHora()));
		pojoContrato.setSueldoHoraExtra(new BigDecimal(pojoRegistro.getSueldoHoraExtra()));
		pojoContrato.setPeridiocidadPago(getIdPeriodicidad(pojoRegistro.getPeriodicidadPago()));
		pojoContrato.setDescuentoInfonavit(pojoRegistro.getDescuentoInfonavit());
		pojoContrato.setFechaInicio(pojoRegistro.getFechaIngreso());
		pojoContrato.setFechaFin(pojoRegistro.getFechaTermino());

		pojoContrato.setModificadoPor(this.usuarioId);
		pojoContrato.setFechaModificacion(Calendar.getInstance().getTime());
		
		return pojoContrato;
	}
	
	private String generaNombre(String paterno, String materno, String nombre1, String nombre2) {
		String resultado = "";
		
		if (nombre1 != null && ! "".equals(nombre1.trim()))
			resultado += nombre1;
		
		if (nombre2 != null && ! "".equals(nombre2.trim()))
			resultado += " " + nombre2;
		
		if (paterno != null && ! "".equals(paterno.trim()))
			resultado += " " + paterno;
		
		if (materno != null && ! "".equals(materno.trim()))
			resultado += " " + materno;
		
		return resultado.trim();
	}
	
	private int getValorTipoContrato(String value) {
		int resultado = 0;
		
		value = (value != null && ! "".equals(value)) ? value.trim().toUpperCase() : "";
		switch (value) {
		case "DETERMINADO" : resultado = 1; break;
		case "INDETERMINADO" : resultado = 0; break;
		default : resultado = 0; break;
		}
		
		return resultado;
	}
	
	private int getValorDiaDescanso(String value) {
		int resultado = 0;
		
		value = value.trim().toUpperCase();
		switch (value) {
		case "DOMINGO" : resultado = 1; break;
		case "LUNES" : resultado = 2; break;
		case "MARTES" : resultado = 3; break;
		case "MIERCOLES" : resultado = 4; break;
		case "JUEVES" : resultado = 5; break;
		case "VIERNES" : resultado = 6; break;
		case "SABADO" : resultado = 7; break;
		default : resultado = 0; break;
		}

		return resultado;
	}

	private Date getHoraFromHorario(String horarioKey) {
		SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm");
		Date resultado = null;
		String hora = "";
		
		try {
			hora = this.listHorarios.get(horarioKey);
			resultado = formatterHora.parse(hora);
		} catch (ParseException e) {
			log.error("Ocurrio un problema al intentar formatear el horario para " + horarioKey, e);
		}
		
		return resultado;
	}
	
	private void iniciarBitacora() {
		if (this.bitacora == null)
			this.bitacora = new ArrayList<String>();
		this.bitacora.clear();
		
		if (this.listErrores == null)
			this.listErrores = new LinkedHashMap<String, String>();
		this.listErrores.clear();
		
		if (this.listErrores == null)
			this.listErrores = new LinkedHashMap<String, String>();
		this.listErrores.clear();
		
		cargarProperty(this.empleadosProperties);
		
		this.resRegistros = "";
		this.registrosTotales = 0;
		this.sumPersonas = 0;
		this.sumEmpleados = 0;
		this.sumContratos = 0;
	}
	
	private void bitacora(String mensaje, boolean replacePrev) {
		this.bitacora.add(mensaje);
		if (replacePrev) this.bitacora.remove(this.bitacora.size() - 2);
		log.info("---> " + mensaje);
	}
	
	private void cargarProperty(PropertyResourceBundle prop) {
		this.layoutEmpleados = new HashMap<String, String>();
		this.layoutEmpleados = getKeyAsHashmapFromProperties(prop, "LAYOUT.");
		
		this.listHorarios = new HashMap<String, String>();
		this.listHorarios = getKeyAsHashmapFromProperties(prop, "HORARIO.");
		
		getRequeridos(prop);
		
		// ALL PROPERTIES IN HASHMAP
		// getKeyAsArrayFromProperties(prop, "");
	}
	
	private HashMap<String, String> getKeyAsHashmapFromProperties(PropertyResourceBundle prop, String prefix) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		if (prefix != null && ! "".equals(prefix)) {
			for(String key : prop.keySet()) {
				if (key.startsWith(prefix)) {
					if ("".equals(key.substring(prefix.length())))
						result.put(key, prop.getString(key).trim());
					else
						result.put(key.substring(prefix.length()).trim(), prop.getString(key).trim());
				}
			}
		} else {
			for(String key : prop.keySet())
				result.put(key.trim(), prop.getString(key).trim());
		}
		
		return result;
	}
	
	private List<String> getKeyAsArrayFromProperties(PropertyResourceBundle prop, String key) {
		List<String> resultado = new ArrayList<String>();
		String[] values = null;
		String value = "";
		
		value = prop.getString(key);
		if (value.contains(",")) { 
			values = value.split(",");
			if (values != null) {
				for (int i = 0; i < values.length; i++)
					resultado.add(values[i].trim());
			}
		} else {
			resultado.add(value.trim());
		}
		
		return resultado;
	}
	
	private void getRequeridos(PropertyResourceBundle prop) {
		this.listRequeridos = new ArrayList<String>();
		this.listRequeridos = getKeyAsArrayFromProperties(prop, "REQUERIDOS");
		
		if (this.listRequeridos.size() == 1 && "TODOS".equals(this.listRequeridos.get(0))) {
			this.listRequeridos.clear();
			for (Entry<String, String> var : this.layoutEmpleados.entrySet())
				this.listRequeridos.add(var.getKey());
		}
	}

	private void dropValues() {
		dropData(false);
	}
		
	private void dropData(boolean clearStored) {
		EmpleadoContrato pojoContrato = null;
		Empleado pojoEmpleado = null;
		Persona pojoPersona = null;
		
		for (EmpleadoRow var : this.listEmpleadoRow) {
			// CONTRATO
			// ----------------------------------------------------------------------------------------
			if ((var.isNuevoContrato() && var.getIdContrato() != null && var.getIdContrato() > 0L) || clearStored) {
				try {
					pojoContrato = this.getContratoFromList(var.getIdContrato());
					if (pojoContrato != null)
						this.ifzContratos.delete(pojoContrato);
				} catch (Exception e) {
					log.error("Ocurrio un problema al intentar borrar el Contrato "  + var.getIdContrato(), e);
				}
			}
			
			// EMPLEADO
			// ----------------------------------------------------------------------------------------
			if ((var.isNuevoEmpleado() && var.getIdEmpleado() != null && var.getIdEmpleado() > 0L) || clearStored) {
				try {
					pojoEmpleado = this.getEmpleadoFromList(var.getIdEmpleado());
					if (pojoEmpleado != null)
						this.ifzEmpleados.delete(pojoEmpleado);
				} catch (Exception e) {
					log.error("Ocurrio un problema al intentar borrar el Empleado "  + var.getIdEmpleado(), e);
				}
			}
			
			// PERSONA
			// ----------------------------------------------------------------------------------------
			if ((var.isNuevaPersona() && var.getIdPersona() != null && var.getIdPersona() > 0L) || clearStored) {
				try {
					pojoPersona = this.getPersonaFromList(var.getIdPersona());
					if (pojoPersona != null)
						this.ifzPersonas.delete(pojoPersona);
				} catch (Exception e) {
					log.error("Ocurrio un problema al intentar borrar la Persona "  + var.getIdPersona(), e);
				}
			}
		}
		
		this.listContratos.clear();
		this.listEmpleados.clear();
		this.listPersonas.clear();
	}

	private void control() { 
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(boolean value) {
		if (value)
			control(value, 1, "ERROR");
		else
			control(value, 0, null);
	}
	
	private void control(String value) { 
		if (value == null || "".equals(value))
			control(true, 1, "ERROR");
		else
			control(true, -1, value); 
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "": mensaje;
	}
	
	// ----------------------------------------------------------------------------------------------
	// PROPIEDADES 
	// ----------------------------------------------------------------------------------------------

	public boolean getOperacion() { 
		return this.operacionCancelada; 
	}
	
	public void setOperacion(boolean value) { 
		this.operacionCancelada = value; 
	}
	
	public int getTipoMensaje() { 
		return this.tipoMensaje; 
	}
	
	public void setTipoMensaje(int value) { 
		this.tipoMensaje = value; 
	}
	
	public String getMensaje() { 
		return this.mensaje; 
	}
	
	public void setMensaje(String value) { 
		this.mensaje = value; 
	}

	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public int getMaxCargas() {
		return maxCargas;
	}

	public void setMaxCargas(int maxCargas) {
		this.maxCargas = maxCargas;
	}

	public boolean getProcesando() {
		return procesando;
	}

	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}
	
	public String getBitacora() {
		String val = "";
		
		if (this.bitacora == null || this.bitacora.isEmpty()) return "";		
		for (String s : this.bitacora)
			val += s + "\n";
		return val;
	}
	
	public void setBitacora(String bitacora) { }

	public boolean isEscapeForInputMesssage() {
		return escapeForInputMesssage;
	}

	public void setEscapeForInputMesssage(boolean escapeForInputMesssage) {
		this.escapeForInputMesssage = escapeForInputMesssage;
	}

	public String getResRegistros() {
		return resRegistros;
	}

	public void setResRegistros(String resRegistros) {
		this.resRegistros = resRegistros;
	}

	public boolean isContinuarConRegistrosIncompletos() {
		return continuarConRegistrosIncompletos;
	}

	public void setContinuarConRegistrosIncompletos(boolean continuarConRegistrosIncompletos) {
		this.continuarConRegistrosIncompletos = continuarConRegistrosIncompletos;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	2.2 | 2017-05-16 | Javier Tirado 			 | Creo clase para el procesamiento de empleados por lotes
 */
package net.giro.adp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;

import com.google.common.io.Files;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraContratos;
import net.giro.adp.beans.ObraSatics;
import net.giro.adp.logica.ObraContratosRem;
import net.giro.adp.logica.ObraRem;
import net.giro.adp.logica.ObraSaticsRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;

@ViewScoped
@ManagedBean(name="saticsAction")
public class SaticsObrasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SaticsObrasAction.class);
	
	private InitialContext ctx; 
	private LoginManager loginManager; 
	private HttpSession httpSession;
	private PropertyResourceBundle entornoProperties;
	
	// Obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	
	// Obras Satics
	private ObraSaticsRem ifzObraSatics;
	private ObraContratosRem ifzContratos;
	private FtpRem ifzFtp;
	private List<ObraSatics> listObraSatics;
	private List<SelectItem> listSaticsItems;
	private List<ConValores> listSatics;
	private List<SelectItem> listObraContratosItems;
	private ObraSatics	pojoSatics;
	private ObraSatics	pojoSaticsBorrar;
	private int numPaginaObraSatics;
	private long saticsId;
	private long saticsAdendumId;

	private byte[] fileSrc; 
	private String fileName;
	private String fileExtension;
	private String ftpDigitalizacionHost;
	private String ftpDigitalizacionPort;
	private String ftpDigitalizacionUser;
	private String ftpDigitalizacionPass;
	private String ftpDigitalizacionRuta;

	// ConValores
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private ConGrupoValores pojoGpoSatics;

	// Busqueda
	private List<SelectItem> busquedaOpciones;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaTipo;
	private int busquedaPaginas;
	
	// Variables de control
	private boolean incompleto;
	private int tipoMensaje;
	private String mensaje;
    private long usuarioId;
    private boolean permiteModificar;
    private double valObraCancelada;

	
	public SaticsObrasAction() {
		long valGpo = 0;
		
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObraSatics = (ObraSaticsRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraSaticsFac!net.giro.adp.logica.ObraSaticsRem");
			this.ifzContratos = (ObraContratosRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraContratosFac!net.giro.adp.logica.ObraContratosRem");
			this.ifzFtp = (FtpRem) this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
			// Inicializaciones
			this.usuarioId = 0;
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = (long) this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			//this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			//this.porcentajeIva = Double.valueOf(this.loginManager.getAutentificacion().getPerfil("VALOR_IVA"));
			this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());
			
			this.ifzObras.showSystemOuts(false);
			this.ifzContratos.showSystemOuts(false);
			this.ifzContratos.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "Clave"));
			this.busquedaCampo = busquedaOpciones.get(0).getDescription();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;

			// GRUPO OBRA SATICS
			// ------------------------------------------------------------------------------------------------------------
			if ( entornoProperties.getString("SYS_SATICS") == null || "".equals(entornoProperties.getString("SYS_SATICS")))
				throw new Exception("No se encontro encontro el grupo SYS_SATICS en con_grupo_valores");
			valGpo = Long.valueOf(entornoProperties.getString("SYS_SATICS"));
			this.pojoGpoSatics = this.ifzGpoVal.findById(valGpo);
			if (this.pojoGpoSatics == null)
				throw new Exception("No se encontro encontro el grupo SYS_SATICS en con_grupo_valores");
			
			// Variables para la subida de archivos
			this.ftpDigitalizacionHost = entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = entornoProperties.getString("ftp.digitalizacion.ruta");
			
			this.valObraCancelada = 10000798L;
			this.numPaginaObraSatics = 1;
		} catch (Exception e) {
			this.ctx = null;
			log.error("Error en constructor GestionProyectos.SaticsObrasAction", e);
		}
	}
	
	
	private void control(boolean procesoInterrumpido, int tipo, String mensaje) {
		this.incompleto = procesoInterrumpido;
		this.tipoMensaje = tipo;
		this.mensaje = mensaje;
	}

	public void buscar() {
		try {
			control(false, 0, "");
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = "nombre";
			
			if (this.listObras != null)
				this.listObras.clear();

			//this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo);
			if (this.listObras.isEmpty()) {
				control(true, 2, "ERROR");
				return;
			}
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.SaticsObrasAction.buscar", e);
    	}
	}
	
	public void ver() {
		try {
			control(false, 0, "");
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}
			
			this.permiteModificar = true;
			if(this.pojoObra.getEstatus().equals(10000798L)) 
				this.permiteModificar = false;

			if (this.listObraSatics == null)
				this.listObraSatics = new ArrayList<ObraSatics>();
			this.listObraSatics.clear();
			
			// cargamos los satic
			cargarSatics();
			
			this.ifzObraSatics.orderBy("fechaCreacion DESC");
			this.listObraSatics = this.ifzObraSatics.findLikeProperty("idObra", this.pojoObra.getId(), 120);
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.SaticsObrasAction.ver", e);
    	}
	}
	
	public void nuevo() {
		try {
			control(false, 0, "");
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}

			this.pojoSatics = new ObraSatics();
			this.pojoSaticsBorrar = null;
			this.saticsId = 0;
			this.saticsAdendumId = 0;

			nuevoUploadFile();
			cargaObraContratos();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.SaticsObrasAction.nuevo", e);
    	}
	}
	
	public void editar() {
		try {
			control(false, 0, "");
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}

			cargaObraContratos();
			
			if (this.pojoSatics != null) {
				this.saticsId = this.pojoSatics.getIdSatics();
				if (this.pojoSatics.getIdSaticsAdendum() != null)
					this.saticsAdendumId = this.pojoSatics.getIdSaticsAdendum();
			}

			nuevoUploadFile();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.SaticsObrasAction.editar", e);
    	}
	}
	
	public void guardar() {
		try {
			control(false, 0, "");
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				return;
			}
			
			if (this.pojoSatics != null) {
				// Recuperamos SATIC si corresponde
				if (this.saticsId > 0L) {
					for (ConValores var : this.listSatics) {
						if (this.saticsId == var.getId()) {
							this.pojoSatics.setIdSatics(var.getId());
							this.pojoSatics.setNombreSatics(var.getValor());
							break;
						}
					}
				}

				// Recuperamos SATIC ADENDUM si corresponde
				this.pojoSatics.setIdSaticsAdendum(0L);
				this.pojoSatics.setNombreSaticsAdendum("");
				if (this.saticsAdendumId > 0L) {
					for (ConValores var : this.listSatics) {
						if (this.saticsAdendumId == var.getId()) {
							this.pojoSatics.setIdSaticsAdendum(var.getId());
							this.pojoSatics.setNombreSaticsAdendum(var.getValor());
							break;
						}
					}
				}
				
				this.pojoSatics.setIdObra(this.ifzObras.findById(this.pojoObra.getId()));
				this.pojoSatics.setNombreObra(this.pojoObra.getNombre());
				this.pojoSatics.setModificadoPor(this.usuarioId);
				this.pojoSatics.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoSatics.getId() == null || this.pojoSatics.getId() <= 0L) {
					this.pojoSatics.setCreadoPor(this.usuarioId);
					this.pojoSatics.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoSatics.setId(this.ifzObraSatics.save(this.pojoSatics));
					
					// Agregamos al listado
					this.listObraSatics.add(this.pojoSatics);
				} else {
					// Actualizamos en la BD
					this.ifzObraSatics.update(this.pojoSatics);
				}
			}

			nuevo();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.SaticsObrasAction.guardar", e);
    	}
	}
	
	public void eliminar() {
		try {
			control(false, 0, "");

			if (this.pojoSaticsBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoSaticsBorrar.getId() != null && this.pojoSaticsBorrar.getId() > 0L)
					this.ifzObraSatics.delete(this.pojoSaticsBorrar.getId());
				
				// Borramos del listado
				this.listObraSatics.remove(this.pojoSaticsBorrar);
			}
	    	
	    	ver();
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.SaticsObrasAction.eliminar", e);
    	}
	}

	public void cargarSatics() {
		try {
			control(false, 0, "");
			
			if (this.listSaticsItems == null)
				this.listSaticsItems = new ArrayList<SelectItem>();
			this.listSaticsItems.clear();
			
			this.listSatics = this.ifzConValores.findAll(this.pojoGpoSatics);
			if (this.listSatics != null && ! this.listSatics.isEmpty()) {
				for (ConValores var : this.listSatics) {
					this.listSaticsItems.add(new SelectItem(var.getId(), var.getValor()));
				}
			}
		} catch (Exception e) {
			control(true, 1, "ERROR");
			log.error("Error en el metodo GestionProyectos.SaticsObrasAction.cargarSatics", e);
		}
	}
	
	public boolean cargaObraContratos() {
    	try {
			control(false, 0, "");
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.listObraContratosItems == null)
				this.listObraContratosItems = new ArrayList<SelectItem>();
			this.listObraContratosItems.clear();
			
			List<ObraContratos> lista = this.ifzContratos.findByProperty("idObra.id", this.pojoObra.getId(), 0);
			if (lista.isEmpty()) {
				control(true, -1, "No se encontro ningun contrato.");
				return false;
			}

			for (ObraContratos var : lista) {
				this.listObraContratosItems.add(new SelectItem(var.getId(), var.getNombreSubcontratante()));
			}
			
	    	return true;
    	} catch (Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.SaticsObrasAction.cargaObraContratos", e);
    		return false;
    	}
    }

	public void nuevoUploadFile() {
		this.fileSrc = null;
		this.fileName = "";
		this.fileExtension = "";
	}
	
	public void uploadListener(FileUploadEvent event) throws Exception {
		try {
			control(false, 0, "");
			
			this.fileSrc = event.getUploadedFile().getData();
			this.fileName = event.getUploadedFile().getName();
			this.fileExtension = Files.getFileExtension(event.getUploadedFile().getName());
		} catch (Exception e) {
			control(true, 1, "ERROR");
			log.error("Error en GestionProyectos.SaticsObrasAction.uploadListener", e);
		}
	}
	
	public void analizarArchivo() throws Exception {
		try {
			control(false, 0, "");
			
			if(this.fileSrc == null || this.fileSrc.length <= 0) {
				nuevoUploadFile();
				return;
			}
			
			if(this.fileExtension == null || "".equals(this.fileExtension))
				this.fileExtension = "pdf";
			
			// Establecemos el nombre del archivo
			this.fileName = this.pojoSatics.getId().toString() + "." + this.fileExtension;
			if (this.fileName == null || "".equals(this.fileName))
				this.fileName = this.ftpDigitalizacionRuta + this.pojoSatics.getId().toString() + "." + this.fileExtension;
			
			// Subimos el archivo al servidor
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			if (! this.ifzFtp.putArchivo(this.fileSrc, this.ftpDigitalizacionRuta + this.fileName)) {
				control(true, 5, "ERROR");
				log.error("Error en GestionProyectos.SaticsObrasAction.analizarArchivo");
				return;
			}
			
			// Indicamos que el archivo ha sido cargado en el servidor.
			this.pojoSatics.setPdfCargado(1);
			this.ifzObraSatics.update(this.pojoSatics);
		} catch (Exception e) {
			control(true, 1, "ERROR");
			log.error("Error en GestionProyectos.SaticsObrasAction.analizarArchivo", e);
		}
	}
	
	public void archivoSatics() {
		try {
			control(false, 0, "");
			if (this.pojoSatics != null && this.pojoSatics.getId() != null && this.pojoSatics.getId() > 0L && this.pojoSatics.getPdfCargado() == 1) {
				// Inicializamos nuestro FTP
				this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
				
				// Inicializamos variables de archivos y recuperamos el archivo
				this.fileName = this.pojoSatics.getId() + ".pdf";
				this.fileExtension = "pdf";
				this.fileSrc = this.ifzFtp.getArchivo(this.ftpDigitalizacionRuta + this.fileName);
				
				// Comprobamos el archivo
				if(this.fileSrc == null || this.fileSrc.length <= 0) {
					control(true, 15, "No se encontro el archivo en el servidor");
					return;
				}

				// Ponemos los datos en session
				this.httpSession.setAttribute("nombreDocumento", this.fileName);
				this.httpSession.setAttribute("formato", this.fileExtension);
				this.httpSession.setAttribute("contenido", this.fileSrc);
				control(false, 0, "");
			}
		} catch (Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.SaticsObrasAction.getArchivoSatics", e);
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

	public boolean getOperacion() {
		return incompleto;
	}

	public void setOperacion(boolean incompleto) {
		this.incompleto = incompleto;
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
	
	public List<SelectItem> getBusquedaOpciones() {
		return busquedaOpciones;
	}

	public void setBusquedaOpciones(List<SelectItem> busquedaOpciones) {
		this.busquedaOpciones = busquedaOpciones;
	}

	public String getBusquedaCampo() {
		return busquedaCampo;
	}

	public void setBusquedaCampo(String busquedaCampo) {
		this.busquedaCampo = busquedaCampo;
	}

	public String getBusquedaValor() {
		return busquedaValor;
	}

	public void setBusquedaValor(String busquedaValor) {
		this.busquedaValor = busquedaValor;
	}

	public int getBusquedaTipo() {
		return busquedaTipo;
	}

	public void setBusquedaTipo(int busquedaTipo) {
		this.busquedaTipo = busquedaTipo;
	}
	
	public int getBusquedaPaginas() {
		return busquedaPaginas;
	}

	public void setBusquedaPaginas(int busquedaPaginas) {
		this.busquedaPaginas = busquedaPaginas;
	}

	public List<Obra> getListObras() {
		return listObras;
	}
	
	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}
	
	public Obra getPojoObra() {
		return pojoObra;
	}
	
	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}
	
	public boolean getObraSaticsTipo() {
		if (this.pojoSatics != null && this.pojoSatics.getTipo() > 1)
			return true;
		return false;
	}
	
	public void setObraSaticsTipo(boolean value) {
		if (this.pojoSatics != null)
			this.pojoSatics.setTipo((value) ? 2 : 1);
	}
	
	public List<SelectItem> getListObraContratosItems() {
		return listObraContratosItems;
	}

	public void setListObraContratosItems(List<SelectItem> listObraContratosItems) {
		this.listObraContratosItems = listObraContratosItems;
	}
	
	public List<ObraSatics> getListObraSatics() {
		return listObraSatics;
	}

	public void setListObraSatics(List<ObraSatics> listObraSatics) {
		this.listObraSatics = listObraSatics;
	}

	public List<SelectItem> getListSaticsItems() {
		return listSaticsItems;
	}

	public void setListSaticsItems(List<SelectItem> listSaticsItems) {
		this.listSaticsItems = listSaticsItems;
	}

	public ObraSatics getPojoSatics() {
		if (pojoSatics == null) pojoSatics = new ObraSatics();
		return pojoSatics;
	}

	public void setPojoSatics(ObraSatics pojoSatics) {
		this.pojoSatics = pojoSatics;
	}

	public ObraSatics getPojoSaticsBorrar() {
		return pojoSaticsBorrar;
	}

	public void setPojoSaticsBorrar(ObraSatics pojoSaticsBorrar) {
		this.pojoSaticsBorrar = pojoSaticsBorrar;
	}

	public int getNumPaginaObraSatics() {
		return numPaginaObraSatics;
	}

	public void setNumPaginaObraSatics(int numPaginaObraSatics) {
		this.numPaginaObraSatics = numPaginaObraSatics;
	}

	public long getSaticsId() {
		return saticsId;
	}

	public void setSaticsId(long saticsId) {
		this.saticsId = saticsId;
	}

	public long getSaticsAdendumId() {
		return saticsAdendumId;
	}

	public void setSaticsAdendumId(long saticsAdendumId) {
		this.saticsAdendumId = saticsAdendumId;
	}
	
	public boolean getPermitirUploadSatic() {
		if (this.pojoSatics != null && this.pojoSatics.getId() != null && this.pojoSatics.getId() > 0L)
			return true;
		return false;
	}
	
	public void setPermitirUploadSatic(boolean value) {}

	public boolean isPermiteModificar() {
		return permiteModificar;
	}

	public void setPermiteModificar(boolean permiteModificar) {
		this.permiteModificar = permiteModificar;
	}

	public double getValObraCancelada() {
		return valObraCancelada;
	}

	public void setValObraCancelada(double valObraCancelada) {
		this.valObraCancelada = valObraCancelada;
	}
}

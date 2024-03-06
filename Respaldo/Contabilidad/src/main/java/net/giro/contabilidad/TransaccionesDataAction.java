package net.giro.contabilidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.beans.TransaccionesData;
import net.giro.contabilidad.logica.TransaccionesDataRem;
import net.giro.contabilidad.logica.TransaccionesRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;

@ViewScoped
@ManagedBean(name="transDataAction")
public class TransaccionesDataAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TransaccionesDataAction.class);
	
	private InitialContext ctx;
	
	// Interfaces
	private TransaccionesDataRem ifzTransData;
	private TransaccionesRem ifzTransacciones;
	private SucursalesRem ifzSucursales;

	// Listas
	private List<TransaccionesData> listTransaccionesData;
	private List<Transacciones> listTransacciones;
	private List<Sucursal> listSucursales;

	// POJO's
	private TransaccionesData pojoTransaccionData;
	private TransaccionesData pojoTransaccionDataBorrar;
	private Transacciones pojoTransaccion;
	private Sucursal pojoSucursal;
	
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	
	// Busqueda sucursales
	private List<SelectItem> tiposBusquedaTransacciones;	
	private String campoBusquedaTransacciones;
	private String valorBusquedaTransacciones;
	private int numPaginaTransacciones;
	
	// Busqueda sucursales
	private List<SelectItem> tiposBusquedaSucursales;	
	private String campoBusquedaSucursales;
	private String valorBusquedaSucursales;
	private int numPaginaSucursales;
	
	// Variables de operacion
	PropertyResourceBundle entornoProperties;
    int usuarioId;
    String usuario;
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	private String valorLlave;
	
	
	public TransaccionesDataAction() {
		try {
			this.ctx = new InitialContext();
			
			this.ifzTransData = (TransaccionesDataRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesDataFac!net.giro.contabilidad.logica.TransaccionesDataRem");
			this.ifzTransacciones = (TransaccionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//TransaccionesFac!net.giro.contabilidad.logica.TransaccionesRem");
			this.ifzSucursales = (SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();

			this.usuarioId = 0;
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = lm.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());

			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------
			
			// Listas
			this.listTransaccionesData= new ArrayList<TransaccionesData>();
			this.listSucursales = new ArrayList<Sucursal>();
			
			// POJO's
			this.pojoTransaccionData = new TransaccionesData();
			this.pojoTransaccionDataBorrar = new TransaccionesData();
			
			// Busqueda Principal
			tiposBusqueda = new ArrayList<SelectItem>();
			tiposBusqueda.add(new SelectItem("id", "Clave"));
			tiposBusqueda.add(new SelectItem("descripcion", "Descripcion"));
			campoBusqueda = tiposBusqueda.get(0).getValue().toString();
			valorBusqueda = "";
			numPagina = 1;
			
			// Busqueda Transacciones
			tiposBusquedaTransacciones = new ArrayList<SelectItem>();
			tiposBusquedaTransacciones.add(new SelectItem("id", "Clave"));
			tiposBusquedaTransacciones.add(new SelectItem("descripcion", "Descripcion"));
			this.campoBusquedaTransacciones = tiposBusquedaTransacciones.get(0).getValue().toString();
			this.valorBusquedaTransacciones = "";
			numPaginaTransacciones = 1;
			
			// Busqueda Sucursales
			tiposBusquedaSucursales = new ArrayList<SelectItem>();
			tiposBusquedaSucursales.add(new SelectItem("id", "Clave"));
			tiposBusquedaSucursales.add(new SelectItem("descripcion", "Descripcion"));
			campoBusquedaSucursales = tiposBusquedaSucursales.get(0).getValue().toString();
			valorBusquedaSucursales = "";
			numPaginaSucursales = 1;
		} catch (Exception e) {
			log.error("Error en constructor TransaccionesDataAction", e);
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
			
			this.listTransaccionesData = this.ifzTransData.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 120);
			
			if (this.listTransaccionesData.isEmpty()) {
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
			log.error("Error en TransaccionesDataAction.buscar", e);
		}
	}
	
	public void nuevo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoTransaccionData = new TransaccionesData();
			this.pojoTransaccionDataBorrar = null;
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesDataAction.nuevo", e);
		}
	}
	
	public void editar() {}
	
	public void guardar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoTransaccionData != null) {
				this.pojoTransaccionData.setModificadoPor(this.usuarioId);
				this.pojoTransaccionData.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoTransaccionData.getId() == null || this.pojoTransaccionData.getId() <= 0L) {
					this.pojoTransaccionData.setCreadoPor(this.usuarioId);
					this.pojoTransaccionData.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoTransaccionData.setId(this.ifzTransData.save(this.pojoTransaccionData));
					// Agregamos a la lista
					this.listTransaccionesData.add(this.pojoTransaccionData);
				} else {
					// Actualizamos en la BD
					this.ifzTransData.update(this.pojoTransaccionData);
					
					// Actualizamo en la lista
					/*for(TransaccionesData var : this.listTransacciones) {
						if (var.getId().equals(this.pojoTransaccion.getId())) {
							var.setDescripcion(this.pojoTransaccion.getDescripcion());
							var.setIdOperacion(this.pojoTransaccion.getIdOperacion());
							var.setModificadoPor(this.pojoTransaccion.getModificadoPor());
							var.setFechaModificacion(this.pojoTransaccion.getFechaModificacion());
							break;
						}
					}*/
				}
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesDataAction.guardar", e);
		}
	}
	
	public void borrar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoTransaccionDataBorrar != null && this.pojoTransaccionDataBorrar.getId() > 0L) {
				// Borramos de la bd
				this.ifzTransData.delete(this.pojoTransaccionDataBorrar.getId());
				
				// Borramos de la lista
				this.listTransaccionesData.remove(this.pojoTransaccionDataBorrar);
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesDataAction.borrar", e);
		}
	}

	public void nuevaBusquedaTransacciones() {
		if (this.listTransacciones == null)
			this.listTransacciones = new ArrayList<Transacciones>();
		this.listTransacciones.clear();
		
		this.pojoTransaccion = null;
		
		this.campoBusquedaTransacciones = tiposBusquedaTransacciones.get(0).getValue().toString();
		this.valorBusquedaTransacciones = "";
		numPaginaTransacciones = 1;
	}
	
	public void buscarTransacciones() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaTransacciones))
				this.campoBusquedaTransacciones = tiposBusquedaTransacciones.get(0).getValue().toString();
			
			this.listTransacciones = this.ifzTransacciones.findLikeProperty(this.campoBusquedaTransacciones, this.valorBusquedaTransacciones, 120);
			
			if (this.listTransacciones == null || this.listTransacciones.isEmpty()) {
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
			log.error("Error en TransaccionesDataAction.buscarTransacciones", e);
		}
	}
	
	public void seleccionarTransaccion() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			this.pojoTransaccionData.setCodigoTransaccion(this.pojoTransaccion.getCodigo());

			nuevaBusquedaTransacciones();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesDataAction.seleccionarTransaccion", e);
		}
	}

	public void nuevaBusquedaSucursales() {
		if (this.listSucursales == null)
			this.listSucursales = new ArrayList<Sucursal>();
		this.listSucursales.clear();
		
		this.pojoSucursal = null;
		
		this.campoBusquedaSucursales = tiposBusquedaSucursales.get(0).getValue().toString();
		this.valorBusquedaSucursales = "";
		numPaginaSucursales = 1;
	}
	
	public void buscarSucursales() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaSucursales))
				this.campoBusquedaSucursales = tiposBusquedaSucursales.get(0).getValue().toString();
			
			this.listSucursales = this.ifzSucursales.buscarSucursales(this.campoBusquedaSucursales, this.valorBusquedaSucursales);
			
			if (this.listSucursales == null || this.listSucursales.isEmpty()) {
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
			log.error("Error en TransaccionesDataAction.buscarSucursales", e);
		}
	}
	
	public void seleccionarSucursal() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			this.pojoTransaccionData.setIdSucursal(this.pojoSucursal.getId());
			this.pojoTransaccionData.setSucursal(this.pojoSucursal.getSucursal());

			nuevaBusquedaSucursales();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en TransaccionesDataAction.seleccionarSucursal", e);
		}
	}

	public String getCodigoTransaccion() {
		if (this.pojoTransaccionData != null && this.pojoTransaccionData.getCodigoTransaccion() != null)
			return this.pojoTransaccionData.getCodigoTransaccion().toString();
		return "";
	}
	
	public void setCodigoTransaccion(String value) {}
	
	public void agregarValorLlave() {
		if(this.pojoTransaccionData.getValorLlave1() == null || "".equals(this.pojoTransaccionData.getValorLlave1())) {
			this.pojoTransaccionData.setValorLlave1(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave2() == null || "".equals(this.pojoTransaccionData.getValorLlave2())) {
			this.pojoTransaccionData.setValorLlave2(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave3() == null || "".equals(this.pojoTransaccionData.getValorLlave3())) {
			this.pojoTransaccionData.setValorLlave3(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave4() == null || "".equals(this.pojoTransaccionData.getValorLlave4())) {
			this.pojoTransaccionData.setValorLlave4(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave5() == null || "".equals(this.pojoTransaccionData.getValorLlave5())) {
			this.pojoTransaccionData.setValorLlave5(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave6() == null || "".equals(this.pojoTransaccionData.getValorLlave6())) {
			this.pojoTransaccionData.setValorLlave6(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave7() == null || "".equals(this.pojoTransaccionData.getValorLlave7())) {
			this.pojoTransaccionData.setValorLlave7(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave8() == null || "".equals(this.pojoTransaccionData.getValorLlave8())) {
			this.pojoTransaccionData.setValorLlave8(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave9() == null || "".equals(this.pojoTransaccionData.getValorLlave9())) {
			this.pojoTransaccionData.setValorLlave9(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave10() == null || "".equals(this.pojoTransaccionData.getValorLlave10())) {
			this.pojoTransaccionData.setValorLlave10(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave11() == null || "".equals(this.pojoTransaccionData.getValorLlave11())) {
			this.pojoTransaccionData.setValorLlave11(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave12() == null || "".equals(this.pojoTransaccionData.getValorLlave12())) {
			this.pojoTransaccionData.setValorLlave12(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave13() == null || "".equals(this.pojoTransaccionData.getValorLlave13())) {
			this.pojoTransaccionData.setValorLlave13(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave14() == null || "".equals(this.pojoTransaccionData.getValorLlave14())) {
			this.pojoTransaccionData.setValorLlave14(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave15() == null || "".equals(this.pojoTransaccionData.getValorLlave15())) {
			this.pojoTransaccionData.setValorLlave15(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave16() == null || "".equals(this.pojoTransaccionData.getValorLlave16())) {
			this.pojoTransaccionData.setValorLlave16(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave17() == null || "".equals(this.pojoTransaccionData.getValorLlave17())) {
			this.pojoTransaccionData.setValorLlave17(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave18() == null || "".equals(this.pojoTransaccionData.getValorLlave18())) {
			this.pojoTransaccionData.setValorLlave18(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave19() == null || "".equals(this.pojoTransaccionData.getValorLlave19())) {
			this.pojoTransaccionData.setValorLlave19(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave20() == null || "".equals(this.pojoTransaccionData.getValorLlave20())) {
			this.pojoTransaccionData.setValorLlave20(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave21() == null || "".equals(this.pojoTransaccionData.getValorLlave21())) {
			this.pojoTransaccionData.setValorLlave21(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave22() == null || "".equals(this.pojoTransaccionData.getValorLlave22())) {
			this.pojoTransaccionData.setValorLlave22(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave23() == null || "".equals(this.pojoTransaccionData.getValorLlave23())) {
			this.pojoTransaccionData.setValorLlave23(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave24() == null || "".equals(this.pojoTransaccionData.getValorLlave24())) {
			this.pojoTransaccionData.setValorLlave24(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave25() == null || "".equals(this.pojoTransaccionData.getValorLlave25())) {
			this.pojoTransaccionData.setValorLlave25(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave26() == null || "".equals(this.pojoTransaccionData.getValorLlave26())) {
			this.pojoTransaccionData.setValorLlave26(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave27() == null || "".equals(this.pojoTransaccionData.getValorLlave27())) {
			this.pojoTransaccionData.setValorLlave27(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave28() == null || "".equals(this.pojoTransaccionData.getValorLlave28())) {
			this.pojoTransaccionData.setValorLlave28(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave29() == null || "".equals(this.pojoTransaccionData.getValorLlave29())) {
			this.pojoTransaccionData.setValorLlave29(this.valorLlave);
			this.valorLlave = "";
			return;
		}

		if(this.pojoTransaccionData.getValorLlave30() == null || "".equals(this.pojoTransaccionData.getValorLlave30())) {
			this.pojoTransaccionData.setValorLlave30(this.valorLlave);
			this.valorLlave = "";
			return;
		}
	}
	
	public void borrarValorLlave() {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String value =params.get("borrarValorLlave");
		
		if (value == null || "".equals(value))
			return;
		
		switch (Integer.parseInt(value)) {
		case 1:
			this.pojoTransaccionData.setValorLlave1("");
			break;
		case 2:
			this.pojoTransaccionData.setValorLlave2("");
			break;
		case 3:
			this.pojoTransaccionData.setValorLlave3("");
			break;
		case 4:
			this.pojoTransaccionData.setValorLlave4("");
			break;
		case 5:
			this.pojoTransaccionData.setValorLlave5("");
			break;
		case 6:
			this.pojoTransaccionData.setValorLlave6("");
			break;
		case 7:
			this.pojoTransaccionData.setValorLlave7("");
			break;
		case 8:
			this.pojoTransaccionData.setValorLlave8("");
			break;
		case 9:
			this.pojoTransaccionData.setValorLlave9("");
			break;
		case 10:
			this.pojoTransaccionData.setValorLlave10("");
			break;
		case 11:
			this.pojoTransaccionData.setValorLlave11("");
			break;
		case 12:
			this.pojoTransaccionData.setValorLlave12("");
			break;
		case 13:
			this.pojoTransaccionData.setValorLlave13("");
			break;
		case 14:
			this.pojoTransaccionData.setValorLlave14("");
			break;
		case 15:
			this.pojoTransaccionData.setValorLlave15("");
			break;
		case 16:
			this.pojoTransaccionData.setValorLlave16("");
			break;
		case 17:
			this.pojoTransaccionData.setValorLlave17("");
			break;
		case 18:
			this.pojoTransaccionData.setValorLlave18("");
			break;
		case 19:
			this.pojoTransaccionData.setValorLlave19("");
			break;
		case 20:
			this.pojoTransaccionData.setValorLlave20("");
			break;
		case 21:
			this.pojoTransaccionData.setValorLlave21("");
			break;
		case 22:
			this.pojoTransaccionData.setValorLlave22("");
			break;
		case 23:
			this.pojoTransaccionData.setValorLlave23("");
			break;
		case 24:
			this.pojoTransaccionData.setValorLlave24("");
			break;
		case 25:
			this.pojoTransaccionData.setValorLlave25("");
			break;
		case 26:
			this.pojoTransaccionData.setValorLlave26("");
			break;
		case 27:
			this.pojoTransaccionData.setValorLlave27("");
			break;
		case 28:
			this.pojoTransaccionData.setValorLlave28("");
			break;
		case 29:
			this.pojoTransaccionData.setValorLlave29("");
			break;
		case 30:
			this.pojoTransaccionData.setValorLlave30("");
			break;

		default:
			break;
		}
	}
	
	
	
	public List<Sucursal> getListSucursales() {
		return listSucursales;
	}

	public void setListSucursales(List<Sucursal> listSucursales) {
		this.listSucursales = listSucursales;
	}

	public List<TransaccionesData> getListTransaccionesData() {
		return listTransaccionesData;
	}

	public void setListTransaccionesData(List<TransaccionesData> listAplicacionesData) {
		this.listTransaccionesData = listAplicacionesData;
	}

	public TransaccionesData getPojoTransaccionData() {
		return pojoTransaccionData;
	}

	public void setPojoTransaccionData(TransaccionesData pojoTransaccionData) {
		this.pojoTransaccionData = pojoTransaccionData;
	}

	public TransaccionesData getPojoTransaccionDataBorrar() {
		return pojoTransaccionDataBorrar;
	}

	public void setPojoTransaccionDataBorrar(TransaccionesData pojoTransaccionDataBorrar) {
		this.pojoTransaccionDataBorrar = pojoTransaccionDataBorrar;
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

	public List<SelectItem> getTiposBusquedaSucursales() {
		return tiposBusquedaSucursales;
	}

	public void setTiposBusquedaSucursales(List<SelectItem> tiposBusquedaSucursales) {
		this.tiposBusquedaSucursales = tiposBusquedaSucursales;
	}

	public String getCampoBusquedaSucursales() {
		return campoBusquedaSucursales;
	}

	public void setCampoBusquedaSucursales(String campoBusquedaSucursales) {
		this.campoBusquedaSucursales = campoBusquedaSucursales;
	}

	public String getValorBusquedaSucursales() {
		return valorBusquedaSucursales;
	}

	public void setValorBusquedaSucursales(String valorBusquedaSucursales) {
		this.valorBusquedaSucursales = valorBusquedaSucursales;
	}

	public int getNumPaginaSucursales() {
		return numPaginaSucursales;
	}

	public void setNumPaginaSucursales(int numPaginaSucursales) {
		this.numPaginaSucursales = numPaginaSucursales;
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

	public Sucursal getPojoSucursal() {
		return pojoSucursal;
	}

	public void setPojoSucursal(Sucursal pojoSucursal) {
		this.pojoSucursal = pojoSucursal;
	}

	public List<Transacciones> getListTransacciones() {
		return listTransacciones;
	}

	public void setListTransacciones(List<Transacciones> listTransacciones) {
		this.listTransacciones = listTransacciones;
	}

	public Transacciones getPojoTransaccion() {
		return pojoTransaccion;
	}

	public void setPojoTransaccion(Transacciones pojoTransaccion) {
		this.pojoTransaccion = pojoTransaccion;
	}

	public List<SelectItem> getTiposBusquedaTransacciones() {
		return tiposBusquedaTransacciones;
	}

	public void setTiposBusquedaTransacciones(List<SelectItem> tiposBusquedaTransacciones) {
		this.tiposBusquedaTransacciones = tiposBusquedaTransacciones;
	}

	public String getCampoBusquedaTransacciones() {
		return campoBusquedaTransacciones;
	}

	public void setCampoBusquedaTransacciones(String campoBusquedaTransacciones) {
		this.campoBusquedaTransacciones = campoBusquedaTransacciones;
	}

	public String getValorBusquedaTransacciones() {
		return valorBusquedaTransacciones;
	}

	public void setValorBusquedaTransacciones(String valorBusquedaTransacciones) {
		this.valorBusquedaTransacciones = valorBusquedaTransacciones;
	}

	public int getNumPaginaTransacciones() {
		return numPaginaTransacciones;
	}

	public void setNumPaginaTransacciones(int numPaginaTransacciones) {
		this.numPaginaTransacciones = numPaginaTransacciones;
	}


	public String getValorLlave() {
		return valorLlave;
	}


	public void setValorLlave(String valorLlave) {
		this.valorLlave = valorLlave;
	}
}

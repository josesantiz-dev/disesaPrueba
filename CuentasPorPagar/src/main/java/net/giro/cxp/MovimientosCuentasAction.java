package net.giro.cxp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.logica.CuentasBancariasRem;
import net.giro.tyg.logica.MonedasValoresRem;

public class MovimientosCuentasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MovimientosCuentasAction.class);
	private InitialContext ctx;
	private LoginManager loginManager;
	private ConGrupoValores pojoGpoVal; 
	private PagosGastosExt pojoMovCtas;
	private CtasBancoExt pojoCtas;
	private ConValores pojoMov;
	private ConGrupoValoresRem ifzGpoVal;	
	private PagosGastosRem ifzMovCtas;
	private CuentasBancariasRem ifzCtas;
	private ConValoresRem ifzMov;
	private List<ConValores> listMov;
	private List<SelectItem> listMovItems;
	private List<CuentaBancaria> listCtas;
	private List<PagosGastosExt> listMovCtas;
	private Long valGpo;
	private boolean band;
	private String mensaje;
	private String mensajeDetalles;
	private String operacion;
	private String descripcionCuenta;
	private String descripcionCuenta2;
	private String movimiento;	
	private String cuenta;
	private String cuenta2;
	private String sucursalesVisibles;
	//private String referencia;
	private long usuarioId;	
	private boolean muestraSalvar;
	private boolean habilita;
	private int numPagina;
	private int tipoMensaje;
	private boolean movCtaCancelado;
	// Busqueda Principal
	private List<SelectItem> tiposBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
	// Monedas
	private MonedasValoresRem ifzMonValores;
	/*private List<Moneda> listMonedas;
	private long idMonedaActual;*/
	private double tipoCambioActual;
	private boolean evaluaConversion;

	
	public MovimientosCuentasAction() throws NamingException, Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		this.loginManager = (LoginManager)ve.getValue(fc.getELContext());
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());

		this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId(); //.getLoginBean().getUsuario().getUsuarioId();
		//sucursalesVisibles = lm.getLoginBean().getSucursalesVisibles();
		
		this.ctx = new InitialContext();
		this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
		this.ifzMovCtas = (PagosGastosRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
		this.ifzCtas = (CuentasBancariasRem) this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
		this.ifzMov = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
		this.ifzMonValores = (MonedasValoresRem) this.ctx.lookup("ejb:/Logica_TYG//MonedasValoresFac!net.giro.tyg.logica.MonedasValoresRem");

		this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzMovCtas.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzMov.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzMonValores.setInfoSesion(this.loginManager.getInfoSesion());

		// Busqueda principal
		// ----------------------------------------------------------------------
		this.tiposBusqueda = new ArrayList<SelectItem>();
		this.tiposBusqueda.add(new SelectItem("beneficiario", "Cuenta Bancaria"));
		this.tiposBusqueda.add(new SelectItem("concepto", "Tipo Movimiento"));
		this.tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
		this.tiposBusqueda.add(new SelectItem("id", "ID"));
		this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
		this.valorBusqueda = "";
		this.fechaBusqueda = Calendar.getInstance().getTime();
		this.numPagina = 1;
		/*this.tipoBusqueda = new ArrayList<String>();
		this.tipoBusqueda.add("Fecha del movimiento");
		this.tipoBusqueda.add("Cuenta bancaria");
		this.tipoBusqueda.add("Nombre del movimiento");		
		this.valorBusqueda ="Fecha del movimiento";*/

		this.pojoMovCtas = new PagosGastosExt();
		this.pojoGpoVal = new ConGrupoValores();
		this.pojoMov = new ConValores();
		this.pojoCtas = new CtasBancoExt();
		this.listMovCtas = new ArrayList<PagosGastosExt>();
		this.listCtas = new ArrayList<CuentaBancaria>();
		this.listMov = new ArrayList<ConValores>();

		this.tipoMensaje = 0;
		this.band = false;
		this.mensaje = "";
		this.movCtaCancelado=false;
		this.operacion="T";

		if ( entornoProperties.getString("SYS_MOVCTAS") == null || "".equals(entornoProperties.getString("SYS_MOVCTAS")) )
			throw new Exception("No se encontro encontro el grupo SYS_MOVCTAS en con_grupo_valores");
		else
			this.valGpo = Long.valueOf(entornoProperties.getString("SYS_MOVCTAS")) ;

		this.pojoGpoVal = this.ifzGpoVal.findById(valGpo);
		if (this.pojoGpoVal == null)
			throw new Exception("No se encontro encontro el grupo SYS_MOVCTAS en con_grupo_valores");
		
		cargarTipoMovimientos();
	}


	public void buscar() {
		String orderBy = "";
   		
   		try {
   			control();
   			if (this.campoBusqueda == null || "".equals(this.campoBusqueda.trim()))
   				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
   			
   			if ("fecha".equals(this.campoBusqueda))
   				orderBy = "id desc";
			else
				orderBy = "fecha desc, id desc";
   			
			this.ifzMovCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listMovCtas = this.ifzMovCtas.findMovimientoCuentasLikePropertyExt(this.campoBusqueda, ("fecha".equals(this.campoBusqueda) ? this.fechaBusqueda : this.valorBusqueda), "", true, orderBy, 0);
			if (this.listMovCtas == null || this.listMovCtas.isEmpty())
				control(4, "Busqueda sin resultados");
		} catch (Exception e) {
			this.listMovCtas = new ArrayList<PagosGastosExt>();
			control("Ocurrio un problema al consultar los Movimientos entre Cuentas", e);
		}
	}

	public void nuevo() {
		try {
			control();
			this.pojoMovCtas = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoMov = new ConValores();
			this.muestraSalvar = true;
			this.habilita = false;
			this.cuenta = "";
			this.cuenta2 = "";
			this.descripcionCuenta = "";
			this.descripcionCuenta2 = "";
			this.movimiento = "";
		} catch (Exception e) {
			control("Ocurrio un problema al instanciar un nuevo Movimiento entre cuentas.", e);
		}
	}

	public void editar() {
		try {
			control();
			this.muestraSalvar = false;
			this.habilita = true;
			this.cuenta = "";
			this.descripcionCuenta = "";
			this.cuenta2 = "";
			this.descripcionCuenta2 = "";
			this.movimiento = "";
			
			if (this.pojoMovCtas != null) {
				if (this.pojoMovCtas.getIdCuentaOrigen() != null) {
					this.cuenta = this.pojoMovCtas.getIdCuentaOrigen().getId()
							+ " - " + this.pojoMovCtas.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto()
							+ " - " + this.pojoMovCtas.getIdCuentaOrigen().getNumeroDeCuenta();
					
					this.descripcionCuenta = this.pojoMovCtas.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto()
						+ " - " + this.pojoMovCtas.getIdCuentaOrigen().getNumeroDeCuenta();
				}

				if (this.pojoMovCtas.getIdCuentaOrigen() != null) {
					this.cuenta2 = this.pojoMovCtas.getIdCuentaDestino().getId()
							+ " - " + this.pojoMovCtas.getIdCuentaDestino().getInstitucionBancaria().getNombreCorto()
							+ " - " + this.pojoMovCtas.getIdCuentaDestino().getNumeroDeCuenta();
					
					this.descripcionCuenta2 = this.pojoMovCtas.getIdCuentaDestino().getInstitucionBancaria().getNombreCorto()
						+ " - " + this.pojoMovCtas.getIdCuentaDestino().getNumeroDeCuenta();
				}
				
				if (this.pojoMovCtas.getIdTiposMovimiento() != null) {
					this.movimiento = this.pojoMovCtas.getIdTiposMovimiento().getId() 
							+ " - " + this.pojoMovCtas.getIdTiposMovimiento().getDescripcion();
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar o editar el Movimiento entre Cuentas indicado", e);
		}
	}

	public void guardar() {
		Respuesta res;
		Pattern pat = null;
		Matcher match = null;
		boolean registroNuevo = false;

		try {
			control();
			if (this.pojoMovCtas.getId() == null)
				registroNuevo=true;

			this.pojoMovCtas.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());

			/* validando Cuenta */
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.cuenta);			
			if (match.find())
				if (registroNuevo)				
					this.pojoCtas = this.ifzMovCtas.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzCtas.findAllById(Short.valueOf(match.group(1)));
				else
					if (this.pojoCtas.getId() != Short.valueOf(match.group(1)))
						this.pojoCtas = this.ifzMovCtas.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzCtas.findAllById(Short.valueOf(match.group(1)));

			this.pojoMovCtas.setIdCuentaOrigen(this.pojoCtas);

			match = pat.matcher(this.cuenta2);			
			if (match.find())
				if (registroNuevo)
					this.pojoCtas = this.ifzMovCtas.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzCtas.findAllById(Short.valueOf(match.group(1)));
				else
					if (this.pojoCtas.getId() != Short.valueOf(match.group(1)))
						this.pojoCtas = this.ifzMovCtas.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzCtas.findAllById(Short.valueOf(match.group(1)));

			this.pojoMovCtas.setIdCuentaDestino(this.pojoCtas);
			//this.pojoMovCtas.setSucursal(pojoCtas.getSucursal());

			/* validando movimientos */
			match = pat.matcher(this.movimiento);
			if (match.find())
				if (registroNuevo)
					this.pojoMov = this.ifzMov.findById(Long.valueOf(match.group(1)));
				else
					if (this.pojoMov.getId() != Long.valueOf(match.group(1)))
						this.pojoMov = this.ifzMov.findById(Long.valueOf(match.group(1)));
			
			this.pojoMovCtas.setIdTiposMovimiento(this.pojoMov); // .setTiposMovtoId(this.pojoMov);

			this.ifzMovCtas.setInfoSesion(this.loginManager.getInfoSesion());
			if (registroNuevo) {
				this.pojoMovCtas.setTipo("M");
				this.pojoMovCtas.setEstatus("G");
				this.pojoMovCtas.setCreadoPor(this.usuarioId);
				this.pojoMovCtas.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoMovCtas.setModificadoPor(this.usuarioId);
				this.pojoMovCtas.setFechaModificacion(Calendar.getInstance().getTime());

				/*if(! this.ifzCtas.haySaldoSuficiente(this.pojoMovCtas.getMonto(), this.pojoCtas.getId())) {
					this.tipoMensaje = 5;
					return "OK";
				}*/

				res = this.ifzMovCtas.salvar(this.pojoMovCtas);
				if (res.getResultado() == 0) {
					pojoMovCtas.setId(Long.valueOf(res.getReferencia()));
					this.listMovCtas.add(this.pojoMovCtas);	
				}
			} else {
				this.pojoMovCtas.setModificadoPor(this.usuarioId);
				this.pojoMovCtas.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzMovCtas.update(this.pojoMovCtas);
				for (PagosGastosExt var : this.listMovCtas) {
					if (var.getId().equals(this.pojoMovCtas.getId())) {
						var = this.pojoMovCtas;
						break;
					}
				}
			}
		} catch (Exception e) {
			control(true, 3, "Ocurrio un problema al intentar guardar el Movimiento entre CUentas", e);
		}
	}

	public void cancelar() {
		Respuesta resp = new Respuesta();
		
		try {
			control();
			resp = this.ifzMovCtas.cancelacion(this.pojoMovCtas, Calendar.getInstance().getTime());
			if (resp.getResultado() == -1) {
				control(1, resp.getReferencia());
				return;					
			} 
			
			for(PagosGastosExt var : this.listMovCtas) {
				if (var.getId().equals(this.pojoMovCtas.getId())) {
					var.setEstatus("X");
					break;
				}
			}
		} catch (Exception e) {
			control(true, 3, "Ocurrio un problema al intentar Cancelar el Movimiento entre Cuentas", e);
		}
	}

	public void evaluaCancelar() {
		try {
			control();
			if ("X".equals(this.pojoMovCtas.getEstatus())) {
				this.movCtaCancelado = true;
				this.tipoMensaje = 2;
				control(2, "Movimientos entre Cuentas previamente Cancelado");
			} 
		} catch (Exception e) {
			control("Ocurrio un problema al intentar evaluar el estatus de Movimiento entre Cuentas seleccionado", e);
		}
	}

	public List<CuentaBancaria> autoacompletaCuenta (Object obj) {
		try {
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCtas = this.ifzCtas.findLikeClaveNombreCuenta(obj.toString(), 100, this.sucursalesVisibles, this.loginManager.getInfoSesion().getEmpresa().getId());
			return this.listCtas;
		} catch (Exception e) {
			log.error("error al autoacompletaCuenta", e);
			return new ArrayList<CuentaBancaria>();
		}
	}

	public List<ConValores> autoacompletaMovimientos (Object obj) {
		try {
			this.ifzMov.setInfoSesion(this.loginManager.getInfoSesion());
			this.listMov = this.ifzMov.findLikeValorIdPropiedadGrupo(obj.toString(), this.pojoGpoVal, 100);
			return this.listMov;
		} catch (Exception e) {
			log.error("error al autoacompletaMovimientos", e);
			return new ArrayList<ConValores>();
		}
	}

	public void cargarTipoMovimientos() {
		try {
			if (this.listMov == null)
				this.listMov = new ArrayList<ConValores>();
			this.listMov.clear();
			
			if (this.listMovItems == null)
				this.listMovItems = new ArrayList<SelectItem>();
			this.listMovItems.clear();
			
			if (this.pojoGpoVal == null || this.pojoGpoVal.getId() <= 0L)
				return;

			this.ifzMov.setInfoSesion(this.loginManager.getInfoSesion());
			this.listMov = this.ifzMov.findAll(this.pojoGpoVal);
			if (this.listMov != null && ! this.listMov.isEmpty()) {
				for (ConValores fp : this.listMov)
					this.listMovItems.add(new SelectItem(fp.getValor(), fp.getDescripcion()));
			}
		} catch (Exception e) {
			log.error("error en cargarTipoDeMovimiento", e);
		}
	}
	
	private void control() {
		this.band = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}

	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.band = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		log.error("\n\nMOVIMIENTOS DE CUENTAS CXP :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n", throwable);
	}

	// -----------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -----------------------------------------------------------------------------------------------------
	
	public PagosGastosExt getPojoMovCtas() {
		return pojoMovCtas;
	}

	public void setPojoMovCtas(PagosGastosExt pojoMovCtas) {
		this.pojoMovCtas = pojoMovCtas;
	}

	public ConValores getPojoMov() {
		return pojoMov;
	}

	public void setPojoMov(ConValores pojoMov) {
		this.pojoMov = pojoMov;
	}

	public Long getPagosGastosId() {
		return this.pojoMovCtas.getId() != null ? this.pojoMovCtas.getId() : 0;
	}

	public void setPagosGastosId(Long pagosGastosId) {
		this.pojoMovCtas.setId(pagosGastosId);
	}

	public Date getFecha() {
		return this.pojoMovCtas.getFecha() != null ? this.pojoMovCtas.getFecha() : Calendar.getInstance().getTime();
	}

	public void setFecha(Date fecha) {
		this.pojoMovCtas.setFecha(fecha);
	}

	public Double getMonto() {
		return this.pojoMovCtas.getMonto() != null ? this.pojoMovCtas.getMonto() : 0;
	}

	public void setMonto(Double monto) {
		this.pojoMovCtas.setMonto(monto);
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getCuenta2() {
		return cuenta2;
	}

	public void setCuenta2(String cuenta2) {
		this.cuenta2 = cuenta2;
	}

	public String getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(String movimiento) {
		this.movimiento = movimiento;
	}

	public String getDescripcionCuenta() {
		return descripcionCuenta;
	}

	public void setDescripcionCuenta(String descripcionCuenta) {
		this.descripcionCuenta = descripcionCuenta;
	}

	public String getDescripcionCuenta2() {
		return descripcionCuenta2;
	}

	public void setDescripcionCuenta2(String descripcionCuenta2) {
		this.descripcionCuenta2 = descripcionCuenta2;
	}

	public ConGrupoValores getPojoGpoVal() {
		return pojoGpoVal;
	}
	
	public void setPojoGpoVal(ConGrupoValores pojoGpoVal) {
		this.pojoGpoVal = pojoGpoVal;
	}

	public CtasBancoExt getPojoCtas() {
		return pojoCtas;
	}

	public void setPojoCtas(CtasBancoExt pojoCtas) {
		this.pojoCtas = pojoCtas;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getValTipoBusqueda() {
		return valorBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valorBusqueda = valTipoBusqueda;
	}

	public long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public List<PagosGastosExt> getListMovCtas() {
		return listMovCtas;
	}

	public void setListMovCtas(List<PagosGastosExt> listMovCtas) {
		this.listMovCtas = listMovCtas;
	}

	public List<ConValores> getListMov() {
		return listMov;
	}

	public void setListMov(List<ConValores> listMov) {
		this.listMov = listMov;
	}
	public List<SelectItem> getlistMovItems() {
		return listMovItems;
	}

	public void setlistMovItems(List<SelectItem> listMovItems) {
		this.listMovItems = listMovItems;
	}

	/*public List<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(List<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}*/

	public boolean isBand() {
		return band;
	}

	public void setBand(boolean band) {
		this.band = band;
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

	public boolean isMovCtaCancelado() {
		return movCtaCancelado;
	}

	public void setMovCtaCancelado(boolean movCtaCancelado) {
		this.movCtaCancelado = movCtaCancelado;
	}

	public boolean isMuestraSalvar() {
		return muestraSalvar;
	}

	public void setMuestraSalvar(boolean muestraSalvar) {
		this.muestraSalvar = muestraSalvar;
	}

	public boolean isHabilita() {
		return habilita;
	}

	public void setHabilita(boolean habilita) {
		this.habilita = habilita;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getReferencia() {
		return this.pojoMovCtas.getFolioAutorizacion() != null ? this.pojoMovCtas.getFolioAutorizacion() : "";
	}

	public void setReferencia(String referencia) {
		this.pojoMovCtas.setFolioAutorizacion(referencia);
	}


	public String getMensajeDetalles() {
		return mensajeDetalles;
	}


	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}


	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}
		
	private Double recuperaTipoCambioActual(long idMonedaOrigen, long idMonedaDestino) {
		//Moneda monedaOrigen = null;
		//Moneda monedaDestino = null;
		MonedasValores valor = null;
		Double tipoCambio = 1.0;

		try {
			if (idMonedaOrigen > 0L && idMonedaDestino > 0L) {
				// Recupero moneda base
				/*for (Moneda var : this.listMonedas) {
					if (var.getId() != idMonedaOrigen) 
						continue;
					monedaOrigen = var;
					break;
				}
				
				// Recupero moneda destino
				for (Moneda var : this.listMonedas) {
					if (var.getId() != idMonedaDestino) 
						continue;
					monedaDestino = var;
					break;
				}*/
				
				// COmpruebo monedas y recupero el tipo de cambio actual si corresponde
				//if (monedaOrigen != null && monedaDestino != null) {
					valor = this.ifzMonValores.findActual(idMonedaOrigen, idMonedaDestino);
					tipoCambio = valor.getValor().doubleValue();
				//}
			}
		} catch (Exception e) {
			log.error("No se pudo obtener el Tipo de Cambio actual para la conversion de " + idMonedaOrigen + " a " + idMonedaDestino, e);
			tipoCambio = 1.0;
		}
		
		return tipoCambio;
	}
	
	public void evaluaTipoCambio() {
		CtasBancoExt pojoCuenta = null;
		Pattern pat = null;
		Matcher match = null;
		if (this.evaluaConversion)
			return;

		/* validando Cuenta */
		pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		match = pat.matcher(this.cuenta);			
		if (match.find())
				pojoCuenta = this.ifzMovCtas.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzCtas.findAllById(Short.valueOf(match.group(1)));
		long monedaOrigen = pojoCuenta.getMoneda().getId();

		match = pat.matcher(this.cuenta2);			
		if (match.find())
				pojoCuenta = this.ifzMovCtas.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzCtas.findAllById(Short.valueOf(match.group(1)));
		long monedaDestino = pojoCuenta.getMoneda().getId();
		
		this.tipoCambioActual = recuperaTipoCambioActual(monedaOrigen, monedaDestino);
		if (this.tipoCambioActual != this.pojoMovCtas.getTipoCambio().doubleValue()) {
			this.evaluaConversion = true;
		}
		this.pojoMovCtas.setTipoCambio(this.tipoCambioActual);
	}
}

// HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 22/06/2016 | Javier Tirado	| Creacion de MovimientosCuentasAction
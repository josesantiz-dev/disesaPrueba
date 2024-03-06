package net.giro.cxp;

import java.io.Serializable;
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
import net.giro.tyg.logica.CuentasBancariasRem;

public class MovimientosCuentasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MovimientosCuentasAction.class);
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
	private List<CuentaBancaria> listCtas;
	private List<PagosGastosExt>	listMovCtas;
	private List<String>	tipoBusqueda;

	private InitialContext ctx;

	private  Long valGpo;
	private String operacion;
	private String mensaje;
	private String descripcionCuenta;
	private String descripcionCuenta2;
	private String movimiento;	
	private String cuenta;
	private String cuenta2;
	private String campoBusqueda;	
	private String valTipoBusqueda;
	private String sucursalesVisibles;
	//private String referencia;
	private long usuarioId;	
	private boolean band;	
	private boolean muestraSalvar;
	private boolean habilita;
	private int 	numPagina;
	private int tipoMensaje;

	private boolean movCtaCancelado;

	
	public MovimientosCuentasAction() throws NamingException, Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		this.loginManager = (LoginManager)ve.getValue(fc.getELContext());
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());

		usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId(); //.getLoginBean().getUsuario().getUsuarioId();
		//sucursalesVisibles = lm.getLoginBean().getSucursalesVisibles();
		
		this.ctx = new InitialContext();
		this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
		this.ifzMovCtas = (PagosGastosRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
		this.ifzCtas = (CuentasBancariasRem) this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
		this.ifzMov = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");

		this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzMovCtas.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzMov.setInfoSesion(this.loginManager.getInfoSesion());
		
		tipoBusqueda = new ArrayList<String>();
		tipoBusqueda.add("Fecha del movimiento");
		tipoBusqueda.add("Cuenta bancaria");
		tipoBusqueda.add("Nombre del movimiento");		
		valTipoBusqueda ="Fecha del movimiento";

		this.pojoMovCtas = new PagosGastosExt();
		this.pojoGpoVal = new ConGrupoValores();
		this.pojoMov = new ConValores();
		this.pojoCtas = new CtasBancoExt();
		listMovCtas = new ArrayList<PagosGastosExt>();
		listCtas = new ArrayList<CuentaBancaria>();
		listMov = new ArrayList<ConValores>();

		this.numPagina = 1;
		tipoMensaje = 0;
		band = false;
		mensaje = "";
		movCtaCancelado=false;
		operacion="T";

		if ( entornoProperties.getString("SYS_MOVCTAS") == null || "".equals(entornoProperties.getString("SYS_MOVCTAS")) )
			throw new Exception("No se encontro encontro el grupo SYS_MOVCTAS en con_grupo_valores");
		else
			this.valGpo = Long.valueOf(entornoProperties.getString("SYS_MOVCTAS")) ;

		this.pojoGpoVal = this.ifzGpoVal.findById(valGpo);
		if (this.pojoGpoVal==null){
			throw new Exception("No se encontro encontro el grupo SYS_MOVCTAS en con_grupo_valores");
		}
	}

	
	public String nuevo() {
		try{
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
			log.error("Error en el metodo nuevo.", e);
			return "ERROR";
		}

		return "OK";
	}

	public String evaluaCancelar() {
		try {
			if ("X".equals(this.pojoMovCtas.getEstatus())) {
				movCtaCancelado = true;
				this.tipoMensaje = 2;
			} else {
				this.tipoMensaje = 0;
				movCtaCancelado = false;
			}
		} catch (Exception e) {
			log.error("Error en el metodo evaluaCancelar.", e);
			return "ERROR";
		}

		return "OK";
	}

	public String cancelar() {
		try{
			Respuesta resp = new Respuesta();
			resp = ifzMovCtas.cancelacion(this.pojoMovCtas, Calendar.getInstance().getTime());

			if (resp.getResultado() == -1) {
				band = true;
				this.mensaje = resp.getRespuesta();
				this.tipoMensaje=1;					
			} else {
				for(PagosGastosExt var : this.listMovCtas) {
					if (var.getId().equals(this.pojoMovCtas.getId())) {
						var.setEstatus("X");
						break;
					}
				}
			}
		} catch (Exception e) {
			mensaje="";
			tipoMensaje=3;
			band=true;
			log.error("error en cancelar", e);
			return "ERROR";
		}

		return "OK";
	}

	public String guardar() {
		try {
			Respuesta res;
			Pattern pat = null;
			Matcher match = null;
			boolean registroNuevo = false;

			this.tipoMensaje = 0;	
			band = false;

			if (this.pojoMovCtas.getId() == null)
				registroNuevo=true;

			this.pojoMovCtas.setModificadoPor(this.usuarioId);
			this.pojoMovCtas.setFechaModificacion(Calendar.getInstance().getTime());

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

			if (registroNuevo) {
				this.pojoMovCtas.setCreadoPor(this.usuarioId);
				this.pojoMovCtas.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoMovCtas.setTipo("M");
				this.pojoMovCtas.setEstatus("G");

				if(! this.ifzCtas.haySaldoSuficiente(this.pojoMovCtas.getMonto(), this.pojoCtas.getId())) {
					this.tipoMensaje = 5;
					return "OK";
				}

				res = this.ifzMovCtas.salvar(this.pojoMovCtas, false);
				if (res.getResultado() == 0) {
					pojoMovCtas.setId(Long.valueOf(res.getReferencia()));
					this.listMovCtas.add(this.pojoMovCtas);	
				}
			} else {
				this.ifzMovCtas.update(this.pojoMovCtas);
				for (PagosGastosExt var : this.listMovCtas) {
					if (var.getId().equals(this.pojoMovCtas.getId())) {
						var = this.pojoMovCtas;
						break;
					}
				}
			}
		} catch (Exception e) {
			mensaje = "";
			tipoMensaje = 3;
			band = true;
			log.error("error al guardar", e);
			return "ERROR";
		}

		return "OK";
	}

	public String editar() {
		try {
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
			log.error("error al editar", e);
			return "ERROR";
		}

		return "OK";
	}

	public String buscar(){
		try {
			tipoMensaje = 0;
			band = false;

			if( "Fecha del movimiento".equals(this.valTipoBusqueda)) {
				this.listMovCtas = this.ifzMovCtas.findLikeCajaChicaExt("fecha",this.campoBusqueda,"M","G",50,this.sucursalesVisibles);
			} else {
				if("Cuenta bancaria".equals(this.valTipoBusqueda)) {
					this.listMovCtas = this.ifzMovCtas.findExtLikeMovCtas("cuentaOrigen",this.campoBusqueda,"M","G",50,this.sucursalesVisibles);//this.ifzMovCtas.findLikeCajaChicaExt("cuentaOrigen",this.campoBusqueda,"M","G",50,this.sucursalesVisibles);
				} else {
					if("Nombre del movimiento".equals(this.valTipoBusqueda))
						this.listMovCtas = this.ifzMovCtas.findExtLikeMovCtas("tiposMovtoId",this.campoBusqueda,"M","G",50,this.pojoGpoVal.getNombre());
					else
						this.listMovCtas = this.ifzMovCtas.findLikeCajaChicaExt("fecha","","M","G",50,this.sucursalesVisibles);
				}
			}

			if (listMovCtas.isEmpty()) {
				tipoMensaje = 4;
				band = true;
			}
		} catch (Exception e) {
			tipoMensaje = 3;
			band = true;
			log.error("error al buscar", e);
			return "ERROR";
		}

		return "OK";
	}

	public List<CuentaBancaria> autoacompletaCuenta (Object obj) {
		try {
			this.listCtas = this.ifzCtas.findLikeClaveNombreCuenta(obj.toString(), 20, this.sucursalesVisibles, null);
			return this.listCtas;
		} catch (Exception e) {
			log.error("error al autoacompletaCuenta", e);
			return new ArrayList<CuentaBancaria>();
		}
	}

	public List<ConValores> autoacompletaMovimientos (Object obj) {
		try {
			this.listMov = this.ifzMov.findLikeValorIdPropiedadGrupo(obj.toString(), this.pojoGpoVal, 20);
			return this.listMov;
		} catch (Exception e) {
			log.error("error al autoacompletaMovimientos", e);
			return new ArrayList<ConValores>();
		}
	}

	public PagosGastosExt getPojoMovCtas() {
		return pojoMovCtas;
	}

	public void setPojoMovCtas(PagosGastosExt pojoMovCtas) {
		this.pojoMovCtas = pojoMovCtas;

		/*if (this.pojoMovCtas.getIdCuentaOrigen() != null) {
			this.cuenta = this.pojoMovCtas.getIdCuentaOrigen().getId()
					+ " - " + this.pojoMovCtas.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto()
					+ " - " + this.pojoMovCtas.getIdCuentaOrigen().getNumeroDeCuenta();
			
			this.descripcionCuenta = this.pojoMovCtas.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto()
				+ " - " + this.pojoMovCtas.getIdCuentaOrigen().getNumeroDeCuenta();
		}
		
		if (this.pojoMovCtas.getIdTiposMovimiento() != null) {
			this.movimiento = this.pojoMovCtas.getIdTiposMovimiento().getId() 
					+ " - " + this.pojoMovCtas.getIdTiposMovimiento().getDescripcion();
		}*/
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
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
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

	public List<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(List<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

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
}

// HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 22/06/2016 | Javier Tirado	| Creacion de MovimientosCuentasAction
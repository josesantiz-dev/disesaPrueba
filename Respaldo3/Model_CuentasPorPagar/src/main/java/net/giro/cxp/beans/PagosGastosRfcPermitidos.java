package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * pagos_gastos_rfc_permitidos
 * @author javaz
 *
 */
public class PagosGastosRfcPermitidos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String rfc; // RFC Permitido
	private String tipoPagosGastos; // Tipo Pagos Gastos: C-CajaChica, P-Registro Gastos, G-Gastos a Comprobar, F-Provision de Facturas
	private String tipoComprobante; // Tipo Comprobante: I-Ingreso, E-Egreso, P-Pago
	private String metodoPagoComprobante; // Metodo Pago Comprobante: PUE, PPD
	private int permitido;
	private int determinado;
	private Date fechaDesde;
	private Date fechaHasta;
	private long idEmpresa;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public PagosGastosRfcPermitidos() {
		this.rfc = "";
		this.tipoPagosGastos = "";
		this.metodoPagoComprobante = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	/**
	 * Tipo Pagos Gastos: C-CajaChica, P-Registro Gastos, G-Gastos a Comprobar, F-Provision de Facturas
	 * @param tipoPagosGastos
	 */
	public String getTipoPagosGastos() {
		return tipoPagosGastos;
	}

	/**
	 * Tipo Pagos Gastos: C-CajaChica, P-Registro Gastos, G-Gastos a Comprobar, F-Provision de Facturas
	 * @param tipoPagosGastos
	 */
	public void setTipoPagosGastos(String tipoPagosGastos) {
		this.tipoPagosGastos = tipoPagosGastos;
	}

	/**
	 * Tipo Comprobante: I-Ingreso, E-Egreso, P-Pago
	 * @return
	 */
	public String getTipoComprobante() {
		return tipoComprobante;
	}

	/**
	 * Tipo Comprobante: I-Ingreso, E-Egreso, P-Pago
	 * @param tipoComprobante
	 */
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	/**
	 * Metodo de Pago Comprobante: PUE, PPD
	 * @param metodoPagoComprobante
	 */
	public String getMetodoPagoComprobante() {
		return metodoPagoComprobante;
	}

	/**
	 * Metodo de Pago Comprobante: PUE, PPD
	 * @param metodoPagoComprobante
	 */
	public void setMetodoPagoComprobante(String metodoPagoComprobante) {
		this.metodoPagoComprobante = metodoPagoComprobante;
	}

	public int getPermitido() {
		return permitido;
	}

	public void setPermitido(int permitido) {
		this.permitido = permitido;
	}

	public int getDeterminado() {
		return determinado;
	}

	public void setDeterminado(int determinado) {
		this.determinado = determinado;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}

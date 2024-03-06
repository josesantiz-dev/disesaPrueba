package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.cxc.util.FACTURA_PAGO_ESTATUS;

/**
 * factura_pagos
 * @author javaz
 */
public class FacturaPagos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date fecha;
	private Factura idFactura;
	private Long idBeneficiario;
	private String beneficiario;
	private String tipoBeneficiario;
	private Long idFormaPago;
	private String formaPago;
	private String referenciaFormaPago;
	private Long idBancoOrigen; 
	private BigDecimal pagoRecibido;
	private BigDecimal diferencia;
	private BigDecimal pago;
	private Long idMoneda;
	private String moneda;
	private double tipoCambio;
	private Long idCuentaDeposito;
	private String cuentaBanco;
	private String cuentaNumero;
	private int parcialidad;
	private BigDecimal saldoAnterior;
	private BigDecimal saldoInsoluto;
	private String observaciones;
	// -----------------------------
	private int timbrado; // 0-Sin Timbrar, 1-Timbrado, 2-Timbre Cancelado
	private long timbradoPor;
	private long idTimbre;
	private String uuid;
	private String serie;
	private String folio;
	private String agrupador;
	// -----------------------------
	private long canceladoPor;	
	private Date fechaCancelacion;
	// -----------------------------
	private int sistema; // SISTEMA: 0-Normal, 1-Sistema
	private int estatus; // ESTATUS: 0-Activo, 1-Cancelado, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;	
	private Date fechaModificacion;
	
	public FacturaPagos() {
		this.pago = BigDecimal.ZERO;
		this.diferencia = BigDecimal.ZERO;
		this.pagoRecibido = BigDecimal.ZERO;
		this.saldoAnterior = BigDecimal.ZERO;
		this.saldoInsoluto = BigDecimal.ZERO;
		this.beneficiario = "";
		this.tipoBeneficiario = "";
		this.formaPago = "";
		this.referenciaFormaPago = "";
		this.moneda = "";
		this.cuentaBanco = "";
		this.cuentaNumero = "";
		this.observaciones = "";
		this.uuid = "";
		this.serie = "";
		this.folio = "";
		this.agrupador = "";
		this.fecha = Calendar.getInstance().getTime();
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Factura getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Factura idFactura) {
		this.idFactura = idFactura;
		if (idFactura != null)
			setSaldoAnterior(idFactura.getSaldo());
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		if (beneficiario == null)
			beneficiario = "";
		this.beneficiario = beneficiario;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		if (tipoBeneficiario == null)
			tipoBeneficiario = "";
		this.tipoBeneficiario = tipoBeneficiario;
	}

	public Long getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(Long idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		if (formaPago == null)
			formaPago = "";
		this.formaPago = formaPago;
	}

	public String getReferenciaFormaPago() {
		return referenciaFormaPago;
	}

	public void setReferenciaFormaPago(String referenciaFormaPago) {
		if (referenciaFormaPago == null)
			referenciaFormaPago = "";
		this.referenciaFormaPago = referenciaFormaPago;
	}

	public Long getIdBancoOrigen() {
		return idBancoOrigen;
	}

	public void setIdBancoOrigen(Long idBancoOrigen) {
		this.idBancoOrigen = idBancoOrigen;
	}

	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}

	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		if (saldoAnterior == null)
			saldoAnterior = BigDecimal.ZERO;
		this.saldoAnterior = saldoAnterior;
	}

	public BigDecimal getPagoRecibido() {
		return pagoRecibido;
	}

	public void setPagoRecibido(BigDecimal pagoRecibido) {
		if (pagoRecibido == null)
			pagoRecibido = BigDecimal.ZERO;
		this.pagoRecibido = pagoRecibido;
	}

	public BigDecimal getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(BigDecimal diferencia) {
		if (diferencia == null)
			diferencia = BigDecimal.ZERO;
		this.diferencia = diferencia;
	}

	public BigDecimal getPago() {
		return pago;
	}

	public void setPago(BigDecimal pago) {
		if (pago == null)
			pago = BigDecimal.ZERO;
		this.pago = pago;
		calculaInsoluto();
	}

	public BigDecimal getSaldoInsoluto() {
		return saldoInsoluto;
	}

	public void setSaldoInsoluto(BigDecimal saldoInsoluto) {
		if (saldoInsoluto == null)
			saldoInsoluto = BigDecimal.ZERO;
		this.saldoInsoluto = saldoInsoluto;
	}

	public Long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		if (moneda == null)
			moneda = "";
		this.moneda = moneda;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public Long getIdCuentaDeposito() {
		return idCuentaDeposito;
	}

	public void setIdCuentaDeposito(Long idCuentaDeposito) {
		this.idCuentaDeposito = idCuentaDeposito;
	}

	public String getCuentaBanco() {
		return cuentaBanco;
	}

	public void setCuentaBanco(String cuentaBanco) {
		if (cuentaBanco == null)
			cuentaBanco = "";
		this.cuentaBanco = cuentaBanco;
	}

	public String getCuentaNumero() {
		return cuentaNumero;
	}

	public void setCuentaNumero(String cuentaNumero) {
		if (cuentaNumero == null)
			cuentaNumero = "";
		this.cuentaNumero = cuentaNumero;
	}

	public int getParcialidad() {
		return parcialidad;
	}

	public void setParcialidad(int parcialidad) {
		this.parcialidad = parcialidad;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		if (observaciones == null)
			observaciones = "";
		this.observaciones = observaciones;
	}
	
	/**
	 * TIMBRADO: 0-Sin Timbrar, 1-Timbrado, 2-Timbre Cancelado
	 * @return
	 */
	public int getTimbrado() {
		return timbrado;
	}

	/**
	 * TIMBRADO: 0-Sin Timbrar, 1-Timbrado, 2-Timbre Cancelado
	 * @param timbrado
	 */
	public void setTimbrado(int timbrado) {
		this.timbrado = timbrado;
	}

	public long getTimbradoPor() {
		return timbradoPor;
	}

	public void setTimbradoPor(long timbradoPor) {
		this.timbradoPor = timbradoPor;
	}

	public long getIdTimbre() {
		return idTimbre;
	}

	public void setIdTimbre(long idTimbre) {
		this.idTimbre = idTimbre;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		if (uuid == null)
			uuid = "";
		this.uuid = uuid;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		if (serie == null)
			serie = "";
		this.serie = serie;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		if (folio == null)
			folio = "";
		this.folio = folio;
	}

	public String getAgrupador() {
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		if (agrupador == null)
			agrupador = "";
		this.agrupador = agrupador;
	}

	/**
	 * SISTEMA: 0-Normal, 1-Sistema
	 * @return
	 */
	public int getSistema() {
		return sistema;
	}

	/**
	 * SISTEMA: 0-Normal, 1-Sistema
	 * @param sistema
	 */
	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	/**
	 * ESTATUS: 0-Activo, 1-Cancelado, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 0-Activo, 1-Cancelado, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	/**
	 * ESTATUS: 0-Activo, 1-Cancelado, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	 * @param estatus
	 */
	public void setEstatus(FACTURA_PAGO_ESTATUS estatus) {
		if (estatus != null)
			this.estatus = estatus.ordinal();
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

	public long getCanceladoPor() {
		return canceladoPor;
	}

	public void setCanceladoPor(long canceladoPor) {
		this.canceladoPor = canceladoPor;
	}

	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	// ----------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------
	
	public double getFacturado() {
		if (this.idFactura != null)
			return this.idFactura.getTotal().doubleValue();
		return 0;
	}
	
	public void setFacturado(double facturado) { }

	public String getEstatusDescripcion() {
		if (this.estatus == FACTURA_PAGO_ESTATUS.Cancelado.ordinal())
			return FACTURA_PAGO_ESTATUS.Cancelado.toString().toUpperCase();
		if (this.estatus == FACTURA_PAGO_ESTATUS.PendienteCancelacion.ordinal())
			return "PENDIENTE CANCELACION";
		if (this.estatus == FACTURA_PAGO_ESTATUS.PendienteAprobacion.ordinal())
			return "PENDIENTE APROBACION";
		return FACTURA_PAGO_ESTATUS.Activo.toString().toUpperCase();
	}
	
	public void setEstatusDescripcion(String value) {}

	public void calculaSaldo() {
		double facturado = 0;
		double saldo = 0;
		double anterior = 0;
		
		if (this.saldoAnterior == null)
			this.saldoAnterior = BigDecimal.ZERO;
		
		if (this.idFactura != null) {
			facturado = this.idFactura.getTotal().doubleValue();
			saldo = this.idFactura.getSaldo().doubleValue();
			if (saldo == facturado)
				anterior = facturado;
			else
				anterior = saldo;
			this.saldoAnterior = new BigDecimal(anterior);
		}
	}

	public void calculaInsoluto() {
		double insoluto = 0;
		
		if (this.saldoAnterior == null)
			this.saldoAnterior = BigDecimal.ZERO;
		if (this.pago == null)
			this.pago = BigDecimal.ZERO;

		if (this.saldoAnterior.doubleValue() > 0 && this.pago.doubleValue() > 0) {
			insoluto = this.saldoAnterior.doubleValue() - this.pago.doubleValue();
			this.saldoInsoluto = new BigDecimal(insoluto);
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
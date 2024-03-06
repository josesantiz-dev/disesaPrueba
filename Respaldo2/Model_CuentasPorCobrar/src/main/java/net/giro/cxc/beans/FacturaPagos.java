package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * factura_pagos
 * @author javaz
 *
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
	private BigDecimal monto;
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
	private int timbrado;
	private Date fechaTimbrado;
	private long codigo;
	private String mensaje;
	private String serie;
	private String folio;
	private String uuid;
	private byte[] xmlPrevio;
	private byte[] xmlTimbrado;
	private String cadenaOriginal;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;	
	private Date fechaModificacion;
	
	
	public FacturaPagos() {
		this.monto = BigDecimal.ZERO;
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
		this.mensaje = "";
		this.serie = "";
		this.folio = "";
		this.uuid = "";
		this.cadenaOriginal = "";
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public FacturaPagos(Long id) {
		this();
		this.id = id;
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
		this.beneficiario = beneficiario;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
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
		this.formaPago = formaPago;
	}

	public String getReferenciaFormaPago() {
		return referenciaFormaPago;
	}

	public void setReferenciaFormaPago(String referenciaFormaPago) {
		this.referenciaFormaPago = referenciaFormaPago;
	}

	public Long getIdBancoOrigen() {
		return idBancoOrigen;
	}

	public void setIdBancoOrigen(Long idBancoOrigen) {
		this.idBancoOrigen = idBancoOrigen;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
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
		this.cuentaBanco = cuentaBanco;
	}

	public String getCuentaNumero() {
		return cuentaNumero;
	}

	public void setCuentaNumero(String cuentaNumero) {
		this.cuentaNumero = cuentaNumero;
	}

	public int getParcialidad() {
		return parcialidad;
	}

	public void setParcialidad(int parcialidad) {
		this.parcialidad = parcialidad;
	}

	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}

	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}

	public BigDecimal getSaldoInsoluto() {
		return saldoInsoluto;
	}

	public void setSaldoInsoluto(BigDecimal saldoInsoluto) {
		this.saldoInsoluto = saldoInsoluto;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(int timbrado) {
		this.timbrado = timbrado;
	}

	public Date getFechaTimbrado() {
		return fechaTimbrado;
	}

	public void setFechaTimbrado(Date fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}

	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public byte[] getXmlPrevio() {
		return xmlPrevio;
	}

	public void setXmlPrevio(byte[] xmlPrevio) {
		this.xmlPrevio = xmlPrevio;
	}

	public byte[] getXmlTimbrado() {
		return xmlTimbrado;
	}

	public void setXmlTimbrado(byte[] xmlTimbrado) {
		this.xmlTimbrado = xmlTimbrado;
	}

	public String getCadenaOriginal() {
		return cadenaOriginal;
	}

	public void setCadenaOriginal(String cadenaOriginal) {
		this.cadenaOriginal = cadenaOriginal;
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

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
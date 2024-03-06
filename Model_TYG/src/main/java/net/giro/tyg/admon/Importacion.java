package net.giro.tyg.admon;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * importacion
 * @author javaz
 *
 */
public class Importacion implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idNumero;
	private String referencia;
	private String referenciaOriginal;
	private Date fechapago;
	private String control;
	private BigDecimal capital;
	private BigDecimal normal;
	private BigDecimal moratorio;
	private BigDecimal intermediario;
	private BigDecimal gastos;
	private BigDecimal impuesto;
	private Long vencimiento;
	private Date fecVenc;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long banco;
	private BigDecimal aportaciones;
	private Long secPro;
	private String agenteId;
	private String sucursal;
	private Date fechaConci;
	private java.lang.Boolean devolucion;
	private String transaccionId;
	private String tarjetaOperativaId;
	private Long app;
	private Long transaccionVersionId;
	private String estatus;
	private Long cheque;
	private Long bancoOrigen;
	private Date fechaDeposito;
	private String observaciones;
	private Long garantiaLiquida;
	private Long formaPagoId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdNumero() {
		return idNumero;
	}

	public void setIdNumero(Long idNumero) {
		this.idNumero = idNumero;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getReferenciaOriginal() {
		return referenciaOriginal;
	}

	public void setReferenciaOriginal(String referenciaOriginal) {
		this.referenciaOriginal = referenciaOriginal;
	}

	public Date getFechapago() {
		return fechapago;
	}

	public void setFechapago(Date fechapago) {
		this.fechapago = fechapago;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	public BigDecimal getNormal() {
		return normal;
	}

	public void setNormal(BigDecimal normal) {
		this.normal = normal;
	}

	public BigDecimal getMoratorio() {
		return moratorio;
	}

	public void setMoratorio(BigDecimal moratorio) {
		this.moratorio = moratorio;
	}

	public BigDecimal getIntermediario() {
		return intermediario;
	}

	public void setIntermediario(BigDecimal intermediario) {
		this.intermediario = intermediario;
	}

	public BigDecimal getGastos() {
		return gastos;
	}

	public void setGastos(BigDecimal gastos) {
		this.gastos = gastos;
	}

	public BigDecimal getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(BigDecimal impuesto) {
		this.impuesto = impuesto;
	}

	public Long getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Long vencimiento) {
		this.vencimiento = vencimiento;
	}

	public Date getFecVenc() {
		return fecVenc;
	}

	public void setFecVenc(Date fecVenc) {
		this.fecVenc = fecVenc;
	}

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getBanco() {
		return banco;
	}

	public void setBanco(Long banco) {
		this.banco = banco;
	}

	public BigDecimal getAportaciones() {
		return aportaciones;
	}

	public void setAportaciones(BigDecimal aportaciones) {
		this.aportaciones = aportaciones;
	}

	public Long getSecPro() {
		return secPro;
	}

	public void setSecPro(Long secPro) {
		this.secPro = secPro;
	}

	public String getAgenteId() {
		return agenteId;
	}

	public void setAgenteId(String agenteId) {
		this.agenteId = agenteId;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public Date getFechaConci() {
		return fechaConci;
	}

	public void setFechaConci(Date fechaConci) {
		this.fechaConci = fechaConci;
	}

	public java.lang.Boolean getDevolucion() {
		return devolucion;
	}

	public void setDevolucion(java.lang.Boolean devolucion) {
		this.devolucion = devolucion;
	}

	public String getTransaccionId() {
		return transaccionId;
	}

	public void setTransaccionId(String transaccionId) {
		this.transaccionId = transaccionId;
	}

	public String getTarjetaOperativaId() {
		return tarjetaOperativaId;
	}

	public void setTarjetaOperativaId(String tarjetaOperativaId) {
		this.tarjetaOperativaId = tarjetaOperativaId;
	}

	public Long getApp() {
		return app;
	}

	public void setApp(Long app) {
		this.app = app;
	}

	public Long getTransaccionVersionId() {
		return transaccionVersionId;
	}

	public void setTransaccionVersionId(Long transaccionVersionId) {
		this.transaccionVersionId = transaccionVersionId;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Long getCheque() {
		return cheque;
	}

	public void setCheque(Long cheque) {
		this.cheque = cheque;
	}

	public Long getBancoOrigen() {
		return bancoOrigen;
	}

	public void setBancoOrigen(Long bancoOrigen) {
		this.bancoOrigen = bancoOrigen;
	}

	public Date getFechaDeposito() {
		return fechaDeposito;
	}

	public void setFechaDeposito(Date fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Long getGarantiaLiquida() {
		return garantiaLiquida;
	}

	public void setGarantiaLiquida(Long garantiaLiquida) {
		this.garantiaLiquida = garantiaLiquida;
	}

	public Long getFormaPagoId() {
		return formaPagoId;
	}

	public void setFormaPagoId(Long formaPagoId) {
		this.formaPagoId = formaPagoId;
	}

}

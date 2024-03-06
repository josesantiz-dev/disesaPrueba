package net.giro.tyg.admon;

public class Importacion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	java.lang.Long id;
	java.lang.Long idNumero;
	java.lang.String referencia;
	java.lang.String referenciaOriginal;
	java.util.Date fechapago;
	java.lang.String control;
	java.math.BigDecimal capital;
	java.math.BigDecimal normal;
	java.math.BigDecimal moratorio;
	java.math.BigDecimal intermediario;
	java.math.BigDecimal gastos;
	java.math.BigDecimal impuesto;
	java.lang.Long vencimiento;
	java.util.Date fecVenc;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long banco;
	java.math.BigDecimal aportaciones;
	java.lang.Long secPro;
	java.lang.String agenteId;
	java.lang.String sucursal;
	java.util.Date fechaConci;
	java.lang.Boolean devolucion;
	java.lang.String transaccionId;
	java.lang.String tarjetaOperativaId;
	java.lang.Long app;
	java.lang.Long transaccionVersionId;
	java.lang.String estatus;
	java.lang.Long cheque;
	java.lang.Long bancoOrigen;
	java.util.Date fechaDeposito;
	java.lang.String observaciones;
	java.lang.Long garantiaLiquida;
	java.lang.Long formaPagoId;


	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Long getIdNumero() {
		return idNumero;
	}

	public void setIdNumero(java.lang.Long idNumero) {
		this.idNumero = idNumero;
	}

	public java.lang.String getReferencia() {
		return referencia;
	}

	public void setReferencia(java.lang.String referencia) {
		this.referencia = referencia;
	}

	public java.lang.String getReferenciaOriginal() {
		return referenciaOriginal;
	}

	public void setReferenciaOriginal(java.lang.String referenciaOriginal) {
		this.referenciaOriginal = referenciaOriginal;
	}

	public java.util.Date getFechapago() {
		return fechapago;
	}

	public void setFechapago(java.util.Date fechapago) {
		this.fechapago = fechapago;
	}

	public java.lang.String getControl() {
		return control;
	}

	public void setControl(java.lang.String control) {
		this.control = control;
	}

	public java.math.BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(java.math.BigDecimal capital) {
		this.capital = capital;
	}

	public java.math.BigDecimal getNormal() {
		return normal;
	}

	public void setNormal(java.math.BigDecimal normal) {
		this.normal = normal;
	}

	public java.math.BigDecimal getMoratorio() {
		return moratorio;
	}

	public void setMoratorio(java.math.BigDecimal moratorio) {
		this.moratorio = moratorio;
	}

	public java.math.BigDecimal getIntermediario() {
		return intermediario;
	}

	public void setIntermediario(java.math.BigDecimal intermediario) {
		this.intermediario = intermediario;
	}

	public java.math.BigDecimal getGastos() {
		return gastos;
	}

	public void setGastos(java.math.BigDecimal gastos) {
		this.gastos = gastos;
	}

	public java.math.BigDecimal getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(java.math.BigDecimal impuesto) {
		this.impuesto = impuesto;
	}

	public java.lang.Long getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(java.lang.Long vencimiento) {
		this.vencimiento = vencimiento;
	}

	public java.util.Date getFecVenc() {
		return fecVenc;
	}

	public void setFecVenc(java.util.Date fecVenc) {
		this.fecVenc = fecVenc;
	}

	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.lang.Long getBanco() {
		return banco;
	}

	public void setBanco(java.lang.Long banco) {
		this.banco = banco;
	}

	public java.math.BigDecimal getAportaciones() {
		return aportaciones;
	}

	public void setAportaciones(java.math.BigDecimal aportaciones) {
		this.aportaciones = aportaciones;
	}

	public java.lang.Long getSecPro() {
		return secPro;
	}

	public void setSecPro(java.lang.Long secPro) {
		this.secPro = secPro;
	}

	public java.lang.String getAgenteId() {
		return agenteId;
	}

	public void setAgenteId(java.lang.String agenteId) {
		this.agenteId = agenteId;
	}

	public java.lang.String getSucursal() {
		return sucursal;
	}

	public void setSucursal(java.lang.String sucursal) {
		this.sucursal = sucursal;
	}

	public java.util.Date getFechaConci() {
		return fechaConci;
	}

	public void setFechaConci(java.util.Date fechaConci) {
		this.fechaConci = fechaConci;
	}

	public java.lang.Boolean getDevolucion() {
		return devolucion;
	}

	public void setDevolucion(java.lang.Boolean devolucion) {
		this.devolucion = devolucion;
	}

	public java.lang.String getTransaccionId() {
		return transaccionId;
	}

	public void setTransaccionId(java.lang.String transaccionId) {
		this.transaccionId = transaccionId;
	}

	public java.lang.String getTarjetaOperativaId() {
		return tarjetaOperativaId;
	}

	public void setTarjetaOperativaId(java.lang.String tarjetaOperativaId) {
		this.tarjetaOperativaId = tarjetaOperativaId;
	}

	public java.lang.Long getApp() {
		return app;
	}

	public void setApp(java.lang.Long app) {
		this.app = app;
	}

	public java.lang.Long getTransaccionVersionId() {
		return transaccionVersionId;
	}

	public void setTransaccionVersionId(java.lang.Long transaccionVersionId) {
		this.transaccionVersionId = transaccionVersionId;
	}

	public java.lang.String getEstatus() {
		return estatus;
	}

	public void setEstatus(java.lang.String estatus) {
		this.estatus = estatus;
	}

	public java.lang.Long getCheque() {
		return cheque;
	}

	public void setCheque(java.lang.Long cheque) {
		this.cheque = cheque;
	}

	public java.lang.Long getBancoOrigen() {
		return bancoOrigen;
	}

	public void setBancoOrigen(java.lang.Long bancoOrigen) {
		this.bancoOrigen = bancoOrigen;
	}

	public java.util.Date getFechaDeposito() {
		return fechaDeposito;
	}

	public void setFechaDeposito(java.util.Date fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}

	public java.lang.String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(java.lang.String observaciones) {
		this.observaciones = observaciones;
	}

	public java.lang.Long getGarantiaLiquida() {
		return garantiaLiquida;
	}

	public void setGarantiaLiquida(java.lang.Long garantiaLiquida) {
		this.garantiaLiquida = garantiaLiquida;
	}

	public java.lang.Long getFormaPagoId() {
		return formaPagoId;
	}

	public void setFormaPagoId(java.lang.Long formaPagoId) {
		this.formaPagoId = formaPagoId;
	}

}

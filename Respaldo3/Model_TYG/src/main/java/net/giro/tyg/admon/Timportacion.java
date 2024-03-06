package net.giro.tyg.admon;

/**
 * timportacion
 * @author javaz
 *
 */
public class Timportacion implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	java.lang.Long id;
	java.util.Date vfec;
	java.lang.String wreferencia;
	java.math.BigDecimal vvmonto;
	java.lang.Long tmovimiento;
	java.lang.String wsucursal;
	java.lang.String aplica;
	java.lang.Long idGasto;
	java.lang.String secuencias;
	java.lang.String noRecibo;
	java.lang.String transaccionId;
	java.lang.String tarjetaOperativaId;
	java.lang.Long app;
	java.lang.Long transaccionVersionId;


	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.util.Date getVfec() {
		return vfec;
	}

	public void setVfec(java.util.Date vfec) {
		this.vfec = vfec;
	}

	public java.lang.String getWreferencia() {
		return wreferencia;
	}

	public void setWreferencia(java.lang.String wreferencia) {
		this.wreferencia = wreferencia;
	}

	public java.math.BigDecimal getVvmonto() {
		return vvmonto;
	}

	public void setVvmonto(java.math.BigDecimal vvmonto) {
		this.vvmonto = vvmonto;
	}

	public java.lang.Long getTmovimiento() {
		return tmovimiento;
	}

	public void setTmovimiento(java.lang.Long tmovimiento) {
		this.tmovimiento = tmovimiento;
	}

	public java.lang.String getWsucursal() {
		return wsucursal;
	}

	public void setWsucursal(java.lang.String wsucursal) {
		this.wsucursal = wsucursal;
	}

	public java.lang.String getAplica() {
		return aplica;
	}

	public void setAplica(java.lang.String aplica) {
		this.aplica = aplica;
	}

	public java.lang.Long getIdGasto() {
		return idGasto;
	}

	public void setIdGasto(java.lang.Long idGasto) {
		this.idGasto = idGasto;
	}

	public java.lang.String getSecuencias() {
		return secuencias;
	}

	public void setSecuencias(java.lang.String secuencias) {
		this.secuencias = secuencias;
	}

	public java.lang.String getNoRecibo() {
		return noRecibo;
	}

	public void setNoRecibo(java.lang.String noRecibo) {
		this.noRecibo = noRecibo;
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

}

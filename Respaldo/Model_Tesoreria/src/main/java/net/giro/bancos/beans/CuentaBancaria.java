package net.giro.bancos.beans;

public class CuentaBancaria implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String numeroDeCuenta;
	java.lang.String clave;
	net.giro.bancos.beans.InstitucionBancaria institucionBancaria;
	net.giro.bancos.beans.SucursalBancaria sucursalBancaria;
	Long funcionario;
	Long puestoFuncionario;
	java.math.BigDecimal saldo;
	java.math.BigDecimal saldoMinimo;
	Long empresa;
	Long logo;
	long validaSaldo;
	long validaReferencia;
	java.lang.String reporteCheques;
	java.lang.String funcionGeneraDv;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public java.lang.String getNumeroDeCuenta() {
		return numeroDeCuenta;
	}

	public void setNumeroDeCuenta(java.lang.String numeroDeCuenta) {
		this.numeroDeCuenta = numeroDeCuenta;
	}

	public java.lang.String getClave() {
		return clave;
	}

	public void setClave(java.lang.String clave) {
		this.clave = clave;
	}

	public net.giro.bancos.beans.InstitucionBancaria getInstitucionBancaria() {
		return institucionBancaria;
	}

	public void setInstitucionBancaria(net.giro.bancos.beans.InstitucionBancaria institucionBancaria) {
		this.institucionBancaria = institucionBancaria;
	}

	public net.giro.bancos.beans.SucursalBancaria getSucursalBancaria() {
		return sucursalBancaria;
	}

	public void setSucursalBancaria(net.giro.bancos.beans.SucursalBancaria sucursalBancaria) {
		this.sucursalBancaria = sucursalBancaria;
	}

	public Long getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Long funcionario) {
		this.funcionario = funcionario;
	}

	public Long getPuestoFuncionario() {
		return puestoFuncionario;
	}

	public void setPuestoFuncionario(Long puestoFuncionario) {
		this.puestoFuncionario = puestoFuncionario;
	}

	public java.math.BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(java.math.BigDecimal saldo) {
		this.saldo = saldo;
	}

	public java.math.BigDecimal getSaldoMinimo() {
		return saldoMinimo;
	}

	public void setSaldoMinimo(java.math.BigDecimal saldoMinimo) {
		this.saldoMinimo = saldoMinimo;
	}

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}

	public Long getLogo() {
		return logo;
	}

	public void setLogo(Long logo) {
		this.logo = logo;
	}

	public long getValidaSaldo() {
		return validaSaldo;
	}

	public void setValidaSaldo(long validaSaldo) {
		this.validaSaldo = validaSaldo;
	}

	public long getValidaReferencia() {
		return validaReferencia;
	}

	public void setValidaReferencia(long validaReferencia) {
		this.validaReferencia = validaReferencia;
	}

	public java.lang.String getReporteCheques() {
		return reporteCheques;
	}

	public void setReporteCheques(java.lang.String reporteCheques) {
		this.reporteCheques = reporteCheques;
	}

	public java.lang.String getFuncionGeneraDv() {
		return funcionGeneraDv;
	}

	public void setFuncionGeneraDv(java.lang.String funcionGeneraDv) {
		this.funcionGeneraDv = funcionGeneraDv;
	}

}

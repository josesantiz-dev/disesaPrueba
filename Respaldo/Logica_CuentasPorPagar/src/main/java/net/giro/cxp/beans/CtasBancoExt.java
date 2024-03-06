package net.giro.cxp.beans;

import java.math.BigDecimal;
import java.util.Date;

import net.giro.ne.beans.Empresa;

public class CtasBancoExt implements java.io.Serializable {
	private static final long serialVersionUID = 4653961046680341266L;
	// Fields
	private long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private String numeroDeCuenta;
	private String clave;
	private CatBancosExt institucionBancaria;
	private SucursalBancariaExt sucursalBancaria;
	private BigDecimal saldo;
	private BigDecimal saldoMinimo;
	private Empresa empresa;
	private boolean validaSaldo;
	private boolean validaReferencia;
	private MonedaTYGExt moneda;
	// Constructors

	/** default constructor */
	
	
	public CtasBancoExt() {
	}

	/** minimal constructor */
	public CtasBancoExt(long id, String descripcion) {
		this.id = id;
	}

	// Property accessors
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	public Long getCreadoPor() {
		return this.creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

		public Long getModificadoPor() {
		return this.modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	public String getNumeroDeCuenta() {
		return numeroDeCuenta;
	}

	public void setNumeroDeCuenta(String numeroDeCuenta) {
		this.numeroDeCuenta = numeroDeCuenta;
	}

	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public CatBancosExt getInstitucionBancaria() {
		return institucionBancaria;
	}

	public void setInstitucionBancaria(CatBancosExt institucionBancaria) {
		this.institucionBancaria = institucionBancaria;
	}
	
	public SucursalBancariaExt getSucursalBancaria() {
		return sucursalBancaria;
	}

	public void setSucursalBancaria(SucursalBancariaExt sucursalBancaria) {
		this.sucursalBancaria = sucursalBancaria;
	}
	
		public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
		public BigDecimal getSaldoMinimo() {
		return saldoMinimo;
	}

	public void setSaldoMinimo(BigDecimal saldoMinimo) {
		this.saldoMinimo = saldoMinimo;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public boolean getValidaSaldo() {
		return validaSaldo;
	}

	public void setValidaSaldo(boolean validaSaldo) {
		this.validaSaldo = validaSaldo;
	}
	
	public boolean getValidaReferencia() {
		return validaReferencia;
	}

	public void setValidaReferencia(boolean validaReferencia) {
		this.validaReferencia = validaReferencia;
	}

	public MonedaTYGExt getMoneda() {
		return moneda;
	}

	public void setMoneda(MonedaTYGExt moneda) {
		this.moneda = moneda;
	}
}

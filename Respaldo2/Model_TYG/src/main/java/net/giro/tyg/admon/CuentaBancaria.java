package net.giro.tyg.admon;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

/**
 * a95f1327c6
 * @author javaz
 *
 */
public class CuentaBancaria implements java.io.Serializable {
	private static final long serialVersionUID = 4653961046680341266L;
	// Fields
	private long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private String numeroDeCuenta;
	private String clave;
	private Banco institucionBancaria;
	private SucursalBancaria sucursalBancaria;
	private BigDecimal saldo;
	private BigDecimal saldoMinimo;
	private Long idEmpresa;
	private Long validaSaldo;
	private Long validaReferencia;
	private Moneda moneda;
	
	
	public CuentaBancaria() {}

	public CuentaBancaria(long id, String descripcion) {
		this.id = id;
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getDescripcion() {
		if (this.institucionBancaria != null)
			return this.institucionBancaria.getNombreCorto();
		return "";
	}
	
	public void setDescripcion(String value) {}
	
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

	public Banco getInstitucionBancaria() {
		return institucionBancaria;
	}

	public void setInstitucionBancaria(Banco institucionBancaria) {
		this.institucionBancaria = institucionBancaria;
	}
	
	public SucursalBancaria getSucursalBancaria() {
		return sucursalBancaria;
	}

	public void setSucursalBancaria(SucursalBancaria sucursalBancaria) {
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
	
		public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	public Long getValidaSaldo() {
		return validaSaldo;
	}

	public void setValidaSaldo(Long validaSaldo) {
		this.validaSaldo = validaSaldo;
	}
	
	public Long getValidaReferencia() {
		return validaReferencia;
	}

	public void setValidaReferencia(Long validaReferencia) {
		this.validaReferencia = validaReferencia;
	}
	
	
	// transients ---------------------------------------------------------------------
	
	@Transient
	public boolean isTransientsValidaSaldo() {
		return this.validaSaldo != null && this.validaSaldo == 1;
	}

	public void setTransientsValidaSaldo(boolean transientsValidaSaldo) {
		this.validaSaldo = transientsValidaSaldo ? 1L : 0L;
	}
	
	@Transient
	public boolean isTransientsValidaReferencia() {
		return this.validaReferencia != null && this.validaReferencia == 1;
	}

	public void setTransientsValidaReferencia(boolean transientsValidaReferencia) {
		this.validaReferencia = transientsValidaReferencia ? 1L : 0L;
	}

	public Object getValidarImp() {
		return null;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}
}
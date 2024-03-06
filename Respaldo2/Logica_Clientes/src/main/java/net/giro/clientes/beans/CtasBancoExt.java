package net.giro.clientes.beans;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;




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
	private Long empresa;
	private Long validaSaldo;
	private Long validaReferencia;
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

	public void setInstitucionBancaria(Object institucionBancaria) throws Exception {
		CatBancosExt banco = new CatBancosExt();
		
		BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
		BeanUtils.copyProperties(banco, institucionBancaria);	
		
		this.institucionBancaria = banco;
	}
	
	public SucursalBancariaExt getSucursalBancaria() {
		return sucursalBancaria;
	}

	public void setSucursalBancaria(Object sucursalBancaria) throws Exception {
		SucursalBancariaExt sucursal = new SucursalBancariaExt();
		
		BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
		BeanUtils.copyProperties(sucursal, sucursalBancaria);
		
		this.sucursalBancaria = sucursal;
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
	
		public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
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

	public MonedaTYGExt getMoneda() {
		return moneda;
	}

	public void setMoneda(Object moneda) throws Exception {
		MonedaTYGExt monedaExt = new MonedaTYGExt();
		
		BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
		BeanUtils.copyProperties(monedaExt, moneda);
		
		this.moneda = monedaExt;
	}

}
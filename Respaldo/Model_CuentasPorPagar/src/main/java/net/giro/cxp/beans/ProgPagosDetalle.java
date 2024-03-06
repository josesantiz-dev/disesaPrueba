package net.giro.cxp.beans;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class ProgPagosDetalle implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long tiposGastos;
	private ProgPagos progPagos;
	private BigDecimal monto;
	private BigDecimal montoRev;
	private BigDecimal montoOriginal;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private String estatus;
	private Long restar;
	private Long control;
	private Long pagosGastosId;
	private Long idBeneficiario;
	private String tipoBeneficiario;
	private Long idOrdenCompra;
	
	
	public ProgPagosDetalle() {
		this.monto = BigDecimal.ZERO;
		this.montoRev = BigDecimal.ZERO;
		this.montoOriginal = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ProgPagosDetalle(Long id) {
		super();
		this.id = id;
		this.monto = BigDecimal.ZERO;
		this.montoRev = BigDecimal.ZERO;
		this.montoOriginal = BigDecimal.ZERO;
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

	public Long getTiposGastos() {
		return tiposGastos;
	}

	public void setTiposGastos(Long tiposGastos) {
		this.tiposGastos = tiposGastos;
	}

	public ProgPagos getProgPagos() {
		return progPagos;
	}

	public void setProgPagos(ProgPagos progPagos) {
		this.progPagos = progPagos;
	}

	public BigDecimal getMonto() {
		return (monto == null) ? BigDecimal.ZERO : monto;
	}

	public void setMonto(BigDecimal monto) {
		monto = (monto == null) ? BigDecimal.ZERO : monto;
		this.monto = monto;
	}

	public BigDecimal getMontoRev() {
		return (montoRev == null) ? BigDecimal.ZERO : montoRev;
	}

	public void setMontoRev(BigDecimal montoRev) {
		montoRev = (montoRev == null) ? BigDecimal.ZERO : montoRev;
		this.montoRev = montoRev;
	}

	public BigDecimal getMontoOriginal() {
		return (montoOriginal == null) ? BigDecimal.ZERO : montoOriginal;
	}

	public void setMontoOriginal(BigDecimal montoOriginal) {
		montoOriginal = (montoOriginal == null) ? BigDecimal.ZERO : montoOriginal;
		this.montoOriginal = montoOriginal;
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

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Long getRestar() {
		return restar;
	}

	public void setRestar(Long restar) {
		this.restar = restar;
	}

	public Long getControl() {
		return control;
	}

	public void setControl(Long control) {
		this.control = control;
	}

	public Long getPagosGastosId() {
		return pagosGastosId;
	}

	public void setPagosGastosId(Long pagosGastosId) {
		this.pagosGastosId = pagosGastosId;
	}
	
	@Override
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}

	public Long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(Long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}
}

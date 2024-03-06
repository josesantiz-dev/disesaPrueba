package net.giro.cxp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;

import net.giro.plataforma.beans.ConValores;

public class ProgPagosDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private ConValores tiposGastos;
	private ProgPagosExt progPagos;
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
	private PagosGastosExt pagosGastosId;
	private PersonaExt idBeneficiario;
	private String tipoBeneficiario;
	private Long idOrdenCompra; // OrdenCompra
	
	
	public ProgPagosDetalleExt() {
		this.monto = BigDecimal.ZERO;
		this.montoRev = BigDecimal.ZERO;
		this.montoOriginal = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ProgPagosDetalleExt(Long id) {
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

	public ConValores getTiposGastos() {
		return tiposGastos;
	}

	public void setTiposGastos(ConValores tiposGastos) {
		this.tiposGastos = tiposGastos;
	}

	public ProgPagosExt getProgPagos() {
		return progPagos;
	}

	public void setProgPagos(ProgPagosExt progPagos) {
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
		return(montoRev == null) ? BigDecimal.ZERO :  montoRev;
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

	public PagosGastosExt getPagosGastosId() {
		return pagosGastosId;
	}

	public void setPagosGastosId(PagosGastosExt pagosGastosId) {
		this.pagosGastosId = pagosGastosId;
	}
	
	@Override
	public ProgPagosDetalleExt clone(){  
	    try{
	    	ProgPagosDetalleExt dest = new ProgPagosDetalleExt();
	    	BeanUtils.copyProperties(dest, this);
	        return dest;  
	    }catch(Exception e){ 
	    	e.printStackTrace();
			return null;
	    }
	}

	public PersonaExt getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(PersonaExt idBeneficiario) {
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

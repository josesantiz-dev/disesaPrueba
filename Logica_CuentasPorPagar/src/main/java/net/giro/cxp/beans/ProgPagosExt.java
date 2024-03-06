package net.giro.cxp.beans;

import java.math.BigDecimal;
import java.util.Date;

import net.giro.ne.beans.Empresa;

public class ProgPagosExt implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private SucursalExt agente;
	private Date del;
	private Date al;
	private BigDecimal total;
	private String estatus;
	private Long transferenciaId;
	private BigDecimal montoAutorizado;
	private String observaciones;
	private BigDecimal montoRevisado;
	private Long revisadoPor;
	private Long numLote;
	private Date fechaRevision;
	private Empresa idEmpresa;
	private PersonaExt beneficiarioId;
	private String tipoBeneficiario;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public SucursalExt getAgente() {
		return agente;
	}

	public void setAgente(SucursalExt agente) {
		this.agente = agente;
	}

	public Date getDel() {
		return del;
	}

	public void setDel(Date del) {
		this.del = del;
	}

	public Date getAl() {
		return al;
	}

	public void setAl(Date al) {
		this.al = al;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Long getTransferenciaId() {
		return transferenciaId;
	}

	public void setTransferenciaId(Long transferenciaId) {
		this.transferenciaId = transferenciaId;
	}

	public BigDecimal getMontoAutorizado() {
		return montoAutorizado;
	}

	public void setMontoAutorizado(BigDecimal montoAutorizado) {
		this.montoAutorizado = montoAutorizado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public BigDecimal getMontoRevisado() {
		return montoRevisado;
	}

	public void setMontoRevisado(BigDecimal montoRevisado) {
		this.montoRevisado = montoRevisado;
	}

	public Long getRevisadoPor() {
		return revisadoPor;
	}

	public void setRevisadoPor(Long revisadoPor) {
		this.revisadoPor = revisadoPor;
	}

	public Long getNumLote() {
		return numLote;
	}

	public void setNumLote(Long numLote) {
		this.numLote = numLote;
	}

	public Date getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public Empresa getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Empresa idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public PersonaExt getBeneficiarioId() {
		return beneficiarioId;
	}

	public void setBeneficiarioId(PersonaExt beneficiarioId) {
		this.beneficiarioId = beneficiarioId;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}
}

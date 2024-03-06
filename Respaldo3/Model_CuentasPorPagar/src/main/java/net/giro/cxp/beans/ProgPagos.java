package net.giro.cxp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * prog_pagos
 * @author javaz
 *
 */
public class ProgPagos implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long agente;
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
	private Long beneficiarioId;
	private String tipoBeneficiario;
	private Long idEmpresa;
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

	public void setId(long id) {
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

	public Long getAgente() {
		return agente;
	}

	public void setAgente(Long agente) {
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

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getBeneficiarioId() {
		return beneficiarioId;
	}

	public void setBeneficiarioId(Long beneficiarioId) {
		this.beneficiarioId = beneficiarioId;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}
}

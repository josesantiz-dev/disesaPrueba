package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class CFDIRow implements Serializable {
	private static final long serialVersionUID = 1L;
	private String expresionImpresa;
	private Long idComprobante;
	private String serie;
	private String folio;
	private String factura;
	private String uuid;
	private Long idEmisor;
	private String emisor;
	private String emisorRfc;
	private String receptor;
	private String receptorRfc;
	private String concepto;
	private String moneda;
	private BigDecimal tipoCambio;
	private BigDecimal subtotal;
	private BigDecimal impuestos;
	private BigDecimal retenciones;
	private BigDecimal total;
	private BigDecimal totalPesos;
	
	public CFDIRow() {
		this.expresionImpresa = "";
		this.serie = "";
		this.folio = "";
		this.factura = "";
		this.uuid = "";
		this.emisor = "";
		this.emisorRfc = "";
		this.receptor = "";
		this.receptorRfc = "";
		this.concepto = "";
		this.moneda = "";
		this.tipoCambio = BigDecimal.ZERO;
		this.subtotal = BigDecimal.ZERO;
		this.impuestos = BigDecimal.ZERO;
		this.retenciones = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.totalPesos = BigDecimal.ZERO;
	}

	public String getExpresionImpresa() {
		return expresionImpresa;
	}

	public void setExpresionImpresa(String value) {
		value = (value != null ? value.trim() : "");
		this.expresionImpresa = value;
	}

	public Long getIdComprobante() {
		return idComprobante;
	}

	public void setIdComprobante(Long value) {
		value = (value != null ? value : 0L);
		this.idComprobante = value;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String value) {
		value = (value != null ? value.trim() : "");
		this.serie = value;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String value) {
		value = (value != null ? value.trim() : "");
		this.folio = value;
	}

	public String getFactura() {
		return factura;
	}

	public void setFactura(String value) {
		value = (value != null ? value.trim() : "");
		this.factura = value;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String value) {
		value = (value != null ? value.trim() : "");
		this.uuid = value;
	}

	public Long getIdEmisor() {
		return idEmisor;
	}

	public void setIdEmisor(Long value) {
		value = (value != null ? value : 0L);
		this.idEmisor = value;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String value) {
		value = (value != null ? value.trim() : "");
		this.emisor = value;
	}

	public String getEmisorRfc() {
		return emisorRfc;
	}

	public void setEmisorRfc(String value) {
		value = (value != null ? value.trim() : "");
		this.emisorRfc = value;
	}

	public String getReceptor() {
		return receptor;
	}

	public void setReceptor(String value) {
		value = (value != null ? value.trim() : "");
		this.receptor = value;
	}

	public String getReceptorRfc() {
		return receptorRfc;
	}

	public void setReceptorRfc(String value) {
		value = (value != null ? value.trim() : "");
		this.receptorRfc = value;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String value) {
		value = (value != null ? value.trim() : "");
		this.concepto = value;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String value) {
		value = (value != null ? value.trim() : "");
		this.moneda = value;
	}

	public BigDecimal getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(BigDecimal value) {
		this.tipoCambio = value;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal value) {
		value = (value != null ? value : BigDecimal.ZERO);
		this.subtotal = value;
	}

	public BigDecimal getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(BigDecimal value) {
		value = (value != null ? value : BigDecimal.ZERO);
		this.impuestos = value;
	}

	public BigDecimal getRetenciones() {
		return retenciones;
	}

	public void setRetenciones(BigDecimal value) {
		value = (value != null ? value : BigDecimal.ZERO);
		this.retenciones = value;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal value) {
		value = (value != null ? value : BigDecimal.ZERO);
		this.total = value;
	}

	public BigDecimal getTotalPesos() {
		return totalPesos;
	}

	public void setTotalPesos(BigDecimal value) {
		value = (value != null ? value : BigDecimal.ZERO);
		this.totalPesos = value;
	}
}

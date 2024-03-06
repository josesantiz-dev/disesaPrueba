package net.giro.plataforma.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class SubfamiliaImpuestosExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private ConValores idSubfamilia;
	private String descSubfamilia;
	private ConValores idImpuesto;
	private String descImpuesto;
	private BigDecimal porcentaje;
	private BigDecimal valor; 
	private int aplicaEn;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public SubfamiliaImpuestosExt() {
		this.descSubfamilia = "";
		this.descImpuesto = "";
		this.porcentaje = BigDecimal.ZERO;
		this.valor = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public SubfamiliaImpuestosExt(Long id) {
		this();
		this.id = id;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ConValores getIdSubfamilia() {
		return idSubfamilia;
	}

	public void setIdSubfamilia(ConValores idSubfamilia) {
		this.idSubfamilia = idSubfamilia;
	}

	public String getDescSubfamilia() {
		return descSubfamilia;
	}

	public void setDescSubfamilia(String descSubfamilia) {
		this.descSubfamilia = descSubfamilia;
	}

	public ConValores getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(ConValores idImpuesto) {
		this.idImpuesto = idImpuesto;
	}

	public String getDescImpuesto() {
		return descImpuesto;
	}

	public void setDescImpuesto(String descImpuesto) {
		this.descImpuesto = descImpuesto;
	}

	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public int getAplicaEn() {
		return aplicaEn;
	}

	public void setAplicaEn(int aplicaEn) {
		this.aplicaEn = aplicaEn;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}

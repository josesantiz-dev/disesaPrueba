package net.giro.plataforma.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * subfamilia_impuestos
 * @author javaz
 *
 */
public class SubfamiliaImpuestos implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idSubfamilia;
	private String descSubfamilia;
	private Long idImpuesto;
	private String descImpuesto;
	private BigDecimal porcentaje;
	private BigDecimal valor; // no esta mapeado en BD, no se ocupa mapeado
	private int aplicaEn;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public SubfamiliaImpuestos() {
		this.descSubfamilia = "";
		this.descImpuesto = "";
		this.porcentaje = BigDecimal.ZERO;
		this.valor = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public SubfamiliaImpuestos(Long id) {
		this();
		this.id = id;
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

	public Long getIdSubfamilia() {
		return idSubfamilia;
	}

	public void setIdSubfamilia(Long idSubfamilia) {
		this.idSubfamilia = idSubfamilia;
	}

	public String getDescSubfamilia() {
		return descSubfamilia;
	}

	public void setDescSubfamilia(String descSubfamilia) {
		this.descSubfamilia = descSubfamilia;
	}

	public Long getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(Long idImpuesto) {
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

	/**
	 * APLICA EN: 0 - Impuesto IVA, 1 - Subtotal
	 * @return
	 */
	public int getAplicaEn() {
		return aplicaEn;
	}

	/**
	 * APLICA EN: 0 - Impuesto IVA, 1 - Subtotal
	 * @param aplicaEn
	 */
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
	
	public BigDecimal getMonto() {
		if (this.valor.compareTo(BigDecimal.ZERO) >= 0 && this.porcentaje.compareTo(BigDecimal.ZERO) > 0)
			return ((this.valor.multiply(this.porcentaje)).divide(new BigDecimal(100)));
		return BigDecimal.ZERO;
	}
	
	public void setMonto(BigDecimal monto) {}
}

package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * retenciones
 * @author javaz
 *
 */
public class Retenciones implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String retencion;
	private String ctaContable;
	private String impuesto;
	private String ctaImpuesto;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;

	public Retenciones() {
		this.id = 0L;
		this.impuesto = "";
		this.retencion = "";
		this.ctaContable = "";
		this.ctaImpuesto = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public Retenciones (Long id) {
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

	public String getRetencion() {
		return retencion;
	}

	public void setRetencion(String retencion) {
		this.retencion = retencion;
	}

	public String getCtaContable() {
		return ctaContable;
	}

	public void setCtaContable(String ctaContable) {
		this.ctaContable = ctaContable;
	}

	public String getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}

	public String getCtaImpuesto() {
		return ctaImpuesto;
	}

	public void setCtaImpuesto(String ctaImpuesto) {
		this.ctaImpuesto = ctaImpuesto;
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
}

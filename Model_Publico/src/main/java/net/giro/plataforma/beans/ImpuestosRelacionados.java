package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * impuestos_relacionados
 * @author javaz
 *
 */
public class ImpuestosRelacionados implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private ConValores idImpuestoBase;
	private ConValores idImpuestoRelacionado;
	private long idEmpresa;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	public ImpuestosRelacionados() {
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
	
	public ConValores getIdImpuestoBase() {
		return idImpuestoBase;
	}
	
	public void setIdImpuestoBase(ConValores idImpuestoBase) {
		this.idImpuestoBase = idImpuestoBase;
	}
	
	public ConValores getIdImpuestoRelacionado() {
		return idImpuestoRelacionado;
	}
	
	public void setIdImpuestoRelacionado(ConValores idImpuestoRelacionado) {
		this.idImpuestoRelacionado = idImpuestoRelacionado;
	}
	
	public long getIdEmpresa() {
		return idEmpresa;
	}
	
	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
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

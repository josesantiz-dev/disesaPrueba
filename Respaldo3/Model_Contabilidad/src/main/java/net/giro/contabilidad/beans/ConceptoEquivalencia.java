package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ConceptoEquivalencia implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long transaccion;
	private long idConceptoOrigen;
	private long idConceptoDestino;
	private String anotacion;
	private long idEmpresa;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public ConceptoEquivalencia() {
		this.anotacion = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
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

	public long getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(long transaccion) {
		this.transaccion = transaccion;
	}

	public long getIdConceptoOrigen() {
		return idConceptoOrigen;
	}

	public void setIdConceptoOrigen(long idConceptoOrigen) {
		this.idConceptoOrigen = idConceptoOrigen;
	}

	public long getIdConceptoDestino() {
		return idConceptoDestino;
	}

	public void setIdConceptoDestino(long idConceptoDestino) {
		this.idConceptoDestino = idConceptoDestino;
	}

	public String getAnotacion() {
		return anotacion;
	}

	public void setAnotacion(String anotacion) {
		this.anotacion = anotacion;
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

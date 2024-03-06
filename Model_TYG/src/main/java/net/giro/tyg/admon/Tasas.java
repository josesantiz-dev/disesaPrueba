package net.giro.tyg.admon;

import java.io.Serializable;
import java.util.Date;

/**
 * f134392853
 * @author javaz
 *
 */
public class Tasas implements Serializable {
	private static final long serialVersionUID = -8285002294298222522L;

	private long	id;
	private Long	creadoPor;
	private Date	fechaCreacion;
	private Long	modificadoPor;
	private Date	fechaModificacion;
	private String	descripcion;
	
	/** constructor */
	public Tasas(){}
	
	/** minimal constructor */
	public Tasas(long id, Long creadoPor, Date fechaCreacion, Long modificadoPor, Date fechaModificacion, String descripcion) {
		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.descripcion = descripcion;
	}
	
	// Getters and Setters
	
	public long getId() {
		return id;
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
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}

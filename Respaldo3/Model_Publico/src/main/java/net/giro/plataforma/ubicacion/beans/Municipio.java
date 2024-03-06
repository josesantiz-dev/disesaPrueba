package net.giro.plataforma.ubicacion.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * f724d6e190
 * @author javaz
 *
 */
public class Municipio implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id; // aa 
	private String nombre; // af
	private Estado estado; // ag
	private String regpob1; // ah
	private String regpob2; // ai
	private String regpob3; // aj
	private long creadoPor; // ab
	private Date fechaCreacion; // ac
	private long modificadoPor; // ad
	private Date fechaModificacion; // ae
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getRegpob1() {
		return regpob1;
	}

	public void setRegpob1(String regpob1) {
		this.regpob1 = regpob1;
	}

	public String getRegpob2() {
		return regpob2;
	}

	public void setRegpob2(String regpob2) {
		this.regpob2 = regpob2;
	}

	public String getRegpob3() {
		return regpob3;
	}

	public void setRegpob3(String regpob3) {
		this.regpob3 = regpob3;
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

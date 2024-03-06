package net.giro.clientes.beans;

import java.io.Serializable;

/**
 * f30f147763
 * @author javaz
 *
 */
public class CanalesNegocio implements Serializable { 

	private static final long serialVersionUID = 1L;

	/*IDENTIFICADOR DE LA TABLA*/
	private long id;

	/*FECHA HORA EN QUE SE CREO EL REGISTRO*/
	private java.util.Date fechaCreacion;

	/*ID DEL USUARIO QUE GENERA EL REGISTRO*/
	private java.lang.Long creadoPor;

	/*FECHA DE MODIFICACION DEL REGISTRO*/
	private java.util.Date fechaModificacion;

	/*ID DEL USUARIO QUE MODIFICA EL REGISTRO*/
	private java.lang.Long modificadoPor;

	/*ID DEL NEGOCIO ASOCIADO AL CANAL*/
	private Negocio idNegocio;

	/*ID DEL CATALOGO GENERICO DE CANALES SYS_CANALES_NEGOCIO*/
	private java.lang.Long idCanal;




	public CanalesNegocio() {}

 	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}
	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}
	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}
	public void setModificadoPor(java.lang.Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
	public Negocio getIdNegocio() {
		return idNegocio;
	}
	public void setIdNegocio(Negocio idNegocio) {
		this.idNegocio = idNegocio;
	}
	public java.lang.Long getIdCanal() {
		return idCanal;
	}
	public void setIdCanal(java.lang.Long idCanal) {
		this.idCanal = idCanal;
	}
}
package net.giro.clientes.beans;

import java.util.Date;

/**
 * b7513d2261
 * @author javaz
 *
 */
public class Prospecto implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	Date fechaModificacion;
	Persona persona;
	Negocio negocio;	
	Long oficial; //Especialista
	Long sucursal;
	Long establecimiento;
	Long modoContacto;
	Long comoEntero;
	Long rangoFacturacion;
	Date fechaContacto;
	Long estatus;
	Long razonRechazo;
	Long calificacion;
	
	public Prospecto(){
		
	}

	public Prospecto(long id, long creadoPor, Date fechaCreacion,
			long modificadoPor, Date fechaModificacion, Persona persona,
			Negocio negocio, Long oficial, Long sucursal, Long establecimiento,
			Long modoContacto, Long comoEntero, Long rangoFacturacion,
			Date fechaContacto, Long estatus, Long razonRechazo,
			Long calificacion) {

		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.persona = persona;
		this.negocio = negocio;
		this.oficial = oficial;
		this.sucursal = sucursal;
		this.establecimiento = establecimiento;
		this.modoContacto = modoContacto;
		this.comoEntero = comoEntero;
		this.rangoFacturacion = rangoFacturacion;
		this.fechaContacto = fechaContacto;
		this.estatus = estatus;
		this.razonRechazo = razonRechazo;
		this.calificacion = calificacion;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
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

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}

	public Long getOficial() {
		return oficial;
	}

	public void setOficial(Long oficial) {
		this.oficial = oficial;
	}

	public Long getSucursal() {
		return sucursal;
	}

	public void setSucursal(Long sucursal) {
		this.sucursal = sucursal;
	}

	public Long getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(Long establecimiento) {
		this.establecimiento = establecimiento;
	}

	public Long getModoContacto() {
		return modoContacto;
	}

	public void setModoContacto(Long modoContacto) {
		this.modoContacto = modoContacto;
	}

	public Long getComoEntero() {
		return comoEntero;
	}

	public void setComoEntero(Long comoEntero) {
		this.comoEntero = comoEntero;
	}

	public Long getRangoFacturacion() {
		return rangoFacturacion;
	}

	public void setRangoFacturacion(Long rangoFacturacion) {
		this.rangoFacturacion = rangoFacturacion;
	}

	public Date getFechaContacto() {
		return fechaContacto;
	}

	public void setFechaContacto(Date fechaContacto) {
		this.fechaContacto = fechaContacto;
	}

	public Long getEstatus() {
		return estatus;
	}

	public void setEstatus(Long estatus) {
		this.estatus = estatus;
	}

	public Long getRazonRechazo() {
		return razonRechazo;
	}

	public void setRazonRechazo(Long razonRechazo) {
		this.razonRechazo = razonRechazo;
	}

	public Long getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Long calificacion) {
		this.calificacion = calificacion;
	}
	
}

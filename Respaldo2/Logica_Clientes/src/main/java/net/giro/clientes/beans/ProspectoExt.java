package net.giro.clientes.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.ne.beans.Sucursal;
import net.giro.plataforma.beans.ConValores;

public class ProspectoExt implements Serializable {
	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	Date fechaModificacion;
	PersonaExt persona;
	NegocioExt negocio;	
	OficialExt oficial; //Especialista
	Sucursal sucursal;
	ConValores establecimiento;
	ConValores modoContacto;
	ConValores comoEntero;
	ConValores rangoFacturacion;
	Date fechaContacto;
	EstatusSeguimientoExt estatus;
	ConValores razonRechazo;
	ConValores calificacion;
	
	public ProspectoExt(){
		
	}

	public ProspectoExt(long id, long creadoPor, Date fechaCreacion,
			long modificadoPor, Date fechaModificacion, PersonaExt persona,
			NegocioExt negocio, OficialExt oficial, Sucursal sucursal,
			ConValores establecimiento, ConValores modoContacto,
			ConValores comoEntero, ConValores rangoFacturacion,
			Date fechaContacto, EstatusSeguimientoExt estatus, ConValores razonRechazo,
			ConValores calificacion) {
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

	public PersonaExt getPersona() {
		return persona;
	}

	public void setPersona(PersonaExt persona) {
		this.persona = persona;
	}

	public NegocioExt getNegocio() {
		return negocio;
	}

	public void setNegocio(NegocioExt negocio) {
		this.negocio = negocio;
	}

	public OficialExt getOficial() {
		return oficial;
	}

	public void setOficial(OficialExt oficial) {
		this.oficial = oficial;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public ConValores getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(ConValores establecimiento) {
		this.establecimiento = establecimiento;
	}

	public ConValores getModoContacto() {
		return modoContacto;
	}

	public void setModoContacto(ConValores modoContacto) {
		this.modoContacto = modoContacto;
	}

	public ConValores getComoEntero() {
		return comoEntero;
	}

	public void setComoEntero(ConValores comoEntero) {
		this.comoEntero = comoEntero;
	}

	public ConValores getRangoFacturacion() {
		return rangoFacturacion;
	}

	public void setRangoFacturacion(ConValores rangoFacturacion) {
		this.rangoFacturacion = rangoFacturacion;
	}

	public Date getFechaContacto() {
		return fechaContacto;
	}

	public void setFechaContacto(Date fechaContacto) {
		this.fechaContacto = fechaContacto;
	}

	public EstatusSeguimientoExt getEstatus() {
		return estatus;
	}

	public void setEstatus(EstatusSeguimientoExt estatus) {
		this.estatus = estatus;
	}

	public ConValores getRazonRechazo() {
		return razonRechazo;
	}

	public void setRazonRechazo(ConValores razonRechazo) {
		this.razonRechazo = razonRechazo;
	}

	public ConValores getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(ConValores calificacion) {
		this.calificacion = calificacion;
	}
}

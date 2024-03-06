package net.giro.clientes.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;

public class NegocioExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	long id;
	Long creadoPor;
	java.util.Date fechaCreacion;
	Long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String nombre;
	java.lang.String domicilio;
	java.lang.String telefono;
	ConValores giro;
	java.util.Date inicioOperaciones;
	java.lang.String formaOperacion;
	ConValores sector;
	java.lang.String referencia;
	java.lang.String nombreContacto;
	java.lang.String correoContacto;
	Long bloqueado;
	java.lang.String tipoPersonalidad;
	java.lang.String rfc;
	Colonia colonia;
	boolean estatus;
	java.lang.String paginaWeb;
	java.lang.Long numeroEmpleados;
	java.lang.String numeroCuenta;
	java.lang.String clabeInterbancaria;
	java.lang.Long banco;
	
	public NegocioExt(){
		
	}
	
	public NegocioExt(long id, Long creadoPor, Date fechaCreacion,
			Long modificadoPor, Date fechaModificacion, String nombre,
			String domicilio, String telefono, ConValores giro,
			Date inicioOperaciones, String formaOperacion, ConValores sector,
			String referencia, String nombreContacto, String correoContacto,
			Long bloqueado, String tipoPersonalidad, String rfc, Colonia colonia) {

		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.nombre = nombre;
		this.domicilio = domicilio;
		this.telefono = telefono;
		this.giro = giro;
		this.inicioOperaciones = inicioOperaciones;
		this.formaOperacion = formaOperacion;
		this.sector = sector;
		this.referencia = referencia;
		this.nombreContacto = nombreContacto;
		this.correoContacto = correoContacto;
		this.bloqueado = bloqueado;
		this.tipoPersonalidad = tipoPersonalidad;
		this.rfc = rfc;
		this.colonia = colonia;
	}

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

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public java.lang.String getNombre() {
		return nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public java.lang.String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(java.lang.String domicilio) {
		this.domicilio = domicilio;
	}

	public java.lang.String getTelefono() {
		return telefono;
	}

	public void setTelefono(java.lang.String telefono) {
		this.telefono = telefono;
	}

	public ConValores getGiro() {
		return giro;
	}

	public void setGiro(ConValores giro) {
		this.giro = giro;
	}

	public java.util.Date getInicioOperaciones() {
		return inicioOperaciones;
	}

	public void setInicioOperaciones(java.util.Date inicioOperaciones) {
		this.inicioOperaciones = inicioOperaciones;
	}

	public java.lang.String getFormaOperacion() {
		return formaOperacion;
	}

	public void setFormaOperacion(java.lang.String formaOperacion) {
		this.formaOperacion = formaOperacion;
	}

	public ConValores getSector() {
		return sector;
	}

	public void setSector(ConValores sector) {
		this.sector = sector;
	}

	public java.lang.String getReferencia() {
		return referencia;
	}

	public void setReferencia(java.lang.String referencia) {
		this.referencia = referencia;
	}

	public java.lang.String getNombreContacto() {
		return nombreContacto;
	}

	public void setNombreContacto(java.lang.String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public java.lang.String getCorreoContacto() {
		return correoContacto;
	}

	public void setCorreoContacto(java.lang.String correoContacto) {
		this.correoContacto = correoContacto;
	}

	public Long getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Long bloqueado) {
		this.bloqueado = bloqueado;
	}

	public java.lang.String getTipoPersonalidad() {
		return tipoPersonalidad;
	}

	public void setTipoPersonalidad(java.lang.String tipoPersonalidad) {
		this.tipoPersonalidad = tipoPersonalidad;
	}

	public java.lang.String getRfc() {
		return rfc;
	}

	public void setRfc(java.lang.String rfc) {
		this.rfc = rfc;
	}

	public Colonia getColonia() {
		return colonia;
	}

	public void setColonia(Colonia colonia) {
		this.colonia = colonia;
	}

	public boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}

	public java.lang.String getPaginaWeb() {
		return paginaWeb;
	}

	public void setPaginaWeb(java.lang.String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	public java.lang.Long getNumeroEmpleados() {
		return numeroEmpleados;
	}

	public void setNumeroEmpleados(java.lang.Long numeroEmpleados) {
		this.numeroEmpleados = numeroEmpleados;
	}

	public java.lang.String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(java.lang.String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public java.lang.String getClabeInterbancaria() {
		return clabeInterbancaria;
	}

	public void setClabeInterbancaria(java.lang.String clabeInterbancaria) {
		this.clabeInterbancaria = clabeInterbancaria;
	}

	public java.lang.Long getBanco() {
		return banco;
	}

	public void setBanco(java.lang.Long banco) {
		this.banco = banco;
	}
}

package net.giro.clientes.beans;

import java.util.Date;

public class Negocio implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private String nombre;
	private String domicilio;
	private String telefono;
	private Long giro;
	private Date inicioOperaciones;
	private String formaOperacion;
	private Long sector;
	private String referencia;
	private String nombreContacto;
	private String correoContacto;
	private long bloqueado;
	private String tipoPersonalidad;
	private String rfc;
	private Long colonia;
	private Long estatus;
	private String paginaWeb;
	private Long numeroEmpleados;
	private String numeroCuenta;
	private String clabeInterbancaria;
	private Long banco;
	

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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Long getGiro() {
		return giro;
	}

	public void setGiro(Long giro) {
		this.giro = giro;
	}

	public Date getInicioOperaciones() {
		return inicioOperaciones;
	}

	public void setInicioOperaciones(Date inicioOperaciones) {
		this.inicioOperaciones = inicioOperaciones;
	}

	public String getFormaOperacion() {
		return formaOperacion;
	}

	public void setFormaOperacion(String formaOperacion) {
		this.formaOperacion = formaOperacion;
	}

	public Long getSector() {
		return sector;
	}

	public void setSector(Long sector) {
		this.sector = sector;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getNombreContacto() {
		return nombreContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public String getCorreoContacto() {
		return correoContacto;
	}

	public void setCorreoContacto(String correoContacto) {
		this.correoContacto = correoContacto;
	}

	public long getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(long bloqueado) {
		this.bloqueado = bloqueado;
	}

	public String getTipoPersonalidad() {
		return tipoPersonalidad;
	}

	public void setTipoPersonalidad(String tipoPersonalidad) {
		this.tipoPersonalidad = tipoPersonalidad;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public Long getColonia() {
		return colonia;
	}

	public void setColonia(Long colonia) {
		this.colonia = colonia;
	}

	public Long getEstatus() {
		return estatus;
	}

	public void setEstatus(Long estatus) {
		this.estatus = estatus;
	}

	public String getPaginaWeb() {
		return paginaWeb;
	}

	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	public Long getNumeroEmpleados() {
		return numeroEmpleados;
	}

	public void setNumeroEmpleados(Long numeroEmpleados) {
		this.numeroEmpleados = numeroEmpleados;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getClabeInterbancaria() {
		return clabeInterbancaria;
	}

	public void setClabeInterbancaria(String clabeInterbancaria) {
		this.clabeInterbancaria = clabeInterbancaria;
	}

	public Long getBanco() {
		return banco;
	}

	public void setBanco(Long banco) {
		this.banco = banco;
	}
}

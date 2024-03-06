package net.giro.plataforma.beans;

import java.util.Date;

public class UsuarioExt implements java.io.Serializable {

private static final long serialVersionUID = 1L;
	
	private long id;
	private java.lang.String usuario;
	private java.lang.String nombre;
	private java.lang.String puesto;
	private java.lang.String password;
	private java.util.Date fechaIni;
	private java.util.Date fechaTerm;
	private java.lang.Long creadoPor;
	private java.util.Date fechaCreacion;
	private java.lang.Long modificadoPor;
	private java.util.Date fechaModificacion;
	private java.lang.Long expirado;
	private java.util.Date fechaCambio;
	private java.lang.Boolean bloqueado;
	private java.util.Date ultimoAcceso;
	private java.lang.String usuarioSpark;
	private java.lang.String correo;
	private java.lang.Long version;

	public UsuarioExt(){
		
		
	}
	
	public UsuarioExt(long id, String usuario, String nombre, String puesto,
			String password, Date fechaIni, Date fechaTerm, Long creadoPor,
			Date fechaCreacion, Long modificadoPor, Date fechaModificacion,
			Long expirado, Date fechaCambio, Boolean bloqueado, Date ultimoAcceso,
			String usuarioSpark, String correo,  Long version) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.nombre = nombre;
		this.puesto = puesto;
		this.password = password;
		this.fechaIni = fechaIni;
		this.fechaTerm = fechaTerm;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.expirado = expirado;
		this.fechaCambio = fechaCambio;
		this.bloqueado = bloqueado;
		this.ultimoAcceso = ultimoAcceso;
		this.usuarioSpark = usuarioSpark;
		this.correo = correo;
		
		this.version = version;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getUsuario() {
		return usuario;
	}

	public void setUsuario(java.lang.String usuario) {
		this.usuario = usuario;
	}

	public java.lang.String getNombre() {
		return nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public java.lang.String getPuesto() {
		return puesto;
	}

	public void setPuesto(java.lang.String puesto) {
		this.puesto = puesto;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public java.util.Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(java.util.Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public java.util.Date getFechaTerm() {
		return fechaTerm;
	}

	public void setFechaTerm(java.util.Date fechaTerm) {
		this.fechaTerm = fechaTerm;
	}

	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(java.lang.Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public java.lang.Long getExpirado() {
		return expirado;
	}

	public void setExpirado(java.lang.Long expirado) {
		this.expirado = expirado;
	}

	public java.util.Date getFechaCambio() {
		return fechaCambio;
	}

	public void setFechaCambio(java.util.Date fechaCambio) {
		this.fechaCambio = fechaCambio;
	}

	public java.lang.Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(java.lang.Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public java.util.Date getUltimoAcceso() {
		return ultimoAcceso;
	}

	public void setUltimoAcceso(java.util.Date ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}

	public java.lang.String getUsuarioSpark() {
		return usuarioSpark;
	}

	public void setUsuarioSpark(java.lang.String usuarioSpark) {
		this.usuarioSpark = usuarioSpark;
	}

	public java.lang.String getCorreo() {
		return correo;
	}

	public void setCorreo(java.lang.String correo) {
		this.correo = correo;
	}



	public java.lang.Long getVersion() {
		return version;
	}

	public void setVersion(java.lang.Long version) {
		this.version = version;
	}




	

}

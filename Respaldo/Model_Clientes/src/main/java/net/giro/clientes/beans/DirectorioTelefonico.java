package net.giro.clientes.beans;

public class DirectorioTelefonico implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private java.lang.Long creadoPor;
	private java.util.Date fechaCreacion;
	private java.lang.Long modificadoPor;
	private java.util.Date fechaModificacion;
	private net.giro.clientes.beans.Persona persona;
	private net.giro.clientes.beans.Negocio negocio;
	private java.lang.Long tipoTelefono;
	private java.lang.Long companiaTelefonica;
	private long lada;
	private java.lang.String numeroTelefono;
	private long extension;
	private java.lang.String observaciones;
	private long principal;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public net.giro.clientes.beans.Persona getPersona() {
		return persona;
	}

	public void setPersona(net.giro.clientes.beans.Persona persona) {
		this.persona = persona;
	}

	public net.giro.clientes.beans.Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(net.giro.clientes.beans.Negocio negocio) {
		this.negocio = negocio;
	}

	public java.lang.Long getTipoTelefono() {
		return tipoTelefono;
	}

	public void setTipoTelefono(java.lang.Long tipoTelefono) {
		this.tipoTelefono = tipoTelefono;
	}

	public java.lang.Long getCompaniaTelefonica() {
		return companiaTelefonica;
	}

	public void setCompaniaTelefonica(java.lang.Long companiaTelefonica) {
		this.companiaTelefonica = companiaTelefonica;
	}

	public long getLada() {
		return lada;
	}

	public void setLada(long lada) {
		this.lada = lada;
	}

	public java.lang.String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(java.lang.String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public long getExtension() {
		return extension;
	}

	public void setExtension(long extension) {
		this.extension = extension;
	}

	public java.lang.String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(java.lang.String observaciones) {
		this.observaciones = observaciones;
	}

	public long getPrincipal() {
		return principal;
	}

	public void setPrincipal(long principal) {
		this.principal = principal;
	}

}

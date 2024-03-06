package net.giro.clientes.beans;

/**
 * d9d1aa0337
 * @author javaz
 *
 */
public class PersonaNegocio implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long creadoPor;
	private java.util.Date fechaCreacion;
	private long modificadoPor;
	private java.util.Date fechaModificacion;
	private net.giro.clientes.beans.Persona persona;
	private net.giro.clientes.beans.Negocio negocio;
	private java.lang.String personalidadJuridica;
	private java.math.BigDecimal participacion;
	private java.lang.String puesto;
	private Long firmaDocumento;
	private net.giro.clientes.beans.Persona apoderadoId;
	private Long representante;

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

	public java.lang.String getPersonalidadJuridica() {
		return personalidadJuridica;
	}

	public void setPersonalidadJuridica(java.lang.String personalidadJuridica) {
		this.personalidadJuridica = personalidadJuridica;
	}

	public java.math.BigDecimal getParticipacion() {
		return participacion;
	}

	public void setParticipacion(java.math.BigDecimal participacion) {
		this.participacion = participacion;
	}

	public java.lang.String getPuesto() {
		return puesto;
	}

	public void setPuesto(java.lang.String puesto) {
		this.puesto = puesto;
	}

	public net.giro.clientes.beans.Persona getApoderadoId() {
		return apoderadoId;
	}

	public void setApoderadoId(net.giro.clientes.beans.Persona apoderadoId) {
		this.apoderadoId = apoderadoId;
	}

	public Long getFirmaDocumento() {
		return firmaDocumento;
	}

	public void setFirmaDocumento(Long firmaDocumento) {
		this.firmaDocumento = firmaDocumento;
	}

	public Long getRepresentante() {
		return representante;
	}

	public void setRepresentante(Long representante) {
		this.representante = representante;
	}

}

package net.giro.clientes.beans;

import java.math.BigDecimal;
import java.util.Date;

public class PersonaNegocioExt implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long creadoPor;
	private long modificadoPor;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private PersonaExt persona;
	private Negocio negocio;
	private String personalidadJuridica;
	private BigDecimal participacion;
	private String puesto;
	private PersonaExt apoderadoId;
	private Boolean firmaDocumento;
	private Boolean representante;
	
	public PersonaNegocioExt(){
	}
	
	public PersonaNegocioExt(long id, long creadoPor, Date fechaCreacion,
			long modificadoPor, Date fechaModificacion, PersonaExt persona,
			Negocio negocio, String personalidadJuridica,
			BigDecimal participacion, String puesto, PersonaExt apoderadoId,Boolean firmaDocumento,Boolean representante) {
		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.persona = persona;
		this.negocio = negocio;
		this.personalidadJuridica = personalidadJuridica;
		this.participacion = participacion;
		this.puesto = puesto;
		this.apoderadoId = apoderadoId;
		this.firmaDocumento = firmaDocumento;
		this.representante =representante;		
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

	public PersonaExt getPersona() {
		return persona;
	}

	public void setPersona(PersonaExt persona) {
		this.persona = persona;
	}

	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}

	public String getPersonalidadJuridica() {
		return personalidadJuridica;
	}

	public void setPersonalidadJuridica(String personalidadJuridica) {
		this.personalidadJuridica = personalidadJuridica;
	}

	public BigDecimal getParticipacion() {
		return participacion;
	}

	public void setParticipacion(BigDecimal participacion) {
		this.participacion = participacion;
	}

	public java.lang.String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public PersonaExt getApoderadoId() {
		return apoderadoId;
	}

	public void setApoderadoId(PersonaExt apoderadoId) {
		this.apoderadoId = apoderadoId;
	}

	public Boolean getFirmaDocumento() {
		return firmaDocumento;
	}

	public void setFirmaDocumento(Boolean firmaDocumento) {
		this.firmaDocumento = firmaDocumento;
	}

	public Boolean getRepresentante() {
		return representante;
	}

	public void setRepresentante(Boolean representante) {
		this.representante = representante;
	}

}

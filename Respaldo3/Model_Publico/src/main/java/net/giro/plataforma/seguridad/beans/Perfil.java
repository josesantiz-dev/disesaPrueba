package net.giro.plataforma.seguridad.beans;

import java.util.Date;


/**
 * d7729f32ba7
 * @author javaz
 *
 */
public class Perfil implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String perfil;
	private long nivelPerfil;
	private String descripcion;
	private String tipoComponente;
	private String componenteSrc;
	private Date validoDesde;
	private Date validoHasta;
	private Aplicacion aplicacion;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	public Perfil() {
		
	}
	
	/*public Perfil(long id, String perfil, long nivelPerfil, String descripcion,
			String tipoComponente, String componenteSrc, Date validoDesde,
			Date validoHasta, long creadoPor, Date fechaCreacion,
			long modificadoPor, Date fechaModificacion, Aplicacion aplicacion) {
		this.id = id;
		this.perfil = perfil;
		this.nivelPerfil = nivelPerfil;
		this.descripcion = descripcion;
		this.tipoComponente = tipoComponente;
		this.componenteSrc = componenteSrc;
		this.validoDesde = validoDesde;
		this.validoHasta = validoHasta;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.aplicacion = aplicacion;
	}*/

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public long getNivelPerfil() {
		return nivelPerfil;
	}

	public void setNivelPerfil(long nivelPerfil) {
		this.nivelPerfil = nivelPerfil;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipoComponente() {
		return tipoComponente;
	}

	public void setTipoComponente(String tipoComponente) {
		this.tipoComponente = tipoComponente;
	}

	public String getComponenteSrc() {
		return componenteSrc;
	}

	public void setComponenteSrc(String componenteSrc) {
		this.componenteSrc = componenteSrc;
	}

	public Date getValidoDesde() {
		return validoDesde;
	}

	public void setValidoDesde(Date validoDesde) {
		this.validoDesde = validoDesde;
	}

	public Date getValidoHasta() {
		return validoHasta;
	}

	public void setValidoHasta(Date validoHasta) {
		this.validoHasta = validoHasta;
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
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

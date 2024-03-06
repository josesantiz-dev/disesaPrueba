package net.giro.plataforma.seguridad.beans;

/**
 * b761110ccfe
 * @author javaz
 *
 */
public class PerfilValor implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	net.giro.plataforma.seguridad.beans.Perfil perfil;
	long nivel;
	long valorNivel;
	java.lang.String valorPerfil;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public net.giro.plataforma.seguridad.beans.Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(net.giro.plataforma.seguridad.beans.Perfil perfil) {
		this.perfil = perfil;
	}

	public long getNivel() {
		return nivel;
	}

	public void setNivel(long nivel) {
		this.nivel = nivel;
	}

	public long getValorNivel() {
		return valorNivel;
	}

	public void setValorNivel(long valorNivel) {
		this.valorNivel = valorNivel;
	}

	public java.lang.String getValorPerfil() {
		return valorPerfil;
	}

	public void setValorPerfil(java.lang.String valorPerfil) {
		this.valorPerfil = valorPerfil;
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

}

package net.giro.plataforma.seguridad.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * b761110ccfe
 * @author javaz
 *
 */
public class PerfilValor implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private Perfil perfil;
	private long nivel;
	private long valorNivel;
	private String valorPerfil;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Perfil getPerfil() {
		return perfil;
	}
	
	public void setPerfil(Perfil perfil) {
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
	
	public String getValorPerfil() {
		return valorPerfil;
	}
	
	public void setValorPerfil(String valorPerfil) {
		this.valorPerfil = valorPerfil;
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

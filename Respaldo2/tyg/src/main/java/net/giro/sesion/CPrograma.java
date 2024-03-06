package net.giro.sesion;

import java.io.Serializable;

public class CPrograma implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String programa;
	private String titulo;
	private String descripcion;
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getPrograma() {
		return this.programa;
	}
	
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	
	public String getTitulo() {
		return this.titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDescripcion() {
		return this.descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
package net.proc.sesion;

import java.io.Serializable;

public class CPrograma implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String programa;
	private String titulo;
	private String descripcion;
	
	
	public CPrograma() {
		
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}

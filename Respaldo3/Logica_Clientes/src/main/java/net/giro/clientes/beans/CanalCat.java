package net.giro.clientes.beans;

import java.io.Serializable;

public class CanalCat implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String nombre;
	
	public CanalCat() {
	}

	public CanalCat(long id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}

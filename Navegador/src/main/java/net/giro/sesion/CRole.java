package net.giro.sesion;

import java.io.Serializable;

public class CRole implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String role;
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getRole() {
		return this.role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
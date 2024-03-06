package net.giro.clientes.beans;

import java.io.Serializable;

public class EstatusSeguimientoExt implements Serializable {
	private static final long serialVersionUID = 1L;

	Long id;
	String estatus;
	
	
	public EstatusSeguimientoExt(){
		
	}


	public EstatusSeguimientoExt(Long id, String estatus) {
		this.id = id;
		this.estatus = estatus;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getEstatus() {
		return estatus;
	}


	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

}

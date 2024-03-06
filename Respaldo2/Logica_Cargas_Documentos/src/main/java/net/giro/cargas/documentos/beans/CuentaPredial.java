package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;

public class CuentaPredial implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Attribute(required=false) 
	private String numero;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}

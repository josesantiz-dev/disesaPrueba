package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;

public class RegimenFiscal implements Serializable {
	private static final long serialVersionUID = 1L;
	@Attribute(name="Regimen")
	String regimen;

	public String getRegimen() {
		return regimen;
	}

	public void setRegimen(String regimen) {
		this.regimen = regimen;
	}
}

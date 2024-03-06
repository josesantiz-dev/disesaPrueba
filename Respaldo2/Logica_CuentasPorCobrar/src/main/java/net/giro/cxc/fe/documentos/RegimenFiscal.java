package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class RegimenFiscal implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="regimen")
	private String regimen;
	
	
	public RegimenFiscal() {}
	
	public RegimenFiscal(String regimen) {
		super();
		this.regimen = regimen;
	}
	

	public String getRegimen() {
		return regimen;
	}

	public void setRegimen(String regimen) {
		this.regimen = regimen;
	}
}

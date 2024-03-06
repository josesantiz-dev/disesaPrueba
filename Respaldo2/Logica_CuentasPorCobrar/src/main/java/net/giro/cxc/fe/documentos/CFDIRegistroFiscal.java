package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class CFDIRegistroFiscal implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name="Folio", required=false)
	private String folio;
	@XmlAttribute(name="Version", required=false)
	private String version;
	
	
	public CFDIRegistroFiscal() {}
	
	
	public String getFolio() {
		return folio;
	}
	
	public void setFolio(String folio) {
		this.folio = folio;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
}

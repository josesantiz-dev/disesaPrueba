package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class CfdiRelacionado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="UUID", required=true)
	private String uuid;

	
	public CfdiRelacionado() {}

	public CfdiRelacionado(String uuid) {
		super();
		this.uuid = uuid;
	}
	

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}

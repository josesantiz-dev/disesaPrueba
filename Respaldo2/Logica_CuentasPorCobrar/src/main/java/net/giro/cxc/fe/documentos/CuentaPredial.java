package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class CuentaPredial implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="Numero", required=false) 
	private String numero;
	
	
	public CuentaPredial() {}
	
	public CuentaPredial(String numero) {
		super();
		this.numero = numero;
	}

	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}

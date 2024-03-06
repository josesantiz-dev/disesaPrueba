package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Receptor implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name="Rfc", required=true)
	private String rfc;
	@XmlAttribute(name="Nombre", required=false)
	private String nombre;
	@XmlAttribute(name="UsoCFDI", required=true)
	private String usoCFDI;
	@XmlElement(name="domicilio", required=false)
	private Domicilio domicilio;
	
	
	public Receptor() {}
	
	public Receptor(String rfc, String usoCFDI) {
		super();
		this.rfc = rfc;
		this.usoCFDI = usoCFDI;
	}

	
	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	public String getUsoCFDI() {
		return usoCFDI;
	}

	public void setUsoCFDI(String usoCFDI) {
		this.usoCFDI = usoCFDI;
	}
}

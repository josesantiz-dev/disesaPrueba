package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Emisor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="Rfc", required=true)
	private String rfc;
	@XmlAttribute(name="Nombre", required=false)
	private String nombre;
	@XmlElement(name="DomicilioFiscal", required=false)
	private Domicilio domicilioFiscal;
	@XmlElement(name="ExpedidoEn", required=false)
	private Domicilio expedidoEn;
	@XmlAttribute(name="RegimenFiscal", required=false)
	private String regimenFiscal;
	@XmlElement(name="regimenfiscal", required=false)
	private RegimenFiscal regimenFiscal32;
	
	
	public Emisor() {}
	
	
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

	public Domicilio getDomicilioFiscal() {
		return domicilioFiscal;
	}

	public void setDomicilioFiscal(Domicilio domicilioFiscal) {
		this.domicilioFiscal = domicilioFiscal;
	}

	public Domicilio getExpedidoEn() {
		return expedidoEn;
	}

	public void setExpedidoEn(Domicilio expedidoEn) {
		this.expedidoEn = expedidoEn;
	}

	public String getRegimenFiscal() {
		return regimenFiscal;
	}

	public void setRegimenFiscal(String regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}
}

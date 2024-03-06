package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


public class Emisor implements Serializable {
	private static final long serialVersionUID = 1L;
	@Attribute(required=false)
	String rfc;
	@Attribute(required=false)
	String nombre;
	
	@Element(name="DomicilioFiscal", required=false)
	Domicilio domicilioFiscal;
	@Element(name="ExpedidoEn", required=false)
	Domicilio expedidoEn;
	@Element(name="RegimenFiscal", required=false)
	RegimenFiscal regimenFiscal;

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

	public RegimenFiscal getRegimenFiscal() {
		return regimenFiscal;
	}

	public void setRegimenFiscal(RegimenFiscal regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}

	public Domicilio getExpedidoEn() {
		return expedidoEn;
	}

	public void setExpedidoEn(Domicilio expedidoEn) {
		this.expedidoEn = expedidoEn;
	}
	
}

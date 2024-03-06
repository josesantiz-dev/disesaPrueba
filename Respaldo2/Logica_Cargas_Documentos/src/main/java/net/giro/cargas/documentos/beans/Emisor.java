package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


public class Emisor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Attribute
	String rfc;
	@Attribute(required=false)
	String nombre;
	@Attribute(name="regimenfiscal", required=false)
	String regimenFiscal33;
	@Element(name="domiciliofiscal", required=false)
	Domicilio domicilioFiscal;
	@Element(name="expedidoen", required=false)
	Domicilio expedidoEn;
	@Element(name="regimenfiscal", required=false)
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

	public Domicilio getExpedidoEn() {
		return expedidoEn;
	}

	public void setExpedidoEn(Domicilio expedidoEn) {
		this.expedidoEn = expedidoEn;
	}
}

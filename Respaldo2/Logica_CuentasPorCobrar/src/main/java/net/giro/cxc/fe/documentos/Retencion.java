package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Retencion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="Impuesto", required=true)
	private String impuesto;
	@XmlAttribute(name="Importe", required=true)
	private String importe;
	
	
	public Retencion() {}
	
	public Retencion(String impuesto, String importe) {
		super();
		this.impuesto = impuesto;
		this.importe = importe;
	}
	
	
	public String getImpuesto() {
		return impuesto;
	}
	
	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}
}

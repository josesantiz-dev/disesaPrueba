package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;

public class Retencion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Attribute(name="impuesto", required=false)
	String impuesto;
	@Attribute(name="importe", required=false)
	String importe;
	
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

package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;

public class Retencion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Attribute(name="impuesto", required=false)
	String impuesto;
	@Attribute(name="tasa", required=false)
	String tasa;
	@Attribute(name="importe", required=false)
	String importe;
	@Attribute(name="base", required=false)
	String base;
	@Attribute(name="tasaocuota", required=false)
	String tasaOCuota;
	@Attribute(name="tipofactor", required=false)
	String tipoFactor;
	
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

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getTipoFactor() {
		return tipoFactor;
	}

	public void setTipoFactor(String tipoFactor) {
		this.tipoFactor = tipoFactor;
	}

	public String getTasaOCuota() {
		return tasaOCuota;
	}

	public void setTasaOCuota(String tasaOCuota) {
		this.tasaOCuota = tasaOCuota;
	}
}

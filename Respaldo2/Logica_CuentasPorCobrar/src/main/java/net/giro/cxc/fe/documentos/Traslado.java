package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Traslado implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="Impuesto", required=true)
	private String impuesto;
	@XmlAttribute(name="TipoFactor", required=true)
	private String tipoFactor;
	@XmlAttribute(name="TasaOCuota", required=true)
	private String tasaOCuota;
	@XmlAttribute(name="Importe", required=true)
	private String importe;
	
	
	public Traslado() {}
	
	public Traslado(String impuesto, String tipoFactor, String tasaOCuota, String importe) {
		super();
		this.impuesto = impuesto;
		this.tipoFactor = tipoFactor;
		this.tasaOCuota = tasaOCuota;
		this.importe = importe;
	}
	
	
	public String getImpuesto() {
		return impuesto;
	}
	
	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}
	
	public String getTipofactor() {
		return tipoFactor;
	}
	
	public void setTipofactor(String tipofactor) {
		this.tipoFactor = tipofactor;
	}
	
	public String getTasaOCuota() {
		return tasaOCuota;
	}
	
	public void setTasaOCuota(String tasaOCuota) {
		this.tasaOCuota = tasaOCuota;
	}
	
	public String getImporte() {
		return importe;
	}
	
	public void setImporte(String importe) {
		this.importe = importe;
	}	
}

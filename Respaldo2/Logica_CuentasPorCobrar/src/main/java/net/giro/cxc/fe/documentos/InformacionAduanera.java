package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class InformacionAduanera implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name="Fecha", required=false) 
	private String fecha;
	@XmlAttribute(name="Numero", required=false) 
	private String numero;
	@XmlAttribute(name="Aduana", required=false) 
	private String aduana;
	@XmlAttribute(name="NumeroPedimento", required=false) 
	private String numeroPedimento;
	
	
	public InformacionAduanera() {}
	
	
	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getAduana() {
		return aduana;
	}
	
	public void setAduana(String aduana) {
		this.aduana = aduana;
	}

	public String getNumeroPedimento() {
		return numeroPedimento;
	}

	public void setNumeroPedimento(String numeroPedimento) {
		this.numeroPedimento = numeroPedimento;
	}
}

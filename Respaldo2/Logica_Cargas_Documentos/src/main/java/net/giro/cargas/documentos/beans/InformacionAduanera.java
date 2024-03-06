package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;

public class InformacionAduanera implements Serializable {
	private static final long serialVersionUID = 1L;

	@Attribute(required=false) 
	private String fecha;
	@Attribute(required=false) 
	private String numero;
	@Attribute(required=false, name="aduana") 
	private String aduana;
	@Attribute(required=false, name="numeropedimento") 
	private String numeroPedimento;
	
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

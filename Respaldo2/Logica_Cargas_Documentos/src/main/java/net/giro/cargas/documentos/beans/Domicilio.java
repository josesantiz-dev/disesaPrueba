package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;

public class Domicilio implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Attribute(required=false)
	String calle;
	@Attribute(name="noexterior", required=false)
	String noExterior;
	@Attribute(name="nointerior", required=false)
	String noInterior;
	@Attribute(required=false)
	String colonia;
	@Attribute(required=false)
	String localidad;
	@Attribute(required=false)
	String municipio;
	@Attribute(required=false)
	String estado;
	@Attribute(required=false)
	String pais;
	@Attribute(name="codigopostal", required=false)
	String codigoPostal;
	@Attribute(required=false)
	String referencia;
	
	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNoExterior() {
		return noExterior;
	}

	public void setNoExterior(String noExterior) {
		this.noExterior = noExterior;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getNoInterior() {
		return noInterior;
	}

	public void setNoInterior(String noInterior) {
		this.noInterior = noInterior;
	}
}

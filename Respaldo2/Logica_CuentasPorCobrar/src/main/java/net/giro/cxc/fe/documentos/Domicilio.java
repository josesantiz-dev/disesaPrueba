package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Domicilio implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(required=false)
	String calle;
	@XmlAttribute(name="noexterior", required=false)
	String noExterior;
	@XmlAttribute(name="nointerior", required=false)
	String noInterior;
	@XmlAttribute(required=false)
	String colonia;
	@XmlAttribute(required=false)
	String localidad;
	@XmlAttribute(required=false)
	String municipio;
	@XmlAttribute(required=false)
	String estado;
	@XmlAttribute(required=false)
	String pais;
	@XmlAttribute(name="codigopostal", required=false)
	String codigoPostal;
	@XmlAttribute(required=false)
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

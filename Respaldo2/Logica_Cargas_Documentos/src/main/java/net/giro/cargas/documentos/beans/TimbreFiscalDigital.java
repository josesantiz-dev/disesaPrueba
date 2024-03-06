package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Namespace;

@Namespace(reference="http://www.sat.gob.mx/TimbreFiscalDigital", prefix="tfd")
public class TimbreFiscalDigital implements Serializable {
	private static final long serialVersionUID = 1L;
	@Attribute(name="schemalocation")
	String schemaLocation;
	@Attribute(name="sellocfd")
	String selloCFD;
	@Attribute(name="fechatimbrado")
	String fechaTimbrado;
	@Attribute(name="uuid")
	String UUID;
	@Attribute(name="nocertificadosat")
	String noCertificadoSAT;
	@Attribute
	String version;
	@Attribute(name="sellosat")
	String selloSAT;
	@Attribute(name="rfcprovcertif", required=false)
	String rfcProvCertif;
	@Attribute(name="leyenda", required=false)
	String leyenda;
	
	public String getSchemaLocation() {
		return schemaLocation;
	}
	
	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}

	public String getSelloCFD() {
		return selloCFD;
	}

	public void setSelloCFD(String selloCFD) {
		this.selloCFD = selloCFD;
	}

	public String getFechaTimbrado() {
		return fechaTimbrado;
	}

	public void setFechaTimbrado(String fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getNoCertificadoSAT() {
		return noCertificadoSAT;
	}

	public void setNoCertificadoSAT(String noCertificadoSAT) {
		this.noCertificadoSAT = noCertificadoSAT;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSelloSAT() {
		return selloSAT;
	}

	public void setSelloSAT(String selloSAT) {
		this.selloSAT = selloSAT;
	}

	public String getRfcProvCertif() {
		return rfcProvCertif;
	}

	public void setRfcProvCertif(String rfcProvCertif) {
		this.rfcProvCertif = rfcProvCertif;
	}

	public String getLeyenda() {
		return leyenda;
	}

	public void setLeyenda(String leyenda) {
		this.leyenda = leyenda;
	}
}

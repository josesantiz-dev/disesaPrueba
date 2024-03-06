package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class TimbreFiscalDigital implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="SchemaLocation")
	private String schemaLocation;
	@XmlAttribute(name="SelloCfd")
	private String selloCFD;
	@XmlAttribute(name="FechaTimbrado")
	private String fechaTimbrado;
	@XmlAttribute(name="UUID")
	private String uuid;
	@XmlAttribute(name="NoCertificadoSAT")
	private String noCertificadoSAT;
	@XmlAttribute(name="Version", required=false)
	private String version;
	@XmlAttribute(name="SelloSAT")
	private String selloSAT;
	@XmlAttribute(name="RfcProvCertif", required=false)
	private String rfcProvCertif;
	@XmlAttribute(name="Leyenda", required=false)
	private String leyenda;
	
	
	public TimbreFiscalDigital() {}
	
	
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
		return uuid;
	}

	public void setUUID(String uUID) {
		uuid = uUID;
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

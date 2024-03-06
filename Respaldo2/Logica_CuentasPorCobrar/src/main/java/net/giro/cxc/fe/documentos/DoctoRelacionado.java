package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class DoctoRelacionado implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name="IdDocumento", required=true)
	private String idDocumento;
	@XmlAttribute(name="Serie", required=false)
	private String serie;
	@XmlAttribute(name="Folio", required=false)
	private String folio;
	@XmlAttribute(name="MonedaDR", required=true)
	private String monedaDR;
	@XmlAttribute(name="TipoCambioDR", required=false)
	private String tipoCambioDR;
	@XmlAttribute(name="MetodoDePagoDR", required=false)
	private String metodoDePagoDR;
	@XmlAttribute(name="NumParcialidad", required=false)
	private String numParcialidad;
	@XmlAttribute(name="ImpSaldoAnt", required=false)
	private String impSaldoAnterior;
	@XmlAttribute(name="ImpPagado", required=false)
	private String impPagado;
	@XmlAttribute(name="ImpSaldoInsoluto", required=false)
	private String impSaldoInsoluto;
	
	
	public DoctoRelacionado() {}
	
	public DoctoRelacionado(String idDocumento, String monedaDR, String metodoDePagoDR) {
		super();
		this.idDocumento = idDocumento;
		this.monedaDR = monedaDR;
		this.metodoDePagoDR = metodoDePagoDR;
	}

	
	public String getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(String idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getMonedaDR() {
		return monedaDR;
	}

	public void setMonedaDR(String monedaDR) {
		this.monedaDR = monedaDR;
	}

	public String getTipoCambioDR() {
		return tipoCambioDR;
	}

	public void setTipoCambioDR(String tipoCambioDR) {
		this.tipoCambioDR = tipoCambioDR;
	}

	public String getMetodoDePagoDR() {
		return metodoDePagoDR;
	}

	public void setMetodoDePagoDR(String metodoDePagoDR) {
		this.metodoDePagoDR = metodoDePagoDR;
	}

	public String getNumParcialidad() {
		return numParcialidad;
	}

	public void setNumParcialidad(String numParcialidad) {
		this.numParcialidad = numParcialidad;
	}

	public String getImpSaldoAnterior() {
		return impSaldoAnterior;
	}

	public void setImpSaldoAnterior(String impSaldoAnterior) {
		this.impSaldoAnterior = impSaldoAnterior;
	}

	public String getImpPagado() {
		return impPagado;
	}

	public void setImpPagado(String impPagado) {
		this.impPagado = impPagado;
	}

	public String getImpSaldoInsoluto() {
		return impSaldoInsoluto;
	}

	public void setImpSaldoInsoluto(String impSaldoInsoluto) {
		this.impSaldoInsoluto = impSaldoInsoluto;
	}
}

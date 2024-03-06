package net.giro.cxc.fe.documentos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Complemento implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="TimbreFiscalDigital", required=false, namespace="http://www.sat.gob.mx/TimbreFiscalDigital")
	private TimbreFiscalDigital timbreFiscalDigital;
	@XmlElement(name="CFDIRegistroFiscal", required=false)
	private CFDIRegistroFiscal cfdiRegistroFiscal;
	@XmlElement(name="Pagos", required=false, namespace="http://www.sat.gob.mx/Pagos")
	private Pagos10 Pagos;
	
	
	public Complemento() {}
	
	
	public TimbreFiscalDigital getTimbreFiscalDigital() {
		return timbreFiscalDigital;
	}

	public void setTimbreFiscalDigital(TimbreFiscalDigital timbreFiscalDigital) {
		this.timbreFiscalDigital = timbreFiscalDigital;
	}

	public CFDIRegistroFiscal getCfdiRegistroFiscal() {
		return cfdiRegistroFiscal;
	}

	public void setCfdiRegistroFiscal(CFDIRegistroFiscal cfdiRegistroFiscal) {
		this.cfdiRegistroFiscal = cfdiRegistroFiscal;
	}

	public Pagos10 getPagos() {
		return Pagos;
	}

	public void setPagos(Pagos10 pagos) {
		Pagos = pagos;
	}
}

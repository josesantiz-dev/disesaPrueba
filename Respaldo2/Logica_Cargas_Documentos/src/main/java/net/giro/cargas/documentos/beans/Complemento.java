package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Element;

public class Complemento implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Element(name="timbrefiscaldigital")
	private TimbreFiscalDigital timbreFiscalDigital;
	@Element(name="cfdiregistrofiscal", required=false)
	private CFDIRegistroFiscal cfdiRegistroFiscal;
	@Element(name="impuestoslocales", required=false)
	private ImpuestosLocales impuestosLocales;
	
	
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

	public ImpuestosLocales getImpuestosLocales() {
		return impuestosLocales;
	}

	public void setImpuestosLocales(ImpuestosLocales impuestosLocales) {
		this.impuestosLocales = impuestosLocales;
	}
}

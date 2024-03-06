package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Element;

public class Complemento implements Serializable {
	private static final long serialVersionUID = 1L;
	@Element(name="TimbreFiscalDigital")
	TimbreFiscalDigital timbreFiscalDigital;

	public TimbreFiscalDigital getTimbreFiscalDigital() {
		return timbreFiscalDigital;
	}

	public void setTimbreFiscalDigital(TimbreFiscalDigital timbreFiscalDigital) {
		this.timbreFiscalDigital = timbreFiscalDigital;
	}
}

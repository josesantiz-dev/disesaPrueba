package net.giro.cargas.documentos.beans;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;

public class CFDIRegistroFiscal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Attribute(name="folio", required=false)
	String folio;
	@Attribute(name="version", required=false)
	String version;
}

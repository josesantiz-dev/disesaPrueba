package net.giro.cxc.fe.documentos;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class CfdiRelacionados implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="TipoRelacion", required=true)
	private String tipoRelacion;
	@XmlElementWrapper(name="CfdiRelacionados", required=true)
	@XmlElement(name="CfdiRelacionado", required=true)
	private List<CfdiRelacionado> cfdiRelacionados;
	
	
	public CfdiRelacionados() {}
	
	public CfdiRelacionados (String tipoRelacion, List<CfdiRelacionado> cfdiRelacionados) {
		super();
		this.tipoRelacion = tipoRelacion;
		this.cfdiRelacionados = cfdiRelacionados;
	}
	
	
	public String getTipoRelacion() {
		return tipoRelacion;
	}
	
	public void setTipoRelacion(String tipoRelacion) {
		this.tipoRelacion = tipoRelacion;
	}
	
	public List<CfdiRelacionado> getCfdiRelacionados() {
		return cfdiRelacionados;
	}
	
	public void setCfdiRelacionados(List<CfdiRelacionado> cfdiRelacionados) {
		this.cfdiRelacionados = cfdiRelacionados;
	}
}

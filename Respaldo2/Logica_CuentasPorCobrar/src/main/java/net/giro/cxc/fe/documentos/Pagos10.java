package net.giro.cxc.fe.documentos;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="Pagos", namespace="")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pagos10 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="Version", required=true)
	private String version;
	@XmlElement(name="Pago", required=true, namespace="http://www.sat.gob.mx/Pagos")
	private List<Pago10> pagos;
	
	
	public Pagos10() {}
	
	public Pagos10(String version, List<Pago10> pagos) {
		super();
		this.version = version;
		this.pagos = pagos;
	}

	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Pago10> getPagos() {
		return pagos;
	}

	public void setPagos(List<Pago10> pagos) {
		this.pagos = pagos;
	}
}

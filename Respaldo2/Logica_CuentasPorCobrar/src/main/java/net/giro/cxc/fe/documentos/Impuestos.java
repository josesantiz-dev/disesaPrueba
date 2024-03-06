package net.giro.cxc.fe.documentos;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class Impuestos implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name="TotalImpuestosTrasladados",required=false)
	private String totalImpuestosTrasladados;
	@XmlAttribute(name="TotalImpuestosRetenidos", required=false)
	private String totalImpuestosRetenidos;
	@XmlElementWrapper(name="Traslados")
	@XmlElement(name="Traslado", required=false)
	private List<Traslado> traslados;
	@XmlElementWrapper(name="Retenciones")
	@XmlElement(name="Retencion", required=false)
	private List<Retencion> retenciones;
	
	
	public Impuestos() {}

	public Impuestos(List<Traslado> traslados, List<Retencion> retenciones) {
		super();
		this.traslados = traslados;
		this.retenciones = retenciones;
		totalizar();
	}
	
	
	public String getTotalImpuestosTrasladados() {
		return totalImpuestosTrasladados;
	}

	public void setTotalImpuestosTrasladados(String totalImpuestosTrasladados) {
		this.totalImpuestosTrasladados = totalImpuestosTrasladados;
	}

	public List<Traslado> getTraslados() {
		return traslados;
	}

	public void setTraslados(List<Traslado> traslados) {
		this.traslados = traslados;
	}

	public String getTotalImpuestosRetenidos() {
		return totalImpuestosRetenidos;
	}

	public void setTotalImpuestosRetenidos(String totalImpuestosRetenidos) {
		this.totalImpuestosRetenidos = totalImpuestosRetenidos;
	}

	public List<Retencion> getRetenciones() {
		return retenciones;
	}

	public void setRetenciones(List<Retencion> retenciones) {
		this.retenciones = retenciones;
	}
	
	
	private void totalizar() {
		double monto = 0;
		
		if (this.traslados != null && ! this.traslados.isEmpty()) {
			monto = 0;
			for (Traslado item : this.traslados) 
				monto += Double.parseDouble(item.getImporte());
			
			this.totalImpuestosTrasladados = "";
			if (monto > 0)
				this.totalImpuestosTrasladados = (new DecimalFormat("#.00####")).format(monto);
		}
		
		if (this.retenciones != null && ! this.retenciones.isEmpty()) {
			monto = 0;
			for (Retencion item : this.retenciones) 
				monto += Double.parseDouble(item.getImporte());
			
			this.totalImpuestosRetenidos = "";
			if (monto > 0)
				this.totalImpuestosRetenidos = (new DecimalFormat("#.00####")).format(monto);
		}
	}
}

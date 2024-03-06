package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class Impuestos implements Serializable {
	private static final long serialVersionUID = 1L;

	@Attribute(required=false)
	String totalImpuestosTrasladados;
	
	@Attribute(name="totalImpuestosRetenidos", required=false)
	String totalImpuestosRetenidos;
	
	@ElementList(name="Traslados", required=false)
	List<Traslado> traslados;
	
	@ElementList(name="Retenciones", required=false)
	List<Retencion> retenciones;
	
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
}

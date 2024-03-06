package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

public class FacturaConcepto implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idConcepto;
	private String descConcepto;
	private String descripcion;
	private double importe;
	private double impuesto;
	private double retencion;
	private HashMap<String,Double> traslados;
	private HashMap<String,Double> retenciones;
	private double total;
	private String observaciones;
	private boolean seleccionado;
	
	public FacturaConcepto() {
		this.descConcepto = "";
		this.descripcion = "";
		this.observaciones = "";
		this.traslados = new HashMap<String, Double>();
		this.retenciones = new HashMap<String, Double>();
	}

	public FacturaConcepto(String descripcion, double importe) {
		this();
		this.descripcion = descripcion;
		this.importe = importe;
	}

	public long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(long idConcepto) {
		this.idConcepto = idConcepto;
	}

	public String getDescConcepto() {
		return descConcepto;
	}

	public void setDescConcepto(String descConcepto) {
		this.descConcepto = descConcepto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public double getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(double impuesto) {
		this.impuesto = impuesto;
	}

	public String getDescImpuesto() {
		String value = "";
		
		if (this.traslados != null && ! this.traslados.isEmpty()) {
			for(Entry<String,Double> item : this.traslados.entrySet()) {
				if ("".equals(value))
					value += ",";
				value += item.getKey();
			}
		}
		
		return value;
	}

	public double getRetencion() {
		return retencion;
	}

	public void setRetencion(double retencion) {
		this.retencion = retencion;
	}

	public String getDescRetencion() {
		String value = "";
		
		if (this.retenciones != null && ! this.retenciones.isEmpty()) {
			for(Entry<String,Double> item : this.retenciones.entrySet()) {
				if ("".equals(value))
					value += ",";
				value += item.getKey();
			}
		}
		
		return value;
	}

	public double getTotal() {
		if (this.total <= 0)
			this.total = this.importe + (this.impuesto - this.retencion);
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public HashMap<String, Double> getTraslados() {
		return this.traslados;
	}

	public HashMap<String, Double> getRetenciones() {
		return this.retenciones;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
}

package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class ObraSubcontratistaImpuestosExt extends ObraSubcontratistaImpuestos implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean vTasaImpuesto;
	private boolean vBaseImpuesto;
	private boolean guardable;

	public ObraSubcontratistaImpuestosExt() {
		super();
		this.vTasaImpuesto = false;
		this.vBaseImpuesto = false;
		this.guardable = false;
	}

	public double getTasaImpuesto() {
		return super.getTasaImpuesto();
	}
	
	public void setTasaImpuesto(double tasaImpuesto) {
		tasaImpuesto = (tasaImpuesto > 0 ? tasaImpuesto : 0);
		super.setTasaImpuesto(tasaImpuesto);
		this.vTasaImpuesto = true;
		this.guardable = true;
	}
	
	public BigDecimal getBaseImpuesto() {
		return super.getBaseImpuesto();
	}
	
	public void setBaseImpuesto(BigDecimal baseImpuesto) {
		baseImpuesto = (baseImpuesto != null ? baseImpuesto : BigDecimal.ZERO);
		super.setBaseImpuesto(baseImpuesto);
		this.vBaseImpuesto = true;
		this.guardable = true;
	}

	public boolean isGuardable() {
		return guardable;
	}

	public void setGuardable(boolean guardable) {
		this.guardable = guardable;
	}
	
	// --------------------------------------------------------------------------------
	// EXTENDIDOS 
	// --------------------------------------------------------------------------------
	
	public boolean isRetencion() {
		return super.getRetencion() == 1;
	}
	
	public void setRetencion(boolean value) {}
	
	public boolean getModificado() {
		return (this.vTasaImpuesto || this.vBaseImpuesto);
	}
	
	public void setModificado(boolean value) {}

	// --------------------------------------------------------------------------------
	// METODOS 
	// --------------------------------------------------------------------------------
	
	public void calcular() {
		double valor = 0;
		
		if (! getModificado())
			return;
		valor = super.getTasaImpuesto();
		valor = (valor > 1 ? (valor / 100) : valor);
		valor = monto((super.getBaseImpuesto().doubleValue() * valor), 4);
		super.setImporte(monto(new BigDecimal(valor), 4));

		this.vTasaImpuesto = false;
		this.vBaseImpuesto = false;
	}

	public void recalcular() {
		this.vTasaImpuesto = true;
		this.vBaseImpuesto = true;
		calcular();
	}

	// --------------------------------------------------------------------------------
	// PRIVADOS 
	// --------------------------------------------------------------------------------
	
	private double monto(double value, int decimales) {
		return new BigDecimal(value).setScale(decimales, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	private BigDecimal monto(BigDecimal value, int decimales) {
		return value.setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
	}
}

package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InsumosTipo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nombre;
	private BigDecimal monto;
	private double porcentaje;
	private List<InsumosDetallesExt> detalles;
	
	public InsumosTipo() {
		this.nombre = "";
		this.monto = BigDecimal.ZERO;
		this.detalles = new ArrayList<InsumosDetallesExt>();
	}
	
	public InsumosTipo(String nombre, BigDecimal monto, List<InsumosDetallesExt> detalles) {
		this();
		if (nombre != null && ! "".equals(nombre))
			this.nombre = nombre;
		if (monto != null)
			this.monto = monto;
		if (detalles != null && ! detalles.isEmpty()) {
			this.detalles = detalles;
			sumaPorcentaje();
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public List<InsumosDetallesExt> getDetalles() {
		if (this.detalles == null)
			this.detalles = new ArrayList<InsumosDetallesExt>();
		return detalles;
	}
	
	private void sumaPorcentaje() {
		if (this.detalles == null)
			return;
		
		this.porcentaje = 0;
		for (InsumosDetallesExt item : this.detalles)
			this.porcentaje += item.getPorcentaje();
	}
}

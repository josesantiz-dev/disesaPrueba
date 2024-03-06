package net.giro.cxc.realvirtual.beans;

public class AcuseCFDI {
	private int state; 
	private String descripcion;
	private String cfdi;
	
	public AcuseCFDI() {
		this.descripcion = "";
		this.cfdi = "";
	}

	public int getState() {
		return state;
	}

	public void setState(int value) {
		this.state = value;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String value) {
		value = (value != null && ! "".equals(value.trim()) ? value.trim() : "");
		this.descripcion = value;
	}

	public String getCFDI() {
		return cfdi;
	}

	public void setCFDI(String value) {
		value = (value != null && ! "".equals(value.trim()) ? value.trim() : "");
		this.cfdi = value;
	}

	@Override
	public String toString() {
		if (this.descripcion != null) 
			return this.state + " - " + this.descripcion;
		return "";
	}
}

package net.giro.navegador.comun;

import java.io.Serializable;

public class Permiso implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean consultar;
	private boolean editar;
	private boolean borrar;
	
	public Permiso() {
		this.borrar = false;
		this.editar = false;
		this.consultar = false;
	}
	
	public Permiso(int value) {
		this();
		convertir(value);
	}

	public boolean getConsultar() {
		return consultar;
	}

	public boolean getEditar() {
		return editar;
	}

	public boolean getBorrar() {
		return borrar;
	}
	
	public int getValue() {
		return Integer.parseInt(this.toString(), 2);
	}

	@Override
	public String toString() {
		return (this.borrar ? "1" : "0") + (this.editar ? "1" : "0") + (this.consultar ? "1" : "0");
	}
	
	private void convertir(int value) {
		String[] splitted = null;
		String val = null;
		
		// Convertimos a binario
		val = Integer.toString(value, 2);
		// Rellenamos de ceros a la izquierda si corresponde
		val = String.format("%03d", Integer.valueOf(val));
		// separamos valores
		splitted = val.split("(?!^)");
		if (splitted == null || splitted.length <= 0)
			return;
		
		// asignamos variables
		this.borrar = "1".equals(splitted[0]);
		this.editar = "1".equals(splitted[1]);
		this.consultar = "1".equals(splitted[2]);
	}
}

package net.giro.sesion;

import java.io.Serializable;
import java.util.HashMap;

import net.giro.sesion.Programa;

public class Subgrupo implements Serializable {
	private static final long serialVersionUID = -3430824341248204415L;
	private HashMap<String, Programa> programas = new HashMap<String,Programa>();
	
	public Subgrupo() {
		this.programas = new HashMap<String,Programa>();
	}
	
	public HashMap<String, Programa> getProgramas() {
		return programas;
	}

	public void setProgramas(HashMap<String, Programa> programas) {
		this.programas = programas;
	}

	public Programa getPrograma(String programa) {
		if (this.programas.get(programa) == null)
			this.programas.put(programa, new Programa());
		return this.programas.get(programa);
	}
}

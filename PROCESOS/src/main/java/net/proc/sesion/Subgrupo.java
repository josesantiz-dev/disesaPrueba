package net.proc.sesion;

import java.io.Serializable;
import java.util.HashMap;

public class Subgrupo implements Serializable {
	private static final long serialVersionUID = 1L;			
	private HashMap<String, Programa> programas = new HashMap<String,Programa>();

	public Subgrupo() {}
	
	public HashMap<String, Programa> getProgramas() {
		return programas;
	}

	public void setProgramas(HashMap<String, Programa> programas) {
		this.programas = programas;
	}

	public Programa getPrograma(String programa) {
		if (programas.get(programa) == null)
			programas.put(programa, new Programa());
		return programas.get(programa);
	}
}

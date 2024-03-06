package net.proc.sesion;

import java.io.Serializable;
import java.util.HashMap;

/**
 * OPCIONES
 * @author javaz
 */
public class Opciones implements Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Grupo> grupos = new HashMap<String, Grupo>();
	
	public Opciones() {}
	
	public HashMap<String, Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(HashMap<String, Grupo> grupos) {
		this.grupos = grupos;
	}

	public Grupo getGrupo() {
		return new Grupo();
	}
	
	public Grupo getGrupo(String grupo) {
		if (grupos.get(grupo) == null)
			grupos.put(grupo, new Grupo());
		return grupos.get(grupo);
	}
}

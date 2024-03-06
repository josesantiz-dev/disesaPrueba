package net.giro.sesion;

import java.io.Serializable;
import java.util.HashMap;

import net.giro.sesion.Subgrupo;

public class Grupo implements Serializable {
	private static final long serialVersionUID = -6802671547295164093L;
	private HashMap<String, Subgrupo> subgrupos = new HashMap<String, Subgrupo>();
	
	public Grupo() {
		this.subgrupos = new HashMap<String, Subgrupo>();
	}

	public HashMap<String, Subgrupo> getSubgrupos() {
		return subgrupos;
	}

	public void setSubgrupos(HashMap<String, Subgrupo> subgrupos) {
		this.subgrupos = subgrupos;
	}

	public Subgrupo getSubgrupo(String subgrupo) {
		if (this.subgrupos.get(subgrupo) == null)
			this.subgrupos.put(subgrupo, new Subgrupo());
		return this.subgrupos.get(subgrupo);
	}
}

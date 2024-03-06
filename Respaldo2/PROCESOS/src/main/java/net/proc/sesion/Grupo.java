package net.proc.sesion;

import java.io.Serializable;
import java.util.HashMap;

public class Grupo implements Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<String,Subgrupo> subgrupos = new HashMap<String,Subgrupo>() ;
	
	public Grupo() {
		
	}
	
	public HashMap<String, Subgrupo> getSubgrupos() {
		return subgrupos;
	}

	public void setSubgrupos(HashMap<String, Subgrupo> subgrupos) {
		this.subgrupos = subgrupos;
	}

	public Subgrupo getSubgrupo(String subgrupo) {
		if (subgrupos.get(subgrupo) == null)
			subgrupos.put(subgrupo, new Subgrupo());
		return subgrupos.get(subgrupo);
	}
}

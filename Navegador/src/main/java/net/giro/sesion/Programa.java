package net.giro.sesion;

import java.io.Serializable;
import java.util.HashMap;

import net.giro.sesion.CParametro;
import net.giro.sesion.CPrograma;

public class Programa implements Serializable {
	private static final long serialVersionUID = 9137368974646488874L;
	private CPrograma cPrograma = new CPrograma();
	private HashMap<String, CParametro> cParametros = new HashMap<String,CParametro>();
	
	
	public Programa() {
		this.cParametros = new HashMap<String,CParametro>();
		this.cPrograma = new CPrograma();
	}
	
	
	public CParametro getParametro(String parametro) {
		if (this.cParametros.get(parametro) == null)
			this.cParametros.put(parametro, new CParametro());
		return this.cParametros.get(parametro);
	}
	
	public void setCPrograma(long id, String descripcion, String titulo, String programa) {
		this.cPrograma.setDescripcion(descripcion);
		this.cPrograma.setId(id);
		this.cPrograma.setTitulo(titulo);
		this.cPrograma.setPrograma(programa);
	}

	public CPrograma getcPrograma() {
		return this.cPrograma;
	}

	public void setcPrograma(CPrograma cPrograma) {
		this.cPrograma = cPrograma;
	}

	public HashMap<String, CParametro> getcParametros() {
		return this.cParametros;
	}

	public void setcParametros(HashMap<String, CParametro> cParametros) {
		this.cParametros = cParametros;
	}
}

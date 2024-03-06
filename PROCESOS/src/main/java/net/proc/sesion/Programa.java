package net.proc.sesion;

import java.io.Serializable;
import java.util.HashMap;

public class Programa implements Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<String, CParametro> cParametros = new HashMap<String,CParametro>();
	private CPrograma cPrograma = new CPrograma();
	
	public Programa() {}
	
	public CParametro getParametro(String parametro) {
		if (cParametros.get(parametro) == null)
			cParametros.put(parametro, new CParametro());
		return cParametros.get(parametro);
	}
	
	public void setCPrograma(long id, String descripcion, String titulo, String programa) {
		cPrograma.setDescripcion(descripcion);
		cPrograma.setId(id);
		cPrograma.setTitulo(titulo);
		cPrograma.setPrograma(programa);
	}

	public CPrograma getcPrograma() {
		return cPrograma;
	}

	public void setcPrograma(CPrograma cPrograma) {
		this.cPrograma = cPrograma;
	}

	public HashMap<String, CParametro> getcParametros() {
		return cParametros;
	}

	public void setcParametros(HashMap<String, CParametro> cParametros) {
		this.cParametros = cParametros;
	}
}

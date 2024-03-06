package net.giro.plataforma;

import java.io.Serializable;
import java.util.ArrayList;

public class Pagina implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String url;
	private String prompt;
	private String descripcion;
	private String parametros;
	private int icono;
	private ArrayList<Pagina> subMenu;

	public Pagina() {
		this.url = "";
		this.prompt = "";
		this.descripcion = "";
		this.parametros = "";
		this.subMenu = new ArrayList<Pagina>();
	}
	
	public Pagina(String url, String prompt, ArrayList<Pagina> subMenu, String desc, String param, long l) {
		this.url = url;
		this.prompt = prompt;
		this.subMenu = subMenu;
		this.descripcion = desc;
		this.parametros = param;
		this.id = l;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	public int getIcono() {
		return icono;
	}

	public void setIcono(int icono) {
		this.icono = icono;
	}

	public ArrayList<Pagina> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(ArrayList<Pagina> subMenu) {
		this.subMenu = subMenu;
	}
}

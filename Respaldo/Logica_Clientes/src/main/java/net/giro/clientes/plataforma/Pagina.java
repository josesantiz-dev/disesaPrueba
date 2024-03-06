package net.giro.clientes.plataforma;

import java.util.ArrayList;

import net.giro.clientes.plataforma.Pagina;


public class Pagina implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String url;
	private String prompt;
	private String descripcion;
	private String parametros;
	private int icono;
	private long id;
	private ArrayList<Pagina> subMenu;
	
	public Pagina(String url, String prompt, ArrayList<Pagina> subMenu, String desc, String param, long l){
		this.url = url;
		this.prompt = prompt;
		this.subMenu = subMenu;
		this.descripcion = desc;
		this.parametros = param;
		this.id = l;
	}
	
	public Pagina(){}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setPrompt(String promt) {
		this.prompt = promt;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setSubMenu(ArrayList<Pagina> subMenu) {
		this.subMenu = subMenu;
	}

	public ArrayList<Pagina> getSubMenu() {
		return subMenu;
	}
	
	public String toString(){
		return this.prompt;
	}
	public void setIcono(int icono) {
		this.icono = icono;
	}
	public int getIcono() {
		return icono;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setParametros(String parametros) {
		this.parametros = parametros;
	}
	public String getParametros() {
		return parametros;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
}

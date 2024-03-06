package net.giro.inventarios;


public class TopicoS {
	
	String evento;
	String objeto;
	String id;
	
	String atributos;
	
	public TopicoS(String evento, String objeto, String id, String atributos) {
		super();
		this.evento = evento;
		this.objeto = objeto;
		this.id = id;
		this.atributos = atributos;
	}
	
	public String getEvento() {
		return evento;
	}
	public void setEvento(String evento) {
		this.evento = evento;
	}
	public String getObjeto() {
		return objeto;
	}
	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAtributos() {
		return atributos;
	}
	public void setAtributos(String atributos) {
		this.atributos = atributos;
	}
	
}

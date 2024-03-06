package net.giro.cargas.documentos.plataforma;

import bsh.Interpreter;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

public class Bsf{
	private HttpSession httpSession;
	private HashMap<String, String> nivelPerfiles;
	
	private Object retorno;
	private String codigo;
	
	public Bsf(HttpSession hsession, HashMap<String, String> niveles){
		this.httpSession = hsession;
		this.nivelPerfiles = niveles;
	}
	
	public Bsf(HashMap<String, String> niveles){
		this.httpSession = null;
		this.nivelPerfiles = niveles;
	}
	
	public Object getRetorno() {
		return this.retorno;
	}
	
	public void setRetorno(Object retorno) {
		this.retorno = retorno;
	}
	
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public Object eval(){
		Interpreter i;
		try {
			i = new Interpreter();
			i.set("session", this.httpSession);
			i.set("nivelPerfiles", this.nivelPerfiles);
			i.eval(this.codigo);
			this.retorno = i.get("retorno");
			return this.retorno;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
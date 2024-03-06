package net.giro.publico.respuesta;

import java.io.Serializable;
import java.util.HashMap;

public class RespuestaBody implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, Object>	params;

	public RespuestaBody(){
		
	}
	
	public void addValor(String nombre, Object valor){
		if(params == null){
			params = new HashMap<String, Object>();
		}
		params.put(nombre, valor);
	}
	
	public Object getValor(String nombre){
		return params == null ? null :  params.get(nombre);
	}
}

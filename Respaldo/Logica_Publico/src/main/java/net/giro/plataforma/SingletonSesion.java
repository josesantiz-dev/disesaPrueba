package net.giro.plataforma;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;

@Singleton
public class SingletonSesion implements Serializable {
	private static final long serialVersionUID = -7062043060064761158L;
	
	private HashMap<String, InfoSesion> params;

	public boolean valida(InfoSesion sesion) {
		if (params != null)
			return (params.containsKey(String.valueOf(sesion.getAcceso().getId())));
		return false;
	}
	
	public void registrar(InfoSesion sesion) throws Exception{
		if(sesion == null)
			throw new Exception("Informacion incompleta");
		
		if(params == null)
			params = new HashMap<String, InfoSesion>();
		
		if(params.containsKey(String.valueOf(sesion.getAcceso().getId())))
			throw new Exception("Session duplicada");
		else
			params.put(String.valueOf(sesion.getAcceso().getId()), sesion);
	}
	
	public void logout(InfoSesion sesion) {
		if (params != null) {
			params.remove(String.valueOf(sesion.getAcceso().getId()));
			
			InfoSesion aux = null;
			List<String> sesionesParaBorrar = new ArrayList<String>();
			for (Map.Entry<String, InfoSesion> s : params.entrySet()) {
				aux = s.getValue();
				if (sesion.getAcceso().getUsuario().getId() == aux.getAcceso().getUsuario().getId())
					sesionesParaBorrar.add(String.valueOf(aux.getAcceso().getId()));
			}

			for (String s : sesionesParaBorrar) {
				if (params.containsKey(s))
					params.remove(s);
			}
		}
	}
}

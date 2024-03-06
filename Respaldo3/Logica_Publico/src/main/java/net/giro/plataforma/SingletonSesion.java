package net.giro.plataforma;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Singleton;

import net.giro.ne.beans.Empresa;

@Singleton
public class SingletonSesion implements Serializable {
	private static final long serialVersionUID = -7062043060064761158L;
	private HashMap<String, InfoSesion> params;
	//private Empresa pojoEmpresa;

	public SingletonSesion() {}
	
	
	public HashMap<String, String> loggedIn() {
		HashMap<String, String> sessiones = new HashMap<String, String>();
		String key = "";
		String value = "";
		
		if (this.params != null && ! this.params.isEmpty()) {
			for (Entry<String, InfoSesion> item : this.params.entrySet()) {
				key = item.getValue().getAcceso().getUsuario().getUsuario();
				if (sessiones.containsKey(key))
					value = sessiones.get(key) + ",";
				value += item.getValue().getAcceso().getAplicacion().getAplicacion() + ":" + item.getValue().getEmpresa().getCodigo();
				sessiones.put(key, value);
				value = "";
			}
		}
		
		return sessiones;
	}
	
	public boolean valida(InfoSesion sesion) {
		if (this.params == null || this.params.isEmpty())
			return false;
		return (this.params.containsKey(String.valueOf(sesion.getAcceso().getId())));
	}
	
	public InfoSesion comprobar(long idUsuario, long idAplicacion) {
		InfoSesion sesion = null;
		
		if (this.params == null || this.params.isEmpty())
			return sesion;
		
		for (Entry<String, InfoSesion> s : this.params.entrySet()) {
			if (idUsuario == s.getValue().getAcceso().getUsuario().getId() && idAplicacion == s.getValue().getAcceso().getAplicacion().getId()) {
				sesion = s.getValue();
				break;
			}
		}
		
		return sesion;
	}
	
	public InfoSesion comprobar(InfoSesion sesion) {
		InfoSesion resultado = null;
		
		if (this.params == null || this.params.isEmpty())
			return resultado;
		
		for (Entry<String, InfoSesion> s : this.params.entrySet()) {
			if (sesion.getAcceso().getUsuario().getId() == s.getValue().getAcceso().getUsuario().getId() && sesion.getAcceso().getAplicacion().getId() == s.getValue().getAcceso().getAplicacion().getId()) {
				resultado = s.getValue();
				break;
			}
		}
		
		return resultado;
	}
	
	public InfoSesion validar(InfoSesion sesion) {
		if (this.params != null && ! this.params.isEmpty()) {
			for (Entry<String, InfoSesion> item : this.params.entrySet()) {
				if (sesion.getAcceso().getUsuario().getId() != item.getValue().getAcceso().getUsuario().getId())
					continue;
				if (sesion.getAcceso().getAplicacion().getId() != item.getValue().getAcceso().getAplicacion().getId())
					continue;
				return item.getValue();
			}
		}
		
		return sesion;
	}

	public InfoSesion validar(long idUsuario, long idAplicacion) {
		if (this.params != null && ! this.params.isEmpty()) {
			for (Entry<String, InfoSesion> item : this.params.entrySet()) {
				if (idUsuario != item.getValue().getAcceso().getUsuario().getId())
					continue;
				if (idAplicacion != item.getValue().getAcceso().getAplicacion().getId())
					continue;
				return item.getValue();
			}
		}
		
		return null;
	}
	
	public void registrar(InfoSesion sesion) throws Exception {
		List<String> sesionesParaBorrar = null;
		Empresa empresaPrevio = null;
		
		if (sesion == null)
			throw new Exception("Informacion incompleta");
		
		if (this.params == null)
			this.params = new HashMap<String, InfoSesion>();
		
		// Comprobamos que no se este registrada previamente la sesion
		if (this.params.containsKey(String.valueOf(sesion.getAcceso().getId())))
			throw new Exception("Session duplicada");
		
		// Comprobamos si existe sesion del mismo usuario y la misma aplicacion
		if (this.params.isEmpty()) {
			this.params.put(String.valueOf(sesion.getAcceso().getId()), sesion);
			return;
		}

		sesionesParaBorrar = new ArrayList<String>();
		for (Entry<String, InfoSesion> s : this.params.entrySet()) {
			if (empresaPrevio == null && sesion.getAcceso().getUsuario().getId() == s.getValue().getAcceso().getUsuario().getId())
				empresaPrevio = s.getValue().getEmpresa();
			if (sesion.getAcceso().getUsuario().getId() == s.getValue().getAcceso().getUsuario().getId() && sesion.getAcceso().getAplicacion().getId() == s.getValue().getAcceso().getAplicacion().getId())
				sesionesParaBorrar.add(String.valueOf(s.getValue().getAcceso().getId()));
		}
		
		// Quito las sessiones correspondientes
		for (String s : sesionesParaBorrar) {
			if (this.params.containsKey(s))
				this.params.remove(s);
		}
		
		// Registro la session: Asigno empresa si corresponde
		if (sesion.getEmpresa() == null && empresaPrevio != null)
			sesion.setEmpresa(empresaPrevio);
		this.params.put(String.valueOf(sesion.getAcceso().getId()), sesion);
	}

	public void putSession(InfoSesion sesion) {
		if (this.params.containsKey(String.valueOf(sesion.getAcceso().getId()))) {
			this.params.put(String.valueOf(sesion.getAcceso().getId()), sesion);
			propagateCompany(sesion, sesion.getEmpresa());
			
			/*if (sesion.getEmpresa() != null) {
				if (this.pojoEmpresa == null || this.pojoEmpresa.getId().longValue() != sesion.getEmpresa().getId().longValue()) {
					this.pojoEmpresa = sesion.getEmpresa();
					
					if (this.params != null && ! this.params.isEmpty()) {
						for (Entry<String, InfoSesion> s : this.params.entrySet()) {
							if (sesion.getAcceso().getUsuario().getId() == s.getValue().getAcceso().getUsuario().getId()) {
								s.getValue().setEmpresa(this.pojoEmpresa);
							}
						}
					}
				}
			}*/
		}
	}
	
	public void propagateCompany(InfoSesion sesion, Empresa pojoEmpresa) {
		//this.pojoEmpresa = pojoEmpresa;
		if (this.params == null || this.params.isEmpty())
			return;
		
		// Cambiamos la empresa para todos los modulos donde este logueado el usuario
		for (Entry<String, InfoSesion> item : this.params.entrySet()) {
			if (sesion.getAcceso().getUsuario().getId() == item.getValue().getAcceso().getUsuario().getId())
				item.getValue().setEmpresa(pojoEmpresa);
		}
	}
	
	public void changeModule(InfoSesion sesion) {
		if (this.params != null)
			this.params.remove(String.valueOf(sesion.getAcceso().getId()));
	}
	
	public void logout(InfoSesion sesion) {
		List<String> sesionesParaBorrar = null;
		
		//this.pojoEmpresa = null;
		if (this.params != null && ! this.params.isEmpty()) {
			sesionesParaBorrar = new ArrayList<String>();
			for (Entry<String, InfoSesion> item : this.params.entrySet()) {
				if (sesion.getAcceso().getUsuario().getId() != item.getValue().getAcceso().getUsuario().getId())
					continue;
				sesionesParaBorrar.add(String.valueOf(item.getValue().getAcceso().getId()));
			}

			for (String s : sesionesParaBorrar) {
				if (this.params.containsKey(s))
					this.params.remove(s);
			}
		}
	}
	
	/*public boolean keepingCompany() {
		return (this.pojoEmpresa != null);
	}
	
	public Empresa getCompany() {
		return this.pojoEmpresa;
	}*/
}

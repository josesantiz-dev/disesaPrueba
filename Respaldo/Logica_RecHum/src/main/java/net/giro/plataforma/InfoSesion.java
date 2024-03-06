package net.giro.plataforma;

import java.util.Calendar;
import java.util.Date;

import net.giro.plataforma.seguridad.beans.Acceso;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;

public class InfoSesion implements java.io.Serializable  {
	private static final long serialVersionUID = 1L;
	private Date		ultimaModificacion;
	private Acceso acceso = null;
	private UsuarioResponsabilidad  responsabilidad = null;
 
	public InfoSesion() {		
		ultimaModificacion = Calendar.getInstance().getTime();
	}
	
	public Date getUltimaModificacion() {
		return ultimaModificacion;
	}
	
	public void setUltimaModificacion(Date utimaModificacion) {
		this.ultimaModificacion = utimaModificacion;
	}

	public Acceso getAcceso() {
		return acceso;
	}

	public void setAcceso(Acceso acceso) {
		this.acceso = acceso;
	}

	public UsuarioResponsabilidad getResponsabilidad() {
		return responsabilidad;
	}

	public void setResponsabilidad(UsuarioResponsabilidad responsabilidad) {
		this.responsabilidad = responsabilidad;
	}
}

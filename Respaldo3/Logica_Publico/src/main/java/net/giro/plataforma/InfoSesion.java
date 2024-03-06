package net.giro.plataforma;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.ne.beans.Empresa;
import net.giro.plataforma.seguridad.beans.Acceso;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;

public class InfoSesion implements Serializable {
	private static final long serialVersionUID = 1L;
	private Acceso acceso = null;
	private UsuarioResponsabilidad responsabilidad = null;
	private Empresa empresa = null;
	private Date ultimaModificacion = null;
 
	public InfoSesion() {
		this.ultimaModificacion = Calendar.getInstance().getTime();
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Date getUltimaModificacion() {
		return ultimaModificacion;
	}

	public void setUltimaModificacion(Date ultimaModificacion) {
		this.ultimaModificacion = ultimaModificacion;
	}
	
	// EXTENDIDOS
	
	public Long getMonedaBaseId() {
		return (this.empresa != null && this.empresa.getId() != null && this.empresa.getId() > 0L && this.empresa.getMonedaId() > 0L) ? this.empresa.getMonedaId() : 0L;
	}
	
	public void setMonedaBaseId(Long value) {}
	
	public String getMonedaBase() {
		return (this.empresa != null && this.empresa.getId() != null && this.empresa.getId() > 0L && this.empresa.getMoneda() != null) ? this.empresa.getMoneda() : "";
	}
	
	public void setMonedaBase(String value) {}
}

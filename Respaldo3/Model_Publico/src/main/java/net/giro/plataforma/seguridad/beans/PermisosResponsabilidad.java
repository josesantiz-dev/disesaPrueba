package net.giro.plataforma.seguridad.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * permisos_responsabilidad
 * @author javaz
 */
public class PermisosResponsabilidad implements Serializable {
	private static final long serialVersionUID = 1L;
    private Long id;
    private Responsabilidad idResponsabilidad;
    private Aplicacion idAplicacion;
    private Funcion idFuncion;
    private int permiso;
    private long idEmpresa;
    private long creadoPor;
    private Date fechaCreacion;
    private long modificadoPor;
    private Date fechaModificacion;
    
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Responsabilidad getIdResponsabilidad() {
		return idResponsabilidad;
	}
	
	public void setIdResponsabilidad(Responsabilidad idResponsabilidad) {
		this.idResponsabilidad = idResponsabilidad;
	}
	
	public Aplicacion getIdAplicacion() {
		return idAplicacion;
	}
	
	public void setIdAplicacion(Aplicacion idAplicacion) {
		this.idAplicacion = idAplicacion;
	}
	
	public Funcion getIdFuncion() {
		return idFuncion;
	}
	
	public void setIdFuncion(Funcion idFuncion) {
		this.idFuncion = idFuncion;
	}
	
	public int getPermiso() {
		return permiso;
	}
	
	public void setPermiso(int permiso) {
		this.permiso = permiso;
	}
	
	public long getIdEmpresa() {
		return idEmpresa;
	}
	
	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	public long getCreadoPor() {
		return creadoPor;
	}
	
	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}
	
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	public long getModificadoPor() {
		return modificadoPor;
	}
	
	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
	
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-04-27 | Javier Tirado 	| Creacion de Bean
 */

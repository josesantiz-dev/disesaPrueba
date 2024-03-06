package net.giro.adp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.rh.admon.beans.EmpleadoExt;

public class ObraEmpleadoExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private ObraExt idObra;
	private String nombreObra;
	private EmpleadoExt idEmpleado;
	private String nombreEmpleado;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public ObraEmpleadoExt() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ObraExt getIdObra() {
		return idObra;
	}

	public void setIdObra(ObraExt idObra) {
		this.idObra = idObra;
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public EmpleadoExt getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(EmpleadoExt idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
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

	// ----------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------
	
	public Long getObraId() {
		if (this.idObra != null && this.idObra.getId() != null) 
			return this.idObra.getId();
		return 0L;
	}
	
	public void setObraId(Long value) {}
	
	public String getObraNombre() {
		if (this.idObra != null && this.idObra.getNombre() != null) 
			return this.idObra.getNombre();
		return "";
	}
	
	public void setObraNombre(String value) {}
	
	public Long getEmpleadoId() {
		if (this.idEmpleado != null && this.idEmpleado.getId() != null) 
			return this.idEmpleado.getId();
		return 0L;
	}
	
	public void setEmpleadoId(Long value) {}
	
	public String getEmpleadoNombre() {
		if (this.idEmpleado != null && this.idEmpleado.getNombre() != null && ! "".equals(this.idEmpleado.getNombre().trim())) 
			return this.idEmpleado.getNombre();
		return "";
	}
	
	public void setEmpleadoNombre(String value) { }

	public String getEmpleadoNombreApellidos() {
		if (this.idEmpleado != null && this.idEmpleado.getNombre() != null && ! "".equals(this.idEmpleado.getNombre().trim())) 
			return this.idEmpleado.getPrimerApellido() 
					+ " " + this.idEmpleado.getSegundoApellido() 
					+ " " + this.idEmpleado.getPrimerNombre() 
					+ " " + this.idEmpleado.getSegundoNombre();
		return "";
	}
	
	public void setEmpleadoNombreApellidos(String value) {}
	
	public String getApellidoPaterno() {
		if (this.idEmpleado != null && this.idEmpleado.getNombre() != null && ! "".equals(this.idEmpleado.getNombre().trim())) 
			return this.idEmpleado.getPrimerApellido();
		return "";
	}
	
	public void setApellidoPaterno(String value) {}
	
	public String getApellidoMaterno() {
		if (this.idEmpleado != null && this.idEmpleado.getNombre() != null && ! "".equals(this.idEmpleado.getNombre().trim())) 
			return this.idEmpleado.getSegundoApellido();
		return "";
	}
	
	public void setApellidoMaterno(String value) {}
	
	public String getNombres() {
		if (this.idEmpleado != null && this.idEmpleado.getNombre() != null && ! "".equals(this.idEmpleado.getNombre().trim())) 
			return (this.idEmpleado.getPrimerNombre() + " " + this.idEmpleado.getSegundoNombre()).trim();
		return "";
	}
	
	public void setNombres(String value) {}
	
	public String getApellidos() {
		if (this.idEmpleado != null && this.idEmpleado.getNombre() != null && ! "".equals(this.idEmpleado.getNombre().trim())) 
			return (this.idEmpleado.getPrimerApellido() + " " + this.idEmpleado.getSegundoApellido()).trim();
		return "";
	}
	
	public void setApellidos(String value) {}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
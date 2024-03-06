package net.giro.adp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.rh.admon.beans.EmpleadoExt;

public class ObraEmpleadoExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private ObraExt idObra;
	private EmpleadoExt idEmpleado;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public ObraEmpleadoExt() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public ObraEmpleadoExt(Long id) {
		this.id = id;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ObraEmpleadoExt(Long id, ObraExt idObra, EmpleadoExt idEmpleado, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.idObra = idObra;
		this.idEmpleado = idEmpleado;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
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

	public EmpleadoExt getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(EmpleadoExt idEmpleado) {
		this.idEmpleado = idEmpleado;
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
	
	public Long getObraId() {
		if (this.idObra != null && this.idObra.getId() != null) 
			return this.idObra.getId();
		return 0L;
	}
	
	public void setObraId(Long value) {
		
	}
	
	public String getObraNombre() {
		if (this.idObra != null && this.idObra.getNombre() != null) 
			return this.idObra.getNombre();
		return "";
	}
	
	public void setObraNombre(String value) {
		
	}
	
	public Long getEmpleadoId() {
		if (this.idEmpleado != null && this.idEmpleado.getId() != null) 
			return this.idEmpleado.getId();
		return 0L;
	}
	
	public void setEmpleadoId(Long value) {
		
	}
	
	public String getEmpleadoNombre() {
		if (this.idEmpleado != null && this.idEmpleado.getNombre() != null) 
			return this.idEmpleado.getNombre();
		return "";
	}
	
	public void setEmpleadoNombre(String value) {
		
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
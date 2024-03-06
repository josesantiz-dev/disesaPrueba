package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.plataforma.seguridad.beans.Aplicacion;

public class OperacionesExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id; // NUMERIC
	private String descripcion; // Varchar
	private Aplicacion idModulo; // NUMERIC
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	
	public OperacionesExt() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public OperacionesExt(Long id) {
		super();
		this.id = id;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public OperacionesExt(Long id, String descripcion, Aplicacion idModulo, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.idModulo = idModulo;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Aplicacion getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(Aplicacion idModulo) {
		this.idModulo = idModulo;
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
	
	
	public long getModuloId() {
		if (this.idModulo != null && this.idModulo.getId() > 0L)
			return this.idModulo.getId();
		return 0L;
	}
	
	public void setModuloId(long value) {}
	
	public String getModuloNombre() {
		if (this.idModulo != null && this.idModulo.getId() > 0L && this.idModulo.getAplicacion() != null)
			return this.idModulo.getAplicacion();
		return "";
	}
	
	public void setModuloNombre(String value) {}
	
	public OperacionesExt getCopia() {
		return this.copia();
	}
	
	public OperacionesExt copia() {
		try {
			OperacionesExt dest = new OperacionesExt();
			dest.setId(this.id);
			dest.setDescripcion(this.descripcion);
			dest.setIdModulo(this.idModulo);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			return dest;
		} catch (Exception e) {
			System.out.println("Error al intentar recuperar copia del POJO en Logica_Contabilidad.OperacionesExt.copia");
			throw e;
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
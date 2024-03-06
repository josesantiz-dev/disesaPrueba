package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class TransaccionesExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id; // NUMERIC
	private String descripcion; // VARCHAR
	private Long codigo; // NUMERIC
	private OperacionesExt idOperacion; // NUMERIC
	private String glosa;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public TransaccionesExt() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public TransaccionesExt(Long id) {
		super();
		this.id = id;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public TransaccionesExt(Long id, String descripcion, Long codigo, OperacionesExt idOperacion, String glosa,
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.codigo = codigo;
		this.idOperacion = idOperacion;
		this.glosa = glosa;
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

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public OperacionesExt getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(OperacionesExt idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getGlosa() {
		return glosa;
	}

	public void setGlosa(String glosa) {
		this.glosa = glosa;
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
	
	
	public long getOperacionId() {
		if (this.idOperacion != null && this.idOperacion.getId() != null && this.idOperacion.getId() > 0L)
			return this.idOperacion.getId();
		return 0L;
	}
	
	public void setOperacionId(long value) {}
	
	public String getOperacionNombre() {
		if (this.idOperacion != null && this.idOperacion.getId() != null && this.idOperacion.getId() > 0L && this.idOperacion.getDescripcion() != null)
			return this.idOperacion.getDescripcion();
		return "";
	}
	
	public void setOperacionNombre(String value) {}
	
	
	public TransaccionesExt getCopia() {
		return this.copia();
	}
	
	public TransaccionesExt copia() {
		try {
			TransaccionesExt dest = new TransaccionesExt();
			dest.setId(this.id);
			dest.setDescripcion(this.descripcion);
			dest.setCodigo(this.codigo);
			dest.setIdOperacion(this.idOperacion);
			dest.setGlosa(this.glosa);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			return dest;
		} catch (Exception e) {
			System.out.println("Error al intentar recuperar copia de Logica_Contabilidad.TransaccionesExt");
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
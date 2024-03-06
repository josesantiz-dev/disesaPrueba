package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ChecadorExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idObra; // tipo ObraExt. Dependencia ciclica. [Logica_RecHum depende de Logica_GestionProyectos y viceversa]
	private String nombreObra;
	private String nombreArchivo;
	private Date fecha;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int estatus;
	
	
	public ChecadorExt() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public ChecadorExt(Long id) {
		super();
		this.id = id;
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ChecadorExt(Long id, Long idObra, String nombreArchivo, Date fecha, long creadoPor, Date fechaCreacion,
			long modificadoPor, Date fechaModificacion, String nombreObra, int estatus) {
		super();
		this.id = id;
		this.idObra = idObra;
		this.nombreArchivo = nombreArchivo;
		this.fecha = fecha;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.nombreObra = nombreObra;
		this.estatus = estatus;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getIdObra() {
		return idObra;
	}

	public void setIdObra(Long idObra) {
		this.idObra = idObra;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	
	
	public ChecadorExt getCopia() {
		return this.copia();
	}
	
	public ChecadorExt copia() {
		try{
			ChecadorExt dest = new ChecadorExt();
			
			dest.setId(this.id);
			dest.setIdObra(this.idObra);
			dest.setNombreArchivo(this.nombreArchivo);
			dest.setFecha(this.fecha);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			dest.setNombreObra(this.nombreObra);
			dest.setEstatus(this.estatus);
			
			return dest;
		} catch (Exception e) {
			System.out.println("Error al generar copia de la instancia Logica_RecHum.ChecadorExt: " + e.getMessage());
			return new ChecadorExt();
		}
	}
}

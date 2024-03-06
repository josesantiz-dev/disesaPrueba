package net.giro.tyg.admon;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Moneda implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String nombre;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private String abreviacion;
	private String sinonimos;
	private Long idShf;

	
	public Moneda() {
		this.nombre = "";
		this.creadoPor = 0L;
		this.modificadoPor = 0L;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.abreviacion = "";
		this.sinonimos = "";
		this.idShf = 0L;
	}

	public Moneda(long id, String nombre, Long creadoPor, Date fechaCreacion,
			Long modificadoPor, Date fechaModificacion, String abreviacion,
			Long idShf) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.abreviacion = abreviacion;
		this.idShf = idShf;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getAbreviacion() {
		return abreviacion;
	}

	public void setAbreviacion(String abreviacion) {
		this.abreviacion = abreviacion;
	}

	public Long getIdShf() {
		return idShf;
	}

	public void setIdShf(Long idShf) {
		this.idShf = idShf;
	}

	public String getSinonimos() {
		return sinonimos;
	}

	public void setSinonimos(String sinonimos) {
		this.sinonimos = sinonimos;
	}

}

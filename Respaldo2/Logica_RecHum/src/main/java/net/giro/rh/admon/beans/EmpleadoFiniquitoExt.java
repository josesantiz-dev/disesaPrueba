package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoFiniquitoExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private EmpleadoExt idEmpleado;
	private double monto;
	private Date fechaSolicitudBaja;
	private long solicitadoPor;
	private Date fechaElaboracionEnvio;
	private int firmaRenuncia;
	private int voBoRh;
	private Long voBoRhPor;
	private Date voBoRhFecha;
	private int aprobacion;
	private Long aprobacionPor;
	private Date aprobacionFecha;
	private String observaciones;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public EmpleadoFiniquitoExt() {
		this.observaciones = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmpleadoExt getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(EmpleadoExt idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public Date getFechaSolicitudBaja() {
		return fechaSolicitudBaja;
	}

	public void setFechaSolicitudBaja(Date fechaSolicitudBaja) {
		this.fechaSolicitudBaja = fechaSolicitudBaja;
	}

	public long getSolicitadoPor() {
		return solicitadoPor;
	}

	public void setSolicitadoPor(long solicitadoPor) {
		this.solicitadoPor = solicitadoPor;
	}

	public Date getFechaElaboracionEnvio() {
		return fechaElaboracionEnvio;
	}

	public void setFechaElaboracionEnvio(Date fechaElaboracionEnvio) {
		this.fechaElaboracionEnvio = fechaElaboracionEnvio;
	}

	public int getFirmaRenuncia() {
		return firmaRenuncia;
	}

	public void setFirmaRenuncia(int firmaRenuncia) {
		this.firmaRenuncia = firmaRenuncia;
	}

	public int getVoBoRh() {
		return voBoRh;
	}

	public void setVoBoRh(int voBoRh) {
		this.voBoRh = voBoRh;
	}

	public Long getVoBoRhPor() {
		return voBoRhPor;
	}

	public void setVoBoRhPor(Long voBoRhPor) {
		this.voBoRhPor = voBoRhPor;
	}

	public Date getVoBoRhFecha() {
		return voBoRhFecha;
	}

	public void setVoBoRhFecha(Date voBoRhFecha) {
		this.voBoRhFecha = voBoRhFecha;
	}

	public int getAprobacion() {
		return aprobacion;
	}

	public void setAprobacion(int aprobacion) {
		this.aprobacion = aprobacion;
	}

	public Long getAprobacionPor() {
		return aprobacionPor;
	}

	public void setAprobacionPor(Long aprobacionPor) {
		this.aprobacionPor = aprobacionPor;
	}

	public Date getAprobacionFecha() {
		return aprobacionFecha;
	}

	public void setAprobacionFecha(Date aprobacionFecha) {
		this.aprobacionFecha = aprobacionFecha;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
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
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
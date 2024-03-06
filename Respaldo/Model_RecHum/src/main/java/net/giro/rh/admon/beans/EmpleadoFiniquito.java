package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

public class EmpleadoFiniquito implements Serializable { 
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private long idEmpleado;
	private Date fechaSolicitudBaja;
	private long solicitadoPor;
	private Date fechaElaboracionEnvio;
	private short firmaRenuncia;
	private short voBoRh;
	private short aprobacion;
	private String observaciones;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private short estatus;
	private double monto;
	
	
	public EmpleadoFiniquito() {}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {	//meotodo necesatio para tomar el correlativo
		this.id = id;
	}

	public long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(long idEmpleado) {
		this.idEmpleado = idEmpleado;
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

	public short getFirmaRenuncia() {
		return firmaRenuncia;
	}

	public void setFirmaRenuncia(short firmaRenuncia) {
		this.firmaRenuncia = firmaRenuncia;
	}

	public short getVoBoRh() {
		return voBoRh;
	}

	public void setVoBoRh(short voBoRh) {
		this.voBoRh = voBoRh;
	}

	public short getAprobacion() {
		return aprobacion;
	}

	public void setAprobacion(short aprobacion) {
		this.aprobacion = aprobacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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

	public short getEstatus() {
		return estatus;
	}

	public void setEstatus(short estatus) {
		this.estatus = estatus;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
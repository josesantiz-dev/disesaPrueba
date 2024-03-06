package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * empleado_finiquito
 * @author javaz
 *
 */
public class EmpleadoFiniquito implements Serializable { 
	private static final long serialVersionUID = 1L;
	private Long id;
	private Empleado idEmpleado;
	private long idContrato;
	private Date fechaIngreso;
	private double monto;
	private Date fechaSolicitudBaja;
	private long solicitadoPor;
	private Date fechaElaboracionEnvio;
	private int firmaRenuncia;
	private int voBoRh;
	private long voBoRhPor;
	private Date voBoRhFecha;
	private int aprobacion;
	private long aprobacionPor;
	private Date aprobacionFecha;
	private String observaciones;
	private int estatus; // 0:en proceso, 1:cancelado, 2:aprobado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public EmpleadoFiniquito() {
		this.id = 0L;
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

	public void setId(long id) {
		this.id = id;
	}

	public Empleado getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Empleado idEmpleado) {
		this.idEmpleado = idEmpleado;
		if (idEmpleado != null)
			this.fechaIngreso = idEmpleado.getFechaIngreso();
	}

	public long getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(long idContrato) {
		this.idContrato = idContrato;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
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

	public Date getVoBoRhFecha() {
		return voBoRhFecha;
	}

	public void setVoBoRhFecha(Date voBoRhFecha) {
		this.voBoRhFecha = voBoRhFecha;
	}

	public long getVoBoRhPor() {
		return voBoRhPor;
	}

	public void setVoBoRhPor(long voBoRhPor) {
		this.voBoRhPor = voBoRhPor;
	}

	public int getAprobacion() {
		return aprobacion;
	}

	public void setAprobacion(int aprobacion) {
		this.aprobacion = aprobacion;
	}

	public Date getAprobacionFecha() {
		return aprobacionFecha;
	}

	public void setAprobacionFecha(Date aprobacionFecha) {
		this.aprobacionFecha = aprobacionFecha;
	}

	public long getAprobacionPor() {
		return aprobacionPor;
	}

	public void setAprobacionPor(long aprobacionPor) {
		this.aprobacionPor = aprobacionPor;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * 0:en proceso, 1:cancelado, 2:aprobado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * 0:en proceso, 1:cancelado, 2:aprobado
	 * @param estatus
	 */
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

	// ------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------
	
	/**
	 * Recupera el estatus del finiquito (lo reasigna si es necesario)
	 * @return
	 */
	public int getEstatusId() {
		validarEstatus();
		/*if (this.voBoRh == 1 && this.voBoRhPor > 0L && this.estatus != 0) 
			this.estatus = 0;
		if (this.aprobacion == 1 && this.aprobacionPor > 0L && this.estatus != 2)
			this.estatus = 2;*/
		return this.estatus;
	}
	
	public void setEstatusId(int value) {}
	
	/**
	 * Descripcion del estatus del Finiquito
	 * @return
	 */
	public String getEstatusDescripcion() {
		validarEstatus();
		switch (this.estatus) {
			case 0 : return "EN PROCESO"; 
			case 1 : return "CANCELADO";
			case 2 : return "APROBADO";
		}
		/*if (this.aprobacion == 1 && this.aprobacionPor > 0L) {
			if (this.estatus != 2)
				this.estatus = 2;
			return "APROBADO";
		}
		
		if (this.voBoRh == 1 && this.voBoRhPor > 0L) {
			if (this.estatus != 0)
				this.estatus = 0;
			return "VISTO BUENO";
		}
		
		this.estatus = 0;
		return "EN PROCESO";*/
		return "DESCONOCIDO";
	}
	
	public void setEstatusDescripcion(String value) {}
	
	public void validarEstatus() {
		if (this.estatus == 1)
			return;
		//if (this.voBoRh == 1 && this.voBoRhPor > 0L && this.estatus == 0) 
		//	this.estatus = 0;
		if (this.aprobacion == 1 && this.aprobacionPor > 0L && this.estatus == 0)
			this.estatus = 2;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
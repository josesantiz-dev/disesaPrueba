package net.giro.tyg.admon;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * formas_pagos
 * @author javaz
 *
 */
public class FormasPagos implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String formaPago;
	private String aplicarAutomaticamente;
	private String claveSat;
	private Long ahorro;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	
	public FormasPagos() {
		this.formaPago = "";
		this.aplicarAutomaticamente = "";
		this.claveSat = "";
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

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getAplicarAutomaticamente() {
		return aplicarAutomaticamente;
	}

	public void setAplicarAutomaticamente(String aplicarAutomaticamente) {
		this.aplicarAutomaticamente = aplicarAutomaticamente;
	}
	
	public String getClaveSat() {
		return claveSat;
	}

	public void setClaveSat(String claveSat) {
		this.claveSat = claveSat;
	}

	public Long getAhorro() {
		return ahorro;
	}

	public void setAhorro(Long ahorro) {
		this.ahorro = ahorro;
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


	public int getEstatus() {
		return estatus;
	}


	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
}

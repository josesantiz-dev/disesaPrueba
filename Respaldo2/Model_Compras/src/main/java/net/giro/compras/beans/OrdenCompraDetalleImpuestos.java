package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class OrdenCompraDetalleImpuestos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idOrdenDetalle;
	private Long idImpuesto;
	private double porcentaje;
	private double monto;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public OrdenCompraDetalleImpuestos() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public OrdenCompraDetalleImpuestos(Long id) {
		this();
		this.id = id;
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
	
	public Long getIdOrdenDetalle() {
		return idOrdenDetalle;
	}
	
	public void setIdOrdenDetalle(Long idOrdenDetalle) {
		this.idOrdenDetalle = idOrdenDetalle;
	}
	
	public Long getIdImpuesto() {
		return idImpuesto;
	}
	
	public void setIdImpuesto(Long idImpuesto) {
		this.idImpuesto = idImpuesto;
	}
	
	public double getPorcentaje() {
		return porcentaje;
	}
	
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	public double getMonto() {
		return monto;
	}
	
	public void setMonto(double monto) {
		this.monto = monto;
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

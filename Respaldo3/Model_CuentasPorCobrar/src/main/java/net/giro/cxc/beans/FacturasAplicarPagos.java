package net.giro.cxc.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class FacturasAplicarPagos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idFacturaOriginal;
	private long idFactura;
	private int aplicado;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public FacturasAplicarPagos() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public FacturasAplicarPagos(long idFacturaOriginal, long idFactura, long creadoPor, long modificadoPor) {
		this();
		this.idFacturaOriginal = idFacturaOriginal;
		this.idFactura = idFactura;
		this.creadoPor = creadoPor;
		this.modificadoPor = modificadoPor;
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
	
	public long getIdFacturaOriginal() {
		return idFacturaOriginal;
	}
	
	public void setIdFacturaOriginal(long idFacturaOriginal) {
		this.idFacturaOriginal = idFacturaOriginal;
	}
	
	public long getIdFactura() {
		return idFactura;
	}
	
	public void setIdFactura(long idFactura) {
		this.idFactura = idFactura;
	}
	
	public int getAplicado() {
		return aplicado;
	}
	
	public void setAplicado(int aplicado) {
		this.aplicado = aplicado;
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

package net.giro.cxc.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * factura
 * 
 * @author javaz
 *
 */
public class FacturasRelacionadas implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private long creadoPor;
	private Date fechaCreacion;
	private long idFacturaRelacionada;
	private long idFactura;
	private String cfdiRelacionadoUuid;

	public FacturasRelacionadas() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getIdFacturaRelacionada() {
		return idFacturaRelacionada;
	}

	public void setIdFacturaRelacionada(long idFacturaRelacionada) {
		this.idFacturaRelacionada = idFacturaRelacionada;
	}

	public long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(long idFactura) {
		this.idFactura = idFactura;
	}

	public String getCfdiRelacionadoUuid() {
		return cfdiRelacionadoUuid;
	}

	public void setCfdiRelacionadoUuid(String cfdiRelacionadoUuid) {
		this.cfdiRelacionadoUuid = cfdiRelacionadoUuid;
	}
}
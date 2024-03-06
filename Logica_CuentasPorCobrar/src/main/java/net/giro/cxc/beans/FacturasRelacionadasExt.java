package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.adp.beans.ObraExt;
import net.giro.cxc.util.FACTURA_ESTATUS;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Sucursal;
import net.giro.plataforma.beans.ConValores;

public class FacturasRelacionadasExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long creadoPor;
	private Date fechaCreacion;
	private Factura idFacturaRelacionada;
	private FacturaExt idFactura;
	
	public FacturasRelacionadasExt() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Factura getIdFacturaRelacionada() {
		return idFacturaRelacionada;
	}

	public void setIdFacturaRelacionada(Factura idFacturaRelacionada) {
		this.idFacturaRelacionada = idFacturaRelacionada;
	}

	public FacturaExt getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(FacturaExt idFactura) {
		this.idFactura = idFactura;
	}
}

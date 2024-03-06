package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * factura_detalle_impuesto
 * @author javaz
 *
 */
public class FacturaDetalleImpuesto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idFacturaDetalle;
	private Long idImpuesto;
	private BigDecimal monto;
	private Date fechaCreacion;
	private long creadoPor;
	private Date fechaModificacion;
	private long modificadoPor;
	
	public FacturaDetalleImpuesto() {
		this.monto = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public FacturaDetalleImpuesto(Long id) {
		this();
		this.id = id;
	}

	public FacturaDetalleImpuesto(Long id, Long idFacturaDetalle, Long idImpuesto, BigDecimal monto, 
			Date fechaCreacion, long creadoPor, Date fechaModificacion, long modificadoPor) {
		this.id = id;
		this.idFacturaDetalle = idFacturaDetalle;
		this.idImpuesto = idImpuesto;
		this.monto = monto;
		this.fechaCreacion = fechaCreacion;
		this.creadoPor = creadoPor;
		this.fechaModificacion = fechaModificacion;
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

	public Long getIdFacturaDetalle() {
		return idFacturaDetalle;
	}

	public void setIdFacturaDetalle(Long idFacturaDetalle) {
		this.idFacturaDetalle = idFacturaDetalle;
	}

	public Long getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(Long idImpuesto) {
		this.idImpuesto = idImpuesto;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}	
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
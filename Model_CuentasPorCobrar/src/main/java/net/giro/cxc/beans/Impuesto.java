package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * impuesto
 * @author javaz
 *
 */
public class Impuesto implements Serializable {	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String descripcion;
	private short tipo;
	private BigDecimal porcentaje;
	private String cuentaContable;
	private Date fechaCreacion;
	private long creadoPor;
	private Date fechaModificacion;
	private long modificadoPor;
	
	public Impuesto() {
		this.id = 0L;
		this.descripcion = "";
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Short getTipo() {
		return tipo;
	}

	public void setTipo(Short tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
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
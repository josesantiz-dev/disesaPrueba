package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoNomina implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Empleado idEmpleado;
	private Date fecha;
	private BigDecimal montoNormal;
	private BigDecimal montoExtra;
	private BigDecimal montoRetenciones;
	private BigDecimal montoNeto;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	
	public EmpleadoNomina() {
		this.fecha = Calendar.getInstance().getTime();
		this.montoNormal = BigDecimal.ZERO;
		this.montoExtra = BigDecimal.ZERO;
		this.montoRetenciones = BigDecimal.ZERO;
		this.montoNeto = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public EmpleadoNomina(Long id) {
		super();
		this.id = id;
		this.fecha = Calendar.getInstance().getTime();
		this.montoNormal = BigDecimal.ZERO;
		this.montoExtra = BigDecimal.ZERO;
		this.montoRetenciones = BigDecimal.ZERO;
		this.montoNeto = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public EmpleadoNomina(Long id, Empleado idEmpleado, Date fecha, BigDecimal montoNormal, BigDecimal montoExtra,
			BigDecimal montoRetenciones, BigDecimal montoNeto, long creadoPor, Date fechaCreacion, long modificadoPor,
			Date fechaModificacion) {
		super();
		this.id = id;
		this.idEmpleado = idEmpleado;
		this.fecha = fecha;
		this.montoNormal = montoNormal;
		this.montoExtra = montoExtra;
		this.montoRetenciones = montoRetenciones;
		this.montoNeto = montoNeto;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
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
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public BigDecimal getMontoNormal() {
		return montoNormal;
	}
	
	public void setMontoNormal(BigDecimal montoNormal) {
		this.montoNormal = montoNormal;
	}
	
	public BigDecimal getMontoExtra() {
		return montoExtra;
	}
	
	public void setMontoExtra(BigDecimal montoExtra) {
		this.montoExtra = montoExtra;
	}
	
	public BigDecimal getMontoRetenciones() {
		return montoRetenciones;
	}
	
	public void setMontoRetenciones(BigDecimal montoRetenciones) {
		this.montoRetenciones = montoRetenciones;
	}
	
	public BigDecimal getMontoNeto() {
		return montoNeto;
	}
	
	public void setMontoNeto(BigDecimal montoNeto) {
		this.montoNeto = montoNeto;
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
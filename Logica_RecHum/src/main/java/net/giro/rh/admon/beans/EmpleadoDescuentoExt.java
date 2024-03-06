package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;

public class EmpleadoDescuentoExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Empleado idEmpleado;
	private ConValores idDescuento;
	private int numeroPagos;
	private ConValores idPeriodo;
	private Date fecha;
	private BigDecimal monto;
	private String observaciones;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int estatus;
	//private long idRegistro;

	
	public EmpleadoDescuentoExt() {
		this.monto = BigDecimal.ZERO;
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public EmpleadoDescuentoExt(Long id) {
		super();
		this.id = id;
		this.monto = BigDecimal.ZERO;
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public EmpleadoDescuentoExt(Long id, Empleado idEmpleado, ConValores idDescuento, 
			Date fecha, BigDecimal monto, int numeroPagos, ConValores idPeriodo, String observaciones, int estatus, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.idEmpleado = idEmpleado;
		this.idDescuento = idDescuento;
		this.numeroPagos = numeroPagos;
		this.idPeriodo = idPeriodo;
		this.fecha = fecha;
		this.monto = monto;
		this.observaciones = observaciones;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.estatus = estatus;
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

	public ConValores getIdDescuento() {
		return idDescuento;
	}

	public void setIdDescuento(ConValores idDescuento) {
		this.idDescuento = idDescuento;
	}

	public int getNumeroPagos() {
		return numeroPagos;
	}

	public void setNumeroPagos(int numeroPagos) {
		this.numeroPagos = numeroPagos;
	}

	public ConValores getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(ConValores idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
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

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	/*public long getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(long idRegistro) {
		this.idRegistro = idRegistro;
	}*/
	
	
	public String getEmpleado() {
		if (this.idEmpleado != null && this.idEmpleado.getId() != null && this.idEmpleado.getId() > 0L && this.idEmpleado.getNombre() != null)
			return this.idEmpleado.getId().toString() + " - " + this.idEmpleado.getNombre();
		return "";
	}
	
	public void setEmpleado(String value) {}
	
	public long getEmpleadoId() {
		if (this.idEmpleado != null && this.idEmpleado.getId() != null && this.idEmpleado.getId() > 0L)
			return this.idEmpleado.getId();
		return 0L;
	}
	
	public void setEmpleadoId(long value) {}
	
	public String getEmpleadoNombre() {
		if (this.idEmpleado != null && this.idEmpleado.getId() != null && this.idEmpleado.getId() > 0L && this.idEmpleado.getNombre() != null)
			return this.idEmpleado.getNombre();
		return "";
	}
	
	public void setEmpleadoNombre(String value) {}

	public String getDescuento() {
		if (this.idDescuento != null && this.idDescuento.getId() > 0L && this.idDescuento.getValor() != null)
			return this.idDescuento.getId() + " - " + this.idDescuento.getValor();
		return "";
	}

	public void setDescuento(String value) {}
	
	public long getDescuentoId() {
		if (this.idDescuento != null && this.idDescuento.getId() > 0L)
			return this.idDescuento.getId();
		return 0L;
	}
	
	public void setDescuentoId(long value) {}

	public String getPeriodo() {
		if (this.idPeriodo != null && this.idPeriodo.getId() > 0L && this.idPeriodo.getValor() != null)
			return this.idPeriodo.getId() + " - " + this.idPeriodo.getValor();
		return "";
	}

	public void setPeriodo(String value) {}
	
	public long getPeriodoId() {
		if (this.idPeriodo != null && this.idPeriodo.getId() > 0L)
			return this.idPeriodo.getId();
		return 0L;
	}
	
	public void setPeriodoId(long value) {}

	
	public EmpleadoDescuentoExt getCopia() {
		return this.copia();
	}
	
	public EmpleadoDescuentoExt copia() {
		try {
			EmpleadoDescuentoExt dest = new EmpleadoDescuentoExt();
			
			dest.setId(this.id);
			dest.setIdEmpleado(this.idEmpleado);
			dest.setIdDescuento(this.idDescuento);
			dest.setNumeroPagos(this.numeroPagos);
			dest.setIdPeriodo(this.idPeriodo);
			dest.setFecha(this.fecha);
			dest.setMonto(this.monto);
			dest.setObservaciones(this.observaciones);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			dest.setEstatus(this.estatus);
			//dest.setIdRegistro(this.idRegistro);
			
			return dest;
		} catch (Exception e) {
			System.out.println("Error al generar una copia de Logica_RecHum.EmpleadoRetencionExt");
			throw e;
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1	| 25/05/2016 | Javier Tirado	| Creacion de EmpleadoDescuentoExt
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
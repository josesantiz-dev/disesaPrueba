package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * almacen_producto
 * @author javaz
 */
public class AlmacenProducto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Almacen idAlmacen;
	private long idProducto;
	private double precioUnitario;
	private double existencia;
	private double solicitud;
	private long idEmpresa;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public AlmacenProducto() {
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
	
	public Almacen getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(Almacen idAlmacen) {
		this.idAlmacen = idAlmacen;
	}

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	
	public double getExistencia() {
		return existencia;
	}

	public void setExistencia(double existencia) {
		this.existencia = existencia;
	}

	public double getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(double solicitud) {
		this.solicitud = solicitud;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public int getEstatus() {
		return estatus;
	}

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

	// -------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------

	public double getTotal() {
		return existencia + solicitud;
	}

	public void setTotal(double value) {}

	/**
	 * Añadir cantidad a existencia
	 * @param existencia
	 */
	public void addExistencia(double existencia) {
		this.existencia += existencia;
		if (this.existencia <= 0)
			this.existencia = 0;
	}

	/**
	 * Añadir cantidad a solictud
	 * @param solicitud
	 */
	public void addSolicitud(double solicitud) {
		this.solicitud += solicitud;
		if (this.solicitud <= 0)
			this.solicitud = 0;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */
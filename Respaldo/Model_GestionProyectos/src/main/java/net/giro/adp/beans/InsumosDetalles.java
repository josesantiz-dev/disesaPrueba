package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class InsumosDetalles implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idInsumo;
	private Long idProducto;
	private String nombreProducto;
	private	double cantidad;
	private double precioUnitario;
	private BigDecimal monto;
	private double porcentaje;
	private int tipo;
	private double cantidadSurtida;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public InsumosDetalles() {
		this.monto = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.nombreProducto = "";
	}
	
	public InsumosDetalles(Long id) {
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
	
	public Long getIdInsumo() {
		return idInsumo;
	}
	
	public void setIdInsumo(Long idInsumo) {
		this.idInsumo = idInsumo;
	}
	
	public Long getIdProducto() {
		return idProducto;
	}
	
	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
	
	public String getNombreProducto() {
		return nombreProducto;
	}
	
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}
	
	public double getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	
	public BigDecimal getMonto() {
		return monto;
	}
	
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	
	public double getPorcentaje() {
		return porcentaje;
	}
	
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	/**
	 * TIPO DE INSUMO: 1 - MATERIALES, 2 - MANO DE OBRA, 3 - HERRAMIENTA
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}
	
	/**
	 * TIPO DE INSUMO: 1 - MATERIALES, 2 - MANO DE OBRA, 3 - HERRAMIENTA
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public double getCantidadSurtida() {
		return cantidadSurtida;
	}
	
	public void setCantidadSurtida(double cantidadSurtida) {
		this.cantidadSurtida = cantidadSurtida;
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
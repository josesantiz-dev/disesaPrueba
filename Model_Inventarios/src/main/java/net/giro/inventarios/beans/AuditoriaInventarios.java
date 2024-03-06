package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * auditoria_inventarios
 * @author javaz
 */
public class AuditoriaInventarios implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date fecha;
	private String descripcion;
	// --------------------------------------------------------
	private long idProducto;
	private String codigo;
	private long cantidad;
	// --------------------------------------------------------
	private long idAlmacen;
	private String almacen;
	// --------------------------------------------------------
	private long idMovimiento;
	private int tipoMovimiento; // 0:Entrada, 1:Salida
	private String tipoReferencia; // OC, TR, SO, DX, DE, TX
	// --------------------------------------------------------
	private long creadoPor;
	private long idEmpresa;
	
	public AuditoriaInventarios() {
		this.fecha = Calendar.getInstance().getTime();
		this.descripcion = "";
		this.codigo = "";
		this.almacen = "";
		this.tipoReferencia = "";
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		fecha = (fecha != null ? fecha : Calendar.getInstance().getTime());
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		descripcion = (descripcion != null ? descripcion : "");
		this.descripcion = descripcion;
	}

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		codigo = (codigo != null ? codigo : "");
		this.codigo = codigo;
	}

	public long getCantidad() {
		return cantidad;
	}

	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}

	public long getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(long idAlmacen) {
		this.idAlmacen = idAlmacen;
	}

	public String getAlmacen() {
		return almacen;
	}

	public void setAlmacen(String almacen) {
		almacen = (almacen != null ? almacen : "");
		this.almacen = almacen;
	}

	public long getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(long idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public int getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(int tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public String getTipoReferencia() {
		return tipoReferencia;
	}

	public void setTipoReferencia(String tipoReferencia) {
		tipoReferencia = (tipoReferencia != null ? tipoReferencia : "");
		this.tipoReferencia = tipoReferencia;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2019-11-29 | Javier Tirado 	| Creacion de Bean
 */

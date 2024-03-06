package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * bitacora_inventarios
 * @author javaz
 *
 */
public class BitacoraInventarios implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Date fecha;
	private Long idAcceso;
	private Long idMovimiento;
	private int tipoMovimiento; // 0:Entrada, 1:Salida
	private String tipoReferencia; // OC, TR, SO, DX, DE, TX
	private Long idReferencia;
	private String observaciones;
	private Long idUsuario;
	private Long idEmpleado;
	
	public BitacoraInventarios() {
		this.tipoReferencia = "";
		this.observaciones = "";
		this.fecha = Calendar.getInstance().getTime();
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
		this.fecha = fecha;
	}

	public Long getIdAcceso() {
		return idAcceso;
	}

	public void setIdAcceso(Long idAcceso) {
		this.idAcceso = idAcceso;
	}

	public Long getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(Long idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	/**
	 * 0:Entrada, 1:Salida
	 * @return
	 */
	public int getTipoMovimiento() {
		return tipoMovimiento;
	}

	/**
	 * 0:Entrada, 1:Salida
	 * @param tipoMovimiento
	 */
	public void setTipoMovimiento(int tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	/**
	 * OC:Orden Compra, TR:Traspaso a Bodega, SO:Salida a Obra, DX:Devolucion a Bodega, DE:Devolucion a Almacen, TX:Traspaso a Almacen
	 * @return
	 */
	public String getTipoReferencia() {
		return tipoReferencia;
	}

	/**
	 * OC:Orden Compra, TR:Traspaso a Bodega, SO:Salida a Obra, DX:Devolucion a Bodega, DE:Devolucion a Almacen, TX:Traspaso a Almacen
	 * @param tipoReferencia
	 */
	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}

	public Long getIdReferencia() {
		return idReferencia;
	}

	public void setIdReferencia(Long idReferencia) {
		this.idReferencia = idReferencia;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
}

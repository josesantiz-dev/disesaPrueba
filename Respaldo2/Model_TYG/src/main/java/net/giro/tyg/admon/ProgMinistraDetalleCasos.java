package net.giro.tyg.admon;

/**
 * prog_ministra_detalle_casos
 * @author javaz
 *
 */
public class ProgMinistraDetalleCasos implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	java.lang.Long id;
	net.giro.tyg.admon.ProgMinistra progMinistraId;
	java.math.BigDecimal monto;
	java.util.Date fechaCreacion;
	java.lang.Long creadoPor;
	java.util.Date fechaModificacion;
	java.lang.Long modificadoPor;
	java.lang.Long idSolicitud;
	java.lang.String tipo;
	java.lang.String estatus;
	java.lang.String idSolicitante;
	java.lang.Long canceladoPor;
	java.lang.String tipoTran;


	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public net.giro.tyg.admon.ProgMinistra getProgMinistraId() {
		return progMinistraId;
	}

	public void setProgMinistraId(net.giro.tyg.admon.ProgMinistra progMinistraId) {
		this.progMinistraId = progMinistraId;
	}

	public java.math.BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(java.math.BigDecimal monto) {
		this.monto = monto;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(java.lang.Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.lang.Long getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(java.lang.Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public java.lang.String getTipo() {
		return tipo;
	}

	public void setTipo(java.lang.String tipo) {
		this.tipo = tipo;
	}

	public java.lang.String getEstatus() {
		return estatus;
	}

	public void setEstatus(java.lang.String estatus) {
		this.estatus = estatus;
	}

	public java.lang.String getIdSolicitante() {
		return idSolicitante;
	}

	public void setIdSolicitante(java.lang.String idSolicitante) {
		this.idSolicitante = idSolicitante;
	}

	public java.lang.Long getCanceladoPor() {
		return canceladoPor;
	}

	public void setCanceladoPor(java.lang.Long canceladoPor) {
		this.canceladoPor = canceladoPor;
	}

	public java.lang.String getTipoTran() {
		return tipoTran;
	}

	public void setTipoTran(java.lang.String tipoTran) {
		this.tipoTran = tipoTran;
	}

}
